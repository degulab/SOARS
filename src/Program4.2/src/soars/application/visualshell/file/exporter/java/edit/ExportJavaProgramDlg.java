/**
 * 
 */
package soars.application.visualshell.file.exporter.java.edit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.border.ComponentTitledBorder;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextLimiter;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class ExportJavaProgramDlg extends Dialog implements ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	static public int _minimumWidth = -1;

	/**
	 * 
	 */
	private JTextField _sourceFolderTextField = null;

	/**
	 * 
	 */
	private JTextField _packagePathTextField = null;

	/**
	 * 
	 */
	private List<JRadioButton> _radioButtons  = new ArrayList<>();

	/**
	 * 
	 */
	private JLabel _numberOfSpaceLabel = null;

	/**
	 * 
	 */
	private JComboBox<String> _numberOfSpaceComboBox = null;

	/**
	 * 
	 */
	private final int _maxNumberOfSpace = 8;

	/**
	 * 
	 */
	private List<JLabel> _labels  = new ArrayList<>();

	/**
	 * 
	 */
	private List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public ExportJavaProgramDlg(Frame arg0, String arg1, boolean arg2) {
		super(arg0, arg1, arg2);
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		if ( Environment.get_instance().get( Environment._exportJavaProgramDialogRectangleKey + "x", "").equals( "")
			|| Environment.get_instance().get( Environment._exportJavaProgramDialogRectangleKey + "x", "").equals( "")
			|| Environment.get_instance().get( Environment._exportJavaProgramDialogRectangleKey + "width", "").equals( "")
			||Environment.get_instance().get( Environment._exportJavaProgramDialogRectangleKey + "height", "").equals( ""))
			return null;
			
		String value = Environment.get_instance().get( Environment._exportJavaProgramDialogRectangleKey + "x", "");
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get( Environment._exportJavaProgramDialogRectangleKey + "y", "");
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get( Environment._exportJavaProgramDialogRectangleKey + "width", "");
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._exportJavaProgramDialogRectangleKey + "height", "");
		if ( null == value)
			return null;

		int height = Integer.parseInt( value);

		return new Rectangle( x, y, width, height);
	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Rectangle rectangle = getBounds();

		Environment.get_instance().set(
			Environment._exportJavaProgramDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._exportJavaProgramDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._exportJavaProgramDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._exportJavaProgramDialogRectangleKey + "height", String.valueOf( rectangle.height));
	}

	/**
	 * @return
	 */
	public boolean do_modal() {
		Rectangle rectangle = get_rectangle_from_environment_file();
		return ( null == rectangle)
			? do_modal( getOwner())
			: do_modal( rectangle);
	}

	@Override
	protected boolean on_init_dialog() {
		if (!super.on_init_dialog())
			return false;


		getContentPane().setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_source_folder_panel( northPanel);

		insert_horizontal_glue( northPanel);

		setup_package_path_panel( northPanel);

		insert_horizontal_glue( northPanel);

		setup_indent_panel( northPanel);

		add( northPanel, "North");

		adjust();

		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		insert_horizontal_glue( southPanel);

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);
		southPanel.add( panel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_source_folder_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "export.java.program.dialog.java.source.folder.path"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_sourceFolderTextField = new JTextField( Environment.get_instance().get( Environment._exportJavaProgramDirectoryKey, ""));
		_sourceFolderTextField.setEditable( false);
		panel.add( _sourceFolderTextField);

		link_to_cancel( _sourceFolderTextField);

		panel.add( Box.createHorizontalStrut( 5));

		JButton button = new JButton( ResourceManager.get_instance().get( "export.java.program.dialog.java.source.folder.path.browse.button"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_browseSourceFolderButton( e);
			}
		});

		link_to_cancel( button);

		panel.add( button);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_browseSourceFolderButton(ActionEvent actionEvent) {
		File folder = CommonTool.get_directory(
			Environment._exportJavaProgramDirectoryKey,
			ResourceManager.get_instance().get( "file.export.java.program.dialog"),
			this);

		if ( null == folder)
			return;

		_sourceFolderTextField.setText( folder.getAbsolutePath());
	}

	/**
	 * @param parent
	 */
	private void setup_package_path_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "export.java.program.dialog.java.class.path"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_packagePathTextField = new JTextField(
			new TextLimiter( "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_/"),
			Environment.get_instance().get( Environment._exportJavaProgramPackagePathKey, "soars2/model"), 0);
		_textUndoRedoManagers .add( new TextUndoRedoManager( _packagePathTextField, this));
		panel.add( _packagePathTextField);

		link_to_cancel( _packagePathTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_indent_panel(JPanel parent) {
		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BoxLayout( basePanel, BoxLayout.X_AXIS));

		basePanel.add( Box.createHorizontalStrut( 5));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		panel.setBorder( BorderFactory.createLineBorder( getForeground(), 1));
		panel.setBorder( new ComponentTitledBorder( new JLabel( ResourceManager.get_instance().get( "export.java.program.dialog.indent.border.name")), panel, BorderFactory.createLineBorder( getForeground())));

		ButtonGroup buttonGroup = new ButtonGroup();
		setup_tab_panel( buttonGroup, panel);
		setup_space_panel( buttonGroup, panel);

		basePanel.add( panel);

		basePanel.add( Box.createHorizontalStrut( 5));

		parent.add( basePanel);
	}

	/**
	 * @param buttonGroup 
	 * @param parent
	 */
	private void setup_tab_panel(ButtonGroup buttonGroup, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JRadioButton radioButton = new JRadioButton( ResourceManager.get_instance().get( "export.java.program.dialog.indent.radio.button.tab"));
		radioButton.addItemListener( new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				_numberOfSpaceLabel.setEnabled( !radioButton.isSelected());
				_numberOfSpaceComboBox.setEnabled( !radioButton.isSelected());
			}
		});
		_radioButtons.add( radioButton);
		buttonGroup.add( radioButton);
		panel.add( radioButton);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup
	 * @param parent
	 */
	private void setup_space_panel(ButtonGroup buttonGroup, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JRadioButton radioButton = new JRadioButton( ResourceManager.get_instance().get( "export.java.program.dialog.indent.radio.button.space"));
		_radioButtons.add( radioButton);
		buttonGroup.add( radioButton);
		panel.add( radioButton);

		_numberOfSpaceLabel = new JLabel( ResourceManager.get_instance().get( "export.java.program.dialog.indent.label.space.length"));
		panel.add( _numberOfSpaceLabel);

		List<String> numbers = new ArrayList<>();
		for ( int i = 1; i <= _maxNumberOfSpace; ++i)
			numbers.add( String.valueOf( i));
		_numberOfSpaceComboBox = new JComboBox<>( numbers.toArray( new String[ numbers.size()]));
		_numberOfSpaceComboBox.setPreferredSize( new Dimension( 50, _numberOfSpaceComboBox.getPreferredSize().height));
		panel.add( _numberOfSpaceComboBox);

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);
		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));
	}

	@Override
	protected void on_setup_completed() {
		_minimumWidth = getPreferredSize().width;
		int height = getPreferredSize().height;

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( 0 > _minimumWidth)
					return;

				int width = getSize().width;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width, height);
			}
		});

		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				set_property_to_environment_file();
			}
		});

		_radioButtons.get( Environment.get_instance().get( Environment._exportJavaProgramDialogIndentTypeKey, "").equals( "space") ? 1 : 0).setSelected( true);
		_numberOfSpaceComboBox.setSelectedIndex( Integer.valueOf( Environment.get_instance().get( Environment._exportJavaProgramDialogNumberOfSpaceForIndentKey, "1")) - 1);
	}

	@Override
	protected void on_ok(ActionEvent actionEvent) {
		String sourceFolderAbsolutePath = _sourceFolderTextField.getText();
		if ( sourceFolderAbsolutePath.equals( "")) {
			JOptionPane.showMessageDialog(
				this,
				ResourceManager.get_instance().get( "export.java.program.source.folder.path.error.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		File folder = new File( sourceFolderAbsolutePath);
		if ( null == folder || !folder.exists()) {
			JOptionPane.showMessageDialog(
				this,
				ResourceManager.get_instance().get( "export.java.program.source.folder.path.error.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		if ( _packagePathTextField.getText().equals( "")) {
			JOptionPane.showMessageDialog(
				this,
				ResourceManager.get_instance().get( "export.java.program.package.path.error.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		Environment.get_instance().set( Environment._exportJavaProgramDirectoryKey, folder.getAbsolutePath());
		Environment.get_instance().set( Environment._exportJavaProgramPackagePathKey, _packagePathTextField.getText());
		Environment.get_instance().set( Environment._exportJavaProgramDialogIndentTypeKey, _radioButtons.get( 0).isSelected() ? "tab" : "space");
		Environment.get_instance().set( Environment._exportJavaProgramDialogNumberOfSpaceForIndentKey, ( String)_numberOfSpaceComboBox.getSelectedItem());

		set_property_to_environment_file();

		super.on_ok(actionEvent);
	}

	@Override
	protected void on_cancel(ActionEvent actionEvent) {
		set_property_to_environment_file();
		super.on_cancel(actionEvent);
	}

	@Override
	public void on_changed(UndoManager undoManager) {
	}

	@Override
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent, UndoManager undoManager) {
	}
}
