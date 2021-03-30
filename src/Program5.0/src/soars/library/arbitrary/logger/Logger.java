/**
 * 
 */
package soars.library.arbitrary.logger;

/**
 * @author kurata
 *
 */
public class Logger {

	/**
	 * 
	 */
	static private LoggerDlg _loggerDlg = null;

	/**
	 * @param logFolderPath
	 */
	public static void start(String logFolderPath) {
		if ( null != _loggerDlg)
			return;

		_loggerDlg = new LoggerDlg( null, ResourceManager.get_instance().get( "logger.dialog.title"), false);
		_loggerDlg.create( logFolderPath);
	}

	/**
	 * 
	 */
	public static void execute() {
		if ( null == _loggerDlg)
			return;

		_loggerDlg.log();
	}

	/**
	 * 
	 */
	public static void terminate() {
		if ( null == _loggerDlg)
			return;

		_loggerDlg.terminate();
		_loggerDlg.dispose();
	}

	/**
	 * 
	 */
	public Logger() {
	}
}
