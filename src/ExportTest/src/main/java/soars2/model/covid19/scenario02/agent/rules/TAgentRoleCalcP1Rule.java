package soars2.model.covid19.scenario02.agent.rules;

import java.util.HashMap;
import soars2.core.TAgent;
import soars2.core.TAgentRule;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.covid19.scenario02.agent.TAgentRole;
import soars2.model.covid19.scenario02.spot.TBigofficeRole;

/**
 * 
 */
public class TAgentRoleCalcP1Rule extends TAgentRule {

	/** ルール名 */
	public static String RULE_NAME = "AgentRoleCalcP1";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TAgentRoleCalcP1Rule(TRole ownerRole) {
		super(RULE_NAME, ownerRole, null); // スポット条件なし
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
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		TSpot currentSpot = spotSet.get(getAgent().getCurrentSpotName()); // 現在いるスポット;
		TBigofficeRole currentSpotRole = (TBigofficeRole) currentSpot.getRole(); // 現在いるスポットの役割;
		// command : getequip : <>getEquip SHLAP=DRSCL ; cloneEquip SHLAP
		ownerRole.setSHLAP(currentSpotRole.getDRSCL());
		// command : substitution : askEquip SHLAP=SHLAP*ACPF
		ownerRole.setSHLAP((ownerRole.getSHLAP())*(ownerRole.getACPF()));
		// command : substitution : askEquip ACL=SHLAP+ACL*AgentAF
		ownerRole.setACL((ownerRole.getSHLAP())+(ownerRole.getACL())*(ownerRole.getAgentAF()));
		// command : substitution : askEquip P=1-exp(-a*PC*ACL)
		ownerRole.setP(1-Math.exp(-(ownerRole.geta())*(ownerRole.getPC())*(ownerRole.getACL())));
		return true;
	}
}
