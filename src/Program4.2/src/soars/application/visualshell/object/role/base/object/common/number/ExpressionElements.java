/*
 * Created on 2005/10/31
 */
package soars.application.visualshell.object.role.base.object.common.number;

import java.util.Vector;

/**
 * @author kurata
 */
public class ExpressionElements {

	/**
	 * 
	 */
	public String _function = "";

	/**
	 * 
	 */
	public Vector _variables = new Vector();

	/**
	 * 
	 */
	public ExpressionElements() {
		super();
	}

	/**
	 * @param number_object
	 * @param function
	 * @param variables
	 */
	public ExpressionElements(String number_object, String function, Vector variables) {
		super();
		_function = function;
		_variables = variables;
	}

	/**
	 * @param number_object
	 * @param operator
	 * @param function
	 * @param variables
	 */
	public ExpressionElements(String number_object, String operator, String function, Vector variables) {
		super();
		_function = function;
		_variables = variables;
	}

	/**
	 * @param function
	 * @param variables
	 */
	public ExpressionElements(String function, Vector variables) {
		super();
		_function = function;
		_variables = variables;
	}

	/**
	 * @return
	 */
	public String get_function() {
		String[] words = _function.split( "\\(");
		if ( null == words || 2 > words.length)
			return null;

		return words[ 0];
	}

	/**
	 * @param original_function
	 * @param new_function
	 * @return
	 */
	public boolean update_function(String original_function, String new_function) {
		String[] words = _function.split( "\\(");
		if ( null == words || 2 > words.length)
			return false;

		if ( !words[ 0].equals( original_function))
			return true;

		_function = ( new_function + "(" + words[ 1]);
		return true;
	}
}
