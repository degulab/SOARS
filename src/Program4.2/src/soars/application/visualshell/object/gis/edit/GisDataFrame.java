/**
 * 
 */
package soars.application.visualshell.object.gis.edit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.main.VisualShellView;
import soars.application.visualshell.object.gis.GisData;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.edit.field.selector.SelectGisDataFieldsDlg;
import soars.application.visualshell.object.gis.edit.variable.object.base.ObjectBase;
import soars.application.visualshell.object.gis.edit.variable.object.keyword.KeywordObject;
import soars.application.visualshell.object.gis.edit.variable.object.number.NumberObject;
import soars.application.visualshell.object.gis.edit.variable.panel.base.PanelBase;
import soars.application.visualshell.object.gis.edit.variable.panel.keyword.KeywordPanel;
import soars.application.visualshell.object.gis.edit.variable.panel.number.NumberVariablePanel;
import soars.application.visualshell.object.gis.edit.variable.table.VariableTable;
import soars.application.visualshell.object.gis.file.loader.FileData;
import soars.application.visualshell.object.gis.file.loader.GisSaxLoader;
import soars.application.visualshell.object.gis.file.loader.ScaleData;
import soars.application.visualshell.object.gis.file.writer.GisSaxWriter;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.observer.Observer;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.IObjectsMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.message.ObjectsMessageDlg;
import soars.common.utility.swing.text.TextLimiter;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.tool.resource.Resource;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class GisDataFrame extends Frame implements IMessageCallback, IObjectsMessageCallback {

	/**
	 * 
	 */
	static public final int _minimumWidth = 800;

	/**
	 * 
	 */
	static public final int _minimumHeight = 600;

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
	private GisDataManager _gisDataManager = null;

	/**
	 * 
	 */
	private List<Field> _spotFields = new ArrayList<Field>();

	/**
	 * 
	 */
	private JTextField _spotFieldsTextField = null;

	/**
	 * 
	 */
	public Map<String, PanelBase> _panelBaseMap = new HashMap<String, PanelBase>();

	/**
	 * 
	 */
	private JLabel _kindLabel = null;

	/**
	 * 
	 */
	private JComboBox _kindComboBox = null;

	/**
	 * 
	 */
	private VariableTable _variableTable = null;

	/**
	 * 
	 */
	private KeywordPanel _keywordPanel = null;

	/**
	 * 
	 */
	private NumberVariablePanel _numberVariablePanel = null;

	/**
	 * 
	 */
	private JComboBox _scaleTypeComboBox = null;

	/**
	 * 
	 */
	private String[] _scaleTypes = new String[] {
		ResourceManager.get_instance().get( "edit.gis.data.dialog.scale.horizontal"),
		ResourceManager.get_instance().get( "edit.gis.data.dialog.scale.vertical"),
		ResourceManager.get_instance().get( "edit.gis.data.dialog.scale.horizontal.and.vertical")
	};

	/**
	 * 
	 */
	private List<JPanel> _scalePanels = new ArrayList<JPanel>();

	/**
	 * 
	 */
	private JTextField _xRangeTextField = null;

	/**
	 * 
	 */
	private JTextField _yRangeTextField = null;

	/**
	 * 
	 */
	private List<JTextField> _xyRangeTextFields = new ArrayList<JTextField>();

	/**
	 * 
	 */
	private List<List<JComponent>> _components = new ArrayList<List<JComponent>>();

	/**
	 * 
	 */
	private JTabbedPane _tabbedPane = null;

	/**
	 * 
	 */
	private List<GisDataPage> _gisDataPages = new ArrayList<GisDataPage>();

	/**
	 * 
	 */
	private List<JTable> _gisDataTables = new ArrayList<JTable>();

	/**
	 * 
	 */
	private List<JButton> _buttons = new ArrayList<JButton>();

	/**
	 * @param arg0
	 */
	public GisDataFrame(String arg0) {
		super(arg0);
		_gisDataManager = new GisDataManager();
		_components.add( new ArrayList<JComponent>());
		_components.add( new ArrayList<JComponent>());
	}

	/**
	 * @param arg0
	 * @param gisDataManager 
	 * @throws HeadlessException
	 */
	public GisDataFrame(String arg0, GisDataManager gisDataManager) throws HeadlessException {
		super(arg0);
		_gisDataManager = gisDataManager;
		_components.add( new ArrayList<JComponent>());
		_components.add( new ArrayList<JComponent>());
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._gisDataFrameRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( MainFrame.get_instance(), _minimumWidth, _minimumHeight).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._gisDataFrameRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( MainFrame.get_instance(), _minimumWidth, _minimumHeight).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._gisDataFrameRectangleKey + "width",
			String.valueOf( _minimumWidth));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._gisDataFrameRectangleKey + "height",
			String.valueOf( _minimumHeight));
		if ( null == value)
			return null;

		int height = Integer.parseInt( value);

		return new Rectangle( x, y, width, height);
	}

	/**
	 * 
	 */
	private void optimize_window_rectangle() {
		Rectangle rectangle = getBounds();
		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( rectangle)
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).width <= 10
			|| rectangle.y <= -getInsets().top
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).height <= getInsets().top) {
			setSize( _minimumWidth, _minimumHeight);
			setLocationRelativeTo( MainFrame.get_instance());
		}
	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Rectangle rectangle = getBounds();

		Environment.get_instance().set(
			Environment._gisDataFrameRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._gisDataFrameRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._gisDataFrameRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._gisDataFrameRectangleKey + "height", String.valueOf( rectangle.height));
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean create(File file) {
		Object[] objects = ObjectsMessageDlg.execute( this, ResourceManager.get_instance().get( "edit.gis.data.dialog.title"), true,
			"on_file_open", ResourceManager.get_instance().get( "file.open.show.message"), new Object[] { file}, ( IObjectsMessageCallback)this, this);
		if ( null == objects) {
			// TODO エラーメッセージ
			JOptionPane.showMessageDialog( this,
				ResourceManager.get_instance().get( "edit.gis.data.dialog.could.not.load.error.message"),
				ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!super.create())
			return false;

		_spotFields = ( List<Field>)objects[ 0];
		_spotFieldsTextField.setText( Field.get( _spotFields));

		List<ObjectBase> objectBases = ( List<ObjectBase>)objects[ 1];
		for ( ObjectBase objectBase:objectBases)
			_variableTable.append( objectBase);

		_variableTable.select( 0);

		ScaleData scaleData = ( ScaleData)objects[ 2];
		_scaleTypeComboBox.setSelectedIndex( scaleData._type);
		switch ( scaleData._type) {
			case 0:
				_xRangeTextField.setText( String.valueOf( scaleData._horizontal));
				break;
			case 1:
				_yRangeTextField.setText( String.valueOf( scaleData._vertical));
				break;
			case 2:
				_xyRangeTextFields.get( 0).setText( String.valueOf( scaleData._horizontal));
				_xyRangeTextFields.get( 1).setText( String.valueOf( scaleData._vertical));
				break;
		}

		List<FileData> fileDataList = ( List<FileData>)objects[ 3];
		for ( int i = 0; i < fileDataList.size(); ++i)
			_gisDataPages.get( i)._spotRoleComboBox.setSelectedItem( fileDataList.get( i)._role);

		setTitle( ResourceManager.get_instance().get( "edit.gis.data.dialog.title") + " - " + _currentFile.getName());

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if (!super.on_create())
			return false;

		setLayout( new BorderLayout());

		if ( !setup_north_panel())
			return false;

		if ( !setup_center_panel())
			return false;

		setup_south_panel();

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			setBounds( rectangle.x, rectangle.y, _minimumWidth, _minimumHeight);
		else
			setBounds( rectangle);

		adjust();

		on_setup_completed();

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			setIconImage( image);

		setVisible( true);

		MainFrame.get_instance().enabled( false);


		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_north_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		setup_north_north_panel( panel);

		if ( !setup_north_center_panel( panel))
			return false;

		if ( !setup_north_south_panel( panel))
			return false;

		add( panel, "North");

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_north_north_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 2));

		setup_north_north_left_panel( panel);

		setup_north_north_right_panel( panel);

		parent.add( panel, "North");
	}

	/**
	 * @param parent
	 */
	private void setup_north_north_left_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		setup_north_north_left_north_panel( panel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_north_north_left_north_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( panel);

		setup_spotFieldsTextField( panel);

		insert_horizontal_glue( panel);

		parent.add( panel, "North");
	}

	/**
	 * @param parent
	 */
	private void setup_spotFieldsTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JButton button = new JButton( ResourceManager.get_instance().get( "edit.gis.data.dialog.spot.field"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				get_spot_fields();
			}
		});
		_components.get( 0).add( button);
		panel.add( button);

		panel.add( Box.createHorizontalStrut( 5));

		_spotFieldsTextField = new JTextField();
		_spotFieldsTextField.setEditable( false);
		panel.add( _spotFieldsTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void get_spot_fields() {
		SelectGisDataFieldsDlg selectGisDataFieldsDlg = new SelectGisDataFieldsDlg( this, ResourceManager.get_instance().get( "select.spot.field.dialog.title"), true, _gisDataManager.get_fields( false)/*_gisDataManager._availableSpotFields*/, _spotFields, _gisDataManager, Constant._prohibitedCharacters2);
		if ( !selectGisDataFieldsDlg.do_modal())
			return;

		_spotFields = selectGisDataFieldsDlg._selectedFields;
		_spotFieldsTextField.setText( Field.get( _spotFields));
	}

	/**
	 * @param parent
	 */
	private void setup_north_north_right_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		setup_north_north_right_north_panel( panel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_north_north_right_north_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( panel);

		insert_horizontal_glue( panel);

		parent.add( panel, "North");
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_north_center_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_variableTable = new VariableTable( this, this, this);
		if ( !_variableTable.setup())
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _variableTable);
		scrollPane.setPreferredSize( new Dimension( scrollPane.getPreferredSize().width, 200));

		panel.add( scrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_north_south_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 2));

		if ( !setup_north_south_left_panel( panel))
			return false;

		setup_north_south_right_panel( panel);

		parent.add( panel, "South");

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_north_south_left_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		if ( !setup_north_south_left_north_panel( panel))
			return false;

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_north_south_left_north_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( panel);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout( new BoxLayout( mainPanel, BoxLayout.X_AXIS));

		mainPanel.add( Box.createHorizontalStrut( 5));

		JPanel borderPanel = new JPanel();
		borderPanel.setLayout( new BoxLayout( borderPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( borderPanel);

		setup_kind_components( borderPanel);

		//insert_horizontal_glue( borderPanel);

		if ( !setup_components( borderPanel))
			return false;

		insert_horizontal_glue( borderPanel);

		borderPanel.setBorder( BorderFactory.createLineBorder( getForeground(), 1));

		mainPanel.add( borderPanel);

		mainPanel.add( Box.createHorizontalStrut( 5));

		panel.add( mainPanel);

		insert_horizontal_glue( panel);

		parent.add( panel, "North");

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_kind_components(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		setup_kindLabel( panel);

		panel.add( Box.createHorizontalStrut( 5));

		setup_kindComboBox( panel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_kindLabel(JPanel parent) {
		_kindLabel = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.kind.label"));
		_kindLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		parent.add( _kindLabel);
	}

	/**
	 * @param parent
	 */
	private void setup_kindComboBox(JPanel parent) {
		Vector<String> items = new Vector<String>();
//		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"));
//		if ( _playerBase instanceof AgentObject) {
//			items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable"));
//		}
//		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"));
//		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"));
		_kindComboBox = new JComboBox( items);
		_kindComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = ( String)_kindComboBox.getSelectedItem();
				Set<String> kinds = _panelBaseMap.keySet();
				for ( String kind:kinds) {
					PanelBase panelBase = _panelBaseMap.get( kind);
					panelBase.setVisible( kind.equals( item));
				}
				invalidate();
				repaint();
			}
		});
		parent.add( _kindComboBox);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_components(JPanel parent) {
		_keywordPanel = new KeywordPanel( _gisDataManager, _variableTable, PropertyPageBase._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword")), this, this);
		if ( !_keywordPanel.setup())
			return false;

		_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"), _keywordPanel);
		parent.add( _keywordPanel);


		_numberVariablePanel = new NumberVariablePanel( _gisDataManager, _variableTable, PropertyPageBase._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object")), this, this);
		if ( !_numberVariablePanel.setup())
			return false;

		_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"), _numberVariablePanel);
		parent.add( _numberVariablePanel);

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_north_south_right_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		setup_north_south_right_north_panel( panel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_north_south_right_north_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( panel);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout( new BoxLayout( mainPanel, BoxLayout.X_AXIS));

		//mainPanel.add( Box.createHorizontalStrut( 5));

		JPanel borderPanel = new JPanel();
		borderPanel.setLayout( new BoxLayout( borderPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( borderPanel);

		setup_scale_component( borderPanel);

		insert_horizontal_glue( borderPanel);

		borderPanel.setBorder( BorderFactory.createLineBorder( getForeground(), 1));

		mainPanel.add( borderPanel);

		mainPanel.add( Box.createHorizontalStrut( 5));

		panel.add( mainPanel);

		insert_horizontal_glue( panel);

		parent.add( panel, "North");
	}

	/**
	 * @param parent
	 */
	private void setup_scale_component(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		setup_scaleTypeComboBox( panel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_scaleTypeComboBox(JPanel parent) {
		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BoxLayout( basePanel, BoxLayout.X_AXIS));

		basePanel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.gis.data.dialog.scale.type"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_components.get( 1).add( label);
		basePanel.add( label);

		basePanel.add( Box.createHorizontalStrut( 5));

		_scaleTypeComboBox = new JComboBox( _scaleTypes);
		_scaleTypeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = _scaleTypeComboBox.getSelectedIndex();
				for ( int i = 0; i < _scalePanels.size(); ++i)
					_scalePanels.get( i).setVisible( index == i);
				invalidate();
				repaint();
			}
		});
		basePanel.add( _scaleTypeComboBox);

		basePanel.add( Box.createHorizontalStrut( 5));

		String[] xy = new String[] { ResourceManager.get_instance().get( "edit.gis.data.dialog.scale.horizontal"), ResourceManager.get_instance().get( "edit.gis.data.dialog.scale.vertical")};
		String[] size = new String[] { String.valueOf( VisualShellView.get_instance().getBounds().width), String.valueOf( VisualShellView.get_instance().getBounds().height)};

		JPanel scalesPanel = new JPanel();
		scalesPanel.setLayout( new BoxLayout( scalesPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		label = new JLabel( xy[ 0]);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_xRangeTextField = new JTextField( new TextLimiter( "0123456789"), size[ 0], 0);
		_xRangeTextField.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _xRangeTextField);

		panel.add( Box.createHorizontalStrut( 5));

		_scalePanels.add( panel);
		scalesPanel.add( panel);


		panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		label = new JLabel( xy[ 1]);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_yRangeTextField = new JTextField( new TextLimiter( "0123456789"), size[ 1], 0);
		_yRangeTextField.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _yRangeTextField);

		panel.add( Box.createHorizontalStrut( 5));

		_scalePanels.add( panel);
		scalesPanel.add( panel);


		panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		for ( int i = 0; i < 2; ++i) {
			label = new JLabel( xy[ i]);
			panel.add( label);

			panel.add( Box.createHorizontalStrut( 5));

			JTextField textField = new JTextField( new TextLimiter( "0123456789"), size[ i], 0);
			textField.setHorizontalAlignment( SwingConstants.RIGHT);
			_xyRangeTextFields.add( textField);
			panel.add( textField);

			panel.add( Box.createHorizontalStrut( 5));
		}

		_scalePanels.add( panel);
		scalesPanel.add( panel);


		basePanel.add( scalesPanel);

		parent.add( basePanel);
	}

	/**
	 * @return
	 */
	private boolean setup_center_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_tabbedPane = new JTabbedPane( SwingConstants.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		for ( GisData gisData:_gisDataManager) {
			GisDataPage gisDataPage = new GisDataPage( gisData, this/*( Frame)getOwner()*/, this);
			if ( !gisDataPage.create())
				return false;

			_tabbedPane.add( gisDataPage, gisData._name);
			_gisDataPages.add( gisDataPage);
			_gisDataTables.add( gisDataPage._gisDataTable);
		}

		_tabbedPane.addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				GisDataPage gisDataPage = ( GisDataPage)_tabbedPane.getSelectedComponent();
				gisDataPage.on_selected();
			}
		});

		panel.add( _tabbedPane);

		panel.add( Box.createHorizontalStrut( 5));

		add( panel);

		return true;
	}

	/**
	 * 
	 */
	private void setup_south_panel() {
		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( southPanel);

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		setup_buttons( panel);

		setup_ok_and_cancel_button( panel);

		southPanel.add( panel);

		insert_horizontal_glue( southPanel);

		add( southPanel, "South");
	}

	/**
	 * @param basePanel
	 */
	private void setup_buttons(JPanel parent) {
		// TODO リソース
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JButton button = new JButton( ResourceManager.get_instance().get( "file.save.as.gis.data.menu"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_file_save_as( arg0);
			}
		});
		_buttons.add( button);
		panel.add( button);

		button = new JButton( ResourceManager.get_instance().get( "file.save.gis.data.menu"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_file_save( arg0);
			}
		});
		button.setEnabled( null != _currentFile);
		_buttons.add( button);
		panel.add( button);

		parent.add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_file_save_as(ActionEvent actionEvent) {
		File file = CommonTool.get_save_file(
			Environment._saveAsGisDataDirectoryKey,
			ResourceManager.get_instance().get( "file.saveas.dialog"),
			new String[] { "gis"},
			"GIS data for VisualShell",
			this);

		requestFocus();

		if ( null == file)
			return;

		String absolute_name = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absolute_name + ".gis");
		else if ( name.length() - 1 == index)
			file = new File( absolute_name + "gis");

		boolean result = MessageDlg.execute( this, ResourceManager.get_instance().get( "edit.gis.data.dialog.title"), true,
			"on_file_save_as", ResourceManager.get_instance().get( "file.saveas.show.message"), new Object[] { file}, ( IMessageCallback)this, this);

		if ( result)
			setTitle( ResourceManager.get_instance().get( "edit.gis.data.dialog.title") + " - " + file.getName());

		if ( !result) {
			// TODO エラーメッセージ
			JOptionPane.showMessageDialog( this,
				ResourceManager.get_instance().get( "edit.gis.data.dialog.could.not.save.as.error.message"),
				ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * @param actionEvent
	 */
	protected void on_file_save(ActionEvent actionEvent) {
		boolean result = MessageDlg.execute( this, ResourceManager.get_instance().get( "edit.gis.data.dialog.title"), true,
			"on_file_save", ResourceManager.get_instance().get( "file.save.show.message"), ( IMessageCallback)this, this);

		setTitle( ResourceManager.get_instance().get( "edit.gis.data.dialog.title") + " - " + _currentFile.getName());

		requestFocus();

		if ( !result) {
			// TODO エラーメッセージ
			JOptionPane.showMessageDialog( this,
				ResourceManager.get_instance().get( "edit.gis.data.dialog.could.not.save.error.message"),
				ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * @param parent
	 */
	protected void setup_ok_and_cancel_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		JButton okButton = new JButton( ResourceManager.get_instance().get( "edit.gis.data.dialog.create.spots"));

		okButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_ok( arg0);
			}
		});
		panel.add( okButton);


		JButton cancelButton = new JButton( ResourceManager.get_instance().get( "edit.gis.data.dialog.close"));
		cancelButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_cancel( arg0);
			}
		});
		panel.add( cancelButton);

		int width = Math.max( okButton.getPreferredSize().width, cancelButton.getPreferredSize().width);

		okButton.setPreferredSize( new Dimension( width, okButton.getPreferredSize().height));
		cancelButton.setPreferredSize( new Dimension( width, cancelButton.getPreferredSize().height));

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JComponent component:_components.get( 0))
			width = Math.max( width, component.getPreferredSize().width);

		width = Math.max( width, _kindLabel.getPreferredSize().width);
		Collection<PanelBase> panelBases = _panelBaseMap.values();
		for ( PanelBase panelBase:panelBases)
			width = panelBase.get_label_width( width);

		for ( JComponent component:_components.get( 0))
			component.setPreferredSize( new Dimension( width, component.getPreferredSize().height));

		int buttonWidth = 0;
		for ( PanelBase panelBase:panelBases)
			buttonWidth = panelBase.adjust( width);

		_kindLabel.setPreferredSize( new Dimension( width, _kindLabel.getPreferredSize().height));


		width = 0;
		for ( JComponent component:_components.get( 1))
			width = Math.max( width, component.getPreferredSize().width);
		for ( JComponent component:_components.get( 1))
			component.setPreferredSize( new Dimension( width, component.getPreferredSize().height));


		width = 0;
		for ( JButton button:_buttons)
			width = Math.max( width, button.getPreferredSize().width);
		for ( JButton button:_buttons)
			button.setPreferredSize( new Dimension( width, button.getPreferredSize().height));
	}

	/**
	 * 
	 */
	private void on_setup_completed() {
		optimize_window_rectangle();

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width,
					( _minimumHeight > height) ? _minimumHeight : height);
			}
		});

		_keywordPanel.on_setup_completed();
		_numberVariablePanel.on_setup_completed();

		_keywordPanel.setVisible( true);
		_numberVariablePanel.setVisible( false);

		_scaleTypeComboBox.setSelectedIndex( 0);
		_scalePanels.get( 0).setVisible( true);
		_scalePanels.get( 1).setVisible( false);
		_scalePanels.get( 2).setVisible( false);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		on_close();
	}

	/**
	 * 
	 */
	private void on_close() {
		set_property_to_environment_file();

		cleanup();

		dispose();

		MainFrame.get_instance().enabled( true);
		MainFrame.get_instance().toFront();
	}

	/**
	 * @param objectBase
	 */
	public void changeSelection(ObjectBase objectBase) {
		if ( null != objectBase) {
			if ( objectBase instanceof KeywordObject)
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"));
			else if ( objectBase instanceof NumberObject)
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"));
			else
				return;
		}

		PanelBase panelBase = _panelBaseMap.get( _kindComboBox.getSelectedItem());
		if ( null == panelBase)
			return;

		panelBase.update( objectBase);
	}

	/**
	 * @param row
	 * @param targetObjectBase
	 * @param selectedObjectBase
	 * @return
	 */
	public ObjectBase confirm(int row, ObjectBase targetObjectBase, ObjectBase selectedObjectBase) {
		ObjectBase objectBase = _keywordPanel.confirm( row, targetObjectBase, selectedObjectBase);
		if ( null != objectBase)
			return objectBase;

		objectBase = _numberVariablePanel.confirm( row, targetObjectBase, selectedObjectBase);
		if ( null != objectBase)
			return objectBase;

		return null;
	}

	/**
	 * @param arg0
	 */
	protected void on_ok(ActionEvent arg0) {
		if ( !_keywordPanel.confirm())
			return;

		if ( !_numberVariablePanel.confirm())
			return;

		if ( _spotFields.isEmpty()) {
			on_error( ResourceManager.get_instance().get( "edit.gis.data.dialog.spot.fields.empty.error.message"));
			return;
		}

		GisDataManager gisDataManager = new GisDataManager();
		if ( !gisDataManager.cerate( _gisDataManager, _gisDataTables)) {
			on_error( ResourceManager.get_instance().get( "edit.gis.data.dialog.invalid.data.error.message"));
			return;
		}

		// TODO リソース
		if ( !MessageDlg.execute( this, ResourceManager.get_instance().get( "edit.gis.data.dialog.title"), true,
			"on_can_append_spots", ResourceManager.get_instance().get( "edit.gis.data.dialog.can.append.spots.message"),
			new Object[] { gisDataManager}, ( IMessageCallback)this, this))
			return;

		// TODO リソース
		Object[] objects = ObjectsMessageDlg.execute( this, ResourceManager.get_instance().get( "edit.gis.data.dialog.title"), true,
			"on_get_range", ResourceManager.get_instance().get( "edit.gis.data.dialog.get.range.message"),
			new Object[] { gisDataManager}, ( IObjectsMessageCallback)this, this);
		if ( null == objects) {
			on_error( ResourceManager.get_instance().get( "edit.gis.data.dialog.get.range.error.message"));
			return;
		}

		double[] range = ( double[])objects[ 0];
		//System.out.println( range[ 0] + ", " + range[ 1] + ", " + range[ 2] + ", " + range[ 3]);

		double width = ( range[ 2] - range[ 0]);
		double height = ( range[ 3] - range[ 1]);

		double[] ratio = new double[] { 0.0, 0.0};
		if ( _scaleTypeComboBox.getSelectedItem().equals( _scaleTypes[ 0])) {
			try {
				int rangeX = Integer.parseInt( _xRangeTextField.getText());
				ratio[ 0] = ratio[ 1] = ( ( 1.0E-4 > Math.abs( width)) ? 0.0 : ( ( double)rangeX / width));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				on_error( ResourceManager.get_instance().get( "edit.gis.data.dialog.invalid.width.value.error.message"));
				return;
			}
		} else if ( _scaleTypeComboBox.getSelectedItem().equals( _scaleTypes[ 1])) {
			try {
				int rangeY = Integer.parseInt( _yRangeTextField.getText());
				ratio[ 0] = ratio[ 1] = ( ( 1.0E-4 > Math.abs( height)) ? 0.0 : ( ( double)rangeY / height));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				on_error( ResourceManager.get_instance().get( "edit.gis.data.dialog.invalid.height.value.error.message"));
				return;
			}
		} else if ( _scaleTypeComboBox.getSelectedItem().equals( _scaleTypes[ 2])) {
			try {
				int rangeX = Integer.parseInt( _xyRangeTextFields.get( 0).getText());
				int rangeY = Integer.parseInt( _xyRangeTextFields.get( 1).getText());
				ratio[ 0] = ( ( 1.0E-4 > Math.abs( width)) ? 0.0 : ( ( double)rangeX / width));
				ratio[ 1] = ( ( 1.0E-4 > Math.abs( height)) ? 0.0 : ( ( double)rangeY / height));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				on_error( ResourceManager.get_instance().get( "edit.gis.data.dialog.invalid.width.or.height.value.error.message"));
				return;
			}
		} else
			return;

		// TODO ここで確認用ダイアログボックスを表示
		CreateSpotsConfirmDlg createSpotsConfirmDlg = new CreateSpotsConfirmDlg( this, ResourceManager.get_instance().get( "edit.gis.data.dialog.title"), true, range);
		if ( !createSpotsConfirmDlg.do_modal( this))
			return;

		// TODO リソース
		if ( !MessageDlg.execute( this, ResourceManager.get_instance().get( "edit.gis.data.dialog.title"), true,
			"on_create_spots", ResourceManager.get_instance().get( "edit.gis.data.dialog.create.spots.message"),
			new Object[] { gisDataManager, range, ratio}, ( IMessageCallback)this, this)) {
			on_error( ResourceManager.get_instance().get( "edit.gis.data.dialog.create.spots.error.message"));
			return;
		}

		for ( String kind:Constant._kinds)
			Observer.get_instance().on_update_object( kind);

		Observer.get_instance().on_update_playerBase( true);

		set_property_to_environment_file();

		dispose();

		MainFrame.get_instance().enabled( true);
		MainFrame.get_instance().toFront();
		MainFrame.get_instance().repaint();
	}

	/**
	 * @param gisDataManager
	 * @return
	 */
	private boolean can_append_spots(GisDataManager gisDataManager) {
		// TODO Auto-generated method stub
		if ( !gisDataManager.can_append_spots( _spotFields, _variableTable.get(), this))
			return false;

		return true;
	}

	/**
	 * @param gisDataManager
	 * @return
	 */
	private Object[] get_range(GisDataManager gisDataManager) {
		// TODO Auto-generated method stub
		double[] range = gisDataManager.get_range();
		if ( null == range)
			return null;

		// 既存のGISスポットのGIS絶対座標を参照してrangeを確定する
		LayerManager.get_instance().get_gis_range( range);

		return new Object[] { range};
	}

	/**
	 * @param gisDataManager
	 * @param range
	 * @param ratio
	 * @return
	 */
	private boolean create_spots(GisDataManager gisDataManager, double[] range, double[] ratio) {
		// TODO Auto-generated method stub
		for ( int i = 0; i < _tabbedPane.getTabCount(); ++i)
			gisDataManager.get( i)._role = ( String)( ( GisDataPage)_tabbedPane.getComponentAt( i))._spotRoleComboBox.getSelectedItem();

		// 既存のGISスポットの位置を変更する
		LayerManager.get_instance().update_gis_coordinates( range, ratio);

		if ( !gisDataManager.append_spots( _spotFields, _variableTable.get(), range, ratio, VisualShellView.get_instance()))
			return false;

		return true;
	}

	/**
	 * @param message
	 */
	private void on_error(String message) {
		JOptionPane.showMessageDialog( this,
			message,
			ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param arg0
	 */
	protected void on_cancel(ActionEvent arg0) {
		on_close();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IObjectsMessageCallback#objects_message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.ObjectsMessageDlg)
	 */
	public Object[] objects_message_callback(String id, Object[] objects, ObjectsMessageDlg objectsMessageDlg) {
		if ( id.equals( "on_file_open"))
			return load( ( File)objects[ 0]);
		else if ( id.equals( "on_get_range"))
			return get_range( ( GisDataManager)objects[ 0]);
		return null;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "on_file_save"))
			return save();
		else if ( id.equals( "on_file_save_as"))
			return save_as( ( File)objects[ 0]);
		else if ( id.equals( "on_can_append_spots"))
			return can_append_spots( ( GisDataManager)objects[ 0]);
		else if ( id.equals( "on_create_spots"))
			return create_spots( ( GisDataManager)objects[ 0], ( double[])objects[ 1], ( double[])objects[ 2]);
		return false;
	}

	/**
	 * @return
	 */
	private boolean setup_work_directory() {
		if ( null != _parentDirectory)
			return true;

		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		File rootDirectory = new File( parentDirectory, Constant._gisRootDirectoryName);
		if ( !rootDirectory.mkdirs()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		_parentDirectory = parentDirectory;
		_rootDirectory = rootDirectory;

		return true;
	}

	/**
	 * 
	 */
	private void cleanup() {
		_currentFile = null;
		_modified = false;

		if ( null != _parentDirectory)
			FileUtility.delete( _parentDirectory, true);

		_rootDirectory = null;
		_parentDirectory = null;
	}

	/**
	 * @param file
	 * @return
	 */
	private Object[] load(File file) {
		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return null;

		if ( !ZipUtility.decompress( file, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return null;
		}

		cleanup();

		File rootDirectory = new File( parentDirectory, Constant._gisRootDirectoryName);
		if ( !rootDirectory.exists() || !rootDirectory.isDirectory())
			return null;

		_parentDirectory = parentDirectory;
		_rootDirectory = rootDirectory;

		Object[] objects = GisSaxLoader.execute( new File( _rootDirectory, Constant._gisDataFilename), _gisDataManager, new File( _rootDirectory, Constant._gisDataDirectoryName));
		if ( null == objects) {
			cleanup();
			return null;
		}

		_currentFile = file;

		return objects;
	}

	/**
	 * @return
	 */
	private boolean save() {
		if ( null == _currentFile)
			return false;

		return save_as( _currentFile);
	}

	/**
	 * @param file
	 * @return
	 */
	private boolean save_as(File file) {
		if ( !setup_work_directory())
			return false;

		GisDataManager gisDataManager = new GisDataManager();
		if ( !gisDataManager.cerate( _gisDataManager, _gisDataTables)) {
			//on_error( ResourceManager.get_instance().get( "edit.gis.data.dialog.invalid.data.error.message"));
			return false;
		}

		for ( int i = 0; i < _tabbedPane.getTabCount(); ++i)
			gisDataManager.get( i)._role = ( String)( ( GisDataPage)_tabbedPane.getComponentAt( i))._spotRoleComboBox.getSelectedItem();

		if ( !GisSaxWriter.execute( new File( _rootDirectory, Constant._gisDataFilename), gisDataManager, this))
			return false;

		File gisDataDirectory = new File( _rootDirectory, Constant._gisDataDirectoryName);
		if ( !gisDataDirectory.exists() && !gisDataDirectory.mkdirs())
			return false;

		if ( !gisDataManager.write( gisDataDirectory))
			return false;

		if ( !ZipUtility.compress( file, _rootDirectory, _parentDirectory))
			return false;

		_currentFile = file;
		_modified = false;
		_buttons.get( 1).setEnabled( true);
		return true;
	}

	/**
	 * @param gisDataManager
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(GisDataManager gisDataManager, Writer writer) throws SAXException {
		if ( !write_spotFields( writer))
			return false;

		if ( !_variableTable.write( writer))
			return false;

		if ( !write_scale( writer))
			return false;

		if ( !gisDataManager.write( writer))
			return false;

		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_spotFields(Writer writer) throws SAXException {
		if ( _spotFields.isEmpty())
			return true;

		writer.startElement( null, null, "spot_data", new AttributesImpl());

		for ( Field field:_spotFields) {
			if ( !field.write( writer))
				return false;
		}

		writer.endElement( null, null, "spot_data");

		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_scale(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		if ( _scaleTypeComboBox.getSelectedItem().equals( _scaleTypes[ 0])) {
			attributesImpl.addAttribute( null, null, "type", "", "0");
			try {
				int rangeX = Integer.parseInt( _xRangeTextField.getText());
				attributesImpl.addAttribute( null, null, "horizontal", "", String.valueOf( rangeX));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				//on_error( ResourceManager.get_instance().get( "edit.gis.data.dialog.invalid.width.value.error.message"));
				return false;
			}
		} else if ( _scaleTypeComboBox.getSelectedItem().equals( _scaleTypes[ 1])) {
			attributesImpl.addAttribute( null, null, "type", "", "1");
			try {
				int rangeY = Integer.parseInt( _yRangeTextField.getText());
				attributesImpl.addAttribute( null, null, "vertical", "", String.valueOf( rangeY));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				//on_error( ResourceManager.get_instance().get( "edit.gis.data.dialog.invalid.height.value.error.message"));
				return false;
			}
		} else if ( _scaleTypeComboBox.getSelectedItem().equals( _scaleTypes[ 2])) {
			attributesImpl.addAttribute( null, null, "type", "", "2");
			try {
				int rangeX = Integer.parseInt( _xyRangeTextFields.get( 0).getText());
				int rangeY = Integer.parseInt( _xyRangeTextFields.get( 1).getText());
				attributesImpl.addAttribute( null, null, "horizontal", "", String.valueOf( rangeX));
				attributesImpl.addAttribute( null, null, "vertical", "", String.valueOf( rangeY));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				//on_error( ResourceManager.get_instance().get( "edit.gis.data.dialog.invalid.width.or.height.value.error.message"));
				return false;
			}
		}

		writer.writeElement( null, null, "scale_data", attributesImpl);

		return true;
	}
}
