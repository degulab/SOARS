/**
 * 
 */
package soars.application.visualshell.object.player.base.object.keyword;

import java.nio.IntBuffer;
import java.util.Map;

import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.player.base.object.base.SimpleVariableObject;

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
	 * Creates this object with the spcified name and initial value.
	 * @param name the specified name
	 * @param initialValue the specified initial value
	 */
	public KeywordObject(String name, String initialValue) {
		super("keyword", name, initialValue);
	}

	/**
	 * Creates this object with the spcified data.
	 * @param name the specified name
	 * @param initialValue the specified initial value
	 * @param comment the specified comment
	 */
	public KeywordObject(String name, String initialValue, String comment) {
		super("keyword",name, initialValue, comment);
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

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#get_initial_data()
	 */
	public String get_initial_data() {
		return get_initial_data( ResourceManager.get_instance().get( "initial.data.keyword"));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.object.base.ObjectBase#get_script(java.lang.String, java.nio.IntBuffer, soars.application.visualshell.object.experiment.InitialValueMap)
	 */
	public String get_script(String prefix, IntBuffer counter, InitialValueMap initialValueMap) {
		String script = ( "\t" + prefix + "keyword " + _name);

		String initial_value = ( ( null == initialValueMap) ? _initialValue : initialValueMap.get_initial_value( _initialValue));
		if ( null != initial_value && !initial_value.equals( ""))
			script += "=" + initial_value;

		counter.put( 0, counter.get( 0) + 1);

		return script;
	}

	@Override
	public String get_java_program_initial_value(RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, InitialValueMap initialValueMap) {
		// TODO Auto-generated method stub
		return ( "\"" + initialValueMap.get_initial_value( _initialValue) + "\"");
	}
}
