/**
 * 
 */
package soars.application.visualshell.file.exporter.java.object;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import soars.application.visualshell.file.exporter.java.JavaProgramExporter;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.player.agent.AgentObject;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.stage.Stage;
import soars.application.visualshell.object.stage.StageManager;

/**
 * @author kurata
 *
 */
public class RoleDataSet {

	/**
	 * Variable kind - Variable names - Variable object(ObjectBase)
	 */
	public Map<String, TreeMap<String, Object>> _agentCommonVariableMap = new HashMap<>();

	/**
	 * type("agent" or "spot") - Role class name - Variable kind - Variable name list
	 */
	public Map<String, Map<String, Map<String, List<String>>>> _variableMap = new HashMap<>();

	/**
	 * Role name - Role class name
	 */
	public Map<String, String> _roleClassNameMap = new TreeMap<>();

	/**
	 * Role class name - Role class code
	 */
	public Map<String, RoleClassCode> _roleClassCodeMap = new HashMap<>();

	/**
	 * Role class name - PlayerBase list
	 */
	public Map<String, List<PlayerBase>> _roleOwnersMap = new HashMap<>();

	/**
	 * Stage name - RuleData list
	 */
	public Map<String, List<RuleData>> _ruleDataMap = new HashMap<>();

	/**
	 * type("agent" or "spot") - Rule class name - Role class name list
	 */
	public Map<String, Map<String, List<String>>> _ruleOwnerRoleClassNamesMap = new HashMap<>();

	/**
	 * type("agent" or "spot") - Rule class name - Variable kind - Variable name - Role class name list
	 */
	public Map<String, Map<String, Map<String, Map<String, List<String>>>>> _sameVariableRoleClassNamesMap = new HashMap<>();

	/**
	 * Rule class name - Variable kind - Variable name - Role class name list
	 */
	public Map<String, Map<String, Map<String, List<String>>>> _currentSporRoleClassNamesMap = new HashMap<>();

	/**
	 * type("agent" or "spot") - Stage name - Role class name - RuleData list
	 */
	public Map<String, Map<String, Map<String, List<RuleData>>>> _initialStageRuleDataMap = new HashMap<>();

	/**
	 * Initial stage name - Initial stage class name
	 */
	public Map<String, String> _initialStageClassNameMap = new HashMap<>();

	/**
	 * 
	 */
	public RoleDataSet() {
	}

	/**
	 * @param kind
	 * @param name 
	 * @return
	 */
	public boolean is_agent_common_variable(String kind, String name) {
		// TODO Auto-generated method stub
		if ( null == _agentCommonVariableMap.get( kind))
			return false;
		return ( null != _agentCommonVariableMap.get( kind).get( name));
	}

	/**
	 * @param type
	 * @return
	 */
	public List<String> get_roleClassNames(String type) {
		List<String> roleClassNames = new ArrayList<>();
		Set<String> roleNames = _roleClassNameMap.keySet();
		for ( String roleName:roleNames) {
			if ( ( type.equals( "agent") && LayerManager.get_instance().is_agent_role_name( roleName))
				|| ( type.equals( "spot") && LayerManager.get_instance().is_spot_role_name( roleName)))
				roleClassNames.add( _roleClassNameMap.get( roleName));
		}
		return roleClassNames;
	}

	/**
	 * @param type
	 * @param ruleData
	 * @return
	 */
	public List<String> get_ruleOwnerRoleClassNames(String type, RuleData ruleData) {
		// TODO Auto-generated method stub
		if ( null == _ruleOwnerRoleClassNamesMap.get( type))
			return null;

		return _ruleOwnerRoleClassNamesMap.get( type).get( ruleData._ruleClassName);
//		return ( null != _ruleOwnerRoleClassNamesMap.get( type))
//			? _ruleOwnerRoleClassNamesMap.get( type).get( ruleData._ruleClassName)
//			: null;
	}

	/**
	 * @param type
	 * @param kind
	 * @param name
	 * @return
	 */
	public List<String> get_variableOwnerRoleClassNames(String type, String kind, String name) {
		// TODO Auto-generated method stub
		// type("agent" or "spot") - Role class name - Variable kind - Variable name list
		List<String> roleClassNames = new ArrayList<>();
		if ( null != _variableMap.get( type)) {
			Set<String> rcns = _variableMap.get( type).keySet();
			for ( String rcn:rcns) {
				if ( null == _variableMap.get( type).get( rcn))
					continue;
				List<String> variableNames = _variableMap.get( type).get( rcn).get( kind);
				if ( null == variableNames)
					continue;
				if ( variableNames.contains( "f" + name))
					roleClassNames.add( rcn);
			}
		}
		return roleClassNames;
	}

	/**
	 * @param type
	 * @param roleClassName
	 * @param kind
	 * @return
	 */
	public List<String> get_roleClassVariables(String type, String roleClassName, String kind) {
		// TODO Auto-generated method stub
		if ( null != _variableMap.get( type)) {
			if ( null != _variableMap.get( type).get( roleClassName))
			return _variableMap.get( type).get( roleClassName).get( kind);
		}
		return null;
	}

	/**
	 * @param string
	 * @param roleClassName
	 * @param kind
	 * @param variableName
	 * @return
	 */
	public boolean is_role_class_variables(String type, String roleClassName, String kind, String variableName) {
		// TODO Auto-generated method stub
		if ( null != _variableMap.get( type)) {
			if ( null != _variableMap.get( type).get( roleClassName))
				if ( null != _variableMap.get( type).get( roleClassName).get( kind))
					return _variableMap.get( type).get( roleClassName).get( kind).contains( variableName);
		}
		return false;
	}

	/**
	 * @return
	 */
	public boolean create_ruleOwnerRolesMap() {
		// TODO Auto-generated method stub
		//System.out.println( ruleData._ruleClassName + " -> " + roleDataSet._roleClassNameMap.get( ruleData._roleName));
		_ruleOwnerRoleClassNamesMap.put( "agent", new HashMap<>());
		_ruleOwnerRoleClassNamesMap.put( "spot", new HashMap<>());
		Collection<List<RuleData>> ruleDataLists = _ruleDataMap.values();
		for ( List<RuleData> ruleDataList:ruleDataLists) {
			for ( RuleData ruleData:ruleDataList) {
				List<String> ownerRoles = _ruleOwnerRoleClassNamesMap.get( ruleData._type).get( ruleData._ruleClassName);
				if ( null == ownerRoles) {
					ownerRoles = new ArrayList<>();
					_ruleOwnerRoleClassNamesMap.get( ruleData._type).put( ruleData._ruleClassName, ownerRoles);
				}
				String roleClassName = _roleClassNameMap.get( ruleData._roleName);
				if ( !ownerRoles.contains( roleClassName))
					ownerRoles.add( roleClassName);
			}
		}

		String[] types = { "agent", "spot"};
		for ( String type:types) {
			_sameVariableRoleClassNamesMap.put( type, new HashMap<>());
			Set<String> ruleClassNames1 = _ruleOwnerRoleClassNamesMap.get( type).keySet();
			for ( String ruleClassName1:ruleClassNames1) {
				List<String> ruleOwnerRoleClassNames = _ruleOwnerRoleClassNamesMap.get( type).get( ruleClassName1);
				if ( 2 > ruleOwnerRoleClassNames.size())	// このルールクラスを保持しているロールクラスは１つしかないから特定可能
					continue;
				if ( type.equals( "agent")) {	// エージェントのルールクラス
					if ( 2 == ruleOwnerRoleClassNames.size() && ruleOwnerRoleClassNames.contains( "TAgentRole"))	 // このルールクラスを保持するロールクラスが２つでうち１つがTAgentRoleだから特定可能
						continue;
				}

				List<String> ruleClassNames2 = new ArrayList<String>();
				for ( String roleClassName2:ruleOwnerRoleClassNames) {
					if ( roleClassName2.equals( "TAgentRole"))
						continue;

					ruleClassNames2.add( roleClassName2);
				}
				for ( String kind:Constant._kinds) {
					List<String> roleClassNames3 = new ArrayList<>();
					for ( String roleClassName2:ruleClassNames2) {
						Map<String, List<String>> map = _variableMap.get( type).get( roleClassName2);
						if ( null == map)
							continue;

						List<String> list = map.get( kind);
						if ( null == list)
							continue;

						roleClassNames3.add( roleClassName2);	// kindの変数を持っている
					}
					if ( 2 > roleClassNames3.size())	// kindの変数を持っているのは１つのロールクラスだけ
						continue;

					for ( int i = 0; i < roleClassNames3.size(); ++i) {
						List<String> list1 = _variableMap.get( type).get( roleClassNames3.get( i)).get( kind);
						for ( int j = 0; j < roleClassNames3.size(); ++j) {
							if ( i == j)
								continue;

							List<String> list2 = _variableMap.get( type).get( roleClassNames3.get( j)).get( kind);
							for ( String name:list1) {
								if ( list2.contains( name)) {
									Map<String, Map<String, List<String>>> map1 = _sameVariableRoleClassNamesMap.get( type).get( ruleClassName1);
									if ( null == map1) {
										map1 = new HashMap<>();
										_sameVariableRoleClassNamesMap.get( type).put( ruleClassName1, map1);
									}
									Map<String, List<String>> map2 = _sameVariableRoleClassNamesMap.get( type).get( ruleClassName1).get( kind);
									if ( null == map2) {
										map2 = new HashMap<>();
										_sameVariableRoleClassNamesMap.get( type).get( ruleClassName1).put( kind, map2);
									}
									List<String> list3 = _sameVariableRoleClassNamesMap.get( type).get( ruleClassName1).get( kind).get( name);
									if ( null == list3) {
										list3 = new ArrayList<>();
										_sameVariableRoleClassNamesMap.get( type).get( ruleClassName1).get( kind).put( name, list3);
									}
									if ( !list3.contains( roleClassNames3.get( i)))
										list3.add( roleClassNames3.get( i));
									if ( !list3.contains( roleClassNames3.get( j)))
										list3.add( roleClassNames3.get( j));
								}
							}
						}
					}
				}
			}
		}
		//debug();
		//debug2( _agentCommonVariableMap);
		//debug5();
		//debug6();
		//debug3( _variableMap);
		return true;
	}

	/**
	 * @param roleClassName
	 * @param type
	 * @param ruleClassName
	 * @param variableKind
	 * @param variableName
	 * @return
	 */
	public boolean has_this_role_class_name(String roleClassName, String type, String ruleClassName, String variableKind, String variableName) {
		// TODO Auto-generated method stub
		if ( null != _sameVariableRoleClassNamesMap.get( type)) {
			if ( null != _sameVariableRoleClassNamesMap.get( type).get( ruleClassName)) {
				if ( null != _sameVariableRoleClassNamesMap.get( type).get( ruleClassName).get( variableKind)) {
					if ( null != _sameVariableRoleClassNamesMap.get( type).get( ruleClassName).get( variableKind).get( variableName))
						return _sameVariableRoleClassNamesMap.get( type).get( ruleClassName).get( variableKind).get( variableName).contains( roleClassName);
				}
			}
		}
		return false;
	}

	/**
	 * @param string
	 * @param ruleData
	 * @param variable
	 * @return
	 */
	public List<String> get_sameVariableRoleClassNames(String type, RuleData ruleData, Variable variable) {
		// TODO Auto-generated method stub
		if ( null != _sameVariableRoleClassNamesMap.get( type)) {
			if ( null != _sameVariableRoleClassNamesMap.get( type).get( ruleData._ruleClassName)) {
				if ( null != _sameVariableRoleClassNamesMap.get( type).get( ruleData._ruleClassName).get( variable._kind)) {
					if ( null != _sameVariableRoleClassNamesMap.get( type).get( ruleData._ruleClassName).get( variable._kind).get( "f" + variable._name))
						return _sameVariableRoleClassNamesMap.get( type).get( ruleData._ruleClassName).get( variable._kind).get( "f" + variable._name);
				}
			}
		}
		return null;
	}

	/**
	 * @param ruleData
	 * @param variable
	 * @param roleClassNames
	 */
	public void set_current_spot_roel_data(RuleData ruleData, Variable variable, List<String> roleClassNames) {
		// TODO Auto-generated method stub
		Map<String, Map<String, List<String>>> map1 = _currentSporRoleClassNamesMap.get( ruleData._ruleClassName);
		if ( null == map1) {
			map1 = new HashMap<>();
			_currentSporRoleClassNamesMap.put( ruleData._ruleClassName, map1);
		}
		Map<String, List<String>> map2 = map1.get( variable._kind);
		if ( null == map2) {
			map2 = new HashMap<>();
			map1.put( variable._kind, map2);
		}
		map2.put( variable._name, roleClassNames);
	}

	/**
	 * _ruleDataMapから_initialStageRuleDataMapを生成する
	 */
	public void create_initialStageRuleDataMap() {
		// TODO Auto-generated method stub
		String[] types = { "agent", "spot"};
		for ( String type:types) {
			_initialStageRuleDataMap.put( type, new HashMap<>());
			for ( int i = 0; i < StageManager.get_instance()._initial_stages.size(); ++i) {
				Stage initialStage = ( Stage)StageManager.get_instance()._initial_stages.get( i);
				if ( initialStage._name.equals( Constant._initializeChartStageName))
					continue;

				List<RuleData> srcRuleDataList = _ruleDataMap.get( initialStage._name);
				if ( null == srcRuleDataList || srcRuleDataList.isEmpty())
					continue;

				Map<String, List<RuleData>> ruleDataMap = _initialStageRuleDataMap.get( type).get( initialStage._name);
				if ( null == ruleDataMap) {
					ruleDataMap = new HashMap<>();
					_initialStageRuleDataMap.get( type).put( initialStage._name, ruleDataMap);
				}

				for ( RuleData srcRuleData:srcRuleDataList) {
					if ( !role_name_correct( type, srcRuleData._roleClassName))
						continue;

					List<RuleData> dstRuleDataList = ruleDataMap.get( srcRuleData._roleClassName);
					if ( null == dstRuleDataList) {
						dstRuleDataList = new ArrayList<>();
						ruleDataMap.put( srcRuleData._roleClassName, dstRuleDataList);
					}
					dstRuleDataList.add( srcRuleData);
					srcRuleData.set_entities();
				}
			}
		}
	}

	/**
	 * @param type
	 * @param roleClassName
	 * @return
	 */
	private boolean role_name_correct(String type, String roleClassName) {
		// TODO Auto-generated method stub
		if ( type.equals( "agent") && roleClassName.equals( "TAgentRole"))
			return true;

		Vector roles = new Vector<>();
		if ( type.equals( "agent"))
			LayerManager.get_instance().get_agent_roles( roles);
		else
			LayerManager.get_instance().get_spot_roles( roles);

		for ( int i = 0; i < roles.size(); ++i) {
			if ( _roleClassNameMap.get( ( ( Role)roles.get( i))._name).equals( roleClassName))
				return true;
		}

		return false;
	}

	/**
	 * 
	 */
	public void debug() {
		System.out.println( "\nagentCommonVariableMap");
		debug2( _agentCommonVariableMap);
		System.out.println( "\nvariableMap");
		debug3( _variableMap);
		System.out.println( "\nroleClassNameMap");
		debug1( _roleClassNameMap);
		System.out.println( "\nroleOwnersMap");
		debug4( _roleOwnersMap);
	}

	/**
	 * @param map
	 */
	public void debug1(Map<String, String> map) {
		// TODO Auto-generated method stub
		Iterator iterator = map.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			System.out.println( JavaProgramExporter._indents[ 1] + entry.getKey().toString() + " -> " + entry.getValue().toString());
		}
	}

	/**
	 * @param map
	 */
	private void debug2(Map<String, TreeMap<String, Object>> map) {
		// TODO Auto-generated method stub
		Iterator iterator = map.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			System.out.println( JavaProgramExporter._indents[ 1] + entry.getKey().toString());
			TreeMap<String, Object> m = ( TreeMap<String, Object>)entry.getValue();
			Iterator it = m.entrySet().iterator();
			while ( it.hasNext()) {
				Object ob = it.next();
				Map.Entry ent = ( Map.Entry)ob;
				System.out.println( JavaProgramExporter._indents[ 2] + ent.getKey().toString()/* + " -> " + ent.getValue()*/);
			}
		}
	}

	/**
	 * @param map
	 */
	private void debug3(Map<String, Map<String, Map<String, List<String>>>> map) {
		// TODO Auto-generated method stub
		Set<String> types = map.keySet();
		for ( String type:types) {
			Map<String, Map<String, List<String>>> m1 = map.get( type);
			if ( null == m1)
				continue;
			System.out.println( JavaProgramExporter._indents[ 1] + type);
			Iterator iterator = m1.entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				System.out.println( JavaProgramExporter._indents[ 2] + entry.getKey().toString());
				Map<String, List<String>> m2 = ( Map<String, List<String>>)entry.getValue();
				Iterator it = m2.entrySet().iterator();
				while ( it.hasNext()) {
					Object ob = it.next();
					Map.Entry ent = ( Map.Entry)ob;
					System.out.println( JavaProgramExporter._indents[ 3] + ent.getKey().toString() + " -> " + ent.getValue());
				}
			}
		}
	}

	/**
	 * @param map
	 */
	private void debug4(Map<String, List<PlayerBase>> map) {
		// TODO Auto-generated method stub
		Iterator iterator = map.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			System.out.println( JavaProgramExporter._indents[ 1] + entry.getKey().toString()/* + " -> " + ( ( null == entry.getValue()) ? "null" : entry.getValue().toString())*/);
			if ( null == entry.getValue())
				continue;
			List<PlayerBase> playerBases = ( List<PlayerBase>)entry.getValue();
			for ( PlayerBase playerBase:playerBases)
				System.out.println( JavaProgramExporter._indents[ 2] + ( AgentObject.class.isInstance( playerBase) ? "agent : " : "spot : ") + playerBase._name);
		}
	}

	/**
	 * 
	 */
	public void debug5() {
		// TODO Auto-generated method stub
		Set<String> types = _ruleOwnerRoleClassNamesMap.keySet();
		for ( String type:types) {
			System.out.println( type);
			//for ( String roleClassName:_ruleOwnerRoleClassNamesMap.get( ruleClassName))
			//	System.out.println( ruleClassName + " -> " + _ruleOwnerRoleClassNamesMap.get( ruleClassName));
			Map<String, List<String>> map1 = _ruleOwnerRoleClassNamesMap.get( type);
			Set<String> ruleClassNames = map1.keySet();
			for ( String ruleClassName:ruleClassNames) {
				System.out.println( JavaProgramExporter._indents[ 1] + ruleClassName);
				List<String> roleClassNames = _ruleOwnerRoleClassNamesMap.get( type).get( ruleClassName);
				System.out.println( JavaProgramExporter._indents[ 2] + roleClassNames);
				//for ( String roleClassName:roleClassNames)
				//	System.out.println( JavaProgramExporter._indents[ 2] + roleClassNames);
			}
		}
	}

	/**
	 * 
	 */
	public void debug6() {
		// TODO Auto-generated method stub
		Set<String> types = _sameVariableRoleClassNamesMap.keySet();
		for ( String type:types) {
			System.out.println( type);
			Set<String> ruleClassNames = _sameVariableRoleClassNamesMap.get( type).keySet();
			for ( String ruleClassName:ruleClassNames) {
				System.out.println( JavaProgramExporter._indents[ 1] + ruleClassName);
				Set<String> kinds = _sameVariableRoleClassNamesMap.get( type).get( ruleClassName).keySet();
				for ( String kind:kinds) {
					System.out.println( JavaProgramExporter._indents[ 2] + kind);
					Set<String> names = _sameVariableRoleClassNamesMap.get( type).get( ruleClassName).get( kind).keySet();
					for ( String name:names) {
						System.out.println( JavaProgramExporter._indents[ 3] + name);
						System.out.println( JavaProgramExporter._indents[ 4] + _sameVariableRoleClassNamesMap.get( type).get( ruleClassName).get( kind).get( name));
					}
				}
			}
		}
	}

	/**
	 * 
	 */
	public void debug7() {
		// TODO Auto-generated method stub
		Set<String> types = _initialStageRuleDataMap.keySet();
		for ( String type:types) {
			System.out.println( type);
			Set<String> initialStageNames = _initialStageRuleDataMap.get( type).keySet();
			for ( String initialStageName:initialStageNames) {
				System.out.println( JavaProgramExporter._indents[ 1] + initialStageName);
				Set<String> roleClassNames = _initialStageRuleDataMap.get( type).get( initialStageName).keySet();
				for ( String roleClassName:roleClassNames) {
					System.out.println( JavaProgramExporter._indents[ 2] + roleClassName);
					List<RuleData> ruleDataList = _initialStageRuleDataMap.get( type).get( initialStageName).get( roleClassName);
					for ( RuleData ruleData:ruleDataList)
						ruleData.print2( JavaProgramExporter._indents[ 3]);
				}
			}
		}
	}

//	/**
//	 * 
//	 */
//	public void debug8() {
//		// TODO Auto-generated method stub
//		Set<String> types = _ruleOwnerRoleClassNamesMap.keySet();
//		for ( String type:types) {
//			System.out.println( type);
//			Set<String> ruleClassNames = _ruleOwnerRoleClassNamesMap.get( type).keySet();
//			for ( String ruleClassName:ruleClassNames) {
//				System.out.println( JavaProgramExporter._indents[ 1] + ruleClassName);
//				List<String> roleClassNames = _ruleOwnerRoleClassNamesMap.get( type).get( ruleClassName);
//				for ( String roleClassName:roleClassNames)
//					System.out.println( JavaProgramExporter._indents[ 2] + roleClassName);
//			}
//		}
//	}

	/**
	 * 
	 */
	public void debug8() {
		// TODO Auto-generated method stub
		Set<String> stageNames = _ruleDataMap.keySet();
		for ( String stageName:stageNames) {
			System.out.println( stageName);
			List<RuleData> ruleDataList = _ruleDataMap.get( stageName);
			for ( RuleData ruleData:ruleDataList)
				ruleData.print2( JavaProgramExporter._indents[ 3]);
		}
	}
}
