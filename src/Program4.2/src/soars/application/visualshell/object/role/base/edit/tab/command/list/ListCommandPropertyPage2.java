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
public class ListCommandPropertyPage2 extends CollectionAndListCommandPropertyPageBase {

	/**
	 * 
	 */
	private RadioButton[][] _radioButtons2 = new RadioButton[][] {
		{ null, null, null, null}
	};

	/**
	 * 
	 */
	private ObjectSelector _add_last_agent_agent_selector = null;

	/**
	 * 
	 */
	private ObjectSelector _add_last_spot_spot_selector = null;

	/**
	 * 
	 */
	private ComboBox _add_last_equip_probability_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _add_last_equip_keyword_comboBox = null;

	/**
	 * 
	 */
	private ComboBox _add_last_equip_number_object_comboBox = null;

	/**
	 * 
	 */
	private TextField _add_last_equip_object_textField = null;

	/**
	 * 
	 */
	private TextField _add_last_string_textField = null;

	/**
	 * 
	 */
	private JLabel[][] _dummy3 = new JLabel[][] {
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
	public ListCommandPropertyPage2(String title, String kind,
		String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);

		_radioButtons1 = new RadioButton[] {
			null, null, null, null
		};

		_label = new JLabel[] {
			null, null, null, null
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

		setup_add_last_agent( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_add_last_spot( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_add_last_equip( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_add_last_string( buttonGroup1, north_panel);

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
	private void setup_add_last_agent(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.last.agent"),
			buttonGroup1, true, false);
		_radioButtons1[ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_add_last_agent_agent_selector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 0]);

		_label[ 1] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.label.add.last.agent"),
			true);
		panel.add( _label[ 1]);

		_add_last_agent_agent_selector = create_agent_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_add_last_spot(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.last.spot"),
			buttonGroup1, true, false);
		_radioButtons1[ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_add_last_spot_spot_selector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 1]);

		_label[ 2] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.label.add.last.spot"),
			true);
		panel.add( _label[ 2]);

		_add_last_spot_spot_selector = create_spot_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_add_last_equip(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.last.equip"),
			buttonGroup1, true, false);
		_radioButtons1[ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_radioButtons2[ 0][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_add_last_equip_probability_comboBox.setEnabled(
					_radioButtons2[ 0][ 0].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_add_last_equip_keyword_comboBox.setEnabled(
					_radioButtons2[ 0][ 1].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_add_last_equip_number_object_comboBox.setEnabled(
					_radioButtons2[ 0][ 2].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 3].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_add_last_equip_object_textField.setEnabled(
					_radioButtons2[ 0][ 3].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 2]);



		ButtonGroup buttonGroup2 = new ButtonGroup();



		_radioButtons2[ 0][ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.last.equip.probability"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_add_last_equip_probability_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 0]);

		_add_last_equip_probability_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _add_last_equip_probability_comboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 0] = new JLabel();
		panel.add( _dummy3[ 0][ 0]);

		_radioButtons2[ 0][ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.last.equip.keyword"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_add_last_equip_keyword_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 1]);

		_add_last_equip_keyword_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _add_last_equip_keyword_comboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 1] = new JLabel();
		panel.add( _dummy3[ 0][ 1]);

		_radioButtons2[ 0][ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.last.equip.number.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_add_last_equip_number_object_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 2]);

		_add_last_equip_number_object_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _add_last_equip_number_object_comboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 2] = new JLabel();
		panel.add( _dummy3[ 0][ 2]);

		_radioButtons2[ 0][ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.last.equip.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_add_last_equip_object_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 3]);

		_add_last_equip_object_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters1), _standardControlWidth, false);
		panel.add( _add_last_equip_object_textField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_add_last_string(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.last.string"),
			buttonGroup1, true, false);
		_radioButtons1[ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 3].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_add_last_string_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 3]);

		_label[ 3] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.label.add.last.string"),
			true);
		panel.add( _label[ 3]);

		_add_last_string_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
		panel.add( _add_last_string_textField);

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

		CommonTool.update( _add_last_equip_probability_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_probability_names( false) : get_spot_probability_names( false));
		CommonTool.update( _add_last_equip_keyword_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_keyword_names( false) : get_spot_keyword_names( false));
		CommonTool.update( _add_last_equip_number_object_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _comboBox, get_spot_list_names( false));

		CommonTool.update( _add_last_equip_probability_comboBox, get_spot_probability_names( false));
		CommonTool.update( _add_last_equip_keyword_comboBox, get_spot_keyword_names( false));
		CommonTool.update( _add_last_equip_number_object_comboBox, get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(spotObject, number, objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));

		CommonTool.update( _add_last_equip_probability_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
		CommonTool.update( _add_last_equip_keyword_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
		CommonTool.update( _add_last_equip_number_object_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
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
		update_components( new boolean[] {
			true, false, false, false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_label[ 1].setEnabled( enables[ 0]);
		_add_last_agent_agent_selector.setEnabled( enables[ 0]);

		_label[ 2].setEnabled( enables[ 1]);
		_add_last_spot_spot_selector.setEnabled( enables[ 1]);

		for ( int i = 0; i < _radioButtons2[ 0].length; ++i)
			_radioButtons2[ 0][ i].setEnabled( enables[ 2]);

		_add_last_equip_probability_comboBox.setEnabled( enables[ 2]);
		_add_last_equip_keyword_comboBox.setEnabled( enables[ 2]);
		_add_last_equip_number_object_comboBox.setEnabled( enables[ 2]);
		_add_last_equip_object_textField.setEnabled( enables[ 2]);

		_label[ 3].setEnabled( enables[ 3]);
		_add_last_string_textField.setEnabled( enables[ 3]);
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
		if ( 7 > kind || 13 < kind) {
			set_handler();
			return false;
		} else if ( 8 >= kind)
			_radioButtons1[ kind - 7].setSelected( true);
		else if ( 9 <= kind && 12 >= kind) {
			_radioButtons1[ 2].setSelected( true);
			_radioButtons2[ 0][ kind - 9].setSelected( true);
		} else if ( 13 == kind)
			_radioButtons1[ 3].setSelected( true);
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
			case 7:
				set2( _comboBox, _add_last_agent_agent_selector, values[ 2], ( 2 > elements.length) ? "" : elements[ 1]);
				break;
			case 8:
				set2( _comboBox, _add_last_spot_spot_selector, values[ 2], ( 2 > elements.length) ? "" : elements[ 1]);
				break;
			case 9:
				set1( _comboBox, _add_last_equip_probability_comboBox, values[ 2], elements[ 1]);
				break;
			case 10:
				set1( _comboBox, _add_last_equip_keyword_comboBox, values[ 2], elements[ 1]);
				break;
			case 11:
				set1( _comboBox, _add_last_equip_number_object_comboBox, values[ 2], elements[ 1]);
				break;
			case 12:
				set3( _comboBox, _add_last_equip_object_textField, values[ 2], elements[ 1]);
				break;
			case 13:
				set3( _comboBox, _add_last_string_textField, values[ 2], elements[ 1]);
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
		if ( 2 > kind)
			kind += 7;
		else if ( 2 == kind)
			kind = ( 9 + SwingTool.get_enabled_radioButton( _radioButtons2[ 0]));
		else if ( 3 == kind)
			kind = 13;
		else
			return null;

		String spot = get( _spot_checkBox, _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);

		String value = null;
		switch ( kind) {
			case 7:
				value = get3( _comboBox, _add_last_agent_agent_selector);
				break;
			case 8:
				value = get3( _comboBox, _add_last_spot_spot_selector);
				break;
			case 9:
				value = get2( _comboBox, _add_last_equip_probability_comboBox, false);
				break;
			case 10:
				value = get2( _comboBox, _add_last_equip_keyword_comboBox, false);
				break;
			case 11:
				value = get2( _comboBox, _add_last_equip_number_object_comboBox, false);
				break;
			case 12:
				if ( null == _add_last_equip_object_textField.getText()
					|| _add_last_equip_object_textField.getText().equals( "")
					|| _add_last_equip_object_textField.getText().equals( "$")
					|| 0 < _add_last_equip_object_textField.getText().indexOf( '$')
					|| _add_last_equip_object_textField.getText().equals( "$Name")
					|| _add_last_equip_object_textField.getText().equals( "$Role")
					|| _add_last_equip_object_textField.getText().equals( "$Spot")
					|| 0 <= _add_last_equip_object_textField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _add_last_equip_object_textField.getText().startsWith( "$")
					&& ( 0 < _add_last_equip_object_textField.getText().indexOf( "$", 1)
					|| 0 < _add_last_equip_object_textField.getText().indexOf( ")", 1)))
					return null;

				// TODO 要動作確認！
				if ( !is_object( spot, _add_last_equip_object_textField.getText(),
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

				value = get4( _comboBox, _add_last_equip_object_textField, false);
				break;
			case 13:
				if ( null == _add_last_string_textField.getText()
					|| _add_last_string_textField.getText().equals( "")
					|| _add_last_string_textField.getText().equals( "$")
					|| 0 < _add_last_string_textField.getText().indexOf( '$')
					|| _add_last_string_textField.getText().startsWith( " ")
					|| _add_last_string_textField.getText().endsWith( " ")
					|| _add_last_string_textField.getText().equals( "$Name")
					|| _add_last_string_textField.getText().equals( "$Role")
					|| _add_last_string_textField.getText().equals( "$Spot")
					|| 0 <= _add_last_string_textField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _add_last_string_textField.getText().startsWith( "$")
					&& ( 0 <= _add_last_string_textField.getText().indexOf( " ")
					|| 0 < _add_last_string_textField.getText().indexOf( "$", 1)
					|| 0 < _add_last_string_textField.getText().indexOf( ")", 1)))
					return null;

				value = get4( _comboBox, _add_last_string_textField, false);
				break;
			default:
				return null;
		}

		if ( null == value)
			return null;

		return Rule.create( _kind, _type, ListCommand._reservedWords[ kind] + spot + value);
//		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);
//		if ( 2 > kind)
//			kind += 7;
//		else if ( 2 == kind)
//			kind = ( 9 + SwingTool.get_enabled_radioButton( _radioButtons2[ 0]));
//		else if ( 3 == kind)
//			kind = 13;
//		else
//			return null;
//
//		String value = null;
//		switch ( kind) {
//			case 7:
//				value = get3( _comboBox, _add_last_agent_agent_selector);
//				break;
//			case 8:
//				value = get3( _comboBox, _add_last_spot_spot_selector);
//				break;
//			case 9:
//				value = get2( _comboBox, _add_last_equip_probability_comboBox, false);
//				break;
//			case 10:
//				value = get2( _comboBox, _add_last_equip_keyword_comboBox, false);
//				break;
//			case 11:
//				value = get2( _comboBox, _add_last_equip_number_object_comboBox, false);
//				break;
//			case 12:
//				if ( null == _add_last_equip_object_textField.getText()
//					|| _add_last_equip_object_textField.getText().equals( "")
//					|| _add_last_equip_object_textField.getText().equals( "$")
//					|| 0 < _add_last_equip_object_textField.getText().indexOf( '$')
//					|| _add_last_equip_object_textField.getText().equals( "$Name")
//					|| _add_last_equip_object_textField.getText().equals( "$Role")
//					|| _add_last_equip_object_textField.getText().equals( "$Spot")
//					|| 0 <= _add_last_equip_object_textField.getText().indexOf( Constant._experimentName))
//					return null;
//
//				if ( _add_last_equip_object_textField.getText().startsWith( "$")
//					&& ( 0 < _add_last_equip_object_textField.getText().indexOf( "$", 1)
//					|| 0 < _add_last_equip_object_textField.getText().indexOf( ")", 1)))
//					return null;
//
//				value = get4( _comboBox, _add_last_equip_object_textField, false);
//				break;
//			case 13:
//				if ( null == _add_last_string_textField.getText()
//					|| _add_last_string_textField.getText().equals( "")
//					|| _add_last_string_textField.getText().equals( "$")
//					|| 0 < _add_last_string_textField.getText().indexOf( '$')
//					|| _add_last_string_textField.getText().startsWith( " ")
//					|| _add_last_string_textField.getText().endsWith( " ")
//					|| _add_last_string_textField.getText().equals( "$Name")
//					|| _add_last_string_textField.getText().equals( "$Role")
//					|| _add_last_string_textField.getText().equals( "$Spot")
//					|| 0 <= _add_last_string_textField.getText().indexOf( Constant._experimentName))
//					return null;
//
//				if ( _add_last_string_textField.getText().startsWith( "$")
//					&& ( 0 <= _add_last_string_textField.getText().indexOf( " ")
//					|| 0 < _add_last_string_textField.getText().indexOf( "$", 1)
//					|| 0 < _add_last_string_textField.getText().indexOf( ")", 1)))
//					return null;
//
//				value = get4( _comboBox, _add_last_string_textField, false);
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
