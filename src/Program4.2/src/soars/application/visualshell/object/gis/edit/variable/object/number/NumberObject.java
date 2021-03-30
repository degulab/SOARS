/*
 * 2005/05/20
 */
package soars.application.visualshell.object.gis.edit.variable.object.number;

import java.util.List;

import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.edit.variable.object.base.SimpleVariableObject;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class NumberObject extends SimpleVariableObject {

	/**
	 * 
	 */
	public String _type = "";	// "integer" or "real number"

	/**
	 * 
	 */
	public NumberObject() {
		super("number object");
	}

	/**
	 * @param name
	 * @param type
	 */
	public NumberObject(String name, String type) {
		super("number object", name);
		_type = type;
	}

	/**
	 * @param name
	 * @param type
	 * @param fields
	 */
	public NumberObject(String name, String type, List<Field> fields) {
		super("number object", name, fields);
		_type = type;
	}

	/**
	 * @param name
	 * @param type
	 * @param fields
	 * @param comment
	 */
	public NumberObject(String name, String type, List<Field> fields, String comment) {
		super("number object", name, fields, comment);
		_type = type;
	}

	/**
	 * @param numberObject
	 */
	public NumberObject(NumberObject numberObject) {
		super(numberObject);
		_type = numberObject._type;
	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.player.base.object.base.SimpleVariableObject#copy(soars.application.visualshell.object.player.base.object.base.ObjectBase)
//	 */
//	public void copy(ObjectBase objectBase) {
//		super.copy(objectBase);
//		NumberObject numberObject = ( NumberObject)objectBase;
//		_type = numberObject._type;
//	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if ( !( object instanceof NumberObject))
			return false;

		if ( !super.equals( object))
			return false;

		NumberObject numberObject = ( NumberObject)object;

		return _type.equals( numberObject._type);
	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#can_paste(soars.application.visualshell.object.player.base.PlayerBase, soars.application.visualshell.layer.Layer)
//	 */
//	public boolean can_paste(PlayerBase playerBase, Layer drawObjects) {
//		if ( !drawObjects.is_number_object_correct(
//			( ( playerBase instanceof AgentObject) ? "agent" : "spot"),
//			_name, _type, playerBase)) {
//			String[] message = new String[] {
//				( ( playerBase instanceof AgentObject) ? "Agent" : "Spot"),
//				"name = " + playerBase._name,
//				"Illegal number object = " + _name
//			};
//
//			WarningManager.get_instance().add( message);
//
//			return false;
//		}
//		return true;
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#get_initial_data()
//	 */
//	public String get_initial_data() {
//		if ( _type.equals( "integer"))
//			return get_initial_data( ResourceManager.get_instance().get( "initial.data.integer.number.object"));
//		else if ( _type.equals( "real number"))
//			return get_initial_data( ResourceManager.get_instance().get( "initial.data.real.number.object"));
//		else
//			return "";
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#get_script(java.lang.String, java.nio.IntBuffer, soars.application.visualshell.object.experiment.InitialValueMap)
//	 */
//	public String get_script(String prefix, IntBuffer counter, InitialValueMap initialValueMap) {
//		String script = "";
//		if ( _type.equals( "integer"))
//			script = ( "\t" + prefix + "setEquip " + _name + "=util.IntValue");
//		else if ( _type.equals( "real number"))
//			script = ( "\t" + prefix + "setEquip " + _name + "=util.DoubleValue");
//		else
//			return "";
//
//		script += ( " ; " + prefix + "logEquip " + _name);
//
//		String initial_value = ( null == initialValueMap) ? _initialValue : initialValueMap.get_initial_value( _initialValue);
//		if ( null != initial_value && !initial_value.equals( ""))
//			script += ( " ; " + prefix + "askEquip " + _name + "=" + initial_value);
//
//		counter.put( 0, counter.get( 0) + 1);
//
//		return script;
//	}

	/**
	 * @param type
	 * @return
	 */
	public static String get_type_name(String type) {
		if ( type.equals( "integer"))
			return ResourceManager.get_instance().get( "number.object.integer");
		else if ( type.equals( "real number"))
			return ResourceManager.get_instance().get( "number.object.real.number");

		return "";
	}

	/**
	 * @param typeName
	 * @return
	 */
	public static String get_type(String typeName) {
		if ( typeName.equals( ResourceManager.get_instance().get( "number.object.integer")))
			return "integer";
		else if ( typeName.equals( ResourceManager.get_instance().get( "number.object.real.number")))
			return "real number";

		return "";
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.variable.object.base.ObjectBase#write(org.xml.sax.helpers.AttributesImpl)
	 */
	protected boolean write(AttributesImpl attributesImpl) {
		// TODO Auto-generated method stub
		if (!super.write(attributesImpl))
			return false;

		attributesImpl.addAttribute( null, null, "type", "", Writer.escapeAttributeCharData( _type));
		return true;
	}

//	/**
//	 * @param value
//	 * @param type
//	 * @return
//	 */
//	public static String is_correct(String value, String type) {
//		if ( value.matches( "\\$.+"))
//			return value;
//
//		if ( type.equals( "integer")) {
//			int n;
//			try {
//				n = Integer.parseInt( value);
//			} catch (NumberFormatException e) {
//				//e.printStackTrace();
//				return null;
//			}
//			return String.valueOf( n);
//		} else if ( type.equals( "real number")) {
//			if ( 0 > value.indexOf( '/')) {
//				double d;
//				try {
//					d = Double.parseDouble( value);
//				} catch (NumberFormatException e) {
//					//e.printStackTrace();
//					return null;
//				}
//				return Tool.format_real_number_text( value);
//				//return value;
//				//return String.valueOf( d);
//			} else {
//				String[] elements = value.split( "/");
//				if ( 2 != elements.length)
//					return null;
//
//				if ( elements[ 0].equals( "") || elements[ 1].equals( ""))
//					return null;
//
//				double numerator;
//				try {
//					numerator = Double.parseDouble( elements[ 0]);
//				} catch (NumberFormatException e) {
//					//e.printStackTrace();
//					return null;
//				}
//
//				double denominator;
//				try {
//					denominator = Double.parseDouble( elements[ 1]);
//				} catch (NumberFormatException e) {
//					//e.printStackTrace();
//					return null;
//				}
//
//				return ( Tool.format_real_number_text_not_always_decimal( elements[ 0])
//					+ "/" + Tool.format_real_number_text_not_always_decimal( elements[ 1]));
//				//return value;
//			}
//		}
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#write(soars.common.utility.xml.sax.Writer)
//	 */
//	public void write(Writer writer) throws SAXException {
//		AttributesImpl attributesImpl = new AttributesImpl();
//		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
//		attributesImpl.addAttribute( null, null, "type", "", Writer.escapeAttributeCharData( _type));
//		attributesImpl.addAttribute( null, null, "initial_value", "", Writer.escapeAttributeCharData( _initialValue));
//		write( writer, attributesImpl);
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#write(soars.common.utility.xml.sax.Writer, java.lang.String)
//	 */
//	public void write(Writer writer, String number) throws SAXException {
//		AttributesImpl attributesImpl = new AttributesImpl();
//		attributesImpl.addAttribute( null, null, "number", "", Writer.escapeAttributeCharData( number));
//		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
//		attributesImpl.addAttribute( null, null, "type", "", Writer.escapeAttributeCharData( _type));
//		attributesImpl.addAttribute( null, null, "initial_value", "", Writer.escapeAttributeCharData( _initialValue));
//		write( writer, attributesImpl);
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.player.base.object.base.SimpleVariableObject#print()
//	 */
//	public void print() {
//		System.out.println( "\t" + _name + ", " + _type + ", " + _initialValue);
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.player.base.object.base.SimpleVariableObject#print(java.util.Vector)
//	 */
//	public void print(Vector indices) {
//		if ( indices.isEmpty()) {
//			System.out.println( "\t" + _name + ", " + _type + ", " + _initialValue + ", " + _comment);
//			return;
//		}
//
//		for ( int i = 0; i < indices.size(); ++i) {
//			int[] range = ( int[])indices.get( i);
//			System.out.println( "\t" + _name + ", " + _type + ", " + _initialValue + ", " + range[ 0] + "-" + range[ 1] + ", " + _comment);
//		}
//	}
}
