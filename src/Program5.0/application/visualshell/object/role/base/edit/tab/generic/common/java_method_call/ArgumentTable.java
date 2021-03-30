/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.java_method_call;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListSelectionModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import soars.application.visualshell.common.swing.TableBase;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;
import soars.application.visualshell.object.role.base.object.generic.element.JavaMethodCallRule;

/**
 * @author kurata
 *
 */
public class ArgumentTable extends TableBase {

	/**
	 * 
	 */
	private Subject _object = null;

	/**
	 * 
	 */
	private Property _property = null;

	/**
	 * 
	 */
	private Role _role = null;

	/**
	 * 
	 */
	private RulePropertyPanelBase _rulePropertyPanelBase = null;

	/**
	 * 
	 */
	private Color _color = null;

	/**
	 * @param rulePropertyPanelBase 
	 * @param role 
	 * @param property 
	 * @param object 
	 * @param color 
	 * @param owner
	 * @param parent
	 */
	public ArgumentTable(Subject object, Property property, Role role, RulePropertyPanelBase rulePropertyPanelBase, Color color, Frame owner, Component parent) {
		super(owner, parent);
		_object = object;
		_property = property;
		_role = role;
		_rulePropertyPanelBase = rulePropertyPanelBase;
		_color = color;
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if (!super.setup( true))
			return false;

		setAutoResizeMode( AUTO_RESIZE_OFF);

		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);
		setSelectionMode( DefaultListSelectionModel.SINGLE_SELECTION);

		JTableHeader tableHeader = getTableHeader();
		tableHeader.setDefaultRenderer( new ArgumentTableHeaderRenderer( _color));

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 2);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.parameter.table.header.parameter"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.parameter.table.header.value"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 2000);

		for ( int i = 0; i < 2; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new ArgumentTableCellRenderer( _role, _color));
		}

		setToolTipText( "ArgumentTable");

		return true;
	}

	/**
	 * @param methodObject
	 */
	public void get(MethodObject methodObject) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( defaultTableModel.getRowCount() != methodObject._argumentTypes.length)
			return;

		methodObject._argumentVariableRules.clear();

		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			EntityVariableRule entityVariableRule = ( EntityVariableRule)defaultTableModel.getValueAt( i, 1);
			methodObject._argumentVariableRules.add( ( null == entityVariableRule) ? new EntityVariableRule() : new EntityVariableRule( entityVariableRule));
		}
	}

	@Override
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		int row = getSelectedRow();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount() || -1 == row)
			return;

		// TODO ( row, 1)のEntityVariableRuleを書き換える
		EditArgumentVariableDlg editArgumentVariableDlg = new EditArgumentVariableDlg(
			_owner,
			ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.parameter.dialog.title"),
			true,
			( String)defaultTableModel.getValueAt( row, 0),
			( EntityVariableRule)defaultTableModel.getValueAt( row, 1),
			_object,
			_property,
			_role,
			_rulePropertyPanelBase);

		if ( !editArgumentVariableDlg.do_modal( _parent))
			return;

		defaultTableModel.setValueAt( editArgumentVariableDlg._entityVariableRule, row, 1);

		repaint();
	}

	/**
	 * @param javaMethodCallRule
	 */
	public void get(JavaMethodCallRule javaMethodCallRule) {
		javaMethodCallRule._argumentVariableRules.clear();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i)
			javaMethodCallRule._argumentVariableRules.add( new EntityVariableRule( ( EntityVariableRule)defaultTableModel.getValueAt( i, 1)));
	}

	/**
	 * @param javaMethodCallRule
	 * @return
	 */
	public boolean set(JavaMethodCallRule javaMethodCallRule) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( defaultTableModel.getRowCount() != javaMethodCallRule._argumentVariableRules.size())
			return false;

		for ( int i = 0; i < javaMethodCallRule._argumentVariableRules.size(); ++i)
			defaultTableModel.setValueAt( javaMethodCallRule._argumentVariableRules.get( i), i, 1);

		return true;
	}

	/**
	 * @param javaMethodCallRule
	 * @param check 
	 * @return
	 */
	public boolean get(JavaMethodCallRule javaMethodCallRule, boolean check) {
		javaMethodCallRule._argumentVariableRules.clear();

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			EntityVariableRule entityVariableRule = ( EntityVariableRule)defaultTableModel.getValueAt( i, 1);
			if ( null == entityVariableRule)
				return false;

			if ( entityVariableRule._variableValue.equals( ""))
				return false;

			javaMethodCallRule._argumentVariableRules.add( new EntityVariableRule( entityVariableRule));
		}
		return true;
	}
}
