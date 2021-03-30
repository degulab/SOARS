/*
 * Created on 2005/10/28
 */
package soars.common.utility.tool.expression;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * @author kurata
 */
public class ExpressionManager extends TreeMap<String, Expression> {

	/**
	 * 
	 */
	static public Vector _functions = null;

	/**
	 * 
	 */
	static public HashMap _argumentCountMap = null;

	/**
	 * 
	 */
	static public Vector _constants = null;

	/**
	 * 
	 */
	public ExpressionManager() {
		super();
	}

	/**
	 * 
	 */
	protected void initialize() {
		if ( null != _functions && null != _constants)
			return;

		Object[][] function_words = new Object[][] {
			{ "abs", Integer.valueOf( 1)},
			{ "acos", Integer.valueOf( 1)},
			{ "asin", Integer.valueOf( 1)},
			{ "atan", Integer.valueOf( 1)},
			{ "atan2", Integer.valueOf( 2)},
			{ "ceil", Integer.valueOf( 1)},
			{ "cos", Integer.valueOf( 1)},
			{ "exp", Integer.valueOf( 1)},
			{ "floor", Integer.valueOf( 1)},
			{ "IEEEremainder", Integer.valueOf( 2)},
			{ "log", Integer.valueOf( 1)},
			{ "max", Integer.valueOf( 2)},
			{ "min", Integer.valueOf( 2)},
			{ "pow", Integer.valueOf( 2)},
			{ "random", Integer.valueOf( 0)},
			{ "rint", Integer.valueOf( 1)},
			{ "round", Integer.valueOf( 1)},
			{ "sin", Integer.valueOf( 1)},
			{ "sqrt", Integer.valueOf( 1)},
			{ "tan", Integer.valueOf( 1)},
			{ "toDegrees", Integer.valueOf( 1)},
			{ "toRadians", Integer.valueOf( 1)}
		};

		_functions = new Vector();
		_argumentCountMap = new HashMap();
		for ( int i = 0; i < function_words.length; ++i) {
			_functions.add( function_words[ i][ 0]);
			_argumentCountMap.put( function_words[ i][ 0], function_words[ i][ 1]);
		}


		String[] constant_words = new String[] {
			"E",
			"PI"
		};

		_constants = new Vector();
		for ( int i = 0; i < constant_words.length; ++i)
			_constants.add( constant_words[ i]);
	}

	/**
	 * 
	 */
	public void cleanup() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();
			expression.cleanup();
		}
		clear();
	}

	/**
	 * @param word
	 * @return
	 */
	public boolean is_reserved_word(String word) {
		return ( _functions.contains( word) || _constants.contains( word));
	}

	/**
	 * @return
	 */
	public HashMap get_variable_map() {
		HashMap variableMap = new HashMap();
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();

			String[] words = expression._value[ 1].split( ",");
			if ( null == words || 0 == words.length)
				continue;

			Vector variables = new Vector();
			for ( int i = 0; i < words.length; ++i) {
				if ( words[ i].equals( ""))
					continue;

				variables.add( new String[] { words[ i], "0.0"});
			}

			variableMap.put( expression.get_function(), variables);
		}

		return variableMap;
	}

	/**
	 * @return
	 */
	public String[] get_functions() {
		if ( isEmpty())
			return null;

		Vector functions = new Vector();

		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();
			functions.add( expression.get_function());
		}

		return ( String[])functions.toArray( new String[ 0]);
	}

	/**
	 * @param function
	 * @return
	 */
	public String get_expression(String function) {
		if ( null == function)
			return "";

		String[] words = function.split( "\\(");
		if ( null == words || 2 > words.length)
			return "";

		Expression expression = ( Expression)get( words[ 0]);
		if ( null == expression)
			return "";

		return expression._value[ 2];
	}

	/**
	 * @return
	 */
	public Vector get_all_functions() {
		Vector allFunctions = ( Vector)_functions.clone();
		allFunctions.addAll( new Vector( keySet()));
		return allFunctions;
	}

	/**
	 * @return
	 */
	public HashMap get_all_argument_count_map() {
		HashMap allArgumentCountMap = ( HashMap)_argumentCountMap.clone();
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();
			Integer argumentCountInteger = expression.get_argument_count();
			if ( null == argumentCountInteger)
				return null;

			allArgumentCountMap.put( expression._value[ 0], argumentCountInteger);
		}
		return allArgumentCountMap;
	}

	/**
	 * @param expression_string
	 * @param variables
	 * @param excluded_function
	 * @return
	 */
	public boolean is_correct(String expression_string, Vector variables, String excluded_function) {
		HashMap allArgumentCountMap = get_all_argument_count_map();
		if ( null == allArgumentCountMap)
			return false;

		Vector allFunctions = get_all_functions();

		if ( null != excluded_function) {
			int index = allFunctions.indexOf( excluded_function);
			if ( -1 < index)
				allFunctions.removeElementAt( index);
		}

		ExpressionManipulator expressionManipulator = new ExpressionManipulator();
		return expressionManipulator.is_correct( expression_string, allFunctions, allArgumentCountMap, _constants, variables);
	}

	/**
	 * @param expression_string
	 * @return
	 */
	public String expand(String expression_string) {
		HashMap allArgumentCountMap = get_all_argument_count_map();
		if ( null == allArgumentCountMap)
			return null;

		Vector allFunctions = get_all_functions();

		return ExpressionManipulator.expand( expression_string,
			allFunctions, allArgumentCountMap, _functions, _constants, this);
	}

	/**
	 * @param expression_string
	 * @param variables
	 * @param variable_map
	 * @return
	 */
	public String get(String expression_string, Vector variables, HashMap variable_map) {
		ExpressionManipulator expressionManipulator = new ExpressionManipulator();
		if ( !expressionManipulator.parse(
			expression_string,
			get_all_functions(),
			_constants,
			variables))
			return null;

		String result = expressionManipulator.get( variable_map);
		if ( result.equals( ""))
			return null;

//		System.out.println( expressionManipulator.parse(
//			expression_string,
//			get_all_functions(),
//			_constants,
//			variables));

		return result;
	}

	/**
	 * @param expression_string
	 * @param variables
	 * @param function_names
	 * @return
	 */
	public String get(String expression_string, Vector variables, String[] function_names) {
		ExpressionManipulator expressionManipulator = new ExpressionManipulator();
		if ( !expressionManipulator.parse(
			expression_string,
			get_all_functions(),
			_constants,
			variables))
			return null;

		String result = expressionManipulator.get( function_names);
		if ( result.equals( ""))
			return null;

//		System.out.println( expressionManipulator.parse(
//			expression_string,
//			get_all_functions(),
//			_constants,
//			variables));

		return result;
	}

	/**
	 * @param expression_string
	 * @param variables
	 * @param from
	 * @param to
	 * @return
	 */
	public String get(String expression_string, Vector variables, String from, String to) {
		ExpressionManipulator expressionManipulator = new ExpressionManipulator();
		if ( !expressionManipulator.parse(
			expression_string,
			get_all_functions(),
			_constants,
			variables))
			return null;

		String result = expressionManipulator.get( from, to);
		if ( result.equals( ""))
			return null;

//		System.out.println( expressionManipulator.parse(
//			expression_string,
//			get_all_functions(),
//			_constants,
//			variables));

		return result;
	}

//	/**
//	 * @return
//	 */
//	public Expression[] get_expressions() {
//		if ( isEmpty())
//			return null;
//
//		Vector expressions = new Vector( values());
//		return ( Expression[])expressions.toArray( new Expression[ 0]);
//	}
//
//	/**
//	 * @param function
//	 * @return
//	 */
//	public Vector get_variables(String function) {
//		String[] words = function.split( "\\(");
//		if ( null == words || 2 > words.length)
//			return null;
//
//		Expression expression = ( Expression)get( words[ 0]);
//		if ( null == expression)
//			return null;
//
//		words = expression._value[ 1].split( ",");
//		if ( null == words || 0 == words.length)
//			return null;
//
//		Vector variables = new Vector();
//		for ( int i = 0; i < words.length; ++i)
//			variables.add( new String[] { words[ i], "0.0"});
//
//		return variables;
//	}
}
