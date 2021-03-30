/*
 * 2005/08/26
 */
package soars.application.visualshell.object.role.base.edit.tab.condition;

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
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.base.object.number.NumberObject;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.condition.ProbabilityCondition;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class ProbabilityConditionPropertyPage extends RulePropertyPageBase {

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
	private CheckBox _deny_checkBox = null;

	/**
	 * 
	 */
	private JLabel _probability_label = null;

	/**
	 * 
	 */
	private ComboBox _probability_comboBox = null;

	/**
	 * 
	 */
	private RadioButton[] _radioButtons1 = new RadioButton[] {
		null, null, null
	};

	/**
	 * 
	 */
	private ComboBox _number_object_comboBox = null;

	/**
	 * 
	 */
	private TextField _probability_value_textField = null;

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
	public ProbabilityConditionPropertyPage(String title, String kind,
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

		setup_probability_comboBox( north_panel);

		insert_vertical_strut( north_panel);

		ButtonGroup buttonGroup1 = new ButtonGroup();

		setup_number_object_comboBox( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_probability_value_textField( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_probability_existing_value_radio_button( buttonGroup1, north_panel);

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
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.probability.spot.check.box.name"),
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
	private void setup_probability_comboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_deny_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.check.box.denial"),
			true, false);
		panel.add( _deny_checkBox);

		_probability_label = create_label(
			"  " + ResourceManager.get_instance().get( "edit.rule.dialog.condition.probability.label"),
			false);
		panel.add( _probability_label);

		_probability_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _probability_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_number_object_comboBox(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.probability.number.object"),
			buttonGroup1, true, false);
		_radioButtons1[ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_number_object_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 0]);

		_number_object_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _number_object_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_probability_value_textField(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.probability.value"),
			buttonGroup1, true, false);
		_radioButtons1[ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_probability_value_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 1]);

		_probability_value_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters5), true);
		panel.add( _probability_value_textField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_probability_existing_value_radio_button(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.probability.existing.value"),
			buttonGroup1, true, false);
		panel.add( _radioButtons1[ 2]);

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width1 = ( _deny_checkBox.getPreferredSize().width
			+ _probability_label.getPreferredSize().width + 5);

		int width2 = _spot_checkBox.getPreferredSize().width;

		width2 = Math.max( width2, _spot_variable_checkBox.getPreferredSize().width);

		for ( int i = 0; i < _radioButtons1.length; ++i)
			width2 = Math.max( width2, _radioButtons1[ i].getPreferredSize().width);

		int width;
		if ( width1 > width2)
			width = width1;
		else {
			width = width2;
			_probability_label.setPreferredSize( new Dimension(
				_probability_label.getPreferredSize().width + ( width2 - width1),
				_probability_label.getPreferredSize().height));
		}

		_spot_checkBox.setPreferredSize( new Dimension( width,
			_spot_checkBox.getPreferredSize().height));

		_spot_variable_checkBox.setPreferredSize( new Dimension( width,
			_spot_variable_checkBox.getPreferredSize().height));

		Dimension dimension = new Dimension( width,
			_radioButtons1[ 0].getPreferredSize().height);
		for ( int i = 0; i < _radioButtons1.length; ++i)
			_radioButtons1[ i].setPreferredSize( dimension);
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

		CommonTool.update( _probability_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_probability_names( false) : get_spot_probability_names( false));
		CommonTool.update( _number_object_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _probability_comboBox, get_spot_probability_names( false));
		CommonTool.update( _number_object_comboBox, get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(spotObject, number, objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _probability_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
		CommonTool.update( _number_object_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
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

		_radioButtons1[ 0].setSelected( true);
		update_components( new boolean[] {
			true, false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_number_object_comboBox.setEnabled( enables[ 0]);

		_probability_value_textField.setEnabled( enables[ 1]);
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

		String[] elements = CommonRuleManipulator.get_elements( rule._value, 1);
		if ( null == elements) {
			set_handler();
			return false;
		}

		String[] values = CommonRuleManipulator.get_spot_and_object( elements[ 0]);
		if ( null == values) {
			set_handler();
			return false;
		}

		if ( null == values[ 0] && _role instanceof SpotRole) {
			set_handler();
			return false;
		}

		String spot = CommonRuleManipulator.get_semantic_prefix( values);

		if ( !set( values[ 0], values[ 1], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox)) {
			set_handler();
			return false;
		}

		_probability_comboBox.setSelectedItem( values[ 2]);

		if ( 2 > elements.length)
			_radioButtons1[ 2].setSelected( true);
		else {
			if ( ( spot.equals( "") && LayerManager.get_instance().is_agent_object_name( "number object", elements[ 1]))
				|| ( spot.equals( "<>") && LayerManager.get_instance().is_spot_object_name( "number object", elements[ 1]))
				|| LayerManager.get_instance().is_spot_object_name( "number object", spot, elements[ 1])) {
				_number_object_comboBox.setSelectedItem( elements[ 1]);
				_radioButtons1[ 0].setSelected( true);
			} else {
				_probability_value_textField.setText( elements[ 1]);
				_radioButtons1[ 1].setSelected( true);
			}
		}

		_deny_checkBox.setSelected( rule._value.startsWith( "!"));

		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String value = null;
		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);
		switch ( kind) {
			case 0:
				value = get2( _probability_comboBox, _number_object_comboBox);
				break;
			case 1:
				value = get_probability();
				break;
			case 2:
				value = ( String)_probability_comboBox.getSelectedItem();
				if ( null == value || value.equals( ""))
					return null;

				value += "=";
				break;
			default:
				return null;
		}

		if ( null == value)
			return null;

		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);

		return Rule.create( _kind, _type,
			( _deny_checkBox.isSelected() ? "!" : "") + ( ProbabilityCondition._reserved_word + spot + value));
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		String value = null;
//		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);
//		switch ( kind) {
//			case 0:
//				value = get2( _probability_comboBox, _number_object_comboBox);
//				break;
//			case 1:
//				value = get_probability();
//				break;
//			case 2:
//				value = ( String)_probability_comboBox.getSelectedItem();
//				if ( null == value || value.equals( ""))
//					return false;
//
//				value += "=";
//				break;
//			default:
//				return false;
//		}
//
//		if ( null == value)
//			return false;
//
//		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
//
//		rule._value =  ( ( _deny_checkBox.isSelected() ? "!" : "")
//			+ ( ProbabilityConditionRuleManipulator._reserved_word + spot + value));
//		rule._type = _type;
//
//		return true;
//	}

	/**
	 * @return
	 */
	private String get_probability() {
		if ( null == _probability_value_textField.getText()
			|| _probability_value_textField.getText().equals( "")
			|| _probability_value_textField.getText().equals( "$")
			|| 0 < _probability_value_textField.getText().indexOf( '$')
			|| _probability_value_textField.getText().equals( "$Name")
			|| _probability_value_textField.getText().equals( "$Role")
			|| _probability_value_textField.getText().equals( "$Spot")
			|| 0 <= _probability_value_textField.getText().indexOf( Constant._experimentName))
			return null;

		if ( _probability_value_textField.getText().startsWith( "$")
			&& 0 < _probability_value_textField.getText().indexOf( "$", 1)
			|| 0 < _probability_value_textField.getText().indexOf( ")", 1))
			return null;

		String text0 = ( String)_probability_comboBox.getSelectedItem();
		if ( null == text0 || text0.equals( ""))
			return null;

		String text1 = _probability_value_textField.getText();
		if ( null == text1 || text1.equals( ""))
			return null;

		if ( !CommonTool.is_probability_correct( text1))
			return null;

		text1 = NumberObject.is_correct( text1, "real number");
		if ( null == text1)
			return null;

		return ( text0 + "=" + text1);
	}
}
