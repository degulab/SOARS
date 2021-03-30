/**
 * 
 */
package soars.application.animator.object.chart.setting;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import soars.application.animator.main.Environment;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.player.ObjectManager;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class ChartDisplaySettingDlg extends Dialog {

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
	private ChartTree _chartTree = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws HeadlessException
	 */
	public ChartDisplaySettingDlg(Frame arg0, String arg1, boolean arg2) throws HeadlessException {
		super(arg0, arg1, arg2);
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._chartDisplaySettingDialogRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimum_width, _minimum_height).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._chartDisplaySettingDialogRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimum_width, _minimum_height).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._chartDisplaySettingDialogRectangleKey + "width",
			String.valueOf( _minimum_width));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._chartDisplaySettingDialogRectangleKey + "height",
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
			Environment._chartDisplaySettingDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._chartDisplaySettingDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._chartDisplaySettingDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._chartDisplaySettingDialogRectangleKey + "height", String.valueOf( rectangle.height));
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

	/* (non-Javadoc)
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

		if ( !setup( center_panel))
			return false;

		getContentPane().add( center_panel);



		JPanel south_panel = new JPanel();
		south_panel.setLayout( new BoxLayout( south_panel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		insert_horizontal_glue( south_panel);

		setup_ok_and_cancel_and_apply_button(
			panel,
			ResourceManager.get( "dialog.ok"),
			ResourceManager.get( "dialog.cancel"),
			ResourceManager.get( "dialog.apply"),
			false, false);
		south_panel.add( panel);

		insert_horizontal_glue( south_panel);

		getContentPane().add( south_panel, "South");



		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_chartTree = new ChartTree( ( Frame)getOwner(), this);

		if ( !_chartTree.setup(false))
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _chartTree);
		panel.add( scrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
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
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		_chartTree.on_ok();
		set_property_to_environment_file();
		ObjectManager.get_instance().modified();
		super.on_ok(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		set_property_to_environment_file();
		super.on_cancel(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_apply(java.awt.event.ActionEvent)
	 */
	protected void on_apply(ActionEvent actionEvent) {
		_chartTree.on_ok();
		set_property_to_environment_file();
		ObjectManager.get_instance().modified();
	}
}
