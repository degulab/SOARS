/*
 * Created on 2006/03/06
 */
package soars.application.visualshell.object.role.base.edit.tab.common.number;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.player.base.object.number.NumberObject;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.common.number.ExpressionElements;
import soars.application.visualshell.object.role.base.object.common.number.NumberRule;
import soars.application.visualshell.object.role.base.object.common.number.SubType;
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
public class NumberObjectPropertyPageBase extends RulePropertyPageBase {

	/**
	 * 
	 */
	protected HashMap[] _variable_maps = new HashMap[] { null, null};

	/**
	 * 
	 */
	protected CheckBox _spot_checkBox = null;

	/**
	 * 
	 */
	protected ObjectSelector _spot_selector = null;

	/**
	 * 
	 */
	protected CheckBox _spot_variable_checkBox = null;

	/**
	 * 
	 */
	protected ComboBox _spot_variable_comboBox = null;

	/**
	 * 
	 */
	protected ComboBox _number_object_comboBox = null;

	/**
	 * 
	 */
	protected RadioButton[][] _radioButtons = new RadioButton[][] {
		{ null, null, null, null},
		{ null, null, null, null}
	};

	/**
	 * 
	 */
	protected ComboBox _operator_comboBox1 = null;

	/**
	 * 
	 */
	protected TextField[] _value_textFields = new TextField[] { null, null};

	/**
	 * 
	 */
	protected ComboBox[] _number_object_comboBoxes = new ComboBox[] { null, null};

	/**
	 * 
	 */
	protected ComboBox[] _function_comboBoxes = new ComboBox[] { null, null};

	/**
	 * 
	 */
	protected TextField[] _expression_textFields = new TextField[] { null, null};

	/**
	 * 
	 */
	protected JScrollPane[] _variableValueTable_scrollPanes = new JScrollPane[] { null, null};

	/**
	 * 
	 */
	protected VariableValueTable[] _variableValueTables = new VariableValueTable[] { null, null};

	/**
	 * 
	 */
	protected TextField[] _others_textFields = new TextField[] { null, null};

	/**
	 * 
	 */
	protected JLabel _number_object_label = null;

	/**
	 * 
	 */
	protected JLabel _operator_label = null;

	/**
	 * 
	 */
	protected JLabel[] _expression_labels = new JLabel[] { null, null};

	/**
	 * 
	 */
	protected JLabel _dummy[] = new JLabel[] {
		null, null, null, null
	};

	/**
	 * 
	 */
	private int _row = -1;

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
	public NumberObjectPropertyPageBase(String title, String kind,
		String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
		for ( int i = 0; i < _variable_maps.length; ++i)
			_variable_maps[ i] = VisualShellExpressionManager.get_instance().get_variable_map();
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

		setup_number_object_and_operator( north_panel);

		insert_vertical_strut( north_panel);

		ButtonGroup buttonGroup1[] = new ButtonGroup[] { new ButtonGroup(), new ButtonGroup()};

		setup_value( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_number_object( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_function( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_expression( north_panel);

		insert_vertical_strut( north_panel);

		setup_variable_value_table( north_panel);

		insert_vertical_strut( north_panel);

		setup_others( buttonGroup1, north_panel);

		setup_handler();

		basic_panel.add( north_panel, "North");


		add( basic_panel);


		setup_apply_button();


		adjust();


		return true;
	}

	/**
	 * @param parent
	 */
	protected void setup_spot_checkBox_and_spot_selector(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_spot_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.spot.check.box.name1"),
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


		setup_comparative_operator( panel);


		parent.add( panel);
	}

	/**
	 * @param panel
	 */
	protected void setup_comparative_operator(JPanel panel) {
	}

	/**
	 * @param parent
	 */
	protected void setup_number_object_and_operator(JPanel parent) {
	}

	/**
	 * @param component
	 * @param parent
	 */
	protected void setup_number_object_and_operator(JComponent component, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_number_object_label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.label.number.object"),
			false);
		_number_object_label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _number_object_label);

		_number_object_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _number_object_comboBox);

		panel.add( component);

		parent.add( panel);
	}

	/**
	 * @param buttonGroups
	 * @param parent
	 */
	protected void setup_value(ButtonGroup[] buttonGroups, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_radioButtons[ i][ 0] = create_radioButton(
				ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.value.radio.button"),
				buttonGroups[ i], true, false);
			_value_textFields[ i] = create_textField( new TextExcluder( Constant._prohibitedCharacters5), true);
			panel.add( _radioButtons[ i][ 0]);
			panel.add( _value_textFields[ i]);

			if ( 0 == i) {
				_dummy[ 0] = new JLabel();
				panel.add( _dummy[ 0]);
			}
		}

		parent.add( panel);
	}

	/**
	 * @param buttonGroups
	 * @param parent
	 */
	private void setup_number_object(ButtonGroup[] buttonGroups, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_radioButtons[ i][ 1] = create_radioButton(
				ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.number.object.radio.button"),
				buttonGroups[ i], true, false);
			panel.add( _radioButtons[ i][ 1]);

			_number_object_comboBoxes[ i] = create_comboBox( null, _standardControlWidth, false);
			panel.add( _number_object_comboBoxes[ i]);

			if ( 0 == i) {
				_dummy[ 1] = new JLabel();
				panel.add( _dummy[ 1]);
			}
		}

		parent.add( panel);
	}

	/**
	 * @param buttonGroups
	 * @param parent
	 */
	private void setup_function(ButtonGroup[] buttonGroups, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_radioButtons[ i][ 2] = create_radioButton(
				ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.function.radio.button"),
				buttonGroups[ i], true, false);
			_function_comboBoxes[ i] = create_comboBox(
				VisualShellExpressionManager.get_instance().get_functions(),
				_standardControlWidth, false);
			panel.add( _radioButtons[ i][ 2]);
			panel.add( _function_comboBoxes[ i]);

			if ( 0 == i) {
				_operator_label = create_label(
					ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.label.operator"),
					false);
				panel.add( _operator_label);
			}
		}

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	protected void setup_expression(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_expression_labels[ i] = create_label(
				ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.label.expression"),
				false);
			_expression_labels[ i].setHorizontalAlignment( SwingConstants.RIGHT);
			panel.add( _expression_labels[ i]);

			_expression_textFields[ i] = create_textField( false);
			_expression_textFields[ i].setEditable( false);
			panel.add( _expression_textFields[ i]);

			if ( 0 == i) {
				_operator_comboBox1 = create_comboBox( new String[] { "", "+", "-", "*", "/", "%"}, 50, true);
				panel.add( _operator_comboBox1);
			}
		}

		parent.add( panel);
	}

	/**
	 * @param parent
	 * @return
	 */
	protected boolean setup_variable_value_table(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_variableValueTables[ i] = new VariableValueTable( _variable_maps[ i], _color, _owner, _parent);

			if ( !_variableValueTables[ i].setup())
				return false;

			_variableValueTable_scrollPanes[ i] = new JScrollPane();
			_variableValueTable_scrollPanes[ i].getViewport().setView( _variableValueTables[ i]);

			panel.add( _variableValueTable_scrollPanes[ i]);

			if ( 0 == i) {
				_dummy[ 2] = new JLabel();
				panel.add( _dummy[ 2]);
			}
		}

		parent.add( panel);

		return true;
	}

	/**
	 * @param buttonGroups
	 * @param parent
	 */
	private void setup_others(ButtonGroup[] buttonGroups, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_radioButtons[ i][ 3] = create_radioButton(
				ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.others.radio.button"),
				buttonGroups[ i], true, false);
			_others_textFields[ i] = create_textField( new TextExcluder( Constant._prohibitedCharacters15), false);
			panel.add( _radioButtons[ i][ 3]);
			panel.add( _others_textFields[ i]);

			if ( 0 == i) {
				_dummy[ 3] = new JLabel();
				panel.add( _dummy[ 3]);
			}
		}

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void setup_handler() {
		_operator_comboBox1.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String operator = ( String)_operator_comboBox1.getSelectedItem();
				for ( int i = 0; i < _radioButtons[ 1].length; ++i)
					_radioButtons[ 1][ i].setEnabled( !operator.equals( ""));

				boolean[] enables = new boolean[] {
					false, false, false, false
				};
				if ( !operator.equals( ""))
					enables[ SwingTool.get_enabled_radioButton( _radioButtons[ 1])] = true;

				update_components( enables, 1);
			}
		});

		for ( int i = 0; i < _radioButtons.length; ++i) {
			addItemListener( _radioButtons[ i][ 0], _value_textFields[ i]);
//			addItemListener( _radioButtons[ i][ 1], _spot_checkBoxes[ i], _spot_selectors[ i], _number_object_comboBoxes[ i]);
			addItemListener( _radioButtons[ i][ 1], _number_object_comboBoxes[ i]);
			addItemListener( _radioButtons[ i][ 2], _function_comboBoxes[ i], _expression_labels[ i], _expression_textFields[ i], _variableValueTables[ i]);
			addActionListener( _function_comboBoxes[ i], _expression_textFields[ i], _variableValueTables[ i]);
			addItemListener( _function_comboBoxes[ i], _variableValueTables[ i]);
			addItemListener( _radioButtons[ i][ 3], _others_textFields[ i]);
		}
	}

	/**
	 * @param radioButton
	 * @param textField
	 */
	private void addItemListener(RadioButton radioButton, final TextField textField) {
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
	}

	/**
	 * @param radioButton
	 * @param comboBox
	 */
	private void addItemListener(RadioButton radioButton, final ComboBox comboBox) {
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
	}

	/**
	 * @param radioButton
	 * @param function_comboBox
	 * @param expression_label
	 * @param expression_textField
	 * @param variableValueTable
	 */
	private void addItemListener(RadioButton radioButton, final ComboBox function_comboBox, final JLabel expression_label, final TextField expression_textField, final VariableValueTable variableValueTable) {
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				function_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				expression_label.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				expression_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());

				if ( ItemEvent.SELECTED == arg0.getStateChange()) {
					if ( 0 <= _row && variableValueTable.getRowCount() > _row)
						variableValueTable.setRowSelectionInterval( _row, _row);
				} else {
					_row = variableValueTable.getSelectedRow();
					if ( 0 < variableValueTable.getRowCount())
						variableValueTable.removeRowSelectionInterval( 0, variableValueTable.getRowCount() - 1);
				}
				variableValueTable.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				variableValueTable.getTableHeader().repaint();
			}
		});
	}

	/**
	 * @param function_comboBox
	 * @param expression_textField
	 * @param variableValueTable
	 */
	private void addActionListener(final ComboBox function_comboBox, final TextField expression_textField, final VariableValueTable variableValueTable) {
		function_comboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				expression_textField.setText( VisualShellExpressionManager.get_instance().get_expression(
					( String)function_comboBox.getSelectedItem()));
				variableValueTable.set( ( String)function_comboBox.getSelectedItem());
			}
		});
	}

	/**
	 * @param function_comboBox
	 * @param variableValueTable
	 */
	private void addItemListener(ComboBox function_comboBox, final VariableValueTable variableValueTable) {
		function_comboBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if ( ItemEvent.DESELECTED == arg0.getStateChange())
					variableValueTable.update_variable_map( ( String)arg0.getItem());
			}
		});
	}

	/**
	 * 
	 */
	protected void adjust() {
		int width1 = 0;
		JComponent[] components = new JComponent[] {
			_spot_checkBox,
			_spot_variable_checkBox,
			_number_object_label,
			_radioButtons[ 0][ 0],
			_radioButtons[ 1][ 0],
			_radioButtons[ 0][ 1],
			_radioButtons[ 1][ 1],
			_radioButtons[ 0][ 2],
			_radioButtons[ 1][ 2],
			_expression_labels[ 0],
			_expression_labels[ 1],
			_radioButtons[ 0][ 3],
			_radioButtons[ 1][ 3]
		};

		for ( int i = 0; i < components.length; ++i)
			width1 = Math.max( width1, components[ i].getPreferredSize().width);
		
		for ( int i = 0; i < components.length; ++i)
			components[ i].setPreferredSize(
				new Dimension( width1, components[ i].getPreferredSize().height));


		int width2 = _spot_selector._objectNameComboBox.getPreferredSize().width
			+ _spot_selector._numberSpinner.getPreferredSize().width;
		components = new JComponent[] {
			_value_textFields[ 0],
			_value_textFields[ 1],
			_number_object_comboBoxes[ 0],
			_number_object_comboBoxes[ 1],
			_function_comboBoxes[ 0],
			_function_comboBoxes[ 1],
			_expression_textFields[ 0],
			_expression_textFields[ 1],
			_others_textFields[ 0],
			_others_textFields[ 1]
		};
		
		for ( int i = 0; i < components.length; ++i)
			components[ i].setPreferredSize(
				new Dimension( width2, components[ i].getPreferredSize().height));


		for ( int i = 0; i < _radioButtons.length; ++i)
			_variableValueTable_scrollPanes[ i].setPreferredSize(
				new Dimension( width1 + width2 + 5, 100));


		int width3 = _operator_comboBox1.getPreferredSize().width;
		width3 = Math.max( width3, _operator_label.getPreferredSize().width);

		_operator_label.setPreferredSize( new Dimension( width3,
			_operator_label.getPreferredSize().height));

		_operator_comboBox1.setPreferredSize( new Dimension( width3,
			_operator_comboBox1.getPreferredSize().height));

		for ( int i = 0; i < _dummy.length; ++i)
			_dummy[ i].setPreferredSize( new Dimension( width3,
				_dummy[ i].getPreferredSize().height));
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

		CommonTool.update( _number_object_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
		CommonTool.update( _number_object_comboBoxes[ 0], ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
		CommonTool.update( _number_object_comboBoxes[ 1], ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));

		for ( int i = 0; i < _variableValueTables.length; ++i)
			_variableValueTables[ i].update( !spot_variable_checkBox.isSelected() ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _number_object_comboBox, get_spot_number_object_names( false));
		CommonTool.update( _number_object_comboBoxes[ 0], get_spot_number_object_names( false));
		CommonTool.update( _number_object_comboBoxes[ 1], get_spot_number_object_names( false));

		for ( int i = 0; i < _variableValueTables.length; ++i)
			_variableValueTables[ i].update( get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(spotObject, number, objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _number_object_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
		CommonTool.update( _number_object_comboBoxes[ 0], !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
		CommonTool.update( _number_object_comboBoxes[ 1], !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));

		for ( int i = 0; i < _variableValueTables.length; ++i)
			_variableValueTables[ i].update( !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
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
	protected void initialize() {
		if ( _role instanceof AgentRole) {
			_spot_checkBox.setSelected( false);
			_spot_selector.setEnabled( false);
		} else {
			_spot_checkBox.setSelected( true);
			_spot_checkBox.setEnabled( false);
			_spot_selector.setEnabled( true);
		}

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_radioButtons[ i][ 0].setSelected( true);
		}

		for ( int i = 0; i < _radioButtons[ 1].length; ++i)
			_radioButtons[ 1][ i].setEnabled( false);

		update_components( new boolean[] {
			true, false, false, false
		}, 0);

		update_components( new boolean[] {
			false, false, false, false
		}, 1);
	}

	/**
	 * @param enables
	 * @param index
	 */
	private void update_components(boolean[] enables, int index) {
		_value_textFields[ index].setEnabled( enables[ 0]);

		_number_object_comboBoxes[ index].setEnabled( enables[ 1]);

		_function_comboBoxes[ index].setEnabled( enables[ 2]);
		_expression_labels[ index].setEnabled( enables[ 2]);
		_expression_textFields[ index].setEnabled( enables[ 2]);
		_variableValueTable_scrollPanes[ index].setEnabled( enables[ 2]);
		_variableValueTables[ index].setEnabled( enables[ 2]);

		_others_textFields[ index].setEnabled( enables[ 3]);
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
//		CommonTool.update( _number_object_comboBox, get_agent_number_object_names( false));
//
//		for ( int i = 0; i < _variableValueTables.length; ++i)
//			_variableValueTables[ i].update( get_agent_number_object_names( false));
//
//		for ( int i = 0; i < _radioButtons.length; ++i) {
//			CommonTool.update( _number_object_comboBoxes[ i], get_agent_number_object_names( false));
//			_expression_textFields[ i].setText( VisualShellExpressionManager.get_instance().get_expression(
//				( String)_function_comboBoxes[ i].getSelectedItem()));
//			_variableValueTables[ i].set( ( String)_function_comboBoxes[ i].getSelectedItem());
//		}

		reset( _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_expression_textFields[ i].setText( VisualShellExpressionManager.get_instance().get_expression(
				( String)_function_comboBoxes[ i].getSelectedItem()));
			_variableValueTables[ i].set( ( String)_function_comboBoxes[ i].getSelectedItem());
		}

		super.on_setup_completed();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.tab.TabbedPage#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		_parent.on_apply( this, actionEvent);
	}

	/**
	 * @param value
	 * @param subType
	 * @return
	 */
	protected boolean set(String value, SubType subType) {
		String main_value = subType.get_value( value);
		if ( null == main_value)
			return false;

		if ( subType._operator.equals( "")) {
			if ( !set1( main_value, subType._kind[ 0], 0))
				return false;
		} else {
			String[] values = main_value.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return false;

			if ( !set1( values[ 0], subType._kind[ 0], 0))
				return false;

			if ( !set2( values[ 1], subType._kind[ 1], 1))
				return false;
		}

		_operator_comboBox1.setSelectedItem( subType._operator);

		return true;
	}

	/**
	 * @param value
	 * @param kind
	 * @param index
	 * @return
	 */
	private boolean set1(String value, int kind, int index) {
		switch ( kind) {
			case 0:
				if ( !set_value1( value, index))
					return false;
				break;
			case 1:
				if ( !set_number_object1( value, index))
					return false;
				break;
			case 2:
				if ( !set_expression1( value, index))
					return false;
				break;
			case 3:
				if ( !set_others1( value, index))
					return false;
				break;
			default:
				return false;
		}
	
		_radioButtons[ index][ kind].setSelected( true);
		return true;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	protected boolean set_value1(String value, int index) {
		return false;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	protected boolean set_number_object1(String value, int index) {
		return false;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	protected boolean set_expression1(String value, int index) {
		return false;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	protected boolean set_others1(String value, int index) {
		return false;
	}

	/**
	 * @param value
	 * @param kind
	 * @param index
	 * @return
	 */
	private boolean set2(String value, int kind, int index) {
		switch ( kind) {
			case 0:
				if ( !set_value2( value, index))
					return false;
				break;
			case 1:
				if ( !set_number_object2( value, index))
					return false;
				break;
			case 2:
				if ( !set_expression2( value, index))
					return false;
				break;
			case 3:
				if ( !set_others2( value, index))
					return false;
				break;
			default:
				return false;
		}
	
		_radioButtons[ index][ kind].setSelected( true);
		return true;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	private boolean set_value2(String value, int index) {
		_value_textFields[ index].setText( value);
		return true;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	private boolean set_number_object2(String value, int index) {
		_number_object_comboBoxes[ index].setSelectedItem( value);
		return true;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	private boolean set_expression2(String value, int index) {
		ExpressionElements expressionElements = NumberRule.get2( value);
		if ( null == expressionElements)
			return false;

		set_expression( expressionElements, index);

		return true;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	private boolean set_others2(String value, int index) {
		_others_textFields[ index].setText( value);
		return true;
	}

	/**
	 * @param number_object
	 * @param spot_checkBox
	 * @param spot_selector
	 * @param spot_variable_checkBox
	 * @param spot_variable_comboBox
	 * @param number_object_comboBox
	 * @return
	 */
	protected boolean set_number_object(String number_object, CheckBox spot_checkBox, ObjectSelector spot_selector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox, ComboBox number_object_comboBox) {
		String[] values = CommonRuleManipulator.get_spot_and_object( number_object);
		if ( null == values) {
			set_handler();
			return false;
		}

		if ( null == values[ 0] && _role instanceof SpotRole) {
			set_handler();
			return false;
		}

		if ( !set( values[ 0], values[ 1], spot_checkBox, spot_selector, spot_variable_checkBox, spot_variable_comboBox)) {
			set_handler();
			return false;
		}

		number_object_comboBox.setSelectedItem( values[ 2]);

		return true;
	}

	/**
	 * @param expressionElements
	 * @param index
	 */
	protected void set_expression(ExpressionElements expressionElements, int index) {
		_function_comboBoxes[ index].setSelectedItem( expressionElements._function);
		_expression_textFields[ index].setText( VisualShellExpressionManager.get_instance().get_expression(
			expressionElements._function));
		_variable_maps[ index].put( expressionElements._function, expressionElements._variables);
		_variableValueTables[ index].set( expressionElements._function);
		_variableValueTables[ index].update();
	}

	/**
	 * @param number_object
	 * @param sub_types
	 * @param operator2
	 * @return
	 */
	protected Rule get(String number_object, String[] sub_types, String operator2) {
		SubType subType = SubType.get( SwingTool.get_enabled_radioButton( _radioButtons[ 0]),
			_operator_comboBox1, SwingTool.get_enabled_radioButton( _radioButtons[ 1]));
		if ( null == subType)
			return null;

		String sub_type = subType.get( sub_types);

		String value1 = get( subType._kind[ 0], 0);
		if ( null == value1)
			return null;

		if ( null == subType._operator || subType._operator.equals( ""))
			return Rule.create( _kind, _type, sub_type + number_object + operator2 + " " + value1);

		String value2 = get( subType._kind[ 1], 1);
		if ( null == value2)
			return null;

		return Rule.create( _kind, _type, sub_type + number_object + operator2 + " " + value1 + " " + subType._operator + " " + value2);
	}

//	/**
//	 * @param rule
//	 * @param number_object
//	 * @param sub_types
//	 * @param operator2
//	 * @return
//	 */
//	protected boolean get(Rule rule, String number_object, String[] sub_types, String operator2) {
//		SubType subType = SubType.get( SwingTool.get_enabled_radioButton( _radioButtons[ 0]),
//			_operator_comboBox1, SwingTool.get_enabled_radioButton( _radioButtons[ 1]));
//		if ( null == subType)
//			return false;
//
//		String sub_type = subType.get( sub_types);
//
//		String value1 = get( subType._kind[ 0], 0);
//		if ( null == value1)
//			return false;
//
//		if ( null == subType._operator || subType._operator.equals( "")) {
//			rule._value = sub_type + number_object + operator2 + " " + value1;
//			rule._type = _type;
//			return true;
//		}
//
//		String value2 = get( subType._kind[ 1], 1);
//		if ( null == value2)
//			return false;
//
//		rule._value = sub_type + number_object + operator2 + " " + value1 + " " + subType._operator + " " + value2;
//		rule._type = _type;
//
//		return true;
//	}

	/**
	 * @param kind
	 * @param index
	 * @return
	 */
	private String get(int kind, int index) {
		switch ( kind) {
			case 0:
				return get_value( _value_textFields[ index]);
			case 1:
				return get_number_object( index);
			case 2:
				return get_expression( _function_comboBoxes[ index], _variableValueTables[ index]);
			case 3:
				return get_others( _others_textFields[ index]);
				//return _others_textFields[ index].getText();
			default:
				return null;
		}
	}

	/**
	 * @param spot_checkBox
	 * @return
	 */
	protected String get_value(TextField value_textField) {
		String value = value_textField.getText();
		if ( null == value || value.equals( "")
			|| value.equals( "$") || 0 < value.indexOf( '$')
			|| value.equals( "$Name") || value.equals( "$Role") || value.equals( "$Spot")
			|| 0 <= value.indexOf( Constant._experimentName))
			return null;

		if ( value.startsWith( "$")
			&& ( 0 < value.indexOf( "$", 1) || 0 < value.indexOf( ")", 1)))
			return null;

		String result = NumberObject.is_correct( value, "integer");
		if ( null == result || result.equals( "")) {
			result = NumberObject.is_correct( value, "real number");
			if ( null == result || result.equals( ""))
				return null;
		}

		return result;
	}

	/**
	 * @param spot_checkBox
	 * @param spot_selector
	 * @param spot_variable_checkBox
	 * @param spot_variable_comboBox
	 * @param number_object_comboBox
	 * @return
	 */
	protected String get_number_object(CheckBox spot_checkBox, ObjectSelector spot_selector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox, ComboBox number_object_comboBox) {
		String number_object = ( String)number_object_comboBox.getSelectedItem();
		if ( null == number_object || number_object.equals( ""))
			return null;

		String spot = get( spot_checkBox, spot_selector, spot_variable_checkBox, spot_variable_comboBox);

		return ( spot + number_object);
	}

	/**
	 * @param index
	 * @return
	 */
	protected String get_number_object(int index) {
		String number_object = ( String)_number_object_comboBoxes[ index].getSelectedItem();
		if ( null == number_object || number_object.equals( ""))
			return null;

		return number_object;
	}

	/**
	 * @param function_comboBox
	 * @param variableValueTable
	 * @return
	 */
	protected String get_expression(ComboBox function_comboBox, VariableValueTable variableValueTable) {
		String function = ( String)function_comboBox.getSelectedItem();
		if ( null == function || function.equals( ""))
			return null;

		Vector variables = variableValueTable.get();
		if ( null == variables)
			return null;

		return NumberRule.get( function, variables);
	}

	/**
	 * @param others_textField
	 * @return
	 */
	protected String get_others(TextField others_textField) {
		if ( null == others_textField.getText()
			|| others_textField.getText().equals( "$")
			|| 0 < others_textField.getText().indexOf( '$')
			|| others_textField.getText().equals( "$Name")
			|| others_textField.getText().equals( "$Role")
			|| others_textField.getText().equals( "$Spot")
			|| 0 <= others_textField.getText().indexOf( Constant._experimentName))
			return null;

		if ( others_textField.getText().startsWith( "$")
			&& ( 0 < others_textField.getText().indexOf( "$", 1)
			|| 0 < others_textField.getText().indexOf( ")", 1)))
			return null;

		return others_textField.getText();
	}
}
