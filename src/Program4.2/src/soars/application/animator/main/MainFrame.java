/*
 * 2005/01/27
 */
package soars.application.animator.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Label;
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
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import soars.application.animator.common.tool.CommonTool;
import soars.application.animator.menu.animation.AnimationSliderAction;
import soars.application.animator.menu.animation.BackwardAction;
import soars.application.animator.menu.animation.BackwardHeadAction;
import soars.application.animator.menu.animation.BackwardStepAction;
import soars.application.animator.menu.animation.ForwardAction;
import soars.application.animator.menu.animation.ForwardStepAction;
import soars.application.animator.menu.animation.ForwardTailAction;
import soars.application.animator.menu.animation.PauseAction;
import soars.application.animator.menu.animation.PlayAction;
import soars.application.animator.menu.animation.RetrieveAgentPropertyAction;
import soars.application.animator.menu.animation.RetrieveSpotPropertyAction;
import soars.application.animator.menu.animation.StopAction;
import soars.application.animator.menu.edit.AgentAction;
import soars.application.animator.menu.edit.AgentPropertyAction;
import soars.application.animator.menu.edit.CommonPropertyAction;
import soars.application.animator.menu.edit.SpotAction;
import soars.application.animator.menu.edit.SpotPropertyAction;
import soars.application.animator.menu.file.CloseAction;
import soars.application.animator.menu.file.ExitAction;
import soars.application.animator.menu.file.ExportGraphicDataAction;
import soars.application.animator.menu.file.ImportAction;
import soars.application.animator.menu.file.ImportGraphicDataAction;
import soars.application.animator.menu.file.NewAction;
import soars.application.animator.menu.file.OpenAction;
import soars.application.animator.menu.file.SaveAction;
import soars.application.animator.menu.file.SaveAsAction;
import soars.application.animator.menu.help.AboutAction;
import soars.application.animator.menu.help.ContentsAction;
import soars.application.animator.menu.help.ForumAction;
import soars.application.animator.menu.option.ChartDisplaySettingAction;
import soars.application.animator.menu.option.PackAgentsAction;
import soars.application.animator.menu.option.RepeatAction;
import soars.application.animator.object.chart.ChartObjectMap;
import soars.application.animator.object.chart.setting.ChartDisplaySettingDlg;
import soars.application.animator.object.player.ObjectManager;
import soars.application.animator.object.property.agent.AgentPropertyManager;
import soars.application.animator.object.property.spot.SpotPropertyManager;
import soars.application.animator.object.scenario.ScenarioManager;
import soars.application.animator.setting.common.CommonProperty;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.mac.IMacScreenMenuHandler;
import soars.common.utility.swing.mac.MacUtility;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.network.BrowserLauncher;

/**
 * The Animator main window class.
 * @author kurata / SOARS project
 */
public class MainFrame extends Frame implements IMacScreenMenuHandler, DropTargetListener {

	/**
	 * Default width.
	 */
	static public final int _minimumWidth = 800;

	/**
	 * Default height.
	 */
	static public final int _minimumHeight = 600;

	/**
	 * Default extension of the Animator data file.
	 */
	static public final String _animatorExtension = "anm";

	/**
	 * Default extension of the legacy Animator data file.
	 */
	static public final String _legacyAnimatorExtension = "aml";

	/**
	 * Default extension of the Animator graphics data file.
	 */
	static public final String _graphicDataExtension = "agd";

	/**
	 * Default extension of the legacy Animator graphics data file.
	 */
	static public final String _legacyGraphicDataExtension = "agml";

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
	private Rectangle _windowRectangle = new Rectangle();

	/**
	 * 
	 */
	private UserInterface _userInterface = null;

	/**
	 * 
	 */
	private JMenuItem _fileNewMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileOpenMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileCloseMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileSaveMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileSaveAsMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileImportMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileImportGraphicDataMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileExportGraphicDataMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editCommonPropertyMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editAgentMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editSpotMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editAgentPropertyMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editSpotPropertyMenuItem = null;

	/**
	 * 
	 */
	private JCheckBoxMenuItem _optionPackAgentsMenuItem = null;

	/**
	 * 
	 */
	private JCheckBoxMenuItem _optionRepeatMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _optionChartDisplaySettingMenuItem = null;

	/**
	 * 
	 */
	private JButton _fileNewButton = null;

	/**
	 * 
	 */
	private JButton _fileOpenButton = null;

	/**
	 * 
	 */
	private JButton _fileCloseButton = null;

	/**
	 * 
	 */
	private JButton _fileSaveButton = null;

	/**
	 * 
	 */
	private JButton _fileSaveAsButton = null;

	/**
	 * 
	 */
	private JButton _fileImportButton = null;

	/**
	 * 
	 */
	private JButton _fileImportGraphicDataButton = null;

	/**
	 * 
	 */
	private JButton _fileExportGraphicDataButton = null;

	/**
	 * 
	 */
	private JButton _editCommonPropertyButton = null;

	/**
	 * 
	 */
	private JButton _editAgentButton = null;

	/**
	 * 
	 */
	private JButton _editSpotButton = null;

	/**
	 * 
	 */
	private JButton _editAgentPropertyButton = null;

	/**
	 * 
	 */
	private JButton _editSpotPropertyButton = null;

	/**
	 * 
	 */
	private JButton _animationBackwardHeadButton = null;

	/**
	 * 
	 */
	private JButton _animationBackwardButton = null;

	/**
	 * 
	 */
	private JButton _animationBackwardStepButton = null;

	/**
	 * 
	 */
	private JButton _animationPlayButton = null;

	/**
	 * 
	 */
	private JButton _animationPauseButton = null;

	/**
	 * 
	 */
	private JButton _animationStopButton = null;

	/**
	 * 
	 */
	private JButton _animationForwardStepButton = null;

	/**
	 * 
	 */
	private JButton _animationForwardButton = null;

	/**
	 * 
	 */
	private JButton _animationForwardTailButton = null;

	/**
	 * 
	 */
	private JButton _animationRetrieveAgentPropertyButton = null;

	/**
	 * 
	 */
	private JButton _animationRetrieveSpotPropertyButton = null;

	/**
	 * 
	 */
	private JButton _animationSliderButton = null;

	/**
	 * 
	 */
	private JLabel _messageLabel = null;

	/**
	 * 
	 */
	private JLabel _informationLabel = null;

	/**
	 * Returns the instance of this class.
	 * @return the instance of this class
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
	 * Creates the instance of the Animator main window class.
	 * @param arg0 the title to be displayed in the frame's border. A null value is treated as an empty string, ""
	 * @throws HeadlessException Thrown when code that is dependent on a keyboard, display, or mouse is called in an environment that does not support a keyboard, display, or mouse
	 */
	public MainFrame(String arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * 
	 */
	private void get_property_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._mainWindowRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).x));
		_windowRectangle.x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._mainWindowRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).y));
		_windowRectangle.y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._mainWindowRectangleKey + "width",
			String.valueOf( _minimumWidth));
		_windowRectangle.width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._mainWindowRectangleKey + "height",
			String.valueOf( _minimumHeight));
		_windowRectangle.height = Integer.parseInt( value);


		value = Environment.get_instance().get(
			Environment._packAgentsKey, "false");
		CommonProperty.get_instance()._pack = value.equals( "true");

		value = Environment.get_instance().get(
			Environment._repeatKey, "false");
		CommonProperty.get_instance()._repeat = value.equals( "true");
	}

	/**
	 * @return
	 */
	private void optimize_window_rectangle() {
		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( _windowRectangle)
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _windowRectangle).width <= 10
			|| _windowRectangle.y <= -getInsets().top
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _windowRectangle).height <= getInsets().top)
			_windowRectangle.setBounds(
				SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).x,
				SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).y,
				_minimumWidth, _minimumHeight);
	}

	/**
	 * 
	 */
	private void set_property_to_environment_file() throws IOException {
		Environment.get_instance().set(
			Environment._packAgentsKey, ( CommonProperty.get_instance()._pack ? "true" : "false"));
		Environment.get_instance().set(
			Environment._repeatKey, ( CommonProperty.get_instance()._repeat ? "true" : "false"));

		Environment.get_instance().set(
			Environment._mainWindowRectangleKey + "x", String.valueOf( _windowRectangle.x));
		Environment.get_instance().set(
			Environment._mainWindowRectangleKey + "y", String.valueOf( _windowRectangle.y));
		Environment.get_instance().set(
			Environment._mainWindowRectangleKey + "width", String.valueOf( _windowRectangle.width));
		Environment.get_instance().set(
			Environment._mainWindowRectangleKey + "height", String.valueOf( _windowRectangle.height));

		Environment.get_instance().store();
	}

	/**
	 * 
	 */
	private void setup_menu() {
		JToolBar statusBar = new JToolBar();

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		_messageLabel = new JLabel( "");
		//statusBar.add( _message_label);
		panel.add( _messageLabel);
		statusBar.add( panel);

		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		//panel.setLayout( new BorderLayout());

		//_information_label = new JLabel( "");
		_informationLabel = new JLabel( "                                             ");
		_informationLabel.setHorizontalAlignment( Label.RIGHT);
		//statusBar.add( _information_label);
		panel.add( _informationLabel);
		statusBar.add( panel);

		statusBar.setFloatable( false);

		//statusBar.setEnabled( false);

		getContentPane().add( statusBar, BorderLayout.SOUTH);



		JMenuBar menuBar = new JMenuBar();

		JToolBar toolBar = new JToolBar();
		toolBar.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));

		if ( !Application._demo)
			setup_menu( menuBar, toolBar);
		else
			setup_menu_for_demo( menuBar, toolBar);

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
			_messageLabel,
			ResourceManager.get( "file.message"));


		NewAction newAction = new NewAction( ResourceManager.get( "file.new.menu"));
		_fileNewMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.new.menu"),
			newAction,
			ResourceManager.get( "file.new.mnemonic"),
			ResourceManager.get( "file.new.stroke"),
			_messageLabel,
			ResourceManager.get( "file.new.message"));


		OpenAction openAction = new OpenAction(
			ResourceManager.get( "file.open.menu"));
		_fileOpenMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.open.menu"),
			openAction,
			ResourceManager.get( "file.open.mnemonic"),
			ResourceManager.get( "file.open.stroke"),
			_messageLabel,
			ResourceManager.get( "file.open.message"));


		menu.addSeparator();


		CloseAction closeAction = new CloseAction(
			ResourceManager.get( "file.close.menu"));
		_fileCloseMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.close.menu"),
			closeAction,
			ResourceManager.get( "file.close.mnemonic"),
			ResourceManager.get( "file.close.stroke"),
			_messageLabel,
			ResourceManager.get( "file.close.message"));
		_fileCloseMenuItem.setEnabled( false);


		menu.addSeparator();


		SaveAction saveAction = new SaveAction(
			ResourceManager.get( "file.save.menu"));
		_fileSaveMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.save.menu"),
			saveAction,
			ResourceManager.get( "file.save.mnemonic"),
			ResourceManager.get( "file.save.stroke"),
			_messageLabel,
			ResourceManager.get( "file.save.message"));
		_fileSaveMenuItem.setEnabled( false);


		SaveAsAction saveAsAction = new SaveAsAction(
			ResourceManager.get( "file.saveas.menu"));
		_fileSaveAsMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.saveas.menu"),
			saveAsAction,
			ResourceManager.get( "file.saveas.mnemonic"),
			ResourceManager.get( "file.saveas.stroke"),
			_messageLabel,
			ResourceManager.get( "file.saveas.message"));
		_fileSaveAsMenuItem.setEnabled( false);


		menu.addSeparator();


		ImportAction importAction = new ImportAction(
			ResourceManager.get( "file.import.menu"));
		_fileImportMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.import.menu"),
			importAction,
			ResourceManager.get( "file.import.mnemonic"),
			ResourceManager.get( "file.import.stroke"),
			_messageLabel,
			ResourceManager.get( "file.import.message"));



		menu.addSeparator();


		ImportGraphicDataAction importGraphicDataAction = new ImportGraphicDataAction(
			ResourceManager.get( "file.import.graphic.data.menu"));
		_fileImportGraphicDataMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.import.graphic.data.menu"),
			importGraphicDataAction,
			ResourceManager.get( "file.import.graphic.data.mnemonic"),
			ResourceManager.get( "file.import.graphic.data.stroke"),
			_messageLabel,
			ResourceManager.get( "file.import.graphic.data.message"));
		_fileImportGraphicDataMenuItem.setEnabled( false);


		ExportGraphicDataAction exportGraphicDataAction = new ExportGraphicDataAction(
			ResourceManager.get( "file.export.graphic.data.menu"));
		_fileExportGraphicDataMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.export.graphic.data.menu"),
			exportGraphicDataAction,
			ResourceManager.get( "file.export.graphic.data.mnemonic"),
			ResourceManager.get( "file.export.graphic.data.stroke"),
			_messageLabel,
			ResourceManager.get( "file.export.graphic.data.message"));
		_fileExportGraphicDataMenuItem.setEnabled( false);


		menu.addSeparator();


		ExitAction exitAction = new ExitAction(
			ResourceManager.get( "file.exit.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.exit.menu"),
			exitAction,
			ResourceManager.get( "file.exit.mnemonic"),
			ResourceManager.get( "file.exit.stroke"),
			_messageLabel,
			ResourceManager.get( "file.exit.message"));



		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get( "edit.menu"),
			true,
			ResourceManager.get( "edit.mnemonic"),
			_messageLabel,
			ResourceManager.get( "edit.message"));


		CommonPropertyAction commonPropertyAction = new CommonPropertyAction(
			ResourceManager.get( "edit.common.property.menu"));
		_editCommonPropertyMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "edit.common.property.menu"),
			commonPropertyAction,
			ResourceManager.get( "edit.common.property.mnemonic"),
			ResourceManager.get( "edit.common.property.stroke"),
			_messageLabel,
			ResourceManager.get( "edit.common.property.message"));
		_editCommonPropertyMenuItem.setEnabled( false);


		menu.addSeparator();


		AgentAction agentAction = new AgentAction(
			ResourceManager.get( "edit.agent.menu"));
		_editAgentMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "edit.agent.menu"),
			agentAction,
			ResourceManager.get( "edit.agent.mnemonic"),
			ResourceManager.get( "edit.agent.stroke"),
			_messageLabel,
			ResourceManager.get( "edit.agent.message"));
		_editAgentMenuItem.setEnabled( false);


		SpotAction spotAction = new SpotAction(
			ResourceManager.get( "edit.spot.menu"));
		_editSpotMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "edit.spot.menu"),
			spotAction,
			ResourceManager.get( "edit.spot.mnemonic"),
			ResourceManager.get( "edit.spot.stroke"),
			_messageLabel,
			ResourceManager.get( "edit.spot.message"));
		_editSpotMenuItem.setEnabled( false);


		menu.addSeparator();


		AgentPropertyAction agentPropertyAction = new AgentPropertyAction(
			ResourceManager.get( "edit.agent.property.menu"));
		_editAgentPropertyMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "edit.agent.property.menu"),
			agentPropertyAction,
			ResourceManager.get( "edit.agent.property.mnemonic"),
			ResourceManager.get( "edit.agent.property.stroke"),
			_messageLabel,
			ResourceManager.get( "edit.agent.property.message"));
		_editAgentPropertyMenuItem.setEnabled( false);


		SpotPropertyAction spotPropertyAction = new SpotPropertyAction(
			ResourceManager.get( "edit.spot.property.menu"));
		_editSpotPropertyMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "edit.spot.property.menu"),
			spotPropertyAction,
			ResourceManager.get( "edit.spot.property.mnemonic"),
			ResourceManager.get( "edit.spot.property.stroke"),
			_messageLabel,
			ResourceManager.get( "edit.spot.property.message"));
		_editSpotPropertyMenuItem.setEnabled( false);



		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get( "option.menu"),
			true,
			ResourceManager.get( "option.mnemonic"),
			_messageLabel,
			ResourceManager.get( "option.message"));


		PackAgentsAction packAgentsAction = new PackAgentsAction(
			ResourceManager.get( "option.pack.agents.menu"));
		_optionPackAgentsMenuItem = _userInterface.append_checkbox_menuitem(
			menu,
			ResourceManager.get( "option.pack.agents.menu"),
			packAgentsAction,
			ResourceManager.get( "option.pack.agents.mnemonic"),
			ResourceManager.get( "option.pack.agents.stroke"),
			_messageLabel,
			ResourceManager.get( "option.pack.agents.message"));
		_optionPackAgentsMenuItem.setState( CommonProperty.get_instance()._pack);


		RepeatAction repeatAction = new RepeatAction(
			ResourceManager.get( "option.repeat.menu"));
		_optionRepeatMenuItem = _userInterface.append_checkbox_menuitem(
			menu,
			ResourceManager.get( "option.repeat.menu"),
			repeatAction,
			ResourceManager.get( "option.repeat.mnemonic"),
			ResourceManager.get( "option.repeat.stroke"),
			_messageLabel,
			ResourceManager.get( "option.repeat.message"));
		_optionRepeatMenuItem.setState( CommonProperty.get_instance()._repeat);


		menu.addSeparator();


		ChartDisplaySettingAction chartDisplaySettingAction = new ChartDisplaySettingAction(
			ResourceManager.get( "option.chart.display.setting.menu"));
		_optionChartDisplaySettingMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "option.chart.display.setting.menu"),
			chartDisplaySettingAction,
			ResourceManager.get( "option.chart.display.setting.mnemonic"),
			ResourceManager.get( "option.chart.display.setting.stroke"),
			_messageLabel,
			ResourceManager.get( "option.chart.display.setting.message"));
		_optionChartDisplaySettingMenuItem.setEnabled( false);



		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get( "help.menu"),
			true,
			ResourceManager.get( "help.mnemonic"),
			_messageLabel,
			ResourceManager.get( "help.message"));


		ContentsAction contentsAction = new ContentsAction(
			ResourceManager.get( "help.contents.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get( "help.contents.menu"),
			contentsAction,
			ResourceManager.get( "help.contents.mnemonic"),
			ResourceManager.get( "help.contents.stroke"),
			_messageLabel,
			ResourceManager.get( "help.contents.message"));


		menu.addSeparator();


		ForumAction forumAction = new ForumAction(
			ResourceManager.get( "help.forum.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get( "help.forum.menu"),
			forumAction,
			ResourceManager.get( "help.forum.mnemonic"),
			ResourceManager.get( "help.forum.stroke"),
			_messageLabel,
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
			_messageLabel,
			ResourceManager.get( "help.about.message"));


		//menuBar.setEnabled( false);



		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/app_exit.png"));
		JButton button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.exit.menu"),
			exitAction,
			ResourceManager.get( "file.exit.tooltip"),
			_messageLabel,
			ResourceManager.get( "file.exit.message"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/new.png"));
		_fileNewButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.new.menu"),
			newAction,
			ResourceManager.get( "file.new.tooltip"),
			_messageLabel,
			ResourceManager.get( "file.new.message"));
		_fileNewButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileNewButton.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/open.png"));
		_fileOpenButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.open.menu"),
			openAction,
			ResourceManager.get( "file.open.tooltip"),
			_messageLabel,
			ResourceManager.get( "file.open.message"));
		_fileOpenButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileOpenButton.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/close.png"));
		_fileCloseButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.close.menu"),
			closeAction,
			ResourceManager.get( "file.close.tooltip"),
			_messageLabel,
			ResourceManager.get( "file.close.message"));
		_fileCloseButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileCloseButton.getPreferredSize().height));
		_fileCloseButton.setEnabled( false);


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/save.png"));
		_fileSaveButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.save.menu"),
			saveAction,
			ResourceManager.get( "file.save.tooltip"),
			_messageLabel,
			ResourceManager.get( "file.save.message"));
		_fileSaveButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileSaveButton.getPreferredSize().height));
		_fileSaveButton.setEnabled( false);


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/save_as.png"));
		_fileSaveAsButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.saveas.menu"),
			saveAsAction,
			ResourceManager.get( "file.saveas.tooltip"),
			_messageLabel,
			ResourceManager.get( "file.saveas.message"));
		_fileSaveAsButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileSaveAsButton.getPreferredSize().height));
		_fileSaveAsButton.setEnabled( false);


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/import.png"));
		_fileImportButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.import.menu"),
			importAction,
			ResourceManager.get( "file.import.tooltip"),
			_messageLabel,
			ResourceManager.get( "file.import.message"));
		_fileImportButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileImportButton.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/import_graphic_data.png"));
		_fileImportGraphicDataButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.import.graphic.data.menu"),
			importGraphicDataAction,
			ResourceManager.get( "file.import.graphic.data.tooltip"),
			_messageLabel,
			ResourceManager.get( "file.import.graphic.data.message"));
		_fileImportGraphicDataButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileImportGraphicDataButton.getPreferredSize().height));
		_fileImportGraphicDataButton.setEnabled( false);


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/export_graphic_data.png"));
		_fileExportGraphicDataButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.export.graphic.data.menu"),
			exportGraphicDataAction,
			ResourceManager.get( "file.export.graphic.data.tooltip"),
			_messageLabel,
			ResourceManager.get( "file.export.graphic.data.message"));
		_fileExportGraphicDataButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileExportGraphicDataButton.getPreferredSize().height));
		_fileExportGraphicDataButton.setEnabled( false);


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/common_property.png"));
		_editCommonPropertyButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "edit.common.property.menu"),
			commonPropertyAction,
			ResourceManager.get( "edit.common.property.tooltip"),
			_messageLabel,
			ResourceManager.get( "edit.common.property.message"));
		_editCommonPropertyButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _editCommonPropertyButton.getPreferredSize().height));
		_editCommonPropertyButton.setEnabled( false);


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/agent.png"));
		_editAgentButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "edit.agent.menu"),
			agentAction,
			ResourceManager.get( "edit.agent.tooltip"),
			_messageLabel,
			ResourceManager.get( "edit.agent.message"));
		_editAgentButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _editAgentButton.getPreferredSize().height));
		_editAgentButton.setEnabled( false);


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/spot.png"));
		_editSpotButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "edit.spot.menu"),
			spotAction,
			ResourceManager.get( "edit.spot.tooltip"),
			_messageLabel,
			ResourceManager.get( "edit.spot.message"));
		_editSpotButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _editSpotButton.getPreferredSize().height));
		_editSpotButton.setEnabled( false);


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/agent_property.png"));
		_editAgentPropertyButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "edit.agent.property.menu"),
			agentPropertyAction,
			ResourceManager.get( "edit.agent.property.tooltip"),
			_messageLabel,
			ResourceManager.get( "edit.agent.property.message"));
		_editAgentPropertyButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _editAgentPropertyButton.getPreferredSize().height));
		_editAgentPropertyButton.setEnabled( false);


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/spot_property.png"));
		_editSpotPropertyButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "edit.spot.property.menu"),
			spotPropertyAction,
			ResourceManager.get( "edit.spot.property.tooltip"),
			_messageLabel,
			ResourceManager.get( "edit.spot.property.message"));
		_editSpotPropertyButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _editSpotPropertyButton.getPreferredSize().height));
		_editSpotPropertyButton.setEnabled( false);


		toolBar.addSeparator();


		BackwardHeadAction backwardHeadAction = new BackwardHeadAction(
			ResourceManager.get( "animation.backward.head.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/backward_head.png"));
		_animationBackwardHeadButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.backward.head.menu"),
			backwardHeadAction,
			ResourceManager.get( "animation.backward.head.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.backward.head.message"));
		_animationBackwardHeadButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationBackwardHeadButton.getPreferredSize().height));
		_animationBackwardHeadButton.setEnabled( false);


		BackwardAction backwardAction = new BackwardAction(
			ResourceManager.get( "animation.backward.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/backward.png"));
		_animationBackwardButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.backward.menu"),
			backwardAction,
			ResourceManager.get( "animation.backward.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.backward.message"));
		_animationBackwardButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationBackwardButton.getPreferredSize().height));
		_animationBackwardButton.setEnabled( false);


		BackwardStepAction backwardStepAction = new BackwardStepAction(
			ResourceManager.get( "animation.backward.step.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/backward_step.png"));
		_animationBackwardStepButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.backward.step.menu"),
			backwardStepAction,
			ResourceManager.get( "animation.backward.step.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.backward.step.message"));
		_animationBackwardStepButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationBackwardStepButton.getPreferredSize().height));
		_animationBackwardStepButton.setEnabled( false);


		toolBar.addSeparator();


		PlayAction playAction = new PlayAction(
			ResourceManager.get( "animation.play.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/play.png"));
		_animationPlayButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.play.menu"),
			playAction,
			ResourceManager.get( "animation.play.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.play.message"));
		_animationPlayButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationPlayButton.getPreferredSize().height));
		_animationPlayButton.setEnabled( false);


		PauseAction pauseAction = new PauseAction(
			ResourceManager.get( "animation.pause.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/pause.png"));
		_animationPauseButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.pause.menu"),
			pauseAction,
			ResourceManager.get( "animation.pause.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.pause.message"));
		_animationPauseButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationPauseButton.getPreferredSize().height));
		_animationPauseButton.setEnabled( false);


		StopAction stopAction = new StopAction(
			ResourceManager.get( "animation.stop.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/stop.png"));
		_animationStopButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.stop.menu"),
			stopAction,
			ResourceManager.get( "animation.stop.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.stop.message"));
		_animationStopButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationStopButton.getPreferredSize().height));
		_animationStopButton.setEnabled( false);


		toolBar.addSeparator();


		ForwardStepAction forwardStepAction = new ForwardStepAction(
			ResourceManager.get( "animation.forward.step.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/forward_step.png"));
		_animationForwardStepButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.forward.step.menu"),
			forwardStepAction,
			ResourceManager.get( "animation.forward.step.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.forward.step.message"));
		_animationForwardStepButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationForwardStepButton.getPreferredSize().height));
		_animationForwardStepButton.setEnabled( false);


		ForwardAction forwardAction = new ForwardAction(
			ResourceManager.get( "animation.forward.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/forward.png"));
		_animationForwardButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.forward.menu"),
			forwardAction,
			ResourceManager.get( "animation.forward.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.forward.message"));
		_animationForwardButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationForwardButton.getPreferredSize().height));
		_animationForwardButton.setEnabled( false);


		ForwardTailAction forwardTailAction = new ForwardTailAction(
			ResourceManager.get( "animation.forward.tail.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/forward_tail.png"));
		_animationForwardTailButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.forward.tail.menu"),
			forwardTailAction,
			ResourceManager.get( "animation.forward.tail.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.forward.tail.message"));
		_animationForwardTailButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationForwardTailButton.getPreferredSize().height));
		_animationForwardTailButton.setEnabled( false);


		toolBar.addSeparator();


		RetrieveAgentPropertyAction retrieveAgentPropertyAction = new RetrieveAgentPropertyAction(
			ResourceManager.get( "animation.retrieve.agent.property.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/retrieve_agent_property.png"));
		_animationRetrieveAgentPropertyButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.retrieve.agent.property.menu"),
			retrieveAgentPropertyAction,
			ResourceManager.get( "animation.retrieve.agent.property.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.retrieve.agent.property.message"));
		_animationRetrieveAgentPropertyButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationRetrieveAgentPropertyButton.getPreferredSize().height));
		_animationRetrieveAgentPropertyButton.setEnabled( false);


		RetrieveSpotPropertyAction retrieveSpotPropertyAction = new RetrieveSpotPropertyAction(
			ResourceManager.get( "animation.retrieve.spot.property.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/retrieve_spot_property.png"));
		_animationRetrieveSpotPropertyButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.retrieve.spot.property.menu"),
			retrieveSpotPropertyAction,
			ResourceManager.get( "animation.retrieve.spot.property.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.retrieve.spot.property.message"));
		_animationRetrieveSpotPropertyButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationRetrieveSpotPropertyButton.getPreferredSize().height));
		_animationRetrieveSpotPropertyButton.setEnabled( false);


		toolBar.addSeparator();


		AnimationSliderAction animationSliderAction = new AnimationSliderAction(
			ResourceManager.get( "animation.slider.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/slider.png"));
		_animationSliderButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.slider.menu"),
			animationSliderAction,
			ResourceManager.get( "animation.slider.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.slider.message"));
		_animationSliderButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationSliderButton.getPreferredSize().height));
		_animationSliderButton.setEnabled( false);
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
			_messageLabel,
			ResourceManager.get( "file.message"));


		ExitAction exitAction = new ExitAction(
			ResourceManager.get( "file.exit.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get( "file.exit.menu"),
			exitAction,
			ResourceManager.get( "file.exit.mnemonic"),
			ResourceManager.get( "file.exit.stroke"),
			_messageLabel,
			ResourceManager.get( "file.exit.message"));



		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get( "edit.menu"),
			true,
			ResourceManager.get( "edit.mnemonic"),
			_messageLabel,
			ResourceManager.get( "edit.message"));


		CommonPropertyAction commonPropertyAction = new CommonPropertyAction(
			ResourceManager.get( "edit.common.property.menu"));
		_editCommonPropertyMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "edit.common.property.menu"),
			commonPropertyAction,
			ResourceManager.get( "edit.common.property.mnemonic"),
			ResourceManager.get( "edit.common.property.stroke"),
			_messageLabel,
			ResourceManager.get( "edit.common.property.message"));
		_editCommonPropertyMenuItem.setEnabled( false);



		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get( "option.menu"),
			true,
			ResourceManager.get( "option.mnemonic"),
			_messageLabel,
			ResourceManager.get( "option.message"));


		PackAgentsAction packAgentsAction = new PackAgentsAction(
			ResourceManager.get( "option.pack.agents.menu"));
		_optionPackAgentsMenuItem = _userInterface.append_checkbox_menuitem(
			menu,
			ResourceManager.get( "option.pack.agents.menu"),
			packAgentsAction,
			ResourceManager.get( "option.pack.agents.mnemonic"),
			ResourceManager.get( "option.pack.agents.stroke"),
			_messageLabel,
			ResourceManager.get( "option.pack.agents.message"));
		_optionPackAgentsMenuItem.setState( CommonProperty.get_instance()._pack);


		RepeatAction repeatAction = new RepeatAction(
			ResourceManager.get( "option.repeat.menu"));
		_optionRepeatMenuItem = _userInterface.append_checkbox_menuitem(
			menu,
			ResourceManager.get( "option.repeat.menu"),
			repeatAction,
			ResourceManager.get( "option.repeat.mnemonic"),
			ResourceManager.get( "option.repeat.stroke"),
			_messageLabel,
			ResourceManager.get( "option.repeat.message"));
		_optionRepeatMenuItem.setState( CommonProperty.get_instance()._repeat);


		menu.addSeparator();


		ChartDisplaySettingAction chartDisplaySettingAction = new ChartDisplaySettingAction(
			ResourceManager.get( "option.chart.display.setting.menu"));
		_optionChartDisplaySettingMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get( "option.chart.display.setting.menu"),
			chartDisplaySettingAction,
			ResourceManager.get( "option.chart.display.setting.mnemonic"),
			ResourceManager.get( "option.chart.display.setting.stroke"),
			_messageLabel,
			ResourceManager.get( "option.chart.display.setting.message"));
		_optionChartDisplaySettingMenuItem.setEnabled( false);



		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get( "help.menu"),
			true,
			ResourceManager.get( "help.mnemonic"),
			_messageLabel,
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
//
//
//		ForumAction forumAction = new ForumAction(
//			ResourceManager.get( "help.forum.menu"));
//		_userInterface.append_menuitem(
//			menu,
//			ResourceManager.get( "help.forum.menu"),
//			forumAction,
//			ResourceManager.get( "help.forum.mnemonic"),
//			ResourceManager.get( "help.forum.stroke"),
//			_message_label,
//			ResourceManager.get( "help.forum.message"));
//
//
//		menu.addSeparator();


		AboutAction aboutAction = new AboutAction(
			ResourceManager.get( "help.about.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get( "help.about.menu"),
			aboutAction,
			ResourceManager.get( "help.about.mnemonic"),
			ResourceManager.get( "help.about.stroke"),
			_messageLabel,
			ResourceManager.get( "help.about.message"));


		//menuBar.setEnabled( false);



		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/app_exit.png"));
		JButton button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "file.exit.menu"),
			exitAction,
			ResourceManager.get( "file.exit.tooltip"),
			_messageLabel,
			ResourceManager.get( "file.exit.message"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/common_property.png"));
		_editCommonPropertyButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "edit.common.property.menu"),
			commonPropertyAction,
			ResourceManager.get( "edit.common.property.tooltip"),
			_messageLabel,
			ResourceManager.get( "edit.common.property.message"));
		_editCommonPropertyButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _editCommonPropertyButton.getPreferredSize().height));
		_editCommonPropertyButton.setEnabled( false);


		toolBar.addSeparator();


		BackwardHeadAction backwardHeadAction = new BackwardHeadAction(
			ResourceManager.get( "animation.backward.head.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/backward_head.png"));
		_animationBackwardHeadButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.backward.head.menu"),
			backwardHeadAction,
			ResourceManager.get( "animation.backward.head.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.backward.head.message"));
		_animationBackwardHeadButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationBackwardHeadButton.getPreferredSize().height));
		_animationBackwardHeadButton.setEnabled( false);


		BackwardAction backwardAction = new BackwardAction(
			ResourceManager.get( "animation.backward.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/backward.png"));
		_animationBackwardButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.backward.menu"),
			backwardAction,
			ResourceManager.get( "animation.backward.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.backward.message"));
		_animationBackwardButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationBackwardButton.getPreferredSize().height));
		_animationBackwardButton.setEnabled( false);


		BackwardStepAction backwardStepAction = new BackwardStepAction(
			ResourceManager.get( "animation.backward.step.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/backward_step.png"));
		_animationBackwardStepButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.backward.step.menu"),
			backwardStepAction,
			ResourceManager.get( "animation.backward.step.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.backward.step.message"));
		_animationBackwardStepButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationBackwardStepButton.getPreferredSize().height));
		_animationBackwardStepButton.setEnabled( false);


		toolBar.addSeparator();


		PlayAction playAction = new PlayAction(
			ResourceManager.get( "animation.play.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/play.png"));
		_animationPlayButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.play.menu"),
			playAction,
			ResourceManager.get( "animation.play.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.play.message"));
		_animationPlayButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationPlayButton.getPreferredSize().height));
		_animationPlayButton.setEnabled( false);


		PauseAction pauseAction = new PauseAction(
			ResourceManager.get( "animation.pause.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/pause.png"));
		_animationPauseButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.pause.menu"),
			pauseAction,
			ResourceManager.get( "animation.pause.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.pause.message"));
		_animationPauseButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationPauseButton.getPreferredSize().height));
		_animationPauseButton.setEnabled( false);


		StopAction stopAction = new StopAction(
			ResourceManager.get( "animation.stop.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/stop.png"));
		_animationStopButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.stop.menu"),
			stopAction,
			ResourceManager.get( "animation.stop.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.stop.message"));
		_animationStopButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationStopButton.getPreferredSize().height));
		_animationStopButton.setEnabled( false);


		toolBar.addSeparator();


		ForwardStepAction forwardStepAction = new ForwardStepAction(
			ResourceManager.get( "animation.forward.step.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/forward_step.png"));
		_animationForwardStepButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.forward.step.menu"),
			forwardStepAction,
			ResourceManager.get( "animation.forward.step.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.forward.step.message"));
		_animationForwardStepButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationForwardStepButton.getPreferredSize().height));
		_animationForwardStepButton.setEnabled( false);


		ForwardAction forwardAction = new ForwardAction(
			ResourceManager.get( "animation.forward.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/forward.png"));
		_animationForwardButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.forward.menu"),
			forwardAction,
			ResourceManager.get( "animation.forward.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.forward.message"));
		_animationForwardButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationForwardButton.getPreferredSize().height));
		_animationForwardButton.setEnabled( false);


		ForwardTailAction forwardTailAction = new ForwardTailAction(
			ResourceManager.get( "animation.forward.tail.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/forward_tail.png"));
		_animationForwardTailButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.forward.tail.menu"),
			forwardTailAction,
			ResourceManager.get( "animation.forward.tail.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.forward.tail.message"));
		_animationForwardTailButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationForwardTailButton.getPreferredSize().height));
		_animationForwardTailButton.setEnabled( false);


		toolBar.addSeparator();


		RetrieveAgentPropertyAction retrieveAgentPropertyAction = new RetrieveAgentPropertyAction(
			ResourceManager.get( "animation.retrieve.agent.property.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/retrieve_agent_property.png"));
		_animationRetrieveAgentPropertyButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.retrieve.agent.property.menu"),
			retrieveAgentPropertyAction,
			ResourceManager.get( "animation.retrieve.agent.property.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.retrieve.agent.property.message"));
		_animationRetrieveAgentPropertyButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationRetrieveAgentPropertyButton.getPreferredSize().height));
		_animationRetrieveAgentPropertyButton.setEnabled( false);


		RetrieveSpotPropertyAction retrieveSpotPropertyAction = new RetrieveSpotPropertyAction(
			ResourceManager.get( "animation.retrieve.spot.property.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/retrieve_spot_property.png"));
		_animationRetrieveSpotPropertyButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.retrieve.spot.property.menu"),
			retrieveSpotPropertyAction,
			ResourceManager.get( "animation.retrieve.spot.property.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.retrieve.spot.property.message"));
		_animationRetrieveSpotPropertyButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationRetrieveSpotPropertyButton.getPreferredSize().height));
		_animationRetrieveSpotPropertyButton.setEnabled( false);


		toolBar.addSeparator();


		AnimationSliderAction animationSliderAction = new AnimationSliderAction(
			ResourceManager.get( "animation.slider.menu"));
		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/animation/slider.png"));
		_animationSliderButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get( "animation.slider.menu"),
			animationSliderAction,
			ResourceManager.get( "animation.slider.tooltip"),
			_messageLabel,
			ResourceManager.get( "animation.slider.message"));
		_animationSliderButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _animationSliderButton.getPreferredSize().height));
		_animationSliderButton.setEnabled( false);
	}

	/**
	 * 
	 */
	private void initialize_user_interface() {
		setTitle( ResourceManager.get( "application.title"));
		update_user_interface(
			true, true, false, false, false, true, false, false, true, false, false, false, false,
			false, false, false, false, false,
			false, false, false, false, false, false, false, false, false, false, false, false, false);
		update( "                                             ");
	}

	/**
	 * @param fileNew
	 * @param fileOpen
	 * @param fileClose
	 * @param fileSave
	 * @param fileSaveAs
	 * @param fileImport
	 * @param fileImportAgentProperty
	 * @param fileImportSpotProperty
	 * @param fileImportFromClipboard
	 * @param fileImportAgentPropertyFromClipboard
	 * @param fileImportSpotPropertyFromClipboard
	 * @param fileImportGraphicData
	 * @param fileExportGraphicData
	 * @param editCommonProperty
	 * @param editAgent
	 * @param editSpot
	 * @param editAgentProperty
	 * @param editSpotProperty
	 * @param animationBackwardHead
	 * @param animationBackward
	 * @param animationBackwardStep
	 * @param animationPlay
	 * @param animationPause
	 * @param animationStop
	 * @param animationForwardStep
	 * @param animationForward
	 * @param animationForwardTail
	 * @param animationRetrieveAgentProperty
	 * @param animation_retrieve_spot_property,
	 * @param animationSlider
	 * @param optionChartDisplaySetting
	 */
	private void update_user_interface(boolean fileNew, boolean fileOpen,
		boolean fileClose, boolean fileSave, boolean fileSaveAs,
		boolean fileImport, boolean fileImportAgentProperty, boolean fileImportSpotProperty,
		boolean fileImportFromClipboard, boolean fileImportAgentPropertyFromClipboard, boolean fileImportSpotPropertyFromClipboard,
		boolean fileImportGraphicData, boolean fileExportGraphicData,
		boolean editCommonProperty,
		boolean editAgent, boolean editSpot,
		boolean editAgentProperty, boolean editSpotProperty,
		boolean animationBackwardHead, boolean animationBackward, boolean animationBackwardStep,
		boolean animationPlay, boolean animationPause, boolean animationStop,
		boolean animationForwardStep, boolean animationForward, boolean animationForwardTail,
		boolean animationRetrieveAgentProperty, boolean animationRetrieveSpotProperty,
		boolean animationSlider, boolean optionChartDisplaySetting) {
		enable_menuItem( _fileNewMenuItem, fileNew);
		enable_menuItem( _fileOpenMenuItem, fileOpen);
		enable_menuItem( _fileCloseMenuItem, fileClose);
		enable_menuItem( _fileSaveMenuItem, fileSave);
		enable_menuItem( _fileSaveAsMenuItem, fileSaveAs);
		enable_menuItem( _fileImportMenuItem, fileImport);
		enable_menuItem( _fileImportGraphicDataMenuItem, fileImportGraphicData);
		enable_menuItem( _fileExportGraphicDataMenuItem, fileExportGraphicData);

		enable_menuItem( _editCommonPropertyMenuItem, editCommonProperty);
		enable_menuItem( _editAgentMenuItem, editAgent);
		enable_menuItem( _editSpotMenuItem, editSpot);
		enable_menuItem( _editAgentPropertyMenuItem, editAgentProperty);
		enable_menuItem( _editSpotPropertyMenuItem, editSpotProperty);

		enable_menuItem( _optionChartDisplaySettingMenuItem, optionChartDisplaySetting);

		enable_button( _fileNewButton, fileNew);
		enable_button( _fileOpenButton, fileOpen);
		enable_button( _fileCloseButton, fileClose);
		enable_button( _fileSaveButton, fileSave);
		enable_button( _fileSaveAsButton, fileSaveAs);
		enable_button( _fileImportButton, fileImport);
		enable_button( _fileImportGraphicDataButton, fileImportGraphicData);
		enable_button( _fileExportGraphicDataButton, fileExportGraphicData);

		enable_button( _editCommonPropertyButton, editCommonProperty);
		enable_button( _editAgentButton, editAgent);
		enable_button( _editSpotButton, editSpot);
		enable_button( _editAgentPropertyButton, editAgentProperty);
		enable_button( _editSpotPropertyButton, editSpotProperty);

		enable_button( _animationBackwardHeadButton, animationBackwardHead);
		enable_button( _animationBackwardButton, animationBackward);
		enable_button( _animationBackwardStepButton, animationBackwardStep);
		enable_button( _animationPlayButton, animationPlay);
		enable_button( _animationPauseButton, animationPause);
		enable_button( _animationStopButton, animationStop);
		enable_button( _animationForwardStepButton, animationForwardStep);
		enable_button( _animationForwardButton, animationForward);
		enable_button( _animationForwardTailButton, animationForwardTail);
		enable_button( _animationRetrieveAgentPropertyButton, animationRetrieveAgentProperty);
		enable_button( _animationRetrieveSpotPropertyButton, animationRetrieveSpotProperty);
		enable_button( _animationSliderButton, animationSlider);
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
	 * Loads the data from the specified file.
	 * @param file the specified file
	 * @return
	 */
	public boolean open(File file) {
		if ( !AnimatorView.get_instance().on_file_open( file)) {
			initialize_user_interface();
			return false;
		}

		update_user_interface(
			true, true, ObjectManager.get_instance().exist_datafile(), ObjectManager.get_instance().exist_datafile(), true, true, true, true, true, true, true, true, true,
			true, true, true, AgentPropertyManager.get_instance().exist_property(), SpotPropertyManager.get_instance().exist_property(),
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_tail(),
			false, false, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, !ChartObjectMap.get_instance().isEmpty());

		ChartObjectMap.get_instance().bring_chartFrames_to_top();

		ChartObjectMap.get_instance().initialize_chartFrames_rectangle();

		AnimatorView.get_instance().scrollRectToVisible( new Rectangle( 0, 0, 1, 1));

		return true;
	}

	/**
	 * Returns the option which the user selected.
	 * @return the option which the user selected
	 */
	private int confirm() {
		int result = JOptionPane.showConfirmDialog(
			this,
			ResourceManager.get( "file.close.confirm.message"),
			ResourceManager.get( "application.title"),
			JOptionPane.YES_NO_CANCEL_OPTION);
		switch ( result) {
			case JOptionPane.YES_OPTION:
				if ( ObjectManager.get_instance().exist_datafile())
					AnimatorView.get_instance().on_file_save();
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

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		if ( !MacUtility.setup_screen_menu_handler( this, this, ResourceManager.get( "application.title")))
			return false;

		get_property_from_environment_file();

		_userInterface = new UserInterface();

		AnimatorView animatorView = new AnimatorView( true, this);
		if ( !animatorView.create())
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView( animatorView);
		scrollPane.setBackground( new Color( 255, 255, 255));
		getContentPane().setLayout( new BorderLayout());
		getContentPane().add( scrollPane);

		setup_menu();

		//setup_key_action();

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		if ( !Application._demo)
			new DropTarget( this, this);

		pack();

		optimize_window_rectangle();
		setLocation( _windowRectangle.x, _windowRectangle.y);
		setSize( _windowRectangle.width, _windowRectangle.height);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		setVisible( true);

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width,
					( _minimumHeight > height) ? _minimumHeight : height);
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

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		if ( ObjectManager.get_instance().isModified()) {
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

		AnimatorView.get_instance().on_closing();
		_windowRectangle = getBounds();
		try {
			set_property_to_environment_file();
		}
		catch (IOException e) {
			throw new RuntimeException( e);
		}
		System.exit( 0);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_key_pressed(java.awt.event.KeyEvent)
	 */
	protected void on_key_pressed(KeyEvent keyEvent) {
		AnimatorView.get_instance().on_key_pressed( keyEvent);
		super.on_key_pressed(keyEvent);
	}

	/**
	 * Invoked when the key is pressed.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_key_pressed(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_key_pressed( actionEvent);
	}

	/**
	 * Invoked when the "New" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_new(ActionEvent actionEvent) {
		if ( ObjectManager.get_instance().isModified()) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		}

		AnimatorView.get_instance().on_file_close();
		initialize_user_interface();
		requestFocus();
	}

	/**
	 * Invoked when the "Open" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_open(ActionEvent actionEvent) {
		if ( ObjectManager.get_instance().isModified()) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		}

		AnimatorView.get_instance().cancel();

		File file = CommonTool.get_open_file(
			Environment._openDirectoryKey,
			ResourceManager.get( "file.open.dialog"),
			new String[] { _animatorExtension, _legacyAnimatorExtension},
			"animation data",
			this);

		requestFocus();

		if ( null == file)
			return;

		open( file);
	}

	/**
	 * Invoked when the specified file is dropped.
	 * @param the specified file
	 */
	public void on_file_open_by_drag_and_drop(File file) {
		if ( ObjectManager.get_instance().isModified()) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		}

		AnimatorView.get_instance().cancel();

		requestFocus();

		if ( null == file)
			return;

		open( file);
	}

	/**
	 * Invoked when the "Close" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_close(ActionEvent actionEvent) {
		if ( ObjectManager.get_instance().isModified()) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		}

		AnimatorView.get_instance().on_file_close();
		initialize_user_interface();
		requestFocus();
	}

	/**
	 * Invoked when the "Save" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_save(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_file_save();
		ChartObjectMap.get_instance().update_chartFrames_rectangle();
		requestFocus();
	}

	/**
	 * Returns true for saving the data to the file successfullly.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 * @return true for saving the data to the file successfullly
	 */
	public boolean on_file_save_as(ActionEvent actionEvent) {
		AnimatorView.get_instance().cancel();

		File file = CommonTool.get_save_file(
			Environment._saveAsDirectoryKey,
			ResourceManager.get( "file.saveas.dialog"),
			new String[] { _animatorExtension},
			"animation data",
			this);

		requestFocus();

		if ( null == file)
			return false;

		String absoluteName = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absoluteName + "." + _animatorExtension);
		else if ( name.length() - 1 == index)
			file = new File( absoluteName + _animatorExtension);

		if ( AnimatorView.get_instance().on_file_save_as( file)) {
			_fileCloseMenuItem.setEnabled( true);
			_fileCloseButton.setEnabled( true);
			_fileSaveMenuItem.setEnabled( true);
			_fileSaveButton.setEnabled( true);
		}

		ChartObjectMap.get_instance().update_chartFrames_rectangle();

		return true;
	}

	/**
	 * Invoked when the "Import" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_import(ActionEvent actionEvent) {
		if ( ObjectManager.get_instance().isModified()) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		}

		AnimatorView.get_instance().cancel();

		File directory = CommonTool.get_import_directory(
			Environment._importDirectoryKey, ResourceManager.get( "file.import.dialog"), this);

		requestFocus();

		if ( null == directory)
			return;

		on_file_import( directory);
	}

	/**
	 * @param directory
	 * @return
	 */
	private boolean on_file_import(File directory) {
		if ( !AnimatorView.get_instance().on_file_import( directory)) {
			initialize_user_interface();
			return false;
		}

		update_user_interface(
			true, true, ObjectManager.get_instance().exist_datafile(), false, true, true, true, true, true, true, true, true, true,
			true, true, true, AgentPropertyManager.get_instance().exist_property(), SpotPropertyManager.get_instance().exist_property(),
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_tail(),
			false, false, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			!ScenarioManager.get_instance().is_tail(),
			!ChartObjectMap.get_instance().isEmpty());

		ChartObjectMap.get_instance().bring_chartFrames_to_top();

		ChartObjectMap.get_instance().initialize_chartFrames_rectangle();

		AnimatorView.get_instance().scrollRectToVisible( new Rectangle( 0, 0, 1, 1));

		return true;
	}

	/**
	 * @param parent_directory
	 * @param root_directory
	 * @return
	 */
	private boolean on_file_import(File parent_directory, File root_directory) {
		if ( !AnimatorView.get_instance().on_file_import( parent_directory, root_directory)) {
			initialize_user_interface();
			return false;
		}

		update_user_interface(
			true, true, ObjectManager.get_instance().exist_datafile(), false, true, true, true, true, true, true, true, true, true,
			true, true, true, AgentPropertyManager.get_instance().exist_property(), SpotPropertyManager.get_instance().exist_property(),
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_tail(),
			false, false, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			!ScenarioManager.get_instance().is_tail(),
			!ChartObjectMap.get_instance().isEmpty());

		ChartObjectMap.get_instance().bring_chartFrames_to_top();

		ChartObjectMap.get_instance().initialize_chartFrames_rectangle();

		AnimatorView.get_instance().scrollRectToVisible( new Rectangle( 0, 0, 1, 1));

		return true;
	}

	/**
	 * Invoked when the "Import graphic data" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_import_graphic_data(ActionEvent actionEvent) {
		AnimatorView.get_instance().cancel();

		File file = CommonTool.get_open_file(
			Environment._graphicPropertyDirectoryKey,
			ResourceManager.get( "file.import.graphic.data.dialog"),
			new String[] { _graphicDataExtension, _legacyGraphicDataExtension, _animatorExtension, _legacyAnimatorExtension},
			"animation graphic data",
			this);

		requestFocus();

		if ( null == file)
			return;

		if ( !AnimatorView.get_instance().on_file_import_graphic_data( file))
			initialize_user_interface();
		else
			update_user_interface(
				true, true, ObjectManager.get_instance().exist_datafile(), ObjectManager.get_instance().exist_datafile(), true, true, true, true, true, true, true, true, true,
				true, true, true, AgentPropertyManager.get_instance().exist_property(), SpotPropertyManager.get_instance().exist_property(),
				!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_tail(),
				false, false, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
				AgentPropertyManager.get_instance().exist_selected_property(),
				SpotPropertyManager.get_instance().exist_selected_property(),
				!ScenarioManager.get_instance().is_tail(),
				!ChartObjectMap.get_instance().isEmpty());
	}

	/**
	 * Invoked when the "Export graphic data" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_export_graphic_data(ActionEvent actionEvent) {
		AnimatorView.get_instance().cancel();

		File file = CommonTool.get_save_file(
			Environment._graphicPropertyDirectoryKey,
			ResourceManager.get( "file.export.graphic.data.dialog"),
			new String[] { _graphicDataExtension},
			"animation graphic data",
			this);

		requestFocus();

		if ( null == file)
			return;

		String absolute_name = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absolute_name + "." + _graphicDataExtension);
		else if ( name.length() - 1 == index)
			file = new File( absolute_name + _graphicDataExtension);

		if ( !AnimatorView.get_instance().on_file_export_graphic_data( file))
			initialize_user_interface();
		else
			update_user_interface(
				true, true, ObjectManager.get_instance().exist_datafile(), ObjectManager.get_instance().exist_datafile(), true, true, true, true, true, true, true, true, true,
				true, true, true, AgentPropertyManager.get_instance().exist_property(), SpotPropertyManager.get_instance().exist_property(),
				!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_tail(),
				false, false, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
				AgentPropertyManager.get_instance().exist_selected_property(),
				SpotPropertyManager.get_instance().exist_selected_property(),
				!ScenarioManager.get_instance().is_tail(),
				!ChartObjectMap.get_instance().isEmpty());
	}

	/**
	 * Invoked when the "Exit" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_exit(ActionEvent actionEvent) {
		if ( ObjectManager.get_instance().isModified()) {
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

		AnimatorView.get_instance().cleanup();
		_windowRectangle = getBounds();
		try {
			set_property_to_environment_file();
		}
		catch (IOException e) {
			throw new RuntimeException( e);
		}
		System.exit( 0);
	}

	/**
	 * Invoked when the "Property" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_common_property(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_edit_common_property( actionEvent);

		requestFocus();

		update_user_interface(
			true, true, ObjectManager.get_instance().exist_datafile(), ObjectManager.get_instance().exist_datafile(), true, true, true, true, true, true, true, true, true,
			true, true, true, AgentPropertyManager.get_instance().exist_property(), SpotPropertyManager.get_instance().exist_property(),
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_tail(),
			false, false, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, !ChartObjectMap.get_instance().isEmpty());
	}

	/**
	 * Invoked when the "Edit agent" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_agent(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_edit_agent( actionEvent);

		requestFocus();

		update_user_interface(
			true, true, ObjectManager.get_instance().exist_datafile(), ObjectManager.get_instance().exist_datafile(), true, true, true, true, true, true, true, true, true,
			true, true, true, AgentPropertyManager.get_instance().exist_property(), SpotPropertyManager.get_instance().exist_property(),
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_tail(),
			false, false, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, !ChartObjectMap.get_instance().isEmpty());
	}

	/**
	 * Invoked when the "Edit spot" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_spot(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_edit_spot( actionEvent);

		requestFocus();

		update_user_interface(
			true, true, ObjectManager.get_instance().exist_datafile(), ObjectManager.get_instance().exist_datafile(), true, true, true, true, true, true, true, true, true,
			true, true, true, AgentPropertyManager.get_instance().exist_property(), SpotPropertyManager.get_instance().exist_property(),
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_tail(),
			false, false, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, !ChartObjectMap.get_instance().isEmpty());
	}

	/**
	 * Invoked when the "Agent property" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_agent_property(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_edit_agent_property( actionEvent);

		requestFocus();

		update_user_interface(
			true, true, ObjectManager.get_instance().exist_datafile(), ObjectManager.get_instance().exist_datafile(), true, true, true, true, true, true, true, true, true,
			true, true, true, AgentPropertyManager.get_instance().exist_property(), SpotPropertyManager.get_instance().exist_property(),
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_tail(),
			false, false, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, !ChartObjectMap.get_instance().isEmpty());
	}

	/**
	 * Invoked when the "Spot property" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_spot_property(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_edit_spot_property( actionEvent);

		requestFocus();

		update_user_interface(
			true, true, ObjectManager.get_instance().exist_datafile(), ObjectManager.get_instance().exist_datafile(), true, true, true, true, true, true, true, true, true,
			true, true, true, AgentPropertyManager.get_instance().exist_property(), SpotPropertyManager.get_instance().exist_property(),
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_tail(),
			false, false, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, !ChartObjectMap.get_instance().isEmpty());
	}

	/**
	 * Invoked when the "Pack agents" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_option_pack_agents(ActionEvent actionEvent) {
		CommonProperty.get_instance()._pack = _optionPackAgentsMenuItem.getState();
		repaint();
	}

	/**
	 * Invoked when the "Repeat" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_option_repeat(ActionEvent actionEvent) {
		CommonProperty.get_instance()._repeat = _optionRepeatMenuItem.getState();
		//update_user_interface();
	}

	/**
	 * Invoked when the "Chart display setting" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_option_chart_display_setting(ActionEvent actionEvent) {
		ChartDisplaySettingDlg chartDisplaySettingDlg = new ChartDisplaySettingDlg(
			this, ResourceManager.get( "chart.display.setting.dialog.title"), true);
		chartDisplaySettingDlg.do_modal();
	}

	/**
	 * Invoked when the "SOARS Animator Help" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_help_contents(ActionEvent actionEvent) {
		String currentDirectoryName = System.getProperty( Constant._soarsHome);
		if ( null == currentDirectoryName)
			return;

		File file = new File( currentDirectoryName + "/"
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
	 * Invoked when the "SOARS forum" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
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
	 * Invoked when the "About SOARS Animator" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_help_about(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_about( actionEvent);

		JOptionPane.showMessageDialog( this,
			Constant.get_version_message(),
			ResourceManager.get( "application.title"),
			JOptionPane.INFORMATION_MESSAGE);

		requestFocus();
	}

	/**
	 * Invoked when the "Backward head" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_backward_head(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_backward_head( actionEvent);

		update_user_interface(
			false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false,
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(),
			CommonProperty.get_instance()._repeat || !ScenarioManager.get_instance().is_tail(),
			false, true, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, false);
	}

	/**
	 * Invoked when the "Backward" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_backward(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_backward( actionEvent);

		update_user_interface(
			false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false,
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(),
			CommonProperty.get_instance()._repeat || !ScenarioManager.get_instance().is_tail(),
			false, true, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, false);
	}

	/**
	 * Invoked when the "Backward step" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_backward_step(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_backward_step( actionEvent);

		update_user_interface(
			false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false,
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(),
			CommonProperty.get_instance()._repeat || !ScenarioManager.get_instance().is_tail(),
			false, true, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, false);
	}

	/**
	 * Invoked when the "Play" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_play(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_play( actionEvent);

		update_user_interface(
			false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false,
			false, false, false, false, true, true, false, false, false, false, false, false, false);
	}

	/**
	 * Invoked when the "Pause" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_pause(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_pause( actionEvent);

		update_user_interface(
			false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false,
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(),
			CommonProperty.get_instance()._repeat || !ScenarioManager.get_instance().is_tail(),
			false, true, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, false);
	}

	/**
	 * Invoked when the "Stop" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_stop(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_stop( actionEvent);

		update_user_interface(
			true, true, ObjectManager.get_instance().exist_datafile(), ObjectManager.get_instance().exist_datafile(), true, true, true, true, true, true, true, true, true,
			true, true, true, AgentPropertyManager.get_instance().exist_property(), SpotPropertyManager.get_instance().exist_property(),
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_tail(),
			false, false, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, !ChartObjectMap.get_instance().isEmpty());

		requestFocus();
	}

	/**
	 * Invoked when the "Forward step" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_forward_step(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_forward_step( actionEvent);

		update_user_interface(
			false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false,
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(),
			CommonProperty.get_instance()._repeat || !ScenarioManager.get_instance().is_tail(),
			false, true, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, false);
	}

	/**
	 * Invoked when the "Forward" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_forward(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_forward( actionEvent);

		update_user_interface(
			false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false,
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(),
			CommonProperty.get_instance()._repeat || !ScenarioManager.get_instance().is_tail(),
			false, true, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, false);
	}

	/**
	 * Invoked when the "Forward tail" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_forward_tail(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_forward_tail( actionEvent);

		update_user_interface(
			false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false,
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(),
			CommonProperty.get_instance()._repeat || !ScenarioManager.get_instance().is_tail(),
			false, true, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, false);
	}

	/**
	 * Invoked when the "Retrieve agent property" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_retrieve_agent_property(ActionEvent actionEvent) {
		if ( !AnimatorView.get_instance().on_retrieve_agent_property( actionEvent))
			return;

		update_user_interface(
			false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false,
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(),
			CommonProperty.get_instance()._repeat || !ScenarioManager.get_instance().is_tail(),
			false, true, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, false);
	}

	/**
	 * Invoked when the "Retrieve spot property" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_retrieve_spot_property(ActionEvent actionEvent) {
		if ( !AnimatorView.get_instance().on_retrieve_spot_property( actionEvent))
			return;

		update_user_interface(
			false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false,
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(),
			CommonProperty.get_instance()._repeat || !ScenarioManager.get_instance().is_tail(),
			false, true, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, false);
	}

	/**
	 * Invoked when the "Animation slider" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_animation_slider(ActionEvent actionEvent) {
		AnimatorView.get_instance().on_animation_slider( actionEvent);

		update_user_interface(
			false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false,
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(),
			CommonProperty.get_instance()._repeat || !ScenarioManager.get_instance().is_tail(),
			false, true, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, false);
	}

	/**
	 * Updates the states of all menus.
	 */
	public void update_user_interface() {
		update_user_interface(
			false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false,
			!ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(), !ScenarioManager.get_instance().is_head(),
			CommonProperty.get_instance()._repeat || !ScenarioManager.get_instance().is_tail(),
			false, true, !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(), !ScenarioManager.get_instance().is_tail(),
			AgentPropertyManager.get_instance().exist_selected_property(),
			SpotPropertyManager.get_instance().exist_selected_property(),
			true, !ChartObjectMap.get_instance().isEmpty());
	}

	/**
	 * Sets the specified text to the status bar.
	 * @param information the specified text
	 */
	public void update(String information) {
		_informationLabel.setText( information);
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent arg0) {
		if ( AnimatorView.get_instance().is_state_animation())
			arg0.rejectDrag();
		else
			arg0.acceptDrag( DnDConstants.ACTION_COPY);
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent arg0) {
		if ( AnimatorView.get_instance().is_state_animation())
			arg0.rejectDrag();
		else
			arg0.acceptDrag( DnDConstants.ACTION_COPY);
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
		if ( AnimatorView.get_instance().is_state_animation())
			arg0.rejectDrag();
		else
			arg0.acceptDrag( DnDConstants.ACTION_COPY);
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
		if ( AnimatorView.get_instance().is_state_animation()) {
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
				load( file);
			} else if ( transferable.isDataFlavorSupported( DataFlavor.stringFlavor)) {
				arg0.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE);
				String string = ( String)transferable.getTransferData( DataFlavor.stringFlavor);
				arg0.getDropTargetContext().dropComplete( true);
				String[] files = string.split( System.getProperty( "line.separator"));
				if ( files.length <= 0)
					arg0.rejectDrop();
				else
					load( new File( new URI( files[ 0].replaceAll( "[\r\n]", ""))));
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

	/**
	 * @param file
	 */
	private void load(File file) {
		if ( file.isDirectory())
			load( file.getAbsolutePath());
		else
			on_file_open_by_drag_and_drop( file);
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
	}

	/**
	 * @param directory
	 */
	private void load(String directory) {
		on_file_import( new File( directory));
		repaint();
	}

	/**
	 * Loads the data from the log files in the specified directory.
	 * @param parentDirectory the specified directory
	 * @param rootDirectory the specified directory
	 * @return
	 */
	public boolean load(String parentDirectory, String rootDirectory) {
		boolean result = on_file_import( new File( parentDirectory), new File( rootDirectory));
		repaint();
		return result;
	}
}
