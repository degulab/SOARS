/**
 * 
 */
package soars.application.visualshell.common.image;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box to rename the thumbnail.
 * @author kurata / SOARS project
 */
public class EditImageFilenameDlg extends Dialog {

	/**
	 * Original filename.
	 */
	private String _original_filename = null;

	/**
	 * Image property hashtable(String[filename] - ImageProperty)
	 */
	private TreeMap _imagePropertyManager = null;

	/**
	 * New filename.
	 */
	public String _new_filename = null;

	/**
	 * Filename text field.
	 */
	private JTextField _filename_textField = null;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog.
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param original_filename the Original filename
	 * @param imagePropertyManager the Image property hashtable(String[filename] - ImageProperty)
	 * @throws HeadlessException
	 */
	public EditImageFilenameDlg(Frame arg0, String arg1, boolean arg2, String original_filename, TreeMap imagePropertyManager) throws HeadlessException {
		super(arg0, arg1, arg2);
		_original_filename = original_filename;
		_imagePropertyManager = imagePropertyManager;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		setResizable( false);


		link_to_cancel( getRootPane());


		getContentPane().setLayout( new BorderLayout());


		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup( north_panel);

		getContentPane().add( north_panel, "North");


		JPanel south_panel = new JPanel();
		south_panel.setLayout( new BoxLayout( south_panel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		insert_horizontal_glue( south_panel);

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			true, true);
		south_panel.add( panel);

		insert_horizontal_glue( south_panel);

		getContentPane().add( south_panel, "South");


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		return true;
	}

	/**
	 * Creates the filename text field.
	 * @param parent the parent container of the component on which the filename text field is displayed
	 */
	private void setup(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel(
			ResourceManager.get_instance().get( "edit.image.filename.dialog.filename.label"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_filename_textField = new JTextField( new TextExcluder( "\\/;*?\"<>|':"), _original_filename, 0);
		_filename_textField.setPreferredSize( new Dimension( 200, _filename_textField.getPreferredSize().height));
		link_to_cancel( _filename_textField);
		panel.add( _filename_textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		String new_filename = _filename_textField.getText();
		if ( null == new_filename || new_filename.equals( "") || new_filename.equals( _original_filename)
			|| new_filename.matches( "\\.+") || new_filename.matches( "^[\\.+].*"))
			return;

		if ( null != _imagePropertyManager.get( new_filename)) {
			JOptionPane.showMessageDialog(
				this,
				ResourceManager.get_instance().get( "edit.image.filename.dialog.duplicated.filename.show.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		_new_filename = new_filename;

		super.on_ok(actionEvent);
	}
}
