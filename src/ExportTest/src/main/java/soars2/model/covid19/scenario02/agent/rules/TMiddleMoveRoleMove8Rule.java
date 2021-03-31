package soars2.model.covid19.scenario02.agent.rules;

import java.util.ArrayList;
import java.util.HashMap;
import soars2.core.TAgent;
import soars2.core.TAgentRule;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.covid19.scenario02.TSpotTypes;
import soars2.model.covid19.scenario02.agent.TMiddleMoveRole;
import soars2.model.covid19.scenario02.agent.TYoungMoveRole;
import soars2.model.covid19.scenario02.spot.TModelRole;

/**
 * 
 */
public class TMiddleMoveRoleMove8Rule extends TAgentRule {

	/** ルール名 */
	public static String RULE_NAME = "MiddleMoveRoleMove8";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TMiddleMoveRoleMove8Rule(TRole ownerRole) {
		super(RULE_NAME, ownerRole, null); // スポット条件なし
	}

	//
	// Getter and Setter
	//

	/**
	 * 
	 * return
	 */
	private ArrayList<Object> getoffice() {
		if (TMiddleMoveRole.class.isInstance( getOwnerRole()))
			return ((TMiddleMoveRole)getOwnerRole()).getoffice();
		else if (TYoungMoveRole.class.isInstance( getOwnerRole()))
			return ((TYoungMoveRole)getOwnerRole()).getoffice();
		else
			return null;
	}

	/**
	 * @ param foffice
	 */
	private void setoffice(ArrayList<Object> foffice) {
		if (TMiddleMoveRole.class.isInstance( getOwnerRole()))
			((TMiddleMoveRole)getOwnerRole()).setoffice(foffice);
		else if (TYoungMoveRole.class.isInstance( getOwnerRole()))
			((TYoungMoveRole)getOwnerRole()).setoffice(foffice);
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
