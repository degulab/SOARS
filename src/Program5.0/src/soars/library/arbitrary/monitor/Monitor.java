/**
 * 
 */
package soars.library.arbitrary.monitor;

import time.TimeCounter;

/**
 * @author kurata
 *
 */
public class Monitor {

	/**
	 * @param terminalTime
	 */
	public static void print(String terminalTime) {
		System.err.println( "Current time: " + TimeCounter.getCurrentTime().toString()
			+ ( terminalTime.equals( "") ? "" : ( " / " + terminalTime)));
	}

	/**
	 * 
	 */
	public Monitor() {
	}
}
