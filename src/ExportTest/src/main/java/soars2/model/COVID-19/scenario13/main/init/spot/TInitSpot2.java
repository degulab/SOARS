package soars2.model.COVID-19.scenario13.main.init.spot;

import java.util.Collections;
import java.util.Random;
import soars2.core.TSpot;
import soars2.core.TSpotManager;
import soars2.model.COVID-19.scenario13.TSpotTypes;
import soars2.model.COVID-19.scenario13.spot.TModelRole;
import soars2.utils.random2013.ICRandom;

/**
 * 
 */
public class TInitSpot2 {

	// Entities : [model]

	public static boolean init1(TSpot spot, TModelRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : list : <model>shuffleAll homes
		Collections.shuffle(modelRole.gethomes(), new Random(rand.nextInt()));
		return true;
	}
}
