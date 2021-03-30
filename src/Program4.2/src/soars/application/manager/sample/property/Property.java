/**
 * 
 */
package soars.application.manager.sample.property;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soars.application.manager.sample.main.Constant;
import soars.application.manager.sample.main.ResourceManager;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 *
 */
public class Property {

	/**
	 * 
	 */
	public File _directory = null;

	/**
	 * 
	 */
	public String _title = null;

	/**
	 * 
	 */
	public BufferedImage _bufferedImage = null;

	/**
	 * 
	 */
	public Property() {
		super();
	}

	/**
	 * 
	 */
	public void cleanup() {
		if ( null == _directory || !_directory.exists() || !_directory.isDirectory())
			return;

		FileUtility.delete( _directory, true);
	}

	/**
	 * @param file
	 * @return
	 */
	public ModelInformation get_model_information(File file) {
		return ModelInformation.get( file);
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean setup(File file) {
		_directory = SoarsCommonTool.make_parent_directory();
		if ( null == _directory)
			return false;

		if ( !ZipUtility.decompress( file, new String[] { "visualshell/doc/"}, _directory)) {
			FileUtility.delete( _directory, true);
			return false;
		}

		_title = get_title();
		_bufferedImage = ZipUtility.get_image( file, "visualshell/theme/image.png");

		return true;
	}

	/**
	 * @return
	 */
	private String get_title() {
		File file = new File( _directory, "visualshell/" + Constant._documentHtmlFilename);
		if ( !file.exists() || !file.isFile())
			return ResourceManager.get( "sample.tree.no.title");

		String text = FileUtility.read_text_from_file( file, "UTF8");
		if ( null == text)
			return ResourceManager.get( "sample.tree.no.title");

		String[] line = text.split( "\n");
		if ( null == line || 2 > line.length)
			return ResourceManager.get( "sample.tree.no.title");

		if ( !line[ 1].startsWith( "<head><title>") || !line[ 1].endsWith( "</title>"))
			return ResourceManager.get( "sample.tree.no.title");

		String title = line[ 1].substring( "<head><title>".length(), line[ 1].length() - "</title>".length());
		if ( null == title || title.equals( ""))
			return ResourceManager.get( "sample.tree.no.title");

		return title;
	}

	/**
	 * @param modelInformation
	 * @param title
	 * @param date
	 * @param author
	 * @param email
	 * @param comment
	 * @return
	 */
	public boolean update_model_information(ModelInformation modelInformation, String title, String date, String author, String email, String comment) {
		if ( !modelInformation.update( title, date, author, email, comment))
			return false;

		_title = title;
		return true;
	}

	/**
	 * @param file
	 * @param imagefile
	 * @return
	 */
	public boolean update_image(File file, File imagefile) {
		if ( null == file)
			return false;

		BufferedImage bufferedImage = null;

		if ( null != imagefile) {
			bufferedImage = Resource.load_image( imagefile);
			if ( null == bufferedImage)
				return false;
		}

		File parent_directory = SoarsCommonTool.make_parent_directory();
		if ( null == parent_directory)
			return false;

		if ( !ZipUtility.decompress( file, parent_directory)) {
			FileUtility.delete( parent_directory, true);
			return false;
		}

		File root_directory = new File( parent_directory, "visualshell");
		if ( !root_directory.exists() || !root_directory.isDirectory()) {
			FileUtility.delete( parent_directory, true);
			return false;
		}

		File theme_directory = new File( root_directory, "theme");
		if ( !theme_directory.exists() && !theme_directory.mkdirs()) {
			FileUtility.delete( parent_directory, true);
			return false;
		}

		File image_file = new File( theme_directory, "image.png");
		if ( null != bufferedImage) {
			if ( !Resource.write_image( bufferedImage, "png", image_file)) {
				FileUtility.delete( parent_directory, true);
				return false;
			}
		} else {
			if ( !image_file.delete()) {
				FileUtility.delete( parent_directory, true);
				return false;
			}
		}

		if ( !ZipUtility.compress( file, root_directory, parent_directory)) {
			FileUtility.delete( parent_directory, true);
			return false;
		}

		FileUtility.delete( parent_directory, true);

		_bufferedImage = bufferedImage;
		return true;
	}

	/**
	 * @param file
	 * @param propertyMap
	 */
	static public void update_propertyMap(File file, Map<File, Property> propertyMap) {
		if ( !file.isFile())
			return;

		Property property = propertyMap.get( file);
		if ( null != property)
			return;

		property = new Property();
		property.setup( file);
		propertyMap.put( file, property);
	}

	/**
	 * @param file
	 * @param propertyMap
	 */
	public static void cleanup_propertyMap(File file, Map<File, Property> propertyMap) {
		if ( !file.isFile())
			return;

		Property property = propertyMap.get( file);
		if ( null == property)
			return;

		property.cleanup();
		propertyMap.remove( file);
	}

	/**
	 * @param propertyMap
	 */
	public static void cleanup_propertyMap(Map<File, Property> propertyMap) {
		List<File> files = new ArrayList<File>();

		Iterator iterator = propertyMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			File file = ( File)entry.getKey();
			Property property = ( Property)entry.getValue();
			if ( file.exists() && file.isFile())
				continue;

			property.cleanup();
			files.add( file);
		}

		for ( int i = 0; i < files.size(); ++i)
			propertyMap.remove( files.get( i));
	}

	/**
	 * @param propertyMap
	 */
	public static void cleanup_propertyMap_all(Map<File, Property> propertyMap) {
		Iterator iterator = propertyMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			File file = ( File)entry.getKey();
			Property property = ( Property)entry.getValue();
			property.cleanup();
		}
		propertyMap.clear();
	}
}
