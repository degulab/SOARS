/*
 * 2005/03/03
 */
package soars.application.animator.object.property.base.edit;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.application.animator.common.image.AnimatorImageManager;
import soars.application.animator.main.Environment;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.property.base.PropertyManager;
import soars.application.animator.object.property.base.edit.select.SelectPropertyDlg;
import soars.application.animator.observer.Observer;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box to edit the all properties.
 * @author kurata / SOARS project
 */
public class EditPropertiesDlg extends Dialog {

	/**
	 * Default width.
	 */
	static public final int _minimum_width = 800;

	/**
	 * DEfaylt height.
	 */
	static public final int _minimum_height = 400;

	/**
	 * 
	 */
	private PropertyManager _propertyManager = null;

	/**
	 * 
	 */
	private String _open_directory_key = "";

	/**
	 * 
	 */
	protected Vector _selected_properties = new Vector();

	/**
	 * 
	 */
	private JTabbedPane _propertyPages = null;

	/**
	 * 
	 */
	private JTextField _textField = null;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param open_directory_key the key mapped to the default directory for JFileChooser
	 * @param propertyManager the Property hashtable(name(String) - value(String) - PropertyBase)
	 */
	public EditPropertiesDlg(Frame arg0, String arg1, boolean arg2, String open_directory_key, PropertyManager propertyManager) {
		super(arg0, arg1, arg2);
		_open_directory_key = open_directory_key;
		_propertyManager = propertyManager;
		_selected_properties.addAll( propertyManager.get_selected_properties());
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._editPropertiesDialogRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimum_width, _minimum_height).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editPropertiesDialogRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimum_width, _minimum_height).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editPropertiesDialogRectangleKey + "width",
			String.valueOf( _minimum_width));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editPropertiesDialogRectangleKey + "height",
			String.valueOf( _minimum_height));
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
			setSize( _minimum_width, _minimum_height);
			setLocationRelativeTo( getOwner());
		}
	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Rectangle rectangle = getBounds();

		Environment.get_instance().set(
			Environment._editPropertiesDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._editPropertiesDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._editPropertiesDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._editPropertiesDialogRectangleKey + "height", String.valueOf( rectangle.height));
	}

	/**
	 * Returns true if this component is created successfully.
	 * @return true if this component is created successfully
	 */
	public boolean do_modal() {
		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			return do_modal( getOwner(), _minimum_width, _minimum_height);
		else
			return do_modal( rectangle);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;



		getContentPane().setLayout( new BorderLayout());



		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		getContentPane().add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		if ( !setup_tabbed_pane( center_panel))
			return false;

		getContentPane().add( center_panel);



		JPanel south_panel = new JPanel();
		south_panel.setLayout( new BoxLayout( south_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( south_panel);

		setup_selected_properties_order_text_field( south_panel);

		insert_horizontal_glue( south_panel);

		setup_select_properties_button( south_panel);

		insert_horizontal_glue( south_panel);

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get( "dialog.ok"),
			ResourceManager.get( "dialog.cancel"),
			false, false);
		south_panel.add( panel);

		insert_horizontal_glue( south_panel);

		getContentPane().add( south_panel, "South");



		setDefaultCloseOperation( DISPOSE_ON_CLOSE);



		return true;
	}

	/**
	 * @param center_panel
	 * @return
	 */
	private boolean setup_tabbed_pane(JPanel center_panel) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_propertyPages = new JTabbedPane( SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

		for( int i = 0 ; i < _propertyManager.get_order().size(); ++i) {
			String name = ( String)_propertyManager.get_order().get( i);
			PropertyPage propertyPage = new PropertyPage( name, _propertyManager, _open_directory_key, ( Frame)getOwner(), this);
			if ( !propertyPage.create())
				return false;

			_propertyPages.add( propertyPage, name);
		}

		panel.add( _propertyPages);
		panel.add( Box.createHorizontalStrut( 5));
		center_panel.add( panel);

		return true;
	}

	/**
	 * @param south_panel
	 * 
	 */
	private void setup_selected_properties_order_text_field(JPanel south_panel) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel(
			ResourceManager.get( "edit.properties.dialog.order"));
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_textField = new JTextField( get_selected_properties_text());
		_textField.setEditable( false);
		panel.add( _textField);

		panel.add( Box.createHorizontalStrut( 5));

		south_panel.add( panel);
	}

	/**
	 * @param south_panel
	 * 
	 */
	private void setup_select_properties_button(JPanel south_panel) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel button_panel = new JPanel();
		button_panel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get( "edit.properties.dialog.select.button"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_select_properties( arg0);
			}
		});

		button_panel.add( button);
		panel.add( button_panel);
		panel.add( Box.createHorizontalStrut( 5));
		south_panel.add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_select_properties(ActionEvent actionEvent) {
		SelectPropertyDlg selectPropertyDlg
			= new SelectPropertyDlg( ( Frame)getOwner(),
				ResourceManager.get( "select.property.dialog.title"),
				true,
				_propertyManager.get_order(),
				_selected_properties);
		if ( !selectPropertyDlg.do_modal( this))
			return;

		_textField.setText( get_selected_properties_text());
	}

	/**
	 * @return
	 */
	private String get_selected_properties_text() {
		String text = "";
		for ( int i = 0; i < _selected_properties.size(); ++i) {
			text += _selected_properties.get( i);
			if ( _selected_properties.size() - 1 > i)
				text += ", ";
		}
		return text;
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
				setSize( ( _minimum_width > width) ? _minimum_width : width,
					( _minimum_height > height) ? _minimum_height : height);
			}
		});

		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				set_property_to_environment_file();
			}
		});

		for ( int i = 0; i < _propertyPages.getTabCount(); ++i) {
			PropertyPage propertyPage = ( PropertyPage)_propertyPages.getComponentAt( i);
			propertyPage.on_setup_completed();
		}
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		for ( int i = 0; i < _propertyPages.getTabCount(); ++i) {
			PropertyPage propertyPage = ( PropertyPage)_propertyPages.getComponentAt( i);
			propertyPage.on_ok( ( Graphics2D)getGraphics());
		}

		_propertyManager.update( _selected_properties);

		Observer.get_instance().modified();

		set_property_to_environment_file();

		AnimatorImageManager.get_instance().update();

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
