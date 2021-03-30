/**
 * 
 */
package soars.library.adapter.exchangealgebra;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;

import org.soars.exalge.util.ExalgeFactory;
import org.soars.exalge.util.ExalgeMath;

import env.EquippedObject;
import exalge2.ExBase;
import exalge2.Exalge;
import soars.library.adapter.userrules.Unit;
import soars.library.adapter.userrules.UserRuleUtility;

/**
 * @author kurata
 *
 */
public class ExchangeAlgebra {

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public static void projection(EquippedObject equippedObject, String arg0, String arg1, String arg2, String arg3) {
		//Exalge exalge1 = ( Exalge)Unit.getInstance( equippedObject, arg1);
		Exalge exalge = ( Exalge)Unit.getInstance( equippedObject, arg2);
		String base = UserRuleUtility.getString( equippedObject, arg3);
		exalge = ExalgeMath.projection( exalge, base);
		UserRuleUtility.setInstance( equippedObject, arg1, exalge);
		//exalge1 = exalge2.projection( new ExBase( base));
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 */
	public static void projection(EquippedObject equippedObject, String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7) {
		//Exalge exalge1 = ( Exalge)Unit.getInstance( equippedObject, arg1);
		Exalge exalge = ( Exalge)Unit.getInstance( equippedObject, arg2);
		String name = UserRuleUtility.getString( equippedObject, arg3);
		String hat = UserRuleUtility.getString( equippedObject, arg4);
		String unit = UserRuleUtility.getString( equippedObject, arg5);
		String time = UserRuleUtility.getTimeString( equippedObject, arg6);
		String subject = UserRuleUtility.getString( equippedObject, arg7);
		exalge = exalge.projection( new ExBase( name, hat, unit, time, subject));
		UserRuleUtility.setInstance( equippedObject, arg1, exalge);
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public static void plus(EquippedObject equippedObject, String arg0, String arg1, String arg2, String arg3) {
		Exalge exalge = ( Exalge)Unit.getInstance( equippedObject, arg1);
		double value = UserRuleUtility.getDoubleValue( equippedObject, arg2);
		String base = UserRuleUtility.getString( equippedObject, arg3);
		exalge = ExalgeMath.plus( exalge, value, base);
		UserRuleUtility.setInstance( equippedObject, arg1, exalge);
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 */
	public static void plus(EquippedObject equippedObject, String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7) {
		Exalge exalge = ( Exalge)Unit.getInstance( equippedObject, arg1);
		double value = UserRuleUtility.getDoubleValue( equippedObject, arg2);
		String name = UserRuleUtility.getString( equippedObject, arg3);
		String hat = UserRuleUtility.getString( equippedObject, arg4);
		String unit = UserRuleUtility.getString( equippedObject, arg5);
		String time = UserRuleUtility.getTimeString( equippedObject, arg6);
		String subject = UserRuleUtility.getString( equippedObject, arg7);
		exalge = exalge.plus( new ExBase( name, hat, unit, time, subject), new BigDecimal( value));
		UserRuleUtility.setInstance( equippedObject, arg1, exalge);
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public static void plus(EquippedObject equippedObject, String arg0, String arg1, String arg2) {
		Exalge exalge1 = ( Exalge)Unit.getInstance( equippedObject, arg1);
		Exalge exalge2 = ( Exalge)Unit.getInstance( equippedObject, arg2);
		Exalge exalge = exalge1.plus( exalge2);
		UserRuleUtility.setInstance( equippedObject, arg1, exalge);
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 */
	public static void clear(EquippedObject equippedObject, String arg0, String arg1) {
		Exalge exalge = ( Exalge)Unit.getInstance( equippedObject, arg1);
		exalge = ExalgeFactory.newExalge();
		UserRuleUtility.setInstance( equippedObject, arg1, exalge);
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 */
	public static void bar(EquippedObject equippedObject, String arg0, String arg1) {
		Exalge exalge = ( Exalge)Unit.getInstance( equippedObject, arg1);
		exalge = exalge.bar();
		UserRuleUtility.setInstance( equippedObject, arg1, exalge);
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public static void multiple(EquippedObject equippedObject, String arg0, String arg1, String arg2) {
		Exalge exalge = ( Exalge)Unit.getInstance( equippedObject, arg1);
		double value = UserRuleUtility.getDoubleValue( equippedObject, arg2);
		exalge = exalge.multiple( new BigDecimal( value));
		UserRuleUtility.setInstance( equippedObject, arg1, exalge);
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 */
	public static void hat(EquippedObject equippedObject, String arg0, String arg1) {
		Exalge exalge = ( Exalge)Unit.getInstance( equippedObject, arg1);
		exalge = exalge.hat();
		UserRuleUtility.setInstance( equippedObject, arg1, exalge);
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public static void copy(EquippedObject equippedObject, String arg0, String arg1, String arg2) {
		//Exalge exalge1 = ( Exalge)Unit.getInstance( equippedObject, arg1);
		Exalge exalge2 = ( Exalge)Unit.getInstance( equippedObject, arg2);
		Exalge exalge = exalge2.copy();
		UserRuleUtility.setInstance( equippedObject, arg1, exalge);
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public static void projectionValue(EquippedObject equippedObject, String arg0, String arg1, String arg2, String arg3) {
		//double value = UserRuleUtility.getDoubleValue( equippedObject, arg1);
		Exalge exalge = ( Exalge)Unit.getInstance( equippedObject, arg2);
		String base = UserRuleUtility.getString( equippedObject, arg3);
		UserRuleUtility.set( equippedObject, arg1, ExalgeMath.getValue( exalge, base));
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 */
	public static void projectionValue(EquippedObject equippedObject, String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7) {
		//double value = UserRuleUtility.getDoubleValue( equippedObject, arg1);
		Exalge exalge = ( Exalge)Unit.getInstance( equippedObject, arg2);
		String name = UserRuleUtility.getString( equippedObject, arg3);
		String hat = UserRuleUtility.getString( equippedObject, arg4);
		String unit = UserRuleUtility.getString( equippedObject, arg5);
		String time = UserRuleUtility.getTimeString( equippedObject, arg6);
		String subject = UserRuleUtility.getString( equippedObject, arg7);
		UserRuleUtility.set( equippedObject, arg1, exalge.get( new ExBase( name, hat, unit, time, subject)).doubleValue());
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public static void norm(EquippedObject equippedObject, String arg0, String arg1, String arg2) {
		//double value = UserRuleUtility.getDoubleValue( equippedObject, arg1);
		Exalge exalge = ( Exalge)Unit.getInstance( equippedObject, arg2);
		UserRuleUtility.set( equippedObject, arg1, exalge.norm().doubleValue());
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 */
	public static void create(EquippedObject equippedObject, String arg0, String arg1) {
		//Exalge exalge = ( Exalge)Unit.getInstance( equippedObject, arg1);
		//exalge = new Exalge();
		//exalge = ExalgeFactory.toExalge();
		UserRuleUtility.setInstance( equippedObject, arg1, new Exalge());
	}

	/**
	 * @param message
	 */
	private static void debug(String message) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( new File( "data.txt")));
			outputStreamWriter.write( message);
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}
