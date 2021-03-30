/**
 * 
 */
package soars.tool.simulator.launcher.main;

import java.awt.Image;
import java.io.File;
import java.util.Locale;

import javax.swing.JFrame;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 *
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
		if ( !SoarsCommonTool.setup_look_and_feel())
			return false;

		MainFrame mainFrame = MainFrame.get_instance();
		if ( !mainFrame.create())
			return false;

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			mainFrame.setIconImage( image);

		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String homeDirectoryName = System.getProperty( CommonConstant._soarsHome);
		if ( null == homeDirectoryName || homeDirectoryName.equals( "")) {
			File currentDirectory = new File( "");
			if ( null == currentDirectory)
				System.exit( 1);

			File homeDirectory = new File( currentDirectory.getAbsolutePath() + Constant._homeDirectory);
			System.setProperty( CommonConstant._soarsHome, homeDirectory.getAbsolutePath());
		}

		String propertyDirectoryName = System.getProperty( CommonConstant._soarsProperty);
		if ( null == propertyDirectoryName || propertyDirectoryName.equals( "")) {
			File currentDirectory = new File( "");
			if ( null == currentDirectory)
				System.exit( 1);

			File propertyDirectory = new File( currentDirectory.getAbsolutePath() + Constant._propertyDirectory);
			if ( null == propertyDirectory)
				System.exit( 1);

			System.setProperty( CommonConstant._soarsProperty, propertyDirectory.getAbsolutePath());
		}

		String propertiesFilename = System.getProperty( CommonConstant._soarsProperties);
		if ( null == propertiesFilename || propertiesFilename.equals( "")) {
			File currentDirectory = new File( "");
			if ( null == currentDirectory)
				System.exit( 1);

			File propertiesFile = SoarsCommonEnvironment.get_instance().get();
			if ( null == propertiesFile)
				System.exit( 1);

			System.setProperty( CommonConstant._soarsProperties, propertiesFile.getAbsolutePath());
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
