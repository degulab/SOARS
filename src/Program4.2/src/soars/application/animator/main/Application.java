/*
 * Created on 2005/02/14
 */
package soars.application.animator.main;

import java.awt.Image;
import java.io.File;
import java.util.Locale;

import javax.swing.JFrame;

import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.window.SplashWindow;
import soars.common.utility.tool.resource.Resource;

/**
 * The Animator main class.
 * @author kurata / SOARS project
 */
public class Application {

	/**
	 * Title string of the main window.
	 */
	static public String _title = "";

	/**
	 * True for the demo mode.
	 */
	static public boolean _demo = false;

	/**
	 * Returns the appropriate title string.
	 * @return
	 */
	public static String get_title() {
		return ( _title.equals( "") ? "" : ( " - " + _title));
	}

	/**
	 * Returns true if this application is initialized successfully.
	 * @param parent_directory the name of the specified directory which contains log files
	 * @param root_directory the name of the specified directory which contains log files
	 * @param anm_filename the name of the specified Animator data file
	 * @param agd_filename the name of the specified Animator graphics data file 
	 * @return true if this application is initialized successfully
	 */
	public boolean init_instance(String parent_directory, String root_directory, String anm_filename, String agd_filename) {
		if ( Constant._enableSplashWindow
			&& !SplashWindow.execute( Constant._resourceDirectory + "/image/splash/splash.gif", true))
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

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			mainFrame.setIconImage( image);

		if ( null != parent_directory && null != root_directory) {
			if ( !mainFrame.load( parent_directory, root_directory))
				return true;

			if ( !import_graphic_data( agd_filename))
				return false;

		} else if ( null != anm_filename) {
			File file = new File( anm_filename);
			if ( !file.exists() || !file.isFile() || !file.canRead())
				return false;

			if ( !mainFrame.open( file))
				return true;

			if ( !import_graphic_data( agd_filename))
				return false;
		}

		return true;
	}

	/**
	 * @param agd_filename
	 * @return
	 */
	private boolean import_graphic_data(String agd_filename) {
		if ( null == agd_filename)
			return true;

		File file = new File( agd_filename);
		if ( !file.exists() || !file.isFile() || !file.canRead())
			return false;

		AnimatorView.get_instance().on_file_import_graphic_data( file);

		return true;
	}

	/**
	 * The entry point of this application.
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		if ( null == home_directory_name || home_directory_name.equals( "")) {
			File home_directory = new File( "");
			if ( null == home_directory)
				System.exit( 1);

			System.setProperty( Constant._soarsHome, home_directory.getAbsolutePath());
		}

		String property_directory_name = System.getProperty( Constant._soarsProperty);
		if ( null == property_directory_name || property_directory_name.equals( "")) {
			File property_directory = SoarsCommonTool.get_default_property_directory();
			if ( null == property_directory)
				System.exit( 1);

			System.setProperty( Constant._soarsProperty, property_directory.getAbsolutePath());
		}

		boolean locale = false;
		String parent_directory = null;
		String root_directory = null;
		String anm_filename = null;
		String agd_filename = null;
		for ( int i = 0; i < args.length; ++i) {
			if ( args[ i].equals( "-language") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				JFrame.setDefaultLookAndFeelDecorated( false);
				//JFrame.setDefaultLookAndFeelDecorated( true);
				Locale.setDefault( new Locale( args[ i + 1]));
				CommonEnvironment.get_instance().set( CommonEnvironment._localeKey, args[ i + 1]);
				locale = true;
				//break;
			} else if ( args[ i].equals( "-parent_directory") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				parent_directory = args[ i + 1];
			} else if ( args[ i].equals( "-root_directory") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				root_directory = args[ i + 1];
			} else if ( args[ i].equals( "-anm") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				anm_filename = args[ i + 1];
			} else if ( args[ i].equals( "-agd") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				agd_filename = args[ i + 1];
			} else if ( args[ i].equals( "-title") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				_title = args[ i + 1];
			} else if ( args[ i].equals( "-soars_version") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				Constant._soarsVersion = args[ i + 1];
			} else if ( args[ i].equals( "-copyright") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				Constant._copyright = args[ i + 1];
			} else if ( args[ i].equals( "-demo")) {
				_demo = true;
			}
		}

		if ( !locale) {
			JFrame.setDefaultLookAndFeelDecorated( false);
			//JFrame.setDefaultLookAndFeelDecorated( true);
			Locale.setDefault(
				new Locale( CommonEnvironment.get_instance().get(
					CommonEnvironment._localeKey, Locale.getDefault().getLanguage())));
		}

		Application application = new Application();
		if ( !application.init_instance( parent_directory, root_directory, anm_filename, agd_filename))
			System.exit( 1);
	}
}
