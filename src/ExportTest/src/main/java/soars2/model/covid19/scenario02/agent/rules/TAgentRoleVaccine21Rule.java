package soars2.model.covid19.scenario02.agent.rules;

import java.util.HashMap;
import soars2.core.TAgent;
import soars2.core.TAgentRule;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.covid19.scenario02.TSpotTypes;
import soars2.model.covid19.scenario02.agent.TAgentRole;
import soars2.model.covid19.scenario02.spot.TModelRole;

/**
 * 
 */
public class TAgentRoleVaccine21Rule extends TAgentRule {

	/** ルール名 */
	public static String RULE_NAME = "AgentRoleVaccine21";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TAgentRoleVaccine21Rule(TRole ownerRole) {
		super(RULE_NAME, ownerRole, null); // スポット条件なし
	}

	@Override
	public boolean doIt(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		if (!meetSpotCondition())
			return false;
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
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		// condition : collection : <model>containsAgent candidate
		if (modelRole.getcandidate().contains(getAgent())) {
			// command : substitution : <model>askEquip v_candidate=v_candidate-1
			modelRole.setv_candidate(modelRole.getv_candidate()-1);
			// condition : numberobject : <model>askEquip vaccinated=<maxvactine
			if (modelRole.getvaccinated()<=modelRole.getmaxvactine()
				// && condition : numberobject : <model>askEquip daycount=>vacdelay
				&& modelRole.getdaycount()>=modelRole.getvacdelay()) {
				// command : collection : <model>removeAgent candidate
				modelRole.getcandidate().remove(getAgent());
				// command : substitution : <model>askEquip v_ability=v_ability-1
				modelRole.setv_ability(modelRole.getv_ability()-1);
				// command : substitution : askEquip PC=0
				ownerRole.setPC(0);
				// command : substitution : <model>askEquip vaccinated=vaccinated+1
				modelRole.setvaccinated(modelRole.getvaccinated()+1);
				return true;
			}
		}
		return false;
	}
}
