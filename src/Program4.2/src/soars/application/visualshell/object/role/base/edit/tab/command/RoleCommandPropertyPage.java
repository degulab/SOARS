/*
 * Created on 2005/12/01
 */
package soars.application.visualshell.object.role.base.edit.tab.command;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.command.RoleCommand;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class RoleCommandPropertyPage extends RulePropertyPageBase {

	/**
	 * 
	 */
	private RadioButton[] _radioButtons1 = new RadioButton[] {
		null, null
	};

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
	public RoleCommandPropertyPage(String title, String kind,
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

		ButtonGroup buttonGroup1 = new ButtonGroup();

		setup_role_variable( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_role( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		basic_panel.add( north_panel, "North");


		add( basic_panel);


		setup_apply_button();


		adjust();


		return true;
	}

	/**
	 * @param buttonGroup1
	 * @param north_panel
	 */
	private void setup_role_variable(ButtonGroup buttonGroup1, JPanel north_panel) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.role.variable.label"),
			buttonGroup1, true, false);
		_radioButtons1[ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_role_variable_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 0]);

		_role_variable_comboBox = create_comboBox(
			get_agent_role_variable_names( false),
//			_parent.get_agent_role_variables( false, true),
			_standardControlWidth, false);
		panel.add( _role_variable_comboBox);

		north_panel.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param north_panel
	 */
	private void setup_role(ButtonGroup buttonGroup1, JPanel north_panel) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.role.label"),
			buttonGroup1, true, false);
		_radioButtons1[ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_role_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 1]);

		_role_comboBox = create_comboBox( get_agent_role_names( true), _standardControlWidth, false);
		panel.add( _role_comboBox);

		north_panel.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( int i = 0; i < _radioButtons1.length; ++i)
			width = Math.max( width, _radioButtons1[ i].getPreferredSize().width);

		Dimension dimension = new Dimension( width,
			_radioButtons1[ 0].getPreferredSize().height);
		for ( int i = 0; i < _radioButtons1.length; ++i)
			_radioButtons1[ i].setPreferredSize( dimension);
	}

	/**
	 * 
	 */
	private void initialize() {
		if ( 0 == _role_variable_comboBox.getItemCount()) {
			_radioButtons1[ 0].setEnabled( false);
			_radioButtons1[ 1].setSelected( true);
			update_components( new boolean[] {
				false, true
			});
		} else {
			_radioButtons1[ 0].setSelected( true);
			update_components( new boolean[] {
				true, false
			});
		}
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_role_variable_comboBox.setEnabled( enables[ 0]);
		_role_comboBox.setEnabled( enables[ 1]);
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
		initialize();

		if ( null == rule || !_type.equals( rule._type) /*|| rule._value.equals( "")*/)
			return false;

		int kind = RoleCommand.get_kind( rule._value);
		if ( 0 > kind)
			return false;

		_radioButtons1[ kind].setSelected( true);

		switch ( kind) {
			case 0:
				_role_variable_comboBox.setSelectedItem( rule._value);
				break;
			case 1:
				_role_comboBox.setSelectedItem( rule._value);
				break;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);

		String value = null;
		switch ( kind) {
			case 0:
				value = ( String)_role_variable_comboBox.getSelectedItem();
				if ( null == value || value.equals( ""))
					return null;
				break;
			case 1:
				value = ( String)_role_comboBox.getSelectedItem();
				if ( null == value)
					value = "";
				break;
		}

		return Rule.create( _kind, _type, value);
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);
//
//		String value = null;
//		switch ( kind) {
//			case 0:
//				value = ( String)_role_variable_comboBox.getSelectedItem();
//				if ( null == value || value.equals( ""))
//					return false;
//				break;
//			case 1:
//				value = ( String)_role_comboBox.getSelectedItem();
//				if ( null == value)
//					value = "";
//				break;
//		}
//
//		rule._value = value;
//		rule._type = _type;
//
//		return true;
//	}
}
