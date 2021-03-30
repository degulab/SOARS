/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.application.visualshell.object.role.base.edit.tree.RuleTree;

/**
 * @author kurata
 *
 */
public class RuleTabbedPane extends JPanel {

	/**
	 * 
	 */
	public RuleTree _ruleTree = null;

	/**
	 * 
	 */
	public List<RulePropertyPageBase> _rulePropertyPageBases = new ArrayList<RulePropertyPageBase>();

	/**
	 * 
	 */
	public RulePropertyPageBase _defaultRulePropertyPageBase = null;

	/**
	 * 
	 */
	public String _kind = "";

	/**
	 * @param kind
	 */
	public RuleTabbedPane(String kind) {
		super();
		_kind = kind;
	}

	/**
	 * @param ruleTree
	 * @return
	 */
	public boolean setup(RuleTree ruleTree) {
		_ruleTree = ruleTree;
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
		return true;
	}

	/**
	 * @param rulePropertyPageBase
	 * @return
	 */
	protected boolean create(RulePropertyPageBase rulePropertyPageBase) {
		if ( !rulePropertyPageBase.create())
			return false;

		add( rulePropertyPageBase);
		_ruleTree.append( rulePropertyPageBase);
		_rulePropertyPageBases.add( rulePropertyPageBase);
		return true;
	}

	/**
	 * @param rulePropertyPageBase
	 * @param node_text
	 * @return
	 */
	protected boolean create(RulePropertyPageBase rulePropertyPageBase, String node_text) {
		if ( !rulePropertyPageBase.create())
			return false;

		add( rulePropertyPageBase);
		_ruleTree.append( node_text, rulePropertyPageBase);
		_rulePropertyPageBases.add( rulePropertyPageBase);
		return true;
	}

	/**
	 * @param rulePropertyPageBase
	 * @param color
	 * @return
	 */
	protected boolean create(RulePropertyPageBase rulePropertyPageBase, Color color) {
		if ( !rulePropertyPageBase.create())
			return false;

		add( rulePropertyPageBase);
		_ruleTree.append( rulePropertyPageBase);
		_rulePropertyPageBases.add( rulePropertyPageBase);
		return true;
	}

	/**
	 * 
	 */
	public void select_default() {
		_ruleTree.select( _defaultRulePropertyPageBase);
		select( _defaultRulePropertyPageBase);
	}

	/**
	 * @param rulePropertyPageBase
	 */
	public void select(RulePropertyPageBase rulePropertyPageBase) {
		for ( RulePropertyPageBase rppb:_rulePropertyPageBases)
			rppb.setVisible( rppb.equals( rulePropertyPageBase));
	}

	/**
	 * @param rulePropertyPageBase
	 */
	public void update(RulePropertyPageBase rulePropertyPageBase) {
		for ( RulePropertyPageBase rppb:_rulePropertyPageBases)
			rppb.update( rulePropertyPageBase);
	}

	/**
	 * @param type
	 * @return
	 */
	public RulePropertyPageBase get(String type) {
		for ( RulePropertyPageBase rppb:_rulePropertyPageBases) {
			if ( rppb._type.equals( type))
				return rppb;
		}
		return null;
	}

	/**
	 * @param rulePropertyPageBase
	 * @return
	 */
	public int get_index(RulePropertyPageBase rulePropertyPageBase) {
		for ( int i = 0; i < _rulePropertyPageBases.size(); ++i) {
			if ( _rulePropertyPageBases.get( i).equals( rulePropertyPageBase))
				return i;
		}
		return -1;
	}
}
