/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab.variable.panel.set_and_list.base.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.agent.AgentObject;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.VariablePanelBase;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.table.InitialValueTableBase;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.table.RowHeaderTable;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.set_and_list.base.panel.initial.VariableInitialValueAgentPanel;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.set_and_list.base.panel.initial.VariableInitialValueObjectPanel;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.set_and_list.base.panel.initial.VariableInitialValueSpotPanel;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.set_and_list.base.panel.initial.base.VariableInitialValuePanel;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.set_and_list.base.panel.table.VariableInitialValueTable;
import soars.application.visualshell.object.player.base.object.base.ObjectBase;
import soars.application.visualshell.object.player.base.object.variable.VariableInitialValue;
import soars.application.visualshell.object.player.base.object.variable.VariableObject;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.observer.Observer;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class VariablePanel extends VariablePanelBase {

	/**
	 * 
	 */
	protected ComboBox _typeComboBox = null;

	/**
	 * 
	 */
	protected List<VariableInitialValuePanel> _variableInitialValuePanels = new ArrayList<VariableInitialValuePanel>();

	/**
	 * 
	 */
	protected VariableInitialValueTable _variableInitialValueTable = null;

	/**
	 * 
	 */
	protected RowHeaderTable _rowHeaderTable = null;

	/**
	 * @param kind
	 * @param playerBase
	 * @param propertyPageMap
	 * @param variableTableBase 
	 * @param color 
	 * @param owner
	 * @param parent
	 */
	public VariablePanel(String kind, PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap, VariableTableBase variableTableBase, Color color, Frame owner, Component parent) {
		super(kind, playerBase, propertyPageMap, variableTableBase, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.VariablePanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	protected boolean setup_center_panel(JPanel parent) {
		_variableInitialValueTable = new VariableInitialValueTable( _color, _playerBase, _propertyPageMap, this, _owner, _parent);
		_rowHeaderTable = new RowHeaderTable(  _color, _playerBase, this, _owner, _parent);

		setup_left_panel( parent);

		if ( !setup_right_panel( parent))
			return false;

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_left_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_vertical_strut( north_panel);
		setup_name_textField( north_panel);
		insert_vertical_strut( north_panel);
		setup_type_comboBox( north_panel);
		insert_vertical_strut( north_panel);
		setup_variableInitialValuePanels( north_panel);
		insert_vertical_strut( north_panel);
		setup_comment_textField( north_panel);

		panel.add( north_panel, "North");

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_type_comboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.variable.dialog.type"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		Vector<String> items = new Vector<String>();
		items.add( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.agent"));
		items.add( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.spot"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"));
		if ( _playerBase instanceof AgentObject) {
			items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable"));
		}
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.list"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.map"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.file"));
		if ( Environment.get_instance().is_exchange_algebra_enable()) {
			items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"));
		}
		_typeComboBox = new ComboBox( items.toArray( new String[ 0]), _color, false, new CommonComboBoxRenderer( _color, false));
		_typeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = ( String)_typeComboBox.getSelectedItem();
				_variableInitialValuePanels.get( 0).setVisible( item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.agent")));
				_variableInitialValuePanels.get( 1).setVisible( item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.spot")));
				boolean flag = ( !item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.agent"))
					&& !item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.spot")));
				_variableInitialValuePanels.get( 2).setVisible( flag);
				if ( flag)
					_variableInitialValuePanels.get( 2).update( item);
				revalidate();
				repaint();
			}
		});
		_components.add( _typeComboBox);
		panel.add( _typeComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_variableInitialValuePanels(JPanel parent) {
		_variableInitialValuePanels.add( new VariableInitialValueAgentPanel( _color, _playerBase, _propertyPageMap, _typeComboBox, _variableInitialValueTable));
		_variableInitialValuePanels.add( new VariableInitialValueSpotPanel( _color, _playerBase, _propertyPageMap, _typeComboBox, _variableInitialValueTable));
		_variableInitialValuePanels.add( new VariableInitialValueObjectPanel( _color, _playerBase, _propertyPageMap, _typeComboBox, _variableInitialValueTable));
		for ( VariableInitialValuePanel variableInitialValuePanel:_variableInitialValuePanels) {
			variableInitialValuePanel.setup();
			_components.add( variableInitialValuePanel);
			parent.add( variableInitialValuePanel);
		}
	}

	/**
	 * @return
	 * @param parent
	 */
	private boolean setup_right_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_vertical_strut( north_panel);

		if ( !_variableInitialValueTable.setup( _rowHeaderTable))
			return false;

		if ( !_rowHeaderTable.setup( _variableInitialValueTable, ( Graphics2D)_parent.getGraphics()))
			return false;

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _variableInitialValueTable);
		scrollPane.getViewport().setOpaque( true);
		scrollPane.getViewport().setBackground( SystemColor.text);

//		JViewport viewport = new JViewport();
//		viewport.setView( _rowHeaderTable);
//		scrollPane.setRowHeader( viewport);
		scrollPane.setRowHeaderView( _rowHeaderTable);
		scrollPane.getRowHeader().setOpaque( true);
		scrollPane.getRowHeader().setBackground( SystemColor.text);
		Dimension dimension = scrollPane.getRowHeader().getPreferredSize();
		scrollPane.getRowHeader().setPreferredSize(
			new Dimension( _rowHeaderTable.getColumnModel().getColumn( 0).getWidth(),
			dimension.height));
		SwingTool.set_table_left_top_corner_column( scrollPane);

		scrollPane.getRowHeader().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JViewport viewport = ( JViewport) e.getSource();
				scrollPane.getVerticalScrollBar().setValue( viewport.getViewPosition().y);
			}
		});

		north_panel.add( scrollPane);

		insert_vertical_strut( north_panel);

		panel.add( north_panel, "North");

		parent.add( panel);

		scrollPane.setPreferredSize( new Dimension( scrollPane.getPreferredSize().width, 300));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.VariablePanelBase#get_label_width(int)
	 */
	public int get_label_width(int width) {
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);

		for ( VariableInitialValuePanel variableInitialValuePanel:_variableInitialValuePanels)
			width = variableInitialValuePanel.get_label_width( width);

		return width;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.VariablePanelBase#adjust(int)
	 */
	public int adjust(int width) {
		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));

		for ( VariableInitialValuePanel variableInitialValuePanel:_variableInitialValuePanels)
			variableInitialValuePanel.adjust( width);

		return ( _buttons.get( 0).getPreferredSize().width + 5 + _buttons.get( 1).getPreferredSize().width);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.VariablePanelBase#on_setup_completed()
	 */
	public void on_setup_completed() {
		_variableInitialValuePanels.get( 1).setVisible( false);
		_variableInitialValuePanels.get( 2).setVisible( false);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#contains(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public boolean contains(ObjectBase objectBase) {
		return _variableInitialValueTable.contains( objectBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.VariablePanelBase#update_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean update_object_name(String type, String name, String new_name) {
		return _variableInitialValueTable.update_object_name( type, name, new_name);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.VariablePanelBase#update(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public void update(ObjectBase objectBase) {
		_nameTextField.setText( ( null != objectBase && objectBase instanceof VariableObject) ? objectBase._name : "");
		_commentTextField.setText( ( null != objectBase && objectBase instanceof VariableObject) ? objectBase._comment : "");
		_variableInitialValueTable.update( ( null != objectBase && objectBase instanceof VariableObject) ? ( VariableObject)objectBase : null);
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
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.VariablePanelBase#update()
	 */
	public void update() {
		String item = ( String)_typeComboBox.getSelectedItem();
		_variableInitialValuePanels.get( 2).update( item);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.VariablePanelBase#changeSelection(java.lang.Object)
	 */
	public void changeSelection(Object object) {
		if ( null == object)
			return;

		if ( !( object instanceof VariableInitialValue))
			return;

		VariableInitialValue variableInitialValue = ( VariableInitialValue)object;
		_typeComboBox.setSelectedItem( InitialValueTableBase.__nameMap.get( variableInitialValue._type));

		if ( variableInitialValue._type.equals( "agent"))
			_variableInitialValuePanels.get( 0).set( variableInitialValue._value);
		else if ( variableInitialValue._type.equals( "spot"))
			_variableInitialValuePanels.get( 1).set( variableInitialValue._value);
		else
			_variableInitialValuePanels.get( 2).set( variableInitialValue._value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.VariablePanelBase#clear()
	 */
	public void clear() {
		super.clear();
		_variableInitialValueTable.update( ( VariableObject)null);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#is_empty()
	 */
	protected boolean is_empty() {
		return ( super.is_empty() && ( 0 == _variableInitialValueTable.getRowCount()));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#create_and_get()
	 */
	protected ObjectBase create_and_get() {
		VariableObject variableObject = new VariableObject( _kind);
		variableObject._name = _nameTextField.getText();
		variableObject._comment = _commentTextField.getText();
		_variableInitialValueTable.get( variableObject);
		return variableObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#can_get_data(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	protected boolean can_get_data(ObjectBase objectBase) {
		if ( !( objectBase instanceof VariableObject))
			return false;

		VariableObject variableObject = ( VariableObject)objectBase;

		if ( !variableObject._kind.equals( _kind))
			// これは起こり得ない筈だが念の為
			return false;

		if ( !Constant.is_correct_name( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( !_nameTextField.getText().equals( variableObject._name)) {
			if ( _variableTableBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( _variableInitialValueTable.contains( variableObject)) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.initial.values.contain.self.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String[] property_pages = Constant.get_property_pages( "variable");

		for ( int i = 0; i < property_pages.length; ++i) {
			PropertyPageBase propertyPageBase = _propertyPageMap.get( property_pages[ i]);
			if ( null != propertyPageBase && propertyPageBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( _kind);

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), _playerBase)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#get_data(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	protected void get_data(ObjectBase objectBase) {
		if ( !( objectBase instanceof VariableObject))
			return;

		VariableObject variableObject = ( VariableObject)objectBase;

		if ( !variableObject._name.equals( "") && !_nameTextField.getText().equals( variableObject._name)) {
			_propertyPageMap.get( "variable").update_object_name( _kind, variableObject._name, _nameTextField.getText());

			WarningManager.get_instance().cleanup();

			boolean result = LayerManager.get_instance().update_object_name( _kind, variableObject._name, _nameTextField.getText(), _playerBase);
			if ( result) {
				if ( _playerBase.update_object_name( _kind, variableObject._name, _nameTextField.getText())) {
					String[] message = new String[] {
						( ( _playerBase instanceof AgentObject) ? "Agent : " : "Spot : ") + _playerBase._name
					};

					WarningManager.get_instance().add( message);
				}

				if ( _playerBase instanceof AgentObject)
					Observer.get_instance().on_update_agent_object( _kind, variableObject._name, _nameTextField.getText());
				else if ( _playerBase instanceof SpotObject)
					Observer.get_instance().on_update_spot_object( _kind, variableObject._name, _nameTextField.getText());

				Observer.get_instance().on_update_playerBase( true);
				Observer.get_instance().modified();
			}
		}

		variableObject._name = _nameTextField.getText();
		variableObject._comment = _commentTextField.getText();

		_variableInitialValueTable.get( variableObject);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PanelBase#can_paste(soars.application.visualshell.object.player.base.object.base.ObjectBase, java.util.List)
	 */
	public boolean can_paste(ObjectBase objectBase, List<ObjectBase> objectBases) {
		if ( !( objectBase instanceof VariableObject))
			return false;

		VariableObject variableObject = ( VariableObject)objectBase;

		if ( !variableObject._kind.equals( _kind))
			// これは起こり得ない筈だが念の為
			return false;

		if ( !Constant.is_correct_name( variableObject._name))
			return false;

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( variableObject._name))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( variableObject._name)))
			return false;

		if ( _variableTableBase.other_objectBase_has_this_name( variableObject._kind, variableObject._name))
			return false;


		if ( _variableInitialValueTable.contains( variableObject))
			return false;

		String[] property_pages = Constant.get_property_pages( "variable");

		for ( int i = 0; i < property_pages.length; ++i) {
			PropertyPageBase propertyPageBase = _propertyPageMap.get( property_pages[ i]);
			if ( null != propertyPageBase && propertyPageBase.contains( variableObject._name))
				return false;
		}

		String[] kinds = Constant.get_kinds( _kind);

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], variableObject._name, _playerBase))
				return false;
		}

		if ( null != LayerManager.get_instance().get_chart( variableObject._name))
			return false;

		if ( !variableObject.can_paste( _propertyPageMap, objectBase, objectBases, _playerBase))
			return false;

		return true;
	}
}
