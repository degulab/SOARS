/**
 * 
 */
package soars.tool.mac.launcher.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * @return
	 */
	public boolean init_instance() {
		File current_directory = new File( "");
		if ( null == current_directory) {
			JOptionPane.showMessageDialog( null,
				ResourceManager.get( "soars.error.messsage"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		File home_directory = new File( current_directory.getAbsolutePath() + Constant._home_directory);
		System.setProperty( Constant._soarsHome, home_directory.getAbsolutePath());

		File property_directory = SoarsCommonTool.get_default_property_directory();
		if ( null == property_directory) {
			JOptionPane.showMessageDialog( null,
				ResourceManager.get( "soars.error.messsage"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( !write_test( property_directory)) {
			JOptionPane.showMessageDialog( null,
				ResourceManager.get( "soars.error.messsage"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		System.setProperty( Constant._soarsProperty, property_directory.getAbsolutePath());

		File soars_properties = SoarsCommonEnvironment.get_instance().get();
		if ( null == soars_properties) {
			JOptionPane.showMessageDialog( null,
				ResourceManager.get( "soars.error.messsage"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		System.setProperty( Constant._soarsProperties, soars_properties.getAbsolutePath());

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec( get_cmdarray( current_directory));
		} catch (IOException e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog( null,
				ResourceManager.get( "soars.error.messsage"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
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
	 * @param current_directory
	 * @return
	 */
	private static String[] get_cmdarray(File current_directory) {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String soars_properties = System.getProperty( Constant._soarsProperties);

		String memory_size = CommonEnvironment.get_instance().get_memory_size();

		List< String>list = new ArrayList< String>();
		list.add( Constant._macJava);
		list.add( "-Dfile.encoding=UTF-8");
		list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( "file.encoding", ""));
		list.add( "-X" + Constant._macScreenMenuName + "=SOARS");
		//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS");
		list.add( "-X" + Constant._macIconFilename + "=resource/icon/application/launcher/icon.png");
		list.add( "-D" + Constant._macScreenMenu + "=true");

		list.add( "-D" + Constant._soarsHome + "=" + home_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		list.add( "-D" + Constant._soarsProperties + "=" + soars_properties);
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-jar");
		list.add( current_directory.getAbsolutePath() + "/" + Constant._soarsLauncherJarFilename);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));

		return list.toArray( new String[ 0]);
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
