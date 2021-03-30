/*
 * 2005/07/28
 */
package soars.application.visualshell.object.role.base.edit.tab.command;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.common.time.TimePropertyPage;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.command.TimeCommand;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class TimeCommandPropertyPage extends TimePropertyPage {

	/**
	 * 
	 */
	private JLabel _time_variable_label = null;

	/**
	 * 
	 */
	private ComboBox _time_variable_comboBox = null;

	/**
	 * 
	 */
	private RadioButton[][] _radioButtons = new RadioButton[][] {
		{ null, null, null},
		{ null, null}
	};

	/**
	 * 
	 */
	private JLabel _operator_label = null;

	/**
	 * 
	 */
	private TextField[] _time_textFields = new TextField[] {
		null, null
	};

	/**
	 * 
	 */
	private ComboBox[][] _time_comboBoxes = new ComboBox[][] {
		{ null, null},
		{ null, null}
	};

	/**
	 * 
	 */
	private JLabel[][] _time_labels = new JLabel[][] {
		{ null, null},
		{ null, null}
	};

	/**
	 * 
	 */
	private ComboBox _operator_comboBox = null;

	/**
	 * 
	 */
	private ComboBox[] _time_variable_comboBoxes = new ComboBox[] {
		null, null
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
	public TimeCommandPropertyPage(String title, String kind,
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

		setup_time_variable_comboBox( north_panel);

		insert_vertical_strut( north_panel);

		ButtonGroup buttonGroups[] = new ButtonGroup[] { new ButtonGroup(), new ButtonGroup()};

		setup_time_components( buttonGroups, north_panel);

		insert_vertical_strut( north_panel);

		setup_time_variable_comboBoxes( buttonGroups, north_panel);

		insert_vertical_strut( north_panel);

		setup_current_time_radioButton( buttonGroups[ 0], north_panel);

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
			ResourceManager.get_instance().get( "edit.rule.dialog.command.time.spot.check.box.name"),
			false, true);
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
	private void setup_time_variable_comboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));


		_time_variable_label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.time.time.variable"),
			true);
		panel.add( _time_variable_label);


		_time_variable_comboBox = create_comboBox( null, false);
		panel.add( _time_variable_comboBox);


		JLabel label = create_label( " = ", false);
		panel.add( label);


		parent.add( panel);
	}

	/**
	 * @param buttonGroups
	 * @param parent
	 */
	private void setup_time_components(ButtonGroup[] buttonGroups, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			setup_time_radioButtons( i, buttonGroups, panel);
			setup_time_components( i, panel);
			if ( 0 == i) {
				_operator_label = create_label(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.time.operator"),
					false);
				panel.add( _operator_label);
			}
		}

		parent.add( panel);
	}

	/**
	 * @param index
	 * @param buttonGroups
	 * @param panel
	 */
	private void setup_time_radioButtons(final int index, ButtonGroup[] buttonGroups, JPanel panel) {
		_radioButtons[ index][ 0] = create_radioButton(
			( ( 0 == index ) ? ResourceManager.get_instance().get( "edit.rule.dialog.command.time.time.radio.button") : null),
			buttonGroups[ index], true, false);
		_radioButtons[ index][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_time_textFields[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_time_labels[ index][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_time_comboBoxes[ index][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_time_labels[ index][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_time_comboBoxes[ index][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons[ index][ 0]);
	}

	/**
	 * @param index
	 * @param panel
	 */
	private void setup_time_components(int index, JPanel panel) {
		_time_textFields[ index] = create_textField( new TextExcluder( Constant._prohibitedCharacters14), "", 60, true);
		panel.add( _time_textFields[ index]);

		_time_labels[ index][ 0] = create_label( " / ", false);
		panel.add( _time_labels[ index][ 0]);

		_time_comboBoxes[ index][ 0] = create_comboBox( CommonTool.get_hours(), 60, true);
		panel.add( _time_comboBoxes[ index][ 0]);

		_time_labels[ index][ 1] = create_label( " : ", false);
		panel.add( _time_labels[ index][ 1]);

		_time_comboBoxes[ index][ 1] = create_comboBox( CommonTool.get_minutes00(), 60, true);
		panel.add( _time_comboBoxes[ index][ 1]);
	}

	/**
	 * @param buttonGroups
	 * @param parent
	 */
	private void setup_time_variable_comboBoxes(ButtonGroup[] buttonGroups, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			setup_time_variable_radioButtons( i, buttonGroups, panel);
			setup_time_variable_comboBoxes( i, panel);
			if ( 0 == i)
				setup_operator_comboBox( panel);
		}

		parent.add( panel);
	}

	/**
	 * @param panel
	 */
	private void setup_operator_comboBox(JPanel panel) {
		_operator_comboBox = create_comboBox( new String[] { "", "+", "-"}, 60, true);
		_operator_comboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				update_right_side();
			}
		});
		panel.add( _operator_comboBox);
	}

	/**
	 * @param index
	 * @param buttonGroups
	 * @param panel
	 */
	private void setup_time_variable_radioButtons(final int index, ButtonGroup[] buttonGroups, JPanel panel) {
		_radioButtons[ index][ 1] = create_radioButton(
			( ( 0 == index ) ? ResourceManager.get_instance().get( "edit.rule.dialog.command.time.time.variable.radio.button") : null),
			buttonGroups[ index], true, false);
		_radioButtons[ index][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_time_variable_comboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons[ index][ 1]);
	}

	/**
	 * @param index
	 * @param panel
	 */
	private void setup_time_variable_comboBoxes(int index, JPanel panel) {
		_time_variable_comboBoxes[ index] = create_comboBox( null, false);
		panel.add( _time_variable_comboBoxes[ index]);
	}

	/**
	 * @param buttonGroup
	 * @param parent
	 */
	private void setup_current_time_radioButton(ButtonGroup buttonGroup, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons[ 0][ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.time.current.time.radio.button"),
			buttonGroup, true, false);
		panel.add( _radioButtons[ 0][ 2]);

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _spot_checkBox.getPreferredSize().width;
		width = Math.max( width, _spot_variable_checkBox.getPreferredSize().width);
		width = Math.max( width, _time_variable_label.getPreferredSize().width);
		for ( int i = 0; i < _radioButtons[ 0].length; ++i)
			width = Math.max( width, _radioButtons[ 0][ i].getPreferredSize().width);

		_spot_checkBox.setPreferredSize( new Dimension( width, _spot_checkBox.getPreferredSize().height));
		_spot_variable_checkBox.setPreferredSize( new Dimension( width, _spot_variable_checkBox.getPreferredSize().height));
		_time_variable_label.setPreferredSize( new Dimension( width, _time_variable_label.getPreferredSize().height));
		for ( int i = 0; i < _radioButtons[ 0].length; ++i)
			_radioButtons[ 0][ i].setPreferredSize( new Dimension( width, _radioButtons[ 0][ i].getPreferredSize().height));


		width = _operator_label.getPreferredSize().width;
		width = Math.max( width, _operator_comboBox.getPreferredSize().width);
		_operator_label.setPreferredSize( new Dimension( width, _operator_label.getPreferredSize().height));
		_operator_comboBox.setPreferredSize( new Dimension( width, _operator_comboBox.getPreferredSize().height));


		width = _time_textFields[ 0].getPreferredSize().width
			+ _time_labels[ 0][ 0].getPreferredSize().width
			+ _time_comboBoxes[ 0][ 0].getPreferredSize().width
			+ _time_labels[ 0][ 1].getPreferredSize().width
			+ _time_comboBoxes[ 0][ 1].getPreferredSize().width
			+ 20;

		_spot_selector.setWidth( width);
		_spot_variable_comboBox.setPreferredSize( new Dimension( width, _spot_variable_comboBox.getPreferredSize().height));
		_time_variable_comboBox.setPreferredSize( new Dimension( width, _time_variable_comboBox.getPreferredSize().height));
		for ( int i = 0; i < _time_variable_comboBoxes.length; ++i)
			_time_variable_comboBoxes[ i].setPreferredSize( new Dimension( width, _time_variable_comboBoxes[ i].getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#reset(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void reset(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		CommonTool.update( spot_variable_comboBox, !_spot_checkBox.isSelected() ? get_agent_spot_variable_names( false) : get_spot_spot_variable_names( false));

		super.reset(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _time_variable_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_time_variable_names( false) : get_spot_time_variable_names( false));

		for ( int i = 0; i < _time_variable_comboBoxes.length; ++i)
			CommonTool.update( _time_variable_comboBoxes[ i], ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_time_variable_names( false) : get_spot_time_variable_names( false));

		update_components();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _time_variable_comboBox, get_spot_time_variable_names( false));

		for ( int i = 0; i < _time_variable_comboBoxes.length; ++i)
			CommonTool.update( _time_variable_comboBoxes[ i], get_spot_time_variable_names( false));

		update_components();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(spotObject, number, objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _time_variable_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "time variable", number, false) : get_spot_time_variable_names( false));

		for ( int i = 0; i < _time_variable_comboBoxes.length; ++i)
			CommonTool.update( _time_variable_comboBoxes[ i], !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "time variable", number, false) : get_spot_time_variable_names( false));

		update_components();
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

		for ( int i = 0; i < _radioButtons.length; ++i)
			_radioButtons[ i][ 0].setSelected( true);

		for ( int i = 0; i < _radioButtons[ 1].length; ++i)
			_radioButtons[ 1][ i].setEnabled( false);

		update_components();
	}

	/**
	 * 
	 */
	private void update_components() {
		if ( 0 < _time_variable_comboBox.getItemCount()) {
			for ( int i = 0; i < _radioButtons[ 0].length; ++i)
				_radioButtons[ 0][ i].setEnabled( true);

			update_left_side();
			update_right_side();

			_operator_label.setEnabled( true);
			_operator_comboBox.setEnabled( true);
		} else {
			for ( int i = 0; i < _radioButtons.length; ++i) {
				for ( int j = 0; j < _radioButtons[ i].length; ++j)
				_radioButtons[ i][ j].setEnabled( false);
			}

			update_components( new boolean[] { false, false}, 0);
			update_components( new boolean[] { false, false}, 1);

			_operator_label.setEnabled( false);
			_operator_comboBox.setEnabled( false);
		}
	}

	/**
	 * 
	 */
	private void update_left_side() {
		boolean[] enables = new boolean[] {
			false, false, false
		};
		enables[ SwingTool.get_enabled_radioButton( _radioButtons[ 0])] = true;
		update_components( enables, 0);
	}

	/**
	 * 
	 */
	private void update_right_side() {
		String operator = ( String)_operator_comboBox.getSelectedItem();
		for ( int i = 0; i < _radioButtons[ 1].length; ++i)
			_radioButtons[ 1][ i].setEnabled( !operator.equals( ""));

		boolean[] enables = new boolean[] {
			false, false
		};
		if ( !operator.equals( ""))
			enables[ SwingTool.get_enabled_radioButton( _radioButtons[ 1])] = true;

		update_components( enables, 1);
	}

	/**
	 * @param enables
	 * @param index
	 */
	private void update_components(boolean[] enables, int index) {
		_time_textFields[ index].setEnabled( enables[ 0]);

		for ( int i = 0; i < _time_comboBoxes[ index].length; ++i)
			_time_comboBoxes[ index][ i].setEnabled( enables[ 0]);

		for ( int i = 0; i < _time_labels[ index].length; ++i)
			_time_labels[ index][ i].setEnabled( enables[ 0]);

		_time_variable_comboBoxes[ index].setEnabled( enables[ 1]);
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

		if ( null == rule || !_type.equals( rule._type) || rule._value.equals( "")) {
			set_handler();
			return false;
		}

		String[] values = TimeCommand.get_values( rule._value);
		if ( null == values) {
			set_handler();
			return false;
		}

		if ( !set( values[ 0], values[ 1], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox)) {
			set_handler();
			return false;
		}

		_time_variable_comboBox.setSelectedItem( values[ 2]);

		set( values[ 3], _time_textFields[ 0], _time_comboBoxes[ 0], _time_variable_comboBoxes[ 0], _radioButtons[ 0]);

		_operator_comboBox.setSelectedItem( values[ 4]);

		if ( !values[ 4].equals( "") && null != values[ 5])
			set( values[ 5], _time_textFields[ 1], _time_comboBoxes[ 1], _time_variable_comboBoxes[ 1], _radioButtons[ 1]);

		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String time_variable = ( String)_time_variable_comboBox.getSelectedItem();
		if ( null == time_variable || time_variable.equals( ""))
			return null;

		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);

		String value = ( spot + "setTime " + time_variable + "=");

		String time = get( _time_textFields[ 0], _time_comboBoxes[ 0], _time_variable_comboBoxes[ 0], _radioButtons[ 0]);
		if ( null == time)
			return null;

		value += time;

		String operator = ( String)_operator_comboBox.getSelectedItem();
		if ( !operator.equals( "")) {
			time = get( _time_textFields[ 1], _time_comboBoxes[ 1], _time_variable_comboBoxes[ 1], _radioButtons[ 1]);
			if ( null == time)
				return null;

			value += ( " " + operator + " " + time);
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
//		String time_variable = ( String)_time_variable_comboBox.getSelectedItem();
//		if ( null == time_variable || time_variable.equals( ""))
//			return false;
//
//		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
//
//		String value = ( spot + "setTime " + time_variable + "=");
//
//		String time = get( _time_textFields[ 0], _time_comboBoxes[ 0], _time_variable_comboBoxes[ 0], _radioButtons[ 0]);
//		if ( null == time)
//			return false;
//
//		value += time;
//
//		String operator = ( String)_operator_comboBox.getSelectedItem();
//		if ( !operator.equals( "")) {
//			time = get( _time_textFields[ 1], _time_comboBoxes[ 1], _time_variable_comboBoxes[ 1], _radioButtons[ 1]);
//			if ( null == time)
//				return false;
//
//			value += ( " " + operator + " " + time);
//		}
//
//		rule._value = value;
//		rule._type = _type;
//
//		return true;
//	}
}
