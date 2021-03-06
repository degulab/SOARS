/*
 * 2005/05/01
 */
package soars.application.visualshell.object.stage.edit;

import java.awt.BorderLayout;
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

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.stage.StageManager;
import soars.application.visualshell.observer.Observer;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 */
public class EditStageDlg extends Dialog {

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
	private StageManager _stageManager = null;

	/**
	 * 
	 */
	private StageList _mainStageList = null;

	/**
	 * 
	 */
	private StageList _initialStageList = null;

	/**
	 * 
	 */
	private StageList _terminalStageList = null;

	/**
	 * 
	 */
	private EditRoleFrame _editRoleFrame = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param stageManager
	 * @param editRoleFrame
	 */
	public EditStageDlg(Frame arg0, String arg1, boolean arg2, StageManager stageManager, EditRoleFrame editRoleFrame) {
		super(arg0, arg1, arg2);
		_stageManager = new StageManager( stageManager);
		_editRoleFrame = editRoleFrame;
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._editStageDialogRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position(
				( null != _editRoleFrame) ? _editRoleFrame : getOwner(),
				_minimum_width, _minimum_height).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editStageDialogRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position(
				( null != _editRoleFrame) ? _editRoleFrame : getOwner(),
				_minimum_width, _minimum_height).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editStageDialogRectangleKey + "width",
			String.valueOf( _minimum_width));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editStageDialogRectangleKey + "height",
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
			setLocationRelativeTo( ( null != _editRoleFrame) ? _editRoleFrame : getOwner());
		}
	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Rectangle rectangle = getBounds();

		Environment.get_instance().set(
			Environment._editStageDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._editStageDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._editStageDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._editStageDialogRectangleKey + "height", String.valueOf( rectangle.height));
	}

	/**
	 * @return
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

		JPanel base_panel = new JPanel();
		base_panel.setLayout( new BoxLayout( base_panel, BoxLayout.X_AXIS));

		base_panel.add( Box.createHorizontalStrut( 5));

		JPanel panel = setup_initial_stage_label();
		if ( null == panel)
			return false;

		setup_main_stage_label( panel);
		setup_terminal_stage_label( panel);

		base_panel.add( panel);
		north_panel.add( base_panel);
		getContentPane().add( north_panel, BorderLayout.NORTH);



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		base_panel = new JPanel();
		base_panel.setLayout( new BoxLayout( base_panel, BoxLayout.X_AXIS));

		base_panel.add( Box.createHorizontalStrut( 5));

		_initialStageList = new StageList(
			new String[] { ResourceManager.get_instance().get( "append.initial.stage.property.dialog.title"),
				ResourceManager.get_instance().get( "edit.initial.stage.property.dialog.title"),
				ResourceManager.get_instance().get( "edit.initial.stage.list.confirm.remove.message")},
			_editRoleFrame, ( Frame)getOwner(), this);
		_mainStageList = new StageList(
			new String[] { ResourceManager.get_instance().get( "append.main.stage.property.dialog.title"),
				ResourceManager.get_instance().get( "edit.main.stage.property.dialog.title"),
				ResourceManager.get_instance().get( "edit.main.stage.list.confirm.remove.message")},
			_editRoleFrame, ( Frame)getOwner(), this);
		_terminalStageList = new StageList(
			new String[] { ResourceManager.get_instance().get( "append.terminal.stage.property.dialog.title"),
				ResourceManager.get_instance().get( "edit.terminal.stage.property.dialog.title"),
				ResourceManager.get_instance().get( "edit.terminal.stage.list.confirm.remove.message")},
			_editRoleFrame, ( Frame)getOwner(), this);
		StageListTransferHandler stageListTransferHandler = new StageListTransferHandler( _mainStageList, _initialStageList, _terminalStageList);

		panel = setup_initial_stage_list( stageListTransferHandler);
		if ( null == panel)
			return false;

		if ( !setup_main_stage_list( stageListTransferHandler, panel))
			return false;

		if ( !setup_terminal_stage_list( stageListTransferHandler, panel))
			return false;

		base_panel.add( panel);
		center_panel.add( base_panel);
		getContentPane().add( center_panel);



		JPanel south_panel = new JPanel();
		south_panel.setLayout( new BoxLayout( south_panel, BoxLayout.Y_AXIS));

		base_panel = new JPanel();
		base_panel.setLayout( new BoxLayout( base_panel, BoxLayout.X_AXIS));

		base_panel.add( Box.createHorizontalStrut( 5));

		panel = setup_append_initial_stage_button();
		if ( null == panel)
			return false;

		setup_append_main_stage_button( panel);
		setup_append_terminal_stage_button( panel);

		base_panel.add( panel);
		south_panel.add( base_panel);


		insert_horizontal_glue( south_panel);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);
		south_panel.add( panel);

		insert_horizontal_glue( south_panel);

		getContentPane().add( south_panel, "South");



		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * @return
	 */
	private JPanel setup_initial_stage_label() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 3));

		JPanel part_panel = new JPanel();
		part_panel.setLayout( new BoxLayout( part_panel, BoxLayout.X_AXIS));

		JPanel label_panel = new JPanel();
		label_panel.setLayout( new GridLayout( 1, 1));

		JLabel label = new JLabel(
			ResourceManager.get_instance().get( "edit.stage.dialog.initial.stage"));

		label_panel.add( label);
		part_panel.add( label_panel);

		part_panel.add( Box.createHorizontalStrut( 5));

		panel.add( part_panel);

		return panel;
	}

	/**
	 * @param panel
	 */
	private void setup_main_stage_label(JPanel panel) {
		JPanel part_panel = new JPanel();
		part_panel.setLayout( new BoxLayout( part_panel, BoxLayout.X_AXIS));

		JPanel label_panel = new JPanel();
		label_panel.setLayout( new GridLayout( 1, 1));

		JLabel label = new JLabel(
			ResourceManager.get_instance().get( "edit.stage.dialog.main.stage"));

		label_panel.add( label);
		part_panel.add( label_panel);

		part_panel.add( Box.createHorizontalStrut( 5));

		panel.add( part_panel);
	}

	/**
	 * @param panel
	 */
	private void setup_terminal_stage_label(JPanel panel) {
		JPanel part_panel = new JPanel();
		part_panel.setLayout( new BoxLayout( part_panel, BoxLayout.X_AXIS));

		JPanel label_panel = new JPanel();
		label_panel.setLayout( new GridLayout( 1, 1));

		JLabel label = new JLabel(
			ResourceManager.get_instance().get( "edit.stage.dialog.terminal.stage"));

		label_panel.add( label);
		part_panel.add( label_panel);

		part_panel.add( Box.createHorizontalStrut( 5));

		panel.add( part_panel);
	}

	/**
	 * @param stageListTransferHandler
	 * @return
	 */
	private JPanel setup_initial_stage_list(StageListTransferHandler stageListTransferHandler) {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 3));

		JPanel part_panel = new JPanel();
		part_panel.setLayout( new BoxLayout( part_panel, BoxLayout.X_AXIS));

		JPanel list_panel = new JPanel();
		list_panel.setLayout( new GridLayout( 1, 1));

		if ( !_initialStageList.setup( _stageManager._initial_stages,
			new StageList[] { _mainStageList, _terminalStageList},
			stageListTransferHandler))
			return null;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _initialStageList);

		list_panel.add( scrollPane);
		part_panel.add( list_panel);

		part_panel.add( Box.createHorizontalStrut( 5));

		panel.add( part_panel);

		return panel;
	}

	/**
	 * @param stageListTransferHandler
	 * @param panel
	 * @return
	 */
	private boolean setup_main_stage_list(StageListTransferHandler stageListTransferHandler, JPanel panel) {
		JPanel part_panel = new JPanel();
		part_panel.setLayout( new BoxLayout( part_panel, BoxLayout.X_AXIS));

		JPanel list_panel = new JPanel();
		list_panel.setLayout( new GridLayout( 1, 1));

		if ( !_mainStageList.setup( _stageManager._main_stages,
			new StageList[] { _initialStageList, _terminalStageList},
			stageListTransferHandler))
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _mainStageList);

		list_panel.add( scrollPane);
		part_panel.add( list_panel);

		part_panel.add( Box.createHorizontalStrut( 5));

		panel.add( part_panel);

		return true;
	}

	/**
	 * @param stageListTransferHandler
	 * @param panel
	 * @return
	 */
	private boolean setup_terminal_stage_list(StageListTransferHandler stageListTransferHandler, JPanel panel) {
		JPanel part_panel = new JPanel();
		part_panel.setLayout( new BoxLayout( part_panel, BoxLayout.X_AXIS));

		JPanel list_panel = new JPanel();
		list_panel.setLayout( new GridLayout( 1, 1));

		if ( !_terminalStageList.setup( _stageManager._terminal_stages,
			new StageList[] { _mainStageList, _initialStageList},
			stageListTransferHandler))
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _terminalStageList);

		list_panel.add( scrollPane);
		part_panel.add( list_panel);

		part_panel.add( Box.createHorizontalStrut( 5));

		panel.add( part_panel);

		return true;
	}

	/**
	 * @return
	 */
	private JPanel setup_append_initial_stage_button() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 3));

		JPanel part_panel = new JPanel();
		part_panel.setLayout( new BoxLayout( part_panel, BoxLayout.X_AXIS));

		JPanel button_panel = new JPanel();
		button_panel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get_instance().get( "edit.stage.dialog.append.initial.stage.button.name"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_initialStageList.on_append( arg0);
			}
		});

		button_panel.add( button);
		part_panel.add( button_panel);

		part_panel.add( Box.createHorizontalStrut( 5));

		panel.add( part_panel);

		return panel;
	}

	/**
	 * @param panel
	 */
	private void setup_append_main_stage_button(JPanel panel) {
		JPanel part_panel = new JPanel();
		part_panel.setLayout( new BoxLayout( part_panel, BoxLayout.X_AXIS));

		JPanel button_panel = new JPanel();
		button_panel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get_instance().get( "edit.stage.dialog.append.main.stage.button.name"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_mainStageList.on_append( arg0);
			}
		});

		button_panel.add( button);
		part_panel.add( button_panel);

		part_panel.add( Box.createHorizontalStrut( 5));

		panel.add( part_panel);
	}

	/**
	 * @param panel
	 */
	private void setup_append_terminal_stage_button(JPanel panel) {
		JPanel part_panel = new JPanel();
		part_panel.setLayout( new BoxLayout( part_panel, BoxLayout.X_AXIS));

		JPanel button_panel = new JPanel();
		button_panel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get_instance().get( "edit.stage.dialog.append.terminal.stage.button.name"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_terminalStageList.on_append( arg0);
			}
		});

		button_panel.add( button);
		part_panel.add( button_panel);

		part_panel.add( Box.createHorizontalStrut( 5));

		panel.add( part_panel);
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

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
//		Vector stage_names = _stageList.get_stage_names();
//		if ( !Observer.get_instance().can_adjust_stage_name( stage_names)) {
//			WarningDlg warningDlg = new WarningDlg( ( Frame)getOwner());
//			warningDlg.do_modal( this);
//			return;
//		}

		_mainStageList.on_ok( _stageManager._main_stages);
		_initialStageList.on_ok( _stageManager._initial_stages);
		_terminalStageList.on_ok( _stageManager._terminal_stages);

		StageManager.get_instance().set( _stageManager);

		Observer.get_instance().on_update_stage();

		Observer.get_instance().modified();

		set_property_to_environment_file();

		if ( null != _editRoleFrame)
			_editRoleFrame.on_update_stage();

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
