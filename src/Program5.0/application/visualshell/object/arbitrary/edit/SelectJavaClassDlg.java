/**
 * 
 */
package soars.application.visualshell.object.arbitrary.edit;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.window.Dialog;
import soars.common.utility.tool.reflection.Reflection;

/**
 * The dialog box to select java class.
 * @author kurata / SOARS project
 */
public class SelectJavaClassDlg extends Dialog {

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
	private List<String> _exceptedJavaClasses = null;

	/**
	 * 
	 */
	private JLabel _label = null;

	/**
	 * 
	 */
	private JTextField _javaClassNameTextField = null;

	/**
	 * 
	 */
	protected String _javaClassName = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param exceptedJavaClasses
	 * @throws HeadlessException
	 */
	public SelectJavaClassDlg(Frame arg0, String arg1, boolean arg2, List<String> exceptedJavaClasses) {
		super(arg0, arg1, arg2);
		_exceptedJavaClasses = exceptedJavaClasses;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		link_to_cancel( getRootPane());


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup_javaClassNameTextFieldd();

		insert_horizontal_glue();

		setup_ok_and_cancel_button(
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			true, true);

		insert_horizontal_glue();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * 
	 */
	private void setup_javaClassNameTextFieldd() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_label = new JLabel(
			ResourceManager.get_instance().get( "select.java.class.dialog.name"));
		panel.add( _label);

		panel.add( Box.createHorizontalStrut( 5));

		_javaClassNameTextField = new JTextField();
		_javaClassNameTextField.setPreferredSize( new Dimension( 400, _javaClassNameTextField.getPreferredSize().height));

		link_to_cancel( _javaClassNameTextField);

		panel.add( _javaClassNameTextField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_javaClassNameTextField.requestFocusInWindow();

		_minimumWidth = getPreferredSize().width;
		_minimumHeight = getPreferredSize().height;

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( 0 > _minimumWidth || 0 > _minimumHeight)
					return;

				int width = getSize().width;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width, _minimumHeight);
			}
		});

		setAlwaysOnTop( true);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		String javaClassName = _javaClassNameTextField.getText();
		if ( javaClassName.equals( ""))
			return;

		if ( _exceptedJavaClasses.contains( javaClassName)) {
			JOptionPane.showMessageDialog(
				this,
				javaClassName + String.format( " %s", ResourceManager.get_instance().get( "select.java.class.dialog.errot.already.exists")),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE );
			return;
		}

		if ( !Reflection.exists( javaClassName)) {
			JOptionPane.showMessageDialog(
				this,
				javaClassName + String.format( " %s", ResourceManager.get_instance().get( "select.java.class.dialog.errot.does.not.exist")),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE );
			return;
		}

		_javaClassName = javaClassName;

		super.on_ok(actionEvent);
	}
}
