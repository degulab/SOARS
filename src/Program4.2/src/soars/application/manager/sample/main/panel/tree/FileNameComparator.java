/**
 * 
 */
package soars.application.manager.sample.main.panel.tree;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import soars.application.manager.sample.main.ResourceManager;
import soars.application.manager.sample.property.Property;
import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 *
 */
public class FileNameComparator implements Comparator<File> {

	/**
	 * 
	 */
	private Map<File, Property> _propertyMap = new HashMap<File, Property>();

	/**
	 * 
	 */
	private boolean _ascend = true;

	/**
	 * 
	 */
	private boolean _to_lower = true;

	/**
	 * @param propertyMap
	 * @param ascend
	 * @param to_lower
	 */
	public FileNameComparator(Map<File, Property> propertyMap, boolean ascend, boolean to_lower) {
		super();
		_propertyMap = propertyMap;
		_ascend = ascend;
		_to_lower = to_lower;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(File file0, File file1) {
		return compare( file0, file1, _propertyMap, _ascend, _to_lower);
	}

	/**
	 * @param file0
	 * @param file1
	 * @param propertyMap
	 * @param ascend
	 * @param to_lower
	 * @return
	 */
	static public int compare(File file0, File file1, Map<File, Property> propertyMap, boolean ascend, boolean to_lower) {
		if ( file0.isDirectory() && file1.isDirectory()) {
			if ( to_lower) {
				if ( ascend)
					return StringNumberComparator.compareTo( file0.getName().toLowerCase(), file1.getName().toLowerCase());
				else
					return StringNumberComparator.compareTo( file1.getName().toLowerCase(), file0.getName().toLowerCase());
			} else {
				if ( ascend)
					return StringNumberComparator.compareTo( file0.getName(), file1.getName());
				else
					return StringNumberComparator.compareTo( file1.getName(), file0.getName());
			}
		} else if ( file0.isFile() && file1.isFile()) {
			String title0 = get_title( file0, propertyMap);
			String title1 = get_title( file1, propertyMap);
			if ( to_lower) {
				if ( ascend)
					return StringNumberComparator.compareTo( title0.toLowerCase(), title1.toLowerCase());
				else
					return StringNumberComparator.compareTo( title1.toLowerCase(), title0.toLowerCase());
			} else {
				if ( ascend)
					return StringNumberComparator.compareTo( title0, title1);
				else
					return StringNumberComparator.compareTo( title1, title0);
			}
		} else
			return ( file0.isDirectory() ? -1 : 1);
	}

	/**
	 * @param file
	 * @param propertyMap
	 * @return
	 */
	static private String get_title(File file, Map<File, Property> propertyMap) {
		Property property = propertyMap.get( file);
		if ( null == property || null == property._title)
			return ResourceManager.get( "sample.tree.no.title");

		return property._title;
	}
}
