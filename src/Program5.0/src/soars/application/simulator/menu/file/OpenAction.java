/**
 * 
 */
package soars.application.simulator.menu.file;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 *
 */
public class OpenAction extends MenuAction {

	/**
	 * 
	 */
	private IFileMenuHandler _fileMenuHandler = null;

	/**
	 * @param name
	 * @param fileMenuHandler
	 */
	public OpenAction(String name, IFileMenuHandler fileMenuHandler) {
		super(name);
		_fileMenuHandler = fileMenuHandler;
	}

	/**
	 * @param name
	 * @param item
	 */
	public OpenAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_fileMenuHandler.on_file_open(actionEvent);
	}
}
