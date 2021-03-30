/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.java_method_call;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.GenericPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.VerbPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Variable;
import soars.application.visualshell.object.role.base.object.generic.element.IObject;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class JavaMethodCallPanel extends PanelRoot {

	/**
	 * 
	 */
	private Subject _object = null;

	/**
	 * 
	 */
	private VerbPanel _verbPanel = null;

	/**
	 * 
	 */
	private GenericPropertyPanel _genericPropertyPanel = null;

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * 
	 */
	private ClassVariablePanel _classVariablePanel = null;

	/**
	 * 
	 */
	private MethodPanel _methodPanel = null;

	/**
	 * @param object 
	 * @param verbPanel 
	 * @param property
	 * @param role
	 * @param buddiesMap
	 * @param rulePropertyPanelBase
	 * @param owner 
	 * @param parent 
	 */
	public JavaMethodCallPanel(Subject object, VerbPanel verbPanel, Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, GenericPropertyPanel genericPropertyPanel, Frame owner, JPanel parent) {
		super(property, role, buddiesMap, genericPropertyPanel);
		_object = object;
		_verbPanel = verbPanel;
		_genericPropertyPanel = genericPropertyPanel;
		_owner = owner;
		_parent = parent;
		if ( Environment.get_instance().is_exchange_algebra_enable()) {
			_object.set_variable( new Variable( "exchange algebra",
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.exchange.algebra"),
				false));
		}
	}

	@Override
	public boolean setup() {
		if (!super.setup())
			return false;

		setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		_classVariablePanel = new ClassVariablePanel( _object, _verbPanel, _property, _role, _buddiesMap, _genericPropertyPanel);
		_methodPanel = _genericPropertyPanel._kind.equals( "condition")
			? new MethodConditionPanel( _object, _property, _role, _buddiesMap, _genericPropertyPanel, _owner, _parent)
			: new MethodCommandPanel( _object, _property, _role, _buddiesMap, _genericPropertyPanel, _owner, _parent);

		if ( !_classVariablePanel.setup( _methodPanel))
			return false;

		if ( !_methodPanel.setup( _classVariablePanel))
			return false;

		northPanel.add( _classVariablePanel);

		SwingTool.insert_vertical_strut( northPanel, 5);

		northPanel.add( _methodPanel);

		add( northPanel, "North");

		_classVariablePanel.initialize();

		//_verbPanel.show_denial_panel( false);

		return true;
	}

	@Override
	public int get_max_width(int width) {
		return _classVariablePanel.get_max_width( _methodPanel.get_max_width( width));
	}

	@Override
	public void set_max_width(int width) {
		_classVariablePanel.set_max_width( width);
		_methodPanel.set_max_width( width);
	}

	@Override
	public boolean set(IObject object, boolean check) {
		return _classVariablePanel.set(object, check);
	}

	@Override
	public IObject get(boolean check) {
		return _classVariablePanel.get(check);
	}
}
