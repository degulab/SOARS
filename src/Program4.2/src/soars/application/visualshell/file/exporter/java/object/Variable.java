/**
 * 
 */
package soars.application.visualshell.file.exporter.java.object;

/**
 * @author kurata
 *
 */
public class Variable {

	/**
	 * 
	 */
	public String _kind = "";

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 現在のスポットを使用する際に Getter / Setter が必要か？
	 */
	public boolean _spotSet = false;

	/**
	 * @param kind 
	 * @param name 
	 */
	public Variable(String kind, String name) {
		_kind = kind;
		_name = name;
	}
}
