/**
 * 
 */
package soars.application.animator.object.file;

import java.awt.Graphics2D;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.common.tool.TimeComparator;
import soars.application.animator.file.loader.legacy.LogObject;
import soars.application.animator.object.player.agent.AgentObject;
import soars.application.animator.object.player.agent.AgentObjectManager;
import soars.application.animator.object.player.base.ObjectBase;
import soars.application.animator.object.player.spot.ISpotObjectManipulator;
import soars.application.animator.object.player.spot.SpotObject;
import soars.application.animator.object.player.spot.SpotObjectManager;
import soars.application.animator.object.property.agent.AgentPropertyManager;
import soars.application.animator.object.property.spot.SpotPropertyManager;
import soars.application.animator.object.transition.agent.AgentTransitionManager;
import soars.application.animator.object.transition.spot.SpotTransitionManager;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.xml.sax.Writer;

/**
 * The log file information class.
 * @author kurata / SOARS project
 */
public class FileObject {

	/**
	 * Extension of the log file name.
	 */
	static public final String _extension = "log";

	/**
	 * 
	 */
	static private final String _encoding = "UTF-8";

	/**
	 * Type of the log file("agents" or "spots").
	 */
	public String _type = null;

	/**
	 * 
	 */
	public File _file = null;

	/**
	 * Name of the log.
	 */
	public String _name = "";

	/**
	 * Position of the header data.
	 */
	public long _header = 0l;

	/**
	 * Names of the log values.
	 */
	public String[] _names = null;

	/**
	 * Max number of agents.
	 */
	public int _max = 1;

	/**
	 * 
	 */
	private RandomAccessFile _randomAccessFile = null;

	/**
	 * 
	 */
	private FileChannel _fileChannel = null;

	/**
	 * 
	 */
	private long _previous_position = 0l;

	/**
	 * Creates the instance of the specified log file information class.
	 * @param type the type of the specified log file("agents" or "spots")
	 * @param file the specified log file
	 */
	public FileObject(String type, File file) {
		super();
		_type = type;
		_file = file;
	}

	/**
	 * Creates the instance of the specified log file information class.
	 * @param type the type of the specified log file("agents" or "spots")
	 * @param name the name of the specified log
	 * @param header the specified log file
	 */
	public FileObject(String type, String name, long header) {
		super();
		_type = type;
		_name = name;
		_header = header;
	}

	/**
	 * @param type the type of the specified log file("agents" or "spots")
	 * @param name the name of the specified log
	 */
	public FileObject(String type, String name) {
		super();
		_type = type;
		_name = name;
	}

	/**
	 * Returns true if the log file is opened successfully.
	 * @return true if the log file is opened successfully
	 */
	public boolean open() {
		if ( null != _randomAccessFile)
			return true;

		try {
			_randomAccessFile = new RandomAccessFile( _file, "r");
			_fileChannel = _randomAccessFile.getChannel();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Closes the log file.
	 */
	public void close() {
		if ( null == _randomAccessFile)
			return;

		try {
			_randomAccessFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		_randomAccessFile = null;
		_fileChannel = null;
		_previous_position = 0l;
	}

	/**
	 * Returns true for reading steps and values successfully.
	 * @param steps the array for the step strings of the log file
	 * @param graphics2D the graphics object of JAVA
	 * @return true for reading steps and values successfully
	 */
	public boolean read(List steps, Graphics2D graphics2D) {
		if ( !open()) {
			close();
			return false;
		}

		while ( true) {
			try {
				_previous_position = _fileChannel.position();
			} catch (IOException e) {
				e.printStackTrace();
				close();
				return false;
			}

			String line = FileUtility.readLine( _randomAccessFile, _encoding);
			if ( null == line)
				break;

			if ( 0 == line.length())
				continue;

			String[] words = Tool.split( line, '\t');
			if ( null == words || 2 > words.length) {
				close();
				return false;
			}

			if ( words[ 0].equals( "")) {
				if ( words[ 1].equals( "$Name")) {
					if ( _type.equals( "agents")) {
						if ( _file.getName().equals( "$Name.log"))
							_name = "$Name";

						append_agents( words, graphics2D);
						get_names( words);
						_header = _previous_position;
					} else if ( _type.equals( "spots")) {
						append_spots( words, graphics2D);
						get_names( words);
						_header = _previous_position;
					}

					continue;
				} else if ( _file.getName().equals( words[ 1] + "." + _extension))
					_name = words[ 1];
				else
					continue;
			} else {
				if ( !steps.contains( words[ 0]))
					steps.add( words[ 0]);
			}

			if ( _name.equals( "")) {
				close();
				return false;
			}

			if ( _type.equals( "agents")) {
				if ( _name.equals( "$Spot"))
					append_spots( words, graphics2D);
				else
					append_agent_properties( _name, words, graphics2D);
			} else if ( _type.equals( "spots"))
				append_spot_properties( _name, words, graphics2D);
			else
				continue;
		}

		close();

		if ( _type.equals( "agents")) {
			if ( !_name.equals( "$Spot"))
				AgentPropertyManager.get_instance().append( _name, "", graphics2D);
		} else if ( _type.equals( "spots")) {
			SpotPropertyManager.get_instance().append( _name, "", graphics2D);
		}

		return true;
	}

	/**
	 * @param words
	 */
	private void get_names(String[] words) {
		_names = new String[ words.length - 2];
		if ( 3 > words.length)
			return;

		System.arraycopy( words, 2, _names, 0, _names.length);
	}

	/**
	 * @param words
	 * @param graphics2D
	 */
	private void append_agents(String[] words, Graphics2D graphics2D) {
		for ( int i = 2; i < words.length; ++i) {
			if ( null != AgentObjectManager.get_instance().get( words[ i]))
				continue;

			AgentObjectManager.get_instance().append( words[ i], new AgentObject( words[ i], graphics2D));
		}
	}

	/**
	 * @param words
	 * @param graphics2D
	 */
	private void append_spots(String[] words, Graphics2D graphics2D) {
		if ( _type.equals( "agents") && _name.equals( "$Spot")) {
			Map map = new HashMap();
			for ( int i = 2; i < words.length; ++i) {
				IntBuffer counter = ( IntBuffer)map.get( words[ i]);
				if ( null != counter) {
					counter.put( 0, counter.get( 0) + 1);
					_max = Math.max( _max, counter.get( 0));
				} else {
					counter = IntBuffer.allocate( 1);
					counter.put( 0, 1);
					map.put( words[ i], counter);
				}

				append_spot( words[ i], graphics2D);
			}
		} else {
			for ( int i = 2; i < words.length; ++i)
				append_spot( words[ i], graphics2D);
		}
	}

	/**
	 * @param name
	 * @param graphics2D
	 */
	private void append_spot(String name, Graphics2D graphics2D) {
		if ( null != SpotObjectManager.get_instance().get( name))
			return;

		SpotObjectManager.get_instance().append( name, new SpotObject( name, null, graphics2D));
	}

	/**
	 * @param name
	 * @param words
	 * @param graphics2D
	 */
	private void append_agent_properties(String name, String[] words, Graphics2D graphics2D) {
		for ( int i = 2; i < words.length; ++i)
			AgentPropertyManager.get_instance().append( name, words[ i], graphics2D);
	}

	/**
	 * @param name
	 * @param words
	 * @param graphics2D
	 */
	private void append_spot_properties(String name, String[] words, Graphics2D graphics2D) {
		for ( int i = 2; i < words.length; ++i)
			SpotPropertyManager.get_instance().append( name, words[ i], graphics2D);
	}

	/**
	 * Returns true for writing the offset of the line header successfully.
	 * @param step the step string of the log file
	 * @param dataOutputStream the stream for writing
	 * @return true for writing the offset of the line header successfully
	 */
	public boolean write(String step, DataOutputStream dataOutputStream) {
		try {
			if ( step.equals( "")) {
				if ( 0l != _fileChannel.position())
					return false;

				if ( null == FileUtility.readLine( _randomAccessFile, _encoding))	// $ID
					return false;

				if ( null == FileUtility.readLine( _randomAccessFile, _encoding))	// $Name
					return false;

				_previous_position = _fileChannel.position();

				dataOutputStream.writeLong( _previous_position);

				if ( null == FileUtility.readLine( _randomAccessFile, _encoding))	// stepが""の行
					return false;

			} else {
				long current_position = _fileChannel.position();

				String current_line = FileUtility.readLine( _randomAccessFile, _encoding);
				if ( null == current_line || current_line.startsWith( "\t")) {
					dataOutputStream.writeLong( _previous_position);
					_fileChannel.position( current_position);
				} else {
					String[] words = Tool.split( current_line, '\t');
					if ( null == words || 3 > words.length || words[ 0].equals( ""))
						return false;

					double w = TimeComparator.get_double( words[ 0]);
					double s = TimeComparator.get_double( step);

					if ( w < s) {
						//words[ 0]がstepより小さいと云うことはありえない筈
						return false;
					} else if ( w > s) {
						//words[ 0]がstepより大きい
						dataOutputStream.writeLong( _previous_position);
						_fileChannel.position( current_position);
					} else {
						//words[ 0]がstepと等しい
						dataOutputStream.writeLong( current_position);
						_previous_position = current_position;
					}
				}
			}

			return true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Returns true if this object is initialized successfully.
	 * @return true if this object is initialized successfully
	 */
	public boolean setup() {
		if ( !open()) {
			close();
			return false;
		}

		try {
			_fileChannel.position( _header);
		} catch (IOException e) {
			e.printStackTrace();
			close();
			return false;
		}

		String line = FileUtility.readLine( _randomAccessFile, _encoding);
		if ( null == line || 0 == line.length()) {
			close();
			return false;
		}

		String[] words = Tool.split( line, '\t');
		if ( null == words || 2 > words.length) {
			close();
			return false;
		}

		get_names( words);

		close();

		return true;
	}

	/**
	 * Returns true for loading the values on the specified line successfully.
	 * @param position the offset of the specified line
	 * @return true for loading the values on the specified line successfully
	 */
	public boolean load(long position) {
//		if ( _type.equals( "agents")
//			&& !_name.equals( "$Spot")
//			&& !AgentPropertyManager.get_instance().is_selected( _name))
//			return true;
//
//		if ( _type.equals( "spots")
//			&& !SpotPropertyManager.get_instance().is_selected( _name))
//			return true;

		try {
			_fileChannel.position( position);
			String line = FileUtility.readLine( _randomAccessFile, _encoding);
			if ( null == line)
				return false;

			String[] words = Tool.split( line, '\t');
			if ( null == words || 2 > words.length)
				return false;

			if ( _type.equals( "agents")) {
				if ( _name.equals( "$Spot")) {
					if ( !append_agents_transition( words))
						return false;
				} else {
					if ( !append_agent_properties_transition( words))
						return false;
				}
			} else if ( _type.equals( "spots")) {
				if ( !append_spot_properties_transition( words))
					return false;
			} else
				return false;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param words
	 * @return
	 */
	private boolean append_agents_transition(String[] words) {
		for ( int i = 0; i < _names.length; ++i) {
			AgentObject agentObject = ( AgentObject)AgentObjectManager.get_instance().get( _names[ i]);
			if ( null == agentObject)
				return false;

			ISpotObjectManipulator spotObjectManipulator = null;
			if ( i < words.length - 2) {
				spotObjectManipulator = ( ISpotObjectManipulator)SpotObjectManager.get_instance().get( words[ i + 2]);
				if ( null == spotObjectManipulator)
					return false;
			}

			if ( null != spotObjectManipulator)
				agentObject.set( spotObjectManipulator);
		}

		// 順序を考慮して設定
		AgentObjectManager.get_instance().setup();

		return true;
	}

	/**
	 * @param words
	 * @return
	 */
	private boolean append_agent_properties_transition(String[] words) {
		for ( int i = 0; i < _names.length; ++i) {
			AgentObject agentObject = ( AgentObject)AgentObjectManager.get_instance().get( _names[ i]);
			if ( null == agentObject)
				return false;

			if ( !agentObject.set_property( _name, ( ( i < words.length - 2) ? words[ i + 2] : "")))
				return false;
		}
		return true;
	}

	/**
	 * @param words
	 * @return
	 */
	private boolean append_spot_properties_transition(String[] words) {
		for ( int i = 0; i < _names.length; ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)SpotObjectManager.get_instance().get( _names[ i]);
			if ( null == spotObjectManipulator)
				return false;

			if ( !spotObjectManipulator.set_property( _name, ( ( i < words.length - 2) ? words[ i + 2] : "")))
				return false;
		}
		return true;
	}

	/**
	 * Returns true for reading the values on the specified line successfully.
	 * @param index the index of the scenario
	 * @param position the offset of the specified line
	 * @param agentTransitionManager the scenario data manager of agents
	 * @param spotTransitionManager the scenario data manager of spots
	 * @return true for reading the values on the specified line successfully
	 */
	public boolean read(int index, long position, AgentTransitionManager agentTransitionManager, SpotTransitionManager spotTransitionManager) {
		if ( _type.equals( "agents")
			&& !_name.equals( "$Spot")
			&& !AgentPropertyManager.get_instance().is_selected( _name))
			return true;

		if ( _type.equals( "spots")
			&& !SpotPropertyManager.get_instance().is_selected( _name))
			return true;

		try {
			_fileChannel.position( position);
			String line = FileUtility.readLine( _randomAccessFile, _encoding);
			if ( null == line)
				return false;

			String[] words = Tool.split( line, '\t');
			if ( null == words || 2 > words.length)
				return false;

			if ( _type.equals( "agents")) {
				if ( _name.equals( "$Spot")) {
					if ( !append_agents_transition( index, words, agentTransitionManager, spotTransitionManager))
						return false;
				} else {
					if ( !append_agent_properties_transition( index, words, agentTransitionManager))
						return false;
				}
			} else if ( _type.equals( "spots")) {
				if ( !append_spot_properties_transition( index, words, spotTransitionManager))
					return false;
			} else
				return false;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param index
	 * @param words
	 * @param agentTransitionManager
	 * @param spotTransitionManager
	 * @return
	 */
	private boolean append_agents_transition(int index, String[] words, AgentTransitionManager agentTransitionManager, SpotTransitionManager spotTransitionManager) {
		for ( int i = 0; i < _names.length; ++i) {
			AgentObject agentObject = ( AgentObject)AgentObjectManager.get_instance().get( _names[ i]);
			if ( null == agentObject)
				return false;

			ISpotObjectManipulator spotObjectManipulator = null;
			if ( i < words.length - 2) {
				spotObjectManipulator = ( ISpotObjectManipulator)SpotObjectManager.get_instance().get( words[ i + 2]);
				if ( null == spotObjectManipulator)
					return false;
			}

			agentTransitionManager.set( index, agentObject, spotObjectManipulator);
		}

		// 順序を考慮して設定
		AgentObjectManager.get_instance().set( index, agentTransitionManager, spotTransitionManager);

		return true;
	}

	/**
	 * @param index
	 * @param words
	 * @param agentTransitionManager
	 * @return
	 */
	private boolean append_agent_properties_transition(int index, String[] words, AgentTransitionManager agentTransitionManager) {
		for ( int i = 0; i < _names.length; ++i) {
			AgentObject agentObject = ( AgentObject)AgentObjectManager.get_instance().get( _names[ i]);
			if ( null == agentObject)
				return false;

			if ( !agentTransitionManager.set_property( agentObject, index, _name, ( ( i < words.length - 2) ? words[ i + 2] : "")))
				return false;
		}
		return true;
	}

	/**
	 * @param index
	 * @param words
	 * @param spotTransitionManager
	 * @return
	 */
	private boolean append_spot_properties_transition(int index, String[] words, SpotTransitionManager spotTransitionManager) {
		for ( int i = 0; i < _names.length; ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)SpotObjectManager.get_instance().get( _names[ i]);
			if ( null == spotObjectManipulator)
				return false;

			if ( !spotTransitionManager.set_property( spotObjectManipulator, index, _name, ( ( i < words.length - 2) ? words[ i + 2] : "")))
				return false;
		}
		return true;
	}

	/**
	 * Returns the index of the specified name.
	 * @param name the specified name
	 * @return the index of the specified name
	 */
	public int get(String name) {
		for ( int i = 0; i < _names.length; ++i) {
			if ( _names[ i].equals( name))
				return i;
		}
		return -1;
	}

	/**
	 * Returns true if the specified value is found.
	 * @param position the offset of the specified line
	 * @param offset the index of the compared value in the specified line
	 * @param value the specified value
	 * @return true if the specified value is found
	 */
	public boolean retrieve(long position, int offset, String value) {
		try {
			_fileChannel.position( position);

			String line = FileUtility.readLine( _randomAccessFile, _encoding);
			if ( null == line || 0 == line.length())
				return false;

			String[] words = Tool.split( line, '\t');
			if ( null == words || ( offset + 3) > words.length)
				return false;

			return words[ offset + 2].equals( value);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns true if the value, which is not less than the specified value, is found.
	 * @param position the offset of the specified line
	 * @param offset the index of the compared value in the specified line
	 * @param value the specified value
	 * @return true if the value, which is not less than the specified value, is found
	 */
	public boolean retrieve_more_than(long position, int offset, double value) {
		try {
			_fileChannel.position( position);

			String line = FileUtility.readLine( _randomAccessFile, _encoding);
			if ( null == line || 0 == line.length())
				return false;

			String[] words = Tool.split( line, '\t');
			if ( null == words || ( offset + 3) > words.length)
				return false;

			double v;
			try {
				v = Double.parseDouble( words[ offset + 2]);
			} catch (NumberFormatException e) {
				return false;
			}

			return ( v >= value);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns true if the value, which is not more than the specified value, is found.
	 * @param position the offset of the specified line
	 * @param offset the index of the compared value in the specified line
	 * @param value the specified value
	 * @return true if the value, which is not more than the specified value, is found
	 */
	public boolean retrieve_less_than(long position, int offset, double value) {
		try {
			_fileChannel.position( position);

			String line = FileUtility.readLine( _randomAccessFile, _encoding);
			if ( null == line || 0 == line.length())
				return false;

			String[] words = Tool.split( line, '\t');
			if ( null == words || ( offset + 3) > words.length)
				return false;

			double v;
			try {
				v = Double.parseDouble( words[ offset + 2]);
			} catch (NumberFormatException e) {
				return false;
			}

			return ( v <= value);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns true if the value, which is not less than the value0 and not more than value1, is found.
	 * @param position the offset of the specified line
	 * @param offset the index of the compared value in the specified line
	 * @param value0 the specified value
	 * @param value1 the specified value
	 * @return
	 */
	public boolean retrieve_more_than_less_than(long position, int offset, double value0, double value1) {
		try {
			_fileChannel.position( position);

			String line = FileUtility.readLine( _randomAccessFile, _encoding);
			if ( null == line || 0 == line.length())
				return false;

			String[] words = Tool.split( line, '\t');
			if ( null == words || ( offset + 3) > words.length)
				return false;

			double v;
			try {
				v = Double.parseDouble( words[ offset + 2]);
			} catch (NumberFormatException e) {
				return false;
			}

			return ( v >= value0 && v <= value1);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns true for writing the log file information successfully.
	 * @param filename the log file name
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing the log file information successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(String filename, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "filename", "", Writer.escapeAttributeCharData( filename));
		attributesImpl.addAttribute( null, null, "type", "", Writer.escapeAttributeCharData( _type));
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		attributesImpl.addAttribute( null, null, "header", "", String.valueOf( _header));
		writer.writeElement( null, null, "file", attributesImpl);
		return true;
	}

	/**
	 * Returns true for generating the log file successfully.
	 * @param directory the directory in which the log file is generated
	 * @param map the LogObject hashtable(object - LogObject)
	 * @param objectBases the array of the objects
	 * @param steps the array for the step strings of the log file
	 * @return true for generating the log file successfully
	 */
	public boolean generate(File directory, Map map, ObjectBase[] objectBases, List steps) {
		int counter = 0;
		for ( int i = 0; i < objectBases.length; ++i) {
			LogObject logObject = ( LogObject)map.get( objectBases[ i]);
			String value = ( String)logObject.get( 0);
			if ( value.equals( "NULL"))
				break;

			++counter;
		}

		boolean footer = ( objectBases.length != counter);

		List names = new ArrayList();

		String id = "\t$ID";
		String name = "\t$Name";
		for ( int i = 0; i < counter; ++i) {
			id += ( "\t" + String.valueOf( i));
			name += ( "\t" + objectBases[ i]._name);
			names.add( objectBases[ i]._name);
		}

		id += "\n";
		name += "\n";

		_file = new File( directory, _name + "." + _extension);

		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile( _file, "rw");
			randomAccessFile.write( id.getBytes( _encoding));
			_header = randomAccessFile.getFilePointer();
			randomAccessFile.write( name.getBytes( _encoding));
			for ( int i = 0; i < steps.size(); ++i) {
				String line = ( ( 0 == i) ? ( "\t" + _name) : ( ( String)steps.get( i) + "\t" + String.valueOf( i - 1)));
				if ( _type.equals( "agents") && _name.equals( "$Spot")) {
					Map counter_map = new HashMap();
					for ( int j = 0; j < objectBases.length; ++j) {
						String value = get_value( map, objectBases[ j], i);
						if ( null == value)
							break;

						line += ( "\t" + value);

						IntBuffer intBuffer = ( IntBuffer)counter_map.get( value);
						if ( null != intBuffer) {
							intBuffer.put( 0, intBuffer.get( 0) + 1);
							_max = Math.max( _max, intBuffer.get( 0));
						} else {
							intBuffer = IntBuffer.allocate( 1);
							intBuffer.put( 0, 1);
							counter_map.put( value, intBuffer);
						}
					}
				} else {
					for ( int j = 0; j < objectBases.length; ++j) {
						String value = get_value( map, objectBases[ j], i);
						if ( null == value)
							break;

						line += ( "\t" + value);
					}
				}
				line += "\n";
				randomAccessFile.write( line.getBytes( _encoding));
			}

			if ( footer) {
				names.clear();
				id = "\t$ID";
				name = "\t$Name";
				for ( int i = 0; i < objectBases.length; ++i) {
					LogObject logObject = ( LogObject)map.get( objectBases[ i]);
					if ( 0 > logObject._start)
						break;

					id += ( "\t" + String.valueOf( i));
					name += ( "\t" + objectBases[ i]._name);
					names.add( objectBases[ i]._name);
				}

				id += "\n";
				name += "\n";

				randomAccessFile.write( id.getBytes( _encoding));
				_header = randomAccessFile.getFilePointer();
				randomAccessFile.write( name.getBytes( _encoding));
			}

			_names = ( String[])names.toArray( new String[ 0]);

			randomAccessFile.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param map
	 * @param objectBase
	 * @param objectBase
	 * @return
	 */
	private String get_value(Map map, ObjectBase objectBase, int index) {
		LogObject logObject = ( LogObject)map.get( objectBase);
		String value = ( String)logObject.get( index);
		return value.equals( "NULL") ? null : value;
	}
}
