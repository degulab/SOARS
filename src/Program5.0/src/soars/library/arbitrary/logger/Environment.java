/*
 * 2005/01/31
 */
package soars.library.arbitrary.logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import soars.common.soars.constant.CommonConstant;
import soars.common.utility.tool.environment.EnvironmentBase;

/**
 * The local properties maintenance class.
 * @author kurata / SOARS project
 */
public class Environment extends EnvironmentBase {

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
	static public final String _loggerDialogRectangleKey = "Logger.dialog.rectangle.";

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
	 * Returns the instance of this class.
	 * @return the instance of this class
	 */
	public static Environment get_instance() {
		if ( null == _environment) {
			System.exit( 0);
		}

		return _environment;
	}

	/**
	 * Creates the local properties maintenance class.
	 */
	public Environment() {
		super(
			System.getProperty( CommonConstant._soarsProperty) + File.separator
				+ "program" + File.separator
				+ "engine" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS Engine properties");
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.tool.environment.EnvironmentBase#initialize()
	 */
	public boolean initialize() throws FileNotFoundException, IOException {
		if ( !super.initialize())
			return false;

		return true;
	}
}
