/**
 * 
 */
package soars.application.visualshell.executor.common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.log.LogManager;
import soars.application.visualshell.object.log.LogOption;
import soars.common.utility.tool.jackson.JacksonUtility;

/**
 * @author kurata
 *
 */
public class LogInformationFileMaker {

	/**
	 * @param logFolderPath
	 * @return
	 */
	public static boolean make(String logFolderPath) {
		Map<String, Map<String, String>> logMap = new HashMap<>();
		Set<String> entities = LogManager.get_instance().keySet();
		for ( String entity:entities) {
			Set<String> types = LogManager.get_instance().get( entity).keySet();
			for ( String type:types) {
				for ( LogOption logOption:LogManager.get_instance().get( entity).get( type)) {
					if ( !logOption._flag)
						continue;

					Map<String, String> map = logMap.get( entity);
					if ( null == map) {
						map = new HashMap<>();
						logMap.put( entity, map);
					}

					map.put( logOption._name, get( entity, type, logOption._name));
				}
			}
		}

		File logFolder = new File( logFolderPath);
		logFolder.mkdirs();
		File file = new File( logFolder, "log.json");
		return JacksonUtility.write_json( file, logMap);
	}

	/**
	 * @param entity
	 * @param type
	 * @param name
	 * @return
	 */
	private static String get(String entity, String type, String name) {
		return !type.equals( "number object") ? type
			: ( entity.equals( "agent") ? LayerManager.get_instance().get_agent_number_object_type( name) : LayerManager.get_instance().get_spot_number_object_type( name));
	}
}
