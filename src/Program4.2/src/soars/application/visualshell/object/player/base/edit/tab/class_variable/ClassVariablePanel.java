/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab.class_variable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.common.arbitrary.ClassManager;
import soars.application.visualshell.object.player.agent.AgentObject;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PanelBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase;
import soars.application.visualshell.object.player.base.object.base.ObjectBase;
import soars.application.visualshell.object.player.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.observer.Observer;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 *
 */
public class ClassVariablePanel extends PanelBase {

	/**
	 * 
	 */
	private ClassManager _classManager = null;

	/**
	 * 
	 */
	private JTextField _jarFilenameTextField = null;

	/**
	 * 
	 */
	private JTextField _classnameTextField = null;

	/**
	 * @param playerBase
	 * @param propertyPageMap
	 * @param variableTableBase
	 * @param classManager
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public ClassVariablePanel(PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap, VariableTableBase variableTableBase, ClassManager classManager, Color color, Frame owner, Component parent) {
		super("class variable", playerBase, propertyPageMap, variableTableBase, color, owner, parent);
		_classManager = classManager;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#setup()
	 */
	public boolean setup() {
		if ( !super.setup())
			return false;


		setLayout( new BorderLayout());


		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		setup_center_panel( center_panel);

		add( center_panel);


		JPanel east_panel = new JPanel();
		east_panel.setLayout( new BorderLayout());

		setup_buttons( east_panel);

		add( east_panel, "East");


		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	protected boolean setup_center_panel(JPanel parent) {
		insert_vertical_strut( parent);
		setup_nameTextField( parent);
		insert_vertical_strut( parent);
		setup_jarFilenameTextField( parent);
		insert_vertical_strut( parent);
		setup_classnameTextField( parent);
		insert_vertical_strut( parent);
		setup_commentTextField( parent);
		insert_vertical_strut( parent);
		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_nameTextField(JPanel parent) {
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
		_textUndoRedoManagers.add( new TextUndoRedoManager( _nameTextField, this));
		_components.add( _nameTextField);
		panel.add( _nameTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_jarFilenameTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.class.variable.table.header.jar.filename"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_jarFilenameTextField = new JTextField();
		_jarFilenameTextField.setEditable( false);
		_jarFilenameTextField.setSelectionColor( _color);
		_jarFilenameTextField.setForeground( _color);
		_components.add( _jarFilenameTextField);
		panel.add( _jarFilenameTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_classnameTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.class.variable.table.header.classname"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_classnameTextField = !_playerBase.is_multi() ? new CustomDDTextField( _jarFilenameTextField) : new JTextField();
		_classnameTextField.setEditable( false);
		_classnameTextField.setSelectionColor( _color);
		_classnameTextField.setForeground( _color);
		_components.add( _classnameTextField);
		panel.add( _classnameTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_commentTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.class.variable.table.header.comment"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_commentTextField = new TextField();
		_commentTextField.setSelectionColor( _color);
		_commentTextField.setForeground( _color);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));
		_components.add( _commentTextField);
		panel.add( _commentTextField);

		parent.add( panel);
	}

	/**
	 * 
	 */
	public void adjust() {
		int width = 0;
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);

		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#on_append(java.awt.event.ActionEvent)
	 */
	protected void on_append(ActionEvent actionEvent) {
		ObjectBase objectBase = on_append();
		if ( null == objectBase)
			return;

		_variableTableBase.append( objectBase);

		_propertyPageMap.get( "variable").update();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#on_update(java.awt.event.ActionEvent)
	 */
	protected void on_update(ActionEvent actionEvent) {
		int[] rows = _variableTableBase.getSelectedRows();
		if ( null == rows || 1 != rows.length)
			return;

		ObjectBase objectBase = ( ObjectBase)_variableTableBase.getValueAt( rows[ 0], 0);
		update( rows[ 0], objectBase, true);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#update(int, soars.application.visualshell.object.player.base.object.base.ObjectBase, boolean)
	 */
	protected void update(int row, ObjectBase objectBase, boolean selection) {
		WarningManager.get_instance().cleanup();

		ObjectBase originalObjectBase = ObjectBase.create( objectBase);
		if ( !on_update( objectBase))
			return;

		_variableTableBase.update( row, originalObjectBase, selection);

		_propertyPageMap.get( "variable").update();

		if ( !WarningManager.get_instance().isEmpty()) {
			WarningDlg1 warningDlg1 = new WarningDlg1(
				_owner,
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message3"),
				_parent);
			warningDlg1.do_modal();
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#update(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public void update(ObjectBase objectBase) {
		if ( null == objectBase) {
			_nameTextField.setText( "");
			//_jarFilenameTextField.setText( "");
			//_classnameTextField.setText( "");
			_commentTextField.setText( "");
		} else {
			ClassVariableObject classVariableObject = ( ClassVariableObject)objectBase;
			_nameTextField.setText( classVariableObject._name);
			_jarFilenameTextField.setText( classVariableObject._jarFilename);
			_classnameTextField.setText( classVariableObject._classname);
			_commentTextField.setText( classVariableObject._comment);
		}

		// TODO Auto-generated method stub
		_textUndoRedoManagers.clear();
		setup_textUndoRedoManagers();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#setup_textUndoRedoManagers()
	 */
	protected void setup_textUndoRedoManagers() {
		// TODO Auto-generated method stub
		if ( !_textUndoRedoManagers.isEmpty())
			return;

		_textUndoRedoManagers.add( new TextUndoRedoManager( _nameTextField, this));
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#create_and_get()
	 */
	protected ObjectBase create_and_get() {
		ClassVariableObject classVariableObject = ( ClassVariableObject)create();
		classVariableObject._name = _nameTextField.getText();
		classVariableObject._jarFilename = _jarFilenameTextField.getText();
		classVariableObject._classname = _classnameTextField.getText();
		classVariableObject._comment = _commentTextField.getText();
		return classVariableObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#can_get_data(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	protected boolean can_get_data(ObjectBase objectBase) {
		return can_get_data( ( ClassVariableObject)objectBase, _nameTextField.getText(), _jarFilenameTextField.getText(), _classnameTextField.getText(), _commentTextField.getText());
	}

	/**
	 * @param classVariableObject
	 * @param name
	 * @param jarfilename
	 * @param classname
	 * @param comment
	 * @return
	 */
	public boolean can_get_data(ClassVariableObject classVariableObject, String name, String jarfilename, String classname, String comment) {
		if ( !Constant.is_correct_name( name)) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( name))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( name))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( jarfilename.equals( "") || classname.equals( "")) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( _playerBase instanceof AgentObject && ( name.equals( "$Name") || name.equals( "$Role") || name.equals( "$Spot"))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		WarningManager.get_instance().cleanup();

		if ( LayerManager.get_instance().other_uses_this_class_variable_as_different_class( name, jarfilename, classname, _playerBase)) {
			if ( !WarningManager.get_instance().isEmpty()) {
				WarningDlg1 warningDlg1 = new WarningDlg1(
					_owner,
					ResourceManager.get_instance().get( "warning.dialog1.title"),
					ResourceManager.get_instance().get( "warning.dialog1.message1"),
					_parent);
				warningDlg1.do_modal();
			}
			return false;
		}

		if ( !name.equals( classVariableObject._name)
			|| !jarfilename.equals( classVariableObject._jarFilename)
			|| !classname.equals( classVariableObject._classname)) {

			if ( !name.equals( classVariableObject._name) && _variableTableBase.contains( name)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] property_pages = Constant.get_property_pages( "class variable");

		for ( int i = 0; i < property_pages.length; ++i) {
			PropertyPageBase propertyPageBase = _propertyPageMap.get( property_pages[ i]);
			if ( null != propertyPageBase && propertyPageBase.contains( name)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( "class variable");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], name, _playerBase)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#get_data(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	protected void get_data(ObjectBase objectBase) {
		get_data( ( ClassVariableObject)objectBase, _nameTextField.getText(), _jarFilenameTextField.getText(), _classnameTextField.getText(), _commentTextField.getText());
	}

	/**
	 * @param classVariableObject
	 * @param name
	 * @param jarfilename
	 * @param classname
	 * @param comment
	 */
	public void get_data(ClassVariableObject classVariableObject, String name, String jarfilename, String classname, String comment) {
		if ( !classVariableObject._name.equals( "") && !name.equals( classVariableObject._name)) {
			WarningManager.get_instance().cleanup();

			_propertyPageMap.get( "variable").update_object_name( "class variable", classVariableObject._name, name);

			boolean result = LayerManager.get_instance().update_object_name( "class variable", classVariableObject._name, name, _playerBase);
			if ( result) {
				if ( _playerBase.update_object_name( "class variable", classVariableObject._name, name)) {
					String[] message = new String[] {
						( ( _playerBase instanceof AgentObject) ? "Agent : " : "Spot : ") + _playerBase._name
					};

					WarningManager.get_instance().add( message);
				}

				Observer.get_instance().on_update_playerBase( true);
				Observer.get_instance().modified();
			}
		}

		classVariableObject._name = name;
		classVariableObject._jarFilename = jarfilename;
		classVariableObject._classname = classname;
		classVariableObject._comment = comment;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#can_paste(soars.application.visualshell.object.player.base.object.base.ObjectBase, java.util.List)
	 */
	public boolean can_paste(ObjectBase objectBase, List<ObjectBase> objectBases) {
		if ( !( objectBase instanceof ClassVariableObject))
			return false;

		ClassVariableObject classVariableObject = ( ClassVariableObject)objectBase;

		if ( !Constant.is_correct_name( classVariableObject._name))
			return false;

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( classVariableObject._name))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( classVariableObject._name)))
			return false;

		if ( classVariableObject._jarFilename.equals( "") || classVariableObject._classname.equals( ""))
			return false;

		if ( _playerBase instanceof AgentObject && ( classVariableObject._name.equals( "$Name")
			|| classVariableObject._name.equals( "$Role") || classVariableObject._name.equals( "$Spot")))
			return false;

		if ( LayerManager.get_instance().other_uses_this_class_variable_as_different_class( classVariableObject._name, classVariableObject._jarFilename, classVariableObject._classname, _playerBase))
			return false;

		if ( _variableTableBase.other_objectBase_has_this_name( classVariableObject._kind, classVariableObject._name))
			return false;

		String[] property_pages = Constant.get_property_pages( "class variable");

		for ( int i = 0; i < property_pages.length; ++i) {
			PropertyPageBase propertyPageBase = _propertyPageMap.get( property_pages[ i]);
			if ( null != propertyPageBase && propertyPageBase.contains( classVariableObject._name))
				return false;
		}

		String[] kinds = Constant.get_kinds( "class variable");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], classVariableObject._name, _playerBase))
				return false;
		}

		return true;
	}
}
