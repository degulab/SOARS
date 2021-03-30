/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab.base;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1;
import soars.application.visualshell.common.menu.basic1.RemoveAction;
import soars.application.visualshell.common.menu.basic2.CopyAction;
import soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2;
import soars.application.visualshell.common.menu.basic2.PasteAction;
import soars.application.visualshell.common.menu.basic2.SelectAllAction;
import soars.application.visualshell.common.swing.TableBase;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.agent.AgentObject;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.table.InitialValueTable;
import soars.application.visualshell.object.player.base.object.base.ObjectBase;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.observer.Observer;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class VariableTableBase extends TableBase implements IBasicMenuHandler1, IBasicMenuHandler2 {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private Map<String, List<ObjectBase>> __objectBasesMap = null;

	/**
	 * 
	 */
	protected String _kind = "";

	/**
	 * 
	 */
	protected PlayerBase _playerBase = null;

	/**
	 * 
	 */
	protected Map<String, PropertyPageBase> _propertyPageMap = null;

	/**
	 * 
	 */
	protected PropertyPageBase _propertyPageBase = null;

	/**
	 * 
	 */
	private JMenuItem _removeMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _copyMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _pasteMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _selectAllMenuItem = null;

	/**
	 * 
	 */
	protected int _columns = 0;

	/**
	 * 
	 */
	protected int _priviousRow = 0;

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null != __objectBasesMap)
				return;

			__objectBasesMap = new HashMap<String, List<ObjectBase>>();
			for ( String propertyPage:Constant._propertyPages)
				__objectBasesMap.put( propertyPage, new ArrayList<ObjectBase>());
			for ( String exceptionalPropertyPage:Constant._exceptionalPropertyPages)
				__objectBasesMap.put( exceptionalPropertyPage, new ArrayList<ObjectBase>());
		}
	}

	/**
	 * 
	 */
	static public void clear() {
		Collection<List<ObjectBase>> objectBasesList = __objectBasesMap.values();
		for ( List<ObjectBase> objectBases:objectBasesList)
			objectBases.clear();
	}

	/**
	 * @param kind
	 * @param playerBase
	 * @param propertyPageMap
	 * @param propertyPageBase
	 * @param columns
	 * @param owner
	 * @param parent
	 */
	public VariableTableBase(String kind, PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap, PropertyPageBase propertyPageBase, int columns, Frame owner, Component parent) {
		super(owner, parent);
		_kind = kind;
		_playerBase = playerBase;
		_propertyPageMap = propertyPageMap;
		_propertyPageBase = propertyPageBase;
		_columns = columns;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#setup(boolean)
	 */
	public boolean setup(boolean popupMenu) {
		if ( !super.setup(popupMenu))
			return false;

		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);
		setFillsViewportHeight( true);

		return true;
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean contains(String name) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == objectBase)
				continue;

			if ( name.equals( objectBase._name))
				return true;
		}
		return false;
	}

	/**
	 * @param kind
	 * @param name
	 * @return
	 */
	public boolean other_objectBase_has_this_name(String kind, String name) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == objectBase || objectBase._kind.equals( kind))
				continue;

			if ( name.equals( objectBase._name))
				return true;
		}
		return false;
	}

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean contains(String name, String number) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == objectBase)
				continue;

			if ( SoarsCommonTool.has_same_name( name, number, objectBase._name))
				return true;
		}
		return false;
	}

	/**
	 * @param objectBase
	 * @return
	 */
	public boolean contains(ObjectBase objectBase) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectBase ob = ( ObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( ob.contains( objectBase)) {
				String[] message = new String[] {
					( ( _playerBase instanceof AgentObject) ? "Agent" : "Spot"),
					"name = " + _playerBase._name,
					objectBase._kind + " " + ob._name + " uses " + objectBase._name
				};

				WarningManager.get_instance().add( message);

				return true;
			}
		}
		return false;
	}

	/**
	 * @param type
	 * @param head_name
	 * @param ranges
	 * @return
	 */
	public boolean can_adjust_name(String type, String head_name, Vector ranges) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == objectBase)
				continue;

			if ( !objectBase.can_adjust_name( type, head_name, ranges, _playerBase))
				return false;
		}
		return true;
	}

	/**
	 * @param type
	 * @param head_name
	 * @param ranges
	 * @param new_head_name
	 * @param new_ranges
	 * @return
	 */
	public boolean can_adjust_name(String type, String head_name, Vector ranges, String new_head_name, Vector new_ranges) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == objectBase)
				continue;

			if ( !objectBase.can_adjust_name( type, head_name, ranges, new_head_name, new_ranges, _playerBase))
				return false;
		}
		return true;
	}

	/**
	 * @param type
	 * @param new_name
	 * @param original_name
	 * @param head_name
	 * @param ranges
	 * @param new_head_name
	 * @param new_ranges
	 */
	public void update_name_and_number(String type, String new_name, String original_name, String head_name, Vector ranges, String new_head_name, Vector new_ranges) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == objectBase)
				continue;

			objectBase.update_name_and_number( type, new_name, original_name, head_name, ranges, new_head_name, new_ranges);
		}
	}

	/**
	 * @param type
	 * @param name
	 * @param newName
	 * @return
	 */
	public boolean update_object_name(String type, String name, String new_name) {
		boolean result = false;
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == objectBase)
				continue;

			if ( objectBase.update_object_name( type, name, new_name))
				result = true;
		}
		return result;
	}

	/**
	 * @return
	 */
	public String[] get() {
		List<String> names = new ArrayList<String>();
		for ( int i = 0; i < getRowCount(); ++i) {
			ObjectBase objectBase = ( ObjectBase)getValueAt( i, 0);
			if ( null == objectBase)
				continue;

			names.add( objectBase._name);
		}
		return names.toArray( new String[ 0]);
	}

	/**
	 * @param kind
	 * @return
	 */
	public String[] get(String kind) {
		List<String> names = new ArrayList<String>();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == objectBase)
				continue;

			if ( ObjectBase.is_target( kind, objectBase))
				names.add( objectBase._name);
		}
		return names.toArray( new String[ 0]);
	}

	/**
	 * @param kind
	 * @param name
	 * @return
	 */
	public ObjectBase get(String kind, String name) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == objectBase)
				continue;

			if ( ObjectBase.is_target( kind, objectBase) && objectBase._name.equals( name))
				return objectBase;
		}
		return null;
	}

	/**
	 * @param objectBase
	 * @return
	 */
	public int get(ObjectBase objectBase) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectBase ob = ( ObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == ob)
				continue;

			if ( ob.equals( objectBase))
				return i;
		}
		return -1;
	}

	/**
	 * @param objectMap
	 * @param defaultTableModel
	 * @return
	 */
	protected boolean setup(TreeMap objectMap, DefaultTableModel defaultTableModel) {
		Collection<ObjectBase> objectBases = objectMap.values();
		for ( ObjectBase objectBase:objectBases) {
			ObjectBase ob = ObjectBase.create( objectBase);
			if ( null == ob)
				return false;

			insert( ob, defaultTableModel, false);
		}
		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		if ( _playerBase.is_multi())
			return;

		_userInterface = new UserInterface();
		_popupMenu = new JPopupMenu();

		_removeMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "common.popup.menu.remove.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.stroke"));

		_popupMenu.addSeparator();

		_copyMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.copy.menu"),
			new CopyAction( ResourceManager.get_instance().get( "common.popup.menu.copy.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.copy.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.copy.stroke"));
		_pasteMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.paste.menu"),
			new PasteAction( ResourceManager.get_instance().get( "common.popup.menu.paste.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.paste.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.paste.stroke"));

		_popupMenu.addSeparator();

		_selectAllMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.select.all.menu"),
			new SelectAllAction( ResourceManager.get_instance().get( "common.popup.menu.select.all.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.select.all.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.select.all.stroke"));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PlayerTableBase#setup_key_event()
	 */
	protected void setup_key_event() {
		super.setup_key_event();

		Action deleteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_remove( null);
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0), "delete");
		getActionMap().put( "delete", deleteAction);


		Action backSpaceAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_remove( null);
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0), "backspace");
		getActionMap().put( "backspace", backSpaceAction);


		Action copyAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_copy();
			}
		};
		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "copy");
		getActionMap().put( "copy", copyAction);


		Action cutAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_cut();
			}
		};
		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "cut");
		getActionMap().put( "cut", cutAction);


		Action pasteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_paste();
			}
		};
		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "paste");
		getActionMap().put( "paste", pasteAction);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#on_mouse_right_up(java.awt.Point)
	 */
	public void on_mouse_right_up(Point point) {
		if ( null == _userInterface)
			return;

		if ( 0 == getRowCount()) {
			_removeMenuItem.setEnabled( false);
			_copyMenuItem.setEnabled( false);
			_selectAllMenuItem.setEnabled( false);
		} else {
			int row = rowAtPoint( point);
			int column = columnAtPoint( point);
			if ( ( 0 <= row && getRowCount() > row)
				&& ( 0 <= column && getColumnCount() > column)) {
				int[] rows = getSelectedRows();
				boolean contains = ( 0 <= Arrays.binarySearch( rows, row));
				_removeMenuItem.setEnabled( contains);
				_copyMenuItem.setEnabled( contains);
			} else {
				_removeMenuItem.setEnabled( false);
				_copyMenuItem.setEnabled( false);
			}

			_selectAllMenuItem.setEnabled( true);
		}

		_pasteMenuItem.setEnabled( !__objectBasesMap.get( _kind).isEmpty());

		_popupMenu.show( this, point.x, point.y);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_redo(java.awt.event.ActionEvent)
	 */
	public void on_redo(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_undo(java.awt.event.ActionEvent)
	 */
	public void on_undo(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_copy(java.awt.event.ActionEvent)
	 */
	public void on_copy(ActionEvent actionEvent) {
		on_copy();
	}

	/**
	 * 
	 */
	protected void on_copy() {
		int[] rows = getSelectedRows();
		if ( 1 > rows.length)
			return;

		InitialValueTable.clear();
		/*__objectBasesMap.get( _kind).*/clear();
		Arrays.sort( rows);
		for ( int i = 0; i < rows.length; ++i)
			__objectBasesMap.get( _kind).add( ObjectBase.create( ( ObjectBase)getValueAt( rows[ i], 0)));
	}

	/**
	 * 
	 */
	protected void on_cut() {
		// これは無理！
		// 削除出来ないものが含まれているから
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_paste(java.awt.event.ActionEvent)
	 */
	public void on_paste(ActionEvent actionEvent) {
		on_paste();
	}

	/**
	 * 
	 */
	protected void on_paste() {
		WarningManager.get_instance().cleanup();

		// 存在しない変数を参照しているコレクション変数を省く
		List<ObjectBase> objectBases = new ArrayList<ObjectBase>();
		for ( ObjectBase objectBase:__objectBasesMap.get( _kind)) {
			PanelBase panelBase = _propertyPageBase._panelBaseMap.get( PropertyPageBase._nameMap.get( objectBase._kind));
			if ( null == panelBase)
				continue;

			if ( !panelBase.can_paste( objectBase, __objectBasesMap.get( _kind))) {
				String[] message = new String[] {
					PropertyPageBase._nameMap.get( objectBase._kind),
					"name = " + objectBase._name
				};

				WarningManager.get_instance().add( message);
				continue;
			}

			objectBases.add( objectBase);
		}

		// １回目で張り付けられると判断されたものだけを対象にして再度行う
		// １回目で省かれたコレクション変数を参照しているコレクション変数が省かれる
		int option = -1;
		List<ObjectBase> obs = new ArrayList<ObjectBase>();
		for ( ObjectBase objectBase:objectBases) {
			PanelBase panelBase = _propertyPageBase._panelBaseMap.get( PropertyPageBase._nameMap.get( objectBase._kind));
			if ( null == panelBase)
				continue;

			if ( !panelBase.can_paste( objectBase, objectBases)) {
				String[] message = new String[] {
					PropertyPageBase._nameMap.get( objectBase._kind),
					"name = " + objectBase._name
				};

				WarningManager.get_instance().add( message);
				continue;
			}

			ObjectBase ob = get( objectBase._kind, objectBase._name);
			if ( null != ob)	// update
				if ( ob.equals( objectBase)) {
					obs.add( ob);
					continue;
				} else {
					if ( 3 == option)		// 全ていいえ
						continue;
					else if ( 2 == option) {		// 全てはい
						ob.copy( objectBase);
						obs.add( ob);
						continue;
					} else {
						option = showOptionDialog( objectBase);
						if ( 0 == option || 2 == option) {
							ob.copy( objectBase);
							obs.add( ob);
						}
						continue;
					}
				}
			else {	// append
				ob = ObjectBase.create( objectBase);
				append( ob);
				obs.add( ob);
			}
		}

		List<Integer> list = new ArrayList<Integer>();
		for ( ObjectBase objectBase:obs) {
			for ( int i = 0; i < getRowCount(); ++i) {
				if ( objectBase == getValueAt( i, 0)) {
					list.add( i);
					break;
				}
			}
		}
		select( Tool.get_array( list));

		if ( !WarningManager.get_instance().isEmpty()) {
			WarningDlg1 warningDlg1 = new WarningDlg1(
				_owner,
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message6"),
				_parent);
			warningDlg1.do_modal();
		}
	}

	/**
	 * @param indices
	 */
	private void select(int[] indices) {
		if ( 0 == indices.length)
			return;

		clearSelection();
		for ( int index:indices) {
			addRowSelectionInterval( index, index);
			Rectangle rect = getCellRect( index, 0, true);
			scrollRectToVisible( rect);
		}

		if ( 1 == indices.length) {
			_priviousRow = indices[ 0];
			_propertyPageBase.changeSelection( ( ObjectBase)getValueAt( indices[ 0], 0));
		} else {
			_priviousRow = -1;
			_propertyPageBase.changeSelection( null);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_select_all(java.awt.event.ActionEvent)
	 */
	public void on_select_all(ActionEvent actionEvent) {
		selectAll();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_deselect_all(java.awt.event.ActionEvent)
	 */
	public void on_deselect_all(ActionEvent actionEvent) {
	}

	/**
	 * @param objectBase
	 * @return
	 */
	private int showOptionDialog(ObjectBase objectBase) {
		String[] overwrite_options = new String[] {
			ResourceManager.get_instance().get( "dialog.yes"),
			ResourceManager.get_instance().get( "dialog.no"),
			ResourceManager.get_instance().get( "dialog.yes.to.all"),
			ResourceManager.get_instance().get( "dialog.no.to.all")};
		return JOptionPane.showOptionDialog(
			getParent(),
			PropertyPageBase._nameMap.get( objectBase._kind) + " : " + objectBase._name + "\n"
				+ ResourceManager.get_instance().get( "edit.object.dialog.variable.table.confirm.overwrite.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.INFORMATION_MESSAGE,
			null,
			overwrite_options,
			overwrite_options[ 0]);
	}

	/**
	 * @param objectBase
	 */
	public void append(ObjectBase objectBase) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		insert( objectBase, defaultTableModel, true);
		if ( !_playerBase._gis.equals( "")) {
			// TODO GISスポットなら、変数をすぐ追加し、同じIDを持つ他の全てのGISスポットにも追加する
			List<PlayerBase> playerBases = new ArrayList<PlayerBase>();
			LayerManager.get_instance().get_gis_spots( playerBases, _playerBase._gis);
			for ( PlayerBase playerBase:playerBases)
				playerBase.append_object( ObjectBase.create( objectBase));
			Observer.get_instance().on_update_object( objectBase._kind);
			Observer.get_instance().on_update_playerBase( true);
			Observer.get_instance().modified();
		}
	}

	/**
	 * @param row
	 * @param originalObjectBase
	 * @param selection
	 */
	public void update(int row, ObjectBase originalObjectBase, boolean selection) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( row, 0);
		if ( null == objectBase)
			return;

		if ( !objectBase._name.equals( originalObjectBase._name)) {
			defaultTableModel.removeRow( row);
			insert( objectBase, defaultTableModel, selection);
			clear();	// 名前が変わったからキャッシュをクリア
			InitialValueTable.clear();
		}

		if ( !_playerBase._gis.equals( "")) {
			// TODO GISスポットなら、同じIDを持つ他の全てのGISスポットについても初期値を変更するかどうか？確認する
			if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
				_parent,
				ResourceManager.get_instance().get( "edit.object.dialog.variable.table.confirm.gis.update.initial.value.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.YES_NO_OPTION)) {
				// 初期値を全て同じにする
				List<PlayerBase> playerBases = new ArrayList<PlayerBase>();
				LayerManager.get_instance().get_gis_spots( playerBases, _playerBase._gis);
				for ( PlayerBase playerBase:playerBases)
					playerBase.append_object( ObjectBase.create( objectBase));

				Observer.get_instance().on_update_object( objectBase._kind);
				Observer.get_instance().on_update_playerBase( true);
				Observer.get_instance().modified();

				clear();
				InitialValueTable.clear();
			}
		}

		repaint();
	}

	/**
	 * @param objectBase
	 * @param defaultTableModel
	 * @param selection
	 */
	private void insert(ObjectBase objectBase, DefaultTableModel defaultTableModel, boolean selection) {
		Object[] objects = new Object[ _columns];
		for ( int i = 0; i < objects.length; ++i)
			objects[ i] = objectBase;

		int index = 0;
		boolean insert = false;
		if ( 0 == defaultTableModel.getRowCount()) {
			defaultTableModel.addRow( objects);
			insert = true;
		} else {
			for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
				ObjectBase ob = ( ObjectBase)defaultTableModel.getValueAt( i, 0);
				if ( null == ob)
					return;

				if ( 0 > compare( objectBase, ob)) {
					defaultTableModel.insertRow( i, objects);
					index = i;
					insert = true;
					break;
				}
			}
		}

		if ( !insert) {
			defaultTableModel.addRow( objects);
			index = defaultTableModel.getRowCount() - 1;
		}

		if ( selection) {
			setRowSelectionInterval( index, index);
			Rectangle rect = getCellRect( index, 0, true);
			scrollRectToVisible( rect);
			_propertyPageBase.changeSelection( objectBase);
			_priviousRow = index;
		}
	}

	/**
	 * @param objectBase0
	 * @param objectBase1
	 * @return
	 */
	protected int compare(ObjectBase objectBase0, ObjectBase objectBase1) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PlayerTableBase#on_remove(java.awt.event.ActionEvent)
	 */
	public void on_remove(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( 1 > rows.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount())
			return;

		WarningManager.get_instance().cleanup();

		boolean result = true;
		for ( int i = 0; i < rows.length; ++i) {
			ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( rows[ i], 0);
			if ( !can_remove_selected_objects( objectBase))
				result = false;
		}

		if ( !result) {
			if ( !WarningManager.get_instance().isEmpty()) {
				WarningDlg1 warningDlg1 = new WarningDlg1(
					_owner,
					ResourceManager.get_instance().get( "warning.dialog1.title"),
					ResourceManager.get_instance().get( "warning.dialog1.message2"),
					_parent);
				warningDlg1.do_modal();
			}
			return;
		}

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			_parent,
			ResourceManager.get_instance().get( "edit.object.dialog.variable.table.confirm.remove.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {

			Arrays.sort( rows);
			if ( _playerBase._gis.equals( "")) {
				for ( int i = rows.length - 1; i >= 0; --i)
					defaultTableModel.removeRow( rows[ i]);
			} else {
				for ( int i = rows.length - 1; i >= 0; --i) {
					// TODO GISスポットなら、変数をすぐ削除し、同じIDを持つ他の全てのGISスポットの変数も削除する
					List<PlayerBase> playerBases = new ArrayList<PlayerBase>();
					LayerManager.get_instance().get_gis_spots( playerBases, _playerBase._gis);
					ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( rows[ i], 0);
					for ( PlayerBase playerBase:playerBases)
						playerBase.remove_object( ObjectBase.create( objectBase));

					Observer.get_instance().on_update_object( objectBase._kind);
					defaultTableModel.removeRow( rows[ i]);
				}
				Observer.get_instance().on_update_playerBase( true);
				Observer.get_instance().modified();
			}

			if ( 0 == getRowCount()) {
				_propertyPageBase.changeSelection( null);
				_priviousRow = 0;
			} else {
				int select = ( rows[ 0] < defaultTableModel.getRowCount()) ? rows[ 0] : defaultTableModel.getRowCount() - 1;
				setRowSelectionInterval( select, select);
				_priviousRow = select;
				_propertyPageBase.changeSelection( ( ObjectBase)getValueAt( select, 0));
			}

			on_remove();

			clear();	// 削除されたからキャッシュをクリア
			InitialValueTable.clear();
		}
	}

	/**
	 * @param objectBase
	 * @return
	 */
	protected boolean can_remove_selected_objects(ObjectBase objectBase) {
		boolean result1 = can_remove_selected_objects_old( objectBase);
		boolean result2 = can_remove_selected_objects_new( objectBase);
		return ( result1 && result2);
	}

	/**
	 * @param objectBase
	 * @return
	 */
	protected boolean can_remove_selected_objects_old(ObjectBase objectBase) {
		// TODO 従来のもの
		if ( ( _playerBase instanceof SpotObject) || ( ( _playerBase instanceof AgentObject)
			&& !LayerManager.get_instance().other_agents_have_same_object_name( objectBase._kind, objectBase._name, ( AgentObject)_playerBase))) {
			if ( _playerBase._gis.equals( "")) {
				boolean otherSpotsHaveSameObjectName
					= ( _playerBase instanceof SpotObject)
							? LayerManager.get_instance().other_spots_have_same_object_name( objectBase._kind, objectBase._name, ( SpotObject)_playerBase)
							: false;

				String headName = null;
				Vector ranges = null;
				if ( _playerBase instanceof SpotObject) {
					headName = SoarsCommonTool.separate( _playerBase._name);
					String headNumber = _playerBase._name.substring( headName.length());
					ranges = SoarsCommonTool.get_ranges( headNumber, _playerBase._number);
				}

				if ( !LayerManager.get_instance().can_remove(
					objectBase._kind, objectBase._name, otherSpotsHaveSameObjectName, headName, ranges, false))
					return false;
			} else {
				// GISスポットの場合
				List<PlayerBase> playerBases = new ArrayList<PlayerBase>();
				LayerManager.get_instance().get_gis_spots( playerBases, _playerBase._gis);
				for ( PlayerBase playerBase:playerBases) {
					if ( playerBase.contains_as_initial_value( objectBase))
						return false;
				}

				boolean otherSpotsHaveSameObjectName
					= ( _playerBase instanceof SpotObject)
							? LayerManager.get_instance().other_spots_have_same_object_name( objectBase._kind, objectBase._name, _playerBase._gis, ( SpotObject)_playerBase)
							: false;

				for ( PlayerBase playerBase:playerBases) {
					String headName = null;
					Vector ranges = null;
					if ( playerBase instanceof SpotObject) {
						headName = SoarsCommonTool.separate( playerBase._name);
						String headNumber = playerBase._name.substring( headName.length());
						ranges = SoarsCommonTool.get_ranges( headNumber, playerBase._number);
					}
					if ( !LayerManager.get_instance().can_remove(
						objectBase._kind, objectBase._name, otherSpotsHaveSameObjectName, headName, ranges, false))
						return false;
				}
			}
		}

		return true;
	}

	/**
	 * @param objectBase
	 * @return
	 */
	protected boolean can_remove_selected_objects_new(ObjectBase objectBase) {
		// TODO これからはこちらに移行してゆく
		if ( ( _playerBase instanceof SpotObject) || ( _playerBase instanceof AgentObject)) {
			String player = null;
			if ( _playerBase instanceof AgentObject)
				player = "agent";
			else if ( _playerBase instanceof SpotObject)
				player = "spot";
			else
				return false;

			if ( _playerBase._gis.equals( "")) {
				boolean otherObjectBasesHaveSameObjectName
					= ( player.equals( "agent")
							? LayerManager.get_instance().other_agents_have_same_object_name( objectBase._kind, objectBase._name, ( AgentObject)_playerBase)
							: LayerManager.get_instance().other_spots_have_same_object_name( objectBase._kind, objectBase._name, ( SpotObject)_playerBase));

				String headName = SoarsCommonTool.separate( _playerBase._name);
				String headNumber = _playerBase._name.substring( headName.length());
				Vector ranges = SoarsCommonTool.get_ranges( headNumber, _playerBase._number);

				if ( !LayerManager.get_instance().can_remove(
					player,
					objectBase._kind,
					objectBase._name,
					otherObjectBasesHaveSameObjectName,
					headName,
					ranges,
					false))
					return false;
			} else {
				// GISスポットの場合
				List<PlayerBase> playerBases = new ArrayList<PlayerBase>();
				LayerManager.get_instance().get_gis_spots( playerBases, _playerBase._gis);
				for ( PlayerBase playerBase:playerBases) {
					if ( playerBase.contains_as_initial_value( objectBase))
						return false;
				}

				boolean otherObjectBasesHaveSameObjectName
					= ( player.equals( "agent")
							? LayerManager.get_instance().other_agents_have_same_object_name( objectBase._kind, objectBase._name, ( AgentObject)_playerBase)
							: LayerManager.get_instance().other_spots_have_same_object_name( objectBase._kind, objectBase._name, _playerBase._gis, ( SpotObject)_playerBase));

				for ( PlayerBase playerBase:playerBases) {
					String headName = SoarsCommonTool.separate( playerBase._name);
					String headNumber = playerBase._name.substring( headName.length());
					Vector ranges = SoarsCommonTool.get_ranges( headNumber, playerBase._number);
					
					if ( !LayerManager.get_instance().can_remove(
						player,
						objectBase._kind,
						objectBase._name,
						otherObjectBasesHaveSameObjectName,
						headName,
						ranges,
						false))
						return false;
				}
			}
		}

		return true;
	}

	/**
	 * 
	 */
	protected void on_remove() {
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		super.valueChanged(e);
		int[] rows = getSelectedRows();
		if ( 1 != rows.length) {
			_propertyPageBase.changeSelection( null);
			_priviousRow = -1;
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#changeSelection(int, int, boolean, boolean)
	 */
	public void changeSelection(int arg0, int arg1, boolean arg2, boolean arg3) {
		// ここがキモ！
		//System.out.println( _priviousRow + ", " + arg0);
		if ( arg3) {
			// 選択が増加する場合
			int[] rows = getSelectedRows();
			if ( 1 == rows.length && rows[ 0] == arg0) {	// 実は選択は１つ
				_propertyPageBase.changeSelection( ( ObjectBase)getValueAt( arg0, 0));
				_priviousRow = arg0;
			} else {	// これから選択が２つ以上になる
				_propertyPageBase.changeSelection( null);
				_priviousRow = -1;
			}
			super.changeSelection(arg0, arg1, arg2, arg3);
		} else if ( arg2) {
			// 選択が減少する場合
			int[] rows = getSelectedRows();
			if ( 2 == rows.length) {	// これから選択が１つになる
				_propertyPageBase.changeSelection( ( ObjectBase)getValueAt( ( arg0 == rows[ 0]) ? rows[ 1] : arg0, 0));
				_priviousRow = ( arg0 == rows[ 0]) ? rows[ 1] : arg0;
			} else {	// この選択が解除されても２つ以上が選択された状態となる
				_propertyPageBase.changeSelection( null);
				_priviousRow = -1;
			}
			super.changeSelection(arg0, arg1, arg2, arg3);
		} else {
			ObjectBase objectBase = ( 0 > _priviousRow) ? null : ( ObjectBase)getValueAt( _priviousRow, 0);
			objectBase = ( ObjectBase)_propertyPageBase.confirm( _priviousRow, objectBase, ( ObjectBase)getValueAt( arg0, 0));
			if ( null == objectBase) {
				// 選択状態を変えない=super.changeSelection( ... )を呼ばない
				//System.out.println( "debug3 : " + _priviousRow + ", " + arg0 + ", " + arg2 + ", " + arg3);
				return;
			} else {
				int index = getIndex( objectBase);
				if ( 0 > index) {
					// これは起こり得ない筈だが念の為
					_propertyPageBase.changeSelection( ( ObjectBase)getValueAt( arg0, 0));
					_priviousRow = arg0;
					super.changeSelection(arg0, arg1, arg2, arg3);
					//System.out.println( "debug4 : " + _priviousRow + ", " + arg0 + ", " + arg2 + ", " + arg3);
				} else {
					// 返されたオブジェクトを選択する
					_propertyPageBase.changeSelection( objectBase);
					_priviousRow = index;
					super.changeSelection(index, arg1, arg2, arg3);
					//System.out.println( "debug5 : " + _priviousRow + ", " + arg0 + ", " + arg2 + ", " + arg3);
				}
			}
		}
//		super.changeSelection(arg0, arg1, arg2, arg3);
//		int[] rows = getSelectedRows();
//		_variablePropertyPage.changeSelection( ( null == rows || 1 != rows.length) ? null : ( ObjectBase)getValueAt( arg0, 0));
	}

	/**
	 * @param objectBase
	 */
	public void select(ObjectBase objectBase) {
		int index = getIndex( objectBase);
		if ( 0 > index)
			return;	// これは起こり得ない筈だが念の為

		setRowSelectionInterval( index, index);
		_priviousRow = index;
		_propertyPageBase.changeSelection( objectBase);
	}

	/**
	 * @param objectBase
	 * @return
	 */
	protected int getIndex(ObjectBase objectBase) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectBase ob = ( ObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == ob)
				continue;

			if ( ob.equals( objectBase))
				return i;
		}
		return -1;
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		if ( 0 < getRowCount()) {
			setRowSelectionInterval( 0, 0);
			_propertyPageBase.changeSelection( ( ObjectBase)getValueAt( 0, 0));
		}
	}
	/**
	 * 
	 */
	public void on_ok() {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_append(java.awt.event.ActionEvent)
	 */
	public void on_append(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_edit(java.awt.event.ActionEvent)
	 */
	public void on_edit(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_insert(java.awt.event.ActionEvent)
	 */
	public void on_insert(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_cut(java.awt.event.ActionEvent)
	 */
	public void on_cut(ActionEvent actionEvent) {
	}
}
