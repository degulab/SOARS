/*
 * 2005/01/31
 */
package soars.application.manager.sample.main;

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
	static public final String _main_panel_divider_location1_key = "Main.panel.divider.location1";

	/**
	 * 
	 */
	static public final String _main_panel_divider_location2_key = "Main.panel.divider.location2";

	/**
	 * 
	 */
	static public final String _export_files_directory_key = "Directory.export.files";

	/**
	 * 
	 */
	static public final String _projectFolderDirectoryKey = "Project.folder.directory";

	/**
	 * 
	 */
	static public final String _projectFolderKey = "Project.folder";

	/**
	 * 
	 */
	static public final String _defaultProjectFolderKey = "Default.project.folder";

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
				+ "manager" + File.separator
				+ "sample" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS Sample Manager properties");
	}
}
