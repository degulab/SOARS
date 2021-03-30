/*
 * 2005/01/31
 */
package soars.application.visualshell.main;

import java.util.Locale;

import soars.common.utility.tool.resource.ResourceManagerBase;

/**
 * The resource maintenance class.
 * @author kurata / SOARS project
 */
public class ResourceManager extends ResourceManagerBase {

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
	static private String _filePath = "../resource/properties/application/visualshell4/Resource_%s.properties";

	/**
	 * 
	 */
	static private String _defaultFilePath = "../resource/properties/application/visualshell4/Resource.properties";

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null == _resourceManager) {
				_resourceManager = new ResourceManager();
				if ( !_resourceManager.initialize( String.format( _filePath, Locale.getDefault()), _defaultFilePath))
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
	 * 
	 */
	public ResourceManager() {
		super();
	}
}
