/**
 * 
 */
package soars.application.visualshell.object.gis.edit.variable.panel.keyword;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.edit.field.selector.SelectGisDataFieldsDlg;
import soars.application.visualshell.object.gis.edit.variable.object.base.ObjectBase;
import soars.application.visualshell.object.gis.edit.variable.object.base.SimpleVariableObject;
import soars.application.visualshell.object.gis.edit.variable.object.keyword.KeywordObject;
import soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase;
import soars.application.visualshell.object.gis.edit.variable.table.VariableTable;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;

/**
 * @author kurata
 *
 */
public class KeywordPanel extends PanelBase {

	/**
	 * 
	 */
	private List<Field> _fields = new ArrayList<Field>();

	/**
	 * 
	 */
	private TextField _initialValueTextField = null;

	/**
	 * @param gisDataManager
	 * @param variableTable
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public KeywordPanel(GisDataManager gisDataManager, VariableTable variableTable, Color color, Frame owner, Component parent) {
		super("keyword", gisDataManager, variableTable, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	protected boolean setup_center_panel(JPanel parent) {
		insert_vertical_strut( parent);
		setup_name_textField( parent);
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
		_nameTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters1));
		_nameTextField.setSelectionColor( _color);
		_nameTextField.setForeground( _color);
		_components.add( _nameTextField);
		panel.add( _nameTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_initial_value_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JButton button = new JButton( ResourceManager.get_instance().get( "edit.keyword.dialog.initial.value"));
		button.setForeground( _color);
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				get_fields();
			}
		});
		_labels.add( button);
		panel.add( button);

		panel.add( Box.createHorizontalStrut( 5));

		_initialValueTextField = new TextField();
		_initialValueTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters3));
		_initialValueTextField.setSelectionColor( _color);
		_initialValueTextField.setForeground( _color);
		_initialValueTextField.setEditable( false);
		_components.add( _initialValueTextField);
		panel.add( _initialValueTextField);

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void get_fields() {
		SelectGisDataFieldsDlg selectGisDataFieldsDlg = new SelectGisDataFieldsDlg( _owner, ResourceManager.get_instance().get( "select.keyword.initial.value.field.dialog.title"), true, _gisDataManager.get_fields( false)/*_gisDataManager._availableKeywordFields*/, _fields, _gisDataManager, Constant._prohibitedCharacters3);
		if ( !selectGisDataFieldsDlg.do_modal())
			return;

		_fields = selectGisDataFieldsDlg._selectedFields;
		_initialValueTextField.setText( Field.get( _fields));
	}

	/**
	 * @param parent
	 */
	private void setup_comment_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.keyword.dialog.comment"));
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
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase#update(soars.application.visualshell.object.gis.edit.variable.object.base.ObjectBase)
	 */
	public void update(ObjectBase objectBase) {
		// TODO Auto-generated method stub
		SimpleVariableObject simpleVariableObject = ( SimpleVariableObject)objectBase;
		if ( null == simpleVariableObject || !(simpleVariableObject instanceof KeywordObject)) {
			_nameTextField.setText( "");
			//_initialValueComboBox.setSelectedItem(arg0).setText( "");
			_commentTextField.setText( "");
			return;
		}

		KeywordObject keywordObject = ( KeywordObject)simpleVariableObject;
		_nameTextField.setText( keywordObject._name);
		copy( keywordObject._fields);
		_initialValueTextField.setText( Field.get( keywordObject._fields));
		_commentTextField.setText( keywordObject._comment);
	}

	/**
	 * @param fields
	 */
	private void copy(List<Field> fields) {
		// TODO Auto-generated method stub
		_fields.clear();
		for ( Field field:fields)
			_fields.add( field);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase#clear()
	 */
	public void clear() {
		// TODO Auto-generated method stub
		super.clear();
		_fields.clear();
		_initialValueTextField.setText( "");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase#is_empty()
	 */
	protected boolean is_empty() {
		// TODO Auto-generated method stub
		return ( super.is_empty() && _initialValueTextField.getText().equals( ""));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase#create_and_get()
	 */
	protected ObjectBase create_and_get() {
		// TODO Auto-generated method stub
		KeywordObject keywordObject = new KeywordObject();
		keywordObject._name = _nameTextField.getText();
		keywordObject.copy( _fields);
		keywordObject._comment = _commentTextField.getText();
		return keywordObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase#can_get_data(soars.application.visualshell.object.gis.edit.variable.object.base.ObjectBase)
	 */
	protected boolean can_get_data(ObjectBase objectBase) {
		// TODO Auto-generated method stub
		if ( !( objectBase instanceof KeywordObject))
			return false;

		KeywordObject keywordObject = ( KeywordObject)objectBase;

		if ( !Constant.is_correct_name( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

//		if ( _playerBase instanceof AgentObject && ( _nameTextField.getText().equals( "$Name")
//			|| _nameTextField.getText().equals( "$Role") || _nameTextField.getText().equals( "$Spot"))) {
//			JOptionPane.showMessageDialog( _parent,
//				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
//				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
//				JOptionPane.ERROR_MESSAGE);
//			return false;
//		}

		if ( null == _initialValueTextField.getText()
			|| 0 < _initialValueTextField.getText().indexOf( '$')
			|| _initialValueTextField.getText().equals( "$")
			|| _initialValueTextField.getText().startsWith( " ")
			|| _initialValueTextField.getText().endsWith( " ")
			|| _initialValueTextField.getText().equals( "$Name")
			|| _initialValueTextField.getText().equals( "$Role")
			|| _initialValueTextField.getText().equals( "$Spot")
			|| 0 <= _initialValueTextField.getText().indexOf( Constant._experimentName)) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( _initialValueTextField.getText().startsWith( "$")
			&& ( 0 <= _initialValueTextField.getText().indexOf( " ")
			|| 0 < _initialValueTextField.getText().indexOf( "$", 1)
			|| 0 < _initialValueTextField.getText().indexOf( ")", 1))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( !_nameTextField.getText().equals( keywordObject._name)) {
			if ( _variableTable.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

//		String[] property_pages = Constant.get_property_pages( "simple variable");
//
//		for ( int i = 0; i < property_pages.length; ++i) {
//			PropertyPageBase propertyPageBase = _propertyPageMap.get( property_pages[ i]);
//			if ( null != propertyPageBase && propertyPageBase.contains( _nameTextField.getText())) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//		}

		String[] kinds = Constant.get_kinds( "keyword");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), new SpotObject()/*_playerBase*/)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
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
		if ( !( objectBase instanceof KeywordObject))
			return;

		KeywordObject keywordObject = ( KeywordObject)objectBase;

		//if ( !keywordObject._name.equals( "") && !_nameTextField.getText().equals( keywordObject._name)) {
//			_propertyPageMap.get( "variable").update_object_name( "keyword", keywordObject._name, _nameTextField.getText());

		//	WarningManager.get_instance().cleanup();

		//	boolean result = LayerManager.get_instance().update_object_name( "keyword", keywordObject._name, _nameTextField.getText(), new SpotObject()/*_playerBase*/);
		//	if ( result) {
//				if ( _playerBase.update_object_name( "keyword", keywordObject._name, _nameTextField.getText())) {
//					String[] message = new String[] {
//						( ( _playerBase instanceof AgentObject) ? "Agent : " : "Spot : ") + _playerBase._name
//					};
//
//					WarningManager.get_instance().add( message);
//				}

//				if ( _playerBase instanceof AgentObject)
//					Observer.get_instance().on_update_agent_object( "keyword", keywordObject._name, _nameTextField.getText());
//				else if ( _playerBase instanceof SpotObject)
		//		Observer.get_instance().on_update_spot_object( "keyword", keywordObject._name, _nameTextField.getText());

		//		Observer.get_instance().on_update_playerBase( true);
		//		Observer.get_instance().modified();
		//	}
		//}

		keywordObject._name = _nameTextField.getText();
		keywordObject.copy( _fields);
		keywordObject._comment = _commentTextField.getText();
	}
}
