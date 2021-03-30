/**
 * 
 */
package soars.common.soars.logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kurata
 *
 */
public class LogProperties {

	/**
	 * 
	 */
	public String _title = "";

	/**
	 * 
	 */
	public String _experimentName = "";

	/**
	 * 
	 */
	public String _filePath = "";

	/**
	 * 
	 */
	public Map<String, Map<String, List<String>>> _entityNamesMap = new HashMap<>();

	/**
	 * 
	 */
	public LogProperties() {
	}
}
