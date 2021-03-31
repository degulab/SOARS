package soars2.model.covid19.scenario13.main.init.agent;

import soars2.core.TAgent;
import soars2.core.TAgentManager;
import soars2.core.TSpot;
import soars2.core.TSpotManager;
import soars2.model.covid19.scenario13.TSpotTypes;
import soars2.model.covid19.scenario13.agent.TAgentRole;
import soars2.model.covid19.scenario13.spot.TModelRole;
import soars2.utils.random2013.ICRandom;

/**
 * 
 */
public class TInitAgent {

	// Entities : [baby, student, young, middle, schoolchild, old]

	public static boolean init1(TAgent agent, TSpot spot, TAgentRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : askEquip AgentAF=EnAAF*StAAF
		ownerRole.setAgentAF((ownerRole.getEnAAF())*(ownerRole.getStAAF()));
		// command : list : <model>moveToFirst homes
		if ( null != spot)
			agent.moveTo(((TSpot)(modelRole.gethomes().get(0))).getName(), spotManager.getSpotDB());
		else {
			spot = (TSpot)modelRole.gethomes().get(0);
			agent.initializeCurrentSpot(spot.getName(), spotManager.getSpotDB());
		}
		// command : list : <model>removeFirst homes
		modelRole.gethomes().remove(0);
		// command : list : addFirstSpot home
		ownerRole.gethome().add(0,spotManager.getSpotDB().get(agent.getCurrentSpotName()));
		return true;
	}
}
