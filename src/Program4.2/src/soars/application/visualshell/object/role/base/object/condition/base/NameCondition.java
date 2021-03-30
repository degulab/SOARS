/**
 * 
 */
package soars.application.visualshell.object.role.base.object.condition.base;

import java.util.Vector;

import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;

/**
 * @author kurata
 *
 */
public class NameCondition extends Rule {

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public NameCondition(String kind, String type, String value) {
		super(kind, type, value);
	}

	/**
	 * @return
	 */
	protected String get_name() {
		String name;
		if ( !_value.startsWith( "!"))
			name = _value;
		else {
			if ( _value.equals( "!"))
				name = "";
			else
				name = _value.substring( "!".length());
		}
		return name;
	}

	/**
	 * @param new_name
	 * @param original_name
	 * @param head_name
	 * @param ranges
	 * @param new_head_name
	 * @param new_ranges
	 * @return
	 */
	protected boolean update_name(String new_name,
		String original_name, String head_name, Vector ranges, String new_head_name, Vector new_ranges) {
		String value = ( _value.startsWith( "!") ? _value.substring( "!".length()) : _value);
		value = CommonRuleManipulator.get_new_agent_or_spot_name( value, new_name, original_name, head_name, ranges, new_head_name, new_ranges);
		if ( null == value)
			return false;

		_value = ( ( _value.startsWith( "!") ? "!" : "") + value);

		return true;
	}
}
