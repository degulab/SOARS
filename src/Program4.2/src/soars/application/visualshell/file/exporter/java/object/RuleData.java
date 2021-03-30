/**
 * 
 */
package soars.application.visualshell.file.exporter.java.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import soars.application.visualshell.file.exporter.java.JavaProgramExporter;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.role.base.object.Rules;
import soars.application.visualshell.object.role.base.object.base.Rule;

/**
 * @author kurata
 *
 */
public class RuleData {

	/**
	 * "agent" or "spot"
	 */
	public String _type = "";

	/**
	 * 初期ステージ実行の為に使用
	 */
	public List<String> _entities = new ArrayList<>();

	/**
	 * SOARSのロール名
	 */
	public String _roleName = "";

	/**
	 * ロールクラス名
	 */
	public String _roleClassName = "";

	/**
	 * ルールクラス名
	 */
	public String _ruleClassName = "";

	/**
	 * 各Rule１つにつき１メソッドが必要
	 */
	public List<Rules> _rulesList = new ArrayList<>();

	/**
	 * メインステージのルールクラス生成時に時刻条件(@8:00等)を含むRulesをまとめる為に使用
	 */
	public String _time = "";

	/**
	 * 初期ステージ実行の為に使用(各Rules１つに対応すメソドの配列 - Rulesの順に並んでいる)
	 */
	public List<String>  _methods = new ArrayList<>();

	/**
	 * 
	 */
	public RuleData() {
		
	}

	/**
	 * @param type
	 * @param roleName
	 * @param rules
	 */
	public RuleData(String type, String roleName, Rules rules) {
		_type = type;
		_roleName = roleName;
		_rulesList.add( rules);
	}

	/**
	 * @param type
	 * @param roleName
	 * @param rules
	 * @param time
	 */
	public RuleData(String type, String roleName, Rules rules, String time) {
		_type = type;
		_roleName = roleName;
		_rulesList.add( rules);
		_time = time;
	}

	/**
	 * @param dstRuleDataList
	 * @param roleName
	 * @param stageName
	 */
	public void set_ruleClassName(List<RuleData> dstRuleDataList, String roleName, String stageName) {
		// TODO Auto-generated method stub
		for ( RuleData ruleData:dstRuleDataList) {
			if ( same_as( ruleData)) {
				_ruleClassName = ruleData._ruleClassName;
				return;
			}
		}
		int index = 1;
		while ( true) {
			String className = JavaProgramExporter.get_rule_class_name( roleName, stageName + String.valueOf( index++));
			if ( !has_same_rule_class_name( className, dstRuleDataList)) {
				_ruleClassName = className;
				return;
			}
		}
	}

	/**
	 * @param ruleData
	 * @return
	 */
	private boolean same_as(RuleData ruleData) {
		// TODO Auto-generated method stub
//		if ( !_time.equals( ruleData._time))
//			return false;
		if ( ( _time.equals( "") && !ruleData._time.equals( ""))
			|| ( !_time.equals( "") && ruleData._time.equals( "")))
			return false;

		if ( _rulesList.size() != ruleData._rulesList.size())
			return false;

		for ( int i = 0; i < _rulesList.size(); ++i) {
			if ( !_rulesList.get( i).same_as_without_time( ruleData._rulesList.get( i)))
				return false;
		}

		return true;
	}

	/**
	 * @param ruleClassName
	 * @param dstRuleDataList
	 * @return
	 */
	private boolean has_same_rule_class_name(String ruleClassName, List<RuleData> dstRuleDataList) {
		// TODO Auto-generated method stub
		for ( RuleData ruleData:dstRuleDataList) {
			if ( ruleData._ruleClassName.equals( ruleClassName))
				return true;
		}
		return false;
	}

	/**
	 * 
	 */
	public void set_entities() {
		// TODO Auto-generated method stub
		Vector entities = new Vector();
		if ( _type.equals( "spot"))
			LayerManager.get_instance().get_spots( entities);
		else {
			LayerManager.get_instance().get_agents( entities);
			if ( _roleName.equals( "")) {
				for ( int i = 0; i < entities.size(); ++i) {
					PlayerBase entity = ( PlayerBase)entities.get( i);
					if ( !_entities.contains( entity._name))
						_entities.add( entity._name);
				}
				return;
			}
		}

		for ( int i = 0; i < entities.size(); ++i) {
			PlayerBase entity = ( PlayerBase)entities.get( i);
			if ( _roleName.equals( entity._initialRole) && !_entities.contains( entity._name))
				_entities.add( entity._name);
		}
	}

	/**
	 * @param indent
	 */
	public void print1(String indent) {
		// TODO Auto-generated method stub
		System.out.println( indent + "method : " + _methods);
		System.out.println( indent + "type : " + _type);
		System.out.println( indent + "roleName : " + _roleName);
		System.out.println( indent + "roleClassName : " + _roleClassName);
		System.out.println( indent + "className : " + _ruleClassName);
		System.out.println( indent + "time : " + _time);
		System.out.println( indent + "entities : " + _entities);
//		int index = 1;
//		for ( Rules rules:_rulesList) {
//			for ( Rule rule:rules)
//				System.out.println( indent + JavaProgramExporter._indents[ 1] + String.valueOf( index) + "." + rule._kind + " : " + rule._type + " : " + rule._value);
//			++index;
//		}
	}

	/**
	 * @param indent
	 */
	public void print2(String indent) {
		// TODO Auto-generated method stub
		System.out.println( indent + "method : " + _methods);
		System.out.println( indent + JavaProgramExporter._indents[ 1] + "type : " + _type);
		System.out.println( indent + JavaProgramExporter._indents[ 1] + "roleName : " + _roleName);
		System.out.println( indent + JavaProgramExporter._indents[ 1] + "roleClassName : " + _roleClassName);
		System.out.println( indent + JavaProgramExporter._indents[ 1] + "className : " + _ruleClassName);
		System.out.println( indent + JavaProgramExporter._indents[ 1] + "time : " + _time);
		System.out.println( indent + JavaProgramExporter._indents[ 1] + "entities : " + _entities);
		int index = 1;
		for ( Rules rules:_rulesList) {
			for ( Rule rule:rules)
				System.out.println( indent + JavaProgramExporter._indents[ 1] + String.valueOf( index) + "." + rule._kind + " : " + rule._type + " : " + rule._value);
			++index;
		}
	}

	/**
	 * @param initialValueMap 
	 * @param commands 
	 * @param imports 
	 * @param spotNameMap 
	 * @param agentNameMap 
	 * @param roleClassNameMap
	 * @param packagePrefix
	 * @param indent
	 */
	public void print(RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, InitialValueMap initialValueMap, String packagePrefix, String indent) {
		// TODO Auto-generated method stub
		System.out.println( indent + "type : " + _type);
		System.out.println( indent + "roleName : " + _roleName);
		System.out.println( indent + "roleClassName : " + roleDataSet._roleClassNameMap.get( _roleName));
		System.out.println( indent + "className : " + _ruleClassName);
		System.out.println( indent + "time : " + _time);
		System.out.println( indent + "methos : " + _methods);
		System.out.println( indent + "entities : " + _entities);
//		int index = 1;
//		for ( Rules rules:_rulesList) {
//			for ( Rule rule:rules) {
//				System.out.println( indent + JavaProgramExporter._indents[ 1] + String.valueOf( index) + "." + rule._kind + " : " + rule._type + " : " + rule._value);
//				System.out.println( indent + JavaProgramExporter._indents[ 1] + "java program : " + rule.get_java_program(
//					this,
//					roleDataSet,
//					//roleDataSet._roleClassNameMap,
//					agentNameMap,
//					spotNameMap,
//					imports,
//					commands,
//					initialValueMap,
//					packagePrefix,
//					LayerManager.get_instance().get_role( _roleName),
//					true));
//			}
//			++index;
//		}
	}
}
