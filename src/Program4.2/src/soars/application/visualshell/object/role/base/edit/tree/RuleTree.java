/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tree;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import soars.application.visualshell.common.swing.TreeBase;
import soars.application.visualshell.object.role.base.edit.tab.RuleTabbedPane;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;

/**
 * @author kurata
 *
 */
public class RuleTree extends TreeBase {

	/**
	 * 
	 */
	public RuleTabbedPane _ruleTabbedPane = null;

	/**
	 * 
	 */
	public Map _map = new HashMap();

	/**
	 * 
	 */
	public String _kind = "";

	/**
	 * @param kind
	 * @param owner
	 * @param parent
	 */
	public RuleTree(String kind, Frame owner, Component parent) {
		super(owner, parent);
		_kind = kind;
	}

	/**
	 * @param ruleTabbedPane 
	 * @param root_text
	 * @return
	 */
	public boolean setup(RuleTabbedPane ruleTabbedPane, String root_text) {
		_ruleTabbedPane = ruleTabbedPane;

		if ( !super.setup( false))
			return false;

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode( root_text);
		defaultTreeModel.setRoot( root);

		addTreeSelectionListener( new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				on_valueChanged( e);
			}
		});

		setCellRenderer( new RuleTreeCellRenderer());

		return true;
	}

	/**
	 * @param treeSelectionEvent
	 */
	protected void on_valueChanged(TreeSelectionEvent treeSelectionEvent) {
		//JOptionPane.showMessageDialog( getParent(), "changed", "RuleTree", JOptionPane.INFORMATION_MESSAGE);
		TreePath treePath = treeSelectionEvent.getPath();
		if ( null == treePath)
			return;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();

		if ( object instanceof RulePropertyPageBase)
			_ruleTabbedPane.select( ( RulePropertyPageBase)object);
		else
			expandPath( treePath);
	}

	/**
	 * @param rulePropertyPageBase
	 */
	public void append(RulePropertyPageBase rulePropertyPageBase) {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode( rulePropertyPageBase);
		root.add( child);
		_map.put( rulePropertyPageBase, child);
	}

	/**
	 * @param node_text
	 * @param rulePropertyPageBase
	 */
	public void append(String node_text, RulePropertyPageBase rulePropertyPageBase) {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		DefaultMutableTreeNode defaultMutableTreeNode = get_child( root, node_text);
		if ( null == defaultMutableTreeNode) {
			defaultMutableTreeNode = new DefaultMutableTreeNode( node_text);
			root.add( defaultMutableTreeNode);
		}
		DefaultMutableTreeNode child = new DefaultMutableTreeNode( rulePropertyPageBase); 
		defaultMutableTreeNode.add( child);
		_map.put( rulePropertyPageBase, child);
	}

	/**
	 * @param parent
	 * @param node_text
	 * @return
	 */
	private DefaultMutableTreeNode get_child(DefaultMutableTreeNode parent, String node_text) {
		for ( int i = 0; i < parent.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)parent.getChildAt( i);
			if ( null == child)
				continue;

			Object object = child.getUserObject();
			if ( null == object)
				continue;

			if ( !( object instanceof String))
				continue;

			String text = ( String)object;
			if ( text.equals( node_text))
				return child;
		}

		return null;
	}

	/**
	 * 
	 */
	public void expand() {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		if ( 0 < root.getChildCount()) {
			TreePath treePath = new TreePath( root.getPath());
			expandPath( treePath);
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.tree.StandardTree#on_mouse_pressed(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
		TreePath treePath = getPathForLocation( mouseEvent.getX(), mouseEvent.getY());
		if ( null == treePath)
			return;

		setSelectionPath( treePath);

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		if ( 0 < defaultMutableTreeNode.getChildCount() && !isExpanded( treePath))
			expandPath( treePath);

		requestFocus();
	}

	/**
	 * @param rulePropertyPageBase
	 */
	public void select(RulePropertyPageBase rulePropertyPageBase) {
		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)_map.get( rulePropertyPageBase);
		if ( null == defaultMutableTreeNode)
			return;

		TreePath treePath = new TreePath( defaultMutableTreeNode.getPath());
		setSelectionPath( treePath);
		expandPath( treePath);
	}

	/**
	 * @return
	 */
	public RulePropertyPageBase get_selected_page() {
		TreePath treePath = getSelectionPath();
		if ( null == treePath)
			return null;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		if ( defaultMutableTreeNode.isRoot())
			return null;

		Object object = defaultMutableTreeNode.getUserObject();
		if ( null == object)
			return null;

		if ( !( object instanceof RulePropertyPageBase))
			return null;

		return ( RulePropertyPageBase)object;
	}
}
