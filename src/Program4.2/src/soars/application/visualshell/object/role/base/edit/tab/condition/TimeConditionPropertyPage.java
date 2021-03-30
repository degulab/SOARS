/*
 * 2005/07/27
 */
package soars.application.visualshell.object.role.base.edit.tab.condition;

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
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.condition.TimeCondition;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;

/**
 * @author kurata
 */
public class TimeConditionPropertyPage extends TimePropertyPage {

	/**
	 * 
	 */
	private JLabel _type_label = null;

	/**
	 * 
	 */
	private ComboBox _type_comboBox = null;

	/**
	 * 
	 */
	private CheckBox _denial_checkBox = null;

	/**
	 * 
	 */
	private RadioButton[][] _radioButtons = new RadioButton[][] {
		{ null, null},
		{ null, null}
	};

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
	private ComboBox[] _time_variable_comboBoxes = new ComboBox[] {
		null, null
	};

	/**
	 * 
	 */
	private JLabel[] _labels = new JLabel[] {
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
	public TimeConditionPropertyPage(String title, String kind,
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

		setup_type_comboBox( north_panel);

		insert_vertical_strut( north_panel);

		ButtonGroup[] buttonGroups = new ButtonGroup[] { new ButtonGroup(), new ButtonGroup()};

		setup_time_components( buttonGroups, north_panel);

		insert_vertical_strut( north_panel);

		setup_time_variable_components( buttonGroups, north_panel);

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
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.spot.check.box.name"),
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
	private void setup_type_comboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));


		_type_label = create_label( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.condition.type"), true);
		panel.add( _type_label);


		_type_comboBox = create_comboBox(
			new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.at.the.specified.time"),
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.after.the.specified.time"),
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.before.the.specified.time"),
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.between.start.time.and.stop.time")
			}, false);
		_type_comboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				update_components();
			}
		});
		panel.add( _type_comboBox);


		_denial_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.check.box.denial"),
			true, false);
		panel.add( _denial_checkBox);


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
				_labels[ 0] = create_label( " - ", false);
				panel.add( _labels[ 0]);
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
			( ( 0 == index ) ? ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.time.radio.button") : null),
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
	private void setup_time_variable_components(ButtonGroup[] buttonGroups, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			setup_time_variable_radioButtons( i, buttonGroups, panel);
			setup_time_variable_comboBox( i, panel);
			if ( 0 == i) {
				_labels[ 1] = create_label( " - ", false);
				panel.add( _labels[ 1]);
			}
		}

		parent.add( panel);
	}

	/**
	 * @param index
	 * @param buttonGroups
	 * @param panel
	 */
	private void setup_time_variable_radioButtons(final int index, ButtonGroup[] buttonGroups, JPanel panel) {
		_radioButtons[ index][ 1] = create_radioButton(
			( ( 0 == index ) ? ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.time.variable.radio.button") : null),
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
	private void setup_time_variable_comboBox(int index, JPanel panel) {
		_time_variable_comboBoxes[ index] = create_comboBox( null, false);
		panel.add( _time_variable_comboBoxes[ index]);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _spot_checkBox.getPreferredSize().width;
		width = Math.max( width, _spot_variable_checkBox.getPreferredSize().width);
		width = Math.max( width, _type_label.getPreferredSize().width);
		for ( int i = 0; i < _radioButtons[ 0].length; ++i)
			width = Math.max( width, _radioButtons[ 0][ i].getPreferredSize().width);

		_spot_checkBox.setPreferredSize( new Dimension( width, _spot_checkBox.getPreferredSize().height));
		_spot_variable_checkBox.setPreferredSize( new Dimension( width, _spot_variable_checkBox.getPreferredSize().height));
		_type_label.setPreferredSize( new Dimension( width, _type_label.getPreferredSize().height));
		for ( int i = 0; i < _radioButtons[ 0].length; ++i)
			_radioButtons[ 0][ i].setPreferredSize( new Dimension( width, _radioButtons[ 0][ i].getPreferredSize().height));


		width = _time_textFields[ 0].getPreferredSize().width
			+ _time_labels[ 0][ 0].getPreferredSize().width
			+ _time_comboBoxes[ 0][ 0].getPreferredSize().width
			+ _time_labels[ 0][ 1].getPreferredSize().width
			+ _time_comboBoxes[ 0][ 1].getPreferredSize().width
			+ 20;

		_spot_selector.setWidth( width);
		_spot_variable_comboBox.setPreferredSize( new Dimension( width, _spot_variable_comboBox.getPreferredSize().height));
		_type_comboBox.setPreferredSize( new Dimension( width, _type_comboBox.getPreferredSize().height));
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

		_type_comboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.at.the.specified.time"));
	}

	/**
	 * 
	 */
	private void update_components() {
		String type = ( String)_type_comboBox.getSelectedItem();

		_denial_checkBox.setEnabled(
			type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.at.the.specified.time")));

		if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.at.the.specified.time")))
			update_components( new boolean[] { true, false, false});
		else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.after.the.specified.time")))
			update_components( new boolean[] { true, false, true});
		else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.before.the.specified.time")))
			update_components( new boolean[] { false, true, true});
		else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.between.start.time.and.stop.time")))
			update_components( new boolean[] { true, true, true});
	}

	/**
	 * @param enables
	 */
	protected void update_components(boolean[] enables) {
		for ( int i = 0; i < _radioButtons.length; ++i) {
			_radioButtons[ i][ 0].setEnabled( enables[ i]);
			_time_textFields[ i].setEnabled( enables[ i] && _radioButtons[ i][ 0].isSelected());
			_time_labels[ i][ 0].setEnabled( enables[ i] && _radioButtons[ i][ 0].isSelected());
			_time_comboBoxes[ i][ 0].setEnabled( enables[ i] && _radioButtons[ i][ 0].isSelected());
			_time_labels[ i][ 1].setEnabled( enables[ i] && _radioButtons[ i][ 0].isSelected());
			_time_comboBoxes[ i][ 1].setEnabled( enables[ i] && _radioButtons[ i][ 0].isSelected());
			_radioButtons[ i][ 1].setEnabled( enables[ i] && 0 < _time_variable_comboBoxes[ 0].getItemCount());
			_time_variable_comboBoxes[ i].setEnabled( enables[ i] && _radioButtons[ i][ 1].isSelected() && 0 < _time_variable_comboBoxes[ 0].getItemCount());
		}

		if ( 0 >= _time_variable_comboBoxes[ 0].getItemCount()) {
			for ( int i = 0; i < _radioButtons.length; ++i)
				_radioButtons[ i][ 0].setSelected( true);
		}

		_labels[ 0].setEnabled( enables[ 2]);
		_labels[ 1].setEnabled( enables[ 2] && 0 < _time_variable_comboBoxes[ 0].getItemCount());
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

		int kind = TimeCondition.get_kind( rule);
		if ( 0 > kind || _type_comboBox.getItemCount() <= kind) {
			set_handler();
			return false;
		}

		_type_comboBox.setSelectedIndex( kind);

		switch ( kind) {
			case 0:
				if ( !set( kind, rule._value, _time_textFields[ 0], _time_comboBoxes[ 0], _time_variable_comboBoxes[ 0], _radioButtons[ 0])) {
					set_handler();
					return false;
				}
				_denial_checkBox.setSelected( rule._value.startsWith( "!"));
				break;
			case 1:
				if ( !set( kind, rule._value, _time_textFields[ 0], _time_comboBoxes[ 0], _time_variable_comboBoxes[ 0], _radioButtons[ 0])) {
					set_handler();
					return false;
				}
				break;
			case 2:
				if ( !set( kind, rule._value, _time_textFields[ 1], _time_comboBoxes[ 1], _time_variable_comboBoxes[ 1], _radioButtons[ 1])) {
					set_handler();
					return false;
				}
				break;
			case 3:
				if ( !set( kind, rule._value)) {
					set_handler();
					return false;
				}
				break;
		}

		set_handler();

		return true;
	}

	/**
	 * @param kind
	 * @param value
	 * @return
	 */
	private boolean set(int kind, String value) {
		String[] elements = value.split( " && ");
		if ( null == elements || 2 != elements.length
			|| null == elements[ 0] || elements[ 0].equals( "")
			|| null == elements[ 1] || elements[ 1].equals( ""))
			return false;

		if ( !set( kind, elements[ 0], _time_textFields[ 0], _time_comboBoxes[ 0], _time_variable_comboBoxes[ 0], _radioButtons[ 0]))
			return false;

		if ( !set( kind, elements[ 1], _time_textFields[ 1], _time_comboBoxes[ 1], _time_variable_comboBoxes[ 1], _radioButtons[ 1]))
			return false;

		return true;
	}

	/**
	 * @param kind
	 * @param value
	 * @param time_textField
	 * @param time_comboBoxes
	 * @param time_variable_comboBox
	 * @param radioButtons
	 * @return
	 */
	private boolean set(int kind, String value, TextField time_textField, ComboBox[] time_comboBoxes, ComboBox time_variable_comboBox, RadioButton[] radioButtons) {
		String[] spots = CommonRuleManipulator.get_spot( value);
		if ( null == spots)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 != elements.length)
			return false;

		if ( !set( spots[ 0], spots[ 1], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox))
			return false;

		set( ( ( 0 == kind) ? elements[ 0].substring( "@".length()) : elements[ 0]), time_textField, time_comboBoxes, time_variable_comboBox, radioButtons);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);

		String type = ( String)_type_comboBox.getSelectedItem();

		if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.at.the.specified.time"))) {
			String value = get( _time_textFields[ 0], _time_comboBoxes[ 0], _time_variable_comboBoxes[ 0], _radioButtons[ 0]);
			if ( null == value)
				return null;

			return Rule.create( _kind, _type, ( _denial_checkBox.isSelected() ? "!" : "") + spot + "isTime @" + value);
		} else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.after.the.specified.time"))) {
			String value = get( _time_textFields[ 0], _time_comboBoxes[ 0], _time_variable_comboBoxes[ 0], _radioButtons[ 0]);
			if ( null == value)
				return null;

			return Rule.create( _kind, _type, spot + "isTime " + value);
		} else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.before.the.specified.time"))) {
			String value = get( _time_textFields[ 1], _time_comboBoxes[ 1], _time_variable_comboBoxes[ 1], _radioButtons[ 1]);
			if ( null == value)
				return null;

			return Rule.create( _kind, _type, "!" + spot + "isTime " + value);
		} else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.between.start.time.and.stop.time"))) {
			String value1 = get( _time_textFields[ 0], _time_comboBoxes[ 0], _time_variable_comboBoxes[ 0], _radioButtons[ 0]);
			if ( null == value1)
				return null;

			String value2 = get( _time_textFields[ 1], _time_comboBoxes[ 1], _time_variable_comboBoxes[ 1], _radioButtons[ 1]);
			if ( null == value1)
				return null;

			return Rule.create( _kind, _type, spot + "isTime " + value1 + " && !" + spot + "isTime " + value2);
		} else
			return null;
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
//
//		String type = ( String)_type_comboBox.getSelectedItem();
//
//		if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.at.the.specified.time"))) {
//			String value = get( _time_textFields[ 0], _time_comboBoxes[ 0], _time_variable_comboBoxes[ 0], _radioButtons[ 0]);
//			if ( null == value)
//				return false;
//
//			rule._value = ( _denial_checkBox.isSelected() ? "!" : "") + spot + "isTime @" + value;
//		} else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.after.the.specified.time"))) {
//			String value = get( _time_textFields[ 0], _time_comboBoxes[ 0], _time_variable_comboBoxes[ 0], _radioButtons[ 0]);
//			if ( null == value)
//				return false;
//
//			rule._value = spot + "isTime " + value;
//		} else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.before.the.specified.time"))) {
//			String value = get( _time_textFields[ 1], _time_comboBoxes[ 1], _time_variable_comboBoxes[ 1], _radioButtons[ 1]);
//			if ( null == value)
//				return false;
//
//			rule._value = "!" + spot + "isTime " + value;
//		} else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.between.start.time.and.stop.time"))) {
//			String value1 = get( _time_textFields[ 0], _time_comboBoxes[ 0], _time_variable_comboBoxes[ 0], _radioButtons[ 0]);
//			if ( null == value1)
//				return false;
//
//			String value2 = get( _time_textFields[ 1], _time_comboBoxes[ 1], _time_variable_comboBoxes[ 1], _radioButtons[ 1]);
//			if ( null == value1)
//				return false;
//
//			rule._value = spot + "isTime " + value1 + " && !" + spot + "isTime " + value2;
//		} else
//			return false;
//
//		rule._type = _type;
//
//		return true;
//	}
}
