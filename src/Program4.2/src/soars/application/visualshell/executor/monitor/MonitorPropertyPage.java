/*
 * Created on 2006/02/27
 */
package soars.application.visualshell.executor.monitor;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.tab.TabbedPage;
import soars.common.utility.tool.clipboard.Clipboard;

/**
 * The monitor component to display the log output of the ModelBuilder.
 * @author kurata / SOARS project
 */
public class MonitorPropertyPage extends TabbedPage {

	/**
	 * 
	 */
	protected String _title = "";

	/**
	 * 
	 */
	private String _log_folder_path = "";

	/**
	 * 
	 */
	private JTextArea _textArea = null;

	/**
	 * 
	 */
	private JScrollPane _scrollPane = null;

	/**
	 * Thread of the observer for the ModelBuilder.
	 */
	public Thread _thread = null;

	/**
	 * Process of the ModelBuilder.
	 */
	public Process _process = null;

	/**
	 * Synchronized object.
	 */
	public Object _lock_process = new Object();

	/**
	 * Creates the instance of MonitorPropertyPage with the specified data.
	 * @param title the title of this component
	 * @param log_folder_path the directory of the log files
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public MonitorPropertyPage(String title, String log_folder_path, Frame owner, Component parent) {
		super();
		_title = title;
		_log_folder_path = log_folder_path;
	}

	/**
	 * Appends the specified text to this component.
	 * @param text the specified text
	 */
	public void append(String text) {
		synchronized( MonitorFrame._lock) {
			_textArea.append( text);
			MonitorFrame.get_instance().select( this);
		}
	}
	/**
	 * Returns true if the ModelBuilder is running.
	 * @return true if the ModelBuilder is running
	 */
	public boolean is_alive() {
		if ( null == _thread)
			return true;

		return _thread.isAlive();
	}

	/**
	 * Terminates the ModelBuilder.
	 */
	public void terminate() {
		synchronized( _lock_process) {
			if ( null == _process)
				return;

			_process.destroy();
			_process = null;
		}
	}

	/**
	 * Copies the text on this component to the clipboard.
	 */
	public void copy_to_clipboard() {
		Clipboard.set( _textArea.getText());
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.tab.TabbedPage#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));

		setup_textArea();

		return true;
	}

	/**
	 * 
	 */
	private void setup_textArea() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		//panel.add( Box.createHorizontalStrut( 5));

		_textArea = new JTextArea();
		_textArea.setEditable( false);

		_scrollPane = new JScrollPane();
		_scrollPane.getViewport().setView( _textArea);
		//_scrollPane.setPreferredSize( new Dimension( 640, 480));

		//panel.add( Box.createHorizontalStrut( 5));

		panel.add( _scrollPane);

		add( panel);

		if ( !_log_folder_path.equals( "")
			&& Environment.get_instance().get( Environment._editExportSettingDialogToFileKey, "false").equals( "true"))
			_textArea.append( ResourceManager.get_instance().get( "run.monitor.tab.log.folder.path.header")
				+ _log_folder_path + Constant._lineSeparator + Constant._lineSeparator);
	}
}
