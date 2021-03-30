/*
 * 2005/05/06
 */
package soars.application.visualshell.object.player.base.edit.tab.initial_data_file;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
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

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.table.InitialValueTable;
import soars.application.visualshell.object.player.base.object.base.ObjectBase;
import soars.application.visualshell.object.player.base.object.initial_data_file.InitialDataFileObject;
import soars.common.utility.swing.file.manager.table.FileItemTransferable;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;

/**
 * @author kurata
 */
public class InitialDataFileTable extends VariableTableBase implements DropTargetListener {

	/**
	 * @param playerBase
	 * @param propertyPageMap
	 * @param propertyPageBase 
	 * @param owner
	 * @param parent
	 */
	public InitialDataFileTable(PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap, PropertyPageBase propertyPageBase, Frame owner, Component parent) {
		super("initial data file", playerBase, propertyPageMap, propertyPageBase, 2, owner, parent);
	}

//	/**
//	 * @param containsEmpty
//	 * @return
//	 */
//	public String[] get_agent_file_names(boolean containsEmpty) {
//		Vector filenames = new Vector();
//		get_file_names( filenames);
//		return LayerManager.get_instance().get_agent_object_names( "file", filenames, _playerBase, containsEmpty);
//	}
//
//	/**
//	 * @param containsEmpty
//	 * @return
//	 */
//	public String[] get_file_names(boolean containsEmpty) {
//		Vector filenames = new Vector();
//		get_file_names( filenames);
//
//		if ( containsEmpty && !filenames.contains( ""))
//			filenames.add( "");
//
//		return Tool.quick_sort_string( filenames, true, false);
//	}
//
//	/**
//	 * @param filenames
//	 */
//	private void get_file_names(Vector filenames) {
//		for ( int i = 0; i < getRowCount(); ++i) {
//			FileObject fileObject = ( FileObject)getValueAt( i, 0);
//			if ( !filenames.contains( fileObject._name))
//				filenames.add( fileObject._name);
//		}
//	}

	/**
	 * @param file
	 * @return
	 */
	public boolean uses_this_file(File file) {
		// TODO Auto-generated method stub
		for ( int i = 0; i < getRowCount(); ++i) {
			InitialDataFileObject initialDataFileObject = ( InitialDataFileObject)getValueAt( i, 0);
			if ( initialDataFileObject.uses_this_file( file))
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
			InitialDataFileObject initialDataFileObject = ( InitialDataFileObject)getValueAt( i, 0);
			initialDataFileObject.move_file( srcPath, destPath);
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

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.initial.data.file.table.header.file"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.initial.data.file.table.header.comment"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 2000);

		for ( int i = 0; i < _columns; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new InitialDataFileTableRowRenderer());
		}

		if ( !_playerBase.is_multi()) {
			for ( ObjectBase objectBase:_playerBase._objectListMap.get( _kind))
				append( ObjectBase.create( objectBase));
		}

		setup_popup_menu();

		new DropTarget( this, this);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase#append(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public void append(ObjectBase objectBase) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		Object[] objects = new Object[ _columns];
		for ( int i = 0; i < objects.length; ++i)
			objects[ i] = objectBase;

		defaultTableModel.addRow( objects);
		int index = defaultTableModel.getRowCount() - 1;
		setRowSelectionInterval( index, index);
		Rectangle rect = getCellRect( index, 0, true);
		scrollRectToVisible( rect);
		_priviousRow = index;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase#update(int, soars.application.visualshell.object.player.base.object.base.ObjectBase, boolean)
	 */
	public void update(int row, ObjectBase originalObjectBase, boolean selection) {
		// TODO
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( row, 0);
		if ( null == objectBase)
			return;

//		if ( !objectBase._name.equals( originalObjectBase._name)) {
//			defaultTableModel.removeRow( row);
//			insert( objectBase, defaultTableModel, selection);
		clear();	// キャッシュをクリア
		InitialValueTable.clear();
//		}

		repaint();
	}

	/**
	 * @param row
	 * @param file
	 */
	private void update_initial_data_file(int row, File file) {
		File user_data_directory = LayerManager.get_instance().get_user_data_directory();
		if ( null == user_data_directory)
			return;

		if ( !file.getAbsolutePath().replaceAll( "\\\\", "/").startsWith( user_data_directory.getAbsolutePath().replaceAll( "\\\\", "/")))
			return;

		InitialDataFileObject initialDataFileObject = ( InitialDataFileObject)getValueAt( row, 0);
		initialDataFileObject._name = ( file.getAbsolutePath().replaceAll( "\\\\", "/").substring( user_data_directory.getAbsolutePath().replaceAll( "\\\\", "/").length() + 1)
			+ ( file.isDirectory() ? "/" : ""));

		setRowSelectionInterval( row, row);
		( ( InitialDataFilePropertyPage)_propertyPageBase).changeSelection( ( InitialDataFileObject)getValueAt( row, 0), true);
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
		_playerBase._objectListMap.get( _kind).clear();

		for ( int i = 0; i < getRowCount(); ++i) {
			InitialDataFileObject initialDataFileObject = ( InitialDataFileObject)getValueAt( i, 0);
			_playerBase._objectListMap.get( _kind).add( initialDataFileObject);
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
//		if ( dataFlavors[ 0].getHumanPresentableName().equals( DirectoryNodeTransferable._name)
//			|| dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
		if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
			try {
				File[] files = null;
				if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
					files = ( File[])arg0.getTransferable().getTransferData( FileItemTransferable._localObjectFlavor);
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

				if ( 0 != columnAtPoint( position)) {
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
				( ( InitialDataFilePropertyPage)_propertyPageBase).changeSelection( ( InitialDataFileObject)getValueAt( row, 0), false);

				// TODO ファイルでないかまたは既存のファイルなら受け付けない
				if ( !files[ 0].isFile() || contains( files[ 0], row)) {
					arg0.rejectDrag();
					repaint();
					return;
				}

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

			if ( 0 != columnAtPoint( position)) {
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
//			if ( dataFlavors[ 0].getHumanPresentableName().equals( DirectoryNodeTransferable._name)) {
//				TreeNode draggedTreeNode = ( TreeNode)arg0.getTransferable().getTransferData( DirectoryNodeTransferable._localObjectFlavor);
//				arg0.getDropTargetContext().dropComplete( true);
//				if ( null == draggedTreeNode) {
//					arg0.rejectDrop();
//					repaint();
//					return;
//				}
//
//				File file = ( File)( ( DefaultMutableTreeNode)draggedTreeNode).getUserObject();
//				if ( null == file || !file.exists()) {
//					arg0.rejectDrop();
//					repaint();
//					return;
//				}
//
//				arg0.acceptDrop( DnDConstants.ACTION_COPY);
//				update_initial_data_file( row, file);
//			} else if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
			if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
				File[] files = ( File[])arg0.getTransferable().getTransferData( FileItemTransferable._localObjectFlavor);
				arg0.getDropTargetContext().dropComplete( true);
				if ( null == files || 1 != files.length || !files[ 0].exists()) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				// TODO ファイルでないかまたは既存のファイルなら受け付けない
				if ( !files[ 0].isFile() || contains( files[ 0], row)) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				arg0.acceptDrop( DnDConstants.ACTION_COPY);
				update_initial_data_file( row, files[ 0]);
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

	/**
	 * @param file
	 * @param row
	 * @return
	 */
	private boolean contains(File file, int row) {
		// 既存のファイルかどうか？のチェック
		for ( int i = 0; i < getRowCount(); ++i) {
			if ( row == i)
				continue;

			InitialDataFileObject initialDataFileObject = ( InitialDataFileObject)getValueAt( i, 0);
			String target = ( file.getAbsolutePath().substring( LayerManager.get_instance().get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/");
			if ( target.equals( initialDataFileObject._name))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
		repaint();
	}
}
