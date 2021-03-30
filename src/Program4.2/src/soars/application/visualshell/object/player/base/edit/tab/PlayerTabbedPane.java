/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab;

import java.awt.Component;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.agent.AgentObject;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.edit.tab.class_variable.ClassVariablePropertyPage;
import soars.application.visualshell.object.player.base.edit.tab.extransfer.ExTransferPropertyPage;
import soars.application.visualshell.object.player.base.edit.tab.file.FilePropertyPage;
import soars.application.visualshell.object.player.base.edit.tab.image.ImagePropertyPage;
import soars.application.visualshell.object.player.base.edit.tab.initial_data_file.InitialDataFilePropertyPage;
import soars.application.visualshell.object.player.base.edit.tab.others.OthersPropertyPage;
import soars.application.visualshell.object.player.base.edit.tab.property.PropertyPage;
import soars.application.visualshell.object.player.base.edit.tab.simple.SimpleVariablePropertyPage;
import soars.application.visualshell.object.player.base.edit.tab.variable.VariablePropertyPage;
import soars.application.visualshell.object.player.base.edit.tree.PlayerTree;

/**
 * @author kurata
 *
 */
public class PlayerTabbedPane extends JPanel {

	/**
	 * 
	 */
	public PlayerTree _playerTree = null;

	/**
	 * 
	 */
	private PlayerBase _playerBase = null;

	/**
	 * 
	 */
	private List<PropertyPageBase> _propertyPageBases = new ArrayList<PropertyPageBase>();

	/**
	 * 
	 */
	static private int _agentSelectedIndex = 0;

	/**
	 * 
	 */
	static private int _spotSelectedIndex = 0;

	/**
	 * @param playerBase
	 */
	public PlayerTabbedPane(PlayerBase playerBase) {
		super();
		_playerBase = playerBase;
	}

	/**
	 * 
	 */
	private void get_selected_index() {
		if ( _playerBase instanceof AgentObject)
			_agentSelectedIndex = getSelectedIndex();
		else
			_spotSelectedIndex = getSelectedIndex();
	}

	/**
	 * @return
	 */
	private int getSelectedIndex() {
		for ( int i = 0; i < _propertyPageBases.size(); ++i) {
			if ( _propertyPageBases.get( i).isVisible())
				return i;
		}
		return 0;
	}

	/**
	 * 
	 */
	private void set_selected_index() {
		if ( _playerBase instanceof AgentObject)
			setSelectedIndex( _agentSelectedIndex);
		else
			setSelectedIndex( _spotSelectedIndex);
	}

	/**
	 * @param index
	 */
	private void setSelectedIndex(int index) {
		for ( PropertyPageBase propertyPageBase:_propertyPageBases)
			propertyPageBase.setVisible( propertyPageBase.equals( _propertyPageBases.get( index)));
	}

	/**
	 * 
	 */
	private void select() {
		for ( PropertyPageBase propertyPageBase:_propertyPageBases) {
			if ( propertyPageBase.isVisible())
				_playerTree.select( propertyPageBase);
		}
	}

	/**
	 * @param component
	 */
	public void select(Component component) {
		for ( PropertyPageBase propertyPageBase:_propertyPageBases)
			propertyPageBase.setVisible( component.equals( propertyPageBase));
	}

	/**
	 * @param component
	 * @return
	 */
	public boolean confirm(Component component) {
		PropertyPageBase propertyPageBase = ( PropertyPageBase)component;
		return propertyPageBase.confirm( true);
	}

	/**
	 * @param playerTree
	 * @param owner
	 * @param parent
	 * @return
	 */
	public boolean setup(PlayerTree playerTree, Frame owner, Component parent) {
		_playerTree = playerTree;

		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));

		Map<String, PropertyPageBase> propertyPageMap = new HashMap<String, PropertyPageBase>();

		PropertyPage propertyPage = new PropertyPage(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.basic.property"),
			_playerBase, propertyPageMap, _propertyPageBases.size(), owner, parent);
		if ( !create( propertyPage, propertyPageMap, "property"))
			return false;

		SimpleVariablePropertyPage simpleVariablePropertyPage = new SimpleVariablePropertyPage(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.simple.variable"),
			_playerBase, propertyPageMap, _propertyPageBases.size(), owner, parent);
		if ( !create( simpleVariablePropertyPage, propertyPageMap, "simple variable"))
			return false;

		VariablePropertyPage variablePropertyPage = new VariablePropertyPage(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.variable"),
			_playerBase, propertyPageMap, _propertyPageBases.size(), owner, parent);
		if ( !create( variablePropertyPage, propertyPageMap, "variable"))
			return false;

		if ( Environment.get_instance().is_functional_object_enable()) {
			ClassVariablePropertyPage classVariablePropertyPage = new ClassVariablePropertyPage(
				ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
				_playerBase, propertyPageMap, _propertyPageBases.size(), owner, parent);
			if ( !create( classVariablePropertyPage, propertyPageMap, "class variable"))
				return false;
		}

		FilePropertyPage filePropertyPage = new FilePropertyPage(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
			_playerBase, propertyPageMap, _propertyPageBases.size(), owner, parent);
		if ( !create( filePropertyPage, propertyPageMap, "file"))
			return false;

		InitialDataFilePropertyPage initialDataFilePropertyPage = new InitialDataFilePropertyPage(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.initial.data.file"),
			_playerBase, propertyPageMap, _propertyPageBases.size(), owner, parent);
		if ( !create( initialDataFilePropertyPage, propertyPageMap, "initial data file"))
			return false;

		if ( Environment.get_instance().is_exchange_algebra_enable() && Environment.get_instance().is_extransfer_enable()) {
			ExTransferPropertyPage exTransferPropertyPage = new ExTransferPropertyPage(
				ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"),
				_playerBase, propertyPageMap, _propertyPageBases.size(), owner, parent);
			if ( !create( exTransferPropertyPage, propertyPageMap, "extransfer"))
				return false;
		}

		ImagePropertyPage imagePropertyPage = new ImagePropertyPage(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.image"),
			_playerBase, propertyPageMap, _propertyPageBases.size(), owner, parent);
		if ( !create( imagePropertyPage, propertyPageMap, "image"))
			return false;

		OthersPropertyPage othersPropertyPage = new OthersPropertyPage(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.others"),
			_playerBase, propertyPageMap, _propertyPageBases.size(), owner, parent);
		if ( !create( othersPropertyPage, propertyPageMap, "others"))
			return false;

		_playerTree.expand();

		return true;
	}

	/**
	 * @param propertyPageBase
	 * @param propertyPageMap
	 * @param key
	 * @return
	 */
	private boolean create(PropertyPageBase propertyPageBase, Map<String, PropertyPageBase> propertyPageMap, String key) {
		if ( !propertyPageBase.create())
			return false;

		add( propertyPageBase);
		_playerTree.append( propertyPageBase);
		propertyPageMap.put( key, propertyPageBase);
		_propertyPageBases.add( propertyPageBase);
		return true;
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		for ( PropertyPageBase propertyPageBase:_propertyPageBases) {
			propertyPageBase.on_setup_completed();
			propertyPageBase.setVisible( propertyPageBase.equals( _propertyPageBases.get( 0)));
		}

		set_selected_index();
		select();
	}

	/**
	 * @param playerBase
	 * @return
	 */
	public boolean on_ok(PlayerBase playerBase) {
		//for ( PropertyPageBase propertyPageBase:_propertyPageBases) {
		// 参照されているものを後から保存しないとダメ！
		for ( int i = _propertyPageBases.size() - 1; 0 <= i; --i) {
			PropertyPageBase propertyPageBase = _propertyPageBases.get( i);
			if ( ( propertyPageBase instanceof PropertyPage)
				|| ( propertyPageBase instanceof ClassVariablePropertyPage)
				|| ( propertyPageBase instanceof FilePropertyPage)
				|| ( propertyPageBase instanceof ExTransferPropertyPage)
				|| ( propertyPageBase instanceof ImagePropertyPage)) {
				if ( !propertyPageBase.on_ok())
					return false;
			} else {
				if ( !playerBase.is_multi()) {
					if ( !propertyPageBase.on_ok())
						return false;
				}
			}
		}

		get_selected_index();

		return true;
	}

	/**
	 * 
	 */
	public void on_cancel() {
		for ( PropertyPageBase propertyPageBase:_propertyPageBases) {
			if ( ( propertyPageBase instanceof ClassVariablePropertyPage)
				|| ( propertyPageBase instanceof FilePropertyPage)
				|| ( propertyPageBase instanceof InitialDataFilePropertyPage)
				|| ( propertyPageBase instanceof ExTransferPropertyPage))
				propertyPageBase.on_cancel();
		}

		get_selected_index();
	}

	/**
	 * 
	 */
	public void windowClosing() {
		get_selected_index();
	}
}
