/**
 * 
 */
package soars.application.animator.file.importer;

import java.awt.Graphics2D;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import soars.application.animator.common.tool.TimeComparator;
import soars.application.animator.object.file.FileObject;
import soars.application.animator.object.file.HeaderObject;
import soars.application.animator.object.player.ObjectManager;
import soars.application.animator.object.player.agent.AgentObjectManager;
import soars.application.animator.object.player.spot.SpotObjectManager;
import soars.application.animator.object.scenario.ScenarioManager;
import soars.common.utility.tool.file.FileUtility;

/**
 * Makes Animator data from SOARS log files.
 * @author kurata / SOARS project
 */
public class Importer {

	/**
	 * Names of directories for SOARS log files.
	 */
	static private final String[] _sub_directory_names = new String[] { "agents", "spots"};

	/**
	 * Informations of SOARS log files.
	 */
	public HeaderObject _headerObject = null; 

	/**
	 * Array for the step strings of SOARS log.
	 */
	private String[] _steps = null;

	/**
	 * Returns the root directory for Animator data if it is created and all of SOARS log files are copied to it successfully.
	 * @param import_directory the source directory which contains all of SOARS log files
	 * @param parent_directory the directory in which the root directory is created
	 * @param graphic_properties_filename
	 * @param chart_properties_filename
	 * @param chart_log_directory
	 * @return the root directory for Animator data if it is created and all of SOARS log files are copied to it successfully
	 */
	public static File copy(File import_directory, File parent_directory, String graphic_properties_filename, String chart_properties_filename, String chart_log_directory) {
		File root_directory = new File( parent_directory, ObjectManager._rootDirectoryName);
		if ( null == root_directory)
			return null;

		if ( !root_directory.mkdir())
			return null;

		if ( !copy( import_directory, graphic_properties_filename, root_directory))
			return null;

		if ( !copy( import_directory, chart_properties_filename, root_directory))
			return null;

		boolean empty = true;

		for ( int i = 0; i < _sub_directory_names.length; ++i) {
			File src_directory = new File( import_directory, _sub_directory_names[ i]);
			if ( null == src_directory || !src_directory.exists() || !src_directory.isDirectory())
				continue;

			File[] files = src_directory.listFiles();
			if ( null == files || 0 == files.length)
				continue;

			File dest_directory = new File( root_directory, _sub_directory_names[ i]);
			if ( !dest_directory.mkdir())
				return null;

			for ( int j = 0; j < files.length; ++j) {
				if ( !files[ j].isFile() || !files[ j].canRead() || !files[ j].getName().endsWith( FileObject._extension))
					continue;

				if ( !FileUtility.copy( files[ j], new File( dest_directory, files[ j].getName())))
					return null;

				empty = false;
			}
		}

		if ( !empty && !copy( new File( import_directory, chart_log_directory), new File( root_directory, chart_log_directory)))
			return null;

		return ( empty ? null : root_directory);
	}

	/**
	 * @param import_directory
	 * @param properties_filename
	 * @param root_directory
	 * @return
	 */
	private static boolean copy(File import_directory, String properties_filename, File root_directory) {
		File file = new File( import_directory, properties_filename);
		if ( null != file && file.exists() && file.canRead()) {
			if ( !FileUtility.copy( file, new File( root_directory, properties_filename)))
				return false;
		}
		return true;
	}

	/**
	 * @param src_directory
	 * @param dest_directory
	 * @return
	 */
	private static boolean copy(File src_directory, File dest_directory) {
		if ( null == src_directory || !src_directory.exists() || !src_directory.isDirectory())
			return true;

		File[] files = src_directory.listFiles();
		if ( null == files || 0 == files.length)
			return true;

		if ( !dest_directory.mkdir())
			return false;

		for ( int j = 0; j < files.length; ++j) {
			if ( !files[ j].isFile() || !files[ j].canRead() || !files[ j].getName().endsWith( "log"))
				continue;

			if ( !FileUtility.copy( files[ j], new File( dest_directory, files[ j].getName())))
				return false;
		}

		return true;
	}

	/**
	 * Constructs a Importer that is initialized with the root directory for Animator data.
	 * @param directory the root directory for Animator data
	 */
	public Importer(File directory) {
		super();
		_headerObject = new HeaderObject();
		_headerObject._directory = directory;
	}

	/**
	 * Constructs a Importer that is initialized with the informations of SOARS log files, the root directory for Animator data, and the array for the step strings of SOARS log.
	 * @param headerObject the informations of SOARS log files
	 * @param directory the root directory for Animator data
	 * @param steps the array for the step strings of SOARS log
	 */
	public Importer(HeaderObject headerObject, File directory, String[] steps) {
		super();
		_headerObject = headerObject;
		_headerObject._directory = directory;
		_steps = steps;
	}

	/**
	 * Returns true if Animator data is created from SOARS log files successfully.
	 * @param graphics2D the graphics object of JAVA
	 * @return true if Animator data is created from SOARS log files successfully
	 */
	public boolean execute(Graphics2D graphics2D) {
		if ( !read( graphics2D))
			return false;

		if ( !make_indices_file())
			return false;

		_headerObject.set( _steps);

		if ( !ScenarioManager.get_instance().load( _headerObject))
			return false;

		SpotObjectManager.get_instance().arrange();
		AgentObjectManager.get_instance().arrange();

		return true;
	}

	/**
	 * Returns true if Animator data is created from SOARS log files successfully.
	 * @return true if Animator data is created from SOARS log files successfully
	 */
	public boolean execute() {
		if ( !make_indices_file())
			return false;

		_headerObject.set( _steps);

		if ( !ScenarioManager.get_instance().load( _headerObject))
			return false;

		AgentObjectManager.get_instance().arrange();

		return true;
	}

	/**
	 * Returns true if the array for the step strings of SOARS log scraped successfully.
	 * @param graphics2D the graphics object of JAVA
	 * @return true if the array for the step strings of SOARS log scraped successfully
	 */
	private boolean read(Graphics2D graphics2D) {
		List list = new ArrayList();

		for ( int i = 0; i < _sub_directory_names.length; ++i) {
			File file = new File( _headerObject._directory, _sub_directory_names[ i]);
			if ( null != file) {
				File[] files = file.listFiles();
				if ( null != files) {
					for ( int j = 0; j < files.length; ++j) {
						if ( !files[ j].isFile() || !files[ j].canRead())
							continue;

						FileObject fileObject = new FileObject( _sub_directory_names[ i], files[ j]);
						if ( !fileObject.read( list, graphics2D))
							return false;

						if ( fileObject._type.equals( "agents") && fileObject._name.equals( "$Spot"))
							_headerObject._max = fileObject._max;

						_headerObject._fileObjectMap.put( _sub_directory_names[ i] + "/" + files[ j].getName(), fileObject);
					}
				}
			}
		}

		if ( list.isEmpty())
			_steps = new String[] { ""};
		else {
			String[] temporary_steps = ( String[])list.toArray( new String[ 0]);
			Arrays.sort( temporary_steps, new TimeComparator());
			_steps = new String[ temporary_steps.length + 1];
			_steps[ 0] = "";
			System.arraycopy( temporary_steps, 0, _steps, 1, temporary_steps.length);
		}

		_headerObject.set_last_time( _steps[ _steps.length - 1]);
//		_headerObject._last = _steps[ _steps.length - 1];

		return true;
	}

	/**
	 * Returns true if the file that consists of the indices of SOARS log files is created successfully.
	 * @return true if the file that consists of the indices of SOARS log files is created successfully
	 */
	private boolean make_indices_file() {
		_headerObject._filenames = ( String[])_headerObject._fileObjectMap.keySet().toArray( new String[ 0]);

		if ( !open()) {
			close();
			return false;
		}

		try {
			_headerObject._indices_file = new File( _headerObject._directory.getAbsolutePath() + "/" + HeaderObject._indices_filename);
			DataOutputStream dataOutputStream = new DataOutputStream( new FileOutputStream( _headerObject._indices_file));
			for ( int i = 0; i < _steps.length; ++i) {
				if ( !write( _steps[ i], dataOutputStream)) {
					dataOutputStream.flush();
					dataOutputStream.close();
					close();
					return false;
				}
			}

			dataOutputStream.flush();
			dataOutputStream.close();
		} catch (IOException e) {
			close();
			e.printStackTrace();
			return false;
		}

		close();

		return true;
	}

	/**
	 * Returns true if all of SOARS log files are opened successfully.
	 * @return true if all of SOARS log files are opened successfully
	 */
	private boolean open() {
		for ( int i = 0; i < _headerObject._filenames.length; ++i) {
			FileObject fileObject = ( FileObject)_headerObject._fileObjectMap.get( _headerObject._filenames[ i]);
			if ( null == fileObject)
				return false;

			if ( !fileObject.open())
				return false;
		}
		return true;
	}

	/**
	 * Closes all of SOARS log files.
	 */
	private void close() {
		for ( int i = 0; i < _headerObject._filenames.length; ++i) {
			FileObject fileObject = ( FileObject)_headerObject._fileObjectMap.get( _headerObject._filenames[ i]);
			if ( null == fileObject)
				continue;

			fileObject.close();
		}
	}

	/**
	 * Returns true if the indices of SOARS log files, which correspond to the the specified step string, is written to the specified stream successfully.
	 * @param stepthe specified step string
	 * @param dataOutputStream the specified stream
	 * @return true if the indices of SOARS log files, which correspond to the the specified step string, is written to the specified stream successfully
	 */
	private boolean write(String step, DataOutputStream dataOutputStream) {
		try {
			if ( step.equals( "")) {
				dataOutputStream.writeLong( -1l);
				dataOutputStream.writeInt( -1);
				dataOutputStream.writeInt( -1);
			} else {
				String[] words = step.split( "[/:]");
				if ( 3 > words.length)
					return false;

				dataOutputStream.writeLong( Long.parseLong( words[ 0]));
				dataOutputStream.writeInt( Integer.parseInt( words[ 1]));
				dataOutputStream.writeInt( Integer.parseInt( words[ 2]));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		String line = step;
		for ( int i = 0; i < _headerObject._filenames.length; ++i) {
			FileObject fileObject = ( FileObject)_headerObject._fileObjectMap.get( _headerObject._filenames[ i]);
			if ( null == fileObject)
				return false;

			if ( !fileObject.write( step, dataOutputStream))
				return false;
		}
		return true;
	}

//	/**
//	 * Name of the output file for the debug.
//	 */
//	static public final String indices_debug_filename = "indices.debug.txt";
//
//	/**
//	 * @return
//	 */
//	public boolean debug() {
//		try {
//			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( new File( _headerObject._directory.getAbsolutePath() + "/" + indices_debug_filename)), "UTF-8");
//
//			outputStreamWriter.write( get_header());
//
//			RandomAccessFile randomAccessFile = new RandomAccessFile( new File( _headerObject._directory.getAbsolutePath() + "/" + HeaderObject._indices_filename), "r");
//			FileChannel fileChannel = randomAccessFile.getChannel();
//
//			for ( int i = 0; i < _steps.length; ++i) {
//				outputStreamWriter.write( String.valueOf( randomAccessFile.readLong()));
//				outputStreamWriter.write( "/");
//				outputStreamWriter.write( String.format( "%02d", randomAccessFile.readInt()));
//				outputStreamWriter.write( ":");
//				outputStreamWriter.write( String.format( "%02d", randomAccessFile.readInt()));
//				for ( int j = 0; j < _headerObject._filenames.length; ++j) {
//					outputStreamWriter.write( "\t");
//					outputStreamWriter.write( String.valueOf( randomAccessFile.readLong()));
//				}
//				outputStreamWriter.write( "\n");
//			}
//
//			randomAccessFile.close();
//			outputStreamWriter.flush();
//			outputStreamWriter.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return false;
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			return false;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * @return
//	 */
//	private String get_header() {
//		String line = "";
//
//		for ( int i = 0; i < _headerObject._filenames.length; ++i)
//			line += ( "\t" + _headerObject._filenames[ i]);
//
//		return ( line += "\n");
//	}
}
