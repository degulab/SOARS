package soars2.model.covid19.scenario02.agent.rules;

import java.util.ArrayList;
import java.util.HashMap;
import soars2.core.TAgent;
import soars2.core.TAgentRule;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.covid19.scenario02.agent.TSchoolchildMoveRole;
import soars2.model.covid19.scenario02.agent.TStudentMoveRole;

/**
 * 
 */
public class TStudentMoveRoleMove2Rule extends TAgentRule {

	/** ルール名 */
	public static String RULE_NAME = "StudentMoveRoleMove2";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TStudentMoveRoleMove2Rule(TRole ownerRole) {
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
		// command : list : moveToFirst school
		getAgent().moveTo(((TSpot)(getschool().get(0))).getName(), spotSet);
		return true;
	}
}
