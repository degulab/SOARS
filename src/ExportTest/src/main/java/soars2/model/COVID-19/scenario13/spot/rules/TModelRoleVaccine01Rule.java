package soars2.model.COVID-19.scenario13.spot.rules;

import java.util.HashMap;
import soars2.core.TAgent;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.COVID-19.scenario13.TSpotTypes;
import soars2.model.COVID-19.scenario13.spot.TModelRole;

/**
 * 
 */
public class TModelRoleVaccine01Rule extends TSpotRule {

	/** ルール名 */
	public static String RULE_NAME = "ModelRoleVaccine01";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TModelRoleVaccine01Rule(TRole ownerRole) {
		super(RULE_NAME, ownerRole);
	}

	@Override
	public boolean doIt(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		// 
		if (method1(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		return false;
	}

	/**
	 * 
	 * 
	 * @param currentTime
	 * @param currentStage
	 * @param spotSet
	 * @param agentSet
	 * @param globalSharedVariables
	 * return
	 */
	private boolean method1(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// command : substitution : <model>askEquip daycount=daycount+1
		modelRole.setdaycount(modelRole.getdaycount()+1);
		// command : substitution : <model>askEquip v_candidate=0
		modelRole.setv_candidate(0);
		// command : substitution : <model>askEquip v_ability=300
		modelRole.setv_ability(300);
		return true;
	}
}
