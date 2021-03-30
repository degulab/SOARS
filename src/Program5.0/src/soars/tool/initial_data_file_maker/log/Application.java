/**
 * 
 */
package soars.tool.initial_data_file_maker.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;

import soars.common.utility.tool.jackson.JacksonUtility;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	private static Map<String, String> _commandMap = null;

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null != _commandMap)
				return;

			_commandMap = ( Map<String, String>)JacksonUtility.get_object( new File( "commandmap.json"), new TypeReference<Map<String, String>>(){});
		}
	}

	/**
	 * @param logFolder
	 * @param encoding 
	 * @return
	 */
	private static boolean execute(String logFolderPath, String encoding) {
		if ( null == _commandMap)
			return false;

		File logFolder = new File( logFolderPath);
		if ( !logFolder.exists())
			return false;

		File file = new File( logFolder, "log.json");
		try {
			Map<String, Map<String, String>> logMap = (Map<String, Map<String, String>>)JacksonUtility.get_object( file, new TypeReference<Map<String, Map<String, String>>>(){});
			if ( null == logMap)
				return false;

			String[] entitieTypes = new String[] { "agent", "spot"};
			File[] folders = new File[] { new File( logFolder, "agents"), new File( logFolder, "spots")};
			for ( int i = 0; i < entitieTypes.length; ++i) {
				Map<String, String> map = logMap.get( entitieTypes[ i]);
				if ( null == map)
					continue;

				Set<String> names = map.keySet();
				for ( String name:names) {
					System.out.println( name + " - " + map.get( name));
					if ( name.equals( "$Spot"))
						continue;

					String type = map.get( name);
					if ( null == type || !_commandMap.keySet().contains( type))
						continue;

					File logFile = new File( folders[ i], name + ".log");

					BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( logFile)));

					// １行目
					String line = bufferedReader.readLine();
					if ( null == line)
						return false;

					if ( !line.startsWith( "\t$ID"))
						return false;

					// ２行目
					String[] entities = get_entities( bufferedReader);
					if ( null == entities)
						return false;

					// ３行目
					String[] initialValues = get_initialValues( bufferedReader, name);
					if ( null == initialValues || entities.length < initialValues.length)
						return false;

					// ４行目
					line = bufferedReader.readLine();
					if ( null == line) {
						// ３行目までしかない場合
						if ( !write( type, name, entities, initialValues.length, initialValues, entitieTypes[ i] + "-" + name + ".csv", logFolder, encoding))
							return false;
					} else {
						// ４行以上の場合
						String[] values = get_values( logFile);
						if ( null == values)
							return false;

						if ( !write( type, name, entities, initialValues.length, values, entitieTypes[ i] + "-" + name + ".csv", logFolder, encoding))
							return false;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param bufferedReader
	 * @return
	 * @throws IOException 
	 */
	private static String[] get_entities(BufferedReader bufferedReader) throws IOException {
		String line = bufferedReader.readLine();
		if ( null == line)
			return null;

		if ( !line.startsWith( "\t$Name"))
			return null;

		System.out.println( line);
		String[] words = line.split( "\t");
		return ( null != words && 3 <= words.length) ? words : null;
	}

	/**
	 * @param bufferedReader
	 * @param name
	 * @return
	 * @throws IOException 
	 */
	private static String[] get_initialValues(BufferedReader bufferedReader, String name) throws IOException {
		String line = bufferedReader.readLine();
		if ( null == line)
			return null;

		if ( !line.startsWith( "\t" + name))
			return null;

		System.out.println( line);
		String[] words = line.split( "\t");
		return ( null != words && 3 <= words.length) ? words : null;
	}

	/**
	 * @param logFile
	 * @return
	 * @throws IOException 
	 */
	private static String[] get_values(File logFile) throws IOException {
		RandomAccessFile randomAccessFile = new RandomAccessFile( logFile, "r");
		FileChannel fileChannel = randomAccessFile.getChannel();
		List<Byte> list = new ArrayList<>();
		long size = fileChannel.size();
		String line = "";
		String[] words = null;
		for ( long pos = size - 1; 0 <= pos; --pos) {
			fileChannel.position( pos);
			byte c = randomAccessFile.readByte();
			switch ( c) {
				case '\n':
				case '\r':
					if ( !list.isEmpty()) {
						line = get( list);
						words = line.split( "\t");
						if ( null != words && !words[ 0].equals( "") && 3 <= words.length)
							return words;
						else
							list.clear();
					}
					break;
				default:
					list.add( c);
					break;
			}
		}
		return null;
	}

	/**
	 * @param list
	 * @return
	 */
	private static String get(List<Byte> list) {
		byte[] bytes = new byte[ list.size()];
		for( int i = list.size() - 1, j = 0; i >= 0; --i, ++j)
			bytes[ j] = list.get( i);
		return new String( bytes);
	}

	/**
	 * @param type 
	 * @param name 
	 * @param entities
	 * @param length
	 * @param values
	 * @param filename
	 * @param logFolder
	 * @param encoding 
	 * @return
	 * @throws IOException 
	 */
	private static boolean write(String type, String name, String[] entities, int length, String[] values, String filename, File logFolder, String encoding) throws IOException {
		String command = _commandMap.get( type);
		if ( null == command)
			return false;

		File file = new File( logFolder, filename);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), encoding);
		outputStreamWriter.write( "\t" + command + "\n");
		outputStreamWriter.write( "\t" + name + "\n");
		for ( int i = 2; i < length; ++i)
			outputStreamWriter.write( entities[ i] + "\t" + values[ i] + "\n");
		outputStreamWriter.flush();
		outputStreamWriter.close();
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		execute( args[ 0], args[ 1]);
	}
}
