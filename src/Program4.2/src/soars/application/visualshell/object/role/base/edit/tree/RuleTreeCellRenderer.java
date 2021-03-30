/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tree;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.application.visualshell.object.role.base.edit.table.data.CommonRuleData;

/**
 * @author kurata
 *
 */
public class RuleTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	public RuleTreeCellRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree arg0, Object arg1,
		boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {

		super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5, 	arg6);

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)arg1;
		Object object = defaultMutableTreeNode.getUserObject();
		if ( null == object)
			setText( "unknown");
		else {
			if ( object instanceof String) {
				String text = ( String)object;

				Color color = null;
				if ( text.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.inclusion.condition")))
					color = CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.list"), "condition");
				else if ( text.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.specified.condition")))
					color = CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.list"), "condition");
				else if ( text.equals( ResourceManager.get_instance().get( "edit.rule.dialog.command.collection")))
					color = CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.collection"), "command");
				else if ( text.equals( ResourceManager.get_instance().get( "edit.rule.dialog.command.list")))
					color = CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.list"), "command");
				else if ( text.equals( ResourceManager.get_instance().get( "edit.rule.dialog.command.dynamic.creation")))
					color = CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.create.agent"), "command");
				else if ( text.equals( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra")))
					color = CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"), "command");

				if ( null != color) {
					if ( arg2 || arg6) {
						setForeground( Color.white);
						setBackground( color);
					} else {
						setForeground( color);
						//setBackground( arg0.getBackground());
					}
				}

				setText( text);

				if ( !arg3)
					setIcon( getClosedIcon());

			} else if ( object instanceof RulePropertyPageBase) {
				RulePropertyPageBase rulePropertyPageBase = ( RulePropertyPageBase)object;
				if ( null != rulePropertyPageBase._color) {
					if ( arg2 || arg6) {
						setForeground( Color.white);
						setBackground( rulePropertyPageBase._color);
					} else {
						setForeground( rulePropertyPageBase._color);
						//setBackground( arg0.getBackground());
					}
				}

				setText( String.valueOf( rulePropertyPageBase._index + 1) + ". " + rulePropertyPageBase._title);
			} else
				setText( "unknown");
		}

		return this;
	}
}
