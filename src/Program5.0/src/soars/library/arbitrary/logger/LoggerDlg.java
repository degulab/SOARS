/**
 * 
 */
package soars.library.arbitrary.logger;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import env.Agent;
import env.EquippedObject;
import env.Spot;
import soars.common.soars.logger.LogProperties;
import soars.common.utility.swing.window.Dialog;
import soars.common.utility.tool.jackson.JacksonUtility;
import time.TimeCounter;

/**
 * @author kurata
 *
 */
public class LoggerDlg extends Dialog {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	private int _minimumWidth = -1;

	/**
	 * 
	 */
	private int _minimumHeight = -1;

	/**
	 * 
	 */
	static final String _propertiesFileName = "properties.json";

	/**
	 * 
	 */
	private LogProperties _logProperties = null;

	/**
	 * 
	 */
	private Map<String, File> _folderMap = new HashMap<>();

	/**
	 * 
	 */
	private int _counter = 0;

	/**
	 * 
	 */
	private JTextField _titleTextField = null;

	/**
	 * 
	 */
	private JTextField _experimentNameTextField = null;

	/**
	 * 
	 */
	private JTextField _filePathTextField = null;

	/**
	 * 
	 */
	private JButton _logButton = null;

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<>();

	/**
	 * 
	 */
	private boolean _flag = false;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public LoggerDlg(Frame arg0, String arg1, boolean arg2) {
		super(arg0, arg1, arg2);
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._loggerDialogRectangleKey + "x",
			"-1"/*String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).x)*/);
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._loggerDialogRectangleKey + "y",
			"-1"/*String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).y)*/);
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._loggerDialogRectangleKey + "width",
			"-1"/*String.valueOf( _minimumWidth)*/);
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._loggerDialogRectangleKey + "height",
			"-1"/*String.valueOf( _minimumHeight)*/);
		if ( null == value)
			return null;

		int height = Integer.parseInt( value);

		return new Rectangle( x, y, width, height);
	}

//	/**
//	 * 
//	 */
//	private void optimize_window_rectangle() {
//		Rectangle rectangle = getBounds();
//		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( rectangle)
//			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).width <= 10
//			|| rectangle.y <= -getInsets().top
//			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).height <= getInsets().top) {
//			setSize( _minimumWidth, _minimumHeight);
//			setLocationRelativeTo( getOwner());
//		}
//	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Rectangle rectangle = getBounds();

		Environment.get_instance().set(
			Environment._loggerDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._loggerDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._loggerDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._loggerDialogRectangleKey + "height", String.valueOf( rectangle.height));

		Environment.get_instance().store();
	}

	/**
	 * @param logFolderPath
	 * @return
	 */
	public boolean create(String logFolderPath) {
		File logFolder = new File( logFolderPath);
		if ( !logFolder.exists())
			return false;

		_logProperties = get( logFolder);
		if ( null == _logProperties)
			return false;

		Set<String> keys = _logProperties._entityNamesMap.keySet();
		for ( String key:keys) {
			File folder = new File( logFolder, key);
			if ( !folder.mkdirs())
				return false;

			_folderMap.put( key, folder);
		}

		if ( !sort())
			return false;

		for ( String key:keys) {
			Set<String> variables = _logProperties._entityNamesMap.get( key).keySet();
			for ( String variable:variables) {
				if ( !write_header( EquippedObject.ID_KEY, _folderMap.get( key), variable, _logProperties._entityNamesMap.get( key).get( variable)))
					return false;

				if ( !write_header( EquippedObject.NAME_KEY, _folderMap.get( key), variable, _logProperties._entityNamesMap.get( key).get( variable)))
					return false;

				if ( !write_initial_value( _folderMap.get( key), variable, _logProperties._entityNamesMap.get( key).get( variable)))
					return false;
			}
		}

		Rectangle rectangle = get_rectangle_from_environment_file();

		if ( null != rectangle && 0 < rectangle.x && 0 < rectangle.y && 0 < rectangle.width && 0 < rectangle.height)
			return create( rectangle);
		else {
			Rectangle screen = getGraphicsConfiguration().getBounds();
			return create( ( screen.width - 600) / 2, ( screen.height - 200) / 2, 600, 200);
		}
	}

	/**
	 * @return
	 */
	private boolean sort() {
		Set<String> keys = _logProperties._entityNamesMap.keySet();
		for ( String key:keys) {
			Set<String> variables = _logProperties._entityNamesMap.get( key).keySet();
			for ( String variable:variables)
				Collections.sort( _logProperties._entityNamesMap.get( key).get( variable), new EntityComparator<String>());
		}
		return true;
	}

	/**
	 * @param logFolder
	 * @return
	 */
	private LogProperties get(File logFolder) {
		File file = new File( logFolder, _propertiesFileName);
		if ( !file.exists())
			return null;

		return ( LogProperties)JacksonUtility.get_object( file, LogProperties.class);
	}

	/**
	 * @param key 
	 * @param folder 
	 * @param variableName 
	 * @param entityNames
	 * @return
	 */
	private boolean write_header(String key, File folder, String variableName, List<String> entityNames) {
		PrintWriter printWriter = null;
		try {
			printWriter = get_printWriter( folder, variableName);
			if ( null == printWriter)
				return false;

			printWriter.print( "\t" + key);

			for ( String entityName:entityNames) {
				if ( !write_header( entityName, key, printWriter))
					return false;
			}

			printWriter.println( "");

		} finally {
			printWriter.close();
		}
		return true;
	}

	/**
	 * @param entityName
	 * @param key 
	 * @param printWriter
	 * @return
	 */
	private boolean write_header(String entityName, String key, PrintWriter printWriter) {
		EquippedObject equippedObject = get_equippedObject( entityName);
		if ( null != equippedObject)
			printWriter.print( "\t" + equippedObject.getProp( key));
		else {
			int index = 1;
			while ( true) {
				equippedObject = get_equippedObject( entityName + String.valueOf( index));
				if ( null == equippedObject)
					return ( 1 < index) ? true : false;

				printWriter.print( "\t" + equippedObject.getProp( key));
				++index;
			}
		}
		return true;
	}

	/**
	 * @param folder
	 * @param variableName
	 * @param entityNames
	 * @return
	 */
	private boolean write_initial_value(File folder, String variableName, List<String> entityNames) {
		PrintWriter printWriter = null;
		try {
			printWriter = get_printWriter( folder, variableName);
			if ( null == printWriter)
				return false;

			printWriter.print( "\t" + variableName);

			for ( String entityName:entityNames) {
				if ( !write_value( entityName, variableName, printWriter))
					return false;
			}

			printWriter.println( "");

		} finally {
			printWriter.close();
		}
		return true;
	}

	/**
	 * @param entityName
	 * @param variableName
	 * @param printWriter
	 * @return
	 */
	private boolean write_value(String entityName, String variableName, PrintWriter printWriter) {
		EquippedObject equippedObject = get_equippedObject( entityName);
		if ( null != equippedObject)
			printWriter.print( "\t" + equippedObject.getEquip( variableName).toString());
		else {
			int index = 1;
			while ( true) {
				equippedObject = get_equippedObject( entityName + String.valueOf( index));
				if ( null == equippedObject)
					return ( 1 < index) ? true : false;

				printWriter.print( "\t" + equippedObject.getEquip( variableName).toString());
				++index;
			}
		}
		return true;
	}

	/**
	 * @param folder
	 * @param variableName
	 * @return
	 */
	private PrintWriter get_printWriter(File folder, String variableName) {
		try {
			File file = new File( folder, variableName + ".log");
			FileWriter fileWriter = new FileWriter( file, true);
			return new PrintWriter( new BufferedWriter( fileWriter));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param entityName
	 * @return
	 */
	public static EquippedObject get_equippedObject(String entityName) {
		Agent agent = Agent.forName( entityName);
		return ( null != agent) ? agent : Spot.forName( entityName);
	}

	@Override
	protected boolean on_init_dialog() {
		if (!super.on_init_dialog())
			return false;

		getContentPane().setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_titleTextField( northPanel);

		insert_horizontal_glue( northPanel);

		setup_experimentNameTextField( northPanel);

		insert_horizontal_glue( northPanel);

		setup_filePathTextField( northPanel);

		getContentPane().add( northPanel, "North");

		insert_horizontal_glue( northPanel);

		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		setup_logButton( southPanel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");

		adjust();

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		return true;
	}

	/**
	 * @param parent 
	 * 
	 */
	private void setup_titleTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "logger.dialog.simulation.title"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_titleTextField = new JTextField( _logProperties._title);
		_titleTextField.setEditable( false);
		panel.add( _titleTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent 
	 * 
	 */
	private void setup_experimentNameTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "logger.dialog.experiment.name"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_experimentNameTextField = new JTextField( _logProperties._experimentName);
		_experimentNameTextField.setEditable( false);
		panel.add( _experimentNameTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent 
	 * 
	 */
	private void setup_filePathTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "logger.dialog.soars.file"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_filePathTextField = new JTextField( _logProperties._filePath);
		_filePathTextField.setEditable( false);
		panel.add( _filePathTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent 
	 */
	private void setup_logButton(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		_logButton = new JButton( ResourceManager.get_instance().get( "logger.dialog.log.button"));
		_logButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_log( arg0);
			}
		});
		buttonPanel.add( _logButton);
		panel.add( buttonPanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;

		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);

		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));
	}

	@Override
	protected void processWindowEvent(WindowEvent e) {
		if ( WindowEvent.WINDOW_CLOSING == e.getID())
			return;

		super.processWindowEvent(e);
	}

	@Override
	protected void on_setup_completed() {
		_minimumWidth = getPreferredSize().width;
		_minimumHeight = getPreferredSize().height;

		_logButton.requestFocusInWindow();

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( 0 > _minimumWidth || 0 > _minimumHeight)
					return;

				int width = getSize().width;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width, _minimumHeight);
			}
		});

		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				set_property_to_environment_file();
			}
		});

		setVisible( true);
	}

	/**
	 * @param arg0
	 */
	protected void on_log(ActionEvent arg0) {
		synchronized( _lock) {
			_flag = true;
			_logButton.setEnabled( false);
		}
	}

	/**
	 * 
	 */
	public void log() {
		synchronized( _lock) {
			if ( !_flag)
				return;

			Set<String> keys = _logProperties._entityNamesMap.keySet();
			for ( String key:keys) {
				Set<String> variables = _logProperties._entityNamesMap.get( key).keySet();
				for ( String variable:variables)
					write_log( _folderMap.get( key), variable, _logProperties._entityNamesMap.get( key).get( variable));
			}
			++_counter;
			_flag = false;
			_logButton.setEnabled( true);
		}
	}

	/**
	 * @param folder
	 * @param variableName
	 * @param entityNames
	 * @return
	 */
	private boolean write_log(File folder, String variableName, List<String> entityNames) {
		PrintWriter printWriter = null;
		try {
			printWriter = get_printWriter( folder, variableName);
			if ( null == printWriter)
				return false;

			printWriter.print( TimeCounter.getCurrentTime().toString() + "\t" + String.valueOf( _counter));

			for ( String entityName:entityNames) {
				if ( !write_value( entityName, variableName, printWriter))
					return false;
			}

			printWriter.println( "");

		} finally {
			printWriter.close();
		}
		return true;
	}

	/**
	 * 
	 */
	public void terminate() {
		JOptionPane.showMessageDialog(
			this,
			ResourceManager.get_instance().get( "logger.dialog.termination"),
			ResourceManager.get_instance().get( "logger.dialog.title"),
			JOptionPane.INFORMATION_MESSAGE );
	}

	@Override
	public void dispose() {
		set_property_to_environment_file();
		super.dispose();
	}
}
