/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab.variable.panel.set_and_list.list;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.util.Map;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.edit.tab.variable.VariableTable;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.set_and_list.base.panel.VariablePanel;

/**
 * @author kurata
 *
 */
public class ListPanel extends VariablePanel {

	/**
	 * @param playerBase
	 * @param propertyPageMap
	 * @param variableTable
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public ListPanel(PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap, VariableTable variableTable, Color color, Frame owner, Component parent) {
		super("list", playerBase, propertyPageMap, variableTable, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.VariablePanelBase#get_name_label_text()
	 */
	protected String get_name_label_text() {
		return ResourceManager.get_instance().get( "edit.object.dialog.name");
	}
}
