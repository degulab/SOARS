/*
 * Created on 2006/02/14
 */
package soars.application.visualshell.object.role.base.edit.tab.command.collection;

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
import soars.application.visualshell.object.role.base.object.command.CollectionCommand;
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
public class CollectionCommandPropertyPage3 extends CollectionAndListCommandPropertyPageBase {

	/**
	 * 
	 */
	private ComboBox _retain_all_collection_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _retain_all_list_comboBox = null;

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
	public CollectionCommandPropertyPage3(String title, String kind,
		String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);

		_radioButtons1 = new RadioButton[] {
			null, null, null, null,
			null, null
		};

		_label = new JLabel[] {
			null, null, null, null,
			null
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

		setup_remove_all_elements( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_remove_random_one( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_retain_all1( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_retain_all2( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_set_all( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_move_to_random_one( buttonGroup1, north_panel);

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
	private void setup_remove_all_elements(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.remove.all.elements"),
			buttonGroup1, true, false);
		panel.add( _radioButtons1[ 0]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_remove_random_one(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.remove.random.one"),
			buttonGroup1, true, false);
		panel.add( _radioButtons1[ 1]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_retain_all1(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.retain.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_retain_all_collection_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 2]);

		_label[ 1] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.label.retain.all"),
			true);
		panel.add( _label[ 1]);

		_retain_all_collection_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _retain_all_collection_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_retain_all2(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.retain.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_retain_all_list_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 3]);

		_label[ 2] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.label.retain.all"),
			true);
		panel.add( _label[ 2]);

		_retain_all_list_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _retain_all_list_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_set_all(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 4] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.set.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 4].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 3].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_set_all_keyword_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_label[ 4].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_set_all_value_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 4]);

		_label[ 3] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.label.set.all.1"),
			true);
		panel.add( _label[ 3]);

		_set_all_keyword_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters1), _standardControlWidth, false);
		panel.add( _set_all_keyword_textField);

		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3 = new JLabel();
		panel.add( _dummy3);

		_label[ 4] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.label.set.all.2"),
			true);
		panel.add( _label[ 4]);

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

		_radioButtons1[ 5] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.move.to.random.one"),
			buttonGroup1, true, false);
		_radioButtons1[ 5].setEnabled( _role instanceof AgentRole);
		panel.add( _radioButtons1[ 5]);

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

		CommonTool.update( _comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));

		CommonTool.update( _retain_all_collection_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));
		CommonTool.update( _retain_all_list_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _comboBox, get_spot_collection_names( false));

		CommonTool.update( _retain_all_collection_comboBox, get_spot_collection_names( false));
		CommonTool.update( _retain_all_list_comboBox, get_spot_list_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(spotObject, number, objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));

		CommonTool.update( _retain_all_collection_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));
		CommonTool.update( _retain_all_list_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
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
			true, false, false, false,
			false, false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_label[ 1].setEnabled( enables[ 2]);
		_retain_all_collection_comboBox.setEnabled( enables[ 2]);

		_label[ 2].setEnabled( enables[ 3]);
		_retain_all_list_comboBox.setEnabled( enables[ 3]);

		_label[ 3].setEnabled( enables[ 4]);
		_set_all_keyword_textField.setEnabled( enables[ 4]);
		_label[ 4].setEnabled( enables[ 4]);
		_set_all_value_textField.setEnabled( enables[ 4]);
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

		int kind = CollectionCommand.get_kind( rule._value);
		if ( 18 > kind) {
			set_handler();
			return false;
		} else if ( 18 <= kind && 23 >= kind)
			_radioButtons1[ kind - 18].setSelected( true);
		else
			return false;

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
			case 18:
				_comboBox.setSelectedItem( values[ 2]);
				break;
			case 19:
				_comboBox.setSelectedItem( values[ 2]);
				break;
			case 20:
				set1( _comboBox, _retain_all_collection_comboBox, values[ 2], elements[ 1]);
				break;
			case 21:
				set1( _comboBox, _retain_all_list_comboBox, values[ 2], elements[ 1]);
				break;
			case 22:
				set4( _comboBox, _set_all_keyword_textField, _set_all_value_textField, values[ 2], elements[ 1], elements[ 2]);
				break;
			case 23:
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
		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);
		if ( 6 > kind)
			kind += 18;
		else
			return null;

		String value = null;
		switch ( kind) {
			case 18:
				value = get1( _comboBox);
				break;
			case 19:
				value = get1( _comboBox);
				break;
			case 20:
				value = get2( _comboBox, _retain_all_collection_comboBox, false);
				break;
			case 21:
				value = get2( _comboBox, _retain_all_list_comboBox, false);
				break;
			case 22:
				if ( null == _set_all_keyword_textField.getText()
					|| _set_all_keyword_textField.getText().equals( "")
					|| _set_all_keyword_textField.getText().equals( "$")
					|| 0 < _set_all_keyword_textField.getText().indexOf( '$'))
					return null;

				if ( _set_all_keyword_textField.getText().startsWith( "$")
					&& 0 < _set_all_keyword_textField.getText().indexOf( "$", 1))
					return null;

				if ( null == _set_all_value_textField.getText()
					|| _set_all_value_textField.getText().equals( "")
					|| _set_all_value_textField.getText().equals( "$")
					|| 0 < _set_all_value_textField.getText().indexOf( '$')
					|| _set_all_value_textField.getText().startsWith( " ")
					|| _set_all_value_textField.getText().endsWith( " "))
					return null;

				if ( _set_all_value_textField.getText().startsWith( "$")
					&& ( 0 <= _set_all_value_textField.getText().indexOf( " ")
					|| 0 < _set_all_value_textField.getText().indexOf( "$", 1)))
					return null;

				value = get5( _comboBox, _set_all_keyword_textField, _set_all_value_textField);
				break;
			case 23:
				value = get1( _comboBox);
				break;
			default:
				return null;
		}

		if ( null == value)
			return null;

		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);

		return Rule.create( _kind, _type, CollectionCommand._reservedWords[ kind] + spot + value);
	}
		
//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);
//		if ( 6 > kind)
//			kind += 18;
//		else
//			return false;
//
//		String value = null;
//		switch ( kind) {
//			case 18:
//				value = get1( _comboBox);
//				break;
//			case 19:
//				value = get1( _comboBox);
//				break;
//			case 20:
//				value = get2( _comboBox, _retain_all_collection_comboBox, false);
//				break;
//			case 21:
//				value = get2( _comboBox, _retain_all_list_comboBox, false);
//				break;
//			case 22:
//				if ( null == _set_all_keyword_textField.getText()
//					|| _set_all_keyword_textField.getText().equals( "")
//					|| _set_all_keyword_textField.getText().equals( "$")
//					|| 0 < _set_all_keyword_textField.getText().indexOf( '$'))
//					return false;
//
//				if ( _set_all_keyword_textField.getText().startsWith( "$")
//					&& 0 < _set_all_keyword_textField.getText().indexOf( "$", 1))
//					return false;
//
//				if ( null == _set_all_value_textField.getText()
//					|| _set_all_value_textField.getText().equals( "")
//					|| _set_all_value_textField.getText().equals( "$")
//					|| 0 < _set_all_value_textField.getText().indexOf( '$')
//					|| _set_all_value_textField.getText().startsWith( " ")
//					|| _set_all_value_textField.getText().endsWith( " "))
//					return false;
//
//				if ( _set_all_value_textField.getText().startsWith( "$")
//					&& ( 0 <= _set_all_value_textField.getText().indexOf( " ")
//					|| 0 < _set_all_value_textField.getText().indexOf( "$", 1)))
//					return false;
//
//				value = get5( _comboBox, _set_all_keyword_textField, _set_all_value_textField);
//				break;
//			case 23:
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
//		rule._value = ( CollectionCommandRuleManipulator._reserved_words[ kind] + spot + value);
//		rule._type = _type;
//
//		return true;
//	}
}
