/**
 * 
 */
package soars.application.manager.sample.main.panel.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import soars.application.manager.sample.main.Constant;
import soars.application.manager.sample.main.ResourceManager;
import soars.application.manager.sample.main.panel.tree.SampleTree;
import soars.application.manager.sample.menu.edit.ClearImageAction;
import soars.application.manager.sample.menu.edit.IEditMenuHandler;
import soars.application.manager.sample.property.Property;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.window.View;
import soars.common.utility.tool.clipboard.URISelection;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 *
 */
public class ImagePanel extends View implements DropTargetListener, IEditMenuHandler {

	/**
	 * 
	 */
	private BufferedImage _no_image_BufferedImage = null;

	/**
	 * 
	 */
	private Property _property = null;

	/**
	 * 
	 */
	private SampleTree _sampleTree = null;

	/**
	 * 
	 */
	protected UserInterface _userInterface = null;

	/**
	 * 
	 */
	protected JPopupMenu _popupMenu = null;

	/**
	 * 
	 */
	protected JMenuItem _editClearImageMenuItem = null;

	/**
	 * 
	 */
	private Map<String, JButton> _buttonMap = null;

	/**
	 * @param buttonMap
	 */
	public ImagePanel(Map<String, JButton> buttonMap) {
		super();
		_no_image_BufferedImage = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/message/" + ResourceManager.get( "image.panel.no.image.file"), getClass());
		setBackground( new Color( 255, 255, 255));
		_buttonMap = buttonMap;
	}

	/**
	 * @param sampleTree
	 * @return
	 */
	public boolean create(SampleTree sampleTree) {
		if ( !super.create())
			return false;

		_sampleTree = sampleTree;

		setup_popup_menu();

		new DropTarget( this, this);

		return true;
	}

	/**
	 * @param menuItemMap
	 */
	public void editMenuSelected(Map<String, JMenuItem> menuItemMap) {
		menuItemMap.get( ResourceManager.get( "edit.clear.image.menu")).setEnabled( null != _property && null != _property._bufferedImage);
	}

	/**
	 * 
	 */
	private void setup_popup_menu() {
		_userInterface = new UserInterface();
		_popupMenu = new JPopupMenu();

		_editClearImageMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "edit.clear.image.menu"),
			new ClearImageAction( ResourceManager.get( "edit.clear.image.menu"), this),
			ResourceManager.get( "edit.clear.image.mnemonic"),
			ResourceManager.get( "edit.clear.image.stroke"));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		_editClearImageMenuItem.setEnabled( null != _property && null != _property._bufferedImage);
		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.sample.menu.edit.IEditMenuHandler#on_edit_clear_image(java.awt.event.ActionEvent)
	 */
	public void on_edit_clear_image(ActionEvent actionEvent) {
		if ( null == _property || null == _property._bufferedImage)
			return;

		if ( !_property.update_image( _sampleTree.get_datafile( _property), null))
			return;

		set_no_image();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if ( null == _property)
			return;

		if ( null != _property._bufferedImage)
			g.drawImage( _property._bufferedImage, 0, 0, this);
		else {
			if ( null == _no_image_BufferedImage)
				return;

			g.drawImage( _no_image_BufferedImage, 0, 0, this);
		}
	}

	/**
	 * @param property
	 */
	public void update(Property property) {
		_property = property;
		
		if ( null == _property || null == _property._bufferedImage)
			set_no_image();
		else {
			setPreferredSize( new Dimension( _property._bufferedImage.getWidth(), _property._bufferedImage.getHeight()));
			updateUI();
			repaint();
		}
	}

	/**
	 * 
	 */
	public void set_empty_image() {
		_property = null;
		setPreferredSize( new Dimension());
		updateUI();
		repaint();
	}

	/**
	 * 
	 */
	public void set_no_image() {
		setPreferredSize( ( null == _no_image_BufferedImage) ? new Dimension() : new Dimension( _no_image_BufferedImage.getWidth(), _no_image_BufferedImage.getHeight()));
		updateUI();
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent arg0) {
		Transferable transferable = arg0.getTransferable();
    DataFlavor[] dataFlavors = arg0.getCurrentDataFlavors();
		if ( !transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)
			&& !transferable.isDataFlavorSupported( URISelection._uriFlavor)) {
			arg0.rejectDrag();
			return;
		}

		if ( null == _property) {
			arg0.rejectDrag();
			return;
		}

		arg0.acceptDrag( DnDConstants.ACTION_COPY);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
		if ( null == _property) {
			arg0.rejectDrop();
			return;
		}

		try {
			Transferable transferable = arg0.getTransferable();
			if ( transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)) {
				arg0.acceptDrop( arg0.getDropAction());
				List list = ( List)transferable.getTransferData( DataFlavor.javaFileListFlavor);
				if ( null == list || list.isEmpty()) {
					arg0.getDropTargetContext().dropComplete( true);
					return;
				}

				File[] files =( File[])list.toArray( new File[ 0]);
				arg0.getDropTargetContext().dropComplete( true);
				on_paste( files[ 0]);
			} else if ( transferable.isDataFlavorSupported( URISelection._uriFlavor)) {
				arg0.acceptDrop( arg0.getDropAction());
				String string = ( String)transferable.getTransferData( URISelection._uriFlavor);
				arg0.getDropTargetContext().dropComplete( true);
				if ( null == string)
					return;

				StringTokenizer stringTokenizer = new StringTokenizer( string, "\r\n");
				List<File> list = new ArrayList<File>();
				while( stringTokenizer.hasMoreElements()) {
					URI uri = new URI( ( String)stringTokenizer.nextElement());
					if ( uri.getScheme().equals( "file"))
						list.add( new File( uri.getPath()));
				}
				File[] files = list.toArray( new File[ 0]);
				on_paste( files[ 0]);
			}
		} catch (UnsupportedFlavorException e) {
			arg0.rejectDrop();
		} catch (IOException e) {
			arg0.rejectDrop();
		} catch (URISyntaxException e) {
			arg0.rejectDrop();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
	}

	/**
	 * @param file
	 */
	private void on_paste(File file) {
		if ( !_property.update_image( _sampleTree.get_datafile( _property), file))
			return;

		setPreferredSize( new Dimension( _property._bufferedImage.getWidth(), _property._bufferedImage.getHeight()));
		updateUI();
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.sample.menu.edit.IEditMenuHandler#on_edit_copy(java.awt.event.ActionEvent)
	 */
	public void on_edit_copy(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.sample.menu.edit.IEditMenuHandler#on_edit_paste(java.awt.event.ActionEvent)
	 */
	public void on_edit_paste(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.sample.menu.edit.IEditMenuHandler#on_edit_export(java.awt.event.ActionEvent)
	 */
	public void on_edit_export(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.sample.menu.edit.IEditMenuHandler#on_edit_remove(java.awt.event.ActionEvent)
	 */
	public void on_edit_remove(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.sample.menu.edit.IEditMenuHandler#on_edit_rename(java.awt.event.ActionEvent)
	 */
	public void on_edit_rename(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.sample.menu.edit.IEditMenuHandler#on_edit_new_directory(java.awt.event.ActionEvent)
	 */
	public void on_edit_new_directory(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.sample.menu.edit.IEditMenuHandler#on_edit_new_simulation_model(java.awt.event.ActionEvent)
	 */
	public void on_edit_new_simulation_model(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.sample.menu.edit.IEditMenuHandler#on_edit_model_information(java.awt.event.ActionEvent)
	 */
	public void on_edit_model_information(ActionEvent actionEvent) {
	}
}
