/**
 * 
 */
package soars.application.animator.menu.help;

import java.awt.event.ActionEvent;

import soars.application.animator.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "SOARS Animator Help" menu.
 * @author kurata / SOARS project
 */
public class ContentsAction extends MenuAction {

	/**
	 * Creates the menu handler of "SOARS Animator Help" menu
	 * @param name the Menu name
	 */
	public ContentsAction(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_help_contents(actionEvent);
	}
}
