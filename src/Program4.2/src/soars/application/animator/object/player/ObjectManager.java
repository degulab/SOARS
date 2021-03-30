/*
 * 2005/01/31
 */
package soars.application.animator.object.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.common.image.AnimatorImageManager;
import soars.application.animator.file.exporter.GraphicDataSaxWriter;
import soars.application.animator.file.importer.GraphicDataSaxLoader;
import soars.application.animator.file.importer.Importer;
import soars.application.animator.file.importer.legacy.LegacyGraphicDataSaxLoader;
import soars.application.animator.file.loader.SaxLoader;
import soars.application.animator.file.loader.legacy.LegacySaxLoader;
import soars.application.animator.file.writer.SaxWriter;
import soars.application.animator.main.AnimatorView;
import soars.application.animator.main.Application;
import soars.application.animator.main.Constant;
import soars.application.animator.main.Environment;
import soars.application.animator.main.MainFrame;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.chart.ChartObjectMap;
import soars.application.animator.object.player.agent.AgentObject;
import soars.application.animator.object.player.agent.AgentObjectManager;
import soars.application.animator.object.player.base.ObjectBase;
import soars.application.animator.object.player.base.edit.objects.EditObjectsDlg;
import soars.application.animator.object.player.base.edit.objects.ObjectComparator;
import soars.application.animator.object.player.spot.ISpotObjectManipulator;
import soars.application.animator.object.player.spot.SpotObject;
import soars.application.animator.object.player.spot.SpotObjectManager;
import soars.application.animator.object.player.spot.SpotPositionComparator;
import soars.application.animator.object.property.agent.AgentPropertyManager;
import soars.application.animator.object.property.spot.SpotPropertyManager;
import soars.application.animator.object.scenario.ScenarioManager;
import soars.application.animator.observer.Observer;
import soars.application.animator.setting.common.CommonProperty;
import soars.application.animator.setting.common.EditCommonPropertyDlg;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.image.ImageProperty;
import soars.common.utility.swing.image.ImagePropertyManager;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.tool.sort.QuickSort;
import soars.common.utility.xml.sax.Writer;

/**
* The all object manager class.
 * @author kurata / SOARS project
 */
public class ObjectManager implements IMessageCallback {

	/**
	 * Name of the root directory for Animator data.
	 */
	static public final String _rootDirectoryName = "animator";

	/**
	 * Name of the data file.
	 */
	static public final String _dataFilename = "data.aml";

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private ObjectManager _objectManager = null;

	/**
	 * 
	 */
	private BufferedImage _bufferedImage = null;

	/**
	 * 
	 */
	private Color _backgroundColor = new Color( 255, 255, 255);

	/**
	 * 
	 */
	private Color _rubberbandColor = new Color( 0, 0, 0);

	/**
	 * 
	 */
	private File _currentFile = null;

	/**
	 * 
	 */
	private File _parentDirectory = null;

	/**
	 * 
	 */
	private File _rootDirectory = null;

	/**
	 * 
	 */
	private boolean _modified = false;

	/**
	 * 
	 */
	private Dimension _preferredSize = new Dimension();

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null == _objectManager) {
				_objectManager = new ObjectManager( ( Graphics2D)AnimatorView.get_instance().getGraphics());
			}
		}
	}

	/**
	 * Returns the instance of the all object manager class.
	 * @return the instance of the all object manager class
	 */
	public static ObjectManager get_instance() {
		if ( null == _objectManager) {
			System.exit( 0);
		}

		return _objectManager;
	}

	/**
	 * Creates the all object manager class.
	 * @param graphics2D the graphics object of JAVA
	 */
	public ObjectManager(Graphics2D graphics2D) {
		super();
	}

	/**
	 * Returns true if the data file exists.
	 * @return true if the data file exists
	 */
	public boolean exist_datafile() {
		return ( null == _currentFile) ? false : true;
	}

	/**
	 * Returns the current data file.
	 * @return the current data file
	 */
	public File get_current_file() {
		return _currentFile;
	}

	/**
	 * Returns true if the image directory exists.
	 * @return true if the image directory exists
	 */
	public boolean exist_image_directory() {
		// TODO Auto-generated method stub
		if ( null == _parentDirectory || null == _rootDirectory)
			return false;

		File directory = new File( _rootDirectory, Constant._imageDirectory);
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the image directory.
	 * @return the image directory
	 */
	public File get_image_directory() {
		return get_image_directory( _rootDirectory);
	}

	/**
	 * Returns the image directory.
	 * @param root_directory the root directory
	 * @return the image directory
	 */
	public File get_image_directory(File root_directory) {
		File directory = new File( root_directory, Constant._imageDirectory);
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * Returns true if the thumbnail directory exists.
	 * @return true if the thumbnail directory exists
	 */
	public boolean exist_thumbnail_image_directory() {
		// TODO Auto-generated method stub
		if ( null == _parentDirectory || null == _rootDirectory)
			return false;

		File directory = new File( _rootDirectory, Constant._thumbnailImageDirectory);
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the thumbnail directory.
	 * @return the thumbnail directory
	 */
	public File get_thumbnail_image_directory() {
		return get_thumbnail_image_directory( _rootDirectory);
	}

	/**
	 * Returns the thumbnail directory.
	 * @param root_directory the root directory
	 * @return the thumbnail directory
	 */
	public File get_thumbnail_image_directory(File root_directory) {
		File directory = new File( root_directory, Constant._thumbnailImageDirectory);
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * Returns the chart directory.
	 * @return the chart directory
	 */
	public File get_chart_directory() {
		return get_chart_directory( _rootDirectory);
	}

	/**
	 * Returns the chart directory.
	 * @param root_directory the root directory
	 * @return the chart directory
	 */
	public File get_chart_directory(File root_directory) {
		File directory = new File( root_directory, Constant._chartLogDirectory);
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * Invoked when the data is changed.
	 */
	public void modified() {
		if ( Application._demo)
			return;

		_modified = true;

		String title = ResourceManager.get( "application.title")
			+ ( ( null == _currentFile) ? "" : ( " - " + _currentFile.getName()))
			+ ResourceManager.get( "state.edit.modified");
		if ( !MainFrame.get_instance().getTitle().equals( title))
			MainFrame.get_instance().setTitle( title);
	}

	/**
	 * Returns true if the data is changed.
	 * @return true if the data is changed
	 */
	public boolean isModified() {
		return ( Application._demo ? false : _modified);
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		AgentObjectManager.get_instance().cleanup();
		SpotObjectManager.get_instance().cleanup();
		ScenarioManager.get_instance().cleanup();
		CommonProperty.get_instance().cleanup();
		AnimatorImageManager.get_instance().cleanup();
		ImagePropertyManager.get_instance().cleanup();
		ChartObjectMap.get_instance().cleanup();
		_currentFile = null;
		_modified = false;
		_preferredSize.setSize( 0, 0);

		if ( null != _parentDirectory)
			FileUtility.delete( _parentDirectory, true);

		_rootDirectory = null;
		_parentDirectory = null;
	}

	/**
	 * Updates the size of the animation area.
	 * @param component the base class for all Swing components
	 */
	public void set_preferred_size(JComponent component) {
		update_preferred_size();
		component.setPreferredSize( _preferredSize);
		component.updateUI();
	}

	/**
	 * Updates the size of the animation area.
	 */
	public void update_preferred_size() {
		SpotObjectManager.get_instance().update_preferred_size( _preferredSize,
			SpotPropertyManager.get_instance().get_max_image_size(),
			AgentObjectManager.get_instance().get_max_image_size(),
			ScenarioManager.get_instance()._headerObject._max);
	}

	/**
	 * Updates the size of the animation area.
	 * @param component the base class for all Swing components
	 */
	public void update_preferred_size(JComponent component) {
		Dimension preferredSize = new Dimension();
		SpotObjectManager.get_instance().update_preferred_size( preferredSize,
			SpotPropertyManager.get_instance().get_max_image_size(),
			AgentObjectManager.get_instance().get_max_image_size(),
			ScenarioManager.get_instance()._headerObject._max);
		if ( !_preferredSize.equals( preferredSize)) {
			_preferredSize.setSize( preferredSize);
			component.setPreferredSize( _preferredSize);
			component.updateUI();
		}
	}

	/**
	 * Returns the size of the animation area.
	 * @return the size of the animation area
	 */
	public Dimension get_preferred_size() {
		return _preferredSize;
	}

	/**
	 * Returns the size of the animation area.
	 * @return the size of the animation area
	 */
	public Dimension get_size() {
		Dimension dimension = new Dimension();
		SpotObjectManager.get_instance().get_size( dimension,
			SpotPropertyManager.get_instance().get_max_image_size(),
			AgentObjectManager.get_instance().get_max_image_size(),
			ScenarioManager.get_instance()._headerObject._max);
		return dimension;
	}

	/**
	 * Changes the buffer size for animation.
	 * @param component the base class for all Swing components
	 */
	public void resize(JComponent component) {
		_bufferedImage = new BufferedImage( component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * Prepares for animation.
	 * @param component the base class for all Swing components
	 */
	public void prepare_for_animation(JComponent component) {
		MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get( "application.title"), true,
			"prepare_for_animation", ResourceManager.get( "file.open.show.message"),
			new Object[] { component}, this, MainFrame.get_instance());
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "prepare_for_animation"))
			on_prepare_for_animation( ( JComponent)objects[ 0]);

		return false;
	}

	/**
	 * Prepares for animation.
	 * @param component the base class for all Swing components
	 */
	private void on_prepare_for_animation(JComponent component) {
		ScenarioManager.get_instance().reset();
		ScenarioManager.get_instance().read();

		Dimension dimension = get_size();

//		SpotObjectManager.get_instance().prepare_for_animation(
//			dimension, SpotPropertyManager.get_instance().get_max_image_size(), component);
//		AgentObjectManager.get_instance().prepare_for_animation(
//			AgentPropertyManager.get_instance().get_max_image_size(), component);

		_bufferedImage = new BufferedImage( dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);

		component.setPreferredSize( dimension);
		component.updateUI();
	}

	/**
	 * Draws the objects in the visible rectangle.
	 * @param graphics the graphics object of JAVA
	 * @param component the base class for all Swing components
	 * @param rectangle the visible rectangle
	 * @param startPoint the start point of the rubber band
	 * @param endPoint the end point of the rubber band
	 */
	public void draw(Graphics graphics, JComponent component, Rectangle rectangle, Point startPoint, Point endPoint) {
		//System.out.println( rectangle.x + ", " + rectangle.y + ", " + rectangle.width + ", " + rectangle.height);
		synchronized( _lock) {
			if ( null == _bufferedImage)
				resize( component);

			Graphics2D graphics2D = ( Graphics2D)_bufferedImage.getGraphics();
			graphics2D.setBackground( _backgroundColor);
			graphics2D.clearRect( rectangle.x, rectangle.y, rectangle.width, rectangle.height);
//			graphics2D.clearRect( 0, 0, _bufferedImage.getWidth(), _bufferedImage.getHeight());

			if ( null != startPoint) {
				graphics2D.setColor( _rubberbandColor);
				graphics2D.drawRect(
					Math.min( startPoint.x, endPoint.x),
					Math.min( startPoint.y, endPoint.y),
					Math.abs( startPoint.x - endPoint.x),
					Math.abs( startPoint.y - endPoint.y));
			}

			SpotObjectManager.get_instance().draw( graphics2D, rectangle, component);
			AgentObjectManager.get_instance().draw( graphics2D, rectangle, component);
			graphics.drawImage( _bufferedImage, rectangle.x, rectangle.y,
				rectangle.x + Math.min( rectangle.width, _bufferedImage.getWidth()),
				rectangle.y + Math.min( rectangle.height, _bufferedImage.getHeight()),
				rectangle.x, rectangle.y,
				rectangle.x + Math.min( rectangle.width, _bufferedImage.getWidth()),
				rectangle.y + Math.min( rectangle.height, _bufferedImage.getHeight()), component);
//			graphics.drawImage( _bufferedImage, rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height,
//				rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, component);
//			graphics.drawImage( _bufferedImage, 0, 0, component);
			graphics2D.dispose();
		}
	}

	/**
	 * Animates the objects in the visible rectangle.
	 * @param graphics the graphics object of JAVA
	 * @param rectangle the visible rectangle
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 * @param next true for loading the next scenario
	 */
	public void animate(Graphics graphics, Rectangle rectangle, ImageObserver imageObserver, boolean next) {
		//System.out.println( rectangle.x + ", " + rectangle.y + ", " + rectangle.width + ", " + rectangle.height);
		Graphics2D graphics2D = ( Graphics2D)_bufferedImage.getGraphics();
		graphics2D.setBackground( _backgroundColor);
		graphics2D.clearRect( rectangle.x, rectangle.y, rectangle.width, rectangle.height);
//		graphics2D.clearRect( 0, 0, _bufferedImage.getWidth(), _bufferedImage.getHeight());
		SpotObjectManager.get_instance().animate( graphics2D, rectangle, imageObserver);
		AgentObjectManager.get_instance().animate( graphics2D, rectangle, imageObserver, next);
		graphics.drawImage( _bufferedImage, rectangle.x, rectangle.y,
			rectangle.x + Math.min( rectangle.width, _bufferedImage.getWidth()),
			rectangle.y + Math.min( rectangle.height, _bufferedImage.getHeight()),
			rectangle.x, rectangle.y,
			rectangle.x + Math.min( rectangle.width, _bufferedImage.getWidth()),
			rectangle.y + Math.min( rectangle.height, _bufferedImage.getHeight()), imageObserver);
//		graphics.drawImage( _bufferedImage, rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height,
//			rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, imageObserver);
//		graphics.drawImage( _bufferedImage, 0, 0, imageObserver);
		graphics2D.dispose();
	}

	/**
	 * Returns true if loading the data from the specified file successfully.
	 * @param file the specified file
	 * @param graphics2D the graphics object of JAVA
	 * @return true if loading the data from the specified file successfully
	 */
	public boolean load(File file, Graphics2D graphics2D) {
		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		if ( !ZipUtility.decompress( file, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		cleanup();

		File rootDirectory = new File( parentDirectory, _rootDirectoryName);
		if ( !rootDirectory.exists() || !rootDirectory.isDirectory()) {
			rootDirectory = new File( parentDirectory, _rootDirectoryName);
			if ( !rootDirectory.mkdirs()) {
				FileUtility.delete( parentDirectory, true);
				return false;
			}

			_parentDirectory = parentDirectory;
			_rootDirectory = rootDirectory;

			if ( !LegacySaxLoader.execute( file, _rootDirectory, graphics2D)) {
				cleanup();
				return false;
			}
		} else {
			_parentDirectory = parentDirectory;
			_rootDirectory = rootDirectory;

			File datafile = get_datafile( _rootDirectory);
			if ( null == datafile)
				return false;

			if ( !SaxLoader.execute( datafile, graphics2D)) {
				cleanup();
				return false;
			}

			_currentFile = file;
		}

		update_preferred_size();

		System.gc();

//		ChartObjectMap.get_instance().initialize_chartFrames_rectangle();

		return true;
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private File get_datafile(File rootDirectory) {
		// TODO Auto-generated method stub
		File datafile = new File( rootDirectory, _dataFilename);
		if ( !datafile.exists() || !datafile.isFile() || !datafile.canRead()) {
			datafile = new File( rootDirectory, "data.xml");
			if ( !datafile.exists() || !datafile.isFile() || !datafile.canRead())
				return null;
		}
		return datafile;
	}

	/**
	 * Returns true if saving the data to the current file successfully.
	 * @return true if saving the data to the current file successfully
	 */
	public boolean save() {
		if ( null == _currentFile)
			return false;

		return save_as( _currentFile);
	}

	/**
	 * Returns true if saving the data to the specified file successfully.
	 * @param file the specified file
	 * @return true if saving the data to the specified file successfully
	 */
	public boolean save_as(File file) {
		if ( !SaxWriter.execute( new File( _rootDirectory, _dataFilename)))
			return false;

		remove_old_datafile( _rootDirectory);

		if ( !ZipUtility.compress( file, _rootDirectory, _parentDirectory))
			return false;

		_currentFile = file;
		_modified = false;
//		ChartObjectMap.get_instance().update_chartFrames_rectangle();
		return true;
	}

	/**
	 * @param rootDirectory
	 */
	private void remove_old_datafile(File rootDirectory) {
		// TODO Auto-generated method stub
		File datafile = new File( rootDirectory, "data.xml");
		if ( datafile.exists() && datafile.isFile() && datafile.canRead())
			datafile.delete();
	}

	/**
	 * Returns true if importing the data from the log files in the specified directory successfully.
	 * @param directory the specified directory
	 * @param graphics2D the graphics object of JAVA
	 * @return true if importing the data from the log files in the specified directory successfully
	 */
	public boolean import_data(File directory, Graphics2D graphics2D) {
		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		File rootDirectory = Importer.copy( directory, parentDirectory,
			Constant._graphicPropertiesFilename, Constant._chartPropertiesFilename, Constant._chartLogDirectory);
		if ( null == rootDirectory) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		return import_data( parentDirectory, rootDirectory, graphics2D);
	}

	/**
	 * Returns true if importing the data from the log files in the specified directory successfully.
	 * @param parentDirectory the specified directory
	 * @param rootDirectory the specified directory
	 * @param graphics2D the graphics object of JAVA
	 * @return true if importing the data from the log files in the specified directory successfully
	 */
	public boolean import_data(File parentDirectory, File rootDirectory, Graphics2D graphics2D) {
		cleanup();

		_parentDirectory = parentDirectory;
		_rootDirectory = rootDirectory;

		Importer importer = new Importer( _rootDirectory);
		if ( !importer.execute( graphics2D)) {
			cleanup();
			return false;
		}

		ChartObjectMap.get_instance().create_chartFrames(
			new File( _rootDirectory, Constant._chartPropertiesFilename),
			new File( _rootDirectory, Constant._chartLogDirectory));

		update_graphic_properties( new File( _rootDirectory, Constant._graphicPropertiesFilename), graphics2D);

		update_preferred_size();

		Observer.get_instance().modified();

		System.gc();

//		ChartObjectMap.get_instance().initialize_chartFrames_rectangle();

		return true;
	}

	/**
	 * @param file
	 * @param graphics2D
	 */
	private void update_graphic_properties(File file, Graphics2D graphics2D) {
		if ( null == file || !file.exists() || !file.isFile() || !file.canRead())
			return;

		String graphicProperties = FileUtility.read_text_from_file( file, "UTF-8");

		String[] lines = graphicProperties.split( "\n");
		if ( null == lines)
			return;

		Map<String, Integer> spotCounterMap = new HashMap<String, Integer>();

		for ( int i = 0; i < lines.length; ++i) {
			if ( lines[ i].startsWith( "image_property")) {
				String[] words = Tool.split( lines[ i], '\t');
				if ( null == words || 4 != words.length)
					continue;

				ImagePropertyManager.get_instance().put( words[ 1],
					new ImageProperty( Integer.parseInt( words[ 2]), Integer.parseInt( words[ 3])));
			} else if ( lines[ i].startsWith( "spot") || lines[ i].startsWith( "agent")) {
				String[] words = Tool.split( lines[ i], '\t');
				if ( null == words || 6 != words.length) {
					words = Tool.split( lines[ i], ',');
					if ( null == words || 5 != words.length)
						continue;

					if ( words[ 0].equals( "spot"))
						SpotObjectManager.get_instance().update( words[ 1], words[ 2],
							( Integer.valueOf( words[ 3])).intValue(),
							( Integer.valueOf( words[ 4])).intValue(),
							spotCounterMap);

					continue;
				}

				if ( words[ 0].equals( "spot"))
					SpotObjectManager.get_instance().update( words[ 1], words[ 2],
						( Integer.valueOf( words[ 3])).intValue(),
						( Integer.valueOf( words[ 4])).intValue(),
						spotCounterMap);

				// TODO ここでイメージを設定する！
				if ( words[ 5].equals( "")
					|| null == ImagePropertyManager.get_instance().get( words[ 5]))
					continue;

				if ( !exist_image_directory() || !exist_thumbnail_image_directory())
					continue;

				File imagefile = new File( get_image_directory(), words[ 5]);
				if ( !imagefile.exists() || !imagefile.isFile() || !imagefile.canRead())
					continue;

				imagefile = new File( get_thumbnail_image_directory(), words[ 5]);
				if ( !imagefile.exists() || !imagefile.isFile() || !imagefile.canRead())
					continue;

				if ( words[ 0].equals( "spot"))
					SpotObjectManager.get_instance().set_image( words[ 1], words[ 2], words[ 5]);
				else if ( words[ 0].equals( "agent"))
					AgentObjectManager.get_instance().set_image( words[ 1], words[ 2], words[ 5]);
			}
		}
	}

	/**
	 * Returns true if importing the graphic data from the specified file successfully.
	 * @param file the specified file
	 * @param graphics2D the graphics object of JAVA
	 * @return true if importing the graphic data from the specified file successfully
	 */
	public boolean import_graphic_data(File file, Graphics2D graphics2D) {
		synchronized( _lock) {
			File parentDirectory = SoarsCommonTool.make_parent_directory();
			if ( null == parentDirectory)
				return false;

			if ( !ZipUtility.decompress( file, parentDirectory)) {
				FileUtility.delete( parentDirectory, true);
				return false;
			}

			File rootDirectory = new File( parentDirectory, _rootDirectoryName);
			if ( !rootDirectory.exists() || !rootDirectory.isDirectory()) {
				if ( !LegacyGraphicDataSaxLoader.execute( file, graphics2D)) {
					FileUtility.delete( parentDirectory, true);
					return false;
				}
			} else {
				File datafile = get_datafile( rootDirectory);
				if ( null == datafile)
					return false;

				if ( !GraphicDataSaxLoader.execute( datafile, rootDirectory, graphics2D)) {
					FileUtility.delete( parentDirectory, true);
					return false;
				}
			}

			FileUtility.delete( parentDirectory, true);

//			update_default_image();

			AgentObjectManager.get_instance().update_dimension();
			SpotObjectManager.get_instance().update_dimension();

			ScenarioManager.get_instance().reset();

			//_current_file = null;

			update_preferred_size();

			Observer.get_instance().modified();

			return true;
		}
	}

	/**
	 * Returns true if exporting the graphic data to the specified file successfully.
	 * @param file the specified file
	 * @return true if exporting the graphic data to the specified file successfully
	 */
	public boolean export_graphic_data(File file) {
		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		File rootDirectory = new File( parentDirectory, _rootDirectoryName);
		if ( !rootDirectory.mkdirs()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		if ( !GraphicDataSaxWriter.execute( new File( rootDirectory, _dataFilename), rootDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		remove_old_datafile( rootDirectory);

		if ( !ZipUtility.compress( file, rootDirectory, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		FileUtility.delete( parentDirectory, true);

		return true;
	}

	/**
	 * Returns true for editting the common properties successfully.
	 * @param component the base class for all Swing components
	 * @param frame the Frame from which the dialog is displayed
	 * @return true for editting the common properties successfully
	 */
	public boolean edit_common_property(JComponent component, Frame frame) {
		EditCommonPropertyDlg editCommonPropertyDlg = new EditCommonPropertyDlg( frame,
			ResourceManager.get( "edit.common.property.dialog.title"),
			true);
		if ( !editCommonPropertyDlg.do_modal( frame))
			return false;

//		update_default_image();

		AgentObjectManager.get_instance().update_dimension();
		SpotObjectManager.get_instance().update_dimension();

		AgentObjectManager.get_instance().arrange();

		update_preferred_size( component);

		return true;
	}

//	/**
//	 * 
//	 */
//	private void update_default_image() {
//		AgentPropertyManager.get_instance().update_default_image(
//			CommonProperty.get_instance()._agent_width,
//			CommonProperty.get_instance()._agent_height);
//		SpotPropertyManager.get_instance().update_default_image(
//			CommonProperty.get_instance()._spot_width,
//			CommonProperty.get_instance()._spot_height);
//	}

	/**
	 * Returns true for editting the agent data successfully.
	 * @param component the base class for all Swing components
	 * @param frame the Frame from which the dialog is displayed
	 * @return true for editting the agent data successfully
	 */
	public boolean edit_agent(JComponent component, Frame frame) {
		EditObjectsDlg editObjectsDlg
			= new EditObjectsDlg( frame,
				ResourceManager.get( "edit.agents.dialog.title"),
				true,
				Environment._openAgentImageDirectoryKey,
				AgentObjectManager.get_instance().get_order());
		if ( !editObjectsDlg.do_modal())
			return false;

		update_preferred_size( component);

		return true;
	}

	/**
	 * Returns true for editting the spot data successfully.
	 * @param component the base class for all Swing components
	 * @param frame the Frame from which the dialog is displayed
	 * @return true for editting the spot data successfully
	 */
	public boolean edit_spot(JComponent component, Frame frame) {
		EditObjectsDlg editObjectsDlg
			= new EditObjectsDlg( frame,
				ResourceManager.get( "edit.spots.dialog.title"),
				true,
				Environment._openSpotImageDirectoryKey,
				SpotObjectManager.get_instance().get_order());
		if ( !editObjectsDlg.do_modal())
			return false;

		AgentObjectManager.get_instance().arrange();

		update_preferred_size( component);

		return true;
	}

	/**
	 * Sets the selection of all objects.
	 * @param selected whether all objects are selected
	 */
	public void select_all_objects(boolean selected) {
		SpotObjectManager.get_instance().select_all_objects( selected);
//		AgentObjectManager.get_instance().select_all_objects( selected);
	}

	/**
	 * Selects the all objects in the specified rectangle
	 * @param rectangle the specified rectangle
	 */
	public void select_object(Rectangle rectangle) {
		SpotObjectManager.get_instance().select( rectangle);
//		AgentObjectManager.get_instance().select( rectangle);
	}

	/**
	 * Returns true for editting the properties of all agents successfully.
	 * @param component the base class for all Swing components
	 * @param frame the Frame from which the dialog is displayed
	 * @return true for editting the properties of all agents successfully
	 */
	public boolean edit_agent_property(JComponent component, Frame frame) {
		if ( !AgentPropertyManager.get_instance().edit( frame))
			return false;

		update_preferred_size( component);
		return true;
	}

	/**
	 * Returns true for editting the properties of all spots successfully.
	 * @param component the base class for all Swing components
	 * @param frame the Frame from which the dialog is displayed
	 * @return true for editting the properties of all spots successfully
	 */
	public boolean edit_spot_property(JComponent component, Frame frame) {
		if ( !SpotPropertyManager.get_instance().edit( frame))
			return false;

		update_preferred_size( component);
		return true;
	}

	/**
	 * Returns true for retrieving the property of agent successfully.
	 * @param frame the Frame from which the dialog is displayed
	 * @return true for retrieving the property of agent successfully
	 */
	public boolean retrieve_agent_property(Frame frame) {
		Vector<AgentObject> agents = new Vector<AgentObject>();
		AgentObjectManager.get_instance().get_visible_agent( agents);

		if ( agents.isEmpty())
			return false;

		ObjectBase[] objectBases = ( ObjectBase[])agents.toArray( new ObjectBase[ 0]);
		QuickSort.sort( objectBases, new ObjectComparator( true, false));

		return ScenarioManager.get_instance().retrieve_agent_property( objectBases, frame);
	}

	/**
	 * Returns true for retrieving the property of spot successfully.
	 * @param frame the Frame from which the dialog is displayed
	 * @return true for retrieving the property of spot successfully
	 */
	public boolean retrieve_spot_property(Frame frame) {
		Vector<ISpotObjectManipulator> spots = new Vector<ISpotObjectManipulator>();
		SpotObjectManager.get_instance().get_visible_spot( spots);

		if ( spots.isEmpty())
			return false;

		ObjectBase[] objectBases = ( ObjectBase[])spots.toArray( new ObjectBase[ 0]);
		QuickSort.sort( objectBases, new ObjectComparator( true, false));

		return ScenarioManager.get_instance().retrieve_spot_property( objectBases, frame);
	}

	/**
	 * Flushes top the selected objects.
	 * @param component the base class for all Swing components
	 */
	public void flush_top(JComponent component) {
		Vector<SpotObject> objects = new Vector<SpotObject>();
		SpotObjectManager.get_instance().get_selected_spot_and_image( objects);
		if ( objects.isEmpty() || 2 > objects.size())
			return;

		int top = 0;
		for ( int i = 0; i < objects.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)objects.get( i);
			Point position = spotObjectManipulator.get_position();
			if ( 0 == i || position.y < top)
				top = position.y;
		}

//		for ( int i = 0; i < objects.size(); ++i) {
//			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)objects.get( i);
		for ( ISpotObjectManipulator spotObjectManipulator:objects) {
			Point position = spotObjectManipulator.get_position();
			spotObjectManipulator.move( 0, top - position.y, true);
		}

		update_preferred_size( component);
		component.repaint();
	}

	/**
	 * Flushes bottom the selected objects.
	 * @param component the base class for all Swing components
	 */
	public void flush_bottom(JComponent component) {
		Vector<SpotObject> objects = new Vector<SpotObject>();
		SpotObjectManager.get_instance().get_selected_spot_and_image( objects);
		if ( objects.isEmpty() || 2 > objects.size())
			return;

		int bottom = 0;
//		for ( int i = 0; i < objects.size(); ++i) {
//			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)objects.get( i);
		for ( ISpotObjectManipulator spotObjectManipulator:objects) {
			Point position = spotObjectManipulator.get_position();
			Dimension dimension = spotObjectManipulator.get_dimension();
			if ( ( position.y + dimension.height) > bottom)
				bottom = ( position.y + dimension.height);
		}

//		for ( int i = 0; i < objects.size(); ++i) {
//			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)objects.get( i);
		for ( ISpotObjectManipulator spotObjectManipulator:objects) {
			Point position = spotObjectManipulator.get_position();
			Dimension dimension = spotObjectManipulator.get_dimension();
			spotObjectManipulator.move( 0, bottom - dimension.height - position.y, true);
		}

		update_preferred_size( component);
		component.repaint();
	}

	/**
	 * Flushes left the selected objects.
	 * @param component the base class for all Swing components
	 */
	public void flush_left(JComponent component) {
		Vector<SpotObject> objects = new Vector<SpotObject>();
		SpotObjectManager.get_instance().get_selected_spot_and_image( objects);
		if ( objects.isEmpty() || 2 > objects.size())
			return;

		int left = 0;
		for ( int i = 0; i < objects.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)objects.get( i);
			Point position = spotObjectManipulator.get_position();
			if ( 0 == i || position.x < left)
				left = position.x;
		}

//		for ( int i = 0; i < objects.size(); ++i) {
//			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)objects.get( i);
		for ( ISpotObjectManipulator spotObjectManipulator:objects) {
			Point position = spotObjectManipulator.get_position();
			spotObjectManipulator.move( left - position.x, 0, true);
		}

		update_preferred_size( component);
		component.repaint();
	}

	/**
	 * Flushes the selected objects right.
	 * @param component the base class for all Swing components
	 */
	public void flush_right(JComponent component) {
		Vector<SpotObject> objects = new Vector<SpotObject>();
		SpotObjectManager.get_instance().get_selected_spot_and_image( objects);
		if ( objects.isEmpty() || 2 > objects.size())
			return;

		int right = 0;
//		for ( int i = 0; i < objects.size(); ++i) {
//			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)objects.get( i);
		for ( ISpotObjectManipulator spotObjectManipulator:objects) {
			Point position = spotObjectManipulator.get_position();
			Dimension dimension = spotObjectManipulator.get_dimension();
			if ( ( position.x + dimension.width) > right)
				right = ( position.x + dimension.width);
		}

//		for ( int i = 0; i < objects.size(); ++i) {
//			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)objects.get( i);
		for ( ISpotObjectManipulator spotObjectManipulator:objects) {
			Point position = spotObjectManipulator.get_position();
			Dimension dimension = spotObjectManipulator.get_dimension();
			spotObjectManipulator.move( right - dimension.width - position.x, 0, true);
		}

		update_preferred_size( component);
		component.repaint();
	}

	/**
	 * Makes vertical gaps between the selected objects equal.
	 * @param component the base class for all Swing components
	 */
	public void vertical_equal_layout(JComponent component) {
		Vector<SpotObject> objects = new Vector<SpotObject>();
		SpotObjectManager.get_instance().get_selected_spot_and_image( objects);
		if ( objects.isEmpty() || 3 > objects.size())
			return;

		ISpotObjectManipulator[] spotObjectManipulators = sort_spots( objects, true);

		int top = 0, bottom = 0, sum = 0;
		for ( int i = 0; i < spotObjectManipulators.length; ++i) {
			Point position = spotObjectManipulators[ i].get_position();
			Dimension dimension = spotObjectManipulators[ i].get_dimension();
			sum += dimension.height;
			if ( 0 == i || position.y < top)
				top = position.y;
			if ( ( position.y + dimension.height) > bottom)
				bottom = ( position.y + dimension.height);
		}

		int space = ( ( bottom - top - sum) / ( spotObjectManipulators.length - 1));

		for ( int i = 0; i < spotObjectManipulators.length; ++i) {
			if ( 0 < i)
				spotObjectManipulators[ i].move( 0, top - spotObjectManipulators[ i].get_position().y, true);

			top += ( spotObjectManipulators[ i].get_dimension().height + space);
		}

		update_preferred_size( component);
		component.repaint();
	}

	/**
	 * Makes horizontal gaps between the selected objects equal.
	 * @param component the base class for all Swing components
	 */
	public void horizontal_equal_layout(JComponent component) {
		Vector<SpotObject> objects = new Vector<SpotObject>();
		SpotObjectManager.get_instance().get_selected_spot_and_image( objects);
		if ( objects.isEmpty() || 3 > objects.size())
			return;

		ISpotObjectManipulator[] spotObjectManipulators = sort_spots( objects, false);

		int left = 0, right = 0, sum = 0;
		for ( int i = 0; i < spotObjectManipulators.length; ++i) {
			Point position = spotObjectManipulators[ i].get_position();
			Dimension dimension = spotObjectManipulators[ i].get_dimension();
			sum += dimension.width;
			if ( 0 == i || position.x < left)
				left = position.x;
			if ( ( position.x + dimension.width) > right)
				right = ( position.x + dimension.width);
		}

		int space = ( ( right - left - sum) / ( spotObjectManipulators.length - 1));

		for ( int i = 0; i < spotObjectManipulators.length; ++i) {
			if ( 0 < i)
				spotObjectManipulators[ i].move( left - spotObjectManipulators[ i].get_position().x, 0, true);

			left += ( spotObjectManipulators[ i].get_dimension().width + space);
		}

		update_preferred_size( component);
		component.repaint();
	}

	/**
	 * 
	 * @param spots
	 * @param vertical
	 * @return
	 */
	private ISpotObjectManipulator[] sort_spots(Vector<SpotObject> spots, boolean vertical) {
		if ( spots.isEmpty())
			return null;

		ISpotObjectManipulator[] spotObjectManipulators = ( ISpotObjectManipulator[])spots.toArray( new ISpotObjectManipulator[ 0]);
		if ( 1 == spotObjectManipulators.length)
			return spotObjectManipulators;

		QuickSort.sort( spotObjectManipulators, new SpotPositionComparator( vertical));
		return spotObjectManipulators;
	}

	/**
	 * Returns true if the specified image file is in use.
	 * @param filename the name of the specified image file
	 * @return true if the specified image file is in use
	 */
	public boolean uses_this_image(String filename) {
		boolean result1 = AgentObjectManager.get_instance().uses_this_image( filename);
		boolean result2 = SpotObjectManager.get_instance().uses_this_image( filename);
		boolean result3 = AgentPropertyManager.get_instance().uses_this_image( filename);
		boolean result4 = SpotPropertyManager.get_instance().uses_this_image( filename);
		return ( result1 || result2 || result3 || result4);
	}

	/**
	 * Sets the specified new image if the object uses the specified image.
	 * @param original_filename the specified image file name
	 * @param new_filename the specified new image file name
	 */
	public void update_image(String original_filename, String new_filename) {
		AgentObjectManager.get_instance().update_image( original_filename, new_filename);
		SpotObjectManager.get_instance().update_image( original_filename, new_filename);
		AgentPropertyManager.get_instance().update_image( original_filename, new_filename);
		SpotPropertyManager.get_instance().update_image( original_filename, new_filename);
	}

	/**
	 * Returns true for writing this spot data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this spot data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		if ( !write_properties( writer))
			return false;

		if ( !write_objects( writer))
			return false;

		if ( !write_image_properties( writer))
			return false;

		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_properties(Writer writer) throws SAXException {
		writer.startElement( null, null, "property", new AttributesImpl());

		if ( !write_common_properties( writer))
			return false;

		if ( !write_spot_properties( writer))
			return false;

		if ( !write_agent_properties( writer))
			return false;

		writer.endElement( null, null, "property");

		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_common_properties(Writer writer) throws SAXException {
		return CommonProperty.get_instance().write( writer);
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_spot_properties(Writer writer) throws SAXException {
		return SpotPropertyManager.get_instance().write( writer);
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_agent_properties(Writer writer) throws SAXException {
		return AgentPropertyManager.get_instance().write( writer);
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_objects(Writer writer) throws SAXException {
		writer.startElement( null, null, "object", new AttributesImpl());

		if ( !SpotObjectManager.get_instance().write( writer))
			return false;

		if ( !AgentObjectManager.get_instance().write( writer))
			return false;

		writer.endElement( null, null, "object");

		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_image_properties(Writer writer) throws SAXException {
		if ( ImagePropertyManager.get_instance().isEmpty())
			return true;

		writer.startElement( null, null, "image", new AttributesImpl());

		if ( !ImagePropertyManager.get_instance().write( writer))
			return false;

		writer.endElement( null, null, "image");

		return true;
	}

	/**
	 * @param rootDirectory
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write_graphic_data(File rootDirectory, Writer writer) throws SAXException {
		if ( !write_graphic_data_properties( rootDirectory, writer))
			return false;

		if ( !write_graphic_data_objects( rootDirectory, writer))
			return false;

		return true;
	}

	/**
	 * Returns true for writing this spot graphic data successfully.
	 * @param rootDirectory the root directory for Animator data
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this spot graphic data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	private boolean write_graphic_data_properties(File rootDirectory, Writer writer) throws SAXException {
		writer.startElement( null, null, "property", new AttributesImpl());

		if ( !write_common_graphic_data( writer))
			return false;

		if ( !write_spot_properties_graphic_data( rootDirectory, writer))
			return false;

		if ( !write_agent_properties_graphic_data( rootDirectory, writer))
			return false;
		
		writer.endElement( null, null, "property");

		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_common_graphic_data(Writer writer) throws SAXException {
		return CommonProperty.get_instance().write_graphic_data( writer);
	}

	/**
	 * @param rootDirectory
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_spot_properties_graphic_data(File rootDirectory, Writer writer) throws SAXException {
		return SpotPropertyManager.get_instance().write_graphic_data( rootDirectory, writer);
	}

	/**
	 * @param rootDirectory
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_agent_properties_graphic_data(File rootDirectory, Writer writer) throws SAXException {
		return AgentPropertyManager.get_instance().write_graphic_data( rootDirectory, writer);
	}

	/**
	 * @param rootDirectory
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_graphic_data_objects(File rootDirectory, Writer writer) throws SAXException {
		writer.startElement( null, null, "object", new AttributesImpl());

		if ( !SpotObjectManager.get_instance().write_graphic_data( rootDirectory, writer))
			return false;

		if ( !AgentObjectManager.get_instance().write_graphic_data( rootDirectory, writer))
			return false;

		writer.endElement( null, null, "object");

		return true;
	}
}
