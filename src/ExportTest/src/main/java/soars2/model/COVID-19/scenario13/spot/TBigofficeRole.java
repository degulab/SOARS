package soars2.model.COVID-19.scenario13.spot;

import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.COVID-19.scenario13.TStages;
import soars2.model.COVID-19.scenario13.spot.rules.TBigofficeRoleCalcDRSCL1Rule;
import soars2.model.COVID-19.scenario13.spot.rules.TBigofficeRoleClearAHL1Rule;
import soars2.utils.random2013.ICRandom;

/**
 * TBigofficeRole
 */
public class TBigofficeRole extends TRole {

	/** 役割名 */
	public static final String ROLE_NAME = "BigofficeRole";

	/**  */
	private double fAHL;
	/**  */
	private double fAHLterm;
	/**  */
	private double fDRSCL;
	/**  */
	private double fEnSAF;
	/**  */
	private double fSCL;
	/**  */
	private double fSpotAF;
	/**  */
	private double fStSAF;
	/**  */
	private double fVD;
	/**  */
	private double fVSS;
	
	/**
	 * コンストラクタ
	 * 
	 * @param ownerSpot この役割を持つスポット
	 * @param rand 乱数発生器
	 */
	public TBigofficeRole(TSpot ownerSpot, ICRandom rand,
		double AHL, double AHLterm, double DRSCL, double EnSAF, double SCL, double SpotAF, double StSAF, double VD,
		double VSS) {
		super(ROLE_NAME, ownerSpot, rand); // 親クラスのコンストラクタを呼び出す
		fAHL=AHL;
		fAHLterm=AHLterm;
		fDRSCL=DRSCL;
		fEnSAF=EnSAF;
		fSCL=SCL;
		fSpotAF=SpotAF;
		fStSAF=StSAF;
		fVD=VD;
		fVSS=VSS;
		// TStages.CLEARAHLクラス
		new TBigofficeRoleClearAHL1Rule(this).setTimeAndStage(new TTime("0:00"), new TTime("23:30"), new TTime("0:30"), TStages.CLEARAHL);
		// TStages.CALCDRSCLクラス
		new TBigofficeRoleCalcDRSCL1Rule(this).setTimeAndStage(new TTime("0:00"), new TTime("23:30"), new TTime("0:30"), TStages.CALCDRSCL);
	}

	//
	// Getter and Setter
	//

	public double getAHL() {
		return this.fAHL;
	}

	public void setAHL(double fAHL) {
		this.fAHL=fAHL;
	}

	public double getAHLterm() {
		return this.fAHLterm;
	}

	public void setAHLterm(double fAHLterm) {
		this.fAHLterm=fAHLterm;
	}

	public double getDRSCL() {
		return this.fDRSCL;
	}

	public void setDRSCL(double fDRSCL) {
		this.fDRSCL=fDRSCL;
	}

	public double getEnSAF() {
		return this.fEnSAF;
	}

	public void setEnSAF(double fEnSAF) {
		this.fEnSAF=fEnSAF;
	}

	public double getSCL() {
		return this.fSCL;
	}

	public void setSCL(double fSCL) {
		this.fSCL=fSCL;
	}

	public double getSpotAF() {
		return this.fSpotAF;
	}

	public void setSpotAF(double fSpotAF) {
		this.fSpotAF=fSpotAF;
	}

	public double getStSAF() {
		return this.fStSAF;
	}

	public void setStSAF(double fStSAF) {
		this.fStSAF=fStSAF;
	}

	public double getVD() {
		return this.fVD;
	}

	public void setVD(double fVD) {
		this.fVD=fVD;
	}

	public double getVSS() {
		return this.fVSS;
	}

	public void setVSS(double fVSS) {
		this.fVSS=fVSS;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof TBigofficeRole))
			return false;
		TBigofficeRole tBigofficeRole = (TBigofficeRole) o;
		return fAHL==tBigofficeRole.fAHL
			&& fAHLterm==tBigofficeRole.fAHLterm
			&& fDRSCL==tBigofficeRole.fDRSCL
			&& fEnSAF==tBigofficeRole.fEnSAF
			&& fSCL==tBigofficeRole.fSCL
			&& fSpotAF==tBigofficeRole.fSpotAF
			&& fStSAF==tBigofficeRole.fStSAF
			&& fVD==tBigofficeRole.fVD
			&& fVSS==tBigofficeRole.fVSS;
	}

	@Override
	public String toString() {
		return "{" + " fAHL='" + getAHL() + "'"
			+ ", fAHLterm='" + getAHLterm() + "'"
			+ ", fDRSCL='" + getDRSCL() + "'"
			+ ", fEnSAF='" + getEnSAF() + "'"
			+ ", fSCL='" + getSCL() + "'"
			+ ", fSpotAF='" + getSpotAF() + "'"
			+ ", fStSAF='" + getStSAF() + "'"
			+ ", fVD='" + getVD() + "'"
			+ ", fVSS='" + getVSS() + "'"
			+ "}";
	}
}
