/**
 * 
 */
package soars.common.soars.tool;

import java.io.File;
import java.util.Vector;

import javax.swing.UIManager;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.tool.file.FileUtility;

/**
 * @author kurata
 *
 */
public class SoarsCommonTool {

	/**
	 * @return
	 */
	public static File get_default_property_directory() {
		String homeDirectoryName = System.getProperty( CommonConstant._soarsHome);
		if ( null == homeDirectoryName)
			return null;

		String configRelativePath = SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._configKey, "");
		if ( configRelativePath.equals( ""))
			return null;

		File propertyDirectory = new File( homeDirectoryName + "/" + configRelativePath);
		if ( !propertyDirectory.exists()) {
			if ( !propertyDirectory.mkdirs())
				return null;
		}

		return propertyDirectory;
////		String propertyDirectoryName = System.getProperty( "user.home");
////		if ( null == propertyDirectoryName)
////			return null;
//
//		String homeDirectoryName = System.getProperty( CommonConstant._soarsHome);
//		if ( null == homeDirectoryName)
//			return null;
//
//		String configRelativePath = SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._configKey, "");
//		if ( configRelativePath.equals( ""))
//			return null;
//
//		File propertyDirectory = new File( homeDirectoryName + "/" + configRelativePath);
//		if ( !propertyDirectory.exists()) {
//			if ( !propertyDirectory.mkdirs())
//				return null;
//		}
//
//		return propertyDirectory;
	}

	/**
	 * @return
	 */
	public static File get_system_property_directory() {
		String propertyDirectoryName = System.getProperty( "user.home");
		if ( null == propertyDirectoryName)
			return null;

		File propertyDirectory = new File( propertyDirectoryName + "/soars");
		if ( !propertyDirectory.exists()) {
			if ( !propertyDirectory.mkdirs())
				return null;
		}

		return propertyDirectory;
	}

	/**
	 * @return
	 */
	public static boolean setup_look_and_feel() {
		String osname = System.getProperty( "os.name");
		if ( 0 <= osname.indexOf( "Windows") || 0 <= osname.indexOf( "Mac"))
			return SwingTool.setup_look_and_feel( UIManager.getSystemLookAndFeelClassName());
		else {
			if ( SwingTool.setup_look_and_feel( "javax.swing.plaf.metal.MetalLookAndFeel"))
				return true;

			return SwingTool.setup_look_and_feel( UIManager.getSystemLookAndFeelClassName());
		}
	}

	/**
	 * @return
	 */
	public static File make_parent_directory() {
		String systemTemporaryDirectory = get_system_temporary_directory();

		File parentDirectory;
		int index = 0;
		while ( true) {
			parentDirectory = new File( systemTemporaryDirectory + "soars" + index);
			if ( parentDirectory.mkdir())
				break;

			++index;
		}

		return parentDirectory;
	}

	/**
	 * @return
	 */
	public static String get_system_temporary_directory() {
		String systemTemporaryDirectory = System.getProperty( "java.io.tmpdir");
		if ( !systemTemporaryDirectory.endsWith( "/") && !systemTemporaryDirectory.endsWith( "\\"))
			systemTemporaryDirectory += "/";

		return systemTemporaryDirectory;
	}

	/**
	 * @param filename
	 * @param systemDefaultFileEncoding
	 * @return
	 */
	public static String read_text_from_file(String filename, String systemDefaultFileEncoding) {
		File file = new File( filename);
		if ( !file.exists() || !file.isFile() || !file.canRead())
			return null;

		return read_text_from_file( file, systemDefaultFileEncoding);
	}

	/**
	 * @param file
	 * @param systemDefaultFileEncoding
	 * @return
	 */
	public static String read_text_from_file(File file, String systemDefaultFileEncoding) {
		if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")
			&& !System.getProperty( systemDefaultFileEncoding, "").equals( ""))
			return FileUtility.read_text_from_file( file, System.getProperty( systemDefaultFileEncoding, ""));
		else
			return FileUtility.read_text_from_file( file);
	}

	/**
	 * @param filename
	 * @param text
	 * @param systemDefaultFileEncoding
	 * @return
	 */
	public static boolean write_text_to_file(String filename, String text, String systemDefaultFileEncoding) {
		File file = new File( filename);
		if ( !file.exists() || !file.isFile() || !file.canRead())
			return false;

		return write_text_to_file( file, text, systemDefaultFileEncoding);
	}

	/**
	 * @param file
	 * @param text
	 * @param systemDefaultFileEncoding
	 * @return
	 */
	public static boolean write_text_to_file(File file, String text, String systemDefaultFileEncoding) {
		if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")
			&& !System.getProperty( systemDefaultFileEncoding, "").equals( ""))
			return FileUtility.write_text_to_file( file, text, System.getProperty( systemDefaultFileEncoding, ""));
		else
			return FileUtility.write_text_to_file( file, text);
	}

	/**
	 * Return true, if names from name1 and number1 contain one of names from name2 and number2.
	 * @param name1
	 * @param number1
	 * @param name2
	 * @param number2
	 * @return
	 */
	public static boolean has_same_name(String name1, String number1, String name2, String number2) {
		String result1 = separate( name1);
		String result2 = separate( name2);
		if ( !result1.equals( result2))
			return false;

		String headNumber1 = name1.substring( result1.length());
		String headNumber2 = name2.substring( result2.length());
		if ( ( headNumber1.equals( "") && number1.equals( ""))
			|| ( headNumber2.equals( "") && number2.equals( "")))
			return false;

		Vector ranges1 = get_ranges( headNumber1, number1);
		Vector ranges2 = get_ranges( headNumber2, number2);
		for ( int i = 0; i < ranges1.size(); ++i) {
			String[] range1 = ( String[])ranges1.get( i);
			for ( int j = 0; j < ranges2.size(); ++j) {
				String[] range2 = ( String[])ranges2.get( j);
				if ( Long.parseLong( range1[ 1]) < Long.parseLong( range2[ 0])
					|| Long.parseLong( range2[ 1]) < Long.parseLong( range1[ 0]))
					continue;

				return true;
			}
		}

		return false;
	}

	/**
	 * @param name
	 * @param number
	 * @param fullName
	 * @return
	 */
	public static boolean has_same_name(String name, String number, String fullName) {
		String headName = separate( name);
		String headNumber = name.substring( headName.length());
		Vector ranges = get_ranges( headNumber, number);
		if ( null == ranges)
			return false;

		return has_same_name( headName, ranges, fullName);
	}

	/**
	 * @param headName
	 * @param ranges
	 * @param fullName
	 * @return
	 */
	public static boolean has_same_name(String headName, Vector ranges, String fullName) {
		if ( ranges.isEmpty())
			return headName.equals( fullName);

		String result2 = separate( fullName);
		if ( !headName.equals( result2))
			return false;

		String head_number2 = fullName.substring( result2.length());
		if ( head_number2.equals( ""))
			return false;

		long number2 = Long.parseLong( head_number2);
		for ( int i = 0; i < ranges.size(); ++i) {
			String[] range = ( String[])ranges.get( i);
			if ( Long.parseLong( range[ 0]) <= number2
				&& number2 <= Long.parseLong( range[ 1]))
				return true;
		}
		return false;
	}

	/**
	 * @param name
	 * @return
	 */
	public static String separate(String name) {
		int index = name.length() - 1;
		while ( index >= 0) {
			char c = name.charAt( index);
			if ( 0 > "0123456789".indexOf( c))
				break;

			--index;
		}

		if ( name.length() - 1 == index)
			return name;

		index += 1;
		while ( index < name.length()) {
			char c = name.charAt( index);
			if ( '0' != c)
				break;

			++index;
		}

		return name.substring( 0, index);
	}

	/**
	 * @param headNumber
	 * @param number
	 * @return
	 */
	public static Vector get_ranges(String headNumber, String number) {
		Vector ranges = new Vector();
		if ( headNumber.equals( "")) {
			if ( number.equals( ""))
				return ranges;

			ranges.add( new String[] { "1", number});
		} else {
			if ( number.equals( ""))
				ranges.add( new String[] { headNumber, headNumber});
			else {
				long n = Long.parseLong( number);
				//int digit = ( int)( Math.floor( Math.log( n) / Math.log( 10)));
				int digit = number.length() - 1;
				for ( int i = 0; i <= digit; ++i) {
					long min = ( long)Math.pow( 10, i);
					long max = Math.min( n, ( int)Math.pow( 10, i + 1) - 1);
					ranges.add( new String[] {
						headNumber + String.valueOf( min),
						headNumber + String.valueOf( max)});
				}
			}
		}
		return ranges;
	}

	/**
	 * @param headNumber
	 * @param number1
	 * @param number2
	 * @return
	 */
	public static Vector get_ranges(String headNumber, int number1, int number2) {
		Vector ranges = new Vector();
		if ( headNumber.equals( "")) {
			ranges.add( new String[] { String.valueOf( number1), String.valueOf( number2)});
		} else {
			//int digit1 = ( int)( Math.floor( Math.log( number1) / Math.log( 10)));
			//int digit2 = ( int)( Math.floor( Math.log( number2) / Math.log( 10)));
			int digit1 = String.valueOf( number1).length() - 1;
			int digit2 = String.valueOf( number2).length() - 1;
			for ( int i = digit1; i <= digit2; ++i) {
				int min = Math.max( number1, ( int)Math.pow( 10, i));
				int max = Math.min( number2, ( int)Math.pow( 10, i + 1) - 1);
				ranges.add( new String[] {
					headNumber + String.valueOf( min),
					headNumber + String.valueOf( max)});
			}
		}
		return ranges;
	}
}
