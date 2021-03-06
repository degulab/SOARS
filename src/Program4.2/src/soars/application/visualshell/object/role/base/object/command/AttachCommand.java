/**
 * 
 */
package soars.application.visualshell.object.role.base.object.command;

import soars.application.visualshell.object.role.base.object.command.base.AttachAndDetachCommand;

/**
 * @author kurata
 *
 */
public class AttachCommand extends AttachAndDetachCommand {

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public AttachCommand(String kind, String type, String value) {
		super(kind, type, value);
	}
}
