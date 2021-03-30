/*
 * Created on 2005/09/16
 */
package soars.application.visualshell.object.log.edit;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import soars.application.visualshell.object.log.LogOptionManager;
import soars.application.visualshell.object.log.edit.option.LogOptionList;
import soars.common.utility.swing.tab.TabbedPage;

/**
 * The base tab component to edit the log options.
 * @author kurata / SOARS project
 */
public class LogPropertyPageBase extends TabbedPage {

	/**
	 * Title for the tab.
	 */
	public String _title = "";

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * Creates this object.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public LogPropertyPageBase(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param text
	 * @param panel
	 * @return
	 */
	protected void setup_label(String text, JPanel panel) {
		JPanel partial_panel = new JPanel();
		partial_panel.setLayout( new BoxLayout( partial_panel, BoxLayout.X_AXIS));

		JPanel label_panel = new JPanel();
		label_panel.setLayout( new GridLayout( 1, 1));

		JLabel label = new JLabel( text);

		label_panel.add( label);
		partial_panel.add( label_panel);

		partial_panel.add( Box.createHorizontalStrut( 5));

		panel.add( partial_panel);
	}

	/**
	 * @param logOptionManager
	 * @param panel
	 * @return
	 */
	protected LogOptionList setup_list(LogOptionManager logOptionManager, JPanel panel) {
		JPanel partial_panel = new JPanel();
		partial_panel.setLayout( new BoxLayout( partial_panel, BoxLayout.X_AXIS));

		JPanel list_panel = new JPanel();
		list_panel.setLayout( new GridLayout( 1, 1));

		LogOptionList logOptionList = new LogOptionList();
		if ( !logOptionList.setup( logOptionManager))
			return null;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( logOptionList);

		list_panel.add( scrollPane);
		partial_panel.add( list_panel);

		partial_panel.add( Box.createHorizontalStrut( 5));

		panel.add( partial_panel);

		return logOptionList;
	}
}
