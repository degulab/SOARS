/*
 * 2005/04/01
 */
package soars.common.utility.swing.file.manager.edit.menu.file;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Close" menu.
 * @author kurata / SOARS project
 */
public class CloseAction extends MenuAction {

	/**
	 * 
	 */
	private IFileMenuHandler _fileMenuHandler = null;

	/**
	 * Creates the menu handler of "Close" menu
	 * @param name the Menu name
	 * @param name fileMenuHandler the instance of IFileMenuHandler
	 */
	public CloseAction(String name, IFileMenuHandler fileMenuHandler) {
		super(name);
		_fileMenuHandler = fileMenuHandler;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_fileMenuHandler.on_file_close(actionEvent);
	}
}
