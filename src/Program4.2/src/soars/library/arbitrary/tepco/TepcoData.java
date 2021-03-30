/**
 * 
 */
package soars.library.arbitrary.tepco;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author kurata
 *
 */
public class TepcoData {

	/**
	 * 
	 */
	public boolean _saving;							// false

	/**
	 * 
	 */
	public int _hour;										// 10 

	/**
	 * 
	 */
	public String _capacity_updated;			// "2011-03-23 16:05:00"

	/**
	 * 
	 */
	public int _month;										// 3

	/**
	 * 
	 */
	public String _usage_updated;				// "2011-03-24 02:05:32"

	/**
	 * 
	 */
	public String _entryfor;							// "2011-03-24 01:00:00"

	/**
	 * 
	 */
	public String _capacity_peak_period;	// 18 null

	/**
	 * 
	 */
	public int _year;										// 2011

	/**
	 * 
	 */
	public int _usage;										// 3720

	/**
	 * 
	 */
	public int _capacity;								// 3850

	/**
	 * 
	 */
	public int _day;											// 24

	/**
	 * 
	 */
	public TepcoData() {
		super();
	}

	/**
	 * @param tepcoData
	 */
	public TepcoData(TepcoData tepcoData) {
		super();
		_saving = tepcoData._saving;
		_hour = tepcoData._hour;
		_capacity_updated = tepcoData._capacity_updated;
		_month = tepcoData._month;
		_usage_updated = tepcoData._usage_updated;
		_entryfor = tepcoData._entryfor;
		_year = tepcoData._year;
		_usage = tepcoData._usage;
		_capacity = tepcoData._capacity;
		_day = tepcoData._day;
	}

	/**
	 * @param jsonObject
	 * @return
	 */
	public boolean set(JSONObject jsonObject) {
		try {
			_saving = jsonObject.getBoolean( "saving");
			_hour = jsonObject.getInt( "hour");
			_capacity_updated = jsonObject.getString( "capacity_updated");
			_month = jsonObject.getInt( "month");
			_usage_updated = jsonObject.getString( "usage_updated");
			_entryfor = jsonObject.getString( "entryfor");
			_year = jsonObject.getInt( "year");
			_usage = jsonObject.getInt( "usage");
			_capacity = jsonObject.getInt( "capacity");
			_day = jsonObject.getInt( "day");
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
