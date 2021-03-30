/*
 * 2005/05/31
 */
package soars.application.visualshell.object.log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.exporter.java.JavaProgramExporter;
import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
import soars.application.visualshell.file.loader.SaxLoader;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.common.utility.xml.sax.Writer;

/**
 * Manages the all log option managers for Visual Shell.
 * @author kurata / SOARS project
 */
public class LogManager extends HashMap<String, Map<String, LogOptionManager>> {

	/**
	 * Names of all object types.
	 */
	static public final String[] _kinds = new String[] {
		"keyword",
		"number object",
		"probability",
		"time variable",
		"role variable",
		"collection",
		"list",
		"map",
		"spot variable",
		//"class variable",
		//"file",
		"exchange algebra"
	};

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private LogManager _logManager = null;

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
			if ( null == _logManager) {
				_logManager = new LogManager();
				if ( !_logManager.initialize())
					System.exit( 1);
			}
		}
	}

	/**
	 * Returns the instance of this object.
	 * @return the instance of this object
	 */
	public static LogManager get_instance() {
		if ( null == _logManager) {
			System.exit( 0);
		}

		return _logManager;
	}

	/**
	 * Creates this object.
	 */
	private LogManager() {
		super();
	}

	/**
	 * @return
	 */
	private boolean initialize() {
		String[] types = new String[] { "agent", "spot"};
		for ( String type:types) {
			Map<String, LogOptionManager> map = new HashMap<String, LogOptionManager>();
			for ( String kind:_kinds)
				map.put( kind, new LogOptionManager( type, kind));
			put( type, map);
		}

		get( "agent").get( "keyword").add( new LogOption( "$Name", false));
		get( "agent").get( "keyword").add( new LogOption( "$Role", false));
		get( "agent").get( "keyword").add( new LogOption( "$Spot", true));

		return true;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		clear();
		initialize();
	}

	/**
	 * Returns the script for ModelBuilder script.
	 * @param work_directory_name the name of the directory for the log files
	 * @param to_display whether to display the logs
	 * @param to_file whether to make the log files
	 * @return the script for ModelBuilder script
	 */
	public String get_script_on_model_builder(String work_directory_name, boolean to_display, boolean to_file) {
		if ( !to_display && !to_file)
			return "";

		String script = "";
		script += get_script_on_model_builder( get( "agent"), "logAgents", "logAgentsToFile", "agents/", work_directory_name, to_display, to_file);
		script += get_script_on_model_builder( get( "spot"), "logSpots", "logSpotsToFile", "spots/", work_directory_name, to_display, to_file);

		return script;
	}

	/**
	 * @param map
	 * @param word0
	 * @param word1
	 * @param word2
	 * @param work_directory_name
	 * @param to_display
	 * @param to_file
	 * @return
	 */
	private String get_script_on_model_builder(Map<String, LogOptionManager> map, String word0, String word1, String word2, String work_directory_name, boolean to_display, boolean to_file) {
		String[] unit = new String[] { "", ""};
		for ( String kind:_kinds) {
			LogOptionManager logOptionManager = map.get( kind);
			if ( null == logOptionManager)
				continue;

			logOptionManager.get_script( unit, word0);
		}

		if ( unit[ 0].equals( "") || unit[ 1].equals( ""))
			return "";

		String script = "";
		if ( to_display && !to_file) {
			script += "itemData" + Constant._lineSeparator;
			script += ( unit[ 0] + Constant._lineSeparator);
			script += ( unit[ 1] + Constant._lineSeparator);
		} else if ( !to_display && to_file) {
			script += "itemData" + Constant._lineSeparator;
			script += ( "logMkdirs\t" + word1 + "\t" + unit[ 0] + Constant._lineSeparator);
			script += ( get_work_directory_name( work_directory_name) + word2 + "\t" + get_work_directory_name( work_directory_name) + word2 + "\t" + unit[ 1] + Constant._lineSeparator);
		} else {
			script += "itemData" + Constant._lineSeparator;
			script += ( unit[ 0] + "\t" + "logMkdirs\t" + word1 + "\t" + unit[ 0] + Constant._lineSeparator);
			script += ( unit[ 1] + "\t" + get_work_directory_name( work_directory_name) + word2 + "\t" + get_work_directory_name( work_directory_name) + word2 + "\t" + unit[ 1] + Constant._lineSeparator);
		}

		return ( script + Constant._lineSeparator);
	}

	/**
	 * @param work_directory
	 * @return
	 */
	private String get_work_directory_name(String work_directory_name) {
		return ( ( null == work_directory_name) ? "" : ( work_directory_name + "/"));
	}

	/**
	 * Returns the script for ModelBuilder script.
	 * @param work_directory the directory for the log files
	 * @return the script for ModelBuilder script
	 */
	public String get_script_on_animator(File work_directory) {
		boolean flag = get( "agent").get( "keyword").get_flag( "$Spot");
		get( "agent").get( "keyword").set_flag( "$Spot", true);

		String script = "";
		script += get_log_script_on_animator( get( "agent"), "logAgents", "logAgentsToFile", "agents/", work_directory);
		script += get_log_script_on_animator( get( "spot"), "logSpots", "logSpotsToFile", "spots/", work_directory);

		get( "agent").get( "keyword").set_flag( "$Spot", flag);

		return script;
	}

	/**
	 * @param map
	 * @param word0
	 * @param word1
	 * @param word2
	 * @param work_directory
	 * @return
	 */
	private String get_log_script_on_animator(Map<String, LogOptionManager> map, String word0, String word1, String word2, File work_directory) {
		String[] unit = new String[] { "", ""};
		for ( String kind:_kinds) {
			LogOptionManager logOptionManager = map.get( kind);
			if ( null == logOptionManager)
				continue;

			logOptionManager.get_script( unit, word0);
		}

		if ( unit[ 0].equals( "") || unit[ 1].equals( ""))
			return "";

		String script = "";
		script += "itemData" + Constant._lineSeparator;
		script += ( "logMkdirs\t" + word1 + "\t" + unit[ 0] + Constant._lineSeparator);
		script += ( get_work_directory( work_directory) + word2 + "\t" + get_work_directory( work_directory) + word2 + "\t" + unit[ 1] + Constant._lineSeparator);

		return ( script + Constant._lineSeparator);
	}

	/**
	 * @param work_directory
	 * @return
	 */
	private String get_work_directory(File work_directory) {
		return ( ( null == work_directory) ? "" : ( work_directory.getAbsolutePath() + "/"));
	}

	/**
	 * Returns the script for ModelBuilder script.
	 * @param sub_directory the name of directory for the script file on Grid
	 * @param log_directory the name of directory for the log files on Grid
	 * @return the script for ModelBuilder script
	 */
	public String get_script_on_grid(String sub_directory, String log_directory) {
		String script = "";
		script += get_log_script_on_grid( get( "agent"), "logAgents", "logAgentsToFile", "agents/", sub_directory, log_directory);
		script += get_log_script_on_grid( get( "spot"), "logSpots", "logSpotsToFile", "spots/", sub_directory, log_directory);
		return script;
	}

	/**
	 * @param map
	 * @param word0
	 * @param word1
	 * @param word2
	 * @param sub_directory
	 * @param log_directory
	 * @return
	 */
	private String get_log_script_on_grid(Map<String, LogOptionManager> map, String word0, String word1, String word2, String sub_directory, String log_directory) {
		String[] unit = new String[] { "", ""};
		for ( String kind:_kinds) {
			LogOptionManager logOptionManager = map.get( kind);
			if ( null == logOptionManager)
				continue;

			logOptionManager.get_script( unit, word0);
		}

		if ( unit[ 0].equals( "") || unit[ 1].equals( ""))
			return "";

		String script = "";
		script += "itemData" + Constant._lineSeparator;
		script += ( "logMkdirs\t" + word1 + "\t" + unit[ 0] + Constant._lineSeparator);
		script += ( log_directory + "/" + sub_directory + "/" + word2 + "\t" + log_directory + "/" + sub_directory + "/" + word2 + "\t" + unit[ 1] + Constant._lineSeparator);

		return ( script + Constant._lineSeparator);
	}

	/**
	 * Appends the specified LogOption to the appropriate LogManager.
	 * @param logOption the specified LogOption
	 * @param tag the tag
	 */
	public void append(LogOption logOption, String tag) {
		String[] kinds = SaxLoader._tagKindMap.get( tag);
		if ( null == kinds)
			return;

		LogOptionManager logOptionManager = get( kinds[ 0]).get( kinds[ 1]);
		if ( null == logOptionManager)
			return;

		logOptionManager.append( logOption);
	}

	/**
	 * @param type
	 * @param kind
	 */
	public void update(String type, String kind) {
		LogOptionManager logOptionManager = get( type).get( kind);
		if ( null == logOptionManager)
			return;

		logOptionManager.update();
	}

	/**
	 * Updates all log managers.
	 */
	public void update_all() {
		Collection<Map<String, LogOptionManager>> maps = values();
		for ( Map<String, LogOptionManager> map:maps) {
			Collection<LogOptionManager> logOptionManagers = map.values();
			for ( LogOptionManager logOptionManager:logOptionManagers)
				logOptionManager.update();
		}
	}

	/**
	 * @param type
	 * @param kind
	 * @param name
	 * @param newName
	 */
	public void update(String type, String kind, String name, String newName) {
		LogOptionManager logOptionManager = get( type).get( kind);
		if ( null == logOptionManager)
			return;

		logOptionManager.update( name, newName);
	}

	/**
	 * @param roleDataSet 
	 * @param agentNameMap 
	 * @param spotNameMap 
	 * @return
	 */
	public List<String> get_java_program(RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap) {
		// TODO Auto-generated method stub
		// type("agent" or "spot") - variable kind 
		List<String> logCodes = new ArrayList<>();

		List<String> codes1 = new ArrayList<>();
		codes1.add( "String[] activeKeys = { \"time\"");

		String code2 = "";

		String[] types = new String[] { "agent", "spot"};
		List<String> usedNames = new ArrayList<>();
		for ( String type:types) {
			Map<String, LogOptionManager> logOptionManagerMap = get( type);
			if ( null == logOptionManagerMap)
				continue;

			for ( String kind:_kinds) {
				LogOptionManager logOptionManager = ( LogOptionManager)logOptionManagerMap.get( kind);
				if ( !kind.equals( "keyword") && !kind.equals( "number object") && !kind.equals( "probability"))	// TODO とりあえず
						continue;

				if ( null == logOptionManager)
					continue;

				for ( LogOption logOption:logOptionManager) {
					if ( !logOption._flag)
						continue;

					List<PlayerBase> entities = new ArrayList<>();
					LayerManager.get_instance().get_entities_have_this_variable( entities ,type, kind, logOption._name);
					for ( PlayerBase entity:entities) {
						String roleClassName = roleDataSet._roleClassNameMap.get( entity._initialRole);
						String name = get_unique_name( logOption._name, usedNames);
						if ( 100 < codes1.get( codes1.size() - 1).length())
							codes1.add( ",\n" + JavaProgramExporter._indents[ 3] + "\"" + name + "\"");
						else
							codes1.set( codes1.size() - 1, codes1.get( codes1.size() - 1) + ( ", \"" + name + "\""));
						if ( type.equals( "agent")) {
							List<String> roleClassNames = roleDataSet.get_variableOwnerRoleClassNames( type, kind, logOption._name);
							roleClassName = roleClassNames.contains( roleClassName) ? roleClassName : "TAgentRole";
							if ( entity._number.equals( "")) {
								if ( !entity._initialRole.equals( "") && roleClassName.equals( "TAgentRole"))
									code2 += JavaProgramExporter._indents[ 3] + "logger.putData(\"" + name + "\", ((TAgentRole)agentManager.getAgentDB().get(" + agentNameMap.get( entity._name) + ").getRole().getMergedRole(\"AgentRole\")).get" + logOption._name + "());\n";
								else
									code2 += JavaProgramExporter._indents[ 3] + "logger.putData(\"" + name + "\", ((" + roleClassName + ")agentManager.getAgentDB().get(" + agentNameMap.get( entity._name) + ").getRole()).get" + logOption._name + "());\n";
							} else {
								code2 += JavaProgramExporter._indents[ 3] + "for (int i = 1; i <= agentManager.getNoOfAgents(" + agentNameMap.get( entity._name) + "); ++i)\n";
								if ( !entity._initialRole.equals( "") && roleClassName.equals( "TAgentRole"))
									code2 += JavaProgramExporter._indents[ 4] + "logger.putData(\"" + name + "\", ((TAgentRole)agentManager.getAgentDB().get(" + agentNameMap.get( entity._name) + "+String.valueOf(i)).getRole().getMergedRole(\"AgentRole\")).get" + logOption._name + "());\n";
								else
									code2 += JavaProgramExporter._indents[ 4] + "logger.putData(\"" + name + "\", ((" + roleClassName + ")agentManager.getAgentDB().get(" + agentNameMap.get( entity._name) + "+String.valueOf(i)).getRole()).get" + logOption._name + "());\n";
							}
						} else {
							if ( entity._number.equals( ""))
								code2 += JavaProgramExporter._indents[ 3] + "logger.putData(\"" + name + "\", ((" + roleClassName + ")spotManager.getSpotDB().get(" + spotNameMap.get( entity._name) + ").getRole()).get" + logOption._name + "());\n";
							else {
								code2 += JavaProgramExporter._indents[ 3] + "for (int i = 1; i <= spotManager.getNoOfSpots(" + spotNameMap.get( entity._name) + "); ++i)\n";
								code2 += JavaProgramExporter._indents[ 4] + "logger.putData(\"" + name + "\", ((" + roleClassName + ")spotManager.getSpotDB().get(" + spotNameMap.get( entity._name) + "+String.valueOf(i)).getRole()).get" + logOption._name + "());\n";
								
							}
						}
					}
				}
			}
		}

		codes1.add( "};");

		String code1 = "";
		for ( String code:codes1)
			code1 += code;

		logCodes.add( code1);

		logCodes.add( code2);

		return logCodes;
	}

	/**
	 * @param name
	 * @param usedNames 
	 * @return
	 */
	private String get_unique_name(String name, List<String> usedNames) {
		// TODO Auto-generated method stub
		int index = 1;
		while ( usedNames.contains( name + String.valueOf( index)))
			++index;

		String newName = name + String.valueOf( index);
		usedNames.add( newName);
		return newName;
//		int index = 1;
//		String newName = "";
//		Integer integer = indexMap.get( name);
//		if ( null == integer) {
////			indexMap.put( name, 1);
//			newName = name + "1";
//		} else {
////			indexMap.put( name, ++integer);
//			newName = name + String.valueOf( ++integer);
//			index = integer;
//		}
//
//		while ( usedNames.contains( newName))
//			newName = name +  String.valueOf( ++integer);
//
//		indexMap.put( name, ++integer);
//		used
//		return newName;
	}

	/**
	 * Returns true for writing this object data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this object data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		writer.startElement( null, null, "log_data", new AttributesImpl());

		Collection<Map<String, LogOptionManager>> maps = values();
		for ( Map<String, LogOptionManager> map:maps) {
			for ( String kind:_kinds) {
				LogOptionManager logOptionManager = map.get( kind);
				if ( null == logOptionManager)
					continue;

				logOptionManager.write( writer);
			}
		}

		writer.endElement( null, null, "log_data");

		return true;
	}
}
