/**
 * 
 */
package soars.application.manager.model.menu.run;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 *
 */
public class StartLibraryManagerAction extends MenuAction {

	/**
	 * 
	 */
	private IRunMenuHandler _runMenuHandler = null;

	/**
	 * @param name
	 * @param runMenuHandler
	 */
	public StartLibraryManagerAction(String name, IRunMenuHandler runMenuHandler) {
		super(name);
		_runMenuHandler = runMenuHandler;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_runMenuHandler.on_run_start_library_manager(actionEvent);
	}
}