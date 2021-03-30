/*
 * 2005/03/28
 */
package soars.application.animator.object.scenario.retrieve;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.player.base.ObjectBase;
import soars.application.animator.object.property.base.PropertyManager;
import soars.application.animator.object.scenario.ScenarioManager;
import soars.application.animator.object.transition.base.TransitionManager;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box to retrieve the value of the property.
 * @author kurata / SOARS project
 */
public class RetrievePropertyDlg extends Dialog {

	/**
	 * Object(AgentObject or SpotObject).
	 */
	public ObjectBase _objectBase = null;

	/**
	 * Name of the property.
	 */
	public String _property_name = "";

	/**
	 * Value of the property.
	 */
	public String _property_value = "";

	/**
	 * 
	 */
	private ObjectBase[] _visible_objects = null;

	/**
	 * 
	 */
	private PropertyManager _propertyManager = null;

	/**
	 * 
	 */
	private TransitionManager _transitionManager = null;

	/**
	 * 
	 */
	private JComboBox _object_comboBox = null;

	/**
	 * 
	 */
	private JComboBox _property_name_comboBox = null;

	/**
	 * 
	 */
	private JComboBox _retrieve_type_comboBox = null;

	/**
	 * 
	 */
	private JComboBox[] _property_value_comboBoxes = new JComboBox[ 2];

	/**
	 * 
	 */
	private JButton _backward_button = null;

	/**
	 * 
	 */
	private JButton _forward_button = null;

	/**
	 * 
	 */
	private String _current_property_name = "";

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param visible_objects the array of the visible objects
	 * @param propertyManager the Property hashtable(name(String) - value(String) - PropertyBase)
	 * @param transitionManager the scenario data manager
	 */
	public RetrievePropertyDlg(Frame arg0, String arg1, boolean arg2, ObjectBase[] visible_objects, PropertyManager propertyManager, TransitionManager transitionManager) {
		super(arg0, arg1, arg2);
		_visible_objects = visible_objects;
		_propertyManager = propertyManager;
		_transitionManager = transitionManager;
	}

	/**
	 * Sets whether or not this dialog is enabled.
	 * @param enable true if this dialog should be enabled, false otherwise
	 */
	public void enable_user_interface(boolean enable) {
		_object_comboBox.setEnabled( enable);
		_property_name_comboBox.setEnabled( enable);

		_property_value_comboBoxes[ 0].setEnabled( enable);

		if ( enable)
			enable_property_value_comboBox();

		_backward_button.setEnabled( enable);
		_forward_button.setEnabled( enable);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		setResizable( false);


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		JPanel panel = setup_object_combo_box();

		setup_property_name_combo_box( panel);

		_current_property_name = ( String)_propertyManager.get_selected_properties().get( 0);

		setup_retrieve_type_combo_box( panel);

		setup_property_value_combo_boxes( panel);


		panel = setup_backward_button();

		setup_forward_button( panel);


		adjust();


		return true;
	}

	/**
	 * @return
	 */
	private JPanel setup_object_combo_box() {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_object_comboBox = new JComboBox( _visible_objects);
		_object_comboBox.setRenderer( new ObjectNameCellRenderer());
		panel.add( _object_comboBox);
		return panel;
	}

	/**
	 * @param panel
	 */
	private void setup_property_name_combo_box(JPanel panel) {
		_property_name_comboBox = new JComboBox( _propertyManager.get_selected_properties());
		_property_name_comboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_property_name_changed( arg0);
			}
		});
		panel.add( _property_name_comboBox);
	}

	/**
	 * @param panel
	 */
	private void setup_retrieve_type_combo_box(JPanel panel) {
		_retrieve_type_comboBox = new JComboBox();
		_retrieve_type_comboBox.addItem(
			ResourceManager.get( "retrieve.property.dialog.equal"));
		_retrieve_type_comboBox.addItem(
			ResourceManager.get( "retrieve.property.dialog.more.than"));
		_retrieve_type_comboBox.addItem(
			ResourceManager.get( "retrieve.property.dialog.less.than"));
		_retrieve_type_comboBox.addItem(
			ResourceManager.get( "retrieve.property.dialog.more.than.less.than"));
		_retrieve_type_comboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_retrieve_type_changed( arg0);
			}
		});
		panel.add( _retrieve_type_comboBox);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_retrieve_type_changed(ActionEvent actionEvent) {
		enable_property_value_comboBox();
	}

	/**
	 * 
	 */
	private void enable_property_value_comboBox() {
		String retrieve_type = ( String)_retrieve_type_comboBox.getSelectedItem();
		_property_value_comboBoxes[ 1].setEnabled(
			retrieve_type.equals( ResourceManager.get(
				"retrieve.property.dialog.more.than.less.than")) ? true : false);
	}

	/**
	 * @param panel
	 */
	private void setup_property_value_combo_boxes(JPanel panel) {
		for ( int i = 0; i < _property_value_comboBoxes.length; ++i) {
			_property_value_comboBoxes[ i] = new JComboBox();
			update_property_value_comboBox( _property_value_comboBoxes[ i], _current_property_name);
			panel.add( _property_value_comboBoxes[ i]);
		}
		_property_value_comboBoxes[ 1].setEnabled( false);

		getContentPane().add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_property_name_changed(ActionEvent actionEvent) {
		String property_name = ( String)_property_name_comboBox.getSelectedItem();

		if ( property_name.equals( _current_property_name))
			return;

		for ( int i = 0; i < _property_value_comboBoxes.length; ++i)
			update_property_value_comboBox( _property_value_comboBoxes[ i], property_name);

		_current_property_name = property_name;
	}

	/**
	 * @param property_value_comboBox
	 * @param property_name
	 */
	private void update_property_value_comboBox(JComboBox property_value_comboBox, String property_name) {
		property_value_comboBox.removeAllItems();

		TreeMap property_map = ( TreeMap)_propertyManager.get( property_name);
		Iterator iterator = property_map.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String value = ( String)entry.getKey();
			property_value_comboBox.addItem( value);
		}
	}

	/**
	 * @return
	 */
	private JPanel setup_backward_button() {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 5));

		_backward_button = new JButton(
			ResourceManager.get( "retrieve.property.dialog.backward.button"));
		_backward_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_backward( arg0);
			}
		});

		panel.add( _backward_button);
		return panel;
	}

	/**
	 * @param actionEvent
	 */
	protected void on_backward(ActionEvent actionEvent) {
		String retrieve_type = ( String)_retrieve_type_comboBox.getSelectedItem();
		if ( retrieve_type.equals( ResourceManager.get( "retrieve.property.dialog.equal")))
			ScenarioManager.get_instance().retrieve_backward(
				_transitionManager,
				( ObjectBase)_object_comboBox.getSelectedItem(),
				( String)_property_name_comboBox.getSelectedItem(),
				( String)_property_value_comboBoxes[ 0].getSelectedItem());
		else {
			String text0 = ( String)_property_value_comboBoxes[ 0].getSelectedItem();
			double value0;
			try {
				value0 = Double.parseDouble( text0);
			} catch (NumberFormatException e) {
				return;
			}
			if ( retrieve_type.equals( ResourceManager.get( "retrieve.property.dialog.more.than")))
				ScenarioManager.get_instance().retrieve_backward_more_than(
					_transitionManager,
					( ObjectBase)_object_comboBox.getSelectedItem(),
					( String)_property_name_comboBox.getSelectedItem(),
					value0);
			else if ( retrieve_type.equals( ResourceManager.get( "retrieve.property.dialog.less.than")))
				ScenarioManager.get_instance().retrieve_backward_less_than(
					_transitionManager,
					( ObjectBase)_object_comboBox.getSelectedItem(),
					( String)_property_name_comboBox.getSelectedItem(),
					value0);
			else if ( retrieve_type.equals( ResourceManager.get( "retrieve.property.dialog.more.than.less.than"))) {
				String text1 = ( String)_property_value_comboBoxes[ 1].getSelectedItem();
				double value1;
				try {
					value1 = Double.parseDouble( text1);
				} catch (NumberFormatException e) {
					return;
				}
				ScenarioManager.get_instance().retrieve_backward_more_than_less_than(
					_transitionManager,
					( ObjectBase)_object_comboBox.getSelectedItem(),
					( String)_property_name_comboBox.getSelectedItem(),
					value0,
					value1);
			}
		}
	}

	/**
	 * @param panel
	 */
	private void setup_forward_button(JPanel panel) {
		_forward_button = new JButton(
			ResourceManager.get( "retrieve.property.dialog.forward.button"));
		_forward_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_forward( arg0);
			}
		});

		panel.add( _forward_button);

		getContentPane().add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_forward(ActionEvent actionEvent) {
		String retrieve_type = ( String)_retrieve_type_comboBox.getSelectedItem();
		if ( retrieve_type.equals( ResourceManager.get( "retrieve.property.dialog.equal")))
			ScenarioManager.get_instance().retrieve_forward(
				_transitionManager,
				( ObjectBase)_object_comboBox.getSelectedItem(),
				( String)_property_name_comboBox.getSelectedItem(),
				( String)_property_value_comboBoxes[ 0].getSelectedItem());
		else {
			String text0 = ( String)_property_value_comboBoxes[ 0].getSelectedItem();
			double value0;
			try {
				value0 = Double.parseDouble( text0);
			} catch (NumberFormatException e) {
				return;
			}
			if ( retrieve_type.equals( ResourceManager.get( "retrieve.property.dialog.more.than")))
				ScenarioManager.get_instance().retrieve_forward_more_than(
					_transitionManager,
					( ObjectBase)_object_comboBox.getSelectedItem(),
					( String)_property_name_comboBox.getSelectedItem(),
					value0);
			else if ( retrieve_type.equals( ResourceManager.get( "retrieve.property.dialog.less.than")))
				ScenarioManager.get_instance().retrieve_forward_less_than(
					_transitionManager,
					( ObjectBase)_object_comboBox.getSelectedItem(),
					( String)_property_name_comboBox.getSelectedItem(),
					value0);
			else if ( retrieve_type.equals( ResourceManager.get( "retrieve.property.dialog.more.than.less.than"))) {
				String text1 = ( String)_property_value_comboBoxes[ 1].getSelectedItem();
				double value1;
				try {
					value1 = Double.parseDouble( text1);
				} catch (NumberFormatException e) {
					return;
				}
				ScenarioManager.get_instance().retrieve_forward_more_than_less_than(
					_transitionManager,
					( ObjectBase)_object_comboBox.getSelectedItem(),
					( String)_property_name_comboBox.getSelectedItem(),
					value0,
					value1);
			}
		}
	}

	/**
	 * 
	 */
	private void adjust() {
		_object_comboBox.setPreferredSize(
			new Dimension( _object_comboBox.getPreferredSize().width + 15,
				_object_comboBox.getPreferredSize().height));
		_property_name_comboBox.setPreferredSize(
			new Dimension( _property_name_comboBox.getPreferredSize().width + 15,
				_property_name_comboBox.getPreferredSize().height));
		_retrieve_type_comboBox.setPreferredSize(
			new Dimension( _retrieve_type_comboBox.getPreferredSize().width + 15,
				_retrieve_type_comboBox.getPreferredSize().height));
		for ( int i = 0; i < _property_value_comboBoxes.length; ++i)
			_property_value_comboBoxes[ i].setPreferredSize(
				new Dimension( _property_value_comboBoxes[ i].getPreferredSize().width + 15,
					_property_value_comboBoxes[ i].getPreferredSize().height));

		int width = _backward_button.getPreferredSize().width;
		width = Math.max( width, _forward_button.getPreferredSize().width);

		Dimension dimension = new Dimension( width,
			_backward_button.getPreferredSize().height);
		_backward_button.setPreferredSize( dimension);
		_forward_button.setPreferredSize( dimension);
	}
}
