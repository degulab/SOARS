/**
 * 
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
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.command.SpotVariableCommand;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 *
 */
public class SpotVariableCommandPropertyPage extends RulePropertyPageBase {

	/**
	 * 
	 */
	private JLabel _target_spot_variable_label = null;

	/**
	 * 
	 */
	private ComboBox _target_spot_variable_comboBox = null;

	/**
	 * 
	 */
	private CheckBox _spot_checkBox = null;

	/**
	 * 
	 */
	private ObjectSelector _spot_selector = null;

	/**
	 * 
	 */
	private CheckBox _spot_variable_checkBox = null;

	/**
	 * 
	 */
	private ComboBox _spot_variable_comboBox = null;

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
	public SpotVariableCommandPropertyPage(String title, String kind,
		String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#on_create()
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

		setup_target_spot_variable_comboBox( north_panel);

		insert_vertical_strut( north_panel);

		setup_spot_selector( north_panel);

		insert_vertical_strut( north_panel);

		basic_panel.add( north_panel, "North");


		add( basic_panel);


		setup_apply_button();


		adjust();


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_target_spot_variable_comboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_target_spot_variable_label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.spot.variable.spot.variable.label.name"),
			true);
		panel.add( _target_spot_variable_label);

		_target_spot_variable_comboBox = create_comboBox(
			( ( _role instanceof AgentRole) ? get_agent_spot_variable_names( false) : get_spot_spot_variable_names( false)),
			_standardControlWidth, false);
		panel.add( _target_spot_variable_comboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_spot_selector(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_spot_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.spot.variable.spot.check.box.name"),
			true, true);
		_spot_checkBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_spot_selector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				on_update( ItemEvent.SELECTED == arg0.getStateChange(),
					_spot_selector,
					_spot_variable_checkBox,
					_spot_variable_comboBox);
			}
		});
		panel.add( _spot_checkBox);

		_spot_selector = create_spot_selector( true, true, panel);


		_spot_variable_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.spot.variable.check.box.name"),
			true, true);
		_spot_variable_checkBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				on_update( _spot_checkBox.isSelected(),
					_spot_selector,
					_spot_variable_checkBox,
					_spot_variable_comboBox);
			}
		});
		panel.add( _spot_variable_checkBox);

		_spot_variable_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _spot_variable_comboBox);

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _target_spot_variable_label.getPreferredSize().width;
		width = Math.max( width, _spot_checkBox.getPreferredSize().width);

		_target_spot_variable_label.setPreferredSize( new Dimension( width, _target_spot_variable_label.getPreferredSize().height));
		_spot_checkBox.setPreferredSize( new Dimension( width, _spot_checkBox.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#reset(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void reset(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		CommonTool.update( spot_variable_comboBox, !_spot_checkBox.isSelected() ? get_agent_spot_variable_names( false) : get_spot_spot_variable_names( false));

		super.reset(objectSelector, spot_variable_checkBox, spot_variable_comboBox);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(objectSelector, spot_variable_checkBox, spot_variable_comboBox);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(spotObject, number, objectSelector, spot_variable_checkBox, spot_variable_comboBox);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector)
	 */
	protected void update(ObjectSelector objectSelector) {
		update( objectSelector, _spot_variable_checkBox, _spot_variable_comboBox);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector) {
		update( spotObject, number, objectSelector, _spot_variable_checkBox, _spot_variable_comboBox);
	}

	/**
	 * 
	 */
	private void initialize() {
		if ( _role instanceof AgentRole) {
			_spot_checkBox.setSelected( false);
			_spot_selector.setEnabled( false);
		} else {
			_spot_checkBox.setSelected( true);
			_spot_checkBox.setEnabled( false);
			_spot_selector.setEnabled( true);
		}
	}

	/**
	 * 
	 */
	protected void set_handler() {
		_spot_selector.set_handler( this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#on_setup_completed()
	 */
	public void on_setup_completed() {
		reset( _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
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

		if ( null == rule || !_type.equals( rule._type) /*|| rule._value.equals( "")*/) {
			set_handler();
			return false;
		}

		String[] values = SpotVariableCommand.get_values( rule._value);
		if ( null == values) {
			set_handler();
			return false;
		}

		if ( !set( values[ 1], values[ 2], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox)) {
			set_handler();
			return false;
		}

		_target_spot_variable_comboBox.setSelectedItem( values[ 0]);

		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String spot_variable = ( String)_target_spot_variable_comboBox.getSelectedItem();
		if ( null == spot_variable || spot_variable.equals( ""))
			return null;

		String value = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
		if ( value.equals( ""))
			return null;

		return Rule.create( _kind, _type, SpotVariableCommand.get_rule_value( spot_variable, value));
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		String spot_variable = ( String)_target_spot_variable_comboBox.getSelectedItem();
//		if ( null == spot_variable || spot_variable.equals( ""))
//			return false;
//
//		String value = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
//		if ( value.equals( ""))
//			return false;
//
//		rule._value = SpotVariableCommandRuleManipulator.get_rule_value( spot_variable, value);
//		rule._type = _type;
//
//		return true;
//	}
}
