/**
 * 
 */
package soars.application.manager.sample.executor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import soars.application.manager.sample.main.Constant;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.stream.StreamPumper;

/**
 * @author kurata
 *
 */
public class VisualShell {

	/**
	 * @param file
	 * @return
	 */
	public static boolean start(File file) {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String soars_properties = System.getProperty( Constant._soarsProperties);

		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS VisualShell");
			//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS VisualShell");
			list.add( "-X" + Constant._macIconFilename + "=../resource/icon/application/visualshell/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		//list.add( "-Djava.endorsed.dirs=" + home_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + home_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		list.add( "-D" + Constant._soarsProperties + "=" + soars_properties);
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-jar");
		list.add( home_directory_name + "/" + Constant._visualShellJarFilename);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));
		list.add( "-vsl");
		list.add( file.getAbsolutePath());

		String[] cmdarray = list.toArray( new String[ 0]);

		debug( "VisualShell", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec(
				cmdarray,
				null,
				new File( home_directory_name));
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param type
	 * @param osname
	 * @param osversion
	 * @param cmdarray
	 */
	private static void debug(String type, String osname, String osversion, String[] cmdarray) {
		String text = ""; 
		text += ( "Type : " + type + Constant._lineSeparator);
		text += ( "OS : " + osname + " [" + osversion + "]" + Constant._lineSeparator);
		for ( int i = 0; i < cmdarray.length; ++i)
			text += ( "Parameter : " + cmdarray[ i] + Constant._lineSeparator);

		Clipboard.set( text);
	}
}
