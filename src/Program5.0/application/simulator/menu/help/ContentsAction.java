/**
 * 
 */
package soars.application.simulator.menu.help;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 *
 */
public class ContentsAction extends MenuAction {

	/**
	 * 
	 */
	private IHelpMenuHandler _helpMenuHandler = null;

	/**
	 * @param name
	 * @param helpMenuHandler
	 */
	public ContentsAction(String name, IHelpMenuHandler helpMenuHandler) {
		super(name);
		_helpMenuHandler = helpMenuHandler;
	}

	/**
	 * @param name
	 * @param item
	 */
	public ContentsAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_helpMenuHandler.on_help_contents(actionEvent);
	}
}