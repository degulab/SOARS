/*
 * 2006/10/21
 */
package soars.application.visualshell.object.experiment.edit.table.menu;

import java.awt.event.ActionEvent;

import soars.application.visualshell.object.experiment.edit.table.ExperimentRowHeaderTable;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Export on" menu.
 * @author kurata / SOARS project
 */
public class ExportScriptAction extends MenuAction {

	/**
	 * 
	 */
	private ExperimentRowHeaderTable _experimentRowHeaderTable = null;

	/**
	 * Creates the menu handler of "Export on" menu
	 * @param name the Menu name
	 * @param experimentRowHeaderTable the table component to edit the experiment data
	 */
	public ExportScriptAction(String name, ExperimentRowHeaderTable experimentRowHeaderTable) {
		super(name);
		_experimentRowHeaderTable = experimentRowHeaderTable;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_experimentRowHeaderTable.on_export_script( actionEvent);
	}
}
