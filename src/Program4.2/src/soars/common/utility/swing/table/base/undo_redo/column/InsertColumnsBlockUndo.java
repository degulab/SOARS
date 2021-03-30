/**
 * 
 */
package soars.common.utility.swing.table.base.undo_redo.column;

import javax.swing.JTable;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import soars.common.utility.swing.table.base.undo_redo.base.BaseUndo;
import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;

/**
 * @author kurata
 *
 */
public class InsertColumnsBlockUndo extends BaseUndo {

	/**
	 * 
	 */
	public int _rowFrom;

	/**
	 * 
	 */
	public int _rowTo;

	/**
	 * 
	 */
	public int _columnFrom;

	/**
	 * 
	 */
	public int _columnTo;

	/**
	 * 
	 */
	public int _width;

	/**
	 * @param rowFrom
	 * @param rowTo
	 * @param columnFrom
	 * @param columnTo
	 * @param width
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public InsertColumnsBlockUndo(int rowFrom, int rowTo, int columnFrom, int columnTo, int width, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(table, tableUndoRedoCallBack);
		_rowFrom = rowFrom;
		_rowTo = rowTo;
		_columnFrom = columnFrom;
		_columnTo = columnTo;
		_width = width;
		_tableUndoRedoCallBack = tableUndoRedoCallBack;
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	public void undo() throws CannotUndoException {
		super.undo();
		_tableUndoRedoCallBack.insertColumnsBlock( "undo", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	public void redo() throws CannotRedoException {
		super.redo();
		_tableUndoRedoCallBack.insertColumnsBlock( "redo", this);
	}
}