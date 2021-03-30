/**
 * 
 */
package soars.application.visualshell.file.exporter.java;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import soars.application.visualshell.file.exporter.java.object.MethodProperty;
import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
import soars.application.visualshell.file.exporter.java.object.RuleData;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.log.LogManager;
import soars.application.visualshell.object.player.agent.AgentObject;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.object.base.ObjectBase;
import soars.application.visualshell.object.player.base.object.variable.VariableObject;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.Rules;
import soars.application.visualshell.object.simulation.SimulationManager;
import soars.application.visualshell.object.stage.Stage;
import soars.application.visualshell.object.stage.StageManager;
import soars.common.utility.tool.file.FileUtility;

/**
 * @author kurata
 *
 */
public class JavaProgramExporter {

	/**
	 * 
	 */
	static public final int _size = 101;

	/**
	 * 
	 */
	static public final String _indents[] = new String[ _size];

	/**
	 * 
	 */
	static private Object _lock = new Object();

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
			update_indent( "\t");
		}
	}

	/**
	 * 
	 */
	private static void update_indents() {
		// TODO Auto-generated method stub
		String indent = "";
		if ( !Environment.get_instance().get( Environment._exportJavaProgramDialogIndentTypeKey, "").equals( "space"))
			indent = "\t";
		else {
			int length = Integer.valueOf( Environment.get_instance().get( Environment._exportJavaProgramDialogNumberOfSpaceForIndentKey, "1"));
			for ( int i = 0; i < length; ++i)
				indent += " ";
		}
		update_indent( indent);
	}

	/**
	 * @param indent
	 */
	private static void update_indent(String indent) {
		// TODO Auto-generated method stub
		for ( int i = 0; i < _size; ++i) {
			_indents[ i] = "";
			for ( int j = 0; j < i; ++j)
				_indents[ i] += indent;
		}
	}

	/**
	 * @param folder
	 * @param path
	 * @param initialValueMap
	 * @return
	 */
	public static boolean execute(File folder, String path, InitialValueMap initialValueMap) {
		// TODO Auto-generated method stub
		if ( StageManager.get_instance()._main_stages.isEmpty())
			return false;	// メインステージが空

		update_indents();

		//String p = "///hoge/g///efsdf";
		//p = format_path( p);

		path = format_path( path);
		if ( null == path)
			return false;

		File programRootFolder = create_programRootFolder( folder, path);
		if ( null == programRootFolder)
			return false;

		String packagePrefix = get_packagePrefix( path);

		// TStages.java
		Map<String, String> stageNameMap = new HashMap<>();
		if ( !StageManager.get_instance().export_TStages( stageNameMap, programRootFolder, packagePrefix))
			return false;

		// TSpotTypes.java
		Map<String, String> spotNameMap = new HashMap<>();
		if ( !LayerManager.get_instance().export_TEntityTypes( spotNameMap, programRootFolder, packagePrefix, SpotObject.class, "TSpotTypes", "S"))
			return false;

		// TAgentTypes.java
		Map<String, String> agentNameMap = new HashMap<>();
		if ( !LayerManager.get_instance().export_TEntityTypes( agentNameMap, programRootFolder, packagePrefix, AgentObject.class, "TAgentTypes", "A"))
			return false;

		// ロールクラス生成準備
		RoleDataSet roleDataSetForMainStage = new RoleDataSet();
		LayerManager.get_instance().get_agent_common_variables( "TAgentRole", roleDataSetForMainStage);
		if ( !LayerManager.get_instance().get_role_class_java_program( roleDataSetForMainStage, programRootFolder, packagePrefix, "main stage"))
			return false;

		// ルールクラス内メソッド生成準備
		// 第２引数にわざわざroleDataSetForMainStageのメンバを指定しているのは、このあとの初期ステージプログラム作成でmこのメソッドを呼ぶから
		if ( !LayerManager.get_instance().get_ruleDataMap( roleDataSetForMainStage, roleDataSetForMainStage._roleClassNameMap, "main stage"))
			return false;

		//roleDataSet.debug8();

		// ルールクラス生成
		if ( !LayerManager.get_instance().export_RuleClasses( roleDataSetForMainStage, agentNameMap, spotNameMap, initialValueMap, programRootFolder, packagePrefix))
			return false;

		// ロールクラス生成
		if ( !LayerManager.get_instance().export_RoleClasses( roleDataSetForMainStage, programRootFolder, packagePrefix, stageNameMap))
			return false;

		// 初期ステージ実行プログラム作成準備
		RoleDataSet roleDataSetForInitialStages = new RoleDataSet();
		LayerManager.get_instance().get_agent_common_variables( "TAgentRole", roleDataSetForInitialStages);
		if ( !LayerManager.get_instance().get_role_class_java_program( roleDataSetForInitialStages, programRootFolder, packagePrefix, "initial stage"))
			return false;

		// ルールクラス内メソッド生成準備
		// 第２引数にはメインステージ処理で作成した[ロール名 -> ロールクラス名]のマップを指定す流必要がある
		// roleDataSetForInitialStagesの_roleDataSetForMainStageにはメインステージ処理で作成した[ロール名 -> ロールクラス名]のマップとは異なっているから
		if ( !LayerManager.get_instance().get_ruleDataMap( roleDataSetForInitialStages, roleDataSetForMainStage._roleClassNameMap, "initial stage"))
			return false;

		//roleDataSetForInitialStages.debug5();
		//roleDataSetForInitialStages.debug8();

		// 初期ステージ実行プログラム作成
		if ( !export_initialStageClasses( roleDataSetForInitialStages, agentNameMap, spotNameMap, initialValueMap, programRootFolder, packagePrefix))
			return false;

		// TMain.java
		if ( !export_TMain( roleDataSetForMainStage, roleDataSetForInitialStages, agentNameMap, spotNameMap, stageNameMap, initialValueMap, programRootFolder, packagePrefix))
			return false;

		//debug( roleDataSet, stageNameMap, agentNameMap, spotNameMap);

		return true;
	}

	/**
	 * @param path
	 * @return
	 */
	private static String format_path(String path) {
		// TODO Auto-generated method stub
		if ( null == path || path.equals( ""))
			return path;

		path = path.replaceAll( "\\/+", "/");
		if ( path.equals( ""))
			return path;

		if ( path.equals( "/"))
			return "";

		while ( path.startsWith( "/"))
			path = path.substring( 1);

		if ( path.endsWith( "/"))
			path = path.substring( 0, path.length() - 1);

		return path;
	}

	/**
	 * @param folder
	 * @param path
	 * @return
	 */
	private static File create_programRootFolder(File folder, String path) {
		// TODO Auto-generated method stub
		if ( null == path || path.equals( "")) {
			if ( !FileUtility.delete( folder, false))
				return null;

			return folder;
		}

		File programRootFolder = new File( folder, path);

		if ( programRootFolder.exists() && !FileUtility.delete( programRootFolder, false))
			return null;

		if ( !programRootFolder.exists() && !programRootFolder.mkdirs())
			return null;

		return programRootFolder;
	}

	/**
	 * @param path
	 * @return
	 */
	private static String get_packagePrefix(String path) {
		// TODO Auto-generated method stub
		return path.replaceAll( "\\/", "\\.");
	}

	/**
	 * @param name
	 * @param prefix
	 * @return
	 */
	public static String format_variable_name(String name, String prefix) {
		// TODO Auto-generated method stub
		name = name.toUpperCase();
		return !name.matches( "^\\d.+") ? name : prefix + name;
	}

	/**
	 * @param roleName
	 * @param roleClassNameMap 
	 * @return
	 */
	public static String get_role_class_name(String roleName, Map<String, String> roleClassNameMap) {
		// TODO Auto-generated method stub
		if ( roleName.equals( ""))
			return "TAgentRole";

		String roleClassName = "T" + format( roleName);
		if ( !roleClassName.endsWith( "Role"))
			roleClassName += "Role";

		int index = 1;
		Collection<String> roleClassNames = roleClassNameMap.values();
		while ( roleClassNames.contains( roleClassName))
			roleClassName += String.valueOf( index);

		return roleClassName;
	}

	/**
	 * @param roleClassName
	 * @param stageName
	 * @return
	 */
	public static String get_rule_class_name(String roleClassName, String stageName) {
		// TODO Auto-generated method stub
		return roleClassName + format( stageName) + "Rule";
	}

	/**
	 * @param name
	 * @return
	 */
	private static String format(String name) {
		// TODO Auto-generated method stub
		while ( name.matches( "^\\d.+") && 0 < name.length())
			name = name.substring( 1);

		while ( name.endsWith( "_") && 0 < name.length())
			name = name.substring( 0, name.length() - 1);

		String newName = "";
		for ( int i = 0; i < name.length(); ++i) {
			String temp = "";
			temp = String.valueOf( name.charAt( i));
			if ( 0 == i)
				newName += temp.toUpperCase();
			else {
				if ( !temp.matches( "^[-_]"))
					newName += temp;
				else {
					temp = String.valueOf( name.charAt( ++i));
					newName += temp.toUpperCase();
				} 
			}
		}
		return newName;
	}

	/**
	 * @param spotFullname 
	 * @param roleClassNameMap
	 * @param roleOwnersMap
	 * @param agentNameMap 
	 * @param spotNameMap 
	 * @param imports
	 * @param commands
	 * @param packagePrefix 
	 * @param type 
	 * @param role 
	 * @param initialStage 
	 * @return
	 */
	public static String get_role_class_local_variable_name(String spotFullname, Map<String, String> roleClassNameMap, Map<String, List<PlayerBase>> roleOwnersMap, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String>imports, List<String>commands, String packagePrefix, String type, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		//List<String> prefixes = new ArrayList<>();
		SpotObject spotObject = LayerManager.get_instance().get_spot_has_this_name( spotFullname);
		if ( null == spotObject)
			return null;
			//return prefixes;

		Set<String> roleClassNames = roleOwnersMap.keySet();
		for ( String roleClassName:roleClassNames) {
			List<PlayerBase> owners = roleOwnersMap.get( roleClassName);
			if ( null == owners)
				continue;
			for ( PlayerBase entty:owners) {
				if ( null == entty)
					continue;
				if ( entty == spotObject) {
					//System.out.println( pb._name);
					String roleClassLocalVariableName = "";
					for ( int i = 1; i < roleClassName.length(); ++i) {
						String temp = "";
						temp = String.valueOf( roleClassName.charAt( i));
						roleClassLocalVariableName += ( 1 == i) ? temp.toLowerCase() : temp;
					}
					String imprt = packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + type + "." + roleClassName;
					JavaProgramExporter.append_import( imprt, imports);
					JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "TSpotTypes", imports);
					if ( initialStage) {
						// "TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();"
						JavaProgramExporter.append_command( roleClassName + " " + get_roleClassLocalVariableName( roleClassLocalVariableName, spotFullname, spotObject) + " = (" + roleClassName + ") spotManager.getSpotDB().get(" + get_entity_name( spotFullname, spotObject, spotNameMap) + ").getBaseRole()", commands);
					} else {
						// "TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();"
						JavaProgramExporter.append_command( roleClassName + " " + get_roleClassLocalVariableName( roleClassLocalVariableName, spotFullname, spotObject) + " = (" + roleClassName + ") spotSet.get(" + get_entity_name( spotFullname, spotObject, spotNameMap) + ").getRole()", commands);
					}
					//prefixes.add( roleClassLocalVariableName);
					return roleClassLocalVariableName;
				}
			}
		}
		return null;
//		return prefixes;
	}

	/**
	 * @param roleClassLocalVariableName
	 * @param entityFullname
	 * @param entity
	 * @return
	 */
	public static String get_roleClassLocalVariableName(String roleClassLocalVariableName, String entityFullname, PlayerBase entity) {
		// TODO Auto-generated method stub
		return roleClassLocalVariableName + get_entity_name_suffix( entityFullname, entity);
	}

	/**
	 * @param fullname
	 * @param agentNameMap
	 * @return
	 */
	public static String get_agent_name(String fullname, Map<String, String> agentNameMap) {
		// TODO Auto-generated method stub
		AgentObject agentObject = LayerManager.get_instance().get_agent_has_this_name( fullname);
		if ( null == agentObject)
			return null;

		return get_entity_name( fullname, agentObject, agentNameMap);
	}

	/**
	 * @param fullname
	 * @param spotNameMap
	 * @return
	 */
	public static String get_spot_name(String fullname, Map<String, String> spotNameMap) {
		// TODO Auto-generated method stub
		SpotObject spotObject = LayerManager.get_instance().get_spot_has_this_name( fullname);
		if ( null == spotObject)
			return null;

		return get_entity_name( fullname, spotObject, spotNameMap);
	}

	/**
	 * @param fullname
	 * @param entity
	 * @param entityNameMap
	 * @return
	 */
	public static String get_entity_name(String fullname, PlayerBase entity, Map<String, String> entityNameMap) {
		// TODO Auto-generated method stub
		String suffix = get_entity_name_suffix( fullname, entity);
		return entityNameMap.get( entity._name) + ( !suffix.equals( "") ? ( "+\"" + suffix + "\"") : "");
	}

	/**
	 * @param entityFullname
	 * @param entity
	 * @return
	 */
	public static String get_entity_name_suffix(String entityFullname, PlayerBase entity) {
		// TODO Auto-generated method stub
		return entityFullname.substring( entity._name.length());
	}

	/**
	 * @param roleClassLocalName
	 * @return
	 */
	public static String get_roleClassLocalVariableName(String roleClassLocalName) {
		// TODO Auto-generated method stub
		return String.valueOf( roleClassLocalName.charAt( 1)).toLowerCase() + roleClassLocalName.substring( 2);
	}

	/**
	 * @param imprt
	 * @param imports
	 */
	public static void append_import(String imprt, List<String> imports) {
		// TODO Auto-generated method stub
		append( imprt, imports);
	}

	/**
	 * @param command
	 * @param commands
	 */
	public static void append_command(String command, List<String> commands) {
		// TODO Auto-generated method stub
		append( command, commands);
	}

	/**
	 * @param string
	 * @param list
	 */
	private static void append(String string, List<String> list) {
		// TODO Auto-generated method stub
		if ( !list.contains( string))
			list.add( string);
	}

	/**
	 * @param name
	 * @return
	 */
	private static String get_gent_name(String name) {
		// TODO Auto-generated method stub
		return name.substring( 0, 1).toUpperCase() + name.substring( 1);
	}

	/**
	 * @param roleDataSet
	 * @param agentNameMap
	 * @param spotNameMap
	 * @param initialValueMap
	 * @param programRootFolder
	 * @param packagePrefix
	 * @return
	 */
	private static boolean export_initialStageClasses(RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, InitialValueMap initialValueMap, File programRootFolder, String packagePrefix) {
		roleDataSet.create_initialStageRuleDataMap();

		File folder = new File( programRootFolder, "main/init/spot");
		if ( !folder.mkdirs())
			return false;

		Vector spots = new Vector();
		LayerManager.get_instance().get_spots( spots);
		write_initialStageClassFiles( "spot", spots, roleDataSet, agentNameMap, spotNameMap, initialValueMap, packagePrefix, folder);

		folder = new File( programRootFolder, "main/init/agent");
		if ( !folder.mkdirs())
			return false;

		Vector agents = new Vector();
		LayerManager.get_instance().get_agents( agents);
		write_initialStageClassFiles( "agent", agents, roleDataSet, agentNameMap, spotNameMap, initialValueMap, packagePrefix, folder);

		//roleDataSet.debug7();

		return true;
	}

	/**
	 * @param type
	 * @param entities
	 * @param roleDataSet
	 * @param initialStageRuleDataMap 
	 * @param agentNameMap
	 * @param spotNameMap
	 * @param initialValueMap
	 * @param packagePrefix
	 * @param folder
	 * @param agent
	 */
	private static void write_initialStageClassFiles(String type, Vector entities, RoleDataSet roleDataSet/*, Map<String, Map<String, List<RuleData>>> initialStageRuleDataMap*/, Map<String, String> agentNameMap, Map<String, String> spotNameMap, InitialValueMap initialValueMap, String packagePrefix, File folder) {
		// TODO Auto-generated method stub
		for ( int i = 0; i < StageManager.get_instance()._initial_stages.size(); ++i) {
			Stage initialStage = ( Stage)StageManager.get_instance()._initial_stages.get( i);
			if ( initialStage._name.equals( Constant._initializeChartStageName))
				continue;

			String initialStageClassName = get_initial_stage_class_name( initialStage._name);
			roleDataSet._initialStageClassNameMap.put( initialStage._name, initialStageClassName);

			String text1 = "package " + packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "main.init." + type + ";\n";

			String text2 = "\n/**\n";
			text2 += " * \n";
			text2 += " */\n";
			text2 += "public class " + initialStageClassName + " {\n";

			Map<String, List<RuleData>> ruleDatalistMap = roleDataSet._initialStageRuleDataMap.get( type).get( initialStage._name);
			if ( null == ruleDatalistMap)
				continue;

			Set<String> roleClassNames = ruleDatalistMap.keySet();
			if ( roleClassNames.isEmpty())
				continue;

			List<String> imports = new ArrayList<>();
			List<String> commands = new ArrayList<>();

			int index = 1;
			for ( String roleClassName:roleClassNames) {
				List<RuleData> ruleDatalist = ruleDatalistMap.get( roleClassName);
				if ( null == ruleDatalist || ruleDatalist.isEmpty())
					continue;

				append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + type + "." + roleClassName, imports);

				for ( RuleData ruleData:ruleDatalist) {
					text2 += "\n" + _indents[ 1] + "// Entities : " + ruleData._entities + "\n";	// このリストのに含まれるエージェント/スポットが小クラスのメソッドを呼ぶことになる筈！
					if ( type.equals( "agent")) {
						append_import( "soars2.core.TAgent", imports);
						append_import( "soars2.core.TAgentManager", imports);
					}
					append_import( "soars2.core.TSpot", imports);
					append_import( "soars2.core.TSpotManager", imports);
					append_import( "soars2.utils.random2013.ICRandom", imports);
					for ( Rules rules:ruleData._rulesList) {
						String method = "init" + String.valueOf( index++);	// これ大事！
						ruleData._methods.add( method);
						List<String> lines = rules.get_rule_class_java_program_body( ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, initialValueMap, folder, packagePrefix, LayerManager.get_instance().get_role( ruleData._roleName), true);
						text2 += "\n" + _indents[ 1] + "public static boolean " + method + "("
							+ ( type.equals( "agent")
								? "TAgent agent, TSpot spot, " + roleClassName + " ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand"
								: "TSpot spot, " + roleClassName + " currentSpotRole, TSpotManager spotManager, ICRandom rand")
								//: "TSpot spot, " + roleClassName + " currentSpotRole, TAgentManager agentManager, TSpotManager spotManager, TCJava48BitLcg rand")
							+ ") {\n";
						for ( String command:commands) {
							text2 += JavaProgramExporter._indents[ 2] + command + ";\n";
						}
						for ( String line:lines) {
								text2 += line + "\n";
						}
						text2 += _indents[ 1] + "}\n";
					}
				}
			}

			text2 += "}\n";

			Collections.sort( imports);

			for ( String imprt:imports)
				text1 += "\nimport " + imprt + ";";

			text1 += imports.isEmpty() ? "" : "\n";

			FileUtility.write_text_to_file( new File( folder, initialStageClassName + ".java"), text1 + text2);
		}
	}

	/**
	 * @param initialStageName
	 * @return
	 */
	public static String get_initial_stage_class_name(String initialStageName) {
		// TODO Auto-generated method stub
		return "T" + format( initialStageName);
	}

	/**
	 * @param roleDataSetForMainStage 
	 * @param roleDataSetForInitialStages 
	 * @param agentNameMap
	 * @param spotNameMap
	 * @param stageNameMap
	 * @param initialValueMap 
	 * @param programRootFolder
	 * @param packagePrefix
	 * @return
	 */
	private static boolean export_TMain(RoleDataSet roleDataSetForMainStage, RoleDataSet roleDataSetForInitialStages, Map<String, String> agentNameMap, Map<String, String> spotNameMap, Map<String, String> stageNameMap, InitialValueMap initialValueMap, File programRootFolder, String packagePrefix) {
		// TODO Auto-generated method stub
		File folder = new File( programRootFolder, "main");
		if ( !folder.exists() && !folder.mkdirs())
			return false;

		List<String> imports = new ArrayList<>();
		append_import( "java.io.File", imports);
		append_import( "java.io.IOException", imports);
		append_import( "java.util.List", imports);

		if ( !get_loggerJavaProgram( programRootFolder, packagePrefix))
			return false;

		String text1 = "package " + packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "main;\n";

		String text2 = "\npublic class TMain {\n\n";
		text2 += _indents[ 1] + "public static void main(String[] args) throws IOException {\n";

		text2 += "\n" + _indents[ 2] + "// ログディレクトリの指定\n";
		text2 += _indents[ 2] + "String logDir = \"log\";\n";

		text2 += "\n" + _indents[ 2] + "long startTime = System.currentTimeMillis();\n";

		// メインステージの定義
		text2 += getMainStagesDefinition( stageNameMap, packagePrefix, imports);
	
		text2 += "\n" + _indents[ 2] + "// ルール収集器\n";
		append_import( "soars2.core.TRuleAggregator", imports);
		text2 += _indents[ 2] + "TRuleAggregator ruleAggregator = new TRuleAggregator(mainStages);\n";

		//append_import( "soars2.core.TRuleLogger", imports);
		//text2 += "\n" + _indents[ 2] + "TRuleLogger ruleLogger = new TRuleLogger(logDir + File.separator + \"ruleAction.csv\");\n";

		append_import( "soars2.utils.random2013.ICRandom", imports);
		append_import( "soars2.utils.random2013.TCJava48BitLcg", imports);
		text2 += "\n" + _indents[ 2] + "ICRandom rand = new TCJava48BitLcg(); // 乱数発生器\n";

		text2 += "\n" + _indents[ 2] + "// スポットの初期化\n";
		append_import( "soars2.core.TSpotManager", imports);
		text2 += _indents[ 2] + "TSpotManager spotManager = new TSpotManager(ruleAggregator); // スポット管理\n";

		// スポットの生成
		List<String> spotNames = new ArrayList<>();
		text2 += createSpots( spotNames, spotNameMap, packagePrefix, imports);

		text2 += "\n" + _indents[ 2] + "// エージェントの初期化\n";
		append_import( "soars2.core.TAgentManager", imports);
		text2 += _indents[ 2] + "TAgentManager agentManager = new TAgentManager(ruleAggregator); // エージェント管理\n";

		List<String> codes = new ArrayList<>();

		// エージェントの生成
		List<String> agentNames = new ArrayList<>();
		text2 += createAgents( codes, agentNames, agentNameMap, spotNameMap, packagePrefix, imports);

		// スポットの初期化
		text2 += initializeSpots( codes, spotNames, roleDataSetForMainStage, spotNameMap, agentNameMap, packagePrefix, imports, initialValueMap);

		// エージェントの初期化
		text2 += initializeAgents( codes, agentNames, roleDataSetForMainStage, agentNameMap, spotNameMap, packagePrefix, imports, initialValueMap);

		// 初期ステージ実行
		text2 += "\n" + _indents[ 2] + "// 初期ステージの実行\n";
		text2 += doInitialStages( codes, roleDataSetForInitialStages._initialStageRuleDataMap, roleDataSetForInitialStages._initialStageClassNameMap, agentNameMap, spotNameMap, packagePrefix, imports);

		text2 += "\n" + _indents[ 2] + "// グローバル共有変数集合を生成する\n";
		text2 += _indents[ 2] + "HashMap<String, Object> globalSharedVariableSet = new HashMap<>();\n";
		JavaProgramExporter.append_import( "java.util.HashMap", imports);

		text2 += "\n" + _indents[ 2] + "// 各時刻における各エージェントの位置のログファイルをオープンする\n";
		text2 += _indents[ 2] + "TAgentLogger agentLocationLog = new TAgentLogger(logDir + File.separator + \"agentLocation.csv\",\n";
		text2 += _indents[ 3] + "TAgentLogger.CURRENT_SPOT_NAME_KEY, agentManager);\n";
		JavaProgramExporter.append_import( "soars2.core.TAgentLogger", imports);
		List<String> logCodes = LogManager.get_instance().get_java_program( roleDataSetForMainStage, agentNameMap, spotNameMap);
		text2 += _indents[ 2] + logCodes.get( 0) + "\n";

		text2 += "\n" + _indents[ 2] + "TCMultiEntryLogger logger = new TCMultiEntryLogger(logDir + File.separator + \"log\", activeKeys); // ロガーを作成\n";
		append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "utils.analysis2019.logger2013.TCMultiEntryLogger", imports);
		text2 += "\n" + _indents[ 2] + "logger.beginLog(); // ロギング開始\n";

		text2 += "\n" + _indents[ 2] + "// メインループ\n";
		text2 += _indents[ 2] + "for (TTime t = new TTime(\"" + SimulationManager.get_instance().getStartTime() /*0/0:00*/ + "\"); t.isLessThan(\"" +  SimulationManager.get_instance().getEndTime() /*300/0:00*/ + "\"); t.add(\"0:30\")) {\n";
		text2 += _indents[ 3] + "for (String stage : mainStages) {\n";
		text2 += _indents[ 4] + "// 時刻t，ステージstageに登録されたルールを実行する\n";
		text2 += _indents[ 4] + "ruleAggregator.executeStage(t, stage, spotManager, agentManager, globalSharedVariableSet);\n";
		text2 += _indents[ 3] + "}\n";
		text2 += _indents[ 3] + "if (t.isEqualTo(\"0:00\")) {\n";
		text2 += _indents[ 4] + "System.out.println(t); // 現在時刻を画面に表示する\n";
		text2 += _indents[ 3] + "}\n";
		text2 += _indents[ 3] + "agentLocationLog.output(t); // 現在時刻の各エージェントの位置をログに出力する\n";
		text2 += _indents[ 3] + "logger.beginEntry(true); // ログのエントリ開始\n";
		text2 += _indents[ 3] + "logger.putData(\"time\", t.toString()); // 時間をログに出力\n";
		text2 += logCodes.get( 1);
		text2 += _indents[ 3] + "logger.endEntry(); // ログのエントリ終了\n";
		text2 += _indents[ 2] + "}\n";
		text2 += _indents[ 2] + "agentLocationLog.close();\n";
		text2 += _indents[ 2] + "logger.endLog();\n";
		text2 += _indents[ 2] + "long elapsedTime = System.currentTimeMillis() - startTime;\n";
		text2 += _indents[ 2] + "System.out.println(elapsedTime + \" [msec]\");\n";

		text2 += _indents[ 1] + "}\n";

		for ( String code:codes)
			text2 += code;

		text2 += "}\n";

		Collections.sort( imports);

		for ( String imprt:imports)
			text1 += "\nimport " + imprt + ";";

		text1 += imports.isEmpty() ? "" : "\n";

		File file = new File( folder, "TMain.java");
		return FileUtility.write_text_to_file( file, text1 + text2, "UTF8");
	}

	/**
	 * @param programRootFolder
	 * @param packagePrefix
	 * @return
	 */
	private static boolean get_loggerJavaProgram(File programRootFolder, String packagePrefix) {
		// TODO Auto-generated method stub
		if ( !FileUtility.copy_all( new File( "../resource/java/src/utils"), new File( programRootFolder, "utils")))
			return false;

		File[] files = {
			new File( programRootFolder, "utils/analysis2019/logger2013/TCMultiEntryLogger.java"),
			new File( programRootFolder, "utils/analysis2019/logger2013/TCSingleEntryLogger.java")};
		for ( File file:files) {
			if ( !update_file( file, "package " + packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "utils.analysis2019.logger2013;\n"))
				return false;
		}

		return true;
	}

	/**
	 * @param file
	 * @param packageString
	 * @return
	 */
	private static boolean update_file(File file, String packageString) {
		// TODO Auto-generated method stub
		String text = FileUtility.read_text_from_file(file);
		if ( null == text)
			return false;

		return FileUtility.write_text_to_file( file, packageString + text);
	}

	/**
	 * メインステージの定義
	 * @param stageNameMap
	 * @param packagePrefix
	 * @param imports
	 * @return
	 */
	private static String getMainStagesDefinition(Map<String, String> stageNameMap, String packagePrefix, List<String> imports) {
		append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "TStages", imports);
		String text = "\n" + _indents[ 2] + "List<String> mainStages = List.of( // メインステージ\n";
		for ( int i = 0; i < StageManager.get_instance()._main_stages.size(); ++i) {
			Stage stage = ( Stage)StageManager.get_instance()._main_stages.get( i);
			if ( null == stageNameMap.get( stage._name))
				continue;
			text += ( ( ( 0 == i) ? "" : ",\n") + _indents[ 3] + stageNameMap.get( stage._name));
		}
		return ( text + ");\n");
	}

	/**
	 * スポットの生成
	 * @param spotNames
	 * @param spotNameMap
	 * @param packagePrefix
	 * @param imports
	 * @return
	 */
	private static String createSpots(List<String> spotNames, Map<String, String> spotNameMap, String packagePrefix, List<String> imports) {
		String text = "\n" + _indents[ 2] + "// スポットの生成\n";

		Set<String> keys = spotNameMap.keySet();
		//List<String> spotNames = new ArrayList<>();
		if ( !keys.isEmpty()) {
			for ( String key:keys)
				spotNames.add( key);
			Collections.sort( spotNames);

			for ( String spotName:spotNames) {
				//System.out.println( spotName);
				SpotObject spotObject = LayerManager.get_instance().get_spot( spotName);
				append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "TSpotTypes", imports);
				text += _indents[ 2] + "spotManager.createSpot"
					+ ( spotObject._number.equals( "") ? "" : "s")
					+ "(" + spotNameMap.get( spotName)
					+ ( spotObject._number.equals( "") ? "" : ( ", " + spotObject._number)) + ");\n";
			}
		}

		return text;
	}

	/**
	 * エージェントの生成
	 * @param codes
	 * @param agentNames
	 * @param agentNameMap
	 * @param spotNameMap
	 * @param packagePrefix
	 * @param imports
	 * @return
	 */
	private static String createAgents(List<String> codes, List<String> agentNames, Map<String, String> agentNameMap, Map<String, String> spotNameMap, String packagePrefix, List<String> imports) {
		String text = "\n" + _indents[ 2] + "// エージェントの生成\n";

		Set<String> keys = agentNameMap.keySet();
		//agentNames = new ArrayList<>();
		//List<String> agentNames = new ArrayList<>();
		if ( !keys.isEmpty()) {
			for ( String key:keys)
				agentNames.add( key);

			Collections.sort( agentNames);
			for ( String agentName:agentNames) {
				AgentObject agentObject = LayerManager.get_instance().get_agent( agentName);
				append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "TAgentTypes", imports);
				text += _indents[ 2] + "createAgent(agentManager, spotManager, "
					+ agentNameMap.get( agentName) + ", "
					+ ( agentObject._number.equals( "") ? "1" : agentObject._number) + ", "
					+ ( agentObject._initialSpot.equals( "") ? "\"\"" : spotNameMap.get( agentObject._initialSpot)) + ");\n";
			}
		}

		append_import( "java.util.ArrayList", imports);
		append_import( "soars2.core.TAgent", imports);
		String line = "\n" + _indents[ 1] + "private static void createAgent(TAgentManager agentManager, TSpotManager spotManager, String agentName, int noOfAgents, String initialSpot) {\n";
		line += _indents[ 2] + "ArrayList<TAgent> agents = agentManager.createAgents(agentName, noOfAgents);\n";
		line += _indents[ 2] + "if (initialSpot.equals(\"\"))\n";
		line += _indents[ 3] + "return;\n";
		line += _indents[ 2] + "for (TAgent agent:agents)\n";
		line += _indents[ 3] + "agent.initializeCurrentSpot(initialSpot, spotManager.getSpotDB());\n";
		line += _indents[ 1] + "}\n";
		codes.add( line);

		return text;
	}

	/**
	 * スポットの初期化
	 * @param codes
	 * @param spotNames
	 * @param roleDataSetForMainStage
	 * @param spotNameMap
	 * @param agentNameMap
	 * @param packagePrefix
	 * @param imports
	 * @param initialValueMap
	 * @return
	 */
	private static String initializeSpots(List<String> codes, List<String> spotNames, RoleDataSet roleDataSetForMainStage, Map<String, String> spotNameMap, Map<String, String> agentNameMap, String packagePrefix, List<String> imports, InitialValueMap initialValueMap) {
		String text = "\n" + _indents[ 2] + "// スポットの初期化\n";
		if ( !spotNames.isEmpty()) {
			int index = 1;
			for ( String spotName:spotNames) {
				SpotObject spotObject = LayerManager.get_instance().get_spot( spotName);

				text += _indents[ 2] + "initializeSpot" + String.valueOf( index) + "(spotManager, agentManager, rand);\n";

				String roleClassName = roleDataSetForMainStage._roleClassNameMap.get( spotObject._initialRole);
				append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "spot." + roleClassName, imports);

				Map<String, String> variableInitialValueMap = new HashMap<>();
				Map<String, String> commandMap = new HashMap<>();

				append_import( "soars2.core.TSpot", imports);
				String line = "\n" + _indents[ 1] + "private static void initializeSpot" + String.valueOf( index++) + "(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {\n";
				if ( spotObject._number.equals( "")) {
					// ロールクラスのコンストラクタ引数に指定する初期値をvariableInitialValueMapへ収集する
					// それらを初期化する必要がある場合は、その初期化コマンドをcommandMapに収納する
					//get_initial_value_map_for_agent( spotObject, variableInitialValueMap, commandMap, roleDataSet, agentNameMap, spotNameMap, initialValueMap, imports, 2);
					get_entity_initial_values( "spot", spotObject, variableInitialValueMap, commandMap, roleDataSetForMainStage, agentNameMap, spotNameMap, roleClassName, initialValueMap, imports, 2);
					line += _indents[ 2] + "TSpot spot = spotManager.getSpotDB().get(" + spotNameMap.get( spotName) + ");\n";

					// ロールクラスのコンストラクタ引数に初期化された配列がある場合それらの配列を初期化するコマンド文字列を生成して書き出す
					line += write_entity_initial_values_command( "spot", spotObject, roleDataSetForMainStage, roleClassName, commandMap);

					line += _indents[ 2] + roleClassName + " role = new " + roleClassName + "(spot, rand";

					// variableInitialValueMapに収納されているロールクラスのコンストラクタ引数を取り出して引数文字列を生成する
					String parameters = get_role_initial_parameters( "spot", spotObject, variableInitialValueMap, roleDataSetForMainStage, roleClassName, 3);

					line += ( parameters.startsWith( ",\n") && 100 > parameters.length()) ? ( ", " + parameters.substring( ( ",\n" + JavaProgramExporter._indents[ 4]).length())) : parameters;

					line += _indents[ 2] + "spot.setBaseRole(role);\n";
				} else {
					line += _indents[ 2] + "for (int i = 1; i <= spotManager.getNoOfSpots(" + spotNameMap.get( spotName) + "); ++i) {\n";

					// ロールクラスのコンストラクタ引数に指定する初期値をvariableInitialValueMapへ収集する
					// それらを初期化する必要がある場合は、その初期化コマンドをcommandMapに収納する
					//get_initial_value_map_for_agent( spotObject, variableInitialValueMap, commandMap, roleDataSet, agentNameMap, spotNameMap, initialValueMap, imports, 3);
					get_entity_initial_values( "spot", spotObject, variableInitialValueMap, commandMap, roleDataSetForMainStage, agentNameMap, spotNameMap, roleClassName, initialValueMap, imports, 3);

					line += _indents[ 3] + "TSpot spot = spotManager.getSpotDB().get(" + spotNameMap.get( spotName) + "+String.valueOf(i));\n";

					// ロールクラスのコンストラクタ引数に初期化された配列がある場合それらの配列を初期化するコマンド文字列を生成して書き出す
					line += write_entity_initial_values_command( "spot", spotObject, roleDataSetForMainStage, roleClassName, commandMap);

					line += _indents[ 3] + roleClassName + " role = new " + roleClassName + "(spot, rand";

					// variableInitialValueMapに収納されているロールクラスのコンストラクタ引数を取り出して引数文字列を生成する
					String parameters = get_role_initial_parameters( "spot", spotObject, variableInitialValueMap, roleDataSetForMainStage, roleClassName, 4);

					line += ( parameters.startsWith( ",\n") && 100 > parameters.length()) ? ( ", " + parameters.substring( ( ",\n" + JavaProgramExporter._indents[ 5]).length())) : parameters;

					line += _indents[ 3] + "spot.setBaseRole(role);\n";
					line += _indents[ 2] + "}\n";
				}
				line += _indents[ 1] + "}\n";
				codes.add( line);
			}
		}
		return text;
	}

	/**
	 * エージェントの初期化
	 * @param codes
	 * @param agentNames
	 * @param roleDataSetForMainStage
	 * @param agentNameMap
	 * @param spotNameMap
	 * @param packagePrefix
	 * @param imports
	 * @param initialValueMap
	 * @return
	 */
	private static String initializeAgents(List<String> codes, List<String> agentNames, RoleDataSet roleDataSetForMainStage, Map<String, String> agentNameMap, Map<String, String> spotNameMap, String packagePrefix, List<String> imports, InitialValueMap initialValueMap) {
		String text = "\n" + _indents[ 2] + "// エージェントの初期化\n";
		if ( !agentNames.isEmpty()) {
			int index = 1;
			for ( String agentName:agentNames) {
				AgentObject agentObject = LayerManager.get_instance().get_agent( agentName);

				text += _indents[ 2] + "initializeAgent" + String.valueOf( index) + "(agentManager, spotManager, rand);\n";

				Map<String, String> variableInitialValueMap = new HashMap<>();
				Map<String, String> commandMap = new HashMap<>();

				String roleClassName = roleDataSetForMainStage._roleClassNameMap.get( agentObject._initialRole);
				append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent.TAgentRole", imports);
				append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + roleClassName, imports);
				String line = "\n" + _indents[ 1] + "private static void initializeAgent" + String.valueOf( index++) + "(TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {\n";
				if ( agentObject._number.equals( "")) {
					// ロールクラスのコンストラクタ引数に指定する初期値をvariableInitialValueMapへ収集する
					// それらを初期化する必要がある場合は、その初期化コマンドをcommandMapに収納する
					get_entity_initial_values( "agent", agentObject, variableInitialValueMap, commandMap, roleDataSetForMainStage, agentNameMap, spotNameMap, "TAgentRole", initialValueMap, imports, 2);
					//get_initial_value_map_for_agent( agentObject, variableInitialValueMap, commandMap, roleDataSet, agentNameMap, spotNameMap, initialValueMap, imports, 2);

					line += _indents[ 2] + "TAgent agent = agentManager.getAgentDB().get(" + agentNameMap.get( agentName) + ");\n";

					// ロールクラスのコンストラクタ引数に初期化された配列がある場合それらの配列を初期化するコマンド文字列を生成して書き出す
					line += write_entity_initial_values_command( "agent", agentObject, roleDataSetForMainStage, "TAgentRole", commandMap);

					line += _indents[ 2] + "TAgentRole agentRole = new TAgentRole(agent, rand";

					// variableInitialValueMapに収納されているロールクラスのコンストラクタ引数を取り出して引数文字列を生成する
					String parameters = get_role_initial_parameters( "agent", agentObject, variableInitialValueMap, roleDataSetForMainStage, "TAgentRole", 3);

					line += ( parameters.startsWith( ",\n") && 100 > parameters.length()) ? ( ", " + parameters.substring( ( ",\n" + JavaProgramExporter._indents[ 3]).length())) : parameters;

					// ロールクラスのコンストラクタ引数に指定する初期値をvariableInitialValueMapへ収集する
					// それらを初期化する必要がある場合は、その初期化コマンドをcommandMapに収納する
					get_entity_initial_values( "agent", agentObject, variableInitialValueMap, commandMap, roleDataSetForMainStage, agentNameMap, spotNameMap, roleClassName, initialValueMap, imports, 2);

					// ロールクラスのコンストラクタ引数に初期化された配列がある場合それらの配列を初期化するコマンド文字列を生成して書き出す
					line += write_entity_initial_values_command( "agent", agentObject, roleDataSetForMainStage, roleClassName, commandMap);

					line += _indents[ 2] + roleClassName + " " + get_roleClassLocalVariableName( roleClassName) + " = new " + roleClassName + "(agent, rand";

					// variableInitialValueMapに収納されているロールクラスのコンストラクタ引数を取り出して引数文字列を生成する
					parameters = get_role_initial_parameters( "agent", agentObject, variableInitialValueMap, roleDataSetForMainStage, roleClassName, 3);

					line += ( parameters.startsWith( ",\n") && 100 > parameters.length()) ? ( ", " + parameters.substring( ( ",\n" + JavaProgramExporter._indents[ 3]).length())) : parameters;

					line += _indents[ 2] + get_roleClassLocalVariableName( roleClassName) + ".mergeRole(agentRole); // 統合\n";
					line += _indents[ 2] + "agent.setBaseRole(" + get_roleClassLocalVariableName( roleClassName) + "); // 基本役割の設定\n";
				} else {
					// ロールクラスのコンストラクタ引数に指定する初期値をvariableInitialValueMapへ収集する
					// それらを初期化する必要がある場合は、その初期化コマンドをcommandMapに収納する
					get_entity_initial_values( "agent", agentObject, variableInitialValueMap, commandMap, roleDataSetForMainStage, agentNameMap, spotNameMap, "TAgentRole", initialValueMap, imports, 3);
					//get_initial_value_map_for_agent( agentObject, variableInitialValueMap, commandMap, roleDataSet, agentNameMap, spotNameMap, initialValueMap, imports, 3);

					line += _indents[ 2] + "for (int i = 1; i <= agentManager.getNoOfAgents(" + agentNameMap.get( agentName) + "); ++i) {\n";

					line += _indents[ 3] + "TAgent agent = agentManager.getAgentDB().get(" + agentNameMap.get( agentName) + "+String.valueOf(i));\n";

					// ロールクラスのコンストラクタ引数に初期化された配列がある場合それらの配列を初期化するコマンド文字列を生成して書き出す
					line += write_entity_initial_values_command( "agent", agentObject, roleDataSetForMainStage, "TAgentRole", commandMap);

					line += _indents[ 3] + "TAgentRole agentRole = new TAgentRole(agent, rand";

					// variableInitialValueMapに収納されているロールクラスのコンストラクタ引数を取り出して引数文字列を生成する
					//text = get_role_initialize_parameters_for_common_role( agentObject, variableInitialValueMap, roleDataSet);
					String parameters = get_role_initial_parameters( "agent", agentObject, variableInitialValueMap, roleDataSetForMainStage, "TAgentRole", 4);

					line += ( parameters.startsWith( ",\n") && 100 > parameters.length()) ? ( ", " + parameters.substring( ( ",\n" + JavaProgramExporter._indents[ 4]).length())) : parameters;

					// ロールクラスのコンストラクタ引数に指定する初期値をvariableInitialValueMapへ収集する
					// それらを初期化する必要がある場合は、その初期化コマンドをcommandMapに収納する
					get_entity_initial_values( "agent", agentObject, variableInitialValueMap, commandMap, roleDataSetForMainStage, agentNameMap, spotNameMap, roleClassName, initialValueMap, imports, 3);
					//get_initial_value_map_for_agent( agentObject, variableInitialValueMap, commandMap, roleDataSet, agentNameMap, spotNameMap, initialValueMap, imports, 3);

					// ロールクラスのコンストラクタ引数に初期化された配列がある場合それらの配列を初期化するコマンド文字列を生成して書き出す
					line += write_entity_initial_values_command( "agent", agentObject, roleDataSetForMainStage, roleClassName, commandMap);

					line += _indents[ 3] + roleClassName + " " + get_roleClassLocalVariableName( roleClassName) + " = new " + roleClassName + "(agent, rand";

					// variableInitialValueMapに収納されているロールクラスのコンストラクタ引数を取り出して引数文字列を生成する
					parameters = get_role_initial_parameters( "agent", agentObject, variableInitialValueMap, roleDataSetForMainStage, roleClassName, 4);

					line += ( parameters.startsWith( ",\n") && 100 > parameters.length()) ? ( ", " + parameters.substring( ( ",\n" + JavaProgramExporter._indents[ 4]).length())) : parameters;

					line += _indents[ 3] + get_roleClassLocalVariableName( roleClassName) + ".mergeRole(agentRole); // 統合\n";
					line += _indents[ 3] + "agent.setBaseRole(" + get_roleClassLocalVariableName( roleClassName) + "); // 基本役割の設定\n";
					line += _indents[ 2] + "}\n";
				}
				line += _indents[ 1] + "}\n";
				codes.add( line);
			}
		}
		return text;
	}

	/**
	 *  ロールクラスのコンストラクタ引数に指定する初期値をvariableInitialValueMapへ収集する
	 *  それらを初期化する必要がある場合は、その初期化コマンドをcommandMapに収納する
	 * @param type 
	 * @param entity
	 * @param variableInitialValueMap
	 * @param commandMap
	 * @param roleDataSet
	 * @param agentNameMap
	 * @param spotNameMap
	 * @param roleClassName
	 * @param initialValueMap
	 * @param imports
	 * @param indentSize
	 * @return
	 */
	private static void get_entity_initial_values(String type, PlayerBase entity, Map<String, String> variableInitialValueMap, Map<String, String> commandMap, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, String roleClassName, InitialValueMap initialValueMap, List<String> imports, int indentSize) {
		variableInitialValueMap.clear();
		commandMap.clear();
		for ( String kind:Constant._kinds) {
			List<String> roleClassVariables = roleDataSet.get_roleClassVariables( type, roleClassName, kind);
			if ( null == roleClassVariables)
				continue;
			for ( String roleClassVariable:roleClassVariables) {
				TreeMap<String, Object> map1 = entity._objectMapMap.get( kind);
				if ( null == map1 || map1.isEmpty())
					continue;
				ObjectBase objectBase = ( ObjectBase)map1.get( roleClassVariable.substring( 1));
				if ( null == objectBase)
					continue;
				//System.out.println( objectBase._kind + ", " + objectBase._name);
				String initialValue = objectBase.get_java_program_initial_value( roleDataSet, agentNameMap, spotNameMap, initialValueMap);
				variableInitialValueMap.put( objectBase._name, initialValue);
				if ( initialValue.contains( "TTime"))
					append_import( "soars2.core.TTime", imports);
				if ( VariableObject.class.isInstance( objectBase)) {
					if ( initialValue.equals( ""))
						variableInitialValueMap.put( objectBase._name, "new ArrayList<>()");
					else {
						String command = "ArrayList<Object> " + objectBase._name + " = new ArrayList<>();\n";
						command += initialValue.replaceAll( "\\$list", objectBase._name);
						String[] commands = command.split( "\n");
						command = "";
						for ( String cmd:commands)
							command += _indents[ indentSize] + cmd + "\n";
						commandMap.put( objectBase._name, command);
						variableInitialValueMap.put( objectBase._name, objectBase._name);
					}
				}
			}
		}
	}

	/**
	 * ロールクラスのコンストラクタ引数に初期化された配列がある場合それらの配列を初期化するコマンド文字列を生成して書き出す
	 * @param type 
	 * @param entity
	 * @param roleDataSet
	 * @param roleClassName
	 * @param commandMap
	 * @return
	 */
	private static String write_entity_initial_values_command(String type, PlayerBase entity, RoleDataSet roleDataSet, String roleClassName, Map<String, String> commandMap) {
		String line = "";
		String[] kinds = { "collection", "list"};
		for ( String kind:kinds) {
			TreeMap<String, Object> map1 = entity._objectMapMap.get( kind);
			if ( null == map1 || map1.isEmpty())
				continue;
			Set<String> objectNames = map1.keySet();
			for ( String objectName:objectNames) {
				ObjectBase objectBase = ( ObjectBase)map1.get( objectName);
				if ( roleDataSet.is_role_class_variables( type, roleClassName, kind, "f" + objectBase._name)) {
					String command = commandMap.get( objectBase._name);
					if ( null == command)
						continue;
					line += command;
				}
			}
		}
		return line;
	}

	/**
	 * variableInitialValueMapに収納されているロールクラスのコンストラクタ引数を取り出して引数文字列を生成する(TAgentRoleクラス以外の場合)
	 * @param type 
	 * @param entity
	 * @param variableInitialValueMap
	 * @param roleDataSet
	 * @param roleClassName
	 * @param tabSize
	 * @return
	 */
	private static String get_role_initial_parameters(String type, PlayerBase entity, Map<String, String> variableInitialValueMap, RoleDataSet roleDataSet, String roleClassName, int tabSize) {
		List<String> values = new ArrayList<>();
		if ( !variableInitialValueMap.isEmpty()) {
			for ( String kind:Constant._kinds) {
				TreeMap<String, Object> map1 = entity._objectMapMap.get( kind);
				if ( null == map1 || map1.isEmpty())
					continue;
				Set<String> objectNames = map1.keySet();
				for ( String objectName:objectNames) {
					ObjectBase objectBase = ( ObjectBase)map1.get( objectName);
					if ( roleDataSet.is_role_class_variables( type, roleClassName, kind, "f" + objectBase._name))
						values.add( variableInitialValueMap.get( objectBase._name));
				}
			}
		}

		String parameters = "";
		String text = "";
		for ( int i = 0; i < values.size(); ++i) {
			parameters += ( 0 == i) ? "," : "";
			text += ( text.equals( "") ? "\n" + JavaProgramExporter._indents[ tabSize] : ", ") + values.get( i);
			if ( 100 < text.length()) {
				parameters += text + ( ( values.size() - 1) == i ? "" : ",");
				text = "";
			}
		}

		return parameters += text + ");\n";
	}

	/**
	 * @param codes
	 * @param initialStageRuleDataMap
	 * @param initialStageClassNameMap
	 * @param agentNameMap 
	 * @param spotNameMap 
	 * @param packagePrefix
	 * @param imports
	 * @return
	 */
	private static String doInitialStages(List<String> codes, Map<String, Map<String, Map<String, List<RuleData>>>> initialStageRuleDataMap, Map<String, String> initialStageClassNameMap, Map<String, String> agentNameMap, Map<String, String> spotNameMap, String packagePrefix, List<String> imports) {
		// TODO Auto-generated method stub
		String text = "";

		if ( initialStageRuleDataMap.isEmpty())
			return text;

		for ( int i = 0; i < StageManager.get_instance()._initial_stages.size(); ++i) {
			Stage stage = ( Stage)StageManager.get_instance()._initial_stages.get( i);
			//System.out.println( stage._name);
			String[] types = { "agent", "spot"};
			for ( String type:types) {
				String code = type.equals( "agent")
					? doAgentInitialStages( codes, stage._name, initialStageRuleDataMap.get( type), initialStageClassNameMap, agentNameMap, packagePrefix, imports)
					: doSpotInitialStages( codes, stage._name, initialStageRuleDataMap.get( type), initialStageClassNameMap, spotNameMap, packagePrefix, imports);
				if ( code.equals( ""))
					continue;
				text += ( text.equals( "") ? "" : "\n") + code;
			}
		}

		return text;
	}

	/**
	 * @param codes
	 * @param initialStageName
	 * @param initialStageRuleDataMap
	 * @param initialStageClassNameMap
	 * @param spotNameMap 
	 * @param spotNameMap2 
	 * @param packagePrefix
	 * @param imports
	 * @return
	 */
	private static String doSpotInitialStages(List<String> codes, String initialStageName, Map<String, Map<String, List<RuleData>>> initialStageRuleDataMap, Map<String, String> initialStageClassNameMap, Map<String, String> spotNameMap, String packagePrefix, List<String> imports) {
		String text = "";

		if ( null == initialStageRuleDataMap || initialStageRuleDataMap.isEmpty())
			return text;	// この初期ステージではなにも行われていない

		Vector spots = new Vector<>();
		LayerManager.get_instance().get_spots( spots);
		if ( spots.isEmpty())
			return text;	// この初期ステージではなにも行われていない

		int index = 1;
		Map<Integer, MethodProperty> usedMethodPropertyMap = new HashMap<>();
		//System.out.println( initialStageName);
		Map<String, List<RuleData>> ruleDataMap = initialStageRuleDataMap.get( initialStageName);
		if ( null == ruleDataMap || ruleDataMap.isEmpty())
			return text;	// この初期ステージではなにも行われていない

		Set<String> roleClassNames = ruleDataMap.keySet();
		for ( int j = 0; j < spots.size(); ++j) {
			PlayerBase spot = ( PlayerBase)spots.get( j);
			for ( String roleClassName:roleClassNames) {
				List<RuleData> ruleDataList = ruleDataMap.get( roleClassName);
				for ( RuleData ruleData:ruleDataList) {
					if ( !ruleData._entities.contains( spot._name))
						continue;

					int number = MethodProperty.get( ruleData, spot, usedMethodPropertyMap);
					if ( 0 < number)
						text += _indents[ 2] + "do" + initialStageClassNameMap.get( initialStageName) + String.valueOf( number) + "(" + spotNameMap.get( spot._name) + ", spotManager, rand);\n";
					else {
						text += _indents[ 2] + "do" + initialStageClassNameMap.get( initialStageName) + String.valueOf( index) + "(" + spotNameMap.get( spot._name) + ", spotManager, rand);\n";
						String line = "\n" + _indents[ 1] + "private static void do" + initialStageClassNameMap.get( initialStageName) + String.valueOf( index) + "(String spotName, TSpotManager spotManager, ICRandom rand) {\n";
						if ( spot._number.equals( "")) {
							line += _indents[ 2] + "TSpot spot = spotManager.getSpotDB().get(spotName);\n";
							for ( int k = 0; k < ruleData._rulesList.size(); ++k) {
								if ( ruleData._rulesList.size() - 1 == k)
									line += _indents[ 2] + initialStageClassNameMap.get( initialStageName) + "." + ruleData._methods.get( k) + "(spot, (" + ruleData._roleClassName + ")spot.getBaseRole(), spotManager, rand);\n";
								else {
									line += _indents[ 2] + "if (" + initialStageClassNameMap.get( initialStageName) + "." + ruleData._methods.get( k) + "(spot, (" + ruleData._roleClassName + ")spot.getBaseRole(), spotManager, rand))\n";
									line += _indents[ 3] + "return;\n";
								}
							}
						} else {
							line += _indents[ 2] + "for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {\n";
							line += _indents[ 3] + "TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));\n";
							for ( int k = 0; k < ruleData._rulesList.size(); ++k) {
								if ( ruleData._rulesList.size() - 1 == k)
									line += _indents[ 3] + initialStageClassNameMap.get( initialStageName) + "." + ruleData._methods.get( k) + "(spot, (" + ruleData._roleClassName + ")spot.getBaseRole(), spotManager, rand);\n";
								else {
									line += _indents[ 3] + "if (" + initialStageClassNameMap.get( initialStageName) + "." + ruleData._methods.get( k) + "(spot, (" + ruleData._roleClassName + ")spot.getBaseRole(), spotManager, rand))\n";
									line += _indents[ 4] + "continue;\n";
								}
							}
							line += _indents[ 2] + "}\n";
						}
						line += _indents[ 1] + "}\n";
						codes.add( line);
						//System.out.println( roleClassName + " - " + ruleData._roleClassName);
						usedMethodPropertyMap.put( index++, new MethodProperty( ruleData._roleClassName, ruleData._methods, !spot._number.equals( "")));
						append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "main.init.spot." + initialStageClassNameMap.get( initialStageName), imports);
					}
				}
			}
		}

		return text;
	}

	/**
	 * @param codes
	 * @param initialStageName
	 * @param initialStageRuleDataMap
	 * @param initialStageClassNameMap
	 * @param agentNameMap
	 * @param packagePrefix
	 * @param imports
	 * @return
	 */
	private static String doAgentInitialStages(List<String> codes, String initialStageName, Map<String, Map<String, List<RuleData>>> initialStageRuleDataMap, Map<String, String> initialStageClassNameMap, Map<String, String> agentNameMap, String packagePrefix, List<String> imports) {
		String text = "";

		if ( null == initialStageRuleDataMap || initialStageRuleDataMap.isEmpty())
			return text;

		Vector agents = new Vector<>();
		LayerManager.get_instance().get_agents( agents);
		if ( agents.isEmpty())
			return text;	// この初期ステージではなにも行われていない

		int index = 1;
		Map<Integer, MethodProperty> usedMethodPropertyMap = new HashMap<>();
		//System.out.println( initialStageName);
		Map<String, List<RuleData>> ruleDataMap = initialStageRuleDataMap.get( initialStageName);
		if ( null == ruleDataMap || ruleDataMap.isEmpty())
			return text;	// この初期ステージではなにも行われない

		Set<String> roleClassNameSet = ruleDataMap.keySet();
		List<String> roleClassNames = rearrange( roleClassNameSet);
		for ( int j = 0; j < agents.size(); ++j) {
			PlayerBase agent = ( PlayerBase)agents.get( j);
			for ( String roleClassName:roleClassNames) {
				List<RuleData> ruleDataList = ruleDataMap.get( roleClassName);
				for ( RuleData ruleData:ruleDataList) {
					if ( !ruleData._entities.contains( agent._name))
						continue;

					int number = MethodProperty.get( ruleData, agent, usedMethodPropertyMap);
					if ( 0 < number)
						text += _indents[ 2] + "do" + initialStageClassNameMap.get( initialStageName) + String.valueOf( number) + "(" + agentNameMap.get( agent._name) + ", agentManager, spotManager, rand);\n";
					else {
						text += _indents[ 2] + "do" + initialStageClassNameMap.get( initialStageName) + String.valueOf( index) + "(" + agentNameMap.get( agent._name) + ", agentManager, spotManager, rand);\n";
						String line = "\n" + _indents[ 1] + "private static void do" + initialStageClassNameMap.get( initialStageName) + String.valueOf( index) + "(String agentName, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {\n";
						String getRoleMethod = ( !agent._initialRole.equals( "") && ruleData._roleClassName.equals( "TAgentRole")) ? ".getMergedRole(\"AgentRole\")" : "";
						if ( agent._number.equals( "")) {
							line += _indents[ 2] + "TAgent agent = agentManager.getAgentDB().get(agentName);\n";
							line += _indents[ 2] + "TSpot spot = spotManager.getSpotDB().get(agent.getCurrentSpotName());\n";
							for ( int k = 0; k < ruleData._rulesList.size(); ++k) {
								if ( ruleData._rulesList.size() - 1 == k)
									line += _indents[ 2] + initialStageClassNameMap.get( initialStageName) + "." + ruleData._methods.get( k) + "(agent, spot, (" + ruleData._roleClassName + ")agent.getBaseRole()" + getRoleMethod + ", agentManager, spotManager, rand);\n";
								else {
									line += _indents[ 2] + "if " + initialStageClassNameMap.get( initialStageName) + "." + ruleData._methods.get( k) + "(agent, spot, (" + ruleData._roleClassName + ")agent.getBaseRole()" + getRoleMethod + ", agentManager, spotManager, rand))\n";
									line += _indents[ 3] + "return;\n";
								}
							}
						} else {
							line += _indents[ 2] + "for (int i = 1; i <= agentManager.getNoOfAgents(agentName); ++i) {\n";
							line += _indents[ 3] + "TAgent agent = agentManager.getAgentDB().get(agentName+String.valueOf(i));\n";
							line += _indents[ 3] + "TSpot spot = spotManager.getSpotDB().get(agent.getCurrentSpotName());\n";
							for ( int k = 0; k < ruleData._rulesList.size(); ++k) {
								if ( ruleData._rulesList.size() - 1 == k)
									line += _indents[ 3] + initialStageClassNameMap.get( initialStageName) + "." + ruleData._methods.get( k) + "(agent, spot, (" + ruleData._roleClassName + ")agent.getBaseRole()" + getRoleMethod + ", agentManager, spotManager, rand);\n";
								else {
									line += _indents[ 3] + "if (" + initialStageClassNameMap.get( initialStageName) + "." + ruleData._methods.get( k) + "(agent, spot, (" + ruleData._roleClassName + ")agent.getBaseRole()" + getRoleMethod + ", agentManager, spotManager, rand))\n";
									line += _indents[ 4] + "continue;\n";
								}
							}
							line += _indents[ 2] + "}\n";
						}
						line += _indents[ 1] + "}\n";
						codes.add( line);
						//System.out.println( roleClassName + " - " + ruleData._roleClassName);
						usedMethodPropertyMap.put( index++, new MethodProperty( ruleData._roleClassName, ruleData._methods, !agent._number.equals( "")));
						append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "main.init.agent." + initialStageClassNameMap.get( initialStageName), imports);
					}
				}
			}
		}

		return text;
	}

	/**
	 * @param roleClassNameSet
	 * @return
	 */
	private static List<String> rearrange(Set<String> roleClassNameSet) {
		// TODO Auto-generated method stub
		List<String> roleClassNames = new ArrayList<>( roleClassNameSet);
		if ( !roleClassNames.contains( "TAgentRole"))
			return roleClassNames;

		if ( roleClassNames.get( 0).equals( "TAgentRole"))
			return roleClassNames;

		roleClassNames.remove( "TAgentRole");
		roleClassNames.add( 0, "TAgentRole");
		return roleClassNames;
	}

	/**
	 * @param roleDataSet 
	 * @param stageNameMap
	 * @param agentNameMap
	 * @param spotNameMap
	 * @param agentCommonVariableMap
	 * @param variableMap
	 * @param roleClassNameMap
	 * @param roleOwnersMap
	 */
	private static void debug(RoleDataSet roleDataSet, Map<String, String> stageNameMap, Map<String, String> agentNameMap, Map<String, String> spotNameMap) {
		// TODO Auto-generated method stub
		System.out.println( "stageNameMap");
		roleDataSet.debug1( stageNameMap);
		System.out.println( "\nagentNameMap");
		roleDataSet.debug1( agentNameMap);
		System.out.println( "\nspotNameMap");
		roleDataSet.debug1( spotNameMap);
		roleDataSet.debug();
	}

	/**
	 * @param ruleDataMap
	 */
	public static void print(Map<String, List<RuleData>> ruleDataMap) {
		// TODO Auto-generated method stub
		Set<String> stageNames = ruleDataMap.keySet();
		for ( String stageName:stageNames) {
			System.out.println( _indents[ 1] + "Stage : " + stageName);
			List<RuleData> ruleDataList = ruleDataMap.get( stageName);
			for ( RuleData ruleData:ruleDataList)
				ruleData.print1( _indents[ 2]);
		}
	}
}
