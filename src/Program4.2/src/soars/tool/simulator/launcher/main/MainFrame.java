/**
 * 
 */
package soars.tool.simulator.launcher.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.label.URLLabel;
import soars.common.utility.swing.mac.IMacScreenMenuHandler;
import soars.common.utility.swing.mac.MacUtility;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.spinner.CustomNumberSpinner;
import soars.common.utility.swing.spinner.INumberSpinnerHandler;
import soars.common.utility.swing.spinner.NumberSpinner;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.stream.StreamPumper;

/**
 * @author kurata
 *
 */
public class MainFrame extends Frame implements IMacScreenMenuHandler, INumberSpinnerHandler, IMessageCallback {

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
	private JPanel _mainPanel = null;

	/**
	 * 
	 */
	private JPanel _topPanel = null;

	/**
	 * 
	 */
	private JPanel _basicPanel = null;

	/**
	 * 
	 */
	private JPanel _sidePanel = null;

	/**
	 * 
	 */
	private JPanel _bottomPanel = null;

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private List<JButton> _startButtons = new ArrayList<JButton>();

	/**
	 * 
	 */
	private JLabel _memorySizeLabel = null;

	/**
	 * 
	 */
	private ComboBox _memorySizeComboBox = null;

	/**
	 * 
	 */
	private JCheckBox _memorySizeCheckBox = null;

	/**
	 * 
	 */
	private CustomNumberSpinner _memorySizeNumberSpinner = null;

	/**
	 * 
	 */
	private List<JLabel> _mbLabels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private JButton _languageButton = null;

	/**
	 * 
	 */
	private JButton _aboutButton = null;

	/**
	 * 
	 */
	private String _memorySize = CommonConstant._defaultMemorySize;

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
	 * @param arg0
	 * @throws HeadlessException
	 */
	public MainFrame(String arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * 
	 */
	private void store() {
		set_properties();
		CommonEnvironment.get_instance().store();
	}

	/**
	 * 
	 */
	private void set_properties() {
		CommonEnvironment.get_instance().set_memory_size( _memorySize);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		if ( !MacUtility.setup_screen_menu_handler( this, this, ResourceManager.get( "application.title")))
			return false;

		setResizable( false);

		_mainPanel = new JPanel();
		_mainPanel.setLayout( new BoxLayout( _mainPanel, BoxLayout.Y_AXIS));

		setup_top_panel();
		_mainPanel.add( _topPanel);

		setup_bottom_panel();
		_mainPanel.add( _bottomPanel);

		initialize();

		adjust();

    setContentPane( _mainPanel);

		pack();

		setLocationRelativeTo( null);

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		setVisible( true);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_about()
	 */
	public void on_mac_about() {
		on_about( null);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_quit()
	 */
	public void on_mac_quit() {
		on_window_closing( null);
	}

	/**
	 * 
	 */
	private void setup_top_panel() {
		_topPanel = new JPanel();
		_topPanel.setLayout( new BoxLayout( _topPanel, BoxLayout.X_AXIS));

		setup_basic_panel();
		_topPanel.add( _basicPanel);

		setup_side_panel();
		_topPanel.add( _sidePanel);
	}

	/**
	 * 
	 */
	private void setup_basic_panel() {
		_basicPanel = new JPanel();
		_basicPanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_simulator( northPanel);

		_basicPanel.add( northPanel, "North");
	}

	/**
	 * 
	 */
	private void setup_side_panel() {
		_sidePanel = new JPanel();
		_sidePanel.setLayout( new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( panel);

		setup_default_memory_panel( panel);

		insert_horizontal_glue( panel);

		setup_advanced_memory_panel( panel);

		insert_horizontal_glue( panel);

		setup_language_panel( panel);

		insert_horizontal_glue( panel);

		_sidePanel.add( panel, "North");
	}

	/**
	 * 
	 */
	private void setup_bottom_panel() {
		_bottomPanel = new JPanel();
		_bottomPanel.setLayout( new BoxLayout( _bottomPanel, BoxLayout.Y_AXIS));

		setup_about( _bottomPanel);

		insert_horizontal_glue( _bottomPanel);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		store();
		System.exit( 0);
	}

	/**
	 * @param parent
	 */
	private void setup_simulator(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JButton button = new JButton( ResourceManager.get( "launcher.dialog.simulator.start"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				store();
				start_simulator();
			}
		});
		panel.add( button);
		_startButtons.add( button);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_default_memory_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_memorySizeLabel = new JLabel( ResourceManager.get( "launcher.dialog.memory.size.label"));
		_memorySizeLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _memorySizeLabel);

		String[] memorySizes = new String[ 1 + CommonConstant._memorySizes.length];
		memorySizes[ 0] = ResourceManager.get( "launcher.dialog.memory.non.use");
		System.arraycopy( CommonConstant._memorySizes, 0, memorySizes, 1, CommonConstant._memorySizes.length);
		_memorySizeComboBox = ComboBox.create( memorySizes, 100, true, new CommonComboBoxRenderer( null, true));
		_memorySizeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String memorySize = ( String)_memorySizeComboBox.getSelectedItem();
				_memorySize = ( memorySize.equals( ResourceManager.get( "launcher.dialog.memory.non.use")) ? "0" : memorySize);
			}
		});
		panel.add( _memorySizeComboBox);

		JLabel label = new JLabel( "MB");
		panel.add( label);
		_mbLabels.add( label);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_advanced_memory_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_memorySizeCheckBox = new JCheckBox( ResourceManager.get( "launcher.dialog.advanced.setting.label"));
		_memorySizeCheckBox.setHorizontalAlignment( SwingConstants.RIGHT);
		_memorySizeCheckBox.setSelected( CommonEnvironment.get_instance().get(
			CommonEnvironment._advancedMemorySettingKey, CommonConstant._defaultAdvancedMemorySetting).equals( "true"));
		_memorySizeCheckBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				on_state_changed( ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _memorySizeCheckBox);

		_memorySizeNumberSpinner = new CustomNumberSpinner( this);
		_memorySizeNumberSpinner.set_minimum( 0);
		_memorySizeNumberSpinner.set_maximum( 1000000);
		_memorySizeNumberSpinner.setPreferredSize( new Dimension( 100,
			_memorySizeNumberSpinner.getPreferredSize().height));
		panel.add( _memorySizeNumberSpinner);

		JLabel label = new JLabel( "MB");
		panel.add( label);
		_mbLabels.add( label);

		parent.add( panel);
	}

	/**
	 * @param selected
	 */
	private void on_state_changed(boolean selected) {
		_memorySizeLabel.setEnabled( !selected);
		_memorySizeComboBox.setEnabled( !selected);
		_mbLabels.get( 0).setEnabled( !selected);
		_memorySizeNumberSpinner.setEnabled( selected);
		_mbLabels.get( 1).setEnabled( selected);
		if ( selected)
			_memorySize = String.valueOf( _memorySizeNumberSpinner.get_value());
		else {
			String memorySize = ( String)_memorySizeComboBox.getSelectedItem();
			_memorySize = ( memorySize.equals( ResourceManager.get( "launcher.dialog.memory.non.use")) ? "0" : memorySize);
		}
		CommonEnvironment.get_instance().set( CommonEnvironment._advancedMemorySettingKey, String.valueOf( selected));
	}

	/**
	 * @param parent
	 */
	private void setup_language_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		_languageButton = new JButton( ResourceManager.get( "launcher.dialog.language"));
		_languageButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_language( arg0);
			}
		});
		panel.add( _languageButton);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_about(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		_aboutButton = new JButton( ResourceManager.get( "launcher.dialog.about"));
		_aboutButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_about( arg0);
			}
		});
		panel.add( _aboutButton);

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void initialize() {
		_memorySize = CommonEnvironment.get_instance().get_memory_size();

		_memorySizeLabel.setEnabled( !_memorySizeCheckBox.isSelected());
		_memorySizeComboBox.setEnabled( !_memorySizeCheckBox.isSelected());
		_mbLabels.get( 0).setEnabled( !_memorySizeCheckBox.isSelected());
		_memorySizeNumberSpinner.setEnabled( _memorySizeCheckBox.isSelected());
		_mbLabels.get( 1).setEnabled( _memorySizeCheckBox.isSelected());

		_memorySizeNumberSpinner.set_value( Integer.valueOf( _memorySize).intValue());
		if ( !_memorySizeCheckBox.isSelected()) {
			if ( _memorySize.equals( "0") || CommonConstant.contained( _memorySize))
				_memorySizeComboBox.setSelectedItem( _memorySize.equals( ResourceManager.get( "launcher.dialog.memory.non.use")) ? "0" : _memorySize);
			else {
				_memorySizeCheckBox.setSelected( true);
				_memorySizeNumberSpinner.set_value( Integer.valueOf( _memorySize).intValue());
			}
		}
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);

		width = Math.max( width, _memorySizeLabel.getPreferredSize().width);
		width = Math.max( width, _memorySizeCheckBox.getPreferredSize().width);

		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));

		_memorySizeLabel.setPreferredSize( new Dimension( width, _memorySizeLabel.getPreferredSize().height));
		_memorySizeCheckBox.setPreferredSize( new Dimension( width, _memorySizeCheckBox.getPreferredSize().height));


		width = ( _memorySizeComboBox.getPreferredSize().width + 5 + _mbLabels.get( 0).getPreferredSize().width);
		width = Math.max( width, _memorySizeNumberSpinner.getPreferredSize().width + 5 + _mbLabels.get( 1).getPreferredSize().width);
		width = Math.max( width, _languageButton.getPreferredSize().width);
		width = Math.max( width, _aboutButton.getPreferredSize().width);

		_memorySizeComboBox.setPreferredSize( new Dimension( width - 5 - _mbLabels.get( 0).getPreferredSize().width,
			_memorySizeComboBox.getPreferredSize().height));
		_memorySizeNumberSpinner.setPreferredSize( new Dimension( width - 5 - _mbLabels.get( 1).getPreferredSize().width,
			_memorySizeNumberSpinner.getPreferredSize().height));
		_languageButton.setPreferredSize( new Dimension( width, _languageButton.getPreferredSize().height));
		_aboutButton.setPreferredSize( new Dimension( width, _aboutButton.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.spinner.INumberSpinnerHandler#changed(java.lang.String, soars.common.utility.swing.spinner.NumberSpinner)
	 */
	public void changed(String number, NumberSpinner numberSpinner) {
		number = ( number.equals( "") ? "0" : number);
		if ( numberSpinner.equals( _memorySizeNumberSpinner))
			_memorySize = number;
	}

	/**
	 * 
	 */
	private void start_simulator() {
		String homeDirectoryName = System.getProperty( CommonConstant._soarsHome);
		String propertyDirectoryName = System.getProperty( CommonConstant._soarsProperty);
		String propertiesFilename = System.getProperty( CommonConstant._soarsProperties);

		String memorySize = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( CommonConstant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( CommonConstant._macJava);
			//if ( System.getProperty( CommonConstant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			//list.add( "-D" + CommonConstant._systemDefaultFileEncoding + "=" + System.getProperty( CommonConstant._systemDefaultFileEncoding, ""));
			list.add( "-X" + CommonConstant._macScreenMenuName + "=SOARS Simulator");
			//list.add( "-D" + CommonConstant._mac_screen_menu_name + "=SOARS Simulator");
			list.add( "-X" + CommonConstant._macIconFilename + "=../resource/icon/application/simulator/icon.png");
			list.add( "-D" + CommonConstant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		list.add( "-D" + CommonConstant._soarsHome + "=" + homeDirectoryName);
		list.add( "-D" + CommonConstant._soarsProperty + "=" + propertyDirectoryName);
		list.add( "-D" + CommonConstant._soarsProperties + "=" + propertiesFilename);
		if ( !memorySize.equals( "0")) {
			list.add( "-D" + CommonConstant._soarsMemorySize + "=" + memorySize);
			list.add( "-Xmx" + memorySize + "m");
		}
		list.add( "-jar");
		list.add( homeDirectoryName + "/" + CommonConstant._simulatorJarFilename);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));
		list.add( "-script");
		list.add( homeDirectoryName + "/../data/model.sor");
		list.add( "-zip");
		list.add( homeDirectoryName + "/../data/parameters.zip");
		list.add( "-demo");

		String[] cmdarray = list.toArray( new String[ 0]);

		debug( "Simulator", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec(
				cmdarray,
				null,
				new File( homeDirectoryName));
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			//e.printStackTrace();
			return;
		}
	}

	/**
	 * @param type
	 * @param osname
	 * @param osversion
	 * @param cmdarray
	 */
	private void debug(String type, String osname, String osversion, String[] cmdarray) {
		String text = ""; 
		text += ( "Type : " + type + CommonConstant._lineSeparator);
		text += ( "OS : " + osname + " [" + osversion + "]" + CommonConstant._lineSeparator);
		for ( int i = 0; i < cmdarray.length; ++i)
			text += ( "Parameter : " + cmdarray[ i] + CommonConstant._lineSeparator);

		Clipboard.set( text);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_language(ActionEvent actionEvent) {
		String language = CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, "en");
		LanguageSelectorDlg languageSelectorDlg = new LanguageSelectorDlg( this, ResourceManager.get( "launcher.dialog.language"), true);
		if ( !languageSelectorDlg.do_modal( this))
			return;

		if ( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, "en").equals( language))
			return;

		store();

		if ( !MessageDlg.execute( this, ResourceManager.get( "application.title"), true,
			"reboot", ResourceManager.get( "launcher.dialog.soars.reboot.messsage"), this, this)) {
			JOptionPane.showMessageDialog( this,
				ResourceManager.get( "launcher.dialog.soars.reboot.error.messsage"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		dispose();
		System.exit( 0);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "reboot"))
			return reboot();

		return true;
	}

	/**
	 * @return
	 */
	private boolean reboot() {
		File current_directory = new File( "");
		if ( null == current_directory)
			return false;

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec( get_cmdarray( current_directory));
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param current_directory
	 * @return
	 */
	private String[] get_cmdarray(File current_directory) {
		String homeDirectoryName = System.getProperty( CommonConstant._soarsHome);
		String propertyDirectoryName = System.getProperty( CommonConstant._soarsProperty);
		String propertiesFilename = System.getProperty( CommonConstant._soarsProperties);

		String memorySize = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( CommonConstant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( CommonConstant._macJava);
			//if ( System.getProperty( CommonConstant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + CommonConstant._systemDefaultFileEncoding + "=" + System.getProperty( CommonConstant._systemDefaultFileEncoding, ""));
			list.add( "-X" + CommonConstant._macScreenMenuName + "=" + ResourceManager.get( "application.title"));
			//list.add( "-D" + CommonConstant._mac_screen_menu_name + "=SOARS");
			list.add( "-X" + CommonConstant._macIconFilename + "=resource/icon/application/simulator/icon.png");
			list.add( "-D" + CommonConstant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		//list.add( "-Djava.endorsed.dirs=" + home_directory_name + "/../" + CommonConstant._xerces_directory);
		list.add( "-D" + CommonConstant._soarsHome + "=" + homeDirectoryName);
		list.add( "-D" + CommonConstant._soarsProperty + "=" + propertyDirectoryName);
		list.add( "-D" + CommonConstant._soarsProperties + "=" + propertiesFilename);
		if ( !memorySize.equals( "0")) {
			list.add( "-D" + CommonConstant._soarsMemorySize + "=" + memorySize);
			list.add( "-Xmx" + memorySize + "m");
		}
		list.add( "-jar");
		list.add( current_directory.getAbsolutePath() + "/" + Constant._program);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));

		return list.toArray( new String[ 0]);
	}

	/**
	 * @param actionEvent
	 */
	private void on_about(ActionEvent actionEvent) {
		JPanel panel1 = new JPanel();
		panel1.setLayout( new BoxLayout( panel1, BoxLayout.X_AXIS));

		panel1.add( new JLabel( "URL : "));
		String url = SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._urlKey, "");
		if ( !url.equals( ""))
			panel1.add( new URLLabel( url));


		Object[] objects = null;
		String currentDirectoryName = System.getProperty( CommonConstant._soarsHome);
		if ( null == currentDirectoryName)
			objects = new Object[] { Constant.get_version_message(), panel1};
		else {
			File file = new File( currentDirectoryName + "/"
				+ ResourceManager.get( "launcher.dialog.about.help.contents"));
			if ( !file.exists() || !file.canRead())
				objects = new Object[] { Constant.get_version_message(), panel1};
			else {
				JPanel panel2 = new JPanel();
				panel2.setLayout( new BoxLayout( panel2, BoxLayout.X_AXIS));
				try {
					panel2.add( new URLLabel( file.toURI().toURL().toString().replaceAll( "\\\\", "/"), "SOARS Help"));
					objects = new Object[] { Constant.get_version_message(), panel1, panel2};
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				//panel2.add( new URLLabel( "file:///" + file.getAbsolutePath().replaceAll( "\\\\", "/"), "SOARS Help"));
			}
		}

		JOptionPane.showMessageDialog(
			this,
			objects,
			ResourceManager.get( "application.title"),
			//JOptionPane.INFORMATION_MESSAGE,
			//	new ImageIcon( Resource.load_image_from_resource( "/application/soars/resource/image/picture/picture.jpg", getClass())));
			JOptionPane.INFORMATION_MESSAGE);
	}
}
