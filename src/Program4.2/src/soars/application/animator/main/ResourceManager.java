/*
 * 2005/01/31
 */
package soars.application.animator.main;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The resource maintenance class.
 * @author kurata / SOARS project
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
	private final String _baseName = "soars.application.animator.resource.language.Resource";

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
	 * The startup routine.
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
	 * Returns the instance of this class.
	 * @return the instance of this class
	 */
	public static ResourceManager get_instance() {
		if ( null == _resourceManager) {
			System.exit( 0);
		}

		return _resourceManager;
	}

	/**
	 * Returns the value associated with the specified key.
	 * @param key the specified key
	 * @return the value associated with the specified key
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
	 * Creates the resource maintenance class.
	 */
	public ResourceManager() {
		super();
	}

	/**
	 * Returns true if this object is initialized successfully.
	 * @return true if this object is initialized successfully
	 */
	public boolean initialize() {
		_resourceBundle = ResourceBundle.getBundle( _baseName, Locale.getDefault());
		return true;
	}
}
