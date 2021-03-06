/*
 * Created on 2006/03/02
 */
package soars.application.visualshell.executor.monitor.menu;

import java.awt.event.ActionEvent;

import soars.application.visualshell.executor.monitor.MonitorTabbedPane;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Remove" menu.
 * @author kurata / SOARS project
 */
public class RemoveAction extends MenuAction {

	/**
	 * 
	 */
	private MonitorTabbedPane _monitorTabbedPane = null;

	/**
	 * Creates the menu handler of "Remove" menu
	 * @param name the Menu name
	 * @param monitorTabbedPane the he container of the monitor components to display the log output of the ModelBuilder
	 */
	public RemoveAction(String name, MonitorTabbedPane monitorTabbedPane) {
		super(name);
		_monitorTabbedPane = monitorTabbedPane;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_monitorTabbedPane.on_remove(actionEvent);
	}
}
