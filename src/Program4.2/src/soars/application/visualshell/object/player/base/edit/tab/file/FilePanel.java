/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab.file;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
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
import soars.application.visualshell.object.player.agent.AgentObject;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PanelBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase;
import soars.application.visualshell.object.player.base.object.base.ObjectBase;
import soars.application.visualshell.object.player.base.object.file.FileObject;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.observer.Observer;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.file.manager.FileManager;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 *
 */
public class FilePanel extends PanelBase {

	/**
	 * 
	 */
	private FileManager _fileManager = null;

	/**
	 * 
	 */
	private JTextField _initialValueTextField = null;

	/**
	 * @param playerBase
	 * @param propertyPageMap
	 * @param variableTableBase
	 * @param fileManager
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public FilePanel(PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap, VariableTableBase variableTableBase, FileManager fileManager, Color color, Frame owner, Component parent) {
		super("file", playerBase, propertyPageMap, variableTableBase, color, owner, parent);
		_fileManager = fileManager;
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
		setup_initialValueTextField( parent);
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
	private void setup_initialValueTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.file.table.header.initial.value"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_initialValueTextField = !_playerBase.is_multi() ? new FileAndDirectoryDDTextField() : new JTextField();
		_initialValueTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters16));
		_initialValueTextField.setSelectionColor( _color);
		_initialValueTextField.setForeground( _color);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _initialValueTextField, this));
		_components.add( _initialValueTextField);
		panel.add( _initialValueTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_commentTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.file.table.header.comment"));
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
	
		_fileManager.select( _initialValueTextField.getText());
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
			_initialValueTextField.setText( "");
			_commentTextField.setText( "");
		} else {
			FileObject fileObject = ( FileObject)objectBase;
			_nameTextField.setText( fileObject._name);
			_initialValueTextField.setText( fileObject._initialValue);
			_commentTextField.setText( fileObject._comment);
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
		_textUndoRedoManagers.add( new TextUndoRedoManager( _initialValueTextField, this));
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#clear()
	 */
	public void clear() {
		super.clear();
		_initialValueTextField.setText( "");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#is_empty()
	 */
	protected boolean is_empty() {
		return ( super.is_empty() && _initialValueTextField.getText().equals( ""));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#create_and_get()
	 */
	protected ObjectBase create_and_get() {
		FileObject fileObject = new FileObject();
		fileObject._name = _nameTextField.getText();
		fileObject._initialValue = _initialValueTextField.getText();
		fileObject._comment = _commentTextField.getText();
		return fileObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#can_get_data(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	protected boolean can_get_data(ObjectBase objectBase) {
		FileObject fileObject = ( FileObject)objectBase;

		if ( !Constant.is_correct_name( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( _playerBase instanceof AgentObject && ( _nameTextField.getText().equals( "$Name")
			|| _nameTextField.getText().equals( "$Role") || _nameTextField.getText().equals( "$Spot"))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

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
				ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( _initialValueTextField.getText().startsWith( "$")
			&& ( 0 <= _initialValueTextField.getText().indexOf( " ")
			|| 0 < _initialValueTextField.getText().indexOf( "$", 1)
			|| 0 < _initialValueTextField.getText().indexOf( ")", 1))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// ファイルとしての名前チェックが必要！
		if ( !_initialValueTextField.getText().startsWith( "$")) {
			if ( _initialValueTextField.getText().startsWith( "/") || _initialValueTextField.getText().matches( ".*[/]{2,}.*")) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			File user_data_directory = LayerManager.get_instance().get_user_data_directory();
			if ( null == user_data_directory) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			File file = new File( user_data_directory.getAbsolutePath() + "/" + _initialValueTextField.getText());
			//if ( !file.exists())
			//	return false;

			if ( _initialValueTextField.getText().endsWith( "/") && !file.isDirectory()) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}


		if ( !_nameTextField.getText().equals( fileObject._name)) {
			if ( _variableTableBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] property_pages = Constant.get_property_pages( "file");

		for ( int i = 0; i < property_pages.length; ++i) {
			PropertyPageBase propertyPageBase = _propertyPageMap.get( property_pages[ i]);
			if ( null != propertyPageBase && propertyPageBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( "file");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), _playerBase)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#get_data(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	protected void get_data(ObjectBase objectBase) {
		FileObject fileObject = ( FileObject)objectBase;

		if ( !fileObject._name.equals( "") && !_nameTextField.getText().equals( fileObject._name)) {

			WarningManager.get_instance().cleanup();

			_propertyPageMap.get( "variable").update_object_name( "file", fileObject._name, _nameTextField.getText());

			boolean result = LayerManager.get_instance().update_object_name( "file", fileObject._name, _nameTextField.getText(), _playerBase);
			if ( result) {
				if ( _playerBase.update_object_name( "file", fileObject._name, _nameTextField.getText())) {
					String[] message = new String[] {
						( ( _playerBase instanceof AgentObject) ? "Agent : " : "Spot : ") + _playerBase._name
					};

					WarningManager.get_instance().add( message);
				}

				if ( _playerBase instanceof AgentObject)
					Observer.get_instance().on_update_agent_object( "file", fileObject._name, _nameTextField.getText());
				else if ( _playerBase instanceof SpotObject)
					Observer.get_instance().on_update_spot_object( "file", fileObject._name, _nameTextField.getText());

				Observer.get_instance().on_update_playerBase( true);
				Observer.get_instance().modified();
			}
		}

		fileObject._name = _nameTextField.getText();
		fileObject._initialValue = _initialValueTextField.getText();
		fileObject._comment = _commentTextField.getText();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#can_paste(soars.application.visualshell.object.player.base.object.base.ObjectBase, java.util.List)
	 */
	public boolean can_paste(ObjectBase objectBase, List<ObjectBase> objectBases) {
		if ( !( objectBase instanceof FileObject))
			return false;

		FileObject fileObject = ( FileObject)objectBase;

		if ( !Constant.is_correct_name( fileObject._name))
			return false;

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( fileObject._name))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( fileObject._name)))
			return false;

		if ( _playerBase instanceof AgentObject && ( fileObject._name.equals( "$Name")
			|| fileObject._name.equals( "$Role") || fileObject._name.equals( "$Spot")))
			return false;

		if ( null == fileObject._initialValue
			|| 0 < fileObject._initialValue.indexOf( '$')
			|| fileObject._initialValue.equals( "$")
			|| fileObject._initialValue.startsWith( " ")
			|| fileObject._initialValue.endsWith( " ")
			|| fileObject._initialValue.equals( "$Name")
			|| fileObject._initialValue.equals( "$Role")
			|| fileObject._initialValue.equals( "$Spot")
			|| 0 <= fileObject._initialValue.indexOf( Constant._experimentName))
			return false;

		if ( fileObject._initialValue.startsWith( "$")
			&& ( 0 <= fileObject._initialValue.indexOf( " ")
			|| 0 < fileObject._initialValue.indexOf( "$", 1)
			|| 0 < fileObject._initialValue.indexOf( ")", 1)))
			return false;

		// ファイルとしての名前チェックが必要！
		if ( !fileObject._initialValue.startsWith( "$")) {
			if ( fileObject._initialValue.startsWith( "/") || fileObject._initialValue.matches( ".*[/]{2,}.*"))
				return false;

			File user_data_directory = LayerManager.get_instance().get_user_data_directory();
			if ( null == user_data_directory)
				return false;

			File file = new File( user_data_directory.getAbsolutePath() + "/" + fileObject._initialValue);
			//if ( !file.exists())
			//	return false;

			if ( fileObject._initialValue.endsWith( "/") && !file.isDirectory())
				return false;
		}


		if ( _variableTableBase.other_objectBase_has_this_name( fileObject._kind, fileObject._name))
			return false;

		String[] property_pages = Constant.get_property_pages( "file");

		for ( int i = 0; i < property_pages.length; ++i) {
			PropertyPageBase propertyPageBase = _propertyPageMap.get( property_pages[ i]);
			if ( null != propertyPageBase && propertyPageBase.contains( fileObject._name))
				return false;
		}

		String[] kinds = Constant.get_kinds( "file");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], fileObject._name, _playerBase))
				return false;
		}

		if ( null != LayerManager.get_instance().get_chart( fileObject._name))
			return false;

		return true;
	}

	/**
	 * @param srcPath
	 * @param destPath
	 */
	public void update_initial_value(File srcPath, File destPath) {
		// TODO Auto-generated method stub
		String value = _initialValueTextField.getText();
		if ( value.equals( "") || value.startsWith( "$"))
			return;

		String srcValue = ( srcPath.getAbsolutePath().substring( LayerManager.get_instance().get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/");
		String destValue = ( destPath.getAbsolutePath().substring( LayerManager.get_instance().get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/");
		if ( !value.startsWith( srcValue))
			return;

		_initialValueTextField.setText( destValue + value.substring( srcValue.length()));
	}
}
