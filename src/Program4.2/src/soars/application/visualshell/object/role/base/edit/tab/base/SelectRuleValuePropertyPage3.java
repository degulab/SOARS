/*
 * Created on 2006/02/09
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

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.object.base.Rule;

/**
 * @author kurata
 */
public class SelectRuleValuePropertyPage3 extends RulePropertyPageBase {

	/**
	 * 
	 */
	private boolean _can_empty = true;

	/**
	 * 
	 */
	private String _agent_or_spot = "";

	/**
	 * 
	 */
	private String _label = "";

	/**
	 * 
	 */
	private ObjectSelector _objectSelector = null;

	/**
	 * @param agent_or_spot
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
	public SelectRuleValuePropertyPage3(String agent_or_spot, String title, String kind, String type,
		Color color, Role role, int index, String label, boolean can_empty, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
		_agent_or_spot = agent_or_spot;
		_label = label;
		_can_empty = can_empty;
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

		if ( !setup( north_panel))
			return false;

		basic_panel.add( north_panel, "North");


		add( basic_panel);


		setup_apply_button();


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = create_label( _label, false);
		panel.add( label);

		if ( _agent_or_spot.equals( "agent"))
			_objectSelector = create_agent_selector( _can_empty, /*400,*/ true, panel);
		else if ( _agent_or_spot.equals( "spot"))
			_objectSelector = create_spot_selector( _can_empty, /*400,*/ true, panel);
		else
			return false;

		parent.add( panel);

		return true;
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

		_objectSelector.set( rule._value);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String value = _objectSelector.get();
		if ( !_can_empty && ( null == value || value.equals( "")))
			return null;

		return Rule.create( _kind, _type, value);
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		String value = _objectSelector.get();
//		if ( !_can_empty && ( null == value || value.equals( "")))
//			return false;
//
//		rule._value = value;
//		rule._type = _type;
//
//		return true;
//	}
}
