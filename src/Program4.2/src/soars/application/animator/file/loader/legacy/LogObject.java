/**
 * 
 */
package soars.application.animator.file.loader.legacy;

import java.util.Vector;

/**
 * The Array of the SOARS log.
 * @author kurata / SOARS project
 */
public class LogObject extends Vector {

	/**
	 * Start position of the log.
	 */
	public int _start = -1;

	/**
	 * Creates the Array of the SOARS log.
	 */
	public LogObject() {
		super();
	}

	/**
	 * Returns false if value equals "NULL".
	 * @param value the log string
	 * @return false if value equals "NULL"
	 */
	public boolean append(String value) {
		if ( 0 <= _start && value.equals( "NULL"))
			return false;

		if ( 0 > _start && !value.equals( "NULL"))
			_start = size();

		add( value);

		return true;
	}
}
