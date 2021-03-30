/*
 * 2005/06/22
 */
package soars.application.simulator.warning;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import soars.application.simulator.main.Environment;
import soars.application.simulator.main.ResourceManager;
import soars.common.soars.warning.WarningList;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 */
public class WarningDlg1 extends Dialog {

	/**
	 * 
	 */
	static public final int _minimum_width = 640;

	/**
	 * 
	 */
	static public final int _minimum_height = 480;

	/**
	 * 
	 */
	private String _message = "";

	/**
	 * 
	 */
	private WarningList _warningList = null;

	/**
	 * 
	 */
	private JButton _copy_to_clipboard_button = null;

	/**
	 * 
	 */
	private Component _parent = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param message
	 * @param parent
	 */
	public WarningDlg1(Frame arg0, String arg1, String message, Component parent) {
		super(arg0, arg1, true);
		_message = message;
		_parent = parent;
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._warning_dialog1_rectangle_key + "x",
			String.valueOf( SwingTool.get_default_window_position(
				( null != _parent) ? _parent : getOwner(),
				_minimum_width, _minimum_height).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._warning_dialog1_rectangle_key + "y",
			String.valueOf( SwingTool.get_default_window_position(
				( null != _parent) ? _parent : getOwner(),
				_minimum_width, _minimum_height).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._warning_dialog1_rectangle_key + "width",
			String.valueOf( _minimum_width));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._warning_dialog1_rectangle_key + "height",
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
			setLocationRelativeTo( ( null != _parent) ? _parent : getOwner());
		}
	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Rectangle rectangle = getBounds();

		Environment.get_instance().set(
			Environment._warning_dialog1_rectangle_key + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._warning_dialog1_rectangle_key + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._warning_dialog1_rectangle_key + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._warning_dialog1_rectangle_key + "height", String.valueOf( rectangle.height));
	}

	/**
	 * 
	 */
	public void do_modal() {
		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			do_modal( getOwner(), _minimum_width, _minimum_height);
		else
			do_modal( rectangle);
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

		setup_message_label( north_panel);

		getContentPane().add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		if ( !setup_warningList( center_panel))
			return false;

		getContentPane().add( center_panel);



		JPanel southg_panel = new JPanel();
		southg_panel.setLayout( new BoxLayout( southg_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( southg_panel);

		setup_copy_to_clipboard_button( southg_panel);

		insert_horizontal_glue( southg_panel);

		setup_close_button( southg_panel);

		insert_horizontal_glue( southg_panel);

		getContentPane().add( southg_panel, "South");



		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * @param north_panel
	 * 
	 */
	private void setup_message_label(JPanel north_panel) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel( _message);
		panel.add( label);

		north_panel.add( panel);
	}

	/**
	 * @param center_panel
	 * @return
	 */
	private boolean setup_warningList(JPanel center_panel) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_warningList = new WarningList( ( Frame)getOwner(), this);
		if ( !_warningList.setup())
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _warningList);

		panel.add( scrollPane);
		panel.add( Box.createHorizontalStrut( 5));
		center_panel.add( panel);

		return true;
	}

	/**
	 * @param southg_panel
	 * 
	 */
	private void setup_copy_to_clipboard_button(JPanel southg_panel) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel button_panel = new JPanel();
		button_panel.setLayout( new GridLayout( 1, 1));

		_copy_to_clipboard_button = new JButton(
			ResourceManager.get( "warning.dialog1.copy.to.clipboard.button.name"));
		_copy_to_clipboard_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WarningManager.get_instance().copy_to_clipboard();
			}
		});

		button_panel.add( _copy_to_clipboard_button);
		panel.add( button_panel);
		panel.add( Box.createHorizontalStrut( 5));
		southg_panel.add( panel);
	}

	/**
	 * @param southg_panel
	 * 
	 */
	private void setup_close_button(JPanel southg_panel) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel button_panel = new JPanel();
		button_panel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get( "warning.dialog1.close.button.name"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				set_property_to_environment_file();
				dispose();
			}
		});

		button_panel.add( button);
		panel.add( button_panel);
		panel.add( Box.createHorizontalStrut( 5));
		southg_panel.add( panel);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		optimize_window_rectangle();

		if ( 0 == _warningList.getModel().getSize())
			_copy_to_clipboard_button.setEnabled( false);

		_warningList.requestFocusInWindow();

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
	}
}
