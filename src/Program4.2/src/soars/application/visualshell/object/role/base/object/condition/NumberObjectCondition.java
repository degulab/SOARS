/**
 * 
 */
package soars.application.visualshell.object.role.base.object.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.file.exporter.java.object.ExpressionData;
import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
import soars.application.visualshell.file.exporter.java.object.RuleData;
import soars.application.visualshell.file.exporter.java.object.Variable;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.player.base.object.number.NumberObject;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.common.number.ExpressionElements;
import soars.application.visualshell.object.role.base.object.common.number.NumberRule;
import soars.application.visualshell.object.role.base.object.common.number.SubType;
import soars.common.utility.tool.expression.Expression;

/**
 * @author kurata
 *
 */
public class NumberObjectCondition extends NumberRule {

	/**
	 * 
	 */
	public static String[] _sub_types = {
		ResourceManager.get_instance().get( "rule.sub.type.condition.number.object.value.name"),
		ResourceManager.get_instance().get( "rule.sub.type.condition.number.object.number.object.name"),
		ResourceManager.get_instance().get( "rule.sub.type.condition.number.object.expression.name"),
		ResourceManager.get_instance().get( "rule.sub.type.condition.number.object.others.name")
	};

	/**
	 * @param elements
	 * @return
	 */
	public static ExpressionElements get1(String[] elements) {
		ExpressionElements expressionElements = new ExpressionElements();

		expressionElements._function = elements[ 2];

		for ( int i = 3; i < elements.length; ++i) {
			String[] words = elements[ i].split( "=");
			if ( null == words || 2 != words.length)
				return null;

			expressionElements._variables.add( words);
		}

		return expressionElements;
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public NumberObjectCondition(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_expressions()
	 */
	protected ExpressionElements[] get_used_expressions() {
		SubType subType = SubType.get( _value, _sub_types);
		if ( null == subType)
			return null;

		if ( 2 != subType._kind[ 0] && 2 != subType._kind[ 1])
			return null;

		String main_value = subType.get_value( _value);
		if ( null == main_value)
			return null;

		Vector results = new Vector();
		if ( subType._operator.equals( "")) {
			if ( 2 == subType._kind[ 0]) {
				ExpressionElements expressionElements = get1( main_value);
				if ( null != expressionElements)
					results.add( expressionElements);
			}
		} else {
			String[] values = main_value.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return null;

			if ( 2 == subType._kind[ 0]) {
				ExpressionElements expressionElements = get1( values[ 0]);
				if ( null != expressionElements)
					results.add( expressionElements);
			}

			if ( 2 == subType._kind[ 1]) {
				ExpressionElements expressionElements = get2( values[ 1]);
				if ( null != expressionElements)
					results.add( expressionElements);
			}
		}

		if ( results.isEmpty())
			return null;

		return ( ExpressionElements[])results.toArray( new ExpressionElements[ 0]);
	}

	/**
	 * @param value
	 * @return
	 */
	private ExpressionElements get1(String value) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 > elements.length)
			return null;

		return get1( elements);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_expression(soars.application.visualshell.object.expression.VisualShellExpressionManager)
	 */
	public boolean update_expression(VisualShellExpressionManager visualShellExpressionManager) {
		SubType subType = SubType.get( _value, _sub_types);
		if ( null == subType)
			return false;

		if ( 2 != subType._kind[ 0] && 2 != subType._kind[ 1])
			return false;

		String main_value = subType.get_value( _value);
		if ( null == main_value)
			return false;

		if ( subType._operator.equals( "")) {
			if ( 2 != subType._kind[ 0])
				return false;

			String new_value = update_expression1( main_value, visualShellExpressionManager);
			if ( null == new_value)
				return false;

			_value = ( subType.get( _sub_types) + new_value);
		} else {
			String[] values = main_value.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return false;

			String[] new_values = new String[] { null, null};

			if ( 2 == subType._kind[ 0])
				new_values[ 0] = update_expression1( values[ 0], visualShellExpressionManager);

			if ( 2 == subType._kind[ 1])
				new_values[ 1] = update_expression2( values[ 1], visualShellExpressionManager);

			_value = ( subType.get( _sub_types)
				+ ( ( null == new_values[ 0]) ? values[ 0] : new_values[ 0])
				+ " " + subType._operator + " "
				+ ( ( null == new_values[ 1]) ? values[ 1] : new_values[ 1]));
		}

		return true;
	}

	/**
	 * @param value
	 * @param visualShellExpressionManager
	 * @return
	 */
	private String update_expression1(String value, VisualShellExpressionManager visualShellExpressionManager) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 > elements.length)
			return null;

		ExpressionElements expressionElements = get1( elements);
		if ( null == expressionElements)
			return null;

		expressionElements = update_expression( expressionElements, visualShellExpressionManager);
		if ( null == expressionElements)
			return null;

		String new_value = get( expressionElements);
		if ( null == new_value)
			return null;

		return ( elements[ 0] + " " + elements[ 1] + " " + new_value);
	}

	/**
	 * @param value
	 * @param visualShellExpressionManager
	 * @return
	 */
	private String update_expression2(String value, VisualShellExpressionManager visualShellExpressionManager) {
		ExpressionElements expressionElements = get2( value);
		if ( null == expressionElements)
			return null;

		expressionElements = update_expression( expressionElements, visualShellExpressionManager);
		if ( null == expressionElements)
			return null;

		return get( expressionElements);
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
		SubType subType = SubType.get( _value, _sub_types);
		if ( null == subType)
			return null;

		String main_value = subType.get_value( _value);
		if ( null == main_value)
			return null;

		String[] elements = main_value.split( " ");
		if ( null == elements || 2 > elements.length)
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
		SubType subType = SubType.get( _value, _sub_types);
		if ( null == subType)
			return null;

		String main_value = subType.get_value( _value);
		if ( null == main_value)
			return null;

		String[] elements = main_value.split( " ");
		if ( null == elements || 2 > elements.length)
			return null;

		return CommonRuleManipulator.get_spot_variable_name2( elements[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_number_object_names()
	 */
	protected String[] get_used_number_object_names() {
		SubType subType = SubType.get( _value, _sub_types);
		if ( null == subType)
			return null;

		String main_value = subType.get_value( _value);
		if ( null == main_value)
			return null;

		String[] elements = main_value.split( " ");
		if ( null == elements || 2 > elements.length)
			return null;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return null;

		Vector number_object_names = new Vector();
		if ( subType._operator.equals( "")) {
			get_number_object_names1( main_value, subType._kind[ 0], prefix, number_object_names);
		} else {
			String[] values = main_value.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return null;

			get_number_object_names1( values[ 0], subType._kind[ 0], prefix, number_object_names);
			get_number_object_names2( values[ 1], subType._kind[ 1], prefix, number_object_names);
		}

		if ( number_object_names.isEmpty())
			return null;

		return ( String[])number_object_names.toArray( new String[ 0]);
	}

	/**
	 * @param value
	 * @param kind
	 * @param prefix
	 * @param number_object_names
	 */
	private void get_number_object_names1(String value, int kind, String prefix, Vector number_object_names) {
		switch ( kind) {
			case 0:
				get_number_object_names( value, 3, number_object_names);
				break;
			case 1:
				get_number_object_names( value, 3, number_object_names);
				get_number_object_names( value, 3, new int[] { 2}, prefix, number_object_names);
				break;
			case 2:
				String[] elements = value.split( " ");
				if ( null == elements || 3 > elements.length)
					return;

				number_object_names.add( elements[ 0]);

				ExpressionElements expressionElements = get1( elements);
				if ( null != expressionElements)
					get_number_object_names( expressionElements, prefix, number_object_names);

				break;
		}
	}

	/**
	 * @param value
	 * @param kind
	 * @param prefix
	 * @param number_object_names
	 */
	private void get_number_object_names2(String value, int kind, String prefix, Vector number_object_names) {
		switch ( kind) {
			case 1:
				number_object_names.add( prefix + value);
	
				break;
			case 2:
				ExpressionElements expressionElements = get2( value);
				if ( null != expressionElements)
					get_number_object_names( expressionElements, prefix, number_object_names);
	
				break;
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_spot_name_and_number(String new_name, String original_name, String head_name, Vector ranges, String new_head_name, Vector new_ranges) {
		SubType subType = SubType.get( _value, _sub_types);
		if ( null == subType)
			return false;

		String main_value = subType.get_value( _value);
		if ( null == main_value)
			return false;

		String[] elements = main_value.split( " ");
		if ( null == elements || 3 > elements.length)
			return false;

		elements[ 0] = CommonRuleManipulator.update_spot_name2( elements[ 0], new_name, original_name, head_name, ranges, new_head_name, new_ranges);
		if ( null == elements[ 0])
			return false;

		String new_value = "";
		for ( int i = 0; i < elements.length; ++i)
			new_value += ( ( ( 0 == i) ? "" : " ") + elements[ i]);

		_value = ( subType.get( _sub_types) + new_value);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_spot_variable_name(String name, String new_name, String type, Role role) {
		SubType subType = SubType.get( _value, _sub_types);
		if ( null == subType)
			return false;

		String main_value = subType.get_value( _value);
		if ( null == main_value)
			return false;

		String[] elements = main_value.split( " ");
		if ( null == elements || 3 > elements.length)
			return false;

		elements[ 0] = CommonRuleManipulator.update_spot_variable_name2( elements[ 0], name, new_name, type);
		if ( null == elements[ 0])
			return false;

		String new_value = "";
		for ( int i = 0; i < elements.length; ++i)
			new_value += ( ( ( 0 == i) ? "" : " ") + elements[ i]);

		_value = ( subType.get( _sub_types) + new_value);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_number_object_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_number_object_name(String name, String new_name, String type, Role role) {
		SubType subType = SubType.get( _value, _sub_types);
		if ( null == subType)
			return false;

		String main_value = subType.get_value( _value);
		if ( null == main_value)
			return false;

		String[] elements = main_value.split( " ");
		if ( null == elements || 2 > elements.length)
			return false;

		String new_value = null;

		if ( subType._operator.equals( "")) {
			new_value = update_number_object_name1( main_value, subType._kind[ 0], name, new_name, type);
		} else {
			String[] values = main_value.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return false;

			String value0 = update_number_object_name1( values[ 0], subType._kind[ 0], name, new_name, type);
			if ( null != value0)
				values[ 0] = value0;

			String prefix = CommonRuleManipulator.get_full_prefix( values[ 0]);
			if ( null == prefix)
				return false;

			String value1 = update_number_object_name2( prefix, values[ 1], subType._kind[ 1], name, new_name, type);
			if ( null != value1)
				values[ 1] = value1;

			if ( null == value0 && null == value1)
				return false;

			new_value = ( values[ 0] + " " + subType._operator + " " + values[ 1]);
		}

		if ( null == new_value)
			return false;

		_value = ( subType.get( _sub_types) + new_value);

		return true;
	}

	/**
	 * @param value
	 * @param kind
	 * @param name
	 * @param new_name
	 * @param type
	 * @return
	 */
	private String update_number_object_name1(String value, int kind, String name, String new_name, String type) {
		switch ( kind) {
			case 0:
				return update_number_object_name10( value, name, new_name, type);
			case 1:
				return update_number_object_name11( value, name, new_name, type);
			case 2:
				return update_number_object_name12( value, name, new_name, type);
		}
		return null;
	}

	/**
	 * @param value
	 * @param name
	 * @param new_name
	 * @param type
	 * @return
	 */
	private String update_number_object_name10(String value, String name, String new_name, String type) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 != elements.length)
			return null;

		elements[ 0] = CommonRuleManipulator.update_object_name( elements[ 0], name, new_name, type);
		if ( null == elements[ 0])
			return null;

		return ( elements[ 0] + " " + elements[ 1] + " " + elements[ 2]);
	}

	/**
	 * @param value
	 * @param name
	 * @param new_name
	 * @param type
	 * @return
	 */
	private String update_number_object_name11(String value, String name, String new_name, String type) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 != elements.length)
			return null;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return null;

		String element0 = CommonRuleManipulator.update_object_name( elements[ 0], name, new_name, type);
		if ( null != element0)
			elements[ 0] = element0;

		if ( elements[ 2].startsWith( "<")) {
			String element2 = CommonRuleManipulator.update_object_name( elements[ 2], name, new_name, type);
			if ( null != element2)
				elements[ 2] = element2;

			if ( null == element0 && null == element2)
				return null;

		} else {
			boolean result2 = CommonRuleManipulator.correspond( prefix, elements[ 2], name, type);
			if ( result2)
				elements[ 2] = new_name;

			if ( null == element0 && !result2)
				return null;

		}

		return ( elements[ 0] + " " + elements[ 1] + " " + elements[ 2]);
	}

	/**
	 * @param value
	 * @param name
	 * @param new_name
	 * @param type
	 * @return
	 */
	private String update_number_object_name12(String value, String name, String new_name, String type) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 > elements.length)
			return null;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return null;

		String element0 = CommonRuleManipulator.update_object_name( elements[ 0], name, new_name, type);
		if ( null != element0)
			elements[ 0] = element0;

		ExpressionElements expressionElements = get1( elements);
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

		if ( null == element0 && !result)
			return null;

		return ( elements[ 0] + " " + elements[ 1] + " " + expression);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	protected boolean can_paste(Role role, Layer drawObjects) {
		if ( !can_paste_spot_and_spot_variable_name( drawObjects))
			return false;

		if ( !can_paste_number_object_name( drawObjects))
			return false;

		return true;
	}
	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_spot_and_spot_variable_name(Layer drawObjects) {
		SubType subType = SubType.get( _value, _sub_types);
		if ( null == subType)
			return false;

		String main_value = subType.get_value( _value);
		if ( null == main_value)
			return false;

		String[] elements = main_value.split( " ");
		if ( null == elements || 2 > elements.length)
			return false;

		return CommonRuleManipulator.can_paste_spot_and_spot_variable_name2( elements[ 0], drawObjects);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_number_object_name(Layer drawObjects) {
		SubType subType = SubType.get( _value, _sub_types);
		if ( null == subType)
			return false;

		String main_value = subType.get_value( _value);
		if ( null == main_value)
			return false;

		String[] elements = main_value.split( " ");
		if ( null == elements || 2 > elements.length)
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return false;

		if ( subType._operator.equals( "")) {
			return can_paste_number_object_names1( main_value, subType._kind[ 0], prefix, drawObjects);
		} else {
			String[] values = main_value.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return false;

			return ( can_paste_number_object_names1( values[ 0], subType._kind[ 0], prefix, drawObjects)
				&& can_paste_number_object_names2( values[ 1], subType._kind[ 1], prefix, drawObjects));
		}
	}

	/**
	 * @param value
	 * @param kind
	 * @param prefix
	 * @param drawObjects
	 */
	private boolean can_paste_number_object_names1(String value, int kind, String prefix, Layer drawObjects) {
		switch ( kind) {
			case 0:
				return can_paste_number_object_names( value, 3, drawObjects);
			case 1:
				return ( can_paste_number_object_names( value, 3, drawObjects)
					&& can_paste_number_object_names( value, 3, new int[] { 2}, prefix, drawObjects));
			case 2:
				String[] elements = value.split( " ");
				if ( null == elements || 3 > elements.length)
					return false;

				if ( !CommonRuleManipulator.can_paste_object( "number object", elements[ 0], drawObjects))
					return false;

				ExpressionElements expressionElements = get1( elements);
				if ( null == expressionElements)
					break;

				return can_paste_number_object_names( expressionElements, prefix, drawObjects);
		}
		return true;
	}

	/**
	 * @param value
	 * @param kind
	 * @param prefix
	 * @param drawObjects
	 */
	private boolean can_paste_number_object_names2(String value, int kind, String prefix, Layer drawObjects) {
		switch ( kind) {
			case 1:
				return CommonRuleManipulator.can_paste_object( "number object", prefix + value, drawObjects);
			case 2:
				ExpressionElements expressionElements = get2( value);
				if ( null == expressionElements)
					break;

				return can_paste_number_object_names( expressionElements, prefix, drawObjects);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected String get_script(String value, Role role) {
		SubType subType = SubType.get( value, _sub_types);
		if ( null == subType)
			return "";

		String main_value = subType.get_value( value);
		if ( null == main_value)
			return "";

		if ( subType._operator.equals( "")) {
			String script = get_script1( main_value, subType._kind[ 0], false, true);
			if ( null == script)
				return "";

			return script;
		} else {
			String[] values = main_value.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return "";

			String[] scripts = new String[] { null, null};

			scripts[ 0] = get_script1( values[ 0], subType._kind[ 0], true, true);
			scripts[ 1] = get_script2( values[ 1], subType._kind[ 1], true);
			if ( null == scripts[ 0] || null == scripts[ 1])
				return "";

			return ( scripts[ 0] + subType._operator + scripts[ 1]);
		}
	}

	/**
	 * @param value
	 * @param kind
	 * @param bracket1
	 * @param bracket2
	 * @return
	 */
	private String get_script1(String value, int kind, boolean bracket1, boolean bracket2) {
		switch ( kind) {
			case 0:
				return get_value_script1( value);
			case 1:
				return get_number_object_script1( value);
			case 2:
				String[] elements = value.split( " ");
				if ( null == elements || 3 > elements.length)
					return null;

				ExpressionElements expressionElements = get1( elements);
				if ( null == expressionElements)
					return "";

				return get_expression_script1( expressionElements, elements[ 0], elements[ 1], bracket1, bracket2);
			case 3:
				return get_others_script1( value);
		}

		return "";
	}

	/**
	 * @param value
	 * @param kind
	 * @param bracket
	 * @return
	 */
	private String get_script2(String value, int kind, boolean bracket) {
		switch ( kind) {
			case 0:
				return ( value.startsWith( "-") ? "(" + value + ")" : value);
			case 1:
				return get_number_object_script2( value);
			case 2:
				ExpressionElements expressionElements = get2( value);
				if ( null == expressionElements)
					return "";

				String expression = get_expression_script2( expressionElements, bracket);
				if ( expression.equals( ""))
					return "";

				return ( "(" + expression + ")");
			case 3:
				return value;
		}

		return "";
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_expressions(java.util.TreeMap, java.util.TreeMap)
	 */
	public void get_used_expressions(TreeMap expression_map, TreeMap used_expression_map) {
		ExpressionElements[] expressionElements = get_used_expressions();
		if ( null == expressionElements)
			return;

		for ( int i = 0; i < expressionElements.length; ++i) {
			if ( null == expressionElements[ i])
				continue;

			if ( null != used_expression_map.get( expressionElements[ i].get_function()))
				continue;

			Expression expression = ( Expression)expression_map.get( expressionElements[ i].get_function());
			if ( null == expression)
				continue;

			used_expression_map.put( expressionElements[ i].get_function(), expression);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_function(java.lang.String, java.lang.String)
	 */
	public boolean update_function(String original_function_name, String new_function_name) {
		SubType subType = SubType.get( _value, _sub_types);
		if ( null == subType)
			return false;

		if ( 2 != subType._kind[ 0] && 2 != subType._kind[ 1])
			return false;

		String main_value = subType.get_value( _value);
		if ( null == main_value)
			return false;

		if ( subType._operator.equals( "")) {
			if ( 2 != subType._kind[ 0])
				return false;

			String new_value = update_function1( main_value, original_function_name, new_function_name);
			if ( null == new_value)
				return false;

			_value = ( subType.get( _sub_types) + new_value);
		} else {
			String[] values = main_value.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return false;

			String[] new_values = new String[] { null, null};

			if ( 2 == subType._kind[ 0])
				new_values[ 0] = update_function1( values[ 0], original_function_name, new_function_name);

			if ( 2 == subType._kind[ 1])
				new_values[ 1] = update_function2( values[ 1], original_function_name, new_function_name);

			_value = ( subType.get( _sub_types)
				+ ( ( null == new_values[ 0]) ? values[ 0] : new_values[ 0])
				+ " " + subType._operator + " "
				+ ( ( null == new_values[ 1]) ? values[ 1] : new_values[ 1]));
		}

		return true;
	}

	/**
	 * @param value
	 * @param original_function_name
	 * @param new_function_name
	 * @return
	 */
	private String update_function1(String value, String original_function_name, String new_function_name) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 > elements.length)
			return null;

		ExpressionElements expressionElements = get1( elements);
		if ( null == expressionElements)
			return null;

		if ( !expressionElements.update_function( original_function_name, new_function_name))
			return null;

		String new_value = get( expressionElements);
		if ( null == new_value)
			return null;

		return ( elements[ 0] + " " + elements[ 1] + " " + new_value);
	}

	/**
	 * @param value
	 * @param original_function_name
	 * @param new_function_name
	 * @return
	 */
	private String update_function2(String value, String original_function_name, String new_function_name) {
		ExpressionElements expressionElements = get2( value);
		if ( null == expressionElements)
			return null;

		if ( !expressionElements.update_function( original_function_name, new_function_name))
			return null;

		return get( expressionElements);
	}

	@Override
	public List<String> get_java_program(String value, RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		SubType subType = SubType.get( value, _sub_types);
		if ( null == subType)
			return get_unknown_codes();

		String mainValue = subType.get_value( value);
		if ( null == mainValue)
			return get_unknown_codes();

		//System.out.println( "condition : " + mainValue);

		String[] elements = mainValue.split( " ");
		if ( null == elements || 0 == elements.length || 0 == elements[ 0].length())
			return get_unknown_codes();

		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);
		String entityPrefix = prefixAndObject[ 0];
		//System.out.println( entityPrefix);

		String code = "";

		if ( subType._operator.equals( "")) {
			ExpressionData expressionData = new ExpressionData();
			String script = get_script1_for_java_program( mainValue, subType._kind[ 0], false, false, entityPrefix, expressionData, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
			if ( null == script)
				return get_unknown_codes();

			code = format( script, expressionData, initialStage);
			//return script;
		} else {
			ExpressionData expressionData = new ExpressionData();
			String[] values = mainValue.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return get_unknown_codes();

			String[] scripts = new String[] { null, null};

			scripts[ 0] = get_script1_for_java_program( values[ 0], subType._kind[ 0], true, false, entityPrefix, expressionData, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
			scripts[ 1] = get_script2_for_java_program( values[ 1], subType._kind[ 1], entityPrefix, expressionData, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
			if ( null == scripts[ 0] || null == scripts[ 1])
				return get_unknown_codes();

			code = format( scripts[ 0] + subType._operator + scripts[ 1], expressionData, initialStage);
			//return ( scripts[ 0] + subType._operator + scripts[ 1]);
		}

		List<String> codes = new ArrayList<>();
		codes.add( code);
		return codes;
	}

	/**
	 * @param script
	 * @param expressionData
	 * @param initialStage
	 * @return
	 */
	private String format(String script, ExpressionData expressionData, boolean initialStage) {
		// TODO Auto-generated method stub
		if ( expressionData._prefixes.isEmpty()
			|| expressionData._variables.isEmpty()
			|| ( expressionData._prefixes.size() != expressionData._variables.size()))
			return "Unknown";

		String expression = "";
		while ( true) {
			int start = script.indexOf( "__|");
			if ( 0 > start)
				break;

			expression += script.substring( 0, start);
			script = script.substring( start + 3);

			int end = script.indexOf( "|__");
			if ( 0 > end)
				return "Unknown";

			String spotDBname = initialStage ? "spotManager.getSpotDB()" : "spotSet";

			//expression += prefixes.get( 0) + ".get" + script.substring( 0, end) + "()";
			for ( int i = 0; i < expressionData._variables.size(); ++i) {
				if ( expressionData._variables.get( i)._name.equals( script.substring( 0, end))) {
					expression += expressionData._prefixes.get( i) + ( expressionData._prefixes.get( i).equals( "") ? "" : ".") +  "get" + script.substring( 0, end)
						+ "(" + ( expressionData._variables.get( i)._spotSet ? spotDBname : "") + ")";
					//script = script.substring( end + 3);
				}
			}
			script = script.substring( end + 3);
		}

		expression += script;

		return expression;
	}

	/**
	 * @param value
	 * @param kind
	 * @param bracket1
	 * @param bracket2
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
	private String get_script1_for_java_program(String value, int kind, boolean bracket1, boolean bracket2, String entityPrefix, ExpressionData expressionData, RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		switch ( kind) {
			case 0:
				return get_value_script1_for_java_program( value, entityPrefix, expressionData, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage).split( " ")[ 1];
			case 1:
				return get_number_object_script1_for_java_program( value, entityPrefix, expressionData, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage).split( " ")[ 1];
			case 2:
				String[] elements = value.split( " ");
				if ( null == elements || 3 > elements.length)
					return null;

				List<String> variableNames = new ArrayList<String>();

				String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);
//				System.out.println( prefixAndObject[ 0] + ":" + prefixAndObject[ 1]);
				elements[ 0] = ( prefixAndObject[ 0] + "__|" + prefixAndObject[ 1] + "|__");
				variableNames.add( prefixAndObject[ 1]);

				ExpressionElements expressionElements = get1( elements);
				if ( null == expressionElements)
					return "";

				for ( int i = 0; i < expressionElements._variables.size(); ++i) {
					String[] variables = ( String[])expressionElements._variables.get( i);
					prefixAndObject = CommonRuleManipulator.get_prefix_and_object( variables[ 1]);
//					System.out.println( prefixAndObject[ 0] + ":" + prefixAndObject[ 1]);
					// TODO 数値ならそのまま
					if ( prefixAndObject[ 0].equals( "") && ( null != NumberObject.is_correct( prefixAndObject[ 1], "real number")))
						variables[ 1] = prefixAndObject[ 1];
					else {
						variables[ 1] = ( prefixAndObject[ 0] + "__|" + prefixAndObject[ 1] + "|__");
						variableNames.add( prefixAndObject[ 1]);
					}
				}

				List<Variable> variables = get_variables( variableNames.toArray( new String[ variableNames.size()]));
				List<String> prefixes = get_prefix( entityPrefix, variables, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
				if ( null == prefixes)
					return "Unknown";

				expressionData._variables.addAll( variables);
				expressionData._prefixes.addAll( prefixes);
//				System.out.println( "prefix1 = " + prefix);
//				for ( String p:prefix) {
//					if ( !expressionData.contains( p))
//						expressionData.add( p);
//				}

				String expression = get_expression_script1( expressionElements, elements[ 0], elements[ 1], bracket1, bracket2).split( " ")[ 1];
				return replace( expression);
			case 3:
				return get_others_script1( value, entityPrefix, expressionData, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage).split( " ")[ 1];
		}

		return "";
	}

	/**
	 * @param value
	 * @param kind
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
	private String get_script2_for_java_program(String value, int kind, String entityPrefix, ExpressionData expressionData, RuleData ruleData, RoleDataSet roleDataSet,Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands,String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		switch ( kind) {
			case 0:
				return ( value.startsWith( "-") ? "(" + value + ")" : value);
			case 1:
				return get_number_object_script2_for_java_program( value);
			case 2:
				ExpressionElements expressionElements = get2( value);
				if ( null == expressionElements)
					return "";

				List<String> variableNames = new ArrayList<String>();

				for ( int i = 0; i < expressionElements._variables.size(); ++i) {
					String[] variables = ( String[])expressionElements._variables.get( i);
					String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( variables[ 1]);
//					System.out.println( prefixAndObject[ 0] + ":" + prefixAndObject[ 1]);
					// 数値ならそのまま
					if ( prefixAndObject[ 0].equals( "") && ( null != NumberObject.is_correct( prefixAndObject[ 1], "real number")))
						variables[ 1] = prefixAndObject[ 1];
					else {
						variables[ 1] = ( prefixAndObject[ 0] + "__|" + prefixAndObject[ 1] + "|__");
						variableNames.add( prefixAndObject[ 1]);
					}
					List<Variable> variableList = get_variables( variableNames.toArray( new String[ variableNames.size()]));
					List<String> prefixes = get_prefix( entityPrefix, variableList, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
					if ( null == prefixes)
						return "Unknown";

					expressionData._variables.addAll( variableList);
					expressionData._prefixes.addAll( prefixes);
//					System.out.println( "prefix2 = " + prefix);
//					for ( String p:prefix) {
//						if ( !expressionData.contains( p))
//							expressionData.add( p);
//					}
				}

				String expression = get_expression_script2( expressionElements, false);
				if ( expression.equals( ""))
					return "";

				expression = replace( expression);
				return ( "(" + expression + ")");
			case 3:
				return value;
		}

		return "";
	}

//	@Override
//	protected String get_script_for_java_program(String value, Role role) {
//		// TODO Auto-generated method stub
//		SubType subType = SubType.get( value, _sub_types);
//		if ( null == subType)
//			return "";
//
//		String mainValue = subType.get_value( value);
//		if ( null == mainValue)
//			return "";
//
//		if ( subType._operator.equals( "")) {
//			String script = get_script1_for_java_program( mainValue, subType._kind[ 0], false, false);
//			if ( null == script)
//				return "";
//
//			return script;
//		} else {
//			String[] values = mainValue.split(
//				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
//					+ subType._operator + " ");
//			if ( null == values || 2 != values.length)
//				return "";
//
//			String[] scripts = new String[] { null, null};
//
//			scripts[ 0] = get_script1_for_java_program( values[ 0], subType._kind[ 0], true, false);
//			scripts[ 1] = get_script2_for_java_program( values[ 1], subType._kind[ 1]);
//			if ( null == scripts[ 0] || null == scripts[ 1])
//				return "";
//
//			return ( scripts[ 0] + subType._operator + scripts[ 1]);
//		}
//	}
//
//	/**
//	 * @param value
//	 * @param kind
//	 * @param bracket1
//	 * @param bracket2
//	 * @return
//	 */
//	private String get_script1_for_java_program(String value, int kind, boolean bracket1, boolean blacket2) {
//		// TODO Auto-generated method stub
//		switch ( kind) {
//			case 0:
//				return get_value_script1_for_java_program( value).split( " ")[ 1];
//			case 1:
//				return get_number_object_script1_for_java_program( value).split( " ")[ 1];
//			case 2:
//				String[] elements = value.split( " ");
//				if ( null == elements || 3 > elements.length)
//					return null;
//
//				ExpressionElements expressionElements = get1( elements);
//				if ( null == expressionElements)
//					return "";
//				String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);
//				System.out.println( prefixAndObject[ 0] + ":" + prefixAndObject[ 1]);
//				elements[ 0] = ( prefixAndObject[ 0] + "__|" + prefixAndObject[ 1] + "|__");
//
//				for ( int i = 0; i < expressionElements._variables.size(); ++i) {
//					String[] variables = ( String[])expressionElements._variables.get( i);
//					prefixAndObject = CommonRuleManipulator.get_prefix_and_object( variables[ 1]);
//					System.out.println( prefixAndObject[ 0] + ":" + prefixAndObject[ 1]);
//					variables[ 1] = ( prefixAndObject[ 0] + "__|" + prefixAndObject[ 1] + "|__");
//				}
//
//				String expression = get_expression_script1( expressionElements, elements[ 0], elements[ 1], bracket1, blacket2).split( " ")[ 1];
//				return replace( expression);
//			case 3:
//				return get_others_script1( value);
//		}
//
//		return "";
//	}
//
//	/**
//	 * @param value
//	 * @param kind
//	 * @return
//	 */
//	private String get_script2_for_java_program(String value, int kind) {
//		// TODO Auto-generated method stub
//		switch ( kind) {
//			case 0:
//				return ( value.startsWith( "-") ? "(" + value + ")" : value);
//			case 1:
//				return get_number_object_script2_for_java_program( value);
//			case 2:
//				ExpressionElements expressionElements = get2( value);
//				if ( null == expressionElements)
//					return "";
//
//				for ( int i = 0; i < expressionElements._variables.size(); ++i) {
//					String[] variables = ( String[])expressionElements._variables.get( i);
//					String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( variables[ 1]);
//					System.out.println( prefixAndObject[ 0] + ":" + prefixAndObject[ 1]);
//					variables[ 1] = ( prefixAndObject[ 0] + "__|" + prefixAndObject[ 1] + "|__");
//				}
//
//				String expression = get_expression_script2( expressionElements, false);
//				if ( expression.equals( ""))
//					return "";
//
//				expression = replace( expression);
//				return ( "(" + expression + ")");
//			case 3:
//				return value;
//		}
//
//		return "";
//	}
}
