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
public class TModelRoleFeedback21Rule extends TSpotRule {

	/** ルール名 */
	public static String RULE_NAME = "ModelRoleFeedback21";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TModelRoleFeedback21Rule(TRole ownerRole) {
		super(RULE_NAME, ownerRole);
	}

	@Override
	public boolean doIt(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		// 
		if (method1(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method2(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
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
		// condition : numberobject : <model>askEquip patientFlowOver2=>bednumforOSD+100
		if (modelRole.getpatientFlowOver2()>=modelRole.getbednumforOSD()+100) {
			// command : keyword : <model>set officeclose=yes
			modelRole.setofficeclose("yes");
			return true;
		}
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
	private boolean method2(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// command : keyword : <model>set officeclose=no
		modelRole.setofficeclose("no");
		return true;
	}
}
