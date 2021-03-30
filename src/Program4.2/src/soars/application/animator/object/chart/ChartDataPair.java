/**
 * 
 */
package soars.application.animator.object.chart;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.object.player.ObjectManager;
import soars.common.utility.xml.sax.Writer;
import soars.plugin.modelbuilder.chart.chart.main.ChartFrame;

/**
 * @author kurata
 *
 */
public class ChartDataPair {

	/**
	 * 
	 */
	public int _dataset = 0;

	/**
	 * 
	 */
	public ChartData[] _chartData = null;

	/**
	 * If true, a line is drawn to connect to the previous point.
	 */
	public boolean _connect = true;

	/**
	 * If true, a line is drawn.
	 */
	public boolean _visible = true;

	/**
	 * 
	 */
	public ChartObject _parent = null;

	/**
	 * @param dataset
	 * @param chartData
	 * @param connect
	 * @param show
	 * @param parent
	 */
	public ChartDataPair(int dataset, ChartData[] chartData, boolean connect, boolean show, ChartObject parent) {
		super();
		_dataset = dataset;
		_chartData = chartData;
		_connect = connect;
		_visible = show;
		_parent = parent;
	}

	/**
	 * @param chartDataPair
	 * @param parent
	 */
	public ChartDataPair(ChartDataPair chartDataPair, ChartObject parent) {
		super();
		_dataset = chartDataPair._dataset;
		_chartData = new ChartData[] { new ChartData( chartDataPair._chartData[ 0]), new ChartData( chartDataPair._chartData[ 1])};
		_connect = chartDataPair._connect;
		_visible = chartDataPair._visible;
		_parent = parent;
	}

	/**
	 * @param chartFrame
	 * @param chart_log_directory
	 * @return
	 */
	public boolean load(ChartFrame chartFrame, File chart_log_directory) {
		if ( !_visible)
			return true;

		if ( !read( chartFrame, chart_log_directory))
			return false;

		chartFrame.addLegend( _dataset, getLegend());

		return true;
	}

	/**
	 * @param chartFrame
	 * @param chart_log_directory
	 * @return
	 */
	private boolean read(ChartFrame chartFrame, File chart_log_directory) {
		File file = new File( chart_log_directory, chartFrame._name + "_" + String.valueOf( _dataset) + ".log");
		if ( !file.exists() || !file.canRead())
			return false;

		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile( file, "r");
			while ( true) {
				double x, y;
				try {
					x = randomAccessFile.readDouble();
				} catch (EOFException e) {
					//e.printStackTrace();
					break;
				}
				try {
					y = randomAccessFile.readDouble();
				} catch (EOFException e) {
					e.printStackTrace();
					randomAccessFile.close();
					return false;
				}
				chartFrame.append( _dataset, x, y, _connect);
			}
			randomAccessFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @return
	 */
	public String getLegend() {
		return ( _chartData[ 0].getLegendWord() + " - " + _chartData[ 1].getLegendWord());
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "dataset", "", String.valueOf( _dataset));
		attributesImpl.addAttribute( null, null, "connect", "", _connect ? "true" : "false");
		attributesImpl.addAttribute( null, null, "show", "", _visible ? "true" : "false");
		_chartData[ 0].write( attributesImpl, 0);
		_chartData[ 1].write( attributesImpl, 1);
		writer.writeElement( null, null, "chart_data", attributesImpl);
		return true;
	}

	/**
	 * @param chartFrame
	 * @param indication_dataset
	 * @param ratio 
	 */
	public void indicate(ChartFrame chartFrame, int indication_dataset, double ratio) {
		if ( !_visible)
			return;

		chartFrame.indicate( _dataset, indication_dataset, ratio);
	}

	/**
	 * @param chartFrame
	 * @param chartDataPair
	 * @return
	 */
	public boolean update(ChartFrame chartFrame, ChartDataPair chartDataPair) {
		if ( _visible == chartDataPair._visible)
			return true;

		_visible = chartDataPair._visible;

		if ( _visible)
			return read( chartFrame, ObjectManager.get_instance().get_chart_directory());
		else {
			chartFrame.clear( _dataset);
			chartFrame.removeLegend( _dataset);
		}

		return true;
	}

	/**
	 * @param chartFrame
	 */
	public void add_legend(ChartFrame chartFrame) {
		chartFrame.addLegend( _dataset, getLegend());
	}

	/**
	 * @param name 
	 * @param points
	 */
	public void debug(String name, Vector points) {
		if ( !_visible)
			return;

		Vector plotPoints = ( Vector)points.get( _dataset);
		System.out.println( "name=" + name + " , dataset=" + String.valueOf( _dataset) + " " + plotPoints.size());
	}
}
