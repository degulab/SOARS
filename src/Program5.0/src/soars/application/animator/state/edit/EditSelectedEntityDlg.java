/*
 * 2005/03/25
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
import soars.application.animator.common.panel.FontPanel;
import soars.application.animator.common.panel.VisiblePanel;
import soars.application.animator.common.tool.CommonTool;
import soars.application.animator.main.Administrator;
import soars.application.animator.main.Environment;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.main.internal.ObjectManager;
import soars.application.animator.object.common.tool.CommonObjectTool;
import soars.application.animator.object.entity.base.EntityBase;
import soars.application.animator.observer.Observer;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box to edit the selected objects.
 * @author kurata / SOARS project
 */
public class EditSelectedEntityDlg extends Dialog {

	/**
	 * Default width of this dialog box.
	 */
	static public final int _minimumWidth = 800;

	/**
	 * Default height of this dialog box.
	 */
	static public final int _minimumHeight = 700;

	/**
	 * 
	 */
	private String _openDirectoryKey = "";

	/**
	 * 
	 */
	private Vector<EntityBase> _entityBases = null;

	/**
	 * 
	 */
	private ObjectManager _objectManager = null; 

	/**
	 * 
	 */
	private VisiblePanel _visiblePanel = null;

	/**
	 * 
	 */
	private ColorPanel _colorPanel = null;

	/**
	 * 
	 */
	private FontPanel _fontPanel = null;

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
	 * @param objects the array of the selected objects
	 * @param objectManager
	 */
	public EditSelectedEntityDlg(Frame arg0, String arg1, boolean arg2, String openDirectoryKey, Vector<EntityBase> objects, ObjectManager objectManager) {
		super(arg0, arg1, arg2);
		_openDirectoryKey = openDirectoryKey;
		_entityBases = objects;
		_objectManager = objectManager;
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._editObjectDialogRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editObjectDialogRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editObjectDialogRectangleKey + "width",
			String.valueOf( _minimumWidth));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editObjectDialogRectangleKey + "height",
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
			Environment._editObjectDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._editObjectDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._editObjectDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._editObjectDialogRectangleKey + "height", String.valueOf( rectangle.height));
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


		EntityBase entityBase = ( EntityBase)_entityBases.get( 0);


		getContentPane().setLayout( new BorderLayout());


		_visiblePanel = new VisiblePanel( ( Frame)getOwner(), this);
		_colorPanel = new ColorPanel( ( Frame)getOwner(), this);
		_fontPanel = new FontPanel( ( Frame)getOwner(), this);
		_selectedImagefilePanel = new SelectedImagefilePanel( ( Frame)getOwner(), this);
		_thumbnailPanel = new ThumbnailPanel( _selectedImagefilePanel, ( Frame)getOwner(), this);


		JPanel westPanel = new JPanel();
		westPanel.setLayout( new BoxLayout( westPanel, BoxLayout.Y_AXIS));

		if ( !setup_west_panel( entityBase, westPanel))
			return false;

		getContentPane().add( westPanel, "West");


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.X_AXIS));

		if ( !setup_center_panel( entityBase, centerPanel))
			return false;

		getContentPane().add( centerPanel);


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		insert_horizontal_glue( southPanel);

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);
		southPanel.add( panel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");


		adjust();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		return true;
	}

	/**
	 * @param entityBase
	 * @param parent
	 * @return
	 */
	private boolean setup_west_panel(EntityBase entityBase, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_visible( entityBase, northPanel);

		insert_horizontal_glue( northPanel);

		setup_color( entityBase, northPanel);

		insert_horizontal_glue( northPanel);

		setup_font( entityBase, northPanel);

		insert_horizontal_glue( northPanel);

		if ( !setup_selectedImagefilePanel( entityBase, northPanel))
			return false;

		panel.add( northPanel, "North");

		parent.add( panel);

		return true;
	}

	/**
	 * @param entityBase
	 * @param parent
	 */
	private void setup_visible(EntityBase entityBase, JPanel parent) {
		_visiblePanel.setup( entityBase._visible, entityBase._visibleName, this);
		parent.add( _visiblePanel);
	}

	/**
	 * @param entityBase
	 * @param parent
	 */
	private void setup_color(EntityBase entityBase, JPanel parent) {
		_colorPanel.setup( entityBase._imageColor, entityBase._textColor, this);
		parent.add( _colorPanel);
	}

	/**
	 * @param entityBase
	 * @param parent
	 */
	private void setup_font(EntityBase entityBase, JPanel parent) {
		_fontPanel.setup( entityBase._font, this);
		parent.add( _fontPanel);
	}

	/**
	 * @param entityBase
	 * @param parent
	 * @return
	 */
	private boolean setup_selectedImagefilePanel(EntityBase entityBase, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_selectedImagefilePanel.setup( entityBase._imageFilename))
			return false;

		panel.add( _selectedImagefilePanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param entityBase
	 * @param parent
	 * @return
	 */
	private boolean setup_center_panel(EntityBase entityBase, JPanel parent) {
		if ( !_thumbnailPanel.setup( entityBase._imageFilename, _openDirectoryKey))
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
		width = Math.max( width, _fontPanel.get_max_width());
		width = Math.max( width, _selectedImagefilePanel.get_max_width());

		_colorPanel.set_width( width);
		_fontPanel.set_width( width);
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
		boolean visible = _visiblePanel._visibleCheckBox.isSelected();
		boolean visibleName = _visiblePanel._visibleNameCheckBox.isSelected();
		Color imageColor = _colorPanel._imageColorLabel.getBackground();
		Color textColor = _colorPanel._textColorLabel.getBackground();
		String fontFamily = ( String)_fontPanel._fontFamilyComboBox.getSelectedItem();
		int fontStyle = CommonTool.get_font_style( ( String)_fontPanel._fontStyleComboBox.getSelectedItem());
		int fontSize = Integer.parseInt( ( String)_fontPanel._fontSizeComboBox.getSelectedItem());

		String newImageFilename = _selectedImagefilePanel.get_selected_image_filename();

		String[] imageFilenames = CommonObjectTool.get_image_filenames( _entityBases);

		for ( int i = 0; i < _entityBases.size(); ++i) {
			EntityBase entityBase = ( EntityBase)_entityBases.get( i);
			entityBase.update( visible, visibleName,
				imageColor.getRed(), imageColor.getGreen(), imageColor.getBlue(),
				textColor.getRed(), textColor.getGreen(), textColor.getBlue(),
				fontFamily, fontStyle, fontSize, newImageFilename,
				( Graphics2D)getGraphics());
		}

		for ( int i = 0; i < imageFilenames.length; ++i) {
			if ( !imageFilenames[ i].equals( newImageFilename) 
				&& !_objectManager.uses_this_image( imageFilenames[ i])) {
				File file = new File( Administrator.get_instance().get_image_directory(), imageFilenames[ i]);
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