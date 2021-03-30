/*
 * 2005/07/12
 */
package soars.application.visualshell.object.experiment.edit.table;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * The renderer for the header of ExperimentTable class.
 * @author kurata / SOARS project
 */
//public class ExperimentTableHeaderRenderer extends JLabel implements TableCellRenderer {
public class ExperimentTableHeaderRenderer extends DefaultTableCellRenderer {

	/**
	 * Cerates a new ExperimentTableHeaderRenderer.
	 */
	public ExperimentTableHeaderRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {

		setOpaque( true);

		setBorder( ( Border)UIManager.getDefaults().get( "TableHeader.cellBorder"));

		ExperimentTable experimentTable = ( ExperimentTable)arg0;

		int[] columns = arg0.getSelectedColumns();
		Arrays.sort( columns);
		if ( 0 <= Arrays.binarySearch( columns, arg5)) {
			setForeground( Color.white);
			setBackground( Color.blue);
		} else {
			setForeground( arg0.getTableHeader().getForeground());
			setBackground( arg0.getTableHeader().getBackground());
		}

		String text = ( null == arg1) ? "" : arg1.toString();
		setText( text);

		return this;
	}
}