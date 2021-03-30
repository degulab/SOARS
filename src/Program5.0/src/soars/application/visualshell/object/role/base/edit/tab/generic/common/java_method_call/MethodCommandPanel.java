/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.java_method_call;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;
import soars.common.utility.swing.text.TextField;

/**
 * @author kurata
 *
 */
public class MethodCommandPanel extends MethodPanel {

	/**
	 * 
	 */
	private EntityVariableRule _returnVariableRule = new EntityVariableRule();

	/**
	 * 
	 */
	private TextField _returnVariableTextField = null;

	/**
	 * 
	 */
	private JButton _returnVariableButton = null;

	/**
	 * @param object
	 * @param property
	 * @param role
	 * @param buddiesMap
	 * @param rulePropertyPanelBase
	 * @param owner
	 * @param parent
	 */
	public MethodCommandPanel(Subject object, Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, RulePropertyPanelBase rulePropertyPanelBase, Frame owner, Component parent) {
		super(object, property, role, buddiesMap, rulePropertyPanelBase, owner, parent);
	}

	@Override
	protected void set_returnVariableRule(EntityVariableRule returnVariableRule) {
		if ( null == returnVariableRule) {
			_returnVariableTextField.setText( "");
			return;
		}

		_returnVariableRule = new EntityVariableRule( returnVariableRule);
		_returnVariableTextField.setText( _returnVariableRule.get_cell_text( _role, false));
	}

	@Override
	protected EntityVariableRule get_returnVariableRule() {
		return new EntityVariableRule( _returnVariableRule);
	}

	@Override
	protected void set_enable_returnVariableButton(boolean enable) {
		_returnVariableButton.setEnabled( enable);
	}

	@Override
	protected void setup_returnVariableTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		JLabel label = _rulePropertyPanelBase.create_label( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.label.return.value"), true);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_returnVariableTextField = _rulePropertyPanelBase.create_textField( _standardControlWidth, false);
		_returnVariableTextField.setEditable( false);
		panel.add( _returnVariableTextField);

		panel.add( Box.createHorizontalStrut( 5));

		_returnVariableButton = _rulePropertyPanelBase.create_button( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.button.return.value"));
		_returnVariableButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_edit_return_variable( arg0);
			}
		});
		_returnVariableButton.setEnabled( false);
		panel.add( _returnVariableButton);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	protected void on_edit_return_variable(ActionEvent arg0) {
		String methodName = ( String)_methodComboBox.getSelectedItem();
		if ( null == methodName)
			return;

		ClassVariableObject classVariableObject = _classVariablePanel.get_selected_classVariableObject();

		TreeMap<String, MethodObject> methodObjectMap = _methodObjectMapMap.get( classVariableObject._classname);
		if ( null == methodObjectMap)
			return;

		MethodObject methodObject = methodObjectMap.get( methodName);
		if ( null == methodObject || null == methodObject._returnType || methodObject._returnType.equals( ""))
			return;

		// TODO methodObject._returnVariableRuleを書き換える
		EditReturnVariableDlg editReturnVariableDlg = new EditReturnVariableDlg(
			_owner,
			ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.return.value.dialog.title"),
			true,
			methodObject._returnType,
			methodObject._returnVariableRule,
			_object,
			_property,
			_role,
			_rulePropertyPanelBase);

		if ( !editReturnVariableDlg.do_modal( _parent))
			return;

		methodObject._returnVariableRule = editReturnVariableDlg._entityVariableRule;
		set_returnVariableRule( methodObject._returnVariableRule);

		repaint();
	}
}
