/**
 * 
 */
package soars.application.simulator.executor.animator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import soars.application.simulator.main.Application;
import soars.application.simulator.main.Constant;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.stream.StreamPumper;

/**
 * @author kurata
 *
 */
public class Animator {

	/**
	 * 
	 */
	public Animator() {
		super();
	}

	/**
	 * @param current_directory
	 * @param current_directory_name
	 * @param parent_directory
	 * @param root_directory
	 * @return
	 */
	public static boolean run(File current_directory, String current_directory_name, File parent_directory, File root_directory) {
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");
		String[] cmdarray = get_cmdarray( current_directory_name, parent_directory, root_directory, property_directory_name, memory_size, osname);

		debug( "Animator.run", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec( cmdarray, null, current_directory);
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param current_directory_name
	 * @param parent_directory
	 * @param root_directory
	 * @param property_directory_name
	 * @param memory_size
	 * @param osname
	 * @return
	 */
	private static String[] get_cmdarray(String current_directory_name, File parent_directory, File root_directory, String property_directory_name, String memory_size, String osname) {
		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Animator");
			//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS Animator");
			list.add( "-X" + Constant._macIconFilename + "=../resource/icon/application/animator/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		//list.add( "-Djava.endorsed.dirs=" + current_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + current_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-jar");
		list.add( current_directory_name + "/" + Constant._animatorJarFilename);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));
		list.add( "-parent_directory");
		list.add( parent_directory.getAbsolutePath());
		list.add( "-root_directory");
		list.add( root_directory.getAbsolutePath());
		if ( Application._demo)
			list.add( "-demo");

		return list.toArray( new String[ 0]);
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
