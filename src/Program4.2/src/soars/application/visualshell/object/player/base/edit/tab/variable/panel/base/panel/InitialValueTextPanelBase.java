/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 *
 */
public class InitialValueTextPanelBase extends JPanel implements ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	public TextField _textField = null;

	/**
	 * 
	 */
	protected List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * 
	 */
	public InitialValueTextPanelBase() {
		super();
	}

	/**
	 * @param value
	 */
	public void set(String value) {
		_textField.setText( value);
	}

	/**
	 * @return
	 */
	public String get() {
		return _textField.getText();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.undo.UndoManager)
	 */
	public void on_changed(UndoManager undoManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.undo.UndoManager)
	 */
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent,UndoManager undoManager) {
	}

	/**
	 * 
	 */
	public void clear_textUndoRedoManagers() {
		// TODO Auto-generated method stub
		_textUndoRedoManagers.clear();
	}

	/**
	 * 
	 */
	public void setup_textUndoRedoManagers() {
		// TODO Auto-generated method stub
		if ( !_textUndoRedoManagers.isEmpty())
			return;

		_textUndoRedoManagers.add( new TextUndoRedoManager( _textField, this));
	}
}
