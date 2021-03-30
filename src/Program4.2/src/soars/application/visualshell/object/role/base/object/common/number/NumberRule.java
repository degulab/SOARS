/**
 * 
 */
package soars.application.visualshell.object.role.base.object.common.number;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.file.exporter.java.object.ExpressionData;
import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
import soars.application.visualshell.file.exporter.java.object.RuleData;
import soars.application.visualshell.file.exporter.java.object.Variable;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.common.utility.tool.expression.Expression;
import soars.common.utility.tool.expression.ExpressionManipulator;

/**
 * @author kurata
 *
 */
public class NumberRule extends Rule {

	/**
	 * 
	 */
	static private HashMap _operator_script_map = null;

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
		_operator_script_map = new HashMap();
		_operator_script_map.put( "",   new String[] { "",  "=" });
		_operator_script_map.put( ">",  new String[] { "!", "=<"});
		_operator_script_map.put( ">=", new String[] { "",  "=>"});
		_operator_script_map.put( "==", new String[] { "",  "=="});
		_operator_script_map.put( "!=", new String[] { "!", "=="});
		_operator_script_map.put( "<=", new String[] { "",  "=<"});
		_operator_script_map.put( "<",  new String[] { "!", "=>"});
	}

	/**
	 * @param value
	 * @return
	 */
	public static ExpressionElements get2(String value) {
		String[] elements = value.split( " ");
		if ( null == elements || 1 > elements.length)
			return null;

		ExpressionElements expressionElements = new ExpressionElements();

		expressionElements._function = elements[ 0];

		for ( int i = 1; i < elements.length; ++i) {
			String[] words = elements[ i].split( "=");
			if ( null == words || 2 != words.length)
				return null;

			expressionElements._variables.add( words);
		}

		return expressionElements;
	}

	/**
	 * @param function
	 * @param variables
	 * @return
	 */
	public static String get(String function, Vector variables) {
		String value = function;

		for ( int i = 0; i < variables.size(); ++i) {
			value += " ";
			String[] words = ( String[])variables.get( i);
			value += ( words[ 0] + "=" + words[ 1]);
		}

		return value;
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public NumberRule(String kind, String type, String value) {
		super(kind, type, value);
	}

	/**
	 * @param expressionElements
	 * @return
	 */
	public String get(ExpressionElements expressionElements) {
		String value = expressionElements._function;

		for ( int i = 0; i < expressionElements._variables.size(); ++i) {
			value += " ";
			String[] words = ( String[])expressionElements._variables.get( i);
			value += ( words[ 0] + "=" + words[ 1]);
		}

		return value;
	}

	/**
	 * @param expressionElements
	 * @param visualShellExpressionManager
	 * @return
	 */
	protected ExpressionElements update_expression(ExpressionElements expressionElements, VisualShellExpressionManager visualShellExpressionManager) {
		String function = expressionElements.get_function();
		if ( null == function || function.equals( ""))
			return null;

		Expression expression = ( Expression)visualShellExpressionManager.get( function);
		if ( null == expression)
			return null;

		if ( expressionElements._function.equals( expression.get_function()))
			return null;

		expressionElements._function = expression.get_function();

		String[] arguments = expression.get_arguments();
		if ( null == arguments)
			expressionElements._variables.clear();
		else {
			if ( arguments.length < expressionElements._variables.size())
				expressionElements._variables.setSize( arguments.length);

			for ( int i = 0; i < arguments.length; ++i) {
				if ( expressionElements._variables.size() > i) {
					String[] words = ( String[])expressionElements._variables.get( i);
					words[ 0] = arguments[ i];
				} else {
					String[] words = new String[ 2];
					words[ 0] = arguments[ i];
					words[ 1] = "0.0";
					expressionElements._variables.add( words);
				}
			}
		}

		return expressionElements;
	}

	/**
	 * @param value
	 * @param length
	 * @param number_object_names
	 */
	protected void get_number_object_names(String value, int length, Vector number_object_names) {
		String[] elements = value.split( " ");
		if ( null == elements || length > elements.length)
			return;

		number_object_names.add( elements[ 0]);
	}

	/**
	 * @param value
	 * @param length
	 * @param indices
	 * @param prefix
	 * @param number_object_names
	 */
	protected void get_number_object_names(String value, int length, int[] indices, String prefix, Vector number_object_names) {
		String[] elements = value.split( " ");
		if ( null == elements || length > elements.length)
			return;

		for ( int i = 0; i < indices.length; ++i)
			number_object_names.add( prefix + elements[ indices[ i]]);
	}

	/**
	 * @param expressionElements
	 * @param prefix
	 * @param number_object_names
	 */
	protected void get_number_object_names(ExpressionElements expressionElements, String prefix, Vector number_object_names) {
		for ( int i = 0; i < expressionElements._variables.size(); ++i) {
			String[] words = ( String[])expressionElements._variables.get( i);
			if ( CommonTool.is_number_correct( words[ 1]))
				continue;

			number_object_names.add( prefix + words[ 1]);
		}
	}

	/**
	 * @param prefix
	 * @param value
	 * @param kind
	 * @param name
	 * @param new_name
	 * @param type
	 * @return
	 */
	protected String update_number_object_name2(String prefix, String value, int kind, String name, String new_name, String type) {
		switch ( kind) {
			case 1:
				return update_number_object_name21( prefix, value, name, new_name, type);
			case 2:
				return update_number_object_name22( prefix, value, name, new_name, type);
		}
		return null;
	}

	/**
	 * @param prefix
	 * @param value
	 * @param name
	 * @param new_name
	 * @param type
	 * @return
	 */
	protected String update_number_object_name21(String prefix, String value, String name, String new_name, String type) {
		if ( value.startsWith( "<"))
			return CommonRuleManipulator.update_object_name( value, name, new_name, type);
		else {
			if ( !CommonRuleManipulator.correspond( prefix, value, name, type))
				return null;

			return new_name;
		}
	}

	/**
	 * @param prefix
	 * @param value
	 * @param name
	 * @param new_name
	 * @param type
	 * @return
	 */
	protected String update_number_object_name22(String prefix, String value, String name, String new_name, String type) {
		ExpressionElements expressionElements = get2( value);
		if ( null == expressionElements)
			return null;

		String expression = expressionElements._function;
		boolean result = false;
		for ( int i = 0; i < expressionElements._variables.size(); ++i) {
			String[] words = ( String[])expressionElements._variables.get( i);
			expression += ( " " + words[ 0] + "=");
			if ( CommonTool.is_number_correct( words[ 1])) {
				expression += words[ 1];
				continue;
			}

			if ( words[ 1].startsWith( "<")) {
				String word = CommonRuleManipulator.update_object_name( words[ 1], name, new_name, type);
				if ( null == word)
					expression += words[ 1];
				else {
					expression += new_name;
					result = true;
				}
			} else {
				if ( !CommonRuleManipulator.correspond( prefix, words[ 1], name, type))
					expression += words[ 1];
				else {
					expression += new_name;
					result = true;
				}
			}
		}

		if ( !result)
			return null;

		return expression;
	}

	/**
	 * @param value
	 * @param length
	 * @param drawObjects
	 * @return
	 */
	protected boolean can_paste_number_object_names(String value, int length, Layer drawObjects) {
		String[] elements = value.split( " ");
		if ( null == elements || length > elements.length)
			return false;

		return CommonRuleManipulator.can_paste_object( "number object", elements[ 0], drawObjects);
	}

	/**
	 * @param value
	 * @param length
	 * @param indices
	 * @param prefix
	 * @param drawObjects
	 * @return
	 */
	protected boolean can_paste_number_object_names(String value, int length, int[] indices, String prefix, Layer drawObjects) {
		String[] elements = value.split( " ");
		if ( null == elements || length > elements.length)
			return false;

		for ( int i = 0; i < indices.length; ++i) {
			if ( !CommonRuleManipulator.can_paste_object( "number object", prefix + elements[ indices[ i]], drawObjects))
				return false;
		}

		return true;
	}

	/**
	 * @param expressionElements
	 * @param prefix
	 * @param drawObjects
	 * @return
	 */
	protected boolean can_paste_number_object_names(ExpressionElements expressionElements, String prefix, Layer drawObjects) {
		for ( int i = 0; i < expressionElements._variables.size(); ++i) {
			String[] words = ( String[])expressionElements._variables.get( i);
			if ( CommonTool.is_number_correct( words[ 1]))
				continue;

			if ( !CommonRuleManipulator.can_paste_object( "number object", prefix + words[ 1], drawObjects))
				return false;
		}

		return true;
	}

	/**
	 * @param value
	 * @return
	 */
	protected String get_value_script1(String value) {
		String[] elements = value.split( " ");
		if ( null == elements || ( 2 != elements.length && 3 != elements.length))
			return "";

		String[] prefix_and_object = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String[] operator_scripts = ( String[])_operator_script_map.get(
			2 == elements.length ? "" : elements[ 1]);
		if ( null == operator_scripts || 2 != operator_scripts.length)
			return "";

		String val = ( 2 == elements.length ? elements[ 1] : elements[ 2]);

		return ( operator_scripts[ 0] + prefix_and_object[ 0] + "askEquip " + prefix_and_object[ 1] + operator_scripts[ 1]
			+ ( val.startsWith( "-") ? "(" + val + ")" : val));
	}

	/**
	 * @param value
	 * @return
	 */
	protected String get_number_object_script1(String value) {
		String[] elements = value.split( " ");
		if ( null == elements || ( 2 != elements.length && 3 != elements.length))
			return "";

		String[] prefix_and_object = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String[] operator_scripts = ( String[])_operator_script_map.get(
			2 == elements.length ? "" : elements[ 1]);
		if ( null == operator_scripts || 2 != operator_scripts.length)
			return "";

		String number_object = get_number_object_script2(
			( 2 == elements.length ? elements[ 1] : elements[ 2]));

		return ( operator_scripts[ 0] + prefix_and_object[ 0] + "askEquip " + prefix_and_object[ 1] + operator_scripts[ 1] + number_object);
	}

	/**
	 * @param element
	 * @return
	 */
	protected String get_number_object_script2(String element) {
		String[] prefix_and_object = CommonRuleManipulator.get_prefix_and_object( element);
		return prefix_and_object[ 1];
	}

	/**
	 * @param expressionElements
	 * @param operator
	 * @param number_object
	 * @param bracket1
	 * @param bracket2
	 * @return
	 */
	protected String get_expression_script1(ExpressionElements expressionElements, String number_object, String operator, boolean bracket1, boolean bracket2) {
		String expression = get_expression_script2( expressionElements, bracket2);
		if ( expression.equals( ""))
			return "";

		if ( bracket1)
			expression = ( "(" + expression + ")");

		String prefix_spot_name = get_prefix_spot_name( number_object);
		if ( null == prefix_spot_name)
			return "";

		String[] words = ( String[])number_object.split( ">");
		if ( null == words)
			return "";

		String number_object_script; 
		if ( 1 == words.length)
			number_object_script = words[ 0];
		else if ( 2 == words.length)
			number_object_script = words[ 1];
		else
			return "";

		String[] operator_scripts = ( String[])_operator_script_map.get( operator);
		if ( null == operator_scripts || 2 != operator_scripts.length)
			return "";

		return ( operator_scripts[ 0] + prefix_spot_name + "askEquip " + number_object_script + operator_scripts[ 1] + expression);
	}

	/**
	 * @param expressionElements
	 * @param bracket
	 * @return
	 */
	protected String get_expression_script2(ExpressionElements expressionElements, boolean bracket) {
		String expression = VisualShellExpressionManager.get_instance().get_expression( expressionElements._function);
		if ( expression.equals( ""))
			return "";

		//System.out.println( expression);	//

		String expanded_expression = VisualShellExpressionManager.get_instance().expand( expression);
		if ( null == expanded_expression)
			return "";

		//System.out.println( expanded_expression);	//

		Vector variables = new Vector();
		for ( int i = 0; i < expressionElements._variables.size(); ++i) {
			String[] words = ( String[])expressionElements._variables.get( i);
			variables.add( words[ 0]);
		}

		ExpressionManipulator expressionManipulator = new ExpressionManipulator();
		if ( !expressionManipulator.parse(
			expanded_expression,
			VisualShellExpressionManager.get_instance().get_all_functions(),
			VisualShellExpressionManager._constants,
			variables))
			return "";

		//System.out.println( expressionManipulator.get());	//

		String script = VisualShellExpressionManager.get_instance().get( expanded_expression, variables, ",", ")(");
		if ( null == script)
			return "";

		//System.out.println( script);	//

		if ( !expressionManipulator.parse(
			script,
			VisualShellExpressionManager.get_instance().get_all_functions(),
			VisualShellExpressionManager._constants,
			variables))
			return "";

		//System.out.println( expressionManipulator.get());	//

		HashMap update_variable_map = new HashMap();
		update_variable_map.put( "PI", "PI()");
		update_variable_map.put( "E", "E()");
		for ( int i = 0; i < expressionElements._variables.size(); ++i) {
			String[] words = ( String[])expressionElements._variables.get( i);
			String[] texts = words[ 1].split( ">");
			if ( null == texts)
				return "";

			if ( 1 == texts.length)
				update_variable_map.put( words[ 0], texts[ 0]);
			else if ( 2 == texts.length)
				update_variable_map.put( words[ 0], texts[ 1]);
			else
				return "";
		}

		expressionManipulator.update_variable( update_variable_map, bracket);

		//System.out.println( expressionManipulator.get());	//
		//System.out.println( "");	//

		return expressionManipulator.get();
	}

	/**
	 * @param value
	 * @return
	 */
	private String get_prefix_spot_name(String value) {
		if ( !value.startsWith( "<"))
			return "";

		if ( value.startsWith( "<>"))
			return "<>";

		String[] elements = value.substring( "<".length()).split( ">");
		if ( 2 != elements.length)
			return null;

		return ( "<" + elements[ 0] + ">");
	}

	/**
	 * @param value
	 * @return
	 */
	protected String get_others_script1(String value) {
		String[] elements = value.split( " ");
		if ( null == elements || ( 2 != elements.length && 3 != elements.length))
			return "";

		String[] prefix_and_object = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String[] operator_scripts = ( String[])_operator_script_map.get(
			2 == elements.length ? "" : elements[ 1]);
		if ( null == operator_scripts || 2 != operator_scripts.length)
			return "";

		return ( operator_scripts[ 0] + prefix_and_object[ 0] + "askEquip " + prefix_and_object[ 1] + operator_scripts[ 1]
			+ ( 2 == elements.length ? elements[ 1] : elements[ 2]));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_cell_text()
	 */
	public String get_cell_text() {
		String[] elements = _value.split( ": ");
		return ( ( null == elements || 2 > elements.length) ? _value : elements[ 1]);
	}

	/**
	 * @param word
	 * @return
	 */
	protected String get_variableName(String word) {
		// TODO Auto-generated method stub
		if ( !word.startsWith( "__|") || !word.endsWith( "|__"))
			return null;

		return word.substring( 3, word.length() - 3);
	}

	/**
	 * @param variableNames
	 * @return
	 */
	protected List<Variable> get_variables(String[] variableNames) {
		// TODO Auto-generated method stub
		List<Variable> variables = new ArrayList<>();
		for ( String variableName:variableNames)
			variables.add( new Variable( "number object", variableName));
		return variables;
	}

	/**
	 * @param expressionElements
	 * @param numberObject
	 * @param operator
	 * @param bracket1
	 * @param bracket2
	 * @return
	 */
	protected String get_expression_script1_java_program(ExpressionElements expressionElements, String numberObject, String operator, boolean bracket1, boolean bracket2) {
		// TODO Auto-generated method stub
		String expression = get_expression_script2( expressionElements, bracket2);
		if ( expression.equals( ""))
			return null;

		if ( bracket1)
			expression = ( "(" + expression + ")");

		String prefixSpotName = get_prefix_spot_name( numberObject);
		if ( null == prefixSpotName)
			return null;

		String[] words = ( String[])numberObject.split( ">");
		if ( null == words)
			return null;

		String numberObjectScript; 
		if ( 1 == words.length)
			numberObjectScript = words[ 0];
		else if ( 2 == words.length)
			numberObjectScript = words[ 1];
		else
			return null;

		return ( prefixSpotName + "askEquip " + numberObjectScript + operator + expression);
	}

	/**
	 * @param value
	 * @param entityPrefix
	 * @param expressionData 
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
	protected String get_value_script1_for_java_program(String value, String entityPrefix, ExpressionData expressionData, RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		String[] elements = value.split( " ");
		if ( null == elements || ( 2 != elements.length && 3 != elements.length))
			return "";

		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String operator = ( 2 == elements.length) ? "=" : elements[ 1];
//		String[] operatorScripts = ( String[])_operator_script_map.get(
//			2 == elements.length ? "" : elements[ 1]);
//		if ( null == operatorScripts || 2 != operatorScripts.length)
//			return "";

		String val = ( 2 == elements.length ? elements[ 1] : elements[ 2]);

//		System.out.println( prefixAndObject[ 0] + ":" + prefixAndObject[ 1]);

		List<Variable> variables = get_variables( new String[] { prefixAndObject[ 1]});
		List<String> prefixes = get_prefix( entityPrefix, variables, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
		if ( null == prefixes)
			return "Unknown";

		expressionData._variables.addAll( variables);
		expressionData._prefixes.addAll( prefixes);
//		System.out.println( "prefix3 = " + prefix);

		// TODO valは常に即値？
		//return ( prefix + ".set" + prefixAndObject[ 1] + "(" + ( val.startsWith( "-") ? "(" + val + ")" : val));
//		return ( operatorScripts[ 0] + prefixAndObject[ 0] + "askEquip " + "__|" + prefixAndObject[ 1] + "|__" + operatorScripts[ 1]
//			+ ( val.startsWith( "-") ? "(" + val + ")" : val));
		if ( val.startsWith( "-"))
			val = "(" + val + ")";
		else {
			if ( !CommonTool.is_number_correct( val))
				val = "(" + val + ")";
		}
		return ( prefixAndObject[ 0] + "askEquip " + "__|" + prefixAndObject[ 1] + "|__" + operator + val);
//		return ( prefixAndObject[ 0] + "askEquip " + "__|" + prefixAndObject[ 1] + "|__" + operator + ( val.startsWith( "-") ? "(" + val + ")" : val));
	}

	/**
	 * @param value
	 * @param entityPrefix
	 * @param expressionData 
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
	protected String get_number_object_script1_for_java_program(String value, String entityPrefix, ExpressionData expressionData, RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		String[] elements = value.split( " ");
		if ( null == elements || ( 2 != elements.length && 3 != elements.length))
			return "";

		String[] prefixAndObject1 = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String operator = ( 2 == elements.length) ? "=" : elements[ 1];
//		String[] operatorScripts = ( String[])_operator_script_map.get(
//			2 == elements.length ? "" : elements[ 1]);
//		if ( null == operatorScripts || 2 != operatorScripts.length)
//			return "";

		//String numberObject = get_number_object_script2( ( 2 == elements.length) ? elements[ 1] : elements[ 2]);
		String[] prefixAndObject2 = CommonRuleManipulator.get_prefix_and_object( ( 2 == elements.length) ? elements[ 1] : elements[ 2]);

//		System.out.println( prefixAndObject1[ 0] + ":" + prefixAndObject1[ 1]);

		List<Variable> variables = get_variables( new String[] { prefixAndObject1[ 1], prefixAndObject2[ 1]});
		List<String> prefixes = get_prefix( entityPrefix, variables, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
		if ( null == prefixes)
			return "Unknown";

		expressionData._variables.addAll( variables);
		expressionData._prefixes.addAll( prefixes);
//		System.out.println( "prefix4 = " + prefix);
//		for ( String p:prefix) {
//			if ( !expressionData.contains( p))
//				expressionData.add( p);
//		}

//		prefix = get_prefix( entityPrefix, get_variables( new String[] { prefixAndObject1[ 1], prefixAndObject2[ 1]}), ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role);
//		if ( null == prefix)
//			return "Unknown";
//
////		System.out.println( "prefix5 = " + prefix);
//		for ( String p:prefix) {
//			if ( !prefixes.contains( p))
//				prefixes.add( p);
//		}

		//return ( operator_scripts[ 0] + prefix_and_object[ 0] + "askEquip " + "__|" + prefix_and_object[ 1] + "|__" + operator_scripts[ 1] + "__|" + numberObject + "|__");
//		return ( operatorScripts[ 0] + prefixAndObject1[ 0] + "askEquip " + "__|" + prefixAndObject1[ 1] + "|__" + operatorScripts[ 1] + "__|" + prefixAndObject2[ 1] + "|__");
		return ( prefixAndObject1[ 0] + "askEquip " + "__|" + prefixAndObject1[ 1] + "|__" + operator + "__|" + prefixAndObject2[ 1] + "|__");
	}

	/**
	 * @param element
	 * @param entityPrefix
	 * @param expressionData 
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
	protected String get_number_object_script2_for_java_program(String element, String entityPrefix, ExpressionData expressionData, RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( element);

		List<Variable> variables = get_variables( new String[] { prefixAndObject[ 1]});
		List<String> prefixes = get_prefix( entityPrefix, variables, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
		if ( null == prefixes)
			return "Unknown";

		expressionData._variables.addAll( variables);
		expressionData._prefixes.addAll( prefixes);
//		System.out.println( "prefix6 = " + prefix);

//		System.out.println( prefixAndObject[ 0] + ":" + prefixAndObject[ 1]);
		return ( "__|" + prefixAndObject[ 1] + "|__");
	}

	/**
	 * @param value
	 * @param entityPrefix
	 * @param expressionData 
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
	protected String get_others_script1(String value, String entityPrefix, ExpressionData expressionData, RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		String[] elements = value.split( " ");
		if ( null == elements || ( 2 != elements.length && 3 != elements.length))
			return "Unknown";

		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String operator = ( 2 == elements.length) ? "" : elements[ 1];
//		String[] operatorScripts = ( String[])_operator_script_map.get(
//			2 == elements.length ? "" : elements[ 1]);
//		if ( null == operatorScripts || 2 != operatorScripts.length)
//			return "Unknown";

		List<Variable> variables = get_variables( new String[] { prefixAndObject[ 1]});
		List<String> prefixes = get_prefix( entityPrefix, variables, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
		if ( null == prefixes)
			return "Unknown";

		expressionData._variables.addAll( variables);
		expressionData._prefixes.addAll( prefixes);
//		System.out.println( "prefix7 = " + prefix);

//		return ( operatorScripts[ 0] + prefixAndObject[ 0] + "askEquip " + prefixAndObject[ 1] + operatorScripts[ 1]
//			+ ( 2 == elements.length ? elements[ 1] : elements[ 2]));
//		return ( prefixAndObject[ 0] + "askEquip " + "__|" + prefixAndObject[ 1] + "|__" + operator + ( 2 == elements.length ? elements[ 1] : elements[ 2]));

		String right = ( 2 == elements.length) ? elements[ 1] : elements[ 2];

		// TODO アドホックな対応
		if ( right.equals( "random()"))
			right = ( initialStage ? "rand" :  "getRandom()")  + ".nextDouble()";

		return ( prefixAndObject[ 0] + "askEquip " + "__|" + prefixAndObject[ 1] + "|__" + operator + right);
	}

	/**
	 * @param expression
	 * @return
	 */
	protected String replace(String expression) {
		// TODO Auto-generated method stub
		String[] constants = {
			"E",
			"PI",
		};

		for ( String constant:constants)
			expression = expression.replaceAll( constant + "\\(\\)", "Math." + constant);

		String[] functions = {
			"E",
			"PI",
			"abs",
			"acos",
			"addExact",
			"asin",
			"atan",
			"atan2",
			"cbrt",
			"ceil",
			"copySig",
			"cos",
			"cosh",
			"decrementExact",
			"exp",
			"expm1",
			"floor",
			"floorDiv",
			"floorMod",
			"getExponent",
			"hypot",
			"IEEEremainder",
			"incrementExact",
			"log",
			"log10",
			"log1p",
			"max",
			"min",
			"multiplyExact",
			"negateExact",
			"nextAfter",
			"nextDown",
			"nextUp",
			"pow",
			"random",
			"rint",
			"round",
			"scalb",
			"signum",
			"sin",
			"sinh",
			"sqrt",
			"subtractExact",
			"tan",
			"tanh",
			"toDegrees",
			"toIntExact",
			"toRadians",
			"ulp"
		};

		for ( String function:functions)
			expression = expression.replaceAll( function + "\\(", "Math." + function + "(");

		return expression;
	}

//	@Override
//	protected void get_variable(List<Variable> variables, String value, Role role) {
//		// TODO Auto-generated method stub
//		System.out.println( "[" + ( role instanceof AgentRole ? "AgentRole" : "SpotRole") + " : " + role._name + "]");
//
//		System.out.println( "[" + _type + "]");
//
//		String denial = ( _value.startsWith( "!") ? "!" : "");
//
//		String method = CommonRuleManipulator.get_reserved_word( value);
//		if ( null == method)
//			return;
//
//		System.out.println( "method=" + denial + method);
//
//		System.out.println( get_script_for_java_program( value, role));
//
////		System.out.println( get_cell_text());
////
////		String[] elements = value.split( ": ");
////		if ( ( null == elements) || 2 > elements.length)
////			System.out.println( value);
////		else {
////			elements = elements[ 1].split( " ");
////			for ( String element:elements)
////				System.out.println( element);
////		}
//
//		System.out.println( "");
////		String[] elements = value.split( ": ");
////		System.out.println( ( null == elements || 2 > elements.length) ? value : elements[ 1]);
//	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	protected String get_script_for_java_program(String value, Role role) {
		// TODO Auto-generated method stub
		return "";
	}

	/**
	 * @param value
	 * @return
	 */
	protected String get_value_script1_for_java_program(String value) {
		// TODO Auto-generated method stub
		String[] elements = value.split( " ");
		if ( null == elements || ( 2 != elements.length && 3 != elements.length))
			return "";

		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String[] operatorScripts = ( String[])_operator_script_map.get(
			2 == elements.length ? "" : elements[ 1]);
		if ( null == operatorScripts || 2 != operatorScripts.length)
			return "";

		String val = ( 2 == elements.length ? elements[ 1] : elements[ 2]);

		System.out.println( prefixAndObject[ 0] + ":" + prefixAndObject[ 1]);

		return ( operatorScripts[ 0] + prefixAndObject[ 0] + "askEquip " + "__|" + prefixAndObject[ 1] + "|__" + operatorScripts[ 1]
			+ ( val.startsWith( "-") ? "(" + val + ")" : val));
	}

	/**
	 * @param value
	 * @return
	 */
	protected String get_number_object_script1_for_java_program(String value) {
		// TODO Auto-generated method stub
		String[] elements = value.split( " ");
		if ( null == elements || ( 2 != elements.length && 3 != elements.length))
			return "";

		String[] prefixAndObject1 = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String[] operatorScripts = ( String[])_operator_script_map.get(
			2 == elements.length ? "" : elements[ 1]);
		if ( null == operatorScripts || 2 != operatorScripts.length)
			return "";

		//String numberObject = get_number_object_script2( ( 2 == elements.length) ? elements[ 1] : elements[ 2]);
		String[] prefixAndObject2 = CommonRuleManipulator.get_prefix_and_object( ( 2 == elements.length) ? elements[ 1] : elements[ 2]);

		System.out.println( prefixAndObject1[ 0] + ":" + prefixAndObject1[ 1]);
		System.out.println( prefixAndObject2[ 0] + ":" + prefixAndObject2[ 1]);

		//return ( operator_scripts[ 0] + prefix_and_object[ 0] + "askEquip " + "__|" + prefix_and_object[ 1] + "|__" + operator_scripts[ 1] + "__|" + numberObject + "|__");
		return ( operatorScripts[ 0] + prefixAndObject1[ 0] + "askEquip " + "__|" + prefixAndObject1[ 1] + "|__" + operatorScripts[ 1] + "__|" + prefixAndObject2[ 1] + "|__");
	}

	/**
	 * @param element
	 * @return
	 */
	protected String get_number_object_script2_for_java_program(String element) {
		// TODO Auto-generated method stub
		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( element);
		System.out.println( prefixAndObject[ 0] + ":" + prefixAndObject[ 1]);
		return ( "|__" + prefixAndObject[ 1] + "__|");
	}
}
