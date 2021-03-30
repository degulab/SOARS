/*
 * Created on 2006/02/28
 */
package soars.application.launcher.main;

import java.awt.Image;
import java.io.File;
import java.util.Locale;

import javax.swing.JFrame;

import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.window.SplashWindow;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata / SOARS project
 */
public class Application {

	/**
	 * 
	 */
	public Application() {
		super();
	}

	/**
	 * @return
	 */
	public boolean init_instance() {
		if ( Constant._enableSplashWindow
			&& !SplashWindow.execute( Constant._resource_directory + "/image/splash/splash.gif", true))
			return false;

		if ( !SoarsCommonTool.setup_look_and_feel()) {
			SplashWindow.terminate();
			return false;
		}

		MainFrame mainFrame = MainFrame.get_instance();
		if ( !mainFrame.create()) {
			SplashWindow.terminate();
			return false;
		}

		SplashWindow.terminate();

		Image image = Resource.load_image_from_resource( Constant._resource_directory + "/image/icon/icon.png", getClass());
		if ( null != image)
			mainFrame.setIconImage( image);

		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		if ( null == home_directory_name || home_directory_name.equals( "")) {
			File current_directory = new File( "");
			if ( null == current_directory)
				System.exit( 1);

			File home_directory = new File( current_directory.getAbsolutePath() + Constant._home_directory);
			System.setProperty( Constant._soarsHome, home_directory.getAbsolutePath());
		}

		String property_directory_name = System.getProperty( Constant._soarsProperty);
		if ( null == property_directory_name || property_directory_name.equals( "")) {
			File property_directory = SoarsCommonTool.get_default_property_directory();
			if ( null == property_directory)
				System.exit( 1);

			System.setProperty( Constant._soarsProperty, property_directory.getAbsolutePath());
		}

		String properties_file_name = System.getProperty( Constant._soarsProperties);
		if ( null == properties_file_name || properties_file_name.equals( "")) {
			File properties_file = SoarsCommonEnvironment.get_instance().get();
			if ( null == properties_file)
				System.exit( 1);

			System.setProperty( Constant._soarsProperties, properties_file.getAbsolutePath());
		}

		boolean locale = false;
		for ( int i = 0; i < args.length; ++i) {
			if ( args[ i].equals( "-language") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				JFrame.setDefaultLookAndFeelDecorated( false);
				Locale.setDefault( new Locale( args[ i + 1]));
				CommonEnvironment.get_instance().set( CommonEnvironment._localeKey, args[ i + 1]);
				locale = true;
			}
		}

		if ( !locale) {
			JFrame.setDefaultLookAndFeelDecorated( false);
			Locale.setDefault(
				new Locale( CommonEnvironment.get_instance().get(
					CommonEnvironment._localeKey, Locale.getDefault().getLanguage())));
		}

		Application application = new Application();
		if ( !application.init_instance())
			System.exit( 1);
	}
}
