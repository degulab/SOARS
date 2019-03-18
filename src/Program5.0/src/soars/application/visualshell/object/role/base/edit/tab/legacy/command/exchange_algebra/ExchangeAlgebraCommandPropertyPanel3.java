/*
 * Created on 2006/02/15
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.command.exchange_algebra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.exchange_algebra.panel.ValuePanel;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.command.ExchangeAlgebraCommand;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class ExchangeAlgebraCommandPropertyPanel3 extends ExchangeAlgebraCommandPropertyPanelBase {

	/**
	 * 
	 */
	private ValuePanel _valuePanel = null;

	/**
	 * 
	 */
	private ComboBox _copyExchangeAlgebraComboBox = null;

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
	public ExchangeAlgebraCommandPropertyPanel3(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
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

		setup_spotCheckBox_and_spotSelector( northPanel);

		insert_vertical_strut( northPanel);

		setup_spotVariableCheckBox_and_spotVariableComboBox( northPanel);

		insert_vertical_strut( northPanel);

		setup_header1( northPanel);

		insert_vertical_strut( northPanel);

		ButtonGroup buttonGroup1 = new ButtonGroup();

		setup_clear_value( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_bar( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		if ( !setup_multiple( buttonGroup1, northPanel))
			return false;

		insert_vertical_strut( northPanel);

		setup_hat( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_copy( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		basicPanel.add( northPanel, "North");


		add( basicPanel);


		setup_apply_button();


		adjust();


		return true;
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_clear_value(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		RadioButton radioButton = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.clear"),
			buttonGroup1, true, false);
		panel.add( radioButton);
		_radioButtons1.add( radioButton);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_bar(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		RadioButton radioButton = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.bar.operation"),
			buttonGroup1, true, false);
		panel.add( radioButton);
		_radioButtons1.add( radioButton);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 * @return
	 */
	private boolean setup_multiple(ButtonGroup buttonGroup1, JPanel parent) {
		RadioButton radioButton = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.multiple"),
			buttonGroup1, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_valuePanel.setEnabled( ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		_radioButtons1.add( radioButton);

		_valuePanel = new ValuePanel( true, this);
		if ( !_valuePanel.create( radioButton))
			return false;

		parent.add( _valuePanel);

		return true;
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_hat(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		RadioButton radioButton = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.hat.operation"),
			buttonGroup1, true, false);
		panel.add( radioButton);
		_radioButtons1.add( radioButton);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_copy(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		RadioButton radioButton = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.copy"),
			buttonGroup1, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_labels.get( 1).setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_copyExchangeAlgebraComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( radioButton);
		_radioButtons1.add( radioButton);

		JLabel label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.copy.exchange.algebra"),
			true);
		panel.add( label);
		_labels.add( label);

		_copyExchangeAlgebraComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _copyExchangeAlgebraComboBox);

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width1 = 0;
		for ( RadioButton radioButton:_radioButtons1)
			width1 = Math.max( width1, radioButton.getPreferredSize().width);
		width1 = _valuePanel.get_margin_width( width1);

		for ( RadioButton radioButton:_radioButtons1)
			radioButton.setPreferredSize( new Dimension( width1, radioButton.getPreferredSize().height));
		_valuePanel.adjust_margin_width( width1);


		int width2 = _spotCheckBox.getPreferredSize().width;
		width2 = _spotVariableCheckBox.getPreferredSize().width;
		for ( JLabel label:_labels)
			width2 = Math.max( width2, label.getPreferredSize().width);
		width2 = _valuePanel.get_width( width2);

		_spotCheckBox.setPreferredSize( new Dimension( width2, _spotCheckBox.getPreferredSize().height));
		_spotVariableCheckBox.setPreferredSize( new Dimension( width2, _spotVariableCheckBox.getPreferredSize().height));
		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width2, label.getPreferredSize().height));
		_valuePanel.adjust( width2);


		_dummy1.setPreferredSize( new Dimension( 	width1, _dummy1.getPreferredSize().height));


		_dummy2.setPreferredSize( new Dimension( 	width1, _dummy2.getPreferredSize().height));


		_dummy3.setPreferredSize( new Dimension( 	width1, _dummy3.getPreferredSize().height));
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

		CommonTool.update( _exchangeAlgebraComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_exchange_algebra_names2( false) : get_spot_exchange_algebra_names2( false));
		_valuePanel.reset( _spotCheckBox, spotVariableCheckBox);
		CommonTool.update( _copyExchangeAlgebraComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_exchange_algebra_names2( false) : get_spot_exchange_algebra_names2( false));
	}


	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _exchangeAlgebraComboBox, get_spot_exchange_algebra_names2( false));
		_valuePanel.update();
		CommonTool.update( _copyExchangeAlgebraComboBox, get_spot_exchange_algebra_names2( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(spotObject, number, objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _exchangeAlgebraComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "exchange algebra", number, false) : get_spot_exchange_algebra_names2( false));
		_valuePanel.update( spotObject, number, spotVariableCheckBox);
		CommonTool.update( _copyExchangeAlgebraComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "exchange algebra", number, false) : get_spot_exchange_algebra_names2( false));
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

		_radioButtons1.get( 0).setSelected( true);
		update_components( new boolean[] {
			true, false, false, false, false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_valuePanel.setEnabled( enables[ 2]);

		_labels.get( 1).setEnabled( enables[ 4]);
		_copyExchangeAlgebraComboBox.setEnabled( enables[ 4]);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.command.exchange_algebra.ExchangeAlgebraCommandPropertyPanelBase#set_handler()
	 */
	@Override
	protected void set_handler() {
		super.set_handler();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#on_setup_completed()
	 */
	@Override
	public void on_setup_completed() {
		super.on_setup_completed();
		_valuePanel.on_setup_completed();
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

		int kind = ExchangeAlgebraCommand.get_kind( rule._value);
		if ( 10 > kind || 15 < kind) {
			set_handler();
			return false;
		} else if ( 10 == kind)
			_radioButtons1.get( 0).setSelected( true);
		else if ( 11 == kind)
			_radioButtons1.get( 1).setSelected( true);
		else if ( 12 == kind || 13 == kind)
			_radioButtons1.get( 2).setSelected( true);
		else if ( 14 == kind)
			_radioButtons1.get( 3).setSelected( true);
		else if ( 15 == kind)
			_radioButtons1.get( 4).setSelected( true);
		else
			return false;

		String[] spots = CommonRuleManipulator.get_spot( rule._value);
		if ( null == spots) {
			set_handler();
			return false;
		}

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements) {
			set_handler();
			return false;
		}

		if ( !set( spots[ 0], spots[ 1], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox)) {
			set_handler();
			return false;
		}

		_exchangeAlgebraComboBox.setSelectedItem( elements[ 0]);

		switch ( kind) {
			case 10:
			case 11:
			case 14:
				break;
			case 12:
			case 13:
				if ( !_valuePanel.set( kind - 12, CommonRuleManipulator.get_full_prefix( rule._value), elements[ 1])) {
					set_handler();
					return false;
				}

				break;
			case 15:
				_copyExchangeAlgebraComboBox.setSelectedItem( elements[ 1]);
				break;
		}

		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);

		String exchangeAlgebra = ( String)_exchangeAlgebraComboBox.getSelectedItem();
		if ( null == exchangeAlgebra || exchangeAlgebra.equals( ""))
			return null;

		String value = null;
		switch ( kind) {
			case 0:
			case 1:
			case 3:
				value = "";
				break;
			case 2:
				String numeric = _valuePanel.get();
				if ( null == numeric)
					return null;

				value = ( ExchangeAlgebraCommand._delimiter + numeric);
				break;
			case 4:
				String copyExchangeAlgebra = ( String)_copyExchangeAlgebraComboBox.getSelectedItem();
				if ( null == copyExchangeAlgebra || copyExchangeAlgebra.equals( ""))
					return null;

				value = ( ExchangeAlgebraCommand._delimiter + copyExchangeAlgebra);
				break;
			default:
				return null;
		}

		if ( null == value)
			return null;


		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);

		int[] indices = new int[] { 10, 11, 12, 14, 15};
		return Rule.create( _kind, _type, spot + ExchangeAlgebraCommand._reservedWords[ indices[ kind]] + exchangeAlgebra + value);
	}
}
