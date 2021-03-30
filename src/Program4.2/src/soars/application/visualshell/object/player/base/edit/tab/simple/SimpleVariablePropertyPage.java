/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab.simple;

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

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.agent.AgentObject;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PanelBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.edit.tab.simple.panel.keyword.KeywordPanel;
import soars.application.visualshell.object.player.base.edit.tab.simple.panel.number.NumberVariablePanel;
import soars.application.visualshell.object.player.base.edit.tab.simple.panel.probability.ProbabilityVariablePanel;
import soars.application.visualshell.object.player.base.edit.tab.simple.panel.role.RoleVariablePanel;
import soars.application.visualshell.object.player.base.edit.tab.simple.panel.spot.SpotVariablePanel;
import soars.application.visualshell.object.player.base.edit.tab.simple.panel.time.TimeVariablePanel;
import soars.application.visualshell.object.player.base.object.base.ObjectBase;
import soars.application.visualshell.object.player.base.object.base.SimpleVariableObject;
import soars.application.visualshell.object.player.base.object.keyword.KeywordObject;
import soars.application.visualshell.object.player.base.object.number.NumberObject;
import soars.application.visualshell.object.player.base.object.probability.ProbabilityObject;
import soars.application.visualshell.object.player.base.object.role.RoleVariableObject;
import soars.application.visualshell.object.player.base.object.spot.SpotVariableObject;
import soars.application.visualshell.object.player.base.object.time.TimeVariableObject;
import soars.application.visualshell.object.player.spot.SpotObject;
import soars.application.visualshell.observer.Observer;

/**
 * @author kurata
 *
 */
public class SimpleVariablePropertyPage extends PropertyPageBase {

	/**
	 * 
	 */
	private SimpleVariableTable _simpleVariableTable = null; 

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
	private ProbabilityVariablePanel _probabilityVariablePanel = null;

	/**
	 * 
	 */
	private KeywordPanel _keywordPanel = null;

	/**
	 * 
	 */
	private NumberVariablePanel _numberVariablePanel = null;

	/**
	 * 
	 */
	private RoleVariablePanel _roleVariablePanel = null;

	/**
	 * 
	 */
	private TimeVariablePanel _timeVariablePanel = null;

	/**
	 * 
	 */
	private SpotVariablePanel _spotVariablePanel = null;

	/**
	 * @param title
	 * @param playerBase
	 * @param propertyPageMap
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public SimpleVariablePropertyPage(String title, PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap, int index, Frame owner, Component parent) {
		super(title, playerBase, propertyPageMap, index, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#contains(java.lang.String)
	 */
	public boolean contains(String name) {
		return _simpleVariableTable.contains( name);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#contains(java.lang.String, java.lang.String)
	 */
	public boolean contains(String name, String number) {
		return _simpleVariableTable.contains(name, number);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#can_adjust_name(java.lang.String, java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_name(String type, String head_name, Vector ranges) {
		return _simpleVariableTable.can_adjust_name( type, head_name, ranges);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#can_adjust_name(java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_name(String type, String head_name, Vector ranges, String new_head_name, Vector new_ranges) {
		return _simpleVariableTable.can_adjust_name( type, head_name, ranges, new_head_name, new_ranges);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#update_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public void update_name_and_number(String type, String new_name, String original_name, String head_name, Vector ranges, String new_head_name, Vector new_ranges) {
		_simpleVariableTable.update_name_and_number( type, new_name, original_name, head_name, ranges, new_head_name, new_ranges);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#get(java.lang.String)
	 */
	public String[] get(String kind) {
		return _simpleVariableTable.get( kind);
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

		create_simple_variable_table();

		if ( !setup_simple_variable_table( center_panel))
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
	private void create_simple_variable_table() {
		_simpleVariableTable = new SimpleVariableTable( _playerBase, _propertyPageMap, this, _owner, _parent);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_simple_variable_table(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_simpleVariableTable.setup())
			return false;

		if ( _playerBase.is_multi())
			_simpleVariableTable.setEnabled( false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _simpleVariableTable);
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
		_kindLabel = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.kind.label"));
		_kindLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		parent.add( _kindLabel);
	}

	/**
	 * @param parent
	 */
	private void setup_kindComboBox(JPanel parent) {
		Vector<String> items = new Vector<String>();
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"));
		if ( _playerBase instanceof AgentObject) {
			items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable"));
		}
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"));
		_kindComboBox = new JComboBox( items);
		_kindComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = ( String)_kindComboBox.getSelectedItem();
				Set<String> kinds = _panelBaseMap.keySet();
				for ( String kind:kinds) {
					PanelBase panelBase = _panelBaseMap.get( kind);
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
		_probabilityVariablePanel = new ProbabilityVariablePanel( _playerBase, _propertyPageMap, _simpleVariableTable,
			_colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability")), _owner, _parent);
		if ( !_probabilityVariablePanel.setup())
			return false;

		_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"), _probabilityVariablePanel);
		parent.add( _probabilityVariablePanel);


		_keywordPanel = new KeywordPanel( _playerBase, _propertyPageMap, _simpleVariableTable,
			_colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword")), _owner, _parent);
		if ( !_keywordPanel.setup())
			return false;

		_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"), _keywordPanel);
		parent.add( _keywordPanel);


		_numberVariablePanel = new NumberVariablePanel( _playerBase, _propertyPageMap, _simpleVariableTable,
			_colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object")), _owner, _parent);
		if ( !_numberVariablePanel.setup())
			return false;

		_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"), _numberVariablePanel);
		parent.add( _numberVariablePanel);


		if ( _playerBase instanceof AgentObject) {
			_roleVariablePanel = new RoleVariablePanel( _playerBase, _propertyPageMap, _simpleVariableTable,
				_colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable")), _owner, _parent);
			if ( !_roleVariablePanel.setup())
				return false;

			_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable"), _roleVariablePanel);
			parent.add( _roleVariablePanel);
		}


		_timeVariablePanel = new TimeVariablePanel( _playerBase, _propertyPageMap, _simpleVariableTable,
			_colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable")), _owner, _parent);
		if ( !_timeVariablePanel.setup())
			return false;

		_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"), _timeVariablePanel);
		parent.add( _timeVariablePanel);


		_spotVariablePanel = new SpotVariablePanel( _playerBase, _propertyPageMap, _simpleVariableTable,
			_colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable")), _owner, _parent);
		if ( !_spotVariablePanel.setup())
			return false;

		_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"), _spotVariablePanel);
		parent.add( _spotVariablePanel);


		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#changeSelection(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public void changeSelection(ObjectBase objectBase) {
		if ( null != objectBase) {
			if ( objectBase instanceof ProbabilityObject)
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"));
			else if ( objectBase instanceof KeywordObject)
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"));
			else if ( objectBase instanceof NumberObject)
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"));
			else if ( objectBase instanceof RoleVariableObject)
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable"));
			else if ( objectBase instanceof TimeVariableObject)
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"));
			else if ( objectBase instanceof SpotVariableObject)
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"));
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
		//_kindComboBox.setPreferredSize( new Dimension( _simpleVariableTable.getPreferredSize().width - width - 5 - button_width, _kindComboBox.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#on_setup_completed()
	 */
	public void on_setup_completed() {
		if ( _playerBase.is_multi()) {
			_simpleVariableTable.setEnabled( false);
			_kindLabel.setEnabled( false);
			_kindComboBox.setEnabled( false);
			Collection<PanelBase> panelBases = _panelBaseMap.values();
			for ( PanelBase panelBase:panelBases)
				panelBase.setEnabled( false);
		}

		_keywordPanel.setVisible( false);
		_numberVariablePanel.setVisible( false);
		if ( _playerBase instanceof AgentObject) {
			_roleVariablePanel.setVisible( false);
		}
		_timeVariablePanel.setVisible( false);
		_spotVariablePanel.setVisible( false);
		_simpleVariableTable.on_setup_completed();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#on_ok()
	 */
	public boolean on_ok() {
		if ( isVisible() && !confirm( false))
			return false;

		_simpleVariableTable.on_ok();

		if ( _playerBase instanceof AgentObject) {
			Observer.get_instance().on_update_agent_object( "probability");
			Observer.get_instance().on_update_agent_object( "keyword");
			Observer.get_instance().on_update_agent_object( "number object");
			Observer.get_instance().on_update_agent_object( "role variable");
			Observer.get_instance().on_update_agent_object( "time variable");
			Observer.get_instance().on_update_agent_object( "spot variable");
		} else if ( _playerBase instanceof SpotObject) {
			Observer.get_instance().on_update_spot_object( "probability");
			Observer.get_instance().on_update_spot_object( "keyword");
			Observer.get_instance().on_update_spot_object( "number object");
			Observer.get_instance().on_update_spot_object( "role variable");
			Observer.get_instance().on_update_spot_object( "time variable");
			Observer.get_instance().on_update_spot_object( "spot variable");
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#confirm(boolean)
	 */
	public boolean confirm(boolean fromTree) {
		if ( !isVisible())
			return true;

		//if ( 0 == _simpleVariableTable.getRowCount())
		//	return true;

		if ( !_probabilityVariablePanel.confirm( fromTree))
			return false;

		if ( !_keywordPanel.confirm( fromTree))
			return false;

		if ( !_numberVariablePanel.confirm( fromTree))
			return false;

		if ( _playerBase instanceof AgentObject) {
			if ( !_roleVariablePanel.confirm( fromTree))
				return false;
		}

		if ( !_timeVariablePanel.confirm( fromTree))
			return false;

		if ( !_spotVariablePanel.confirm( fromTree))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#confirm(int, soars.application.visualshell.object.player.base.object.base.ObjectBase, soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	public ObjectBase confirm(int row, ObjectBase targetObjectBase, ObjectBase selectedObjectBase) {
		ObjectBase objectBase = _probabilityVariablePanel.confirm( row, ( SimpleVariableObject)targetObjectBase, ( SimpleVariableObject)selectedObjectBase);
		if ( null != objectBase)
			return objectBase;

		objectBase = _keywordPanel.confirm( row, targetObjectBase, selectedObjectBase);
		if ( null != objectBase)
			return objectBase;

		objectBase = _numberVariablePanel.confirm( row, targetObjectBase, selectedObjectBase);
		if ( null != objectBase)
			return objectBase;

		if ( _playerBase instanceof AgentObject) {
			objectBase = _roleVariablePanel.confirm( row, targetObjectBase, selectedObjectBase);
			if ( null != objectBase)
				return objectBase;
		}

		objectBase = _timeVariablePanel.confirm( row, targetObjectBase, selectedObjectBase);
		if ( null != objectBase)
			return objectBase;

		objectBase = _spotVariablePanel.confirm( row, targetObjectBase, selectedObjectBase);
		if ( null != objectBase)
			return objectBase;

		return null;
	}
}
