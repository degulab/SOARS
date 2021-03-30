package env;

import role.Role;
import role.RoleType;
import time.Time;
import util.DoubleValue;
import util.IntValue;

public abstract class ObjectFacade extends NamedObject implements Context {

	private static final long serialVersionUID = 7604762210439526542L;

	abstract String getProp(String key);
	abstract String setProp(String key, String value);
	abstract <E> E getEquip(String key);
	abstract Object setEquip(String key, Object obj);

	/**
	 * Constructor for ObjectFacade class.
	 * @param database database object
	 */
	public ObjectFacade(NamedObjectDB database) {
		super(database);
	}
	/**
	 * Get keyword value of agent or spot.
	 * @return keyword value
	 */
	public String getKeyword(String key) {
		return getProp(key);
	}
	/**
	 * Set keyword value of agent or spot.
	 */
	public void setKeyword(String key, String value) {
		setProp(key, value);
	}
	/**
	 * Get spot by name.
	 * @param name name string of a spot
	 * @return spot with specified name or null
	 */
	public static Spot getSpot(String name) {
		return SpotVal.forName(name).getSpot(null);
	}
	/**
	 * Get spot value of agent or spot.
	 * @return spot value
	 */
	public Spot getSpotVariable(String key) {
		return SpotVal.forName(key).getSpot(this);
	}
	/**
	 * Set spot value of agent or spot.
	 */
	public void setSpotVariable(String key, Spot value) {
		setEquip(key, value);
	}
	/**
	 * Get int value of agent or spot.
	 * @return int value
	 */
	public int getIntVariable(String key) {
		return this.<Number>getEquip(key).intValue();
	}
	/**
	 * Set int value of agent or spot.
	 */
	public void setIntVariable(String key, int value) {
		Object v = getEquip(key);
		if (!(v instanceof IntValue)) {
			setEquip(key, v = new IntValue());
		}
		((IntValue) v).setInteger(value);
	}
	/**
	 * Get double value of agent or spot.
	 * @return double value
	 */
	public double getDoubleVariable(String key) {
		return this.<Number>getEquip(key).doubleValue();
	}
	/**
	 * Set double value of agent or spot.
	 */
	public void setDoubleVariable(String key, double value) {
		Object v = getEquip(key);
		if (!(v instanceof DoubleValue)) {
			setEquip(key, v = new DoubleValue());
		}
		((DoubleValue) v).setDouble(value);
	}
	/**
	 * Get role value of agent or spot.
	 * @return role value
	 */
	public RoleType getRoleVariable(String key) {
		if (this instanceof Agent) {
			return this.<Role>getEquip("$Role." + key).getRoleType();
		}
		return this.<RoleType>getEquip("$Role." + key);
	}
	/**
	 * Set role value of agent or spot.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void setRoleVariable(String key, RoleType value) throws InstantiationException, IllegalAccessException {
		if (this instanceof Agent) {
			setEquip("$Role." + key, value.createRole((Agent) this));
		}
		else {
			setEquip("$Role." + key, value);
		}
	}
	/**
	 * Set role value of agent or spot.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void setRoleVariable(String key, String value) throws InstantiationException, IllegalAccessException {
		setRoleVariable(key, RoleType.forName(value));
	}
	/**
	 * Get time value of agent or spot.
	 * @return role value
	 */
	public Time getTimeVariable(String key) {
		return this.<Time>getEquip("$Time." + key);
	}
	/**
	 * Set time value of agent or spot.
	 */
	public void setTimeVariable(String key, String value) {
		setEquip("$Time." + key, Time.parse(value));
	}
}
