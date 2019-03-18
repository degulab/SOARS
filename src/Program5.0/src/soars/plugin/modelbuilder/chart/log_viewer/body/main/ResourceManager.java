/*
 * 2014/01/20
 */
package soars.plugin.modelbuilder.chart.log_viewer.body.main;

import java.util.Locale;

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
	static private String _filePath = "../../resource/properties/plugin/modelbuilder/chart/log_viewer/body/Resource_%s.properties";

	/**
	 * 
	 */
	static private String _defaultFilePath = "../../resource/properties/plugin/modelbuilder/chart/log_viewer/body/Resource.properties";

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
