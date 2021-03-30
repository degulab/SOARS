/*
 * 2005/05/30
 */
package soars.application.visualshell.object.role.base.object.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.file.exporter.java.JavaProgramExporter;
import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
import soars.application.visualshell.file.exporter.java.object.RuleData;
import soars.application.visualshell.file.exporter.java.object.Variable;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.Rules;
import soars.application.visualshell.object.role.base.object.command.AttachCommand;
import soars.application.visualshell.object.role.base.object.command.CollectionCommand;
import soars.application.visualshell.object.role.base.object.command.CreateAgentCommand;
import soars.application.visualshell.object.role.base.object.command.CreateObjectCommand;
import soars.application.visualshell.object.role.base.object.command.DetachCommand;
import soars.application.visualshell.object.role.base.object.command.ExTransferCommand;
import soars.application.visualshell.object.role.base.object.command.ExchangeAlgebraCommand;
import soars.application.visualshell.object.role.base.object.command.FunctionalObjectCommand;
import soars.application.visualshell.object.role.base.object.command.GetEquipCommand;
import soars.application.visualshell.object.role.base.object.command.KeywordCommand;
import soars.application.visualshell.object.role.base.object.command.ListCommand;
import soars.application.visualshell.object.role.base.object.command.MapCommand;
import soars.application.visualshell.object.role.base.object.command.MoveCommand;
import soars.application.visualshell.object.role.base.object.command.NextStageCommand;
import soars.application.visualshell.object.role.base.object.command.OthersCommand;
import soars.application.visualshell.object.role.base.object.command.PutEquipCommand;
import soars.application.visualshell.object.role.base.object.command.RoleCommand;
import soars.application.visualshell.object.role.base.object.command.SetRoleCommand;
import soars.application.visualshell.object.role.base.object.command.SpotVariableCommand;
import soars.application.visualshell.object.role.base.object.command.SubstitutionCommand;
import soars.application.visualshell.object.role.base.object.command.TerminateCommand;
import soars.application.visualshell.object.role.base.object.command.TimeCommand;
import soars.application.visualshell.object.role.base.object.command.TraceCommand;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.common.number.ExpressionElements;
import soars.application.visualshell.object.role.base.object.condition.AgentNameCondition;
import soars.application.visualshell.object.role.base.object.condition.CollectionCondition;
import soars.application.visualshell.object.role.base.object.condition.FunctionalObjectCondition;
import soars.application.visualshell.object.role.base.object.condition.KeywordCondition;
import soars.application.visualshell.object.role.base.object.condition.ListCondition;
import soars.application.visualshell.object.role.base.object.condition.NumberObjectCondition;
import soars.application.visualshell.object.role.base.object.condition.OthersCondition;
import soars.application.visualshell.object.role.base.object.condition.ProbabilityCondition;
import soars.application.visualshell.object.role.base.object.condition.RoleCondition;
import soars.application.visualshell.object.role.base.object.condition.SpotCondition;
import soars.application.visualshell.object.role.base.object.condition.SpotNameCondition;
import soars.application.visualshell.object.role.base.object.condition.StageCondition;
import soars.application.visualshell.object.role.base.object.condition.TimeCondition;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class Rule {

	/**
	 * Column number.
	 */
	public int _column = 0;

	/**
	 * Rule kind.
	 */
	public String _kind = "";

	/**
	 * Rule type.
	 */
	public String _type = "";

	/**
	 * Rule value.
	 */
	public String _value = "";

	/**
	 * Logical add.
	 */
	public boolean _or = false;

	/**
	 * Creates a new Rule with the specified parameters.
	 * @param kind
	 * @param type
	 * @param value
	 * @return a new Rule
	 */
	public static Rule create(String kind, String type, String value) {
		if ( kind.equals( "condition")) {
			if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")))
				return new StageCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.spot")))
				return new SpotCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.keyword")))
				return new KeywordCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.time")))
				return new TimeCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.role")))
				return new RoleCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.probability")))
				return new ProbabilityCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.agent.name")))
				return new AgentNameCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.spot.name")))
				return new SpotNameCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.number.object")))
				return new NumberObjectCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				return new CollectionCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")))
				return new ListCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.functional.object")))
				return new FunctionalObjectCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.others")))
				return new OthersCondition( kind, type, value);
			else
				return new OthersCondition( kind, ResourceManager.get_instance().get( "rule.type.condition.others"), value);
		} else if ( kind.equals( "command")) {
			if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.keyword")))
				return new  KeywordCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.move")))
				return new  MoveCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.spot.variable")))
				return new  SpotVariableCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.time")))
				return new  TimeCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.role")))
				return new  RoleCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.set.role")))
				return new  SetRoleCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.substitution")))
				return new  SubstitutionCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.collection")))
				return new CollectionCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.list")))
				return new ListCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.get.equip")))
				return new GetEquipCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.put.equip")))
				return new PutEquipCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.create.agent")))
				return new CreateAgentCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.create.object")))
				return new CreateObjectCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.attach")))
				return new AttachCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.detach")))
				return new DetachCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.functional.object")))
				return new FunctionalObjectCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.map")))
				return new MapCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra")))
				return new ExchangeAlgebraCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.extransfer")))
				return new ExTransferCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.next.stage")))
				return new NextStageCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.terminate")))
				return new TerminateCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.trace")))
				return new TraceCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.others")))
				return new OthersCommand( kind, type, value);
			else
				return new OthersCommand( kind, ResourceManager.get_instance().get( "rule.type.command.others"), value);
		} else {
			return null;
		}
	}

	/**
	 * Creates a new Rule with the specified Rule.
	 * @param rule
	 * @return a new Rule
	 */
	public static Rule create(Rule rule) {
		Rule r = create( rule._kind, rule._type, rule._value);
		r._column = rule._column;
		r._or = rule._or;
		return r;
	}

	/**
	 * Creates a new Rule with the specified parameters.
	 * @param column Column number
	 * @param type Rule type
	 * @param value Rule value
	 * @param or Logical add
	 * @return a new Rule
	 */
	public static Rule create(String kind, int column, String type, String value, boolean or) {
		Rule rule = create( kind, type, value);
		if ( null == rule)
			return null;

		rule._column = column;
		rule._or = or;
		return rule;
	}

	/**
	 * Creates a new Rule with the specified parameters.
	 * @param kind Rule kind
	 * @param type Rule type
	 * @param value Rule value
	 */
	public Rule(String kind, String type, String value) {
		super();
		_kind = kind;
		_type = type;
		_value = value;
	}

	/**
	 * Clears the all parameters.
	 */
	public void cleanup() {
		_column = 0;
		_kind = "";
		_type = "";
		_value = "";
		_or = false;
	}

	/**
	 * Adds this Rule's initial value to the specified array.
	 * @param initialValues Array of initial values
	 * @param suffixes Suffix characters
	 */
	public void get_initial_values(Vector<String> initialValues, String[] suffixes) {
		CommonTool.get_aliases( _value, initialValues, suffixes);
	}

	/**
	 * @param role
	 * @return
	 */
	public boolean transform_time_conditions_and_commands(Role role) {
		return true;
	}

	/**
	 * @param role
	 * @return
	 */
	public boolean transform_keyword_conditions_and_commands(Role role) {
		return true;
	}

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean has_same_agent_name(String name, String number) {
		return false;
	}

	/**
	 * @param alias
	 * @return
	 */
	public boolean contains_this_alias(String alias) {
		if ( _value.equals( alias))
			return true;

		if ( _value.endsWith( alias))
			return true;

		for ( int i = 0; i < ExperimentManager._suffixes.length; ++i) {
			if ( 0 <= _value.indexOf( alias + ExperimentManager._suffixes[ i]))
				return true;
		}

		return false;
	}

	/**
	 * @param roleName
	 * @param row
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @return
	 */
	public boolean can_adjust_agent_name(String roleName, int row, String headName, Vector ranges, Rules rules) {
		String used_agent_names[] = get_used_agent_names();
		if ( null == used_agent_names)
			return true;

		boolean result = true;

		for ( int i = 0; i < used_agent_names.length; ++i) {
			if ( null == used_agent_names[ i])
				continue;

			if ( !SoarsCommonTool.has_same_name( headName, ranges, used_agent_names[ i]))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + roleName,
				"type = " + _kind + "::" + _type,
				"agent = " + used_agent_names[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
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
	 * @param rules
	 * @return
	 */
	public boolean can_adjust_agent_name(String roleName, int row, String headName, Vector ranges, String newHeadName, Vector newRanges, Rules rules) {
		String used_agent_names[] = get_used_agent_names();
		if ( null == used_agent_names)
			return true;

		boolean result = true;

		for ( int i = 0; i < used_agent_names.length; ++i) {
			if ( null == used_agent_names[ i])
				continue;

			if ( !SoarsCommonTool.has_same_name( headName, ranges, used_agent_names[ i]))
				continue;

			if ( SoarsCommonTool.has_same_name( newHeadName, newRanges, used_agent_names[ i]))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + roleName,
				"type = " + _kind + "::" + _type,
				"agent = " + used_agent_names[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}

		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_agent_names() {
		return null;
	}

	/**
	 * @param roleName
	 * @param row
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @return
	 */
	public boolean can_adjust_spot_name(String roleName, int row, String headName, Vector ranges, Rules rules) {
		String used_spot_names[] = get_used_spot_names();
		if ( null == used_spot_names)
			return true;

		boolean result = true;

		for ( int i = 0; i < used_spot_names.length; ++i) {
			if ( null == used_spot_names[ i])
				continue;

			if ( !SoarsCommonTool.has_same_name( headName, ranges, used_spot_names[ i]))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + roleName,
				"type = " + _kind + "::" + _type,
				"spot = " + used_spot_names[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
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
	 * @param rules
	 * @return
	 */
	public boolean can_adjust_spot_name(String roleName, int row, String headName, Vector ranges, String newHeadName, Vector newRanges, Rules rules) {
		String used_spot_names[] = get_used_spot_names();
		if ( null == used_spot_names)
			return true;

		boolean result = true;

		for ( int i = 0; i < used_spot_names.length; ++i) {
			if ( null == used_spot_names[ i])
				continue;

			if ( !SoarsCommonTool.has_same_name( headName, ranges, used_spot_names[ i]))
				continue;

			if ( SoarsCommonTool.has_same_name( newHeadName, newRanges, used_spot_names[ i]))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + roleName,
				"type = " + _kind + "::" + _type,
				"spot = " + used_spot_names[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}

		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_spot_names() {
		return null;
	}

	/**
	 * @param row
	 * @param kind
	 * @param name
	 * @param otherSpotsHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	public boolean can_remove(int row, String kind, String name, boolean otherSpotsHaveThisObjectName, String headName, Vector ranges, Rules rules, Role role) {
		// TODO 従来のもの
		if ( kind.equals( "spot variable"))
			return can_remove_spot_variable_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "probability"))
			return can_remove_probability_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "collection"))
			return can_remove_collection_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "list"))
			return can_remove_list_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "map"))
			return can_remove_map_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "keyword"))
			return can_remove_keyword_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "number object"))
			return can_remove_number_object_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "role variable"))
			return can_remove_role_variable_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "time variable"))
			return can_remove_time_variable_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "class variable"))
			return can_remove_class_variable_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "file"))
			return can_remove_file_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "exchange algebra"))
			return can_remove_exchange_algebra_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "extransfer"))
			return can_remove_extransfer_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else
			return true;
	}

	/**
	 * @param row
	 * @param player
	 * @param kind
	 * @param objectName
	 * @param otherPlayersHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	public boolean can_remove(int row, String player, String kind, String objectName, boolean otherPlayersHaveThisObjectName, String headName, Vector ranges, Rules rules, Role role) {
		// TODO これからはこちらに移行してゆく→実装はRuleNewクラス
		return true;
	}

	/**
	 * @param row
	 * @param spotVariableName
	 * @param otherSpotsHaveThisSpotVariableName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_spot_variable_name(int row, String spotVariableName, boolean otherSpotsHaveThisSpotVariableName, String headName, Vector ranges, Rules rules, Role role) {
		String usedSpotVariableNames[] = get_used_spot_variable_names( role);
		if ( null == usedSpotVariableNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedSpotVariableNames.length; ++i) {
			if ( null == usedSpotVariableNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedSpotVariableNames[ i], spotVariableName, otherSpotsHaveThisSpotVariableName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"spot variable = " + usedSpotVariableNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param role
	 * @return
	 */
	protected String[] get_used_spot_variable_names(Role role) {
		return null;
	}

	/**
	 * @param row
	 * @param probabilityName
	 * @param otherSpotsHaveThisProbabilityName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_probability_name(int row, String probabilityName, boolean otherSpotsHaveThisProbabilityName, String headName, Vector ranges, Rules rules, Role role) {
		String usedProbabilityNames[] = get_used_probability_names();
		if ( null == usedProbabilityNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedProbabilityNames.length; ++i) {
			if ( null == usedProbabilityNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedProbabilityNames[ i], probabilityName, otherSpotsHaveThisProbabilityName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"probability = " + usedProbabilityNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_probability_names() {
		return null;
	}

	/**
	 * @param row
	 * @param collectionName
	 * @param otherSpotsHaveThisCollectionName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_collection_name(int row, String collectionName, boolean otherSpotsHaveThisCollectionName, String headName, Vector ranges, Rules rules, Role role) {
		String usedCollectionNames[] = get_used_collection_names();
		if ( null == usedCollectionNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedCollectionNames.length; ++i) {
			if ( null == usedCollectionNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedCollectionNames[ i], collectionName, otherSpotsHaveThisCollectionName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"collection = " + usedCollectionNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_collection_names() {
		return null;
	}

	/**
	 * @param row
	 * @param listName
	 * @param otherSpotsHaveThisListName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_list_name(int row, String listName, boolean otherSpotsHaveThisListName, String headName, Vector ranges, Rules rules, Role role) {
		String usedListNames[] = get_used_list_names();
		if ( null == usedListNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedListNames.length; ++i) {
			if ( null == usedListNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedListNames[ i], listName, otherSpotsHaveThisListName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"list = " + usedListNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_list_names() {
		return null;
	}

	/**
	 * @param row
	 * @param mapMame
	 * @param otherSpotsHaveThisMapName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_map_name(int row, String mapMame, boolean otherSpotsHaveThisMapName, String headName, Vector ranges, Rules rules, Role role) {
		String usedMapNames[] = get_used_map_names();
		if ( null == usedMapNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedMapNames.length; ++i) {
			if ( null == usedMapNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedMapNames[ i], mapMame, otherSpotsHaveThisMapName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"map = " + usedMapNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_map_names() {
		return null;
	}

	/**
	 * @param row
	 * @param keywordName
	 * @param otherSpotsHaveThisKeywordName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_keyword_name(int row, String keywordName, boolean otherSpotsHaveThisKeywordName, String headName, Vector ranges, Rules rules, Role role) {
		String usedKeywordNames[] = get_used_keyword_names();
		if ( null == usedKeywordNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedKeywordNames.length; ++i) {
			if ( null == usedKeywordNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedKeywordNames[ i], keywordName, otherSpotsHaveThisKeywordName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"keyword = " + usedKeywordNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_keyword_names() {
		return null;
	}

	/**
	 * @param row
	 * @param numberObjectName
	 * @param otherSpotsHaveThisNumberObjectName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_number_object_name(int row, String numberObjectName, boolean otherSpotsHaveThisNumberObjectName, String headName, Vector ranges, Rules rules, Role role) {
		String usedNumberObjectNames[] = get_used_number_object_names();
		if ( null == usedNumberObjectNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedNumberObjectNames.length; ++i) {
			if ( null == usedNumberObjectNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedNumberObjectNames[ i], numberObjectName, otherSpotsHaveThisNumberObjectName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"number object = " + usedNumberObjectNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_number_object_names() {
		return null;
	}

	/**
	 * @param row
	 * @param roleVariableName
	 * @param otherSpotsHaveThisRoleVariableName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_role_variable_name(int row, String roleVariableName, boolean otherSpotsHaveThisRoleVariableName, String headName, Vector ranges, Rules rules, Role role) {
		String usedRoleVariableNames[] = get_used_role_variable_names();
		if ( null == usedRoleVariableNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedRoleVariableNames.length; ++i) {
			if ( null == usedRoleVariableNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedRoleVariableNames[ i], roleVariableName, otherSpotsHaveThisRoleVariableName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"role variable = " + usedRoleVariableNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_role_variable_names() {
		return null;
	}

	/**
	 * @param row
	 * @param timeVariableName
	 * @param otherSpotsHaveThisTimeVariableName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_time_variable_name(int row, String timeVariableName, boolean otherSpotsHaveThisTimeVariableName, String headName, Vector ranges, Rules rules, Role role) {
		String usedTimeVariableNames[] = get_used_time_variable_names();
		if ( null == usedTimeVariableNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedTimeVariableNames.length; ++i) {
			if ( null == usedTimeVariableNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedTimeVariableNames[ i], timeVariableName, otherSpotsHaveThisTimeVariableName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"time variable = " + usedTimeVariableNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_time_variable_names() {
		return null;
	}

	/**
	 * @param row
	 * @param classVariableName
	 * @param otherSpotsHaveThisClassVariableName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_class_variable_name(int row, String classVariableName, boolean otherSpotsHaveThisClassVariableName, String headName, Vector ranges, Rules rules, Role role) {
		String[] usedClassVariableNames = get_used_class_variable_names( role);
		if ( null == usedClassVariableNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedClassVariableNames.length; ++i) {
			if ( null == usedClassVariableNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedClassVariableNames[ i], classVariableName, otherSpotsHaveThisClassVariableName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"class variable = " + usedClassVariableNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param role
	 * @return
	 */
	protected String[] get_used_class_variable_names(Role role) {
		return null;
	}

	/**
	 * @param row
	 * @param fileName
	 * @param other_spots_have_this_object_name
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_file_name(int row, String fileName, boolean otherSpotsHaveThisFileName, String headName,Vector ranges, Rules rules, Role role) {
		String[] usedFileNames = get_used_file_names();
		if ( null == usedFileNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedFileNames.length; ++i) {
			if ( null == usedFileNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedFileNames[ i], fileName, otherSpotsHaveThisFileName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"file = " + usedFileNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_file_names() {
		return null;
	}

	/**
	 * @param row
	 * @param exchangeAlgebraName
	 * @param otherSpotsHaveThisExchangeAlgebraName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_exchange_algebra_name(int row, String exchangeAlgebraName, boolean otherSpotsHaveThisExchangeAlgebraName, String headName, Vector ranges, Rules rules, Role role) {
		String used_exchange_algebra_names[] = get_used_exchange_algebra_names( role);
		if ( null == used_exchange_algebra_names)
			return true;

		boolean result = true;

		for ( int i = 0; i < used_exchange_algebra_names.length; ++i) {
			if ( null == used_exchange_algebra_names[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( used_exchange_algebra_names[ i], exchangeAlgebraName, otherSpotsHaveThisExchangeAlgebraName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"exchange algebra = " + used_exchange_algebra_names[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param role
	 * @return
	 */
	protected String[] get_used_exchange_algebra_names(Role role) {
		return null;
	}

	/**
	 * @param row
	 * @param exTransferName
	 * @param otherSpotsHaveThisExTransferName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_extransfer_name(int row, String exTransferName, boolean otherSpotsHaveThisExTransferName, String headName, Vector ranges, Rules rules, Role role) {
		// TODO Auto-generated method stub
		String usedExtransferNames[] = get_used_extransfer_names( role);
		if ( null == usedExtransferNames)
			return true;

		boolean result = true;

		for ( String usedExtransferName:usedExtransferNames) {
			if ( null == usedExtransferName)
				continue;

			if ( !CommonRuleManipulator.correspond( usedExtransferName, exTransferName, otherSpotsHaveThisExTransferName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"exchange algebra = " + usedExtransferName,
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param role
	 * @return
	 */
	protected String[] get_used_extransfer_names(Role role) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param row
	 * @param player
	 * @param numberObjectName
	 * @param newType
	 * @param rules
	 * @param role
	 * @return
	 */
	public boolean is_number_object_type_correct(int row, String player, String numberObjectName, String newType, Rules rules, Role role) {
		return true;
	}

	/**
	 * @param name
	 * @param row
	 * @param roleName
	 * @param rules
	 * @return
	 */
	public boolean can_remove_role(String name, int row, String roleName, Rules rules) {
		String used_role_names[] = get_used_role_names();
		if ( null == used_role_names)
			return true;

		boolean result = true;

		for ( int i = 0; i < used_role_names.length; ++i) {
			if ( null == used_role_names[ i])
				continue;

			if ( !used_role_names[ i].equals( roleName))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + name,
				"type = " + _kind + "::" + _type,
				"role = " + used_role_names[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_role_names() {
		return null;
	}

	/**
	 * @param name
	 * @param row
	 * @param function
	 * @param rules
	 * @return
	 */
	public boolean can_remove_expression(String name, int row, String function, Rules rules) {
		ExpressionElements[] expressionElements = get_used_expressions();
		if ( null == expressionElements)
			return true;

		boolean result1 = true;
		boolean result2 = true;
		for ( int i = 0; i < expressionElements.length; ++i) {
			if ( 0 <= expressionElements[ i]._function.indexOf( function + "(")) {
				WarningManager.get_instance().add(
					new String[] {
						"Role",
						"name = " + name,
						"type = "
							+ ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.number.object")) ? "condition" : "command")
							+ "::" + _type,
						"function = " + expressionElements[ i]._function,
						"row = " + ( row + 1),
						"column = " + ( _column + 1)}
				);
				result1 = false;
			}

			String expression_string
				= VisualShellExpressionManager.get_instance().get_expression( expressionElements[ i]._function);
			if ( 0 <= expression_string.indexOf( function + "(")) {
				WarningManager.get_instance().add(
					new String[] {
						"Role",
						"name = " + name,
						"type = "
							+ ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.number.object")) ? "condition" : "command")
							+ "::" + _type,
						"expression = " + expression_string,
						"row = " + ( row + 1),
						"column = " + ( _column + 1)}
				);
				result2 = false;
			}
		}

		return ( result1 && result2);
	}

	/**
	 * @return
	 */
	protected ExpressionElements[] get_used_expressions() {
		return null;
	}

	/**
	 * @param name
	 * @param row
	 * @param newName
	 * @param originalName
	 * @param rules
	 * @return
	 */
	public boolean update_stage_name(String name, int row, String newName, String originalName, Rules rules) {
		return update_stage_name( newName, originalName);
	}

	/**
	 * @param newName
	 * @param originalName
	 * @return
	 */
	public boolean update_stage_name(String newName, String originalName) {
		return false;
	}

	/**
	 * @param name
	 * @param row
	 * @param stageName
	 * @param rules
	 * @return
	 */
	public boolean can_remove_stage_name(String name, int row, String stageName, Rules rules) {
		String[] usedStageNames = get_used_stage_names();
		if ( null == usedStageNames)
			return true;

		boolean result = true;

		for ( String usedStageName:usedStageNames) {
			if ( null == usedStageName)
				continue;

			if ( !usedStageName.equals( stageName))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + name,
				"type = "
					+ ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")) ? "condition" : "command")
					+ "::" + _type,
				"stage = " + _value,
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};

			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param stageName
	 * @return
	 */
	public boolean can_remove_stage_name(String stageName) {
		String[] usedStageNames = get_used_stage_names();
		if ( null == usedStageNames)
			return true;

		boolean result = true;

		for ( String usedStageName:usedStageNames) {
			if ( null == usedStageName)
				continue;

			if ( !usedStageName.equals( stageName))
				continue;

			result = false;
		}
		return result;
	}

	/**
	 * @param name
	 * @param row
	 * @param stageNames
	 * @param rules
	 * @return
	 */
	public boolean can_adjust_stage_name(String name, int row, Vector<String> stageNames, Rules rules) {
		String[] usedStageNames = get_used_stage_names();
		if ( null == usedStageNames)
			return true;

		boolean result = true;

		for ( String usedStageName:usedStageNames) {
			if ( null == usedStageName)
				continue;

			if ( stageNames.contains( usedStageName))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + name,
				"type = "
					+ ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")) ? "condition" : "command")
					+ "::" + _type,
				"stage = " + _value,
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};

			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_stage_names() {
		return null;
	}

	/**
	 * @return
	 */
	public boolean update_stage_manager() {
		return false;
	}

	/**
	 * @param expressionMap
	 * @param usedExpressionMap
	 */
	public void get_used_expressions(TreeMap expressionMap, TreeMap usedExpressionMap) {
	}

	/**
	 * @param originalFunctionName
	 * @param newFunctionName
	 * @return
	 */
	public boolean update_function(String originalFunctionName, String newFunctionName) {
		return false;
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
		return false;
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
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @return
	 */
	public boolean update_role_name(String originalName, String newName) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param rules
	 * @return
	 */
	public boolean update_role_name(String originalName, String newName, Rules rules) {
		return update_role_name( originalName, newName);
	}

	/**
	 * @param kind
	 * @param originalName
	 * @param newName
	 * @param player
	 * @param rules
	 * @param role
	 * @return
	 */
	public boolean update_object_name(String kind, String originalName, String newName, String player, Rules rules, Role role) {
		if ( kind.equals( "spot variable"))
			return update_spot_variable_name( originalName, newName, player, role);
		else if ( kind.equals( "probability"))
			return update_probability_name( originalName, newName, player, role);
		else if ( kind.equals( "collection"))
			return update_collection_name( originalName, newName, player, role);
		else if ( kind.equals( "list"))
			return update_list_name( originalName, newName, player, role);
		else if ( kind.equals( "map"))
			return update_map_name( originalName, newName, player, role);
		else if ( kind.equals( "keyword"))
			return update_keyword_name( originalName, newName, player, role);
		else if ( kind.equals( "number object"))
			return update_number_object_name( originalName, newName, player, role);
		else if ( kind.equals( "role variable"))
			return update_role_variable_name( originalName, newName, player, role);
		else if ( kind.equals( "time variable"))
			return update_time_variable_name( originalName, newName, player, role);
		else if ( kind.equals( "class variable"))
			return update_class_variable_name( originalName, newName, player, role);
		else if ( kind.equals( "file"))
			return update_file_name( originalName, newName, player, role);
		else if ( kind.equals( "exchange algebra"))
			return update_exchange_algebra_name( originalName, newName, player, role);
		else if ( kind.equals( "extransfer"))
			return update_extransfer_name( originalName, newName, player, role);

		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param player
	 * @param role
	 * @return
	 */
	protected boolean update_spot_variable_name(String originalName, String newName, String player, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param player
	 * @param role
	 * @return
	 */
	protected boolean update_probability_name(String originalName, String newName, String player, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param player
	 * @param role
	 * @return
	 */
	protected boolean update_collection_name(String originalName, String newName, String player, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param player
	 * @param role
	 * @return
	 */
	protected boolean update_list_name(String originalName, String newName, String player, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param player
	 * @param role
	 * @return
	 */
	protected boolean update_map_name(String originalName, String newName, String player, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param player
	 * @param role
	 * @return
	 */
	protected boolean update_keyword_name(String originalName, String newName, String player, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param player
	 * @param role
	 * @return
	 */
	protected boolean update_number_object_name(String originalName, String newName, String player, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param player
	 * @param role
	 * @return
	 */
	protected boolean update_role_variable_name(String originalName, String newName, String player, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param player
	 * @param role
	 * @return
	 */
	protected boolean update_time_variable_name(String originalName, String newName, String player, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param player
	 * @param role
	 * @return
	 */
	protected boolean update_class_variable_name(String originalName, String newName, String player, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param player
	 * @param role
	 * @return
	 */
	protected boolean update_file_name(String originalName, String newName, String player, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param player
	 * @param role
	 * @return
	 */
	protected boolean update_exchange_algebra_name(String originalName, String newName, String player, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param player
	 * @param role
	 * @return
	 */
	protected boolean update_extransfer_name(String originalName, String newName, String player, Role role) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @param visualShellExpressionManager
	 * @return
	 */
	public boolean update_expression(VisualShellExpressionManager visualShellExpressionManager) {
		return false;
	}

	/**
	 * @param row
	 * @param rules
	 * @param role
	 * @param drawObjects
	 * @return
	 */
	public boolean can_paste(int row, Rules rules, Role role, Layer drawObjects) {
		if ( can_paste( role, drawObjects))
			return true;

		String[] message = new String[] {
			"Role",
			"name = " + role._name,
			"type = " + _kind + "::" + _type,
			"row = " + ( row + 1),
			"column = " + ( _column + 1),
			_value
		};

		WarningManager.get_instance().add( message);

		return false;
	}

	/**
	 * @param role
	 * @param drawObjects
	 * @return
	 */
	protected boolean can_paste(Role role, Layer drawObjects) {
		return true;
	}

	/**
	 * @param row
	 * @param initialValueMap
	 * @param demo
	 * @param role
	 * @return
	 */
	public String get_script(int row, InitialValueMap initialValueMap, boolean demo, Role role) {
		String value = ( null == initialValueMap || demo) ? _value : initialValueMap.get_script( _value);
		if ( null == value) {
			on_get_script_error( row, role);
			return "";
		}

		if ( ( ( _kind.equals( "condition") && _type.equals( ResourceManager.get_instance().get( "rule.type.condition.number.object")))
			|| ( _kind.equals( "command") && _type.equals( ResourceManager.get_instance().get( "rule.type.command.substitution"))))
			&& null != initialValueMap && demo) {
			value = initialValueMap.get_script( _value);
			if ( null == value) {
				on_get_script_error( row, role);
				return "";
			}
		}

		value = get_script( value, role);
		if ( null == value) {
			on_get_script_error( row, role);
			return "";
		}

		if ( value.equals( "")
			&& !_type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage"))) {
			on_get_script_error( row, role);
			return "";
		}

		if ( role instanceof SpotRole
			&& !value.startsWith( "<")
			&& !value.startsWith( "!<")
			&& !_type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage"))
			&& !_type.equals( ResourceManager.get_instance().get( "rule.type.condition.others"))
			&& !_type.equals( ResourceManager.get_instance().get( "rule.type.command.next.stage"))
			&& !_type.equals( ResourceManager.get_instance().get( "rule.type.command.terminate"))
			&& !_type.equals( ResourceManager.get_instance().get( "rule.type.command.trace"))
			&& !_type.equals( ResourceManager.get_instance().get( "rule.type.command.others")))
			return ( ( value.startsWith( "!"))
				? ( "!<>" + value.substring( "!".length()))
				: ( "<>" + value));

		return value;
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	protected String get_script(String value, Role role) {
		return value;
	}

	/**
	 * @param row
	 * @param role
	 */
	private void on_get_script_error(int row, Role role) {
		String[] message = new String[] {
			"Role",
			"name = " + role._name,
			"type = " + _kind + "::" + _type,
			"row = " + ( row + 1),
			"column = " + ( _column + 1),
			_value
		};
		WarningManager.get_instance().add( message);
	}

	/**
	 * @return
	 */
	public String get_cell_text() {
		return _value;
	}

	/**
	 * @param ruleData 
	 * @param roleDataSet
	 * @param agentNameMap
	 * @param spotNameMap
	 * @param imports
	 * @param commands
	 * @param initialValueMap
	 * @param packagePrefix 
	 * @param role 
	 * @param initialStage
	 * @return
	 */
	public List<String> get_java_program(RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String>imports, List<String>commands, InitialValueMap initialValueMap, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		String value = ( null == initialValueMap) ? _value : initialValueMap.get_script( _value);
		if ( null == value) {
			//on_get_script_error( row, role);
			return get_unknown_codes();
		}

		return get_java_program( value, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
	}

	/**
	 * @param value
	 * @param ruleData 
	 * @param roleDataSet
	 * @param agentNameMap
	 * @param spotNameMap
	 * @param imports
	 * @param commands
	 * @param packagePrefix
	 * @param role
	 * @param initialStage
	 * @return
	 */
	public List<String> get_java_program(String value, RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		return get_unknown_codes();
	}

	/**
	 * @return
	 */
	public static List<String> get_empty_codes() {
		// TODO Auto-generated method stub
		List<String> codes = new ArrayList<>();
		codes.add( "");
		return codes;
	}

	/**
	 * @return
	 */
	public static List<String> get_unknown_codes() {
		// TODO Auto-generated method stub
		List<String> codes = new ArrayList<>();
		codes.add( "Unknown");
		return codes;
	}

	/**
	 * @param entity
	 * @param variables
	 * @param ruleData 
	 * @param roleDataSet
	 * @param agentNameMap
	 * @param spotNameMap
	 * @param imports
	 * @param commands
	 * @param packagePrefix
	 * @param role
	 * @param initialStage
	 * @return
	 */
	public List<String> get_prefix(String entity, List<Variable> variables, RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		if ( entity.equals( ""))
			// TODO この変数を持つエージェントロールクラスを見極める必要がある(ロールクラス名-変数名配列のハッシュテーブルが必要)
			//String command = roleDataSet._roleClassNameMap.get( role._name) + " ownerRole = (" + roleDataSet._roleClassNameMap.get( role._name) + ") getOwnerRole()";
			return get_agent_prefix( variables, ruleData, roleDataSet, imports, commands, packagePrefix, initialStage);
		else if ( entity.equals( "<>")) {
			if ( AgentRole.class.isInstance( role))
				// この変数を持つスポットロールクラスを見極める必要がある(ロールクラス名-変数名配列のハッシュテーブルが必要)
				//command = /*TSpotRole*/"ここどーすんの？" + " currentSpotRole = (" + /*TSpotRole*/"ここどーすんの？" + ") currentSpot.getRole(); // 現在いるスポットの役割";
				return get_current_spot_prefix_on_agent_role( variables, ruleData, roleDataSet, imports, commands, packagePrefix, initialStage);
			else {
				String prefix = get_current_spot_prefix_on_spot_role( roleDataSet._roleClassNameMap, role._name, imports, commands, packagePrefix, initialStage);
				if ( null == prefix)
					return null;

				List<String> prefixes = new ArrayList<>();
				for ( int i = 0; i < variables.size(); ++i)
					prefixes.add( prefix);
				return prefixes;
			}
		} else {
			String prefix = get_spot_prefix( entity.substring( 1, entity.length() - 1), roleDataSet._roleClassNameMap, roleDataSet._roleOwnersMap, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
			if ( null == prefix)
				return null;

			List<String> prefixes = new ArrayList<>();
			for ( int i = 0; i < variables.size(); ++i)
				prefixes.add( prefix);
			return prefixes;
		}
	}

	/**
	 * @param variables
	 * @param ruleData 
	 * @param roleDataSet 
	 * @param imports
	 * @param commands
	 * @param packagePrefix
	 * @param initialStage
	 * @return
	 */
	private List<String> get_agent_prefix(List<Variable> variables, RuleData ruleData, RoleDataSet roleDataSet, List<String> imports, List<String> commands, String packagePrefix, boolean initialStage) {
		List<String> prefixes = new ArrayList<>();

		List<String> ruleOwnerRoleClassNames = roleDataSet.get_ruleOwnerRoleClassNames( "agent", ruleData);	// このルールクラスを保持するロールクラス集合
		if ( null == ruleOwnerRoleClassNames)
			return null;
		else {
			for ( Variable variable:variables) {
				List<String> variableOwnerRoleClassNames = roleDataSet.get_variableOwnerRoleClassNames( "agent", variable._kind, variable._name);	// この変数を保持するロールクラス集合
				if ( variableOwnerRoleClassNames.isEmpty()) {
					//System.out.println( "Error! agent " + variable._kind + " " + variable._name);
					return null;
				} else {
					//System.out.println( ruleData._ruleClassName);
					//System.out.println( "\t" + variable._kind + " " + variable._name);
					//System.out.println( "\t\tRule's owner roles" + ruleOwnerRoleClassNames);
					//System.out.println( "\t\tVariable's owner roles" + variableOwnerRoleClassNames);
					if ( 1 == ruleOwnerRoleClassNames.size() && 1 == variableOwnerRoleClassNames.size()) {
						// このルールを保持しているロールとこの変数も保持しているロールがそれぞれ１つだけの場合
						if ( ruleOwnerRoleClassNames.get( 0).equals( "TAgentRole")) {
							// このルールを保持しているロールがTAgentRoleの場合
							if ( variableOwnerRoleClassNames.get( 0).equals( "TAgentRole")) {
								// この変数を保持しているロールがTAgentRoleの場合
								prefixes.add( "ownerRole");
								if ( !initialStage) {
									JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent.TAgentRole", imports);
									JavaProgramExporter.append_command( "TAgentRole ownerRole = (TAgentRole) getOwnerRole()", commands);
								}
							} else {
								// TODO これどーすんの？
								// この変数を保持しているロールがTAgentRoleではない場合
								return null;
							}	
						} else {
							// このルールを保持しているロールがTAgentRoleではない場合
							if ( variableOwnerRoleClassNames.get( 0).equals( "TAgentRole")) {
								// この変数を保持しているロールがTAgentRoleの場合
								prefixes.add( "ownerRole");
								if ( !initialStage) {
									JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent.TAgentRole", imports);
									JavaProgramExporter.append_command( "TAgentRole ownerRole = (TAgentRole) getOwnerRole().getMergedRole(\"AgentRole\")", commands);
								}
							} else {
								// この変数を保持しているロールがTAgentRoleではない場合
								if ( !ruleOwnerRoleClassNames.get( 0).equals( variableOwnerRoleClassNames.get( 0)))
									// 同じロールクラスでなければ矛盾？
									return null;
								prefixes.add( JavaProgramExporter.get_roleClassLocalVariableName( ruleOwnerRoleClassNames.get( 0)));
								if ( !initialStage) {
									JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + ruleOwnerRoleClassNames.get( 0), imports);
									JavaProgramExporter.append_command( ruleOwnerRoleClassNames.get( 0) + " ownerRole = (" + ruleOwnerRoleClassNames.get( 0) + ") getOwnerRole()", commands);
								}
							}
						}
					} else {
						// このルールを保持しているロールとこの変数も保持しているロールのどちらかが少なくとも２つ以上の場合
						if ( 1 < ruleOwnerRoleClassNames.size()) {
							// このルールを保持しているロールが２つ以上の場合
							if ( ruleOwnerRoleClassNames.contains( "TAgentRole")) {
								// この変数を保持しているロールにTAgentRoleが含まれている場合
								// このルールを保持しているロールが複数存在し、この変数を保持するロールにもしTAgentRoleが含まれている場合 → この変数を保持するロールはTAgentRoleの１つだけの筈
								if ( initialStage)
									prefixes.add( "ownerRole");
								else {
									prefixes.add( "");
									for ( String ruleOwnerRoleClassName:ruleOwnerRoleClassNames)
										JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + ruleOwnerRoleClassName, imports);
								}
							} else {
								// このルールを保持しているロールにTAgentRoleが含まれていない場合
								if ( 1 == variableOwnerRoleClassNames.size()) {
									// この変数を保持しているロールが１つだけの場合
									if ( variableOwnerRoleClassNames.get( 0).equals( "TAgentRole")) {
										// この変数を保持しているロールがTAgentRoleの場合
										prefixes.add( "ownerRole");
										if ( !initialStage) {
											JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent.TAgentRole", imports);
											JavaProgramExporter.append_command( "TAgentRole ownerRole = (TAgentRole) getOwnerRole().getMergedRole(\"AgentRole\")", commands);
										}
									} else {
										// この変数を保持しているロールがTAgentRoleではない場合
										prefixes.add( JavaProgramExporter.get_roleClassLocalVariableName( variableOwnerRoleClassNames.get( 0)));
										if ( !initialStage) {
											JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + variableOwnerRoleClassNames.get( 0), imports);
											JavaProgramExporter.append_command( variableOwnerRoleClassNames.get( 0) + " ownerRole = (" + variableOwnerRoleClassNames.get( 0) + ") getOwnerRole()", commands);
										}
									}
								} else {
									// この変数を保持しているロールが２つ以上の場合
									// TAgentRoleが含まれていることはない
									// TAgentRoleが保持する変数を他のロールクラスが保持することはありえない
									// もし"TAgentRole"含まれているのであればvariableOwnerRoleClassNamesは１つだけの筈
									List<String> roleClassNames = new ArrayList<>();
									for ( String variableOwnerRoleClassName:variableOwnerRoleClassNames) {
										if ( ruleOwnerRoleClassNames.contains( variableOwnerRoleClassName))
											roleClassNames.add( variableOwnerRoleClassName);
									}
									if ( roleClassNames.isEmpty())
										// ロール名が存在しないと云うことは無い筈！
										return null;
									else if ( 1 == roleClassNames.size()) {
										prefixes.add( JavaProgramExporter.get_roleClassLocalVariableName( roleClassNames.get( 0)));
										if ( !initialStage) {
											JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + roleClassNames.get( 0), imports);
											JavaProgramExporter.append_command( roleClassNames.get( 0) + " ownerRole = (" + roleClassNames.get( 0) + ") getOwnerRole()", commands);
										}
									} else {
										if ( initialStage)
											prefixes.add( "ownerRole");
										else {
											prefixes.add( "");
											for ( String roleClassName:roleClassNames)
												JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + roleClassName, imports);
										}
									}
								}
							}
						} else {
							// このルールを保持しているロールが１つだけの場合 (この変数を保持しているロールが２つ以上)
							if ( ruleOwnerRoleClassNames.get( 0).equals( "TAgentRole")) {
								// このルールを保持しているロールがTAgentRoleで、この変数を保持しているロールはTAgentRole以外の複数のロール
								// TODO キャストして使うしかない？
								if ( initialStage)
									prefixes.add( "ownerRole");
								else {
									prefixes.add( "");
									for ( String variableOwnerRoleClassName:variableOwnerRoleClassNames)
										JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + variableOwnerRoleClassName, imports);
								}
							} else {
								// このルールを保持しているロールがTAgentRoleではなく、この変数を保持しているロールもTAgentRole以外の複数のロール
								if ( initialStage)
									// TODO 初期ステージでの処理なら引数のownerRoleが変数を保持していることを期待
									prefixes.add( "ownerRole");
								else {
									// TODO キャストして使うしかない？
									prefixes.add( "");
									for ( String variableOwnerRoleClassName:variableOwnerRoleClassNames)
										JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + variableOwnerRoleClassName, imports);
								}
							}
						}
					}
				}
			}
			return prefixes;
		}

//		System.out.println( ruleData._ruleClassName);
//		for ( Variable variable:variables) {
//			System.out.println( "\t" + variable._kind + " " + variable._name);
//			System.out.println( "\t\t" + roleClassNameMap.get( variable));
//		}

//		for ( Variable variable:variables) {
//			List<String> roleClassNames = roleClassNameMap.get( variable);
//			if ( 1 == roleClassNames.size()) {
//				// このルールを保持していて尚且つこの変数も保持しているロールが１つだけの場合
//				if ( roleClassNames.get( 0).equals( "TAgentRole")) {
//					// そのロールがTAgentRoleの場合
//					prefixes.add( "ownerRole");
//					String command = "TAgentRole ownerRole = (TAgentRole) getOwnerRole()";
//					if ( !imports.contains( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent.TAgentRole"))
//						imports.add( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent.TAgentRole");
//					if ( !commands.contains( command))
//						commands.add( command);
//				} else {
//					// そのロールがTAgentRoleではない場合
//					prefixes.add( JavaProgramExporter.get_roleClassLocalVariableName( roleClassNames.get( 0)));
//					String command = roleClassNames.get( 0) + " ownerRole = (" + roleClassNames.get( 0) + ") getOwnerRole()";
//					if ( !commands.contains( command))
//						commands.add( command);
//					if ( !imports.contains( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + roleClassNames.get( 0)))
//						imports.add( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + roleClassNames.get( 0));
//				}
//			} else {
//				// このルールを保持していて尚且つこの変数も保持しているロールが複数存在する場合
//				// roleClassNamesにTAgentRoleが含まれていたらどーすんの？
//				prefixes.add( "");
//				for ( String roleClassName:roleClassNames) {
//					if ( !imports.contains( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + roleClassName))
//						imports.add( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + roleClassName);
//				}
//			}
//		}
//
//		return prefixes;
			//System.out.println( "Error! agent " + ruleData._ruleClassName + " " + variable._kind + " " + variable._name);
//		for ( Variable variable:variables) {
//			List<String> arcns = roleDataSet.get_sameVariableRoleClassNames( "agent", ruleData, variable);
//			if ( null == arcns)
//				System.out.println( "Error! agent " + ruleData._ruleClassName + " " + variable._kind + " " + variable._name);
//		}

//		List<String> agentRoleClassNames = get_roleClassNames( "agent", variables, roleDataSet._variableMap);
//
//		for ( String agentRoleClassName:agentRoleClassNames) {
//			if ( roleDataSet._roleClassNameMap.get( ruleData._roleName).equals( "TAgentRole")) {
//				// このルールを所有しているロールはTAgentRole
//				if ( agentRoleClassName.equals( "TAgentRole")) {
//					//	変数を所有していーロールがTAgentRole
//					prefixes.add( "ownerRole");
//					String command = "TAgentRole ownerRole = (TAgentRole) getOwnerRole()";
//					if ( !imports.contains( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent.TAgentRole"))
//						imports.add( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent.TAgentRole");
//					if ( !commands.contains( command))
//						commands.add( command);
//				} else {
//					// TODO これどーすんの？
//					// 変数を所有していーロールがTAgentRoleではない
//					for ( Variable variable:variables) {
//						if ( roleDataSet.has_this_role_class_name( agentRoleClassName, "agent", ruleData._ruleClassName, variable._kind, "f" + variable._name)) {
//							// TAgent以外の複数のロールクラスがこの変数を保持している
//							prefixes.add( "");
//						} else {
//							// TAgent以外の1つのロールクラスがこの変数を保持している
//							prefixes.add( JavaProgramExporter.get_roleClassLocalVariableName( agentRoleClassName));
//							String command = agentRoleClassName + " ownerRole = (" + agentRoleClassName + ") getOwnerRole()";
//							if ( !commands.contains( command))
//								commands.add( command);
//						}
//						if ( !imports.contains( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + agentRoleClassName))
//							imports.add( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + agentRoleClassName);
//					}
//				}
//			} else {
//				// このルールを所有しているロールはTAgentRoleではない
//				if ( agentRoleClassName.equals( "TAgentRole")) {
//					//	変数を所有していーロールがTAgentRole
//					prefixes.add( "ownerRole");
//					String command = "TAgentRole ownerRole = (TAgentRole) getOwnerRole().getMergedRole(\"AgentRole\")";
//					if ( !imports.contains( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent.TAgentRole"))
//						imports.add( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent.TAgentRole");
//					if ( !commands.contains( command))
//						commands.add( command);
//				} else {
//					// 変数を所有していーロールがTAgentRoleではない
//					for ( Variable variable:variables) {
//						// TAgent以外の複数のロールクラスがこの変数を保持している
//						if ( roleDataSet.has_this_role_class_name( agentRoleClassName, "agent", ruleData._ruleClassName, variable._kind, "f" + variable._name)) {
//							prefixes.add( "");
//						} else {
//							// TAgent以外の1つのロールクラスがこの変数を保持している
//							prefixes.add( JavaProgramExporter.get_roleClassLocalVariableName( agentRoleClassName));
//							String command = agentRoleClassName + " ownerRole = (" + agentRoleClassName + ") getOwnerRole()";
//							if ( !commands.contains( command))
//								commands.add( command);
//						}
//						if ( !imports.contains( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + agentRoleClassName))
//							imports.add( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "agent." + agentRoleClassName);
//					}
//				}
//			}
//		}
//		return prefixes;
	}

	/**
	 * @param variables
	 * @param ruleData
	 * @param roleDataSet
	 * @param imports
	 * @param commands
	 * @param packagePrefix
	 * @param initialStage
	 * @return
	 */
	private List<String> get_current_spot_prefix_on_agent_role(List<Variable> variables, RuleData ruleData, RoleDataSet roleDataSet, List<String> imports, List<String> commands, String packagePrefix, boolean initialStage) {
		// TODO Auto-generated method stub
		List<String> prefixes = new ArrayList<>();

		List<String> roleClassNames = new ArrayList<>();
		for ( Variable variable:variables) {
			List<String> variableOwnerRoleClassNames = roleDataSet.get_variableOwnerRoleClassNames( "spot", variable._kind, variable._name);	// この変数を保持するロールクラス集合
			if ( variableOwnerRoleClassNames.isEmpty()) {
				//System.out.println( "Error! agent " + variable._kind + " " + variable._name);
				return null;
			} else {
				//System.out.println( ruleData._ruleClassName);
				//System.out.println( "\t" + variable._kind + " " + variable._name);
				//System.out.println( "\t\tVariable's owner roles" + variableOwnerRoleClassNames);
				for ( String variableOwnerRoleClassName:variableOwnerRoleClassNames) {
					if ( !roleClassNames.contains( variableOwnerRoleClassName))
						roleClassNames.add( variableOwnerRoleClassName);
				}
			}
	
			if ( roleClassNames.isEmpty())
				return null;
			else if ( 1 == roleClassNames.size()) {
				prefixes.add( "currentSpotRole");
				if ( !initialStage) {
					JavaProgramExporter.append_command( "TSpot currentSpot = spotSet.get(getAgent().getCurrentSpotName()); // 現在いるスポット", commands);
					JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "spot." + roleClassNames.get( 0), imports);
					JavaProgramExporter.append_command( roleClassNames.get( 0) + " currentSpotRole = (" + roleClassNames.get( 0) + ") currentSpot.getRole(); // 現在いるスポットの役割", commands);
				}
			} else {
				prefixes.add( "");
				variable._spotSet = true;
				for ( String roleClassName:roleClassNames) 
					JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "spot." + roleClassName, imports);
				roleDataSet.set_current_spot_roel_data( ruleData, variable, roleClassNames);
			}
		}

		return prefixes;

//		// variablesに含まれる変数を保持しているロールクラス集合を取得する
//		List<String> spotRoleClassNames = get_roleClassNames( "spot", variables, roleDataSet._variableMap);
//
//		for ( String spotRoleClassName:spotRoleClassNames) {
//			prefixes.add( "currentSpotRole");
//			command = spotRoleClassName + " currentSpotRole = (" + spotRoleClassName + ") currentSpot.getRole(); // 現在いるスポットの役割";
//			if ( !imports.contains( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "spot." + spotRoleClassName))
//				imports.add( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "spot." + spotRoleClassName);
//			if ( !commands.contains( command))
//				commands.add( command);
//		}
//
//		return prefixes;
	}

//	/**
//	 * @param variables
//	 * @param variableMap
//	 * @param imports
//	 * @param commands
//	 * @param packagePrefix
//	 * @param initialStage
//	 * @return
//	 */
//	private List<String> get_current_spot_prefix_on_agent_role(List<Variable> variables, Map<String, Map<String, Map<String, List<String>>>> variableMap, List<String> imports, List<String> commands, String packagePrefix, boolean initialStage) {
//		// TODO Auto-generated method stub
//		List<String> prefixes = new ArrayList<>();
//
//		String command = "TSpot currentSpot = spotSet.get(getAgent().getCurrentSpotName()); // 現在いるスポット";
//		if ( !commands.contains( command))
//			commands.add( command);
//
//		// variablesに含まれる変数を保持しているロールクラス集合を取得する
//		List<String> spotRoleClassNames = get_roleClassNames( "spot", variables, variableMap);
//
//		for ( String spotRoleClassName:spotRoleClassNames) {
//			prefixes.add( "currentSpotRole");
//			command = spotRoleClassName + " currentSpotRole = (" + spotRoleClassName + ") currentSpot.getRole(); // 現在いるスポットの役割";
//			if ( !imports.contains( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "spot." + spotRoleClassName))
//				imports.add( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "spot." + spotRoleClassName);
//			if ( !commands.contains( command))
//				commands.add( command);
//		}
//
//		return prefixes;
//	}

	/**
	 * @param roleClassNameMap
	 * @param roleName
	 * @param imports
	 * @param commands
	 * @param packagePrefix
	 * @param initialStage
	 * @return
	 */
	private /*List<String>*/String get_current_spot_prefix_on_spot_role(Map<String, String> roleClassNameMap, String roleName, List<String> imports, List<String> commands, String packagePrefix, boolean initialStage) {
		// TODO Auto-generated method stub
		//List<String> prefixes = new ArrayList<>();
		//prefixes.add( "currentSpotRole");
		if ( !initialStage) {
			JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "spot." + roleClassNameMap.get( roleName), imports);
			JavaProgramExporter.append_command( roleClassNameMap.get( roleName) + " currentSpotRole = (" + roleClassNameMap.get( roleName) + ") getOwnerRole()", commands);
		}
		return "currentSpotRole";
		//return prefixes;
	}

	/**
	 * @param fullname
	 * @param roleClassNameMap
	 * @param roleOwnersMap
	 * @param agentNameMap
	 * @param spotNameMap
	 * @param imports
	 * @param commands
	 * @param packagePrefix
	 * @param role
	 * @param initialStage
	 * @return
	 */
	private String /*List<String>*/ get_spot_prefix(String fullname, Map<String, String> roleClassNameMap, Map<String, List<PlayerBase>> roleOwnersMap, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		return JavaProgramExporter.get_role_class_local_variable_name( fullname, roleClassNameMap, roleOwnersMap, agentNameMap, spotNameMap, imports, commands, packagePrefix, "spot", role, initialStage);
	}

//	/**
//	 * variablesに含まれる各変数を保持しているロールクラス集合を返す
//	 * @param type
//	 * @param variables
//	 * @param variableMap
//	 * @return
//	 */
//	private Map<Variable, List<String>> get_roleClassNamesMap(String type, List<Variable> variables, Map<String, Map<String, Map<String, List<String>>>> variableMap) {
//		// TODO Auto-generated method stub
//		Map<Variable, List<String>> roleClassNamesMap = new HashMap<>();
//		Map<String, Map<String, List<String>>> map1 = variableMap.get( type);
//		if ( null == map1)
//			return roleClassNamesMap;
//		Set<String> rcns = map1.keySet();
//		if ( rcns.isEmpty())
//			return roleClassNamesMap;
//		for ( String roleClassName:rcns) {
//			Map<String, List<String>> map2 = map1.get( roleClassName);
//			if ( null == map2)
//				continue;
//			for ( Variable variable:variables) {
//				List<String> list = map2.get( variable._type);
//				if ( null == list)
//					continue;
//				if ( !list.contains( "f" + variable._name))
//					break;
//				List<String> roleClassNames = roleClassNamesMap.get( variable);
//				if ( null == roleClassNames) {
//					roleClassNames = new ArrayList<>();
//					roleClassNamesMap.put( variable, roleClassNames);
//				}
//				roleClassNames.add( roleClassName);
//			}
//		}
//		return roleClassNamesMap;
//	}

//	/**
//	 * variablesに含まれる変数を保持しているロールクラス集合を返す
//	 * @param type
//	 * @param variables
//	 * @param variableMap
//	 * @return
//	 */
//	private List<String> get_roleClassNames(String type, List<Variable> variables, Map<String, Map<String, Map<String, List<String>>>> variableMap) {
//		// TODO Auto-generated method stub
//		List<String> roleClassNames = new ArrayList<>();
//		Map<String, Map<String, List<String>>> map1 = variableMap.get( type);
//		if ( null == map1) {
//			roleClassNames.add( "UnknownRoleClass");
//			return roleClassNames;
//		}
//		Set<String> rcns = map1.keySet();
//		if ( rcns.isEmpty()) {
//			roleClassNames.add( "UnknownRoleClass");
//			return roleClassNames;
//		}
//		for ( String roleClassName:rcns) {
//			Map<String, List<String>> map2 = map1.get( roleClassName);
//			if ( null == map2)
//				continue;
//			for ( Variable variable:variables) {
//				List<String> list = map2.get( variable._kind);
//				if ( null == list)
//					continue;
//				if ( !list.contains( "f" + variable._name))
//					break;
//				roleClassNames.add( roleClassName);
//			}
//		}
//
//		if ( roleClassNames.isEmpty()) {
//			roleClassNames.add( "UnknownRoleClass");
//			for ( Variable variable:variables)
//				System.out.println( type + ", " + variable._kind + " : " + variable._name);
//		}
//
//		return roleClassNames;
//	}

	/**
	 * @param rule
	 * @return
	 */
	public boolean same_as(Rule rule) {
		// TODO Auto-generated method stub
		return _kind.equals( rule._kind)
			&& _type.equals( rule._type)
			&& _value.equals( rule._value)
			&& ( _or == rule._or);
	}

	/**
	 * @param variables
	 * @param value
	 * @param role
	 */
	protected void get_variable(List<Variable> variables, String value, Role role) {
		// TODO Auto-generated method stub
	}

	/**
	 * @param writer
	 * @return
	 */
	public boolean write(/*String name, */Writer writer) throws SAXException {
		// 2011.6.6 _kindを使用するように変更
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "column", "", String.valueOf( _column));
		attributesImpl.addAttribute( null, null, "type", "", Writer.escapeAttributeCharData( _type));
		attributesImpl.addAttribute( null, null, "value", "", Writer.escapeAttributeCharData( _value));
		attributesImpl.addAttribute( null, null, "or", "", _or ? "true" : "false");
		writer.writeElement( null, null, _kind, attributesImpl);
		return true;
	}

	/**
	 * @param role
	 */
	public void make_unique_agent_name(Role role) {
//		if ( !_type.equals( ResourceManager.get_instance().get( "rule.type.command.create.agent")))
//			return;
//
//		String[] elements = CommonRuleManipulator2.get_elements( _value);
//		if ( null == elements || ( 2 != elements.length && 3 != elements.length))
//			return;
//
//		String name = "";
//		int index = 1;
//		while ( true) {
//			name = elements[ 0] + "(copy" + index + ")";
//
//			if ( LayerManager.get_instance().has_same_agent_name( name, elements[ 1])) {
//				++index;
//				continue;
//			}
//
//			if ( LayerManager.get_instance().has_same_agent_name( name, elements[ 1], role)) {
//				++index;
//				continue;
//			}
//
//			break;
//		}
//
//		String[] texts = _value.split( " ");
//		if ( null == texts || 2 != texts.length)
//			return;
//
//		_value = ( texts[ 0] + " " + name);
//		for ( int i = 1; i < elements.length; ++i)
//			_value += ( "=" + elements[ i]);
	}

	/**
	 * 
	 */
	public void print() {
		System.out.println( _kind + " : " + _type + " : " + _value);
	}
}
