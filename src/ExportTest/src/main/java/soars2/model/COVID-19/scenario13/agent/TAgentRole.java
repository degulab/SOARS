package soars2.model.COVID-19.scenario13.agent;

import java.util.ArrayList;
import java.util.Objects;
import soars2.core.TAgent;
import soars2.core.TRole;
import soars2.core.TTime;
import soars2.model.COVID-19.scenario13.TStages;
import soars2.model.COVID-19.scenario13.agent.rules.TAgentRoleCalcAES1Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TAgentRoleCalcAHL1Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TAgentRoleCalcP1Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TAgentRoleCalcpatientOnbed1Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TAgentRoleCheck1Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TAgentRoleUpdateADS1Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TAgentRoleVaccine11Rule;
import soars2.model.COVID-19.scenario13.agent.rules.TAgentRoleVaccine21Rule;
import soars2.utils.random2013.ICRandom;

/**
 * TAgentRole
 */
public class TAgentRole extends TRole {

	/** 役割名 */
	public static final String ROLE_NAME = "AgentRole";

	/**  */
	private double fp12;
	/**  */
	private double fp23;
	/**  */
	private double fp3s4m;
	/**  */
	private double fp4cd;
	/**  */
	private String fADS;
	/**  */
	private String fage;
	/**  */
	private double fACL;
	/**  */
	private double fACPF;
	/**  */
	private double fAES;
	/**  */
	private double fAHL;
	/**  */
	private double fAgentAF;
	/**  */
	private double fEPF;
	/**  */
	private double fEnAAF;
	/**  */
	private double fP;
	/**  */
	private double fPC;
	/**  */
	private double fSHLAP;
	/**  */
	private double fStAAF;
	/**  */
	private double fa;
	/**  */
	private TTime ftimer;
	/**  */
	private ArrayList<Object> fhome;
	
	/**
	 * コンストラクタ
	 * 
	 * @param ownerAgent この役割を持つエージェント
	 * @param rand 乱数発生器
	 */
	public TAgentRole(TAgent ownerAgent, ICRandom rand,
		double p12, double p23, double p3s4m, double p4cd, String ADS, String age, double ACL, double ACPF,
		double AES, double AHL, double AgentAF, double EPF, double EnAAF, double P, double PC, double SHLAP,
		double StAAF, double a, TTime timer, ArrayList<Object> home) {
		super(ROLE_NAME, ownerAgent, rand); // 親クラスのコンストラクタを呼び出す
		fp12=p12;
		fp23=p23;
		fp3s4m=p3s4m;
		fp4cd=p4cd;
		fADS=ADS;
		fage=age;
		fACL=ACL;
		fACPF=ACPF;
		fAES=AES;
		fAHL=AHL;
		fAgentAF=AgentAF;
		fEPF=EPF;
		fEnAAF=EnAAF;
		fP=P;
		fPC=PC;
		fSHLAP=SHLAP;
		fStAAF=StAAF;
		fa=a;
		ftimer=timer;
		fhome=home;
		// TStages.CALCAESクラス
		new TAgentRoleCalcAES1Rule(this).setTimeAndStage(new TTime("0:00"), new TTime("23:30"), new TTime("0:30"), TStages.CALCAES);
		// TStages.CALCAHLクラス
		new TAgentRoleCalcAHL1Rule(this).setTimeAndStage(new TTime("0:00"), new TTime("23:30"), new TTime("0:30"), TStages.CALCAHL);
		// TStages.VACCINE1クラス
		new TAgentRoleVaccine11Rule(this).setTimeAndStage(true, new TTime("8:00"), TStages.VACCINE1);
		// TStages.VACCINE2クラス
		new TAgentRoleVaccine21Rule(this).setTimeAndStage(true, new TTime("8:00"), TStages.VACCINE2);
		// TStages.CALCPクラス
		new TAgentRoleCalcP1Rule(this).setTimeAndStage(new TTime("0:00"), new TTime("23:30"), new TTime("0:30"), TStages.CALCP);
		// TStages.UPDATEADSクラス
		new TAgentRoleUpdateADS1Rule(this).setTimeAndStage(new TTime("0:00"), new TTime("23:30"), new TTime("0:30"), TStages.UPDATEADS);
		// TStages.CALCPATIENT_ONBEDクラス
		new TAgentRoleCalcpatientOnbed1Rule(this).setTimeAndStage(new TTime("0:00"), new TTime("23:30"), new TTime("0:30"), TStages.CALCPATIENT_ONBED);
		// TStages.CHECKクラス
		new TAgentRoleCheck1Rule(this).setTimeAndStage(new TTime("0:00"), new TTime("23:30"), new TTime("0:30"), TStages.CHECK);
	}

	//
	// Getter and Setter
	//

	public double getp12() {
		return this.fp12;
	}

	public void setp12(double fp12) {
		this.fp12=fp12;
	}

	public double getp23() {
		return this.fp23;
	}

	public void setp23(double fp23) {
		this.fp23=fp23;
	}

	public double getp3s4m() {
		return this.fp3s4m;
	}

	public void setp3s4m(double fp3s4m) {
		this.fp3s4m=fp3s4m;
	}

	public double getp4cd() {
		return this.fp4cd;
	}

	public void setp4cd(double fp4cd) {
		this.fp4cd=fp4cd;
	}

	public String getADS() {
		return this.fADS;
	}

	public void setADS(String fADS) {
		this.fADS=fADS;
	}

	public String getage() {
		return this.fage;
	}

	public void setage(String fage) {
		this.fage=fage;
	}

	public double getACL() {
		return this.fACL;
	}

	public void setACL(double fACL) {
		this.fACL=fACL;
	}

	public double getACPF() {
		return this.fACPF;
	}

	public void setACPF(double fACPF) {
		this.fACPF=fACPF;
	}

	public double getAES() {
		return this.fAES;
	}

	public void setAES(double fAES) {
		this.fAES=fAES;
	}

	public double getAHL() {
		return this.fAHL;
	}

	public void setAHL(double fAHL) {
		this.fAHL=fAHL;
	}

	public double getAgentAF() {
		return this.fAgentAF;
	}

	public void setAgentAF(double fAgentAF) {
		this.fAgentAF=fAgentAF;
	}

	public double getEPF() {
		return this.fEPF;
	}

	public void setEPF(double fEPF) {
		this.fEPF=fEPF;
	}

	public double getEnAAF() {
		return this.fEnAAF;
	}

	public void setEnAAF(double fEnAAF) {
		this.fEnAAF=fEnAAF;
	}

	public double getP() {
		return this.fP;
	}

	public void setP(double fP) {
		this.fP=fP;
	}

	public double getPC() {
		return this.fPC;
	}

	public void setPC(double fPC) {
		this.fPC=fPC;
	}

	public double getSHLAP() {
		return this.fSHLAP;
	}

	public void setSHLAP(double fSHLAP) {
		this.fSHLAP=fSHLAP;
	}

	public double getStAAF() {
		return this.fStAAF;
	}

	public void setStAAF(double fStAAF) {
		this.fStAAF=fStAAF;
	}

	public double geta() {
		return this.fa;
	}

	public void seta(double fa) {
		this.fa=fa;
	}

	public TTime gettimer() {
		return this.ftimer;
	}

	public void settimer(TTime ftimer) {
		this.ftimer=ftimer;
	}

	public ArrayList<Object> gethome() {
		return this.fhome;
	}

	public void sethome(ArrayList<Object> fhome) {
		this.fhome=fhome;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof TAgentRole))
			return false;
		TAgentRole tAgentRole = (TAgentRole) o;
		return fp12==tAgentRole.fp12
			&& fp23==tAgentRole.fp23
			&& fp3s4m==tAgentRole.fp3s4m
			&& fp4cd==tAgentRole.fp4cd
			&& fADS==tAgentRole.fADS
			&& fage==tAgentRole.fage
			&& fACL==tAgentRole.fACL
			&& fACPF==tAgentRole.fACPF
			&& fAES==tAgentRole.fAES
			&& fAHL==tAgentRole.fAHL
			&& fAgentAF==tAgentRole.fAgentAF
			&& fEPF==tAgentRole.fEPF
			&& fEnAAF==tAgentRole.fEnAAF
			&& fP==tAgentRole.fP
			&& fPC==tAgentRole.fPC
			&& fSHLAP==tAgentRole.fSHLAP
			&& fStAAF==tAgentRole.fStAAF
			&& fa==tAgentRole.fa
			&& Objects.equals(ftimer, tAgentRole.ftimer)
			&& Objects.equals(fhome, tAgentRole.fhome);
	}

	@Override
	public String toString() {
		return "{" + " fp12='" + getp12() + "'"
			+ ", fp23='" + getp23() + "'"
			+ ", fp3s4m='" + getp3s4m() + "'"
			+ ", fp4cd='" + getp4cd() + "'"
			+ ", fADS='" + getADS() + "'"
			+ ", fage='" + getage() + "'"
			+ ", fACL='" + getACL() + "'"
			+ ", fACPF='" + getACPF() + "'"
			+ ", fAES='" + getAES() + "'"
			+ ", fAHL='" + getAHL() + "'"
			+ ", fAgentAF='" + getAgentAF() + "'"
			+ ", fEPF='" + getEPF() + "'"
			+ ", fEnAAF='" + getEnAAF() + "'"
			+ ", fP='" + getP() + "'"
			+ ", fPC='" + getPC() + "'"
			+ ", fSHLAP='" + getSHLAP() + "'"
			+ ", fStAAF='" + getStAAF() + "'"
			+ ", fa='" + geta() + "'"
			+ ", ftimer='" + gettimer() + "'"
			+ ", fhome='" + gethome() + "'"
			+ "}";
	}
}
