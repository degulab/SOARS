/**
 * 
 */
package soars.application.animator.object.chart;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class ChartObjectMap extends HashMap {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private ChartObjectMap _chartObjectMap = null;

	/**
	 * 
	 */
	public String[] _actual_end_time = new String[] { "0", "00", "00"};

	/**
	 * 
	 */
	public String[] _start_time = new String[] { "0", "00", "00"};

	/**
	 * 
	 */
	public String[] _step_time = new String[] { "0", "00", "00"};

	/**
	 * 
	 */
	public String[] _end_time = new String[] { "0", "00", "00"};

	/**
	 * 
	 */
	public String[] _log_step_time = new String[] { "0", "00", "00"};

	/**
	 * 
	 */
	public boolean _export_end_time = true;

	/**
	 * 
	 */
	public boolean _export_log_step_time = false;

	/**
	 * Returns the instance of this class.
	 * @return the instance of this class
	 */
	public static ChartObjectMap get_instance() {
		synchronized( _lock) {
			if ( null == _chartObjectMap) {
				_chartObjectMap = new ChartObjectMap();
			}
		}
		return _chartObjectMap;
	}

	/**
	 * 
	 */
	public ChartObjectMap() {
		super();
	}

	/**
	 * @return
	 */
	public ChartObjectMap(ChartObjectMap chartObjectMap) {
		super();
		System.arraycopy( chartObjectMap._actual_end_time, 0, _actual_end_time, 0, _actual_end_time.length);
		System.arraycopy( chartObjectMap._start_time, 0, _start_time, 0, _start_time.length);
		System.arraycopy( chartObjectMap._step_time, 0, _step_time, 0, _step_time.length);
		System.arraycopy( chartObjectMap._end_time, 0, _end_time, 0, _end_time.length);
		System.arraycopy( chartObjectMap._log_step_time, 0, _log_step_time, 0, _log_step_time.length);
		_export_end_time = chartObjectMap._export_end_time;
		_export_log_step_time = chartObjectMap._export_log_step_time;
		Iterator iterator = chartObjectMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			put( chartObject._name, new ChartObject( chartObject));
		}
	}

	/**
	 * @param chart_properties_file
	 * @param chart_log_directory
	 * @return
	 */
	public boolean create_chartFrames(File chart_properties_file, File chart_log_directory) {
		if ( !chart_properties_file.exists() || !chart_properties_file.canRead()
			|| !chart_log_directory.exists() || !chart_log_directory.isDirectory())
			return true;

		String chart_properties = FileUtility.read_text_from_file( chart_properties_file, "UTF-8");
		if ( null == chart_properties)
			return false;

		String[] lines = chart_properties.split( "\n");
		if ( null == lines)
			return false;

		if ( 8 > lines.length)
			return true;

		_actual_end_time = lines[ 0].split( ",");
		_start_time = lines[ 1].split( ",");
		_step_time = lines[ 2].split( ",");
		_end_time = lines[ 3].split( ",");
		_log_step_time = lines[ 4].split( ",");
		_export_end_time = lines[ 5].equals( "true");
		_export_log_step_time = lines[ 6].equals( "true");

		for ( int i = 7; i < lines.length; ++i) {
			if ( !create_chartFrames( lines[ i], chart_log_directory))
				return false;
		}

		return true;
	}

	/**
	 * @param line
	 * @param chart_log_directory
	 * @return
	 */
	private boolean create_chartFrames(String line, File chart_log_directory) {
		String[] words = line.split( "\t");
		if ( null == words || 13 != words.length)
			return false;

		ChartObject chartObject = ( ChartObject)get( words[ 0]);
		if ( null != chartObject) {
			if ( !chartObject.append( words, chart_log_directory))
				return false;
		} else {
			chartObject = new ChartObject( words);
			if ( !chartObject.setup( words, chart_log_directory))
				return false;

			put( words[ 0], chartObject);
		}

		return true;
	}

	/**
	 * 
	 */
	public void bring_chartFrames_to_top() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			chartObject._chartFrame.toFront();
		}
	}

	/**
	 * 
	 */
	public void cleanup() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			chartObject.cleanup();
		}
		clear();
	}

	/**
	 * @param chartObjectMap
	 * @param chart_log_directory
	 * @return
	 */
	public boolean set(ChartObjectMap chartObjectMap, File chart_log_directory) {
		_actual_end_time = chartObjectMap._actual_end_time;
		_start_time = chartObjectMap._start_time;
		_step_time = chartObjectMap._step_time;
		_end_time = chartObjectMap._end_time;
		_log_step_time = chartObjectMap._log_step_time;
		_export_end_time = chartObjectMap._export_end_time;
		_export_log_step_time = chartObjectMap._export_log_step_time;

		Iterator iterator = chartObjectMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			if ( !chartObject.setup( chartObject._name, chart_log_directory))
				return false;

			put( chartObject._name, chartObject);
		}

		return true;
	}

	/**
	 * 
	 */
	public void initialize_chartFrames_rectangle() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			chartObject.initialize_chartFrames_rectangle();
		}
	}

	/**
	 * 
	 */
	public void update_chartFrames_rectangle() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			chartObject.update_chartFrames_rectangle();
		}
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		if ( isEmpty())
			return true;

		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "actual_end_time", "", _actual_end_time[ 0] + "," + _actual_end_time[ 1] + "," + _actual_end_time[ 2]);
		attributesImpl.addAttribute( null, null, "start_time", "", _start_time[ 0] + "," + _start_time[ 1] + "," + _start_time[ 2]);
		attributesImpl.addAttribute( null, null, "step_time", "", _step_time[ 0] + "," + _step_time[ 1] + "," + _step_time[ 2]);
		attributesImpl.addAttribute( null, null, "end_time", "", _end_time[ 0] + "," + _end_time[ 1] + "," + _end_time[ 2]);
		attributesImpl.addAttribute( null, null, "log_step_time", "", _log_step_time[ 0] + "," + _log_step_time[ 1] + "," + _log_step_time[ 2]);
		attributesImpl.addAttribute( null, null, "export_end_time", "", _export_end_time ? "true" : "false");
		attributesImpl.addAttribute( null, null, "export_log_step_time", "", _export_log_step_time ? "true" : "false");

		writer.startElement( null, null, "chart", attributesImpl);

		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			if ( !chartObject.write( writer))
				return false;
		}

		writer.endElement( null, null, "chart");

		return true;
	}

	/**
	 * @param current_time
	 */
	public void indicate(double current_time) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			chartObject.indicate( ( current_time - get_time( _start_time)) / ( get_time( _actual_end_time) - get_time( _start_time)));
		}
	}

	/**
	 * @param times
	 * @return
	 */
	private double get_time(String[] times) {
		return ( 24.0f * 60.0f * Double.parseDouble( times[ 0]) + 60.0f * Double.parseDouble( times[ 1]) + Double.parseDouble( times[ 2]));
	}

	/**
	 * 
	 */
	public void clear_indication() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			chartObject.clear_indication();
		}
	}

	/**
	 * @param chartObjectMap
	 * @return
	 */
	public boolean update(ChartObjectMap chartObjectMap) {
		Iterator iterator = chartObjectMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject co = ( ChartObject)entry.getValue();
			ChartObject chartObject = ( ChartObject)get( co._name);
			if ( null == chartObject)
				continue;

			if ( !chartObject.update( co))
				return false;
		}
		return true;
	}

	/**
	 * 
	 */
	public void debug() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			chartObject.debug();
		}
	}
}
