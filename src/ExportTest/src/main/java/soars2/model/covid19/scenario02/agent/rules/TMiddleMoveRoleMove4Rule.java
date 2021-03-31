package soars2.model.covid19.scenario02.agent.rules;

import java.util.ArrayList;
import java.util.HashMap;
import soars2.core.TAgent;
import soars2.core.TAgentRule;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.covid19.scenario02.TSpotTypes;
import soars2.model.covid19.scenario02.agent.TAgentRole;
import soars2.model.covid19.scenario02.agent.TMiddleMoveRole;
import soars2.model.covid19.scenario02.agent.TSchoolchildMoveRole;
import soars2.model.covid19.scenario02.agent.TStudentMoveRole;
import soars2.model.covid19.scenario02.agent.TYoungMoveRole;
import soars2.model.covid19.scenario02.spot.TModelRole;

/**
 * 
 */
public class TMiddleMoveRoleMove4Rule extends TAgentRule {

	/** ルール名 */
	public static String RULE_NAME = "MiddleMoveRoleMove4";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TMiddleMoveRoleMove4Rule(TRole ownerRole) {
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
		TAgentRole ownerRole = (TAgentRole) getOwnerRole().getMergedRole("AgentRole");
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=2
		if (ownerRole.getADS().equals("2")
			// && condition : probability : <model>askEquip piso2=
			&& getRandom().nextDouble()<=modelRole.getpiso2()) {
			// command : move : <hospital>moveTo
			getAgent().moveTo(TSpotTypes.HOSPITAL, spotSet);
			return true;
		}
		return false;
	}
}
