/*
 * Created on 2006/04/05
 */
package soars.application.visualshell.object.player.base.object.variable;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.file.exporter.java.JavaProgramExporter;
import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
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
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class VariableInitialValue extends InitialValueBase {

	/**
	 * 
	 */
	public String _type = "";

	/**
	 * 
	 */
	public String _value = "";

	/**
	 * 
	 */
	public VariableInitialValue() {
		super();
	}

	/**
	 * @param type
	 * @param value
	 */
	public VariableInitialValue(String type, String value) {
		super();
		_type = type;
		_value = value;
	}

	/**
	 * @param variableInitialValue
	 */
	public VariableInitialValue(VariableInitialValue variableInitialValue) {
		super();
		copy( variableInitialValue);
	}

	/**
	 * For copy, cut and paste only.
	 * @param row
	 * @param variableInitialValue
	 */
	public VariableInitialValue(VariableInitialValue variableInitialValue, int row) {
		super(row);
		copy( variableInitialValue);
	}

	/**
	 * @param variableInitialValue
	 */
	public void copy(VariableInitialValue variableInitialValue) {
		_type = variableInitialValue._type;
		_value = variableInitialValue._value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ( !( obj instanceof VariableInitialValue))
			return false;

		VariableInitialValue variableInitialValue = ( VariableInitialValue)obj;
		return ( _type.equals( variableInitialValue._type)
			&& _value.equals( variableInitialValue._value));
	}

	/**
	 * @param objectBase
	 * @return
	 */
	public boolean contains(ObjectBase objectBase) {
		return ( _type.equals( objectBase._kind) && _value.equals( objectBase._name));
	}

	/**
	 * @param kind
	 * @param name
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param playerBase
	 * @return
	 */
	public boolean contains(String kind, String name, String type, String headName, Vector ranges, PlayerBase playerBase) {
		if ( contains( type, headName, ranges)) {
			String[] message = new String[] {
				( ( playerBase instanceof AgentObject) ? "Agent" : "Spot"),
				"name = " + playerBase._name,
				kind + " " + name,
				"type = " + _type,
				"value = " + _value
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
	public boolean contains(String kind, String name, String type, String headName, Vector ranges, String newHeadName, Vector newRanges, PlayerBase playerBase) {
		if ( contains( type, headName, ranges)
			&& !contains( type, newHeadName, newRanges)) {
			String[] message = new String[] {
				( ( playerBase instanceof AgentObject) ? "Agent" : "Spot"),
				"name = " + playerBase._name,
				kind + " " + name,
				"type = " + _type,
				"value = " + _value
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
	 * @param playerBase
	 * @return
	 */
	public boolean can_adjust_name(String kind, String name, String type, String headName, Vector ranges, PlayerBase playerBase) {
		if ( contains( type, headName, ranges)) {
			String[] message = new String[] {
				( ( playerBase instanceof AgentObject) ? "Agent" : "Spot"),
				"name = " + playerBase._name,
				kind + " " + name,
				"type = " + _type,
				"value = " + _value
			};

			WarningManager.get_instance().add( message);

			return false;
		}
		return true;
	}

	/**
	 * @param kind
	 * @param name
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param playerBase
	 * @return
	 */
	public boolean can_adjust_name(String kind, String name, String type, String headName, Vector ranges, String newHeadName, Vector newRanges, PlayerBase playerBase) {
		if ( contains( type, headName, ranges) && !contains( type, newHeadName, newRanges)) {
			String[] message = new String[] {
				( ( playerBase instanceof AgentObject) ? "Agent" : "Spot"),
				"name = " + playerBase._name,
				kind + " " + name,
				"type = " + _type,
				"value = " + _value
			};

			WarningManager.get_instance().add( message);

			return false;
		}
		return true;
	}

	/**
	 * @param type
	 * @param headName
	 * @param ranges
	 * @return
	 */
	private boolean contains(String type, String headName, Vector ranges) {
		return ( _type.equals( type) && SoarsCommonTool.has_same_name( headName, ranges, _value));
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
		if ( !_type.equals( type))
			return false;

		if ( !SoarsCommonTool.has_same_name( headName, ranges, _value))
			return false;

		if ( SoarsCommonTool.has_same_name( newHeadName, newRanges, _value))
			return false;

		_value = newName + _value.substring( originalName.length());

		return true;
	}

	/**
	 * @param type
	 * @param name
	 * @param newName
	 * @return
	 */
	public boolean update_object_name(String type, String name, String newName) {
		if ( !type.equals( _type) || !name.equals( _value))
			return false;

		_value = newName;
		return true;
	}

	/**
	 * @param alias
	 * @return
	 */
	public boolean contains_this_alias(String alias) {
		return _value.equals( alias);
	}

	/**
	 * @param initialValues
	 */
	public void get_initial_values1(Vector initialValues) {
		if ( !initialValues.contains( _value))
			initialValues.add( _value);
	}

	/**
	 * @param initialValues
	 */
	public void get_initial_values2(Vector initialValues) {
		initialValues.add( _value);
	}

	/**
	 * @return
	 */
	public String get_initial_data() {
		return ( "\t" + _type + "\t" + _value);
	}

	/**
	 * @param kind
	 * @param prefix
	 * @param name
	 * @param initialValueMap
	 * @return
	 */
	public String get_script(String kind, String prefix, String name, InitialValueMap initialValueMap) {
		String[] commands = new String[] {
			kind.equals( "collection") ? "addAgent " : "addLastAgent ",
			kind.equals( "collection") ? "addSpot " : "addLastSpot ",
			kind.equals( "collection") ? "addEquip " : "addLastEquip "
		};

		// TODO 修正済
		if ( _type.equals( "agent"))
			return ( prefix + commands[ 0] + name + "=" + _value);
		else if ( _type.equals( "spot"))
			return ( prefix + commands[ 1] + name + "=" + _value);
		else if ( _type.equals( "keyword") || _type.equals( "file"))
			return ( prefix + "equip " + _value + " ; " + prefix + commands[ 2] + name + "=" + _value);
		else if ( _type.equals( "role variable"))
			return ( prefix + commands[ 2] + name + "=$Role." + _value);
		else if ( _type.equals( "time variable"))
			return ( prefix + commands[ 2] + name + "=$Time." + _value + " ; " + prefix + "cloneEquip $Time." + _value);
		else if ( _type.equals( "spot variable"))
			return ( prefix + commands[ 2] + name + "=" + _value);
		else
			return ( prefix + commands[ 2] + name + "=" + _value + " ; " + prefix + "cloneEquip " + _value);
	}

	/**
	 * @param index
	 * @param attributesImpl
	 */
	public void write(int index, AttributesImpl attributesImpl) {
		attributesImpl.addAttribute( null, null, "type" + index, "", Writer.escapeAttributeCharData( _type));
		attributesImpl.addAttribute( null, null, "value" + index, "", Writer.escapeAttributeCharData( _value));
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	public boolean can_paste(Layer drawObjects) {
		// エージェントまたはスポット以外は全て自らが保持しているのでチェックは必要無い筈！
		if ( _type.equals( "agent"))
			return ( null != drawObjects.get_agent_has_this_name( _value));
		else if ( _type.equals( "spot"))
			return ( null != drawObjects.get_spot_has_this_name( _value));
		else
			return true;
	}

	/**
	 * @param msg
	 * @param agentDataMap
	 * @param spotDataMap
	 * @param playerData
	 * @return
	 */
	public boolean verify(String msg, PlayerDataMap agentDataMap, PlayerDataMap spotDataMap, PlayerData playerData) {
		if ( _type.equals( "agent")) {
			if ( !agentDataMap.contains( _value) && null == LayerManager.get_instance().get_agent_has_this_name( _value)) {
				String[] message = new String[] {
					"Agent",
					"name = " + playerData._name,
					msg,
					"agent " + _value + " does not exist!"
				};

				WarningManager.get_instance().add( message);

				return false;
			}
		} else if ( _type.equals( "spot")) {
			if ( !spotDataMap.contains( _value) && null == LayerManager.get_instance().get_spot_has_this_name( _value)) {
				String[] message = new String[] {
					"Spot",
					"name = " + playerData._name,
					msg,
					"spot " + _value + " does not exist!"
				};

				WarningManager.get_instance().add( message);

				return false;
			}
		} else {
			if ( playerData.has_same_object_name( _type, _value))
				return true;

			PlayerBase playerBase;
			if ( playerData._type.equals( "agent"))
				playerBase = LayerManager.get_instance().get_agent( playerData._name);
			else
				playerBase = LayerManager.get_instance().get_spot( playerData._name);

			if ( null == playerBase || playerBase.is_multi() || !playerBase.has_same_object_name( _type, _value)) {
				String[] message = new String[] {
					( playerData._type.equals( "agent") ? "Agent" : "Spot"),
					"name = " + playerData._name,
					msg,
					_type + " " + _value + " does not exist!"
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
		if ( _type.equals( "agent")) {
			if ( !agentDataMap.contains( _value) && null == LayerManager.get_instance().get_agent_has_this_name( _value)) {
				String[] message = new String[] {
					"Agent",
					"name = " + playerData._name,
					msg,
					"agent " + _value + " does not exist!"
				};

				WarningManager.get_instance().add( message);

				return false;
			}
		} else if ( _type.equals( "spot")) {
			if ( !spotDataMap.contains( _value) && null == LayerManager.get_instance().get_spot_has_this_name( _value)) {
				String[] message = new String[] {
					"Spot",
					"name = " + playerData._name,
					msg,
					"spot " + _value + " does not exist!"
				};

				WarningManager.get_instance().add( message);

				return false;
			}
		} else {
			Vector objectRanges = playerData.get_object_ranges( _type, _value);

			PlayerBase playerBase;
			if ( playerData._type.equals( "agent"))
				playerBase = LayerManager.get_instance().get_agent( playerData._name);
			else
				playerBase = LayerManager.get_instance().get_spot( playerData._name);

			if ( null != playerBase && playerBase.is_multi()) {
				Vector ranges = playerBase.get_object_ranges( _type, _value);
				CommonTool.merge_indices( ranges, objectRanges);
			}

			if ( !CommonTool.contains_ranges( objectRanges, indices)) {
				String[] message = new String[] {
					( playerData._type.equals( "agent") ? "Agent" : "Spot"),
					"name = " + playerData._name,
					msg,
					_type + " " + _value + " does not exist!"
				};

				WarningManager.get_instance().add( message);

				return false;
			}

			return true;
		}

		return true;
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
		if ( _type.equals( "agent") && null != LayerManager.get_instance().get_agent_has_this_name( _value))
			return true;

		if ( _type.equals( "spot") && null != LayerManager.get_instance().get_spot_has_this_name( _value))
			return true;

		if ( _type.equals( "probability") || _type.equals( "keyword") || _type.equals( "number object")
			|| _type.equals( "time variable") || _type.equals( "spot variable")) {
			PropertyPageBase propertyPageBase = propertyPageMap.get( "simple variable");
			return ( ( null != propertyPageBase) && propertyPageBase.contains( _value));
		}

		if ( _type.equals( "role variable")) {
			PropertyPageBase propertyPageBase = propertyPageMap.get( "simple variable");
			return ( ( playerBase instanceof AgentObject) && ( null != propertyPageBase) && propertyPageBase.contains( _value));
		}

		if ( _type.equals( "collection") || _type.equals( "list") || _type.equals( "map") || _type.equals( "exchange algebra")) {
			PropertyPageBase propertyPageBase = propertyPageMap.get( "variable");
			return ( ( null != propertyPageBase) && ( propertyPageBase.contains( _value) || contains( _value, objectBase, objectBases)));
//			if ( null == propertyPageBase)
//				return false;
//
//			if ( propertyPageBase.contains( _value))
//				return true;
//
//			return contains( _value, propertyPageMap, objectBase, objectBases, playerBase);
		}

		if ( _type.equals( "class variable")) {
			PropertyPageBase propertyPageBase = propertyPageMap.get( "class variable");
			return ( ( null != propertyPageBase) && propertyPageBase.contains( _value));
		}

		if ( _type.equals( "file")) {
			PropertyPageBase propertyPageBase = propertyPageMap.get( "file");
			return ( ( null != propertyPageBase) && propertyPageBase.contains( _value));
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

	/**
	 * @param roleDataSet
	 * @param agentNameMap
	 * @param spotNameMap
	 * @param initialValueMap
	 * @return
	 */
	public String get_java_program_initial_value(RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, InitialValueMap initialValueMap) {
		// TODO Auto-generated method stub
		String value = initialValueMap.get_initial_value( _value);
		if ( _type.equals( "agent"))
			return "agentManager.getAgentDB().get(" + JavaProgramExporter.get_agent_name( value, agentNameMap) + ")";
		if ( _type.equals( "spot"))
			return "spotManager.getSpotDB().get(" + JavaProgramExporter.get_spot_name( value, spotNameMap) + ")";
		else
			return value;
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
