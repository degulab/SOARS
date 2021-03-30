/**
 * 
 */
package soars.application.visualshell.object.gis.edit.variable.object.keyword;

import java.util.List;

import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.edit.variable.object.base.SimpleVariableObject;

/**
 * The keyword object class
 * @author kurata / SOARS project
 */
public class KeywordObject extends SimpleVariableObject {

	/**
	 * Creates this object.
	 */
	public KeywordObject() {
		super("keyword");
	}

	/**
	 * Creates this object.
	 * @param name the specified name
	 */
	public KeywordObject(String name) {
		super("keyword", name);
	}

	/**
	 * Creates this object with the spcified name and initial value.
	 * @param name the specified name
	 * @param fields the specified initial value
	 */
	public KeywordObject(String name, List<Field> fields) {
		super("keyword", name, fields);
	}

	/**
	 * Creates this object with the spcified data.
	 * @param name the specified name
	 * @param fields the specified initial value
	 * @param comment the specified comment
	 */
	public KeywordObject(String name, List<Field> fields, String comment) {
		super("keyword",name, fields, comment);
	}

	/**
	 * Creates this object with the spcified data.
	 * @param keywordObject the spcified data
	 */
	public KeywordObject(KeywordObject keywordObject) {
		super(keywordObject);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if ( !( object instanceof KeywordObject))
			return false;

		return super.equals( object);
	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#get_initial_data()
//	 */
//	public String get_initial_data() {
//		return get_initial_data( ResourceManager.get( "initial.data.keyword"));
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#get_script(java.lang.String, java.nio.IntBuffer, soars.application.visualshell.object.experiment.InitialValueMap)
//	 */
//	public String get_script(String prefix, IntBuffer counter, InitialValueMap initialValueMap) {
//		String script = ( "\t" + prefix + "keyword " + _name);
//
//		String initial_value = ( ( null == initialValueMap) ? _initialValue : initialValueMap.get_initial_value( _initialValue));
//		if ( null != initial_value && !initial_value.equals( ""))
//			script += "=" + initial_value;
//
//		counter.put( 0, counter.get( 0) + 1);
//
//		return script;
//	}
}
