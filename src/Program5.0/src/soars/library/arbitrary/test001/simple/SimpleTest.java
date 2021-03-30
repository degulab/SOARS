/**
 * 
 */
package soars.library.arbitrary.test001.simple;

/**
 * @author kurata
 *
 */
public class SimpleTest {

	/**
	 * 
	 */
	private String _text = "";

	/**
	 * 
	 */
	private int _value1 = 0;

	/**
	 * 
	 */
	private long _value2 = 0;

	/**
	 * 
	 */
	private float _value3 = 0.0f;

	/**
	 * 
	 */
	private double _value4 = 0.0;

	/**
	 * @param text
	 * @param value1
	 * @param value2
	 * @param value3
	 * @param value4
	 * @return
	 */
	static public SimpleTest create(String text, int value1, long value2, float value3, double value4) {
		return new SimpleTest( text, value1, value2, value3, value4);
	}

	/**
	 * 
	 */
	public SimpleTest() {
	}

	/**
	 * @param text
	 * @param value1
	 * @param value2
	 * @param value3
	 * @param value4
	 */
	public SimpleTest(String text, int value1, long value2, float value3, double value4) {
		set( text, value1, value2, value3, value4);
	}

	/**
	 * @param text
	 * @param value1
	 * @param value2
	 * @param value3
	 * @param value4
	 */
	public void set(String text, int value1, long value2, float value3, double value4) {
		_text = text;
		_value1 = value1;
		_value2 = value2;
		_value3 = value3;
		_value4 = value4;
	}

	/**
	 * @return
	 */
	public String getText() {
		return _text;
	}

	/**
	 * @return
	 */
	public int getValue1() {
		return _value1;
	}

	/**
	 * @return
	 */
	public long getValue2() {
		return _value2;
	}

	/**
	 * @return
	 */
	public float getValue3() {
		return _value3;
	}

	/**
	 * @return
	 */
	public double getValue4() {
		return _value4;
	}

	/**
	 * @return
	 */
	public boolean getTrue() {
		return true;
	}

	/**
	 * @return
	 */
	public boolean getFalse() {
		return false;
	}
}
