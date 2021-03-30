/*
 * 2005/08/29
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
import javax.swing.SwingConstants;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.command.CreateAgentCommand;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;

/**
 * @author kurata
 */
public class CreateAgentCommandPropertyPage extends RulePropertyPageBase {

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
	 * 
	 */
	private TextField _name_textField = null;

	/**
	 * 
	 */
	private TextField _number_textField = null;

	/**
	 * 
	 */
	private ComboBox _role_comboBox = null;

	/**
	 * 
	 */
	private JLabel[] _labels = new JLabel[] {
		null, null, null
	};

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
	public CreateAgentCommandPropertyPage(String title, String kind,
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

		setup_spot_checkBox_and_spot_selector( north_panel);

		insert_vertical_strut( north_panel);

		setup_spot_variable_checkBox_and_spot_variable_comboBox( north_panel);

		insert_vertical_strut( north_panel);

		setup_name_textField( north_panel);

		insert_vertical_strut( north_panel);

		setup_number_textField( north_panel);

		insert_vertical_strut( north_panel);

		setup_role_comboBox( north_panel);

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
	private void setup_spot_checkBox_and_spot_selector(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_spot_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.agent.spot.check.box.name"),
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

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_spot_variable_checkBox_and_spot_variable_comboBox( JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

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
	 * @param parent
	 */
	private void setup_name_textField(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 0] = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.agent.label.name"),
			false);
		_labels[ 0].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 0]);

		_name_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters1), false);
		panel.add( _name_textField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_number_textField(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 1] = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.agent.label.number"),
			false);
		_labels[ 1].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 1]);

		_number_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters1), true);
		_number_textField.setText( "1");
		panel.add( _number_textField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_role_comboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 2] = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.agent.label.role"),
			false);
		_labels[ 2].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 2]);

		_role_comboBox = create_comboBox(
			LayerManager.get_instance().get_agent_role_names( true), false);
		panel.add( _role_comboBox);

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _spot_checkBox.getPreferredSize().width;

		width = Math.max( width, _spot_variable_checkBox.getPreferredSize().width);

		for ( int i = 0; i < _labels.length; ++i)
			width = Math.max( width, _labels[ i].getPreferredSize().width);

		_spot_checkBox.setPreferredSize( new Dimension( width,
			_spot_checkBox.getPreferredSize().height));

		_spot_variable_checkBox.setPreferredSize( new Dimension( width,
			_spot_variable_checkBox.getPreferredSize().height));

		for ( int i = 0; i < _labels.length; ++i)
			_labels[ i].setPreferredSize( new Dimension( width,
				_labels[ i].getPreferredSize().height));

//		_name_textField.setPreferredSize( new Dimension( _standard_control_width,
//			_name_textField.getPreferredSize().height));
//		_number_textField.setPreferredSize( new Dimension( _standard_control_width,
//			_number_textField.getPreferredSize().height));
//		_role_comboBox.setPreferredSize( new Dimension( _standard_control_width,
//			_role_comboBox.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#reset(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void reset(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		CommonTool.update( spot_variable_comboBox, !_spot_checkBox.isSelected() ? get_agent_spot_variable_names( false) : get_spot_spot_variable_names( false));

		super.reset(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		objectSelector.setEnabled( _spot_checkBox.isSelected());
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
		_spot_checkBox.setSelected( false);
		_spot_selector.setEnabled( false);
//		if ( _role instanceof AgentRole) {
//			_spot_checkBox.setSelected( false);
//			_spot_selector.setEnabled( false);
//		} else {
//			_spot_checkBox.setSelected( true);
//			_spot_checkBox.setEnabled( false);
//			_spot_selector.setEnabled( true);
//		}
	}

	/**
	 * 
	 */
	private void set_handler() {
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

		if ( null == rule || !_type.equals( rule._type) || rule._value.equals( "")) {
			set_handler();
			return false;
		}

		String[] spots = CommonRuleManipulator.get_spot( rule._value);
		if ( null == spots) {
			set_handler();
			return false;
		}

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || ( 2 != elements.length && 3 != elements.length))
			return false;

		if ( !set( spots[ 0], spots[ 1], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox)) {
			set_handler();
			return false;
		}

		_name_textField.setText( elements[ 0]);
		_number_textField.setText( elements[ 1]);

		_role_comboBox.setSelectedItem( "");
		if ( 3 == elements.length)
			_role_comboBox.setSelectedItem( elements[ 2]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String name = _name_textField.getText();
		if ( null == name || name.equals( "")
			|| name.equals( "$") || 0 < name.indexOf( '$')
//			|| name.equals( "$Name") || name.equals( "$Role") || name.equals( "$Spot")
			|| 0 <= name.indexOf( Constant._experimentName))
			return null;

		if ( name.startsWith( "$") && ( 0 < name.indexOf( "$", 1) || 0 < name.indexOf( ")", 1)))
			return null;

		String number = _number_textField.getText();
//		if ( null == number || number.equals( ""))
//			return null;
		if ( null == number
			|| number.equals( "$")
			|| 0 < number.indexOf( '$')
			|| number.equals( "$Name")
			|| number.equals( "$Role")
			|| number.equals( "$Spot")
			|| 0 <= number.indexOf( Constant._experimentName))
			return null;

		if ( number.startsWith( "$")) {
			if ( 0 < number.indexOf( "$", 1)
				|| 0 < number.indexOf( ")", 1))
				return null;
		} else {
			try {
				int n = Integer.parseInt( number);
				if ( 1 > n)
					return null;

				number = String.valueOf( n);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				return null;
			}
		}

//		if ( LayerManager.get_instance().has_same_agent_name( name, number))
//			return false;

//		if ( LayerManager.get_instance().chartObject_has_same_name( name, number))
//			return null;

//		if ( _ruleTableBase.has_same_agent_name( name, number, _rule))
//			return false;

		String role = ( String)_role_comboBox.getSelectedItem();

		String value = ( name + "=" + number);
		if ( null != role && !role.equals( ""))
			value += ( "=" + role);

		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
//		String prefix = get_spot_prefix( _spot_checkBox, _spot_selector);

		return Rule.create( _kind, _type, spot + CreateAgentCommand._reserved_word + value);
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		String name = _name_textField.getText();
//		if ( null == name || name.equals( "")
//			|| name.equals( "$") || 0 < name.indexOf( '$')
////			|| name.equals( "$Name") || name.equals( "$Role") || name.equals( "$Spot")
//			|| 0 <= name.indexOf( Constant._experiment_name))
//			return false;
//
//		if ( name.startsWith( "$") && ( 0 < name.indexOf( "$", 1) || 0 < name.indexOf( ")", 1)))
//			return false;
//
//		String number = _number_textField.getText();
//		if ( null == number || number.equals( ""))
//			return false;
//
//		try {
//			int n = Integer.parseInt( number);
//			if ( 1 > n)
//				return false;
//
//			number = String.valueOf( n);
//		} catch (NumberFormatException e) {
//			//e.printStackTrace();
//			return false;
//		}
//
////		if ( LayerManager.get_instance().has_same_agent_name( name, number))
////			return false;
//
//		if ( LayerManager.get_instance().chartObject_has_same_name( name, number))
//			return false;
//
////		if ( _ruleTableBase.has_same_agent_name( name, number, _rule))
////			return false;
//
//		String role = ( String)_role_comboBox.getSelectedItem();
//
//		String value = ( name + "=" + number);
//		if ( null != role && !role.equals( ""))
//			value += ( "=" + role);
//
//		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
////		String prefix = get_spot_prefix( _spot_checkBox, _spot_selector);
//
//		rule._value = ( spot + CreateAgentCommandRuleManipulator._reserved_word + value);
//		rule._type = _type;
//
//		return true;
//	}
}
