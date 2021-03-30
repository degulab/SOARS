/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.common.functional_object;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author kurata
 *
 */
//public class ParameterTableHeaderRenderer extends JLabel implements TableCellRenderer {
public class ParameterTableHeaderRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private Color _color = null;

	/**
	 * @param color
	 */
	public ParameterTableHeaderRenderer(Color color) {
		super();
		_color = color;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {

		if ( !arg0.isEnabled())
			setEnabled( false);
		else {
			setForeground( _color);
			setEnabled( true);
		}

		setHorizontalAlignment( JLabel.RIGHT);

		String text = ( null == arg1) ? "" : ( String)arg1;

		setOpaque( true);
		setBorder( ( Border)UIManager.getDefaults().get( "TableHeader.cellBorder"));
		setText( text);

		return this;
	}

//	/* (non-Javadoc)
//	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
//	 */
//	public Component getTableCellRendererComponent(JTable arg0, Object arg1,
//		boolean arg2, boolean arg3, int arg4, int arg5) {
//
//		if ( !arg0.isEnabled())
//			setEnabled( false);
//		else {
//			setForeground( _color);
//			setEnabled( true);
//		}
//
//		setHorizontalAlignment( JLabel.RIGHT);
//
//		String text = ( null == arg1) ? "" : ( String)arg1;
//
//		setOpaque( true);
//		setBorder( ( Border)UIManager.getDefaults().get( "TableHeader.cellBorder"));
//		setText( text);
//
//		return this;
//	}
}
