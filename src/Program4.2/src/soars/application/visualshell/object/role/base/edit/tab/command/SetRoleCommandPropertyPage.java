/*
 * Created on 2005/12/01
 */
package soars.application.visualshell.object.role.base.edit.tab.command;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.command.SetRoleCommand;
import soars.application.visualshell.object.role.base.object.condition.RoleCondition;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 */
public class SetRoleCommandPropertyPage extends RulePropertyPageBase {

	/**
	 * 
	 */
	private ComboBox _role_variable_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _role_comboBox = null;

//	private String _original_role_variable = "";

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
	public SetRoleCommandPropertyPage(String title, String kind,
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
	 * @param north_panel
	 * 
	 */
	private void setup(JPanel north_panel) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.set.role.variable.label"),
			true);
		panel.add( label);

		_role_variable_comboBox = create_comboBox(
			get_agent_role_variable_names( false), _standardControlWidth, false);
		panel.add( _role_variable_comboBox);

		label = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.set.role.label"),
			true);
		panel.add( label);

		_role_comboBox = create_comboBox(
			get_agent_role_names( true), _standardControlWidth, false);
		panel.add( _role_comboBox);

		north_panel.add( panel);
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
//		_original_role_variable = "";

		if ( null == rule || !_type.equals( rule._type) || rule._value.equals( ""))
			return false;

		String[] elements = RoleCondition.get_elements( rule._value);
		if ( null == elements)
			return false;
		else {
			switch ( elements.length) {
				case 1:
					_role_variable_comboBox.setSelectedItem( elements[ 0]);
					_role_comboBox.setSelectedItem( "");
//					_original_role_variable = elements[ 0];
					break;
				case 2:
					_role_variable_comboBox.setSelectedItem( elements[ 0]);
					_role_comboBox.setSelectedItem( elements[ 1]);
//					_original_role_variable = elements[ 0];
					break;
				default:
					return false;
			}
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String role_variable = ( String)_role_variable_comboBox.getSelectedItem();
		if ( null == role_variable || role_variable.equals( "")
			//|| role_variable.equals( Constant._spot_chart_role_name)
			|| LayerManager.get_instance().is_role_name( role_variable))
			return null;

//		if ( !_original_role_variable.equals( "")
//			&& !role_variable.equals( _original_role_variable)
//			&& !_parent.contains_this_agent_role_variable( _original_role_variable)) {
//			WarningManager.get_instance().cleanup();
//			if ( _parent.use_this_role_variable( _original_role_variable)) {
//				WarningDlg1 warningDlg1 = new WarningDlg1(
//					_owner,
//					ResourceManager.get_instance().get( "warning.dialog1.title"),
//					ResourceManager.get_instance().get( "warning.dialog1.message1"));
//				warningDlg1.do_modal();
//				return false;
//			}
//		}

		String role = ( String)_role_comboBox.getSelectedItem();

		String value = role_variable;
		if ( null != role && !role.equals( ""))
			value += ( "=" + role);

//		_original_role_variable = role_variable;

		return Rule.create( _kind, _type, SetRoleCommand._reserved_word + value);
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		String role_variable = ( String)_role_variable_comboBox.getSelectedItem();
//		if ( null == role_variable || role_variable.equals( "")
//			//|| role_variable.equals( Constant._spot_chart_role_name)
//			|| LayerManager.get_instance().is_role_name( role_variable))
//			return false;
//
////		if ( !_original_role_variable.equals( "")
////			&& !role_variable.equals( _original_role_variable)
////			&& !_parent.contains_this_agent_role_variable( _original_role_variable)) {
////			WarningManager.get_instance().cleanup();
////			if ( _parent.use_this_role_variable( _original_role_variable)) {
////				WarningDlg1 warningDlg1 = new WarningDlg1(
////					_owner,
////					ResourceManager.get_instance().get( "warning.dialog1.title"),
////					ResourceManager.get_instance().get( "warning.dialog1.message1"));
////				warningDlg1.do_modal();
////				return false;
////			}
////		}
//
//		String role = ( String)_role_comboBox.getSelectedItem();
//
//		String value = role_variable;
//		if ( null != role && !role.equals( ""))
//			value += ( "=" + role);
//
//		rule._value = ( SetRoleCommandRuleManipulator._reserved_word + value);
//		rule._type = _type;
//
////		_original_role_variable = role_variable;
//
//		return true;
//	}
}
