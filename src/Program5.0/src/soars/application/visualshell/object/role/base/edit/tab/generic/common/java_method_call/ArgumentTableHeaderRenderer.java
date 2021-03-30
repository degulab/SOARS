/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.java_method_call;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author kurata
 *
 */
public class ArgumentTableHeaderRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private Color _color = null;

	/**
	 * @param color 
	 * 
	 */
	public ArgumentTableHeaderRenderer(Color color) {
		_color = color;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if ( !table.isEnabled())
			setEnabled( false);
		else {
			setForeground( _color);
			setEnabled( true);
		}

		//setHorizontalAlignment( JLabel.RIGHT);

		String text = ( null == value) ? "" : ( String)value;

		setOpaque( true);
		setBorder( ( Border)UIManager.getDefaults().get( "TableHeader.cellBorder"));
		setText( text);

		return this;
	}
}
