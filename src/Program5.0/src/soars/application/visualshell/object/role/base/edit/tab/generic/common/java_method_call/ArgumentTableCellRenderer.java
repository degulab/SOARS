/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.java_method_call;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;

/**
 * @author kurata
 *
 */
public class ArgumentTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	private Role _role = null;

	/**
	 * 
	 */
	private Color _color = null;

	/**
	 * @param role 
	 * @param color 
	 */
	public ArgumentTableCellRenderer(Role role, Color color) {
		super();
		_role = role;
		_color = color;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		//setHorizontalAlignment( JLabel.RIGHT);

		String text = "Unknown!";

		switch ( column) {
			case 0:
				text = ( null == value) ? "" : ( String)value;
				setToolTipText( text);

				String[] words = text.split( "\\.");
				if ( null != words && 0 < words.length)
					text = words[ words.length - 1];

				break;
			case 1:
				EntityVariableRule entityVariableRule = ( EntityVariableRule)value;
				text = ( null == entityVariableRule) ? "" : entityVariableRule.get_cell_text( _role, false);
				break;
		}

		setOpaque( true);

		if ( !table.isEnabled())
			setEnabled( false);
		else {
			setForeground( isSelected ? Color.white : _color);
			setEnabled( true);
		}

		//setForeground( isSelected ? Color.white : _color);
		setBackground( isSelected ? _color : table.getBackground());

		setText( text);

		return this;
	}
}
