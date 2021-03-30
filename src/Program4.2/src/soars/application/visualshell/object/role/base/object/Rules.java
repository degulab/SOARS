/*
 * 2005/05/30
 */
package soars.application.visualshell.object.role.base.object;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.exporter.java.JavaProgramExporter;
import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
import soars.application.visualshell.file.exporter.java.object.RuleData;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.table.data.CommonRuleData;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.condition.TimeCondition;
import soars.application.visualshell.object.stage.StageManager;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class Rules extends Vector<Rule> {
	/**
	 * 
	 */
	public String _comment = "";

	/**
	 * 
	 */
	public Rules() {
		super();
	}

	/**
	 * @param rules
	 */
	public Rules(Rules rules) {
		super();
		copy( rules);
	}

	/**
	 * @param rules
	 */
	private void copy(Rules rules) {
		clear();
		for ( Rule rule:rules)
			add( Rule.create( rule));
	}

	/**
	 * @return
	 */
	public boolean is_empty() {
		return ( isEmpty() && _comment.equals( ""));
	}

	/**
	 * 
	 */
	public void cleanup() {
		for ( Rule rule:this)
			rule.cleanup();

		clear();

		_comment = "";
	}

	/**
	 * @return
	 */
	public int get_max_column_count() {
		int column = -1;
		for ( Rule rule:this)
			column = Math.max( column, rule._column);

		return column + 1;
	}

	/**
	 * @param role
	 */
	public void on_paste(Role role) {
		for ( Rule rule:this)
			rule.make_unique_agent_name( role);
	}

	/**
	 * @param roleName
	 * @param row
	 * @param headName
	 * @param ranges
	 * @return
	 */
	public boolean can_adjust_agent_name(String roleName, int row, String headName, Vector ranges) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_adjust_agent_name( roleName, row, headName, ranges, this))
				result = false;
		}
		return result;
	}

	/**
	 * @param roleName
	 * @param row
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public boolean can_adjust_agent_name(String roleName, int row, String headName, Vector ranges, String newHeadName, Vector newRanges) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_adjust_agent_name( roleName, row, headName, ranges, newHeadName, newRanges, this))
				result = false;
		}
		return result;
	}

	/**
	 * @param roleName
	 * @param row
	 * @param headName
	 * @param ranges
	 * @return
	 */
	public boolean can_adjust_spot_name(String roleName, int row, String headName, Vector ranges) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_adjust_spot_name( roleName, row, headName, ranges, this))
				result = false;
		}
		return result;
	}

	/**
	 * @param roleName
	 * @param row
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public boolean can_adjust_spot_name(String roleName, int row, String headName, Vector ranges, String newHeadName, Vector newRanges) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_adjust_spot_name( roleName, row, headName, ranges, newHeadName, newRanges, this))
				result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param kind
	 * @param name
	 * @param otherSpotsHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @param role
	 * @return
	 */
	public boolean can_remove(int row, String kind, String name, boolean otherSpotsHaveThisObjectName, String headName, Vector ranges, Role role) {
		// TODO 従来のもの
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_remove( row, kind, name, otherSpotsHaveThisObjectName, headName, ranges, this, role))
				result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param player
	 * @param kind
	 * @param objectName
	 * @param otherPlayersHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @param role
	 * @return
	 */
	public boolean can_remove(int row, String player, String kind, String objectName, boolean otherPlayersHaveThisObjectName, String headName, Vector ranges, Role role) {
		// TODO これからはこちらに移行してゆく
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_remove( row, player, kind, objectName, otherPlayersHaveThisObjectName, headName, ranges, this, role))
				result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param player
	 * @param numberObjectName
	 * @param newType
	 * @param role
	 * @return
	 */
	public boolean is_number_object_type_correct(int row, String player, String numberObjectName, String newType, Role role) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.is_number_object_type_correct( row, player, numberObjectName, newType, this, role))
				result = false;
		}
		return result;
	}

	/**
	 * @param name
	 * @param row
	 * @param roleName
	 * @return
	 */
	public boolean can_remove_role_name(String name, int row, String roleName) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_remove_role( name, row, roleName, this))
				result = false;
		}
		return result;
	}

	/**
	 * @param name
	 * @param row
	 * @param function
	 * @return
	 */
	public boolean can_remove_expression(String name, int row, String function) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_remove_expression( name, row, function, this))
				result = false;
		}
		return result;
	}

	/**
	 * @param name
	 * @param row
	 * @param newName
	 * @param originalName
	 * @return
	 */
	public boolean update_stage_name(String name, int row, String newName, String originalName) {
		boolean result = false;
		for ( Rule rule:this) {
			if ( rule.update_stage_name( name, row, newName, originalName, this))
				result = true;
		}
		return result;
	}

	/**
	 * @param name
	 * @param row
	 * @param stageName
	 * @return
	 */
	public boolean can_remove_stage_name(String name, int row, String stageName) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_remove_stage_name( name, row, stageName, this))
				result = false;
		}
		return result;
	}

	/**
	 * @param name
	 * @param row
	 * @param stageNames
	 * @return
	 */
	public boolean can_adjust_stage_name(String name, int row, Vector<String> stageNames) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_adjust_stage_name( name, row, stageNames, this))
				result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param role
	 * @param drawObjects
	 * @return
	 */
	public boolean can_paste(int row, Role role, Layer drawObjects) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_paste( row, this, role, drawObjects))
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
		for ( Rule rule:this) {
			if ( rule.update_agent_name_and_number( newName, originalName, headName, ranges, newHeadName, newRanges))
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
		for ( Rule rule:this) {
			if ( rule.update_spot_name_and_number( newName, originalName, headName, ranges, newHeadName, newRanges))
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
		for ( Rule rule:this) {
			if ( rule.update_role_name( originalName, name, this))
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
		for ( Rule rule:this) {
			if ( rule.update_object_name( kind, originalName, newName, player, this, role))
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
		for ( Rule rule:this) {
			if ( rule.update_expression( visualShellExpressionManager))
				result = true;
		}
		return result;
	}

	/**
	 * @param headName
	 * @param ranges
	 */
	public void on_remove_agent_name_and_number(String headName, Vector ranges) {
	}

	/**
	 * @param headName
	 * @param ranges
	 */
	public void on_remove_spot_name_and_number(String headName, Vector ranges) {
//		for ( Rule rule:this) {
//			if ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.spot"))
//				|| rule._type.equals( ResourceManager.get_instance().get( "rule.type.command.move")))
//				&& !spot_names.contains( rule._value))
//				rule._value = "";
//		}
	}

	/**
	 * @param roleNames
	 */
	public void on_remove_role_name(Vector<String> roleNames) {
//		for ( Rule rule:this) {
//			if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.command.role"))
//				&& !roleNames.contains( rule._value))
//				rule._value = "";
//		}
	}

	/**
	 * @param stageNames
	 */
	public void on_remove_stage_name(Vector<String> stageNames) {
//		for ( Rule rule:this) {
//			if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage"))
//				&& !stageNames.contains( rule._value))
//				rule._value = "";
//		}
	}

	/**
	 * @return
	 */
	public boolean update_stage_manager() {
		boolean result = false;
		for ( Rule rule:this) {
			if ( rule.update_stage_manager())
				result = true;
		}
		return result;
	}

	/**
	 * @param expressionMap
	 * @param usedExpressionMap
	 */
	public void get_used_expressions(TreeMap expressionMap, TreeMap usedExpressionMap) {
		for ( Rule rule:this)
			rule.get_used_expressions( expressionMap, usedExpressionMap);
	}

	/**
	 * @param originalFunctionName
	 * @param newFunctionName
	 * @return
	 */
	public boolean update_function(String originalFunctionName, String newFunctionName) {
		boolean result = false;
		for ( Rule rule:this) {
			if ( rule.update_function( originalFunctionName, newFunctionName))
				result = true;
		}
		return result;
	}

	/**
	 * @param initialValues
	 * @param suffixes
	 */
	public void get_initial_values(Vector<String> initialValues, String[] suffixes) {
		for ( Rule rule:this)
			rule.get_initial_values( initialValues, suffixes);
	}

	/**
	 * @param role
	 * @return
	 */
	public boolean transform_time_conditions_and_commands(Role role) {
		for ( Rule rule:this) {
			if ( !rule.transform_time_conditions_and_commands( role))
				return false;
		}
		return true;
	}

	/**
	 * @param role
	 * @return
	 */
	public boolean transform_keyword_conditions_and_commands(Role role) {
		for ( Rule rule:this) {
			if ( !rule.transform_keyword_conditions_and_commands( role))
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
		for ( Rule rule:this) {
			if ( rule.has_same_agent_name( name, number))
				return true;
		}
		return false;
	}

	/**
	 * @param alias
	 * @return
	 */
	public boolean contains_this_alias(String alias) {
		for ( Rule rule:this) {
			if ( rule.contains_this_alias( alias))
				return true;
		}
		return false;
	}

	/**
	 * @param ruleCount
	 */
	public void how_many_rules(IntBuffer ruleCount) {
		if ( isEmpty())
			return;

		for ( int i = size() - 1; i >= 0 ; --i) {
			Rule rule = ( Rule)get( i);
			if ( rule._or)
				continue;

			if ( ruleCount.get( 0) < ( rule._column + 1))
				ruleCount.put( 0, rule._column + 1);

			return;
		}
	}

	/**
	 * @param roleName
	 * @param role
	 * @return
	 */
	public String get_initial_data(String roleName, Role role) {
		if ( isEmpty())
			return "";

		String script = "";
		for ( Rule rule:this) {
			if ( 0 == rule._column) {
				if ( !script.equals( ""))
					return null;

				script = rule._value;
			} else {
				String name = CommonRuleData.get_name( rule._type, rule._kind);
				if ( null == name || name.equals( ""))
					return null;

				script += ( "\t" + rule._kind + "\t" + name + "\t" + rule._value
					+ ( rule._kind.equals( "command") ? "" : ( "\t" + ( rule._or ? "true" : "false"))));
			}
		}

		return ( ( ( role instanceof AgentRole) ? ResourceManager.get_instance().get( "initial.data.agent.role") : ResourceManager.get_instance().get( "initial.data.spot.role"))
			+ "\t" + roleName/*role._name*/ + "\tcondition\t" + script + Constant._lineSeparator);
	}

	/**
	 * @param name
	 * @param row
	 * @param rulesCount
	 * @param initialValueMap
	 * @param demo
	 * @param role
	 * @return
	 */
	public String get_script(String name, int row, int rulesCount, InitialValueMap initialValueMap, boolean demo, Role role) {
		if ( isEmpty())
			return "";

		if ( !get( 0)._kind.equals( "condition") || !get( 0)._type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")))
			return "";

		int index = 0;
		String script = "", value = "";
		for ( Rule rule:this) {
			if ( rule._or)
				value += ( " || " + rule.get_script( row, initialValueMap, demo, role));
			else {
				if ( !value.equals( "")) {
					script += ( value + "\t");
					value = "";
				}

				if ( rule._kind.equals( "condition"))
					value = rule.get_script( row, initialValueMap, demo, role);
				else {
					script += rule.get_script( row, initialValueMap, demo, role);
					script += " ; TRUE\t";
				}

				for ( int i = index; i < rule._column; ++i)
					script += "\t";

				index = ( rule._column + 1);
			}
		}

		script += ( value.equals( "") ? "" : ( value + "\t"));

		for ( int i = index; i < rulesCount; ++i)
			script += "\t";

		return ( name + "\t" + script + "Line=" + String.valueOf( row + 1) + Constant._lineSeparator);
	}

	/**
	 * @param rulesArray
	 * @param stageType
	 * @return
	 */
	public void get_this_stage_rules(List<Rules> rulesArray, String stageType) {
		// TODO Auto-generated method stub
		Rules rules = new Rules();
		for ( int i = 0; i < size(); ++i) {
//			if ( rule._type.equals( "stage") && StageManager.get_instance().contains( "initial stage", rule._value))
			if ( 0 == i && ( !get( i)._type.equals( "stage") || !StageManager.get_instance().contains( stageType, get( i)._value)))
				return;

			rules.add( get( i));
		}
		rulesArray.add( rules);
	}

	/**
	 * @param rules
	 * @return
	 */
	public boolean same_as(Rules rules) {
		// TODO Auto-generated method stub
		if ( size() != rules.size())
			return false;

		for ( int i = 0; i < size(); ++i) {
			if ( !get( i).same_as( rules.get( i)))
				return false;
		}
		return true;
	}

	/**
	 * @param rules
	 * @return
	 */
	public boolean same_as_without_time(Rules rules) {
		// TODO Auto-generated method stub
		if ( size() != rules.size())
			return false;

		for ( int i = 0; i < size(); ++i) {
			if ( get( i)._type.equals( ResourceManager.get_instance().get( "rule.type.condition.time")) && 0 == TimeCondition.get_kind( get( i))
				&& rules.get( i)._type.equals( ResourceManager.get_instance().get( "rule.type.condition.time")) && 0 == TimeCondition.get_kind( get( i)))
				continue;

			if ( !get( i).same_as( rules.get( i)))
				return false;
		}
		return true;
	}

	/**
	 * @param ruleDataMap
	 * @param stageType
	 * @param role
	 * @return
	 */
	public boolean create_ruleData(Map<String, List<RuleData>> ruleDataMap, String stageType, Role role) {
		// TODO Auto-generated method stub
		if ( isEmpty())
			return true;

		if ( !get( 0)._kind.equals( "condition") || !get( 0)._type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")))
			return true;

		if ( !StageManager.get_instance().contains( stageType, get( 0)._value))
			return true;

		List<RuleData> ruleDataList = ruleDataMap.get( get( 0)._value);
		if ( null == ruleDataList) {
			ruleDataList = new ArrayList<>();
			ruleDataMap.put( get( 0)._value, ruleDataList);
		}

		if ( has_time_condition())
			ruleDataList.add( new RuleData( AgentRole.class.isInstance( role) ? "agent" : "spot", role._name, this, get_time()));
		else {
			if ( ruleDataList.isEmpty())
				ruleDataList.add( new RuleData( AgentRole.class.isInstance( role) ? "agent" : "spot", role._name, this));
			else {
				if ( ruleDataList.get( ruleDataList.size() - 1)._time.equals( ""))
					ruleDataList.get( ruleDataList.size() - 1)._rulesList.add( this);
				else
					ruleDataList.add( new RuleData( AgentRole.class.isInstance( role) ? "agent" : "spot", role._name, this));
			}
		}

		return true;
	}

	/**
	 * @return
	 */
	private boolean has_time_condition() {
		// TODO Auto-generated method stub
		for ( Rule rule:this) {
			if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.time")) && TimeCondition.is_java_program_time( rule))
			//if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.time")) && 0 == TimeCondition.get_kind( rule))
				return true;
		}
		return false;
	}

	/**
	 * @return
	 */
	private String get_time() {
		// TODO Auto-generated method stub
		for ( Rule rule:this) {
			if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.time")) && TimeCondition.is_java_program_time( rule))
			//if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.time")) && 0 == TimeCondition.get_kind( rule))
				return rule._value.split( "@")[ 1];
		}
		return "";
	}

	/**
	 * @param ruleData 
	 * @param roleDataSet
	 * @param agentNameMap
	 * @param spotNameMap
	 * @param imports
	 * @param commands
	 * @param initialValueMap
	 * @param folder
	 * @param packagePrefix
	 * @param role
	 * @param initialStage
	 * @return
	 */
	public List<String> get_rule_class_java_program_body(RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, InitialValueMap initialValueMap, File folder, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		List<String> lines = new ArrayList<>();

		if ( isEmpty())
			return lines;

		if ( !get( 0)._kind.equals( "condition") || !get( 0)._type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")))
			return lines;

		if ( !initialStage && StageManager.get_instance().contains( "initial stage", get( 0)._value))
			return lines;

		// エージェントロールの場合、各ステージ毎に、使用されている変数から考えられ得るエージェントロールと現在のスポットロールを列挙しておく必要がある
		int indent = 2;
		boolean condition = false;
		boolean conditionExists = false;
		boolean or = false;
		for ( int i = 0; i < size(); ++i) {
			if ( get( i)._type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage"))
				|| ( get( i)._type.equals( ResourceManager.get_instance().get( "rule.type.condition.time")) && TimeCondition.is_java_program_time( get( i))))
				//|| ( get( i)._type.equals( ResourceManager.get_instance().get( "rule.type.condition.time")) && 0 == TimeCondition.get_kind( get( i))))
				continue;

			List<String> codes = get( i).get_java_program( ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, initialValueMap, packagePrefix, role, initialStage);
			if ( codes.isEmpty() || ( 1 == codes.size() && codes.get( 0).equals( "")))
				continue;
//			String code = get( i).get_java_program( ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, initialValueMap, packagePrefix, role, initialStage);
//			if ( code.equals( ""))
//				continue;

			String script = get( i)._kind + " : " + get( i)._type + " : " + get( i).get_script( 0/*row*/, initialValueMap, false, role);

			if ( get( i)._kind.equals( "condition")) {
				if ( condition) {
					// if節の内部
					String prefix = "";
					String suffix = "";
					if ( or) {
						if ( !is_next_or( i)) {
							suffix = ")";
							or = false;
						}
					} else {
						or = is_next_or( i);
						if ( or)
							prefix = "(";
					}
					lines.add( JavaProgramExporter._indents[ indent] + "// " + ( get( i)._or ? "|| " : "&& ") + script);
					lines.add( JavaProgramExporter._indents[ indent] + ( get( i)._or ? "|| " : "&& ") + prefix + codes.get( 0) + suffix);
//					lines.add( JavaProgramExporter._indents[ indent] + ( get( i)._or ? "|| " : "&& ") + prefix + code + suffix);
				} else {
					// if節の先頭
					or = is_next_or( i) && !or_all( i);
					lines.add( JavaProgramExporter._indents[ indent] + "// " + script);
					lines.add( JavaProgramExporter._indents[ indent] + "if (" + ( or ? "(" : "") + codes.get( 0));
//					lines.add( JavaProgramExporter._indents[ indent] + "if (" + ( or ? "(" : "") + code);
					condition = true;
					++indent;
				}
				conditionExists = true;
			} else {
				if ( condition && !lines.isEmpty()) {
					// if節が終わった直後
					String text = lines.get( lines.size() - 1);
					text += ( or ? ")" : "") + ") {";
					lines.set( lines.size() - 1, text);
				}
				lines.add( JavaProgramExporter._indents[ indent] + "// " + script);
				if ( 1 == codes.size())
					lines.add( JavaProgramExporter._indents[ indent] + codes.get( 0) + ";");
				else {
					String line = "";
					for ( String code:codes)
						line += JavaProgramExporter._indents[ indent] + code;
					lines.add( line);
				}
				condition = false;
				or = false;
			}
		}

		if ( condition && !lines.isEmpty()) {
			String text = lines.get( lines.size() - 1);
			text += ( or ? ")" : "") + ") {";
			lines.set( lines.size() - 1, text);
		}
//		if ( condition) {
//			if ( !lines.isEmpty()) {
//				String text = lines.get( lines.size() - 1);
//				text += ( or ? ")" : "") + ") {";
//				lines.set( lines.size() - 1, text);
//			}
//			lines.add( JavaProgramExporter._indents[ indent] + "return true;");
//		} else
//			lines.add( JavaProgramExporter._indents[ indent] + "return true;");

			if ( !lines.isEmpty())
				lines.add( JavaProgramExporter._indents[ indent] + "return true;");

			while ( 2 < indent)
				lines.add( JavaProgramExporter._indents[ --indent] + "}");

		if ( conditionExists)
			lines.add( JavaProgramExporter._indents[ indent] + "return false;");

		return lines;
	}

	/**
	 * @param index
	 * @return
	 */
	private boolean is_next_or(int index) {
		// TODO Auto-generated method stub
		if ( size() - 1 == index)
			return false;

		return get( index + 1)._or;
	}

	/**
	 * @param index
	 * @return
	 */
	private boolean or_all(int index) {
		// TODO Auto-generated method stub
		if ( size() - 1 == index)
			return false;

		boolean or = false;
		for ( int i = index + 1; i < size(); ++i) {
			if ( !get( i)._kind.equals( "condition"))
				break;

			if ( !get( i)._or)
				return false;
		}
		return true;
	}

	/**
	 * @param stageName
	 * @return
	 */
	public boolean has_this_stage(String stageName) {
		// TODO Auto-generated method stub
		if ( isEmpty())
			return false;

		if ( !get( 0)._kind.equals( "condition") || !get( 0)._type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")))
			return false;

		return get( 0)._value.equals( stageName);
	}

	/**
	 * @param stageName
	 * @param ruleData
	 */
	public void get_ruleData(String stageName, RuleData ruleData) {
		// TODO Auto-generated method stub
		if ( isEmpty())
			return;

		if ( !get( 0)._kind.equals( "condition") || !get( 0)._type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")))
			return;

		if ( get( 0)._value.equals( stageName))
			ruleData._rulesList.add( this);
	}

	/**
	 * @param row
	 * @param writer
	 * @return
	 */
	public boolean write(int row, Writer writer) throws SAXException {
		if ( is_empty())
			return true;

		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "row", "", String.valueOf( row));

		writer.startElement( null, null, "rule", attributesImpl);
		for ( Rule rule:this) {
			if ( !rule.write( writer))
				return false;
		}

		if ( !_comment.equals( "")) {
			writer.startElement( null, null, "comment", new AttributesImpl());
			writer.characters( _comment.toCharArray(), 0, _comment.length());
			writer.endElement( null, null, "comment");
		}

		writer.endElement( null, null, "rule");
		return true;
	}
}
