/**
 * 
 */
package soars.application.simulator.main;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;

import javax.swing.JFrame;

import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.window.SplashWindow;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private Application _application = null;

	/**
	 * 
	 */
	static public boolean _demo = false;

	/**
	 * @return
	 */
	public static Application get_instance() {
		synchronized( _lock) {
			if ( null == _application) {
				_application = new Application();
			}
		}
		return _application;
	}

	/**
	 * 
	 */
	public Application() {
		super();
	}

	/**
	 * @return
	 */
	private boolean init_instance() {
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
	 * @param reader
	 * @param parent_directory
	 * @param chart_properties_file_path
	 * @param experiment
	 * @param log_folder_path
	 * @return
	 */
	public boolean init_instance(Reader reader, File parent_directory, String vml, String experiment, String log_folder_path) {
		if ( Constant._enableSplashWindow
			&& !SplashWindow.execute( Constant._resource_directory + "/image/splash/splash.gif", true))
			return false;

		if ( !SoarsCommonTool.setup_look_and_feel()) {
			SplashWindow.terminate();
			return false;
		}

		MainFrame mainFrame = MainFrame.get_instance();
		if ( !mainFrame.create( parent_directory, log_folder_path)) {
			SplashWindow.terminate();
			return false;
		}

		SplashWindow.terminate();

		Image image = Resource.load_image_from_resource( Constant._resource_directory + "/image/icon/icon.png", getClass());
		if ( null != image)
			mainFrame.setIconImage( image);

		return mainFrame.start( reader, experiment, vml);
	}

	/**
	 * 
	 */
	public void exit_instance() {
		System.exit( 0);
	}

	/**
	 * @param args
	 * @param inputStream
	 */
	public static boolean start(String[] args, InputStream inputStream) {
		if ( null == args)
			return false;

		if ( null == inputStream)
			return false;

		String home_directory_name = System.getProperty( Constant._soarsHome);
		if ( null == home_directory_name || home_directory_name.equals( "")) {
			File home_directory = new File( "");
			if ( null == home_directory)
				return false;

			System.setProperty( Constant._soarsHome, home_directory.getAbsolutePath());
		}

		String property_directory_name = System.getProperty( Constant._soarsProperty);
		if ( null == property_directory_name || property_directory_name.equals( "")) {
			File property_directory = SoarsCommonTool.get_system_property_directory();
			if ( null == property_directory)
				return false;

			System.setProperty( Constant._soarsProperty, property_directory.getAbsolutePath());
		}

		boolean locale = false;
		String parent_directory = null;
		String zip = null;
		String vml = "";
		String experiment = "";
		String log_folder_path = "";
		boolean remove = false;
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
			} else if ( args[ i].equals( "-zip") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				zip = args[ i + 1];
			} else if ( args[ i].equals( "-vml") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				vml = args[ i + 1];
			} else if ( args[ i].equals( "-experiment") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				experiment = args[ i + 1];
			} else if ( args[ i].equals( "-log") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				log_folder_path = args[ i + 1];
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

		File directory = get_parent_directory( parent_directory, zip);
		if ( null == directory)
			return false;

		Reader reader = get_reader( inputStream, directory);
		if ( null == reader)
			return false;

		//String memory_size = System.getProperty( Constant._soars_memory_size);
		//JOptionPane.showMessageDialog( null, ( null == memory_size ? "No property" : memory_size), "Simulator", JOptionPane.INFORMATION_MESSAGE);

		if ( !Application.get_instance().init_instance( reader, directory, vml, experiment, log_folder_path))
			return false;

		return true;
	}

	/**
	 * @param inputStream
	 * @param parent_directory
	 * @return
	 */
	private static Reader get_reader(InputStream inputStream, File parent_directory) {
		String script = FileUtility.read_text_from_file( inputStream);
		if ( null == script)
			return null;

		String user_data_directory = ( parent_directory.getAbsolutePath().replaceAll( "\\\\", "/") + "/simulator/" + Constant._userDataDirectory);
		System.setProperty( Constant._soarsUserDataDirectory, user_data_directory);

		user_data_directory += "/"; 

		//JOptionPane.showMessageDialog( null, user_data_directory, "Simulator", JOptionPane.INFORMATION_MESSAGE);

		while ( 0 <= script.indexOf( Constant._reservedUserDataDirectory))
			script = script.replace( Constant._reservedUserDataDirectory, user_data_directory);

		try {
			File newFile = File.createTempFile( "soars_", ".sor");
			newFile.deleteOnExit();
			if ( !FileUtility.write_text_to_file( newFile, script))
				return null;

			return new FileReader( newFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String memory_size = System.getProperty( Constant._soars_memory_size);
		//JOptionPane.showMessageDialog( null, ( null == memory_size ? "No property" : memory_size), "Simulator", JOptionPane.INFORMATION_MESSAGE);

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
		String script = null;
		String parent_directory = null;
		String zip = null;
		String vml = "";
		String experiment = "";
		String log_folder_path = "";
		boolean remove = false;
		for ( int i = 0; i < args.length; ++i) {
			if ( args[ i].equals( "-language") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				JFrame.setDefaultLookAndFeelDecorated( false);
				//JFrame.setDefaultLookAndFeelDecorated( true);
				Locale.setDefault( new Locale( args[ i + 1]));
				CommonEnvironment.get_instance().set( CommonEnvironment._localeKey, args[ i + 1]);
				locale = true;
				//break;
			} else if ( args[ i].equals( "-script") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				script = args[ i + 1];
			} else if ( args[ i].equals( "-parent_directory") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				parent_directory = args[ i + 1];
			} else if ( args[ i].equals( "-zip") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				zip = args[ i + 1];
			} else if ( args[ i].equals( "-vml") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				vml = args[ i + 1];
			} else if ( args[ i].equals( "-experiment") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				experiment = args[ i + 1];
			} else if ( args[ i].equals( "-log") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				log_folder_path = args[ i + 1];
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

		if ( null == script) {
			if ( !Application.get_instance().init_instance())
				System.exit( 1);
		} else {
			File file = new File( script);
			if ( !file.exists() || !file.isFile() || !file.canRead())
				System.exit( 1);

			File directory = get_parent_directory( parent_directory, zip);
			if ( null == directory || !directory.exists() || !directory.isDirectory())
				System.exit( 1);

			Reader reader = null;
			if ( null != parent_directory) {
				try {
					reader = new FileReader( file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.exit( 1);
				}
			} else if ( null != zip) {
				reader = get_reader( file, directory);
				if ( null == reader)
					System.exit( 1);
			} else
				System.exit( 1);

			if ( !Application.get_instance().init_instance( reader, directory, vml, experiment, log_folder_path))
				System.exit( 1);
		}
	}

	/**
	 * @param parent_directory
	 * @param zip
	 * @return
	 */
	private static File get_parent_directory(String parent_directory, String zip) {
		if ( null != parent_directory)
			return new File( parent_directory);

		if ( null != zip) {
			File directory = SoarsCommonTool.make_parent_directory();
			if ( null == directory)
				return null;

			if ( !ZipUtility.decompress( new File( zip), directory)) {
				FileUtility.delete( directory, true);
				return null;
			}

			return directory;
		}

		return null;
	}

	/**
	 * @param file
	 * @param parent_directory
	 * @return
	 */
	private static Reader get_reader(File file, File parent_directory) {
		String script = FileUtility.read_text_from_file( file);
		if ( null == script)
			System.exit( 1);

		String user_data_directory = ( parent_directory.getAbsolutePath().replaceAll( "\\\\", "/") + "/simulator/" + Constant._userDataDirectory);
		System.setProperty( Constant._soarsUserDataDirectory, user_data_directory);

		user_data_directory += "/"; 

		//JOptionPane.showMessageDialog( null, user_data_directory, "Simulator", JOptionPane.INFORMATION_MESSAGE);

		while ( 0 <= script.indexOf( Constant._reservedUserDataDirectory))
			script = script.replace( Constant._reservedUserDataDirectory, user_data_directory);

		if ( !FileUtility.write_text_to_file( file, script))
			return null;

		Reader reader = null;
		try {
			reader = new FileReader( file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		return reader;
	}
}
