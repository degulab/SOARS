/**
 * 
 */
package soars.application.visualshell.object.gis.edit.variable.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.edit.variable.object.base.SimpleVariableObject;
import soars.application.visualshell.object.gis.edit.variable.object.keyword.KeywordObject;
import soars.application.visualshell.object.gis.edit.variable.object.number.NumberObject;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;

/**
 * @author kurata
 *
 */
public class VariableTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	public VariableTableRowRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {

		setOpaque( true);

		String text = "";
		if ( null != arg1 && arg1 instanceof SimpleVariableObject) {
			SimpleVariableObject simpleVariableObject = ( SimpleVariableObject)arg1;
			switch ( arg0.convertColumnIndexToModel( arg5)) {
				case 0:
//					if ( value instanceof ProbabilityObject)
//						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.probability");
					if ( arg1 instanceof KeywordObject)
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword");
					else if ( arg1 instanceof NumberObject)
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object");
//					else if ( value instanceof RoleVariableObject)
//						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable");
//					else if ( value instanceof TimeVariableObject)
//						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable");
//					else if ( value instanceof SpotVariableObject)
//						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable");
					break;
				case 1:
					text = simpleVariableObject._name;
					break;
				case 2:
					text = Field.get( simpleVariableObject._fields);
					break;
				case 3:
					if ( simpleVariableObject instanceof NumberObject) {
						NumberObject numberObject = ( NumberObject)simpleVariableObject;
						text = NumberObject.get_type_name( numberObject._type);
					}
					break;
				case 4:
					text = simpleVariableObject._comment;
			}

//			if ( value instanceof ProbabilityObject) {
//				setForeground( isSelected ? Color.white : SimpleVariablePropertyPage._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability")));
//				setBackground( isSelected ? SimpleVariablePropertyPage._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability")) : table.getBackground());
			if ( arg1 instanceof KeywordObject) {
				setForeground( arg2 ? Color.white : PropertyPageBase._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword")));
				setBackground( arg2 ? PropertyPageBase._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword")) : arg0.getBackground());
			} else if ( arg1 instanceof NumberObject) {
				setForeground( arg2 ? Color.white : PropertyPageBase._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object")));
				setBackground( arg2 ? PropertyPageBase._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object")) : arg0.getBackground());
//			} else if ( value instanceof RoleVariableObject) {
//				setForeground( isSelected ? Color.white : SimpleVariablePropertyPage._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable")));
//				setBackground( isSelected ? SimpleVariablePropertyPage._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable")) : table.getBackground());
//			} else if ( value instanceof TimeVariableObject) {
//				setForeground( isSelected ? Color.white : SimpleVariablePropertyPage._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable")));
//				setBackground( isSelected ? SimpleVariablePropertyPage._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable")) : table.getBackground());
//			} else if ( value instanceof SpotVariableObject) {
//				setForeground( isSelected ? Color.white : SimpleVariablePropertyPage._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable")));
//				setBackground( isSelected ? SimpleVariablePropertyPage._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable")) : table.getBackground());
			}
//			setForeground( isSelected ? SystemColor.textHighlightText : SystemColor.textText);
//			setBackground( isSelected ? SystemColor.textHighlight : SystemColor.text);
		}

		setText( text);

		return this;
	}
}
