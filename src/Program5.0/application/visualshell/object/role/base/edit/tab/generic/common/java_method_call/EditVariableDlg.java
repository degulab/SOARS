/*
 * Created on 2005/11/02
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.java_method_call;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.common.common.SpecificObjectPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Variable;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 */
public class EditVariableDlg extends Dialog {

	/**
	 * 
	 */
	protected String _type = null;

	/**
	 * 
	 */
	public EntityVariableRule _entityVariableRule = null;

	/**
	 * 
	 */
	protected Subject _object = null;

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
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private TextField _typeTextField = null;

	/**
	 * 
	 */
	private SpecificObjectPanel _specificObjectPanel = null;

	/**
	 * 
	 */
	private int _minimumHeight;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param type 
	 * @param entityVariableRule
	 * @param object
	 * @param property
	 * @param role
	 * @param rulePropertyPanelBase
	 */
	public EditVariableDlg(Frame arg0, String arg1, boolean arg2, String type, EntityVariableRule entityVariableRule, Subject object, Property property, Role role, RulePropertyPanelBase rulePropertyPanelBase) {
		super(arg0, arg1, arg2);
		_type = type;
		_entityVariableRule = new EntityVariableRule( entityVariableRule);
		_property = property;
		_role = role;
		_rulePropertyPanelBase = rulePropertyPanelBase;
		_object = new Subject( object);
		_object.clear();
		setup_object( object);
	}

	/**
	 * @param object
	 */
	protected void setup_object(Subject object) {
		if ( _type.equals( "boolean")) {
			_object.add( new Variable( "keyword",
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.keyword")));
		} else if ( _type.equals( "java.lang.String")) {
			_object.add( new Variable( "keyword",
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.keyword")));
			_object.add( new Variable( "file",
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.file")));
		} else if ( _type.equals( "int")
			|| _type.equals( "byte")
			|| _type.equals( "short")
			|| _type.equals( "long")) {
		} else if ( _type.equals( "double")
			|| _type.equals( "float")) {
			_object.add( new Variable( "number object",
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.number.object")));
		} else if ( _type.equals( "java.util.Collection")) {
			_object.add( new Variable( "collection",
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.collection")));
			_object.add( new Variable( "list",
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.list")));
		} else if ( _type.equals( "java.util.HashSet")
			|| _type.equals( "java.util.Set")) {
			_object.add( new Variable( "collection",
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.collection")));
		} else if ( _type.equals( "java.util.LinkedList")
			|| _type.equals( "java.util.List")) {
			_object.add( new Variable( "list",
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.list")));
		} else if ( _type.equals( "java.util.HashMap")
			|| _type.equals( "java.util.Map")) {
			_object.add( new Variable( "map",
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.map")));
		} else {
			_object.add( new Variable( "class variable",
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.class.variable")));
			if ( Environment.get_instance().is_exchange_algebra_enable()) {
				if ( _type.equals( Constant._exchangeAlgebraClassname))
					_object.add( new Variable( "exchange algebra",
						ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.exchange.algebra")));
			}
		}
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	@Override
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		getContentPane().setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		SwingTool.insert_vertical_strut( northPanel, 5);

		setup_variablePanel( northPanel);

		if ( !setup_objectPanel( northPanel))
			return false;

		getContentPane().add( northPanel, "North");


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);
		southPanel.add( panel);

		SwingTool.insert_vertical_strut( southPanel, 5);

		getContentPane().add( southPanel, "South");


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		link_to_cancel( getRootPane());


		adjust();


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_variablePanel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 2));

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		SwingTool.insert_vertical_strut( northPanel, 5);

		setup_typeTextField( northPanel);

		SwingTool.insert_vertical_strut( northPanel, 5);

		basePanel.add( northPanel, "North");

		basePanel.setBorder( BorderFactory.createLineBorder( Color.blue));

		panel.add( basePanel);

		panel.add( Box.createHorizontalStrut( 2));

		parent.add( panel);

		SwingTool.insert_vertical_strut( parent, 5);
	}

	/**
	 * @param parent
	 */
	private void setup_typeTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = _rulePropertyPanelBase.create_label( get_type_name(), true);
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_typeTextField = _rulePropertyPanelBase.create_textField( false);
		String[] words = _type.split( "\\.");
		_typeTextField.setText( ( null != words && 0 < words.length) ? words[ words.length - 1] : "");
		_typeTextField.setEditable( false);
		_typeTextField.setToolTipText( _type);
		panel.add( _typeTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @return
	 */
	protected String get_type_name() {
		return "";
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_objectPanel(JPanel parent) {
		_specificObjectPanel = new SpecificObjectPanel( _object, _property, _role, _rulePropertyPanelBase);
		if ( !_specificObjectPanel.setup())
			return false;

		_specificObjectPanel.set( _entityVariableRule, true);

		parent.add( _specificObjectPanel);

		SwingTool.insert_vertical_strut( parent, 5);

		return true;
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);
		width = _specificObjectPanel.get_max_width( width);

		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension(width, label.getPreferredSize().height));
		_specificObjectPanel.set_max_width( width);
}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	@Override
	protected void on_setup_completed() {
		_minimumHeight = getPreferredSize().height;

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				setSize( width, _minimumHeight);
			}
		});
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_ok(ActionEvent actionEvent) {
		EntityVariableRule entityVariableRule = ( EntityVariableRule)_specificObjectPanel.get( true);
		if ( null == entityVariableRule)
			return;

		_entityVariableRule.copy( entityVariableRule);

		super.on_ok(actionEvent);
	}
}
