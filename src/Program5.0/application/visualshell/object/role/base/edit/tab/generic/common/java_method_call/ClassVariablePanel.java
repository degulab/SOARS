/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.java_method_call;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.GenericPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.VerbPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.common.ObjectPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Variable;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;
import soars.application.visualshell.object.role.base.object.generic.element.IObject;
import soars.application.visualshell.object.role.base.object.generic.element.JavaMethodCallRule;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class ClassVariablePanel extends ObjectPanel {

	/**
	 * 
	 */
	private Map<String, String> _classVariableMap = new HashMap<String, String>();

	/**
	 * 
	 */
	//private boolean _ignore = false;

	/**
	 * 
	 */
	private boolean _store = true;

	/**
	 * 
	 */
	private MethodPanel _methodPanel = null;

	/**
	 * 
	 */
	private TextField _classnameTextField = null;

	/**
	 * 
	 */
	private TextField _jarFilenameTextField = null;

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * @param object
	 * @param verbPanel
	 * @param property
	 * @param role
	 * @param buddiesMap
	 * @param genericPropertyPanel
	 */
	public ClassVariablePanel(Subject object, VerbPanel verbPanel, Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, GenericPropertyPanel genericPropertyPanel) {
		super(object, verbPanel, property, role, buddiesMap, genericPropertyPanel);
	}

	/**
	 * @param methodPanel
	 * @return
	 */
	public boolean setup(MethodPanel methodPanel) {
		if ( !super.setup())
			return false;

		_methodPanel = methodPanel;

		_variablePanelMap.get( "exchange algebra").setVisible( false);

		return true;
	}

	@Override
	protected void on_setup_variables_panel(List<Variable> variables, JPanel parent) {
		append_classnameTextField( parent);

		SwingTool.insert_vertical_strut( parent, 5);

		append_jarFilenameTextField( parent);
	}

	/**
	 * @param parent
	 */
	private void append_classnameTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		JLabel label = _rulePropertyPanelBase.create_label( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.label.class.name"), true);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_classnameTextField = _rulePropertyPanelBase.create_textField( _standardControlWidth, false);
		_classnameTextField.setEditable( false);
		panel.add( _classnameTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void append_jarFilenameTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		JLabel label = _rulePropertyPanelBase.create_label( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.label.jar.filename"), true);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_jarFilenameTextField = _rulePropertyPanelBase.create_textField( _standardControlWidth, false);
		_jarFilenameTextField.setEditable( false);
		panel.add( _jarFilenameTextField);

		parent.add( panel);
	}

	@Override
	public int get_max_width(int width) {
		return super.get_max_width(width);
	}

	@Override
	public void set_max_width(int width) {
		super.set_max_width(width);

		int w = 0;
		for ( JLabel label:_labels)
			w = Math.max( w, label.getPreferredSize().width);
		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( w, label.getPreferredSize().height));
	}

	/**
	 * @param classVariableRule
	 * @return
	 */
	private String get_key1(EntityVariableRule classVariableRule) {
		return ( classVariableRule._entity + "." + classVariableRule._entityName + "." + classVariableRule._agentVariable + "." + classVariableRule._spotVariable + "." + classVariableRule._variableType);
	}

	/**
	 * @param classVariableRule
	 * @return
	 */
	private String get_key2(EntityVariableRule classVariableRule) {
		return get_key2( classVariableRule, classVariableRule._variableValue);
	}

	/**
	 * @param classVariableRule
	 * @param classVariable
	 * @return
	 */
	private String get_key2(EntityVariableRule classVariableRule, String classVariable) {
		return ( get_key1( classVariableRule) + "." + classVariable);
	}

	/**
	 * @param check
	 * @return
	 */
	public String get_key2(boolean check) {
		EntityVariableRule classVariableRule = ( EntityVariableRule)super.get( true);
		if ( null == classVariableRule)
			return null;

		return get_key2( classVariableRule);
	}

	@Override
	protected void on_self_deselected(RadioButton radioButton, ItemEvent arg0) {
		super.on_self_deselected(radioButton, arg0);
		on_radioButton_deselected( radioButton);
//		System.out.println( "on_self_deselected : " + get_key1( classVariableRule));
//		System.out.println( "on_self_deselected" + ( ( null == classVariableRule) ? "" : ( " : " + classVariableRule.get_cell_text( _role, false))));
	}

	@Override
	protected void on_self_selected(ItemEvent arg0) {
		super.on_self_selected(arg0);
		on_radioButton_selected();
//		System.out.println( "on_self_selected : " + get_key1( classVariableRule));
//		System.out.println( "on_self_selected" + ( ( null == classVariableRule) ? "" : ( " : " + classVariableRule.get_cell_text( _role, false))));
	}

	@Override
	protected void on_agentSelector_deselected(RadioButton radioButton, ItemEvent arg0) {
		super.on_agentSelector_deselected(radioButton, arg0);
		on_radioButton_deselected( radioButton);
//		System.out.println( "on_agentSelector_deselected : " + get_key1( classVariableRule));
//		System.out.println( "on_agentSelector_deselected" + ( ( null == classVariableRule) ? "" : ( " : " + classVariableRule.get_cell_text( _role, false))));
	}

	@Override
	protected void on_agentSelector_selected(ItemEvent arg0) {
		super.on_agentSelector_selected(arg0);
		on_radioButton_selected();
//		System.out.println( "on_agentSelector_selected : " + get_key1( classVariableRule));
//		System.out.println( "on_agentSelector_selected" + ( ( null == classVariableRule) ? "" : ( " : " + classVariableRule.get_cell_text( _role, false))));
	}

	@Override
	protected void on_spotSelector_deselected(RadioButton radioButton, ItemEvent arg0) {
		super.on_spotSelector_deselected(radioButton, arg0);
		on_radioButton_deselected( radioButton);
//		System.out.println( "on_spotSelector_deselected : " + get_key1( classVariableRule));
//		System.out.println( "on_spotSelector_deselected" + ( ( null == classVariableRule) ? "" : ( " : " + classVariableRule.get_cell_text( _role, false))));
	}

	@Override
	protected void on_spotSelector_selected(ItemEvent arg0) {
		super.on_spotSelector_selected(arg0);
		on_radioButton_selected();
//		System.out.println( "on_spotSelector_selected : " + get_key1( classVariableRule));
//		System.out.println( "on_spotSelector_selected" + ( ( null == classVariableRule) ? "" : ( " : " + classVariableRule.get_cell_text( _role, false))));
	}

	@Override
	protected void on_currentSpot_deselected(RadioButton radioButton, ItemEvent arg0) {
		super.on_currentSpot_deselected(radioButton, arg0);
		on_radioButton_deselected( radioButton);
//		System.out.println( "on_currentSpot_deselected : " + get_key1( classVariableRule));
//		System.out.println( "on_currentSpot_deselected" + ( ( null == classVariableRule) ? "" : ( " : " + classVariableRule.get_cell_text( _role, false))));
	}

	@Override
	protected void on_currentSpot_selected(ItemEvent arg0) {
		super.on_currentSpot_selected(arg0);
		on_radioButton_selected();
//		System.out.println( "on_currentSpot_selected : " + get_key1( classVariableRule));
//		System.out.println( "on_currentSpot_selected" + ( ( null == classVariableRule) ? "" : ( " : " + classVariableRule.get_cell_text( _role, false))));
	}

	/**
	 * @param radioButton
	 */
	private void on_radioButton_deselected(RadioButton radioButton) {
		EntityVariableRule classVariableRule = ( EntityVariableRule)get( radioButton);
		if ( null == classVariableRule)
			return;

		_classVariableMap.put( get_key1( classVariableRule), classVariableRule._variableValue);

		if ( _store) {
			_methodPanel.store( get_key2( classVariableRule, classVariableRule._variableValue), get_classVariableObject( classVariableRule._variableValue, radioButton));
			_store = false;
		}
	}

	/**
	 * @param radioButton
	 * @return
	 */
	private EntityVariableRule get(RadioButton radioButton) {
		EntityVariableRule entityVariableRule = new EntityVariableRule();
		entityVariableRule._entity = _radioButtonEntityMap.get( radioButton);
		return get( entityVariableRule, true) ? entityVariableRule : null;
	}

	/**
	 * 
	 */
	private void on_radioButton_selected() {
		EntityVariableRule classVariableRule = ( EntityVariableRule)super.get( true);
		if ( null == classVariableRule)
			return;

		set_classVariable( classVariableRule);

		_store = true;
	}

	/**
	 * @param classVariableRule
	 */
	private void set_classVariable(EntityVariableRule classVariableRule) {
		String classVariable = _classVariableMap.get( get_key1( classVariableRule));
		if ( null != classVariable) {
			ComboBox variableComboBox = _variableComboBoxMap.get( classVariableRule._variableType);
			if ( null != variableComboBox) {
				for ( int i = 0; i < variableComboBox.getItemCount(); ++i) {
					if ( classVariable.equals( variableComboBox.getItemAt( i))) {
						variableComboBox.setSelectedItem( classVariable);
						break;
					}
				}
			}
		} else {
			ComboBox variableComboBox = _variableComboBoxMap.get( classVariableRule._variableType);
			if ( null != variableComboBox && 0 < variableComboBox.getItemCount())
				variableComboBox.setSelectedIndex( 0);
			//System.out.println( "ClassVariablePanel : set_classVariable : No data!");
		}
	}

	@Override
	protected void before_agentSelector_changed() {
		super.before_agentSelector_changed();
		before_entitySelector_changed();
//		System.out.println( "before_agentSelector_changed : " + get_key1( classVariableRule));
//		System.out.println( "before_agentSelector_changed : " + classVariableRule.get_cell_text( _role, false));
	}

	@Override
	protected void after_agentSelector_changed() {
		super.after_agentSelector_changed();
		after_entitySelector_changed();
//		System.out.println( "after_agentSelector_changed : " + get_key1( classVariableRule));
//		System.out.println( "after_agentSelector_changed : " + classVariableRule.get_cell_text( _role, false));
	}

	@Override
	protected void before_spotSelector_changed() {
		super.before_spotSelector_changed();
		before_entitySelector_changed();
//		System.out.println( "before_spotSelector_changed : " + get_key1( classVariableRule));
//		System.out.println( "before_spotSelector_changed : " + classVariableRule.get_cell_text( _role, false));
	}

	@Override
	protected void after_spotSelector_changed() {
		super.after_spotSelector_changed();
		after_entitySelector_changed();
//		System.out.println( "after_spotSelector_changed : " + get_key1( classVariableRule));
//		System.out.println( "after_spotSelector_changed : " + classVariableRule.get_cell_text( _role, false));
	}

	/**
	 * 
	 */
	private void before_entitySelector_changed() {
//		if ( _ignore)
//			return;

		EntityVariableRule classVariableRule = ( EntityVariableRule)super.get( true);
		if ( null == classVariableRule)
			return;

//		_ignore = true;

		_classVariableMap.put( get_key1( classVariableRule), classVariableRule._variableValue);

		if ( _store) {
			_methodPanel.store( get_key2( classVariableRule, classVariableRule._variableValue), get_classVariableObject( classVariableRule._variableValue));
			_store = false;
		}
	}

	/**
	 * 
	 */
	private void after_entitySelector_changed() {
//		if ( !_ignore)
//			return;

		EntityVariableRule classVariableRule = ( EntityVariableRule)super.get( true);
		if ( null == classVariableRule)
			return;

		set_classVariable( classVariableRule);

//		_ignore = false;
		_store = true;
	}

	@Override
	protected void before_agentVariableCheckBox_state_changed(ItemEvent arg0) {
		super.before_agentVariableCheckBox_state_changed(arg0);
		before_entityVariableCheckBox_state_changed( "agentVariable", arg0);
//		System.out.println( "on_before_agentVariableCheckBox_state_changed : " + get_key1( classVariableRule));
//		System.out.println( "on_before_agentVariableCheckBox_state_changed : " + classVariableRule.get_cell_text( _role, false));
	}

	@Override
	protected void after_agentVariableCheckBox_state_changed(ItemEvent arg0) {
		super.after_agentVariableCheckBox_state_changed(arg0);
		after_entityVariableCheckBox_state_changed();

//	System.out.println( "on_after_spotVariableCheckBox_state_changed : " + get_key1( classVariableRule) + " - " + _classVariableMap.get( get_key1( classVariableRule)));
//	System.out.println( "on_after_agentVariableCheckBox_state_changed : " + classVariableRule.get_cell_text( _role, false));
	}

	@Override
	protected void before_spotVariableCheckBox_state_changed(ItemEvent arg0) {
		super.before_spotVariableCheckBox_state_changed(arg0);
		before_entityVariableCheckBox_state_changed( "spotVariable", arg0);
//		System.out.println( "on_before_spotVariableCheckBox_state_changed : " + get_key1( classVariableRule) + " - " + _classVariableMap.get( get_key1( classVariableRule)));
//		System.out.println( "on_before_spotVariableCheckBox_state_changed : " + classVariableRule.get_cell_text( _role, false));
	}

	@Override
	protected void after_spotVariableCheckBox_state_changed(ItemEvent arg0) {
		super.after_spotVariableCheckBox_state_changed(arg0);
		after_entityVariableCheckBox_state_changed();
//		System.out.println( "on_after_spotVariableCheckBox_state_changed : " + get_key1( classVariableRule) + " - " + _classVariableMap.get( get_key1( classVariableRule)));
//		System.out.println( "on_after_spotVariableCheckBox_state_changed : " + classVariableRule.get_cell_text( _role, false));
	}

	/**
	 * @param entityVariableType
	 * @param arg0
	 */
	private void before_entityVariableCheckBox_state_changed(String entityVariableType, ItemEvent arg0) {
//		if ( _ignore)
//			return;

		EntityVariableRule classVariableRule = ( EntityVariableRule)get( entityVariableType, arg0);
		if ( null == classVariableRule)
			return;

//		_ignore = true;

		_classVariableMap.put( get_key1( classVariableRule), classVariableRule._variableValue);

		String entityType = classVariableRule._entity;
		String entityName = classVariableRule._entityName;
		if ( !classVariableRule._agentVariable.equals( "")) {
			entityType = "agent";
			entityName = "";
		} else if ( !classVariableRule._spotVariable.equals( "")) {
			entityType = "spot";
			entityName = "";
		}

		if ( _store) {
			ComboBox comboBox = _variableComboBoxMap.get( classVariableRule._variableType);
			_methodPanel.store( get_key2( classVariableRule, classVariableRule._variableValue),
				classVariableRule._variableType.equals( "exchange algebra")
					? ( ( 0 < comboBox.getItemCount()) ? new ClassVariableObject( classVariableRule._variableValue, Constant._exchangeAlgebraJarFilename, Constant._exchangeAlgebraClassname) : null)
					: LayerManager.get_instance().get_class_variable( entityType, entityName, classVariableRule._variableValue));
			_store = false;
		}
	}

	/**
	 * 
	 */
	private void after_entityVariableCheckBox_state_changed() {
//		if ( !_ignore)
//			return;

		EntityVariableRule classVariableRule = ( EntityVariableRule)super.get( true);
		if ( null == classVariableRule)
			return;

		set_classVariable( classVariableRule);

//		_ignore = false;
		_store = true;
	}

	/**
	 * @param entityVariableType 
	 * @param arg0
	 * @return
	 */
	private EntityVariableRule get(String entityVariableType, ItemEvent arg0) {
		EntityVariableRule classVariableRule = ( EntityVariableRule)super.get( true);
		if ( null == classVariableRule)
			return null;

		if ( entityVariableType.equals( "agentVariable")) {
			if ( ItemEvent.SELECTED == arg0.getStateChange())
				classVariableRule._agentVariable = "";
			else if ( ItemEvent.DESELECTED == arg0.getStateChange()) {
				String agentVariable = ( String)_agentVariableComboBox.getSelectedItem();
				classVariableRule._agentVariable = ( null == agentVariable) ? "" : agentVariable;
			}
		} else if ( entityVariableType.equals( "spotVariable")) {
			if ( ItemEvent.SELECTED == arg0.getStateChange())
				classVariableRule._spotVariable = "";
			else if ( ItemEvent.DESELECTED == arg0.getStateChange()) {
				String spotVariable = ( String)_spotVariableComboBox.getSelectedItem();
				classVariableRule._spotVariable = ( null == spotVariable) ? "" : spotVariable;
			}
		} else
			return null;

		return classVariableRule;
	}

	@Override
	protected void on_agentVariableComboBox_deselected(ItemEvent arg0) {
		super.on_agentVariableComboBox_deselected(arg0);
		on_entityVariableComboBox_deselected();
//		System.out.println( "on_agentVariableComboBox_deselected : " + get_key1( classVariableRule));
//		System.out.println( "on_agentVariableComboBox_deselected : " + classVariableRule.get_cell_text( _role, false));
	}

	@Override
	protected void on_agentVariableComboBox_selected(ActionEvent arg0) {
		super.on_agentVariableComboBox_selected(arg0);
		on_entityVariableComboBox_selected();
//		System.out.println( "on_agentVariableComboBox_selected : " + get_key1( classVariableRule));
//		System.out.println( "on_agentVariableComboBox_selected : " + classVariableRule.get_cell_text( _role, false));
	}

	@Override
	protected void on_spotVariableComboBox_deselected(ItemEvent arg0) {
		super.on_spotVariableComboBox_deselected(arg0);
		on_entityVariableComboBox_deselected();
//		System.out.println( "on_spotVariableComboBox_deselected : " + get_key1( classVariableRule));
//		System.out.println( "on_spotVariableComboBox_deselected : " + classVariableRule.get_cell_text( _role, false));
	}

	@Override
	protected void on_spotVariableComboBox_selected(ActionEvent arg0) {
		super.on_spotVariableComboBox_selected(arg0);
		on_entityVariableComboBox_selected();
//		System.out.println( "on_spotVariableComboBox_selected : " + get_key1( classVariableRule));
//		System.out.println( "on_spotVariableComboBox_selected : " + classVariableRule.get_cell_text( _role, false));
	}

	/**
	 * 
	 */
	private void on_entityVariableComboBox_deselected() {
//		if ( _ignore)
//			return;

		EntityVariableRule classVariableRule = ( EntityVariableRule)super.get( true);
		if ( null == classVariableRule)
			return;

		_classVariableMap.put( get_key1( classVariableRule), classVariableRule._variableValue);

		String entityType = classVariableRule._entity;
		String entityName = classVariableRule._entityName;
		if ( !classVariableRule._agentVariable.equals( "")) {
			entityType = "agent";
			entityName = "";
		} else if ( !classVariableRule._spotVariable.equals( "")) {
			entityType = "spot";
			entityName = "";
		}

		if ( _store) {
			ComboBox comboBox = _variableComboBoxMap.get( classVariableRule._variableType);
			_methodPanel.store( get_key2( classVariableRule, classVariableRule._variableValue),
				classVariableRule._variableType.equals( "exchange algebra")
					? ( ( 0 < comboBox.getItemCount()) ? new ClassVariableObject( classVariableRule._variableValue, Constant._exchangeAlgebraJarFilename, Constant._exchangeAlgebraClassname) : null)
					: LayerManager.get_instance().get_class_variable( entityType, entityName, classVariableRule._variableValue));
			_store = false;
		}
	}

	/**
	 * 
	 */
	private void on_entityVariableComboBox_selected() {
//		if ( _ignore)
//			return;

		EntityVariableRule classVariableRule = ( EntityVariableRule)super.get( true);
		if ( null == classVariableRule)
			return;

		set_classVariable( classVariableRule);

		_store = true;
	}

	@Override
	protected void on_variableTypeComboBox_deselected(ItemEvent arg0) {
		super.on_variableTypeComboBox_deselected(arg0);

//		if ( _ignore)
//			return;

		EntityVariableRule classVariableRule = ( EntityVariableRule)get( ( String)arg0.getItem());
		if ( null == classVariableRule)
			return;

		_classVariableMap.put( get_key1( classVariableRule), classVariableRule._variableValue);
		if ( _store) {
			_methodPanel.store( get_key2( classVariableRule, classVariableRule._variableValue), get_classVariableObject( get_variable_by_type( classVariableRule._variableType), classVariableRule._variableValue));
			_store = false;
		}
//		System.out.println( "on_variableTypeComboBox_deselected : " + get_key1( classVariableRule));
//		System.out.println( "on_variableTypeComboBox_deselected : " + classVariableRule.get_cell_text( _role, false));
	}

	@Override
	protected void on_variableTypeComboBox_selected(ActionEvent arg0) {
		super.on_variableTypeComboBox_selected(arg0);

//		if ( _ignore)
//			return;

		EntityVariableRule classVariableRule = ( EntityVariableRule)super.get( true);
		if ( null == classVariableRule)
			return;

		set_classVariable( classVariableRule);

		_store = true;
//		System.out.println( "on_variableTypeComboBox_selected : " + get_key1( classVariableRule));
//		System.out.println( "on_variableTypeComboBox_selected : " + classVariableRule.get_cell_text( _role, false));
	}

	/**
	 * @param variableTypeName
	 * @return
	 */
	private EntityVariableRule get(String variableTypeName) {
		EntityVariableRule classVariableRule = ( EntityVariableRule)super.get( true);
		if ( null == classVariableRule)
			return null;

		Variable variable = get_variable_by_typename( variableTypeName);
		if ( null == variable)
			return null;

		classVariableRule._variableType = variable._type;

		ComboBox comboBox = _variableComboBoxMap.get( variable._type);
		if ( null == comboBox)
			return null;

		String variableValue = ( String)comboBox.getSelectedItem();
		if ( null == variableValue)
			return null;

		classVariableRule._variableValue = variableValue;

		return classVariableRule;
	}

	@Override
	protected void on_variableComboBox_deselected(ComboBox comboBox, ItemEvent arg0) {
		super.on_variableComboBox_deselected(comboBox, arg0);

		EntityVariableRule classVariableRule = ( EntityVariableRule)super.get( true);
		if ( null == classVariableRule)
			return;

		if ( _store) {
			_methodPanel.store( get_key2( classVariableRule, ( String)arg0.getItem()), get_classVariableObject( ( String)arg0.getItem()));
			_store = false;
		}
	}

	@Override
	protected void on_variableComboBox_selected(ComboBox comboBox, ActionEvent arg0) {
		super.on_variableComboBox_selected(comboBox, arg0);

		// TODO 選択されたのが交換代数の場合の処理を追加すること！
		_classnameTextField.setText( "");
		_classnameTextField.setToolTipText( null);
		_jarFilenameTextField.setText( "");
		_jarFilenameTextField.setToolTipText( null);

		if ( null == comboBox.getSelectedItem() || 0 == comboBox.getItemCount()) {
			_methodPanel.clear();
			_store = true;
			return;
		}

		ClassVariableObject classVariableObject = get_classVariableObject( ( String)comboBox.getSelectedItem());
		if ( null == classVariableObject) {
			_methodPanel.clear();
			_store = true;
			return;
		}
	
		if ( null == classVariableObject._classname || classVariableObject._classname.equals( ""))
			_classnameTextField.setText( "");
		else {
			String[] words = classVariableObject._classname.split( "\\.");
			_classnameTextField.setText( ( null != words && 0 < words.length) ? words[ words.length - 1] : "");
		}

		_classnameTextField.setToolTipText( classVariableObject._classname);

		if ( null == classVariableObject._jarFilename || classVariableObject._jarFilename.equals( ""))
			_jarFilenameTextField.setText( "");
		else {
			String[] words = classVariableObject._jarFilename.split( "/");
			_jarFilenameTextField.setText( ( null != words && 0 < words.length) ? words[ words.length - 1] : "");
		}

		_jarFilenameTextField.setToolTipText( classVariableObject._jarFilename);

		EntityVariableRule classVariableRule = ( EntityVariableRule)super.get( true);
		if ( null == classVariableRule)
			return;

		_methodPanel.on_variable_selected( get_key2( classVariableRule), classVariableObject);

		_store = true;
	}

	/**
	 * @return
	 */
	public ClassVariableObject get_selected_classVariableObject() {
		Variable variable = get_selected_variable();
		ComboBox comboBox = _variableComboBoxMap.get( variable._type);
		if ( null == comboBox)
			return null;

		return get_classVariableObject( ( String)comboBox.getSelectedItem());
	}

	/**
	 * @param classVariableName
	 * @return
	 */
	private ClassVariableObject get_classVariableObject(String classVariableName) {
		return get_classVariableObject( classVariableName, get_selected_radioButton());
	}

	/**
	 * @param classVariableName
	 * @param radioButton
	 * @return
	 */
	private ClassVariableObject get_classVariableObject(Variable variable, String classVariableName) {
		return get_classVariableObject( variable, classVariableName, get_selected_radioButton());
	}

	/**
	 * @param variable
	 * @param classVariableName
	 * @return
	 */
	private ClassVariableObject get_classVariableObject(String classVariableName, RadioButton radioButton) {
		return get_classVariableObject( get_selected_variable(), classVariableName, radioButton);
	}

	/**
	 * @param variable
	 * @param classVariableName
	 * @param radioButton
	 * @return
	 */
	private ClassVariableObject get_classVariableObject(Variable variable, String classVariableName, RadioButton radioButton) {
		if ( null == variable || null == classVariableName || null == radioButton)
			return null;

		if ( variable._type.equals( "exchange algebra")) {
			ComboBox comboBox = _variableComboBoxMap.get( variable._type);
			return ( 0 < comboBox.getItemCount())
				? new ClassVariableObject( ( String)comboBox.getSelectedItem(), Constant._exchangeAlgebraJarFilename, Constant._exchangeAlgebraClassname)
				: null;
		}

		String selectedRadioButton = _radioButtonEntityMap.get( radioButton);
		if ( null == selectedRadioButton)
			return null;

		String entityType = "";
		String entityName = "";
		if ( selectedRadioButton.equals( "self") || selectedRadioButton.equals( "agent")) {
			entityType = "agent";
			if ( selectedRadioButton.equals( "agent")) {
				entityName = _agentSelector.get_exactly();
				if ( null == entityName || entityName.equals( ""))
					return null;
			}

			if ( null != _agentVariableCheckBox && _agentVariableCheckBox.isSelected()) {
				String agentVariable = ( String)_agentVariableComboBox.getSelectedItem();
				if ( null == agentVariable || agentVariable.equals( ""))
					return null;

				entityName = "";
			}

			if ( null != _spotVariableCheckBox && _spotVariableCheckBox.isSelected()) {
				String spotVariable = ( String)_spotVariableComboBox.getSelectedItem();
				if ( null == spotVariable || spotVariable.equals( ""))
					return null;

				entityType = "spot";
				entityName = "";
			}
		} else if ( selectedRadioButton.equals( "spot") || selectedRadioButton.equals( "currentspot")) {
			entityType = "spot";
			if ( selectedRadioButton.equals( "spot")) {
				entityName = _spotSelector.get_exactly();
				if ( null == entityName || entityName.equals( ""))
					return null;
			}

			if ( null != _agentVariableCheckBox && _agentVariableCheckBox.isSelected()) {
				String agentVariable = ( String)_agentVariableComboBox.getSelectedItem();
				if ( null == agentVariable || agentVariable.equals( ""))
					return null;

				entityType = "agent";
				entityName = "";
			}

			if ( null != _spotVariableCheckBox && _spotVariableCheckBox.isSelected()) {
				String spotVariable = ( String)_spotVariableComboBox.getSelectedItem();
				if ( null == spotVariable || spotVariable.equals( ""))
					return null;

				entityName = "";
			}
		} else
			return null;

		// ClassVariableObjectを取得してjarファイル名とクラス名を取得する
		return LayerManager.get_instance().get_class_variable( entityType, entityName, classVariableName);
	}

	/**
	 * @param check
	 * @return
	 */
	public EntityVariableRule get_classVariableRule(boolean check) {
		return ( EntityVariableRule)super.get( check);
	}

	@Override
	public boolean set(IObject object, boolean check) {
		if ( !( object instanceof JavaMethodCallRule))
			return false;

		JavaMethodCallRule javaMethodCallRule = ( JavaMethodCallRule)object;
		if (!super.set(javaMethodCallRule._classVariableRule, check))
			return false;

		EntityVariableRule classVariableRule = ( EntityVariableRule)super.get( true);
		if ( null == classVariableRule)
			return false;

		if ( !_methodPanel.set( object, check, get_key2( classVariableRule)))
			return false;

		return true;
	}

	@Override
	public IObject get(boolean check) {
		JavaMethodCallRule javaMethodCallRule = new JavaMethodCallRule( _rulePropertyPanelBase._kind);

		javaMethodCallRule._classVariableRule = ( EntityVariableRule)super.get(check);
		if ( null == javaMethodCallRule._classVariableRule)
			return null;

		if ( !_methodPanel.get( javaMethodCallRule, check))
			return null;

		return javaMethodCallRule;
	}
}
