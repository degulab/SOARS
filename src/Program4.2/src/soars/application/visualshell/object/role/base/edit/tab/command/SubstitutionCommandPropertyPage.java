/*
 * Created on 2005/10/31
 */
package soars.application.visualshell.object.role.base.edit.tab.command;

import java.awt.Color;
import java.awt.Frame;

import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.common.number.NumberObjectPropertyPageBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.command.SubstitutionCommand;
import soars.application.visualshell.object.role.base.object.common.number.ExpressionElements;
import soars.application.visualshell.object.role.base.object.common.number.SubType;


/**
 * @author kurata
 */
public class SubstitutionCommandPropertyPage extends NumberObjectPropertyPageBase {

	/**
	 * 
	 */
	protected JLabel _equal_label = null;

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
	public SubstitutionCommandPropertyPage(String title, String kind,
		String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPageBase#setup_number_object_and_operator(javax.swing.JPanel)
	 */
	protected void setup_number_object_and_operator(JPanel parent) {
		_equal_label = create_label( " = ", false);
		setup_number_object_and_operator( _equal_label, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#set(soars.application.visualshell.object.role.base.rule.base.Rule)
	 */
	public boolean set(Rule rule) {
		initialize();

		if ( null == rule || !_type.equals( rule._type) || rule._value.equals( "")) {
			set_handler();
			return false;
		}

		SubType subType = SubType.get( rule._value, SubstitutionCommand._sub_types);
		if ( null == subType) {
			set_handler();
			return false;
		}

		if ( !set( rule._value, subType)) {
			set_handler();
			return false;
		}

		set_handler();

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPageBase#set_value1(java.lang.String, int)
	 */
	protected boolean set_value1(String value, int index) {
		String[] elements = value.split( " ");
		if ( null == elements || 2 > elements.length)
			return false;

		if ( !set_number_object( elements[ 0], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox, _number_object_comboBox))
			return false;

		_value_textFields[ index].setText( elements[ 1]);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPageBase#set_number_object1(java.lang.String, int)
	 */
	protected boolean set_number_object1(String value, int index) {
		String[] elements = value.split( " ");
		if ( null == elements || 2 > elements.length)
			return false;

		if ( !set_number_object( elements[ 0], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox, _number_object_comboBox))
			return false;

		_number_object_comboBoxes[ index].setSelectedItem( elements[ 1]);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPageBase#set_expression1(java.lang.String, int)
	 */
	protected boolean set_expression1(String value, int index) {
		String[] elements = value.split( " ");
		if ( null == elements || 2 > elements.length)
			return false;

		ExpressionElements expressionElements = SubstitutionCommand.get1( elements);
		if ( null == expressionElements)
			return false;

		if ( !set_number_object( elements[ 0], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox, _number_object_comboBox))
			return false;

		set_expression( expressionElements, index);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPageBase#set_others1(java.lang.String, int)
	 */
	protected boolean set_others1(String value, int index) {
		String[] elements = value.split( " ");
		if ( null == elements || 2 > elements.length)
			return false;

		if ( !set_number_object( elements[ 0], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox, _number_object_comboBox))
			return false;
	
		_others_textFields[ index].setText( value.substring( ( elements[ 0] + " ").length()));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String number_object1 = get_number_object( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox, _number_object_comboBox);
		if ( null == number_object1)
			return null;

		return get( number_object1, SubstitutionCommand._sub_types, "");
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		String number_object1 = get_number_object( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox, _number_object_comboBox);
//		if ( null == number_object1)
//			return false;
//
//		return get( rule, number_object1, SubstitutionCommandRuleManipulator._sub_types, "");
//	}
}
