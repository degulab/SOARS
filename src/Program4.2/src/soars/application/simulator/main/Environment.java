/*
 * 2005/01/31
 */
package soars.application.simulator.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import soars.common.utility.tool.environment.EnvironmentBase;

/**
 * @author kurata
 */
public class Environment extends EnvironmentBase {

	/**
	 * 
	 */
	static public final String _main_window_rectangle_key = "MainWindow.window.rectangle.";

	/**
	 * 
	 */
	static public final String _log_viewer_window_rectangle_key = "LogViewerWindow.window.rectangle.";

	/**
	 * 
	 */
	static public final String _open_directory_key = "Directory.open";

	/**
	 * 
	 */
	static public final String _save_as_directory_key = "Directory.save.as";

	/**
	 * 
	 */
	static public final String _export_files_directory_key = "Directory.export.files";

	/**
	 * 
	 */
	static public final String _file_editor_window_rectangle_key = "File.editor.rectangle.";

	/**
	 * Key mapped to the position and size of the dialog box to show the warning messages.
	 */
	static public final String _warning_dialog1_rectangle_key = "Warning.dialog1.rectangle.";

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private Environment _environment = null;

	/**
	 * 
	 */
	static {
		try {
			startup();
		} catch (FileNotFoundException e) {
			throw new RuntimeException( e);
		} catch (IOException e) {
			throw new RuntimeException( e);
		}
	}

	/**
	 * 	
	 */
	private static void startup() throws FileNotFoundException, IOException {
		synchronized( _lock) {
			if ( null == _environment) {
				_environment = new Environment();
				if ( !_environment.initialize())
					System.exit( 0);
			}
		}
	}

	/**
	 * @return
	 */
	public static Environment get_instance() {
		if ( null == _environment) {
			System.exit( 0);
		}

		return _environment;
	}

	/**
	 * 
	 */
	public Environment() {
		super(
			System.getProperty( Constant._soarsProperty) + File.separator
				+ "program" + File.separator
				+ "simulator" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS Simulator properties");
	}
}
