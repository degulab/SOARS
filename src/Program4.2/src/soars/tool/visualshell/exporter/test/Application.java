/**
 * 
 */
package soars.tool.visualshell.exporter.test;

import java.io.File;

import soars.common.soars.tool.SoarsCommonTool;
import soars.tool.animator.launcher.main.Constant;
import soars.tool.visualshell.exporter.export.Exporter;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * @param args
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

		switch ( args.length) {
			case 0:
				return;
			case 1:
				if ( !Exporter.run( args[ 0]))
					System.exit( 1);

				break;
			default:
				if ( !Exporter.run( args[ 0], args[ 1]))
					System.exit( 1);

			break;
		}
	}
}
