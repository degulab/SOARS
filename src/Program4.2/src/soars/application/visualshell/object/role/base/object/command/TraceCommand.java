/**
 * 
 */
package soars.application.visualshell.object.role.base.object.command;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;

/**
 * @author kurata
 *
 */
public class TraceCommand extends Rule {

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public TraceCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected String get_script(String value, Role role) {
		return "trace";
	}
}
