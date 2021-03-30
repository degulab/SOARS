/*
 * Created on 2006/04/07
 */
package soars.tool.simulator.launcher.main;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.SoarsCommonEnvironment;

/**
 * @author kurata
 */
public class Constant extends CommonConstant {

	/**
	 * 
	 */
	static public final String _application_version = "20110603";

	/**
	 * 
	 */
	static public final String _homeDirectory = "/lib";

	/**
	 * 
	 */
	static public final String _propertyDirectory = "/config";

	/**
	 * 
	 */
	static public final String _resourceDirectory = "/soars/tool/simulator/launcher/resource";

	/**
	 * 
	 */
	static public final String _program = "launcher.bin";

	/**
	 * 
	 */
	static public final String[][] _languages = {
		{ "en", "english"},
		{ "ja", "japanese"}
	};

	/**
	 * @return
	 */
	public static String get_version_message() {
		return ( SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._versionKey, "SOARS") + _lineSeparator
			+ "- Build " + _application_version + _lineSeparator + _lineSeparator
			+ SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._copyrightKey, "Copyright (c) 2003-???? SOARS Project."));
	}
}
