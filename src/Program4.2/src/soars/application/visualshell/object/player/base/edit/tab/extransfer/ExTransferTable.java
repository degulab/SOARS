/*
 * 2005/05/06
 */
package soars.application.visualshell.object.player.base.edit.tab.extransfer;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase;
import soars.application.visualshell.object.player.base.object.base.ObjectBase;
import soars.application.visualshell.object.player.base.object.extransfer.ExTransferObject;
import soars.common.utility.swing.file.manager.table.FileItemTransferable;
import soars.common.utility.swing.file.manager.tree.DirectoryNodeTransferable;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 */
public class ExTransferTable extends VariableTableBase implements DropTargetListener {

	/**
	 * @param playerBase
	 * @param propertyPageMap
	 * @param propertyPageBase 
	 * @param owner
	 * @param parent
	 */
	public ExTransferTable(PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap, PropertyPageBase propertyPageBase, Frame owner, Component parent) {
		super("extransfer", playerBase, propertyPageMap, propertyPageBase, 3, owner, parent);
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public String[] get_agent_file_names(boolean containsEmpty) {
		Vector filenames = new Vector();
		get_file_names( filenames);
		return LayerManager.get_instance().get_agent_object_names( "extransfer", filenames, _playerBase, containsEmpty);
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public String[] get_file_names(boolean containsEmpty) {
		Vector filenames = new Vector();
		get_file_names( filenames);

		if ( containsEmpty && !filenames.contains( ""))
			filenames.add( "");

		return Tool.quick_sort_string( filenames, true, false);
	}

	/**
	 * @param filenames
	 */
	private void get_file_names(Vector filenames) {
		for ( int i = 0; i < getRowCount(); ++i) {
			ExTransferObject exTransferObject = ( ExTransferObject)getValueAt( i, 0);
			if ( !filenames.contains( exTransferObject._name))
				filenames.add( exTransferObject._name);
		}
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean uses_this_file(File file) {
		// TODO Auto-generated method stub
		for ( int i = 0; i < getRowCount(); ++i) {
			ExTransferObject exTransferObject = ( ExTransferObject)getValueAt( i, 0);
			if ( exTransferObject.uses_this_file( file))
				return true;
		}
		return false;
//		return LayerManager.get_instance().uses_this_file( file);
	}

	/**
	 * @param srcPath
	 * @param destPath
	 */
	public void move_file(File srcPath, File destPath) {
		// TODO Auto-generated method stub
		for ( int i = 0; i < getRowCount(); ++i) {
			ExTransferObject exTransferObject = ( ExTransferObject)getValueAt( i, 0);
			exTransferObject.move_file( srcPath, destPath);
		}

		repaint();

//		return LayerManager.get_instance().move_file( srcPath, destPath);
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !super.setup(!_playerBase.is_multi()))
			return false;


		setAutoResizeMode( AUTO_RESIZE_OFF);


		JTableHeader tableHeader = getTableHeader();
		StandardTableHeaderRenderer standardTableHeaderRenderer = new StandardTableHeaderRenderer();
		if ( _playerBase.is_multi())
			standardTableHeaderRenderer.setEnabled( false);

		tableHeader.setDefaultRenderer( standardTableHeaderRenderer);

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( _columns);

		// TODO とりあえず
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.extransfer.table.header.name"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.extransfer.table.header.initial.value"));
		defaultTableColumnModel.getColumn( 2).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.extransfer.table.header.comment"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 2).setPreferredWidth( 2000);

		for ( int i = 0; i < _columns; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new ExTransferTableRowRenderer());
		}


		if ( !_playerBase.is_multi()) {
			if ( !setup( _playerBase._objectMapMap.get( "extransfer"), defaultTableModel))
				return false;
		}

		setup_popup_menu();

		new DropTarget( this, this);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase#compare(soars.application.visualshell.object.player.base.object.base.ObjectBase, soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	protected int compare(ObjectBase objectBase0, ObjectBase objectBase1) {
		return StringNumberComparator.compareTo( objectBase0._name, objectBase1._name);
	}

	/**
	 * @param row
	 * @param file
	 */
	private void update_initial_value(int row, File file) {
		File user_data_directory = LayerManager.get_instance().get_user_data_directory();
		if ( null == user_data_directory)
			return;

		if ( !file.getAbsolutePath().replaceAll( "\\\\", "/").startsWith( user_data_directory.getAbsolutePath().replaceAll( "\\\\", "/")))
			return;

		ExTransferObject exTransferObject = ( ExTransferObject)getValueAt( row, 0);
		exTransferObject._initialValue = ( file.getAbsolutePath().replaceAll( "\\\\", "/").substring( user_data_directory.getAbsolutePath().replaceAll( "\\\\", "/").length() + 1)
			+ ( file.isDirectory() ? "/" : ""));

		setRowSelectionInterval( row, row);
		( ( ExTransferPropertyPage)_propertyPageBase).changeSelection( ( ExTransferObject)getValueAt( row, 0), true);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase#can_remove_selected_objects(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	protected boolean can_remove_selected_objects(ObjectBase objectBase) {
		if ( _propertyPageMap.get( "variable").contains( objectBase))
			return false;

		return super.can_remove_selected_objects( objectBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase#on_remove()
	 */
	protected void on_remove() {
		_propertyPageMap.get( "variable").update();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase#on_ok()
	 */
	public void on_ok() {
		_playerBase._objectMapMap.get( "extransfer").clear();

		for ( int i = 0; i < getRowCount(); ++i) {
			ExTransferObject exTransferObject = ( ExTransferObject)getValueAt( i, 0);
			_playerBase._objectMapMap.get( "extransfer").put( exTransferObject._name, exTransferObject);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent arg0) {
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent arg0) {
    DataFlavor[] dataFlavors = arg0.getCurrentDataFlavors();
		if ( dataFlavors[ 0].getHumanPresentableName().equals( DirectoryNodeTransferable._name)
			|| dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
			try {
				if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
					File[] files = ( File[])arg0.getTransferable().getTransferData( FileItemTransferable._localObjectFlavor);
					if ( null == files || 1 != files.length || !files[ 0].exists()) {
						arg0.rejectDrag();
						repaint();
						return;
					}
				}

				Point position = getMousePosition();
				if ( null == position) {
					arg0.rejectDrag();
					repaint();
					return;
				}

				if ( 1 != columnAtPoint( position)) {
					arg0.rejectDrag();
					repaint();
					return;
				}

				int row = rowAtPoint( position);
				if ( 0 > row || getRowCount() <= row) {
					arg0.rejectDrag();
					repaint();
					return;
				}

				setRowSelectionInterval( row, row);
				( ( ExTransferPropertyPage)_propertyPageBase).changeSelection( ( ExTransferObject)getValueAt( row, 0), false);

				arg0.acceptDrag( DnDConstants.ACTION_COPY);
				repaint();
				return;
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
				arg0.rejectDrag();
			} catch (IOException e) {
				e.printStackTrace();
				arg0.rejectDrag();
			} catch (ArrayIndexOutOfBoundsException e) {
				arg0.rejectDrag();
				repaint();
				return;
			}
		}

		arg0.rejectDrag();
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
		try {
			Point position = getMousePosition();
			if ( null == position) {
				arg0.rejectDrop();
				repaint();
				return;
			}

			if ( 1 != columnAtPoint( position)) {
				arg0.rejectDrop();
				repaint();
				return;
			}

			int row = rowAtPoint( position);
			if ( 0 > row || getRowCount() <= row) {
				arg0.rejectDrop();
				repaint();
				return;
			}

			DataFlavor[] dataFlavors = arg0.getCurrentDataFlavors();
			if ( dataFlavors[ 0].getHumanPresentableName().equals( DirectoryNodeTransferable._name)) {
				TreeNode draggedTreeNode = ( TreeNode)arg0.getTransferable().getTransferData( DirectoryNodeTransferable._localObjectFlavor);
				arg0.getDropTargetContext().dropComplete( true);
				if ( null == draggedTreeNode) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				File file = ( File)( ( DefaultMutableTreeNode)draggedTreeNode).getUserObject();
				if ( null == file || !file.exists()) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				arg0.acceptDrop( DnDConstants.ACTION_COPY);
				update_initial_value( row, file);
			} else if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
				File[] files = ( File[])arg0.getTransferable().getTransferData( FileItemTransferable._localObjectFlavor);
				arg0.getDropTargetContext().dropComplete( true);
				if ( null == files || 1 != files.length || !files[ 0].exists()) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				arg0.acceptDrop( DnDConstants.ACTION_COPY);
				update_initial_value( row, files[ 0]);
			} else {
				arg0.rejectDrop();
			}
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
			arg0.rejectDrop();
		} catch (IOException e) {
			e.printStackTrace();
			arg0.rejectDrop();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			arg0.rejectDrop();
		}

		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
		repaint();
	}
}
