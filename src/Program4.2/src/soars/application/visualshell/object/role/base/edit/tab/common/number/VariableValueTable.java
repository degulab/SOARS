/*
 * Created on 2005/10/31
 */
package soars.application.visualshell.object.role.base.edit.tab.common.number;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JMenuItem;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import soars.application.visualshell.common.menu.basic1.EditAction;
import soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1;
import soars.application.visualshell.common.swing.TableBase;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.ResourceManager;

/**
 * @author kurata
 */
public class VariableValueTable extends TableBase implements IBasicMenuHandler1 {

	/**
	 * 
	 */
	private String[] _number_object_names = null;

	/**
	 * 
	 */
	private HashMap _variable_map = null;

	/**
	 * 
	 */
	private Color _color = null;

	/**
	 * 
	 */
	private JMenuItem _edit_menuItem = null;

	/**
	 * @param variable_map
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public VariableValueTable(HashMap variable_map, Color color, Frame owner, Component parent) {
		super(owner, parent);
		_variable_map = variable_map;
		_color = color;
	}

	/**
	 * @param function
	 */
	public void set(String function) {
		Vector variables = ( Vector)_variable_map.get( function);
		if ( null == variables)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setRowCount( 0);
		for ( int i = 0; i < variables.size(); ++i)
			defaultTableModel.addRow( ( String[])variables.get( i));
	}

	/**
	 * @param function
	 */
	public void update_variable_map(String function) {
		_variable_map.put( function, get());
	}

	/**
	 * @return
	 */
	public Vector get() {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		Vector variables = new Vector();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			variables.add( new String[] {
				( String)defaultTableModel.getValueAt( i, 0),
				( String)defaultTableModel.getValueAt( i, 1)});
		}
		return variables;
	}

	/**
	 * @param number_object_names
	 */
	public void update(String[] number_object_names) {
		_number_object_names = number_object_names;
		update();
	}

	/**
	 * 
	 */
	public void update() {
		List number_object_name_list= null;
		if ( null == _number_object_names)
			number_object_name_list = new ArrayList();
		else
			number_object_name_list = new ArrayList( Arrays.asList( _number_object_names));

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			String value = ( String)defaultTableModel.getValueAt( i, 1);
			if ( !CommonTool.is_number_correct( value) && !number_object_name_list.contains( value))
				defaultTableModel.setValueAt( "0.0", i, 1);
		}

		Iterator iterator = _variable_map.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Vector variables = ( Vector)entry.getValue();
			for ( int i = 0; i < variables.size(); ++i) {
				String[] values = ( String[])variables.get( i);
				if ( !CommonTool.is_number_correct( values[ 1]) && !number_object_name_list.contains( values[ 1]))
					values[ 1] = "0.0";
			}
		}
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !super.setup(true))
			return false;

		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);
		setSelectionMode( DefaultListSelectionModel.SINGLE_SELECTION);

		JTableHeader tableHeader = getTableHeader();
		tableHeader.setDefaultRenderer( new VariableValueTableHeaderRenderer( _color));

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 2);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.table.header.variable"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.table.header.value"));

		for ( int i = 0; i < 2; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new VariableValueTableCellRenderer( _color));
		}

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_edit_menuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.edit.menu"),
			new EditAction( ResourceManager.get_instance().get( "common.popup.menu.edit.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.edit.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.edit.stroke"));
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		int row = rowAtPoint( mouseEvent.getPoint());
		if ( 0 > row || getRowCount() <= row)
			return;

		int column = columnAtPoint( mouseEvent.getPoint());
		if ( 0 > column || getColumnCount() <= column)
			return;

		on_edit( null);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#on_mouse_right_up(java.awt.Point)
	 */
	public void on_mouse_right_up(Point point) {
		if ( !isEnabled())
			return;

		_edit_menuItem.setEnabled( true);

		//int index = getSelectedRow();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount() /*|| -1 == index*/) {
			_edit_menuItem.setEnabled( false);
		} else {
			int row = rowAtPoint( point);
			int column = columnAtPoint( point);
			if ( ( 0 <= row && getRowCount() > row)
				&& ( 0 <= column && getColumnCount() > column)) {
				setRowSelectionInterval( row, row);
				setColumnSelectionInterval( column, column);
			} else {
				_edit_menuItem.setEnabled( false);
			}

			//if ( !isRowSelected( row) || !isColumnSelected( column)) {
			//	_edit_menuItem.setEnabled( false);
			//}
		}

		_popupMenu.show( this, point.x, point.y);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_key_pressed(java.awt.event.KeyEvent)
	 */
	protected void on_key_pressed(KeyEvent keyEvent) {
//		int row = getSelectedRow();
//		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
//		if ( 0 == defaultTableModel.getRowCount() || -1 == row)
//			return;
//
//		switch ( keyEvent.getKeyCode()) {
//			case KeyEvent.VK_DELETE:
//			case KeyEvent.VK_BACK_SPACE:
//				on_remove( null);
//				break;
//		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#on_enter(java.awt.event.ActionEvent)
	 */
	protected void on_enter(ActionEvent actionEvent) {
		on_edit( actionEvent);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_append(java.awt.event.ActionEvent)
	 */
	public void on_append(ActionEvent actionEvent) {
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_insert(java.awt.event.ActionEvent)
	 */
	public void on_insert(ActionEvent actionEvent) {
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_edit(java.awt.event.ActionEvent)
	 */
	public void on_edit(ActionEvent actionEvent) {
		int row = getSelectedRow();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount() || -1 == row)
			return;

		EditValueDlg editValueDlg = new EditValueDlg(
			_owner,
			ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.value.dialog.title"),
			true,
			_color,
			( String)defaultTableModel.getValueAt( row, 0),
			( String)defaultTableModel.getValueAt( row, 1),
			_number_object_names);

		if ( !editValueDlg.do_modal( _parent))
			return;

		defaultTableModel.setValueAt( editValueDlg._value, row, 1);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_remove(java.awt.event.ActionEvent)
	 */
	public void on_remove(ActionEvent actionEvent) {
	}
}
