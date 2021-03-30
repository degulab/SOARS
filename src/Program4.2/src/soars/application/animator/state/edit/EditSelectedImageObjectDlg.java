/**
 * 
 */
package soars.application.animator.state.edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.animator.common.image.AnimatorImageManager;
import soars.application.animator.common.image.SelectedImagefilePanel;
import soars.application.animator.common.image.ThumbnailPanel;
import soars.application.animator.common.panel.ColorPanel;
import soars.application.animator.main.Environment;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.common.tool.CommonObjectTool;
import soars.application.animator.object.player.ObjectManager;
import soars.application.animator.object.player.base.ObjectBase;
import soars.application.animator.object.player.spot.SpotObject;
import soars.application.animator.observer.Observer;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box to edit the selected image objects.
 * @author kurata / SOARS project
 */
public class EditSelectedImageObjectDlg extends Dialog {

	/**
	 * Default width of this dialog box.
	 */
	static public final int _minimumWidth = 800;

	/**
	 * Default height of this dialog box.
	 */
	static public final int _minimumHeight = 480;

	/**
	 * 
	 */
	private String _openDirectoryKey = "";

	/**
	 * 
	 */
	private Vector<SpotObject> _objects = null;

	/**
	 * 
	 */
	private ColorPanel _colorPanel = null;

	/**
	 * 
	 */
	private SelectedImagefilePanel _selectedImagefilePanel = null;

	/**
	 * 
	 */
	private ThumbnailPanel _thumbnailPanel = null;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog.
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param openDirectoryKey the key mapped to the default directory for JFileChooser
	 * @param objects the array of the selected image objects
	 */
	public EditSelectedImageObjectDlg(Frame arg0, String arg1, boolean arg2, String openDirectoryKey, Vector<SpotObject> objects) {
		super(arg0, arg1, arg2);
		_openDirectoryKey = openDirectoryKey;
		_objects = objects;
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._editImageObjectDialogRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editImageObjectDialogRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editImageObjectDialogRectangleKey + "width",
			String.valueOf( _minimumWidth));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editImageObjectDialogRectangleKey + "height",
			String.valueOf( _minimumHeight));
		if ( null == value)
			return null;

		int height = Integer.parseInt( value);

		return new Rectangle( x, y, width, height);
	}

	/**
	 * 
	 */
	private void optimize_window_rectangle() {
		Rectangle rectangle = getBounds();
		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( rectangle)
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).width <= 10
			|| rectangle.y <= -getInsets().top
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).height <= getInsets().top) {
			setSize( _minimumWidth, _minimumHeight);
			setLocationRelativeTo( getOwner());
		}
	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Rectangle rectangle = getBounds();

		Environment.get_instance().set(
			Environment._editImageObjectDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._editImageObjectDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._editImageObjectDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._editImageObjectDialogRectangleKey + "height", String.valueOf( rectangle.height));
	}

	/**
	 * Returns true if this component is created successfully.
	 * @return true if this component is created successfully
	 */
	public boolean do_modal() {
		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			return do_modal( getOwner(), _minimumWidth, _minimumHeight);
		else
			return do_modal( rectangle);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		ObjectBase objectBase = ( ObjectBase)_objects.get( 0);


		getContentPane().setLayout( new BorderLayout());


		_colorPanel = new ColorPanel( ( Frame)getOwner(), this);
		_selectedImagefilePanel = new SelectedImagefilePanel( ( Frame)getOwner(), this);
		_thumbnailPanel = new ThumbnailPanel( _selectedImagefilePanel, ( Frame)getOwner(), this);


		JPanel west_panel = new JPanel();
		west_panel.setLayout( new BoxLayout( west_panel, BoxLayout.Y_AXIS));

		if ( !setup_west_panel( objectBase, west_panel))
			return false;

		getContentPane().add( west_panel, "West");


		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.X_AXIS));

		if ( !setup_center_panel( objectBase, center_panel))
			return false;

		getContentPane().add( center_panel);


		JPanel south_panel = new JPanel();
		south_panel.setLayout( new BoxLayout( south_panel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		insert_horizontal_glue( south_panel);

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get( "dialog.ok"),
			ResourceManager.get( "dialog.cancel"),
			false, false);
		south_panel.add( panel);

		insert_horizontal_glue( south_panel);

		getContentPane().add( south_panel, "South");


		adjust();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		return true;
	}

	/**
	 * @param objectBase
	 * @param parent
	 * @return
	 */
	private boolean setup_west_panel(ObjectBase objectBase, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_color( objectBase, north_panel);

		insert_horizontal_glue( north_panel);

		if ( !setup_selectedImagefilePanel( objectBase, north_panel))
			return false;

		panel.add( north_panel, "North");

		parent.add( panel);

		return true;
	}

	/**
	 * @param objectBase
	 * @param parent
	 */
	private void setup_color(ObjectBase objectBase, JPanel parent) {
		_colorPanel.setup( objectBase._imageColor, this);
		parent.add( _colorPanel);
	}

	/**
	 * @param objectBase
	 * @param parent
	 * @return
	 */
	private boolean setup_selectedImagefilePanel(ObjectBase objectBase, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_selectedImagefilePanel.setup( objectBase._imageFilename))
			return false;

		panel.add( _selectedImagefilePanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param objectBase
	 * @param parent
	 * @return
	 */
	private boolean setup_center_panel(ObjectBase objectBase, JPanel parent) {
		if ( !_thumbnailPanel.setup( objectBase._imageFilename, _openDirectoryKey))
			return false;

		parent.add( _thumbnailPanel);

		return true;
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		width = Math.max( width, _colorPanel.get_max_width());
		width = Math.max( width, _selectedImagefilePanel.get_max_width());

		_colorPanel.set_width( width);
		_selectedImagefilePanel.set_width( width);

		_thumbnailPanel.adjust();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		optimize_window_rectangle();

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width,
					( _minimumHeight > height) ? _minimumHeight : height);
			}
		});
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		Color imageColor = _colorPanel._image_color_label.getBackground();

		String newImageFilename = _selectedImagefilePanel.get_selected_image_filename();

		String[] imageFilenames = CommonObjectTool.get_image_filenames( _objects);

		for ( int i = 0; i < _objects.size(); ++i) {
			ObjectBase objectBase = ( ObjectBase)_objects.get( i);
			objectBase.update( imageColor.getRed(), imageColor.getGreen(),
				imageColor.getBlue(), newImageFilename, ( Graphics2D)getGraphics());
		}

		for ( int i = 0; i < imageFilenames.length; ++i) {
			if ( !imageFilenames[ i].equals( newImageFilename) 
				&& !ObjectManager.get_instance().uses_this_image( imageFilenames[ i])) {
				File file = new File( ObjectManager.get_instance().get_image_directory(), imageFilenames[ i]);
				AnimatorImageManager.get_instance().remove( file.getAbsolutePath());
			}
		}

		Observer.get_instance().modified();

		set_property_to_environment_file();

		super.on_ok(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		set_property_to_environment_file();
		super.on_cancel(actionEvent);
	}
}
