/*
 * Created on 2006/03/29
 */
package soars.application.visualshell.object.player.base.edit.tab.variable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.agent.AgentObject;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PanelBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.exchange_algebra.ExchangeAlgebraPanel;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.map.MapPanel;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.set_and_list.list.ListPanel;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.set_and_list.set.SetPanel;
import soars.application.visualshell.object.player.base.object.base.ObjectBase;
import soars.application.visualshell.object.player.base.object.exchange_algebra.ExchangeAlgebraObject;
import soars.application.visualshell.object.player.base.object.map.MapObject;
import soars.application.visualshell.object.player.base.object.variable.VariableObject;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.observer.Observer;

/**
 * @author kurata
 */
public class VariablePropertyPage extends PropertyPageBase {

	/**
	 * 
	 */
	private VariableTable _variableTable = null;

	/**
	 * 
	 */
	private JLabel _kindLabel = null;

	/**
	 * 
	 */
	private JComboBox _kindComboBox = null;

	/**
	 * 
	 */
	private SetPanel _setPanel = null;

	/**
	 * 
	 */
	private ListPanel _listPanel = null;

	/**
	 * 
	 */
	private MapPanel _mapPanel = null;

	/**
	 * 
	 */
	private ExchangeAlgebraPanel _exchangeAlgebraPanel = null;

	/**
	 * @param title
	 * @param playerBase
	 * @param propertyPageMap
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public VariablePropertyPage(String title, PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap, int index, Frame owner, Component parent) {
		super(title, playerBase, propertyPageMap, index, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#contains(java.lang.String)
	 */
	public boolean contains(String name) {
		return _variableTable.contains( name);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#contains(java.lang.String, java.lang.String)
	 */
	public boolean contains(String name, String number) {
		return _variableTable.contains(name, number);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#contains(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public boolean contains(ObjectBase objectBase) {
		boolean result = false;
		if ( _variableTable.contains( objectBase))
			result = true;

		for ( PanelBase panelBase:_panelBaseMap.values()) {
			if ( panelBase.contains( objectBase))
				result = true;
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#can_adjust_name(java.lang.String, java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_name(String type, String headName, Vector ranges) {
		return _variableTable.can_adjust_name( type, headName, ranges);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#can_adjust_name(java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_name(String type, String headName, Vector ranges, String newHeadName, Vector newRanges) {
		return _variableTable.can_adjust_name( type, headName, ranges, newHeadName, newRanges);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#update_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public void update_name_and_number(String type, String newName, String originalName, String headName, Vector ranges, String newHeadName, Vector newRanges) {
		_variableTable.update_name_and_number( type, newName, originalName, headName, ranges, newHeadName, newRanges);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#update_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean update_object_name(String type, String name, String newName) {
		boolean result = false;
		if ( _variableTable.update_object_name( type, name, newName))
			result = true;

		for ( PanelBase panelBase:_panelBaseMap.values()) {
			if ( panelBase.update_object_name( type, name, newName))
				result = true;
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#get(java.lang.String)
	 */
	public String[] get(String kind) {
		return _variableTable.get(kind);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#update()
	 */
	public void update() {
		for ( PanelBase panelBase:_panelBaseMap.values())
			panelBase.update();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.tab.TabbedPage#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		setLayout( new BorderLayout());


		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( center_panel);

		if ( !setup( center_panel))
			return false;

		insert_vertical_strut( center_panel);

		add( center_panel);


		adjust();


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup(JPanel parent) {
		JPanel base_panel = new JPanel();
		base_panel.setLayout( new BorderLayout());


		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		if ( !setup_center_panel( center_panel))
			return false;

		base_panel.add( center_panel);


		JPanel south_panel = new JPanel();
		south_panel.setLayout( new BoxLayout( south_panel, BoxLayout.Y_AXIS));

		if ( !setup_components( south_panel))
			return false;

		base_panel.add( south_panel, "South");
		

		parent.add( base_panel);


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_center_panel(JPanel parent) {
		parent.setLayout( new BorderLayout());

		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		create_variableTable();

		if ( !setup_variableTable( center_panel))
			return false;

		parent.add( center_panel);


		JPanel south_panel = new JPanel();
		south_panel.setLayout( new BoxLayout( south_panel, BoxLayout.Y_AXIS));

		insert_vertical_strut( south_panel);

		if ( !setup_kind_components( south_panel))
			return false;

		parent.add( south_panel, "South");

		return true;
	}

	/**
	 * 
	 */
	private void create_variableTable() {
		_variableTable = new VariableTable( _playerBase, _propertyPageMap, this, _owner, _parent);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_variableTable(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_variableTable.setup())
			return false;

		if ( _playerBase.is_multi())
			_variableTable.setEnabled( false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _variableTable);
		scrollPane.getViewport().setOpaque( true);
		scrollPane.getViewport().setBackground( SystemColor.text);

		panel.add( scrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_kind_components(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		setup_kindLabel( panel);

		panel.add( Box.createHorizontalStrut( 5));

		setup_kindComboBox( panel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_kindLabel(JPanel parent) {
		_kindLabel = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.variable.kind.label"));
		_kindLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		parent.add( _kindLabel);
	}

	/**
	 * @param parent
	 */
	private void setup_kindComboBox(JPanel parent) {
		Vector<String> items = new Vector<String>();
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.list"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.map"));
		if ( Environment.get_instance().is_exchange_algebra_enable())
			items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"));
		_kindComboBox = new JComboBox( items);
		_kindComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = ( String)_kindComboBox.getSelectedItem();
				Set<String> kinds = _panelBaseMap.keySet();
				for ( String kind:kinds) {
					PanelBase panelBase = _panelBaseMap.get( kind);
					panelBase.clear();
					panelBase.activate( kind.equals( item));
					//panelBase.setVisible( kind.equals( item));
				}
				revalidate();
				repaint();
			}
		});
		parent.add( _kindComboBox);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_components(JPanel parent) {
		_setPanel = new SetPanel( _playerBase, _propertyPageMap, _variableTable,
			_colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection")), _owner, _parent);
		if ( !_setPanel.setup())
			return false;

		_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"), _setPanel);
		parent.add( _setPanel);


		_listPanel = new ListPanel( _playerBase, _propertyPageMap, _variableTable,
			_colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.list")), _owner, _parent);
		if ( !_listPanel.setup())
			return false;

		_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.list"), _listPanel);
		parent.add( _listPanel);


		_mapPanel = new MapPanel( _playerBase, _propertyPageMap, _variableTable,
			_colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.map")), _owner, _parent);
		if ( !_mapPanel.setup())
			return false;

		_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.map"), _mapPanel);
		parent.add( _mapPanel);


		if ( Environment.get_instance().is_exchange_algebra_enable()) {
			_exchangeAlgebraPanel = new ExchangeAlgebraPanel( _playerBase, _propertyPageMap, _variableTable,
				_colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra")), _owner, _parent);
			if ( !_exchangeAlgebraPanel.setup())
				return false;

			_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"), _exchangeAlgebraPanel);
			parent.add( _exchangeAlgebraPanel);
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#changeSelection(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public void changeSelection(ObjectBase objectBase) {
		if ( null != objectBase) {
			if ( objectBase instanceof VariableObject && ( ( VariableObject)objectBase)._kind.equals( "collection"))
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"));
			else if ( objectBase instanceof VariableObject && ( ( VariableObject)objectBase)._kind.equals( "list"))
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.list"));
			else if ( objectBase instanceof MapObject)
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.map"));
			else if ( objectBase instanceof ExchangeAlgebraObject)
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"));
			else
				return;
		}

		PanelBase panelBase = _panelBaseMap.get( _kindComboBox.getSelectedItem());
		if ( null == panelBase)
			return;

		panelBase.update( objectBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#adjust()
	 */
	protected void adjust() {
		int width = _kindLabel.getPreferredSize().width;
		Collection<PanelBase> panelBases = _panelBaseMap.values();
		for ( PanelBase panelBase:panelBases)
			width = panelBase.get_label_width( width);

		int button_width = 0;
		for ( PanelBase panelBase:panelBases)
			button_width = panelBase.adjust( width);

		_kindLabel.setPreferredSize( new Dimension( width, _kindLabel.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#on_setup_completed()
	 */
	public void on_setup_completed() {
		if ( _playerBase.is_multi()) {
			_variableTable.setEnabled( false);
			_kindLabel.setEnabled( false);
			_kindComboBox.setEnabled( false);
			Collection<PanelBase> panelBases = _panelBaseMap.values();
			for ( PanelBase panelBase:panelBases)
				panelBase.setEnabled( false);
		}

		Collection<PanelBase> panelBases = _panelBaseMap.values();
		for ( PanelBase panelBase:panelBases)
			panelBase.on_setup_completed();

		_listPanel.setVisible( false);
		_mapPanel.setVisible( false);
		if ( Environment.get_instance().is_exchange_algebra_enable())
			_exchangeAlgebraPanel.setVisible( false);

		_variableTable.select_at_first();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#on_ok()
	 */
	public boolean on_ok() {
		if ( isVisible() && !confirm( false))
			return false;

		_variableTable.on_ok();

		if ( _playerBase instanceof AgentObject) {
			Observer.get_instance().on_update_agent_object( "collection");
			Observer.get_instance().on_update_agent_object( "list");
			Observer.get_instance().on_update_agent_object( "map");
			Observer.get_instance().on_update_agent_object( "exchange algebra");
		} else if ( _playerBase instanceof SpotObject) {
			Observer.get_instance().on_update_spot_object( "collection");
			Observer.get_instance().on_update_spot_object( "list");
			Observer.get_instance().on_update_spot_object( "map");
			Observer.get_instance().on_update_spot_object( "exchange algebra");
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#confirm(boolean)
	 */
	public boolean confirm(boolean fromTree) {
		if ( !isVisible())
			return true;

		//if ( 0 == _variableTable.getRowCount())
		//	return true;

		if ( !_setPanel.confirm( fromTree))
			return false;

		if ( !_listPanel.confirm( fromTree))
			return false;

		if ( !_mapPanel.confirm( fromTree))
			return false;

		if ( Environment.get_instance().is_exchange_algebra_enable()) {
			if ( !_exchangeAlgebraPanel.confirm( fromTree))
				return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#confirm(int, soars.application.visualshell.object.player.base.object.base.ObjectBase, soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public ObjectBase confirm(int row, ObjectBase targetObjectBase, ObjectBase selectedObjectBase) {
		ObjectBase objectBase = _setPanel.confirm( row, targetObjectBase, selectedObjectBase);
		if ( null != objectBase)
			return objectBase;

		objectBase = _listPanel.confirm( row, targetObjectBase, selectedObjectBase);
		if ( null != objectBase)
			return objectBase;

		objectBase = _mapPanel.confirm( row, targetObjectBase, selectedObjectBase);
		if ( null != objectBase)
			return objectBase;

		if ( Environment.get_instance().is_exchange_algebra_enable()) {
			objectBase = _exchangeAlgebraPanel.confirm( row, targetObjectBase, selectedObjectBase);
			if ( null != objectBase)
				return objectBase;
		}

		return null;
	}
}
