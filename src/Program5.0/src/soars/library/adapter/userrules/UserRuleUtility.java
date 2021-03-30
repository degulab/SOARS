/**
 * 
 */
package soars.library.adapter.userrules;

import java.util.HashMap;
import java.util.Map;

import env.EquippedObject;
import time.Time;
import util.DoubleValue;
import util.IntValue;
import util.Invoker;

/**
 * @author kurata
 *
 */
public class UserRuleUtility {

	/**
	 * 
	 */
	static private Map<String, String>_hourMap = new HashMap<String, String>();

	/**
	 * 
	 */
	static private Map<String, String>_minuteMap = new HashMap<String, String>();

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
		for ( int i = 0; i < 10; ++i) {
			_hourMap.put( String.format( "%02d", i), String.format( "%d", i));
			_minuteMap.put( String.format( "%d", i), String.format( "%02d", i));
		}
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static String getString(EquippedObject equippedObject, String argument) {
		if ( argument.startsWith( "\"") && argument.endsWith( "\""))
			return argument.replaceAll( "\"", "");

		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return argument;

		return ( String)unit.get();
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static int getIntValue(EquippedObject equippedObject, String argument) {
		if ( argument.startsWith( "\"") && argument.endsWith( "\""))
			return Integer.valueOf( argument.replaceAll( "\"", ""));

		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return 0;

		return ( ( IntValue)unit.get()).intValue();
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static double getDoubleValue(EquippedObject equippedObject, String argument) {
		if ( argument.startsWith( "\"") && argument.endsWith( "\""))
			return Double.valueOf( argument.replaceAll( "\"", ""));

		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return 0.0;

		return ( ( DoubleValue)unit.get()).doubleValue();
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static String getTimeString(EquippedObject equippedObject, String argument) {
		// TODO Auto-generated method stub
		if ( argument.startsWith( "\"") && argument.endsWith( "\""))
			return argument.replaceAll( "\"", "");

		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return null;

		Object object = unit.get();
		return ( object instanceof Time) ? getTimeString( object.toString()) : ( String)object;
	}

	/**
	 * @param element
	 * @return
	 */
	private static String getTimeString(String element) {
		// TODO Auto-generated method stub
		String[] words = element.split( "/");
		if ( 1 == words.length)
			return getDailyTimeString( words[ 0]);
		else if ( 2 == words.length)
			return ( words[ 0] + "/" + getDailyTimeString( words[ 1]));
		else
			return element;
	}

	/**
	 * @param word
	 * @return
	 */
	static private String getDailyTimeString(String word) {
		// TODO Auto-generated method stub
		String[] words = word.split( ":");
		if ( 2 != words.length)
			return word;

		String hour = _hourMap.get( words[ 0]);
		String minute = _minuteMap.get( words[ 1]);
		return ( ( ( null == hour) ? words[ 0] : hour) + ":" + ( ( null == minute) ? words[ 1] : minute));
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static Object getClassVariable(EquippedObject equippedObject, String argument) {
		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return 0;

		Object object = unit.get();
		if ( null == object || !( object instanceof Invoker))
			return null;

		Invoker invoker = ( Invoker)object;

		return invoker.getInstance();
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static Object get(EquippedObject equippedObject, String argument) {
		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return null;

		return unit.get();
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @param object
	 */
	public static void set(EquippedObject equippedObject, String argument, Object object) {
		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return;

		unit.set( object);
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @param value
	 */
	public static void set(EquippedObject equippedObject, String argument, String value) {
		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return;

		unit.set( value);
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @param value
	 */
	public static void set(EquippedObject equippedObject, String argument, int value) {
		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return;

		unit.set( value);
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @param value
	 */
	public static void set(EquippedObject equippedObject, String argument, double value) {
		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return;

		unit.set( value);
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @param object
	 */
	public static void setInstance(EquippedObject equippedObject, String argument, Object object) {
		// TODO Auto-generated method stub
		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return;

		unit.setInstance( object);
	}
}
