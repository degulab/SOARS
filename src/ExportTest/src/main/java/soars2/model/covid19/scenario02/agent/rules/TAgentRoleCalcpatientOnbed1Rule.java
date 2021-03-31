package soars2.model.covid19.scenario02.agent.rules;

import java.util.HashMap;
import soars2.core.TAgent;
import soars2.core.TAgentRule;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.covid19.scenario02.TSpotTypes;
import soars2.model.covid19.scenario02.spot.TModelRole;

/**
 * 
 */
public class TAgentRoleCalcpatientOnbed1Rule extends TAgentRule {

	/** ルール名 */
	public static String RULE_NAME = "AgentRoleCalcpatientOnbed1";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TAgentRoleCalcpatientOnbed1Rule(TRole ownerRole) {
		super(RULE_NAME, ownerRole, null); // スポット条件なし
	}

	@Override
	public boolean doIt(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		if (!meetSpotCondition())
			return false;
		// 2m,3m not reduced
		if (method1(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		return false;
	}

	/**
	 * 2m,3m not reduced
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
		// command : substitution : <model>askEquip patientFlowOver1=patientpluusdead-dead
		modelRole.setpatientFlowOver1(modelRole.getpatientpluusdead()-modelRole.getdead());
		// command : substitution : <model>askEquip patientFlowOver2=patientFlowOver1-InfectionLevel1
		modelRole.setpatientFlowOver2(modelRole.getpatientFlowOver1()-modelRole.getInfectionLevel1());
		// command : substitution : <model>askEquip patient_severe_critical=InfectionLevel3s+InfectionLevel4c
		modelRole.setpatient_severe_critical(modelRole.getInfectionLevel3s()+modelRole.getInfectionLevel4c());
		return true;
	}
}
