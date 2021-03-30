/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.java_method_call;

import java.awt.Frame;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Variable;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;

/**
 * @author kurata
 *
 */
public class EditArgumentVariableDlg extends EditVariableDlg {

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param type
	 * @param entityVariableRule
	 * @param object
	 * @param property
	 * @param role
	 * @param rulePropertyPanelBase
	 */
	public EditArgumentVariableDlg(Frame arg0, String arg1, boolean arg2, String type, EntityVariableRule entityVariableRule, Subject object, Property property, Role role, RulePropertyPanelBase rulePropertyPanelBase) {
		super(arg0, arg1, arg2, type, entityVariableRule,object, property, role, rulePropertyPanelBase);
	}

	@Override
	protected void setup_object(Subject object) {
		super.setup_object(object);

		if ( _type.equals( "java.lang.String"))
			_object.add( new Variable( "immediate keyword",
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.literal")));
		else if ( _type.equals( "int")
			|| _type.equals( "byte")
			|| _type.equals( "short")
			|| _type.equals( "long")) {
			_object.add( new Variable( "integer number object",
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.integer.number.object")));
			_object.add( new Variable( "immediate integer",
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.immediate.integer.number")));
		} else if ( _type.equals( "double")
			|| _type.equals( "float"))
			_object.add( new Variable( "immediate real number",
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.immediate.real.number")));
	}

	@Override
	protected String get_type_name() {
		return ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.parameter.table.header.parameter");
	}
}
