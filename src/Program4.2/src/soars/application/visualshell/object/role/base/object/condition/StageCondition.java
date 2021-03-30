/**
 * 
 */
package soars.application.visualshell.object.role.base.object.condition;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.stage.Stage;
import soars.application.visualshell.object.stage.StageManager;

/**
 * @author kurata
 *
 */
public class StageCondition extends Rule {

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public StageCondition(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_stage_names()
	 */
	protected String[] get_used_stage_names() {
		return new String[] { _value};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_stage_name(java.lang.String, java.lang.String)
	 */
	public boolean update_stage_name(String new_name, String original_name) {
		if ( _value.equals( original_name)) {
			_value = new_name;
			return true;
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_stage_manager()
	 */
	public boolean update_stage_manager() {
		if ( _value.equals( ""))
			return false;

		StageManager.get_instance().append_main_stage( _value);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected String get_script(String value, Role role) {
		Stage stage = StageManager.get_instance().get( value);
		if ( null == stage)
			return "";

		return ( stage._name + ( stage._random ? "*" : ""));
	}
}
