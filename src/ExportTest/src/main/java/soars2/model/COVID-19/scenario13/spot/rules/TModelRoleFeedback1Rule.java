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
public class TModelRoleFeedback1Rule extends TSpotRule {

	/** ルール名 */
	public static String RULE_NAME = "ModelRoleFeedback1";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TModelRoleFeedback1Rule(TRole ownerRole) {
		super(RULE_NAME, ownerRole);
	}

	@Override
	public boolean doIt(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		// フィードバックはベット数を患者数が超えると学校閉鎖生じる。以下になると自動的に閉鎖ノーになる
		if (method1(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method2(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		return false;
	}

	/**
	 * フィードバックはベット数を患者数が超えると学校閉鎖生じる。以下になると自動的に閉鎖ノーになる
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
		// condition : numberobject : <model>askEquip patientFlowOver2=>bednumforSSD
		if (modelRole.getpatientFlowOver2()>=modelRole.getbednumforSSD()) {
			// command : keyword : <model>set schoolclose=yes
			modelRole.setschoolclose("yes");
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
		// command : keyword : <model>set schoolclose=no
		modelRole.setschoolclose("no");
		return true;
	}
}
