/*
 * 2005/06/02
 */
package soars.application.visualshell.object.player.base.edit.tab.file;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.awt.SystemColor;
import java.io.File;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.agent.AgentObject;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.object.base.ObjectBase;
import soars.application.visualshell.object.player.base.object.file.FileObject;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.observer.Observer;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.file.manager.FileManager;
import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;
import soars.common.utility.swing.file.manager.edit.FileEditorDlg;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 */
public class FilePropertyPage extends PropertyPageBase implements IFileManagerCallBack {

	/**
	 * 
	 */
	private JSplitPane _splitPane = null;

	/**
	 * 
	 */
	private FileTable _fileTable = null;

	/**
	 * 
	 */
	private FilePanel _filePanel = null;

	/**
	 * 
	 */
	private FileManager _fileManager = null;

	/**
	 * @param title
	 * @param playerBase
	 * @param propertyPageMap
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public FilePropertyPage(String title, PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap, int index, Frame owner, Component parent) {
		super(title, playerBase, propertyPageMap, index, owner, parent);
	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Environment.get_instance().set(
			Environment._editObjectDialogFilePropertyPageDividerLocationKey, String.valueOf( _splitPane.getDividerLocation()));
		Environment.get_instance().set(
			Environment._editObjectDialogFileManagerDividerLocationKey, String.valueOf( _fileManager.getDividerLocation()));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#contains(java.lang.String)
	 */
	public boolean contains(String name) {
		return _fileTable.contains( name);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#contains(java.lang.String, java.lang.String)
	 */
	public boolean contains(String name, String number) {
		return _fileTable.contains(name, number);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#get()
	 */
	public String[] get() {
		return _fileTable.get();
	}

	/**
	 * @param contains_empty
	 * @return
	 */
	public String[] get_agent_file_names(boolean contains_empty) {
		return _fileTable.get_agent_file_names( contains_empty);
	}

	/**
	 * @param contains_empty
	 * @return
	 */
	public String[] get_file_names(boolean contains_empty) {
		return _fileTable.get_file_names( contains_empty);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.tab.TabbedPage#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		create_fileTable();
		create_fileManager();


		setLayout( new BorderLayout());


		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));


		_splitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT);


		if ( !setup1())
			return false;


		if ( !setup2())
			return false;


		_splitPane.setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._editObjectDialogFilePropertyPageDividerLocationKey, "250")));


		center_panel.add( _splitPane);


		insert_vertical_strut( center_panel);


		add( center_panel);


		adjust();


		return true;
	}

	/**
	 * 
	 */
	private void create_fileTable() {
		_fileTable = new FileTable( _playerBase, _propertyPageMap, this, _owner, _parent);
	}

	/**
	 * 
	 */
	private void create_fileManager() {
		_fileManager = new FileManager( this, _owner, _parent);
	}

	/**
	 * @return
	 */
	private boolean setup1() {
		JPanel base_panel = new JPanel();
		base_panel.setLayout( new BorderLayout());


		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( center_panel);

		if ( !setup_fileTable( center_panel))
			return false;

		base_panel.add( center_panel);


		JPanel south_panel = new JPanel();
		south_panel.setLayout( new BoxLayout( south_panel, BoxLayout.Y_AXIS));

		if ( !setup_components( south_panel))
			return false;

		base_panel.add( south_panel, "South");


		_splitPane.setTopComponent( base_panel);


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_fileTable(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_fileTable.setup())
			return false;

		if ( _playerBase.is_multi())
			_fileTable.setEnabled( false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _fileTable);
		scrollPane.getViewport().setOpaque( true);
		scrollPane.getViewport().setBackground( SystemColor.text);

		panel.add( scrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_components(JPanel parent) {
		_filePanel = new FilePanel( _playerBase, _propertyPageMap, _fileTable, _fileManager, SystemColor.textText, _owner, _parent);
		if ( !_filePanel.setup())
			return false;

		_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.file"), _filePanel);
		parent.add( _filePanel);

		return true;
	}


	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#changeSelection(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public void changeSelection(ObjectBase objectBase) {
		changeSelection( ( FileObject)objectBase, true);
	}

	/**
	 * @param fileObject
	 * @param update_all
	 */
	public void changeSelection(FileObject fileObject, boolean update_all) {
		_filePanel.update( fileObject);

		if ( !update_all)
			return;

		if ( null == fileObject)
			return;

		// FileManagerの表示も更新する必要がある
		_fileManager.select( fileObject._initialValue.startsWith( "$") ? "" : fileObject._initialValue);
	}

	/**
	 * @return
	 */
	private boolean setup2() {
		JPanel base_panel = new JPanel();
		base_panel.setLayout( new BorderLayout());


		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		if ( !setup_fileManager( center_panel))
			return false;

		base_panel.add( center_panel);


		_splitPane.setBottomComponent( base_panel);


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_fileManager(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_fileManager.setup( false, LayerManager.get_instance().get_user_data_directory()))
			return false;

		if ( _playerBase.is_multi())
			_fileManager.select( "");

		_fileManager.setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._editObjectDialogFileManagerDividerLocationKey, "100")));

		panel.add( _fileManager);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_start_paste()
	 */
	public void on_start_paste() {
		WarningManager.get_instance().cleanup();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_start_paste_and_remove()
	 */
	public void on_start_paste_and_remove() {
		WarningManager.get_instance().cleanup();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#can_copy(java.io.File)
	 */
	public boolean can_copy(File file) {
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#can_paste(java.io.File)
	 */
	public boolean can_paste(File file) {
		if ( 0 > file.getAbsolutePath().indexOf( "$"))
			return true;

		if ( WarningManager.get_instance().size() < 100)
			WarningManager.get_instance().add( new String[] { file.getAbsolutePath()});

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_paste_completed()
	 */
	public void on_paste_completed() {
		warn( ResourceManager.get_instance().get( "warning.dialog1.message4"));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_paste_and_remove_completed()
	 */
	public void on_paste_and_remove_completed() {
		warn( ResourceManager.get_instance().get( "warning.dialog1.message5"));
	}

	/**
	 * @param message
	 */
	private void warn(String message) {
		if ( WarningManager.get_instance().isEmpty())
			return;

		WarningDlg1 warningDlg1 = new WarningDlg1(
			_owner,
			ResourceManager.get_instance().get( "warning.dialog1.title"),
			message,
			_parent);
		warningDlg1.do_modal();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#modified(soars.common.utility.swing.file.manager.IFileManager)
	 */
	public void modified(IFileManager fileManager) {
		// 対象のファイルシステム内で変更があった場合に呼び出される
		_propertyPageMap.get( "initial data file").refresh();	// 他のFileManagerを更新
		if ( Environment.get_instance().is_extransfer_enable())
			_propertyPageMap.get( "extransfer").refresh();
		Observer.get_instance().modified();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#refresh()
	 */
	public void refresh() {
		// TODO Auto-generated method stub
		_fileManager.refresh();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#select_changed(soars.common.utility.swing.file.manager.IFileManager)
	 */
	public void select_changed(IFileManager fileManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#can_remove(java.io.File)
	 */
	public boolean can_remove(File file) {
		// TODO Auto-generated method stub
		// ファイルを削除する際に呼び出される
		// 削除可能ならtrueを、そうでなければfalseを返す必要がある
		if ( Environment.get_instance().is_extransfer_enable() && _propertyPageMap.get( "extransfer").uses_this_file( file))
			return false;

		return ( !_fileTable.uses_this_file( file)
			&& !_propertyPageMap.get( "initial data file").uses_this_file( file)
			&& !LayerManager.get_instance().uses_this_file( file));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#uses_this_file(java.io.File)
	 */
	public boolean uses_this_file(File file) {
		// TODO Auto-generated method stub
		return _fileTable.uses_this_file( file);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_move(java.io.File, java.io.File)
	 */
	public void on_move(File srcPath, File destPath) {
		// TODO Auto-generated method stub
		// ファイルが移動された時に元のファイルと移動されたファイルを受け取る
		_filePanel.update_initial_value( srcPath, destPath);
		_propertyPageMap.get( "initial data file").update_file( srcPath, destPath);
		_fileTable.move_file( srcPath, destPath);
		_propertyPageMap.get( "initial data file").move_file( srcPath, destPath);
		if ( Environment.get_instance().is_extransfer_enable()) {
			_propertyPageMap.get( "extransfer").update_file( srcPath, destPath);
			_propertyPageMap.get( "extransfer").move_file( srcPath, destPath);
		}
		if ( LayerManager.get_instance().move_file( srcPath, destPath))
			Observer.get_instance().modified();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#move_file(java.io.File, java.io.File)
	 */
	public void move_file(File srcPath, File destPath) {
		// TODO Auto-generated method stub
		_fileTable.move_file( srcPath, destPath);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#update_file(java.io.File, java.io.File)
	 */
	public void update_file(File srcPath, File destPath) {
		_filePanel.update_initial_value( srcPath, destPath);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_select(java.io.File, java.lang.String)
	 */
	public void on_select(File file, String encoding) {
		if ( !file.exists() || !file.isFile() || !file.canRead() || !file.canWrite())
			return;

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null == image)
			return;

		FileEditorDlg fileEditorDlg = new FileEditorDlg( _owner, file.getName(), true, image, file, Environment.get_instance(), Environment._fileEditorWindowRectangleKey, encoding, _fileManager, this);
		fileEditorDlg.do_modal( _fileManager);
//			fileEditorFrame.setIconImage( image);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_rename(java.io.File, java.io.File)
	 */
	public void on_rename(File originalFile, File newFile) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#get_export_directory()
	 */
	public File get_export_directory() {
		String export_directory = "";
		File directory = null;
		
		String value = Environment.get_instance().get( Environment._exportFilesDirectoryKey, "");
		if ( null != value && !value.equals( "")) {
			directory = new File( value);
			if ( directory.exists())
				export_directory = value;
		}
	
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( ResourceManager.get_instance().get( "edit.object.dialog.export.files.title"));
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);

		if ( !export_directory.equals( "")) {
			fileChooser.setCurrentDirectory( new File( export_directory + "/../"));
			fileChooser.setSelectedFile( directory);
		}

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( _fileManager)) {
			directory = fileChooser.getSelectedFile();
			export_directory = directory.getAbsolutePath();
			Environment.get_instance().set( Environment._exportFilesDirectoryKey, export_directory);
			return directory;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#visible(java.io.File)
	 */
	public boolean visible(File file) {
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#adjust()
	 */
	protected void adjust() {
		_filePanel.adjust();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#on_setup_completed()
	 */
	public void on_setup_completed() {
		if ( _playerBase.is_multi()) {
			_fileTable.setEnabled( false);
			_filePanel.setEnabled( false);
		} else {
			_fileManager.on_setup_completed();
			_fileTable.on_setup_completed();
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#on_ok()
	 */
	public boolean on_ok() {
		if ( isVisible() && !confirm( false))
			return false;;

		_fileTable.on_ok();

		if ( _playerBase instanceof AgentObject)
			Observer.get_instance().on_update_agent_object( "file");
		else if ( _playerBase instanceof SpotObject)
			Observer.get_instance().on_update_spot_object( "file");

		set_property_to_environment_file();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#on_cancel()
	 */
	public void on_cancel() {
		set_property_to_environment_file();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#confirm(boolean)
	 */
	public boolean confirm(boolean fromTree) {
		if ( !isVisible())
			return true;

		//if ( 0 == _fileTable.getRowCount())
		//	return true;

		if ( !_filePanel.confirm( fromTree))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#confirm(int, soars.application.visualshell.object.player.base.object.base.ObjectBase, soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public ObjectBase confirm(int row, ObjectBase targetObjectBase, ObjectBase selectedObjectBase) {
		return _filePanel.confirm( row, targetObjectBase, selectedObjectBase);
	}
}
