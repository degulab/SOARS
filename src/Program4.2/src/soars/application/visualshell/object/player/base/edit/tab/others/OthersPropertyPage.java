/*
 * Created on 2005/10/16
 */
package soars.application.visualshell.object.player.base.edit.tab.others;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 */
public class OthersPropertyPage extends PropertyPageBase {

	/**
	 * 
	 */
	private JTextArea _othersTextArea = null;

	/**
	 * @param title
	 * @param playerBase
	 * @param propertyPageMap
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public OthersPropertyPage(String title, PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap, int index, Frame owner, Component parent) {
		super(title, playerBase, propertyPageMap, index, owner, parent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.tab.TabbedPage#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;



		setLayout( new BorderLayout());



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( center_panel);

		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		setup_others_text_area( panel);

		center_panel.add( panel);


		insert_vertical_strut( center_panel);


		add( center_panel);


		return true;
	}

	/**
	 * @param panel
	 */
	private void setup_others_text_area(JPanel panel) {
		JPanel text_area_panel = new JPanel();
		text_area_panel.setLayout( new BoxLayout( text_area_panel, BoxLayout.X_AXIS));

		text_area_panel.add( Box.createHorizontalStrut( 5));

		_othersTextArea = new JTextArea( new TextExcluder( "\t"));
		_othersTextArea.setText( _playerBase._others);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _othersTextArea, this));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _othersTextArea);

		if ( _playerBase.is_multi())
			_othersTextArea.setEnabled( false);

		text_area_panel.add( scrollPane);

		text_area_panel.add( Box.createHorizontalStrut( 5));

		panel.add( text_area_panel);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#on_setup_completed()
	 */
	public void on_setup_completed() {
		_othersTextArea.requestFocusInWindow();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#on_ok()
	 */
	public boolean on_ok() {
		_playerBase._others = _othersTextArea.getText();
		return true;
	}
}
