/**
 * 
 */
package soars.common.utility.swing.file.manager.menu;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.file.manager.table.FileTableBase;
import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 *
 */
public class ConvertEncodingAction extends MenuAction {

	/**
	 * 
	 */
	private FileTableBase _fileTableBase = null;

	/**
	 * @param name
	 * @param fileTableBase
	 */
	public ConvertEncodingAction(String name, FileTableBase fileTableBase) {
		super(name);
		_fileTableBase = fileTableBase;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_fileTableBase.on_convert_encoding( actionEvent);
	}
}
