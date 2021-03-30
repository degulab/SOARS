/**
 * 
 */
package soars.application.visualshell.object.role.base.object.condition;

import java.util.Vector;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.condition.base.NameCondition;

/**
 * @author kurata
 *
 */
public class SpotNameCondition extends NameCondition {

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public SpotNameCondition(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_names()
	 */
	protected String[] get_used_spot_names() {
		return new String[] { get_name()};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_spot_name_and_number(String new_name, String original_name, String head_name, Vector ranges, 	String new_head_name, Vector new_ranges) {
		return update_name( new_name, original_name, head_name, ranges, new_head_name, new_ranges);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	protected boolean can_paste(Role role, Layer drawObjects) {
		return ( null != drawObjects.get_spot_has_this_name( get_name()));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected String get_script(String value, Role role) {
		if ( value.startsWith( "!"))
			return ( "!<>is $Name=" + value.substring( "!".length()));
		else
			return ( "<>is $Name=" + value);
	}
}
