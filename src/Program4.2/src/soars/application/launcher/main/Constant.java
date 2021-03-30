/*
 * Created on 2006/04/07
 */
package soars.application.launcher.main;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.SoarsCommonEnvironment;

/**
 * @author kurata
 */
public class Constant extends CommonConstant {

	/**
	 * 
	 */
	static public final String _application_version = "20120928";

	/**
	 * 
	 */
	static public final String _resource_directory = "/soars/application/launcher/resource";

	/**
	 * 
	 */
	static public final String[][] _languages = {
		{ "en", "english"},
		{ "ja", "japanese"}
//		{ "zh", "chinese"},
//		{ "en", "english"},
//		{ "in", "indonesian"},
//		{ "ja", "japanese"},
//		{ "kr", "korean"},
//		{ "es", "spanish"}
	};

	/**
	 * 
	 */
	static public final String _home_directory = "/program";

	/**
	 * 
	 */
	static public final String _download_directory = "/soars/program/launcher/download";

	/**
	 * @return
	 */
	public static String get_version_message() {
		return ( SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._versionKey, "SOARS") + _lineSeparator
			+ "- Build " + _application_version + _lineSeparator + _lineSeparator
			+ SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._copyrightKey, "Copyright (c) 2003-???? SOARS Project."));
	}
}
