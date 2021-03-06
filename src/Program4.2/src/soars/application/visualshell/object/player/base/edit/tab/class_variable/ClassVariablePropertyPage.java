/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab.class_variable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.SystemColor;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.common.arbitrary.ClassManager;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.object.base.ObjectBase;
import soars.application.visualshell.object.player.base.object.class_variable.ClassVariableObject;

/**
 * @author kurata
 *
 */
public class ClassVariablePropertyPage extends PropertyPageBase {

	/**
	 * 
	 */
	private JSplitPane _splitPane = null;

	/**
	 * 
	 */
	private ClassVariableTable _classVariableTable = null; 

	/**
	 * 
	 */
	private ClassVariablePanel _classVariablePanel = null; 

	/**
	 * 
	 */
	private ClassManager _classManager = null;

	/**
	 * @param title
	 * @param playerBase
	 * @param propertyPageMap
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public ClassVariablePropertyPage(String title, PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap, int index, Frame owner, Component parent) {
		super(title, playerBase, propertyPageMap, index, owner, parent);
	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Environment.get_instance().set(
			Environment._editObjectDialogClassVariablePropertyPageDividerLocationKey, String.valueOf( _splitPane.getDividerLocation()));
		Environment.get_instance().set(
			Environment._editObjectDialogClassManagerDividerLocationKey, String.valueOf( _classManager.getDividerLocation()));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#contains(java.lang.String)
	 */
	public boolean contains(String name) {
		if ( !Environment.get_instance().is_functional_object_enable())
			return false;

		return _classVariableTable.contains( name);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#contains(java.lang.String, java.lang.String)
	 */
	public boolean contains(String name, String number) {
		if ( !Environment.get_instance().is_functional_object_enable())
			return false;

		return _classVariableTable.contains( name, number);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#get()
	 */
	public String[] get() {
		return _classVariableTable.get();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.tab.TabbedPage#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		create_classVariableTable();
		create_classManager();


		setLayout( new BorderLayout());


		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));


		_splitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT);


		if ( !setup1())
			return false;


		if ( !setup2())
			return false;


		_splitPane.setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._editObjectDialogClassVariablePropertyPageDividerLocationKey, "250")));


		center_panel.add( _splitPane);


		insert_vertical_strut( center_panel);


		add( center_panel);


		adjust();


		return true;
	}

	/**
	 * 
	 */
	private void create_classVariableTable() {
		_classVariableTable = new ClassVariableTable( _playerBase, _propertyPageMap, this, _owner, _parent);
	}

	/**
	 * 
	 */
	private void create_classManager() {
		_classManager = new ClassManager( _owner, _parent);
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

		if ( !setup_classVariableTable( center_panel))
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
	private boolean setup_classVariableTable(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_classVariableTable.setup())
			return false;

		if ( _playerBase.is_multi())
			_classVariableTable.setEnabled( false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _classVariableTable);
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
		_classVariablePanel = new ClassVariablePanel( _playerBase, _propertyPageMap, _classVariableTable, _classManager, SystemColor.textText, _owner, _parent);
		if ( !_classVariablePanel.setup())
			return false;

		_classVariableTable._classVariablePanel = _classVariablePanel;

		_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"), _classVariablePanel);
		parent.add( _classVariablePanel);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#changeSelection(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public void changeSelection(ObjectBase objectBase) {
		changeSelection( ( ClassVariableObject)objectBase, true);
	}

	/**
	 * @param classVariableObject
	 * @param update_all
	 */
	public void changeSelection(ClassVariableObject classVariableObject, boolean update_all) {
		_classVariablePanel.update( classVariableObject);

		if ( !update_all)
			return;

		if ( null == classVariableObject)
			return;

		// ClassManagerの表示も更新する必要がある
		_classManager.select( classVariableObject, update_all);
	}

	/**
	 * @return
	 */
	private boolean setup2() {
		JPanel base_panel = new JPanel();
		base_panel.setLayout( new BorderLayout());


		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		if ( !setup_classManager( center_panel))
			return false;

		base_panel.add( center_panel);


		_splitPane.setBottomComponent( base_panel);


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_classManager(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_classManager.setup())
			return false;

		_classManager.setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._editObjectDialogClassManagerDividerLocationKey, "100")));

		panel.add( _classManager);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#adjust()
	 */
	protected void adjust() {
		_classVariablePanel.adjust();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#on_setup_completed()
	 */
	public void on_setup_completed() {
		if ( _playerBase.is_multi()) {
			_classVariableTable.setEnabled( false);
			_classVariablePanel.setEnabled( false);
		} else {
			_classManager.on_setup_completed();
			_classVariableTable.on_setup_completed();
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#on_ok()
	 */
	public boolean on_ok() {
		if ( isVisible() && !confirm( false))
			return false;;

		_classVariableTable.on_ok();

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

		//if ( 0 == _classVariableTable.getRowCount())
		//	return true;

		if ( !_classVariablePanel.confirm( fromTree))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#confirm(int, soars.application.visualshell.object.player.base.object.base.ObjectBase, soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public ObjectBase confirm(int row, ObjectBase targetObjectBase, ObjectBase selectedObjectBase) {
		return _classVariablePanel.confirm( row, targetObjectBase, selectedObjectBase);
	}
}
