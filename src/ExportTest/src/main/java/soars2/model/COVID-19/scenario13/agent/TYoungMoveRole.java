package soars2.model.COVID-19.scenario13.agent;

import java.util.ArrayList;
import java.util.Objects;
import soars2.core.TAgent;
import soars2.core.TRole;
import soars2.core.TTime;
import soars2.model.COVID-19.scenario13.TStages;
import soars2.model.COVID-19.scenario13.agent.rules.TMiddleMoveRoleMove11Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TMiddleMoveRoleMove1Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TMiddleMoveRoleMove2Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TMiddleMoveRoleMove3Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TMiddleMoveRoleMove4Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TMiddleMoveRoleMove5Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TMiddleMoveRoleMove6Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TMiddleMoveRoleMove7Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TMiddleMoveRoleMove8Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TMiddleMoveRoleMove8Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TMiddleMoveRoleMove9Rule;
import soars2.utils.random2013.ICRandom;

/**
 * TYoungMoveRole
 */
public class TYoungMoveRole extends TRole {

	/** 役割名 */
	public static final String ROLE_NAME = "YoungMoveRole";

	/**  */
	private ArrayList<Object> foffice;
	
	/**
	 * コンストラクタ
	 * 
	 * @param ownerAgent この役割を持つエージェント
	 * @param rand 乱数発生器
	 */
	public TYoungMoveRole(TAgent ownerAgent, ICRandom rand, ArrayList<Object> office) {
		super(ROLE_NAME, ownerAgent, rand); // 親クラスのコンストラクタを呼び出す
		foffice=office;
		// TStages.MOVEクラス
		new TMiddleMoveRoleMove1Rule(this).setTimeAndStage(new TTime("0:00"), new TTime("23:30"), new TTime("0:30"), TStages.MOVE);
		// TStages.MOVEクラス
		new TMiddleMoveRoleMove2Rule(this).setTimeAndStage(true, new TTime("19:00"), TStages.MOVE);
		// TStages.MOVEクラス
		new TMiddleMoveRoleMove3Rule(this).setTimeAndStage(new TTime("0:00"), new TTime("23:30"), new TTime("0:30"), TStages.MOVE);
		// TStages.MOVEクラス
		new TMiddleMoveRoleMove4Rule(this).setTimeAndStage(true, new TTime("8:00"), TStages.MOVE);
		// TStages.MOVEクラス
		new TMiddleMoveRoleMove5Rule(this).setTimeAndStage(true, new TTime("8:00"), TStages.MOVE);
		// TStages.MOVEクラス
		new TMiddleMoveRoleMove6Rule(this).setTimeAndStage(true, new TTime("8:00"), TStages.MOVE);
		// TStages.MOVEクラス
		new TMiddleMoveRoleMove7Rule(this).setTimeAndStage(new TTime("0:00"), new TTime("23:30"), new TTime("0:30"), TStages.MOVE);
		// TStages.MOVEクラス
		new TMiddleMoveRoleMove8Rule(this).setTimeAndStage(true, new TTime("8:00"), TStages.MOVE);
		// TStages.MOVEクラス
		new TMiddleMoveRoleMove9Rule(this).setTimeAndStage(true, new TTime("9:00"), TStages.MOVE);
		// TStages.MOVEクラス
		new TMiddleMoveRoleMove8Rule(this).setTimeAndStage(true, new TTime("18:00"), TStages.MOVE);
		// TStages.MOVEクラス
		new TMiddleMoveRoleMove11Rule(this).setTimeAndStage(true, new TTime("19:00"), TStages.MOVE);
	}

	//
	// Getter and Setter
	//

	public ArrayList<Object> getoffice() {
		return this.foffice;
	}

	public void setoffice(ArrayList<Object> foffice) {
		this.foffice=foffice;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof TYoungMoveRole))
			return false;
		TYoungMoveRole tYoungMoveRole = (TYoungMoveRole) o;
		return Objects.equals(foffice, tYoungMoveRole.foffice);
	}

	@Override
	public String toString() {
		return "{" + " foffice='" + getoffice() + "'" + "}";
	}
}
