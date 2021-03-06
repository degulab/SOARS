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
import soars.common.utility.swing.text.TextLimiter;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class ListCommandPropertyPage6 extends CollectionAndListCommandPropertyPageBase {

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
	private TextField _rotate_all_forward_value_textField = null;

	/**
	 * 
	 */
	private ComboBox _rotate_all_forward_number_object_comboBox = null;

	/**
	 * 
	 */
	private TextField _rotate_all_backward_value_textField = null;

	/**
	 * 
	 */
	private ComboBox _rotate_all_backward_number_object_comboBox = null;

	/**
	 * 
	 */
	private JLabel[][] _dummy3 = new JLabel[][] {
		{ null},
		{ null}
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
	public ListCommandPropertyPage6(String title, String kind,
		String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);

		_radioButtons1 = new RadioButton[] {
			null, null, null, null,
			null
		};

		_label = new JLabel[] {
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

		setup_reverse_all( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_shuffle_all( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_sort_all( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_rotate_all_forward( buttonGroup1, north_panel);

		insert_vertical_strut( north_panel);

		setup_rotate_all_backward( buttonGroup1, north_panel);

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
	private void setup_reverse_all(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.reverse.all"),
			buttonGroup1, true, false);
		panel.add( _radioButtons1[ 0]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_shuffle_all(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.shuffle.all"),
			buttonGroup1, true, false);
		panel.add( _radioButtons1[ 1]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_sort_all(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.sort.all"),
			buttonGroup1, true, false);
		panel.add( _radioButtons1[ 2]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_rotate_all_forward(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.rotate.all.forward"),
			buttonGroup1, true, false);
		_radioButtons1[ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_radioButtons2[ 0][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_rotate_all_forward_value_textField.setEnabled(
					_radioButtons2[ 0][ 0].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_rotate_all_forward_number_object_comboBox.setEnabled(
					_radioButtons2[ 0][ 1].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 3]);



		ButtonGroup buttonGroup2 = new ButtonGroup();



		_radioButtons2[ 0][ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.rotate.all.forward.value"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_rotate_all_forward_value_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 0]);

		_rotate_all_forward_value_textField = create_textField( new TextLimiter( "0123456789"), _standardControlWidth, true);
		panel.add( _rotate_all_forward_value_textField);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 0] = new JLabel();
		panel.add( _dummy3[ 0][ 0]);

		_radioButtons2[ 0][ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.rotate.all.forward.number.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_rotate_all_forward_number_object_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 1]);

		_rotate_all_forward_number_object_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _rotate_all_forward_number_object_comboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_rotate_all_backward(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 4] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.rotate.all.backward"),
			buttonGroup1, true, false);
		_radioButtons1[ 4].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_radioButtons2[ 1][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_rotate_all_backward_value_textField.setEnabled(
					_radioButtons2[ 1][ 0].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 1][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_rotate_all_backward_number_object_comboBox.setEnabled(
					_radioButtons2[ 1][ 1].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 4]);



		ButtonGroup buttonGroup2 = new ButtonGroup();



		_radioButtons2[ 1][ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.rotate.all.backward.value"),
			buttonGroup2, true, false);
		_radioButtons2[ 1][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_rotate_all_backward_value_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 1][ 0]);

		_rotate_all_backward_value_textField = create_textField( new TextExcluder( Constant._prohibitedCharacters1), _standardControlWidth, true);
		panel.add( _rotate_all_backward_value_textField);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 1][ 0] = new JLabel();
		panel.add( _dummy3[ 1][ 0]);

		_radioButtons2[ 1][ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.rotate.all.backward.number.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 1][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_rotate_all_backward_number_object_comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 1][ 1]);

		_rotate_all_backward_number_object_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _rotate_all_backward_number_object_comboBox);

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

		CommonTool.update( _rotate_all_forward_number_object_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
		CommonTool.update( _rotate_all_backward_number_object_comboBox, ( !_spot_checkBox.isSelected() && !spot_variable_checkBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _comboBox, get_spot_list_names( false));

		CommonTool.update( _rotate_all_forward_number_object_comboBox, get_spot_number_object_names( false));
		CommonTool.update( _rotate_all_backward_number_object_comboBox, get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#update(soars.application.visualshell.object.player.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spot_variable_checkBox, ComboBox spot_variable_comboBox) {
		if ( !objectSelector.equals( _spot_selector))
			return;

		super.update(spotObject, number, objectSelector, spot_variable_checkBox, spot_variable_comboBox);

		CommonTool.update( _comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));

		CommonTool.update( _rotate_all_forward_number_object_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
		CommonTool.update( _rotate_all_backward_number_object_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
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
			true, false, false, false,
			false, false, false, false,
			false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		for ( int i = 0; i < _radioButtons2[ 0].length; ++i)
			_radioButtons2[ 0][ i].setEnabled( enables[ 7]);

		_rotate_all_forward_value_textField.setEnabled( enables[ 7]);
		_rotate_all_forward_number_object_comboBox.setEnabled( enables[ 7]);

		for ( int i = 0; i < _radioButtons2[ 1].length; ++i)
			_radioButtons2[ 1][ i].setEnabled( enables[ 8]);

		_rotate_all_backward_value_textField.setEnabled( enables[ 8]);
		_rotate_all_backward_number_object_comboBox.setEnabled( enables[ 8]);
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
		if ( 39 > kind || 45 < kind) {
			set_handler();
			return false;
		} else if ( 39 <= kind && 41 >= kind)
			_radioButtons1[ kind - 39].setSelected( true);
		else {
			if ( 42 <= kind && 43 >= kind) {
				_radioButtons1[ 3].setSelected( true);
				_radioButtons2[ 0][ kind - 42].setSelected( true);
			} else {
				_radioButtons1[ 4].setSelected( true);
				_radioButtons2[ 1][ kind - 44].setSelected( true);
			}
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
			case 39:
				_comboBox.setSelectedItem( values[ 2]);
				break;
			case 40:
				_comboBox.setSelectedItem( values[ 2]);
				break;
			case 41:
				_comboBox.setSelectedItem( values[ 2]);
				break;
			case 42:
				if ( elements[ 1].startsWith( "-"))
					elements[ 1] = elements[ 1].substring( 1);

				set3( _comboBox, _rotate_all_forward_value_textField, values[ 2], elements[ 1]);
				break;
			case 43:
				if ( elements[ 1].startsWith( "-"))
					elements[ 1] = elements[ 1].substring( 1);

				set1( _comboBox, _rotate_all_forward_number_object_comboBox, values[ 2], elements[ 1]);
				break;
			case 44:
				set3( _comboBox, _rotate_all_backward_value_textField, values[ 2], elements[ 1]);
				break;
			case 45:
				set1( _comboBox, _rotate_all_backward_number_object_comboBox, values[ 2], elements[ 1]);
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
		if ( 2 >= kind)
			kind += 39;
		else if ( 3 <= kind && 4 >= kind) {
			switch ( kind) {
				case 3:
					kind = ( 42 + SwingTool.get_enabled_radioButton( _radioButtons2[ 0]));
					break;
				case 4:
					kind = ( 44 + SwingTool.get_enabled_radioButton( _radioButtons2[ 1]));
					break;
			}
		} else
			return null;

		String value = null;
		switch ( kind) {
			case 39:
				value = get1( _comboBox);
				break;
			case 40:
				value = get1( _comboBox);
				break;
			case 41:
				value = get1( _comboBox);
				break;
			case 42:
				value = get_rotate_all_forward_value();
				break;
			case 43:
				value = get2( _comboBox, _rotate_all_forward_number_object_comboBox, false, "-");
				break;
			case 44:
				value = get_rotate_all_backward_value();
				break;
			case 45:
				value = get2( _comboBox, _rotate_all_backward_number_object_comboBox, false);
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
//		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);
//		if ( 2 >= kind)
//			kind += 32;
//		else if ( 3 <= kind && 4 >= kind) {
//			switch ( kind) {
//				case 3:
//					kind = ( 35 + SwingTool.get_enabled_radioButton( _radioButtons2[ 0]));
//					break;
//				case 4:
//					kind = ( 37 + SwingTool.get_enabled_radioButton( _radioButtons2[ 1]));
//					break;
//			}
//		} else
//			return false;
//
//		String value = null;
//		switch ( kind) {
//			case 32:
//				value = get1( _comboBox);
//				break;
//			case 33:
//				value = get1( _comboBox);
//				break;
//			case 34:
//				value = get1( _comboBox);
//				break;
//			case 35:
//				value = get_rotate_all_forward_value();
//				break;
//			case 36:
//				value = get2( _comboBox, _rotate_all_forward_number_object_comboBox, false, "-");
//				break;
//			case 37:
//				value = get_rotate_all_backward_value();
//				break;
//			case 38:
//				value = get2( _comboBox, _rotate_all_backward_number_object_comboBox, false);
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

	/**
	 * @param comboBox
	 * @param textField
	 * @return
	 */
	private String get_rotate_all_forward_value() {
		String text0 = ( String)_comboBox.getSelectedItem();
		if ( null == text0 || text0.equals( ""))
			return null;

		String text1 = _rotate_all_forward_value_textField.getText();
		if ( null == text1 || text1.equals( ""))
			return null;

		int n;
		try {
			n = Integer.parseInt( text1);
			if ( 1 > n)
				return null;

			text1 = String.valueOf( n);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return null;
		}

		return ( text0 + "=-" + text1);
	}

	/**
	 * @return
	 */
	private String get_rotate_all_backward_value() {
		String text0 = ( String)_comboBox.getSelectedItem();
		if ( null == text0 || text0.equals( ""))
			return null;

		String text1 = _rotate_all_backward_value_textField.getText();
		if ( null == text1 || text1.equals( "")
			|| text1.equals( "$") || 0 < text1.indexOf( '$')
			|| text1.equals( "$Name") || text1.equals( "$Role") || text1.equals( "$Spot")
			|| 0 <= text1.indexOf( Constant._experimentName))
			return null;

		if ( text1.startsWith( "$")
			&& ( 0 < text1.indexOf( "$", 1) || 0 < text1.indexOf( ")", 1)))
			return null;

		int n;
		try {
			n = Integer.parseInt( text1);
			if ( 1 > n)
				return null;

			text1 = String.valueOf( n);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			if ( !text1.matches( "\\$.+"))
				return null;
		}

		return ( text0 + "=" + text1);
	}
}
