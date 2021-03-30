/*
 * 2005/04/22
 */
package soars.application.simulator.file.loader.legacy;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import soars.application.simulator.data.ChartData;
import soars.application.simulator.data.Dataset;
import soars.application.simulator.data.LogData;
import soars.application.simulator.main.MainFrame;

/**
 * @author kurata
 */
public class LegacySaxLoader extends DefaultHandler {

	/**
	 * 
	 */
	static private boolean _result;

	/**
	 * 
	 */
	private String _filename = "";

	/**
	 * 
	 */
	private String _simulator_window_title = "";

	/**
	 * 
	 */
	private String _simulator_window_time = "";

	/**
	 * 
	 */
	private String _log_viewer_window_title = "";

	/**
	 * 
	 */
	private Rectangle _log_viewer_window_rectangle = new Rectangle();

	/**
	 * 
	 */
	private String _name = "";

	/**
	 * 
	 */
	private String _value = "";

	/**
	 * 
	 */
	private String _type = "";

	/**
	 * 
	 */
	private String _console = "";

	/**
	 * 
	 */
	private List _agents = new ArrayList();

	/**
	 * 
	 */
	private List _spots = new ArrayList();

	/**
	 * 
	 */
	private String _stdout = "";

	/**
	 * 
	 */
	private String _stderr = "";

	/**
	 * 
	 */
	private ChartData _chartData = null;

	/**
	 * 
	 */
	private List _chartDatas = new ArrayList();

	/**
	 * 
	 */
	private Dataset _dataset = null;

	/**
	 * 
	 */
	private String _graphic_properties = "";

	/**
	 * 
	 */
	private String _chart_properties = "";

	/**
	 * @param file
	 * @return
	 */
	public static boolean execute(File file) {
		_result = false;

		LegacySaxLoader legacySaxLoader = new LegacySaxLoader( file.getName());

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse( file, legacySaxLoader);
			legacySaxLoader.at_end_of_load();
		} catch (Exception e) {
			e.printStackTrace();
			legacySaxLoader.at_end_of_load();
			return false;
		}
		return _result;
	}

	/**
	 * 
	 */
	private void at_end_of_load() {
		if ( !_result)
			return;
	}

	/**
	 * @param filename 
	 * 
	 */
	public LegacySaxLoader(String filename) {
		super();
		_filename = filename;
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement( String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {

		super.startElement(arg0, arg1, arg2, arg3);

		if ( arg2.equals( "simulator_data")) {
			on_simulator_data( arg3);
			_result = true;
			return;
		}

		if ( !_result)
			return;

		if ( arg2.equals( "log_viewer_data"))
			on_log_viewer_data( arg3);
		else if ( arg2.equals( "console"))
			on_console( arg3);
		else if ( arg2.equals( "agents"))
			on_agents( arg3);
		else if ( arg2.equals( "spots"))
			on_spots( arg3);
		else if ( arg2.equals( "stdout"))
			on_stdout( arg3);
		else if ( arg2.equals( "stderr"))
			on_stderr( arg3);
		else if ( arg2.startsWith( "log"))
			on_log( arg2, arg3);
		else if ( arg2.equals( "chart_data"))
			on_chart_data( arg3);
		else if ( arg2.equals( "xRange"))
			on_xRange( arg3);
		else if ( arg2.equals( "yRange"))
			on_yRange( arg3);
		else if ( arg2.equals( "dataset"))
			on_dataset( arg3);
		else if ( arg2.equals( "graphic_properties_data"))
			on_graphic_properties_data( arg3);
		else if ( arg2.equals( "chart_properties_data"))
			on_chart_properties_data( arg3);
	}

	/**
	 * @param attributes
	 */
	private void on_simulator_data(Attributes attributes) {
		String value = attributes.getValue( "title");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		_simulator_window_title = value;


		value = attributes.getValue( "time");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		_simulator_window_time = value;
	}

	/**
	 * @param attributes
	 */
	private void on_log_viewer_data(Attributes attributes) {
		String value = attributes.getValue( "title");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		_log_viewer_window_title = value;

		value = attributes.getValue( "x");
		_log_viewer_window_rectangle.x = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "y");
		_log_viewer_window_rectangle.y = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "width");
		_log_viewer_window_rectangle.width = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "height");
		_log_viewer_window_rectangle.height = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));
	}

	/**
	 * @param attributes
	 */
	private void on_console(Attributes attributes) {
		_name = "console";
		_value = "";
	}

	/**
	 * @param attributes
	 */
	private void on_agents(Attributes attributes) {
		_type = "agents";
	}

	/**
	 * @param attributes
	 */
	private void on_spots(Attributes attributes) {
		_type = "spots";
	}

	/**
	 * @param attributes
	 */
	private void on_stdout(Attributes attributes) {
		_name = "stdout";
		_value = "";
	}

	/**
	 * @param attributes
	 */
	private void on_stderr(Attributes attributes) {
		_name = "stderr";
		_value = "";
	}

	/**
	 * @param qName
	 * @param attributes
	 */
	private void on_log(String qName, Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		_name = name;
		_value = "";
	}

	/**
	 * @param attributes
	 */
	private void on_chart_data(Attributes attributes) {
		if ( null != _chartData) {
			_result = false;
			return;
		}

		_chartData = new ChartData();

		String value = attributes.getValue( "name");
		if ( null == value)
			value = "";

		_chartData._name = value;

		value = attributes.getValue( "title");
		if ( null == value || value.equals( ""))
			return;

		_chartData._title = value;

		value = attributes.getValue( "xLabel");
		if ( null == value || value.equals( ""))
			return;

		_chartData._XLabel = value;

		value = attributes.getValue( "yLabel");
		if ( null == value || value.equals( ""))
			return;

		_chartData._YLabel = value;

		value = attributes.getValue( "x");
		_chartData._window_rectangle.x = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "y");
		_chartData._window_rectangle.y = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "width");
		_chartData._window_rectangle.width = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "height");
		_chartData._window_rectangle.height = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));
	}

	/**
	 * @param attributes
	 */
	private void on_xRange(Attributes attributes) {
		if ( null == _chartData) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "min");
		if ( null == value || value.equals( ""))
			return;

		double min = Double.valueOf( value).doubleValue();

		value = attributes.getValue( "max");
		if ( null == value || value.equals( ""))
			return;

		double max = Double.valueOf( value).doubleValue();

		_chartData._XRange = true;
		_chartData._XRangeMin = min;
		_chartData._XRangeMax = max;
	}

	/**
	 * @param attributes
	 */
	private void on_yRange(Attributes attributes) {
		if ( null == _chartData) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "min");
		if ( null == value || value.equals( ""))
			return;

		double min = Double.valueOf( value).doubleValue();

		value = attributes.getValue( "max");
		if ( null == value || value.equals( ""))
			return;

		double max = Double.valueOf( value).doubleValue();

		_chartData._YRange = true;
		_chartData._YRangeMin = min;
		_chartData._YRangeMax = max;
	}

	/**
	 * @param attributes
	 */
	private void on_dataset(Attributes attributes) {
		if ( null == _chartData) {
			_result = false;
			return;
		}

		if ( null != _dataset) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "id");
		if ( null == value || value.equals( ""))
			return;

		int id = Integer.parseInt( value);

		value = attributes.getValue( "legend");
		if ( null == value /*|| value.equals( "")*/)
			return;

		_dataset = new Dataset( id, value);

		_value = "";
	}

	/**
	 * @param attributes
	 */
	private void on_graphic_properties_data(Attributes attributes) {
		_value = "";
	}

	/**
	 * @param attributes
	 */
	private void on_chart_properties_data(Attributes attributes) {
		_value = "";
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		if ( !_result)
			return;

		String value = new String( arg0, arg1, arg2);
		_value += value;
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String arg0, String arg1, String arg2) throws SAXException {

		if ( !_result) {
			super.endElement(arg0, arg1, arg2);
			return;
		}

		if ( arg2.equals( "simulator_data"))
			on_simulator_data();
		else if ( arg2.equals( "log_viewer_data"))
			on_log_viewer_data();
		else if ( arg2.equals( "console"))
			on_console();
		else if ( arg2.equals( "agents"))
			on_agents();
		else if ( arg2.equals( "spots"))
			on_spots();
		else if ( arg2.equals( "stdout"))
			on_stdout();
		else if ( arg2.equals( "stderr"))
			on_stderr();
		else if ( arg2.startsWith( "log"))
			on_log();
		else if ( arg2.equals( "chart_data"))
			on_chart_data();
		else if ( arg2.equals( "xRange"))
			on_xRange();
		else if ( arg2.equals( "yRange"))
			on_yRange();
		else if ( arg2.equals( "dataset"))
			on_dataset();
		else if ( arg2.equals( "graphic_properties_data"))
			on_graphic_properties_data();
		else if ( arg2.equals( "chart_properties_data"))
			on_chart_properties_data();

		super.endElement(arg0, arg1, arg2);
	}

	/**
	 * 
	 */
	private void on_simulator_data() {
		if ( !MainFrame.get_instance().load(
			_filename,
			_simulator_window_title,
			_simulator_window_time,
			_log_viewer_window_title,
			_log_viewer_window_rectangle,
			_console, _agents, _spots, _stdout, _stderr, _chartDatas, _graphic_properties, _chart_properties))
			_result = false;
	}

	/**
	 * 
	 */
	private void on_log_viewer_data() {
		_name = "";
		_value = "";
		_type = "";
	}

	/**
	 * 
	 */
	private void on_console() {
		if ( !_name.equals( "console")) {
			_result = false;
			return;
		}

		_console = _value;
		_name = "";
		_value = "";
	}

	/**
	 * 
	 */
	private void on_agents() {
		if ( !_type.equals( "agents")) {
			_result = false;
			return;
		}

		_type = "";
	}

	/**
	 * 
	 */
	private void on_spots() {
		if ( !_type.equals( "spots")) {
			_result = false;
			return;
		}

		_type = "";
	}

	/**
	 * 
	 */
	private void on_stdout() {
		if ( !_name.equals( "stdout")) {
			_result = false;
			return;
		}

		_stdout = _value;
		_name = "";
		_value = "";
	}

	/**
	 * 
	 */
	private void on_stderr() {
		if ( !_name.equals( "stderr")) {
			_result = false;
			return;
		}

		_stderr = _value;
		_name = "";
		_value = "";
	}

	/**
	 * 
	 */
	private void on_log() {
		if ( _type.equals( "agents"))
			_agents.add( new LogData( _name, _value));
		else if ( _type.equals( "spots"))
			_spots.add( new LogData( _name, _value));
		else {
			_result = false;
			return;
		}

		_name = "";
		_value = "";
	}

	/**
	 * 
	 */
	private void on_chart_data() {
		if ( null == _chartData) {
			_result = false;
			return;
		}

		_chartDatas.add( _chartData);

		_chartData = null;
	}

	/**
	 * 
	 */
	private void on_xRange() {
	}

	/**
	 * 
	 */
	private void on_yRange() {
	}

	/**
	 * 
	 */
	private void on_dataset() {
		if ( null == _chartData) {
			_result = false;
			return;
		}

		if ( null == _dataset) {
			_result = false;
			return;
		}

		_dataset._value = _value;
		_chartData._datasets.add( _dataset);

		_dataset = null;
		_value = "";
	}

	/**
	 * 
	 */
	private void on_graphic_properties_data() {
		_graphic_properties = _value;
		_value = "";
	}

	/**
	 * 
	 */
	private void on_chart_properties_data() {
		_chart_properties = _value;
		_value = "";
	}
}
