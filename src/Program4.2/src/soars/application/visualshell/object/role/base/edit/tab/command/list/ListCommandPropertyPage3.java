/**
 * 
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
 *
 */
public class ListCommandPropertyPage3 extends CollectionAndListCommandPropertyPageBase {

	/**
	 * 
	 */
	private RadioButton[][] _radioButtons2 = new RadioButton[][] {
		{ null, null, null, null},
		{ null, null, null, null}
	};

	/**
	 * 
	 */
	private ComboBox _equip_first_probability_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _equip_first_keyword_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _equip_first_number_object_comboBox = null;

	/**
	 * 
	 */
	private TextField _equip_first_object_textField = null;

	/**
	 * 
	 */
	private ComboBox _equip_last_probability_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _equip_last_keyword_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _equip_last_number_object_comboBox = null;

	/**
	 * 
	 */
	private TextField _equip_last_object_textField = null;

	/**
	 * 
	 */
	private JLabel[][] _dummy3 = new JLabel[][] {
		{ null, null, null},
		{ null, null, null}
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
	public ListCommandPropertyPage3(String title, String kind,
		String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);

		_radioButtons1 = new RadioButton[] {
			null, null
		};

		_label = new JLabel[] {
			null
		};
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

		setup_spot_checkBox_and_spot_selector( north_panel);

		insert_vertical_strut( north_panel);

		setup_header( north_panel);

		insert_vertical_strut( north_panel);

		ButtonGroup buttonGroup1 = new ButtonGroup();

		setup_equip_first( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_equip_last( buttonGroup1, north_panel);

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
	private void setup_equip_first(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.first"),
			buttonGroup1, true, false);
		_radioButtons1[ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_radioButtons2[ 0][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equip_first_probability_comboBox.setEnabled(
					_radioButtons2[ 0][ 0].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equip_first_keyword_comboBox.setEnabled(
					_radioButtons2[ 0][ 1].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equip_first_number_object_comboBox.setEnabled(
					_radioButtons2[ 0][ 2].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 3].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equip_first_object_textField.setEnabled(
					_radioButtons2[ 0][ 3].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 0]);



		ButtonGroup buttonGroup2 = new ButtonGroup();



		_radioButtons2[ 0][ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.first.probability"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equip_first_probability_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 0]);

		_equip_first_probability_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _equip_first_probability_comboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 0] = new JLabel();
		panel.add( _dummy3[ 0][ 0]);

		_radioButtons2[ 0][ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.first.keyword"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equip_first_keyword_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 1]);

		_equip_first_keyword_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _equip_first_keyword_comboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 1] = new JLabel();
		panel.add( _dummy3[ 0][ 1]);

		_radioButtons2[ 0][ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.first.number.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equip_first_number_object_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 2]);

		_equip_first_number_object_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _equip_first_number_object_comboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 2] = new JLabel();
		panel.add( _dummy3[ 0][ 2]);

		_radioButtons2[ 0][ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.first.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equip_first_object_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 3]);

		_equip_first_object_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters1), _standardControlWidth, false);
		panel.add( _equip_first_object_textField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_equip_last(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.last"),
			buttonGroup1, true, false);
		_radioButtons1[ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_radioButtons2[ 1][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equip_last_probability_comboBox.setEnabled(
					_radioButtons2[ 1][ 0].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 1][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equip_last_keyword_comboBox.setEnabled(
					_radioButtons2[ 1][ 1].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 1][ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equip_last_number_object_comboBox.setEnabled(
					_radioButtons2[ 1][ 2].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 1][ 3].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equip_last_object_textField.setEnabled(
					_radioButtons2[ 1][ 3].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 1]);



		ButtonGroup buttonGroup2 = new ButtonGroup();



		_radioButtons2[ 1][ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.last.probability"),
			buttonGroup2, true, false);
		_radioButtons2[ 1][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equip_last_probability_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 1][ 0]);

		_equip_last_probability_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _equip_last_probability_comboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 1][ 0] = new JLabel();
		panel.add( _dummy3[ 1][ 0]);

		_radioButtons2[ 1][ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.last.keyword"),
			buttonGroup2, true, false);
		_radioButtons2[ 1][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equip_last_keyword_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 1][ 1]);

		_equip_last_keyword_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _equip_last_keyword_comboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 1][ 1] = new JLabel();
		panel.add( _dummy3[ 1][ 1]);

		_radioButtons2[ 1][ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.last.number.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 1][ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equip_last_number_object_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 1][ 2]);

		_equip_last_number_object_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _equip_last_number_object_comboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 1][ 2] = new JLabel();
		panel.add( _dummy3[ 1][ 2]);

		_radioButtons2[ 1][ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.last.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 1][ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equip_last_object_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 1][ 3]);

		_equip_last_object_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters1), _standardControlWidth, false);
		panel.add( _equip_last_object_textField);

		parent.add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.command.base.CollectionAndListCommandPropertyPageBase#adjust()
	 */
	protected void adjust() {
		super.adjust();

		for ( int i = 0; i < _dummy3.length; ++i) {
			for ( int j = 0; j < _dummy3[ i].length; ++j) {
				_dummy3[ i][ j].setPreferredSize( new Dimension(
					_dummy1.getPreferredSize().width,
					_dummy3[ i][ j].getPreferredSize().height));
			}
		}


		int width = _spot_checkBox.getPreferredSize().width;
		for ( int i = 0; i < _radioButtons2.length; ++i) {
			for ( int j = 0; j < _radioButtons2[ i].length; ++j) {
				width = Math.max( width, _radioButtons2[ i][ j].getPreferredSize().width);
			}
		}

		_spot_checkBox.setPreferredSize( new Dimension( width,
			_spot_checkBox.getPreferredSize().height));

		for ( int i = 0; i < _label.length; ++i)
			_label[ i].setPreferredSize( new Dimension( width,
				_label[ i].getPreferredSize().height));

		for ( int i = 0; i < _radioButtons2.length; ++i) {
			for ( int j = 0; j < _radioButtons2[ i].length; ++j) {
				_radioButtons2[ i][ j].setPreferredSize( new Dimension( width,
					_radioButtons2[ i][ j].getPreferredSize().height));
			}
		}
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

		CommonTool.update( _equip_first_probability_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_probability_names( false) : get_spot_probability_names( false));
		CommonTool.update( _equip_first_keyword_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_keyword_names( false) : get_spot_keyword_names( false));
		CommonTool.update( _equip_first_number_object_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));

		CommonTool.update( _equip_last_probability_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_probability_names( false) : get_spot_probability_names( false));
		CommonTool.update( _equip_last_keyword_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_keyword_names( false) : get_spot_keyword_names( false));
		CommonTool.update( _equip_last_number_object_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _comboBox, get_spot_list_names( false));

		CommonTool.update( _equip_first_probability_comboBox, get_spot_probability_names( false));
		CommonTool.update( _equip_first_keyword_comboBox, get_spot_keyword_names( false));
		CommonTool.update( _equip_first_number_object_comboBox, get_spot_number_object_names( false));

		CommonTool.update( _equip_last_probability_comboBox, get_spot_probability_names( false));
		CommonTool.update( _equip_last_keyword_comboBox, get_spot_keyword_names( false));
		CommonTool.update( _equip_last_number_object_comboBox, get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(spotObject, number, objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));

		CommonTool.update( _equip_first_probability_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
		CommonTool.update( _equip_first_keyword_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
		CommonTool.update( _equip_first_number_object_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));

		CommonTool.update( _equip_last_probability_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
		CommonTool.update( _equip_last_keyword_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
		CommonTool.update( _equip_last_number_object_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
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
		_radioButtons2[ 0][ 0].setSelected( true);
		_radioButtons2[ 1][ 0].setSelected( true);
		update_components( new boolean[] {
			true, false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		for ( int i = 0; i < _radioButtons2[ 0].length; ++i)
			_radioButtons2[ 0][ i].setEnabled( enables[ 0]);

		_equip_first_probability_comboBox.setEnabled( enables[ 0]);
		_equip_first_keyword_comboBox.setEnabled( false);
		_equip_first_number_object_comboBox.setEnabled( false);
		_equip_first_object_textField.setEnabled( false);

		for ( int i = 0; i < _radioButtons2[ 1].length; ++i)
			_radioButtons2[ 1][ i].setEnabled( enables[ 1]);

		_equip_last_probability_comboBox.setEnabled( enables[ 1]);
		_equip_last_keyword_comboBox.setEnabled( enables[ 1]);
		_equip_last_number_object_comboBox.setEnabled( enables[ 1]);
		_equip_last_object_textField.setEnabled( enables[ 1]);
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
		if ( 14 > kind || 21 < kind) {
			set_handler();
			return false;
		} else if ( 14 <= kind && 17 >= kind) {
			_radioButtons1[ 0].setSelected( true);
			_radioButtons2[ 0][ kind - 14].setSelected( true);
		} else {
			_radioButtons1[ 1].setSelected( true);
			_radioButtons2[ 1][ kind - 18].setSelected( true);
		}

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
			case 14:
				set1( _equip_first_probability_comboBox, _comboBox, values[ 2], elements[ 1]);
				break;
			case 15:
				set1( _equip_first_keyword_comboBox, _comboBox, values[ 2], elements[ 1]);
				break;
			case 16:
				set1( _equip_first_number_object_comboBox, _comboBox, values[ 2], elements[ 1]);
				break;
			case 17:
				set3( _comboBox, _equip_first_object_textField, elements[ 1], values[ 2]);
				break;
			case 18:
				set1( _equip_last_probability_comboBox, _comboBox, values[ 2], elements[ 1]);
				break;
			case 19:
				set1( _equip_last_keyword_comboBox, _comboBox, values[ 2], elements[ 1]);
				break;
			case 20:
				set1( _equip_last_number_object_comboBox, _comboBox, values[ 2], elements[ 1]);
				break;
			case 21:
				set3( _comboBox, _equip_last_object_textField, elements[ 1], values[ 2]);
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
		if ( 0 == kind)
			kind = ( 14 + SwingTool.get_enabled_radioButton( _radioButtons2[ 0]));
		else if ( 1 == kind)
			kind = ( 18 + SwingTool.get_enabled_radioButton( _radioButtons2[ 1]));
		else
			return null;

		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);

		String value = null;
		switch ( kind) {
			case 14:
				value = get2( _equip_first_probability_comboBox, _comboBox);
				break;
			case 15:
				value = get2( _equip_first_keyword_comboBox, _comboBox);
				break;
			case 16:
				value = get2( _equip_first_number_object_comboBox, _comboBox);
				break;
			case 17:
				if ( null == _equip_first_object_textField.getText()
					|| _equip_first_object_textField.getText().equals( "")
					|| _equip_first_object_textField.getText().equals( "$")
					|| 0 < _equip_first_object_textField.getText().indexOf( '$')
					|| _equip_first_object_textField.getText().equals( "$Name")
					|| _equip_first_object_textField.getText().equals( "$Role")
					|| _equip_first_object_textField.getText().equals( "$Spot")
					|| 0 <= _equip_first_object_textField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _equip_first_object_textField.getText().startsWith( "$")
					&& ( 0 < _equip_first_object_textField.getText().indexOf( "$", 1)
					|| 0 < _equip_first_object_textField.getText().indexOf( ")", 1)))
					return null;

				// TODO 要動作確認！
				if ( !is_object( spot, _equip_first_object_textField.getText(),
					new String[] {
						"collection",
						"list",
						"spot variable",
						"role variable",
						"time variable",
						"map",
						"exchange algebra",
						"file",
						"class variable"}))
					return null;

				value = get4( _equip_first_object_textField, _comboBox, false);
				break;
			case 18:
				value = get2( _equip_last_probability_comboBox, _comboBox);
				break;
			case 19:
				value = get2( _equip_last_keyword_comboBox, _comboBox);
				break;
			case 20:
				value = get2( _equip_last_number_object_comboBox, _comboBox);
				break;
			case 21:
				if ( null == _equip_last_object_textField.getText()
					|| _equip_last_object_textField.getText().equals( "")
					|| _equip_last_object_textField.getText().equals( "$")
					|| 0 < _equip_last_object_textField.getText().indexOf( '$')
					|| _equip_last_object_textField.getText().equals( "$Name")
					|| _equip_last_object_textField.getText().equals( "$Role")
					|| _equip_last_object_textField.getText().equals( "$Spot")
					|| 0 <= _equip_last_object_textField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _equip_last_object_textField.getText().startsWith( "$")
					&& ( 0 < _equip_last_object_textField.getText().indexOf( "$", 1)
					|| 0 < _equip_last_object_textField.getText().indexOf( ")", 1)))
					return null;

				// TODO 要動作確認！
				if ( !is_object( spot, _equip_last_object_textField.getText(),
					new String[] {
						"collection",
						"list",
						"spot variable",
						"role variable",
						"time variable",
						"map",
						"exchange algebra",
						"file",
						"class variable"}))
					return null;

				value = get4( _equip_last_object_textField, _comboBox, false);
				break;
			default:
				return null;
		}

		if ( null == value)
			return null;

		return Rule.create( _kind, _type, ListCommand._reservedWords[ kind] + spot + value);
//		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);
//		if ( 0 == kind)
//			kind = ( 14 + SwingTool.get_enabled_radioButton( _radioButtons2[ 0]));
//		else if ( 1 == kind)
//			kind = ( 18 + SwingTool.get_enabled_radioButton( _radioButtons2[ 1]));
//		else
//			return null;
//
//		String value = null;
//		switch ( kind) {
//			case 14:
//				value = get2( _equip_first_probability_comboBox, _comboBox);
//				break;
//			case 15:
//				value = get2( _equip_first_keyword_comboBox, _comboBox);
//				break;
//			case 16:
//				value = get2( _equip_first_number_object_comboBox, _comboBox);
//				break;
//			case 17:
//				if ( null == _equip_first_object_textField.getText()
//					|| _equip_first_object_textField.getText().equals( "")
//					|| _equip_first_object_textField.getText().equals( "$")
//					|| 0 < _equip_first_object_textField.getText().indexOf( '$')
//					|| _equip_first_object_textField.getText().equals( "$Name")
//					|| _equip_first_object_textField.getText().equals( "$Role")
//					|| _equip_first_object_textField.getText().equals( "$Spot")
//					|| 0 <= _equip_first_object_textField.getText().indexOf( Constant._experimentName))
//					return null;
//
//				if ( _equip_first_object_textField.getText().startsWith( "$")
//					&& ( 0 < _equip_first_object_textField.getText().indexOf( "$", 1)
//					|| 0 < _equip_first_object_textField.getText().indexOf( ")", 1)))
//					return null;
//
//				value = get4( _equip_first_object_textField, _comboBox, false);
//				break;
//			case 18:
//				value = get2( _equip_last_probability_comboBox, _comboBox);
//				break;
//			case 19:
//				value = get2( _equip_last_keyword_comboBox, _comboBox);
//				break;
//			case 20:
//				value = get2( _equip_last_number_object_comboBox, _comboBox);
//				break;
//			case 21:
//				if ( null == _equip_last_object_textField.getText()
//					|| _equip_last_object_textField.getText().equals( "")
//					|| _equip_last_object_textField.getText().equals( "$")
//					|| 0 < _equip_last_object_textField.getText().indexOf( '$')
//					|| _equip_last_object_textField.getText().equals( "$Name")
//					|| _equip_last_object_textField.getText().equals( "$Role")
//					|| _equip_last_object_textField.getText().equals( "$Spot")
//					|| 0 <= _equip_last_object_textField.getText().indexOf( Constant._experimentName))
//					return null;
//
//				if ( _equip_last_object_textField.getText().startsWith( "$")
//					&& ( 0 < _equip_last_object_textField.getText().indexOf( "$", 1)
//					|| 0 < _equip_last_object_textField.getText().indexOf( ")", 1)))
//					return null;
//
//				value = get4( _equip_last_object_textField, _comboBox, false);
//				break;
//			default:
//				return null;
//		}
//
//		if ( null == value)
//			return null;
//
//		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
//
//		return Rule.create( _kind, _type, ListCommand._reservedWords[ kind] + spot + value);
	}
}
