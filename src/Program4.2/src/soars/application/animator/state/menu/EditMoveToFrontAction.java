/**
 * 
 */
package soars.application.animator.state.menu;

import java.awt.event.ActionEvent;

import soars.application.animator.state.EditState;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Move to front" menu.
 * @author kurata / SOARS project
 */
public class EditMoveToFrontAction extends MenuAction {

	/**
	 * 
	 */
	private EditState _editState = null;

	/**
	 * Creates the menu handler of "Move to front" menu
	 * @param name the Menu name
	 * @param editState the instance of the EditState class
	 */
	public EditMoveToFrontAction(String name, EditState editState) {
		super(name);
		_editState = editState;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_editState.on_edit_move_to_front(actionEvent);
	}
}
