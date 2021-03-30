/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.java_method_call;

import java.awt.Component;
import java.awt.Frame;
import java.util.List;
import java.util.Map;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;

/**
 * @author kurata
 *
 */
public class MethodConditionPanel extends MethodPanel {

	/**
	 * @param object
	 * @param property
	 * @param role
	 * @param buddiesMap
	 * @param rulePropertyPanelBase
	 * @param owner
	 * @param parent
	 */
	public MethodConditionPanel(Subject object, Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, RulePropertyPanelBase rulePropertyPanelBase, Frame owner, Component parent) {
		super(object, property, role, buddiesMap, rulePropertyPanelBase, owner, parent);
	}
}
