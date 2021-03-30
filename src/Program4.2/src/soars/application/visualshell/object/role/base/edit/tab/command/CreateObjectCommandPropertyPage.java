/*
 * Created on 2006/08/24
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
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.player.base.object.number.NumberObject;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.command.CreateObjectCommand;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.common.time.TimeRule;
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
public class CreateObjectCommandPropertyPage extends RulePropertyPageBase {

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
	private JLabel _dummy1 = null;

	/**
	 * 
	 */
	private RadioButton[] _radioButtons1 = new RadioButton[] {
		null, null, null, null,
		null, null, null, null,
		null, null
	};

	/**
	 * 
	 */
	private ComboBox _probability_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _collection_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _list_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _map_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _keyword_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _number_object_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _role_variable_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _time_variable_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _spot_variable_comboBox2 = null;

	/**
	 * 
	 */
	private ComboBox _class_variable_comboBox = null;

	/**
	 * 
	 */
	private TextField _probability_initial_value_textField = null;

	/**
	 * 
	 */
	private TextField _keyword_initial_value_textField = null;

	/**
	 * 
	 */
	private TextField _number_object_type_textField = null;

	/**
	 * 
	 */
	private TextField _number_object_initial_value_textField = null;

	/**
	 * 
	 */
	private ComboBox _role_comboBox = null;

	/**
	 * 
	 */
	private TextField _time_textField = null;

	/**
	 * 
	 */
	private ComboBox[] _time_comboBoxes = new ComboBox[] { null, null};

	/**
	 * 
	 */
	private JLabel[] _time_labels = new JLabel[] { null, null};

	/**
	 * 
	 */
	private ObjectSelector _spot_selector2 = null;

	/**
	 * 
	 */
	private TextField _classname_textField = null;

	/**
	 * 
	 */
	private TextField _jar_filename_textField = null;

	/**
	 * 
	 */
	private JLabel[][] _labels = new JLabel[][] {
		{ null},
		{ null},
		{ null, null},
		{ null},
		{ null},
		{ null},
		{ null, null}
	};

//	/**
//	 * 
//	 */
//	private JLabel[] _dummy2 = new JLabel[] {
//		null, null
//	};

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
	public CreateObjectCommandPropertyPage(String title, String kind,
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

		ButtonGroup buttonGroup1 = new ButtonGroup();

		setup_probability( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_collection( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_list( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_map( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_keyword( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_number_object( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_role_variable( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_time_variable( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_spot_variable( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_class_variable( buttonGroup1, north_panel);

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

		_dummy1 = new JLabel();
		panel.add( _dummy1);

		_spot_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.spot.check.box.name"),
			false, true);
		_spot_checkBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_spot_selector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				on_update( ItemEvent.SELECTED == arg0.getStateChange(),
					_spot_selector,
					_spot_variable_checkBox,
					_spot_variable_comboBox);
				_radioButtons1[ 6].setEnabled( !_spot_variable_checkBox.isSelected() && _role instanceof AgentRole && ItemEvent.SELECTED != arg0.getStateChange());
				if ( _spot_variable_checkBox.isSelected() || _role instanceof SpotRole || ItemEvent.SELECTED == arg0.getStateChange()) {
					_role_variable_comboBox.setEnabled( false);
					_labels[ 3][ 0].setEnabled( false);
					_role_comboBox.setEnabled( false);
				}
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
				_radioButtons1[ 6].setEnabled( !_spot_checkBox.isSelected() && _role instanceof AgentRole && ItemEvent.SELECTED != arg0.getStateChange());
				if ( _spot_checkBox.isSelected() || _role instanceof SpotRole || ItemEvent.SELECTED == arg0.getStateChange()) {
					_role_variable_comboBox.setEnabled( false);
					_labels[ 3][ 0].setEnabled( false);
					_role_comboBox.setEnabled( false);
				}
			}
		});
		panel.add( _spot_variable_checkBox);

		_spot_variable_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _spot_variable_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_probability(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.probability"),
			buttonGroup1, true, false);
		_radioButtons1[ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_probability_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 0][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_probability_initial_value_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 0]);

		_probability_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _probability_comboBox);

		_labels[ 0][ 0] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.probability.initial.value"),
			true);
		panel.add( _labels[ 0][ 0]);

		_probability_initial_value_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters5), _standardControlWidth, true);
		panel.add( _probability_initial_value_textField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_collection(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.collection"),
			buttonGroup1, true, false);
		_radioButtons1[ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_collection_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 1]);

		_collection_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _collection_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_list(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.list"),
			buttonGroup1, true, false);
		_radioButtons1[ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_list_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 2]);

		_list_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _list_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_map(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.map"),
			buttonGroup1, true, false);
		_radioButtons1[ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_map_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 3]);

		_map_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _map_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_keyword(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 4] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.keyword"),
			buttonGroup1, true, false);
		_radioButtons1[ 4].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_keyword_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 1][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_keyword_initial_value_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 4]);

		_keyword_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _keyword_comboBox);

		_labels[ 1][ 0] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.keyword.initial.value"),
			true);
		panel.add( _labels[ 1][ 0]);

		_keyword_initial_value_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
		panel.add( _keyword_initial_value_textField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_number_object(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 5] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.number.object"),
			buttonGroup1, true, false);
		_radioButtons1[ 5].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_number_object_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 2][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_number_object_type_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 2][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_number_object_initial_value_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 5]);

		_number_object_comboBox = create_comboBox( null, _standardControlWidth, false);
		_number_object_comboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_update_number_object_comboBox();
			}
		});
		panel.add( _number_object_comboBox);

		_labels[ 2][ 0] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.number.object.type"),
			true);
		panel.add( _labels[ 2][ 0]);

		_number_object_type_textField = create_textField( _standardControlWidth, false);
		_number_object_type_textField.setEditable( false);
		panel.add( _number_object_type_textField);

		parent.add( panel);


//		insert_vertical_strut( parent);
//
//
//		panel = new JPanel();
//		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));
//
//		_dummy2[ 0] = new JLabel();
//		panel.add( _dummy2[ 0]);

		_labels[ 2][ 1] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.number.object.initial.value"),
			true);
		panel.add( _labels[ 2][ 1]);

		_number_object_initial_value_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters5), _standardControlWidth, true);
		panel.add( _number_object_initial_value_textField);

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void on_update_number_object_comboBox() {
		String number_object_name = ( String)_number_object_comboBox.getSelectedItem();
		if ( null == number_object_name || number_object_name.equals( "")) {
			_number_object_type_textField.setText( "");
			return;
		}

		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
		spot = CommonRuleManipulator.get_semantic_prefix( spot);
		if ( null == spot) {
			_number_object_type_textField.setText( "");
			return;
		}

		String number_object_type = CreateObjectCommand.get_number_object_type( spot, number_object_name);
//			( !_spot_checkBox.isSelected() ? null : _spot_selector.get()), number_object_name);
		if ( null == number_object_type) {
			_number_object_type_textField.setText( "");
			return;
		}

		String number_object_type_name = NumberObject.get_type_name( number_object_type);
		if ( number_object_type_name.equals( "")) {
			_number_object_type_textField.setText( "");
			return;
		}

		_number_object_type_textField.setText( number_object_type_name);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_role_variable(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 6] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.role.variable"),
			buttonGroup1, true, false);
		_radioButtons1[ 6].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_role_variable_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 3][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_role_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 6]);

		_role_variable_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _role_variable_comboBox);

		_labels[ 3][ 0] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.role.variable.role"),
			true);
		panel.add( _labels[ 3][ 0]);

		_role_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _role_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_time_variable(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 7] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.time.variable"),
			buttonGroup1, true, false);
		_radioButtons1[ 7].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_time_variable_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 4][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_time_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_time_labels[ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_time_comboBoxes[ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_time_labels[ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_time_comboBoxes[ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 7]);

		_time_variable_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _time_variable_comboBox);

		_labels[ 4][ 0] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.time.variable.initial.value"),
			true);
		panel.add( _labels[ 4][ 0]);

		_time_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters14), 52, true);
		panel.add( _time_textField);

		_time_labels[ 0] = new JLabel( " / ");
		panel.add( _time_labels[ 0]);

		_time_comboBoxes[ 0] = create_comboBox( CommonTool.get_hours(), 52, true);
		panel.add( _time_comboBoxes[ 0]);

		_time_labels[ 1] = new JLabel( " : ");
		panel.add( _time_labels[ 1]);

		_time_comboBoxes[ 1] = create_comboBox( CommonTool.get_minutes00(), 52, true);
		panel.add( _time_comboBoxes[ 1]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_spot_variable(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 8] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.spot.variable"),
			buttonGroup1, true, false);
		_radioButtons1[ 8].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_spot_variable_comboBox2.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 5][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_spot_selector2.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 8]);

		_spot_variable_comboBox2 = create_comboBox( null, _standardControlWidth, false);
		panel.add( _spot_variable_comboBox2);

		_labels[ 5][ 0] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.spot.variable.spot"),
			true);
		panel.add( _labels[ 5][ 0]);

		_spot_selector2 = create_spot_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_class_variable(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 9] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.class.variable"),
			buttonGroup1, true, false);
		_radioButtons1[ 9].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if ( !Environment.get_instance().is_functional_object_enable())
					return;

				_class_variable_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 6][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_classname_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 6][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_jar_filename_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 9]);

		_class_variable_comboBox = create_comboBox( null, _standardControlWidth, false);
		_class_variable_comboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_update_class_variable_comboBox();
			}
		});
		panel.add( _class_variable_comboBox);

		_labels[ 6][ 0] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.class.variable.class.name"),
			true);
		panel.add( _labels[ 6][ 0]);

		_classname_textField = create_textField( _standardControlWidth, false);
		_classname_textField.setEditable( false);
		panel.add( _classname_textField);

		parent.add( panel);


//		insert_vertical_strut( parent);
//
//
//		panel = new JPanel();
//		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));
//
//		_dummy2[ 1] = new JLabel();
//		panel.add( _dummy2[ 1]);

		_labels[ 6][ 1] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.class.variable.jar.filename"),
			true);
		panel.add( _labels[ 6][ 1]);

		_jar_filename_textField = create_textField( _standardControlWidth, false);
		_jar_filename_textField.setEditable( false);
		panel.add( _jar_filename_textField);

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void on_update_class_variable_comboBox() {
		String class_variable_name = ( String)_class_variable_comboBox.getSelectedItem();
		if ( null == class_variable_name || class_variable_name.equals( "")) {
			_classname_textField.setText( "");
			_jar_filename_textField.setText( "");
			return;
		}

		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
		spot = CommonRuleManipulator.get_semantic_prefix( spot);
		if ( null == spot) {
			_classname_textField.setText( "");
			_jar_filename_textField.setText( "");
			return;
		}

		ClassVariableObject classVariableObject;
//		if ( !_spot_checkBox.isSelected())
		if ( spot.equals( ""))
			classVariableObject = get_agent_class_variable( class_variable_name);
		else
			classVariableObject = get_spot_class_variable( class_variable_name);
		if ( null == classVariableObject) {
			return;
		}

		if ( null == classVariableObject._classname || classVariableObject._classname.equals( ""))
			_classname_textField.setText( "");
		else {
			String[] words = classVariableObject._classname.split( "\\.");
			_classname_textField.setText( ( null != words && 0 < words.length) ? words[ words.length - 1] : "");
		}

		_classname_textField.setToolTipText( classVariableObject._classname);

		if ( null == classVariableObject._jarFilename || classVariableObject._jarFilename.equals( ""))
			_jar_filename_textField.setText( "");
		else {
			String[] words = classVariableObject._jarFilename.split( "/");
			_jar_filename_textField.setText( ( null != words && 0 < words.length) ? words[ words.length - 1] : "");
		}

		_jar_filename_textField.setToolTipText( classVariableObject._jarFilename);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _spot_checkBox.getPreferredSize().width;
		width = Math.max( width, _spot_variable_checkBox.getPreferredSize().width);
		for ( int i = 0; i < _radioButtons1.length; ++i)
			width = Math.max( width, _radioButtons1[ i].getPreferredSize().width);

		_spot_checkBox.setPreferredSize( new Dimension( width,
			_spot_checkBox.getPreferredSize().height));

		_spot_variable_checkBox.setPreferredSize( new Dimension( width,
			_spot_variable_checkBox.getPreferredSize().height));

		Dimension dimension = new Dimension( width,
			_radioButtons1[ 0].getPreferredSize().height);
		for ( int i = 0; i < _radioButtons1.length; ++i)
			_radioButtons1[ i].setPreferredSize( dimension);


		_dummy1.setPreferredSize( new Dimension(
			_radioButtons1[ 0].getPreferredSize().width
				- _spot_checkBox.getPreferredSize().width - 5,
			_dummy1.getPreferredSize().height));


//		_dummy2[ 0].setPreferredSize( new Dimension(
//			_radioButtons1[ 4].getPreferredSize().width
//				+ 5
//				+ _number_object_comboBox.getPreferredSize().width,
//			_dummy2[ 0].getPreferredSize().height));
//
//
//		_dummy2[ 1].setPreferredSize( new Dimension(
//			_radioButtons1[ 5].getPreferredSize().width
//				+ 5
//				+ _class_variable_comboBox.getPreferredSize().width,
//			_dummy2[ 1].getPreferredSize().height));


		width = 0;
		for ( int i = 0; i < _labels.length; ++i)
			width = Math.max( width, _labels[ i][ 0].getPreferredSize().width);

		for ( int i = 0; i < _labels.length; ++i)
			_labels[ i][ 0].setPreferredSize( new Dimension( width, _labels[ i][ 0].getPreferredSize().height));


		width = 0;
		for ( int i = 0; i < _labels.length; ++i) {
			if ( 1 < _labels[ i].length)
				width = Math.max( width, _labels[ i][ 1].getPreferredSize().width);
		}

		for ( int i = 0; i < _labels.length; ++i) {
			if ( 1 < _labels[ i].length)
				_labels[ i][ 1].setPreferredSize( new Dimension( width, _labels[ i][ 1].getPreferredSize().height));
		}


		width = ( _time_textField.getPreferredSize().width
			+ _time_labels[ 0].getPreferredSize().width
			+ _time_comboBoxes[ 0].getPreferredSize().width
			+ _time_labels[ 1].getPreferredSize().width
			+ _time_comboBoxes[ 1].getPreferredSize().width
			+ 20);

		_probability_initial_value_textField.setPreferredSize(
			new Dimension( width, _probability_initial_value_textField.getPreferredSize().height));
		_keyword_initial_value_textField.setPreferredSize(
			new Dimension( width, _keyword_initial_value_textField.getPreferredSize().height));
		_number_object_type_textField.setPreferredSize(
			new Dimension( width, _number_object_type_textField.getPreferredSize().height));
		_number_object_initial_value_textField.setPreferredSize(
			new Dimension( width, _number_object_initial_value_textField.getPreferredSize().height));
		_role_comboBox.setPreferredSize(
			new Dimension( width, _role_comboBox.getPreferredSize().height));
		_spot_selector2.setWidth( width);
		_classname_textField.setPreferredSize(
			new Dimension( width, _classname_textField.getPreferredSize().height));
		_jar_filename_textField.setPreferredSize(
			new Dimension( width, _jar_filename_textField.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#reset(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void reset(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		CommonTool.update( spot_variable_comboBox, !_spot_checkBox.isSelected() ? get_agent_spot_variable_names( false) : get_spot_spot_variable_names( false));

		super.reset(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		objectSelector.setEnabled( false);

		CommonTool.update( _probability_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_probability_names( false) : get_spot_probability_names( false));
		CommonTool.update( _collection_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));
		CommonTool.update( _list_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));
		CommonTool.update( _map_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_map_names( false) : get_spot_map_names( false));
		CommonTool.update( _keyword_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_keyword_names( false) : get_spot_keyword_names( false));
		CommonTool.update( _number_object_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));

		if ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) {
			CommonTool.update( _role_variable_comboBox, get_agent_role_variable_names( false));
			CommonTool.update( _role_comboBox, get_agent_role_names( true));
		}

		CommonTool.update( _role_variable_comboBox, get_agent_role_variable_names( false));
		CommonTool.update( _role_comboBox, get_agent_role_names( true));

		_radioButtons1[ 6].setEnabled( true);
		if ( 6 == SwingTool.get_enabled_radioButton( _radioButtons1)) {
			_role_variable_comboBox.setEnabled( true);
			_labels[ 3][ 0].setEnabled( true);
			_role_comboBox.setEnabled( true);
		}

		CommonTool.update( _time_variable_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_time_variable_names( false) : get_spot_time_variable_names( false));
		CommonTool.update( _spot_variable_comboBox2, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_spot_variable_names( false) : get_spot_spot_variable_names( false));
		CommonTool.update( _class_variable_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_class_variable_names( false) : get_spot_class_variable_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _probability_comboBox, get_spot_probability_names( false));
		CommonTool.update( _collection_comboBox, get_spot_collection_names( false));
		CommonTool.update( _list_comboBox, get_spot_list_names( false));
		CommonTool.update( _map_comboBox, get_spot_map_names( false));
		CommonTool.update( _keyword_comboBox, get_spot_keyword_names( false));
		CommonTool.update( _number_object_comboBox, get_spot_number_object_names( false));

		_radioButtons1[ 6].setEnabled( false);
		_role_variable_comboBox.setEnabled( false);
		_labels[ 3][ 0].setEnabled( false);
		_role_comboBox.setEnabled( false);

		CommonTool.update( _time_variable_comboBox, get_spot_time_variable_names( false));
		CommonTool.update( _spot_variable_comboBox2, get_spot_spot_variable_names( false));
		CommonTool.update( _class_variable_comboBox, get_spot_class_variable_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(spotObject, number, objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _probability_comboBox, get_spot_probability_names( false));
		CommonTool.update( _collection_comboBox, get_spot_collection_names( false));
		CommonTool.update( _list_comboBox, get_spot_list_names( false));
		CommonTool.update( _map_comboBox, get_spot_map_names( false));
		CommonTool.update( _keyword_comboBox, get_spot_keyword_names( false));
		CommonTool.update( _number_object_comboBox, get_spot_number_object_names( false));
//		CommonTool.update( _probability_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
//		CommonTool.update( _collection_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));
//		CommonTool.update( _list_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
//		CommonTool.update( _map_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "map", number, false) : get_spot_map_names( false));
//		CommonTool.update( _keyword_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
//		CommonTool.update( _number_object_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));

		_radioButtons1[ 6].setEnabled( false);
		_role_variable_comboBox.setEnabled( false);
		_labels[ 3][ 0].setEnabled( false);
		_role_comboBox.setEnabled( false);

		CommonTool.update( _time_variable_comboBox, get_spot_time_variable_names( false));
		CommonTool.update( _spot_variable_comboBox2, get_spot_spot_variable_names( false));
		CommonTool.update( _class_variable_comboBox, get_spot_class_variable_names( false));
//		CommonTool.update( _time_variable_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "time variable", number, false) : get_spot_time_variable_names( false));
//		CommonTool.update( _spot_variable_comboBox2, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "spot variable", number, false) : get_spot_spot_variable_names( false));
//		CommonTool.update( _class_variable_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "class variable", number, false) : get_spot_class_variable_names( false));
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
			false, false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_probability_comboBox.setEnabled( enables[ 0]);
		_labels[ 0][ 0].setEnabled( enables[ 0]);
		_probability_initial_value_textField.setEnabled( enables[ 0]);

		_collection_comboBox.setEnabled( enables[ 1]);

		_list_comboBox.setEnabled( enables[ 2]);

		_map_comboBox.setEnabled( enables[ 3]);

		_keyword_comboBox.setEnabled( enables[ 4]);
		_labels[ 1][ 0].setEnabled( enables[ 4]);
		_keyword_initial_value_textField.setEnabled( enables[ 4]);

		_number_object_comboBox.setEnabled( enables[ 5]);
		_labels[ 2][ 0].setEnabled( enables[ 5]);
		_number_object_type_textField.setEnabled( enables[ 5]);
		_labels[ 2][ 1].setEnabled( enables[ 5]);
		_number_object_initial_value_textField.setEnabled( enables[ 5]);

		_role_variable_comboBox.setEnabled( enables[ 6]);
		_labels[ 3][ 0].setEnabled( enables[ 6]);
		_role_comboBox.setEnabled( enables[ 6]);

		_time_variable_comboBox.setEnabled( enables[ 7]);
		_labels[ 4][ 0].setEnabled( enables[ 7]);
		_time_textField.setEnabled( enables[ 7]);
		_time_labels[ 0].setEnabled( enables[ 7]);
		_time_comboBoxes[ 0].setEnabled( enables[ 7]);
		_time_labels[ 1].setEnabled( enables[ 7]);
		_time_comboBoxes[ 1].setEnabled( enables[ 7]);

		_spot_variable_comboBox2.setEnabled( enables[ 8]);
		_labels[ 5][ 0].setEnabled( enables[ 8]);
		_spot_selector2.setEnabled( enables[ 8]);

		_class_variable_comboBox.setEnabled( enables[ 9]);
		_labels[ 6][ 0].setEnabled( enables[ 9]);
		_classname_textField.setEnabled( enables[ 9]);
		_labels[ 6][ 1].setEnabled( enables[ 9]);
		_jar_filename_textField.setEnabled( enables[ 9]);
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

		if ( !Environment.get_instance().is_functional_object_enable()) {
			_radioButtons1[ 9].setEnabled( false);
			_labels[ 6][ 0].setEnabled( false);
			_classname_textField.setEnabled( false);
			_labels[ 6][ 1].setEnabled( false);
			_jar_filename_textField.setEnabled( false);
		}

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

		int kind = CreateObjectCommand.get_kind( rule._value);
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

		if ( !set( values[ 0], values[ 1], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox)) {
			set_handler();
			return false;
		}

		switch ( kind) {
			case 0:
				_probability_comboBox.setSelectedItem( values[ 2]);
				_probability_initial_value_textField.setText( ( 1 < elements.length) ? elements[ 1] : "");
				break;
			case 1:
				_collection_comboBox.setSelectedItem( values[ 2]);
				break;
			case 2:
				_list_comboBox.setSelectedItem( values[ 2]);
				break;
			case 3:
				_map_comboBox.setSelectedItem( values[ 2]);
				break;
			case 4:
				_keyword_comboBox.setSelectedItem( values[ 2]);
				_keyword_initial_value_textField.setText( ( 1 < elements.length) ? elements[ 1] : "");
				break;
			case 5:
				String number_object_type = CreateObjectCommand.get_number_object_type(
					CommonRuleManipulator.get_semantic_prefix( values), values[ 2]);
				if ( null == number_object_type) {
					set_handler();
					return false;
				}

				String number_object_type_name = NumberObject.get_type_name( number_object_type);
				if ( number_object_type_name.equals( "")) {
					set_handler();
					return false;
				}

				_number_object_type_textField.setText( number_object_type_name);

				_number_object_comboBox.setSelectedItem( values[ 2]);

				_number_object_initial_value_textField.setText( ( 1 < elements.length) ? elements[ 1] : "");

				break;
			case 6:
				_role_variable_comboBox.setSelectedItem( values[ 2]);
				_role_comboBox.setSelectedItem( ( 1 < elements.length) ? elements[ 1] : "");
				break;
			case 7:
				_time_variable_comboBox.setSelectedItem( values[ 2]);
				set_time_variable_initial_values( ( 1 < elements.length) ? elements[ 1] : "");
				break;
			case 8:
				_spot_variable_comboBox2.setSelectedItem( values[ 2]);
				_spot_selector2.set( ( 1 < elements.length) ? elements[ 1] : "");
				break;
			case 9:
				_class_variable_comboBox.setSelectedItem( values[ 2]);
				break;
		}

		set_handler();

		return true;
	}

	/**
	 * @param initial_value
	 */
	private void set_time_variable_initial_values(String initial_value) {
		if ( initial_value.startsWith( "$"))
			_time_textField.setText( initial_value);
		else {
			String[] words = TimeRule.get_time_elements( initial_value);
			if ( null == words)
				return;

			_time_textField.setText( words[ 0]);
			_time_comboBoxes[ 0].setSelectedItem( words[ 1]);
			_time_comboBoxes[ 1].setSelectedItem( words[ 2]);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String value = null;
		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);

		switch ( kind) {
			case 0:
				value = get_probability();
				break;
			case 1:
				value = get1( _collection_comboBox);
				break;
			case 2:
				value = get1( _list_comboBox);
				break;
			case 3:
				value = get1( _map_comboBox);
				break;
			case 4:
				value = get_keyword();
				break;
			case 5:
				value = get_number_object();
				break;
			case 6:
				if ( _spot_checkBox.isSelected() || _spot_variable_checkBox.isSelected())
					return null;

				value = get2( _role_variable_comboBox, _role_comboBox, true);
				break;
			case 7:
				value = get_time_variable();
				break;
			case 8:
				value = get_spot_variable();
				break;
			case 9:
				if ( !Environment.get_instance().is_functional_object_enable())
					return null;

				value = get1( _class_variable_comboBox);
				break;
		}

		if ( null == value)
			return null;

		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);

		return Rule.create( _kind, _type, CreateObjectCommand._reserved_word[ kind] + spot + value);
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
//
//		switch ( kind) {
//			case 0:
//				value = get_probability();
//				break;
//			case 1:
//				value = get1( _collection_comboBox);
//				break;
//			case 2:
//				value = get1( _list_comboBox);
//				break;
//			case 3:
//				value = get1( _map_comboBox);
//				break;
//			case 4:
//				value = get_keyword();
//				break;
//			case 5:
//				value = get_number_object();
//				break;
//			case 6:
//				if ( _spot_checkBox.isSelected() || _spot_variable_checkBox.isSelected())
//					return false;
//
//				value = get2( _role_variable_comboBox, _role_comboBox, true);
//				break;
//			case 7:
//				value = get_time_variable();
//				break;
//			case 8:
//				value = get_spot_variable();
//				break;
//			case 9:
//				if ( !Environment.get_instance().is_functional_object_enable())
//					return false;
//
//				value = get1( _class_variable_comboBox);
//				break;
//		}
//
//		if ( null == value)
//			return false;
//
//		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
//
//		rule._value = ( CreateObjectCommandRuleManipulator._reserved_word[ kind] + spot + value);
//		rule._type = _type;
//
//		return true;
//	}

	/**
	 * @return
	 */
	private String get_probability() {
		if ( null == _probability_initial_value_textField.getText()
			|| _probability_initial_value_textField.getText().equals( "$")
			|| 0 < _probability_initial_value_textField.getText().indexOf( '$')
			|| _probability_initial_value_textField.getText().equals( "$Name")
			|| _probability_initial_value_textField.getText().equals( "$Role")
			|| _probability_initial_value_textField.getText().equals( "$Spot")
			|| 0 <= _probability_initial_value_textField.getText().indexOf( Constant._experimentName))
			return null;

		if ( _probability_initial_value_textField.getText().startsWith( "$")
			&& ( 0 < _probability_initial_value_textField.getText().indexOf( "$", 1)
			|| 0 < _probability_initial_value_textField.getText().indexOf( ")", 1)))
			return null;

		String probability = ( String)_probability_comboBox.getSelectedItem();
		if ( null == probability || probability.equals( ""))
			return null;

		String initial_value = _probability_initial_value_textField.getText();
		if ( null == initial_value || initial_value.equals( ""))
			initial_value = "";
		else {
			if ( !CommonTool.is_probability_correct( initial_value))
				return null;

			initial_value = NumberObject.is_correct( initial_value, "real number");
		}

		return ( probability + "=" + initial_value);
	}

	/**
	 * @return
	 */
	private String get_keyword() {
		if ( null == _keyword_initial_value_textField.getText()
			|| _keyword_initial_value_textField.getText().equals( "$")
			|| 0 < _keyword_initial_value_textField.getText().indexOf( '$')
			||_keyword_initial_value_textField.getText().startsWith( " ")
			|| _keyword_initial_value_textField.getText().endsWith( " ")
			|| _keyword_initial_value_textField.getText().equals( "$Name")
			|| _keyword_initial_value_textField.getText().equals( "$Role")
			|| _keyword_initial_value_textField.getText().equals( "$Spot")
			|| 0 <= _keyword_initial_value_textField.getText().indexOf( Constant._experimentName))
			return null;

		if ( _keyword_initial_value_textField.getText().startsWith( "$")
			&& ( 0 <= _keyword_initial_value_textField.getText().indexOf( " ")
			|| 0 < _keyword_initial_value_textField.getText().indexOf( "$", 1)
			|| 0 < _keyword_initial_value_textField.getText().indexOf( ")", 1)))
			return null;

		return get4( _keyword_comboBox, _keyword_initial_value_textField, true);
	}

	/**
	 * @return
	 */
	private String get_number_object() {
		if ( null == _number_object_initial_value_textField.getText()
			|| _number_object_initial_value_textField.getText().equals( "$")
			|| 0 < _number_object_initial_value_textField.getText().indexOf( '$')
			|| _number_object_initial_value_textField.getText().equals( "$Name")
			|| _number_object_initial_value_textField.getText().equals( "$Role")
			|| _number_object_initial_value_textField.getText().equals( "$Spot")
			|| 0 <= _number_object_initial_value_textField.getText().indexOf( Constant._experimentName))
			return null;

		if ( _number_object_initial_value_textField.getText().startsWith( "$")
			&& ( 0 < _number_object_initial_value_textField.getText().indexOf( "$", 1)
			|| 0 < _number_object_initial_value_textField.getText().indexOf( ")", 1)))
			return null;

		String number_object = ( String)_number_object_comboBox.getSelectedItem();
		if ( null == number_object || number_object.equals( ""))
			return null;

		String type_name = _number_object_type_textField.getText();
		if ( null == type_name)
			return null;

		String type = NumberObject.get_type( type_name);
		if ( type.equals( ""))
			return null;

		String initial_value = _number_object_initial_value_textField.getText();
		if ( null == initial_value || initial_value.equals( ""))
			initial_value = "";
		else {
			initial_value = NumberObject.is_correct( initial_value, type);
			if ( null == initial_value)
				return null;
		}

		return ( number_object + "=" + initial_value);
	}

	/**
	 * @return
	 */
	private String get_time_variable() {
		String time_variable = ( String)_time_variable_comboBox.getSelectedItem();
		if ( null == time_variable || time_variable.equals( ""))
			return null;

		String initial_value = "";

		if ( _time_textField.getText().startsWith( "$")) {
			if ( _time_textField.getText().equals( "$")
				|| _time_textField.getText().equals( "$Name")
				|| _time_textField.getText().equals( "$Role")
				|| _time_textField.getText().equals( "$Spot")
				|| 0 <= _time_textField.getText().indexOf( Constant._experimentName)
				|| 0 <= _time_textField.getText().indexOf( Constant._currentTimeName))
				return null;

			if ( 0 < _time_textField.getText().indexOf( "$", 1)
				|| 0 < _time_textField.getText().indexOf( ")", 1))
				return null;

			initial_value = _time_textField.getText();

		} else {
			if ( !_time_textField.getText().equals( "")) {
				int day;
				try {
					day = Integer.parseInt( _time_textField.getText());
				} catch (NumberFormatException e) {
					//e.printStackTrace();
					return null;
				}
				initial_value = ( String.valueOf( day) + "/");
			}
			initial_value += ( ( String)_time_comboBoxes[ 0].getSelectedItem() + ":" + ( String)_time_comboBoxes[ 1].getSelectedItem());
		}

		return ( time_variable + "=" + initial_value);
	}

	/**
	 * @return
	 */
	private String get_spot_variable() {
		String spot_variable = ( String)_spot_variable_comboBox2.getSelectedItem();
		if ( null == spot_variable || spot_variable.equals( ""))
			return null;

		return ( spot_variable + "=" + _spot_selector2.get());
	}
}
