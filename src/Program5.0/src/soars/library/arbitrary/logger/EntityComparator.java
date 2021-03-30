/**
 * 
 */
package soars.library.arbitrary.logger;

import java.util.Comparator;

import env.EquippedObject;

/**
 * @author kurata
 *
 */
public class EntityComparator<T> implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		if ( get_id( ( String)o1) < get_id( ( String)o2))
			return -1;
		else if ( get_id( ( String)o1) > get_id( ( String)o2))
			return 1;
		else
			return 0;
	}

	/**
	 * @param entityName
	 * @return
	 */
	private int get_id(String entityName) {
		EquippedObject equippedObject = LoggerDlg.get_equippedObject( entityName);
		if ( null != equippedObject)
			return Integer.valueOf( equippedObject.getProp( EquippedObject.ID_KEY));
		else {
			equippedObject = LoggerDlg.get_equippedObject( entityName + "1");
			if ( null != equippedObject)
				return Integer.valueOf( equippedObject.getProp( EquippedObject.ID_KEY));
		}
		return 0;
	}
}
