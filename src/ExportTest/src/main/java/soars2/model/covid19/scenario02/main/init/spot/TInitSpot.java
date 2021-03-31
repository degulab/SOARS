package soars2.model.covid19.scenario02.main.init.spot;

import soars2.core.TSpot;
import soars2.core.TSpotManager;
import soars2.model.covid19.scenario02.TSpotTypes;
import soars2.model.covid19.scenario02.spot.TBigofficeRole;
import soars2.model.covid19.scenario02.spot.TModelRole;
import soars2.utils.random2013.ICRandom;

/**
 * 
 */
public class TInitSpot {

	// Entities : [bigoffice]

	public static boolean init1(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addFirstSpot bigoffices
		modelRole.getbigoffices().add(0,spot);
		return true;
	}

	// Entities : [yard, DEAD, hospital]

	public static boolean init2(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		return true;
	}

	// Entities : [smalloffice]

	public static boolean init3(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addFirstSpot smalloffices
		modelRole.getsmalloffices().add(0,spot);
		return true;
	}

	// Entities : [middleoffice]

	public static boolean init4(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addFirstSpot middleoffices
		modelRole.getmiddleoffices().add(0,spot);
		return true;
	}

	// Entities : [highschool]

	public static boolean init5(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addFirstSpot highschools
		modelRole.gethighschools().add(0,spot);
		return true;
	}

	// Entities : [primaryschool]

	public static boolean init6(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addFirstSpot primaryschools
		modelRole.getprimaryschools().add(0,spot);
		return true;
	}

	// Entities : [traffic]

	public static boolean init7(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addFirstSpot traffics
		modelRole.gettraffics().add(0,spot);
		return true;
	}

	// Entities : [10home]

	public static boolean init8(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		return true;
	}

	// Entities : [9home]

	public static boolean init9(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		return true;
	}

	// Entities : [8home]

	public static boolean init10(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		return true;
	}

	// Entities : [7home]

	public static boolean init11(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		return true;
	}

	// Entities : [6home]

	public static boolean init12(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		return true;
	}

	// Entities : [5home]

	public static boolean init13(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		return true;
	}

	// Entities : [4home]

	public static boolean init14(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		return true;
	}

	// Entities : [3home]

	public static boolean init15(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		return true;
	}

	// Entities : [1home]

	public static boolean init16(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		return true;
	}

	// Entities : [2home]

	public static boolean init17(TSpot spot, TBigofficeRole currentSpotRole, TSpotManager spotManager, ICRandom rand) {
		TModelRole modelRole = (TModelRole) spotManager.getSpotDB().get(TSpotTypes.MODEL).getBaseRole();
		// command : substitution : <>askEquip SpotAF=(0.0)*(0.0)
		currentSpotRole.setSpotAF(0.0*0.0);
		// command : substitution : <>askEquip VD=1/(0.0)
		currentSpotRole.setVD(1/0.0);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		// command : list : <model>addLastSpot homes
		modelRole.gethomes().add(spot);
		return true;
	}
}
