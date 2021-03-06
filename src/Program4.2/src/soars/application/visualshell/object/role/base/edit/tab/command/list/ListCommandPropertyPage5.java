/*
 * 2005/08/03
 */
package soars.application.visualshell.object.role.base.edit.tab.command.list;

import java.awt.BorderLayout;
import java.awt.Color;
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
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class ListCommandPropertyPage5 extends CollectionAndListCommandPropertyPageBase {

	/**
	 * 
	 */
	private ComboBox _retain_all_list_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _retain_all_collection_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _add_all_list_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _add_all_collection_comboBox = null;

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
	public ListCommandPropertyPage5(String title, String kind,
		String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);

		_radioButtons1 = new RadioButton[] {
			null, null, null, null,
			null, null, null, null
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

		setup_remove_first( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_remove_last( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_retain_all1( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_retain_all2( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_add_all1( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_add_all2( buttonGroup1, north_panel);

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
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.remove.all.elements"),
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
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.remove.random.one"),
			buttonGroup1, true, false);
		panel.add( _radioButtons1[ 1]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_remove_first(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.remove.first"),
			buttonGroup1, true, false);
		panel.add( _radioButtons1[ 2]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_remove_last(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.remove.last"),
			buttonGroup1, true, false);
		panel.add( _radioButtons1[ 3]);

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

		_radioButtons1[ 4] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.retain.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 4].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_retain_all_list_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 4]);

		_label[ 1] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.label.retain.all"),
			true);
		panel.add( _label[ 1]);

		_retain_all_list_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _retain_all_list_comboBox);

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

		_radioButtons1[ 5] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.retain.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 5].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_retain_all_collection_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 5]);

		_label[ 2] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.label.retain.all"),
			true);
		panel.add( _label[ 2]);

		_retain_all_collection_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _retain_all_collection_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_add_all1(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 6] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 6].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 3].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_add_all_list_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 6]);

		_label[ 3] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.label.add.all"),
			true);
		panel.add( _label[ 3]);

		_add_all_list_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _add_all_list_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_add_all2(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 7] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.add.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 7].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 4].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_add_all_collection_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 7]);

		_label[ 4] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.label.add.all"),
			true);
		panel.add( _label[ 4]);

		_add_all_collection_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _add_all_collection_comboBox);

		parent.add( panel);
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

		CommonTool.update( _retain_all_list_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));
		CommonTool.update( _retain_all_collection_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));
		CommonTool.update( _add_all_list_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));
		CommonTool.update( _add_all_collection_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _comboBox, get_spot_list_names( false));

		CommonTool.update( _retain_all_list_comboBox, get_spot_list_names( false));
		CommonTool.update( _retain_all_collection_comboBox, get_spot_collection_names( false));
		CommonTool.update( _add_all_list_comboBox, get_spot_list_names( false));
		CommonTool.update( _add_all_collection_comboBox, get_spot_collection_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(spotObject, number, objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));

		CommonTool.update( _retain_all_list_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
		CommonTool.update( _retain_all_collection_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));
		CommonTool.update( _add_all_list_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
		CommonTool.update( _add_all_collection_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));
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
			false, false, false, false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_label[ 1].setEnabled( enables[ 4]);
		_retain_all_list_comboBox.setEnabled( enables[ 4]);

		_label[ 2].setEnabled( enables[ 5]);
		_retain_all_collection_comboBox.setEnabled( enables[ 5]);

		_label[ 3].setEnabled( enables[ 6]);
		_add_all_list_comboBox.setEnabled( enables[ 6]);

		_label[ 4].setEnabled( enables[ 7]);
		_add_all_collection_comboBox.setEnabled( enables[ 7]);
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
		if ( 31 > kind || 38 < kind) {
			set_handler();
			return false;
		}

		_radioButtons1[ kind - 31].setSelected( true);

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
			case 31:
				_comboBox.setSelectedItem( values[ 2]);
				break;
			case 32:
				_comboBox.setSelectedItem( values[ 2]);
				break;
			case 33:
				_comboBox.setSelectedItem( values[ 2]);
				break;
			case 34:
				_comboBox.setSelectedItem( values[ 2]);
				break;
			case 35:
				set1( _comboBox, _retain_all_list_comboBox, values[ 2], elements[ 1]);
				break;
			case 36:
				set1( _comboBox, _retain_all_collection_comboBox, values[ 2], elements[ 1]);
				break;
			case 37:
				set1( _comboBox, _add_all_list_comboBox, values[ 2], elements[ 1]);
				break;
			case 38:
				set1( _comboBox, _add_all_collection_comboBox, values[ 2], elements[ 1]);
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
		int kind = ( 31 + SwingTool.get_enabled_radioButton( _radioButtons1));
		switch ( kind) {
			case 31:
				value = get1( _comboBox);
				break;
			case 32:
				value = get1( _comboBox);
				break;
			case 33:
				value = get1( _comboBox);
				break;
			case 34:
				value = get1( _comboBox);
				break;
			case 35:
				value = get2( _comboBox, _retain_all_list_comboBox, false);
				break;
			case 36:
				value = get2( _comboBox, _retain_all_collection_comboBox, false);
				break;
			case 37:
				value = get2( _comboBox, _add_all_list_comboBox, false);
				break;
			case 38:
				value = get2( _comboBox, _add_all_collection_comboBox, false);
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
//		int kind = ( 22 + SwingTool.get_enabled_radioButton( _radioButtons1));
//		switch ( kind) {
//			case 22:
//				value = get2( _comboBox, _add_all_list_comboBox, false);
//				break;
//			case 23:
//				value = get2( _comboBox, _add_all_collection_comboBox, false);
//				break;
//			case 24:
//				value = get2( _comboBox, _remove_all_list_comboBox, false);
//				break;
//			case 25:
//				value = get2( _comboBox, _remove_all_collection_comboBox, false);
//				break;
//			case 26:
//				value = get1( _comboBox);
//				break;
//			case 27:
//				value = get1( _comboBox);
//				break;
//			case 28:
//				value = get1( _comboBox);
//				break;
//			case 29:
//				value = get1( _comboBox);
//				break;
//			case 30:
//				value = get2( _comboBox, _retain_all_list_comboBox, false);
//				break;
//			case 31:
//				value = get2( _comboBox, _retain_all_collection_comboBox, false);
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
