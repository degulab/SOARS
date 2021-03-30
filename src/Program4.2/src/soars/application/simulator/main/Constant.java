/**
 * 
 */
package soars.application.simulator.main;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.SoarsCommonEnvironment;

/**
 * @author kurata
 */
public class Constant extends CommonConstant {

	/**
	 * 
	 */
	static public final String _application_version = "20110627";

	/**
	 * 
	 */
	static public final String _resource_directory = "/soars/application/simulator/resource";

	/**
	 * 
	 */
	static public final String _console_filename = "console.log";

	/**
	 * 
	 */
	static public final String _standard_out_filename = "stdout.log";

	/**
	 * 
	 */
	static public final String _standard_error_filename = "stderr.log";

	/**
	 * @return
	 */
	public static String get_version_message() {
		return ( SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._versionKey, "SOARS") + _lineSeparator
			+ "- Simulator (" + _application_version  + ")" + _lineSeparator + _lineSeparator
			+ SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._copyrightKey, "Copyright (c) 2003-???? SOARS Project."));
	}
}
