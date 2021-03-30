/*
 * 2005/03/15
 */
package soars.application.animator.menu.edit;

import java.awt.event.ActionEvent;

import soars.application.animator.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Edit spot" menu.
 * @author kurata / SOARS project
 */
public class SpotAction extends MenuAction {

	/**
	 * Creates the menu handler of "Edit spot" menu
	 * @param name the Menu name
	 */
	public SpotAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_edit_spot(actionEvent);
	}
}