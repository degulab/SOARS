package soars2.model.COVID-19.scenario13.main.init.agent;

import java.util.Collections;
import soars2.core.TAgent;
import soars2.core.TAgentManager;
import soars2.core.TSpot;
import soars2.core.TSpotManager;
import soars2.model.COVID-19.scenario13.TSpotTypes;
import soars2.model.COVID-19.scenario13.agent.TMiddleMoveRole;
import soars2.model.COVID-19.scenario13.agent.TSchoolchildMoveRole;
import soars2.model.COVID-19.scenario13.agent.TStudentMoveRole;
import soars2.model.COVID-19.scenario13.agent.TYoungMoveRole;
import soars2.model.COVID-19.scenario13.spot.TModelRole;
import soars2.utils.random2013.ICRandom;

/**
 * 
 */
public class TInitAgent2 {

	// Entities : [middle]

	public static boolean init1(TAgent agent, TSpot spot, TMiddleMoveRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// condition : probability : <model>askEquip pbig=
		if (rand.nextDouble()<=modelRole.getpbig()) {
			// command : list : <model>moveToRandomOne bigoffices
			if ( null != spot)
				agent.moveTo(((TSpot)(modelRole.getbigoffices().get( rand.nextInt(modelRole.getbigoffices().size())))).getName(), spotManager.getSpotDB());
			else {
				spot = (TSpot)modelRole.getbigoffices().get( rand.nextInt(modelRole.getbigoffices().size()));
				agent.initializeCurrentSpot(spot.getName(), spotManager.getSpotDB());
			}
			// command : list : addFirstSpot office
			ownerRole.getoffice().add(0,spotManager.getSpotDB().get(agent.getCurrentSpotName()));
			return true;
		}
		return false;
	}

	public static boolean init2(TAgent agent, TSpot spot, TMiddleMoveRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// condition : probability : <model>askEquip pmiddle=
		if (rand.nextDouble()<=modelRole.getpmiddle()) {
			// command : list : <model>moveToRandomOne middleoffices
			if ( null != spot)
				agent.moveTo(((TSpot)(modelRole.getmiddleoffices().get( rand.nextInt(modelRole.getmiddleoffices().size())))).getName(), spotManager.getSpotDB());
			else {
				spot = (TSpot)modelRole.getmiddleoffices().get( rand.nextInt(modelRole.getmiddleoffices().size()));
				agent.initializeCurrentSpot(spot.getName(), spotManager.getSpotDB());
			}
			// command : list : addFirstSpot office
			ownerRole.getoffice().add(0,spotManager.getSpotDB().get(agent.getCurrentSpotName()));
			return true;
		}
		return false;
	}

	public static boolean init3(TAgent agent, TSpot spot, TMiddleMoveRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : list : <model>moveToRandomOne smalloffices
		if ( null != spot)
			agent.moveTo(((TSpot)(modelRole.getsmalloffices().get( rand.nextInt(modelRole.getsmalloffices().size())))).getName(), spotManager.getSpotDB());
		else {
			spot = (TSpot)modelRole.getsmalloffices().get( rand.nextInt(modelRole.getsmalloffices().size()));
			agent.initializeCurrentSpot(spot.getName(), spotManager.getSpotDB());
		}
		// command : list : addFirstSpot office
		ownerRole.getoffice().add(0,spotManager.getSpotDB().get(agent.getCurrentSpotName()));
		return true;
	}

	// Entities : [schoolchild]

	public static boolean init4(TAgent agent, TSpot spot, TSchoolchildMoveRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : list : <model>moveToFirst primaryschools
		if ( null != spot)
			agent.moveTo(((TSpot)(modelRole.getprimaryschools().get(0))).getName(), spotManager.getSpotDB());
		else {
			spot = (TSpot)modelRole.getprimaryschools().get(0);
			agent.initializeCurrentSpot(spot.getName(), spotManager.getSpotDB());
		}
		// command : list : <model>rotateAll primaryschools=-1
		Collections.rotate(modelRole.getprimaryschools(), 1);
		// command : list : addFirstSpot school
		ownerRole.getschool().add(0,spotManager.getSpotDB().get(agent.getCurrentSpotName()));
		return true;
	}

	// Entities : [young]

	public static boolean init5(TAgent agent, TSpot spot, TYoungMoveRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// condition : probability : <model>askEquip pbig=
		if (rand.nextDouble()<=modelRole.getpbig()) {
			// command : list : <model>moveToRandomOne bigoffices
			if ( null != spot)
				agent.moveTo(((TSpot)(modelRole.getbigoffices().get( rand.nextInt(modelRole.getbigoffices().size())))).getName(), spotManager.getSpotDB());
			else {
				spot = (TSpot)modelRole.getbigoffices().get( rand.nextInt(modelRole.getbigoffices().size()));
				agent.initializeCurrentSpot(spot.getName(), spotManager.getSpotDB());
			}
			// command : list : addFirstSpot office
			ownerRole.getoffice().add(0,spotManager.getSpotDB().get(agent.getCurrentSpotName()));
			return true;
		}
		return false;
	}

	public static boolean init6(TAgent agent, TSpot spot, TYoungMoveRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// condition : probability : <model>askEquip pmiddle=
		if (rand.nextDouble()<=modelRole.getpmiddle()) {
			// command : list : <model>moveToRandomOne middleoffices
			if ( null != spot)
				agent.moveTo(((TSpot)(modelRole.getmiddleoffices().get( rand.nextInt(modelRole.getmiddleoffices().size())))).getName(), spotManager.getSpotDB());
			else {
				spot = (TSpot)modelRole.getmiddleoffices().get( rand.nextInt(modelRole.getmiddleoffices().size()));
				agent.initializeCurrentSpot(spot.getName(), spotManager.getSpotDB());
			}
			// command : list : addFirstSpot office
			ownerRole.getoffice().add(0,spotManager.getSpotDB().get(agent.getCurrentSpotName()));
			return true;
		}
		return false;
	}

	public static boolean init7(TAgent agent, TSpot spot, TYoungMoveRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : list : <model>moveToRandomOne smalloffices
		if ( null != spot)
			agent.moveTo(((TSpot)(modelRole.getsmalloffices().get( rand.nextInt(modelRole.getsmalloffices().size())))).getName(), spotManager.getSpotDB());
		else {
			spot = (TSpot)modelRole.getsmalloffices().get( rand.nextInt(modelRole.getsmalloffices().size()));
			agent.initializeCurrentSpot(spot.getName(), spotManager.getSpotDB());
		}
		// command : list : addFirstSpot office
		ownerRole.getoffice().add(0,spotManager.getSpotDB().get(agent.getCurrentSpotName()));
		return true;
	}

	// Entities : [student]

	public static boolean init8(TAgent agent, TSpot spot, TStudentMoveRole ownerRole, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : list : <model>moveToFirst highschools
		if ( null != spot)
			agent.moveTo(((TSpot)(modelRole.gethighschools().get(0))).getName(), spotManager.getSpotDB());
		else {
			spot = (TSpot)modelRole.gethighschools().get(0);
			agent.initializeCurrentSpot(spot.getName(), spotManager.getSpotDB());
		}
		// command : list : <model>rotateAll highschools=-1
		Collections.rotate(modelRole.gethighschools(), 1);
		// command : list : addFirstSpot school
		ownerRole.getschool().add(0,spotManager.getSpotDB().get(agent.getCurrentSpotName()));
		return true;
	}
}
