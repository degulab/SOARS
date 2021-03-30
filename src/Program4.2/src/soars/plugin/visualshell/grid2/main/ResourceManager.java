/*
 * 2005/01/31
 */
package soars.plugin.visualshell.grid2.main;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author kurata
 */
public class ResourceManager {


	/**
	 * 
	 */
	static private Object _lock = new Object();


	/**
	 * 
	 */
	static private ResourceManager _resourceManager = null;


	/**
	 * 
	 */
	private final String _baseName = "soars.plugin.visualshell.grid2.resource.language.Resource";


	/**
	 * 
	 */
	protected ResourceBundle _resourceBundle = null;


	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	public static void startup() {
		synchronized( _lock) {
			if ( null == _resourceManager) {
				_resourceManager = new ResourceManager();
				if ( !_resourceManager.initialize())
					System.exit( 0);
			}
		}
	}

	/**
	 * @return
	 */
	public static ResourceManager get_instance() {
		if ( null == _resourceManager) {
			System.exit( 0);
		}

		return _resourceManager;
	}

	/**
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		try {
			return get_instance()._resourceBundle.getString( key);
		} catch (NullPointerException e) {
			return "Key is null!";
		} catch (MissingResourceException e) {
			return "Unknown resource!";
		} catch (ClassCastException e) {
			return "Value is not String!";
		}
	}

	/**
	 * 
	 */
	public ResourceManager() {
		super();
	}

	/**
	 * @return
	 */
	public boolean initialize() {
		_resourceBundle = ResourceBundle.getBundle( _baseName, Locale.getDefault());
		return true;
	}
}
