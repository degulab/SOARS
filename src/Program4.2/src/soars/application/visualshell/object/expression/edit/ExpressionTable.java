/*
 * Created on 2005/10/28
 */
package soars.application.visualshell.object.expression.edit;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import soars.application.visualshell.common.menu.basic1.AppendAction;
import soars.application.visualshell.common.menu.basic1.EditAction;
import soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1;
import soars.application.visualshell.common.menu.basic1.RemoveAction;
import soars.application.visualshell.common.swing.TableBase;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.expression.Expression;

/**
 * The table component to edit the expressions.
 * @author kurata / SOARS project
 */
public class ExpressionTable extends TableBase implements IBasicMenuHandler1 {

	/**
	 * 
	 */
	protected JMenuItem _append_menuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _edit_menuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _remove_menuItem = null;

	/**
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public ExpressionTable(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * Returns true for containing the specified function.
	 * @param function1 the specified function
	 * @return true for containing the specified function
	 */
	public boolean contains(String function1) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			Expression expression = ( Expression)defaultTableModel.getValueAt( i, 0);
			if ( expression._value[ 0].equals( function1))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this component is initialized successfully.
	 * @return true if this component is initialized successfully
	 */
	public boolean setup() {
		if ( !super.setup(true))
			return false;

		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);
		setFillsViewportHeight( true);


		setAutoResizeMode( AUTO_RESIZE_OFF);


		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 2);

		JTableHeader tableHeader = getTableHeader();
		tableHeader.setDefaultRenderer( new StandardTableHeaderRenderer());

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.expressions.dialog.expression.table.header.function"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.expressions.dialog.expression.table.header.expression"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 300);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 2000);

		for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new ExpressionTableCellRenderer());
		}

		Object[] objects = new Object[ 2];

		Iterator iterator = VisualShellExpressionManager.get_instance().entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String function1 = ( String)entry.getKey();
			Expression expression = ( Expression)entry.getValue();
			objects[ 0] = expression;
			objects[ 1] = expression;
			defaultTableModel.addRow( objects);
		}

		if ( 0 < defaultTableModel.getRowCount())
			setRowSelectionInterval( 0, 0);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_append_menuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.append.menu"),
			new AppendAction( ResourceManager.get_instance().get( "common.popup.menu.append.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.append.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.append.stroke"));
		_edit_menuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.edit.menu"),
			new EditAction( ResourceManager.get_instance().get( "common.popup.menu.edit.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.edit.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.edit.stroke"));
		_remove_menuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "common.popup.menu.remove.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.stroke"));
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
		_edit_menuItem.setEnabled( true);
		_remove_menuItem.setEnabled( true);

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount() /*|| -1 == index*/) {
			_edit_menuItem.setEnabled( false);
			_remove_menuItem.setEnabled( false);
		} else {
			int row = rowAtPoint( point);
			int column = columnAtPoint( point);
			if ( ( 0 <= row && getRowCount() > row)
				&& ( 0 <= column && getColumnCount() > column)) {
				//setRowSelectionInterval( row, row);
				//setColumnSelectionInterval( column, column);
				int[] rows = getSelectedRows();
				boolean contains = ( 0 <= Arrays.binarySearch( rows, row));
				_edit_menuItem.setEnabled( 1 == rows.length && contains);
				_remove_menuItem.setEnabled( contains);
			} else {
				_edit_menuItem.setEnabled( false);
				_remove_menuItem.setEnabled( false);
			}
		}

		_popupMenu.show( this, point.x, point.y);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_key_pressed(java.awt.event.KeyEvent)
	 */
	protected void on_key_pressed(KeyEvent keyEvent) {
		int row = getSelectedRow();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount() || -1 == row)
			return;

		switch ( keyEvent.getKeyCode()) {
			case KeyEvent.VK_DELETE:
			case KeyEvent.VK_BACK_SPACE:
				on_remove( null);
				break;
		}
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
		ExpressionDlg expressionDlg = new ExpressionDlg(
			_owner,
			ResourceManager.get_instance().get( "append.expression.dialog.title"),
			true,
			this);

		if ( !expressionDlg.do_modal( _parent))
			return;

		Expression expression = new Expression( expressionDlg._value);

		Object[] objects = new Object[ 2];
		objects[ 0] = expression;
		objects[ 1] = expression;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.addRow( objects);
		setRowSelectionInterval( defaultTableModel.getRowCount() - 1, defaultTableModel.getRowCount() - 1);
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
		int[] rows = getSelectedRows();
		if ( 1 != rows.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount() || -1 == rows[ 0])
			return;

		Expression expression = ( Expression)defaultTableModel.getValueAt( rows[ 0], 0);

		ExpressionDlg expressionDlg = new ExpressionDlg(
			_owner,
			ResourceManager.get_instance().get( "edit.expression.dialog.title"),
			true,
			expression,
			this);

		if ( !expressionDlg.do_modal( _parent))
			return;

		expression._value = expressionDlg._value;

		setRowSelectionInterval( rows[ 0], rows[ 0]);

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_remove(java.awt.event.ActionEvent)
	 */
	public void on_remove(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( 1 > rows.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount())
			return;

		WarningManager.get_instance().cleanup();

		for ( int i = 0; i < rows.length; ++i) {
			Expression expression = ( Expression)defaultTableModel.getValueAt( rows[ i], 0);
			boolean result1 = get_visualShellExpressionManager().can_remove( expression);
			boolean result2 = LayerManager.get_instance().can_remove_expression( expression);
			if ( !result1 || !result2) {
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
		}

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			_parent,
			ResourceManager.get_instance().get( "edit.expression.dialog.expression.table.confirm.remove.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {

			rows = Tool.quick_sort_int( rows, true);
			for ( int i = rows.length - 1; i >= 0; --i)
				defaultTableModel.removeRow( rows[ i]);

			if ( 0 < defaultTableModel.getRowCount()) {
				if ( rows[ 0] < defaultTableModel.getRowCount())
					setRowSelectionInterval( rows[ 0], rows[ 0]);
				else
					setRowSelectionInterval( defaultTableModel.getRowCount() - 1, defaultTableModel.getRowCount() - 1);
			}
		}
	}

	/**
	 * 
	 */
	protected VisualShellExpressionManager get_visualShellExpressionManager() {
		VisualShellExpressionManager visualShellExpressionManager = new VisualShellExpressionManager();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			Expression expression = ( Expression)defaultTableModel.getValueAt( i, 0);
			visualShellExpressionManager.put( expression._value[ 0], expression);
		}
		return visualShellExpressionManager;
	}

	/**
	 * 
	 */
	protected void on_ok() {
		VisualShellExpressionManager.get_instance().clear();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			Expression expression = ( Expression)defaultTableModel.getValueAt( i, 0);
			VisualShellExpressionManager.get_instance().put( expression._value[ 0], expression);
		}
	}
}
