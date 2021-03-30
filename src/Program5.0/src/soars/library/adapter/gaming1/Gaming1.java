/**
 * 
 */
package soars.library.adapter.gaming1;

import env.EquippedObject;
import net.sf.json.JSONObject;
import soars.library.adapter.userrules.UserRuleUtility;

/**
 * @author kurata
 *
 */
public class Gaming1 {

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 */
	public static void get(EquippedObject equippedObject, String arg0, String arg1, String arg2, String arg3, String arg4) {
		String payload = UserRuleUtility.getString( equippedObject, arg1);
		JSONObject jsonObject = JSONObject.fromObject( payload);
		UserRuleUtility.set( equippedObject, arg2, Integer.valueOf( jsonObject.getString( "stage")));
		UserRuleUtility.set( equippedObject, arg3, Integer.valueOf( jsonObject.getString( "growth")));
		UserRuleUtility.set( equippedObject, arg4, Double.valueOf( jsonObject.getString( "scale")));
	}
}
