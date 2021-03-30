/**
 * 
 */
package soars.application.visualshell.file.exporter.java.object;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kurata
 *
 */
public class RoleClassCode {

	/**
	 * 
	 */
	public String _className = "";

	/**
	 * 
	 */
	public String _package = "";

	/**
	 * 
	 */
	public List<String> _imports = new ArrayList<>();

	/**
	 * 
	 */
	public String _body1 = "";

	/**
	 * 
	 */
	public List<Variable> _variables = new ArrayList<>();

	/**
	 * 
	 */
	public String _body2 = "";

	/**
	 * 
	 */
	public RoleClassCode() {
	}
}
