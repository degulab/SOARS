/**
 * 
 */
package soars.application.manager.sample.main;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.SoarsCommonEnvironment;

/**
 * @author kurata
 *
 */
public class Constant extends CommonConstant {

	/**
	 * Application version string.
	 */
	static public final String _applicationVersion = "20110708";

	/**
	 * Resource directory string.
	 */
	static public final String _resourceDirectory = "/soars/application/manager/sample/resource";

	/**
	 * Name of the root directory for Visual Shell data.
	 */
	static public final String _rootDirectoryName = "visualshell";

	/**
	 * Name of the data file.
	 */
	static public final String _dataFilename = "data.vml";

	/**
	 * Returns the version message string.
	 * @return the version message string
	 */
	public static String get_version_message() {
		return ( SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._versionKey, "SOARS") + _lineSeparator
			+ "- Model Manager (" + _applicationVersion  + ")" + _lineSeparator + _lineSeparator
			+ SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._copyrightKey, "Copyright (c) 2003-???? SOARS Project."));
	}
}
