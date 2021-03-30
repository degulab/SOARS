package soars2.model.COVID-19.scenario13.agent.rules;

import java.util.HashMap;
import soars2.core.TAgent;
import soars2.core.TAgentRule;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.COVID-19.scenario13.TSpotTypes;
import soars2.model.COVID-19.scenario13.spot.TModelRole;

/**
 * 
 */
public class TMiddleMoveRoleMove10Rule extends TAgentRule {

	/** ルール名 */
	public static String RULE_NAME = "MiddleMoveRoleMove10";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TMiddleMoveRoleMove10Rule(TRole ownerRole) {
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
		// command : list : <model>moveToRandomOne traffics
		getAgent().moveTo(((TSpot)(modelRole.gettraffics().get( getRandom().nextInt(modelRole.gettraffics().size())))).getName(), spotSet);
		return true;
	}
}
