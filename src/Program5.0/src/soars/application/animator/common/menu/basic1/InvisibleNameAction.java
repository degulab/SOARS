/*
 * Created on 2005/11/11
 */
package soars.application.animator.common.menu.basic1;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler to make the name invisible.
 * @author kurata / SOARS project
 */
public class InvisibleNameAction extends MenuAction {

	/**
	 * Menu handler interface.
	 */
	private IBasicMenuHandler1 _basicMenuHandler1 = null;

	/**
	 * Creates the menu handler to make the name invisible.
	 * @param name the Menu name
	 * @param the menu handler interface
	 */
	public InvisibleNameAction(String name, IBasicMenuHandler1 basicMenuHandler1) {
		super(name);
		_basicMenuHandler1 = basicMenuHandler1;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_basicMenuHandler1.on_invisible_name(actionEvent);
	}
}
