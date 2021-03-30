/**
 * 
 */
package soars.application.visualshell.object.arbitrary;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.plugin.Plugin;

/**
 * The Plugin class for the java method call.
 * @author kurata / SOARS project
 */
public class JavaMethodCallPlugin extends Plugin {

	/**
	 * Flag which indicates whether to enable the java method call.
	 */
	public boolean _originalEnable = Environment._enableJavaMethodCallDefaultValue.equals( "true");

	/**
	 * Creates the instance of this class.
	 */
	public JavaMethodCallPlugin() {
		super();
		_enable = _originalEnable
			= Environment.get_instance().get( Environment._enableJavaMethodCallKey, Environment._enableJavaMethodCallDefaultValue).equals( "true");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.plugin.Plugin#getName()
	 */
	public String getName() {
		return ResourceManager.get_instance().get( "edit.plugin.dialog.plugin.table.java.method.call.name");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.plugin.Plugin#getVersion()
	 */
	public String getVersion() {
		return Constant._applicationVersion;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.plugin.Plugin#get_comment()
	 */
	public String get_comment() {
		return ResourceManager.get_instance().get( "edit.plugin.dialog.plugin.table.java.method.call.comment");
	}
}
