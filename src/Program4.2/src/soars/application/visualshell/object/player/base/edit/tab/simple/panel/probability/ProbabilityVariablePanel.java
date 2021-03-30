/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab.simple.panel.probability;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.agent.AgentObject;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase;
import soars.application.visualshell.object.player.base.edit.tab.simple.panel.base.SimpleVariablePanel;
import soars.application.visualshell.object.player.base.object.base.ObjectBase;
import soars.application.visualshell.object.player.base.object.base.SimpleVariableObject;
import soars.application.visualshell.object.player.base.object.number.NumberObject;
import soars.application.visualshell.object.player.base.object.probability.ProbabilityObject;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.observer.Observer;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;


/**
 * @author kurata
 *
 */
public class ProbabilityVariablePanel extends SimpleVariablePanel {

	/**
	 * 
	 */
	private TextField _initialValueTextField = null;

	/**
	 * @param playerBase
	 * @param propertyPageMap
	 * @param variableTableBase
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public ProbabilityVariablePanel(PlayerBase playerBase, Map propertyPageMap, VariableTableBase variableTableBase, Color color, Frame owner, Component parent) {
		super("probability", playerBase, propertyPageMap, variableTableBase, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	protected boolean setup_center_panel(JPanel parent) {
		insert_vertical_strut( parent);
		setup_name_textField( parent);
		insert_vertical_strut( parent);
		setup_initial_value_textField( parent);
		insert_vertical_strut( parent);
		setup_comment_textField( parent);
		insert_vertical_strut( parent);
		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_name_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.name"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_nameTextField = new TextField();
		_nameTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters4));
		_nameTextField.setSelectionColor( _color);
		_nameTextField.setForeground( _color);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _nameTextField, this));
		_components.add( _nameTextField);
		panel.add( _nameTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_initial_value_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.probability.dialog.initial.value"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_initialValueTextField = new TextField();
		_initialValueTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters5));
		_initialValueTextField.setSelectionColor( _color);
		_initialValueTextField.setForeground( _color);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _initialValueTextField, this));
		_components.add( _initialValueTextField);
		panel.add( _initialValueTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_comment_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.probability.dialog.comment"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_commentTextField = new TextField();
		_commentTextField.setSelectionColor( _color);
		_commentTextField.setForeground( _color);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));
		_components.add( _commentTextField);
		panel.add( _commentTextField);

		parent.add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.simple.panel.base.SimpleVariablePanel#update(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public void update(ObjectBase objectBase) {
		SimpleVariableObject simpleVariableObject = ( SimpleVariableObject)objectBase;
		_nameTextField.setText( ( null != simpleVariableObject && simpleVariableObject instanceof ProbabilityObject) ? simpleVariableObject._name : "");
		_initialValueTextField.setText( ( null != simpleVariableObject && simpleVariableObject instanceof ProbabilityObject) ? simpleVariableObject._initialValue : "");
		_commentTextField.setText( ( null != simpleVariableObject && simpleVariableObject instanceof ProbabilityObject) ? simpleVariableObject._comment : "");
		// TODO Auto-generated method stub
		_textUndoRedoManagers.clear();
		setup_textUndoRedoManagers();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#setup_textUndoRedoManagers()
	 */
	protected void setup_textUndoRedoManagers() {
		// TODO Auto-generated method stub
		if ( !_textUndoRedoManagers.isEmpty())
			return;

		_textUndoRedoManagers.add( new TextUndoRedoManager( _nameTextField, this));
		_textUndoRedoManagers.add( new TextUndoRedoManager( _initialValueTextField, this));
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#clear()
	 */
	public void clear() {
		super.clear();
		_initialValueTextField.setText( "");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#is_empty()
	 */
	protected boolean is_empty() {
		return ( super.is_empty() && _initialValueTextField.getText().equals( ""));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#create_and_get()
	 */
	protected ObjectBase create_and_get() {
		ProbabilityObject probabilityObject = new ProbabilityObject();
		probabilityObject._name = _nameTextField.getText();
		probabilityObject._initialValue = _initialValueTextField.getText();
		probabilityObject._comment = _commentTextField.getText();
		return probabilityObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#can_get_data(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	protected boolean can_get_data(ObjectBase objectBase) {
		if ( !( objectBase instanceof ProbabilityObject))
			return false;

		ProbabilityObject probabilityObject = ( ProbabilityObject)objectBase;

		if ( !Constant.is_correct_name( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String initial_value = _initialValueTextField.getText();
		if ( !initial_value.equals( "")) {
			if ( initial_value.equals( "$") || 0 < initial_value.indexOf( '$')) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if ( initial_value.startsWith( "$") && 0 < initial_value.indexOf( "$", 1)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if ( initial_value.startsWith( "$") && 0 < initial_value.indexOf( ")", 1)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if ( initial_value.equals( "$Name")
				|| initial_value.equals( "$Role")
				|| initial_value.equals( "$Spot")
				|| 0 <= initial_value.indexOf( Constant._experimentName)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if ( !CommonTool.is_probability_correct( initial_value)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			initial_value = NumberObject.is_correct( initial_value, "real number");
			if ( null == initial_value) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( !_nameTextField.getText().equals( probabilityObject._name)) {
			if ( _variableTableBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] property_pages = Constant.get_property_pages( "simple variable");

		for ( int i = 0; i < property_pages.length; ++i) {
			PropertyPageBase propertyPageBase = _propertyPageMap.get( property_pages[ i]);
			if ( null != propertyPageBase && propertyPageBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( "probability");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), _playerBase)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

//		if ( !initial_value.equals( "")) {
//			initial_value = NumberObject.is_correct( initial_value, "real number");
//			if ( null == initial_value) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#get_data(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	protected void get_data(ObjectBase objectBase) {
		if ( !( objectBase instanceof ProbabilityObject))
			return;

		ProbabilityObject probabilityObject = ( ProbabilityObject)objectBase;

		if ( !probabilityObject._name.equals( "") && !_nameTextField.getText().equals( probabilityObject._name)) {
			_propertyPageMap.get( "variable").update_object_name( "probability", probabilityObject._name, _nameTextField.getText());

			WarningManager.get_instance().cleanup();

			boolean result = LayerManager.get_instance().update_object_name( "probability", probabilityObject._name, _nameTextField.getText(), _playerBase);
			if ( result) {
				if ( _playerBase.update_object_name( "probability", probabilityObject._name, _nameTextField.getText())) {
					String[] message = new String[] {
						( ( _playerBase instanceof AgentObject) ? "Agent : " : "Spot : ") + _playerBase._name
					};

					WarningManager.get_instance().add( message);
				}

				if ( _playerBase instanceof AgentObject)
					Observer.get_instance().on_update_agent_object( "probability", probabilityObject._name, _nameTextField.getText());
				else if ( _playerBase instanceof SpotObject)
					Observer.get_instance().on_update_spot_object( "probability", probabilityObject._name, _nameTextField.getText());

				Observer.get_instance().on_update_playerBase( true);
				Observer.get_instance().modified();
			}
		}

		probabilityObject._name = _nameTextField.getText();
		probabilityObject._initialValue = _initialValueTextField.getText();
		probabilityObject._comment = _commentTextField.getText();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#can_paste(soars.application.visualshell.object.player.base.object.base.ObjectBase, java.util.List)
	 */
	public boolean can_paste(ObjectBase objectBase, List<ObjectBase> objectBases) {
		if ( !( objectBase instanceof ProbabilityObject))
			return false;

		ProbabilityObject probabilityObject = ( ProbabilityObject)objectBase;

		if ( !Constant.is_correct_name( probabilityObject._name))
			return false;

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( probabilityObject._name))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( probabilityObject._name)))
			return false;

		if ( !probabilityObject._initialValue.equals( "")) {
			if ( probabilityObject._initialValue.equals( "$") || 0 < probabilityObject._initialValue.indexOf( '$'))
				return false;

			if ( probabilityObject._initialValue.startsWith( "$") && 0 < probabilityObject._initialValue.indexOf( "$", 1))
				return false;

			if ( probabilityObject._initialValue.startsWith( "$") && 0 < probabilityObject._initialValue.indexOf( ")", 1))
				return false;

			if ( probabilityObject._initialValue.equals( "$Name")
				|| probabilityObject._initialValue.equals( "$Role")
				|| probabilityObject._initialValue.equals( "$Spot")
				|| 0 <= probabilityObject._initialValue.indexOf( Constant._experimentName))
				return false;

			if ( !CommonTool.is_probability_correct( probabilityObject._initialValue))
				return false;

			if ( null == NumberObject.is_correct( probabilityObject._initialValue, "real number"))
				return false;
		}

		if ( _variableTableBase.other_objectBase_has_this_name( probabilityObject._kind, probabilityObject._name))
			return false;

		String[] property_pages = Constant.get_property_pages( "simple variable");

		for ( int i = 0; i < property_pages.length; ++i) {
			PropertyPageBase propertyPageBase = _propertyPageMap.get( property_pages[ i]);
			if ( null != propertyPageBase && propertyPageBase.contains( probabilityObject._name))
				return false;
		}

		String[] kinds = Constant.get_kinds( "probability");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], probabilityObject._name, _playerBase))
				return false;
		}

		if ( null != LayerManager.get_instance().get_chart( probabilityObject._name))
			return false;

		return true;
	}
}
