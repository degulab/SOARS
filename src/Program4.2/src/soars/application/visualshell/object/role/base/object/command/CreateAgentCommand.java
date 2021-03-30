/**
 * 
 */
package soars.application.visualshell.object.role.base.object.command;

import java.util.Vector;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.common.soars.tool.SoarsCommonTool;

/**
 * @author kurata
 *
 */
public class CreateAgentCommand extends Rule {

	/**
	 * 
	 */
	static public String _reserved_word = "createAgent ";

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public CreateAgentCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#has_same_agent_name(java.lang.String, java.lang.String)
	 */
	public boolean has_same_agent_name(String name, String number) {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || ( 2 != elements.length && 3 != elements.length))
			return false;

		return SoarsCommonTool.has_same_name( elements[ 0], elements[ 1], name, number);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_names()
	 */
	protected String[] get_used_spot_names() {
		return new String[] { CommonRuleManipulator.extract_spot_name1( _value)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	protected String[] get_used_spot_variable_names(Role role) {
		return new String[] { CommonRuleManipulator.get_spot_variable_name2( _value)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_role_names()
	 */
	protected String[] get_used_role_names() {
		return new String[] { get_used_role_name()};
	}

	/**
	 * @return
	 */
	private String get_used_role_name() {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 3 != elements.length)
			return null;

		return elements[ 2];
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_spot_name_and_number(String new_name, String original_name, String head_name, Vector ranges, String new_head_name, Vector new_ranges) {
		String value = CommonRuleManipulator.update_spot_name2( _value, new_name, original_name, head_name, ranges, new_head_name, new_ranges);
		if ( null == value)
			return false;

		_value = value;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_spot_variable_name(String name, String new_name, String type, Role role) {
		String value = CommonRuleManipulator.update_spot_variable_name2( _value, name, new_name, type);
		if ( null == value)
			return false;

		_value = value;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_role_name(java.lang.String, java.lang.String)
	 */
	public boolean update_role_name(String original_name, String name) {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 3 != elements.length)
			return false;

		if ( !elements[ 2].equals( original_name))
			return false;

		elements[ 2] = name;

		String[] parts = _value.split( " ");
		if ( null == parts || 2 != parts.length)
			return false;

		_value = ( parts[ 0] + " ");

		int length = ( name.equals( "") ? 2 : 3);

		for ( int i = 0; i < length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	protected boolean can_paste(Role role, Layer drawObjects) {
		if ( !CommonRuleManipulator.can_paste_spot_and_spot_variable_name1( _value, drawObjects))
			return false;

		if ( !can_paste_role_name( drawObjects))
			return false;

		return true;
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_role_name(Layer drawObjects) {
		String agent_role_name = get_used_role_name();
		if ( null == agent_role_name)
			return true;

		return drawObjects.is_agent_role_name( agent_role_name);
	}
}
