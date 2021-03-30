/**
 * 
 */
package soars.application.animator.file.loader.legacy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import soars.application.animator.file.common.ImageManipulator;
import soars.application.animator.file.importer.Importer;
import soars.application.animator.object.file.FileObject;
import soars.application.animator.object.file.HeaderObject;
import soars.application.animator.object.player.agent.AgentObject;
import soars.application.animator.object.player.agent.AgentObjectManager;
import soars.application.animator.object.player.base.ObjectBase;
import soars.application.animator.object.player.base.edit.objects.ObjectComparator;
import soars.application.animator.object.player.spot.SpotObject;
import soars.application.animator.object.player.spot.SpotObjectManager;
import soars.application.animator.object.property.agent.AgentProperty;
import soars.application.animator.object.property.agent.AgentPropertyManager;
import soars.application.animator.object.property.base.PropertyBase;
import soars.application.animator.object.property.spot.SpotProperty;
import soars.application.animator.object.property.spot.SpotPropertyManager;
import soars.application.animator.setting.common.CommonProperty;
import soars.common.utility.swing.image.ImagePropertyManager;
import soars.common.utility.tool.file.FileUtility;

/**
 * The XML SAX loader for legacy Animator data.
 * @author kurata / SOARS project
 */
public class LegacySaxLoader extends DefaultHandler {

	/**
	 * Graphics object of JAVA.
	 */
	private Graphics2D _graphics2D = null;

	/**
	 * Parent directory which contains XML file of legacy Animator data.
	 */
	private File _file_directory = null; 

	/**
	 * Root directory for Animator data file.
	 */
	private File _root_directory = null;

	/**
	 * True while reading the data of the properties for SOARS.
	 */
	private boolean _property = false;

	/**
	 * True while reading the data of the steps for SOARS.
	 */
	private boolean _time = false;

	/**
	 * String in which XML string is stored temporarily.
	 */
	private String _value = null;

	/**
	 * String in which the name of the property for SOARS is stored temporarily.
	 */
	private String _name = null;

	/**
	 * SpotProperty hashtable(String[name] - SpotProperty)
	 */
	private TreeMap _spot_property_map = null;

	/**
	 * Array of the names of visible spot properties.
	 */
	private Vector _selected_spot_properties = null;

	/**
	 * AgentProperty hashtable(String[name] - AgentProperty)
	 */
	private TreeMap _agent_property_map = null;

	/**
	 * Array of the names of visible agent properties.
	 */
	private Vector _selected_agent_properties = null;

	/**
	 * Array of the steps for SOARS.
	 */
	private List _steps = new ArrayList();

	/**
	 * Temporary SpotObject.
	 */
	private SpotObject _spotObject = null;

	/**
	 * Temporary AgentObject.
	 */
	private AgentObject _agentObject = null;

	/**
	 * Agent log hashtable(String[name] - LogObject)
	 */
	private Map _agent_transition_map = new HashMap();

	/**
	 * Spot log hashtable(String[name] - LogObject)
	 */
	private Map _spot_transition_map = new HashMap();

	/**
	 * Temporary array of the SOARS log.
	 */
	private LogObject _logObject = null;

	/**
	 * Informations of SOARS log files.
	 */
	public HeaderObject _headerObject = new HeaderObject();

	/**
	 * ImageProperty hashtable(String[filename] - ImageProperty)
	 */
	private TreeMap _image_property_map = new TreeMap();

	/**
	 * False if error occurred.
	 */
	static private boolean _result;

	/**
	 * Returns true if loading the specified file is completed successfully.
	 * @param file the specified XML file of legacy Animator data
	 * @param root_directory the root directory for Animator data
	 * @param graphics2D the graphics object of JAVA
	 * @return true if loading the specified file is completed successfully
	 */
	public static boolean execute(File file, File root_directory, Graphics2D graphics2D) {
		_result = false;
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			LegacySaxLoader legacySaxLoader = new LegacySaxLoader( file.getParentFile(), root_directory, graphics2D);
			saxParser.parse( file, legacySaxLoader);
			legacySaxLoader.at_end_of_load();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return _result;
	}

	/**
	 * Invoked at the end of the loading.
	 */
	private void at_end_of_load() {
		if ( !_result)
			return;

		//debug();

		if ( !is_correct()) {
			_result = false;
			return;
		}

		if ( !regenerate()) {
			_result = false;
			return;
		}

		String[] steps = ( _steps.isEmpty() ? ( new String[] { ""}) : ( String[])_steps.toArray( new String[ 0]));

		_headerObject.set_last_time( steps[ steps.length - 1]);
//		_headerObject._last = steps[ steps.length - 1];

		Importer importer = new Importer( _headerObject, _root_directory, steps);
		if ( !importer.execute()) {
			_result = false;
			return;
		}

		ImagePropertyManager.get_instance().putAll( _image_property_map);
	}

	/**
	 * Returns true if all log data are correct.
	 * @return true if all log data are correct
	 */
	private boolean is_correct() {
		IntBuffer counter = IntBuffer.allocate( 1);
		counter.put( 0, -1);

		if ( !is_correct( _agent_transition_map, counter))
			return false;

		if ( !is_correct( _spot_transition_map, counter))
			return false;

		if ( _steps.isEmpty())
			setup_steps( counter.get( 0));
		else {
			if ( _steps.size() != counter.get( 0))
				return false;
		}

		return true;
	}

	/**
	 * Returns true if all log data are correct.
	 * @param transition_map the log hashtable(String[name] - LogObject)
	 * @param counter the number of log
	 * @return true if all log data are correct
	 */
	private boolean is_correct(Map transition_map, IntBuffer counter) {
		Iterator iterator = transition_map.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			Map map = ( Map)entry.getValue();
			if ( !is_correct( name, map, counter))
				return false;
		}
		return true;
	}

	/**
	 * Returns true if all log data and steps data are correct.
	 * @param name the name of object
	 * @param map the log hashtable(String[name] - LogObject)
	 * @param counter the number of log
	 * @return true if all log data and steps data are correct
	 */
	private boolean is_correct(String name, Map map, IntBuffer counter) {
		Iterator iterator = map.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			LogObject logObject = ( LogObject)entry.getValue();
			if ( logObject.isEmpty() /*|| 0 > logObject._start*/)
				return false;

			if ( 0 > counter.get( 0))
				counter.put( 0, logObject.size());
			else {
				if ( logObject.size() != counter.get( 0))
					return false;
			}
		}
		return true;
	}

	/**
	 * Creates the steps data.
	 * @param size the length of steps
	 */
	private void setup_steps(int size) {
		for ( int i = 0; i < size; ++i)
			_steps.add( ( 0 == i) ? "" : ( String.valueOf( i - 1) + "/00:00"));
	}

	/**
	 * Returns true if all log files are regenerated successfully.
	 * @return true if all log files are regenerated successfully
	 */
	private boolean regenerate() {
		if ( !regenerate( "agents", _agent_transition_map))
			return false;

		if ( !regenerate( "spots", _spot_transition_map))
			return false;

		return true;
	}

	/**
	 * Returns true if the specified log files are regenerated successfully.
	 * @param type the type of log("agents" or "spots")
	 * @param transition_map the log hashtable(String[name] - LogObject)
	 * @return true if the specified log files are regenerated successfully
	 */
	private boolean regenerate(String type, Map transition_map) {
		Iterator iterator = transition_map.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			Map map = ( Map)entry.getValue();
			if ( !regenerate( type, name, map))
				return false;
		}
		return true;
	}

	/**
	 * Returns true if the specified log file are regenerated successfully.
	 * @param type the type of log("agents" or "spots")
	 * @param name the name of object
	 * @param map the log hashtable(String[name] - LogObject)
	 * @return true if the specified log file are regenerated successfully
	 */
	private boolean regenerate(String type, String name, Map map) {
		// regenerates log file from type and name
		ObjectBase[] objectBases = ( ObjectBase[])map.keySet().toArray( new ObjectBase[ 0]);
		Arrays.sort( objectBases, new ObjectComparator( true, false));
		Arrays.sort( objectBases, new LogObjectComparator( map));

		File directory = new File( _root_directory, type);
		if ( !directory.exists() && !directory.mkdirs())
			return false;

		FileObject fileObject = new FileObject( type, name);
		if ( !fileObject.generate( directory, map, objectBases, _steps))
			return false;

		if ( type.equals( "agents") && name.equals( "$Spot"))
			_headerObject._max = fileObject._max;

		_headerObject._fileObjectMap.put( type + "/" + fileObject._file.getName(), fileObject);

		return true;
	}

	/**
	 * Creates the XML SAX loader for legacy Animator data.
	 * @param file_directory the parent directory which contains XML file of legacy Animator data
	 * @param root_directory the root directory for Animator data 
	 * @param graphics2D the graphics object of JAVA
	 */
	public LegacySaxLoader(File file_directory, File root_directory, Graphics2D graphics2D) {
		super();
		_file_directory  = file_directory;
		_root_directory = root_directory;
		_graphics2D = graphics2D;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);

		if ( name.equals( "animation_data")) {
			_result = true;
			return;
		}

		if ( !_result)
			return;

		if ( name.equals( "property")) {
			on_property( attributes);
		} else if ( name.equals( "agent_width")) {
			on_agent_width( attributes);
		} else if ( name.equals( "agent_height")) {
			on_agent_height( attributes);
		} else if ( name.equals( "spot_width")) {
			on_spot_width( attributes);
		} else if ( name.equals( "spot_height")) {
			on_spot_height( attributes);
		} else if ( name.equals( "minimum_width")) {
			on_minimum_width( attributes);
		} else if ( name.equals( "velocity")) {
			on_velocity( attributes);
		} else if ( name.equals( "selected_spot_property")) {
			on_selected_spot_property( attributes);
		} else if ( name.equals( "selected_agent_property")) {
			on_selected_agent_property( attributes);
		} else if ( name.equals( "time")) {
			on_time( attributes);
		} else if ( name.equals( "spot")) {
			on_spot( attributes);
		} else if ( name.equals( "agent")) {
			on_agent( attributes);
		} else if ( name.equals( "data")) {
			on_data( attributes);
		}
	}

	/**
	 * Invoked at the head of "property" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_property(Attributes attributes) {
		if ( null != _spotObject)
			on_spot_transition_property( attributes);
		else if ( null != _agentObject)
			on_agent_transition_property( attributes);
		else
			_property = true;
	}

	/**
	 * Invoked at the head of "agent_width" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent_width(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "agent_height" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent_height(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "spot_width" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot_width(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "spot_height" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot_height(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "minimum_width" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_minimum_width(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "velocity" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_velocity(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "selected_spot_property" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_selected_spot_property(Attributes attributes) {
		_selected_spot_properties = new Vector();
	}

	/**
	 * Invoked at the head of "selected_agent_property" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_selected_agent_property(Attributes attributes) {
		_selected_agent_properties = new Vector();
	}

	/**
	 * Invoked at the head of "time" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_time(Attributes attributes) {
		_time = true;
	}

	/**
	 * Invoked at the head of "spot" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot(Attributes attributes) {
		if ( _property)
			on_spot_property( attributes);
		else {
			if ( null == _agentObject)
				on_spot_object( attributes);
			else
				on_agent_transition_spot( attributes);
		}
	}

	/**
	 * Invoked at the head of "agent" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent(Attributes attributes) {
		if ( _property)
			on_agent_property( attributes);
		else
			on_agent_object( attributes);
	}

	/**
	 * Prepares for reading the new spot properties.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot_property(Attributes attributes) {
		_name = attributes.getValue( "name");
		if ( null == _name || _name.equals( "")) {
			_result = false;
			return;
		}

		_spot_property_map = new TreeMap();
	}

	/**
	 * Prepares for reading the new agent properties.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent_property(Attributes attributes) {
		_name = attributes.getValue( "name");
		if ( null == _name || _name.equals( "")) {
			_result = false;
			return;
		}

		_agent_property_map = new TreeMap();
	}

	/**
	 * Reads the spot object data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot_object(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		if ( null != SpotObjectManager.get_instance().get( name)) {
			_result = false;
			return;
		}

		_spotObject = new SpotObject( name, null, _graphics2D);
		SpotObjectManager.get_instance().append( name, _spotObject);


		String attribute = attributes.getValue( "visible");
		if ( null != attribute && !attribute.equals( ""))
			_spotObject._visible = ( attribute.equals( "true")) ? true : false;


		attribute = attributes.getValue( "visible_name");
		if ( null != attribute && !attribute.equals( ""))
			_spotObject._visibleName = ( attribute.equals( "true")) ? true : false;


		String image_r = attributes.getValue( "image_r");
		if ( null != image_r && !image_r.equals( "")) {
			String image_g = attributes.getValue( "image_g");
			if ( null != image_g && !image_g.equals( "")) {
				String image_b = attributes.getValue( "image_b");
				if ( null != image_b && !image_b.equals( "")) {
					try {
						_spotObject._imageColor = new Color(
							Integer.parseInt( image_r),
							Integer.parseInt( image_g),
							Integer.parseInt( image_b));
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_result = false;
						return;
					}
				}
			}
		}


		String text_r = attributes.getValue( "text_r");
		if ( null != text_r && !text_r.equals( "")) {
			String text_g = attributes.getValue( "text_g");
			if ( null != text_g && !text_g.equals( "")) {
				String text_b = attributes.getValue( "text_b");
				if ( null != text_b && !text_b.equals( "")) {
					try {
						_spotObject._textColor = new Color(
							Integer.parseInt( text_r),
							Integer.parseInt( text_g),
							Integer.parseInt( text_b));
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_result = false;
						return;
					}
				}
			}
		}


		String font_name = attributes.getValue( "font_name");
		if ( null != font_name && !font_name.equals( "")) {
			String font_style = attributes.getValue( "font_style");
			if ( null != font_style && !font_style.equals( "")) {
				String font_size = attributes.getValue( "font_size");
				if ( null != font_size && !font_size.equals( "")) {
					try {
						_spotObject._font = new Font( font_name,
							Integer.parseInt( font_style), Integer.parseInt( font_size));
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_result = false;
						return;
					}
				}
			}
		}

		if ( null == _spotObject._font) {
			Font font = _graphics2D.getFont();
			_spotObject._font = new Font( font.getFontName(), font.getStyle(), font.getSize());
		}


		if ( !_spotObject.setup( _graphics2D)) {
			_result = false;
			return;
		}


		// TODO エラーメッセージを表示(未実装)
		if ( !get_image( attributes, _spotObject)) {
			System.out.println( "Could not get image!");
		}


		int x, y;

		attribute = attributes.getValue( "x");
		if ( null == attribute || attribute.equals( "")) {
			_result = false;
			return;
		}

		try {
			x = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}

		attribute = attributes.getValue( "y");
		if ( null == attribute || attribute.equals( "")) {
			_result = false;
			return;
		}

		try {
			y = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}

		_spotObject.set_position( x, y);


		attribute = attributes.getValue( "image_object");
		if ( null != attribute && !attribute.equals( ""))
			_spotObject._imageObject = ( attribute.equals( "true")) ? true : false;
	}

	/**
	 * Reads the agent object data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent_object(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		_agentObject = new AgentObject( name, _graphics2D);
		AgentObjectManager.get_instance().append( name, _agentObject);


		String attribute = attributes.getValue( "visible");
		if ( null != attribute && !attribute.equals( ""))
			_agentObject._visible = ( attribute.equals( "true")) ? true : false;


		attribute = attributes.getValue( "visible_name");
		if ( null != attribute && !attribute.equals( ""))
			_agentObject._visibleName = ( attribute.equals( "true")) ? true : false;


		String image_r = attributes.getValue( "image_r");
		if ( null != image_r && !image_r.equals( "")) {
			String image_g = attributes.getValue( "image_g");
			if ( null != image_g && !image_g.equals( "")) {
				String image_b = attributes.getValue( "image_b");
				if ( null != image_b && !image_b.equals( "")) {
					try {
						_agentObject._imageColor = new Color(
							Integer.parseInt( image_r),
							Integer.parseInt( image_g),
							Integer.parseInt( image_b));
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_result = false;
						return;
					}
				}
			}
		}


		String text_r = attributes.getValue( "text_r");
		if ( null != text_r && !text_r.equals( "")) {
			String text_g = attributes.getValue( "text_g");
			if ( null != text_g && !text_g.equals( "")) {
				String text_b = attributes.getValue( "text_b");
				if ( null != text_b && !text_b.equals( "")) {
					try {
						_agentObject._textColor = new Color(
							Integer.parseInt( text_r),
							Integer.parseInt( text_g),
							Integer.parseInt( text_b));
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_result = false;
						return;
					}
				}
			}
		}


		String font_name = attributes.getValue( "font_name");
		if ( null != font_name && !font_name.equals( "")) {
			String font_style = attributes.getValue( "font_style");
			if ( null != font_style && !font_style.equals( "")) {
				String font_size = attributes.getValue( "font_size");
				if ( null != font_size && !font_size.equals( "")) {
					try {
						_agentObject._font = new Font( font_name,
							Integer.parseInt( font_style), Integer.parseInt( font_size));
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_result = false;
						return;
					}
				}
			}
		}

		if ( null == _agentObject._font) {
			Font font = _graphics2D.getFont();
			_agentObject._font = new Font( font.getFontName(), font.getStyle(), font.getSize());
		}


		if ( !_agentObject.setup( _graphics2D)) {
			_result = false;
			return;
		}


		// TODO エラーメッセージを表示(未実装)
		if ( !get_image( attributes, _agentObject)) {
			System.out.println( "Could not get image!");
		}
	}

	/**
	 * Reads the spot log data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot_transition_property(Attributes attributes) {
		_name = attributes.getValue( "name");
		if ( null == _name || _name.equals( "")) {
			_result = false;
			return;
		}

		Map map = ( Map)_spot_transition_map.get( _name);
		if ( null == map) {
			map = new HashMap();
			_spot_transition_map.put( _name, map);
		}

		_logObject = ( LogObject)map.get( _spotObject);
		if ( null == _logObject) {
			_logObject = new LogObject();
			map.put( _spotObject, _logObject);
		}
	}

	/**
	 * Reads the agent log data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent_transition_property(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "value");
		if ( null == value) {
			_result = false;
			return;
		}

		on_agent_transition( name, value);
	}

	/**
	 * Reads the agent log data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent_transition_spot(Attributes attributes) {
		String value = attributes.getValue( "name");
		if ( null == value) {
			_result = false;
			return;
		}

		on_agent_transition( "$Spot", value);
	}

	/**
	 * Reads the agent log data.
	 * @param name the specified name of agent
	 * @param value the value of log
	 */
	private void on_agent_transition(String name, String value) {
		Map map = ( Map)_agent_transition_map.get( name);
		if ( null == map) {
			map = new HashMap();
			_agent_transition_map.put( name, map);
		}

		LogObject logObject = ( LogObject)map.get( _agentObject);
		if ( null == logObject) {
			logObject = new LogObject();
			map.put( _agentObject, logObject);
		}

		if ( !logObject.append( value)) {
			_result = false;
			return;
		}
	}

	/**
	 * Invoked at the head of "data" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_data(Attributes attributes) {
		if ( _property) {
			if ( null != _spot_property_map) {
				get_spot_property( attributes);
				return;
			}
			if ( null != _agent_property_map) {
				get_agent_property( attributes);
				return;
			}
			if ( null != _selected_spot_properties) {
				get_selected_spot_property( attributes);
				return;
			}
			if ( null != _selected_agent_properties) {
				get_selected_agent_property( attributes);
				return;
			}
			if ( _time) {
				get_time( attributes);
				return;
			}
		} else {
			if ( null != _spotObject && null != _logObject) {
				get_spot_transition_property( attributes);
				return;
			}
		}
	}

	/**
	 * Reads the spot property data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_spot_property(Attributes attributes) {
		String value = attributes.getValue( "value");
		if ( null == value) {
			_result = false;
			return;
		}

		SpotProperty spotProperty = new SpotProperty();
		get_property( spotProperty, attributes);
		_spot_property_map.put( value, spotProperty);
	}

	/**
	 * Reads the agent property data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_agent_property(Attributes attributes) {
		String value = attributes.getValue( "value");
		if ( null == value) {
			_result = false;
			return;
		}

		AgentProperty agentProperty = new AgentProperty();
		get_property( agentProperty, attributes);
		_agent_property_map.put( value, agentProperty);
	}

	/**
	 * Reads the property data into the specified property data.
	 * @param propertyBase the specified property data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_property(PropertyBase propertyBase, Attributes attributes) {
		String attribute = attributes.getValue( "visible");
		if ( null != attribute && !attribute.equals( ""))
			propertyBase._visible = ( attribute.equals( "true")) ? true : false;

		attribute = attributes.getValue( "image_r");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				propertyBase._imageR = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		attribute = attributes.getValue( "image_g");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				propertyBase._imageG = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		attribute = attributes.getValue( "image_b");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				propertyBase._imageB = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}


		attribute = attributes.getValue( "text_r");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				propertyBase._textR = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		attribute = attributes.getValue( "text_g");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				propertyBase._textG = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		attribute = attributes.getValue( "text_b");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				propertyBase._textB = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}


		String font_name = attributes.getValue( "font_name");
		if ( null != font_name && !font_name.equals( "")) {
			String font_style = attributes.getValue( "font_style");
			if ( null != font_style && !font_style.equals( "")) {
				String font_size = attributes.getValue( "font_size");
				if ( null != font_size && !font_size.equals( "")) {
					try {
						propertyBase._font = new Font( font_name,
							Integer.parseInt( font_style), Integer.parseInt( font_size));
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_result = false;
						return;
					}
				}
			}
		}

		if ( null == propertyBase._font) {
			Font font = _graphics2D.getFont();
			propertyBase._font = new Font( font.getFontName(), font.getStyle(), font.getSize());
		}



		// TODO エラーメッセージを表示(未実装)
		if ( !get_image( attributes, propertyBase)) {
			System.out.println( "Could not get image!");
		}
	}

	/**
	 * Reads the name of the visible spot property.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_selected_spot_property(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		_selected_spot_properties.add( name);
	}

	/**
	 * Reads the name of the visible agent property.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_selected_agent_property(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		_selected_agent_properties.add( name);
	}

	/**
	 * Reads the value of the step.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_time(Attributes attributes) {
		String value = attributes.getValue( "value");
		if ( null == value) {
			_result = false;
			return;
		}

		_steps.add( value);
	}

	/**
	 * Reads the spot log data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_spot_transition_property(Attributes attributes) {
		String value = attributes.getValue( "value");
		if ( null == value) {
			_result = false;
			return;
		}

		if ( !_logObject.append( value)) {
			_result = false;
			return;
		}
	}

	/**
	 * Returns true if the specified image is set to the specified object.
	 * @param attributes the interface for a list of XML attributes
	 * @param object the specified object(AgentObject, SpotObject, AgentProperty or SpotProperty)
	 * @return true if the specified image is set to the specified object
	 */
	private boolean get_image(Attributes attributes, Object object) {
		String attribute = attributes.getValue( "image");
		if ( null == attribute || attribute.equals( ""))
			return true;

		File src = new File( FileUtility.get_absolute_path( _file_directory.getAbsolutePath(), attribute));
		if ( !src.exists() || !src.isFile() || !src.canRead())
			return ImageManipulator.get_default_image( object, src.getName(), _image_property_map);

		return ImageManipulator.get_image( object, src, _image_property_map);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		if ( !_result)
			return;

		if ( null != _value) {
			String value = new String( ch, start, length);
			_value += value;
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String name) throws SAXException {
		if ( !_result) {
			super.endElement(uri, localName, name);
			return;
		}

		if ( name.equals( "property")) {
			on_property();
		} else if ( name.equals( "agent_width")) {
			on_agent_width();
		} else if ( name.equals( "agent_height")) {
			on_agent_height();
		} else if ( name.equals( "spot_width")) {
			on_spot_width();
		} else if ( name.equals( "spot_height")) {
			on_spot_height();
		} else if ( name.equals( "minimum_width")) {
			on_minimum_width();
		} else if ( name.equals( "velocity")) {
			on_velocity();
		} else if ( name.equals( "selected_spot_property")) {
			on_selected_spot_property();
		} else if ( name.equals( "selected_agent_property")) {
			on_selected_agent_property();
		} else if ( name.equals( "time")) {
			on_time();
		} else if ( name.equals( "spot")) {
			on_spot();
		} else if ( name.equals( "agent")) {
			on_agent();
		}
	}

	/**
	 * Invoked at the end of the "property" tag.
	 */
	private void on_property() {
		if ( null != _spotObject)
			on_spot_transition_property();
		else if ( null != _agentObject)
			on_agent_transition_property();
		else
			_property = false;
	}

	/**
	 * Invoked at the end of the "agent_width" tag.
	 */
	private void on_agent_width() {
		try {
			CommonProperty.get_instance()._agentWidth = Integer.parseInt( _value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}
		_value = null;
	}

	/**
	 * Invoked at the end of the "agent_height" tag.
	 */
	private void on_agent_height() {
		try {
			CommonProperty.get_instance()._agentHeight = Integer.parseInt( _value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}
		_value = null;
	}

	/**
	 * Invoked at the end of the "spot_width" tag.
	 */
	private void on_spot_width() {
		try {
			CommonProperty.get_instance()._spotWidth = Integer.parseInt( _value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}
		_value = null;
	}

	/**
	 * Invoked at the end of the "spot_height" tag.
	 */
	private void on_spot_height() {
		try {
			CommonProperty.get_instance()._spotHeight = Integer.parseInt( _value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}
		_value = null;
	}

	/**
	 * Invoked at the end of the "minimum_width" tag.
	 */
	private void on_minimum_width() {
//		try {
//			CommonProperty.get_instance()._minimum_width = Integer.parseInt( _value);
//		} catch (NumberFormatException e) {
//			//e.printStackTrace();
//			_result = false;
//			return;
//		}
//		_value = null;
	}

	/**
	 * Invoked at the end of the "velocity" tag.
	 */
	private void on_velocity() {
		try {
			CommonProperty.get_instance()._divide = Integer.parseInt( _value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}
		_value = null;
	}

	/**
	 * Invoked at the end of "selected_spot_property" tag.
	 */
	private void on_selected_spot_property() {
		if ( _selected_spot_properties.isEmpty())
			return;

		SpotPropertyManager.get_instance().set_selected_properties( _selected_spot_properties);
		_selected_spot_properties = null;
	}

	/**
	 * Invoked at the end of "selected_agent_property" tag.
	 */
	private void on_selected_agent_property() {
		if ( _selected_agent_properties.isEmpty())
			return;

		AgentPropertyManager.get_instance().set_selected_properties( _selected_agent_properties);
		_selected_agent_properties = null;
	}

	/**
	 * Invoked at the end of "time" tag.
	 */
	private void on_time() {
		_time = false;
	}

	/**
	 * Invoked at the end of "spot" tag.
	 */
	private void on_spot() {
		if ( _property)
			on_spot_property();
		else {
			if ( null == _agentObject)
				on_spot_object();
			else
				on_agent_transition_spot();
		}
	}

	/**
	 * Invoked at the end of "agent" tag.
	 */
	private void on_agent() {
		if ( _property)
			on_agent_property();
		else
			on_agent_object();
	}

	/**
	 * Invoked after reading the new spot properties.
	 */
	private void on_spot_property() {
		_result = SpotPropertyManager.get_instance().append_properties( _name, _spot_property_map, _graphics2D);
		_name = null;
		_spot_property_map = null;
	}

	/**
	 * Invoked after reading the new agent properties.
	 */
	private void on_agent_property() {
		_result = AgentPropertyManager.get_instance().append_properties( _name, _agent_property_map, _graphics2D);
		_name = null;
		_agent_property_map = null;
	}

	/**
	 * Invoked after reading the spot object data.
	 */
	private void on_spot_object() {
		_spotObject = null;
	}

	/**
	 * Invoked after reading the agent object data.
	 */
	private void on_agent_object() {
		_agentObject = null;
	}

	/**
	 * Invoked after reading the spot log data.
	 */
	private void on_spot_transition_property() {
		_logObject = null;
	}

	/**
	 * Invoked after reading the agent log data.
	 */
	private void on_agent_transition_property() {
	}

	/**
	 * Invoked after reading the agent log data.
	 */
	private void on_agent_transition_spot() {
	}

//	/**
//	 * 
//	 */
//	private void debug() {
//		System.out.println( "Time : " + _steps.size());
//		debug( _agent_transition_map);
//		debug( _spot_transition_map);
//	}
//
//	/**
//	 * @param transition_map
//	 */
//	private void debug(Map transition_map) {
//		Iterator iterator = transition_map.entrySet().iterator();
//		while ( iterator.hasNext()) {
//			Object object = iterator.next();
//			Map.Entry entry = ( Map.Entry)object;
//			String name = ( String)entry.getKey();
//			Map map = ( Map)entry.getValue();
//			debug( name, map);
//		}
//	}
//
//	/**
//	 * @param name
//	 * @param map
//	 */
//	private void debug(String name, Map map) {
//		Iterator iterator = map.entrySet().iterator();
//		while ( iterator.hasNext()) {
//			Object object = iterator.next();
//			Map.Entry entry = ( Map.Entry)object;
//			ObjectBase objectBase = ( ObjectBase)entry.getKey();
//			LogObject logObject = ( LogObject)entry.getValue();
//			System.out.println( name + " : " + objectBase._name + " : " + logObject.size());
//		}
//	}
}
