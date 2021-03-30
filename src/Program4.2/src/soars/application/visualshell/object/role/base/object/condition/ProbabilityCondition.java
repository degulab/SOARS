/**
 * 
 */
package soars.application.visualshell.object.role.base.object.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
import soars.application.visualshell.file.exporter.java.object.RuleData;
import soars.application.visualshell.file.exporter.java.object.Variable;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;

/**
 * @author kurata
 *
 */
public class ProbabilityCondition extends Rule {

	/**
	 * 
	 */
	static public String _reserved_word = "askEquip ";

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public ProbabilityCondition(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_names()
	 */
	protected String[] get_used_spot_names() {
		return new String[] { get_used_spot_name()};
	}

	/**
	 * @return
	 */
	private String get_used_spot_name() {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return null;

		return CommonRuleManipulator.extract_spot_name2( elements[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	protected String[] get_used_spot_variable_names(Role role) {
		return new String[] { get_used_spot_variable_name()};
	}

	/**
	 * @return
	 */
	private String get_used_spot_variable_name() {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return null;

		return CommonRuleManipulator.get_spot_variable_name2( elements[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_probability_names()
	 */
	protected String[] get_used_probability_names() {
		return new String[] { get_used_probability_name()};
	}

	/**
	 * @return
	 */
	private String get_used_probability_name() {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return null;

		return elements[ 0];
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_number_object_names()
	 */
	protected String[] get_used_number_object_names() {
		return new String[] { get_number_object_name()};
	}

	/**
	 * @return
	 */
	private String get_number_object_name() {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return null;

		if ( 2 > elements.length || CommonTool.is_probability_correct( elements[ 1]))
			return null;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return null;

		return ( prefix + elements[ 1]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_spot_name_and_number(String new_name, String original_name, String head_name, Vector ranges, String new_head_name, Vector new_ranges) {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return false;

		String spot_name = get_used_spot_name();
		if ( null == spot_name)
			return false;

		elements[ 0] = CommonRuleManipulator.update_spot_name2( elements[ 0], new_name, original_name, head_name, ranges, new_head_name, new_ranges);
		if ( null == elements[ 0])
			return false;

		_value = ( ( _value.startsWith( "!") ? "!" : "")
			+ _reserved_word + elements[ 0] + "=");

		if ( 1 < elements.length)
			_value += elements[ 1];

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_spot_variable_name(String name, String new_name, String type, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return false;

		elements[ 0] = CommonRuleManipulator.update_spot_variable_name2( elements[ 0], name, new_name, type);
		if ( null == elements[ 0])
			return false;

		_value = ( ( _value.startsWith( "!") ? "!" : "")
			+ _reserved_word + elements[ 0] + "=");

		if ( 1 < elements.length)
			_value += elements[ 1];

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_probability_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_probability_name(String name, String new_name, String type, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return false;

		elements[ 0] = CommonRuleManipulator.update_object_name( elements[ 0], name, new_name, type);
		if ( null == elements[ 0])
			return false;

		_value = ( ( _value.startsWith( "!") ? "!" : "")
			+ _reserved_word + elements[ 0] + "=");

		if ( 1 < elements.length)
			_value += elements[ 1];

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_number_object_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_number_object_name(String name, String new_name, String type, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 > elements.length || elements[ 1].equals( ""))
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
			return false;

		elements[ 1] = new_name;

		_value = ( ( _value.startsWith( "!") ? "!" : "") + _reserved_word);
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	protected boolean can_paste(Role role, Layer drawObjects) {
		if ( !can_paste_probability_name( drawObjects))
			return false;

		if ( !can_paste_number_object_name( drawObjects))
			return false;

		return true;
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_probability_name(Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return false;

		return CommonRuleManipulator.can_paste_object( "probability", elements[ 0], drawObjects);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_number_object_name(Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return false;

		if ( 2 > elements.length || CommonTool.is_probability_correct( elements[ 1]))
			return true;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return false;

		return CommonRuleManipulator.can_paste_object( "number object", prefix + elements[ 1], drawObjects);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected String get_script(String value, Role role) {
		String denial = ( value.startsWith( "!") ? "!" : "");

		String method = CommonRuleManipulator.get_reserved_word( value);
		if ( null == method)
			return "";

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements)
			return "";

		String[] prefix_and_object = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String script = ( denial + prefix_and_object[ 0] + method + prefix_and_object[ 1] + "=");

		if ( 1 < elements.length)
			script += elements[ 1];

		return script;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_cell_text()
	 */
	public String get_cell_text() {
		return get_script( _value, null);
	}

	@Override
	public List<String> get_java_program(String value, RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		String operator = ( value.startsWith( "!") ? ">" : "<=");

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements)
			return get_unknown_codes();

		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		List<Variable> variables = new ArrayList<>();
		variables.add( new Variable( "probability", prefixAndObject[ 1]));
		if ( 2 <= elements.length && !CommonTool.is_number_correct( elements[ 1]))
			variables.add( new Variable( "number object", elements[ 1]));

		List<String> prefixes = get_prefix( prefixAndObject[ 0], variables, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
		if ( null == prefixes)
			return get_unknown_codes();

		String spotDBname = initialStage ? "spotManager.getSpotDB()" : "spotSet";
		String random = initialStage ? "rand" : "getRandom()";

		String code = "";

		if ( 2 > elements.length)
			code = random + ".nextDouble()" + operator + prefixes.get( 0) + ( prefixes.get( 0).equals( "") ? "" : ".") + "get" + prefixAndObject[ 1] + "(" + ( variables.get( 0)._spotSet ? spotDBname : "") + ")";
		else {
			if ( CommonTool.is_number_correct( elements[ 1]))
				code = elements[ 1] + operator + prefixes.get( 0) + ( prefixes.get( 0).equals( "") ? "" : ".") + "get" + prefixAndObject[ 1] + "()";
			else
				code = prefixes.get( 0) + ( prefixes.get( 0).equals( "") ? "" : ".") + "get" + prefixAndObject[ 1] + "(" + ( variables.get( 0)._spotSet ? spotDBname : "") + ")" + operator + prefixes.get( 0) + ( prefixes.get( 1).equals( "") ? "" : ".") + "get" + elements[ 1] + "(" + ( variables.get( 1)._spotSet ? spotDBname : "") + ")";
		}
		List<String> codes = new ArrayList<>();
		codes.add( code);
		return codes;
	}
}
