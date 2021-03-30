/*
 * Created on 2006/04/18
 */
package soars.application.launcher.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.label.URLLabel;
import soars.common.utility.swing.mac.IMacScreenMenuHandler;
import soars.common.utility.swing.mac.MacUtility;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.progress.IProgressCallback;
import soars.common.utility.swing.progress.ProgressDlg;
import soars.common.utility.swing.spinner.CustomNumberSpinner;
import soars.common.utility.swing.spinner.INumberSpinnerHandler;
import soars.common.utility.swing.spinner.NumberSpinner;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.http.HttpAuthenticator;
import soars.common.utility.tool.stream.StreamPumper;

/**
 * @author kurata
 */
public class MainFrame extends Frame implements IMacScreenMenuHandler, INumberSpinnerHandler, IMessageCallback, IProgressCallback {

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
	private JButton _updateButton = null;

	/**
	 * 
	 */
	private String _memorySize = Constant._defaultMemorySize;

	/**
	 * 
	 */
	private boolean _canceled = false;

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
		_basicPanel.setLayout( new BoxLayout( _basicPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( _basicPanel);

		setup_model_manager( _basicPanel);

		insert_horizontal_glue( _basicPanel);

		insert_horizontal_glue( _basicPanel);

		setup_visualShell( _basicPanel);

		insert_horizontal_glue( _basicPanel);

		setup_simulator( _basicPanel);

		insert_horizontal_glue( _basicPanel);

		setup_animator( _basicPanel);

		insert_horizontal_glue( _basicPanel);

		insert_horizontal_glue( _basicPanel);

		setup_gaming_builder( _basicPanel);

		insert_horizontal_glue( _basicPanel);

		setup_aadl_editor( _basicPanel);

		// TODO To disable the AADL Editor. 2009.04.03
		//insert_horizontal_glue( _basicPanel);

		setup_application_builder( _basicPanel);

		insert_horizontal_glue( _basicPanel);

		setup_animation_builder( _basicPanel);

		insert_horizontal_glue( _basicPanel);

		insert_horizontal_glue( _basicPanel);

		setup_library_manager( _basicPanel);
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

		setup_update( _bottomPanel);

		// TODO To disable the Update. 2009.11.10
		//insert_horizontal_glue( _bottom_panel);
	}

	/* (non-Javadoc)
	 * @see soars.application.launcher.common.LauncherFrameBase#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		store();
		System.exit( 0);
	}

	/**
	 * @param parent
	 */
	private void setup_model_manager(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel( ResourceManager.get( "launcher.dialog.soars.manager"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		JButton button = new JButton( ResourceManager.get( "launcher.dialog.start"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				store();
				start_model_manager();
			}
		});
		_startButtons.add( button);
		panel.add( button);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_visualShell(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel( ResourceManager.get( "launcher.dialog.visual.shell"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		JButton button = new JButton( ResourceManager.get( "launcher.dialog.start"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				store();
				start_visual_shell();
			}
		});
		_startButtons.add( button);
		panel.add( button);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_simulator(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel( ResourceManager.get( "launcher.dialog.simulator"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		JButton button = new JButton( ResourceManager.get( "launcher.dialog.start"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				store();
				start_simulator();
			}
		});
		_startButtons.add( button);
		panel.add( button);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_animator(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel( ResourceManager.get( "launcher.dialog.animator"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		JButton button = new JButton( ResourceManager.get( "launcher.dialog.start"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				store();
				start_animator();
			}
		});
		_startButtons.add( button);
		panel.add( button);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_gaming_builder(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel( ResourceManager.get( "launcher.dialog.gaming.builder"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		JButton button = new JButton( ResourceManager.get( "launcher.dialog.start"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				store();
				start_gaming_builder();
			}
		});
		_startButtons.add( button);
		panel.add( button);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_aadl_editor(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel( ResourceManager.get( "launcher.dialog.aadl.editor"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		JButton button = new JButton( ResourceManager.get( "launcher.dialog.start"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				store();
				start_aadl_editor();
			}
		});
		_startButtons.add( button);
		panel.add( button);

		// TODO To disable the AADL Editor. 2009.04.03
		//parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_application_builder(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel( ResourceManager.get( "launcher.dialog.application.builder"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		JButton button = new JButton( ResourceManager.get( "launcher.dialog.start"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				store();
				start_application_builder();
			}
		});
		_startButtons.add( button);
		panel.add( button);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_animation_builder(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel( ResourceManager.get( "launcher.dialog.animation.builder"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		JButton button = new JButton( ResourceManager.get( "launcher.dialog.start"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				store();
				start_animation_builder();
			}
		});
		_startButtons.add( button);
		panel.add( button);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_library_manager(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel( ResourceManager.get( "launcher.dialog.library.manager"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		JButton button = new JButton( ResourceManager.get( "launcher.dialog.start"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				store();
				start_library_manager();
			}
		});
		_startButtons.add( button);
		panel.add( button);

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

		String[] memory_sizes = new String[ 1 + Constant._memorySizes.length];
		memory_sizes[ 0] = ResourceManager.get( "launcher.dialog.memory.non.use");
		System.arraycopy( Constant._memorySizes, 0, memory_sizes, 1, Constant._memorySizes.length);
		_memorySizeComboBox = ComboBox.create( memory_sizes, 100, true, new CommonComboBoxRenderer( null, true));
		_memorySizeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String memory_size = ( String)_memorySizeComboBox.getSelectedItem();
				_memorySize = ( memory_size.equals( ResourceManager.get( "launcher.dialog.memory.non.use")) ? "0" : memory_size);
			}
		});
		panel.add( _memorySizeComboBox);

		JLabel label = new JLabel( "MB");
		_mbLabels.add( label);
		panel.add( label);

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
			CommonEnvironment._advancedMemorySettingKey, Constant._defaultAdvancedMemorySetting).equals( "true"));
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
		_mbLabels.add( label);
		panel.add( label);

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
			String memory_size = ( String)_memorySizeComboBox.getSelectedItem();
			_memorySize = ( memory_size.equals( ResourceManager.get( "launcher.dialog.memory.non.use")) ? "0" : memory_size);
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
	 * @param parent
	 */
	private void setup_update(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		_updateButton = new JButton( ResourceManager.get( "launcher.dialog.update"));
		_updateButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_update( arg0);
			}
		});
		panel.add( _updateButton);

		// TODO To disable the Update. 2009.11.10
		//parent.add( panel);
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
			if ( _memorySize.equals( "0") || Constant.contained( _memorySize))
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
		width = Math.max( width, _updateButton.getPreferredSize().width);

		_memorySizeComboBox.setPreferredSize( new Dimension( width - 5 - _mbLabels.get( 0).getPreferredSize().width,
			_memorySizeComboBox.getPreferredSize().height));
		_memorySizeNumberSpinner.setPreferredSize( new Dimension( width - 5 - _mbLabels.get( 1).getPreferredSize().width,
			_memorySizeNumberSpinner.getPreferredSize().height));
		_languageButton.setPreferredSize( new Dimension( width, _languageButton.getPreferredSize().height));
		_aboutButton.setPreferredSize( new Dimension( width, _aboutButton.getPreferredSize().height));
		_updateButton.setPreferredSize( new Dimension( width, _updateButton.getPreferredSize().height));
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
	public void start_model_manager() {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String soars_properties = System.getProperty( Constant._soarsProperties);

		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Model Manager");
			//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS Manager");
			list.add( "-X" + Constant._macIconFilename + "=../resource/icon/application/manager/soars/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		list.add( "-D" + Constant._soarsHome + "=" + home_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		list.add( "-D" + Constant._soarsProperties + "=" + soars_properties);
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-jar");
		list.add( home_directory_name + "/" + Constant._soarsManagerJarFilename);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));

		String[] cmdarray = list.toArray( new String[ 0]);

		debug( "SOARS Manager", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec(
				cmdarray,
				null,
				new File( home_directory_name));
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			//e.printStackTrace();
			return;
		}
	}

	/**
	* 
	*/
	private void start_visual_shell() {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String soars_properties = System.getProperty( Constant._soarsProperties);

		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS VisualShell");
			//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS VisualShell");
			list.add( "-X" + Constant._macIconFilename + "=../resource/icon/application/visualshell/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		//list.add( "-Djava.endorsed.dirs=" + home_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + home_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		list.add( "-D" + Constant._soarsProperties + "=" + soars_properties);
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-jar");
		list.add( home_directory_name + "/" + Constant._visualShellJarFilename);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));

		String[] cmdarray = list.toArray( new String[ 0]);

		debug( "VisualShell", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec(
				cmdarray,
				null,
				new File( home_directory_name));
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			//e.printStackTrace();
			return;
		}
	}

	/**
	 * 
	 */
	private void start_simulator() {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String soars_properties = System.getProperty( Constant._soarsProperties);

		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Simulator");
			//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS Simulator");
			list.add( "-X" + Constant._macIconFilename + "=../resource/icon/application/simulator/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		//list.add( "-Djava.endorsed.dirs=" + home_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + home_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		list.add( "-D" + Constant._soarsProperties + "=" + soars_properties);
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-jar");
		list.add( home_directory_name + "/" + Constant._simulatorJarFilename);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));

		String[] cmdarray = list.toArray( new String[ 0]);

		debug( "Simulator", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec(
				cmdarray,
				null,
				new File( home_directory_name));
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			//e.printStackTrace();
			return;
		}
	}

	/**
	 * 
	 */
	private void start_animator() {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String soars_properties = System.getProperty( Constant._soarsProperties);

		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Animator");
			//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS Animator");
			list.add( "-X" + Constant._macIconFilename + "=../resource/icon/application/animator/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		//list.add( "-Djava.endorsed.dirs=" + home_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + home_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		list.add( "-D" + Constant._soarsProperties + "=" + soars_properties);
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-jar");
		list.add( home_directory_name + "/" + Constant._animatorJarFilename);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));

		String[] cmdarray = list.toArray( new String[ 0]);

		debug( "Animator", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec(
				cmdarray,
				null,
				new File( home_directory_name));
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			//e.printStackTrace();
			return;
		}
	}

	/**
	 * 
	 */
	private void start_gaming_builder() {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String soars_properties = System.getProperty( Constant._soarsProperties);

		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Gaming Builder");
			//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS Gaming Builder");
			//list.add( "-X" + Constant._mac_icon_filename + "=../resource/icon/application/gamingbuilder/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		//list.add( "-Djava.endorsed.dirs=" + home_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + home_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		list.add( "-D" + Constant._soarsProperties + "=" + soars_properties);
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-jar");
		list.add( home_directory_name + "/../" + Constant._gamingBuilderJarFilename);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));

		String[] cmdarray = list.toArray( new String[ 0]);

		debug( "Gaming Builder", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec(
				cmdarray,
				null,
				new File( home_directory_name));
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			//e.printStackTrace();
			return;
		}
	}

	/**
	 * 
	 */
	private void start_aadl_editor() {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String soars_properties = System.getProperty( Constant._soarsProperties);

		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			//list.add( "-D" + Constant._system_default_file_encoding + "=" + System.getProperty( Constant._system_default_file_encoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=AADL Editor");
			//list.add( "-D" + Constant._mac_screen_menu_name + "=AADL Editor");
			//list.add( "-X" + Constant._mac_icon_filename + "=../resource/icon/application/aadleditor/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		//list.add( "-Djava.endorsed.dirs=" + home_directory_name + "/../" + Constant._xerces_directory);
		//list.add( "-D" + Constant._soars_home + "=" + home_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		list.add( "-D" + Constant._soarsProperties + "=" + soars_properties);
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-jar");
		list.add( home_directory_name + "/../" + Constant._aadlEditorJarFilename);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));

		String[] cmdarray = list.toArray( new String[ 0]);

		debug( "AADL Editor", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec(
				cmdarray,
				null,
				new File( home_directory_name));
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			//e.printStackTrace();
			return;
		}
	}

	/**
	 * 
	 */
	public void start_application_builder() {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String soars_properties = System.getProperty( Constant._soarsProperties);

		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Application Builder");
			//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS Application Builder");
			//list.add( "-X" + Constant._mac_icon_filename + "=../resource/icon/application/builder/application/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		//list.add( "-Djava.endorsed.dirs=" + home_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + home_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		list.add( "-D" + Constant._soarsProperties + "=" + soars_properties);
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-cp");
		list.add( home_directory_name + "/../" + Constant._applicationBuilderJarFilename);
		list.add( Constant._applicationBuilderMainClassname);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));

		String[] cmdarray = list.toArray( new String[ 0]);

		debug( "Application Builder", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec(
				cmdarray,
				null,
				new File( home_directory_name));
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			//e.printStackTrace();
			return;
		}
	}

	/**
	 * 
	 */
	public void start_animation_builder() {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String soars_properties = System.getProperty( Constant._soarsProperties);

		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Animation Builder");
			//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS Animation Builder");
			list.add( "-X" + Constant._macIconFilename + "=../resource/icon/application/builder/animation/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		//list.add( "-Djava.endorsed.dirs=" + home_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + home_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		list.add( "-D" + Constant._soarsProperties + "=" + soars_properties);
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-jar");
		list.add( home_directory_name + "/" + Constant._animationBuilderJarFilename);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));

		String[] cmdarray = list.toArray( new String[ 0]);

		debug( "Animation Builder", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec(
				cmdarray,
				null,
				new File( home_directory_name));
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			//e.printStackTrace();
			return;
		}
	}

	/**
	 * 
	 */
	public void start_library_manager() {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String soars_properties = System.getProperty( Constant._soarsProperties);

		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Library Manager");
			//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS Library Manager");
			list.add( "-X" + Constant._macIconFilename + "=../resource/icon/application/manager/library/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		list.add( "-D" + Constant._soarsHome + "=" + home_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		list.add( "-D" + Constant._soarsProperties + "=" + soars_properties);
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-jar");
		list.add( home_directory_name + "/" + Constant._libraryManagerJarFilename);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));

		String[] cmdarray = list.toArray( new String[ 0]);

		debug( "Library Manager", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec(
				cmdarray,
				null,
				new File( home_directory_name));
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
		text += ( "Type : " + type + Constant._lineSeparator);
		text += ( "OS : " + osname + " [" + osversion + "]" + Constant._lineSeparator);
		for ( int i = 0; i < cmdarray.length; ++i)
			text += ( "Parameter : " + cmdarray[ i] + Constant._lineSeparator);

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
		String home_directory_name = System.getProperty( Constant._soarsHome);
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String soars_properties = System.getProperty( Constant._soarsProperties);

		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS");
			//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS");
			list.add( "-X" + Constant._macIconFilename + "=resource/icon/application/launcher/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		//list.add( "-Djava.endorsed.dirs=" + home_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + home_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		list.add( "-D" + Constant._soarsProperties + "=" + soars_properties);
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-jar");
		list.add( current_directory.getAbsolutePath() + "/" + Constant._soarsLauncherJarFilename);
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
		String current_directory_name = System.getProperty( Constant._soarsHome);
		if ( null == current_directory_name)
			objects = new Object[] { Constant.get_version_message(), panel1};
		else {
			File file = new File( current_directory_name + "/"
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

	/**
	 * @param actionEvent
	 */
	private void on_update(ActionEvent actionEvent) {
		_canceled = false;
		if ( !ProgressDlg.execute( this,
			ResourceManager.get( "application.title") + " - " + ResourceManager.get( "launcher.dialog.download"),
			true, "update", ResourceManager.get( "dialog.cancel"), this, this)) {
			if ( _canceled)
				return;

			JOptionPane.showMessageDialog(
				this,
				ResourceManager.get( "launcher.dialog.download.error.message"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.progress.IProgressCallback#progress_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.progress.ProgressDlg)
	 */
	public boolean progress_callback(String id, Object[] objects, ProgressDlg progressDlg) {
		try {
			URL url = new URL( "http://lydian7th.servehttp.com/lydian7th/soars/impression.jpg");
			HttpURLConnection httpURLConnection = ( HttpURLConnection)url.openConnection();
			httpURLConnection.setRequestMethod( "GET");
			httpURLConnection.setInstanceFollowRedirects( false);

			HttpAuthenticator httpAuthenticator = new HttpAuthenticator( "soars", "hogehoge#");
			Authenticator.setDefault( httpAuthenticator);

			httpURLConnection.connect();

			InputStream inputStream = new BufferedInputStream( httpURLConnection.getInputStream());
			byte[] bytes = read( inputStream, httpURLConnection.getContentLength(), progressDlg);
			inputStream.close();
			if ( null == bytes)
				return false;

			String home_directory_name = System.getProperty( Constant._soarsHome);
			String tmp_directory_name = SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._tmpKey, "");
			File directory = new File( home_directory_name + "/" + tmp_directory_name + Constant._download_directory);
			if ( !directory.exists() && !directory.mkdirs())
				return false;

			if ( !FileUtility.write( bytes, directory.getAbsolutePath() + "/" + "impression.jpg"))
				return false;

			httpURLConnection.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param inputStream
	 * @param size 
	 * @param progressDlg
	 * @return
	 */
	private byte[] read(InputStream inputStream, int size, ProgressDlg progressDlg) {
		List list = new ArrayList();
		int c;
		try {
			int length = 0;
			while ( -1 != ( c = inputStream.read())) {
				if ( progressDlg._canceled) {
					_canceled = true;
					return null;
				}
				list.add( Byte.valueOf( ( byte)c));
				if ( 0 < size)
					progressDlg.set( ++length * 100 / size);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		if ( list.isEmpty())
			return null;

		byte[] result = new byte[ list.size()];
		for ( int i = 0; i < list.size(); ++i) {
			Byte b = ( Byte)list.get( i);
			result[ i] = b.byteValue();
		}

		return result;
	}
}
