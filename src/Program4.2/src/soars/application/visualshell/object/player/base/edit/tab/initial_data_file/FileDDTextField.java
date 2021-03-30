/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab.initial_data_file;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.File;
import java.io.IOException;

import soars.application.visualshell.layer.LayerManager;
import soars.common.utility.swing.dnd.text.DDTextField;
import soars.common.utility.swing.file.manager.table.FileItemTransferable;

/**
 * @author kurata
 *
 */
public class FileDDTextField extends DDTextField {

	/**
	 * 
	 */
	public FileDDTextField() {
		super();
	}

	/**
	 * @param file
	 */
	private void setText(File file) {
		File user_data_directory = LayerManager.get_instance().get_user_data_directory();
		if ( null == user_data_directory)
			return;

		if ( !file.getAbsolutePath().replaceAll( "\\\\", "/").startsWith( user_data_directory.getAbsolutePath().replaceAll( "\\\\", "/")))
			return;

		setText( file.getAbsolutePath().replaceAll( "\\\\", "/").substring( user_data_directory.getAbsolutePath().replaceAll( "\\\\", "/").length() + 1)
			+ ( file.isDirectory() ? "/" : ""));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent arg0) {
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent arg0) {
    DataFlavor[] dataFlavors = arg0.getCurrentDataFlavors();
		if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
			try {
				File[] files = ( File[])arg0.getTransferable().getTransferData( FileItemTransferable._localObjectFlavor);
				if ( null == files || 1 != files.length || !files[ 0].exists() || !files[ 0].isFile() /*|| !InitialDataFileChecker.execute( files[ 0])*/) {
					arg0.rejectDrag();
					repaint();
					return;
				}

				arg0.acceptDrag( DnDConstants.ACTION_COPY);
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
				arg0.rejectDrag();
			} catch (IOException e) {
				e.printStackTrace();
				arg0.rejectDrag();
			}
		} else
			arg0.rejectDrag();

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
		try {
			DataFlavor[] dataFlavors = arg0.getCurrentDataFlavors();
			if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
				File[] files = ( File[])arg0.getTransferable().getTransferData( FileItemTransferable._localObjectFlavor);
				arg0.getDropTargetContext().dropComplete( true);
				if ( null == files || 1 != files.length || !files[ 0].exists() || !files[ 0].isFile()/* || !InitialDataFileChecker.execute( files[ 0])*/) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				arg0.acceptDrop( DnDConstants.ACTION_COPY);
				setText( files[ 0]);
			} else {
				arg0.rejectDrop();
			}
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
			arg0.rejectDrop();
		} catch (IOException e) {
			e.printStackTrace();
			arg0.rejectDrop();
		}

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
		repaint();
	}
}
