/**
 * 
 */
package soars.application.manager.sample.menu.run;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 *
 */
public class StartVisualShellAction extends MenuAction {

	/**
	 * 
	 */
	private IRunMenuHandler _runMenuHandler = null;

	/**
	 * @param name
	 * @param runMenuHandler
	 */
	public StartVisualShellAction(String name, IRunMenuHandler runMenuHandler) {
		super(name);
		_runMenuHandler = runMenuHandler;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_runMenuHandler.on_run_start_visual_shell(actionEvent);
	}
}
