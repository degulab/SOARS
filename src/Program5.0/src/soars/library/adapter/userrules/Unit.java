/**
 * 
 */
package soars.library.adapter.userrules;

import env.Agent;
import env.EquippedObject;
import env.Spot;
import soars.common.utility.tool.common.Tool;
import util.DoubleValue;
import util.IntValue;
import util.Invoker;

/**
 * @author kurata
 *
 */
public class Unit {

	/**
	 * 
	 */
	public String[] _elements = new String[] { "", "", ""};

	/**
	 * 
	 */
	public EquippedObject _entity = null;

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static Unit create(EquippedObject equippedObject, String argument) {
		Unit unit = new Unit();
		if ( !unit.set( equippedObject, argument))
			return null;

		return unit;
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static Object get(EquippedObject equippedObject, String argument) {
		Unit unit = create( equippedObject, argument);
		if ( null == unit)
			return null;

		return unit.get();
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static Object getInstance(EquippedObject equippedObject, String argument) {
		Unit unit = create( equippedObject, argument);
		if ( null == unit)
			return null;

		return unit.getInstance();
	}

	/**
	 * 
	 */
	public Unit() {
		super();
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public boolean set(EquippedObject equippedObject, String argument) {
		if ( !get( argument))
			return false;

		_entity = getEntity( equippedObject, _elements[ 0]);
		if ( null == _entity)
			return false;

		return true;
	}

	/**
	 * @param argument
	 * @return
	 */
	public boolean get(String argument) {
		String[] words = Tool.split( argument, '.');
		if ( 2 != words.length)
			return false;

		_elements[ 2] = words[ 1];

		words = Tool.split( words[ 0], ':');
		switch ( words.length) {
			case 1:
				_elements[ 0] = words[ 0];
				break;
			case 2:
				_elements[ 0] = words[ 0];
				_elements[ 1] = words[ 1];
				break;
			default:
				return false;
		}

		return true;
	}

	/**
	 * @param equippedObject
	 * @param name
	 * @return
	 */
	public static EquippedObject getEntity(EquippedObject equippedObject, String name) {
		if ( name.equals( "__self"))
			return equippedObject;
		else if ( name.equals( "__currentspot"))
			return ( equippedObject instanceof Agent) ? equippedObject.getSpot() : null;
		else {
			Agent agent = Agent.forName( name);
			return ( null != agent) ? agent : Spot.forName( name);
		}
	}

	/**
	 * @return
	 */
	public Object get() {
		Object element = getElement();
		return ( null != element) ? element : _elements[ 2];
	}

	/**
	 * @return
	 */
	public Object getInstance() {
		Object element = getElement();
		if ( null == element)
			return null;

		return ( element instanceof Invoker) ? ( ( Invoker)element).getInstance() : element;
		//return ( element instanceof Invoker) ? ( ( Invoker)element).getInstance() : null;
	}

	/**
	 * @param object
	 * @return
	 */
	public boolean set(Object object) {
		return setElement( object);
	}

	/**
	 * @param value
	 * @return
	 */
	public boolean set(String value) {
		return setElement( value);
	}

	/**
	 * @param value
	 * @return
	 */
	public boolean set(int value) {
		IntValue intValue = new IntValue();
		intValue.setInteger( value);
		return setElement( intValue);
	}

	/**
	 * @param value
	 * @return
	 */
	public boolean set(double value) {
		DoubleValue doubleValue = new DoubleValue();
		doubleValue.setDouble( value);
		return setElement( doubleValue);
	}

	/**
	 * @param object
	 * @return
	 */
	public boolean setInstance(Object object) {
		Object element = getElement();
		if ( null == element)
			return set( object);
			//return false;

		if ( !( element instanceof Invoker))
			return set( object);
			//return false;
			
		( ( Invoker)element).setInstance( object);
		return true;
	}

	/**
	 * @return
	 */
	private Object getElement() {
		EquippedObject equippedObject = getEntity();
		return ( null != equippedObject) ? equippedObject.getEquip( _elements[ 2]) : null;
	}

	/**
	 * @param object
	 * @return
	 */
	private boolean setElement(Object object) {
		EquippedObject equippedObject = getEntity();
		if ( null == equippedObject)
			return false;

		equippedObject.setEquip( _elements[ 2], object);
		return true;
	}

	/**
	 * @return
	 */
	private EquippedObject getEntity() {
		return ( _elements[ 1].equals( "")) ? _entity : _entity.getSpotVariable( _elements[ 1]);
	}
}
