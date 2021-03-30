/*
 * 2005/05/10
 */
package soars.application.visualshell.object.role.base.object;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.exporter.java.JavaProgramExporter;
import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
import soars.application.visualshell.file.exporter.java.object.RuleData;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.base.Role;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class RuleManager extends Vector<Rules> {

	/**
	 * 
	 */
	public static final int _ruleTableRowMinimumCount = 100;

	/**
	 * 
	 */
	private static final int _ruleTableColumnMinimumCount = 51;

	/**
	 * 
	 */
	public static final int _defaultRuleTableColumnWidth = 150;

	/**
	 * 
	 */
	public int _columnCount = get_column_minimum_count();

	/**
	 * 
	 */
	public Vector<Integer> _columnWidths = new Vector<Integer>();

	/**
	 * @return
	 */
	public static int get_row_minimum_count() {
		return _ruleTableRowMinimumCount;
	}

	/**
	 * @return
	 */
	public static int get_column_minimum_count() {
		return _ruleTableColumnMinimumCount;
	}

	/**
	 * 
	 */
	public RuleManager() {
		super();
		for ( int i = 0; i < _ruleTableRowMinimumCount; ++i)
			add( new Rules());
	}

	/**
	 * @param ruleManager
	 */
	public RuleManager(RuleManager ruleManager) {
		super();
		copy( ruleManager);
	}

	/**
	 * @param ruleManager
	 */
	private void copy(RuleManager ruleManager) {
		cleanup();
		_columnCount = ruleManager._columnCount;
		for ( Rules rules:ruleManager)
			add( new Rules( rules));
		for ( Integer integer:ruleManager._columnWidths)
			_columnWidths.add( Integer.valueOf( integer.intValue()));
	}

	/**
	 * 
	 */
	public void cleanup() {
		_columnCount = get_column_minimum_count();

		_columnWidths.clear();

		for ( Rules rules:this)
			rules.cleanup();

		clear();
	}

	/**
	 * @param ruleManager
	 */
	public boolean update(RuleManager ruleManager) {
		if ( isEmpty())
			addAll( ruleManager);
		else {
			int index;
			if ( is_empty())
				index = 0;
			else {
				index = size() - 1;
				for ( ; index >= 0; --index) {
					Rules rules = get( index);
					if ( !rules.is_empty())
						break;
				}

				++index;
			}

			for ( Rules rules:ruleManager) {
				if ( rules.is_empty())
					continue;

				if ( size() <= index)
					add( rules);
				else
					set( index, rules);

				++index;
			}
		}

		if ( _columnCount < ruleManager._columnCount) {
			_columnCount = ruleManager._columnCount;
			while ( _columnWidths.size() < _columnCount)
				_columnWidths.add( Integer.valueOf( _defaultRuleTableColumnWidth));
//				_command_column_widths.add( Integer.valueOf( _defaultRuleTableColumnWidth));
//			}
		}

		return true;
	}

	/**
	 * @return
	 */
	private boolean is_empty() {
		for ( Rules rules:this) {
			if ( !rules.is_empty())
				return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public int get_max_column_count() {
		int column = 0;
		for ( Rules rules:this)
			column = Math.max( column, rules.get_max_column_count());

		return column;
	}

	/**
	 * @param role
	 */
	public void on_paste(Role role) {
		for ( Rules rules:this)
			rules.on_paste( role);
	}

	/**
	 * @param roleName
	 * @param headName
	 * @param ranges
	 * @return
	 */
	public boolean can_adjust_agent_name(String roleName, String headName, Vector ranges) {
		boolean result = true;
		int index = 0;
		for ( Rules rules:this) {
			if ( !rules.can_adjust_agent_name( roleName, index++, headName, ranges))
				result = false;
		}
		return result;
	}

	/**
	 * @param roleName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public boolean can_adjust_agent_name(String roleName, String headName, Vector ranges, String newHeadName, Vector newRanges) {
		boolean result = true;
		int index = 0;
		for ( Rules rules:this) {
			if ( !rules.can_adjust_agent_name( roleName, index++, headName, ranges, newHeadName, newRanges))
				result = false;
		}
		return result;
	}

	/**
	 * @param roleName
	 * @param headName
	 * @param ranges
	 * @return
	 */
	public boolean can_adjust_spot_name(String roleName, String headName, Vector ranges) {
		boolean result = true;
		int index = 0;
		for ( Rules rules:this) {
			if ( !rules.can_adjust_spot_name( roleName, index++, headName, ranges))
				result = false;
		}
		return result;
	}

	/**
	 * @param roleName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public boolean can_adjust_spot_name(String roleName, String headName, Vector ranges, String newHeadName, Vector newRanges) {
		boolean result = true;
		int index = 0;
		for ( Rules rules:this) {
			if ( !rules.can_adjust_spot_name( roleName, index++, headName, ranges, newHeadName, newRanges))
				result = false;
		}
		return result;
	}

	/**
	 * @param kind
	 * @param name
	 * @param otherSpotsHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @param role
	 * @return
	 */
	public boolean can_remove(String kind, String name, boolean otherSpotsHaveThisObjectName, String headName, Vector ranges, Role role) {
		// TODO 従来のもの
		boolean result = true;
		int index = 0;
		for ( Rules rules:this) {
			if ( !rules.can_remove( index++, kind, name, otherSpotsHaveThisObjectName, headName, ranges, role))
				result = false;
		}
		return result;
	}

	/**
	 * @param player
	 * @param kind
	 * @param objectName
	 * @param otherPlayersHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @param role
	 * @return
	 */
	public boolean can_remove(String player, String kind, String objectName, boolean otherPlayersHaveThisObjectName, String headName, Vector ranges, Role role) {
		// TODO これからはこちらに移行してゆく
		boolean result = true;
		int index = 0;
		for ( Rules rules:this) {
			if ( !rules.can_remove( index++, player, kind, objectName, otherPlayersHaveThisObjectName, headName, ranges, role))
				result = false;
		}
		return result;
	}

	/**
	 * @param player
	 * @param numberObjectName
	 * @param newType
	 * @param role
	 * @return
	 */
	public boolean is_number_object_type_correct(String player, String numberObjectName, String newType, Role role) {
		boolean result = true;
		int index = 0;
		for ( Rules rules:this) {
			if ( !rules.is_number_object_type_correct( index++, player, numberObjectName, newType, role))
				result = false;
		}
		return result;
	}

	/**
	 * @param name
	 * @param roleName
	 * @return
	 */
	public boolean can_remove_role_name(String name, String roleName) {
		boolean result = true;
		int index = 0;
		for ( Rules rules:this) {
			if ( !rules.can_remove_role_name( name, index++, roleName))
				result = false;
		}
		return result;
	}

	/**
	 * @param name
	 * @param function
	 * @return
	 */
	public boolean can_remove_expression(String name, String function) {
		boolean result = true;
		int index = 0;
		for ( Rules rules:this) {
			if ( !rules.can_remove_expression( name, index++, function))
				result = false;
		}
		return result;
	}

	/**
	 * @param name
	 * @param newName
	 * @param originalName
	 * @return
	 */
	public boolean update_stage_name(String name, String newName, String originalName) {
		boolean result = false;
		int index = 0;
		for ( Rules rules:this) {
			if ( rules.update_stage_name( name, index++, newName, originalName))
				result = true;
		}
		return result;
	}

	/**
	 * @param name
	 * @param stageName
	 * @return
	 */
	public boolean can_remove_stage_name(String name, String stageName) {
		boolean result = true;
		int index = 0;
		for ( Rules rules:this) {
			if ( !rules.can_remove_stage_name( name, index++, stageName))
				result = false;
		}
		return result;
	}

	/**
	 * @param name
	 * @param stageNames
	 * @return
	 */
	public boolean can_adjust_stage_name(String name, Vector<String> stageNames) {
		boolean result = true;
		int index = 0;
		for ( Rules rules:this) {
			if ( !rules.can_adjust_stage_name( name, index++, stageNames))
				result = false;
		}
		return result;
	}

	/**
	 * @param role
	 * @param drawObjects
	 * @return
	 */
	public boolean can_paste(Role role, Layer drawObjects) {
		boolean result = true;
		int index = 0;
		for ( Rules rules:this) {
			if ( !rules.can_paste( index++, role, drawObjects))
				result = false;
		}
		return result;
	}

	/**
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public boolean update_agent_name_and_number(String newName, String originalName, String headName, Vector ranges, String newHeadName, Vector newRanges) {
		boolean result = false;
		for ( Rules rules:this) {
			if ( rules.update_agent_name_and_number( newName, originalName, headName, ranges, newHeadName, newRanges))
				result = true;
		}
		return result;
	}

	/**
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector ranges, String newHeadName, Vector newRanges) {
		boolean result = false;
		for ( Rules rules:this) {
			if ( rules.update_spot_name_and_number( newName, originalName, headName, ranges, newHeadName, newRanges))
				result = true;
		}
		return result;
	}

	/**
	 * @param originalName
	 * @param name
	 * @return
	 */
	public boolean update_role_name(String originalName, String name) {
		boolean result = false;
		for ( Rules rules:this) {
			if ( rules.update_role_name( originalName, name))
				result = true;
		}
		return result;
	}

	/**
	 * @param kind
	 * @param originalName
	 * @param newName
	 * @param player
	 * @param role
	 * @return
	 */
	public boolean update_object_name(String kind, String originalName, String newName, String player, Role role) {
		boolean result = false;
		for ( Rules rules:this) {
			if ( rules.update_object_name( kind, originalName, newName, player, role))
				result = true;
		}
		return result;
	}

	/**
	 * @param visualShellExpressionManager
	 * @return
	 */
	public boolean update_expression(VisualShellExpressionManager visualShellExpressionManager) {
		boolean result = false;
		for ( Rules rules:this) {
			if ( rules.update_expression( visualShellExpressionManager))
				result = true;
		}
		return result;
	}

	/**
	 * @param headName
	 * @param ranges
	 */
	public void on_remove_agent_name_and_number(String headName, Vector ranges) {
		for ( Rules rules:this)
			rules.on_remove_agent_name_and_number( headName, ranges);
	}

	/**
	 * @param headName
	 * @param ranges
	 */
	public void on_remove_spot_name_and_number(String headName, Vector ranges) {
		for ( Rules rules:this)
			rules.on_remove_spot_name_and_number( headName, ranges);
	}

	/**
	 * @param roleNames
	 */
	public void on_remove_role_name(Vector<String> roleNames) {
		for ( Rules rules:this)
			rules.on_remove_role_name( roleNames);
	}

	/**
	 * @param stageNames
	 */
	public void on_remove_stage_name(Vector<String> stageNames) {
		for ( Rules rules:this)
			rules.on_remove_stage_name( stageNames);
	}

	/**
	 * @return
	 */
	public boolean update_stage_manager() {
		boolean result = false;
		for ( Rules rules:this) {
			if ( rules.update_stage_manager())
				result = true;
		}
		return result;
	}

	/**
	 * @param expressionMap
	 * @param usedExpressionMap
	 */
	public void get_used_expressions(TreeMap expressionMap, TreeMap usedExpressionMap) {
		for ( Rules rules:this)
			rules.get_used_expressions( expressionMap, usedExpressionMap);
	}

	/**
	 * @param originalFunctionName
	 * @param newFunctionName
	 * @return
	 */
	public boolean update_function(String originalFunctionName, String newFunctionName) {
		boolean result = false;
		for ( Rules rules:this) {
			if ( rules.update_function( originalFunctionName, newFunctionName))
				result = true;
		}
		return result;
	}

	/**
	 * @param initialValues
	 * @param suffixes
	 */
	public void get_initial_values(Vector<String> initialValues, String[] suffixes) {
		for ( Rules rules:this)
			rules.get_initial_values( initialValues, suffixes);
	}

	/**
	 * @param role
	 * @return
	 */
	public boolean transform_time_conditions_and_commands(Role role) {
		for ( Rules rules:this) {
			if ( !rules.transform_time_conditions_and_commands( role))
				return false;
		}
		return true;
	}

	/**
	 * @param role
	 * @return
	 */
	public boolean transform_keyword_conditions_and_commands(Role role) {
		for ( Rules rules:this) {
			if ( !rules.transform_keyword_conditions_and_commands( role))
				return false;
		}
		return true;
	}

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean has_same_agent_name(String name, String number) {
		for ( Rules rules:this) {
			if ( rules.has_same_agent_name( name, number))
				return true;
		}
		return false;
	}

	/**
	 * @param alias
	 * @return
	 */
	public boolean contains_this_alias(String alias) {
		for ( Rules rules:this) {
			if ( rules.contains_this_alias( alias))
				return true;
		}
		return false;
	}

	/**
	 * @param ruleCount
	 */
	public void how_many_rules(IntBuffer ruleCount) {
		for ( Rules rules:this)
			rules.how_many_rules( ruleCount);
	}

	/**
	 * @param roleName
	 * @param role
	 * @return
	 */
	public String get_initial_data(String roleName, Role role) {
		String script = "";
		for ( Rules rules:this)
			script += rules.get_initial_data( roleName, role);
		return script;
	}

	/**
	 * @param name
	 * @param ruleCount
	 * @param initialValueMap
	 * @param demo
	 * @param role
	 * @return
	 */
	public String get_script(String name, int ruleCount, InitialValueMap initialValueMap, boolean demo, Role role) {
		String script = "";
		int index = 0;
		for ( Rules rules:this) {
			script += rules.get_script( name, index++, ruleCount, initialValueMap, demo, role);
		}
		return script;
	}

	/**
	 * @param row
	 * @param column
	 * @param columnWidthsMap
	 */
	public void setup(int row, int column, Map columnWidthsMap) {
		// SaxLoaderのon_rule_data( ... )から呼ばれる
		clear();
		for ( int i = 0; i < row; ++i)
			add( new Rules());

		_columnCount = column;

		setup_column_widths( columnWidthsMap);
	}

/**
	 * @param columnWidthsMap
	 */
	private void setup_column_widths(Map columnWidthsMap) {
		_columnWidths.clear();

		for ( int i = 0; i < _columnCount; ++i) {
			Integer integer = ( Integer)columnWidthsMap.get( String.valueOf( i));
			_columnWidths.add( ( null != integer) ? integer : Integer.valueOf( _defaultRuleTableColumnWidth));
		}
	}

	/**
	 * 
	 */
	public void adjust_column_count() {
		_columnCount = Math.max( _columnCount, get_max_column_count());
		while ( _columnWidths.size() < _columnCount)
			_columnWidths.add( Integer.valueOf( _defaultRuleTableColumnWidth));
	}

	/**
	 * @param stageType
	 * @param role
	 * @return
	 */
	public boolean has_this_stage_rules(Role role, String stageType) {
		// TODO Auto-generated method stub
		return role.has_same_this_stage_rules( get_this_stage_rulesArray( stageType), stageType);
	}

	/**
	 * @param stageType
	 * @return
	 */
	private List<Rules> get_this_stage_rulesArray(String stageType) {
		// TODO Auto-generated method stub
		List<Rules> rulesArray = new ArrayList<>();
		for ( Rules rules:this)
			rules.get_this_stage_rules( rulesArray, stageType);
		return rulesArray;
	}

	/**
	 * @param stageType
	 * @param rulesArray
	 * @return
	 */
	public boolean has_same_this_stage_rules(List<Rules> rulesArray, String stageType) {
		// TODO Auto-generated method stub
		List<Rules> rlsArray = get_this_stage_rulesArray( stageType);
		if ( rlsArray.size() != rulesArray.size())
			return false;

		for ( int i = 0; i < rlsArray.size(); ++i) {
			if ( !rlsArray.get( i).same_as( rulesArray.get( i)))
				return false;
		}

		return true;
	}

	/**
	 * @param roleDataSet
	 * @param roleClassNameMap 
	 * @param stageType
	 * @param role
	 * @return
	 */
	public boolean get_ruleDataMap(RoleDataSet roleDataSet, Map<String, String> roleClassNameMap, String stageType, Role role) {
		// TODO Auto-generated method stub
		Map<String, List<RuleData>> ruleDataMap = new HashMap<>();
		for ( Rules rules:this) {
			if ( !rules.create_ruleData( ruleDataMap, stageType, role))
				return false;
		}

		if ( !merge_ruleClasses( ruleDataMap, roleDataSet, roleClassNameMap, stageType, role))
			return false;

		return true;
	}

	/**
	 * @param ruleDataMap
	 * @param roleDataSet
	 * @param roleClassNameMap 
	 * @param role
	 * @return
	 */
	private boolean merge_ruleClasses(Map<String, List<RuleData>> ruleDataMap, RoleDataSet roleDataSet, Map<String, String> roleClassNameMap, String stageType, Role role) {
		// TODO Auto-generated method stub
		Set<String> stageNames = ruleDataMap.keySet();
		for ( String stageName:stageNames) {
			List<RuleData> dstRuleDataList = roleDataSet._ruleDataMap.get( stageName);
			if ( null == dstRuleDataList) {
				dstRuleDataList = new ArrayList<>();
				roleDataSet._ruleDataMap.put( stageName, dstRuleDataList);
				List<RuleData> srcRuleDataList = ruleDataMap.get( stageName);
				for ( int i = 0; i < srcRuleDataList.size(); ++i) {
					srcRuleDataList.get( i)._roleClassName = roleClassNameMap.get( role._name);
					srcRuleDataList.get( i)._ruleClassName = JavaProgramExporter.get_rule_class_name( srcRuleDataList.get( i)._roleClassName, stageName + String.valueOf( i + 1));
					dstRuleDataList.add( srcRuleDataList.get( i));
				}
			} else {
				List<RuleData> srcRuleDataList = ruleDataMap.get( stageName);
				for ( RuleData ruleData:srcRuleDataList) {
					ruleData._roleClassName = roleClassNameMap.get( role._name);
					ruleData.set_ruleClassName( dstRuleDataList, roleDataSet._roleClassNameMap.get( role._name), stageName);
					dstRuleDataList.add( ruleData);
				}
			}
		}
		return true;
	}

	/**
	 * @param stageName
	 * @return
	 */
	public boolean has_this_stage(String stageName) {
		// TODO Auto-generated method stub
		for ( Rules rules:this) {
			if ( rules.has_this_stage( stageName))
				return true;
		}
		return false;
	}

	/**
	 * @param stageName
	 * @return
	 */
	public RuleData get_ruleData(String stageName) {
		// TODO Auto-generated method stub
		RuleData ruleData = new RuleData();
		for ( Rules rules:this)
			rules.get_ruleData( stageName, ruleData);

		if ( ruleData._rulesList.isEmpty())
			return null;

		return ruleData;
	}

	/**
	 * @param writer
	 * @return
	 */
	public boolean write(Writer writer) throws SAXException {
		write( _columnWidths, writer);

		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "row", "", String.valueOf( size()));
		attributesImpl.addAttribute( null, null, "column", "", String.valueOf( _columnCount));

		if ( is_empty()) {
			writer.writeElement( null, null, "rule_data", attributesImpl);
			return true;
		}

		writer.startElement( null, null, "rule_data", attributesImpl);
		int row = 0;
		for ( Rules rules:this) {
			if ( !rules.write( row++, writer))
				return false;
		}
		writer.endElement( null, null, "rule_data");

		return true;
	}

	/**
	 * @param columnWidths
	 * @param writer
	 */
	private void write(Vector<Integer> columnWidths, Writer writer) throws SAXException {
		if ( columnWidths.isEmpty())
			return;

		boolean exist = false;
		AttributesImpl attributesImpl = new AttributesImpl();
		for ( int i = 0; i < columnWidths.size(); ++i) {
			if ( columnWidths.get( i).intValue() != _defaultRuleTableColumnWidth) {
				attributesImpl.addAttribute( null, null,
					"width" + String.valueOf( i), "", String.valueOf( columnWidths.get( i).intValue()));
				exist = true;
			}
		}

		if ( !exist)
			return;

		writer.writeElement( null, null, "rule_column", attributesImpl);
	}
}
