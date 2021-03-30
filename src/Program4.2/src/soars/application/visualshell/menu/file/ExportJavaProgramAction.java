/**
 * 
 */
package soars.application.visualshell.menu.file;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Export java program" menu.
 * @author kurata / SOARS project
 */
public class ExportJavaProgramAction extends MenuAction {

	/**
	 * Creates the menu handler of "Export java program" menu
	 * @param name the Menu name
	 */
	public ExportJavaProgramAction(String name) {
		super(name);
	}

	@Override
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_export_java_program(actionEvent);
	}
}
