package soars2.model.covid19.scenario02.spot.rules;

import java.util.HashMap;
import soars2.core.TAgent;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.covid19.scenario02.TSpotTypes;
import soars2.model.covid19.scenario02.spot.TModelRole;

/**
 * 
 */
public class TModelRoleCheck1Rule extends TSpotRule {

	/** ルール名 */
	public static String RULE_NAME = "ModelRoleCheck1";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TModelRoleCheck1Rule(TRole ownerRole) {
		super(RULE_NAME, ownerRole);
	}

	@Override
	public boolean doIt(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		// If  NumberVariable : <model>patientFlowOver1 == 0  then Others : terminate を使うか検討。
		if (method1(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		return false;
	}

	/**
	 * If  NumberVariable : <model>patientFlowOver1 == 0  then Others : terminate を使うか検討。
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
		// condition : numberobject : <model>askEquip patientFlowOver1==0
		if (modelRole.getpatientFlowOver1()==0) {
			return true;
		}
		return false;
	}
}
