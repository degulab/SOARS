/**
 * 
 */
package soars.library.adapter.exchangealgebra;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import env.EquippedObject;
import exalge2.ExTransfer;
import exalge2.Exalge;
import exalge2.io.csv.CsvFormatException;
import soars.library.adapter.userrules.Unit;
import soars.library.adapter.userrules.UserRuleUtility;
import ssac.aadl.runtime.AADLFunctions;

/**
 * @author kurata
 *
 */
public class Extransfer {

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 * @throws CsvFormatException 
	 * @throws FileNotFoundException 
	 */
	public static void extransfer(EquippedObject equippedObject, String arg0, String arg1, String arg2, String arg3) throws FileNotFoundException, CsvFormatException, UnsupportedEncodingException, IOException {
		ExTransfer exTransfer = ( ExTransfer)Unit.getInstance( equippedObject, arg1);
		Exalge exalgeIN = ( Exalge)Unit.getInstance( equippedObject, arg2);
		//Exalge exalgeOUT = ( Exalge)Unit.getInstance( equippedObject, arg3);
		//String characterCode = UserRuleUtility.getString( equippedObject, arg4);
		//ExTransfer exTransfer = ExTransfer.fromCSV( new File( path), characterCode);
		Exalge exalgeOUT = AADLFunctions.transfer( exalgeIN, exTransfer);
		UserRuleUtility.setInstance( equippedObject, arg3, exalgeOUT);
	}
}
