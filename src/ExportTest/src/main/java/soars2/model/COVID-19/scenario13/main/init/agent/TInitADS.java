package soars2.model.COVID-19.scenario13.main.init.agent;

import soars2.core.TAgent;
import soars2.core.TAgentManager;
import soars2.core.TSpot;
import soars2.core.TSpotManager;
import soars2.core.TTime;
import soars2.model.COVID-19.scenario13.TSpotTypes;
import soars2.model.COVID-19.scenario13.agent.TAgentRole;
import soars2.model.COVID-19.scenario13.spot.TModelRole;
import soars2.utils.random2013.ICRandom;

/**
 * 
 */
public class TInitADS {

	// Entities : [baby, student, young, middle, schoolchild, old]

	public static boolean init1(TAgent agent, TSpot spot, TAgentRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// condition : collection : <model>containsAgent initia_linfected
		if (modelRole.getinitia_linfected().contains(agent)) {
			// command : keyword : set ADS=1
			ownerRole.setADS("1");
			// command : time : setTime timer=timer ; setTime timer+=0/0:00
			ownerRole.settimer(new TTime("0/0:00").add("0/0:00"));
			// command : substitution : <model>askEquip infected=infected+1
			modelRole.setinfected(modelRole.getinfected()+1);
			// command : substitution : <model>askEquip patientpluusdead=patientpluusdead+1
			modelRole.setpatientpluusdead(modelRole.getpatientpluusdead()+1);
			// command : substitution : <model>askEquip InfectionLevel1=InfectionLevel1+1
			modelRole.setInfectionLevel1(modelRole.getInfectionLevel1()+1);
			return true;
		}
		return false;
	}

	public static boolean init2(TAgent agent, TSpot spot, TAgentRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <model>askEquip naive=naive+1
		modelRole.setnaive(modelRole.getnaive()+1);
		return true;
	}
}
