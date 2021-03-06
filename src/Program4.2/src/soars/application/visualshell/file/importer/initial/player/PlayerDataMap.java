/*
 * Created on 2005/12/12
 */
package soars.application.visualshell.file.importer.initial.player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import soars.application.visualshell.file.importer.initial.InitialDataImporter;
import soars.application.visualshell.file.importer.initial.role.RoleDataMap;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.player.base.object.role.RoleVariableObject;
import soars.common.soars.warning.WarningManager;

/**
 * The PlayerData hashtable(name[String] - PlayerData).
 * @author kurata / SOARS project
 */
public class PlayerDataMap extends HashMap<String, PlayerData> {

	/**
	 * 
	 */
	private String _type = "";

	/**
	 * Creates this object with the specified data.
	 * @param type the type of this object("agent" or "spot")
	 */
	public PlayerDataMap(String type) {
		super();
		_type = type;
	}

	/**
	 * Returns true if the object which has the specified name is contained.
	 * @param name the specified neme
	 * @return true if the object which has the specified name is contained
	 */
	public boolean contains(String name) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			PlayerData playerData = ( PlayerData)entry.getValue();
			if ( playerData.contains( name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if the object which has the specified name and number is contained.
	 * @param name the specified name
	 * @param number the specified number
	 * @param exception the exception name
	 * @return true if the object which has the specified name and number is contained
	 */
	public boolean contains(String name, String number, String exception) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			PlayerData playerData = ( PlayerData)entry.getValue();
			if ( playerData._name.equals( exception))
				continue;

			if ( playerData.contains( name, number))
				return true;
		}
		return false;
	}

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean contains_as_object_name(String name, String number) {
		// TODO Auto-generated method stub
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			PlayerData playerData = ( PlayerData)entry.getValue();
			if ( playerData.contains_as_object_name( name, number))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if the object which has the specified name exists.
	 * @param kind the object type
	 * @param name the object name
	 * @return true if the object which has the specified name exists
	 */
	public boolean is_object_name(String kind, String name) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			PlayerData playerData = ( PlayerData)entry.getValue();
			if ( playerData.has_same_object_name( kind, name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if the loaded data are valid.
	 * @param anotherDataMap the another PlayertData hashtable
	 * @param roleDataMap the role data hashtable
	 * @return true if the loaded data are valid
	 */
	public boolean verify(PlayerDataMap anotherDataMap, RoleDataMap roleDataMap) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			PlayerData playerData = ( PlayerData)entry.getValue();
			if ( !playerData.verify(
				_type.equals( "agent") ? this : anotherDataMap,
				_type.equals( "agent") ? anotherDataMap : this))
				return false;
			if ( !playerData._initialRole.equals( "")
				&& !roleDataMap.contains( playerData._initialRole)) {
				String[] message = new String[] {
					_type.equals( "agent") ? "Agent" : "Spot",
					"Initial role = " + playerData._initialRole
				};
				WarningManager.get_instance().add( message);
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if the loaded data are valid.
	 * @param agentRoleDataMap the agent role data hashtable
	 * @param spotRoleDataMap the spot role data hashtable
	 * @return true if the loaded data are valid
	 */
	public boolean verify(RoleDataMap agentRoleDataMap, RoleDataMap spotRoleDataMap) {
		boolean result = true;
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			PlayerData playerData = ( PlayerData)entry.getValue();
			if ( !verify( playerData, agentRoleDataMap, spotRoleDataMap))
				result = false;
		}
		return result;
	}

	/**
	 * @param playerData
	 * @param agentRoleDataMap
	 * @param spotRoleDataMap
	 * @return
	 */
	private boolean verify(PlayerData playerData, RoleDataMap agentRoleDataMap, RoleDataMap spotRoleDataMap) {
		boolean result = true;
		Iterator iterator = playerData._objectMapMap.get( "role variable").entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String role_variable = ( String)entry.getKey();
			if ( agentRoleDataMap.contains( role_variable)
				|| spotRoleDataMap.contains( role_variable)) {
				String[] message = new String[] {
					_type.equals( "agent") ? "Agent" : "Spot",
					"Invalid role variable name : " + role_variable
				};
				WarningManager.get_instance().add( message);
				result = false;
			}
			if ( !playerData.is_multi()) {
				RoleVariableObject roleVariableObject = ( RoleVariableObject)entry.getValue();
				if ( roleVariableObject._initialValue.equals( ""))
					continue;

				if ( !agentRoleDataMap.contains( roleVariableObject._initialValue)) {
					String[] message = new String[] {
						_type.equals( "agent") ? "Agent" : "Spot",
						"Invalid role name : " + roleVariableObject._initialValue
					};
					WarningManager.get_instance().add( message);
					result = false;
				}
			} else {
				HashMap indices_map = ( HashMap)entry.getValue();
				if ( !verify( indices_map, agentRoleDataMap))
					result = false;
			}
		}
		return result;
	}

	/**
	 * @param indicesMap
	 * @param agentRoleDataMap
	 * @return
	 */
	private boolean verify(HashMap indicesMap, RoleDataMap agentRoleDataMap) {
		boolean result = true;
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String role_name = ( String)entry.getKey();
			if ( role_name.equals( ""))
				continue;

			if ( !agentRoleDataMap.contains( role_name)) {
				String[] message = new String[] {
					_type.equals( "agent") ? "Agent" : "Spot",
					"Invalid role name : " + role_name
				};
				WarningManager.get_instance().add( message);
				result = false;
			}
		}
		return result;
	}

	/**
	 * Returns true if the specified class variable name which the specified agent or spot has is in use for a different class.
	 * @param playerData the specified agent or spot
	 * @param name the specified class variable name
	 * @param jarFilename the jar file name
	 * @param classname the class name
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true if the specified class variable name which the specified agent or spot has is in use for a different class
	 */
	public boolean uses_this_class_variable_as_different_class(PlayerData playerData, String name, String jarFilename, String classname, int number, boolean warning) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			PlayerData od = ( PlayerData)entry.getValue();
			if ( od.equals( playerData))
				continue;

			if ( od.uses_this_class_variable_as_different_class( name, jarFilename, classname, number, warning))
				return true;
		}

		if ( LayerManager.get_instance().other_uses_this_class_variable_as_different_class(name, jarFilename, classname, playerData._type)) {
			InitialDataImporter.on_invalid_line_error( number, warning);
			return true;
		}

		return false;
	}

	/**
	 * For debug print.
	 */
	public void print() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			PlayerData playerData = ( PlayerData)entry.getValue();
			playerData.print();
		}
	}
}
