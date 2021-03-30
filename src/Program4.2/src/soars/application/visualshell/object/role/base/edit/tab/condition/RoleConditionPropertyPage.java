/*
 * Created on 2005/12/01
 */
package soars.application.visualshell.object.role.base.edit.tab.condition;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.condition.RoleCondition;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 */
public class RoleConditionPropertyPage extends RulePropertyPageBase {

	/**
	 * 
	 */
	private CheckBox _checkBox = null;

	/**
	 * 
	 */
	private ComboBox _role_variable_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _role_comboBox = null;

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
	public RoleConditionPropertyPage(String title, String kind,
		String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.tab.TabbedPage#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		setLayout( new BorderLayout());


		JPanel basic_panel = new JPanel();
		basic_panel.setLayout( new BorderLayout());


		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup( north_panel);

		insert_vertical_strut( north_panel);

		basic_panel.add( north_panel, "North");


		add( basic_panel);


		setup_apply_button();


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.check.box.denial"),
			true, false);
		panel.add( _checkBox);

		JLabel label = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.role.variable.label"),
			true);
		panel.add( label);

		_role_variable_comboBox = create_comboBox(
			get_agent_role_variable_names( true),
//			_parent.get_agent_role_variables( true, true),
			_standardControlWidth, false);
		panel.add( _role_variable_comboBox);

		label = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.role.label"),
			true);
		panel.add( label);

		_role_comboBox = create_comboBox(
			get_agent_role_names( true), _standardControlWidth, false);
		panel.add( _role_comboBox);

		parent.add( panel);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#on_setup_completed()
	 */
	public void on_setup_completed() {
		super.on_setup_completed();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.tab.TabbedPage#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		_parent.on_apply( this, actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#set(soars.application.visualshell.object.role.base.rule.base.Rule)
	 */
	public boolean set(Rule rule) {
		if ( null == rule || !_type.equals( rule._type) || rule._value.equals( ""))
			return false;

		String[] elements = RoleCondition.get_elements( rule._value);
		if ( null == elements) {
			_role_variable_comboBox.setSelectedItem( "");
			_role_comboBox.setSelectedItem( "");
		} else {
			switch ( elements.length) {
				case 1:
					_role_variable_comboBox.setSelectedItem( "");
					_role_comboBox.setSelectedItem( elements[ 0]);
					break;
				case 2:
					_role_variable_comboBox.setSelectedItem( elements[ 0]);
					_role_comboBox.setSelectedItem( elements[ 1]);
					break;
				default:
					return false;
			}
		}

		_checkBox.setSelected( rule._value.startsWith( "!"));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String role_variable = ( String)_role_variable_comboBox.getSelectedItem();
		String role = ( String)_role_comboBox.getSelectedItem();

		String value = "";
		if ( null == role_variable || role_variable.equals( "")) {
			if ( null != role && !role.equals( ""))
				value = role;
		} else {
			if ( null == role || role.equals( ""))
				return null;
			else {
				value = ( role_variable + "=" + role);
			}
		}

		return Rule.create( _kind, _type,
			( _checkBox.isSelected() ? "!" : "") + RoleCondition._reserved_word + value);
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		String role_variable = ( String)_role_variable_comboBox.getSelectedItem();
//		String role = ( String)_role_comboBox.getSelectedItem();
//
//		String value = "";
//		if ( null == role_variable || role_variable.equals( "")) {
//			if ( null != role && !role.equals( ""))
//				value = role;
//		} else {
//			if ( null == role || role.equals( ""))
//				return false;
//			else {
//				value = ( role_variable + "=" + role);
//			}
//		}
//
//		rule._value = ( ( _checkBox.isSelected() ? "!" : "")
//			+ RoleConditionRuleManipulator._reserved_word + value);
//		rule._type = _type;
//
//		return true;
//	}
}
