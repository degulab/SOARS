/**
 * 
 */
package soars.application.visualshell.object.player.base.object.map;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.importer.initial.player.PlayerData;
import soars.application.visualshell.file.importer.initial.player.PlayerDataMap;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.player.agent.AgentObject;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.object.base.ObjectBase;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class MapObject extends ObjectBase {

	/**
	 * 
	 */
	public List<MapInitialValue> _mapInitialValues = new ArrayList<MapInitialValue>();

	/**
	 * 
	 */
	public MapObject() {
		super("map");
	}

	/**
	 * @param name
	 */
	public MapObject(String name) {
		super("map", name);
	}

	/**
	 * @param mapObject
	 */
	public MapObject(MapObject mapObject) {
		super(mapObject);
		for ( MapInitialValue mapInitialValue:mapObject._mapInitialValues)
			_mapInitialValues.add( new MapInitialValue( mapInitialValue));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#copy(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public void copy(ObjectBase objectBase) {
		super.copy(objectBase);
		MapObject mapObject = ( MapObject)objectBase;
		for ( MapInitialValue mapInitialValue:mapObject._mapInitialValues)
			_mapInitialValues.add( new MapInitialValue( mapInitialValue));
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if ( !( object instanceof MapObject))
			return false;

		if ( !super.equals( object))
			return false;

		MapObject mapObject = ( MapObject)object;

		if ( mapObject._mapInitialValues.size() != _mapInitialValues.size())
			return false;

		for ( int i = 0; i < mapObject._mapInitialValues.size(); ++i) {
			if ( !mapObject._mapInitialValues.get( i).equals( _mapInitialValues.get( i)))
				return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#contains(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public boolean contains(ObjectBase objectBase) {
		for ( MapInitialValue mapInitialValue:_mapInitialValues) {
			if ( mapInitialValue.contains( objectBase))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#contains(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, soars.application.visualshell.object.player.base.PlayerBase)
	 */
	public boolean contains(String kind, String type, String headName, Vector ranges, String name, PlayerBase playerBase) {
		for ( MapInitialValue mapInitialValue:_mapInitialValues) {
			if ( mapInitialValue.contains( name, type, headName, ranges, playerBase))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#contains(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector, soars.application.visualshell.object.player.base.PlayerBase)
	 */
	public boolean contains(String kind, String name, String type, String headName, Vector ranges, String newHeadName, Vector newRanges, PlayerBase playerBase) {
		for ( MapInitialValue mapInitialValue:_mapInitialValues) {
			if ( mapInitialValue.contains( name, type, headName, ranges, newHeadName, newRanges, playerBase))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#can_adjust_name(java.lang.String, java.lang.String, java.util.Vector, soars.application.visualshell.object.player.base.PlayerBase)
	 */
	public boolean can_adjust_name(String type, String headName, Vector ranges, PlayerBase playerBase) {
		for ( MapInitialValue mapInitialValue:_mapInitialValues) {
			if ( !mapInitialValue.can_adjust_name( _name, type, headName, ranges, playerBase))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#can_adjust_name(java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector, soars.application.visualshell.object.player.base.PlayerBase)
	 */
	public boolean can_adjust_name(String type, String headName, Vector ranges, String newHeadName, Vector newRanges, PlayerBase playerBase) {
		for ( MapInitialValue mapInitialValue:_mapInitialValues) {
			if ( !mapInitialValue.can_adjust_name( _name, type, headName, ranges, newHeadName, newRanges, playerBase))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#update_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_name_and_number(String type, String newName, String originalName, String headName, Vector ranges, String newHeadName, Vector newRanges) {
		boolean result = false;
		for ( MapInitialValue mapInitialValue:_mapInitialValues) {
			if ( mapInitialValue.update_name_and_number( type, newName, originalName, headName, ranges, newHeadName, newRanges))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#update_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean update_object_name(String type, String name, String newName) {
		boolean result = false;
		for ( MapInitialValue mapInitialValue:_mapInitialValues) {
			if ( mapInitialValue.update_object_name( type, name, newName))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#contains_this_alias(java.lang.String)
	 */
	public boolean contains_this_alias(String alias) {
		for ( MapInitialValue mapInitialValue:_mapInitialValues) {
			if ( mapInitialValue.contains_this_alias( alias))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#get_initial_values(java.util.Vector)
	 */
	public void get_initial_values1(Vector initialValues) {
		for ( MapInitialValue mapInitialValue:_mapInitialValues)
			mapInitialValue.get_initial_values1( initialValues);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#get_initial_values2(java.util.Vector)
	 */
	public void get_initial_values2(Vector initialValues) {
		for ( MapInitialValue mapInitialValue:_mapInitialValues)
			mapInitialValue.get_initial_values2( initialValues);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#can_paste(soars.application.visualshell.object.player.base.PlayerBase, soars.application.visualshell.layer.Layer)
	 */
	public boolean can_paste(PlayerBase playerBase, Layer drawObjects) {
		boolean result = true;
		for ( MapInitialValue mapInitialValue:_mapInitialValues) {
			if ( !mapInitialValue.can_paste( drawObjects)) {
				result = false;
				String[] message = new String[] {
					( ( playerBase instanceof AgentObject) ? "Agent" : "Spot"),
					"name = " + playerBase._name,
					"map " + _name + " has " + mapInitialValue._value[ 1]
				};

				WarningManager.get_instance().add( message);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#get_initial_data()
	 */
	public String get_initial_data() {
		String script = ( ResourceManager.get_instance().get( "initial.data.map") + "\t" + _name);
		for ( MapInitialValue mapInitialValue:_mapInitialValues)
			script += mapInitialValue.get_initial_data();
		return ( script + Constant._lineSeparator);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#get_script(java.lang.String, java.nio.IntBuffer, soars.application.visualshell.object.experiment.InitialValueMap)
	 */
	public String get_script(String prefix, IntBuffer counter, InitialValueMap initialValueMap) {
		String script = ( "\t" + prefix + "setEquip " + _name + "=java.util.HashMap ; " + prefix + "logEquip " + _name);
		counter.put( 0, counter.get( 0) + 1);
		return script;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#get_initial_values_script(java.lang.String, soars.application.visualshell.object.experiment.InitialValueMap)
	 */
	public String get_initial_values_script(String prefix, InitialValueMap initialValueMap) {
		String script = "";
		for ( MapInitialValue mapInitialValue:_mapInitialValues)
			script += ( ( script.equals( "") ? "" : " ; ") + mapInitialValue.get_script( prefix, _name, initialValueMap));
		return script;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#write(soars.common.utility.xml.sax.Writer)
	 */
	public void write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		write( writer, attributesImpl);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#write(soars.common.utility.xml.sax.Writer, java.lang.String)
	 */
	public void write(Writer writer, String number) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "number", "", Writer.escapeAttributeCharData( number));
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		write( writer, attributesImpl);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#write(soars.common.utility.xml.sax.Writer, org.xml.sax.helpers.AttributesImpl)
	 */
	protected void write(Writer writer, AttributesImpl attributesImpl) throws SAXException {
		for ( int i = 0; i < _mapInitialValues.size(); ++i)
			_mapInitialValues.get( i).write( i ,attributesImpl);

		super.write(writer, attributesImpl);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#verify(soars.application.visualshell.file.importer.initial.player.PlayerDataMap, soars.application.visualshell.file.importer.initial.player.PlayerDataMap, soars.application.visualshell.file.importer.initial.player.PlayerData)
	 */
	public boolean verify(PlayerDataMap agentDataMap, PlayerDataMap spotDataMap, PlayerData playerData) {
		for ( MapInitialValue mapInitialValue:_mapInitialValues) {
			if ( !mapInitialValue.verify( "map name = " + _name, agentDataMap, spotDataMap, playerData))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#verify(soars.application.visualshell.file.importer.initial.player.PlayerDataMap, soars.application.visualshell.file.importer.initial.player.PlayerDataMap, java.util.Vector, soars.application.visualshell.file.importer.initial.player.PlayerData)
	 */
	public boolean verify(PlayerDataMap agentDataMap, PlayerDataMap spotDataMap, Vector indices, PlayerData playerData) {
		for ( MapInitialValue mapInitialValue:_mapInitialValues) {
			if ( !mapInitialValue.verify( "map name = " + _name, agentDataMap, spotDataMap, indices, playerData))
				return false;
		}
		return true;
	}

	/**
	 * @param playerBase
	 * @return
	 */
	public boolean transform(PlayerBase playerBase) {
		for ( MapInitialValue mapInitialValue:_mapInitialValues) {
			if ( !mapInitialValue.transform( playerBase))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#print()
	 */
	public void print() {
		if ( _mapInitialValues.isEmpty()) {
			System.out.println( "\t" + _name);
			return;
		}

		for ( MapInitialValue mapInitialValue:_mapInitialValues)
			System.out.println( "\t" + _name + ", [" + mapInitialValue._key[ 0] + "]"+ mapInitialValue._key[ 1] + " - [" + mapInitialValue._value[ 0] + "]" + mapInitialValue._value[ 1]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#print(java.util.Vector)
	 */
	public void print(Vector indices) {
		String text = "";
		for ( MapInitialValue mapInitialValue:_mapInitialValues)
			text += ( ( text.equals( "") ? "" : " ") + "[" + mapInitialValue._key[ 0] + "]"+ mapInitialValue._key[ 1] + " - [" + mapInitialValue._value[ 0] + "]" + mapInitialValue._value[ 1]);

		if ( indices.isEmpty()) {
			System.out.println( "\t" + _name + ", " + text);
			return;
		}

		for ( int i = 0; i < indices.size(); ++i) {
			int[] range = ( int[])indices.get( i);
			System.out.println( "\t" + _name + ", " + text + ", " + range[ 0] + "-" + range[ 1]);
		}
	}

	/**
	 * @param propertyPageMap
	 * @param objectBase
	 * @param objectBases
	 * @param playerBase
	 * @return
	 */
	public boolean can_paste(Map<String, PropertyPageBase> propertyPageMap, ObjectBase objectBase, List<ObjectBase> objectBases, PlayerBase playerBase) {
		for ( MapInitialValue mapInitialValue:_mapInitialValues) {
			if ( !mapInitialValue.can_paste( propertyPageMap, objectBase, objectBases, playerBase))
				return false;
		}
		return true;
	}
}
