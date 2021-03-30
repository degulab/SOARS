/*
 * 2005/08/03
 */
package soars.application.visualshell.object.role.base.edit.tab.command.list;

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
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.command.base.CollectionAndListCommandPropertyPageBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.command.ListCommand;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
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
public class ListCommandPropertyPage7 extends CollectionAndListCommandPropertyPageBase {

	/**
	 * 
	 */
	private TextField _set_all_keyword_textField = null;

	/**
	 * 
	 */
	private TextField _set_all_value_textField = null;

	/**
	 * 
	 */
	private JLabel _dummy3 = null;

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
	public ListCommandPropertyPage7(String title, String kind,
		String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);

		_radioButtons1 = new RadioButton[] {
			null, null, null, null
		};

		_label = new JLabel[] {
			null, null, null
		};
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

		setup_header( north_panel);

		insert_vertical_strut( north_panel);

		ButtonGroup buttonGroup1 = new ButtonGroup();

		setup_set_all( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_move_to_random_one( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_move_to_first( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_move_to_last( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		basic_panel.add( north_panel, "North");


		add( basic_panel);


		setup_apply_button();


		adjust();


		return true;
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_set_all(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.set.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_set_all_keyword_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_label[ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_set_all_value_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 0]);

		_label[ 1] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.label.set.all.1"),
			true);
		panel.add( _label[ 1]);

		_set_all_keyword_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters1), _standardControlWidth, false);
		panel.add( _set_all_keyword_textField);

		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3 = new JLabel();
		panel.add( _dummy3);

		_label[ 2] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.label.set.all.2"),
			true);
		panel.add( _label[ 2]);

		_set_all_value_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
		panel.add( _set_all_value_textField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_move_to_random_one(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.move.to.random.one"),
			buttonGroup1, true, false);
		_radioButtons1[ 1].setEnabled( _role instanceof AgentRole);
		panel.add( _radioButtons1[ 1]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_move_to_first(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.move.to.first"),
			buttonGroup1, true, false);
		_radioButtons1[ 2].setEnabled( _role instanceof AgentRole);
		panel.add( _radioButtons1[ 2]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_move_to_last(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.move.to.last"),
			buttonGroup1, true, false);
		_radioButtons1[ 3].setEnabled( _role instanceof AgentRole);
		panel.add( _radioButtons1[ 3]);

		parent.add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.command.base.CollectionAndListCommandPropertyPageBase#adjust()
	 */
	protected void adjust() {
		super.adjust();

		_dummy3.setPreferredSize( new Dimension(
			_dummy1.getPreferredSize().width,
			_dummy3.getPreferredSize().height));
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

		CommonTool.update( _comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _comboBox, get_spot_list_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(spotObject, number, objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
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
			true, false, false, false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_label[ 0].setEnabled( enables[ 0]);
		_set_all_keyword_textField.setEnabled( enables[ 0]);
		_label[ 1].setEnabled( enables[ 0]);
		_set_all_value_textField.setEnabled( enables[ 0]);
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

		int kind = ListCommand.get_kind( rule._value);
		if ( 46 > kind || 49 < kind) {
			set_handler();
			return false;
		}

		_radioButtons1[ kind - 46].setSelected( true);

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
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

		if ( !set( values[ 0], values[ 1], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox)) {
			set_handler();
			return false;
		}

		switch ( kind) {
			case 46:
				set4( _comboBox, _set_all_keyword_textField, _set_all_value_textField, values[ 2], elements[ 1], elements[ 2]);
				break;
			case 47:
				_comboBox.setSelectedItem( values[ 2]);
				break;
			case 48:
				_comboBox.setSelectedItem( values[ 2]);
				break;
			case 49:
				_comboBox.setSelectedItem( values[ 2]);
				break;
		}

		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String value = null;
		int kind = ( 46 + SwingTool.get_enabled_radioButton( _radioButtons1));
		switch ( kind) {
			case 46:
				if ( null == _set_all_keyword_textField.getText()
					|| _set_all_keyword_textField.getText().equals( "")
					|| _set_all_keyword_textField.getText().equals( "$")
					|| 0 < _set_all_keyword_textField.getText().indexOf( '$')
					|| _set_all_keyword_textField.getText().equals( "$Name")
					|| _set_all_keyword_textField.getText().equals( "$Role")
					|| _set_all_keyword_textField.getText().equals( "$Spot")
					|| 0 <= _set_all_keyword_textField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _set_all_keyword_textField.getText().startsWith( "$")
					&& ( 0 < _set_all_keyword_textField.getText().indexOf( "$", 1)
					|| 0 < _set_all_keyword_textField.getText().indexOf( ")", 1)))
					return null;

				if ( null == _set_all_value_textField.getText()
					|| _set_all_value_textField.getText().equals( "")
					|| _set_all_value_textField.getText().equals( "$")
					|| 0 < _set_all_value_textField.getText().indexOf( '$')
					|| _set_all_value_textField.getText().startsWith( " ")
					|| _set_all_value_textField.getText().endsWith( " ")
					|| _set_all_value_textField.getText().equals( "$Name")
					|| _set_all_value_textField.getText().equals( "$Role")
					|| _set_all_value_textField.getText().equals( "$Spot")
					|| 0 <= _set_all_value_textField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _set_all_value_textField.getText().startsWith( "$")
					&& ( 0 <= _set_all_value_textField.getText().indexOf( " ")
					|| 0 < _set_all_value_textField.getText().indexOf( "$", 1)
					|| 0 < _set_all_value_textField.getText().indexOf( ")", 1)))
					return null;

				value = get5( _comboBox, _set_all_keyword_textField, _set_all_value_textField);
				break;
			case 47:
				value = get1( _comboBox);
				break;
			case 48:
				value = get1( _comboBox);
				break;
			case 49:
				value = get1( _comboBox);
				break;
			default:
				return null;
		}

		if ( null == value)
			return null;

		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);

		return Rule.create( _kind, _type, ListCommand._reservedWords[ kind] + spot + value);
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		String value = null;
//		int kind = ( 39 + SwingTool.get_enabled_radioButton( _radioButtons1));
//		switch ( kind) {
//			case 39:
//				if ( null == _set_all_keyword_textField.getText()
//					|| _set_all_keyword_textField.getText().equals( "")
//					|| _set_all_keyword_textField.getText().equals( "$")
//					|| 0 < _set_all_keyword_textField.getText().indexOf( '$')
//					|| _set_all_keyword_textField.getText().equals( "$Name")
//					|| _set_all_keyword_textField.getText().equals( "$Role")
//					|| _set_all_keyword_textField.getText().equals( "$Spot")
//					|| 0 <= _set_all_keyword_textField.getText().indexOf( Constant._experiment_name))
//					return false;
//
//				if ( _set_all_keyword_textField.getText().startsWith( "$")
//					&& ( 0 < _set_all_keyword_textField.getText().indexOf( "$", 1)
//					|| 0 < _set_all_keyword_textField.getText().indexOf( ")", 1)))
//					return false;
//
//				if ( null == _set_all_value_textField.getText()
//					|| _set_all_value_textField.getText().equals( "")
//					|| _set_all_value_textField.getText().equals( "$")
//					|| 0 < _set_all_value_textField.getText().indexOf( '$')
//					|| _set_all_value_textField.getText().startsWith( " ")
//					|| _set_all_value_textField.getText().endsWith( " ")
//					|| _set_all_value_textField.getText().equals( "$Name")
//					|| _set_all_value_textField.getText().equals( "$Role")
//					|| _set_all_value_textField.getText().equals( "$Spot")
//					|| 0 <= _set_all_value_textField.getText().indexOf( Constant._experiment_name))
//					return false;
//
//				if ( _set_all_value_textField.getText().startsWith( "$")
//					&& ( 0 <= _set_all_value_textField.getText().indexOf( " ")
//					|| 0 < _set_all_value_textField.getText().indexOf( "$", 1)
//					|| 0 < _set_all_value_textField.getText().indexOf( ")", 1)))
//					return false;
//
//				value = get5( _comboBox, _set_all_keyword_textField, _set_all_value_textField);
//				break;
//			case 40:
//				value = get1( _comboBox);
//				break;
//			case 41:
//				value = get1( _comboBox);
//				break;
//			case 42:
//				value = get1( _comboBox);
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
//		rule._value = ( ListCommandRuleManipulator._reserved_words[ kind] + spot + value);
//		rule._type = _type;
//
//		return true;
//	}
}
