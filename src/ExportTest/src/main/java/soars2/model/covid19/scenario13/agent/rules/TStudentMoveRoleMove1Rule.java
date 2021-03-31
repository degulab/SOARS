package soars2.model.covid19.scenario13.agent.rules;

import java.util.ArrayList;
import java.util.HashMap;
import soars2.core.TAgent;
import soars2.core.TAgentRule;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.covid19.scenario13.TSpotTypes;
import soars2.model.covid19.scenario13.agent.TSchoolchildMoveRole;
import soars2.model.covid19.scenario13.agent.TStudentMoveRole;
import soars2.model.covid19.scenario13.spot.TModelRole;

/**
 * 
 */
public class TStudentMoveRoleMove1Rule extends TAgentRule {

	/** ルール名 */
	public static String RULE_NAME = "StudentMoveRoleMove1";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TStudentMoveRoleMove1Rule(TRole ownerRole) {
		super(RULE_NAME, ownerRole, null); // スポット条件なし
	}

	//
	// Getter and Setter
	//

	/**
	 * 
	 * return
	 */
	private ArrayList<Object> getschool() {
		if (TStudentMoveRole.class.isInstance( getOwnerRole()))
			return ((TStudentMoveRole)getOwnerRole()).getschool();
		else if (TSchoolchildMoveRole.class.isInstance( getOwnerRole()))
			return ((TSchoolchildMoveRole)getOwnerRole()).getschool();
		else
			return null;
	}

	/**
	 * @ param fschool
	 */
	private void setschool(ArrayList<Object> fschool) {
		if (TStudentMoveRole.class.isInstance( getOwnerRole()))
			((TStudentMoveRole)getOwnerRole()).setschool(fschool);
		else if (TSchoolchildMoveRole.class.isInstance( getOwnerRole()))
			((TSchoolchildMoveRole)getOwnerRole()).setschool(fschool);
	}

	@Override
	public boolean doIt(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		if (!meetSpotCondition())
			return false;
		// バグらしいのでデイカウントがワクチンディレイより大きいという条件を削除した
		if (method1(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		return false;
	}

	/**
	 * バグらしいのでデイカウントがワクチンディレイより大きいという条件を削除した
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
		// condition : keyword : <model>is schoolclose=yes
		if (modelRole.getschoolclose().equals("yes")) {
			return true;
		}
		return false;
	}
}
