/**
 * 
 */
package soars.application.builder.animation.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.builder.animation.common.tool.CommonTool;
import soars.application.builder.animation.document.DocumentManager;
import soars.application.builder.animation.main.panel.MainPanel;
import soars.application.builder.animation.main.text.AnimatorFileTextField;
import soars.application.builder.animation.main.tree.LanguageTree;
import soars.application.builder.animation.menu.file.CloseAction;
import soars.application.builder.animation.menu.file.ExitAction;
import soars.application.builder.animation.menu.file.ExportArchiveAction;
import soars.application.builder.animation.menu.file.NewAction;
import soars.application.builder.animation.menu.file.OpenAction;
import soars.application.builder.animation.menu.file.SaveAction;
import soars.application.builder.animation.menu.file.SaveAsAction;
import soars.application.builder.animation.menu.help.AboutAction;
import soars.application.builder.animation.menu.help.ForumAction;
import soars.application.builder.animation.menu.run.ApplicationAction;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.mac.IMacScreenMenuHandler;
import soars.common.utility.swing.mac.MacUtility;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.network.BrowserLauncher;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class MainFrame extends Frame implements IMacScreenMenuHandler, IMessageCallback {

	/**
	 * 
	 */
	static public final int _minimum_width = 800;

	/**
	 * 
	 */
	static public final int _minimum_height = 600;

	/**
	 * 
	 */
	static public final String _animator_builder_extension = "abml";

	/**
	 * 
	 */
	static public final String _export_archive_extension = "zip";

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
	private JMenuItem _file_export_archive_menuItem = null;

	/**
	 * 
	 */
	private JMenuItem _run_application_menuItem = null;

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
	private JButton _file_export_archive_button = null;

	/**
	 * 
	 */
	private JButton _run_application_button = null;

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
	private JLabel _animatorFileLabel = null;

	/**
	 * 
	 */
	private AnimatorFileTextField _animatorFileTextField = null;

	/**
	 * 
	 */
	private JLabel _languageLabel = null;

	/**
	 * 
	 */
	private JComboBox _languageComboBox = null;

	/**
	 * 
	 */
	private JSplitPane _splitPane = null;

	/**
	 * 
	 */
	private LanguageTree _languageTree = null;

	/**
	 * 
	 */
	private MainPanel _mainPanel = null;

	/**
	 * 
	 */
	private boolean _ignore = false;

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
	 * @param arg0
	 * @throws HeadlessException
	 */
	public MainFrame(String arg0) throws HeadlessException {
		super(arg0);
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
	private void set_property_to_environment_file() {
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "x", String.valueOf( _window_rectangle.x));
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "y", String.valueOf( _window_rectangle.y));
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "width", String.valueOf( _window_rectangle.width));
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "height", String.valueOf( _window_rectangle.height));

		Environment.get_instance().set(
			Environment._main_window_divider_location_key, String.valueOf( _splitPane.getDividerLocation()));

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

		JToolBar toolBar = new JToolBar();
		toolBar.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));

		setup_menu( menuBar, toolBar);

		setJMenuBar( menuBar);

		toolBar.setFloatable( false);

		getContentPane().add( toolBar, BorderLayout.NORTH);
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


		OpenAction openAction = new OpenAction(
			ResourceManager.get( "file.open.menu"));
		_file_open_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.open.menu"),
			openAction,
			ResourceManager.get( "file.open.mnemonic"),
			ResourceManager.get( "file.open.stroke"),
			_message_label,
			ResourceManager.get( "file.open.message"));


		menu.addSeparator();


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
		_file_close_menuItem.setEnabled( false);


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
		_file_save_menuItem.setEnabled( false);


		SaveAsAction saveAsAction = new SaveAsAction(
			ResourceManager.get( "file.saveas.menu"));
		_file_save_as_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.saveas.menu"),
			saveAsAction,
			ResourceManager.get( "file.saveas.mnemonic"),
			ResourceManager.get( "file.saveas.stroke"),
			_message_label,
			ResourceManager.get( "file.saveas.message"));
		//_file_save_as_menuItem.setEnabled( false);


		menu.addSeparator();


		ExportArchiveAction exportArchiveAction = new ExportArchiveAction(
			ResourceManager.get( "file.export.archive.menu"));
		_file_export_archive_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.export.archive.menu"),
			exportArchiveAction,
			ResourceManager.get( "file.export.archive.mnemonic"),
			ResourceManager.get( "file.export.archive.stroke"),
			_message_label,
			ResourceManager.get( "file.export.archive.message"));
		_file_export_archive_menuItem.setEnabled( false);


		menu.addSeparator();


		ExitAction exitAction = new ExitAction(
			ResourceManager.get( "file.exit.menu"));
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
			ResourceManager.get( "run.menu"),
			true,
			ResourceManager.get( "run.mnemonic"),
			_message_label,
			ResourceManager.get( "run.message"));


		ApplicationAction applicationAction = new ApplicationAction(
			ResourceManager.get( "run.application.menu"));
		_run_application_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "run.application.menu"),
			applicationAction,
			ResourceManager.get( "run.application.mnemonic"),
			ResourceManager.get( "run.application.stroke"),
			_message_label,
			ResourceManager.get( "run.application.message"));
		_run_application_menuItem.setEnabled( false);



		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get( "help.menu"),
			true,
			ResourceManager.get( "help.mnemonic"),
			_message_label,
			ResourceManager.get( "help.message"));


//		ContentsAction contentsAction = new ContentsAction(
//			ResourceManager.get( "help.contents.menu"));
//		_userInterface.append_menuitem(
//			menu,
//			ResourceManager.get( "help.contents.menu"),
//			contentsAction,
//			ResourceManager.get( "help.contents.mnemonic"),
//			ResourceManager.get( "help.contents.stroke"),
//			_message_label,
//			ResourceManager.get( "help.contents.message"));
//
//
//		menu.addSeparator();


		ForumAction forumAction = new ForumAction(
			ResourceManager.get( "help.forum.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get( "help.forum.menu"),
			forumAction,
			ResourceManager.get( "help.forum.mnemonic"),
			ResourceManager.get( "help.forum.stroke"),
			_message_label,
			ResourceManager.get( "help.forum.message"));


		menu.addSeparator();


		AboutAction aboutAction = new AboutAction(
			ResourceManager.get( "help.about.menu"));
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
		_file_close_button.setEnabled( false);


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
		_file_save_button.setEnabled( false);


		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/file/save_as.png"));
		_file_save_as_button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.saveas.menu"),
			saveAsAction,
			ResourceManager.get( "file.saveas.tooltip"),
			_message_label,
			ResourceManager.get( "file.saveas.message"));
		_file_save_as_button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _file_save_as_button.getPreferredSize().height));
		//_file_save_as_button.setEnabled( false);


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/file/export_archive.png"));
		_file_export_archive_button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.export.archive.menu"),
			exportArchiveAction,
			ResourceManager.get( "file.export.archive.tooltip"),
			_message_label,
			ResourceManager.get( "file.export.archive.message"));
		_file_export_archive_button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _file_export_archive_button.getPreferredSize().height));
		_file_export_archive_button.setEnabled( false);


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/run/application.png"));
		_run_application_button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "run.application.menu"),
			applicationAction,
			ResourceManager.get( "run.application.tooltip"),
			_message_label,
			ResourceManager.get( "run.application.message"));
		_run_application_button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _run_application_button.getPreferredSize().height));
		_run_application_button.setEnabled( false);
	}

	/**
	 * 
	 */
	private void initialize_user_interface() {
		setTitle( ResourceManager.get( "application.title"));
		update_user_interface( true, true, false, false, true, false, false);
		update( "                                             ");
	}

	/**
	 * @param file_new
	 * @param file_open
	 * @param file_close
	 * @param file_save
	 * @param file_save_as
	 * @param file_export_archive
	 * @param run_application
	 */
	private void update_user_interface(boolean file_new, boolean file_open,
		boolean file_close, boolean file_save, boolean file_save_as,
		boolean file_export_archive, boolean run_application) {
		enable_menuItem( _file_new_menuItem, file_new);
		enable_menuItem( _file_open_menuItem, file_open);
		enable_menuItem( _file_close_menuItem, file_close);
		enable_menuItem( _file_save_menuItem, file_save);
		enable_menuItem( _file_save_as_menuItem, file_save_as);
		enable_menuItem( _file_export_archive_menuItem, file_export_archive);

		enable_menuItem( _run_application_menuItem, run_application);

		enable_button( _file_new_button, file_new);
		enable_button( _file_open_button, file_open);
		enable_button( _file_close_button, file_close);
		enable_button( _file_save_button, file_save);
		enable_button( _file_save_as_button, file_save_as);
		enable_button( _file_export_archive_button, file_export_archive);

		enable_button( _run_application_button, run_application);
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
	 * 
	 */
	private void reset() {
		_mainPanel.reset();
		set_animator_file( "");
		DocumentManager.get_instance().reset();
		initialize_user_interface();
	}

	/**
	 * @param language
	 */
	public void set_selected_language(String language) {
		_ignore = true;
		_languageComboBox.setSelectedItem( DocumentManager.get_instance().get_language_name( language));
		_ignore = false;
	}

	/**
	 * @param filename
	 */
	public void set_animator_file(String filename) {
		_ignore = true;
		_animatorFileTextField.setText( filename);
		_ignore = false;
	}

	/**
	 * @return
	 */
	private boolean animator_file_exists() {
		String filename = _animatorFileTextField.getText();
		if ( null == filename || filename.equals( ""))
			return false;

		File file = new File( _animatorFileTextField.getText());
		return ( file.exists() && file.isFile() && file.canRead());
	}

	/**
	 * @param file
	 */
	public void open(File file) {
		if ( !MessageDlg.execute( this, ResourceManager.get( "application.title"), true,
			"on_file_open", ResourceManager.get( "file.open.show.message"), new Object[] { file}, this, this)) {
			reset();
			return;
		}

		_languageTree.load();

		setTitle( ResourceManager.get( "application.title") + " - " + file.getName());

		update_user_interface( true, true,
			DocumentManager.get_instance().exist_datafile(),
			DocumentManager.get_instance().exist_datafile(),
			true, animator_file_exists(), animator_file_exists());
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "on_file_open"))
			return DocumentManager.get_instance().load( ( File)objects[ 0]);
		else if ( id.equals( "on_file_save"))
			return DocumentManager.get_instance().save();
		else if ( id.equals( "on_file_save_as"))
			return DocumentManager.get_instance().save_as( ( File)objects[ 0]);
		else if ( id.equals( "on_file_export_archive"))
			return DocumentManager.get_instance().export_archive( ( File)objects[ 0], ( String)objects[ 1], ( String)objects[ 2]);
		else if ( id.equals( "on_run_application"))
			return DocumentManager.get_instance().run_application( ( String)objects[ 0], ( String)objects[ 1]);

		return true;
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
				if ( DocumentManager.get_instance().exist_datafile())
					on_file_save( null);
				else {
					if ( !on_file_save_as( null))
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
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		if ( !MacUtility.setup_screen_menu_handler( this, this, ResourceManager.get( "application.title")))
			return false;

		get_property_from_environment_file();

		_userInterface = new UserInterface();

		if ( !setup())
			return false;

		setup_menu();

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		pack();

		optimize_window_rectangle();
		setLocation( _window_rectangle.x, _window_rectangle.y);
		setSize( _window_rectangle.width, _window_rectangle.height);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		setVisible( true);

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

	/**
	 * @return
	 */
	private boolean setup() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());


		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		setup_animatorFileTextField( north_panel);

		insert_horizontal_glue( north_panel);

		setup_languageComboBox( north_panel);

		insert_horizontal_glue( north_panel);

		panel.add( north_panel, "North");


		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		if ( !setup_center_panel( center_panel))
			return false;

		panel.add( center_panel);


		adjust();


		getContentPane().add( panel);


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_animatorFileTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_animatorFileLabel = new JLabel( ResourceManager.get( "main.frame.animator.file.label"));
		_animatorFileLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _animatorFileLabel);

		panel.add( Box.createHorizontalStrut( 5));

		_animatorFileTextField = new AnimatorFileTextField();
		_animatorFileTextField.getDocument().addDocumentListener( new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				on_update();
			}
			public void insertUpdate(DocumentEvent e) {
				on_update();
			}
			public void removeUpdate(DocumentEvent e) {
				on_update();
			}
		});
		_animatorFileTextField.setEditable( false);
		panel.add( _animatorFileTextField);

		panel.add( Box.createHorizontalStrut( 5));

		JButton button = new JButton( ResourceManager.get( "main.frame.animator.file.reference.button"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_animator_file_selector_button_actionPerformed();
			}
		});
		panel.add( button);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_languageComboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_languageLabel = new JLabel( ResourceManager.get( "main.frame.language.label"));
		_languageLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _languageLabel);

		_languageComboBox = new JComboBox( DocumentManager.get_instance().get_language_names());
		_languageComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ( _ignore || !DocumentManager.get_instance().exist_datafile())
					return;

				DocumentManager.get_instance().modified();
			}
		});
		_languageComboBox.setPreferredSize( new Dimension( 150, _languageComboBox.getPreferredSize().height));
		panel.add( _languageComboBox);

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void on_update() {
		enable_menuItem( _file_export_archive_menuItem, animator_file_exists());
		enable_menuItem( _run_application_menuItem, animator_file_exists());
		enable_button( _file_export_archive_button, animator_file_exists());
		enable_button( _run_application_button, animator_file_exists());

		if ( _ignore)
			return;

		DocumentManager.get_instance().modified();
	}

	/**
	 * 
	 */
	protected void on_animator_file_selector_button_actionPerformed() {
		File file = CommonTool.get_animator_file(
			Environment._select_animator_file_directory_key,
			ResourceManager.get( "select.animator.file.dialog.title"),
			this);
		if ( null == file)
			return;

		_animatorFileTextField.setText( file.getAbsolutePath().replaceAll(  "\\\\", "/"));
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_center_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));


		_mainPanel = new MainPanel();
		_languageTree = new LanguageTree( _mainPanel, this, this);

		if ( !_mainPanel.setup())
				return false;

		if ( !_languageTree.setup())
			return false;

		_splitPane = new JSplitPane();

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView( _languageTree);
		_splitPane.setLeftComponent( scrollPane);

		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView( _mainPanel);
		_splitPane.setRightComponent( scrollPane);
		
		String value = Environment.get_instance().get(
			Environment._main_window_divider_location_key, "100");
		_splitPane.setDividerLocation( Integer.parseInt( value));


		panel.add( _splitPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);


		return true;
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _animatorFileLabel.getPreferredSize().width;
		width = Math.max( width, _languageLabel.getPreferredSize().width);

		_animatorFileLabel.setPreferredSize( new Dimension( width, _animatorFileLabel.getPreferredSize().height));
		_languageLabel.setPreferredSize( new Dimension( width, _languageLabel.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		if ( DocumentManager.get_instance().isModified()) {
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
		set_property_to_environment_file();
		System.exit( 0);
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_new(ActionEvent actionEvent) {
		on_file_close( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_open(ActionEvent actionEvent) {
		if ( DocumentManager.get_instance().isModified()) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		}

		File file = CommonTool.get_open_file(
			Environment._open_directory_key,
			ResourceManager.get( "file.open.dialog"),
			new String[] { _animator_builder_extension},
			"animator builder data",
			this);

		requestFocus();

		if ( null == file)
			return;

		open( file);
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_close(ActionEvent actionEvent) {
		if ( DocumentManager.get_instance().isModified()) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		}

		reset();

		requestFocus();
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_save(ActionEvent actionEvent) {
		MessageDlg.execute( this, ResourceManager.get( "application.title"), true,
			"on_file_save", ResourceManager.get( "file.save.show.message"), this, this);

		setTitle( ResourceManager.get( "application.title")
			+ " - " + DocumentManager.get_instance().get_current_file().getName());

		requestFocus();
	}

	/**
	 * @param actionEvent
	 * @return
	 */
	public boolean on_file_save_as(ActionEvent actionEvent) {
		File file = CommonTool.get_save_file(
			Environment._save_as_directory_key,
			ResourceManager.get( "file.saveas.dialog"),
			new String[] { _animator_builder_extension},
			"animator builder data",
			this);

		requestFocus();

		if ( null == file)
			return false;

		String absolute_name = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absolute_name + "." + _animator_builder_extension);
		else if ( name.length() - 1 == index)
			file = new File( absolute_name + _animator_builder_extension);

		if ( !MessageDlg.execute( this, ResourceManager.get( "application.title"), true,
			"on_file_save_as", ResourceManager.get( "file.saveas.show.message"),
			new Object[] { file}, this, this))
			return false;

		_file_close_menuItem.setEnabled( true);
		_file_close_button.setEnabled( true);
		_file_save_menuItem.setEnabled( true);
		_file_save_button.setEnabled( true);

		setTitle( ResourceManager.get( "application.title") + " - " + file.getName());

		return true;
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_export_archive(ActionEvent actionEvent) {
		_languageTree.store();

		File file = CommonTool.get_save_file(
			Environment._export_archive_directory_key,
			ResourceManager.get( "file.export.archive.dialog"),
			new String[] { _export_archive_extension},
			"animation archive data",
			this);

		requestFocus();

		if ( null == file)
			return;

		String absolute_name = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absolute_name + "." + _export_archive_extension);
		else if ( name.length() - 1 == index)
			file = new File( absolute_name + _export_archive_extension);

		if ( !MessageDlg.execute( this, ResourceManager.get( "application.title"), true,
			"on_file_export_archive", ResourceManager.get( "file.export.archive.show.message"),
			new Object[] { file, _animatorFileTextField.getText().replaceAll(  "\\\\", "/"),
				( String)_languageComboBox.getSelectedItem()},
			this, this))
			JOptionPane.showMessageDialog(
				this,
				ResourceManager.get( "file.export.archive.could.not.export.archive.message"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_exit(ActionEvent actionEvent) {
		if ( DocumentManager.get_instance().isModified()) {
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
		set_property_to_environment_file();
		System.exit( 0);
	}

	/**
	 * @param actionEvent
	 */
	public void on_run_application(ActionEvent actionEvent) {
		_languageTree.store();

		if ( !MessageDlg.execute( this, ResourceManager.get( "application.title"), true,
			"on_run_application", ResourceManager.get( "run.application.show.message"),
			new Object[] { _animatorFileTextField.getText().replaceAll(  "\\\\", "/"),
				( String)_languageComboBox.getSelectedItem()},
			this, this))
			JOptionPane.showMessageDialog(
				this,
				ResourceManager.get( "run.application.could.not.launch.application.message"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param actionEvent
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

	/**
	 * @param actionEvent
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

	/**
	 * @param actionEvent
	 */
	public void on_help_about(ActionEvent actionEvent) {
		JOptionPane.showMessageDialog( this,
			Constant.get_version_message(),
			ResourceManager.get( "application.title"),
			JOptionPane.INFORMATION_MESSAGE);

		requestFocus();
	}

	/**
	 * @param information
	 */
	public void update(String information) {
		_information_label.setText( information);
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException 
	 */
	public boolean write(Writer writer) throws SAXException {
		_languageTree.store();


		if ( !DocumentManager.get_instance().write( writer))
			return false;


		String language_name = ( String)_languageComboBox.getSelectedItem();
		if ( null == language_name)
			return false;

		String langage = DocumentManager.get_instance().get_language( language_name);
		if ( null == langage)
			return false;

		writer.startElement( null, null, "selected_language", new AttributesImpl());
		writer.characters( langage.toCharArray(), 0, langage.length());
		writer.endElement( null, null, "selected_language");


		String filename = _animatorFileTextField.getText();
		if ( null == filename)
			return false;

		writer.startElement( null, null, "animator_file", new AttributesImpl());
		writer.characters( filename.toCharArray(), 0, filename.length());
		writer.endElement( null, null, "animator_file");


		return true;
	}
}
