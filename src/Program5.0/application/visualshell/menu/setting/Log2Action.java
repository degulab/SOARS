/*
 * 2005/05/31
 */
package soars.application.visualshell.menu.setting;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Log2" menu.
 * @author kurata / SOARS project
 */
public class Log2Action extends MenuAction {

	/**
	 * Creates the menu handler of "Log2" menu
	 * @param name the Menu name
	 */
	public Log2Action(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_setting_log2(actionEvent);
	}
}