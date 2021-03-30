/**
 * 
 */
package soars.application.animator.object.chart;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.main.MainFrame;
import soars.application.animator.object.player.ObjectManager;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.xml.sax.Writer;
import soars.plugin.modelbuilder.chart.chart.main.ChartFrame;

/**
 * @author kurata
 *
 */
public class ChartObject {

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * Title of this chart.
	 */
	public String _title = "";

	/**
	 * Name for the horizontal axis.
	 */
	public String _horizontal_axis = "";

	/**
	 * Name for the vertical axis.
	 */
	public String _vertical_axis = "";

	/**
	 * Array for the pairs of the NumberObjectData objects.
	 */
	public List _chartDataPairs = new ArrayList();

	/**
	 * 
	 */
	public ChartFrame _chartFrame = null;

	/**
	 * 
	 */
	public Rectangle _rectangle = null;

	/**
	 * 
	 */
	static private final int _indication_dataset = 0;

	/**
	 * @param words 
	 */
	public ChartObject(String[] words) {
		super();
		_name = words[ 0];
		_title = words[ 8];
		_horizontal_axis = words[ 9];
		_vertical_axis = words[ 10];
	}

	/**
	 * @param name
	 * @param title
	 * @param horizontal_axis
	 * @param vertical_axis
	 * @param rectangle 
	 */
	public ChartObject(String name, String title, String horizontal_axis, String vertical_axis, Rectangle rectangle) {
		super();
		_name = name;
		_title = title;
		_horizontal_axis = horizontal_axis;
		_vertical_axis = vertical_axis;
		_rectangle = rectangle;
	}

	/**
	 * _chartFrame and _rectangle are not copied!
	 * @param chartObject
	 */
	public ChartObject(ChartObject chartObject) {
		super();
		_name = chartObject._name;
		_title = chartObject._title;
		_horizontal_axis = chartObject._horizontal_axis;
		_vertical_axis = chartObject._vertical_axis;
		for ( int i = 0; i < chartObject._chartDataPairs.size(); ++i) {
			ChartDataPair chartDataPair = ( ChartDataPair)chartObject._chartDataPairs.get( i);
			_chartDataPairs.add( new ChartDataPair( chartDataPair, this));
		}
	}

	/**
	 * Invoked from ObjectManager's import_data( ... ) method.
	 * @param words
	 * @param chart_log_directory
	 * @return
	 */
	public boolean setup(String[] words, File chart_log_directory) {
		if ( null != _chartFrame)
			return false;

		_chartFrame = new ChartFrame( MainFrame.get_instance(), JFrame.DO_NOTHING_ON_CLOSE);
		if ( !_chartFrame.create( words[ 0]))
			return false;

		_chartFrame.setTitle( _title);
		_chartFrame.setXLabel( _horizontal_axis);
		_chartFrame.setYLabel( _vertical_axis);

		return append( words, chart_log_directory);
	}

	/**
	 * Invoked from ObjectManager's import_data( ... ) method.
	 * @param words
	 * @param chart_log_directory
	 * @return
	 */
	public boolean append(String[] words, File chart_log_directory) {
		if ( null == _chartFrame)
			return false;

		int index = Integer.parseInt( words[ 1]);
		ChartDataPair chartDataPair = new ChartDataPair(
			index,
			new ChartData[] { new ChartData( words[ 2], words[ 3], words[ 4]),
				new ChartData( words[ 5], words[ 6], words[ 7])},
			words[ 11].equals( "true"), words[ 12].equals( "true"), this);

		if ( !chartDataPair.load( _chartFrame, chart_log_directory))
			return false;

		_chartDataPairs.add( chartDataPair);

		return true;
	}

	/**
	 * Invoked from SaxLoader's at_end_of_load() method.
	 * @param name
	 * @param chart_log_directory
	 * @return
	 */
	public boolean setup(String name, File chart_log_directory) {
		if ( null != _chartFrame)
			return false;

		_chartFrame = new ChartFrame( MainFrame.get_instance(), JFrame.DO_NOTHING_ON_CLOSE);
		if ( !_chartFrame.create( name))
			return false;

		_chartFrame.setTitle( _title);
		_chartFrame.setXLabel( _horizontal_axis);
		_chartFrame.setYLabel( _vertical_axis);

		for ( int i = 0; i < _chartDataPairs.size(); ++i) {
			ChartDataPair chartDataPair = ( ChartDataPair)_chartDataPairs.get( i);
			if ( !chartDataPair.load( _chartFrame, chart_log_directory))
				return false;
		}

		if ( null != _rectangle) {
			if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( _rectangle)
				|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _rectangle).width <= 10
				|| _rectangle.y <= -_chartFrame.getInsets().top
				|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _rectangle).height <= _chartFrame.getInsets().top)
				_rectangle = null;
			else
				_chartFrame.setBounds( _rectangle);
		}

		_chartFrame.setVisible( visible());

		return true;
	}

	/**
	 * 
	 */
	public void initialize_chartFrames_rectangle() {
		if ( null == _rectangle) {
			_rectangle = _chartFrame.getBounds();
			ObjectManager.get_instance().modified();
		}

		set_sensor();
	}

	/**
	 * 
	 */
	private void set_sensor() {
		// TODO Auto-generated method stub
		_chartFrame.addComponentListener( new ComponentListener() {
			public void componentHidden(ComponentEvent e) {
			}
			public void componentMoved(ComponentEvent e) {
//				System.out.println( "moved!\n");
//				if ( null != _rectangle && !_chartFrame.getSize().equals( _rectangle.getSize()))
//					return;
//
//				on_changed();
				ObjectManager.get_instance().modified();
			}
			public void componentResized(ComponentEvent e) {
//				System.out.println( "resized!\n");
//				on_changed();
				ObjectManager.get_instance().modified();
			}
			public void componentShown(ComponentEvent e) {
			}
		});
		_chartFrame.addWindowStateListener( new WindowStateListener() {
			public void windowStateChanged(WindowEvent e) {
//				System.out.println( "windowStateChanged");
//				if ( Frame.MAXIMIZED_BOTH == ( _chartFrame.getExtendedState() & Frame.MAXIMIZED_BOTH))
//					System.out.println( "Max!\n");
//
//				if ( Frame.ICONIFIED == ( _chartFrame.getExtendedState() & Frame.ICONIFIED))
//					System.out.println( "Min!\n");

				if ( Frame.MAXIMIZED_BOTH == ( _chartFrame.getExtendedState() & Frame.MAXIMIZED_BOTH)
					|| Frame.ICONIFIED == ( _chartFrame.getExtendedState() & Frame.ICONIFIED))
					ObjectManager.get_instance().modified();
			}
		});
	}

//	/**
//	 * 
//	 */
//	private void on_changed() {
//		if ( Frame.MAXIMIZED_BOTH == ( _chartFrame.getExtendedState() & Frame.MAXIMIZED_BOTH)
//			|| Frame.ICONIFIED == ( _chartFrame.getExtendedState() & Frame.ICONIFIED))
////		if ( Frame.NORMAL != ( _chartFrame.getExtendedState() & Frame.NORMAL))
//			return;
//
//		System.out.println( _chartFrame.getLocation().toString());
//		System.out.println( _chartFrame.getSize().toString());
//		//System.out.println( _chartFrame.getBounds().toString());
//		System.out.println( _rectangle.toString() + "\n");
//		if ( null != _rectangle && _chartFrame.getBounds().equals( _rectangle))
//			return;
//
//		ObjectManager.get_instance().modified();
//	}

	/**
	 * 
	 */
	public void update_chartFrames_rectangle() {
		_rectangle = _chartFrame.getBounds();
	}

	/**
	 * 
	 */
	public void cleanup() {
		if ( null != _chartFrame) {
			_chartFrame.dispose();
			_chartFrame = null;
		}
		_chartDataPairs.clear();
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		attributesImpl.addAttribute( null, null, "title", "", Writer.escapeAttributeCharData( _title));
		attributesImpl.addAttribute( null, null, "horizontal_axis", "", Writer.escapeAttributeCharData( _horizontal_axis));
		attributesImpl.addAttribute( null, null, "vertical_axis", "", Writer.escapeAttributeCharData( _vertical_axis));

		_rectangle = _chartFrame.getBounds();
		attributesImpl.addAttribute( null, null, "x", "", String.valueOf( _rectangle.x));
		attributesImpl.addAttribute( null, null, "y", "", String.valueOf( _rectangle.y));
		attributesImpl.addAttribute( null, null, "width", "", String.valueOf( _rectangle.width));
		attributesImpl.addAttribute( null, null, "height", "", String.valueOf( _rectangle.height));

		writer.startElement( null, null, "chart_frame", attributesImpl);

		for ( int i = 0; i < _chartDataPairs.size(); ++i) {
			ChartDataPair chartDataPair = ( ChartDataPair)_chartDataPairs.get( i);
			if ( !chartDataPair.write( writer))
				return false;
		}

		writer.endElement( null, null, "chart_frame");

		return true;
	}

	/**
	 * @param ratio
	 */
	public void indicate(double ratio) {
		_chartFrame.clear( _indication_dataset);
		for ( int i = 0; i < _chartDataPairs.size(); ++i) {
			ChartDataPair chartDataPair = ( ChartDataPair)_chartDataPairs.get( i);
			chartDataPair.indicate( _chartFrame, _indication_dataset, ratio);
		}
	}

	/**
	 * 
	 */
	public void clear_indication() {
		_chartFrame.clear( _indication_dataset);
	}

	/**
	 * @return
	 */
	public boolean visible() {
		for ( int i = 0; i < _chartDataPairs.size(); ++i) {
			ChartDataPair chartDataPair = ( ChartDataPair)_chartDataPairs.get( i);
			if ( chartDataPair._visible)
				return true;
		}
		return false;
	}

	/**
	 * @param visible
	 */
	public void visible(boolean visible) {
		for ( int i = 0; i < _chartDataPairs.size(); ++i) {
			ChartDataPair chartDataPair = ( ChartDataPair)_chartDataPairs.get( i);
			chartDataPair._visible = visible;
		}
	}

	/**
	 * @param chartObject
	 * @return
	 */
	public boolean update(ChartObject chartObject) {
		for ( int i = 0; i < _chartDataPairs.size(); ++i) {
			ChartDataPair chartDataPair = ( ChartDataPair)_chartDataPairs.get( i);
			if ( !chartDataPair.update( _chartFrame, ( ChartDataPair)chartObject._chartDataPairs.get( i)))
				return false;
		}

		_chartFrame.clearLegends();

		for ( int i = 0; i < _chartDataPairs.size(); ++i) {
			ChartDataPair chartDataPair = ( ChartDataPair)_chartDataPairs.get( i);
			if ( !chartDataPair._visible)
				continue;

			_chartFrame.addLegend( chartDataPair._dataset, chartDataPair.getLegend());
		}

		_chartFrame.repaint();

		_rectangle = _chartFrame.getBounds();

		_chartFrame.setVisible( visible());

		return true;
	}

	/**
	 * 
	 */
	public void debug() {
		for ( int i = 0; i < _chartDataPairs.size(); ++i) {
			ChartDataPair chartDataPair = ( ChartDataPair)_chartDataPairs.get( i);
			chartDataPair.debug( _name, _chartFrame.get_points());
		}
	}
}
