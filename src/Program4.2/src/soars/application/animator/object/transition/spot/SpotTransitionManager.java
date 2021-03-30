/*
 * 2005/02/25
 */
package soars.application.animator.object.transition.spot;

import java.util.Map;

import soars.application.animator.object.player.spot.ISpotObjectManipulator;
import soars.application.animator.object.transition.base.TransitionBase;
import soars.application.animator.object.transition.base.TransitionManager;

/**
 * The scenario data manager for spots.
 * @author kurata / SOARS project
 */
public class SpotTransitionManager extends TransitionManager {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private SpotTransitionManager _spotTransitionManager = null;

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
			if ( null == _spotTransitionManager) {
				_spotTransitionManager = new SpotTransitionManager();
			}
		}
	}

	/**
	 * Returns the instance of the scenario data manager for spots.
	 * @return the instance of the scenario data manager for spots
	 */
	public static SpotTransitionManager get_instance() {
		if ( null == _spotTransitionManager) {
			System.exit( 0);
		}

		return _spotTransitionManager;
	}

	/**
	 * Creates the scenario data manager for spots.
	 */
	public SpotTransitionManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.transition.base.TransitionManager#createTransition()
	 */
	protected TransitionBase createTransition() {
		return new SpotTransition();
	}

	/**
	 * Returns the number of agents on the specified spot.
	 * @param spotObjectManipulator the specified spot
	 * @param index the specified position of the scenario
	 * @return the number of agents on the specified spot
	 */
	public int get_index(ISpotObjectManipulator spotObjectManipulator, int index) {
		Map spotTransitionMap = ( Map)get( index);
		SpotTransition spotTransition = ( SpotTransition)spotTransitionMap.get( spotObjectManipulator);
		if ( null == spotTransition) {
			spotTransition = new SpotTransition();
			spotTransitionMap.put( spotObjectManipulator, spotTransition);
		}
		return spotTransition.get_index();
	}
}
