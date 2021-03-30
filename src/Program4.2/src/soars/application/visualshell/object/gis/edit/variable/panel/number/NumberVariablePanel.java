/**
 * 
 */
package soars.application.visualshell.object.gis.edit.variable.panel.number;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.edit.variable.object.base.ObjectBase;
import soars.application.visualshell.object.gis.edit.variable.object.base.SimpleVariableObject;
import soars.application.visualshell.object.gis.edit.variable.object.number.NumberObject;
import soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase;
import soars.application.visualshell.object.gis.edit.variable.table.VariableTable;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;

/**
 * @author kurata
 *
 */
public class NumberVariablePanel extends PanelBase {

	/**
	 * 
	 */
	private ComboBox _typeComboBox = null;

	/**
	 * 
	 */
	private ComboBox _initialValueComboBox = null;

	/**
	 * @param gisDataManager
	 * @param variableTable
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public NumberVariablePanel(GisDataManager gisDataManager, VariableTable variableTable, Color color, Frame owner, Component parent) {
		super("number object", gisDataManager, variableTable, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	protected boolean setup_center_panel(JPanel parent) {
		insert_vertical_strut( parent);
		setup_name_textField( parent);
		insert_vertical_strut( parent);
		setup_type_comboBox( parent);
		insert_vertical_strut( parent);
		setup_initial_value_textField( parent);
		insert_vertical_strut( parent);
		setup_comment_textField( parent);
		//insert_vertical_strut( parent);
		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_name_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.name"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_nameTextField = new TextField();
		_nameTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters4));
		_nameTextField.setSelectionColor( _color);
		_nameTextField.setForeground( _color);
		_components.add( _nameTextField);
		panel.add( _nameTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_type_comboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.number.object.dialog.type"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_typeComboBox = new ComboBox( new String[] {
				ResourceManager.get_instance().get( "number.object.integer"),
				ResourceManager.get_instance().get( "number.object.real.number")},
			_color, false, new CommonComboBoxRenderer( _color, false));
		_typeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> fields = _gisDataManager.get_numeric_fields( ( String)_typeComboBox.getSelectedItem(), false);
				CommonTool.update( _initialValueComboBox, fields.toArray( new String[ 0]));
			}
		});
		_components.add( _typeComboBox);
		panel.add( _typeComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_initial_value_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.number.object.dialog.initial.value"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_initialValueComboBox = new ComboBox( _color, false, new CommonComboBoxRenderer( _color, false));
		_components.add( _initialValueComboBox);
		panel.add( _initialValueComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_comment_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.number.object.dialog.comment"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_commentTextField = new TextField();
		_commentTextField.setSelectionColor( _color);
		_commentTextField.setForeground( _color);
		_components.add( _commentTextField);
		panel.add( _commentTextField);

		parent.add( panel);
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_typeComboBox.setSelectedItem( ResourceManager.get_instance().get( "number.object.integer"));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase#update(soars.application.visualshell.object.gis.edit.variable.object.base.ObjectBase)
	 */
	public void update(ObjectBase objectBase) {
		// TODO Auto-generated method stub
		SimpleVariableObject simpleVariableObject = ( SimpleVariableObject)objectBase;
		if ( null == simpleVariableObject || !(simpleVariableObject instanceof NumberObject)) {
			_nameTextField.setText( "");
			//_initialValueComboBox.setSelectedItem(arg0).setText( "");
			_commentTextField.setText( "");
			return;
		}

		NumberObject numberObject = ( NumberObject)simpleVariableObject;
		_nameTextField.setText( numberObject._name);
		_typeComboBox.setSelectedItem( NumberObject.get_type_name( numberObject._type));
		_initialValueComboBox.setSelectedItem( Field.get( numberObject._fields));
		_commentTextField.setText( numberObject._comment);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase#clear()
	 */
	public void clear() {
		// TODO Auto-generated method stub
		super.clear();
		_typeComboBox.setSelectedIndex( 0);
//		_initialValueTextField.setText( "");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase#is_empty()
	 */
	protected boolean is_empty() {
		// TODO Auto-generated method stub
		return super.is_empty();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase#create_and_get()
	 */
	protected ObjectBase create_and_get() {
		// TODO Auto-generated method stub
		NumberObject numberObject = new NumberObject();
		numberObject._name = _nameTextField.getText();
		numberObject._type = NumberObject.get_type( ( String)_typeComboBox.getSelectedItem());
		numberObject._fields = Field.get( ( String)_initialValueComboBox.getSelectedItem());
		numberObject._comment = _commentTextField.getText();
		return numberObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase#can_get_data(soars.application.visualshell.object.gis.edit.variable.object.base.ObjectBase)
	 */
	protected boolean can_get_data(ObjectBase objectBase) {
		// TODO Auto-generated method stub
		if ( !( objectBase instanceof NumberObject))
			return false;

		NumberObject numberObject = ( NumberObject)objectBase;

		if ( !Constant.is_correct_name( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String type = ( String)_typeComboBox.getSelectedItem();
		if ( null == type || type.equals( ""))
			return false;

		if ( !_nameTextField.getText().equals( numberObject._name)) {
			if ( _variableTable.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		// TODO 初期値については別途チェックが必要
//		String initial_value = _initialValueTextField.getText();
//
//		if ( !initial_value.equals( "")) {
//			if ( initial_value.equals( "$") || 0 < initial_value.indexOf( '$')) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//
//			if ( initial_value.startsWith( "$") && 0 < initial_value.indexOf( "$", 1)) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//
//			if ( initial_value.startsWith( "$") && 0 < initial_value.indexOf( ")", 1)) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//
//			if ( initial_value.equals( "$Name")
//				|| initial_value.equals( "$Role")
//				|| initial_value.equals( "$Spot")
//				|| 0 <= initial_value.indexOf( Constant._experimentName)) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//
//			initial_value = NumberObject.is_correct( initial_value, NumberObject.get_type( type));
//			if ( null == initial_value) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//		}

//		String[] property_pages = Constant.get_property_pages( "simple variable");
//
//		for ( int i = 0; i < property_pages.length; ++i) {
//			PropertyPageBase propertyPageBase = _propertyPageMap.get( property_pages[ i]);
//			if ( null != propertyPageBase && propertyPageBase.contains( _nameTextField.getText())) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//		}

		String[] kinds = Constant.get_kinds( "number object");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), new SpotObject()/*_playerBase*/)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		// TODO 型チェック
		if ( !numberObject._type.equals( ( String)_typeComboBox.getSelectedItem())) {
			if ( !LayerManager.get_instance().is_number_object_type_correct( "spot", numberObject._name, ( String)_typeComboBox.getSelectedItem())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.number.object.type.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase#get_data(soars.application.visualshell.object.gis.edit.variable.object.base.ObjectBase)
	 */
	protected void get_data(ObjectBase objectBase) {
		// TODO Auto-generated method stub
		if ( !( objectBase instanceof NumberObject))
			return;

		NumberObject numberObject = ( NumberObject)objectBase;

		String type = ( String)_typeComboBox.getSelectedItem();

//		if ( !numberObject._name.equals( "") && !_nameTextField.getText().equals( numberObject._name))
//			_propertyPageMap.get( "variable").update_object_name( "number object", numberObject._name, _nameTextField.getText());

		//WarningManager.get_instance().cleanup();

		//boolean result1 = false;
		//if ( !numberObject._name.equals( "") && !_nameTextField.getText().equals( numberObject._name))
		//	result1 = LayerManager.get_instance().update_number_object_name( "spot", numberObject._name, _nameTextField.getText(), new SpotObject()/*_playerBase*/);
		//boolean result2 = false;
		//if ( !numberObject._type.equals( "") && !numberObject._type.equals( NumberObject.get_type( type)))
		//	result2 = LayerManager.get_instance().update_number_object_type( "spot", _nameTextField.getText(), NumberObject.get_type( type), new SpotObject()/*_playerBase*/);
		//if ( result1 || result2) {
//			boolean result3 = false;
//			if ( !numberObject._name.equals( "") && !_nameTextField.getText().equals( numberObject._name))
//				result3 = _playerBase.update_object_name( "number object", numberObject._name, _nameTextField.getText());
//			boolean result4 = false;
//			if ( !numberObject._type.equals( "") && !numberObject._type.equals( NumberObject.get_type( type)))
//				result4 = _playerBase.update_number_object_type(
//					( _playerBase instanceof AgentObject) ? "agent" : "spot",
//					_nameTextField.getText(), NumberObject.get_type( type));
//			if ( result3 || result4) {
//				String[] message = new String[] {
//					( ( _playerBase instanceof AgentObject) ? "Agent : " : "Spot : ") + _playerBase._name
//				};
//
//				WarningManager.get_instance().add( message);
//			}

//			if ( _playerBase instanceof AgentObject)
//				Observer.get_instance().on_update_agent_object( "number object", numberObject._name, _nameTextField.getText());
//			else if ( _playerBase instanceof SpotObject)
		//	Observer.get_instance().on_update_spot_object( "number object", numberObject._name, _nameTextField.getText());

		//	Observer.get_instance().on_update_playerBase( true);
		//	Observer.get_instance().modified();
		//}

		numberObject._name = _nameTextField.getText();
		numberObject._type = NumberObject.get_type( type);
		numberObject._fields = Field.get( ( String)_initialValueComboBox.getSelectedItem());
		numberObject._comment = _commentTextField.getText();
	}
}
