/*
 * 2005/08/03
 */
package soars.application.visualshell.object.role.base.edit.tab.condition;

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
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.condition.base.CollectionAndListConditionPropertyPageBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.condition.base.CollectionAndListCondition;
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
public class CollectionAndListConditionPropertyPage extends CollectionAndListConditionPropertyPageBase {

	/**
	 * 
	 */
	private ObjectSelector _contains_agent_agent_selector = null;

	/**
	 * 
	 */
	private ObjectSelector _contains_spot_spot_selector = null;

	/**
	 * 
	 */
	private ComboBox _contains_equip_number_object_number_object_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _contains_equip_probability_probability_comboBox = null;

	/**
	 * 
	 */
	private TextField _contains_string_textField = null;

	/**
	 * 
	 */
	private ComboBox _contains_all_collection_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _contains_all_list_comboBox = null;

	/**
	 * 
	 */
	private TextField _ask_all_textField = null;

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
	public CollectionAndListConditionPropertyPage(String title, String kind,
		String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);

		_radioButtons1 = new RadioButton[] {
			null, null, null, null,
			null, null, null, null,
			null
		};

		_label = new JLabel[] {
			null, null, null, null,
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

		setup_is_empty( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_contains_agent( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_contains_spot( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_contains_equip_number_object( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_contains_equip_probability( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_contains_string( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_contains_all1( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_contains_all2( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_ask_all( buttonGroup1, north_panel);

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
	private void setup_is_empty(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.is.empty")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.is.empty"),
			buttonGroup1, true, false);
		panel.add( _radioButtons1[ 0]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_contains_agent(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.contains.agent")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.contains.agent"),
			buttonGroup1, true, false);
		_radioButtons1[ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_contains_agent_agent_selector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 1]);

		_label[ 1] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.contains.agent")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.contains.agent")),
			true);
		panel.add( _label[ 1]);

		_contains_agent_agent_selector = create_agent_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_contains_spot(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 2] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.contains.spot")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.contains.spot"),
			buttonGroup1, true, false);
		_radioButtons1[ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_contains_spot_spot_selector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 2]);

		_label[ 2] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.contains.spot")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.contains.spot")),
			true);
		panel.add( _label[ 2]);

		_contains_spot_spot_selector = create_spot_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_contains_equip_number_object(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 3] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.contains.equip.number.object")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.contains.equip.number.object"),
			buttonGroup1, true, false);
		_radioButtons1[ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 3].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_contains_equip_number_object_number_object_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 3]);

		_label[ 3] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.contains.equip.number.object")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.contains.equip.number.object")),
			true);
		panel.add( _label[ 3]);

		_contains_equip_number_object_number_object_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _contains_equip_number_object_number_object_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_contains_equip_probability(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 4] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.contains.equip.probability")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.contains.equip.probability"),
			buttonGroup1, true, false);
		_radioButtons1[ 4].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 4].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_contains_equip_probability_probability_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 4]);

		_label[ 4] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.contains.equip.probability")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.contains.equip.probability")),
			true);
		panel.add( _label[ 4]);

		_contains_equip_probability_probability_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _contains_equip_probability_probability_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_contains_string(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 5] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.contains.string")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.contains.string"),
			buttonGroup1, true, false);
		_radioButtons1[ 5].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 5].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_contains_string_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 5]);

		_label[ 5] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.contains.string")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.contains.string")),
			true);
		panel.add( _label[ 5]);

		_contains_string_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
		panel.add( _contains_string_textField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_contains_all1(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 6] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.contains.all")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.contains.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 6].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 6].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_contains_all_collection_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 6]);

		_label[ 6] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.contains.all")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.contains.all")),
			true);
		panel.add( _label[ 6]);

		_contains_all_collection_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _contains_all_collection_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_contains_all2(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 7] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.contains.all")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.contains.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 7].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 7].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_contains_all_list_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 7]);

		_label[ 7] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.contains.all")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.contains.all")),
			true);
		panel.add( _label[ 7]);

		_contains_all_list_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _contains_all_list_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_ask_all(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 8] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.ask.all")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.ask.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 8].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 8].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_ask_all_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 8]);

		_label[ 8] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.ask.all")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.ask.all")),
			true);
		panel.add( _label[ 8]);

		_ask_all_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
		panel.add( _ask_all_textField);

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

		if ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection"))) {
			CommonTool.update( _comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));

			CommonTool.update( _contains_all_collection_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));
			CommonTool.update( _contains_all_list_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));
		} else {
			CommonTool.update( _comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));

			CommonTool.update( _contains_all_collection_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));
			CommonTool.update( _contains_all_list_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));
		}

		CommonTool.update( _contains_equip_number_object_number_object_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
		CommonTool.update( _contains_equip_probability_probability_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_probability_names( false) : get_spot_probability_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		if ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection"))) {
			CommonTool.update( _comboBox, get_spot_collection_names( false));

			CommonTool.update( _contains_all_collection_comboBox, get_spot_collection_names( false));
			CommonTool.update( _contains_all_list_comboBox, get_spot_list_names( false));
		} else {
			CommonTool.update( _comboBox, get_spot_list_names( false));

			CommonTool.update( _contains_all_collection_comboBox, get_spot_list_names( false));
			CommonTool.update( _contains_all_list_comboBox, get_spot_collection_names( false));
		}

		CommonTool.update( _contains_equip_number_object_number_object_comboBox, get_spot_number_object_names( false));
		CommonTool.update( _contains_equip_probability_probability_comboBox, get_spot_probability_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(spotObject, number, objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		if ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection"))) {
			CommonTool.update( _comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));

			CommonTool.update( _contains_all_collection_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));
			CommonTool.update( _contains_all_list_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
		} else {
			CommonTool.update( _comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));

			CommonTool.update( _contains_all_collection_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
			CommonTool.update( _contains_all_list_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));
		}

		CommonTool.update( _contains_equip_number_object_number_object_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
		CommonTool.update( _contains_equip_probability_probability_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
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
			false, false, false, false,
			false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_label[ 1].setEnabled( enables[ 1]);
		_contains_agent_agent_selector.setEnabled( enables[ 1]);

		_label[ 2].setEnabled( enables[ 2]);
		_contains_spot_spot_selector.setEnabled( enables[ 2]);

		_label[ 3].setEnabled( enables[ 3]);
		_contains_equip_number_object_number_object_comboBox.setEnabled( enables[ 3]);

		_label[ 4].setEnabled( enables[ 4]);
		_contains_equip_probability_probability_comboBox.setEnabled( enables[ 4]);

		_label[ 5].setEnabled( enables[ 5]);
		_contains_string_textField.setEnabled( enables[ 5]);

		_label[ 6].setEnabled( enables[ 6]);
		_contains_all_collection_comboBox.setEnabled( enables[ 6]);

		_label[ 7].setEnabled( enables[ 7]);
		_contains_all_list_comboBox.setEnabled( enables[ 7]);

		_label[ 8].setEnabled( enables[ 8]);
		_ask_all_textField.setEnabled( enables[ 8]);
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

		int kind = CollectionAndListCondition.get_kind( rule);
		if ( 0 > kind) {
			set_handler();
			return false;
		}

		_radioButtons1[ kind].setSelected( true);

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
			case 0:
				_comboBox.setSelectedItem( values[ 2]);
				break;
			case 1:
				set2( _comboBox, _contains_agent_agent_selector, values[ 2], ( 2 > elements.length) ? "" : elements[ 1]);
				break;
			case 2:
				set2( _comboBox, _contains_spot_spot_selector, values[ 2], ( 2 > elements.length) ? "" : elements[ 1]);
				break;
			case 3:
				set1( _comboBox, _contains_equip_number_object_number_object_comboBox, values[ 2], elements[ 1]);
				break;
			case 4:
				set1( _comboBox, _contains_equip_probability_probability_comboBox, values[ 2], elements[ 1]);
				break;
			case 5:
				set3( _comboBox, _contains_string_textField, values[ 2], elements[ 1]);
				break;
			case 6:
				set1( _comboBox, _contains_all_collection_comboBox, values[ 2], elements[ 1]);
				break;
			case 7:
				set1( _comboBox, _contains_all_list_comboBox, values[ 2], elements[ 1]);
				break;
			case 8:
				set3( _comboBox, _ask_all_textField, values[ 2], elements[ 1]);
				break;
			default:
				set_handler();
				return false;
		}

		_checkBox.setSelected( rule._value.startsWith( "!"));

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
				value = get1( _comboBox);
				break;
			case 1:
				value = get3( _comboBox, _contains_agent_agent_selector);
				break;
			case 2:
				value = get3( _comboBox, _contains_spot_spot_selector);
				break;
			case 3:
				value = get2( _comboBox, _contains_equip_number_object_number_object_comboBox, false);
				break;
			case 4:
				value = get2( _comboBox, _contains_equip_probability_probability_comboBox, false);
				break;
			case 5:
				if ( null == _contains_string_textField.getText()
					|| _contains_string_textField.getText().equals( "")
					|| _contains_string_textField.getText().equals( "$")
					|| 0 < _contains_string_textField.getText().indexOf( '$')
					|| _contains_string_textField.getText().startsWith( " ")
					|| _contains_string_textField.getText().endsWith( " ")
					|| _contains_string_textField.getText().equals( "$Name")
					|| _contains_string_textField.getText().equals( "$Role")
					|| _contains_string_textField.getText().equals( "$Spot")
					|| 0 <= _contains_string_textField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _contains_string_textField.getText().startsWith( "$")
					&& ( 0 <= _contains_string_textField.getText().indexOf( " ")
					|| 0 < _contains_string_textField.getText().indexOf( "$", 1)
					|| 0 < _contains_string_textField.getText().indexOf( ")", 1)))
					return null;

				value = get4( _comboBox, _contains_string_textField, false);
				break;
			case 6:
				value = get2( _comboBox, _contains_all_collection_comboBox, false);
				break;
			case 7:
				value = get2( _comboBox, _contains_all_list_comboBox, false);
				break;
			case 8:
				if ( null == _ask_all_textField.getText()
					|| _ask_all_textField.getText().equals( "")
					|| _ask_all_textField.getText().equals( "$")
					|| 0 < _ask_all_textField.getText().indexOf( '$')
					|| _ask_all_textField.getText().startsWith( " ")
					|| _ask_all_textField.getText().endsWith( " ")
					|| _ask_all_textField.getText().equals( "$Name")
					|| _ask_all_textField.getText().equals( "$Role")
					|| _ask_all_textField.getText().equals( "$Spot")
					|| 0 <= _ask_all_textField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _ask_all_textField.getText().startsWith( "$")
					&& ( 0 <= _ask_all_textField.getText().indexOf( " ")
					|| 0 < _ask_all_textField.getText().indexOf( "$", 1)
					|| 0 < _ask_all_textField.getText().indexOf( ")", 1)))
					return null;

				value = get4( _comboBox, _ask_all_textField, false);
				break;
			default:
				return null;
		}

		if ( null == value)
			return null;

		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);

		return Rule.create( _kind, _type,
			( _checkBox.isSelected() ? "!" : "") + ( CollectionAndListCondition._reservedWords[ kind] + spot + value));
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
//				value = get1( _comboBox);
//				break;
//			case 1:
//				value = get3( _comboBox, _contains_agent_agent_selector);
//				break;
//			case 2:
//				value = get3( _comboBox, _contains_spot_spot_selector);
//				break;
//			case 3:
//				value = get2( _comboBox, _contains_equip_number_object_number_object_comboBox, false);
//				break;
//			case 4:
//				value = get2( _comboBox, _contains_equip_probability_probability_comboBox, false);
//				break;
//			case 5:
//				if ( null == _contains_string_textField.getText()
//					|| _contains_string_textField.getText().equals( "")
//					|| _contains_string_textField.getText().equals( "$")
//					|| 0 < _contains_string_textField.getText().indexOf( '$')
//					|| _contains_string_textField.getText().startsWith( " ")
//					|| _contains_string_textField.getText().endsWith( " ")
//					|| _contains_string_textField.getText().equals( "$Name")
//					|| _contains_string_textField.getText().equals( "$Role")
//					|| _contains_string_textField.getText().equals( "$Spot")
//					|| 0 <= _contains_string_textField.getText().indexOf( Constant._experiment_name))
//					return false;
//
//				if ( _contains_string_textField.getText().startsWith( "$")
//					&& ( 0 <= _contains_string_textField.getText().indexOf( " ")
//					|| 0 < _contains_string_textField.getText().indexOf( "$", 1)
//					|| 0 < _contains_string_textField.getText().indexOf( ")", 1)))
//					return false;
//
//				value = get4( _comboBox, _contains_string_textField, false);
//				break;
//			case 6:
//				value = get2( _comboBox, _contains_all_collection_comboBox, false);
//				break;
//			case 7:
//				value = get2( _comboBox, _contains_all_list_comboBox, false);
//				break;
//			case 8:
//				if ( null == _ask_all_textField.getText()
//					|| _ask_all_textField.getText().equals( "")
//					|| _ask_all_textField.getText().equals( "$")
//					|| 0 < _ask_all_textField.getText().indexOf( '$')
//					|| _ask_all_textField.getText().startsWith( " ")
//					|| _ask_all_textField.getText().endsWith( " ")
//					|| _ask_all_textField.getText().equals( "$Name")
//					|| _ask_all_textField.getText().equals( "$Role")
//					|| _ask_all_textField.getText().equals( "$Spot")
//					|| 0 <= _ask_all_textField.getText().indexOf( Constant._experiment_name))
//					return false;
//
//				if ( _ask_all_textField.getText().startsWith( "$")
//					&& ( 0 <= _ask_all_textField.getText().indexOf( " ")
//					|| 0 < _ask_all_textField.getText().indexOf( "$", 1)
//					|| 0 < _ask_all_textField.getText().indexOf( ")", 1)))
//					return false;
//
//				value = get4( _comboBox, _ask_all_textField, false);
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
//		rule._value = ( ( _checkBox.isSelected() ? "!" : "")
//			+ ( CollectionAndListConditionRuleManipulator._reserved_words[ kind] + spot + value));
//		rule._type = _type;
//
//		return true;
//	}
}
