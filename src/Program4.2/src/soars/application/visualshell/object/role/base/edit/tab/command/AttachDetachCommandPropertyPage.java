/**
 * 
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
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.command.base.AttachAndDetachCommand;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class AttachDetachCommandPropertyPage extends RulePropertyPageBase {

	/**
	 * 
	 */
	private JLabel _which_label = null;

	/**
	 * 
	 */
	private ComboBox _which_comboBox = null;

	/**
	 * 
	 */
	private RadioButton[] _radioButtons1 = new RadioButton[] {
		null, null, null, null,
		null
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
	private ObjectSelector _agent_selector = null;

	/**
	 * 
	 */
	private String _previous_agent_name = "";

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
	private ComboBox _comboBoxes[][] = new ComboBox[][] {
		{ null, null},
		{ null},
		{ null},
		{ null, null},
	};

	/**
	 * 
	 */
	private JLabel[] _labels = new JLabel[] {
		null, null, null
	};

	/**
	 * 
	 */
	private JLabel[] _dummies = new JLabel[] {
		null, null, null, null
	};

	/**
	 * @param title
	 * @param kind
	 * @param color
	 * @param role
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public AttachDetachCommandPropertyPage(String title, String kind,
		Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, color, role, index, owner, parent);
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

		setup_which_comboBox( north_panel);

		insert_vertical_strut( north_panel);

		ButtonGroup buttonGroup1 = new ButtonGroup();

		setup_agent_selector( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_spot_checkBox_and_spot_selector( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_spot_variable_checkBox_and_spot_variable_comboBox( north_panel);

		insert_vertical_strut( north_panel);

		setup_all_agents( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_first_agent( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_last_agent( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_anyt_agent( buttonGroup1, north_panel);

		basic_panel.add( north_panel, "North");


		add( basic_panel);


		setup_apply_button();


		adjust();


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_which_comboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_which_label = create_label( ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.label.which"), true);
		panel.add( _which_label);

		_which_comboBox = create_comboBox(
			new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.combo.box.which.attach"),
				ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.combo.box.which.detach")},
			true);
		_which_comboBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if ( ( ( String)_which_comboBox.getSelectedItem()).equals(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.combo.box.which.attach"))) {
					_agent_selector.update( false);
					if ( !_previous_agent_name.equals( ""))
						_agent_selector.set( _previous_agent_name);
				} else {
					String previous_agent_name = _agent_selector.get();
					if ( !previous_agent_name.equals( ""))
						_previous_agent_name = previous_agent_name;
					_agent_selector.cleanup();
				}
				_agent_selector.setEnabled( ( ( String)_which_comboBox.getSelectedItem()).equals(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.combo.box.which.attach"))
					&& ( 0 == SwingTool.get_enabled_radioButton( _radioButtons1)));
			}
		});
		panel.add( _which_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_agent_selector(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.radio.button.agent"),
			buttonGroup1, true, false);
		_radioButtons1[ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_labels[ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_agent_selector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_spot_checkBox.setEnabled(
					ItemEvent.SELECTED != arg0.getStateChange());
				_spot_selector.setEnabled(
					ItemEvent.SELECTED != arg0.getStateChange());
				_spot_variable_checkBox.setEnabled(
					ItemEvent.SELECTED != arg0.getStateChange());
				_spot_variable_comboBox.setEnabled(
					ItemEvent.SELECTED != arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 0]);

		_labels[ 0] = create_label( ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.label.agent"), true);
		panel.add( _labels[ 0]);

		_agent_selector = create_agent_selector( false, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_spot_checkBox_and_spot_selector(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies[ 0] = new JLabel();
		panel.add( _dummies[ 0]);

		_spot_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.spot.check.box"),
			true, true);
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
	private void setup_spot_variable_checkBox_and_spot_variable_comboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies[ 1] = new JLabel();
		panel.add( _dummies[ 1]);

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
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_all_agents(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.radio.button.all.agents"),
			buttonGroup1, true, false);
		_radioButtons1[ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_radioButtons2[ 0][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_comboBoxes[ 0][ 0].setEnabled(
					_radioButtons2[ 0][ 0].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_comboBoxes[ 0][ 1].setEnabled(
					_radioButtons2[ 0][ 1].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_spot_checkBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_spot_selector.setEnabled(
					_spot_checkBox.isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_spot_variable_checkBox.setEnabled(
					0 < _spot_variable_comboBox.getItemCount() && ItemEvent.SELECTED == arg0.getStateChange());
				_spot_variable_comboBox.setEnabled(
					_spot_variable_checkBox.isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 1]);

		ButtonGroup buttonGroup2 = new ButtonGroup();

		_radioButtons2[ 0][ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.radio.button.all.agents.collection"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_comboBoxes[ 0][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 0]);

		_comboBoxes[ 0][ 0] = create_comboBox( null, _standardControlWidth, false);
		panel.add( _comboBoxes[ 0][ 0]);

		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies[ 2] = new JLabel();
		panel.add( _dummies[ 2]);

		_radioButtons2[ 0][ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.radio.button.all.agents.list"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_comboBoxes[ 0][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 1]);

		_comboBoxes[ 0][ 1] = create_comboBox( null, _standardControlWidth, false);
		panel.add( _comboBoxes[ 0][ 1]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_first_agent(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.radio.button.first.agent"),
			buttonGroup1, true, false);
		_radioButtons1[ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_labels[ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_comboBoxes[ 1][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_spot_checkBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_spot_selector.setEnabled(
					_spot_checkBox.isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_spot_variable_checkBox.setEnabled(
					0 < _spot_variable_comboBox.getItemCount() && ItemEvent.SELECTED == arg0.getStateChange());
				_spot_variable_comboBox.setEnabled(
					_spot_variable_checkBox.isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 2]);

		_labels[ 1] = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.label.first.agent.list"),
			true);
		panel.add( _labels[ 1]);

		_comboBoxes[ 1][ 0] = create_comboBox( null, _standardControlWidth, false);
		panel.add( _comboBoxes[ 1][ 0]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_last_agent(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.radio.button.last.agent"),
			buttonGroup1, true, false);
		_radioButtons1[ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_labels[ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_comboBoxes[ 2][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_spot_checkBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_spot_selector.setEnabled(
					_spot_checkBox.isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_spot_variable_checkBox.setEnabled(
					0 < _spot_variable_comboBox.getItemCount() && ItemEvent.SELECTED == arg0.getStateChange());
				_spot_variable_comboBox.setEnabled(
					_spot_variable_checkBox.isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 3]);

		_labels[ 2] = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.label.last.agent.list"),
			true);
		panel.add( _labels[ 2]);

		_comboBoxes[ 2][ 0] = create_comboBox( null, _standardControlWidth, false);
		panel.add( _comboBoxes[ 2][ 0]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_anyt_agent(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 4] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.radio.button.any.agent"),
			buttonGroup1, true, false);
		_radioButtons1[ 4].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_radioButtons2[ 1][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 1][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_comboBoxes[ 3][ 0].setEnabled(
					_radioButtons2[ 1][ 0].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_comboBoxes[ 3][ 1].setEnabled(
					_radioButtons2[ 1][ 1].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_spot_checkBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_spot_selector.setEnabled(
					_spot_checkBox.isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_spot_variable_checkBox.setEnabled(
					0 < _spot_variable_comboBox.getItemCount() && ItemEvent.SELECTED == arg0.getStateChange());
				_spot_variable_comboBox.setEnabled(
					_spot_variable_checkBox.isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 4]);

		ButtonGroup buttonGroup2 = new ButtonGroup();

		_radioButtons2[ 1][ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.radio.button.any.agent.collection"),
			buttonGroup2, true, false);
		_radioButtons2[ 1][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_comboBoxes[ 3][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 1][ 0]);

		_comboBoxes[ 3][ 0] = create_comboBox( null, _standardControlWidth, false);
		panel.add( _comboBoxes[ 3][ 0]);

		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies[ 3] = new JLabel();
		panel.add( _dummies[ 3]);

		_radioButtons2[ 1][ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach.radio.button.any.agent.list"),
			buttonGroup2, true, false);
		_radioButtons2[ 1][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_comboBoxes[ 3][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 1][ 1]);

		_comboBoxes[ 3][ 1] = create_comboBox( null, _standardControlWidth, false);
		panel.add( _comboBoxes[ 3][ 1]);

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width1 = 0;
		for ( int i = 0; i < _radioButtons1.length; ++i)
			width1 = Math.max( width1, _radioButtons1[ i].getPreferredSize().width);

		Dimension dimension = new Dimension( width1,
			_radioButtons1[ 0].getPreferredSize().height);
		for ( int i = 0; i < _radioButtons1.length; ++i)
			_radioButtons1[ i].setPreferredSize( dimension);

		dimension = new Dimension( width1,
			_dummies[ 0].getPreferredSize().height);
		for ( int i = 0; i < _dummies.length; ++i)
			_dummies[ i].setPreferredSize( dimension);


		int width2 = 0;
		for ( int i = 0; i < _radioButtons2.length; ++i)
			for ( int j = 0; j < _radioButtons2[ i].length; ++j)
			width2 = Math.max( width2, _radioButtons2[ i][ j].getPreferredSize().width);

		for ( int i = 0; i < _labels.length; ++i)
			width2 = Math.max( width2, _labels[ i].getPreferredSize().width);

		width2 = Math.max( width2, _spot_checkBox.getPreferredSize().width);

		width2 = Math.max( width2, _spot_variable_checkBox.getPreferredSize().width);

		dimension = new Dimension( width2,
			_radioButtons2[ 0][ 0].getPreferredSize().height);
		for ( int i = 0; i < _radioButtons2.length; ++i)
			for ( int j = 0; j < _radioButtons2[ i].length; ++j)
				_radioButtons2[ i][ j].setPreferredSize( dimension);

		dimension = new Dimension( width2,
			_labels[ 0].getPreferredSize().height);
		for ( int i = 0; i < _labels.length; ++i)
			_labels[ i].setPreferredSize( dimension);

		_spot_checkBox.setPreferredSize( new Dimension( width2, _spot_checkBox.getPreferredSize().height));

		_spot_variable_checkBox.setPreferredSize( new Dimension( width2, _spot_variable_checkBox.getPreferredSize().height));

		_which_label.setPreferredSize( new Dimension( width1 + width2 + 5, _which_label.getPreferredSize().height));
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

		CommonTool.update( _comboBoxes[ 0][ 0], ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));
		CommonTool.update( _comboBoxes[ 0][ 1], ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));
		CommonTool.update( _comboBoxes[ 1][ 0], ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));
		CommonTool.update( _comboBoxes[ 2][ 0], ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));
		CommonTool.update( _comboBoxes[ 3][ 0], ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));
		CommonTool.update( _comboBoxes[ 3][ 1], ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _comboBoxes[ 0][ 0], get_spot_collection_names( false));
		CommonTool.update( _comboBoxes[ 0][ 1], get_spot_list_names( false));
		CommonTool.update( _comboBoxes[ 1][ 0], get_spot_list_names( false));
		CommonTool.update( _comboBoxes[ 2][ 0], get_spot_list_names( false));
		CommonTool.update( _comboBoxes[ 3][ 0], get_spot_collection_names( false));
		CommonTool.update( _comboBoxes[ 3][ 1], get_spot_list_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(spotObject, number, objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _comboBoxes[ 0][ 0], !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));
		CommonTool.update( _comboBoxes[ 0][ 1], !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
		CommonTool.update( _comboBoxes[ 1][ 0], !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
		CommonTool.update( _comboBoxes[ 2][ 0], !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
		CommonTool.update( _comboBoxes[ 3][ 0], !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));
		CommonTool.update( _comboBoxes[ 3][ 1], !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
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
		_which_comboBox.setSelectedIndex( 0);

		_spot_checkBox.setEnabled( false);
		_spot_selector.setEnabled( false);

		_radioButtons1[ 0].setSelected( true);
		update_components( new boolean[] {
			true, false, false, false, false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_labels[ 0].setEnabled( enables[ 0]);
		_agent_selector.setEnabled( enables[ 0]);

		_radioButtons2[ 0][ 0].setSelected( true);
		_radioButtons2[ 0][ 0].setEnabled( enables[ 1]);
		_radioButtons2[ 0][ 1].setEnabled( enables[ 1]);
		_comboBoxes[ 0][ 0].setEnabled( enables[ 1]);
		_comboBoxes[ 0][ 1].setEnabled( enables[ 1]);

		_labels[ 1].setEnabled( enables[ 2]);
		_comboBoxes[ 1][ 0].setEnabled( enables[ 2]);

		_labels[ 2].setEnabled( enables[ 3]);
		_comboBoxes[ 2][ 0].setEnabled( enables[ 3]);

		_radioButtons2[ 1][ 0].setSelected( true);
		_radioButtons2[ 1][ 0].setEnabled( enables[ 4]);
		_radioButtons2[ 1][ 1].setEnabled( enables[ 4]);
		_comboBoxes[ 3][ 0].setEnabled( enables[ 4]);
		_comboBoxes[ 3][ 1].setEnabled( enables[ 4]);
	}

	/**
	 * 
	 */
	private void set_handler() {
		_spot_selector.set_handler( this);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#on_setup_completed()
	 */
	public void on_setup_completed() {
		_agent_selector.selectFirstItem();
		reset( _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
		super.on_setup_completed();
	}

	/* (non-Javadoc)
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

		if ( null == rule || rule._value.equals( "")) {
			set_handler();
			return false;
		}

		int which;
		if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.command.attach")))
			which = 0;
		else if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.command.detach")))
			which = 1;
		else {
			set_handler();
			return false;
		}


		if ( rule._value.equals( AttachAndDetachCommand._reserved_words[ 0][ 0].trim())
			|| rule._value.equals( AttachAndDetachCommand._reserved_words[ 0][ 1].trim())) {
			_which_comboBox.setSelectedIndex( which);
			_radioButtons1[ 0].setSelected( true);
			set_handler();
			return true;
		}


		int kind = AttachAndDetachCommand.get_kind( rule._value);
		if ( 0 > kind) {
			set_handler();
			return false;
		}

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements) {
			set_handler();
			return false;
		}

		_radioButtons1[ 0].setSelected( true);

		if ( 0 == kind) {
			_radioButtons1[ 0].setSelected( true);
			_agent_selector.set( elements[ 0]);
		} else {
			String[] spots = CommonRuleManipulator.get_spot( rule._value);
			if ( null == spots) {
				set_handler();
				return false;
			}

			if ( !set( spots[ 0], spots[ 1], _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox)) {
				set_handler();
				return false;
			}

			switch ( kind) {
				case 1:
					_radioButtons1[ 1].setSelected( true);
					_radioButtons2[ 0][ 0].setSelected( true);
					_comboBoxes[ 0][ 0].setSelectedItem( elements[ 0]);
					break;
				case 2:
					_radioButtons1[ 1].setSelected( true);
					_radioButtons2[ 0][ 1].setSelected( true);
					_comboBoxes[ 0][ 1].setSelectedItem( elements[ 0]);
					break;
				case 3:
					_radioButtons1[ 2].setSelected( true);
					_comboBoxes[ 1][ 0].setSelectedItem( elements[ 0]);
					break;
				case 4:
					_radioButtons1[ 3].setSelected( true);
					_comboBoxes[ 2][ 0].setSelectedItem( elements[ 0]);
					break;
				case 5:
					_radioButtons1[ 4].setSelected( true);
					_radioButtons2[ 1][ 0].setSelected( true);
					_comboBoxes[ 3][ 0].setSelectedItem( elements[ 0]);
					break;
				case 6:
					_radioButtons1[ 4].setSelected( true);
					_radioButtons2[ 1][ 1].setSelected( true);
					_comboBoxes[ 3][ 1].setSelectedItem( elements[ 0]);
					break;
				default:
					set_handler();
					return false;
			}
		}

		_which_comboBox.setSelectedIndex( which);

		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get()
	 */
	public Rule get() {
		int which = _which_comboBox.getSelectedIndex();
		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);

		if ( 0 == kind)
			kind = 0;
		else if ( 1 == kind)
			kind = ( 1 + SwingTool.get_enabled_radioButton( _radioButtons2[ 0]));
		else if ( 2 == kind)
			kind = 3;
		else if ( 3 == kind)
			kind = 4;
		else if ( 4 == kind)
			kind = ( 5 + SwingTool.get_enabled_radioButton( _radioButtons2[ 1]));
		else
			return null;

		String value = null;
		String spot = "";

		if ( 0 == kind) {
			value = "";
			if ( 0 == which) {
				value = _agent_selector.get();
				if ( ( null == value) || value.equals( ""))
					return null;
			}
		} else {
			switch ( kind) {
				case 1:
					value = get1( _comboBoxes[ 0][ 0]);
					break;
				case 2:
					value = get1( _comboBoxes[ 0][ 1]);
					break;
				case 3:
					value = get1( _comboBoxes[ 1][ 0]);
					break;
				case 4:
					value = get1( _comboBoxes[ 2][ 0]);
					break;
				case 5:
					value = get1( _comboBoxes[ 3][ 0]);
					break;
				case 6:
					value = get1( _comboBoxes[ 3][ 1]);
					break;
				default:
					return null;
			}

			spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
		}

		if ( null == value)
			return null;

		return Rule.create(
			_kind,
			( 0 == which)
				? ResourceManager.get_instance().get( "rule.type.command.attach")
				: ResourceManager.get_instance().get( "rule.type.command.detach"),
			( spot
				+ ( value.equals( "") ? AttachAndDetachCommand._reserved_words[ kind][ which].trim()
					: ( AttachAndDetachCommand._reserved_words[ kind][ which] + value))));
	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	public boolean get(Rule rule) {
////		if ( !super.get(rule))
////			return false;
//
//		int which = _which_comboBox.getSelectedIndex();
//		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);
//
//		if ( 0 == kind)
//			kind = 0;
//		else if ( 1 == kind)
//			kind = ( 1 + SwingTool.get_enabled_radioButton( _radioButtons2[ 0]));
//		else if ( 2 == kind)
//			kind = 3;
//		else if ( 3 == kind)
//			kind = 4;
//		else if ( 4 == kind)
//			kind = ( 5 + SwingTool.get_enabled_radioButton( _radioButtons2[ 1]));
//		else
//			return false;
//
//		String value = null;
//		String spot = "";
//
//		if ( 0 == kind) {
//			value = "";
//			if ( 0 == which) {
//				value = _agent_selector.get();
//				if ( ( null == value) || value.equals( ""))
//					return false;
//			}
//		} else {
//			switch ( kind) {
//				case 1:
//					value = get1( _comboBoxes[ 0][ 0]);
//					break;
//				case 2:
//					value = get1( _comboBoxes[ 0][ 1]);
//					break;
//				case 3:
//					value = get1( _comboBoxes[ 1][ 0]);
//					break;
//				case 4:
//					value = get1( _comboBoxes[ 2][ 0]);
//					break;
//				case 5:
//					value = get1( _comboBoxes[ 3][ 0]);
//					break;
//				case 6:
//					value = get1( _comboBoxes[ 3][ 1]);
//					break;
//				default:
//					return false;
//			}
//
//			spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
//		}
//
//		if ( null == value)
//			return false;
//
//		rule._type = ( ( 0 == which)
//			? ResourceManager.get_instance().get( "rule.type.command.attach")
//			: ResourceManager.get_instance().get( "rule.type.command.detach"));
//		rule._value = ( spot
//			+ ( value.equals( "") ? AttachDetachCommandRuleManipulator._reserved_words[ kind][ which].trim()
//				: ( AttachDetachCommandRuleManipulator._reserved_words[ kind][ which] + value)));
//
//		return true;
//	}
}
