/**
 * 
 */
package soars.application.manager.model.menu.edit;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 *
 */
public class ImportUserDefinedRuleAction extends MenuAction {

	/**
	 * 
	 */
	private IEditMenuHandler _editMenuHandler = null;

	/**
	 * @param name
	 * @param editMenuHandler
	 */
	public ImportUserDefinedRuleAction(String name, IEditMenuHandler editMenuHandler) {
		super(name);
		_editMenuHandler = editMenuHandler;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	@Override
	public void selected(ActionEvent actionEvent) {
		_editMenuHandler.on_edit_import_user_defined_rule(actionEvent);
	}
}
