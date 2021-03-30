/**
 * 
 */
package soars.application.animator.file.loader.legacy;

import java.util.Comparator;
import java.util.Map;

/**
 * The comparison function for the start positions of log.
 * @author kurata / SOARS project
 */
public class LogObjectComparator implements Comparator {

	/**
	 * Log hashtable(String[name] - LogObject)
	 */
	private Map _map = null;

	/**
	 * Creates the comparison function for the start positions of log.
	 * @param map the log hashtable(String[name] - LogObject)
	 */
	public LogObjectComparator(Map map) {
		super();
		_map = map;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		LogObject logObject0 = ( LogObject)_map.get( arg0);
		LogObject logObject1 = ( LogObject)_map.get( arg1);
		if ( logObject0._start < logObject1._start)
			return -1;
		else if ( logObject0._start > logObject1._start)
			return 1;
		else
			return 0;
	}
}
