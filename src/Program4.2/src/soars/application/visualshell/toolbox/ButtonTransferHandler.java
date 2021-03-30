/*
 * 2005/04/18
 */
package soars.application.visualshell.toolbox;

import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;

import soars.common.utility.swing.dnd.common.StringTransferHandler;
import soars.application.visualshell.main.MainFrame;

/**
 * @author kurata
 */
public class ButtonTransferHandler extends StringTransferHandler {


	/**
	 * 
	 */
	private HashMap _button_keyword_map = null;

	/**
	 * 
	 */
	public ButtonTransferHandler() {
		super();
	}

	/**
	 * @param arg0
	 */
	public ButtonTransferHandler(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param button_keyword_map
	 */
	public ButtonTransferHandler(String arg0, HashMap button_keyword_map) {
		_button_keyword_map = button_keyword_map;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.dnd.common.StringTransferHandler#exportString(javax.swing.JComponent)
	 */
	protected String exportString(JComponent c) {
		String name = ( String)_button_keyword_map.get( c);
		if ( null == name)
			return "";

		return name;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.dnd.common.StringTransferHandler#importString(javax.swing.JComponent, java.lang.String)
	 */
	protected void importString(JComponent c, String str) {
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.dnd.common.StringTransferHandler#cleanup(javax.swing.JComponent, boolean)
	 */
	protected void cleanup(JComponent c, boolean remove) {
		JButton button = ( JButton)c;
		button.doClick();
		MainFrame.get_instance().on_end_of_drag();
	}
}
