/**
 * 
 */
package soars.application.simulator.main.log;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.InternalFrameEvent;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.simulator.data.LogData;
import soars.application.simulator.executor.animator.Animator;
import soars.application.simulator.main.Constant;
import soars.application.simulator.main.Environment;
import soars.application.simulator.main.MainFrame;
import soars.application.simulator.main.ResourceManager;
import soars.application.simulator.main.log.tab.LogPropertyPage;
import soars.application.simulator.main.log.tab.SystemOutPropertyPage;
import soars.application.simulator.stream.StdErrOutStreamPumper;
import soars.application.simulator.stream.StdOutStreamPumper;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.window.MDIChildFrame;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class LogViewerFrame extends MDIChildFrame {

	/**
	 * 
	 */
	static public final int _minimum_width = 600;

	/**
	 * 
	 */
	static public final int _minimum_height = 450;

	/**
	 * 
	 */
	static public final String _animator_root_directory = "animator";

	/**
	 * 
	 */
	protected Rectangle _window_rectangle = new Rectangle();

	/**
	 * 
	 */
	protected JTabbedPane _tabbedPane = null;

	/**
	 * 
	 */
	protected SystemOutPropertyPage _stdOutPropertyPage = null;

	/**
	 * 
	 */
	protected SystemOutPropertyPage _stdErrOutPropertyPage = null;

	/**
	 * 
	 */
	protected Map _textAreaMap = new HashMap();

	/**
	 * 
	 */
	public boolean _terminated = false;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 */
	public LogViewerFrame(String arg0, boolean arg1, boolean arg2, boolean arg3, boolean arg4) {
		super(arg0, arg1, arg2, arg3, arg4);
	}

	/**
	 * 
	 */
	private void get_property_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._log_viewer_window_rectangle_key + "x",
			"0");
		_window_rectangle.x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._log_viewer_window_rectangle_key + "y",
			"0");
		_window_rectangle.y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._log_viewer_window_rectangle_key + "width",
			String.valueOf( _minimum_width));
		_window_rectangle.width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._log_viewer_window_rectangle_key + "height",
			String.valueOf( _minimum_height));
		_window_rectangle.height = Integer.parseInt( value);
	}

	/**
	 * @return
	 */
	public boolean optimize_window_rectangle() {
		if ( !MainFrame.get_instance().get_client_rectangle().intersects( _window_rectangle)
			|| MainFrame.get_instance().get_client_rectangle().intersection( _window_rectangle).width <= 10
			|| _window_rectangle.y <= -MainFrame.get_instance().getInsets().top
			|| MainFrame.get_instance().get_client_rectangle().intersection( _window_rectangle).height <= MainFrame.get_instance().getInsets().top) {
			_window_rectangle.setBounds( 0, 0, _minimum_width, _minimum_height);
			setLocation( _window_rectangle.x, _window_rectangle.y);
			setSize( _window_rectangle.width, _window_rectangle.height);
			return true;
		}
		return false;
	}

	/**
	 * 
	 */
	public void set_property_to_environment_file() {
		_window_rectangle = getBounds();

		Environment.get_instance().set(
			Environment._log_viewer_window_rectangle_key + "x", String.valueOf( _window_rectangle.x));
		Environment.get_instance().set(
			Environment._log_viewer_window_rectangle_key + "y", String.valueOf( _window_rectangle.y));
		Environment.get_instance().set(
			Environment._log_viewer_window_rectangle_key + "width", String.valueOf( _window_rectangle.width));
		Environment.get_instance().set(
			Environment._log_viewer_window_rectangle_key + "height", String.valueOf( _window_rectangle.height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		get_property_from_environment_file();

		_tabbedPane = new JTabbedPane();
		getContentPane().setLayout( new BorderLayout());
		getContentPane().add( _tabbedPane);

		//setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		setLocation( _window_rectangle.x, _window_rectangle.y);
		setSize( _window_rectangle.width, _window_rectangle.height);
//		setSize( 600, 450);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		setVisible( true);

//		try {
//			setSelected( true);
//		} catch (PropertyVetoException e) {
//			e.printStackTrace();
//		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_closing(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_closing(InternalFrameEvent internalFrameEvent) {
		super.on_internal_frame_closing(internalFrameEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_activated(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_activated(InternalFrameEvent internalFrameEvent) {
		super.on_internal_frame_activated(internalFrameEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_deactivated(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_deactivated(InternalFrameEvent internalFrameEvent) {
		super.on_internal_frame_deactivated(internalFrameEvent);
	}

	/**
	 * @param reader
	 * @param stdOutStreamPumper 
	 * @param stdErrOutStreamPumper 
	 * @return
	 */
	public boolean start(Reader reader, final StdOutStreamPumper stdOutStreamPumper, final StdErrOutStreamPumper stdErrOutStreamPumper) {
		return false;
	}

	/**
	 * @param stdOutStreamPumper
	 * @return
	 */
	protected boolean create_stdOutPropertyPage(StdOutStreamPumper stdOutStreamPumper) {
		_stdOutPropertyPage = new SystemOutPropertyPage(
			_tabbedPane, ResourceManager.get( "log.viewer.stdout.title"), stdOutStreamPumper);
		return _stdOutPropertyPage.create();
	}

	/**
	 * @param stdErrOutStreamPumper
	 * @return
	 */
	protected boolean create_stdErrOutPropertyPage( StdErrOutStreamPumper stdErrOutStreamPumper) {
		_stdErrOutPropertyPage  = new SystemOutPropertyPage(
			_tabbedPane, ResourceManager.get( "log.viewer.stderr.title"), stdErrOutStreamPumper);
		return _stdErrOutPropertyPage.create();
	}

	/**
	 * 
	 */
	public void flush() {
		_stdOutPropertyPage.flush();
		_stdErrOutPropertyPage.flush();
	}

	/**
	 * @return
	 */
	public boolean terminated_normally() {
		return false;
	}

	/**
	 * 
	 */
	public void stop_simulation() {
	}

	/**
	 * @param graphic_properties
	 * @param chart_properties
	 * @param actionEvent
	 * @return
	 */
	public boolean on_run_animator(String graphic_properties, String chart_properties, ActionEvent actionEvent) {
		if ( !dollar_spot_log_exists())
			return false;

		File parent_directory = SoarsCommonTool.make_parent_directory();
		if ( null == parent_directory)
			return false;

		File root_directory = new File( parent_directory, _animator_root_directory);
		if ( !root_directory.mkdir()) {
			FileUtility.delete( parent_directory, true);
			return false;
		}

		if ( !setup_log_files( root_directory)) {
			FileUtility.delete( parent_directory, true);
			return false;
		}

		if ( !setup_graphic_properties_file( graphic_properties, root_directory)) {
			FileUtility.delete( parent_directory, true);
			return false;
		}

		if ( !setup_chart_properties_file( chart_properties, root_directory)) {
			FileUtility.delete( parent_directory, true);
			return false;
		}

		if ( !MainFrame.get_instance().setup_chart_data_files( root_directory)) {
			FileUtility.delete( parent_directory, true);
			return false;
		}

		if ( !MainFrame.get_instance().setup_image_files( root_directory)) {
			FileUtility.delete( parent_directory, true);
			return false;
		}

		if ( !MainFrame.get_instance().setup_thumbnail_image_files( root_directory)) {
			FileUtility.delete( parent_directory, true);
			return false;
		}

		if ( !run_animator( parent_directory, root_directory)) {
			FileUtility.delete( parent_directory, true);
			return false;
		}

		return true;
	}

	/**
	 * @return
	 */
	public boolean dollar_spot_log_exists() {
		for ( int i = 0; i < _tabbedPane.getComponentCount(); ++i) {
			if ( _tabbedPane.getTitleAt( i).equals( "$Spot")) {
				JTextArea textArea = ( JTextArea)_textAreaMap.get( "$Spot");
				if ( null == textArea)
					return false;

				return !textArea.getText().equals( "");
			}
		}
		return false;
	}

	/**
	 * @param root_directory
	 * @return
	 */
	private boolean setup_log_files(File root_directory) {
		File agents_directory = new File( root_directory, "agents");
		agents_directory.mkdir();

		File spots_directory = new File( root_directory, "spots");
		spots_directory.mkdir();

		for ( int i = 0; i < _tabbedPane.getComponentCount(); ++i) {
			Component component = _tabbedPane.getComponentAt( i);
			if ( ignore( component))
				continue;

			String title = _tabbedPane.getTitleAt( i);
			JTextArea textArea = ( JTextArea)_textAreaMap.get( title);
			if ( null == textArea)
				return false;

			if ( !setup_log_file( title.startsWith( "<>") ? title.substring( "<>".length()) : title,
				textArea, title.startsWith( "<>") ? spots_directory : agents_directory))
				return false;
		}

		return true;
	}

	/**
	 * @param name
	 * @param textArea
	 * @param directory
	 * @return
	 */
	private boolean setup_log_file(String name, JTextArea textArea, File directory) {
		File file = new File( directory, name + ".log");
		return FileUtility.write_text_to_file( file, textArea.getText(), "UTF-8");
	}

	/**
	 * @param graphic_properties
	 * @param root_directory
	 * @return
	 */
	private boolean setup_graphic_properties_file(String graphic_properties, File root_directory) {
		File file = new File( root_directory, Constant._graphicPropertiesFilename);
		return FileUtility.write_text_to_file( file, graphic_properties, "UTF-8");
	}

	/**
	 * @param chart_properties
	 * @param root_directory
	 * @return
	 */
	private boolean setup_chart_properties_file(String chart_properties, File root_directory) {
		File file = new File( root_directory, Constant._chartPropertiesFilename);
		return FileUtility.write_text_to_file( file, chart_properties, "UTF-8");
	}

	/**
	 * @param parent_directory
	 * @param root_directory 
	 * @return
	 */
	private boolean run_animator(File parent_directory, File root_directory) {
		String current_directory_name = System.getProperty( Constant._soarsHome);
		File current_directory = new File( current_directory_name);
		if ( null == current_directory)
			return false;

		return Animator.run( current_directory, current_directory_name, parent_directory, root_directory);
	}

	/**
	 * @param root_directory
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(File root_directory, Writer writer) throws SAXException {
		String console = "";
		String stdout = "";
		String stderr = "";
		List agents = new ArrayList();
		List spots = new ArrayList();
		for ( int i = 0; i < _tabbedPane.getComponentCount(); ++i) {
			Component component = _tabbedPane.getComponentAt( i);
			String text = get_console_text( component);
			if ( null != text)
				console = text;
			else {
				if ( component.equals( _stdOutPropertyPage))
					stdout = _stdOutPropertyPage.getText();
				else if ( component.equals( _stdErrOutPropertyPage))
					stderr = _stdErrOutPropertyPage.getText();
				else {
					String title = _tabbedPane.getTitleAt( i);
					if ( title.endsWith( " - " + ResourceManager.get( "log.viewer.state.logging")))
						title = title.substring( 0, title.length() - ( " - " + ResourceManager.get( "log.viewer.state.logging")).length());
						
					JTextArea textArea = ( JTextArea)_textAreaMap.get( title);
					if ( null == textArea)
						return false;

					get_log( title.startsWith( "<>") ? title.substring( "<>".length()) : title,
						textArea, title.startsWith( "<>") ? spots : agents);
				}
			}
		}

		Rectangle rectangle = getBounds();

		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "title", "", Writer.escapeAttributeCharData( getTitle()));
		attributesImpl.addAttribute( null, null, "x", "", String.valueOf( rectangle.x));
		attributesImpl.addAttribute( null, null, "y", "", String.valueOf( rectangle.y));
		attributesImpl.addAttribute( null, null, "width", "", String.valueOf( rectangle.width));
		attributesImpl.addAttribute( null, null, "height", "", String.valueOf( rectangle.height));
		writer.startElement( null, null, "log_viewer_data", attributesImpl);

		if ( !write( console, root_directory, Constant._console_filename))
			return false;

		if ( !write( stdout, root_directory, Constant._standard_out_filename))
			return false;

		if ( !write( stderr, root_directory, Constant._standard_error_filename))
			return false;

		File agents_directory = new File( root_directory, "agents");
		if ( !agents_directory.exists() && !agents_directory.mkdir())
			return false;

		File spots_directory = new File( root_directory, "spots");
		if ( !spots_directory.exists() && !spots_directory.mkdir())
			return false;

		if ( !write( "agents", agents, agents_directory, writer))
			return false;

		if ( !write( "spots", spots, spots_directory, writer))
			return false;

		writer.endElement( null, null, "log_viewer_data");

		return true;
	}


	/**
	 * @param component
	 * @return
	 */
	protected String get_console_text(Component component) {
		return null;
	}

	/**
	 * @param name
	 * @param textArea
	 * @param list
	 */
	private void get_log(String name, JTextArea textArea, List list) {
		list.add( new LogData( name, textArea.getText()));
	}

	/**
	 * @param value
	 * @param root_directory
	 * @param filename
	 * @return
	 */
	private boolean write(String value, File root_directory, String filename) {
		File file = new File( root_directory, filename);
		return FileUtility.write_text_to_file( file, value, "UTF-8");
	}

	/**
	 * @param name
	 * @param list
	 * @param directory
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write(String name, List list, File directory, Writer writer) throws SAXException {
		if ( list.isEmpty())
			return true;

		writer.startElement( null, null, name, new AttributesImpl());

		for ( int i = 0; i < list.size(); ++i) {
			LogData logData = ( LogData)list.get( i);
			if ( !write( i, logData, directory, writer))
				return false;
		}

		writer.endElement( null, null, name);

		return true;
	}

	/**
	 * @param index
	 * @param logData
	 * @param directory
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write(int index, LogData logData, File directory, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", logData._name);
		writer.writeElement( null, null, "log" + String.valueOf( index), attributesImpl);

		File file = new File( directory, logData._name + ".log");
		return FileUtility.write_text_to_file( file, logData._value, "UTF-8");
	}

	/**
	 * @param log_viewer_window_title
	 * @param window_rectangle
	 * @param console
	 * @param agents
	 * @param spots
	 * @param stdout
	 * @param stderr
	 * @return
	 */
	public boolean update(String log_viewer_window_title, Rectangle window_rectangle, String console, List agents, List spots, String stdout, String stderr) {
		setTitle( log_viewer_window_title);

		while ( clear())
			;

		_textAreaMap.clear();

		append( agents, "");
		append( spots, "<>");

		if ( null != _stdOutPropertyPage)
			_stdOutPropertyPage.setText( stdout);

		if ( null != _stdErrOutPropertyPage)
			_stdErrOutPropertyPage.setText( stderr);

		_window_rectangle = window_rectangle;
		setLocation( _window_rectangle.x, _window_rectangle.y);
		setSize( _window_rectangle.width, _window_rectangle.height);

		return true;
	}

	/**
	 * @return
	 */
	private boolean clear() {
		for ( int i = 0; i < _tabbedPane.getComponentCount(); ++i) {
			Component component = _tabbedPane.getComponentAt( i);
			if ( ignore( component))
				continue;

			_tabbedPane.remove( i);
			return true;
		}

		return false;
	}

	/**
	 * @param list
	 * @param prefix
	 */
	private void append(List list, String prefix) {
		for ( int i = 0; i < list.size(); ++i) {
			LogPropertyPage logPropertyPage = new LogPropertyPage( _tabbedPane);
			if ( !logPropertyPage.create( prefix, ( LogData)list.get( i), _textAreaMap))
				return;
		}
	}

	/**
	 * @param component
	 * @return
	 */
	protected boolean ignore(Component component) {
		return false;
	}
}
