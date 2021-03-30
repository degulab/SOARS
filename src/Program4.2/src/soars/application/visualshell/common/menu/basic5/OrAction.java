/*
 * Created on 2005/11/16
 */
package soars.application.visualshell.common.menu.basic5;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Or" menu.
 * @author kurata / SOARS project
 */
public class OrAction extends MenuAction {

	/**
	 * 
	 */
	private IBasicMenuHandler5 _basicMenuHandler5 = null;

	/**
	 * Creates the menu handler of "Or" menu
	 * @param name the Menu name
	 * @param basicMenuHandler5 the common menu handler interface
	 */
	public OrAction(String name, IBasicMenuHandler5 basicMenuHandler5) {
		super(name);
		_basicMenuHandler5 = basicMenuHandler5;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_basicMenuHandler5.on_or(actionEvent);
	}
}