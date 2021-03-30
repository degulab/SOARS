/*
 * Created on 2005/11/05
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
public class ExpressionManipulator {

	/**
	 * 
	 */
	private Vector _stack = new Vector();

	/**
	 * 
	 */
	public ExpressionManipulator() {
		super();
	}

	/**
	 * 
	 */
	public void cleanup() {
		for ( int i = 0; i < _stack.size(); ++i) {
			Vector stack = ( Vector)_stack.get( i);
			stack.clear();
		}
		_stack.clear();

		_stack.add( new Vector());
	}

	/**
	 * @param word
	 * @return
	 */
	private boolean is_number(String word) {
		try {
			double d = Double.parseDouble( word);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param expression
	 * @param functions
	 * @param constants
	 * @param variables
	 * @return
	 */
	public boolean parse(String expression, Vector functions, Vector constants, Vector variables) {
		cleanup();

		String word = "";

		int rank = 0;

		ExpressionElement expressionElement = null;

		String previous = "";

		for ( int i = 0; i < expression.length(); ++i) {
			char c = expression.charAt( i);
			switch ( c) {
				case ' ':
					continue;
				case '(':
					if ( !word.equals( "") && !functions.contains( word))
						return false;

					if ( expression.length() - 1 == i)
						return false;

					if ( ')' == expression.charAt( i + 1)) {
						if ( 0 > rank || _stack.size() <= rank)
							return false;

						( ( Vector)_stack.get( rank)).add( new ExpressionElement( word));
						( ( Vector)_stack.get( rank)).add( new ExpressionElement( "("));
						( ( Vector)_stack.get( rank)).add( new ExpressionElement( ")"));
						previous = ")";
						word = "";
						++i;
						continue;
					} else if ( '+' == expression.charAt( i + 1)
						|| '*' == expression.charAt( i + 1)
						//|| '/' == expression.charAt( i + 1)) {	// TODO 2015.9.20
						|| '/' == expression.charAt( i + 1)				// TODO 2015.9.20
						|| '%' == expression.charAt( i + 1)) {		// TODO 2015.9.20
						return false;
					}

					expressionElement = null;
					if ( !word.equals( "")) {
						expressionElement = new ExpressionElement( word);
						word = "";
					} else
						expressionElement = new ExpressionElement( "(");

					if ( 0 > rank || _stack.size() <= rank)
						return false;

					( ( Vector)_stack.get( rank)).add( expressionElement);

					++rank;
					while ( _stack.size() <= rank)
						_stack.add( new Vector());

					if ( null != expressionElement) {
						if ( 0 > rank || _stack.size() <= rank)
							return false;

						expressionElement._nextDown = ( ( Vector)_stack.get( rank)).size();
					}

					break;
				case ')':
//					if ( functions.contains( word)
//						|| previous.equals( "+")
					if ( previous.equals( "+")
						|| previous.equals( "-")
						|| previous.equals( "*")
						//|| previous.equals( "/"))	// TODO 2015.9.20
						|| previous.equals( "/")		// TODO 2015.9.20
						|| previous.equals( "%"))		// TODO 2015.9.20
						return false;

					expressionElement = null;
					if ( !word.equals( "")) {
						if ( !variables.contains( word) && !constants.contains( word) && !is_number( word))
							return false;

						expressionElement = new ExpressionElement( word, variables.contains( word), constants.contains( word));
						if ( 0 > rank || _stack.size() <= rank)
							return false;

						( ( Vector)_stack.get( rank)).add( expressionElement);
						word = "";
					} else {
						if ( 0 > rank || _stack.size() <= rank)
							return false;

						Vector stack = ( Vector)_stack.get( rank);
						if ( stack.isEmpty())
							return false;

						expressionElement = ( ExpressionElement)stack.lastElement();
						if ( expressionElement._word.equals( ")")) {
							if ( 0 > rank || _stack.size() <= rank)
								return false;

							expressionElement._nextUp = ( ( Vector)_stack.get( rank)).size();
						} else
						 	return false;
					}

					--rank;
					if ( 0 > rank)
						return false;

					if ( null != expressionElement) {
						if ( 0 > rank || _stack.size() <= rank)
							return false;

						expressionElement._nextUp = ( ( Vector)_stack.get( rank)).size();
					}

					if ( 0 > rank || _stack.size() <= rank)
						return false;

					( ( Vector)_stack.get( rank)).add( new ExpressionElement( ")"));

					break;
				case '-':
					if ( expression.length() - 1 == i)
						return false;

//					if ( functions.contains( word))
//						return false;

					if ( !word.equals( "")) {
//						if ( !constants.contains( word)
//							&& !variables.contains( word)) {
//							if ( !is_number( word))
//								return false;
//						}
						if ( !constants.contains( word)
							&& !variables.contains( word)
							&& !is_number( word))
							return false;

						expressionElement = new ExpressionElement( word, variables.contains( word), constants.contains( word));
						if ( 0 > rank || _stack.size() <= rank)
							return false;

						( ( Vector)_stack.get( rank)).add( expressionElement);
						word = "";
					} else {
						if ( !previous.equals( "(")
							&& !previous.equals( ")")
							&& !previous.equals( ""))
							return false;
					}

					expressionElement = new ExpressionElement( String.valueOf( c));
					if ( 0 > rank || _stack.size() <= rank)
						return false;

					( ( Vector)_stack.get( rank)).add( expressionElement);
					break;
				case '+':
				case '*':
				case '/':
				case '%':	// TODO 2015.9.20
				case ',':
					if ( expression.length() - 1 == i)
						return false;

//					if ( functions.contains( word))
//						return false;

					if ( !word.equals( "")) {
//						if ( !constants.contains( word)
//							&& !variables.contains( word)) {
//							if ( !is_number( word))
//								return false;
//						}
						if ( !constants.contains( word)
							&& !variables.contains( word)
							&& !is_number( word))
							return false;

						expressionElement = new ExpressionElement( word, variables.contains( word), constants.contains( word));
						if ( 0 > rank || _stack.size() <= rank)
							return false;

						( ( Vector)_stack.get( rank)).add( expressionElement);
						word = "";
					} else {
						if ( !previous.equals( ")"))
							return false;
					}

					expressionElement = new ExpressionElement( String.valueOf( c));
					if ( 0 > rank || _stack.size() <= rank)
						return false;

					( ( Vector)_stack.get( rank)).add( expressionElement);
					break;
				default:
					if ( previous.equals( ")"))
						return false;

					word += String.valueOf( c);
					break;
			}

			previous = String.valueOf( c);
		}

		if ( !word.equals( "")) {
//			if ( !constants.contains( word)
//				&& !variables.contains( word)) {
//				if ( !is_number( word))
//					return false;
//			}
			if ( !constants.contains( word)
				&& !variables.contains( word)
				&& !is_number( word))
				return false;

			if ( 0 > rank || _stack.size() <= rank)
				return false;

			( ( Vector)_stack.get( rank)).add( new ExpressionElement( word, variables.contains( word), constants.contains( word)));
		}

		return ( 0 == rank);
	}

	/**
	 * @param expression
	 * @param functions
	 * @param argumentCountMap
	 * @param constants
	 * @param variables
	 * @return
	 */
	public boolean is_correct(String expression, Vector functions, HashMap argumentCountMap, Vector constants, Vector variables) {
		if ( !parse( expression, functions, constants, variables))
			return false;

		return is_correct( expression, functions, argumentCountMap);
	}

	/**
	 * @param expression
	 * @param functions
	 * @param argumentCountMap
	 * @return
	 */
	private boolean is_correct(String expression, Vector functions, HashMap argumentCountMap) {
		for ( int i = 0; i < expression.length(); ++i) {
			String function = is_function( expression.substring( i), functions);
			if ( null == function)
				continue;

			i += ( function + "(").length();
			if ( expression.length() <= i)
				return false;

			Integer argumentCountInteger = ( Integer)argumentCountMap.get( function);
			if ( null == argumentCountInteger)
				return false;
	
			int argumentCount = argumentCountInteger.intValue();
			//if ( 2 > argument_count)
			//	continue;
	
			int state = 0;
			Vector arguments = new Vector();
			String argument = "";
			while ( true) {
				char c = expression.charAt( i);
				if ( '(' == c) {
					argument += String.valueOf( c);
					++state;
				} else if ( ')' == c) {
					--state;
					if ( 0 > state) {
						if ( !argument.equals( ""))
							arguments.add( argument);
						break;
					}
					argument += String.valueOf( c);
				} else if ( ',' == c) { 
					if ( 0 == state) {
						if ( !argument.equals( "")) {
							arguments.add( argument);
							argument = "";
						}
					} else
						argument += String.valueOf( c);
				} else
					argument += String.valueOf( c);

				++i;
				if ( expression.length() <= i)
					return false;
			}

			if ( arguments.size() != argumentCount)
				return false;

			for ( int j = 0; j < arguments.size(); ++j) {
				if ( !is_correct( ( String)arguments.get( j), functions, argumentCountMap))
					return false;

				//System.out.println( ( String)arguments.get( j));
			}
		}

		return true;
	}

	/**
	 * @param expression
	 * @param functions
	 * @return
	 */
	private static String is_function(String expression, Vector functions) {
		for ( int i = 0; i < functions.size(); ++i) {
			String function = ( String)functions.get( i);
			if ( expression.startsWith( function + "("))
				return function;
		}
		return null;
	}

	/**
	 * @param expressionString
	 * @param allFunctions
	 * @param argumentCountMap
	 * @param functions
	 * @param constants
	 * @param treeMap
	 * @return
	 */
	public static String expand(String expressionString, Vector allFunctions, HashMap argumentCountMap, Vector functions, Vector constants, TreeMap treeMap) {
		String result = "";
		Expression expression = null;
		for ( int i = 0; i < expressionString.length(); ++i) {
			String function = is_function( expressionString.substring( i), functions);
			if ( null != function) {
				i += ( ( function + "(").length() - 1);
				if ( expressionString.length() <= i)
					return null;

				result += ( function + "(");
				continue;
			}

			expression = is_specific_function( expressionString.substring( i), treeMap);
			if ( null == expression)
				result += String.valueOf( expressionString.charAt( i));
			else {
				i += ( expression._value[ 0] + "(").length();
				if ( expressionString.length() <= i)
					return null;

				result += "(";

				int state = 0;
				Vector arguments = new Vector();
				String argument = "";
				while ( true) {
					char c = expressionString.charAt( i);
					if ( '(' == c) {
						argument += String.valueOf( c);
						++state;
					} else if ( ')' == c) {
						--state;
						if ( 0 > state) {
							if ( !argument.equals( ""))
								arguments.add( argument);
							break;
						}
						argument += String.valueOf( c);
					} else if ( ',' == c) { 
						if ( 0 == state) {
							if ( !argument.equals( "")) {
								arguments.add( argument);
								argument = "";
							}
						} else
							argument += String.valueOf( c);
					} else
						argument += String.valueOf( c);

					++i;
					if ( expressionString.length() <= i)
						return null;
				}

				String specificExpressionString = expression.replace(
					arguments, allFunctions, argumentCountMap, constants);
				if ( null == specificExpressionString)
					return null;

				specificExpressionString = expand(
					specificExpressionString, allFunctions, argumentCountMap, functions, constants, treeMap);
				if ( null == specificExpressionString)
					return null;

				result += ( specificExpressionString + ")");
			}
		}

		return result;
	}

	/**
	 * @param expressionString
	 * @param treeMap
	 * @return
	 */
	private static Expression is_specific_function(String expressionString, TreeMap treeMap) {
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();
			if ( expressionString.startsWith( expression._value[ 0] + "("))
				return expression;
		}
		return null;
	}

	/**
	 * @param updateVariableMap
	 * @param bracket
	 */
	public void update_variable(HashMap updateVariableMap, boolean bracket) {
		for ( int i = 0; i < _stack.size(); ++i) {
			Vector stack = ( Vector)_stack.get( i);
			for ( int j = 0; j < stack.size(); ++j) {
				ExpressionElement expressionElement = ( ExpressionElement)stack.get( j);
				if ( !expressionElement._variable && !expressionElement._constant)
					continue;

				String variable = ( String)updateVariableMap.get( expressionElement._word);
				if ( null == variable)
					continue;

				if ( bracket) {
					if ( is_number( variable))
						variable = ( "(" + variable + ")");
				} else {
					if ( variable.startsWith( "-"))
						variable = ( "(" + variable + ")");
					else {
						if ( !is_number( variable))
							variable = ( "(" + variable + ")");
					}
				} 

				expressionElement._word = variable;
			}
		}
	}

	/**
	 * @return
	 */
	public String get() {
		String expression = "";
		int rank = 0;
		int index = 0;
		if ( _stack.isEmpty())
			return "";

		Vector stack = ( Vector)_stack.get( 0);
		if ( null == stack || stack.isEmpty())
			return "";

		ExpressionElement expressionElement = ( ExpressionElement)stack.get( 0);
		while ( true) {
			expression += expressionElement._word;

			if ( -1 == expressionElement._nextDown
				&& -1 == expressionElement._nextUp) {
				++index;
			} else  {
				if ( -1 != expressionElement._nextDown) {
					index = expressionElement._nextDown;
					++rank;
					if ( !expressionElement._word.equals( "("))
						expression += "(";
				} else if ( -1 != expressionElement._nextUp) {
					index = expressionElement._nextUp;
					--rank;
				}
			}

			if ( 0 > rank || _stack.size() <= rank)
				return "";

			stack = ( Vector)_stack.get( rank);
			if ( 0 > index || stack.size() <= index)
				break;

			expressionElement = ( ExpressionElement)stack.get( index);
		}

		return expression;
	}

	/**
	 * @param variableMap
	 * @return
	 */
	public String get(HashMap variableMap) {
		String expression = "";
		int rank = 0;
		int index = 0;
		if ( _stack.isEmpty())
			return "";

		Vector stack = ( Vector)_stack.get( 0);
		if ( null == stack || stack.isEmpty())
			return "";

		String word;
		ExpressionElement expressionElement = ( ExpressionElement)stack.get( 0);
		while ( true) {
			if ( !expressionElement._variable)
				word = expressionElement._word;
			else {
				word = ( String)variableMap.get( expressionElement._word);
				if ( null == word)
					word = expressionElement._word;
			}

			expression += word;

			if ( -1 == expressionElement._nextDown
				&& -1 == expressionElement._nextUp) {
				++index;
			} else  {
				if ( -1 != expressionElement._nextDown) {
					index = expressionElement._nextDown;
					++rank;
					if ( !word.equals( "("))
						expression += "(";
				} else if ( -1 != expressionElement._nextUp) {
					index = expressionElement._nextUp;
					--rank;
				}
			}

			if ( 0 > rank || _stack.size() <= rank)
				return "";

			stack = ( Vector)_stack.get( rank);
			if ( 0 > index || stack.size() <= index)
				break;

			expressionElement = ( ExpressionElement)stack.get( index);
		}

		return expression;
	}

	/**
	 * @param functionNames
	 * @return
	 */
	public String get(String[] functionNames) {
		String expression = "";
		int rank = 0;
		int index = 0;
		if ( _stack.isEmpty())
			return "";

		Vector stack = ( Vector)_stack.get( 0);
		if ( null == stack || stack.isEmpty())
			return "";

		String word;
		ExpressionElement expressionElement = ( ExpressionElement)stack.get( 0);
		while ( true) {
			if ( expressionElement._variable || expressionElement._constant)
				word = expressionElement._word;
			else
				word = ( ( expressionElement._word.equals( functionNames[ 0]))
					? functionNames[ 1] : expressionElement._word);

			expression += word;

			if ( -1 == expressionElement._nextDown
				&& -1 == expressionElement._nextUp) {
				++index;
			} else  {
				if ( -1 != expressionElement._nextDown) {
					index = expressionElement._nextDown;
					++rank;
					if ( !word.equals( "("))
						expression += "(";
				} else if ( -1 != expressionElement._nextUp) {
					index = expressionElement._nextUp;
					--rank;
				}
			}

			if ( 0 > rank || _stack.size() <= rank)
				return "";

			stack = ( Vector)_stack.get( rank);
			if ( 0 > index || stack.size() <= index)
				break;

			expressionElement = ( ExpressionElement)stack.get( index);
		}

		return expression;
	}

	/**
	 * @param from
	 * @param to
	 * @return
	 */
	public String get(String from, String to) {
		String expression = "";
		int rank = 0;
		int index = 0;
		if ( _stack.isEmpty())
			return "";

		Vector stack = ( Vector)_stack.get( 0);
		if ( null == stack || stack.isEmpty())
			return "";

		ExpressionElement expressionElement = ( ExpressionElement)stack.get( 0);
		while ( true) {
			String word = ( ( expressionElement._word.equals( from)) ? to : expressionElement._word);

			expression += word;

			if ( -1 == expressionElement._nextDown
				&& -1 == expressionElement._nextUp) {
				++index;
			} else  {
				if ( -1 != expressionElement._nextDown) {
					index = expressionElement._nextDown;
					++rank;
					if ( !word.equals( "("))
						expression += "(";
				} else if ( -1 != expressionElement._nextUp) {
					index = expressionElement._nextUp;
					--rank;
				}
			}

			if ( 0 > rank || _stack.size() <= rank)
				return "";

			stack = ( Vector)_stack.get( rank);
			if ( 0 > index || stack.size() <= index)
				break;

			expressionElement = ( ExpressionElement)stack.get( index);
		}

		return expression;
	}

	/**
	 * 
	 */
	public void debug_print() {
		for ( int i = 0; i < _stack.size(); ++i) {
			Vector stack = ( Vector)_stack.get( i);
			String text = "";
			for ( int j = 0; j < stack.size(); ++j) {
				ExpressionElement expressionElement = ( ExpressionElement)stack.get( j);
				text += ( ( ( 0 == j) ? "" : ", ") + expressionElement._word + ":" + expressionElement._nextDown + ":" + expressionElement._nextUp);
			}

			System.out.println( text);
		}
	}
}
