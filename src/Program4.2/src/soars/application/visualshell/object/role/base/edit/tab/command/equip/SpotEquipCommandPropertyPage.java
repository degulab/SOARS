/*
 * Created on 2005/10/18
 */
package soars.application.visualshell.object.role.base.edit.tab.command.equip;

import java.awt.Color;
import java.awt.Frame;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.command.GetEquipCommand;
import soars.application.visualshell.object.role.base.object.command.PutEquipCommand;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class SpotEquipCommandPropertyPage extends EquipCommandPropertyPageBase {

	/**
	 * @param title
	 * @param kind
	 * @param color
	 * @param role
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public SpotEquipCommandPropertyPage(String title, String kind,
		Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, color, role, index, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.command.equip.EquipCommandPropertyPageBase#set(java.lang.String, int, int, int)
	 */
	protected boolean set(String value, int kind, int current_spot, int spot) {
		String[] elements = CommonRuleManipulator.get_elements( value, 2);
		if ( null == elements)
			return false;

		String[] values1 = CommonRuleManipulator.get_spot_and_object( elements[ current_spot]);
		if ( null == values1)
			return false;

		String[] values2 = CommonRuleManipulator.get_spot_and_object( elements[ spot]);
		if ( null == values2)
			return false;

		if ( !set( values2[ 0], values2[ 1], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox))
			return false;

		set1( _comboBoxes.get( kind).get( 0), _comboBoxes.get( kind).get( 1), values1[ 2], values2[ 2]);

		_directionComboBox.setSelectedIndex( current_spot);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String value = null;
		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);

		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);
		if ( spot.equals( "<>"))
			return null;

		if ( 0 == _directionComboBox.getSelectedIndex())
			value = get2( "<>", _comboBoxes.get( kind).get( 0), spot, _comboBoxes.get( kind).get( 1));
		else if ( 1 == _directionComboBox.getSelectedIndex())
			value = get2( spot, _comboBoxes.get( kind).get( 1), "<>", _comboBoxes.get( kind).get( 0));

		if ( null == value)
			return null;

		if ( 0 == _directionComboBox.getSelectedIndex())
			return Rule.create( _kind, ResourceManager.get_instance().get( "rule.type.command.get.equip"),
				GetEquipCommand._reserved_word + value);
		else if ( 1 == _directionComboBox.getSelectedIndex())
			return Rule.create( _kind, ResourceManager.get_instance().get( "rule.type.command.put.equip"),
				PutEquipCommand._reserved_word + value);
		else
			return null;
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
////		if ( !super.get(rule))
////			return false;
//
//		String value = null;
//		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);
//
//		String spot = get( _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
//		if ( spot.equals( "<>"))
//			return false;
//
//		if ( 0 == _direction_comboBox.getSelectedIndex())
//			value = get2( "<>", _comboBoxes[ kind][ 0], spot, _comboBoxes[ kind][ 1]);
//		else if ( 1 == _direction_comboBox.getSelectedIndex())
//			value = get2( spot, _comboBoxes[ kind][ 1], "<>", _comboBoxes[ kind][ 0]);
//
//		if ( null == value)
//			return false;
//
//		if ( 0 == _direction_comboBox.getSelectedIndex()) {
//			rule._type = ResourceManager.get_instance().get( "rule.type.command.get.equip");
//			rule._value = ( GetEquipCommandRuleManipulator._reserved_word + value);
//		} else if ( 1 == _direction_comboBox.getSelectedIndex()) {
//			rule._type = ResourceManager.get_instance().get( "rule.type.command.put.equip");
//			rule._value = ( PutEquipCommandRuleManipulator._reserved_word + value);
//		} else
//			return false;
//
//		return true;
//	}
}
