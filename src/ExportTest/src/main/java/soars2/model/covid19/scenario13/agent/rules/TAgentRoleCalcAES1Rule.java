package soars2.model.covid19.scenario13.agent.rules;

import java.util.HashMap;
import soars2.core.TAgent;
import soars2.core.TAgentRule;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.covid19.scenario13.agent.TAgentRole;

/**
 * 
 */
public class TAgentRoleCalcAES1Rule extends TAgentRule {

	/** ルール名 */
	public static String RULE_NAME = "AgentRoleCalcAES1";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TAgentRoleCalcAES1Rule(TRole ownerRole) {
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
		// 
		else if (method2(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method3(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method4(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method5(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method6(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method7(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method8(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method9(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method10(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method11(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method12(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method13(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
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
		// condition : keyword : is ADS=0
		if (ownerRole.getADS().equals("0")) {
			// command : substitution : askEquip AES=0
			ownerRole.setAES(0);
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
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		// condition : keyword : is ADS=0i
		if (ownerRole.getADS().equals("0i")) {
			// command : substitution : askEquip AES=0
			ownerRole.setAES(0);
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
	private boolean method3(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		// condition : keyword : is ADS=1
		if (ownerRole.getADS().equals("1")) {
			// command : substitution : askEquip AES=0
			ownerRole.setAES(0);
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
	private boolean method4(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		// condition : keyword : is ADS=2
		if (ownerRole.getADS().equals("2")) {
			// command : substitution : askEquip AES=0.6
			ownerRole.setAES(0.6);
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
	private boolean method5(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		// condition : keyword : is ADS=2m
		if (ownerRole.getADS().equals("2m")) {
			// command : substitution : askEquip AES=0.4
			ownerRole.setAES(0.4);
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
	private boolean method6(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		// condition : keyword : is ADS=3
		if (ownerRole.getADS().equals("3")) {
			// command : substitution : askEquip AES=0.8
			ownerRole.setAES(0.8);
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
	private boolean method7(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		// condition : keyword : is ADS=3m
		if (ownerRole.getADS().equals("3m")) {
			// command : substitution : askEquip AES=0.5
			ownerRole.setAES(0.5);
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
	private boolean method8(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		// condition : keyword : is ADS=3s
		if (ownerRole.getADS().equals("3s")) {
			// command : substitution : askEquip AES=0.6
			ownerRole.setAES(0.6);
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
	private boolean method9(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		// condition : keyword : is ADS=3p
		if (ownerRole.getADS().equals("3p")) {
			// command : substitution : askEquip AES=0.6
			ownerRole.setAES(0.6);
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
	private boolean method10(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		// condition : keyword : is ADS=4c
		if (ownerRole.getADS().equals("4c")) {
			// command : substitution : askEquip AES=0.5
			ownerRole.setAES(0.5);
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
	private boolean method11(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		// condition : keyword : is ADS=4m
		if (ownerRole.getADS().equals("4m")) {
			// command : substitution : askEquip AES=0.5
			ownerRole.setAES(0.5);
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
	private boolean method12(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		// condition : keyword : is ADS=5
		if (ownerRole.getADS().equals("5")) {
			// command : substitution : askEquip AES=0.2
			ownerRole.setAES(0.2);
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
	private boolean method13(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		// command : substitution : askEquip AES=0
		ownerRole.setAES(0);
		return true;
	}
}
