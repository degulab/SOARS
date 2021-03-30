/**
 * 
 */
package soars.tool.simulator.starter.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * @return
	 */
	public boolean init_instance() {
		File currentDirectory = new File( "");
		if ( null == currentDirectory) {
			JOptionPane.showMessageDialog( null,
				ResourceManager.get( "soars.error.messsage"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		File homeDirectory = new File( currentDirectory.getAbsolutePath() + Constant._homeDirectory);
		if ( !homeDirectory.exists()) {
			JOptionPane.showMessageDialog( null,
				ResourceManager.get( "soars.error.messsage"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		System.setProperty( CommonConstant._soarsHome, homeDirectory.getAbsolutePath());


		File propertyDirectory = new File( currentDirectory.getAbsolutePath() + Constant._propertyDirectory);
		if ( !propertyDirectory.exists()) {
			JOptionPane.showMessageDialog( null,
				ResourceManager.get( "soars.error.messsage"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( !write_test( propertyDirectory)) {
			JOptionPane.showMessageDialog( null,
				ResourceManager.get( "soars.error.messsage"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		System.setProperty( CommonConstant._soarsProperty, propertyDirectory.getAbsolutePath());


		File propertiesFile = SoarsCommonEnvironment.get_instance().get();
		if ( !propertiesFile.exists()) {
			JOptionPane.showMessageDialog( null,
				ResourceManager.get( "soars.error.messsage"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		System.setProperty( CommonConstant._soarsProperties, propertiesFile.getAbsolutePath());


		String osname = System.getProperty( "os.name");


		String[] cmdarray = get_cmdarray( currentDirectory, osname);

		debug( "SOARS Simulator Launcher", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec(
				cmdarray,
				null,
				currentDirectory.getAbsoluteFile());
		} catch (IOException e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog( null,
				ResourceManager.get( "soars.error.messsage"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
		}

		return true;
	}

	/**
	 * @param property_directory
	 * @return
	 */
	private boolean write_test(File property_directory) {
		try {
			File file = File.createTempFile( "soars", "test", property_directory);
			if ( null == file)
				return false;

			file.delete();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param currentDirectory
	 * @param osname
	 * @return
	 */
	private String[] get_cmdarray(File currentDirectory, String osname) {
		String homeDirectoryName = System.getProperty( CommonConstant._soarsHome);
		String propertyDirectoryName = System.getProperty( CommonConstant._soarsProperty);
		String propertiesFilename = System.getProperty( CommonConstant._soarsProperties);

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( CommonConstant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( CommonConstant._macJava);
			//if ( System.getProperty( CommonConstant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + CommonConstant._systemDefaultFileEncoding + "=" + System.getProperty( CommonConstant._systemDefaultFileEncoding, ""));
			list.add( "-X" + CommonConstant._macScreenMenuName + "=" + ResourceManager.get( "application.title"));
			//list.add( "-D" + CommonConstant._mac_screen_menu_name + "=SOARS Simulator");
			list.add( "-X" + CommonConstant._macIconFilename + "=resource/icon/application/simulator/icon.png");
			list.add( "-D" + CommonConstant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		list.add( "-D" + CommonConstant._soarsHome + "=" + homeDirectoryName);
		list.add( "-D" + CommonConstant._soarsProperty + "=" + propertyDirectoryName);
		list.add( "-D" + CommonConstant._soarsProperties + "=" + propertiesFilename);
		list.add( "-jar");
		list.add( currentDirectory.getAbsolutePath() + "/" + Constant._program);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));

		return list.toArray( new String[ 0]);
	}

	/**
	 * @param string
	 * @param osname
	 * @param property
	 * @param cmdarray
	 */
	private static void debug(String type, String osname, String osversion, String[] cmdarray) {
		String text = ""; 
		text += ( "Type : " + type + CommonConstant._lineSeparator);
		text += ( "OS : " + osname + " [" + osversion + "]" + CommonConstant._lineSeparator);
		for ( int i = 0; i < cmdarray.length; ++i)
			text += ( "Parameter : " + cmdarray[ i] + CommonConstant._lineSeparator);

		Clipboard.set( text);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application application = new Application();
		if ( !application.init_instance())
			System.exit( 1);
	}
}
