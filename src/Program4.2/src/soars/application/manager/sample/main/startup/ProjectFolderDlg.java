/**
 * 
 */
package soars.application.manager.sample.main.startup;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.manager.sample.common.tool.CommonTool;
import soars.application.manager.sample.main.Constant;
import soars.application.manager.sample.main.Environment;
import soars.application.manager.sample.main.ResourceManager;
import soars.common.utility.swing.window.Dialog;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 *
 */
public class ProjectFolderDlg extends Dialog {

	/**
	 * 
	 */
	private int _minimumWidth = -1;

	/**
	 * 
	 */
	private int _minimumHeight = -1;

	/**
	 * 
	 */
	private int _numberOfProjectFolder = 10;

	/**
	 * 
	 */
	private String _buttonName = null;

	/**
	 * 
	 */
	private JComboBox _projectFolderComboBox = null;

	/**
	 * 
	 */
	private JButton _selectProjectFolderButton = null;

	/**
	 * 
	 */
	private JCheckBox _confirmCheckBox = null;

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param buttonName 
	 * @throws HeadlessException
	 */
	public ProjectFolderDlg(Frame arg0, String arg1, boolean arg2, String buttonName) throws HeadlessException {
		super(arg0, arg1, arg2);
		_buttonName = buttonName;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( 0 > _minimumWidth || 0 > _minimumHeight)
					return;

				int width = getSize().width;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width, _minimumHeight);
			}
		});


		link_to_cancel( getRootPane());


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup_projectFolderComboBox();

		insert_horizontal_glue();

		setup_confirmCheckBox();

		insert_horizontal_glue();

		setup_ok_and_cancel_button( ResourceManager.get( "dialog.ok"), _buttonName, true, true);

		insert_horizontal_glue();


		adjust();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			setIconImage( image);

		return true;
	}

	/**
	 * 
	 */
	private void setup_projectFolderComboBox() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get( "project.folder.dialog.folder"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_projectFolderComboBox = new JComboBox( get_project_folders());
		_projectFolderComboBox.setEditable( true);
		_projectFolderComboBox.setPreferredSize( new Dimension( 400, _projectFolderComboBox.getPreferredSize().height));

		link_to_cancel( _projectFolderComboBox);

		panel.add( _projectFolderComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		_selectProjectFolderButton = new JButton( ResourceManager.get( "project.folder.dialog.folder.button"));
		_selectProjectFolderButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_selectFolderButton( e);
			}
		});

		link_to_cancel( _selectProjectFolderButton);

		panel.add( _selectProjectFolderButton);

		panel.add( Box.createHorizontalStrut( 5));

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_confirmCheckBox() {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_confirmCheckBox = new JCheckBox( ResourceManager.get( "project.folder.dialog.confirm.check.box"),
			Environment.get_instance().get( Environment._defaultProjectFolderKey, "false").equals( "true"));

		link_to_cancel( _confirmCheckBox);

		panel.add( _confirmCheckBox);

		getContentPane().add( panel);
	}

	/**
	 * @return
	 */
	private Vector<String> get_project_folders() {
		Vector<String> projectFolders = new Vector<String>();
		for ( int i = 0; i < _numberOfProjectFolder; ++i) {
			String path = Environment.get_instance().get( Environment._projectFolderKey + String.valueOf( i), "");
			if ( path.equals( ""))
				break;

			File folder = new File( path);
			if ( !folder.exists() || !folder.isDirectory())
				continue;

			projectFolders.add( path);
		}

		if ( projectFolders.isEmpty()) {
			String path = ( System.getProperty( "user.home") + File.separator + "SOARS" + File.separator + "workspace");
			Environment.get_instance().set( Environment._projectFolderKey + "0", path);
			projectFolders.add( path);
		}

		return projectFolders;
	}

	/**
	 * @param actionEvent
	 */
	protected void on_selectFolderButton(ActionEvent actionEvent) {
		File folder = CommonTool.get_directory( Environment._projectFolderDirectoryKey, ResourceManager.get( "project.folder.dialog.title"), this);
		if ( null == folder)
			return;

		_projectFolderComboBox.getEditor().setItem( folder.getAbsolutePath());
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JLabel label:_labels)
			width = Math.max( label.getPreferredSize().width, width);
		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_projectFolderComboBox.requestFocusInWindow();

		_minimumWidth = getPreferredSize().width;
		_minimumHeight = getPreferredSize().height;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		File projectFolder = new File( ( String)_projectFolderComboBox.getEditor().getItem());
		if ( projectFolder.exists() && !projectFolder.isDirectory()) {
			JOptionPane.showMessageDialog( this,
				ResourceManager.get( "project.folder.dialog.error.new.project.folder.message"),
				ResourceManager.get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		if ( !projectFolder.exists()) {
			if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog( this,
				ResourceManager.get( "project.folder.dialog.confirm.new.project.folder.message"),
				ResourceManager.get( "application.title"),
				JOptionPane.YES_NO_OPTION))
				return;

			if ( !projectFolder.mkdirs()) {
				JOptionPane.showMessageDialog( this,
					ResourceManager.get( "project.folder.dialog.error.new.project.folder.message"),
					ResourceManager.get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		append_new_project_folder( projectFolder);

		Environment.get_instance().set( Environment._defaultProjectFolderKey, String.valueOf( _confirmCheckBox.isSelected()));

		super.on_ok(actionEvent);
	}

	/**
	 * @param projectFolder
	 */
	private void append_new_project_folder(File projectFolder) {
		Environment.get_instance().set( Environment._projectFolderKey + "0", projectFolder.getAbsolutePath());

		int index = 1;
		for ( int i = 0; i < _projectFolderComboBox.getItemCount(); ++i) {
			if ( _numberOfProjectFolder <= index)
				break;

			if ( projectFolder.getAbsolutePath().equals( ( String)_projectFolderComboBox.getItemAt( i)))
				continue;

			Environment.get_instance().set( Environment._projectFolderKey + String.valueOf( index++), ( String)_projectFolderComboBox.getItemAt( i));
		}
	}
}
