/*
 * 2005/03/15
 */
package soars.application.animator.object.player.base.edit.objects;

import java.util.Comparator;

import soars.common.utility.tool.sort.StringNumberComparator;

import soars.application.animator.object.player.base.ObjectBase;

/**
 * The comparison function for the object.
 * @author kurata / SOARS project
 */
public class ObjectComparator implements Comparator {

	/**
	 * 
	 */
	private boolean _ascend = true;

	/**
	 * 
	 */
	private boolean _to_lower = true;

	/**
	 * Creates the comparison function for the object.
	 */
	public ObjectComparator(boolean ascend, boolean to_lower) {
		super();
		_ascend = ascend;
		_to_lower = to_lower;
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public final int compare(Object arg0, Object arg1) {
		ObjectBase a = ( ObjectBase)arg0;
		ObjectBase b = ( ObjectBase)arg1;
		if ( _to_lower) {
			if ( _ascend)
				return StringNumberComparator.compareTo( a._name.toLowerCase(), b._name.toLowerCase());
			else
				return StringNumberComparator.compareTo( b._name.toLowerCase(), a._name.toLowerCase());
		} else {
			if ( _ascend)
				return StringNumberComparator.compareTo( a._name, b._name);
			else
				return StringNumberComparator.compareTo( b._name, a._name);
		}
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#equals(java.lang.Object, java.lang.Object)
	 */
	public final boolean equals(Object arg0, Object arg1) {
		ObjectBase a = ( ObjectBase)arg0;
		ObjectBase b = ( ObjectBase)arg1;
		return( a._name.equals( b._name));
	}
}
