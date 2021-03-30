/**
 * 
 */
package soars.tool.simulator.launcher.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.common.soars.environment.CommonEnvironment;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class LanguageSelectorDlg extends Dialog {

	/**
	 * 
	 */
	private Map _language_name_map = new HashMap();

	/**
	 * 
	 */
	private Map _language_map = new TreeMap();

	/**
	 * 
	 */
	private JComboBox _language_comboBox = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws HeadlessException
	 */
	public LanguageSelectorDlg(Frame arg0, String arg1, boolean arg2) throws HeadlessException {
		super(arg0, arg1, arg2);

		for ( int i = 0; i < Constant._languages.length; ++i) {
			_language_name_map.put( Constant._languages[ i][ 0], ResourceManager.get( "launcher.dialog.language." + Constant._languages[ i][ 1]));
			_language_map.put( ResourceManager.get( "launcher.dialog.language." + Constant._languages[ i][ 1]), Constant._languages[ i][ 0]);
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		setResizable( false);


		getContentPane().setLayout( new BorderLayout());


		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( center_panel);

		setup( center_panel);

		getContentPane().add( center_panel);


		JPanel south_panel = new JPanel();
		south_panel.setLayout( new BoxLayout( south_panel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		insert_horizontal_glue( south_panel);

		setup_ok_and_cancel_button(
			panel,
			"OK",	// ResourceManager.get( "dialog.ok"),
			"Cancel",	//ResourceManager.get( "dialog.cancel"),
			true, true);
		south_panel.add( panel);

		insert_horizontal_glue( south_panel);

		getContentPane().add( south_panel, "South");


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));


		panel.add( Box.createHorizontalStrut( 5));


		JLabel label = new JLabel( ResourceManager.get( "launcher.dialog.language"));
		link_to_cancel( label);
		panel.add( label);


		panel.add( Box.createHorizontalStrut( 5));


		_language_comboBox = new JComboBox( ( String[])_language_map.keySet().toArray( new String[ 0]));
		_language_comboBox.setPreferredSize( new Dimension( 200, _language_comboBox.getPreferredSize().height));
		_language_comboBox.setSelectedItem( _language_name_map.get(
			CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, "en")));
		link_to_cancel( _language_comboBox);
		panel.add( _language_comboBox);


		panel.add( Box.createHorizontalStrut( 5));


		parent.add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		CommonEnvironment.get_instance().set( CommonEnvironment._localeKey, ( String)_language_map.get( _language_comboBox.getSelectedItem()));
		super.on_ok(actionEvent);
	}
}
