/*
 * 2014/01/20
 */
package soars.library.arbitrary.logger;

import soars.common.soars.environment.CommonEnvironment;
import soars.common.utility.tool.resource.ResourceManagerBase;

/**
 * @author kurata
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
	static private String _filePath = "../resource/properties/application/engine/Resource_%s.properties";

	/**
	 * 
	 */
	static private String _defaultFilePath = "../resource/properties/application/engine/Resource.properties";

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
				if ( !_resourceManager.initialize( String.format( _filePath, CommonEnvironment.get_instance().get( CommonEnvironment._localeKey)), _defaultFilePath))
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
