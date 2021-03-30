/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.java_method_call;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.arbitrary.JarFileProperties;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;
import soars.application.visualshell.object.role.base.object.generic.element.IObject;
import soars.application.visualshell.object.role.base.object.generic.element.JavaMethodCallRule;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.xml.dom.XmlTool;

/**
 * @author kurata
 *
 */
public class MethodPanel extends PanelRoot {

	/**
	 * 
	 */
	protected Subject _object = null;

	/**
	 * 
	 */
	private Map<String, String> _methodMap = new HashMap<String, String>();

	/**
	 * 
	 */
	private Map<String, String> _methodHistoryMap = new HashMap<String, String>();

	/**
	 * 
	 */
	protected Map<String, TreeMap<String, MethodObject>> _methodObjectMapMap = new HashMap<String, TreeMap<String, MethodObject>>();

	/**
	 * 
	 */
	private boolean _ignore = false;

	/**
	 * 
	 */
	protected ClassVariablePanel _classVariablePanel = null;

	/**
	 * 
	 */
	protected ComboBox _methodComboBox = null;

	/**
	 * 
	 */
	protected TextField _returnTypeTextField = null;

	/**
	 * 
	 */
	protected String _returnType = "";

	/**
	 * 
	 */
	protected ArgumentTable _argumentTable = null;

	/**
	 * 
	 */
	protected JScrollPane _argumentTableScrollPane = null;

	/**
	 * 
	 */
	protected List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * @param object 
	 * @param property
	 * @param role
	 * @param buddiesMap
	 * @param rulePropertyPanelBase
	 * @param owner 
	 * @param parent 
	 */
	public MethodPanel(Subject object, Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, RulePropertyPanelBase rulePropertyPanelBase, Frame owner, Component parent) {
		super(property, role, buddiesMap, rulePropertyPanelBase);
		_object = object;
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param classVariablePanel
	 * @return
	 */
	public boolean setup(ClassVariablePanel classVariablePanel) {
		if ( !super.setup())
			return false;

		_classVariablePanel = classVariablePanel;

		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		add( Box.createHorizontalStrut( 2));

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		SwingTool.insert_vertical_strut( northPanel, 5);

		setup_methodComboBox( northPanel);

		SwingTool.insert_vertical_strut( northPanel, 5);

		setup_returnTypeTextField( northPanel);

		SwingTool.insert_vertical_strut( northPanel, 5);

		setup_returnVariableTextField( northPanel);

		SwingTool.insert_vertical_strut( northPanel, 5);

		if ( !setup_argumentTable( northPanel))
			return false;

		SwingTool.insert_vertical_strut( northPanel, 5);

		basePanel.add( northPanel, "North");

		basePanel.setBorder( BorderFactory.createLineBorder( _rulePropertyPanelBase._color, 1));

		add( basePanel);

		add( Box.createHorizontalStrut( 2));

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_methodComboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		JLabel label = _rulePropertyPanelBase.create_label( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.label.method"), true);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_methodComboBox = _rulePropertyPanelBase.create_comboBox( null, _standardControlWidth, false);
		_methodComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_method_selected( arg0);
			}
		});
		_methodComboBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if ( ItemEvent.DESELECTED != arg0.getStateChange())
					return;

				on_method_deselected( arg0);
				//on_method_deselected( ( String)arg0.getItem());
			}
		});
		panel.add( _methodComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param arg0
	 */
	protected void on_method_deselected(ItemEvent arg0) {
		if ( _ignore)
			return;

		on_method_deselected( _classVariablePanel.get_key2( true), ( String)arg0.getItem());
		//on_method_deselected( ( String)arg0.getItem());
	}

//	/**
//	 * @param method
//	 */
//	private void on_method_deselected(String method) {
////		on_method_deselected( _classVariablePanel.get_key2( true), method);
//		if ( null == method)
//			return;
//
//		ClassVariableObject classVariableObject = _classVariablePanel.get_selected_classVariableObject();
//		if ( null == classVariableObject)
//			return;
//
//		TreeMap<String, MethodObject> methodObjectMap = _methodObjectMapMap.get( classVariableObject._classname);
//		if ( null == methodObjectMap)
//			return;
//
//		MethodObject methodObject = methodObjectMap.get( method);
//		if ( null == methodObject)
//			return;
//
//		_argumentTable.get( methodObject);
//
//		methodObject._returnVariableRule = get_returnVariableRule();
//
//		String key = _classVariablePanel.get_key2( true);
//		if ( null != key) {
//			_methodMap.put( key, method);
//		}
//	}

	/**
	 * @param key
	 * @param method
	 */
	private void on_method_deselected(String key, String method) {
		if ( null == method)
			return;

		ClassVariableObject classVariableObject = _classVariablePanel.get_selected_classVariableObject();
		if ( null == classVariableObject)
			return;

		TreeMap<String, MethodObject> methodObjectMap = _methodObjectMapMap.get( classVariableObject._classname);
		if ( null == methodObjectMap)
			return;

		MethodObject methodObject = methodObjectMap.get( method);
		if ( null == methodObject)
			return;

		_argumentTable.get( methodObject);

		methodObject._returnVariableRule = get_returnVariableRule();

		if ( null != key) {
			_methodMap.put( key, method);
		}
	}

	/**
	 * @param arg0
	 */
	protected void on_method_selected(ActionEvent arg0) {
		if ( _ignore)
			return;

		on_method_selected( ( String)_methodComboBox.getSelectedItem());
	}

	/**
	 * @param method
	 */
	private void on_method_selected(String method) {
		// TODO Auto-generated method stub
		DefaultTableModel defaultTableModel = ( DefaultTableModel)_argumentTable.getModel();

		if ( null == method) {
			_returnTypeTextField.setText( "");
			set_enable_returnVariableButton( false);
			set_returnVariableRule( null);
			_argumentTable.removeAll();
			defaultTableModel.setRowCount( 0);
			return;
		}

//		String classVariableName = ( String)_classVriableComboBox.getSelectedItem();
//		if ( null == classVariableName) {
//			_returnTypeTextField.setText( "");
//			set_enable_return_value_button( false);
//			_argumentTable.removeAll();
//			defaultTableModel.setRowCount( 0);
//			return;
//		}
//
//		ClassVariableObject classVariableObject;
//		if ( !_spotCheckBox.isSelected() && !_spotVariableCheckBox.isSelected())
//			classVariableObject = get_agent_class_variable( classVariableName);
//		else
//			classVariableObject = get_spot_class_variable( classVariableName);
//		if ( null == classVariableObject) {
//			if ( !Environment.get_instance().is_exchange_algebra_enable()) {
//				_returnTypeTextField.setText( "");
//				set_enable_return_value_button( false);
//				_argumentTable.removeAll();
//				defaultTableModel.setRowCount( 0);
//				return;
//			} else {
//				String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);
//				if ( CommonRuleManipulator.is_object( "exchange algebra", spot + classVariableName))
//					// TODO もし選択されているのが交換代数なら、、、
//					classVariableObject = new ClassVariableObject( classVariableName, Constant._exchangeAlgebraJarFilename, Constant._exchangeAlgebraClassname);
//				else {
//					_returnTypeTextField.setText( "");
//					set_enable_return_value_button( false);
//					_argumentTable.removeAll();
//					defaultTableModel.setRowCount( 0);
//					return;
//				}
//			}
//		}

		ClassVariableObject classVariableObject = _classVariablePanel.get_selected_classVariableObject();
		if ( null == classVariableObject) {
			_returnTypeTextField.setText( "");
			set_enable_returnVariableButton( false);
			set_returnVariableRule( null);
			_argumentTable.removeAll();
			defaultTableModel.setRowCount( 0);
			return;
		}

		TreeMap<String, MethodObject> methodObjectMap = _methodObjectMapMap.get( classVariableObject._classname);
		if ( null == methodObjectMap) {
			_returnTypeTextField.setText( "");
			set_enable_returnVariableButton( false);
			set_returnVariableRule( null);
			_argumentTable.removeAll();
			defaultTableModel.setRowCount( 0);
			return;
		}

		MethodObject methodObject = methodObjectMap.get( method);
		if ( null == methodObject) {
			_returnTypeTextField.setText( "");
			set_enable_returnVariableButton( false);
			set_returnVariableRule( null);
			_argumentTable.removeAll();
			defaultTableModel.setRowCount( 0);
			return;
		}


		set_enable_returnVariableButton( ( null == methodObject._returnType || methodObject._returnType.equals( "") || methodObject._returnType.equals( "void")) ? false : true);
		

		if ( null == methodObject._returnType || methodObject._returnType.equals( "")) {
			_returnTypeTextField.setText( "");
			_returnType = "";
			set_returnVariableRule( null);
		} else {
			String[] words = methodObject._returnType.split( "\\.");
			_returnTypeTextField.setText( ( null != words && 0 < words.length) ? words[ words.length - 1] : "");
			_returnType = methodObject._returnType;
			set_returnVariableRule( methodObject._returnVariableRule);
		}

		_returnTypeTextField.setToolTipText( ( null != methodObject._returnType && !methodObject._returnType.equals( "")) ? methodObject._returnType : null);

		_argumentTable.removeAll();
		defaultTableModel.setRowCount( 0);

		Object[] objects = new Object[ 2];
		for ( int i = 0; i < methodObject._argumentTypes.length; ++i) {
			objects[ 0] = methodObject._argumentTypes[ i];
			if ( null == objects[ 0])
				continue;

			objects[ 1] = methodObject._argumentVariableRules.get( i);
			if ( null == objects[ 1])
				continue;

			defaultTableModel.addRow( objects);
		}

		_methodHistoryMap.put( classVariableObject._classname, method);
	}

	/**
	 * @param parent
	 */
	private void setup_returnTypeTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		JLabel label = _rulePropertyPanelBase.create_label( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.label.return.type"), true);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_returnTypeTextField = _rulePropertyPanelBase.create_textField( _standardControlWidth, false);
		_returnTypeTextField.setEditable( false);
		panel.add( _returnTypeTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param returnVariableRule
	 */
	protected void set_returnVariableRule(EntityVariableRule returnVariableRule) {
	}

	/**
	 * @return
	 */
	protected EntityVariableRule get_returnVariableRule() {
		return new EntityVariableRule();
	}

	/**
	 * @param enable
	 */
	protected void set_enable_returnVariableButton(boolean enable) {
	}

	/**
	 * @param parent
	 */
	protected void setup_returnVariableTextField(JPanel parent) {
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_argumentTable(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		JLabel label = _rulePropertyPanelBase.create_label( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.label.parameter"), true);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_argumentTable = new ArgumentTable( _object, _property, _role, _rulePropertyPanelBase, _rulePropertyPanelBase._color, _owner, _parent);
		if ( !_argumentTable.setup())
			return false;

		_argumentTableScrollPane = new JScrollPane();
		_argumentTableScrollPane.getViewport().setView( _argumentTable);
		panel.add( _argumentTableScrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	@Override
	public int get_max_width(int width) {
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);
		return width;
	}

	@Override
	public void set_max_width(int width) {
		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));
		_argumentTableScrollPane.setPreferredSize( new Dimension( _argumentTableScrollPane.getPreferredSize().width, 120));
	}

	/**
	 * 
	 */
	public void clear() {
		_methodComboBox.removeAllItems();
	}

	/**
	 * @param key
	 * @param classVariableObject
	 */
	public void on_variable_selected(String key, ClassVariableObject classVariableObject) {
		if ( null == key || null == classVariableObject)
			return;

		String previousMethod = ( String)_methodComboBox.getSelectedItem();

		TreeMap<String, MethodObject> methodObjectMap = _methodObjectMapMap.get( classVariableObject._classname);
		if ( null == methodObjectMap) {
			Node node = JarFileProperties.get_instance().get_jarfile_node( classVariableObject._jarFilename);
			if ( null == node)
				return;

			node = JarFileProperties.get_instance().get_class_node( node, classVariableObject._classname);
			if ( null == node)
				return;

			NodeList nodeList = XmlTool.get_node_list( node, "method");
			if ( null == nodeList)
				return;

			List<MethodObject> methodList = new ArrayList<MethodObject>();
			for ( int i = 0; i < nodeList.getLength(); ++i) {
				node = nodeList.item( i);
				if ( null == node)
					continue;

				String name = XmlTool.get_attribute( node, "name");
				if ( null == name)
					continue;

				if ( name.equals( classVariableObject._classname)) {
					String[] words = name.split( "\\.");
					name = ( ( null != words && 0 < words.length) ? words[ words.length - 1] : name);
				}

				String returnType = XmlTool.get_attribute( node, "return_type");
				if ( null == returnType || ( _rulePropertyPanelBase._kind.equals( "condition") && !returnType.equals( "boolean")))
				//if ( null == returnType || ( _rulePropertyPanelBase._kind.equals( "condition") && ( returnType.equals( "") || returnType.equals( "void"))))
					continue;

				List<String> argumentTypes = new ArrayList<String>();
				int index = 0;
				while ( true) {
					String argumentType = XmlTool.get_attribute( node, "parameter_type" + index);
					if ( null == argumentType)
						break;

					argumentTypes.add( argumentType);

					++index;
				}

				methodList.add( new MethodObject( name, returnType, argumentTypes, _role));
			}

			MethodObject[] methodObjects = methodList.toArray( new MethodObject[ 0]);
			Arrays.sort( methodObjects, new MethodObjectComparator());

			methodObjectMap = new TreeMap<String, MethodObject>();

			for ( int i = 0; i < methodObjects.length; ++i) {
				if ( i == methodObjects.length - 1)
					methodObjectMap.put( methodObjects[ i].get_full_method_name(), methodObjects[ i]);
					//methodObjectMap.put( methodObjects[ i]._name, methodObjects[ i]);
				else {
					if ( methodObjects[ i]._name.equals( methodObjects[ i + 1]._name))
						i = append( methodObjects, methodObjects[ i]._name, i, methodObjectMap);
					else
						methodObjectMap.put( methodObjects[ i].get_full_method_name(), methodObjects[ i]);
						//methodObjectMap.put( methodObjects[ i]._name, methodObjects[ i]);
				}
			}

			_methodObjectMapMap.put( classVariableObject._classname, methodObjectMap);
		}

		_ignore = true;

		if ( methodObjectMap.isEmpty()) {
			_methodComboBox.removeAllItems();
			_ignore = false;
			return;
		}

		List<String> methodNames = new ArrayList<String>( methodObjectMap.keySet());
		CommonTool.update( _methodComboBox, methodNames.toArray( new String[ 0]));

		String method = _methodMap.get( key);
		if ( null != method) {
			// key(self....class variable.variable)が表すエンティティ選択状態に対して過去に選択したメソッドが一意に決まる場合(このエンティティ選択状態で過去にメソッドを選択している場合)
			//if ( !select_method( previousMethod, method)) {
			if ( !select_method( key, previousMethod, method)) {
				_ignore = false;
				_methodComboBox.setSelectedIndex( 0);
			}
			//System.out.println( "on_variable_selected : " + key + " = " + method);
		} else {
			// key(self....class variable.variable)が表すエンティティ選択状態に対して過去に選択したメソッドが一意に決まらない場合(このエンティティ選択状態で初めてメソッドを選択する場合)
			method = _methodHistoryMap.get( classVariableObject._classname);
			if ( null != method) {
				// key(self....class variable.variable)が表すエンティティ選択状態に対して過去に選択したメソッドは一意に決まらないが、過去に同じクラスを使用したことがある場合
				// このエンティティ選択状態でメソッドを選択するのは初めてだが、過去に同じクラスをクラスを使用したことがある場合
				//if ( !select_method( previousMethod, method)) {
				if ( !select_method( key, previousMethod, method)) {
					_ignore = false;
					_methodComboBox.setSelectedIndex( 0);
				}
				//System.out.println( "on_variable_selected : " + key + " = " + method);
			} else {
				if ( null != previousMethod) {
					// key(self....class variable.variable)が表すエンティティ選択状態に対して過去に選択したメソッドが一意に決まらないだけでなく過去に同じクラスを使用したことがない場合
					// このエンティティ選択状態でメソッドを選択するのは初めてであり、更に過去に同じクラスをクラスを使用したこともない場合
					//if ( !select_method( previousMethod, previousMethod)) {
					if ( !select_method( key, previousMethod, previousMethod)) {
						_ignore = false;
						_methodComboBox.setSelectedIndex( 0);
					}
					//System.out.println( "on_variable_selected : " + key + " = " + previousMethod);
				}
				// 上記のいずれでもない場合はなにもしようがない
			}
		}

		_ignore = false;
	}

	/**
	 * @param methodObjects
	 * @param name
	 * @param start
	 * @param methodObjectMap
	 * @return
	 */
	private int append(MethodObject[] methodObjects, String name, int start, TreeMap<String, MethodObject> methodObjectMap) {
		int index = 1;
		for ( int i = start; i < methodObjects.length; ++i) {
			if ( !methodObjects[ i]._name.equals( name))
				return ( i - 1);

			methodObjectMap.put( methodObjects[ i].get_full_method_name(), methodObjects[ i]);
			//methodObjectMap.put( name + " - (" + index + ")", methodObjects[ i]);
			++index;
		}
		return ( methodObjects.length - 1);
	}

	/**
	 * @param key
	 * @param previousMethod
	 * @param method
	 * @return
	 */
	private boolean select_method(String key, String previousMethod, String method) {
	//private boolean select_method(String previousMethod, String method) {
		for ( int i = 0; i < _methodComboBox.getItemCount(); ++i) {
			if ( method.equals( _methodComboBox.getItemAt( i))) {
				//on_method_deselected( key, previousMethod);
				//on_method_deselected( previousMethod);
				_methodComboBox.setSelectedItem( method);
				on_method_selected( method);
				return true;
			}
		}
		return false;
	}

	/**
	 * @param key
	 * @param classVariableObject
	 */
	public void store(String key, ClassVariableObject classVariableObject) {
		if ( null == key || null == classVariableObject)
			return;

		String method = ( String)_methodComboBox.getSelectedItem();
		if ( null == method)
			return;

		_methodMap.put( key, method);

		TreeMap<String, MethodObject> methodObjectMap = _methodObjectMapMap.get( classVariableObject._classname);
		if ( null == methodObjectMap)
			return;

		MethodObject methodObject = methodObjectMap.get( method);
		if ( null == methodObject)
			return;

		_argumentTable.get( methodObject);

//		System.out.println( "key = " + key + " : class name = " + classVariableObject._classname + " : method = " + method);
	}

	/**
	 * @param object
	 * @param check
	 * @param key
	 * @return
	 */
	public boolean set(IObject object, boolean check, String key) {
		if ( !( object instanceof JavaMethodCallRule))
			return false;

		JavaMethodCallRule javaMethodCallRule = ( JavaMethodCallRule)object;

		_ignore = true;
		_methodComboBox.setSelectedItem( javaMethodCallRule._method);
		_ignore = false;
		on_method_selected( javaMethodCallRule._method);

		set_returnVariableRule( javaMethodCallRule._returnVariableRule);

		if ( !_argumentTable.set( javaMethodCallRule))
			return false;

		ClassVariableObject classVariableObject = _classVariablePanel.get_selected_classVariableObject();
		String method = ( String)_methodComboBox.getSelectedItem();
		if ( null != classVariableObject && null != method) {
			_methodMap.clear();
			_methodMap.put( key, method);

			TreeMap<String, MethodObject> methodObjectMap = _methodObjectMapMap.get( classVariableObject._classname);
			if ( null == methodObjectMap)
				return false;

			MethodObject methodObject = methodObjectMap.get( method);
			if ( null == methodObject)
				return false;

			_argumentTable.get( methodObject);

			methodObject._returnVariableRule = new EntityVariableRule( javaMethodCallRule._returnVariableRule);

			_methodHistoryMap.clear();
			_methodHistoryMap.put( classVariableObject._classname, method);
		}

		return true;
	}

	/**
	 * @param object
	 * @param check
	 * @return
	 */
	public boolean get(IObject object, boolean check) {
		if ( !( object instanceof JavaMethodCallRule))
			return false;

		JavaMethodCallRule javaMethodCallRule = ( JavaMethodCallRule)object;

		String method = ( String)_methodComboBox.getSelectedItem();
		if ( null == method || method.equals( ""))
			return false;

		javaMethodCallRule._method = method;

		EntityVariableRule returnVariableRule = get_returnVariableRule();
		if ( _rulePropertyPanelBase._kind.equals( "command") && !_returnType.equals( "") && !_returnType.equals( "void") && returnVariableRule._variableValue.equals( ""))
			return false;

		javaMethodCallRule._returnVariableRule = returnVariableRule;

		if ( !_argumentTable.get( javaMethodCallRule, check))
			return false;

		return true;
	}
}
