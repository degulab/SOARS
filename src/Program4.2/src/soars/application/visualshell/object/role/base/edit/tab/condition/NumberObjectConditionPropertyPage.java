/*
 * Created on 2006/03/06
 */
package soars.application.visualshell.object.role.base.edit.tab.condition;

import java.awt.Color;
import java.awt.Frame;

import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.common.number.NumberObjectPropertyPageBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.number.ExpressionElements;
import soars.application.visualshell.object.role.base.object.common.number.SubType;
import soars.application.visualshell.object.role.base.object.condition.NumberObjectCondition;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 */
public class NumberObjectConditionPropertyPage extends NumberObjectPropertyPageBase {

	/**
	 * 
	 */
	protected ComboBox _operator_comboBox2 = null;

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
	public NumberObjectConditionPropertyPage(String title, String kind,
		String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPageBase#setup_comparative_operator(javax.swing.JPanel)
	 */
	protected void setup_comparative_operator(JPanel panel) {
		JLabel label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.number.object.label.comparative.operator"),
			false);
		panel.add( label);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPageBase#setup_number_object_and_operator(javax.swing.JPanel)
	 */
	protected void setup_number_object_and_operator(JPanel parent) {
		_operator_comboBox2 = create_comboBox( new String[] { ">", ">=", "==", "!=", "<=", "<"}, 50, true);
		setup_number_object_and_operator( _operator_comboBox2, parent);
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

		SubType subType = SubType.get( rule._value, NumberObjectCondition._sub_types);
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
		if ( null == elements || 3 > elements.length)
			return false;

		if ( !set_number_object( elements[ 0], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox, _number_object_comboBox))
			return false;

		_operator_comboBox2.setSelectedItem( elements[ 1]);

		_value_textFields[ index].setText( elements[ 2]);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPageBase#set_number_object1(java.lang.String, int)
	 */
	protected boolean set_number_object1(String value, int index) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 > elements.length)
			return false;

		if ( !set_number_object( elements[ 0], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox, _number_object_comboBox))
			return false;

		_operator_comboBox2.setSelectedItem( elements[ 1]);

		_number_object_comboBoxes[ index].setSelectedItem( elements[ 2]);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPageBase#set_expression1(java.lang.String, int)
	 */
	protected boolean set_expression1(String value, int index) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 > elements.length)
			return false;

		ExpressionElements expressionElements = NumberObjectCondition.get1( elements);
		if ( null == expressionElements)
			return false;

		if ( !set_number_object( elements[ 0], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox, _number_object_comboBox))
			return false;

		_operator_comboBox2.setSelectedItem( elements[ 1]);

		set_expression( expressionElements, index);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPageBase#set_others1(java.lang.String, int)
	 */
	protected boolean set_others1(String value, int index) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 > elements.length)
			return false;

		if ( !set_number_object( elements[ 0], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox, _number_object_comboBox))
			return false;

		_operator_comboBox2.setSelectedItem( elements[ 1]);

		_others_textFields[ index].setText( value.substring( ( elements[ 0] + " " + elements[ 1] + " ").length()));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String number_object1 = get_number_object( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox, _number_object_comboBox);
		if ( null == number_object1)
			return null;

		String operator2 = ( String)_operator_comboBox2.getSelectedItem();
		if ( null == operator2 || operator2.equals( ""))
			return null;

		return get( number_object1, NumberObjectCondition._sub_types, " " + operator2);
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
//		String operator2 = ( String)_operator_comboBox2.getSelectedItem();
//		if ( null == operator2 || operator2.equals( ""))
//			return false;
//
//		return get( rule, number_object1, NumberObjectConditionRuleManipulator._sub_types, " " + operator2);
//	}
}
