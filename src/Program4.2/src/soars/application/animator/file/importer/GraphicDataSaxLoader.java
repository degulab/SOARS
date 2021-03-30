/*
 * 2005/07/01
 */
package soars.application.animator.file.importer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import soars.application.animator.file.common.ImageManipulator;
import soars.application.animator.object.player.ObjectManager;
import soars.application.animator.object.player.agent.AgentObject;
import soars.application.animator.object.player.agent.AgentObjectManager;
import soars.application.animator.object.player.spot.SpotObject;
import soars.application.animator.object.player.spot.SpotObjectManager;
import soars.application.animator.object.property.agent.AgentProperty;
import soars.application.animator.object.property.agent.AgentPropertyManager;
import soars.application.animator.object.property.base.PropertyBase;
import soars.application.animator.object.property.spot.SpotProperty;
import soars.application.animator.object.property.spot.SpotPropertyManager;
import soars.application.animator.setting.common.CommonProperty;
import soars.common.utility.swing.image.ImagePropertyManager;

/**
 * The XML SAX loader for Animator graphics data.
 * @author kurata / SOARS project
 */
public class GraphicDataSaxLoader extends DefaultHandler {

	/**
	 * Graphics object of JAVA.
	 */
	private Graphics2D _graphics2D = null;

	/**
	 * Root directory for Animator graphics data file.
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
	 * True while reading the data of the SOARS objects.
	 */
	private boolean _object = false;

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
	 * Temporary SpotObject.
	 */
	private SpotObject _spotObject = null;

	/**
	 * Temporary AgentObject.
	 */
	private AgentObject _agentObject = null;

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
	 * @param file the specified XML file of Animator graphics data
	 * @param root_directory the root directory for Animator data
	 * @param graphics2D the graphics object of JAVA
	 * @return true if loading the specified file is completed successfully
	 */
	public static boolean execute(File file, File root_directory, Graphics2D graphics2D) {
		_result = false;
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			GraphicDataSaxLoader graphicDataSaxLoader = new GraphicDataSaxLoader( file.getParentFile(), root_directory, graphics2D);
			saxParser.parse( file, graphicDataSaxLoader);
			graphicDataSaxLoader.at_end_of_load();
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

		AgentObjectManager.get_instance().arrange();
		ImagePropertyManager.get_instance().putAll( _image_property_map);
	}

	/**
	 * Creates the XML SAX loader for Animator graphics data.
	 * @param file_directory the parent directory which contains XML file of Animator graphics data
	 * @param root_directory the root directory for Animator data
	 * @param graphics2D
	 */
	public GraphicDataSaxLoader(File file_directory, File root_directory, Graphics2D graphics2D) {
		super();
		_file_directory = file_directory;
		_root_directory = root_directory;
		_graphics2D = graphics2D;
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {

		super.startElement(arg0, arg1, arg2, arg3);

		if ( arg2.equals( "animation_data")
			|| arg2.equals( "animator_graphic_data")) {
			_result = true;
			return;
		}

		if ( !_result)
			return;

		if ( arg2.equals( "property")) {
			on_property( arg3);
		} else if ( arg2.equals( "object")) {
			on_object( arg3);
		} else if ( arg2.equals( "agent_width")) {
			on_agent_width( arg3);
		} else if ( arg2.equals( "agent_height")) {
			on_agent_height( arg3);
		} else if ( arg2.equals( "spot_width")) {
			on_spot_width( arg3);
		} else if ( arg2.equals( "spot_height")) {
			on_spot_height( arg3);
		} else if ( arg2.equals( "minimum_width")) {
			on_minimum_width( arg3);
		} else if ( arg2.equals( "velocity")) {
			on_velocity( arg3);
		} else if ( arg2.equals( "selected_spot_property")) {
			on_selected_spot_property( arg3);
		} else if ( arg2.equals( "selected_agent_property")) {
			on_selected_agent_property( arg3);
		} else if ( arg2.equals( "spot")) {
			on_spot( arg3);
		} else if ( arg2.equals( "agent")) {
			on_agent( arg3);
		} else if ( arg2.equals( "data")) {
			on_data( arg3);
		}
	}

	/**
	 * Invoked at the head of "property" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_property(Attributes attributes) {
		if ( null == _spotObject && null == _agentObject)
			_property = true;
	}

	/**
	 * Invoked at the head of "object" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_object(Attributes attributes) {
		_object = true;
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
	 * Invoked at the head of "spot" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot(Attributes attributes) {
		if ( _property)
			on_spot_property( attributes);
		else if ( _object)
			on_spot_object( attributes);
	}

	/**
	 * Invoked at the head of "agent" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent(Attributes attributes) {
		if ( _property)
			on_agent_property( attributes);
		else if ( _object)
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

//		if ( null != SpotObjectManager.get_instance().get( name)) {
//			_result = false;
//			return;
//		}

		_spotObject = new SpotObject( name, null);


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
				if ( null != image_b && !image_b.equals( ""))
					_spotObject._imageColor = new Color(
						Integer.parseInt( image_r),
						Integer.parseInt( image_g),
						Integer.parseInt( image_b));
			}
		}


		String text_r = attributes.getValue( "text_r");
		if ( null != text_r && !text_r.equals( "")) {
			String text_g = attributes.getValue( "text_g");
			if ( null != text_g && !text_g.equals( "")) {
				String text_b = attributes.getValue( "text_b");
				if ( null != text_b && !text_b.equals( ""))
					_spotObject._textColor = new Color(
						Integer.parseInt( text_r),
						Integer.parseInt( text_g),
						Integer.parseInt( text_b));
			}
		}


		String font_name = attributes.getValue( "font_name");
		if ( null != font_name && !font_name.equals( "")) {
			String font_style = attributes.getValue( "font_style");
			if ( null != font_style && !font_style.equals( "")) {
				String font_size = attributes.getValue( "font_size");
				if ( null != font_size && !font_size.equals( "")) {
					_spotObject._font = new Font( font_name,
						Integer.parseInt( font_style), Integer.parseInt( font_size));
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

		x = Integer.parseInt( attribute);

		attribute = attributes.getValue( "y");
		if ( null == attribute || attribute.equals( "")) {
			_result = false;
			return;
		}

		y = Integer.parseInt( attribute);

		_spotObject.set_position( x, y);

		// TODO Auto-generated method stub
		if ( !set_image_position_and_image_size( attributes)) {
			_result = false;
			return;
		}

		SpotObjectManager.get_instance().update( name, _spotObject);
	}

	/**
	 * @param attributes
	 * @return
	 */
	private boolean set_image_position_and_image_size(Attributes attributes) {
		// TODO Auto-generated method stub
		int imageX, imageY, imageWidth, imageHeight;

		String attribute = attributes.getValue( "image_x");
		if ( null == attribute || attribute.equals( ""))
			return true;

		try {
			imageX = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		attribute = attributes.getValue( "image_y");
		if ( null == attribute || attribute.equals( ""))
			return true;

		try {
			imageY = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		attribute = attributes.getValue( "image_width");
		if ( null == attribute || attribute.equals( ""))
			return true;

		try {
			imageWidth = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		attribute = attributes.getValue( "image_height");
		if ( null == attribute || attribute.equals( ""))
			return true;

		try {
			imageHeight = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		_spotObject.set_image_position_and_image_size( imageX, imageY, imageWidth, imageHeight);

		return true;
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

		_agentObject = new AgentObject( name);


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
				if ( null != image_b && !image_b.equals( ""))
					_agentObject._imageColor = new Color(
						Integer.parseInt( image_r),
						Integer.parseInt( image_g),
						Integer.parseInt( image_b));
			}
		}


		String text_r = attributes.getValue( "text_r");
		if ( null != text_r && !text_r.equals( "")) {
			String text_g = attributes.getValue( "text_g");
			if ( null != text_g && !text_g.equals( "")) {
				String text_b = attributes.getValue( "text_b");
				if ( null != text_b && !text_b.equals( ""))
					_agentObject._textColor = new Color(
						Integer.parseInt( text_r),
						Integer.parseInt( text_g),
						Integer.parseInt( text_b));
			}
		}


		String font_name = attributes.getValue( "font_name");
		if ( null != font_name && !font_name.equals( "")) {
			String font_style = attributes.getValue( "font_style");
			if ( null != font_style && !font_style.equals( "")) {
				String font_size = attributes.getValue( "font_size");
				if ( null != font_size && !font_size.equals( "")) {
					_agentObject._font = new Font( font_name,
						Integer.parseInt( font_style), Integer.parseInt( font_size));
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

		AgentObjectManager.get_instance().update( name, _agentObject);
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
		}
	}

	/**
	 * Reads the spot property data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_spot_property(Attributes attributes) {
		if ( null == _spot_property_map)
			return;

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
		if ( null == _agent_property_map)
			return;

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
		if ( null != attribute && !attribute.equals( ""))
			propertyBase._imageR = Integer.parseInt( attribute);

		attribute = attributes.getValue( "image_g");
		if ( null != attribute && !attribute.equals( ""))
			propertyBase._imageG = Integer.parseInt( attribute);

		attribute = attributes.getValue( "image_b");
		if ( null != attribute && !attribute.equals( ""))
			propertyBase._imageB = Integer.parseInt( attribute);


		attribute = attributes.getValue( "text_r");
		if ( null != attribute && !attribute.equals( ""))
			propertyBase._textR = Integer.parseInt( attribute);

		attribute = attributes.getValue( "text_g");
		if ( null != attribute && !attribute.equals( ""))
			propertyBase._textG = Integer.parseInt( attribute);

		attribute = attributes.getValue( "text_b");
		if ( null != attribute && !attribute.equals( ""))
			propertyBase._textB = Integer.parseInt( attribute);


		String font_name = attributes.getValue( "font_name");
		if ( null != font_name && !font_name.equals( "")) {
			String font_style = attributes.getValue( "font_style");
			if ( null != font_style && !font_style.equals( "")) {
				String font_size = attributes.getValue( "font_size");
				if ( null != font_size && !font_size.equals( "")) {
					propertyBase._font = new Font( font_name,
						Integer.parseInt( font_style), Integer.parseInt( font_size));
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
//		String name = attributes.getValue( "name");
//		if ( null == name || name.equals( "")) {
//			_result = false;
//			return;
//		}
//
//		_selected_spot_properties.add( name);
	}

	/**
	 * Reads the name of the visible agent property.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_selected_agent_property(Attributes attributes) {
//		String name = attributes.getValue( "name");
//		if ( null == name || name.equals( "")) {
//			_result = false;
//			return;
//		}
//
//		_selected_agent_properties.add( name);
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

		File src = new File( ObjectManager.get_instance().get_image_directory( _root_directory), attribute);
		if ( !src.exists() || !src.isFile() || !src.canRead())
			return ImageManipulator.get_default_image( object, src.getName(), _image_property_map);

		return ImageManipulator.get_image( object, src, _image_property_map);
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		if ( !_result)
			return;

		if ( null != _value) {
			String value = new String( arg0, arg1, arg2);
			_value += value;
		}
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String arg0, String arg1, String arg2) throws SAXException {

		if ( !_result) {
			super.endElement(arg0, arg1, arg2);
			return;
		}

		if ( arg2.equals( "property")) {
			on_property();
		} else if ( arg2.equals( "object")) {
			on_object();
		} else if ( arg2.equals( "agent_width")) {
			on_agent_width();
		} else if ( arg2.equals( "agent_height")) {
			on_agent_height();
		} else if ( arg2.equals( "spot_width")) {
			on_spot_width();
		} else if ( arg2.equals( "spot_height")) {
			on_spot_height();
		} else if ( arg2.equals( "minimum_width")) {
			on_minimum_width();
		} else if ( arg2.equals( "velocity")) {
			on_velocity();
		} else if ( arg2.equals( "selected_spot_property")) {
			on_selected_spot_property();
		} else if ( arg2.equals( "selected_agent_property")) {
			on_selected_agent_property();
		} else if ( arg2.equals( "spot")) {
			on_spot();
		} else if ( arg2.equals( "agent")) {
			on_agent();
		}
	}

	/**
	 * Invoked at the end of the "property" tag.
	 */
	private void on_property() {
		_property = false;
	}

	/**
	 * Invoked at the end of the "object" tag.
	 */
	private void on_object() {
		_object = false;
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
//		CommonProperty.get_instance()._minimum_width = Integer.parseInt( _value);
//	} catch (NumberFormatException e) {
//		//e.printStackTrace();
//		_result = false;
//		return;
//	}
		_value = null;
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
//		if ( _selected_spot_properties.isEmpty())
//			return;
//
//		_result = ScenarioManager.get_instance().set_selected_spot_properties( _selected_spot_properties);
		_selected_spot_properties = null;
	}

	/**
	 * Invoked at the end of "selected_agent_property" tag.
	 */
	private void on_selected_agent_property() {
//		if ( _selected_agent_properties.isEmpty())
//			return;
//
//		_result = ScenarioManager.get_instance().set_selected_agent_properties( _selected_agent_properties);
		_selected_agent_properties = null;
	}

	/**
	 * Invoked at the end of "spot" tag.
	 */
	private void on_spot() {
		if ( _property)
			on_spot_property();
		else if ( _object)
			on_spot_object();
	}

	/**
	 * Invoked at the end of "agent" tag.
	 */
	private void on_agent() {
		if ( _property)
			on_agent_property();
		else if ( _object)
			on_agent_object();
	}

	/**
	 * Invoked after reading the new spot properties.
	 */
	private void on_spot_property() {
		_result = SpotPropertyManager.get_instance().update_properties( _name, _spot_property_map, _graphics2D);
		_name = null;
		_spot_property_map = null;
	}

	/**
	 * Invoked after reading the new agent properties.
	 */
	private void on_agent_property() {
		_result = AgentPropertyManager.get_instance().update_properties( _name, _agent_property_map, _graphics2D);
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
}
