/**
 * 
 */
package soars.tool.initial_data_file_maker.database;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import soars.common.utility.tool.jackson.JacksonUtility;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * @param databaseFile
	 * @param propertyFile
	 * @return
	 */
	private static boolean execute(String databaseFile, String propertyFile) {
		String url = "jdbc:sqlite:" + databaseFile;
//	String user = "";
//	String password = "";

		File file = new File( propertyFile);
		if ( !file.exists())
			return false;

		List<InitialData> list = ( List<InitialData>)JacksonUtility.get_object( file, new TypeReference< List<InitialData>>(){});

		int index = 1;
		for ( InitialData initialData:list) {
			if ( !initialData.write( url, index++, file.getParentFile()))
				return false;
		}

		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		execute( args[ 0], args[ 1]);
	}
}
