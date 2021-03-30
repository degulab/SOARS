/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;

/**
 * @author kurata
 *
 */
public class PlayerTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	public PlayerTreeCellRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {
		super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5, arg6);

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)arg1;
		Object object = defaultMutableTreeNode.getUserObject();
		if ( null == object)
			setText( "unknown");
		else {
			if ( object instanceof PropertyPageBase) {
				PropertyPageBase propertyPageBase = ( PropertyPageBase)object;
				setText( String.valueOf( propertyPageBase._index + 1) + ". " + propertyPageBase._title);
			}
		}

		return this;
	}
}
