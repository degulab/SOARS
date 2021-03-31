package soars2.model.covid19.scenario13.agent.rules;

import java.util.HashMap;
import soars2.core.TAgent;
import soars2.core.TAgentRule;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.covid19.scenario13.TSpotTypes;
import soars2.model.covid19.scenario13.agent.TAgentRole;
import soars2.model.covid19.scenario13.spot.TModelRole;

/**
 * 
 */
public class TAgentRoleCheck1Rule extends TAgentRule {

	/** ルール名 */
	public static String RULE_NAME = "AgentRoleCheck1";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TAgentRoleCheck1Rule(TRole ownerRole) {
		super(RULE_NAME, ownerRole, null); // スポット条件なし
	}

	@Override
	public boolean doIt(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		if (!meetSpotCondition())
			return false;
		// ここで一旦感染治っても再感染させる　If  NumberVariable : <model>patientFlowOver1 == 0  Collection : <model>containsAgent initia_linfected then 再感染
		if (method1(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		return false;
	}

	/**
	 * ここで一旦感染治っても再感染させる　If  NumberVariable : <model>patientFlowOver1 == 0  Collection : <model>containsAgent initia_linfected then 再感染
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
		// condition : numberobject : <model>askEquip patientFlowOver1==0
		if (modelRole.getpatientFlowOver1()==0
			// && condition : collection : <model>containsAgent initia_linfected
			&& modelRole.getinitia_linfected().contains(getAgent())) {
			// command : time : setTime timer=timer ; setTime timer+=7/0:00
			ownerRole.settimer(new TTime(currentTime).add("7/0:00"));
			// command : keyword : set ADS=1
			ownerRole.setADS("1");
			return true;
		}
		return false;
	}
}
