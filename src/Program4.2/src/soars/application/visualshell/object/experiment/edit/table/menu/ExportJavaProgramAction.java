/**
 * 
 */
package soars.application.visualshell.object.experiment.edit.table.menu;

import java.awt.event.ActionEvent;

import soars.application.visualshell.object.experiment.edit.table.ExperimentRowHeaderTable;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Export java program" menu.
 * @author kurata / SOARS project
 */
public class ExportJavaProgramAction extends MenuAction {

	/**
	 * 
	 */
	private ExperimentRowHeaderTable _experimentRowHeaderTable = null;

	/**
	 * Creates the menu handler of "Export java program" menu
	 * @param name the Menu name
	 * @param experimentRowHeaderTable the table component to edit the experiment data
	 */
	public ExportJavaProgramAction(String name, ExperimentRowHeaderTable experimentRowHeaderTable) {
		super(name);
		_experimentRowHeaderTable = experimentRowHeaderTable;
	}

	@Override
	public void selected(ActionEvent actionEvent) {
		_experimentRowHeaderTable.on_export_java_program( actionEvent);
	}
}
