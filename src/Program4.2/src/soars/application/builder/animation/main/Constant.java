/**
 * 
 */
package soars.application.builder.animation.main;

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
	static public final String _application_version = "20110708";

	/**
	 * Resource directory string.
	 */
	static public final String _resource_directory = "/soars/application/builder/animation/resource";

	/**
	 * Library directory string.
	 */
	static public final String _library_directory = "/../library/builder/animation";

	/**
	 * JASON directory string.
	 */
	static public final String _json_library_directory = "/../library/json";

	/**
	 * JASON jar file name string.
	 */
	static public final String _json_jar_filename = "json.jar";

	/**
	 * Chart directory string.
	 */
	static public final String _chart_library_directory = "/../function/chart/module";

	/**
	 * Chart jar file name string.
	 */
	static public final String _chart_jar_filename = "chart.jar";

	/**
	 * Plot directory string.
	 */
	static public final String _plot_library_directory = "/../library/chart";

	/**
	 * Plot jar file name string.
	 */
	static public final String _plot_jar_filename = "plot.jar";

	/**
	 * Icon file name string.
	 */
	static public final String _icon_filename = "icon.png";

	/**
	 * Returns the version message string.
	 * @return the version message string
	 */
	public static String get_version_message() {
		return ( SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._versionKey, "SOARS") + _lineSeparator
			+ "- Animation Builder (" + _application_version  + ")" + _lineSeparator + _lineSeparator
			+ SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._copyrightKey, "Copyright (c) 2003-???? SOARS Project."));
	}
}
