/**
 * 
 */
package soars.application.visualshell.object.gis.edit.variable.table;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1;
import soars.application.visualshell.common.menu.basic1.RemoveAction;
import soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2;
import soars.application.visualshell.common.menu.basic2.SelectAllAction;
import soars.application.visualshell.common.swing.TableBase;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.edit.GisDataFrame;
import soars.application.visualshell.object.gis.edit.variable.object.base.ObjectBase;
import soars.application.visualshell.object.gis.edit.variable.object.base.SimpleVariableObject;
import soars.application.visualshell.object.gis.edit.variable.object.keyword.KeywordObject;
import soars.application.visualshell.object.gis.edit.variable.object.number.NumberObject;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;
import soars.common.utility.tool.sort.StringNumberComparator;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class VariableTable extends TableBase implements IBasicMenuHandler1, IBasicMenuHandler2 {

	/**
	 * 
	 */
	private GisDataFrame _gisDataFrame = null;

	/**
	 * 
	 */
	private JMenuItem _removeMenuItem = null;

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
	 * @param gisDataFrame
	 * @param owner
	 * @param parent
	 */
	public VariableTable(GisDataFrame gisDataFrame, Frame owner, Component parent) {
		super(owner, parent);
		_gisDataFrame = gisDataFrame;
	}

	/**
	 * @return
	 */
	public List<SimpleVariableObject> get() {
		List<SimpleVariableObject> simpleVariableObjects = new ArrayList<SimpleVariableObject>();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i)
			simpleVariableObjects.add( ( SimpleVariableObject)defaultTableModel.getValueAt( i, 0));
		return simpleVariableObjects;
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
	 * @return
	 */
	public boolean setup() {
		if ( !super.setup(true))
			return false;

		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);
		setFillsViewportHeight( true);

		setAutoResizeMode( AUTO_RESIZE_OFF);


		JTableHeader tableHeader = getTableHeader();
		StandardTableHeaderRenderer standardTableHeaderRenderer = new StandardTableHeaderRenderer();

		tableHeader.setDefaultRenderer( standardTableHeaderRenderer);

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 5);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.table.header.kind"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.table.header.name"));
		defaultTableColumnModel.getColumn( 2).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.table.header.initial.value"));
		defaultTableColumnModel.getColumn( 3).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.table.header.type"));
		defaultTableColumnModel.getColumn( 4).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.table.header.comment"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 2).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 3).setPreferredWidth( 100);
		defaultTableColumnModel.getColumn( 4).setPreferredWidth( 2000);

		for ( int i = 0; i < 5; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new VariableTableRowRenderer());
		}

//		if ( !_playerBase.is_multi()) {
//			if ( !setup( _playerBase._objectMapMap.get( "probability"), defaultTableModel))
//				return false;
//			if ( !setup( _playerBase._objectMapMap.get( "keyword"), defaultTableModel))
//				return false;
//			if ( !setup( _playerBase._objectMapMap.get( "number object"), defaultTableModel))
//				return false;
//			if ( !setup( _playerBase._objectMapMap.get( "role variable"), defaultTableModel))
//				return false;
//			if ( !setup( _playerBase._objectMapMap.get( "time variable"), defaultTableModel))
//				return false;
//			if ( !setup( _playerBase._objectMapMap.get( "spot variable"), defaultTableModel))
//				return false;
//
//			if ( 0 < defaultTableModel.getRowCount())
//				setRowSelectionInterval( 0, 0);
//		}

//		setup_popup_menu();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		_userInterface = new UserInterface();
		_popupMenu = new JPopupMenu();

		_removeMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "common.popup.menu.remove.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.stroke"));

//		_popupMenu.addSeparator();
//
//		_copyMenuItem = _userInterface.append_popup_menuitem(
//			_popupMenu,
//			ResourceManager.get_instance().get( "common.popup.menu.copy.menu"),
//			new CopyAction( ResourceManager.get_instance().get( "common.popup.menu.copy.menu"), this),
//			ResourceManager.get_instance().get( "common.popup.menu.copy.mnemonic"),
//			ResourceManager.get_instance().get( "common.popup.menu.copy.stroke"));
//		_pasteMenuItem = _userInterface.append_popup_menuitem(
//			_popupMenu,
//			ResourceManager.get_instance().get( "common.popup.menu.paste.menu"),
//			new PasteAction( ResourceManager.get_instance().get( "common.popup.menu.paste.menu"), this),
//			ResourceManager.get_instance().get( "common.popup.menu.paste.mnemonic"),
//			ResourceManager.get_instance().get( "common.popup.menu.paste.stroke"));

		_popupMenu.addSeparator();

		_selectAllMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.select.all.menu"),
			new SelectAllAction( ResourceManager.get_instance().get( "common.popup.menu.select.all.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.select.all.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.select.all.stroke"));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#setup_key_event()
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


//		Action copyAction = new AbstractAction() {
//			public void actionPerformed(ActionEvent e) {
//				on_copy();
//			}
//		};
//		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "copy");
//		getActionMap().put( "copy", copyAction);
//
//
//		Action cutAction = new AbstractAction() {
//			public void actionPerformed(ActionEvent e) {
//				on_cut();
//			}
//		};
//		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "cut");
//		getActionMap().put( "cut", cutAction);
//
//
//		Action pasteAction = new AbstractAction() {
//			public void actionPerformed(ActionEvent e) {
//				on_paste();
//			}
//		};
//		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "paste");
//		getActionMap().put( "paste", pasteAction);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#on_mouse_right_up(java.awt.Point)
	 */
	protected void on_mouse_right_up(Point point) {
		if ( null == _userInterface)
			return;

		if ( 0 == getRowCount()) {
			_removeMenuItem.setEnabled( false);
			_selectAllMenuItem.setEnabled( false);
		} else {
			int row = rowAtPoint( point);
			int column = columnAtPoint( point);
			if ( ( 0 <= row && getRowCount() > row)
				&& ( 0 <= column && getColumnCount() > column)) {
				int[] rows = getSelectedRows();
				boolean contains = ( 0 <= Arrays.binarySearch( rows, row));
				_removeMenuItem.setEnabled( contains);
			} else {
				_removeMenuItem.setEnabled( false);
			}

			_selectAllMenuItem.setEnabled( true);
		}

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
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_cut(java.awt.event.ActionEvent)
	 */
	public void on_cut(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_paste(java.awt.event.ActionEvent)
	 */
	public void on_paste(ActionEvent actionEvent) {
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
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_remove(java.awt.event.ActionEvent)
	 */
	public void on_remove(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( 1 > rows.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount())
			return;

//		WarningManager.get_instance().cleanup();
//
//		boolean result = true;
//		for ( int i = 0; i < rows.length; ++i) {
//			ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( rows[ i], 0);
//			if ( !can_remove_selected_objects( objectBase))
//				result = false;
//		}
//
//		if ( !result) {
//			if ( !WarningManager.get_instance().isEmpty()) {
//				WarningDlg1 warningDlg1 = new WarningDlg1(
//					_owner,
//					ResourceManager.get_instance().get( "warning.dialog1.title"),
//					ResourceManager.get_instance().get( "warning.dialog1.message2"),
//					_parent);
//				warningDlg1.do_modal();
//			}
//			return;
//		}

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			_parent,
			ResourceManager.get_instance().get( "edit.object.dialog.variable.table.confirm.remove.message"),
			ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
			JOptionPane.YES_NO_OPTION)) {

			Arrays.sort( rows);
			for ( int i = rows.length - 1; i >= 0; --i)
				defaultTableModel.removeRow( rows[ i]);

			if ( 0 == getRowCount()) {
				_gisDataFrame.changeSelection( null);
				_priviousRow = 0;
			} else {
				int select = ( rows[ 0] < defaultTableModel.getRowCount()) ? rows[ 0] : defaultTableModel.getRowCount() - 1;
				setRowSelectionInterval( select, select);
				_priviousRow = select;
				_gisDataFrame.changeSelection( ( ObjectBase)getValueAt( select, 0));
			}

//			on_remove();

//			clear();	// 削除されたからキャッシュをクリア
//			InitialValueTable.clear();
		}
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
	 * @param objectBase
	 */
	public void append(ObjectBase objectBase) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		insert( objectBase, defaultTableModel, true);
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
//			clear();	// 名前が変わったからキャッシュをクリア
//			InitialValueTable.clear();
		}

		repaint();
	}

	/**
	 * @param objectBase
	 * @param defaultTableModel
	 * @param selection
	 */
	private void insert(ObjectBase objectBase, DefaultTableModel defaultTableModel, boolean selection) {
		Object[] objects = new Object[ 5];
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
			Rectangle rectangle = getCellRect( index, 0, true);
			scrollRectToVisible( rectangle);
			_gisDataFrame.changeSelection( objectBase);
			_priviousRow = index;
		}
	}

	/**
	 * @param objectBase0
	 * @param objectBase1
	 * @return
	 */
	protected int compare(ObjectBase objectBase0, ObjectBase objectBase1) {
		if ( ( objectBase0 instanceof KeywordObject && objectBase1 instanceof NumberObject))
			return -1;
		else if ( ( objectBase0 instanceof NumberObject && objectBase1 instanceof KeywordObject))
			return 1;
		else
			return StringNumberComparator.compareTo( objectBase0._name, objectBase1._name);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		super.valueChanged(e);
		int[] rows = getSelectedRows();
		if ( 1 != rows.length) {
			_gisDataFrame.changeSelection( null);
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
				_gisDataFrame.changeSelection( ( ObjectBase)getValueAt( arg0, 0));
				_priviousRow = arg0;
			} else {	// これから選択が２つ以上になる
				_gisDataFrame.changeSelection( null);
				_priviousRow = -1;
			}
			super.changeSelection(arg0, arg1, arg2, arg3);
		} else if ( arg2) {
			// 選択が減少する場合
			int[] rows = getSelectedRows();
			if ( 2 == rows.length) {	// これから選択が１つになる
				_gisDataFrame.changeSelection( ( ObjectBase)getValueAt( ( arg0 == rows[ 0]) ? rows[ 1] : arg0, 0));
				_priviousRow = ( arg0 == rows[ 0]) ? rows[ 1] : arg0;
			} else {	// この選択が解除されても２つ以上が選択された状態となる
				_gisDataFrame.changeSelection( null);
				_priviousRow = -1;
			}
			super.changeSelection(arg0, arg1, arg2, arg3);
		} else {
			ObjectBase objectBase = ( 0 > _priviousRow) ? null : ( ObjectBase)getValueAt( _priviousRow, 0);
			objectBase = ( ObjectBase)_gisDataFrame.confirm( _priviousRow, objectBase, ( ObjectBase)getValueAt( arg0, 0));
			if ( null == objectBase) {
				// 選択状態を変えない=super.changeSelection( ... )を呼ばない
				//System.out.println( "debug3 : " + _priviousRow + ", " + arg0 + ", " + arg2 + ", " + arg3);
				return;
			} else {
				int index = getIndex( objectBase);
				if ( 0 > index) {
					// これは起こり得ない筈だが念の為
					_gisDataFrame.changeSelection( ( ObjectBase)getValueAt( arg0, 0));
					_priviousRow = arg0;
					super.changeSelection(arg0, arg1, arg2, arg3);
					//System.out.println( "debug4 : " + _priviousRow + ", " + arg0 + ", " + arg2 + ", " + arg3);
				} else {
					// 返されたオブジェクトを選択する
					_gisDataFrame.changeSelection( objectBase);
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
		_gisDataFrame.changeSelection( objectBase);
	}

	/**
	 * @param index
	 */
	public void select(int index) {
		if ( 0 == getRowCount())
			return;

		setRowSelectionInterval( index, index);
		_priviousRow = index;
		_gisDataFrame.changeSelection( ( ObjectBase)getValueAt( index, 0));
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
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		if ( 0 == getRowCount())
			return true;

		writer.startElement( null, null, "variable_data", new AttributesImpl());

		for ( int row = 0; row < getRowCount(); ++row) {
			ObjectBase objectBase = ( ObjectBase)getValueAt( row, 0);
			if ( !objectBase.write( writer))
				return false;
		}

		writer.endElement( null, null, "variable_data");

		return true;
	}
}
