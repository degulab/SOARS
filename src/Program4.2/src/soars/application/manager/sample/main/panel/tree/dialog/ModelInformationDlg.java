/**
 * 
 */
package soars.application.manager.sample.main.panel.tree.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.application.manager.sample.file.VisualShell;
import soars.application.manager.sample.main.ResourceManager;
import soars.application.manager.sample.property.ModelInformation;
import soars.application.manager.sample.property.Property;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class ModelInformationDlg extends Dialog {

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
	private JTextField _titleTextField = null;

	/**
	 * 
	 */
	private JTextField _dateTextField = null;

	/**
	 * 
	 */
	private JTextField _authorTextField = null;

	/**
	 * 
	 */
	private JTextField _emailTextField = null;

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private JTextArea _commentTextArea = null;

	/**
	 * 
	 */
	private File _folder = null;

	/**
	 * 
	 */
	private Property _property = null;

	/**
	 * 
	 */
	public File _file = null;

	/**
	 * 
	 */
	private ModelInformation _modelInformation = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param folder 
	 * @throws HeadlessException
	 */
	public ModelInformationDlg(Frame arg0, String arg1, boolean arg2, File folder) throws HeadlessException {
		super(arg0, arg1, arg2);
		_folder = folder;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param property
	 * @param file
	 */
	public ModelInformationDlg(Frame arg0, String arg1, boolean arg2, Property property, File file) {
		super(arg0, arg1, arg2);
		_property = property;
		_file = file;
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

		getContentPane().setLayout( new BorderLayout());

		if ( null != _property) {
			_modelInformation = _property.get_model_information( _file);
			if ( null == _modelInformation)
				return false;
		}

		setup_north_panel();

		setup_center_panel();

		setup_south_panel();


		setPreferredSize( new Dimension( 800, 600));


		adjust();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 *  
	 */
	private void setup_north_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( panel);

		setup_titleTextField(  panel);

		insert_horizontal_glue( panel);

		setup_dateTextField(  panel);

		insert_horizontal_glue( panel);

		setup_authorTextField(  panel);

		insert_horizontal_glue( panel);

		setup_emailTextField(  panel);

		insert_horizontal_glue( panel);

		getContentPane().add( panel, "North");
	}

	/**
	 * @param parent
	 */
	private void setup_titleTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel(
			ResourceManager.get( "model.information.dialog.label.title"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		link_to_cancel( label);
		_labels.add( label);
		panel.add( label);


		panel.add( Box.createHorizontalStrut( 5));


		_titleTextField = new JTextField();
		_titleTextField.setDocument( new TextExcluder( "\t"));
		_titleTextField.setText( ( null != _modelInformation) ? _modelInformation._title : "");
		link_to_cancel( _titleTextField);

		panel.add( _titleTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_dateTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel(
			ResourceManager.get( "model.information.dialog.label.date"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		link_to_cancel( label);
		_labels.add( label);
		panel.add( label);


		panel.add( Box.createHorizontalStrut( 5));


		_dateTextField = new JTextField();
		_dateTextField.setDocument( new TextExcluder( "\t"));
		_dateTextField.setText( ( null != _modelInformation) ? _modelInformation._date : get_current_date());
		link_to_cancel( _dateTextField);

		panel.add( _dateTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @return
	 */
	private String get_current_date() {
		return ( String.valueOf( Calendar.getInstance().get( Calendar.YEAR)) + "."
			+ String.valueOf( Calendar.getInstance().get( Calendar.MONTH) + 1) + "."
			+ String.valueOf( Calendar.getInstance().get( Calendar.DATE)));
	}

	/**
	 * @param parent
	 */
	private void setup_authorTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel(
			ResourceManager.get( "model.information.dialog.label.author"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		link_to_cancel( label);
		_labels.add( label);
		panel.add( label);


		panel.add( Box.createHorizontalStrut( 5));


		_authorTextField = new JTextField();
		_authorTextField.setDocument( new TextExcluder( "\t"));
		_authorTextField.setText( ( null != _modelInformation) ? _modelInformation._author : "");
		link_to_cancel( _authorTextField);

		panel.add( _authorTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_emailTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel(
			ResourceManager.get( "model.information.dialog.label.email"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		link_to_cancel( label);
		_labels.add( label);
		panel.add( label);


		panel.add( Box.createHorizontalStrut( 5));


		_emailTextField = new JTextField();
		_emailTextField.setDocument( new TextExcluder( "\t"));
		_emailTextField.setText( ( null != _modelInformation) ? _modelInformation._email : "");
		link_to_cancel( _emailTextField);

		panel.add( _emailTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 *  
	 */
	private void setup_center_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		setup_commentTextArea( panel);

		getContentPane().add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_commentTextArea(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel textAreaPanel = new JPanel();
		textAreaPanel.setLayout( new GridLayout( 1, 1));

		_commentTextArea = new JTextArea( new TextExcluder( "\t"));
		_commentTextArea.setText( ( null != _modelInformation) ? _modelInformation._comment : "");
		_commentTextArea.setLineWrap( true);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _commentTextArea);

		textAreaPanel.add( scrollPane);
		panel.add( textAreaPanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void setup_south_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( panel);

		setup_buttons( panel);

		insert_horizontal_glue( panel);

		getContentPane().add( panel, "South");
	}

	/**
	 * @param parent
	 */
	private void setup_buttons(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		JButton startVisualShellButton = new JButton( ResourceManager.get( "model.information.dialog.button.save.and.start.visual.shell"));

		startVisualShellButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_start_visual_shell( arg0);
			}
		});

		link_to_cancel( startVisualShellButton);

		panel.add( startVisualShellButton);


		JButton okButton = new JButton( ResourceManager.get( "dialog.ok"));
		getRootPane().setDefaultButton( okButton);
		okButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_ok( arg0);
			}
		});

		link_to_cancel( okButton);

		panel.add( okButton);


		JButton cancelButton = new JButton( ResourceManager.get( "dialog.cancel"));
		cancelButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_cancel( arg0);
			}
		});

		link_to_cancel( cancelButton);

		panel.add( cancelButton);


		int width = Math.max( okButton.getPreferredSize().width, cancelButton.getPreferredSize().width);
		okButton.setPreferredSize( new Dimension( width, okButton.getPreferredSize().height));
		cancelButton.setPreferredSize( new Dimension( width, cancelButton.getPreferredSize().height));
//		int width = Math.max( startVisualShellButton.getPreferredSize().width, okButton.getPreferredSize().width);
//		width = Math.max( width, cancelButton.getPreferredSize().width);
//
//		startVisualShellButton.setPreferredSize( new Dimension( width, startVisualShellButton.getPreferredSize().height));
//		okButton.setPreferredSize( new Dimension( width, okButton.getPreferredSize().height));
//		cancelButton.setPreferredSize( new Dimension( width, cancelButton.getPreferredSize().height));

		parent.add( panel);
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
		_titleTextField.requestFocusInWindow();

		_minimumWidth = getPreferredSize().width;
		_minimumHeight = getPreferredSize().height;
	}

	/**
	 * @param actionEvent
	 */
	protected void on_start_visual_shell(ActionEvent actionEvent) {
		if ( !save())
			return;

		if ( null == _file || !_file.exists() || !_file.isFile())
			return;

		soars.application.manager.sample.executor.VisualShell.start( _file);

		super.on_ok(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		if ( !save())
			return;

		super.on_ok(actionEvent);
	}

	/**
	 * @return
	 */
	private boolean save() {
		if ( null != _folder) {
			File file = VisualShell.create(
				_folder,
				_titleTextField.getText(),
				_dateTextField.getText(),
				_authorTextField.getText(),
				_emailTextField.getText(),
				_commentTextArea.getText());
			if ( null == file) {
				JOptionPane.showMessageDialog( this,
					ResourceManager.get( "model.information.dialog.error.new.simulation.model.message"),
					ResourceManager.get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
			_file = file;
		} else {
			if ( !_property.update_model_information(
				_modelInformation,
				_titleTextField.getText(),
				_dateTextField.getText(),
				_authorTextField.getText(),
				_emailTextField.getText(),
				_commentTextArea.getText())) {
				_modelInformation.cleanup();
				JOptionPane.showMessageDialog( this,
					ResourceManager.get( "model.information.dialog.error.update.model.information.message"),
					ResourceManager.get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
			_modelInformation.cleanup();
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		if ( null != _modelInformation)
			_modelInformation.cleanup();

		super.on_cancel(actionEvent);
	}
}
