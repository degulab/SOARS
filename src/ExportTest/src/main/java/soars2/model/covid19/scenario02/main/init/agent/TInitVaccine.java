package soars2.model.covid19.scenario02.main.init.agent;

import soars2.core.TAgent;
import soars2.core.TAgentManager;
import soars2.core.TSpot;
import soars2.core.TSpotManager;
import soars2.model.covid19.scenario02.TSpotTypes;
import soars2.model.covid19.scenario02.agent.TBabyMoveRole;
import soars2.model.covid19.scenario02.agent.TMiddleMoveRole;
import soars2.model.covid19.scenario02.agent.TSchoolchildMoveRole;
import soars2.model.covid19.scenario02.agent.TStudentMoveRole;
import soars2.model.covid19.scenario02.agent.TYoungMoveRole;
import soars2.model.covid19.scenario02.spot.TModelRole;
import soars2.utils.random2013.ICRandom;

/**
 * 
 */
public class TInitVaccine {

	// Entities : [middle]

	public static boolean init1(TAgent agent, TSpot spot, TMiddleMoveRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// condition : keyword : <model>is v_strategy=all
		if (modelRole.getv_strategy().equals("all")) {
			// command : collection : <model>addAgent candidate
			modelRole.getcandidate().add(agent);
			return true;
		}
		return false;
	}

	// Entities : [schoolchild]

	public static boolean init2(TAgent agent, TSpot spot, TSchoolchildMoveRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : collection : <model>addAgent candidate
		modelRole.getcandidate().add(agent);
		return true;
	}

	// Entities : [baby]

	public static boolean init3(TAgent agent, TSpot spot, TBabyMoveRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : collection : <model>addAgent candidate
		modelRole.getcandidate().add(agent);
		return true;
	}

	// Entities : [old]

	public static boolean init4(TAgent agent, TSpot spot, TBabyMoveRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// condition : keyword : <model>is v_strategy=all
		if (modelRole.getv_strategy().equals("all")) {
			// command : collection : <model>addAgent candidate
			modelRole.getcandidate().add(agent);
			return true;
		}
		return false;
	}

	// Entities : [young]

	public static boolean init5(TAgent agent, TSpot spot, TYoungMoveRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : collection : <model>addAgent candidate
		modelRole.getcandidate().add(agent);
		return true;
	}

	// Entities : [student]

	public static boolean init6(TAgent agent, TSpot spot, TStudentMoveRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : collection : <model>addAgent candidate
		modelRole.getcandidate().add(agent);
		return true;
	}
}
