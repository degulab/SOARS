/*
 * Created on 2005/10/28
 */
package soars.common.utility.tool.expression;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author kurata
 */
public class Expression {

	/**
	 * 
	 */
	public String[] _value = new String[ 3];

	/**
	 * @param function1
	 * @param function2
	 * @param expression
	 * 
	 */
	public Expression(String function1, String function2, String expression) {
		super();
		_value[ 0] = function1;
		_value[ 1] = function2;
		_value[ 2] = expression;
	}

	/**
	 * @param value
	 */
	public Expression(String[] value) {
		_value = value;
	}

	/**
	 * @param expression
	 */
	public Expression(Expression expression) {
		super();
		_value[ 0] = expression._value[ 0];
		_value[ 1] = expression._value[ 1];
		_value[ 2] = expression._value[ 2];
	}

	/**
	 * 
	 */
	public void cleanup() {
		for ( int i = 0; i < _value.length; ++i)
			_value[ i] = "";
	}

	/**
	 * @return
	 */
	public Integer get_argument_count() {
		if ( _value[ 1].equals( ""))
			return ( Integer.valueOf( 0));

		String[] arguments = _value[ 1].split( ",");
		if ( null == arguments || 0 == arguments.length)
			return null;

		return ( Integer.valueOf( arguments.length));
	}

	/**
	 * @return
	 */
	public String get_function() {
		return ( _value[ 0] + "(" + _value[ 1] + ")");
	}

	/**
	 * @return
	 */
	public String[] get_arguments() {
		if ( _value[ 1].equals( ""))
			return null;

		String[] arguments = _value[ 1].split( ",");
		if ( null == arguments || 0 == arguments.length)
			return null;

		return arguments;
	}

	/**
	 * @param arguments
	 * @param functions
	 * @param argumentCountMap
	 * @param constants
	 * @return
	 */
	public String replace(Vector arguments, Vector functions, HashMap argumentCountMap, Vector constants) {
		if ( _value[ 2].equals( ""))
			return null;

		if ( _value[ 1].equals( "")) {
			 if ( !arguments.isEmpty())
			 	return null;

			 return _value[ 2];
		}

		String[] words = _value[ 1].split( ",");
		if ( null == words || 0 == words.length)
			return null;

		if ( words.length != arguments.size())
			return null;

		HashMap variable_map = new HashMap();
		for ( int i = 0; i < words.length; ++i)
			variable_map.put( words[ i], ( String)arguments.get( i));

		Vector variables = new Vector();
		for ( int i = 0; i < words.length; ++i)
			variables.add( words[ i]);

		ExpressionManipulator expressionManipulator = new ExpressionManipulator();
		if ( !expressionManipulator.is_correct( _value[ 2],
			functions, argumentCountMap, constants, variables))
			return null;

		String result = expressionManipulator.get( variable_map);
		if ( result.equals( ""))
			return null;

		return result;
	}

	/**
	 * @param expressionManager
	 * @param originalFunctionName
	 * @param newFunctionName
	 * @return
	 */
	public boolean update_expression(ExpressionManager expressionManager, String originalFunctionName, String newFunctionName) {
		String[] arguments = get_arguments();
		if ( null == arguments)
			return true;

		Vector variables = new Vector( Arrays.asList( arguments));

		if ( !variables.contains( newFunctionName))
			return true;

		String new_variable_name;
		int index = 1;
		while ( true) {
			new_variable_name = ( newFunctionName + index);
			if ( !variables.contains( new_variable_name))
				break;

			++index;
		}

		HashMap variableMap = new HashMap();
		for ( int i = 0; i < arguments.length; ++i)
			variableMap.put( arguments[ i],
				( ( arguments[ i].equals( newFunctionName)) ? new_variable_name : arguments[ i]));

		String expression = expressionManager.get( _value[ 2], variables, variableMap);
		if ( null == expression)
			return false;

		String value = "";
		for ( int i = 0; i < variables.size(); ++i) {
			String variable = ( String)variables.get( i);
			if ( variable.equals( newFunctionName))
				variable = new_variable_name;

			value += ( ( ( 0 == i) ? "" : ",") + variable);
		}

		_value[ 1] = value;

		_value[ 2] = expression;

		return true;
	}

	/**
	 * @param expressionManager
	 * @param originalFunctionName
	 * @param newFunctionName
	 * @return
	 */
	public boolean update_function(ExpressionManager expressionManager, String originalFunctionName, String newFunctionName) {
		String[] arguments = get_arguments();
		Vector variables = ( ( null == arguments)
			? new Vector()
			: new Vector( Arrays.asList( arguments)));

		String expression = expressionManager.get( _value[ 2], variables,
			new String[] { originalFunctionName, newFunctionName});
		if ( null == expression)
			return false;

		_value[ 2] = expression;

		return true;
	}
}
