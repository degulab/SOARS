package soars2.model.covid19.scenario13.spot.rules;

import java.util.HashMap;
import soars2.core.TAgent;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.covid19.scenario13.spot.TBigofficeRole;

/**
 * 
 */
public class TBigofficeRoleClearAHL1Rule extends TSpotRule {

	/** ルール名 */
	public static String RULE_NAME = "BigofficeRoleClearAHL1";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TBigofficeRoleClearAHL1Rule(TRole ownerRole) {
		super(RULE_NAME, ownerRole);
	}

	@Override
	public boolean doIt(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
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
		TBigofficeRole currentSpotRole = (TBigofficeRole) getOwnerRole();
		// command : substitution : <>askEquip AHL=0
		currentSpotRole.setAHL(0);
		return true;
	}
}
