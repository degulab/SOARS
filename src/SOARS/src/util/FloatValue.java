package util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.StringTokenizer;

import role.Role;

import env.Environment;
import env.EquippedObject;

/**
 * The FloatValue class represents float variable.
 * @author H. Tanuma / SOARS project
 */
public class FloatValue extends MutableNumber implements Comparable<Object>, Ownable, Cloneable, Askable<Role> {

	private static final long serialVersionUID = -5532066407554974250L;
	protected EquippedObject owner = null;
	protected float value = 0;

	/**
	 * Set number value.
	 * @param value number value
	 */
	public void setNumber(Number value) {
		this.value = value.floatValue();
	}
	/**
	 * Set float value.
	 * @param value float value
	 */
	public void setFloat(float value) {
		this.value = value;
	}
	/**
	 * Get number value.
	 * @return number value
	 */
	public Number numberValue() {
		return new Float(floatValue());
	}
	/**
	 * Get double value.
	 * @return double value
	 */
	public double doubleValue() {
		return floatValue();
	}
	/**
	 * Get float value.
	 * @return float value
	 */
	public float floatValue() {
		return value;
	}
	/**
	 * Get int value.
	 * @return int value
	 */
	public int intValue() {
		return (int) floatValue();
	}
	/**
	 * Get long value.
	 * @return long value
	 */
	public long longValue() {
		return (long) floatValue();
	}
	/**
	 * Compare to other value.
	 * @param o other value
	 * @return result of comparation
	 */
	public int compareTo(Object o) {
		return Float.compare(value, ((FloatValue) o).value);
	}
	/**
	 * Check equality of values.
	 * @param o other value
	 * @return true if two values agree
	 */
	public boolean equals(Object o) {
		return o instanceof FloatValue ? compareTo(o) == 0 : false;
	}
	/**
	 * Get string expression of the value.
	 * @return string expression
	 */
	public String toString() {
		return Float.toString(value);
	}
	/**
	 * Set owner of the variable.
	 * @param owner of the variable
	 */
	public void setOwner(Object owner) {
		if (this.owner == null || owner == null) {
			this.owner = (EquippedObject) owner;
		}
	}
	/**
	 * Create clone of the variable.
	 * @return clone of the variable
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	/**
	 * Evaluate assignment formula.
	 * @param caller role context of caller
	 * @param message assignment formula
	 * @return result of inequality
	 * @throws Exception
	 */
	public boolean ask(Role caller, Object message) throws Exception {
		String formula = message.toString();
		if (formula.charAt(0) == '=') {
			return floatValue() == new Parser(formula.substring(1), owner).sum(null);
		}
		if (formula.charAt(0) == '<') {
			return floatValue() <= new Parser(formula.substring(1), owner).sum(null);
		}
		if (formula.charAt(0) == '>') {
			return floatValue() >= new Parser(formula.substring(1), owner).sum(null);
		}
		value = new Parser(formula, owner).sum(null);
		return true;
	}

	static class Parser {

		String formula;
		EquippedObject owner;
		StringTokenizer tokens;
		String token = null;

		Parser(String formula, EquippedObject owner) {
			this.formula = formula;
			this.owner = owner;
			tokens = new StringTokenizer(formula, "+-*/%()", true);
		}
		void error() {
			String message = "Illegal Formula \"" + formula + '\"';
			if (token != null) {
				message += " at \'" + token + '\'';
				while (tokens.hasMoreTokens()) {
					message += tokens.nextToken();
				}
			}
			throw new RuntimeException(message);
		}
		String nextToken() {
			if (token == null && tokens.hasMoreTokens()) {
				token = tokens.nextToken().trim();
				if (token.equals("")) {
					token = null;
					nextToken();
				}
			}
			return token;
		}
		float sum(String check) throws Exception {
			float value;
			if (nextToken().equals("-")) {
				token = null;
				value = -term();
			}
			else {
				value = term();
			}
			while (nextToken() != null) {
				if (token.equals("+")) {
					token = null;
					value += term();
				}
				else if (token.equals("-")) {
					token = null;
					value -= term();
				}
				else if (token.equals(check)) {
					token = null;
					return value;
				}
				else {
					error();
				}
			}
			if (check != null) {
				error();
			}
			return value;
		}
		float term() throws Exception {
			float value = element();
			while (nextToken() != null) {
				if (token.equals("*")) {
					token = null;
					value *= element();
				}
				else if (token.equals("/")) {
					token = null;
					value /= element();
				}
				else if (token.equals("%")) {
					token = null;
					value %= element();
				}
				else {
					break;
				}
			}
			return value;
		}
		float element() throws Exception {
			if (nextToken() == null) {
				error();
			}
			if (token.equals("(")) {
				token = null;
				return sum(")");
			}
			String name = token;
			Class<?> math = Math.class;
			token = null;
			if (nextToken() != null && token.equals("(")) {
				ArrayList<Double> params = new ArrayList<Double>();
				ArrayList<Class<?>> types = new ArrayList<Class<?>>();
				token = null;
				if (nextToken() == null) {
					error();
				}
				else if (token.equals(")")) {
					token = null;
					math = getClass();
				}
				else {
					params.add(new Double(sum(")")));
					types.add(float.class);
					while (nextToken() != null && token.equals("(")) {
						token = null;
						params.add(new Double(sum(")")));
						types.add(float.class);
					}
				}
				Method m = math.getMethod(name, (Class[]) types.toArray(new Class[types.size()]));
				return ((Number) m.invoke(null, params.toArray())).floatValue();
			}
			float value = 0;
			Object o = owner.getEquip(name);
			try {
				value = o instanceof Number ? ((Number) o).floatValue() : Float.parseFloat(name);
			}
			catch (NumberFormatException e) {
				token = token == null ? name : name + token;
				error();
			}
			return value;
		}
		public static double E() {
			return Math.E;
		}
		public static double PI() {
			return Math.PI;
		}
		public static float random() {
			return Environment.getCurrent().getRandom().nextFloat();
		}
	}
}
