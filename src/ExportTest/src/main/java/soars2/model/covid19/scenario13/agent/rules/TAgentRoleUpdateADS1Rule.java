package soars2.model.covid19.scenario13.agent.rules;

import java.util.HashMap;
import soars2.core.TAgent;
import soars2.core.TAgentRule;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.covid19.scenario13.TSpotTypes;
import soars2.model.covid19.scenario13.agent.TAgentRole;
import soars2.model.covid19.scenario13.spot.TModelRole;

/**
 * 
 */
public class TAgentRoleUpdateADS1Rule extends TAgentRule {

	/** ルール名 */
	public static String RULE_NAME = "AgentRoleUpdateADS1";

	/**
	 * コンストラクタ
	 * 
	 * @param ownerRole
	 */
	public TAgentRoleUpdateADS1Rule(TRole ownerRole) {
		super(RULE_NAME, ownerRole, null); // スポット条件なし
	}

	@Override
	public boolean doIt(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		if (!meetSpotCondition())
			return false;
		// 天然痘からコロナ用に１の期間5日にした 標準発症５日でそれ以上は２がディテクトできないとみなす。
		if (method1(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 天然痘からコロナ用に１の期間5日にした 標準発症５日でそれ以上は２がディテクトできないとみなす。
		else if (method2(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 天然痘からコロナ用に１の期間5日にした 標準発症５日でそれ以上は２がディテクトできないとみなす。
		else if (method3(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 天然痘からコロナ用に１の期間5日にした 標準発症５日でそれ以上は２がディテクトできないとみなす。
		else if (method4(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 天然痘からコロナ用に１の期間5日にした 標準発症５日でそれ以上は２がディテクトできないとみなす。
		else if (method5(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 天然痘からコロナ用に１の期間5日にした 標準発症５日でそれ以上は２がディテクトできないとみなす。
		else if (method6(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method7(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// ここでタイマーになっていなければ何もしないでスキップするタイマー機能
		else if (method8(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// レベル２の期間を５日にセットした,patientonbed start from 2
		else if (method9(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// P12mルートでレベル１をここに遷移しても引く
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
		// 
		else if (method14(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method15(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method16(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method17(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method18(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		// 
		else if (method19(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))
			return true;
		return false;
	}

	/**
	 * 天然痘からコロナ用に１の期間5日にした 標準発症５日でそれ以上は２がディテクトできないとみなす。
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
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=0
		if (ownerRole.getADS().equals("0")
			// && condition : numberobject : !askEquip P=<random()
			&& ownerRole.getP()>getRandom().nextDouble()
			// && condition : keyword : is age=baby
			&& ownerRole.getage().equals("baby")) {
			// command : keyword : set ADS=1
			ownerRole.setADS("1");
			// command : time : setTime timer=timer ; setTime timer+=5/0:00
			ownerRole.settimer(new TTime(currentTime).add("5/0:00"));
			// command : substitution : <model>askEquip infected=infected+1
			modelRole.setinfected(modelRole.getinfected()+1);
			// command : substitution : <model>askEquip patientpluusdead=patientpluusdead+1
			modelRole.setpatientpluusdead(modelRole.getpatientpluusdead()+1);
			// command : substitution : <model>askEquip naive=naive-1
			modelRole.setnaive(modelRole.getnaive()-1);
			// command : collection : <model>removeAgent candidate
			modelRole.getcandidate().remove(getAgent());
			// command : substitution : <model>askEquip InfectionLevel1=InfectionLevel1+1
			modelRole.setInfectionLevel1(modelRole.getInfectionLevel1()+1);
			// command : substitution : <model>askEquip infectedBaby=infectedBaby+1
			modelRole.setinfectedBaby(modelRole.getinfectedBaby()+1);
			return true;
		}
		return false;
	}

	/**
	 * 天然痘からコロナ用に１の期間5日にした 標準発症５日でそれ以上は２がディテクトできないとみなす。
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
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=0
		if (ownerRole.getADS().equals("0")
			// && condition : numberobject : !askEquip P=<random()
			&& ownerRole.getP()>getRandom().nextDouble()
			// && condition : keyword : is age=schoolchild
			&& ownerRole.getage().equals("schoolchild")) {
			// command : keyword : set ADS=1
			ownerRole.setADS("1");
			// command : time : setTime timer=timer ; setTime timer+=5/0:00
			ownerRole.settimer(new TTime(currentTime).add("5/0:00"));
			// command : substitution : <model>askEquip infected=infected+1
			modelRole.setinfected(modelRole.getinfected()+1);
			// command : substitution : <model>askEquip patientpluusdead=patientpluusdead+1
			modelRole.setpatientpluusdead(modelRole.getpatientpluusdead()+1);
			// command : substitution : <model>askEquip naive=naive-1
			modelRole.setnaive(modelRole.getnaive()-1);
			// command : collection : <model>removeAgent candidate
			modelRole.getcandidate().remove(getAgent());
			// command : substitution : <model>askEquip InfectionLevel1=InfectionLevel1+1
			modelRole.setInfectionLevel1(modelRole.getInfectionLevel1()+1);
			// command : substitution : <model>askEquip infectedSchoolchild=infectedSchoolchild+1
			modelRole.setinfectedSchoolchild(modelRole.getinfectedSchoolchild()+1);
			return true;
		}
		return false;
	}

	/**
	 * 天然痘からコロナ用に１の期間5日にした 標準発症５日でそれ以上は２がディテクトできないとみなす。
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
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=0
		if (ownerRole.getADS().equals("0")
			// && condition : numberobject : !askEquip P=<random()
			&& ownerRole.getP()>getRandom().nextDouble()
			// && condition : keyword : is age=student
			&& ownerRole.getage().equals("student")) {
			// command : keyword : set ADS=1
			ownerRole.setADS("1");
			// command : time : setTime timer=timer ; setTime timer+=5/0:00
			ownerRole.settimer(new TTime(currentTime).add("5/0:00"));
			// command : substitution : <model>askEquip infected=infected+1
			modelRole.setinfected(modelRole.getinfected()+1);
			// command : substitution : <model>askEquip patientpluusdead=patientpluusdead+1
			modelRole.setpatientpluusdead(modelRole.getpatientpluusdead()+1);
			// command : substitution : <model>askEquip naive=naive-1
			modelRole.setnaive(modelRole.getnaive()-1);
			// command : collection : <model>removeAgent candidate
			modelRole.getcandidate().remove(getAgent());
			// command : substitution : <model>askEquip InfectionLevel1=InfectionLevel1+1
			modelRole.setInfectionLevel1(modelRole.getInfectionLevel1()+1);
			// command : substitution : <model>askEquip infectedStudent=infectedStudent+1
			modelRole.setinfectedStudent(modelRole.getinfectedStudent()+1);
			return true;
		}
		return false;
	}

	/**
	 * 天然痘からコロナ用に１の期間5日にした 標準発症５日でそれ以上は２がディテクトできないとみなす。
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
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=0
		if (ownerRole.getADS().equals("0")
			// && condition : numberobject : !askEquip P=<random()
			&& ownerRole.getP()>getRandom().nextDouble()
			// && condition : keyword : is age=young
			&& ownerRole.getage().equals("young")) {
			// command : keyword : set ADS=1
			ownerRole.setADS("1");
			// command : time : setTime timer=timer ; setTime timer+=5/0:00
			ownerRole.settimer(new TTime(currentTime).add("5/0:00"));
			// command : substitution : <model>askEquip infected=infected+1
			modelRole.setinfected(modelRole.getinfected()+1);
			// command : substitution : <model>askEquip patientpluusdead=patientpluusdead+1
			modelRole.setpatientpluusdead(modelRole.getpatientpluusdead()+1);
			// command : substitution : <model>askEquip naive=naive-1
			modelRole.setnaive(modelRole.getnaive()-1);
			// command : collection : <model>removeAgent candidate
			modelRole.getcandidate().remove(getAgent());
			// command : substitution : <model>askEquip InfectionLevel1=InfectionLevel1+1
			modelRole.setInfectionLevel1(modelRole.getInfectionLevel1()+1);
			// command : substitution : <model>askEquip infectedYoung=infectedYoung+1
			modelRole.setinfectedYoung(modelRole.getinfectedYoung()+1);
			return true;
		}
		return false;
	}

	/**
	 * 天然痘からコロナ用に１の期間5日にした 標準発症５日でそれ以上は２がディテクトできないとみなす。
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
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=0
		if (ownerRole.getADS().equals("0")
			// && condition : numberobject : !askEquip P=<random()
			&& ownerRole.getP()>getRandom().nextDouble()
			// && condition : keyword : is age=middle
			&& ownerRole.getage().equals("middle")) {
			// command : keyword : set ADS=1
			ownerRole.setADS("1");
			// command : time : setTime timer=timer ; setTime timer+=5/0:00
			ownerRole.settimer(new TTime(currentTime).add("5/0:00"));
			// command : substitution : <model>askEquip infected=infected+1
			modelRole.setinfected(modelRole.getinfected()+1);
			// command : substitution : <model>askEquip patientpluusdead=patientpluusdead+1
			modelRole.setpatientpluusdead(modelRole.getpatientpluusdead()+1);
			// command : substitution : <model>askEquip naive=naive-1
			modelRole.setnaive(modelRole.getnaive()-1);
			// command : collection : <model>removeAgent candidate
			modelRole.getcandidate().remove(getAgent());
			// command : substitution : <model>askEquip InfectionLevel1=InfectionLevel1+1
			modelRole.setInfectionLevel1(modelRole.getInfectionLevel1()+1);
			// command : substitution : <model>askEquip infectedMiddle=infectedMiddle+1
			modelRole.setinfectedMiddle(modelRole.getinfectedMiddle()+1);
			return true;
		}
		return false;
	}

	/**
	 * 天然痘からコロナ用に１の期間5日にした 標準発症５日でそれ以上は２がディテクトできないとみなす。
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
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=0
		if (ownerRole.getADS().equals("0")
			// && condition : numberobject : !askEquip P=<random()
			&& ownerRole.getP()>getRandom().nextDouble()
			// && condition : keyword : is age=old
			&& ownerRole.getage().equals("old")) {
			// command : keyword : set ADS=1
			ownerRole.setADS("1");
			// command : time : setTime timer=timer ; setTime timer+=5/0:00
			ownerRole.settimer(new TTime(currentTime).add("5/0:00"));
			// command : substitution : <model>askEquip infected=infected+1
			modelRole.setinfected(modelRole.getinfected()+1);
			// command : substitution : <model>askEquip patientpluusdead=patientpluusdead+1
			modelRole.setpatientpluusdead(modelRole.getpatientpluusdead()+1);
			// command : substitution : <model>askEquip naive=naive-1
			modelRole.setnaive(modelRole.getnaive()-1);
			// command : collection : <model>removeAgent candidate
			modelRole.getcandidate().remove(getAgent());
			// command : substitution : <model>askEquip InfectionLevel1=InfectionLevel1+1
			modelRole.setInfectionLevel1(modelRole.getInfectionLevel1()+1);
			// command : substitution : <model>askEquip infectedOld=infectedOld+1
			modelRole.setinfectedOld(modelRole.getinfectedOld()+1);
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
		// condition : keyword : is ADS=0
		if (ownerRole.getADS().equals("0")
			// || condition : keyword : is ADS=0i
			|| (ownerRole.getADS().equals("0i")
			// || condition : keyword : is ADS=D
			|| ownerRole.getADS().equals("D"))) {
			return true;
		}
		return false;
	}

	/**
	 * ここでタイマーになっていなければ何もしないでスキップするタイマー機能
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
		// condition : time : !isTime timer
		if (currentTime.isLessThan(ownerRole.gettimer())) {
			return true;
		}
		return false;
	}

	/**
	 * レベル２の期間を５日にセットした,patientonbed start from 2
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
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=1
		if (ownerRole.getADS().equals("1")
			// && condition : probability : askEquip p12=
			&& getRandom().nextDouble()<=ownerRole.getp12()) {
			// command : keyword : set ADS=2
			ownerRole.setADS("2");
			// command : time : setTime timer=timer ; setTime timer+=5/0:00
			ownerRole.settimer(new TTime(currentTime).add("5/0:00"));
			// command : substitution : <model>askEquip InfectionLevel2=InfectionLevel2+1
			modelRole.setInfectionLevel2(modelRole.getInfectionLevel2()+1);
			// command : substitution : <model>askEquip InfectionLevel1=InfectionLevel1-1
			modelRole.setInfectionLevel1(modelRole.getInfectionLevel1()-1);
			return true;
		}
		return false;
	}

	/**
	 * P12mルートでレベル１をここに遷移しても引く
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
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=1
		if (ownerRole.getADS().equals("1")) {
			// command : keyword : set ADS=2m
			ownerRole.setADS("2m");
			// command : time : setTime timer=timer ; setTime timer+=5/0:00
			ownerRole.settimer(new TTime(currentTime).add("5/0:00"));
			// command : substitution : <model>askEquip InfectionLevel2m=InfectionLevel2m+1
			modelRole.setInfectionLevel2m(modelRole.getInfectionLevel2m()+1);
			// command : substitution : <model>askEquip InfectionLevel1=InfectionLevel1-1
			modelRole.setInfectionLevel1(modelRole.getInfectionLevel1()-1);
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
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=2
		if (ownerRole.getADS().equals("2")
			// && condition : probability : askEquip p23=
			&& getRandom().nextDouble()<=ownerRole.getp23()) {
			// command : keyword : set ADS=3
			ownerRole.setADS("3");
			// command : time : setTime timer=timer ; setTime timer+=3/0:00
			ownerRole.settimer(new TTime(currentTime).add("3/0:00"));
			// command : substitution : <model>askEquip InfectionLevel2=InfectionLevel2-1
			modelRole.setInfectionLevel2(modelRole.getInfectionLevel2()-1);
			// command : substitution : <model>askEquip InfectionLevel3=InfectionLevel3+1
			modelRole.setInfectionLevel3(modelRole.getInfectionLevel3()+1);
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
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=2
		if (ownerRole.getADS().equals("2")) {
			// command : keyword : set ADS=3s
			ownerRole.setADS("3s");
			// command : time : setTime timer=timer ; setTime timer+=3/0:00
			ownerRole.settimer(new TTime(currentTime).add("3/0:00"));
			// command : substitution : <model>askEquip InfectionLevel2=InfectionLevel2-1
			modelRole.setInfectionLevel2(modelRole.getInfectionLevel2()-1);
			// command : substitution : <model>askEquip InfectionLevel3s=InfectionLevel3s+1
			modelRole.setInfectionLevel3s(modelRole.getInfectionLevel3s()+1);
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
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=2m
		if (ownerRole.getADS().equals("2m")) {
			// command : keyword : set ADS=3m
			ownerRole.setADS("3m");
			// command : time : setTime timer=timer ; setTime timer+=3/0:00
			ownerRole.settimer(new TTime(currentTime).add("3/0:00"));
			// command : substitution : <model>askEquip InfectionLevel2m=InfectionLevel2m-1
			modelRole.setInfectionLevel2m(modelRole.getInfectionLevel2m()-1);
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
	private boolean method14(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		// condition : keyword : is ADS=3
		if (ownerRole.getADS().equals("3")
			// || condition : keyword : is ADS=3m
			|| (ownerRole.getADS().equals("3m")
			// || condition : keyword : is ADS=4m
			|| ownerRole.getADS().equals("4m"))) {
			// command : keyword : set ADS=5
			ownerRole.setADS("5");
			// command : time : setTime timer=timer ; setTime timer+=3/0:00
			ownerRole.settimer(new TTime(currentTime).add("3/0:00"));
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
	private boolean method15(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=3s
		if (ownerRole.getADS().equals("3s")
			// && condition : probability : askEquip p3s4m=
			&& getRandom().nextDouble()<=ownerRole.getp3s4m()) {
			// command : keyword : set ADS=4m
			ownerRole.setADS("4m");
			// command : time : setTime timer=timer ; setTime timer+=3/0:00
			ownerRole.settimer(new TTime(currentTime).add("3/0:00"));
			// command : substitution : <model>askEquip InfectionLevel3s=InfectionLevel3s-1
			modelRole.setInfectionLevel3s(modelRole.getInfectionLevel3s()-1);
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
	private boolean method16(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=3s
		if (ownerRole.getADS().equals("3s")) {
			// command : keyword : set ADS=4c
			ownerRole.setADS("4c");
			// command : time : setTime timer=timer ; setTime timer+=5/0:00
			ownerRole.settimer(new TTime(currentTime).add("5/0:00"));
			// command : substitution : <model>askEquip InfectionLevel4c=InfectionLevel4c+1
			modelRole.setInfectionLevel4c(modelRole.getInfectionLevel4c()+1);
			// command : substitution : <model>askEquip InfectionLevel3s=InfectionLevel3s-1
			modelRole.setInfectionLevel3s(modelRole.getInfectionLevel3s()-1);
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
	private boolean method17(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=4c
		if (ownerRole.getADS().equals("4c")
			// && condition : probability : askEquip p4cd=
			&& getRandom().nextDouble()<=ownerRole.getp4cd()) {
			// command : keyword : set ADS=D
			ownerRole.setADS("D");
			// command : substitution : <model>askEquip dead=dead+1
			modelRole.setdead(modelRole.getdead()+1);
			// command : substitution : <model>askEquip InfectionLevel4c=InfectionLevel4c-1
			modelRole.setInfectionLevel4c(modelRole.getInfectionLevel4c()-1);
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
	private boolean method18(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=4c
		if (ownerRole.getADS().equals("4c")) {
			// command : keyword : set ADS=4m
			ownerRole.setADS("4m");
			// command : substitution : <model>askEquip InfectionLevel4c=InfectionLevel4c-1
			modelRole.setInfectionLevel4c(modelRole.getInfectionLevel4c()-1);
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
	private boolean method19(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,
		HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {
		TAgentRole ownerRole = (TAgentRole) getOwnerRole();
		TModelRole modelRole = (TModelRole) spotSet.get(TSpotTypes.MODEL).getRole();
		// condition : keyword : is ADS=5
		if (ownerRole.getADS().equals("5")) {
			// command : keyword : set ADS=0i
			ownerRole.setADS("0i");
			// command : substitution : <model>askEquip patientpluusdead=patientpluusdead-1
			modelRole.setpatientpluusdead(modelRole.getpatientpluusdead()-1);
			return true;
		}
		return false;
	}
}
