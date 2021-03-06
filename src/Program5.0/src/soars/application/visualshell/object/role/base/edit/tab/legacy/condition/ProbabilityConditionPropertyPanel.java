/*
 * 2005/08/26
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.condition;

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
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.condition.ProbabilityCondition;
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
public class ProbabilityConditionPropertyPanel extends RulePropertyPanelBase {

	/**
	 * 
	 */
	private CheckBox _spotCheckBox = null;

	/**
	 * 
	 */
	private ObjectSelector _spotSelector = null;

	/**
	 * 
	 */
	private CheckBox _spotVariableCheckBox = null;

	/**
	 * 
	 */
	private ComboBox _spotVariableComboBox = null;

	/**
	 * 
	 */
	private CheckBox _denyCheckBox = null;

	/**
	 * 
	 */
	private JLabel _probabilityLabel = null;

	/**
	 * 
	 */
	private ComboBox _probabilityComboBox = null;

	/**
	 * 
	 */
	private RadioButton[] _radioButtons1 = new RadioButton[] {
		null, null, null
	};

	/**
	 * 
	 */
	private ComboBox _numberObjectComboBox = null;

	/**
	 * 
	 */
	private TextField _probabilityValueTextField = null;

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
	public ProbabilityConditionPropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_create()
	 */
	@Override
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		setLayout( new BorderLayout());


		JPanel basicPanel = new JPanel();
		basicPanel.setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_spot_checkBox_and_spot_selector( northPanel);

		insert_vertical_strut( northPanel);

		setup_spot_variable_checkBox_and_spot_variable_comboBox( northPanel);

		insert_vertical_strut( northPanel);

		setup_probability_comboBox( northPanel);

		insert_vertical_strut( northPanel);

		ButtonGroup buttonGroup1 = new ButtonGroup();

		setup_number_object_comboBox( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_probability_value_textField( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_probability_existing_value_radio_button( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		basicPanel.add( northPanel, "North");


		add( basicPanel);


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

		_spotCheckBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.probability.spot.check.box.name"),
			false, true);
		_spotCheckBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_spotSelector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				on_update( ItemEvent.SELECTED == arg0.getStateChange(),
					_spotSelector,
					_spotVariableCheckBox,
					_spotVariableComboBox);
			}
		});
		panel.add( _spotCheckBox);

		_spotSelector = create_spot_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_spot_variable_checkBox_and_spot_variable_comboBox( JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_spotVariableCheckBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.spot.variable.check.box.name"),
			true, true);
		_spotVariableCheckBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				on_update( _spotCheckBox.isSelected(),
					_spotSelector,
					_spotVariableCheckBox,
					_spotVariableComboBox);
			}
		});
		panel.add( _spotVariableCheckBox);

		_spotVariableComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _spotVariableComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_probability_comboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_denyCheckBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.check.box.denial"),
			true, false);
		panel.add( _denyCheckBox);

		_probabilityLabel = create_label(
			"  " + ResourceManager.get_instance().get( "edit.rule.dialog.condition.probability.label"),
			false);
		panel.add( _probabilityLabel);

		_probabilityComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _probabilityComboBox);

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
				_numberObjectComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 0]);

		_numberObjectComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _numberObjectComboBox);

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
				_probabilityValueTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 1]);

		_probabilityValueTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters5), true);
		panel.add( _probabilityValueTextField);

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
		int width1 = ( _denyCheckBox.getPreferredSize().width
			+ _probabilityLabel.getPreferredSize().width + 5);

		int width2 = _spotCheckBox.getPreferredSize().width;

		width2 = Math.max( width2, _spotVariableCheckBox.getPreferredSize().width);

		for ( int i = 0; i < _radioButtons1.length; ++i)
			width2 = Math.max( width2, _radioButtons1[ i].getPreferredSize().width);

		int width;
		if ( width1 > width2)
			width = width1;
		else {
			width = width2;
			_probabilityLabel.setPreferredSize( new Dimension(
				_probabilityLabel.getPreferredSize().width + ( width2 - width1),
				_probabilityLabel.getPreferredSize().height));
		}

		_spotCheckBox.setPreferredSize( new Dimension( width,
			_spotCheckBox.getPreferredSize().height));

		_spotVariableCheckBox.setPreferredSize( new Dimension( width,
			_spotVariableCheckBox.getPreferredSize().height));

		Dimension dimension = new Dimension( width,
			_radioButtons1[ 0].getPreferredSize().height);
		for ( int i = 0; i < _radioButtons1.length; ++i)
			_radioButtons1[ i].setPreferredSize( dimension);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#reset(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void reset(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		CommonTool.update( spotVariableComboBox, !_spotCheckBox.isSelected() ? get_agent_spot_variable_names( false) : get_spot_spot_variable_names( false));

		super.reset(objectSelector, spotVariableCheckBox, spotVariableComboBox);

		objectSelector.setEnabled( _spotCheckBox.isSelected());

		CommonTool.update( _probabilityComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_probability_names( false) : get_spot_probability_names( false));
		CommonTool.update( _numberObjectComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _probabilityComboBox, get_spot_probability_names( false));
		CommonTool.update( _numberObjectComboBox, get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(spotObject, number, objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _probabilityComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
		CommonTool.update( _numberObjectComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	protected void update(ObjectSelector objectSelector) {
		update( objectSelector, _spotVariableCheckBox, _spotVariableComboBox);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector) {
		update( spotObject, number, objectSelector, _spotVariableCheckBox, _spotVariableComboBox);
	}

	/**
	 * 
	 */
	private void initialize() {
		if ( _role instanceof AgentRole) {
			_spotCheckBox.setSelected( false);
			_spotSelector.setEnabled( false);
		} else {
			_spotCheckBox.setSelected( true);
			_spotCheckBox.setEnabled( false);
			_spotSelector.setEnabled( true);
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
		_numberObjectComboBox.setEnabled( enables[ 0]);

		_probabilityValueTextField.setEnabled( enables[ 1]);
	}

	/**
	 * 
	 */
	private void set_handler() {
		_spotSelector.set_handler( this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#on_setup_completed()
	 */
	@Override
	public void on_setup_completed() {
		reset( _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);
		super.on_setup_completed();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_ok(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_ok(ActionEvent actionEvent) {
		_parent.on_apply( this, actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#set(soars.application.visualshell.object.role.base.rule.base.Rule)
	 */
	@Override
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

		if ( !set( values[ 0], values[ 1], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox)) {
			set_handler();
			return false;
		}

		_probabilityComboBox.setSelectedItem( values[ 2]);

		if ( 2 > elements.length)
			_radioButtons1[ 2].setSelected( true);
		else {
			if ( ( spot.equals( "") && LayerManager.get_instance().is_agent_object_name( "number object", elements[ 1]))
				|| ( spot.equals( "<>") && LayerManager.get_instance().is_spot_object_name( "number object", elements[ 1]))
				|| LayerManager.get_instance().is_spot_object_name( "number object", spot, elements[ 1])) {
				_numberObjectComboBox.setSelectedItem( elements[ 1]);
				_radioButtons1[ 0].setSelected( true);
			} else {
				_probabilityValueTextField.setText( elements[ 1]);
				_radioButtons1[ 1].setSelected( true);
			}
		}

		_denyCheckBox.setSelected( rule._value.startsWith( "!"));

		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String value = null;
		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);
		switch ( kind) {
			case 0:
				value = get2( _probabilityComboBox, _numberObjectComboBox);
				break;
			case 1:
				value = get_probability();
				break;
			case 2:
				value = ( String)_probabilityComboBox.getSelectedItem();
				if ( null == value || value.equals( ""))
					return null;

				value += "=";
				break;
			default:
				return null;
		}

		if ( null == value)
			return null;

		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);

		return Rule.create( _kind, _type,
			( _denyCheckBox.isSelected() ? "!" : "") + ( ProbabilityCondition._reservedWord + spot + value));
	}

	/**
	 * @return
	 */
	private String get_probability() {
		if ( null == _probabilityValueTextField.getText()
			|| _probabilityValueTextField.getText().equals( "")
			|| _probabilityValueTextField.getText().equals( "$")
			|| 0 < _probabilityValueTextField.getText().indexOf( '$')
			|| _probabilityValueTextField.getText().equals( "$Name")
			|| _probabilityValueTextField.getText().equals( "$Role")
			|| _probabilityValueTextField.getText().equals( "$Spot")
			|| 0 <= _probabilityValueTextField.getText().indexOf( Constant._experimentName))
			return null;

		if ( _probabilityValueTextField.getText().startsWith( "$")
			&& 0 < _probabilityValueTextField.getText().indexOf( "$", 1)
			|| 0 < _probabilityValueTextField.getText().indexOf( ")", 1))
			return null;

		String text0 = ( String)_probabilityComboBox.getSelectedItem();
		if ( null == text0 || text0.equals( ""))
			return null;

		String text1 = _probabilityValueTextField.getText();
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
