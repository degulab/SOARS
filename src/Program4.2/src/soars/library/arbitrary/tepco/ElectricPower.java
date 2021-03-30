/**
 * 
 */
package soars.library.arbitrary.tepco;

import java.awt.Component;
import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import soars.common.utility.swing.progress.IProgressCallback;
import soars.common.utility.swing.progress.ProgressDlg;
import soars.common.utility.tool.reflection.Reflection;
import soars.plugin.modelbuilder.chart.chartmanager.ChartManager;
import util.DoubleValue;
import util.IntValue;
import env.Agent;
import env.ObjectLoader;
import env.Spot;

/**
 * @author kurata
 *
 */
public class ElectricPower implements IProgressCallback {

	/**
	 * 
	 */
	private TepcoData _tepcoData = new TepcoData();

	/**
	 * 
	 */
	private TimeData _timeData = null;

	/**
	 * 
	 */
	private int _days = 7;

	/**
	 * 
	 */
	private double _time;

	/**
	 * 
	 */
	public ElectricPower() {
		super();
	}

	/**
	 * @return
	 */
	public int get_usage() {
		return _tepcoData._usage;
	}

	/**
	 * @return
	 */
	public int get_capacity() {
		return _tepcoData._capacity;
	}

	/**
	 * @param agent
	 * @param capacity
	 * @param demand
	 * @param ratio
	 * @param time
	 * @param chart
	 * @param days
	 * @return
	 */
	public boolean initialize(Agent agent, String capacity, String demand, String ratio, String time, String chart, int days) {
		_days = days;
		return initialize( agent, capacity, demand, ratio, time, chart);
	}

	/**
	 * @param agent
	 * @param capacity
	 * @param demand
	 * @param ratio
	 * @param time
	 * @param chart
	 * @return
	 */
	public boolean initialize(Agent agent, String capacity, String demand, String ratio, String time, String chart) {
		List resultList = new ArrayList();
		if ( !Reflection.execute_static_method( "soars.application.simulator.main.MainFrame", "get_instance", new Class[] {}, new Object[] {}, resultList))
			return false;

		if ( resultList.isEmpty() || null == resultList.get( 0) || !( resultList.get( 0) instanceof Frame))
			return false;

		Spot spot = Spot.forName( chart);
		ChartManager chartManager = spot.getEquip( chart);

		if ( !ProgressDlg.execute( ( Frame)resultList.get( 0),
			"Getting data ...", true, "initialize", "Cancel",
			new Object[] { agent, capacity, demand, ratio, time, chartManager},
			this, ( Component)resultList.get( 0)))
			return false;

		TimeData timeData = new TimeData( _timeData);
		timeData.forward();
		ObjectLoader.envStartTime( "0/" + String.format( "%02d", timeData._hour) + ":00");

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.progress.IProgressCallback#progress_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.progress.ProgressDlg)
	 */
	public boolean progress_callback(String id, Object[] objects, ProgressDlg progressDlg) {
		if ( !id.equals( "initialize"))
			return false;

		Agent agent = ( Agent)objects[ 0];
		String capacity = ( String)objects[ 1];
		String demand = ( String)objects[ 2];
		String ratio = ( String)objects[ 3];
		String time = ( String)objects[ 4];
		ChartManager chartManager = ( ChartManager)objects[ 5];

		TimeData timeData = new TimeData( Calendar.getInstance().get( Calendar.YEAR),
			Calendar.getInstance().get( Calendar.MONTH) + 1,
			Calendar.getInstance().get( Calendar.DATE),
			Calendar.getInstance().get( Calendar.HOUR_OF_DAY));

		boolean in = false;
		int hours = ( 24 * _days + timeData._hour + 1);
		List<TimeData> timeDataList = new ArrayList<TimeData>();
		Map<TimeData, TepcoData> tepcoDataMap = new HashMap<TimeData, TepcoData>();
		for ( int i = 0; i < hours; ++i) {
			if ( progressDlg._canceled)
				return false;

			String address = "http://tepco-usage-api.appspot.com/"
				+ String.valueOf( timeData._year) + "/"
				+ String.valueOf( timeData._month) + "/"
				+ String.valueOf( timeData._day) + "/"
				+ String.valueOf( timeData._hour) + ".json";
			//System.out.println( address);
			if ( !get( address)) {
				if ( !in) {
					timeData.backward();
					progressDlg.set( ( int)( 100.0 * ( double)( i + 1) / ( double)hours));
					continue;
				} else {
					JOptionPane.showMessageDialog( null,
						"Could not get \"" + address + "\"!",
						"Error!",
						JOptionPane.ERROR_MESSAGE);
					//System.out.println( "Could not get \"" + address + "\"!");
					return false;
				}
			}

			if ( !in && 0 >= _tepcoData._usage) {
				timeData.backward();
				progressDlg.set( ( int)( 100.0 * ( double)( i + 1) / ( double)hours));
				continue;
			}

			if ( !in) {
				_timeData = timeData;
				in = true;
			}

			timeData = new TimeData( timeData);
			timeDataList.add( timeData);
			tepcoDataMap.put( timeData, new TepcoData( _tepcoData));

			timeData.backward();

			progressDlg.set( ( int)( 100.0 * ( double)( i + 1) / ( double)hours));
		}

		if ( null == _timeData)
			return false;

		//System.out.println( year + ":" + month + ":" + day + ":" + hour + ":" + minute);

		int days = ( timeDataList.size() / 24);
		for ( int i = timeDataList.size() - 1; 0 <= i; --i) {
			_time = ( _timeData._hour >= i)
				? ( ( double)( _timeData._hour - i) / 24.0)
				: -( ( double)days * ( double)( i - _timeData._hour) / ( double)( 24 * days));
				//System.out.println( "i = " + i + ", hour = " + _timeData._hour + ", time = " + _time);
			try {
				chartManager.append( 1, _time, tepcoDataMap.get( timeDataList.get( i))._usage, true);
				chartManager.append( 2, _time, tepcoDataMap.get( timeDataList.get( i))._capacity, true);
			} catch (Exception e) {
				System.out.println( "Could not append data!");
				return false;
			}
		}

		IntValue intValue = new IntValue();
		intValue.setInteger( tepcoDataMap.get( timeDataList.get( 0))._capacity);
		agent.setEquip( capacity, intValue);

		intValue = new IntValue();
		intValue.setInteger( tepcoDataMap.get( timeDataList.get( 0))._usage);
		agent.setEquip( demand, intValue);

		DoubleValue doubleValue = new DoubleValue();
		doubleValue.setDouble( 100.0 * ( double)tepcoDataMap.get( timeDataList.get( 0))._usage / ( double)tepcoDataMap.get( timeDataList.get( 0))._capacity);
		agent.setEquip( ratio, doubleValue);

		doubleValue = new DoubleValue();
		doubleValue.setDouble( _time);
		agent.setEquip( time, doubleValue);

		return true;
	}

	/**
	 * @return
	 */
	public boolean get() {
		if ( null == _timeData)
			return false;

		_timeData.forward();

		while ( true) {
			String address = "http://tepco-usage-api.appspot.com/"
				+ String.valueOf( _timeData._year) + "/"
				+ String.valueOf( _timeData._month) + "/"
				+ String.valueOf( _timeData._day) + "/"
				+ String.valueOf( _timeData._hour) + ".json";
			//System.out.println( address);
			if ( get( address) && 0 < _tepcoData._usage)
				break;

			try {
				Thread.sleep( 300000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return true;
//		return get( "http://tepco-usage-api.appspot.com/latest.json");
	}

	/**
	 * @param address
	 * @return
	 */
	private boolean get(String address) {
		URL url;
		try {
			url = new URL( address);
			HttpURLConnection httpURLConnection = ( HttpURLConnection)url.openConnection();
			httpURLConnection.setRequestMethod( "GET");
			httpURLConnection.setInstanceFollowRedirects( false);
			httpURLConnection.connect();
			InputStream inputStream = new BufferedInputStream( httpURLConnection.getInputStream());
			String data = read( inputStream/*, httpURLConnection.getContentLength()*/);
			if ( null == data) {
				inputStream.close();
				return false;
			}
			inputStream.close();

			JSONObject jsonObject = new JSONObject( data);
			if ( !_tepcoData.set( jsonObject))
				return false;

			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param inputStream
	 * @param size 
	 * @return
	 */
	private String read(InputStream inputStream/*, int size*/) {
		List<Byte> list = new ArrayList<Byte>();
		int c;
		try {
			long length = 0;
			while ( -1 != ( c = inputStream.read())) {
				list.add( Byte.valueOf( ( byte)c));
				//if ( 0 < size)
				//	System.out.println( ++length * 100 / size);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		if ( list.isEmpty())
			return null;

		byte[] bytes = new byte[ list.size()];
		for ( int i = 0; i < list.size(); ++i) {
			Byte b = ( Byte)list.get( i);
			bytes[ i] = b.byteValue();
		}

		return new String( bytes);
	}

	/**
	 * 
	 */
	private void print() {
		System.out.println( "saving=" + _tepcoData._saving);
		System.out.println( "hour=" + _tepcoData._hour);
		System.out.println( "capacity_updated=" + _tepcoData._capacity_updated);
		System.out.println( "month=" + _tepcoData._month);
		System.out.println( "usage_updated=" + _tepcoData._usage_updated);
		System.out.println( "entryfor=" + _tepcoData._entryfor);
		System.out.println( "year=" + _tepcoData._year);
		System.out.println( "usage=" + _tepcoData._usage);
		System.out.println( "capacity=" + _tepcoData._capacity);
		System.out.println( "day=" + _tepcoData._day);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ElectricPower electricPower = new ElectricPower();
		if ( !electricPower.get()) {
			System.out.println( "!Error!");
		}
		electricPower.print();
	}
}
