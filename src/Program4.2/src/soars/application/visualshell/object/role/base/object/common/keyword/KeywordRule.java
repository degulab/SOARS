/**
 * 
 */
package soars.application.visualshell.object.role.base.object.common.keyword;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
import soars.application.visualshell.file.exporter.java.object.RuleData;
import soars.application.visualshell.file.exporter.java.object.Variable;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.condition.KeywordCondition;

/**
 * @author kurata
 *
 */
public class KeywordRule extends Rule {

	public String[] __reservedWords = null;

	/**
	 * @param reservedWords
	 * @param kind
	 * @param type
	 * @param value
	 */
	public KeywordRule(String[] reservedWords, String kind, String type, String value) {
		super(kind, type, value);
		__reservedWords = reservedWords;
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
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_keyword_names()
	 */
	protected String[] get_used_keyword_names() {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return null;

		String[] keywordNames = new String[] { elements[ 0], null};

		if ( 1 < elements.length) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null != prefix && CommonRuleManipulator.is_object( "keyword", prefix + elements[ 1], LayerManager.get_instance()))
				keywordNames[ 1] = ( prefix + elements[ 1]);
		}

		return keywordNames;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector ranges, String newHeadName, Vector newRanges) {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return false;

		elements[ 0] = CommonRuleManipulator.update_spot_name2( elements[ 0], newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == elements[ 0])
			return false;

		_value = ( ( _value.startsWith( "!") ? "!" : "")
			+ __reservedWords[ 0] + elements[ 0] + "=");

		if ( 1 < elements.length)
			_value += elements[ 1];

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_spot_variable_name(String name, String newName, String type, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return false;

		elements[ 0] = CommonRuleManipulator.update_spot_variable_name2( elements[ 0], name, newName, type);
		if ( null == elements[ 0])
			return false;

		_value = ( ( _value.startsWith( "!") ? "!" : "")
			+ __reservedWords[ 0] + elements[ 0] + "=");

		if ( 1 < elements.length)
			_value += elements[ 1];

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_keyword_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_keyword_name(String name, String newName, String type, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return false;

		boolean result1 = false;
		String element = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
		if ( null != element) {
			elements[ 0] = element;
			result1 = true;
		}

		boolean result2 = false;
		if ( 1 < elements.length) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null != prefix && CommonRuleManipulator.correspond( prefix, elements[ 1], name, type)) {
				elements[ 1] = newName;
				result2 = true;
			}
		}

		if ( !result1 && !result2)
			return false;

		_value = ( ( _value.startsWith( "!") ? "!" : "")
			+ __reservedWords[ 0] + elements[ 0] + "=");

		if ( 1 < elements.length)
			_value += elements[ 1];

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	protected boolean can_paste(Role role, Layer drawObjects) {
		// TODO Auto-generated method stub
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return false;

		boolean result1 = CommonRuleManipulator.can_paste_object( "keyword", elements[ 0], drawObjects);

		boolean result2 = true;
		if ( !elements[ 1].startsWith( "\"")) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return false;
			
			result2 = CommonRuleManipulator.is_object( "keyword", prefix + elements[ 1], drawObjects);
		}

		return ( result1 && result2);
//		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
//		if ( null == elements)
//			return false;
//
//		return CommonRuleManipulator.can_paste_object( "keyword", elements[ 0], drawObjects);
//		// elements[ 1]があってもそれはキーワードとは限らない
//		// キーワードでなければ任意の文字列とみなされるので貼り付けてもエラーにはならない
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected String get_script(String value, Role role) {
		// TODO Auto-generated method stub
		String[] elements = CommonRuleManipulator.get_elements( value, 1);
		if ( null == elements)
			return "";

		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String script = ( ( value.startsWith( "!") ? "!" : "") + prefixAndObject[ 0]);

		String command = __reservedWords[ CommonRuleManipulator.is_object( "keyword", prefixAndObject[ 0] + elements[ 1]) ? 1 : 0];

		if ( 2 <= elements[ 1].length() && elements[ 1].startsWith( "\"") && elements[ 1].endsWith( "\""))
			elements[ 1] = elements[ 1].substring( 1, elements[ 1].length() - 1);

		script += ( command + prefixAndObject[ 1] + "=" + elements[ 1]);

		return script;
//		String[] elements = CommonRuleManipulator.get_elements( value, 1);
//		if ( null == elements)
//			return "";
//
//		String[] prefix_and_object = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);
//
//		String script = ( ( value.startsWith( "!") ? "!" : "") + prefix_and_object[ 0]);
//
//		if ( 2 > elements.length)
//			script += ( __reserved_words[ 0] + prefix_and_object[ 1] + "=");
//		else
//			script += ( __reserved_words[ CommonRuleManipulator.is_object( "keyword", prefix_and_object[ 0] + elements[ 1]) ? 1 : 0]
//				+ prefix_and_object[ 1] + "=" + elements[ 1]);
//
//		return script;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_cell_text()
	 */
	public String get_cell_text() {
		// TODO Auto-generated method stub
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return "";

		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String script = ( ( _value.startsWith( "!") ? "!" : "") + prefixAndObject[ 0]);

		script += ( __reservedWords[ CommonRuleManipulator.is_object( "keyword", prefixAndObject[ 0] + elements[ 1]) ? 1 : 0]
			+ prefixAndObject[ 1] + "=" + elements[ 1]);

		return script;
//		return get_script( _value, null);
	}

	/**
	 * @param variableName1
	 * @param variableName2 
	 * @return
	 */
	protected List<Variable> get_variables(String variableName1, String variableName2) {
		// TODO Auto-generated method stub
		List<Variable> variables = new ArrayList<>();
		variables.add( new Variable( "keyword", variableName1));
		if ( !variableName2.startsWith( "\"") && !variableName2.endsWith( "\""))
			variables.add( new Variable( "keyword", variableName2));
		return variables;
	}

	@Override
	protected void get_variable(List<Variable> variables, String value, Role role) {
		// TODO Auto-generated method stub
		System.out.println( "[" + ( role instanceof AgentRole ? "AgentRole" : "SpotRole") + " : " + role._name + "]");

		System.out.println( "[" + _type + "]");

		String[] elements = CommonRuleManipulator.get_elements( value, 1);
		if ( null == elements)
			return;

		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String parameter = "";
		for ( int i = 0; i < prefixAndObject.length; ++i)
			parameter += ( ( 0 == i) ? "" : ":") + prefixAndObject[ i];

		System.out.println( parameter);

		for ( int i = 1; i < elements.length; ++i) {
			prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ i]);	// 旧いデータへの配慮
			parameter = "";
			for ( int j = 0; j < prefixAndObject.length; ++j)
				parameter += ( ( 0 == j) ? "" : ":") + prefixAndObject[ j];
			System.out.println( parameter);
		}

		System.out.println( get_script( value, role));
		System.out.println( "");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#transform_keyword_conditions_and_commands(soars.application.visualshell.object.role.base.Role)
	 */
	public boolean transform_keyword_conditions_and_commands(Role role) {
		// TODO Auto-generated method stub
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return false;

		if ( 2 > elements.length) {
			_value += "\"\"";
			return true;
		} else {
			if ( elements[ 1].startsWith( "$")
				|| ( 2 <= elements[ 1].length() && elements[ 1].startsWith( "\"") && elements[ 1].endsWith( "\"")))
				return true;
			else {
				String[] values = CommonRuleManipulator.get_spot_and_object( elements[ 0]);
				if ( null == values)
					return false;

				String spot = CommonRuleManipulator.get_semantic_prefix( values);

				if ( ( spot.equals( "") && LayerManager.get_instance().is_agent_object_name( "keyword", elements[ 1]))
					|| ( spot.equals( "<>") && LayerManager.get_instance().is_spot_object_name( "keyword", elements[ 1]))
					|| LayerManager.get_instance().is_spot_object_name( "keyword", spot, elements[ 1]))
					return true;

				elements[ 1] = ( "\"" + elements[ 1] + "\"");
			}
		}

		_value = ( ( _value.startsWith( "!") ? "!" : "")
			+ __reservedWords[ 0] + elements[ 0] + "=" + elements[ 1]);

		return true;
	}

	@Override
	public List<String> get_java_program(String value, RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		String[] elements = CommonRuleManipulator.get_elements( value, 1);
		if ( null == elements)
			return get_unknown_codes();

		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		List<Variable> variables = get_variables( prefixAndObject[ 1], elements[ 1]);

		List<String> prefixes = get_prefix( prefixAndObject[ 0], variables, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
		if ( null == prefixes)
			return get_unknown_codes();

		List<String> codes = new ArrayList<>();

		String spotDBname = initialStage ? "spotManager.getSpotDB()" : "spotSet";

		if ( KeywordCondition.class.isInstance( this)) {
			String code = prefixes.get( 0) + ( prefixes.get( 0).equals( "") ? "" : ".") +  "get" + prefixAndObject[ 1] + "(" + ( variables.get( 0)._spotSet ? spotDBname : "") + ").equals(";

			if ( elements[ 1].startsWith( "\"") && elements[ 1].endsWith( "\""))
				code += elements[ 1] + ")";
			else
				code += prefixes.get( 1) + ( prefixes.get( 1).equals( "") ? "" : ".") + "get" + elements[ 1] + "(" + ( variables.get( 1)._spotSet ? spotDBname : "") + "))";

			codes.add( ( value.startsWith( "!") ? "!" : "") + code);
		} else {
			String code = prefixes.get( 0) + ( prefixes.get( 0).equals( "") ? "" : ".") + "set" + prefixAndObject[ 1] + "(" + ( variables.get( 0)._spotSet ? spotDBname + "," : "");

			if ( elements[ 1].startsWith( "\"") && elements[ 1].endsWith( "\""))
				code += elements[ 1] + ")";
			else
				code += prefixes.get( 1) + ( prefixes.get( 1).equals( "") ? "" : ".") + "get" + elements[ 1] + "(" + ( variables.get( 1)._spotSet ? spotDBname : "") +  "))";

			codes.add( code);
		}

		return codes;
	}
}
