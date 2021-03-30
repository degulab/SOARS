/*
 * Created on 2006/09/25
 */
package soars.application.visualshell.object.role.base.edit.tab.command;

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
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.command.MapCommand;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class MapCommandPropertyPage extends RulePropertyPageBase {

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
	private JLabel _map_label = null;

	/**
	 * 
	 */
	private ComboBox _map_comboBox = null;

	/**
	 * 
	 */
	private RadioButton[] _radioButtons1 = new RadioButton[] {
		null, null
	};

	/**
	 * 
	 */
	private JLabel[][] _labels = new JLabel[][] {
		{ null, null},
		{ null, null}
	};

	/**
	 * 
	 */
	private RadioButton[][] _radioButtons2 = new RadioButton[][] {
		{ null, null},
		{ null, null}
	};

	/**
	 * 
	 */
	private ComboBox[] _keyword_key_comboBoxes = new ComboBox[] {
		null, null
	};

	/**
	 * 
	 */
	private TextField[] _key_textFields = new TextField[] {
		null, null
	};

	/**
	 * 
	 */
	private RadioButton[][] _radioButtons3 = new RadioButton[][] {
		{ null, null, null},
		{ null, null, null}
	};

	/**
	 * 
	 */
	private ComboBox[] _probability_value_comboBoxes = new ComboBox[] {
		null, null
	};

	/**
	 * 
	 */
	private ComboBox[] _keyword_value_comboBoxes = new ComboBox[] {
		null, null
	};

	/**
	 * 
	 */
	private ComboBox[] _number_object_value_comboBoxes = new ComboBox[] {
		null, null
	};

	/**
	 * 
	 */
	private JLabel[][] _dummies1 = new JLabel[][] {
		{ null, null, null},
		{ null, null, null}
	};

	/**
	 * 
	 */
	private JLabel[][] _dummies2 = new JLabel[][] {
		{ null, null}
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
	public MapCommandPropertyPage(String title, String kind,
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


		create1();
		create2();
		create3();


		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_spot_checkBox_and_spot_selector( north_panel);

		insert_vertical_strut( north_panel);

		setup_spot_variable_checkBox_and_spot_variable_comboBox( north_panel);

		insert_vertical_strut( north_panel);

		setup_map_comboBox( north_panel);

		insert_vertical_strut( north_panel);

		setup_get( north_panel);

		insert_vertical_strut( north_panel);

		setup_set( north_panel);

		insert_vertical_strut( north_panel);

		basic_panel.add( north_panel, "North");


		add( basic_panel);


		setup_apply_button();


		adjust();


		return true;
	}

	/**
	 * 
	 */
	private void create1() {
		ButtonGroup buttonGroup = new ButtonGroup();

		String[][] texts = new String[][] {
			{ ResourceManager.get_instance().get( "edit.rule.dialog.command.map.get"),
				ResourceManager.get_instance().get( "edit.rule.dialog.command.map.label.value"),
				ResourceManager.get_instance().get( "edit.rule.dialog.command.map.label.key")
			},
			{ ResourceManager.get_instance().get( "edit.rule.dialog.command.map.put"),
				ResourceManager.get_instance().get( "edit.rule.dialog.command.map.label.key"),
				ResourceManager.get_instance().get( "edit.rule.dialog.command.map.label.value")
			}
		};

		for ( int i = 0; i < 2; ++i)
			create1( i, texts, buttonGroup);
	}

	/**
	 * @param index
	 * @param texts
	 * @param buttonGroup
	 */
	private void create1(final int index, String[][] texts, ButtonGroup buttonGroup) {
		_radioButtons1[ index] = create_radioButton( texts[ index][ 0], buttonGroup, true, false);
		_radioButtons1[ index].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_labels[ index][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ index][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());

				_radioButtons2[ index][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ index][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_keyword_key_comboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange()
					&& _radioButtons2[ index][ 0].isSelected());
				_key_textFields[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange()
					&& _radioButtons2[ index][ 1].isSelected());

				_radioButtons3[ index][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons3[ index][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons3[ index][ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_probability_value_comboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange()
					&& _radioButtons3[ index][ 0].isSelected());
				_keyword_value_comboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange()
					&& _radioButtons3[ index][ 1].isSelected());
				_number_object_value_comboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange()
					&& _radioButtons3[ index][ 2].isSelected());
			}
		});
		_labels[ index][ 0] = create_label( texts[ index][ 1], false);
		_labels[ index][ 1] = create_label( texts[ index][ 2], false);
	}

	/**
	 * 
	 */
	private void create2() {
		for ( int i = 0; i < 2; ++i)
			create2( i);
	}

	/**
	 * @param index
	 */
	private void create2(final int index) {
		ButtonGroup buttonGroup = new ButtonGroup();
		_radioButtons2[ index][ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.map.key.keyword"),
			buttonGroup, true, false);
		_radioButtons2[ index][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_keyword_key_comboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		_radioButtons2[ index][ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.map.key.immediate.data"),
			buttonGroup, true, false);
		_radioButtons2[ index][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_key_textFields[ index].setEnabled(
				ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		_keyword_key_comboBoxes[ index] = create_comboBox( null, _standardControlWidth, false);
		_key_textFields[ index] = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
	}

	/**
	 * 
	 */
	private void create3() {
		for ( int i = 0; i < 2; ++i)
			create3( i);
	}

	/**
	 * @param index
	 */
	private void create3(final int index) {
		ButtonGroup buttonGroup = new ButtonGroup();
		_radioButtons3[ index][ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.map.value.probability"),
			buttonGroup, true, false);
		_radioButtons3[ index][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_probability_value_comboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		_radioButtons3[ index][ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.map.value.keyword"),
			buttonGroup, true, false);
		_radioButtons3[ index][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_keyword_value_comboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		_radioButtons3[ index][ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.map.value.number.object"),
			buttonGroup, true, false);
		_radioButtons3[ index][ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_number_object_value_comboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		_probability_value_comboBoxes[ index] = create_comboBox( null, _standardControlWidth, false);
		_keyword_value_comboBoxes[ index] = create_comboBox( null, _standardControlWidth, false);
		_number_object_value_comboBoxes[ index] = create_comboBox( null, _standardControlWidth, false);
	}

	/**
	 * @param parent
	 */
	private void setup_spot_checkBox_and_spot_selector(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_spot_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.map.spot.check.box.name"),
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
	private void setup_map_comboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_map_label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.map.label.map"),
			true);
		panel.add( _map_label);

		_map_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _map_comboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_get(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		panel.add( _radioButtons1[ 0]);
		panel.add( _labels[ 0][ 0]);
		panel.add( _labels[ 0][ 1]);
		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies1[ 0][ 0] = new JLabel();
		panel.add( _dummies1[ 0][ 0]);
		panel.add( _radioButtons3[ 0][ 0]);
		panel.add( _probability_value_comboBoxes[ 0]);
		panel.add( _radioButtons2[ 0][ 0]);
		panel.add( _keyword_key_comboBoxes[ 0]);
		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies1[ 0][ 1] = new JLabel();
		panel.add( _dummies1[ 0][ 1]);
		panel.add( _radioButtons3[ 0][ 1]);
		panel.add( _keyword_value_comboBoxes[ 0]);
		panel.add( _radioButtons2[ 0][ 1]);
		panel.add( _key_textFields[ 0]);
		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies1[ 0][ 2] = new JLabel();
		panel.add( _dummies1[ 0][ 2]);
		panel.add( _radioButtons3[ 0][ 2]);
		panel.add( _number_object_value_comboBoxes[ 0]);
		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_set(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		panel.add( _radioButtons1[ 1]);
		panel.add( _labels[ 1][ 0]);
		panel.add( _labels[ 1][ 1]);
		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies1[ 1][ 0] = new JLabel();
		panel.add( _dummies1[ 1][ 0]);
		panel.add( _radioButtons2[ 1][ 0]);
		panel.add( _keyword_key_comboBoxes[ 1]);
		panel.add( _radioButtons3[ 1][ 0]);
		panel.add( _probability_value_comboBoxes[ 1]);
		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies1[ 1][ 1] = new JLabel();
		panel.add( _dummies1[ 1][ 1]);
		panel.add( _radioButtons2[ 1][ 1]);
		panel.add( _key_textFields[ 1]);
		panel.add( _radioButtons3[ 1][ 1]);
		panel.add( _keyword_value_comboBoxes[ 1]);
		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies1[ 1][ 2] = new JLabel();
		panel.add( _dummies1[ 1][ 2]);
		_dummies2[ 0][ 0] = new JLabel();
		panel.add( _dummies2[ 0][ 0]);
		_dummies2[ 0][ 1] = new JLabel();
		panel.add( _dummies2[ 0][ 1]);
		panel.add( _radioButtons3[ 1][ 2]);
		panel.add( _number_object_value_comboBoxes[ 1]);
		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width1 = 0;
		for ( int i = 0; i < _radioButtons1.length; ++i)
			width1 = Math.max( width1, _radioButtons1[ i].getPreferredSize().width);

		for ( int i = 0; i < _radioButtons1.length; ++i)
			_radioButtons1[ i].setPreferredSize( new Dimension( width1,
				_radioButtons1[ i].getPreferredSize().height));
		for ( int i = 0; i < _dummies1.length; ++i) {
			for ( int j = 0; j < _dummies1[ i].length; ++j)
				_dummies1[ i][ j].setPreferredSize( new Dimension( width1,
					_dummies1[ i][ j].getPreferredSize().height));
		}


		int width2 = 0;
		for ( int i = 0; i < _radioButtons2.length; ++i) {
			for ( int j = 0; j < _radioButtons2[ i].length; ++j) {
				width2 = Math.max( width2, _radioButtons2[ i][ j].getPreferredSize().width);
			}
		}
		for ( int i = 0; i < _radioButtons3.length; ++i) {
			for ( int j = 0; j < _radioButtons3[ i].length; ++j) {
				width2 = Math.max( width2, _radioButtons3[ i][ j].getPreferredSize().width);
			}
		}

		for ( int i = 0; i < _labels.length; ++i)
			_labels[ i][ 0].setPreferredSize( new Dimension(
				width2 + _key_textFields[ 1].getPreferredSize().width + 5,
				//width2 + _standard_control_width + 5,
				_labels[ i][ 0].getPreferredSize().height));
		for ( int i = 0; i < _radioButtons2.length; ++i) {
			for ( int j = 0; j < _radioButtons2[ i].length; ++j)
				_radioButtons2[ i][ j].setPreferredSize( new Dimension( width2,
					_radioButtons2[ i][ j].getPreferredSize().height));
		}
		for ( int i = 0; i < _radioButtons3.length; ++i) {
			for ( int j = 0; j < _radioButtons3[ i].length; ++j)
				_radioButtons3[ i][ j].setPreferredSize( new Dimension( width2,
					_radioButtons3[ i][ j].getPreferredSize().height));
		}
		for ( int i = 0; i < _dummies2.length; ++i)
			_dummies2[ i][ 0].setPreferredSize( new Dimension( width2,
				_dummies2[ i][ 0].getPreferredSize().height));


		_spot_checkBox.setPreferredSize( new Dimension( width1 + width2 + 5,
			_spot_checkBox.getPreferredSize().height));
		_spot_variable_checkBox.setPreferredSize( new Dimension( width1 + width2 + 5,
			_spot_variable_checkBox.getPreferredSize().height));
		_map_label.setPreferredSize( new Dimension( width1 + width2 + 5,
			_map_label.getPreferredSize().height));


		for ( int i = 0; i < _dummies2.length; ++i)
			_dummies2[ i][ 1].setPreferredSize(
				new Dimension( _key_textFields[ 1].getPreferredSize().width,
				//new Dimension( _standard_control_width,
					_dummies2[ i][ 1].getPreferredSize().height));
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

		CommonTool.update( _map_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_map_names( false) : get_spot_map_names( false));

		for ( int i = 0; i < _radioButtons1.length; ++i) {
			CommonTool.update( _keyword_key_comboBoxes[ i], ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_keyword_names( false) : get_spot_keyword_names( false));
			CommonTool.update( _probability_value_comboBoxes[ i], ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_probability_names( false) : get_spot_probability_names( false));
			CommonTool.update( _keyword_value_comboBoxes[ i], ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_keyword_names( false) : get_spot_keyword_names( false));
			CommonTool.update( _number_object_value_comboBoxes[ i], ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _map_comboBox, get_spot_map_names( false));

		for ( int i = 0; i < _radioButtons1.length; ++i) {
			CommonTool.update( _keyword_key_comboBoxes[ i], get_spot_keyword_names( false));
			CommonTool.update( _probability_value_comboBoxes[ i], get_spot_probability_names( false));
			CommonTool.update( _keyword_value_comboBoxes[ i], get_spot_keyword_names( false));
			CommonTool.update( _number_object_value_comboBoxes[ i], get_spot_number_object_names( false));
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(spotObject, number, objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _map_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "map", number, false) : get_spot_map_names( false));

		for ( int i = 0; i < _radioButtons1.length; ++i) {
			CommonTool.update( _keyword_key_comboBoxes[ i], !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
			CommonTool.update( _probability_value_comboBoxes[ i], !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
			CommonTool.update( _keyword_value_comboBoxes[ i], !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
			CommonTool.update( _number_object_value_comboBoxes[ i], !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
		}
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

		for ( int i = _radioButtons1.length - 1; i >= 0; --i) {
			_radioButtons2[ i][ 0].setSelected( true);
			_radioButtons3[ i][ 0].setSelected( true);
			_radioButtons1[ i].setSelected( true);
		}
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


		String[] spots = CommonRuleManipulator.get_spot( rule._value);
		if ( null == spots) {
			set_handler();
			return false;
		}

		int kind = MapCommand.get_kind( rule._value);
		if ( 0 > kind) {
			set_handler();
			return false;
		}

		String[] elements = CommonRuleManipulator.get_elements( rule._value, 3);
		if ( null == elements) {
			set_handler();
			return false;
		}

		if ( !set( spots[ 0], spots[ 1], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox)) {
			set_handler();
			return false;
		}

		_radioButtons1[ kind].setSelected( true);

		if ( 0 == kind) {
			if ( !set( kind, CommonRuleManipulator.get_full_prefix( spots), elements[ 1], elements[ 2], elements[ 0])) {
				set_handler();
				return false;
			}
		} else if ( 1 == kind) {
			if ( !set( kind, CommonRuleManipulator.get_full_prefix( spots), elements[ 0], elements[ 1], elements[ 2])) {
				set_handler();
				return false;
			}
		} else {
			set_handler();
			return false;
		}

		set_handler();

		return true;
	}

	/**
	 * @param kind
	 * @param spot
	 * @param map
	 * @param key
	 * @param value
	 */
	private boolean set(int kind, String spot, String map, String key, String value) {
		if ( !CommonRuleManipulator.is_object( "map", spot + map, LayerManager.get_instance()))
			return false;

		_map_comboBox.setSelectedItem( map);


		if ( key.startsWith( "\"") && key.endsWith( "\"")) {
			_radioButtons2[ kind][ 1].setSelected( true);
			_key_textFields[ kind].setText( key.substring( 1, key.length() - 1));
		} else {
			if ( !CommonRuleManipulator.is_object( "keyword", spot + key, LayerManager.get_instance()))
				return false;

			_radioButtons2[ kind][ 0].setSelected( true);
			_keyword_key_comboBoxes[ kind].setSelectedItem( key);
		}


		if ( CommonRuleManipulator.is_object( "probability", spot + value, LayerManager.get_instance())) {
			_radioButtons3[ kind][ 0].setSelected( true);
			_probability_value_comboBoxes[ kind].setSelectedItem( value);
		} else if ( CommonRuleManipulator.is_object( "keyword", spot + value, LayerManager.get_instance())) {
			_radioButtons3[ kind][ 1].setSelected( true);
			_keyword_value_comboBoxes[ kind].setSelectedItem( value);
		} else if ( CommonRuleManipulator.is_object( "number object", spot + value, LayerManager.get_instance())) {
			_radioButtons3[ kind][ 2].setSelected( true);
			_number_object_value_comboBoxes[ kind].setSelectedItem( value);
		} else
			return false;


		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		String map = get1( _map_comboBox);
		if ( null == map)
			return null;

		int kind1 = SwingTool.get_enabled_radioButton( _radioButtons1);
		int kind2 = SwingTool.get_enabled_radioButton( _radioButtons2[ kind1]);
		int kind3 = SwingTool.get_enabled_radioButton( _radioButtons3[ kind1]);

		String key = null;
		switch ( kind2) {
			case 0:
				key = get1( _keyword_key_comboBoxes[ kind1]);
				break;
			case 1:
				if ( null == _key_textFields[ kind1].getText()
					|| _key_textFields[ kind1].getText().equals( "$")
					|| 0 < _key_textFields[ kind1].getText().indexOf( '$')
					|| _key_textFields[ kind1].getText().startsWith( " ")
					|| _key_textFields[ kind1].getText().endsWith( " ")
					|| _key_textFields[ kind1].getText().equals( "$Name")
					|| _key_textFields[ kind1].getText().equals( "$Role")
					|| _key_textFields[ kind1].getText().equals( "$Spot")
					|| 0 <= _key_textFields[ kind1].getText().indexOf( Constant._experimentName))
					return null;

				if ( _key_textFields[ kind1].getText().startsWith( "$")
					&& ( 0 <= _key_textFields[ kind1].getText().indexOf( " ")
					|| 0 < _key_textFields[ kind1].getText().indexOf( "$", 1)
					|| 0 < _key_textFields[ kind1].getText().indexOf( ")", 1)))
					return null;

				key = get1( _key_textFields[ kind1], true);
				key = "\"" + key + "\"";
				break;
		}

		if ( null == key)
			return null;

		String val = null;
		switch ( kind3) {
			case 0:
				val = get1( _probability_value_comboBoxes[ kind1]);
				break;
			case 1:
				val = get1( _keyword_value_comboBoxes[ kind1]);
				break;
			case 2:
				val = get1( _number_object_value_comboBoxes[ kind1]);
				break;
		}

		if ( null == val)
			return null;

		String value = null;
		switch ( kind1) {
			case 0:
				value = val + "=" + map + "=" + key;
				break;
			case 1:
				value = map + "=" + key + "=" + val;
				break;
		}

		if ( null == value)
			return null;

		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);

		return Rule.create( _kind, _type, spot + MapCommand._reserved_words[ kind1] + value);
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		String map = get1( _map_comboBox);
//		if ( null == map)
//			return false;
//
//		int kind1 = SwingTool.get_enabled_radioButton( _radioButtons1);
//		int kind2 = SwingTool.get_enabled_radioButton( _radioButtons2[ kind1]);
//		int kind3 = SwingTool.get_enabled_radioButton( _radioButtons3[ kind1]);
//
//		String key = null;
//		switch ( kind2) {
//			case 0:
//				key = get1( _keyword_key_comboBoxes[ kind1]);
//				break;
//			case 1:
//				if ( null == _key_textFields[ kind1].getText()
//					|| _key_textFields[ kind1].getText().equals( "$")
//					|| 0 < _key_textFields[ kind1].getText().indexOf( '$')
//					|| _key_textFields[ kind1].getText().startsWith( " ")
//					|| _key_textFields[ kind1].getText().endsWith( " ")
//					|| _key_textFields[ kind1].getText().equals( "$Name")
//					|| _key_textFields[ kind1].getText().equals( "$Role")
//					|| _key_textFields[ kind1].getText().equals( "$Spot")
//					|| 0 <= _key_textFields[ kind1].getText().indexOf( Constant._experiment_name))
//					return false;
//
//				if ( _key_textFields[ kind1].getText().startsWith( "$")
//					&& ( 0 <= _key_textFields[ kind1].getText().indexOf( " ")
//					|| 0 < _key_textFields[ kind1].getText().indexOf( "$", 1)
//					|| 0 < _key_textFields[ kind1].getText().indexOf( ")", 1)))
//					return false;
//
//				key = get1( _key_textFields[ kind1], true);
//				key = "\"" + key + "\"";
//				break;
//		}
//
//		if ( null == key)
//			return false;
//
//		String val = null;
//		switch ( kind3) {
//			case 0:
//				val = get1( _probability_value_comboBoxes[ kind1]);
//				break;
//			case 1:
//				val = get1( _keyword_value_comboBoxes[ kind1]);
//				break;
//			case 2:
//				val = get1( _number_object_value_comboBoxes[ kind1]);
//				break;
//		}
//
//		if ( null == val)
//			return false;
//
//		String value = null;
//		switch ( kind1) {
//			case 0:
//				value = val + "=" + map + "=" + key;
//				break;
//			case 1:
//				value = map + "=" + key + "=" + val;
//				break;
//		}
//
//		if ( null == value)
//			return false;
//
//		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
//
//		rule._value = ( spot + MapCommandRuleManipulator._reserved_words[ kind1] + value);
//		rule._type = _type;
//
//		return true;
//	}
}
