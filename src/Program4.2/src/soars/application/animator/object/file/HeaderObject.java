/**
 * 
 */
package soars.application.animator.object.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.common.tool.CommonTool;
import soars.application.animator.object.player.agent.AgentObject;
import soars.application.animator.object.player.base.ObjectBase;
import soars.application.animator.object.player.spot.ISpotObjectManipulator;
import soars.application.animator.object.transition.agent.AgentTransitionManager;
import soars.application.animator.object.transition.spot.SpotTransitionManager;
import soars.common.utility.xml.sax.Writer;

/**
 * The informations of SOARS log files.
 * @author kurata / SOARS project
 */
public class HeaderObject {

	/**
	 * Indices file name.
	 */
	static public final String _indices_filename = "indices.ndx";

	/**
	 * Root directory for Animator data.
	 */
	public File _directory = null;

	/**
	 * Indices file.
	 */
	public File _indices_file = null;

	/**
	 * Log file names.
	 */
	public String[] _filenames = null;

	/**
	 * FileObject hashtable(file name - FileObject)
	 */
	public Map _fileObjectMap = new TreeMap();

	/**
	 * The number of steps.
	 */
	public int _size = 0;

	/**
	 * Length of a record.
	 */
	public int _length = 0;

	/**
	 * Max number of agents.
	 */
	public int _max = 1;

	/**
	 * Last step string.
	 */
	public String _last = "";

	/**
	 * Last step string.
	 */
	public double _last_time = 0.0f;

	/**
	 * Returns true if the log files are opened successfully.
	 * @return true if the log files are opened successfully
	 */
	private boolean open() {
		for ( int i = 0; i < _filenames.length; ++i) {
			FileObject fileObject = ( FileObject)_fileObjectMap.get( _filenames[ i]);
			if ( null == fileObject)
				return false;

			if ( !fileObject.open())
				return false;
		}
		return true;
	}

	/**
	 * Closes the log files.
	 */
	private void close() {
		for ( int i = 0; i < _filenames.length; ++i) {
			FileObject fileObject = ( FileObject)_fileObjectMap.get( _filenames[ i]);
			if ( null == fileObject)
				continue;

			fileObject.close();
		}
	}

	/**
	 * @param last
	 */
	public void set_last_time(String last) {
		_last = last;
		_last_time = CommonTool.time_to_double( _last);
}

	/**
	 * Sets the number of steps and the length of a record
	 * @param steps the array for the step strings of the log file
	 */
	public void set(String[] steps) {
		_size = steps.length;
		_length = ( 8 + 4 + 4 + ( 8 * _filenames.length));
	}

	/**
	 * Returns true for loading the values successfully.
	 * @return true for loading the values successfully
	 */
	public synchronized boolean load() {
		if ( !open()) {
			close();
			return false;
		}

		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile( _indices_file, "r");
			FileChannel fileChannel = randomAccessFile.getChannel();

			fileChannel.position( 0l);

			long day = randomAccessFile.readLong();
			int hour = randomAccessFile.readInt();
			int min = randomAccessFile.readInt();
			if ( -1l != day) {
				randomAccessFile.close();
				close();
				return false;
			}

			for ( int i = 0; i < _filenames.length; ++i) {
				FileObject fileObject = ( FileObject)_fileObjectMap.get( _filenames[ i]);
				if ( !fileObject.load( randomAccessFile.readLong())) {
					randomAccessFile.close();
					close();
					return false;
				}
			}

			randomAccessFile.close();
		} catch (FileNotFoundException e) {
			close();
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			close();
			e.printStackTrace();
			return false;
		}

		close();

		return true;
	}

	/**
	 * Returns true for reading the values successfully.
	 * @param position the start position
	 * @param size the size of record
	 * @param times the array for the step strings
	 * @param agentTransitionManager the scenario data manager of agents
	 * @param spotTransitionManager the scenario data manager of spots
	 * @return true for reading the values successfully
	 */
	public synchronized boolean read(int position, int size, Vector times, AgentTransitionManager agentTransitionManager, SpotTransitionManager spotTransitionManager) {
		if ( !open()) {
			close();
			return false;
		}

		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile( _indices_file, "r");
			FileChannel fileChannel = randomAccessFile.getChannel();

			fileChannel.position( ( long)position * _length);

			for ( int i = 0; i < size; ++i) {
				agentTransitionManager.append();
				spotTransitionManager.append();
				long day = randomAccessFile.readLong();
				int hour = randomAccessFile.readInt();
				int min = randomAccessFile.readInt();
				times.add( ( -1l == day) ? "" : ( String.valueOf( day) + "/" + String.format( "%02d", hour) + ":" + String.format( "%02d", min)));
				for ( int j = 0; j < _filenames.length; ++j) {
					FileObject fileObject = ( FileObject)_fileObjectMap.get( _filenames[ j]);
					if ( !fileObject.read( i, randomAccessFile.readLong(), agentTransitionManager, spotTransitionManager)) {
						randomAccessFile.close();
						close();
						return false;
					}
				}
			}

			randomAccessFile.close();
		} catch (FileNotFoundException e) {
			close();
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			close();
			e.printStackTrace();
			return false;
		}

		close();

		return true;
	}

	/**
	 * Returns true if the specified value is found backward.
	 * @param objectBase the specified object
	 * @param name the specified name
	 * @param value the specified value
	 * @param start_position the start position
	 * @return true if the specified value is found backward
	 */
	public int retrieve_backward(ObjectBase objectBase, String name, String value, int start_position) {
		if ( 0 > start_position)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( objectBase, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_backward( value, start_position);
	}

	/**
	 * Returns true if the value, which is not less than the specified value, is found backward.
	 * @param objectBase the specified object
	 * @param name the specified name
	 * @param value the specified value
	 * @param start_position the start position
	 * @return true if the value, which is not less than the specified value, is found backward
	 */
	public int retrieve_backward_more_than(ObjectBase objectBase, String name, double value, int start_position) {
		if ( 0 > start_position)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( objectBase, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_backward_more_than( value, start_position);
	}

	/**
	 * Returns true if the value, which is not more than the specified value, is found backward.
	 * @param objectBase the specified object
	 * @param name the specified name
	 * @param value the specified value
	 * @param start_position the start position
	 * @return true if the value, which is not more than the specified value, is found backward
	 */
	public int retrieve_backward_less_than(ObjectBase objectBase, String name, double value, int start_position) {
		if ( 0 > start_position)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( objectBase, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_backward_less_than( value, start_position);
	}

	/**
	 * Returns true if the value, which is not less than the value0 and not more than value1, is found backward.
	 * @param objectBase the specified object
	 * @param name the specified name
	 * @param value0 the specified value
	 * @param value1 the specified value
	 * @param start_position the start position
	 * @return true if the value, which is not less than the value0 and not more than value1, is found backward
	 */
	public int retrieve_backward_more_than_less_than(ObjectBase objectBase, String name, double value0, double value1, int start_position) {
		if ( 0 > start_position)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( objectBase, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_backward_more_than_less_than( value0, value1, start_position);
	}

	/**
	 * Returns true if the specified value is found forward.
	 * @param objectBase the specified object
	 * @param name the specified name
	 * @param value the specified value
	 * @param start_position the start position
	 * @return true if the specified value is found forward
	 */
	public int retrieve_forward(ObjectBase objectBase, String name, String value, int start_position) {
		if ( _size <= start_position)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( objectBase, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_forward( value, start_position);
	}

	/**
	 * Returns true if the value, which is not less than the specified value, is found forward.
	 * @param objectBase the specified object
	 * @param name the specified name
	 * @param value the specified value
	 * @param start_position the start position
	 * @return true if the value, which is not less than the specified value, is found forward
	 */
	public int retrieve_forward_more_than(ObjectBase objectBase, String name, double value, int start_position) {
		if ( _size <= start_position)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( objectBase, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_forward_more_than( value, start_position);
	}

	/**
	 * Returns true if the value, which is not more than the specified value, is found forward.
	 * @param objectBase the specified object
	 * @param name the specified name
	 * @param value the specified value
	 * @param start_position the start position
	 * @return true if the value, which is not more than the specified value, is found forward
	 */
	public int retrieve_forward_less_than(ObjectBase objectBase, String name, double value, int start_position) {
		if ( _size <= start_position)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( objectBase, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_forward_less_than( value, start_position);
	}

	/**
	 * Returns true if the value, which is not less than the value0 and not more than value1, is found forward.
	 * @param objectBase the specified object
	 * @param name the specified name
	 * @param value0 the specified value
	 * @param value1 the specified value
	 * @param start_position the start position
	 * @return true if the value, which is not less than the value0 and not more than value1, is found forward
	 */
	public int retrieve_forward_more_than_less_than(ObjectBase objectBase, String name, double value0, double value1, int start_position) {
		if ( _size <= start_position)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( objectBase, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_forward_more_than_less_than( value0, value1, start_position);
	}

	/**
	 * @param objectBase
	 * @param name
	 * @return
	 */
	private RetrieveObject create_retrieveObject(ObjectBase objectBase, String name) {
		String filename = get( objectBase, name);
		if ( null == filename)
			return null;

		FileObject fileObject = ( FileObject)_fileObjectMap.get( filename);
		if ( null == fileObject)
			return null;

		int index = get( filename);
		if ( 0 > index)
			return null;

		int offset = fileObject.get( objectBase._name);
		if ( 0 > offset)
			return null;

		return new RetrieveObject( index, fileObject, offset, this);
	}

	/**
	 * @param objectBase
	 * @param name
	 * @return
	 */
	private String get(ObjectBase objectBase, String name) {
		String prefix = "";
		if ( objectBase instanceof AgentObject)
			prefix += "agents/";
		else if ( objectBase instanceof ISpotObjectManipulator)
			prefix += "spots/";
		else
			return null;

		return ( prefix + name + "." + FileObject._extension);
	}

	/**
	 * @param filename
	 * @return
	 */
	private int get(String filename) {
		for ( int i = 0; i < _filenames.length; ++i) {
			if ( _filenames[ i].equals( filename))
				return i;
		}
		return -1;
	}

	/**
	 * Returns true for writing the informations successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing the informations successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "size", "", String.valueOf( _size));
		attributesImpl.addAttribute( null, null, "length", "", String.valueOf( _length));
		attributesImpl.addAttribute( null, null, "max", "", String.valueOf( _max));
		attributesImpl.addAttribute( null, null, "last", "", Writer.escapeAttributeCharData( _last));

		writer.startElement( null, null, "header", attributesImpl);

		for ( int i = 0; i < _filenames.length; ++i) {
			FileObject fileObject = ( FileObject)_fileObjectMap.get( _filenames[ i]);
			if ( null == fileObject)
				return false;

			if ( !fileObject.write( _filenames[ i], writer))
				return false;
		}

		writer.endElement( null, null, "header");

		return true;
	}
}
