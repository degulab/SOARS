/**
 * 
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
import soars.application.visualshell.object.role.base.object.condition.SpotCondition;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 *
 */
public class SpotConditionPropertyPage extends RulePropertyPageBase {

	/**
	 * 
	 */
	private CheckBox _deny_checkBox = null;

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
	private JLabel _dummy = null;

	/**
	 * 
	 */
	private CheckBox[] _spot_checkBoxes = new CheckBox[ 2];

	/**
	 * 
	 */
	private ObjectSelector[] _spot_selectors = new ObjectSelector[ 2];

	/**
	 * 
	 */
	private CheckBox[] _spot_variable_checkBoxes = new CheckBox[ 2];

	/**
	 * 
	 */
	private ComboBox[] _spot_variable_comboBoxes = new ComboBox[ 2];

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
	public SpotConditionPropertyPage(String title, String kind,
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

		setup_spot_selector0( north_panel);

		insert_vertical_strut( north_panel);

		setup_spot_selector1( north_panel);

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
	private void setup_spot_selector0(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_deny_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.check.box.denial"),
			true, true);
		panel.add( _deny_checkBox);

		setup_spot_selector( 0, panel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_spot_selector1(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_type_label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.type"),
			true);
		panel.add( _type_label);

		_type_comboBox = create_comboBox(
			new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.spot.condition"),
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.spot.comparison")
			}, false);
		_type_comboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				update_components();
			}
		});
		panel.add( _type_comboBox);

		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy = new JLabel();
		panel.add( _dummy);

		setup_spot_selector( 1, panel);

		parent.add( panel);
	}

	/**
	 * @param index
	 * @param panel
	 */
	private void setup_spot_selector(final int index, JPanel panel) {
		_spot_checkBoxes[ index] = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.spot.check.box.name"),
			true, true);
		_spot_checkBoxes[ index].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_spot_selectors[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				on_update( ItemEvent.SELECTED == arg0.getStateChange(),
					_spot_selectors[ index],
					_spot_variable_checkBoxes[ index],
					_spot_variable_comboBoxes[ index]);
			}
		});
		panel.add( _spot_checkBoxes[ index]);

		_spot_selectors[ index] = create_spot_selector( true, true, panel);


		_spot_variable_checkBoxes[ index] = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.spot.variable.check.box.name"),
			true, true);
		_spot_variable_checkBoxes[ index].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				on_update( _spot_checkBoxes[ index].isSelected(),
					_spot_selectors[ index],
					_spot_variable_checkBoxes[ index],
					_spot_variable_comboBoxes[ index]);
			}
		});
		panel.add( _spot_variable_checkBoxes[ index]);

		_spot_variable_comboBoxes[ index] = create_comboBox( null, _standardControlWidth, false);
		panel.add( _spot_variable_comboBoxes[ index]);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width1 = ( _deny_checkBox.getPreferredSize().width + _spot_checkBoxes[ 0].getPreferredSize().width + 5);
		int width2 = _type_label.getPreferredSize().width;
		if ( width1 > width2)
			_type_label.setPreferredSize( new Dimension( width1, _type_label.getPreferredSize().height));
		else if ( width1 < width2)
			_deny_checkBox.setPreferredSize(
				new Dimension( width2 - _spot_checkBoxes[ 0].getPreferredSize().width - 5,
					_deny_checkBox.getPreferredSize().height));

		_dummy.setPreferredSize( new Dimension( _deny_checkBox.getPreferredSize().width, _dummy.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#reset(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void reset(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( objectSelector.equals( _spot_selectors[ 0]))
			CommonTool.update( spot_variable_comboBox, !_spot_checkBoxes[ 0].isSelected() ? get_agent_spot_variable_names( false) : get_spot_spot_variable_names( false));
		else if ( objectSelector.equals( _spot_selectors[ 1]))
			CommonTool.update( spot_variable_comboBox, !_spot_checkBoxes[ 1].isSelected() ? get_agent_spot_variable_names( false) : get_spot_spot_variable_names( false));

		super.reset(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		if ( objectSelector.equals( _spot_selectors[ 0]))
			objectSelector.setEnabled( _spot_checkBoxes[ 0].isSelected());
		else
			objectSelector.setEnabled( _spot_checkBoxes[ 1].isSelected());
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selectors[ 0]) && !objectSelector.equals( _spot_selectors[ 1]))
			return;

		super.update(objectSelector, spot_variable_checkBox, spot_variable_comboBox);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selectors[ 0]) && !objectSelector.equals( _spot_selectors[ 1]))
			return;

		super.update(spotObject, number, objectSelector, spot_variable_checkBox, spot_variable_comboBox);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector)
	 */
	protected void update(ObjectSelector objectSelector) {
		if ( objectSelector.equals( _spot_selectors[ 0]))
			update( objectSelector, _spot_variable_checkBoxes[ 0], _spot_variable_comboBoxes[ 0]);
		else if ( objectSelector.equals( _spot_selectors[ 1]))
			update( objectSelector, _spot_variable_checkBoxes[ 1], _spot_variable_comboBoxes[ 1]);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector) {
		if ( objectSelector.equals( _spot_selectors[ 0]))
			update( spotObject, number, objectSelector, _spot_variable_checkBoxes[ 0], _spot_variable_comboBoxes[ 0]);
		else if ( objectSelector.equals( _spot_selectors[ 1]))
			update( spotObject, number, objectSelector, _spot_variable_checkBoxes[ 1], _spot_variable_comboBoxes[ 1]);
	}

	/**
	 * 
	 */
	private void initialize() {
		for ( int i = 0; i < 2; ++i) {
			if ( _role instanceof AgentRole) {
				_spot_checkBoxes[ i].setSelected( false);
				_spot_selectors[ i].setEnabled( false);
			} else {
				_spot_checkBoxes[ i].setSelected( true);
				_spot_checkBoxes[ i].setEnabled( false);
				_spot_selectors[ i].setEnabled( true);
			}
		}

		_type_comboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.spot.condition"));
	}

	/**
	 * 
	 */
	private void update_components() {
		String type = ( String)_type_comboBox.getSelectedItem();
		if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.spot.comparison"))) {
			if ( _role instanceof AgentRole)
				_spot_checkBoxes[ 1].setEnabled( true);

			_spot_selectors[ 1].setEnabled( true);
			on_update( _spot_checkBoxes[ 1].isSelected(),
				_spot_selectors[ 1],
				_spot_variable_checkBoxes[ 1],
				_spot_variable_comboBoxes[ 1]);
		} else {
			_spot_checkBoxes[ 1].setEnabled( false);
			_spot_selectors[ 1].setEnabled( false);
			_spot_variable_checkBoxes[ 1].setEnabled( false);
			_spot_variable_comboBoxes[ 1].setEnabled( false);
		}
	}

	/**
	 * 
	 */
	protected void set_handler() {
		for ( int i = 0; i < 2; ++i)
			_spot_selectors[ i].set_handler( this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#on_setup_completed()
	 */
	public void on_setup_completed() {
		for ( int i = 0; i < 2; ++i)
			reset( _spot_selectors[ i], _spot_variable_checkBoxes[ i], _spot_variable_comboBoxes[ i]);

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

		String[][] values = SpotCondition.get_values( rule._value);
		if ( null == values) {
			set_handler();
			return false;
		}

		if ( !set( values[ 0][ 0], values[ 0][ 1], _spot_checkBoxes[ 0], _spot_selectors[ 0], _spot_variable_checkBoxes[ 0], _spot_variable_comboBoxes[ 0])) {
			set_handler();
			return false;
		}

		if ( null != values[ 1]) {
			if ( !set( values[ 1][ 0], values[ 1][ 1], _spot_checkBoxes[ 1], _spot_selectors[ 1], _spot_variable_checkBoxes[ 1], _spot_variable_comboBoxes[ 1])) {
				set_handler();
				return false;
			}

			_type_comboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.spot.comparison"));
		}

		_deny_checkBox.setSelected( rule._value.startsWith( "!"));

		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String[] values = new String[] { null, null};

		values[ 0] = get( _spot_checkBoxes[ 0], _spot_selectors[ 0], _spot_variable_checkBoxes[ 0], _spot_variable_comboBoxes[ 0]);
		if ( values[ 0].equals( ""))
			return null;

		String type = ( String)_type_comboBox.getSelectedItem();
		if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.spot.comparison"))) {
			values[ 1] = get( _spot_checkBoxes[ 1], _spot_selectors[ 1], _spot_variable_checkBoxes[ 1], _spot_variable_comboBoxes[ 1]);
			if ( values[ 1].equals( ""))
				return null;
		}

		return Rule.create( _kind, _type,
			( _deny_checkBox.isSelected() ? "!" : "") + SpotCondition.get_rule_value( values));
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		String[] values = new String[] { null, null};
//
//		values[ 0] = get( _spot_checkBoxes[ 0], _spot_selectors[ 0], _spot_variable_checkBoxes[ 0], _spot_variable_comboBoxes[ 0]);
//		if ( values[ 0].equals( ""))
//			return false;
//
//		String type = ( String)_type_comboBox.getSelectedItem();
//		if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.spot.comparison"))) {
//			values[ 1] = get( _spot_checkBoxes[ 1], _spot_selectors[ 1], _spot_variable_checkBoxes[ 1], _spot_variable_comboBoxes[ 1]);
//			if ( values[ 1].equals( ""))
//				return false;
//		}
//
//		rule._value = ( ( _deny_checkBox.isSelected() ? "!" : "") + SpotConditionRuleManipulator.get_rule_value( values));
//		rule._type = _type;
//
//		return true;
//	}
}
