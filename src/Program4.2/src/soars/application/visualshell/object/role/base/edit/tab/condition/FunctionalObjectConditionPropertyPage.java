/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.condition;

import java.awt.Color;
import java.awt.Frame;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.common.functional_object.FunctionalObjectPropertyPageBase;

/**
 * @author kurata
 *
 */
public class FunctionalObjectConditionPropertyPage extends FunctionalObjectPropertyPageBase {

	/**
	 * @param title
	 * @param kind
	 * @param type
	 * @param color
	 * @param role
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public FunctionalObjectConditionPropertyPage(String title, String kind,
		String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}
}
