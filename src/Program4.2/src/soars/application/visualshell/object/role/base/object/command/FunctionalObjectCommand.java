/**
 * 
 */
package soars.application.visualshell.object.role.base.object.command;

import java.util.List;

import soars.application.visualshell.file.exporter.java.object.Variable;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.common.functional_object.FunctionalObjectRule;

/**
 * @author kurata
 *
 */
public class FunctionalObjectCommand extends FunctionalObjectRule {

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public FunctionalObjectCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected String get_script(String value, Role role) {
		return get_script( value, role, " ; ");
	}

	@Override
	protected void get_variable(List<Variable> variables, String value, Role role) {
		// TODO Auto-generated method stub
		super.get_variable(variables, value, role, " ; ");
	}
}
