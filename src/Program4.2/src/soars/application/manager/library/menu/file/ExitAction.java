/*
 * 2005/01/28
 */
package soars.application.manager.library.menu.file;

import java.awt.event.ActionEvent;

import soars.application.manager.library.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Exit" menu.
 * @author kurata / SOARS project
 */
public class ExitAction extends MenuAction {

	/**
	 * Creates the menu handler of "Exit" menu
	 * @param name the Menu name
	 */
	public ExitAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_exit(actionEvent);
	}
}
