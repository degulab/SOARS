package soars2.model.COVID-19.scenario13.spot;

import java.util.ArrayList;
import java.util.Objects;
import soars2.core.TRole;
import soars2.core.TSpot;
import soars2.core.TTime;
import soars2.model.COVID-19.scenario13.TStages;
import soars2.model.COVID-19.scenario13.spot.rules.TModelRoleCheck1Rule;
import soars2.model.COVID-19.scenario13.spot.rules.TModelRoleClearAHL1Rule;
import soars2.model.COVID-19.scenario13.spot.rules.TModelRoleFeedback1Rule;
import soars2.model.COVID-19.scenario13.spot.rules.TModelRoleFeedback21Rule;
import soars2.model.COVID-19.scenario13.spot.rules.TModelRoleVaccine01Rule;
import soars2.utils.random2013.ICRandom;

/**
 * TModelRole
 */
public class TModelRole extends TRole {

	/** 役割名 */
	public static final String ROLE_NAME = "ModelRole";

	/**  */
	private double fpbig;
	/**  */
	private double fpiso2;
	/**  */
	private double fpiso3;
	/**  */
	private double fpmiddle;
	/**  */
	private double fv_prob;
	/**  */
	private String fofficeclose;
	/**  */
	private String fschoolclose;
	/**  */
	private String fv_strategy;
	/**  */
	private double fInfectionLevel1;
	/**  */
	private double fInfectionLevel2;
	/**  */
	private double fInfectionLevel2m;
	/**  */
	private double fInfectionLevel3;
	/**  */
	private double fInfectionLevel3s;
	/**  */
	private double fInfectionLevel4c;
	/**  */
	private double fInfectionLevel4m;
	/**  */
	private double fbednumforOSD;
	/**  */
	private double fbednumforSSD;
	/**  */
	private double fdaycount;
	/**  */
	private double fdead;
	/**  */
	private double finfected;
	/**  */
	private double finfectedBaby;
	/**  */
	private double finfectedMiddle;
	/**  */
	private double finfectedOld;
	/**  */
	private double finfectedSchoolchild;
	/**  */
	private double finfectedStudent;
	/**  */
	private double finfectedYoung;
	/**  */
	private double fmaxvactine;
	/**  */
	private double fnaive;
	/**  */
	private double fpatientFlowOver1;
	/**  */
	private double fpatientFlowOver2;
	/**  */
	private double fpatient_severe_critical;
	/**  */
	private double fpatientpluusdead;
	/**  */
	private double ftime;
	/**  */
	private double fv_ability;
	/**  */
	private double fv_candidate;
	/**  */
	private double fvaccinated;
	/**  */
	private double fvacdelay;
	/**  */
	private ArrayList<Object> fcandidate;
	/**  */
	private ArrayList<Object> finitia_linfected;
	/**  */
	private ArrayList<Object> fbigoffices;
	/**  */
	private ArrayList<Object> fhighschools;
	/**  */
	private ArrayList<Object> fhomes;
	/**  */
	private ArrayList<Object> fmiddleoffices;
	/**  */
	private ArrayList<Object> fprimaryschools;
	/**  */
	private ArrayList<Object> fsmalloffices;
	/**  */
	private ArrayList<Object> ftraffics;
	
	/**
	 * コンストラクタ
	 * 
	 * @param ownerSpot この役割を持つスポット
	 * @param rand 乱数発生器
	 */
	public TModelRole(TSpot ownerSpot, ICRandom rand,
		double pbig, double piso2, double piso3, double pmiddle, double v_prob, String officeclose, String schoolclose,
		String v_strategy, double InfectionLevel1, double InfectionLevel2, double InfectionLevel2m, double InfectionLevel3,
		double InfectionLevel3s, double InfectionLevel4c, double InfectionLevel4m, double bednumforOSD, double bednumforSSD,
		double daycount, double dead, double infected, double infectedBaby, double infectedMiddle, double infectedOld,
		double infectedSchoolchild, double infectedStudent, double infectedYoung, double maxvactine, double naive,
		double patientFlowOver1, double patientFlowOver2, double patient_severe_critical, double patientpluusdead,
		double time, double v_ability, double v_candidate, double vaccinated, double vacdelay, ArrayList<Object> candidate,
		ArrayList<Object> initia_linfected, ArrayList<Object> bigoffices, ArrayList<Object> highschools, ArrayList<Object> homes,
		ArrayList<Object> middleoffices, ArrayList<Object> primaryschools, ArrayList<Object> smalloffices, ArrayList<Object> traffics) {
		super(ROLE_NAME, ownerSpot, rand); // 親クラスのコンストラクタを呼び出す
		fpbig=pbig;
		fpiso2=piso2;
		fpiso3=piso3;
		fpmiddle=pmiddle;
		fv_prob=v_prob;
		fofficeclose=officeclose;
		fschoolclose=schoolclose;
		fv_strategy=v_strategy;
		fInfectionLevel1=InfectionLevel1;
		fInfectionLevel2=InfectionLevel2;
		fInfectionLevel2m=InfectionLevel2m;
		fInfectionLevel3=InfectionLevel3;
		fInfectionLevel3s=InfectionLevel3s;
		fInfectionLevel4c=InfectionLevel4c;
		fInfectionLevel4m=InfectionLevel4m;
		fbednumforOSD=bednumforOSD;
		fbednumforSSD=bednumforSSD;
		fdaycount=daycount;
		fdead=dead;
		finfected=infected;
		finfectedBaby=infectedBaby;
		finfectedMiddle=infectedMiddle;
		finfectedOld=infectedOld;
		finfectedSchoolchild=infectedSchoolchild;
		finfectedStudent=infectedStudent;
		finfectedYoung=infectedYoung;
		fmaxvactine=maxvactine;
		fnaive=naive;
		fpatientFlowOver1=patientFlowOver1;
		fpatientFlowOver2=patientFlowOver2;
		fpatient_severe_critical=patient_severe_critical;
		fpatientpluusdead=patientpluusdead;
		ftime=time;
		fv_ability=v_ability;
		fv_candidate=v_candidate;
		fvaccinated=vaccinated;
		fvacdelay=vacdelay;
		fcandidate=candidate;
		finitia_linfected=initia_linfected;
		fbigoffices=bigoffices;
		fhighschools=highschools;
		fhomes=homes;
		fmiddleoffices=middleoffices;
		fprimaryschools=primaryschools;
		fsmalloffices=smalloffices;
		ftraffics=traffics;
		// TStages.FEEDBACKクラス
		new TModelRoleFeedback1Rule(this).setTimeAndStage(new TTime("0:00"), new TTime("23:30"), new TTime("0:30"), TStages.FEEDBACK);
		// TStages.FEEDBACK2クラス
		new TModelRoleFeedback21Rule(this).setTimeAndStage(new TTime("0:00"), new TTime("23:30"), new TTime("0:30"), TStages.FEEDBACK2);
		// TStages.CLEARAHLクラス
		new TModelRoleClearAHL1Rule(this).setTimeAndStage(true, new TTime("0:00"), TStages.CLEARAHL);
		// TStages.VACCINE0クラス
		new TModelRoleVaccine01Rule(this).setTimeAndStage(true, new TTime("8:00"), TStages.VACCINE0);
		// TStages.CHECKクラス
		new TModelRoleCheck1Rule(this).setTimeAndStage(new TTime("0:00"), new TTime("23:30"), new TTime("0:30"), TStages.CHECK);
	}

	//
	// Getter and Setter
	//

	public double getpbig() {
		return this.fpbig;
	}

	public void setpbig(double fpbig) {
		this.fpbig=fpbig;
	}

	public double getpiso2() {
		return this.fpiso2;
	}

	public void setpiso2(double fpiso2) {
		this.fpiso2=fpiso2;
	}

	public double getpiso3() {
		return this.fpiso3;
	}

	public void setpiso3(double fpiso3) {
		this.fpiso3=fpiso3;
	}

	public double getpmiddle() {
		return this.fpmiddle;
	}

	public void setpmiddle(double fpmiddle) {
		this.fpmiddle=fpmiddle;
	}

	public double getv_prob() {
		return this.fv_prob;
	}

	public void setv_prob(double fv_prob) {
		this.fv_prob=fv_prob;
	}

	public String getofficeclose() {
		return this.fofficeclose;
	}

	public void setofficeclose(String fofficeclose) {
		this.fofficeclose=fofficeclose;
	}

	public String getschoolclose() {
		return this.fschoolclose;
	}

	public void setschoolclose(String fschoolclose) {
		this.fschoolclose=fschoolclose;
	}

	public String getv_strategy() {
		return this.fv_strategy;
	}

	public void setv_strategy(String fv_strategy) {
		this.fv_strategy=fv_strategy;
	}

	public double getInfectionLevel1() {
		return this.fInfectionLevel1;
	}

	public void setInfectionLevel1(double fInfectionLevel1) {
		this.fInfectionLevel1=fInfectionLevel1;
	}

	public double getInfectionLevel2() {
		return this.fInfectionLevel2;
	}

	public void setInfectionLevel2(double fInfectionLevel2) {
		this.fInfectionLevel2=fInfectionLevel2;
	}

	public double getInfectionLevel2m() {
		return this.fInfectionLevel2m;
	}

	public void setInfectionLevel2m(double fInfectionLevel2m) {
		this.fInfectionLevel2m=fInfectionLevel2m;
	}

	public double getInfectionLevel3() {
		return this.fInfectionLevel3;
	}

	public void setInfectionLevel3(double fInfectionLevel3) {
		this.fInfectionLevel3=fInfectionLevel3;
	}

	public double getInfectionLevel3s() {
		return this.fInfectionLevel3s;
	}

	public void setInfectionLevel3s(double fInfectionLevel3s) {
		this.fInfectionLevel3s=fInfectionLevel3s;
	}

	public double getInfectionLevel4c() {
		return this.fInfectionLevel4c;
	}

	public void setInfectionLevel4c(double fInfectionLevel4c) {
		this.fInfectionLevel4c=fInfectionLevel4c;
	}

	public double getInfectionLevel4m() {
		return this.fInfectionLevel4m;
	}

	public void setInfectionLevel4m(double fInfectionLevel4m) {
		this.fInfectionLevel4m=fInfectionLevel4m;
	}

	public double getbednumforOSD() {
		return this.fbednumforOSD;
	}

	public void setbednumforOSD(double fbednumforOSD) {
		this.fbednumforOSD=fbednumforOSD;
	}

	public double getbednumforSSD() {
		return this.fbednumforSSD;
	}

	public void setbednumforSSD(double fbednumforSSD) {
		this.fbednumforSSD=fbednumforSSD;
	}

	public double getdaycount() {
		return this.fdaycount;
	}

	public void setdaycount(double fdaycount) {
		this.fdaycount=fdaycount;
	}

	public double getdead() {
		return this.fdead;
	}

	public void setdead(double fdead) {
		this.fdead=fdead;
	}

	public double getinfected() {
		return this.finfected;
	}

	public void setinfected(double finfected) {
		this.finfected=finfected;
	}

	public double getinfectedBaby() {
		return this.finfectedBaby;
	}

	public void setinfectedBaby(double finfectedBaby) {
		this.finfectedBaby=finfectedBaby;
	}

	public double getinfectedMiddle() {
		return this.finfectedMiddle;
	}

	public void setinfectedMiddle(double finfectedMiddle) {
		this.finfectedMiddle=finfectedMiddle;
	}

	public double getinfectedOld() {
		return this.finfectedOld;
	}

	public void setinfectedOld(double finfectedOld) {
		this.finfectedOld=finfectedOld;
	}

	public double getinfectedSchoolchild() {
		return this.finfectedSchoolchild;
	}

	public void setinfectedSchoolchild(double finfectedSchoolchild) {
		this.finfectedSchoolchild=finfectedSchoolchild;
	}

	public double getinfectedStudent() {
		return this.finfectedStudent;
	}

	public void setinfectedStudent(double finfectedStudent) {
		this.finfectedStudent=finfectedStudent;
	}

	public double getinfectedYoung() {
		return this.finfectedYoung;
	}

	public void setinfectedYoung(double finfectedYoung) {
		this.finfectedYoung=finfectedYoung;
	}

	public double getmaxvactine() {
		return this.fmaxvactine;
	}

	public void setmaxvactine(double fmaxvactine) {
		this.fmaxvactine=fmaxvactine;
	}

	public double getnaive() {
		return this.fnaive;
	}

	public void setnaive(double fnaive) {
		this.fnaive=fnaive;
	}

	public double getpatientFlowOver1() {
		return this.fpatientFlowOver1;
	}

	public void setpatientFlowOver1(double fpatientFlowOver1) {
		this.fpatientFlowOver1=fpatientFlowOver1;
	}

	public double getpatientFlowOver2() {
		return this.fpatientFlowOver2;
	}

	public void setpatientFlowOver2(double fpatientFlowOver2) {
		this.fpatientFlowOver2=fpatientFlowOver2;
	}

	public double getpatient_severe_critical() {
		return this.fpatient_severe_critical;
	}

	public void setpatient_severe_critical(double fpatient_severe_critical) {
		this.fpatient_severe_critical=fpatient_severe_critical;
	}

	public double getpatientpluusdead() {
		return this.fpatientpluusdead;
	}

	public void setpatientpluusdead(double fpatientpluusdead) {
		this.fpatientpluusdead=fpatientpluusdead;
	}

	public double gettime() {
		return this.ftime;
	}

	public void settime(double ftime) {
		this.ftime=ftime;
	}

	public double getv_ability() {
		return this.fv_ability;
	}

	public void setv_ability(double fv_ability) {
		this.fv_ability=fv_ability;
	}

	public double getv_candidate() {
		return this.fv_candidate;
	}

	public void setv_candidate(double fv_candidate) {
		this.fv_candidate=fv_candidate;
	}

	public double getvaccinated() {
		return this.fvaccinated;
	}

	public void setvaccinated(double fvaccinated) {
		this.fvaccinated=fvaccinated;
	}

	public double getvacdelay() {
		return this.fvacdelay;
	}

	public void setvacdelay(double fvacdelay) {
		this.fvacdelay=fvacdelay;
	}

	public ArrayList<Object> getcandidate() {
		return this.fcandidate;
	}

	public void setcandidate(ArrayList<Object> fcandidate) {
		this.fcandidate=fcandidate;
	}

	public ArrayList<Object> getinitia_linfected() {
		return this.finitia_linfected;
	}

	public void setinitia_linfected(ArrayList<Object> finitia_linfected) {
		this.finitia_linfected=finitia_linfected;
	}

	public ArrayList<Object> getbigoffices() {
		return this.fbigoffices;
	}

	public void setbigoffices(ArrayList<Object> fbigoffices) {
		this.fbigoffices=fbigoffices;
	}

	public ArrayList<Object> gethighschools() {
		return this.fhighschools;
	}

	public void sethighschools(ArrayList<Object> fhighschools) {
		this.fhighschools=fhighschools;
	}

	public ArrayList<Object> gethomes() {
		return this.fhomes;
	}

	public void sethomes(ArrayList<Object> fhomes) {
		this.fhomes=fhomes;
	}

	public ArrayList<Object> getmiddleoffices() {
		return this.fmiddleoffices;
	}

	public void setmiddleoffices(ArrayList<Object> fmiddleoffices) {
		this.fmiddleoffices=fmiddleoffices;
	}

	public ArrayList<Object> getprimaryschools() {
		return this.fprimaryschools;
	}

	public void setprimaryschools(ArrayList<Object> fprimaryschools) {
		this.fprimaryschools=fprimaryschools;
	}

	public ArrayList<Object> getsmalloffices() {
		return this.fsmalloffices;
	}

	public void setsmalloffices(ArrayList<Object> fsmalloffices) {
		this.fsmalloffices=fsmalloffices;
	}

	public ArrayList<Object> gettraffics() {
		return this.ftraffics;
	}

	public void settraffics(ArrayList<Object> ftraffics) {
		this.ftraffics=ftraffics;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof TModelRole))
			return false;
		TModelRole tModelRole = (TModelRole) o;
		return fpbig==tModelRole.fpbig
			&& fpiso2==tModelRole.fpiso2
			&& fpiso3==tModelRole.fpiso3
			&& fpmiddle==tModelRole.fpmiddle
			&& fv_prob==tModelRole.fv_prob
			&& fofficeclose==tModelRole.fofficeclose
			&& fschoolclose==tModelRole.fschoolclose
			&& fv_strategy==tModelRole.fv_strategy
			&& fInfectionLevel1==tModelRole.fInfectionLevel1
			&& fInfectionLevel2==tModelRole.fInfectionLevel2
			&& fInfectionLevel2m==tModelRole.fInfectionLevel2m
			&& fInfectionLevel3==tModelRole.fInfectionLevel3
			&& fInfectionLevel3s==tModelRole.fInfectionLevel3s
			&& fInfectionLevel4c==tModelRole.fInfectionLevel4c
			&& fInfectionLevel4m==tModelRole.fInfectionLevel4m
			&& fbednumforOSD==tModelRole.fbednumforOSD
			&& fbednumforSSD==tModelRole.fbednumforSSD
			&& fdaycount==tModelRole.fdaycount
			&& fdead==tModelRole.fdead
			&& finfected==tModelRole.finfected
			&& finfectedBaby==tModelRole.finfectedBaby
			&& finfectedMiddle==tModelRole.finfectedMiddle
			&& finfectedOld==tModelRole.finfectedOld
			&& finfectedSchoolchild==tModelRole.finfectedSchoolchild
			&& finfectedStudent==tModelRole.finfectedStudent
			&& finfectedYoung==tModelRole.finfectedYoung
			&& fmaxvactine==tModelRole.fmaxvactine
			&& fnaive==tModelRole.fnaive
			&& fpatientFlowOver1==tModelRole.fpatientFlowOver1
			&& fpatientFlowOver2==tModelRole.fpatientFlowOver2
			&& fpatient_severe_critical==tModelRole.fpatient_severe_critical
			&& fpatientpluusdead==tModelRole.fpatientpluusdead
			&& ftime==tModelRole.ftime
			&& fv_ability==tModelRole.fv_ability
			&& fv_candidate==tModelRole.fv_candidate
			&& fvaccinated==tModelRole.fvaccinated
			&& fvacdelay==tModelRole.fvacdelay
			&& Objects.equals(fcandidate, tModelRole.fcandidate)
			&& Objects.equals(finitia_linfected, tModelRole.finitia_linfected)
			&& Objects.equals(fbigoffices, tModelRole.fbigoffices)
			&& Objects.equals(fhighschools, tModelRole.fhighschools)
			&& Objects.equals(fhomes, tModelRole.fhomes)
			&& Objects.equals(fmiddleoffices, tModelRole.fmiddleoffices)
			&& Objects.equals(fprimaryschools, tModelRole.fprimaryschools)
			&& Objects.equals(fsmalloffices, tModelRole.fsmalloffices)
			&& Objects.equals(ftraffics, tModelRole.ftraffics);
	}

	@Override
	public String toString() {
		return "{" + " fpbig='" + getpbig() + "'"
			+ ", fpiso2='" + getpiso2() + "'"
			+ ", fpiso3='" + getpiso3() + "'"
			+ ", fpmiddle='" + getpmiddle() + "'"
			+ ", fv_prob='" + getv_prob() + "'"
			+ ", fofficeclose='" + getofficeclose() + "'"
			+ ", fschoolclose='" + getschoolclose() + "'"
			+ ", fv_strategy='" + getv_strategy() + "'"
			+ ", fInfectionLevel1='" + getInfectionLevel1() + "'"
			+ ", fInfectionLevel2='" + getInfectionLevel2() + "'"
			+ ", fInfectionLevel2m='" + getInfectionLevel2m() + "'"
			+ ", fInfectionLevel3='" + getInfectionLevel3() + "'"
			+ ", fInfectionLevel3s='" + getInfectionLevel3s() + "'"
			+ ", fInfectionLevel4c='" + getInfectionLevel4c() + "'"
			+ ", fInfectionLevel4m='" + getInfectionLevel4m() + "'"
			+ ", fbednumforOSD='" + getbednumforOSD() + "'"
			+ ", fbednumforSSD='" + getbednumforSSD() + "'"
			+ ", fdaycount='" + getdaycount() + "'"
			+ ", fdead='" + getdead() + "'"
			+ ", finfected='" + getinfected() + "'"
			+ ", finfectedBaby='" + getinfectedBaby() + "'"
			+ ", finfectedMiddle='" + getinfectedMiddle() + "'"
			+ ", finfectedOld='" + getinfectedOld() + "'"
			+ ", finfectedSchoolchild='" + getinfectedSchoolchild() + "'"
			+ ", finfectedStudent='" + getinfectedStudent() + "'"
			+ ", finfectedYoung='" + getinfectedYoung() + "'"
			+ ", fmaxvactine='" + getmaxvactine() + "'"
			+ ", fnaive='" + getnaive() + "'"
			+ ", fpatientFlowOver1='" + getpatientFlowOver1() + "'"
			+ ", fpatientFlowOver2='" + getpatientFlowOver2() + "'"
			+ ", fpatient_severe_critical='" + getpatient_severe_critical() + "'"
			+ ", fpatientpluusdead='" + getpatientpluusdead() + "'"
			+ ", ftime='" + gettime() + "'"
			+ ", fv_ability='" + getv_ability() + "'"
			+ ", fv_candidate='" + getv_candidate() + "'"
			+ ", fvaccinated='" + getvaccinated() + "'"
			+ ", fvacdelay='" + getvacdelay() + "'"
			+ ", fcandidate='" + getcandidate() + "'"
			+ ", finitia_linfected='" + getinitia_linfected() + "'"
			+ ", fbigoffices='" + getbigoffices() + "'"
			+ ", fhighschools='" + gethighschools() + "'"
			+ ", fhomes='" + gethomes() + "'"
			+ ", fmiddleoffices='" + getmiddleoffices() + "'"
			+ ", fprimaryschools='" + getprimaryschools() + "'"
			+ ", fsmalloffices='" + getsmalloffices() + "'"
			+ ", ftraffics='" + gettraffics() + "'"
			+ "}";
	}
}
