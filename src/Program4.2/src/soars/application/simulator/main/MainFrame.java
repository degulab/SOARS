/**
 * 
 */
package soars.application.simulator.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.xml.sax.SAXException;

import soars.application.simulator.common.tool.CommonTool;
import soars.application.simulator.data.ChartData;
import soars.application.simulator.data.FileManagerData;
import soars.application.simulator.data.LogData;
import soars.application.simulator.file.loader.SaxLoader;
import soars.application.simulator.file.loader.legacy.LegacySaxLoader;
import soars.application.simulator.file.writer.SaxWriter;
import soars.application.simulator.main.chart.ChartFrame;
import soars.application.simulator.main.filemanager.FileManagerFrame;
import soars.application.simulator.main.log.LiveLogViewerFrame;
import soars.application.simulator.main.log.LogViewerFrame;
import soars.application.simulator.main.log.StaticLogViewerFrame;
import soars.application.simulator.menu.file.CloseAction;
import soars.application.simulator.menu.file.ExitAction;
import soars.application.simulator.menu.file.IFileMenuHandler;
import soars.application.simulator.menu.file.NewAction;
import soars.application.simulator.menu.file.OpenAction;
import soars.application.simulator.menu.file.SaveAction;
import soars.application.simulator.menu.file.SaveAsAction;
import soars.application.simulator.menu.help.AboutAction;
import soars.application.simulator.menu.help.ContentsAction;
import soars.application.simulator.menu.help.ForumAction;
import soars.application.simulator.menu.help.IHelpMenuHandler;
import soars.application.simulator.menu.run.AnimatorAction;
import soars.application.simulator.menu.run.FileManagerAction;
import soars.application.simulator.menu.run.IRunMenuHandler;
import soars.application.simulator.menu.simulation.ISimulationMenuHandler;
import soars.application.simulator.menu.simulation.StopAction;
import soars.application.simulator.stream.StdErrOutStreamPumper;
import soars.application.simulator.stream.StdOutStreamPumper;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.mac.IMacScreenMenuHandler;
import soars.common.utility.swing.mac.MacUtility;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.MDIChildFrame;
import soars.common.utility.swing.window.MDIFrame;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.tool.network.BrowserLauncher;
import soars.common.utility.tool.resource.Resource;
import soars.common.utility.tool.timer.ITimerTaskImplementCallback;
import soars.common.utility.tool.timer.TimerTaskImplement;
import soars.common.utility.xml.sax.Writer;
import soars.plugin.modelbuilder.chart.chart.main.InternalChartFrame;
import time.TimeCounter;

/**
 * @author kurata
 *
 */
public class MainFrame extends MDIFrame implements IMacScreenMenuHandler, DropTargetListener, ITimerTaskImplementCallback, IFileMenuHandler, IHelpMenuHandler, IRunMenuHandler, ISimulationMenuHandler, IMessageCallback {

	/**
	 * 
	 */
	static public final int _minimum_width = 800;

	/**
	 * 
	 */
	static public final int _minimum_height = 600;

	/**
	 * Name of the root directory for Simulator data.
	 */
	static public final String _root_directory_name = "simulator";

	/**
	 * Name of the data file.
	 */
	static public final String _data_filename = "data.sml";

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private MainFrame _mainFrame = null;

	/**
	 * 
	 */
	private Rectangle _window_rectangle = new Rectangle();

	/**
	 * 
	 */
	private UserInterface _userInterface = null;

	/**
	 * 
	 */
	private JMenuItem _file_new_menuItem = null;

	/**
	 * 
	 */
	private JMenuItem _file_open_menuItem = null;

	/**
	 * 
	 */
	private JMenuItem _file_close_menuItem = null;

	/**
	 * 
	 */
	private JMenuItem _file_save_menuItem = null;

	/**
	 * 
	 */
	private JMenuItem _file_save_as_menuItem = null;

	/**
	 * 
	 */
	private JMenuItem _simulation_stop_menuItem = null;

	/**
	 * 
	 */
	private JMenuItem _run_animator_menuItem = null;

	/**
	 * 
	 */
	private JMenuItem _run_file_manager_menuItem = null;

	/**
	 * 
	 */
	private JButton _file_new_button = null;

	/**
	 * 
	 */
	private JButton _file_open_button = null;

	/**
	 * 
	 */
	private JButton _file_close_button = null;

	/**
	 * 
	 */
	private JButton _file_save_button = null;

	/**
	 * 
	 */
	private JButton _file_save_as_button = null;

	/**
	 * 
	 */
	private JButton _simulation_stop_button = null;

	/**
	 * 
	 */
	private JButton _run_animator_button = null;

	/**
	 * 
	 */
	private JButton _run_file_manager_button = null;

	/**
	 * 
	 */
	private JLabel _message_label = null;

	/**
	 * 
	 */
	private JLabel _information_label = null;

	/**
	 * 
	 */
	private final Color _background_color = new Color( 128, 128, 128);

	/**
	 * 
	 */
	private String _graphic_properties = "";

	/**
	 * 
	 */
	private String _chart_properties = "";

	/**
	 * 
	 */
	private String _simulator_window_title = "";

	/**
	 * 
	 */
	private String _simulator_window_time = "";

	/**
	 * 
	 */
	private String _log_folder_path = "";

	/**
	 * 
	 */
	private LogViewerFrame _logViewerFrame = null;

	/**
	 * 
	 */
	private env.Environment _environment = null;

	/**
	 * 
	 */
	private Timer _timer = null;

	/**
	 * 
	 */
	private TimerTaskImplement _timerTaskImplement = null;

	/**
	 * 
	 */
	private int _id = 0;

	/**
	 * 
	 */
	private final long _delay = 0;

	/**
	 * 
	 */
	private final long _period = 1000;

	/**
	 * 
	 */
	private boolean _running = false;

	/**
	 * 
	 */
	public StdOutStreamPumper _stdOutStreamPumper = null;

	/**
	 * 
	 */
	public StdErrOutStreamPumper _stdErrOutStreamPumper = null;

	/**
	 * 
	 */
	private File _current_file = null;

	/**
	 * 
	 */
	private File _parent_directory = null;

	/**
	 * 
	 */
	private File _root_directory = null;

	/**
	 * 
	 */
	private boolean _modified = false;

	/**
	 * 
	 */
	private InternalFrameRectangleMap _internalFrameRectangleMap = new InternalFrameRectangleMap();

	/**
	 * @return
	 */
	public static MainFrame get_instance() {
		synchronized( _lock) {
			if ( null == _mainFrame) {
				_mainFrame = new MainFrame( ResourceManager.get( "application.title"));
			}
		}
		return _mainFrame;
	}

	/**
	 * @param mdiChildFrame
	 * @return
	 */
	public static boolean append(MDIChildFrame mdiChildFrame) {
		synchronized( _lock) {
			if ( null == _mainFrame)
				return false;
		}

		_mainFrame._desktopPane.add( mdiChildFrame);

		Point position = SwingTool.get_default_window_position(
			_mainFrame._desktopPane.getBounds().width,
			_mainFrame._desktopPane.getBounds().height,
			mdiChildFrame.getBounds().width,
			mdiChildFrame.getBounds().height);
		mdiChildFrame.setLocation( position);
		mdiChildFrame.setSize( mdiChildFrame.getBounds().width, mdiChildFrame.getBounds().height);

		mdiChildFrame.toFront();

		_mainFrame._desktopPane.setSelectedFrame( mdiChildFrame);

		return true;
	}

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public MainFrame(String arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * @param mdiChildFrame
	 * @return
	 */
	public boolean appendChildFrame( MDIChildFrame mdiChildFrame) {
		_desktopPane.add( mdiChildFrame);

//		Point position = SwingTool.get_default_window_position(
//			_mainFrame._desktopPane.getBounds().width,
//			_mainFrame._desktopPane.getBounds().height,
//			mdiChildFrame.getBounds().width,
//			mdiChildFrame.getBounds().height);
//		mdiChildFrame.setLocation( position);
//		mdiChildFrame.setSize( mdiChildFrame.getBounds().width, mdiChildFrame.getBounds().height);

		mdiChildFrame.toFront();

		_desktopPane.setSelectedFrame( mdiChildFrame);

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_work_directory() {
		if ( null != _parent_directory)
			return true;

		File parent_directory = SoarsCommonTool.make_parent_directory();
		if ( null == parent_directory)
			return false;

		File root_directory = new File( parent_directory, _root_directory_name);
		if ( !root_directory.mkdirs()) {
			FileUtility.delete( parent_directory, true);
			return false;
		}

		_parent_directory = parent_directory;
		_root_directory = root_directory;

		return true;
	}

	/**
	 * @param parent_directory
	 * @return
	 */
	private boolean setup_work_directory(File parent_directory) {
		File root_directory = new File( parent_directory, _root_directory_name);
		if ( !root_directory.exists()) {
			cleanup();
			return false;
		}

		_parent_directory = parent_directory;
		_root_directory = root_directory;

		return true;
	}

	/**
	 * Returns true if the image directory exists.
	 * @return true if the image directory exists
	 */
	public boolean exist_image_directory() {
		if ( null == _parent_directory || null == _root_directory)
			return false;

		File directory = new File( _root_directory, Constant._imageDirectory);
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the image directory.
	 * @return the image directory
	 */
	public File get_image_directory() {
		if ( !setup_work_directory())
			return null;

		File directory = new File( _root_directory, Constant._imageDirectory);
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * Returns true if the thumbnail directory exists.
	 * @return true if the thumbnail directory exists
	 */
	public boolean exist_thumbnail_image_directory() {
		if ( null == _parent_directory || null == _root_directory)
			return false;

		File directory = new File( _root_directory, Constant._thumbnailImageDirectory);
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the thumbnail directory.
	 * @return the thumbnail directory
	 */
	public File get_thumbnail_image_directory() {
		if ( !setup_work_directory())
			return null;

		File directory = new File( _root_directory, Constant._thumbnailImageDirectory);
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * Returns true if the user's data directory exists.
	 * @return true if the user's data directory exists
	 */
	public boolean exist_user_data_directory() {
		if ( null == _parent_directory || null == _root_directory)
			return false;

		File directory = new File( _root_directory, Constant._userDataDirectory);
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the user's data directory.
	 * @return the user's data directory
	 */
	public File get_user_data_directory() {
		if ( !setup_work_directory())
			return null;

		File directory = new File( _root_directory, Constant._userDataDirectory);
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * 
	 */
	private void cleanup() {
		if ( null != _parent_directory)
			FileUtility.delete( _parent_directory, true);

		_root_directory = null;
		_parent_directory = null;
	}

	/**
	 * @return
	 */
	public String get_simulator_window_title() {
		return _simulator_window_title;
	}

	/**
	 * 
	 */
	private void get_property_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._main_window_rectangle_key + "x",
			String.valueOf( SwingTool.get_default_window_position( _minimum_width, _minimum_height).x));
		_window_rectangle.x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._main_window_rectangle_key + "y",
			String.valueOf( SwingTool.get_default_window_position( _minimum_width, _minimum_height).y));
		_window_rectangle.y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._main_window_rectangle_key + "width",
			String.valueOf( _minimum_width));
		_window_rectangle.width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._main_window_rectangle_key + "height",
			String.valueOf( _minimum_height));
		_window_rectangle.height = Integer.parseInt( value);
	}

	/**
	 * @return
	 */
	private void optimize_window_rectangle() {
		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( _window_rectangle)
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _window_rectangle).width <= 10
			|| _window_rectangle.y <= -getInsets().top
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _window_rectangle).height <= getInsets().top)
			_window_rectangle.setBounds(
				SwingTool.get_default_window_position( _minimum_width, _minimum_height).x,
				SwingTool.get_default_window_position( _minimum_width, _minimum_height).y,
				_minimum_width, _minimum_height);
	}

	/**
	 * 
	 */
	private void set_property_to_environment_file() throws IOException {
		if ( null != _logViewerFrame)
			_logViewerFrame.set_property_to_environment_file();

		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "x", String.valueOf( _window_rectangle.x));
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "y", String.valueOf( _window_rectangle.y));
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "width", String.valueOf( _window_rectangle.width));
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "height", String.valueOf( _window_rectangle.height));

		Environment.get_instance().store();
	}

	/**
	 * 
	 */
	private void setup_menu() {
		JToolBar statusBar = new JToolBar();

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		_message_label = new JLabel( "");
		//statusBar.add( _message_label);
		panel.add( _message_label);
		statusBar.add( panel);

		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		//panel.setLayout( new BorderLayout());

		//_information_label = new JLabel( "");
		_information_label = new JLabel( "                                             ");
		_information_label.setHorizontalAlignment( Label.RIGHT);
		//statusBar.add( _information_label);
		panel.add( _information_label);
		statusBar.add( panel);

		statusBar.setFloatable( false);

		//statusBar.setEnabled( false);

		getContentPane().add( statusBar, BorderLayout.SOUTH);




		JMenuBar menuBar = new JMenuBar();

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		JToolBar toolBar = new JToolBar();
		toolBar.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));

		if ( !Application._demo)
			setup_menu( menuBar, toolBar);
		else
			setup_menu_for_demo( menuBar, toolBar);

		setJMenuBar( menuBar);

		toolBar.setFloatable( false);

		//toolBar.setEnabled( false);

		north_panel.add( toolBar);


		if ( !_log_folder_path.equals( "")) {
			panel = new JPanel();
			panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

			panel.add( new JLabel( ResourceManager.get( "application.toolbar.log.folder.path") + _log_folder_path));
			north_panel.add( panel);

			insert_horizontal_glue( north_panel);
		}


		getContentPane().add( north_panel, BorderLayout.NORTH);
	}

	/**
	 * @param menuBar
	 * @param toolBar
	 */
	private void setup_menu(JMenuBar menuBar, JToolBar toolBar) {
		JMenu menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get( "file.menu"),
			true,
			ResourceManager.get( "file.mnemonic"),
			_message_label,
			ResourceManager.get( "file.message"));


		NewAction newAction = new NewAction( ResourceManager.get( "file.new.menu"));
		_file_new_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.new.menu"),
			newAction,
			ResourceManager.get( "file.new.mnemonic"),
			ResourceManager.get( "file.new.stroke"),
			_message_label,
			ResourceManager.get( "file.new.message"));


		OpenAction openAction = new OpenAction( ResourceManager.get( "file.open.menu"), this);
		_file_open_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.open.menu"),
			openAction,
			ResourceManager.get( "file.open.mnemonic"),
			ResourceManager.get( "file.open.stroke"),
			_message_label,
			ResourceManager.get( "file.open.message"));


		CloseAction closeAction = new CloseAction(
			ResourceManager.get( "file.close.menu"));
		_file_close_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.close.menu"),
			closeAction,
			ResourceManager.get( "file.close.mnemonic"),
			ResourceManager.get( "file.close.stroke"),
			_message_label,
			ResourceManager.get( "file.close.message"));


		menu.addSeparator();


		SaveAction saveAction = new SaveAction(
			ResourceManager.get( "file.save.menu"));
		_file_save_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.save.menu"),
			saveAction,
			ResourceManager.get( "file.save.mnemonic"),
			ResourceManager.get( "file.save.stroke"),
			_message_label,
			ResourceManager.get( "file.save.message"));


		SaveAsAction saveAsAction = new SaveAsAction( ResourceManager.get( "file.save.as.menu"), this);
		_file_save_as_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.save.as.menu"),
			saveAsAction,
			ResourceManager.get( "file.save.as.mnemonic"),
			ResourceManager.get( "file.save.as.stroke"),
			_message_label,
			ResourceManager.get( "file.save.as.message"));


		menu.addSeparator();


		ExitAction exitAction = new ExitAction( ResourceManager.get( "file.exit.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.exit.menu"),
			exitAction,
			ResourceManager.get( "file.exit.mnemonic"),
			ResourceManager.get( "file.exit.stroke"),
			_message_label,
			ResourceManager.get( "file.exit.message"));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get( "simulation.menu"),
			true,
			ResourceManager.get( "simulation.mnemonic"),
			_message_label,
			ResourceManager.get( "simulation.message"));


		StopAction stopAction = new StopAction( ResourceManager.get( "simulation.stop.menu"), this);
		_simulation_stop_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "simulation.stop.menu"),
			stopAction,
			ResourceManager.get( "simulation.stop.mnemonic"),
			ResourceManager.get( "simulation.stop.stroke"),
			_message_label,
			ResourceManager.get( "simulation.stop.message"));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get( "run.menu"),
			true,
			ResourceManager.get( "run.mnemonic"),
			_message_label,
			ResourceManager.get( "run.message"));


		AnimatorAction animatorAction = new AnimatorAction( ResourceManager.get( "run.animator.menu"), this);
		_run_animator_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "run.animator.menu"),
			animatorAction,
			ResourceManager.get( "run.animator.mnemonic"),
			ResourceManager.get( "run.animator.stroke"),
			_message_label,
			ResourceManager.get( "run.animator.message"));


		menu.addSeparator();


		FileManagerAction fileManagerAction = new FileManagerAction( ResourceManager.get( "run.file.manager.menu"), this);
		_run_file_manager_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "run.file.manager.menu"),
			fileManagerAction,
			ResourceManager.get( "run.file.manager.mnemonic"),
			ResourceManager.get( "run.file.manager.stroke"),
			_message_label,
			ResourceManager.get( "run.file.manager.message"));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get( "help.menu"),
			true,
			ResourceManager.get( "help.mnemonic"),
			_message_label,
			ResourceManager.get( "help.message"));


		ContentsAction contentsAction = new ContentsAction( ResourceManager.get( "help.contents.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get( "help.contents.menu"),
			contentsAction,
			ResourceManager.get( "help.contents.mnemonic"),
			ResourceManager.get( "help.contents.stroke"),
			_message_label,
			ResourceManager.get( "help.contents.message"));


		menu.addSeparator();


		ForumAction forumAction = new ForumAction( ResourceManager.get( "help.forum.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get( "help.forum.menu"),
			forumAction,
			ResourceManager.get( "help.forum.mnemonic"),
			ResourceManager.get( "help.forum.stroke"),
			_message_label,
			ResourceManager.get( "help.forum.message"));


		menu.addSeparator();


		AboutAction aboutAction = new AboutAction( ResourceManager.get( "help.about.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get( "help.about.menu"),
			aboutAction,
			ResourceManager.get( "help.about.mnemonic"),
			ResourceManager.get( "help.about.stroke"),
			_message_label,
			ResourceManager.get( "help.about.message"));


		//menuBar.setEnabled( false);



		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/file/app_exit.png"));
		JButton button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.exit.menu"),
			exitAction,
			ResourceManager.get( "file.exit.tooltip"),
			_message_label,
			ResourceManager.get( "file.exit.message"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/file/new.png"));
		_file_new_button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.new.menu"),
			newAction,
			ResourceManager.get( "file.new.tooltip"),
			_message_label,
			ResourceManager.get( "file.new.message"));
		_file_new_button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _file_new_button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/file/open.png"));
		_file_open_button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.open.menu"),
			openAction,
			ResourceManager.get( "file.open.tooltip"),
			_message_label,
			ResourceManager.get( "file.open.message"));
		_file_open_button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _file_open_button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/file/close.png"));
		_file_close_button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.close.menu"),
			closeAction,
			ResourceManager.get( "file.close.tooltip"),
			_message_label,
			ResourceManager.get( "file.close.message"));
		_file_close_button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _file_close_button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/file/save.png"));
		_file_save_button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.save.menu"),
			saveAction,
			ResourceManager.get( "file.save.tooltip"),
			_message_label,
			ResourceManager.get( "file.save.message"));
		_file_save_button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _file_save_button.getPreferredSize().height));


		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/file/save_as.png"));
		_file_save_as_button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.save.as.menu"),
			saveAsAction,
			ResourceManager.get( "file.save.as.tooltip"),
			_message_label,
			ResourceManager.get( "file.save.as.message"));
		_file_save_as_button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _file_save_as_button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/simulation/stop.png"));
		_simulation_stop_button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "simulation.stop.menu"),
			stopAction,
			ResourceManager.get( "simulation.stop.tooltip"),
			_message_label,
			ResourceManager.get( "simulation.stop.message"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/run/animator.png"));
		_run_animator_button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "run.animator.menu"),
			animatorAction,
			ResourceManager.get( "run.animator.tooltip"),
			_message_label,
			ResourceManager.get( "run.animator.message"));
		_run_animator_button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _run_animator_button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/run/file_manager.png"));
		_run_file_manager_button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "run.file.manager.menu"),
			fileManagerAction,
			ResourceManager.get( "run.file.manager.tooltip"),
			_message_label,
			ResourceManager.get( "run.file.manager.message"));
		_run_file_manager_button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _run_animator_button.getPreferredSize().height));


		initialize_user_interface();
	}

	/**
	 * @param menuBar
	 * @param toolBar
	 */
	private void setup_menu_for_demo(JMenuBar menuBar, JToolBar toolBar) {
		JMenu menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get( "file.menu"),
			true,
			ResourceManager.get( "file.mnemonic"),
			_message_label,
			ResourceManager.get( "file.message"));


		ExitAction exitAction = new ExitAction( ResourceManager.get( "file.exit.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.exit.menu"),
			exitAction,
			ResourceManager.get( "file.exit.mnemonic"),
			ResourceManager.get( "file.exit.stroke"),
			_message_label,
			ResourceManager.get( "file.exit.message"));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get( "simulation.menu"),
			true,
			ResourceManager.get( "simulation.mnemonic"),
			_message_label,
			ResourceManager.get( "simulation.message"));


		StopAction stopAction = new StopAction( ResourceManager.get( "simulation.stop.menu"), this);
		_simulation_stop_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "simulation.stop.menu"),
			stopAction,
			ResourceManager.get( "simulation.stop.mnemonic"),
			ResourceManager.get( "simulation.stop.stroke"),
			_message_label,
			ResourceManager.get( "simulation.stop.message"));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get( "run.menu"),
			true,
			ResourceManager.get( "run.mnemonic"),
			_message_label,
			ResourceManager.get( "run.message"));


		AnimatorAction animatorAction = new AnimatorAction( ResourceManager.get( "run.animator.menu"), this);
		_run_animator_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "run.animator.menu"),
			animatorAction,
			ResourceManager.get( "run.animator.mnemonic"),
			ResourceManager.get( "run.animator.stroke"),
			_message_label,
			ResourceManager.get( "run.animator.message"));


		menu.addSeparator();


		FileManagerAction fileManagerAction = new FileManagerAction( ResourceManager.get( "run.file.manager.menu"), this);
		_run_file_manager_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "run.file.manager.menu"),
			fileManagerAction,
			ResourceManager.get( "run.file.manager.mnemonic"),
			ResourceManager.get( "run.file.manager.stroke"),
			_message_label,
			ResourceManager.get( "run.file.manager.message"));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get( "help.menu"),
			true,
			ResourceManager.get( "help.mnemonic"),
			_message_label,
			ResourceManager.get( "help.message"));


		AboutAction aboutAction = new AboutAction( ResourceManager.get( "help.about.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get( "help.about.menu"),
			aboutAction,
			ResourceManager.get( "help.about.mnemonic"),
			ResourceManager.get( "help.about.stroke"),
			_message_label,
			ResourceManager.get( "help.about.message"));


		//menuBar.setEnabled( false);



		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/file/app_exit.png"));
		JButton button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.exit.menu"),
			exitAction,
			ResourceManager.get( "file.exit.tooltip"),
			_message_label,
			ResourceManager.get( "file.exit.message"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/simulation/stop.png"));
		_simulation_stop_button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "simulation.stop.menu"),
			stopAction,
			ResourceManager.get( "simulation.stop.tooltip"),
			_message_label,
			ResourceManager.get( "simulation.stop.message"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/run/animator.png"));
		_run_animator_button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "run.animator.menu"),
			animatorAction,
			ResourceManager.get( "run.animator.tooltip"),
			_message_label,
			ResourceManager.get( "run.animator.message"));
		_run_animator_button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _run_animator_button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/run/file_manager.png"));
		_run_file_manager_button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "run.file.manager.menu"),
			fileManagerAction,
			ResourceManager.get( "run.file.manager.tooltip"),
			_message_label,
			ResourceManager.get( "run.file.manager.message"));
		_run_file_manager_button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _run_animator_button.getPreferredSize().height));


		initialize_user_interface();
	}

	/**
	 * 
	 */
	private void initialize_user_interface() {
		setTitle( ResourceManager.get( "application.title"));
		update_user_interface( true, true, false, false, false, false, false, false);
		update( "                                             ");
		_current_file = null;
	}

	/**
	 * @param file_new
	 * @param file_open
	 * @param file_close
	 * @param file_save
	 * @param file_save_as
	 * @param stop_simulation
	 * @param run_animator
	 * @param run_file_manager
	 */
	private void update_user_interface(boolean file_new, boolean file_open,
		boolean file_close, boolean file_save, boolean file_save_as,
		boolean stop_simulation, boolean run_animator, boolean run_file_manager) {
		enable_menuItem( _file_new_menuItem, file_new);
		enable_menuItem( _file_open_menuItem, file_open);
		enable_menuItem( _file_close_menuItem, file_close);
		enable_menuItem( _file_save_menuItem, file_save);
		enable_menuItem( _file_save_as_menuItem, file_save_as);

		enable_menuItem( _simulation_stop_menuItem, stop_simulation);

		enable_menuItem( _run_animator_menuItem, run_animator);
		enable_menuItem( _run_file_manager_menuItem, run_file_manager);

		enable_button( _file_new_button, file_new);
		enable_button( _file_open_button, file_open);
		enable_button( _file_close_button, file_close);
		enable_button( _file_save_button, file_save);
		enable_button( _file_save_as_button, file_save_as);

		enable_button( _simulation_stop_button, stop_simulation);

		enable_button( _run_animator_button, run_animator);
		enable_button( _run_file_manager_button, run_file_manager);
	}

	/**
	 * @param menuItem
	 * @param enable
	 */
	private void enable_menuItem(JMenuItem menuItem, boolean enable) {
		if ( null == menuItem)
			return;

		menuItem.setEnabled( enable);
	}

	/**
	 * @param button
	 * @param enable
	 */
	private void enable_button(JButton button, boolean enable) {
		if ( null == button)
			return;

		button.setEnabled( enable);
	}

	/**
	 * @param modified
	 */
	public void modified(boolean modified) {
		if ( Application._demo)
			return;

		String title = "SOARS Simulator";
		if ( null != _current_file)
			title += ( " - " + _current_file.getName());

		if ( !_simulator_window_title.equals( ""))
			title += ( " - " + _simulator_window_title);

		if ( modified)
			title += ResourceManager.get( "state.edit.modified");

		setTitle( title);
		_modified = modified;
	}

	/**
	 * @param parent_directory
	 * @param log_folder_path 
	 * @return
	 */
	public boolean create(File parent_directory, String log_folder_path) {
		if ( !setup_work_directory( parent_directory))
			return false;

		_graphic_properties = FileUtility.read_text_from_file( new File( _root_directory, Constant._graphicPropertiesFilename), "UTF-8");
		if ( null == _graphic_properties)
			_graphic_properties = "";

		_chart_properties = FileUtility.read_text_from_file( new File( _root_directory, Constant._chartPropertiesFilename), "UTF-8");
		if ( null == _chart_properties)
			_chart_properties = "";

		_log_folder_path = log_folder_path;

		return super.create();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		if ( !MacUtility.setup_screen_menu_handler( this, this, ResourceManager.get( "application.title")))
			return false;

		_desktopPane.setBackground( _background_color);

		get_property_from_environment_file();

		_userInterface = new UserInterface();

		setup_menu();

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		pack();

		optimize_window_rectangle();
		setLocation( _window_rectangle.x, _window_rectangle.y);
		setSize( _window_rectangle.width, _window_rectangle.height);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		setVisible( true);

		new DropTarget( this, this);

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimum_width > width) ? _minimum_width : width,
					( _minimum_height > height) ? _minimum_height : height);
			}
		});

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_about()
	 */
	public void on_mac_about() {
		on_help_about( null);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_quit()
	 */
	public void on_mac_quit() {
		on_file_exit( null);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		if ( _modified) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		} else {
			if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
				this,
				ResourceManager.get( "file.exit.confirm.message"),
				ResourceManager.get( "application.title"),
				JOptionPane.YES_NO_OPTION)) {
				requestFocus();
				return;
			}
		}

		_window_rectangle = getBounds();
		try {
			set_property_to_environment_file();
		}
		catch (IOException e) {
			throw new RuntimeException( e);
		}

		stop_timer();

		if ( null != _logViewerFrame)
			_logViewerFrame.stop_simulation();

		cleanup();

		Application.get_instance().exit_instance();
	}

	/**
	 * @return
	 */
	public boolean createStaticLogViewerFrame() {
		_logViewerFrame = new StaticLogViewerFrame(
			ResourceManager.get( "log.viewer.window.title")/* + ( experiment.equals( "") ? "" : ( " - " + experiment))*/,
			true, false, true, true);
		if ( !_logViewerFrame.create())
			return false;

		Image image = Resource.load_image_from_resource( Constant._resource_directory + "/image/icon/icon.png", getClass());
		if ( null != image)
			_logViewerFrame.setFrameIcon( new ImageIcon( image));

		_desktopPane.add( _logViewerFrame);

		_logViewerFrame.toFront();

		_desktopPane.setSelectedFrame( _logViewerFrame);

		return true;
	}

	/**
	 * @param reader
	 * @param experiment
	 * @param vml
	 * @return
	 */
	public boolean start(Reader reader, String experiment, String vml) {
		if ( !experiment.equals( ""))
			_simulator_window_title = experiment;

		if ( !vml.equals( ""))
			_simulator_window_title += ( _simulator_window_title.equals( "") ? "" : " - ") + vml;

		if ( !_simulator_window_title.equals( ""))
			setTitle( getTitle() + " - " + _simulator_window_title);

		if ( !Application._demo) {
			setTitle( getTitle() + ResourceManager.get( "state.edit.modified"));
			_modified = true;
		}

		_logViewerFrame = new LiveLogViewerFrame(
			ResourceManager.get( "log.viewer.window.title") + ( experiment.equals( "") ? "" : ( " - " + experiment)),
			true, false, true, true);
		if ( !_logViewerFrame.create())
			return false;

		Image image = Resource.load_image_from_resource( Constant._resource_directory + "/image/icon/icon.png", getClass());
		if ( null != image)
			_logViewerFrame.setFrameIcon( new ImageIcon( image));

		_desktopPane.add( _logViewerFrame);

		_logViewerFrame.toFront();

		_desktopPane.setSelectedFrame( _logViewerFrame);

		_stdOutStreamPumper = new StdOutStreamPumper();
		_stdOutStreamPumper.start();

		_stdErrOutStreamPumper = new StdErrOutStreamPumper();
		_stdErrOutStreamPumper.start();

		if ( !_logViewerFrame.start( reader, _stdOutStreamPumper, _stdErrOutStreamPumper))
			return false;

		_logViewerFrame.optimize_window_rectangle();

		start_timer();

		update_user_interface( false, false, false, false, true, true, false, false);

		return true;
	}

	/**
	 * 
	 */
	public void on_start_simulation() {
		_environment = env.Environment.getCurrent();
	}

//	/**
//	 * 
//	 */
//	public void on_terminate_simulation() {
//		stop_timer();
//		_logViewerFrame.flush();
//		set_sensor( true);
//		update( "[ " + get_time() + " ]");
//		update_user_interface( true, true, false, false, true, false, _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists());
//	}

	/**
	 * 
	 */
	private void start_timer() {
		if ( null == _timer) {
			_timer = new Timer();
			_timerTaskImplement = new TimerTaskImplement( _id, this);
			_timer.schedule( _timerTaskImplement, _delay, _period);
		}
	}

	/**
	 * 
	 */
	private void stop_timer() {
		if ( null != _timer) {
			_timer.cancel();
			_timer = null;
		}

		if ( null != _stdOutStreamPumper)
			_stdOutStreamPumper.cleanup();

		if ( null != _stdErrOutStreamPumper)
			_stdErrOutStreamPumper.cleanup();

		_running = false;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.tool.timer.ITimerTaskImplementCallback#execute_timer_task(int)
	 */
	public void execute_timer_task(int id) {
		if ( _id != id)
			return;

		if ( _running)
			return;

		if ( null == _environment)
			return;

		_running = true;

		update( "[ " + get_time() + " ]");

		if ( _logViewerFrame._terminated) {
			stop_timer();
			_logViewerFrame.flush();
			set_sensor( true);
			update_user_interface( true, true, false, false, true, false, _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists(), exist_user_data_directory());
		}
//		if ( _logViewerFrame.terminated_normally()) {
//			update_user_interface( true, true, false, false, true, false, _logViewerFrame.dollar_spot_log_exists());
//			_stdOutStreamPumper.cleanup();
//			_stdErrOutStreamPumper.cleanup();
//			_logViewerFrame.flush();
//		}

		_running = false;
	}

	/**
	 * @return
	 */
	public String get_time() {
		if ( null == _environment)
			return _simulator_window_time;

		TimeCounter timeCounter = ( TimeCounter)_environment.getStepCounter();
		return timeCounter.getTime().toString();
	}

	/**
	 * @return
	 */
	private String get_actual_end_time() {
		String[] times = new String[] { "0", "00", "00"};
		String[] words = get_time().split( "/");
		times[ 0] = words[ 0];
		words = words[ 1].split( ":");
		times[ 1] = words[ 0];
		times[ 2] = words[ 1];
		return ( times[ 0] + "," + times[ 1] + "," + times[ 2]);
	}

	/**
	 * @param information
	 */
	public void update(String information) {
		_information_label.setText( information);
	}

	/**
	 * @param file
	 */
	private void open(File file) {
		if ( null == _logViewerFrame) {
			if ( !createStaticLogViewerFrame())
				return;
		}

		_logViewerFrame.stop_simulation();
		stop_timer();

		cleanup();

		if ( !MessageDlg.execute( this, ResourceManager.get( "application.title"), true, "on_file_open",
			ResourceManager.get( "file.open.show.message"), new Object[] { file}, this, this)) {
			JOptionPane.showMessageDialog( this, ResourceManager.get( "file.open.error.message"), ResourceManager.get( "application.title"), JOptionPane.ERROR_MESSAGE);
			close_all();
			initialize_user_interface();
			return;
		}

		_logViewerFrame.toBack();

		update_user_interface( true, true,
			null != _current_file,
			null != _current_file,
			true, false,
			_logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists(),
			exist_user_data_directory());

		boolean modified = false;
		if ( _logViewerFrame.optimize_window_rectangle())
			modified = true;

		JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
		for ( int i = 0; i < internalFrames.length; ++i) {
			if ( internalFrames[ i] instanceof LogViewerFrame)
				continue;

			if ( !( internalFrames[ i] instanceof ChartFrame))
				continue;

			ChartFrame chartFrame = ( ChartFrame)internalFrames[ i];
			if ( chartFrame.optimize_window_rectangle())
				modified = true;
		}

		FileManagerFrame fileManagerFrame = getFileManagerFrame();
		if ( null != fileManagerFrame && fileManagerFrame.optimize_window_rectangle())
			modified = true;
	
		modified( modified);

		set_sensor( true);
	}

	/**
	 * @param enable
	 */
	private void set_sensor(boolean enable) {
		JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
		for ( int i = 0; i < internalFrames.length; ++i) {
			if ( enable)
				enable_sensor( internalFrames[ i]);
			else {
				disable_sensor( internalFrames[ i]);
			}
		}
	}

	/**
	 * @param internalFrame
	 */
	private void enable_sensor(final JInternalFrame internalFrame) {
		internalFrame.addComponentListener( new ComponentListener() {
			public void componentHidden(ComponentEvent arg0) {
			}
			public void componentMoved(ComponentEvent arg0) {
				on_changed( internalFrame);
			}
			public void componentResized(ComponentEvent arg0) {
				on_changed( internalFrame);
			}
			public void componentShown(ComponentEvent arg0) {
			}
		});
	}

	/**
	 * @param internalFrame
	 */
	private void on_changed(JInternalFrame internalFrame) {
		if ( internalFrame.isMaximum() || internalFrame.isIcon()) {
			return;
		}

		Rectangle rectangle = ( Rectangle)_internalFrameRectangleMap.get( internalFrame);
		if ( null != rectangle && internalFrame.getBounds().equals( rectangle))
			return;

		modified( true);
	}

	/**
	 * @param internalFrame
	 */
	private void disable_sensor(JInternalFrame internalFrame) {
		ComponentListener[] componentListeners = internalFrame.getComponentListeners();
		for ( int j = 0; j < componentListeners.length; ++j)
			internalFrame.removeComponentListener( componentListeners[ j]);
	}

	/**
	 * SaxLoader
	 * @param filename
	 * @param simulator_window_title
	 * @param simulator_window_time
	 * @param log_viewer_window_title
	 * @param log_viewer_window_rectangle
	 * @param agents
	 * @param spots
	 * @param chartDataMap
	 * @param fileManagerData
	 * @return
	 */
	public boolean load(String filename, String simulator_window_title, String simulator_window_time, String log_viewer_window_title, Rectangle log_viewer_window_rectangle, List agents, List spots, Map<String, ChartData> chartDataMap, FileManagerData fileManagerData) {
		if ( null == log_viewer_window_rectangle || null == agents || null == spots)
			return false;


		if ( !read( agents, new File( _root_directory, "agents")))
			return false;

		if ( !read( spots, new File( _root_directory, "spots")))
			return false;


		String console = read( new File( _root_directory, Constant._console_filename));
		String stdout = read( new File( _root_directory, Constant._standard_out_filename));
		String stderr = read( new File( _root_directory, Constant._standard_error_filename));
		String graphic_properties = read( new File( _root_directory, Constant._graphicPropertiesFilename));
		String chart_properties = read( new File( _root_directory, Constant._chartPropertiesFilename));


		while ( close())
			;

		_internalFrameRectangleMap.clear();

		update( "[ " + simulator_window_time + " ]");

		if ( !_logViewerFrame.update( log_viewer_window_title, log_viewer_window_rectangle, console, agents, spots, stdout, stderr))
			return false;

		Collection<ChartData> chartDataList = chartDataMap.values();
			for ( ChartData chartData:chartDataList) {
				ChartFrame chartFrame = new ChartFrame( chartData._name, chartData._title, true, false, true, true);
				if ( !chartFrame.create( chartData, new File( _root_directory, Constant._chartLogDirectory))) {
//					_run_animator_menuItem.setEnabled( _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists());
//					_run_animator_button.setEnabled( _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists());
					return false;
				}

			_desktopPane.add( chartFrame);
		}

		if ( !createFileManagerFrame( fileManagerData))
			return false;

		_graphic_properties = graphic_properties;

		_chart_properties = chart_properties;

//		_run_animator_menuItem.setEnabled( _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists());
//		_run_animator_button.setEnabled( _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists());

		_simulator_window_title = simulator_window_title;
//		setTitle( "SOARS Simulator - " + filename + " - " + _simulator_window_title);

		_simulator_window_time = simulator_window_time;

		_internalFrameRectangleMap.update( _desktopPane);

		return true;
	}

	/**
	 * @param list
	 * @param directory
	 * @return
	 */
	private boolean read(List list, File directory) {
		for ( int i = 0; i < list.size(); ++i) {
			LogData logData = ( LogData)list.get( i);
			String value = FileUtility.read_text_from_file( new File( directory, logData._name + ".log"), "UTF-8");
			if ( null == value)
				return false;

			logData._value = value;
		}
		return true;
	}

	/**
	 * @param file
	 * @return
	 */
	private String read(File file) {
		if ( !file.exists() || !file.isFile() || !file.canRead())
			return "";

		String value = FileUtility.read_text_from_file( file, "UTF-8");
		if ( null == value)
			return "";

		return value;
	}

	/**
	 * @param fileManagerData
	 * @return
	 */
	private boolean createFileManagerFrame(FileManagerData fileManagerData) {
		if ( null == fileManagerData)
			return true;

		if ( null != fileManagerData && !exist_user_data_directory())
			return false;

		FileManagerFrame fileManagerFrame = getFileManagerFrame();
		if ( null != fileManagerFrame)
			return false;

		fileManagerFrame = new FileManagerFrame( ResourceManager.get( "file.manager.title"), true, true, true, true);
		if ( !fileManagerFrame.create( fileManagerData))
			return false;

		_desktopPane.add( fileManagerFrame);

		return true;
	}

	/**
	 * @return
	 */
	private FileManagerFrame getFileManagerFrame() {
		JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
		for ( int i = 0; i < internalFrames.length; ++i) {
			if ( internalFrames[ i] instanceof FileManagerFrame)
				return ( FileManagerFrame)internalFrames[ i];
		}
		return null;
	}

	/**
	 * LegacySaxLoader
	 * @param filename
	 * @param simulator_window_title
	 * @param simulator_window_time
	 * @param log_viewer_window_title
	 * @param log_viewer_window_rectangle
	 * @param console
	 * @param agents
	 * @param spots
	 * @param stdout
	 * @param stderr
	 * @param chartDatas
	 * @param graphic_properties
	 * @param chart_properties
	 * @return
	 */
	public boolean load(String filename, String simulator_window_title, String simulator_window_time, String log_viewer_window_title, Rectangle log_viewer_window_rectangle, String console, List agents, List spots, String stdout, String stderr, List chartDatas, String graphic_properties, String chart_properties) {
		if ( null == log_viewer_window_rectangle || null == console || null == agents || null == spots || null == stdout || null == stderr)
			return false;

		while ( close())
			;

		_internalFrameRectangleMap.clear();

		update( "[ " + simulator_window_time + " ]");

		if ( !_logViewerFrame.update( log_viewer_window_title, log_viewer_window_rectangle, console, agents, spots, stdout, stderr))
			return false;

		for ( int i = 0; i < chartDatas.size(); ++i) {
			ChartData chartData = ( ChartData)chartDatas.get( i);
			ChartFrame chartFrame = new ChartFrame( chartData._name, chartData._title, true, false, true, true);
			if ( !chartFrame.create( chartData)) {
//				_run_animator_menuItem.setEnabled( _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists());
//				_run_animator_button.setEnabled( _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists());
				return false;
			}

			_desktopPane.add( chartFrame);
		}

		_graphic_properties = graphic_properties;

		_chart_properties = chart_properties;

//		_run_animator_menuItem.setEnabled( _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists());
//		_run_animator_button.setEnabled( _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists());

		_simulator_window_title = simulator_window_title;
//		setTitle( "SOARS Simulator - " + filename + " - " + _simulator_window_title);

		_simulator_window_time = simulator_window_time;

		_internalFrameRectangleMap.update( _desktopPane);

		return true;
	}

	/**
	 * @return
	 */
	private boolean close() {
		JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
		for ( int i = 0; i < internalFrames.length; ++i) {
			if ( internalFrames[ i] instanceof LogViewerFrame) {
				disable_sensor( internalFrames[ i]);
				continue;
			}

			internalFrames[ i].dispose();
			return true;
		}

		return false;
	}

	/**
	 * 
	 */
	private void close_all() {
		if ( null != _logViewerFrame)
			_logViewerFrame.stop_simulation();

		stop_timer();

		JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
		for ( int i = 0; i < internalFrames.length; ++i)
			internalFrames[ i].dispose();

		_logViewerFrame = null;

		_simulator_window_title = "";
		_internalFrameRectangleMap.clear();
		modified( false);

		cleanup();
	}

	/**
	 * @return
	 */
	private int confirm() {
		int result = JOptionPane.showConfirmDialog(
			this,
			ResourceManager.get( "file.close.confirm.message"),
			ResourceManager.get( "application.title"),
			JOptionPane.YES_NO_CANCEL_OPTION);
		switch ( result) {
			case JOptionPane.YES_OPTION:
				if ( null != _current_file)
					on_file_save( null);
				else {
					if ( !on_file_save_as())
						result = JOptionPane.CANCEL_OPTION;
				}
				break;
			case JOptionPane.CANCEL_OPTION:
			case JOptionPane.CLOSED_OPTION:
				requestFocus();
				break;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_exit(java.awt.event.ActionEvent)
	 */
	public void on_file_exit(ActionEvent actionEvent) {
		if ( _modified) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		} else {
			if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
				this,
				ResourceManager.get( "file.exit.confirm.message"),
				ResourceManager.get( "application.title"),
				JOptionPane.YES_NO_OPTION)) {
				requestFocus();
				return;
			}
		}

		_window_rectangle = getBounds();
		try {
			set_property_to_environment_file();
		}
		catch (IOException e) {
			throw new RuntimeException( e);
		}

		stop_timer();

		if ( null != _logViewerFrame)
			_logViewerFrame.stop_simulation();

		cleanup();

		Application.get_instance().exit_instance();
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_new(java.awt.event.ActionEvent)
	 */
	public void on_file_new(ActionEvent actionEvent) {
		if ( _modified) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		}

		close_all();
		initialize_user_interface();
		requestFocus();
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_open(java.awt.event.ActionEvent)
	 */
	public void on_file_open(ActionEvent actionEvent) {
		if ( _modified) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		}

		File file = CommonTool.get_open_file(
			Environment._open_directory_key,
			ResourceManager.get( "file.open.dialog"),
			new String[] { "sim", "sml"},
			"soars simulator data",
			this);

		requestFocus();

		if ( null == file)
			return;

		open( file);
	}

	/**
	 * @param file
	 */
	private void on_file_open_by_drag_and_drop(File file) {
		if ( _modified) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		}

		requestFocus();

		if ( null == file)
			return;

		open( file);
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_close(java.awt.event.ActionEvent)
	 */
	public void on_file_close(ActionEvent actionEvent) {
		if ( _modified) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		}

		close_all();
		initialize_user_interface();
		requestFocus();
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_save(java.awt.event.ActionEvent)
	 */
	public void on_file_save(ActionEvent actionEvent) {
		if ( null == _current_file) {
			JOptionPane.showMessageDialog( this, ResourceManager.get( "file.save.error.message"), ResourceManager.get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		if ( !MessageDlg.execute( this, ResourceManager.get( "application.title"), true,
			"on_file_save_as", ResourceManager.get( "file.save.show.message"),
			new Object[] { _current_file}, this, this)) {
			JOptionPane.showMessageDialog( this, ResourceManager.get( "file.save.error.message"), ResourceManager.get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		_internalFrameRectangleMap.update( _desktopPane);

		modified( false);
		requestFocus();
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_save_as(java.awt.event.ActionEvent)
	 */
	public void on_file_save_as(ActionEvent actionEvent) {
		on_file_save_as();
	}

	/**
	 * @return
	 */
	private boolean on_file_save_as() {
		File file = CommonTool.get_save_file(
			Environment._save_as_directory_key,
			ResourceManager.get( "file.save.as.dialog"),
			new String[] { "sim"},
			"soars simulator data",
			this);

		requestFocus();

		if ( null == file)
			return false;

		String absolute_name = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absolute_name + ".sim");
		else if ( name.length() - 1 == index)
			file = new File( absolute_name + "sim");

		if ( !MessageDlg.execute( this, ResourceManager.get( "application.title"), true,
			"on_file_save_as", ResourceManager.get( "file.save.as.show.message"),
			new Object[] { file}, this, this)) {
			JOptionPane.showMessageDialog( this, ResourceManager.get( "file.save.as.error.message"), ResourceManager.get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return false;
		}

		_internalFrameRectangleMap.update( _desktopPane);

		update_user_interface( true, true, true, true, true, false, _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists(), exist_user_data_directory());

		_current_file = file;

		modified( false);

		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		if ( null == _logViewerFrame)
			return false;

		if ( !_logViewerFrame.write( _root_directory, writer))
			return false;

		if ( !write_chart_data( writer))
			return false;

		if ( !write_file_manager_data( writer))
			return false;

		if ( !write_graphic_properties( writer))
			return false;

		if ( !write_chart_properties( writer))
			return false;

		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 * @throws NumberFormatException
	 */
	private boolean write_chart_data(Writer writer) throws NumberFormatException, SAXException {
		String[] lines = _chart_properties.split( "\n");
		if ( null == lines)
			return false;

		if ( 7 > lines.length)
			return true;

		for ( int i = 6; i < lines.length; ++i) {
			if ( !write_chart_data( lines[ i], writer))
				return false;
		}

		return true;
	}

	/**
	 * @param line
	 * @param writer
	 * @return
	 * @throws SAXException
	 * @throws NumberFormatException
	 */
	private boolean write_chart_data(String line, Writer writer) throws NumberFormatException, SAXException {
		String[] words = line.split( "\t");
		if ( null == words || 2 > words.length)
			return false;

		JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
		for ( int i = 0; i < internalFrames.length; ++i) {
			if ( internalFrames[ i] instanceof LogViewerFrame)
				continue;

			if ( internalFrames[ i] instanceof InternalChartFrame) {
				InternalChartFrame internalChartFrame = ( InternalChartFrame)internalFrames[ i];
				if ( !internalChartFrame._name.equals( words[ 0]))
					continue;

				File chart_log_directory = new File( _root_directory, Constant._chartLogDirectory);
				if ( !chart_log_directory.exists() && !chart_log_directory.mkdir())
					return false;

				if ( !internalChartFrame.write( Integer.parseInt( words[ 1]),
					new File( chart_log_directory, words[ 0] + "_" + words[ 1] + ".log"),
					words[ 11].equals( "true"), writer))
					return false;
			} else if ( internalFrames[ i] instanceof ChartFrame) {
				ChartFrame chartFrame = ( ChartFrame)internalFrames[ i];
				if ( !chartFrame._name.equals( words[ 0]))
					continue;

				File chart_log_directory = new File( _root_directory, Constant._chartLogDirectory);
				if ( !chart_log_directory.exists() && !chart_log_directory.mkdir())
					return false;

				if ( !chartFrame.write( Integer.parseInt( words[ 1]),
					new File( chart_log_directory, words[ 0] + "_" + words[ 1] + ".log"),
					words[ 11].equals( "true"), writer))
					return false;
			}
		}

		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_file_manager_data(Writer writer) throws SAXException {
		if ( !exist_user_data_directory())
			return true;

		FileManagerFrame fileManagerFrame = getFileManagerFrame();
		if ( null == fileManagerFrame)
			return true;

		return fileManagerFrame.write( writer);
	}

	/**
	 * @param writer
	 * @return
	 */
	private boolean write_graphic_properties(Writer writer) {
		if ( _graphic_properties.equals( ""))
			return true;

		File file = new File( _root_directory, Constant._graphicPropertiesFilename);
		if ( file.exists())
			return true;

		return FileUtility.write_text_to_file( file, _graphic_properties, "UTF-8");
	}

	/**
	 * @param writer
	 * @return
	 */
	private boolean write_chart_properties(Writer writer) {
		if ( _chart_properties.equals( ""))
			return true;

		File file = new File( _root_directory, Constant._chartPropertiesFilename);
		if ( file.exists())
			return true;

		return FileUtility.write_text_to_file( file, _chart_properties, "UTF-8");
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_save_image_as(java.awt.event.ActionEvent)
	 */
	public void on_file_save_image_as(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.simulation.ISimulationMenuHandler#on_simulation_stop(java.awt.event.ActionEvent)
	 */
	public void on_simulation_stop(ActionEvent actionEvent) {
		if ( null == _logViewerFrame)
			return;

		_logViewerFrame.stop_simulation();
		stop_timer();

		update_user_interface( true, true, false, false, true, false, _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists(), exist_user_data_directory());
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.run.IRunMenuHandler#on_run_animator(java.awt.event.ActionEvent)
	 */
	public void on_run_animator(ActionEvent actionEvent) {
		if ( null == _logViewerFrame)
			return;

		if ( !MessageDlg.execute( this, ResourceManager.get( "application.title"), true, "on_run_animator",
			ResourceManager.get( "run.animator.show.message"), new Object[] { actionEvent}, this, this)) {
			JOptionPane.showMessageDialog( this, ResourceManager.get( "run.animator.error.message"), ResourceManager.get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.run.IRunMenuHandler#on_run_file_manager(java.awt.event.ActionEvent)
	 */
	public void on_run_file_manager(ActionEvent actionEvent) {
		if ( !exist_user_data_directory())
			return;

		FileManagerFrame fileManagerFrame = getFileManagerFrame();
		if ( null != fileManagerFrame) {
			if ( fileManagerFrame.isVisible())
				fileManagerFrame.setVisible( false);
			else {
				fileManagerFrame.setVisible( true);
				fileManagerFrame.toFront();
			}
		} else {
			fileManagerFrame = new FileManagerFrame( ResourceManager.get( "file.manager.title"), true, true, true, true);
			if ( !fileManagerFrame.create())
				return;

			if ( !append( fileManagerFrame))
					return;

//			_desktopPane.add( fileManagerFrame);
//
//			fileManagerFrame.toFront();
		}

		modified( true);
	}

	/**
	 * @param root_directory
	 * @return
	 */
	public boolean setup_chart_data_files(File root_directory) {
		String[] lines = _chart_properties.split( "\n");
		if ( null == lines)
			return false;

		if ( 7 > lines.length)
			return true;

		for ( int i = 6; i < lines.length; ++i) {
			if ( !setup_chart_data_file( lines[ i], root_directory))
				return false;
		}

		return true;
	}

	/**
	 * @param line
	 * @param root_directory
	 * @return
	 */
	private boolean setup_chart_data_file(String line, File root_directory) {
		String[] words = line.split( "\t");
		if ( null == words || 2 > words.length)
			return false;

		JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
		for ( int i = 0; i < internalFrames.length; ++i) {
			if ( internalFrames[ i] instanceof LogViewerFrame)
				continue;

			if ( internalFrames[ i] instanceof InternalChartFrame) {
				InternalChartFrame internalChartFrame = ( InternalChartFrame)internalFrames[ i];
				if ( !internalChartFrame._name.equals( words[ 0]))
					continue;

				File chart_log_directory = new File( root_directory, Constant._chartLogDirectory);
				if ( !chart_log_directory.exists() && !chart_log_directory.mkdir())
					return false;

				if ( !internalChartFrame.write( Integer.parseInt( words[ 1]),
					new File( chart_log_directory, words[ 0] + "_" + words[ 1] + ".log")))
					return false;
			} else if ( internalFrames[ i] instanceof ChartFrame) {
				ChartFrame chartFrame = ( ChartFrame)internalFrames[ i];
				if ( !chartFrame._name.equals( words[ 0]))
					continue;

				File chart_log_directory = new File( root_directory, Constant._chartLogDirectory);
				if ( !chart_log_directory.exists() && !chart_log_directory.mkdir())
					return false;

				if ( !chartFrame.write( Integer.parseInt( words[ 1]),
					new File( chart_log_directory, words[ 0] + "_" + words[ 1] + ".log")))
					return false;
			}
		}

		return true;
	}

	/**
	 * @param root_directory
	 * @return
	 */
	public boolean setup_image_files(File root_directory) {
		if ( !exist_image_directory())
			return true;

		return FileUtility.copy_all( get_image_directory(), new File( root_directory, Constant._imageDirectory));
	}

	/**
	 * @param root_directory
	 * @return
	 */
	public boolean setup_thumbnail_image_files(File root_directory) {
		if ( !exist_thumbnail_image_directory())
			return true;

		return FileUtility.copy_all( get_thumbnail_image_directory(), new File( root_directory, Constant._thumbnailImageDirectory));
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.help.IHelpMenuHandler#on_help_contents(java.awt.event.ActionEvent)
	 */
	public void on_help_contents(ActionEvent actionEvent) {
		String current_directory_name = System.getProperty( Constant._soarsHome);
		if ( null == current_directory_name)
			return;

		File file = new File( current_directory_name + "/"
			+ ResourceManager.get( "help.contents.url"));
		if ( !file.exists() || !file.canRead())
			return;

		try {
			BrowserLauncher.openURL( file.toURI().toURL().toString().replaceAll( "\\\\", "/"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//BrowserLauncher.openURL( "file:///" + file.getAbsolutePath().replaceAll( "\\\\", "/"));
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.help.IHelpMenuHandler#on_help_forum(java.awt.event.ActionEvent)
	 */
	public void on_help_forum(ActionEvent actionEvent) {
		String language = Locale.getDefault().getLanguage();
		if ( null == language)
			return;

		String url = SoarsCommonEnvironment.get_instance().getProperty(
			language.equals( "ja") ? SoarsCommonEnvironment._forumJaUrlKey : SoarsCommonEnvironment._forumEnUrlKey);
		if ( null == url)
			return;

		BrowserLauncher.openURL( url);
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.help.IHelpMenuHandler#on_help_about(java.awt.event.ActionEvent)
	 */
	public void on_help_about(ActionEvent actionEvent) {
		JOptionPane.showMessageDialog( this,
			Constant.get_version_message(),
			ResourceManager.get( "application.title"),
//				JOptionPane.INFORMATION_MESSAGE,
//				new ImageIcon( Resource.load_image_from_resource( Constant._resource_directory + "/image/picture/picture.jpg", getClass())));
			JOptionPane.INFORMATION_MESSAGE);

		requestFocus();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "on_file_open"))
			return load( ( File)objects[ 0]);
		else if ( id.equals( "on_file_save_as"))
			return write( ( File)objects[ 0]);
		else if ( id.equals( "on_run_animator"))
			return _logViewerFrame.on_run_animator( _graphic_properties, get_actual_end_time() + "\n" + _chart_properties, ( ActionEvent)objects[ 0]);

		return true;
	}

	/**
	 * Returns true if loading the data from the specified file successfully.
	 * @param file the specified file
	 * @return true if loading the data from the specified file successfully
	 */
	private boolean load(File file) {
		File parent_directory = SoarsCommonTool.make_parent_directory();
		if ( null == parent_directory)
			return false;

		if ( !ZipUtility.decompress( file, parent_directory)) {
			FileUtility.delete( parent_directory, true);
			return false;
		}

		cleanup();

		File root_directory = new File( parent_directory, _root_directory_name);
		if ( !root_directory.exists() || !root_directory.isDirectory()) {
			root_directory = new File( parent_directory, _root_directory_name);
			if ( !root_directory.mkdirs()) {
				FileUtility.delete( parent_directory, true);
				return false;
			}

			_parent_directory = parent_directory;
			_root_directory = root_directory;

			if ( !LegacySaxLoader.execute( file)) {
				cleanup();
				return false;
			}
		} else {
			_parent_directory = parent_directory;
			_root_directory = root_directory;

			if ( !SaxLoader.execute( new File( _root_directory, _data_filename))) {
				cleanup();
				return false;
			}

			_current_file = file;
		}

		return true;
	}

	/**
	 * @param file
	 * @return
	 */
	private boolean write(File file) {
		if ( !setup_work_directory())
			return false;

		if ( !SaxWriter.execute( new File( _root_directory, _data_filename)))
			return false;

		if ( !ZipUtility.compress( file, _root_directory, _parent_directory))
			return false;

		_current_file = file;
		_modified = false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY_OR_MOVE);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
		if ( !_file_open_menuItem.isEnabled() || !_file_open_button.isEnabled()) {
			arg0.rejectDrop();
			return;
		}

		try {
			Transferable transferable = arg0.getTransferable();
			if ( transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)) {
				arg0.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE);
				List list = ( List)transferable.getTransferData( DataFlavor.javaFileListFlavor);
				if ( list.isEmpty()) {
					arg0.getDropTargetContext().dropComplete( true);
					return;
				}

				File file =( File)list.get( 0);
				arg0.getDropTargetContext().dropComplete( true);
				requestFocus();
				on_file_open_by_drag_and_drop( file);
			} else if ( transferable.isDataFlavorSupported( DataFlavor.stringFlavor)) {
				arg0.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE);
				String string = ( String)transferable.getTransferData( DataFlavor.stringFlavor);
				arg0.getDropTargetContext().dropComplete( true);
				String[] files = string.split( System.getProperty( "line.separator"));
				if ( files.length <= 0)
					arg0.rejectDrop();
				else
					on_file_open_by_drag_and_drop( new File( new URI( files[ 0].replaceAll( "[\r\n]", ""))));
			} else {
				arg0.rejectDrop();
			}
		} catch (IOException ioe) {
			arg0.rejectDrop();
		} catch (UnsupportedFlavorException ufe) {
			arg0.rejectDrop();
		} catch (URISyntaxException e) {
			arg0.rejectDrop();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
	}
}
