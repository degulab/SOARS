/**
 * 
 */
package soars.application.manager.sample.main.panel.tree;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import soars.application.manager.sample.property.Property;

/**
 * @author kurata
 *
 */
public class NodeComparator implements Comparator<Node> {

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
	public NodeComparator(Map<File, Property> propertyMap, boolean ascend, boolean to_lower) {
		super();
		_propertyMap = propertyMap;
		_ascend = ascend;
		_to_lower = to_lower;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Node arg0, Node arg1) {
		return FileNameComparator.compare( arg0._file, arg1._file, _propertyMap, _ascend, _to_lower);
	}
}
