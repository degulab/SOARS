/**
 * 
 */
package soars.library.arbitrary.tepco;

/**
 * @author kurata
 *
 */
public class TimeData {

	/**
	 * 
	 */
	public int _year;

	/**
	 * 
	 */
	public int _month;

	/**
	 * 
	 */
	public int _day;

	/**
	 * 
	 */
	public int _hour;

	/**
	 * 
	 */
	static private final int[][] _dayTable = {
			{ 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
			{ 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}
		};

	/**
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 */
	public TimeData(int year, int month, int day, int hour) {
		super();
		_year = year;
		_month = month;
		_day = day;
		_hour = hour;
	}

	/**
	 * @param timeData
	 */
	public TimeData(TimeData timeData) {
		super();
		_year = timeData._year;
		_month = timeData._month;
		_day = timeData._day;
		_hour = timeData._hour;
	}

	/**
	 * 
	 */
	public void forward() {
		++_hour;
		if ( 23 < _hour) {
			_hour = 0;
			++_day;
			int leap = ( 0 == _year % 4 && 0 != _year % 100 || 0 == _year % 400) ? 1 : 0;
			if ( _dayTable[ leap][ _month] < _day) {
				_day = 1;
				++_month;
				if ( 12 < _month) {
					_month = 1;
					++_year;
				}
			}
		}
	}

	/**
	 * 
	 */
	public void backward() {
		--_hour;
		if ( 0 > _hour) {
			_hour = 23;
			--_day;
			if ( 1 > _day) {
				--_month;
				if ( 1 > _month) {
					--_year;
					_month = 12;
					_day = 31;
				} else {
					int leap = ( 0 == _year % 4 && 0 != _year % 100 || 0 == _year % 400) ? 1 : 0;
					_day = _dayTable[ leap][ _month];
				}
			}
		}
	}
}
