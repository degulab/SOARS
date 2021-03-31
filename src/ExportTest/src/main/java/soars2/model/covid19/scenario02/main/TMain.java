package soars2.model.covid19.scenario02.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import soars2.core.TAgent;
import soars2.core.TAgentLogger;
import soars2.core.TAgentManager;
import soars2.core.TRuleAggregator;
import soars2.core.TSpot;
import soars2.core.TSpotManager;
import soars2.core.TTime;
import soars2.model.covid19.scenario02.TAgentTypes;
import soars2.model.covid19.scenario02.TSpotTypes;
import soars2.model.covid19.scenario02.TStages;
import soars2.model.covid19.scenario02.agent.TAgentRole;
import soars2.model.covid19.scenario02.agent.TBabyMoveRole;
import soars2.model.covid19.scenario02.agent.TMiddleMoveRole;
import soars2.model.covid19.scenario02.agent.TSchoolchildMoveRole;
import soars2.model.covid19.scenario02.agent.TStudentMoveRole;
import soars2.model.covid19.scenario02.agent.TYoungMoveRole;
import soars2.model.covid19.scenario02.main.init.agent.TInitADS;
import soars2.model.covid19.scenario02.main.init.agent.TInitAgent;
import soars2.model.covid19.scenario02.main.init.agent.TInitAgent2;
import soars2.model.covid19.scenario02.main.init.agent.TInitVaccine;
import soars2.model.covid19.scenario02.main.init.spot.TInitSpot;
import soars2.model.covid19.scenario02.main.init.spot.TInitSpot2;
import soars2.model.covid19.scenario02.spot.TBigofficeRole;
import soars2.model.covid19.scenario02.spot.TModelRole;
import soars2.model.covid19.scenario02.utils.analysis2019.logger2013.TCMultiEntryLogger;
import soars2.utils.random2013.ICRandom;
import soars2.utils.random2013.TCJava48BitLcg;

public class TMain {

	public static void main(String[] args) throws IOException {

		// ログディレクトリの指定
		String logDir = "log";

		long startTime = System.currentTimeMillis();

		List<String> mainStages = List.of( // メインステージ
			TStages.FEEDBACK,
			TStages.FEEDBACK2,
			TStages.CLEARAHL,
			TStages.CALCAES,
			TStages.CALCAHL,
			TStages.CALCDRSCL,
			TStages.VACCINE0,
			TStages.VACCINE1,
			TStages.VACCINE2,
			TStages.CALCP,
			TStages.UPDATEADS,
			TStages.CALCPATIENT_ONBED,
			TStages.MOVE,
			TStages.CHECK);

		// ルール収集器
		TRuleAggregator ruleAggregator = new TRuleAggregator(mainStages);

		ICRandom rand = new TCJava48BitLcg(); // 乱数発生器

		// スポットの初期化
		TSpotManager spotManager = new TSpotManager(ruleAggregator); // スポット管理

		// スポットの生成
		spotManager.createSpots(TSpotTypes.S10HOME, 31);
		spotManager.createSpots(TSpotTypes.S1HOME, 2000);
		spotManager.createSpots(TSpotTypes.S2HOME, 1500);
		spotManager.createSpots(TSpotTypes.S3HOME, 500);
		spotManager.createSpots(TSpotTypes.S4HOME, 250);
		spotManager.createSpots(TSpotTypes.S5HOME, 200);
		spotManager.createSpots(TSpotTypes.S6HOME, 83);
		spotManager.createSpots(TSpotTypes.S7HOME, 42);
		spotManager.createSpots(TSpotTypes.S8HOME, 25);
		spotManager.createSpots(TSpotTypes.S9HOME, 22);
		spotManager.createSpot(TSpotTypes.DEAD);
		spotManager.createSpots(TSpotTypes.BIGOFFICE, 4);
		spotManager.createSpots(TSpotTypes.HIGHSCHOOL, 3);
		spotManager.createSpot(TSpotTypes.HOSPITAL);
		spotManager.createSpots(TSpotTypes.MIDDLEOFFICE, 7);
		spotManager.createSpot(TSpotTypes.MODEL);
		spotManager.createSpots(TSpotTypes.PRIMARYSCHOOL, 3);
		spotManager.createSpots(TSpotTypes.SMALLOFFICE, 7);
		spotManager.createSpots(TSpotTypes.TRAFFIC, 40);
		spotManager.createSpot(TSpotTypes.YARD);

		// エージェントの初期化
		TAgentManager agentManager = new TAgentManager(ruleAggregator); // エージェント管理

		// エージェントの生成
		createAgent(agentManager, spotManager, TAgentTypes.BABY, 1000, "");
		createAgent(agentManager, spotManager, TAgentTypes.MIDDLE, 2000, "");
		createAgent(agentManager, spotManager, TAgentTypes.OLD, 1000, "");
		createAgent(agentManager, spotManager, TAgentTypes.SCHOOLCHILD, 2000, "");
		createAgent(agentManager, spotManager, TAgentTypes.STUDENT, 2000, "");
		createAgent(agentManager, spotManager, TAgentTypes.YOUNG, 2000, "");

		// スポットの初期化
		initializeSpot1(spotManager, agentManager, rand);
		initializeSpot2(spotManager, agentManager, rand);
		initializeSpot3(spotManager, agentManager, rand);
		initializeSpot4(spotManager, agentManager, rand);
		initializeSpot5(spotManager, agentManager, rand);
		initializeSpot6(spotManager, agentManager, rand);
		initializeSpot7(spotManager, agentManager, rand);
		initializeSpot8(spotManager, agentManager, rand);
		initializeSpot9(spotManager, agentManager, rand);
		initializeSpot10(spotManager, agentManager, rand);
		initializeSpot11(spotManager, agentManager, rand);
		initializeSpot12(spotManager, agentManager, rand);
		initializeSpot13(spotManager, agentManager, rand);
		initializeSpot14(spotManager, agentManager, rand);
		initializeSpot15(spotManager, agentManager, rand);
		initializeSpot16(spotManager, agentManager, rand);
		initializeSpot17(spotManager, agentManager, rand);
		initializeSpot18(spotManager, agentManager, rand);
		initializeSpot19(spotManager, agentManager, rand);
		initializeSpot20(spotManager, agentManager, rand);

		// エージェントの初期化
		initializeAgent1(agentManager, spotManager, rand);
		initializeAgent2(agentManager, spotManager, rand);
		initializeAgent3(agentManager, spotManager, rand);
		initializeAgent4(agentManager, spotManager, rand);
		initializeAgent5(agentManager, spotManager, rand);
		initializeAgent6(agentManager, spotManager, rand);

		// 初期ステージの実行
		doTInitADS1(TAgentTypes.BABY, agentManager, spotManager, rand);
		doTInitADS1(TAgentTypes.STUDENT, agentManager, spotManager, rand);
		doTInitADS1(TAgentTypes.YOUNG, agentManager, spotManager, rand);
		doTInitADS1(TAgentTypes.MIDDLE, agentManager, spotManager, rand);
		doTInitADS1(TAgentTypes.SCHOOLCHILD, agentManager, spotManager, rand);
		doTInitADS1(TAgentTypes.OLD, agentManager, spotManager, rand);

		doTInitSpot1(TSpotTypes.PRIMARYSCHOOL, spotManager, rand);
		doTInitSpot2(TSpotTypes.YARD, spotManager, rand);
		doTInitSpot3(TSpotTypes.BIGOFFICE, spotManager, rand);
		doTInitSpot2(TSpotTypes.DEAD, spotManager, rand);
		doTInitSpot4(TSpotTypes.HIGHSCHOOL, spotManager, rand);
		doTInitSpot5(TSpotTypes.SMALLOFFICE, spotManager, rand);
		doTInitSpot6(TSpotTypes.S3HOME, spotManager, rand);
		doTInitSpot7(TSpotTypes.S4HOME, spotManager, rand);
		doTInitSpot8(TSpotTypes.S8HOME, spotManager, rand);
		doTInitSpot9(TSpotTypes.S7HOME, spotManager, rand);
		doTInitSpot10(TSpotTypes.S5HOME, spotManager, rand);
		doTInitSpot11(TSpotTypes.S1HOME, spotManager, rand);
		doTInitSpot12(TSpotTypes.S2HOME, spotManager, rand);
		doTInitSpot13(TSpotTypes.S10HOME, spotManager, rand);
		doTInitSpot14(TSpotTypes.MIDDLEOFFICE, spotManager, rand);
		doTInitSpot15(TSpotTypes.S9HOME, spotManager, rand);
		doTInitSpot16(TSpotTypes.S6HOME, spotManager, rand);
		doTInitSpot17(TSpotTypes.TRAFFIC, spotManager, rand);
		doTInitSpot2(TSpotTypes.HOSPITAL, spotManager, rand);

		doTInitSpot21(TSpotTypes.MODEL, spotManager, rand);

		doTInitAgent21(TAgentTypes.STUDENT, agentManager, spotManager, rand);
		doTInitAgent22(TAgentTypes.YOUNG, agentManager, spotManager, rand);
		doTInitAgent23(TAgentTypes.MIDDLE, agentManager, spotManager, rand);
		doTInitAgent24(TAgentTypes.SCHOOLCHILD, agentManager, spotManager, rand);

		doTInitAgent1(TAgentTypes.BABY, agentManager, spotManager, rand);
		doTInitAgent1(TAgentTypes.STUDENT, agentManager, spotManager, rand);
		doTInitAgent1(TAgentTypes.YOUNG, agentManager, spotManager, rand);
		doTInitAgent1(TAgentTypes.MIDDLE, agentManager, spotManager, rand);
		doTInitAgent1(TAgentTypes.SCHOOLCHILD, agentManager, spotManager, rand);
		doTInitAgent1(TAgentTypes.OLD, agentManager, spotManager, rand);

		doTInitVaccine1(TAgentTypes.BABY, agentManager, spotManager, rand);
		doTInitVaccine2(TAgentTypes.STUDENT, agentManager, spotManager, rand);
		doTInitVaccine3(TAgentTypes.YOUNG, agentManager, spotManager, rand);
		doTInitVaccine4(TAgentTypes.MIDDLE, agentManager, spotManager, rand);
		doTInitVaccine5(TAgentTypes.SCHOOLCHILD, agentManager, spotManager, rand);
		doTInitVaccine6(TAgentTypes.OLD, agentManager, spotManager, rand);

		// グローバル共有変数集合を生成する
		HashMap<String, Object> globalSharedVariableSet = new HashMap<>();

		// 各時刻における各エージェントの位置のログファイルをオープンする
		TAgentLogger agentLocationLog = new TAgentLogger(logDir + File.separator + "agentLocation.csv",
			TAgentLogger.CURRENT_SPOT_NAME_KEY, agentManager);
		String[] activeKeys = { "time", "InfectionLevel11", "dead1", "infected1", "naive1", "patientFlowOver11",
			"patientFlowOver21", "patient_severe_critical1", "vaccinated1"};

		TCMultiEntryLogger logger = new TCMultiEntryLogger(logDir + File.separator + "log", activeKeys); // ロガーを作成

		logger.beginLog(); // ロギング開始

		// メインループ
		for (TTime t = new TTime("0/00:00"); t.isLessThan("300/01:00"); t.add("0:30")) {
			for (String stage : mainStages) {
				// 時刻t，ステージstageに登録されたルールを実行する
				ruleAggregator.executeStage(t, stage, spotManager, agentManager, globalSharedVariableSet);
			}
			if (t.isEqualTo("0:00")) {
				System.out.println(t); // 現在時刻を画面に表示する
			}
			agentLocationLog.output(t); // 現在時刻の各エージェントの位置をログに出力する
			logger.beginEntry(true); // ログのエントリ開始
			logger.putData("time", t.toString()); // 時間をログに出力
			logger.putData("InfectionLevel11", ((TModelRole)spotManager.getSpotDB().get(TSpotTypes.MODEL).getRole()).getInfectionLevel1());
			logger.putData("dead1", ((TModelRole)spotManager.getSpotDB().get(TSpotTypes.MODEL).getRole()).getdead());
			logger.putData("infected1", ((TModelRole)spotManager.getSpotDB().get(TSpotTypes.MODEL).getRole()).getinfected());
			logger.putData("naive1", ((TModelRole)spotManager.getSpotDB().get(TSpotTypes.MODEL).getRole()).getnaive());
			logger.putData("patientFlowOver11", ((TModelRole)spotManager.getSpotDB().get(TSpotTypes.MODEL).getRole()).getpatientFlowOver1());
			logger.putData("patientFlowOver21", ((TModelRole)spotManager.getSpotDB().get(TSpotTypes.MODEL).getRole()).getpatientFlowOver2());
			logger.putData("patient_severe_critical1", ((TModelRole)spotManager.getSpotDB().get(TSpotTypes.MODEL).getRole()).getpatient_severe_critical());
			logger.putData("vaccinated1", ((TModelRole)spotManager.getSpotDB().get(TSpotTypes.MODEL).getRole()).getvaccinated());
			logger.endEntry(); // ログのエントリ終了
		}
		agentLocationLog.close();
		logger.endLog();
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println(elapsedTime + " [msec]");
	}

	private static void createAgent(TAgentManager agentManager, TSpotManager spotManager, String agentName, int noOfAgents, String initialSpot) {
		ArrayList<TAgent> agents = agentManager.createAgents(agentName, noOfAgents);
		if (initialSpot.equals(""))
			return;
		for (TAgent agent:agents)
			agent.initializeCurrentSpot(initialSpot, spotManager.getSpotDB());
	}

	private static void initializeSpot1(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.S10HOME); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.S10HOME+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 800);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot2(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.S1HOME); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.S1HOME+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 800);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot3(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.S2HOME); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.S2HOME+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 800);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot4(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.S3HOME); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.S3HOME+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 800);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot5(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.S4HOME); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.S4HOME+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 800);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot6(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.S5HOME); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.S5HOME+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 800);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot7(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.S6HOME); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.S6HOME+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 800);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot8(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.S7HOME); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.S7HOME+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 800);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot9(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.S8HOME); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.S8HOME+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 800);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot10(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.S9HOME); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.S9HOME+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 800);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot11(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		TSpot spot = spotManager.getSpotDB().get(TSpotTypes.DEAD);
		TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0);
		spot.setBaseRole(role);
	}

	private static void initializeSpot12(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.BIGOFFICE); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.BIGOFFICE+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 1600);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot13(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.HIGHSCHOOL); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.HIGHSCHOOL+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 1600);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot14(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		TSpot spot = spotManager.getSpotDB().get(TSpotTypes.HOSPITAL);
		TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 100.0);
		spot.setBaseRole(role);
	}

	private static void initializeSpot15(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.MIDDLEOFFICE); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.MIDDLEOFFICE+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 1600);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot16(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		TSpot spot = spotManager.getSpotDB().get(TSpotTypes.MODEL);
		ArrayList<Object> initia_linfected = new ArrayList<>();
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.YOUNG+"1"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.YOUNG+"2"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.YOUNG+"3"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.YOUNG+"4"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.YOUNG+"5"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.SCHOOLCHILD+"1"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.SCHOOLCHILD+"2"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.SCHOOLCHILD+"3"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.SCHOOLCHILD+"4"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.SCHOOLCHILD+"5"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.STUDENT+"1"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.STUDENT+"2"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.STUDENT+"3"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.STUDENT+"4"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.STUDENT+"5"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.MIDDLE+"1"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.MIDDLE+"2"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.MIDDLE+"3"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.MIDDLE+"4"));
		initia_linfected.add(agentManager.getAgentDB().get(TAgentTypes.MIDDLE+"5"));
		TModelRole role = new TModelRole(spot, rand,
			4000/4770, 0/100, 80/100, 700/770, 0.0, "no", "no", "all", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0,
			0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10000, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 250,
			new ArrayList<>(), initia_linfected, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
			new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		spot.setBaseRole(role);
	}

	private static void initializeSpot17(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.PRIMARYSCHOOL); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.PRIMARYSCHOOL+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 1600);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot18(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.SMALLOFFICE); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.SMALLOFFICE+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 1600);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot19(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(TSpotTypes.TRAFFIC); ++i) {
			TSpot spot = spotManager.getSpotDB().get(TSpotTypes.TRAFFIC+String.valueOf(i));
			TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.3, 0.0, 0.0, 1, 0.0, 1600);
			spot.setBaseRole(role);
		}
	}

	private static void initializeSpot20(TSpotManager spotManager, TAgentManager agentManager, ICRandom rand) {
		TSpot spot = spotManager.getSpotDB().get(TSpotTypes.YARD);
		TBigofficeRole role = new TBigofficeRole(spot, rand, .0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 100.0);
		spot.setBaseRole(role);
	}

	private static void initializeAgent1(TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(TAgentTypes.BABY); ++i) {
			TAgent agent = agentManager.getAgentDB().get(TAgentTypes.BABY+String.valueOf(i));
			TAgentRole agentRole = new TAgentRole(agent, rand,
				80/100, 0.8, 0.8, 0.2, "0", "baby", 0.0, 1, 0.0, 0.0, 0.0, 0.3, 0.9, 0.0, 0.8, 0.0, 1, 0.008, new TTime("0/0:00"),
				new ArrayList<>());
			TBabyMoveRole babyMoveRole = new TBabyMoveRole(agent, rand);
			babyMoveRole.mergeRole(agentRole); // 統合
			agent.setBaseRole(babyMoveRole); // 基本役割の設定
		}
	}

	private static void initializeAgent2(TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(TAgentTypes.MIDDLE); ++i) {
			TAgent agent = agentManager.getAgentDB().get(TAgentTypes.MIDDLE+String.valueOf(i));
			TAgentRole agentRole = new TAgentRole(agent, rand,
				80/100, 0.5, 0.7, 0.3, "0", "middle", 0.0, 1, 0.0, 0.0, 0.0, 0.3, 0.9, 0.0, 0.8, 0.0, 1, 0.008, new TTime("0/0:00"),
				new ArrayList<>());
			TMiddleMoveRole middleMoveRole = new TMiddleMoveRole(agent, rand, new ArrayList<>());
			middleMoveRole.mergeRole(agentRole); // 統合
			agent.setBaseRole(middleMoveRole); // 基本役割の設定
		}
	}

	private static void initializeAgent3(TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(TAgentTypes.OLD); ++i) {
			TAgent agent = agentManager.getAgentDB().get(TAgentTypes.OLD+String.valueOf(i));
			TAgentRole agentRole = new TAgentRole(agent, rand,
				80/100, 0.4, 0.6, 0.4, "0", "old", 0.0, 1, 0.0, 0.0, 0.0, 0.3, 0.9, 0.0, 0.8, 0.0, 1, 0.008, new TTime("0/0:00"),
				new ArrayList<>());
			TBabyMoveRole babyMoveRole = new TBabyMoveRole(agent, rand);
			babyMoveRole.mergeRole(agentRole); // 統合
			agent.setBaseRole(babyMoveRole); // 基本役割の設定
		}
	}

	private static void initializeAgent4(TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(TAgentTypes.SCHOOLCHILD); ++i) {
			TAgent agent = agentManager.getAgentDB().get(TAgentTypes.SCHOOLCHILD+String.valueOf(i));
			TAgentRole agentRole = new TAgentRole(agent, rand,
				80/100, 0.8, 0.8, 0.2, "0", "schoolchild", 0.0, 1, 0.0, 0.0, 0.0, 0.3, 0.9, 0.0, 0.8, 0.0, 1, 0.008,
				new TTime("0/0:00"), new ArrayList<>());
			TSchoolchildMoveRole schoolchildMoveRole = new TSchoolchildMoveRole(agent, rand, new ArrayList<>());
			schoolchildMoveRole.mergeRole(agentRole); // 統合
			agent.setBaseRole(schoolchildMoveRole); // 基本役割の設定
		}
	}

	private static void initializeAgent5(TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(TAgentTypes.STUDENT); ++i) {
			TAgent agent = agentManager.getAgentDB().get(TAgentTypes.STUDENT+String.valueOf(i));
			TAgentRole agentRole = new TAgentRole(agent, rand,
				80/100, 0.8, 0.8, 0.2, "0", "student", 0.0, 1, 0.0, 0.0, 0.0, 0.3, 0.9, 0.0, 0.8, 0.0, 1, 0.008, new TTime("0/0:00"),
				new ArrayList<>());
			TStudentMoveRole studentMoveRole = new TStudentMoveRole(agent, rand, new ArrayList<>());
			studentMoveRole.mergeRole(agentRole); // 統合
			agent.setBaseRole(studentMoveRole); // 基本役割の設定
		}
	}

	private static void initializeAgent6(TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(TAgentTypes.YOUNG); ++i) {
			TAgent agent = agentManager.getAgentDB().get(TAgentTypes.YOUNG+String.valueOf(i));
			TAgentRole agentRole = new TAgentRole(agent, rand,
				80/100, 0.8, 0.8, 0.2, "0", "young", 0.0, 1, 0.0, 0.0, 0.0, 0.3, 0.9, 0.0, 0.8, 0.0, 1, 0.008, new TTime("0/0:00"),
				new ArrayList<>());
			TYoungMoveRole youngMoveRole = new TYoungMoveRole(agent, rand, new ArrayList<>());
			youngMoveRole.mergeRole(agentRole); // 統合
			agent.setBaseRole(youngMoveRole); // 基本役割の設定
		}
	}

	private static void doTInitADS1(String agentName, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(agentName); ++i) {
			TAgent agent = agentManager.getAgentDB().get(agentName+String.valueOf(i));
			TSpot spot = spotManager.getSpotDB().get(agent.getCurrentSpotName());
			if (TInitADS.init1(agent, spot, (TAgentRole)agent.getBaseRole().getMergedRole("AgentRole"), agentManager, spotManager, rand))
				continue;
			TInitADS.init2(agent, spot, (TAgentRole)agent.getBaseRole().getMergedRole("AgentRole"), agentManager, spotManager, rand);
		}
	}

	private static void doTInitSpot1(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init6(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot2(String spotName, TSpotManager spotManager, ICRandom rand) {
		TSpot spot = spotManager.getSpotDB().get(spotName);
		TInitSpot.init2(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
	}

	private static void doTInitSpot3(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init1(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot4(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init5(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot5(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init3(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot6(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init15(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot7(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init14(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot8(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init10(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot9(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init11(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot10(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init13(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot11(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init16(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot12(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init17(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot13(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init8(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot14(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init4(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot15(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init9(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot16(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init12(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot17(String spotName, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= spotManager.getNoOfSpots(spotName); ++i) {
			TSpot spot = spotManager.getSpotDB().get(spotName+String.valueOf(i));
			TInitSpot.init7(spot, (TBigofficeRole)spot.getBaseRole(), spotManager, rand);
		}
	}

	private static void doTInitSpot21(String spotName, TSpotManager spotManager, ICRandom rand) {
		TSpot spot = spotManager.getSpotDB().get(spotName);
		TInitSpot2.init1(spot, (TModelRole)spot.getBaseRole(), spotManager, rand);
	}

	private static void doTInitAgent21(String agentName, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(agentName); ++i) {
			TAgent agent = agentManager.getAgentDB().get(agentName+String.valueOf(i));
			TSpot spot = spotManager.getSpotDB().get(agent.getCurrentSpotName());
			TInitAgent2.init8(agent, spot, (TStudentMoveRole)agent.getBaseRole(), agentManager, spotManager, rand);
		}
	}

	private static void doTInitAgent22(String agentName, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(agentName); ++i) {
			TAgent agent = agentManager.getAgentDB().get(agentName+String.valueOf(i));
			TSpot spot = spotManager.getSpotDB().get(agent.getCurrentSpotName());
			if (TInitAgent2.init5(agent, spot, (TYoungMoveRole)agent.getBaseRole(), agentManager, spotManager, rand))
				continue;
			if (TInitAgent2.init6(agent, spot, (TYoungMoveRole)agent.getBaseRole(), agentManager, spotManager, rand))
				continue;
			TInitAgent2.init7(agent, spot, (TYoungMoveRole)agent.getBaseRole(), agentManager, spotManager, rand);
		}
	}

	private static void doTInitAgent23(String agentName, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(agentName); ++i) {
			TAgent agent = agentManager.getAgentDB().get(agentName+String.valueOf(i));
			TSpot spot = spotManager.getSpotDB().get(agent.getCurrentSpotName());
			if (TInitAgent2.init1(agent, spot, (TMiddleMoveRole)agent.getBaseRole(), agentManager, spotManager, rand))
				continue;
			if (TInitAgent2.init2(agent, spot, (TMiddleMoveRole)agent.getBaseRole(), agentManager, spotManager, rand))
				continue;
			TInitAgent2.init3(agent, spot, (TMiddleMoveRole)agent.getBaseRole(), agentManager, spotManager, rand);
		}
	}

	private static void doTInitAgent24(String agentName, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(agentName); ++i) {
			TAgent agent = agentManager.getAgentDB().get(agentName+String.valueOf(i));
			TSpot spot = spotManager.getSpotDB().get(agent.getCurrentSpotName());
			TInitAgent2.init4(agent, spot, (TSchoolchildMoveRole)agent.getBaseRole(), agentManager, spotManager, rand);
		}
	}

	private static void doTInitAgent1(String agentName, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(agentName); ++i) {
			TAgent agent = agentManager.getAgentDB().get(agentName+String.valueOf(i));
			TSpot spot = spotManager.getSpotDB().get(agent.getCurrentSpotName());
			TInitAgent.init1(agent, spot, (TAgentRole)agent.getBaseRole().getMergedRole("AgentRole"), agentManager, spotManager, rand);
		}
	}

	private static void doTInitVaccine1(String agentName, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(agentName); ++i) {
			TAgent agent = agentManager.getAgentDB().get(agentName+String.valueOf(i));
			TSpot spot = spotManager.getSpotDB().get(agent.getCurrentSpotName());
			TInitVaccine.init3(agent, spot, (TBabyMoveRole)agent.getBaseRole(), agentManager, spotManager, rand);
		}
	}

	private static void doTInitVaccine2(String agentName, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(agentName); ++i) {
			TAgent agent = agentManager.getAgentDB().get(agentName+String.valueOf(i));
			TSpot spot = spotManager.getSpotDB().get(agent.getCurrentSpotName());
			TInitVaccine.init6(agent, spot, (TStudentMoveRole)agent.getBaseRole(), agentManager, spotManager, rand);
		}
	}

	private static void doTInitVaccine3(String agentName, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(agentName); ++i) {
			TAgent agent = agentManager.getAgentDB().get(agentName+String.valueOf(i));
			TSpot spot = spotManager.getSpotDB().get(agent.getCurrentSpotName());
			TInitVaccine.init5(agent, spot, (TYoungMoveRole)agent.getBaseRole(), agentManager, spotManager, rand);
		}
	}

	private static void doTInitVaccine4(String agentName, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(agentName); ++i) {
			TAgent agent = agentManager.getAgentDB().get(agentName+String.valueOf(i));
			TSpot spot = spotManager.getSpotDB().get(agent.getCurrentSpotName());
			TInitVaccine.init1(agent, spot, (TMiddleMoveRole)agent.getBaseRole(), agentManager, spotManager, rand);
		}
	}

	private static void doTInitVaccine5(String agentName, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(agentName); ++i) {
			TAgent agent = agentManager.getAgentDB().get(agentName+String.valueOf(i));
			TSpot spot = spotManager.getSpotDB().get(agent.getCurrentSpotName());
			TInitVaccine.init2(agent, spot, (TSchoolchildMoveRole)agent.getBaseRole(), agentManager, spotManager, rand);
		}
	}

	private static void doTInitVaccine6(String agentName, TAgentManager agentManager, TSpotManager spotManager, ICRandom rand) {
		for (int i = 1; i <= agentManager.getNoOfAgents(agentName); ++i) {
			TAgent agent = agentManager.getAgentDB().get(agentName+String.valueOf(i));
			TSpot spot = spotManager.getSpotDB().get(agent.getCurrentSpotName());
			TInitVaccine.init4(agent, spot, (TBabyMoveRole)agent.getBaseRole(), agentManager, spotManager, rand);
		}
	}
}
