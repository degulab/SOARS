/*
 * 2005/05/06
 */
package soars.application.visualshell.object.player.base.edit;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.PlayerTabbedPane;
import soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.table.InitialValueTable;
import soars.application.visualshell.object.player.base.edit.tree.PlayerTree;
import soars.application.visualshell.observer.Observer;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 */
public class EditObjectDlg extends Dialog {

	/**
	 * 
	 */
	static public final int _minimumWidth = 640;

	/**
	 * 
	 */
	static public final int _minimumHeight = 480;

	/**
	 * 
	 */
	private PlayerBase _playerBase = null;

	/**
	 * 
	 */
	private String _title = "";

	/**
	 * 
	 */
	private JSplitPane _splitPane = null;

	/**
	 * 
	 */
	private PlayerTree _playerTree = null;

	/**
	 * 
	 */
	private PlayerTabbedPane _playerTabbedPane = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param playerBase
	 */
	public EditObjectDlg(Frame arg0, String arg1, boolean arg2, PlayerBase playerBase) {
		super(arg0, arg1, arg2);
		_title = arg1;
		_playerBase = playerBase;
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
			if ( _splitPane.getDividerLocation() >= ( _minimumWidth - getInsets().left - getInsets().right))
				_splitPane.setDividerLocation( 100);

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

		Environment.get_instance().set(
			Environment._editObjectDialogDividerLocationKey, String.valueOf( _splitPane.getDividerLocation()));
	}

	/**
	 * 
	 */
	public void do_modal() {
		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			do_modal( getOwner(), _minimumWidth, _minimumHeight);
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



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( center_panel);

		if ( !setup( center_panel))
			return false;

		getContentPane().add( center_panel);

		_splitPane.setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._editObjectDialogDividerLocationKey, "100")));



		JPanel south_panel = new JPanel();
		south_panel.setLayout( new BoxLayout( south_panel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		insert_horizontal_glue( south_panel);

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);
		south_panel.add( panel);

		insert_horizontal_glue( south_panel);

		getContentPane().add( south_panel, "South");



		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

//		// Test
//		Dialog dialog = new Dialog( this);
//		dialog.create();
//		dialog.setVisible( true);

		return true;
	}

	/**
	 * @param center_panel
	 * @return
	 */
	private boolean setup(JPanel center_panel) {
		JPanel base_panel = new JPanel();
		base_panel.setLayout( new BoxLayout( base_panel, BoxLayout.X_AXIS));

		base_panel.add( Box.createHorizontalStrut( 5));



		_splitPane = new JSplitPane();



		_playerTree = new PlayerTree( ( Frame)getOwner(), this);
		_playerTabbedPane = new PlayerTabbedPane( _playerBase);



		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

//		panel.add( Box.createHorizontalStrut( 5));

		if ( !_playerTree.setup( _playerTabbedPane, _playerBase._name/*_title*/))
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _playerTree);
		panel.add( scrollPane);

//		panel.add( Box.createHorizontalStrut( 5));

		_splitPane.setLeftComponent( panel);



		panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

//		panel.add( Box.createHorizontalStrut( 5));

		if ( !_playerTabbedPane.setup( _playerTree, ( Frame)getOwner(), this))
			return false;


		panel.add( _playerTabbedPane);

		panel.add( Box.createHorizontalStrut( 5));

		_splitPane.setRightComponent( panel);



		base_panel.add( _splitPane);



		center_panel.add( base_panel);



		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		optimize_window_rectangle();

		_playerTabbedPane.on_setup_completed();

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width,
					( _minimumHeight > height) ? _minimumHeight : height);
			}
		});

		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				set_property_to_environment_file();
				_playerTabbedPane.windowClosing();
			}
		});
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		String name = _playerBase._name;
		String number = _playerBase._number;

		if ( !_playerTabbedPane.on_ok( _playerBase))
			return;

		update_gis_spots();

		for ( String kind:Constant._kinds)
			Observer.get_instance().on_update_object( kind);

		Observer.get_instance().on_update_playerBase( !_playerBase._name.equals( name) || !_playerBase._number.equals( number));

		Observer.get_instance().modified();

		set_property_to_environment_file();

		super.on_ok(actionEvent);
	}

	/**
	 * 
	 */
	private void update_gis_spots() {
		// TODO Auto-generated method stub
		if ( _playerBase._gis.equals( ""))
			return;

		List<PlayerBase> playerBases = new ArrayList<PlayerBase>();
		LayerManager.get_instance().get_gis_spots( playerBases, _playerBase._gis);
		for ( PlayerBase playerBase:playerBases)
			playerBase.update_on_gis( _playerBase);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		_playerTabbedPane.on_cancel();
		set_property_to_environment_file();
		super.on_cancel(actionEvent);
	}

	/**
	 * Clear buffers for copy and paste.
	 */
	public static void clear() {
		VariableTableBase.clear();
		InitialValueTable.clear();
	}
}
