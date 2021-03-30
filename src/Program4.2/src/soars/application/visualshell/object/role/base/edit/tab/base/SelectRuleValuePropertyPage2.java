/*
 * 2005/08/19
 */
package soars.application.visualshell.object.role.base.edit.tab.base;

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
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 */
public class SelectRuleValuePropertyPage2 extends RulePropertyPageBase {

	/**
	 * 
	 */
	private boolean _can_empty = true;

	/**
	 * 
	 */
	private String _label = "";

	/**
	 * 
	 */
	private CheckBox _denial_checkBox = null;

	/**
	 * 
	 */
	private ComboBox _value_comboBox = null;

	/**
	 * @param title
	 * @param kind
	 * @param type
	 * @param color
	 * @param role
	 * @param index
	 * @param label
	 * @param can_empty
	 * @param owner
	 * @param parent
	 */
	public SelectRuleValuePropertyPage2(String title, String kind, String type,
		Color color, Role role, int index, String label, boolean can_empty, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
		_label = label;
		_can_empty = can_empty;
	}

	/**
	 * @param items
	 */
	public void setup(String[] items) {
		if ( null == items || 0 == items.length)
			return;

		for ( int i = 0; i < items.length; ++i)
			_value_comboBox.addItem( items[ i]);

		_value_comboBox.setSelectedIndex( 0);
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

		basic_panel.add( north_panel, "North");


		add( basic_panel);


		setup_apply_button();


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_value_combo_box(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel( _label);
		label.setForeground( _color);
		panel.add( label);

		_value_comboBox = create_comboBox( null, 	_standardControlWidth/*500*/, false);

		panel.add( _value_comboBox);
		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));

		_denial_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.check.box.denial"),
			true, false);
		panel.add( _denial_checkBox);

		JLabel label = create_label( _label, false);
		panel.add( label);

		_value_comboBox = create_comboBox( null, 	_standardControlWidth/*500*/, false);

		panel.add( _value_comboBox);

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
		if ( null == rule || !_type.equals( rule._type))
			return false;

		_denial_checkBox.setSelected( rule._value.startsWith( "!"));

		_value_comboBox.setSelectedItem( rule._value.substring( ( rule._value.startsWith( "!")) ? ( "!".length()) : 0));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String value = ( String)_value_comboBox.getSelectedItem();
		if ( !_can_empty && ( null == value || value.equals( "")))
			return null;

		return Rule.create( _kind, _type, ( _denial_checkBox.isSelected() ? "!" : "") + value);
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		String value = ( String)_value_comboBox.getSelectedItem();
//		if ( !_can_empty && ( null == value || value.equals( "")))
//			return false;
//
//		rule._value = ( ( _denial_checkBox.isSelected() ? "!" : "") + value);
//		rule._type = _type;
//
//		return true;
//	}
}
