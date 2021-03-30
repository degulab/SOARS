package soars2.model.COVID-19.scenario13.agent.rules;

import java.util.ArrayList;
import java.util.HashMap;
import soars2.core.TAgent;
import soars2.core.TAgentRule;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.COVID-19.scenario13.agent.TMiddleMoveRole;
import soars2.model.COVID-19.scenario13.agent.TYoungMoveRole;

/**
 * 
 */
public class TMiddleMoveRoleMove9Rule extends TAgentRule {

	/** ルール名 */
	public static String RULE_NAME = "MiddleMoveRoleMove9";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TMiddleMoveRoleMove9Rule(TRole ownerRole) {
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
		// command : list : moveToFirst office
		getAgent().moveTo(((TSpot)(getoffice().get(0))).getName(), spotSet);
		return true;
	}
}
