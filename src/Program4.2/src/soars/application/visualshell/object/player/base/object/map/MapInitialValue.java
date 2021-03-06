/**
 * 
 */
package soars.application.visualshell.object.player.base.object.map;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.file.importer.initial.player.PlayerData;
import soars.application.visualshell.file.importer.initial.player.PlayerDataMap;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.player.agent.AgentObject;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.object.base.InitialValueBase;
import soars.application.visualshell.object.player.base.object.base.ObjectBase;
import soars.application.visualshell.object.role.base.object.command.MapCommand;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class MapInitialValue extends InitialValueBase {

	/**
	 * 
	 */
	public String[] _key = new String[] { "", ""};		// { type, value}

	/**
	 * 
	 */
	public String[] _value = new String[] { "", ""};	// { type, value}

	/**
	 * 
	 */
	public MapInitialValue() {
		super();
	}

	/**
	 * @param key_type
	 * @param key
	 * @param value_type
	 * @param value
	 */
	public MapInitialValue(String key_type, String key, String value_type, String value) {
		super();
		_key[ 0] = key_type;
		_key[ 1] = key;
		_value[ 0] = value_type;
		_value[ 1] = value;
	}

	/**
	 * @param mapInitialValue
	 */
	public MapInitialValue(MapInitialValue mapInitialValue) {
		super();
		copy( mapInitialValue);
	}

	/**
	 * For copy, cut and paste only.
	 * @param mapInitialValue
	 */
	public MapInitialValue(MapInitialValue mapInitialValue, int row) {
		super(row);
		copy( mapInitialValue);
	}

	/**
	 * @param mapInitialValue
	 */
	public void copy(MapInitialValue mapInitialValue) {
		_key[ 0] = mapInitialValue._key[ 0];
		_key[ 1] = mapInitialValue._key[ 1];
		_value[ 0] = mapInitialValue._value[ 0];
		_value[ 1] = mapInitialValue._value[ 1];
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ( !( obj instanceof MapInitialValue))
			return false;

		MapInitialValue mapInitialValue = ( MapInitialValue)obj;
		return ( _key[ 0].equals( mapInitialValue._key[ 0])
			&& _key[ 1].equals( mapInitialValue._key[ 1])
			&& _value[ 0].equals( mapInitialValue._value[ 0])
			&& _value[ 1].equals( mapInitialValue._value[ 1]));
	}

	/**
	 * @param objectBase
	 * @return
	 */
	public boolean contains(ObjectBase objectBase) {
		return ( ( _key[ 0].equals( objectBase._kind) && _key[ 1].equals( objectBase._name))
			|| ( _value[ 0].equals( objectBase._kind) && _value[ 1].equals( objectBase._name)));
	}

	/**
	 * @param name
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param playerBase
	 * @return
	 */
	public boolean contains(String name, String type, String headName, Vector ranges, PlayerBase playerBase) {
		if ( contains( _key, type, headName, ranges)) {
			String[] message = new String[] {
				( ( playerBase instanceof AgentObject) ? "Agent" : "Spot"),
				"name = " + playerBase._name,
				"map " + name,
				"type = " + _key[ 0],
				"value = " + _key[ 1]
			};

			WarningManager.get_instance().add( message);

			return true;
		}

		if ( contains( _value, type, headName, ranges)) {
			String[] message = new String[] {
				( ( playerBase instanceof AgentObject) ? "Agent" : "Spot"),
				"name = " + playerBase._name,
				"map " + name,
				"type = " + _value[ 0],
				"value = " + _value[ 1]
			};

			WarningManager.get_instance().add( message);

			return true;
		}

		return false;
	}

	/**
	 * @param kind
	 * @param name
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param playerBase
	 * @return
	 */
	public boolean contains(String name, String type, String headName, Vector ranges, String newHeadName, Vector newRanges, PlayerBase playerBase) {
		if ( contains( _key, type, headName, ranges)
			&& !contains( _key, type, newHeadName, newRanges)) {
			String[] message = new String[] {
				( ( playerBase instanceof AgentObject) ? "Agent" : "Spot"),
				"name = " + playerBase._name,
				"map " + name,
				"type = " + _key[ 0],
				"value = " + _key[ 1]
			};

			WarningManager.get_instance().add( message);

			return true;
		}

		if ( contains( _value, type, headName, ranges)
			&& !contains( _value, type, newHeadName, newRanges)) {
			String[] message = new String[] {
				( ( playerBase instanceof AgentObject) ? "Agent" : "Spot"),
				"name = " + playerBase._name,
				"map " + name,
				"type = " + _value[ 0],
				"value = " + _value[ 1]
			};

			WarningManager.get_instance().add( message);

			return true;
		}

		return false;
	}

	/**
	 * @param name
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param playerBase
	 * @return
	 */
	public boolean can_adjust_name(String name, String type, String headName, Vector ranges, PlayerBase playerBase) {
		if ( contains( _key, type, headName, ranges)) {
			String[] message = new String[] {
				( ( playerBase instanceof AgentObject) ? "Agent" : "Spot"),
				"name = " + playerBase._name,
				"map " + name,
				"type = " + _key[ 0],
				"value = " + _key[ 1]
			};

			WarningManager.get_instance().add( message);

			return false;
		}

		if ( contains( _value, type, headName, ranges)) {
			String[] message = new String[] {
				( ( playerBase instanceof AgentObject) ? "Agent" : "Spot"),
				"name = " + playerBase._name,
				"map " + name,
				"type = " + _value[ 0],
				"value = " + _value[ 1]
			};

			WarningManager.get_instance().add( message);

			return false;
		}

		return true;
	}

	/**
	 * @param name
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param playerBase
	 * @return
	 */
	public boolean can_adjust_name(String name, String type, String headName, Vector ranges, String newHeadName, Vector newRanges, PlayerBase playerBase) {
		if ( contains( _key, type, headName, ranges)
			&& !contains( _key, type, newHeadName, newRanges)) {
			String[] message = new String[] {
				( ( playerBase instanceof AgentObject) ? "Agent" : "Spot"),
				"name = " + playerBase._name,
				"map " + name,
				"type = " + _key[ 0],
				"value = " + _key[ 1]
			};

			WarningManager.get_instance().add( message);

			return false;
		}

		if ( contains( _value, type, headName, ranges)
			&& !contains( _value, type, newHeadName, newRanges)) {
			String[] message = new String[] {
				( ( playerBase instanceof AgentObject) ? "Agent" : "Spot"),
				"name = " + playerBase._name,
				"map " + name,
				"type = " + _value[ 0],
				"value = " + _value[ 1]
			};

			WarningManager.get_instance().add( message);

			return false;
		}

		return true;
	}

	/**
	 * @param values
	 * @param type
	 * @param headName
	 * @param ranges
	 * @return
	 */
	private boolean contains(String[] values, String type, String headName, Vector ranges) {
		return ( values[ 0].equals( type) && SoarsCommonTool.has_same_name( headName, ranges, values[ 1]));
	}

	/**
	 * @param type
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public boolean update_name_and_number(String type, String newName, String originalName, String headName, Vector ranges, String newHeadName, Vector newRanges) {
		return ( update_name_and_number( _key, type, newName, originalName, headName, ranges, newHeadName, newRanges)
			|| update_name_and_number( _value, type, newName, originalName, headName, ranges, newHeadName, newRanges));
	}

	/**
	 * @param values
	 * @param type
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	private boolean update_name_and_number(String[] values, String type, String newName, String originalName, String headName, Vector ranges, String newHeadName, Vector newRanges) {
		if ( !values[ 0].equals( type))
			return false;

		if ( !SoarsCommonTool.has_same_name( headName, ranges, values[ 1]))
			return false;

		if ( SoarsCommonTool.has_same_name( newHeadName, newRanges, values[ 1]))
			return false;

		values[ 1] = newName + values[ 1].substring( originalName.length());

		return true;
	}

	/**
	 * @param alias
	 * @return
	 */
	public boolean contains_this_alias(String alias) {
		return ( _key[ 1].equals( alias) || _value[ 1].equals( alias));
	}

	/**
	 * @param initialValues
	 */
	public void get_initial_values1(Vector initialValues) {
		String key = ( ( _key[ 0].equals( "immediate") ? "\"" : "") + _key[ 1] + ( _key[ 0].equals( "immediate") ? "\"" : ""));
		if ( !initialValues.contains( key))
			initialValues.add( key);

		if ( !initialValues.contains( _value[ 1]))
			initialValues.add( _value[ 1]);
	}

	/**
	 * @param initialValues
	 */
	public void get_initial_values2(Vector initialValues) {
		String key = ( ( _key[ 0].equals( "immediate") ? "\"" : "") + _key[ 1] + ( _key[ 0].equals( "immediate") ? "\"" : ""));
		String initialValue = ( "[" + key + ", " + _value[ 1] + "]");
		initialValues.add( initialValue);
	}

	/**
	 * @param type
	 * @param name
	 * @param newName
	 * @return
	 */
	public boolean update_object_name(String type, String name, String newName) {
		boolean result = false;
		if ( type.equals( _key[ 0]) && name.equals( _key[ 1])) {
			_key[ 1] = newName;
			result = true;
		}

		if ( type.equals( _value[ 0]) && name.equals( _value[ 1])) {
			_value[ 1] = newName;
			result = true;
		}

		return result;
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	public boolean can_paste(Layer drawObjects) {
		// エージェントまたはスポット以外は全て自らが保持しているのでチェックは必要無い筈！
		if ( _value[ 0].equals( "agent"))
			return ( null != drawObjects.get_agent_has_this_name( _value[ 1]));
		else if ( _value[ 0].equals( "spot"))
			return ( null != drawObjects.get_spot_has_this_name( _value[ 1]));
		else
			return true;
	}

	/**
	 * @param mapInitialValue
	 * @return
	 */
	public boolean is_same_key(MapInitialValue mapInitialValue) {
		return ( _key[ 0].equals( mapInitialValue._key[ 0]) && _key[ 1].equals( mapInitialValue._key[ 1]));
	}

	/**
	 * @return
	 */
	public String get_initial_data() {
		return ( "\t" + _key[ 0] + "\t" + _key[ 1] + "\t" + _value[ 0] + "\t" + _value[ 1]);
	}

	/**
	 * @param prefix
	 * @param name
	 * @param initialValueMap
	 * @return
	 */
	public String get_script(String prefix, String name, InitialValueMap initialValueMap) {
		// TODO 修正済
		String key = ( null == initialValueMap) ? _key[ 1] : initialValueMap.get_initial_value( _key[ 1]);
		key = ( ( _key[ 0].equals( "immediate") ? "\"" : "") + key + ( _key[ 0].equals( "immediate") ? "\"" : ""));

		String value = ( null == initialValueMap) ? _value[ 1] : initialValueMap.get_initial_value( _value[ 1]);
		if ( _value[ 0].equals( "agent"))
			value = ( "{" + value + "}");
		else if ( _value[ 0].equals( "spot"))
			value = ( "<" + value + ">");
		else if ( _value[ 0].equals( "role variable"))
			value = ( "$Role." + value);
		else if ( _value[ 0].equals( "time variable"))
			value = ( "$Time." + value);

		String script = ( _key[ 0].equals( "keyword") ? ( prefix + "equip " + key) : "");

		if ( _value[ 0].equals( "keyword") || _value[ 0].equals( "file"))
			script += ( ( script.equals( "") ? "" : " ; ") + prefix + "equip " + value);

		script += ( ( script.equals( "") ? "" : " ; ") + prefix + MapCommand._reserved_words[ 1] + name + "=" + key + "=" + value);

		if ( _value[ 0].equals( "keyword") || _value[ 0].equals( "file"))
			return script;
		else if ( _value[ 0].equals( "time variable"))
			return ( script + " ; " + prefix + "cloneEquip " + value);
		else if ( _value[ 0].equals( "agent") || _value[ 0].equals( "spot") || _value[ 0].equals( "role variable") || _value[ 0].equals( "spot variable"))
			return script;
		else
			return ( script + " ; " + prefix + "cloneEquip " + value);
	}

	/**
	 * @param index
	 * @param attributesImpl
	 */
	public void write(int index, AttributesImpl attributesImpl) {
		attributesImpl.addAttribute( null, null, "key_type" + index, "", Writer.escapeAttributeCharData( _key[ 0]));
		attributesImpl.addAttribute( null, null, "key" + index, "", Writer.escapeAttributeCharData( _key[ 1]));
		attributesImpl.addAttribute( null, null, "value_type" + index, "", Writer.escapeAttributeCharData( _value[ 0]));
		attributesImpl.addAttribute( null, null, "value" + index, "", Writer.escapeAttributeCharData( _value[ 1]));
	}

	/**
	 * @param msg
	 * @param agentDataMap
	 * @param spotDataMap
	 * @param playerData
	 * @return
	 */
	public boolean verify(String msg, PlayerDataMap agentDataMap, PlayerDataMap spotDataMap, PlayerData playerData) {
		return ( verify( _key[ 0], _key[ 1], msg, agentDataMap, spotDataMap, playerData)
			&& verify( _value[ 0], _value[ 1], msg, agentDataMap, spotDataMap, playerData));
	}

	/**
	 * @param type
	 * @param value
	 * @param msg
	 * @param agentDataMap
	 * @param spotDataMap
	 * @param playerData
	 * @return
	 */
	private boolean verify(String type, String value, String msg, PlayerDataMap agentDataMap, PlayerDataMap spotDataMap, PlayerData playerData) {
		if ( type.equals( "immediate"))
			return true;
		else if ( type.equals( "agent")) {
			if ( !agentDataMap.contains( value) && null == LayerManager.get_instance().get_agent_has_this_name( value)) {
				String[] message = new String[] {
					"Agent",
					"name = " + playerData._name,
					msg,
					"agent " + value + " does not exist!"
				};

				WarningManager.get_instance().add( message);

				return false;
			}
		} else if ( type.equals( "spot")) {
			if ( !spotDataMap.contains( value) && null == LayerManager.get_instance().get_spot_has_this_name( value)) {
				String[] message = new String[] {
					"Spot",
					"name = " + playerData._name,
					msg,
					"spot " + value + " does not exist!"
				};

				WarningManager.get_instance().add( message);

				return false;
			}
		} else {
			if ( playerData.has_same_object_name( type, value))
				return true;

			PlayerBase playerBase;
			if ( playerData._type.equals( "agent"))
				playerBase = LayerManager.get_instance().get_agent( playerData._name);
			else
				playerBase = LayerManager.get_instance().get_spot( playerData._name);

			if ( null == playerBase || playerBase.is_multi() || !playerBase.has_same_object_name( type, value)) {
				String[] message = new String[] {
					( playerData._type.equals( "agent") ? "Agent" : "Spot"),
					"name = " + playerData._name,
					msg,
					type + " " + value + " does not exist!"
				};

				WarningManager.get_instance().add( message);

				return false;
			}

			return true;
		}

		return true;
	}

	/**
	 * @param msg
	 * @param agentDataMap
	 * @param spotDataMap
	 * @param indices
	 * @param playerData
	 * @return
	 */
	public boolean verify(String msg, PlayerDataMap agentDataMap, PlayerDataMap spotDataMap, Vector indices, PlayerData playerData) {
		return ( verify( _key[ 0], _key[ 1], msg, agentDataMap, spotDataMap, indices, playerData)
			&& verify( _value[ 0], _value[ 1], msg, agentDataMap, spotDataMap, indices, playerData));
	}

	/**
	 * @param type
	 * @param value
	 * @param msg
	 * @param agentDataMap
	 * @param spotDataMap
	 * @param indices
	 * @param playerData
	 * @return
	 */
	private boolean verify(String type, String value, String msg, PlayerDataMap agentDataMap, PlayerDataMap spotDataMap, Vector indices, PlayerData playerData) {
		if ( type.equals( "immediate"))
			return true;
		else if ( type.equals( "agent")) {
			if ( !agentDataMap.contains( value) && null == LayerManager.get_instance().get_agent_has_this_name( value)) {
				String[] message = new String[] {
					"Agent",
					"name = " + playerData._name,
					msg,
					"agent " + value + " does not exist!"
				};

				WarningManager.get_instance().add( message);

				return false;
			}
		} else if ( type.equals( "spot")) {
			if ( !spotDataMap.contains( value) && null == LayerManager.get_instance().get_spot_has_this_name( value)) {
				String[] message = new String[] {
					"Spot",
					"name = " + playerData._name,
					msg,
					"spot " + value + " does not exist!"
				};

				WarningManager.get_instance().add( message);

				return false;
			}
		} else {
			Vector objectRanges = playerData.get_object_ranges( type, value);

			PlayerBase playerBase;
			if ( playerData._type.equals( "agent"))
				playerBase = LayerManager.get_instance().get_agent( playerData._name);
			else
				playerBase = LayerManager.get_instance().get_spot( playerData._name);

			if ( null != playerBase && playerBase.is_multi()) {
				Vector ranges = playerBase.get_object_ranges( type, value);
				CommonTool.merge_indices( ranges, objectRanges);
			}

			if ( !CommonTool.contains_ranges( objectRanges, indices)) {
				String[] message = new String[] {
					( playerData._type.equals( "agent") ? "Agent" : "Spot"),
					"name = " + playerData._name,
					msg,
					type + " " + value + " does not exist!"
				};

				WarningManager.get_instance().add( message);

				return false;
			}

			return true;
		}

		return true;
	}

	/**
	 * @param playerBase
	 * @return
	 */
	public boolean transform(PlayerBase playerBase) {
		if ( _key[ 1].equals( "")) {
			if ( _key[ 0].equals( ""))
				_key[ 0] = "immediate";
		} else {
			if ( !transform( _key, playerBase))
				_key[ 0] = "immediate";
		}

		if ( !transform( _value, playerBase))
			return false;

		return true;
	}

	/**
	 * @param values
	 * @param playerBase
	 * @return
	 */
	private boolean transform(String[] values, PlayerBase playerBase) {
		if ( !values[ 0].equals( ""))
			return true;

		String type = playerBase.which_object_has_this_name( values[ 1]);
		if ( null != type) {
			values[ 0] = type;
			return true;
		}

		if ( null != LayerManager.get_instance().get_agent_has_this_name( values[ 1])) {
			values[ 0] = "agent";
			return true;
		}
			
		if ( null != LayerManager.get_instance().get_spot_has_this_name( values[ 1])) {
			values[ 0] = "spot";
			return true;
		}
			
		return false;
	}

	/**
	 * @param propertyPageMap
	 * @param objectBase
	 * @param objectBases
	 * @param playerBase
	 * @return
	 */
	public boolean can_paste(Map<String, PropertyPageBase> propertyPageMap, ObjectBase objectBase, List<ObjectBase> objectBases, PlayerBase playerBase) {
		// TODO Auto-generated method stub
		return ( can_paste_key( propertyPageMap, playerBase) && can_paste_value( propertyPageMap, objectBase, objectBases, playerBase));
	}

	/**
	 * @param propertyPageMap
	 * @param playerBase
	 * @return
	 */
	private boolean can_paste_key(Map<String, PropertyPageBase> propertyPageMap, PlayerBase playerBase) {
		// TODO Auto-generated method stub
		if ( _key[ 0].equals( "immediate"))
			return true;

		if ( _key[ 0].equals( "keyword")) {
			PropertyPageBase propertyPageBase = propertyPageMap.get( "simple variable");
			return ( ( null != propertyPageBase) && propertyPageBase.contains( _key[ 1]));
		}

		return false;
	}

	/**
	 * @param propertyPageMap
	 * @param objectBase
	 * @param objectBases
	 * @param playerBase
	 * @return
	 */
	private boolean can_paste_value(Map<String, PropertyPageBase> propertyPageMap, ObjectBase objectBase, List<ObjectBase> objectBases, PlayerBase playerBase) {
		// TODO Auto-generated method stub
		if ( _value[ 0].equals( "agent") && null != LayerManager.get_instance().get_agent_has_this_name( _value[ 1]))
			return true;

		if ( _value[ 0].equals( "spot") && null != LayerManager.get_instance().get_spot_has_this_name( _value[ 1]))
			return true;

		if ( _value[ 0].equals( "probability") || _value[ 0].equals( "keyword") || _value[ 0].equals( "number object")
			|| _value[ 0].equals( "time variable") || _value[ 0].equals( "spot variable")) {
			PropertyPageBase propertyPageBase = propertyPageMap.get( "simple variable");
			return ( ( null != propertyPageBase) && propertyPageBase.contains( _value[ 1]));
		}

		if ( _value[ 0].equals( "role variable")) {
			PropertyPageBase propertyPageBase = propertyPageMap.get( "simple variable");
			return ( ( playerBase instanceof AgentObject) && ( null != propertyPageBase) && propertyPageBase.contains( _value[ 1]));
		}

		if ( _value[ 0].equals( "collection") || _value[ 0].equals( "list") || _value[ 0].equals( "map") || _value[ 0].equals( "exchange algebra")) {
			PropertyPageBase propertyPageBase = propertyPageMap.get( "variable");
			return ( ( null != propertyPageBase) && ( propertyPageBase.contains( _value[ 1]) || contains( _value[ 1], objectBase, objectBases)));
//			if ( null == propertyPageBase)
//				return false;
//
//			if ( propertyPageBase.contains( _value[ 1]))
//				return true;
//
//			return contains( _value[ 1], propertyPageMap, objectBase, objectBases, playerBase);
		}

		if ( _value[ 0].equals( "class variable")) {
			PropertyPageBase propertyPageBase = propertyPageMap.get( "class variable");
			return ( ( null != propertyPageBase) && propertyPageBase.contains( _value[ 1]));
		}

		if ( _value[ 0].equals( "file")) {
			PropertyPageBase propertyPageBase = propertyPageMap.get( "file");
			return ( ( null != propertyPageBase) && propertyPageBase.contains( _value[ 1]));
		}

		return false;
	}

	/**
	 * @param name
	 * @param objectBase
	 * @param objectBases
	 * @return
	 */
	private boolean contains(String name, ObjectBase objectBase, List<ObjectBase> objectBases) {
		// TODO Auto-generated method stub
		if ( null == objectBase || null == objectBases)
			return false;

		for ( ObjectBase ob:objectBases) {
			if ( ob == objectBase)
				continue;

			if ( ob._name.equals( name))
				return true;
		}
		return false;
	}

//	/**
//	 * @param name
//	 * @param propertyPageMap
//	 * @param objectBase
//	 * @param objectBases
//	 * @param playerBase
//	 * @return
//	 */
//	private boolean contains(String name, Map<String, PropertyPageBase> propertyPageMap, ObjectBase objectBase, List<ObjectBase> objectBases, PlayerBase playerBase) {
//		if ( null == objectBase || null == objectBases)
//			return false;
//
//		for ( ObjectBase ob:objectBases) {
//			if ( ob == objectBase)
//				continue;
//
//			if ( ob._name.equals( name))
//				return true;
//			if ( !ob._name.equals( name))
//				continue;
//
//			if ( ob instanceof VariableObject) {
//				VariableObject variableObject = ( VariableObject)ob;
//				return variableObject.can_paste( propertyPageMap, objectBase, objectBases, playerBase);
//			} else if ( ob instanceof MapObject) {
//				MapObject mapObject = ( MapObject)ob;
//				return mapObject.can_paste( propertyPageMap, objectBase, objectBases, playerBase);
//			} else if ( ob instanceof ExchangeAlgebraObject) {
//				ExchangeAlgebraObject exchangeAlgebraObject = ( ExchangeAlgebraObject)ob;
//				return exchangeAlgebraObject.can_paste( propertyPageMap);
//			}
//			return false;
//		}
//		return false;
//	}
}
