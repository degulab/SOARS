/*
 * Created on 2005/09/16
 */
package soars.application.visualshell.object.log.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.log.LogManager;
import soars.application.visualshell.object.log.edit.option.LogOptionList;

/**
 * The tab component to edit the log options.
 * @author kurata / SOARS project
 */
public class LogPropertyPage2 extends LogPropertyPageBase {

	/**
	 * 
	 */
	private LogOptionList _agentCollectionList = null; 

	/**
	 * 
	 */
	private LogOptionList _agentListList = null; 

	/**
	 * 
	 */
	private LogOptionList _agentMapList = null; 

	/**
	 * 
	 */
	private LogOptionList _agentSpotVariableList = null; 

	/**
	 * 
	 */
	private LogOptionList _agentExchangeAlgebraList = null;

	/**
	 * 
	 */
	private LogOptionList _spotCollectionList = null; 

	/**
	 * 
	 */
	private LogOptionList _spotListList = null; 

	/**
	 * 
	 */
	private LogOptionList _spotSpotVariableList = null; 

	/**
	 * 
	 */
	private LogOptionList _spotMapList = null; 

	/**
	 * 
	 */
	private LogOptionList _spotExchangeAlgebraList = null;

	/**
	 * Creates this object.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public LogPropertyPage2(Frame owner, Component parent) {
		super(owner, parent);
		_title = ( ResourceManager.get_instance().get( "edit.log.dialog.title") + 2);
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

		JPanel base_panel = new JPanel();
		base_panel.setLayout( new BoxLayout( base_panel, BoxLayout.X_AXIS));

		base_panel.add( Box.createHorizontalStrut( 5));

		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 4));

		JPanel partial_panel = get_collection_panel();
		if ( null == partial_panel)
			return false;

		panel.add( partial_panel);


		partial_panel = get_list_panel();
		if ( null == partial_panel)
			return false;

		panel.add( partial_panel);


		partial_panel = get_spot_variable_panel();
		if ( null == partial_panel)
			return false;

		panel.add( partial_panel);


		partial_panel = get_map_panel();
		if ( null == partial_panel)
			return false;

		panel.add( partial_panel);


		if ( Environment.get_instance().is_exchange_algebra_enable()) {
			partial_panel = get_exchange_algebra_panel();
			if ( null == partial_panel)
				return false;

			panel.add( partial_panel);
		}


		base_panel.add( panel);
		center_panel.add( base_panel);

		add( center_panel);



		JPanel south_panel = new JPanel();
		south_panel.setLayout( new BoxLayout( south_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( south_panel);

		add( south_panel, "South");



		return true;
	}

	/**
	 * @return
	 */
	private JPanel get_collection_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel patial_panel = get_agent_collection_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		patial_panel = get_spot_collection_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_list_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel patial_panel = get_agent_list_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		patial_panel = get_spot_list_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_variable_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel patial_panel = get_agent_spot_variable_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		patial_panel = get_spot_spot_variable_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_map_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel patial_panel = get_agent_map_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		patial_panel = get_spot_map_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_exchange_algebra_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel patial_panel = get_agent_exchange_algebra_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		patial_panel = get_spot_exchange_algebra_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_collection_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.collection.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_agentCollectionList = setup_list( LogManager.get_instance().get( "agent").get( "collection"), center_panel);
		if ( null == _agentCollectionList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_collection_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.collection.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_spotCollectionList = setup_list( LogManager.get_instance().get( "spot").get( "collection"), center_panel);
		if ( null == _spotCollectionList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_list_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.list.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_agentListList = setup_list( LogManager.get_instance().get( "agent").get( "list"), center_panel);
		if ( null == _agentListList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_list_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.list.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_spotListList = setup_list( LogManager.get_instance().get( "spot").get( "list"), center_panel);
		if ( null == _spotListList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_map_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.map.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_agentMapList = setup_list( LogManager.get_instance().get( "agent").get( "map"), center_panel);
		if ( null == _agentMapList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_map_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.map.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_spotMapList = setup_list( LogManager.get_instance().get( "spot").get( "map"), center_panel);
		if ( null == _spotMapList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_spot_variable_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.spot.variable.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_agentSpotVariableList = setup_list( LogManager.get_instance().get( "agent").get( "spot variable"), center_panel);
		if ( null == _agentSpotVariableList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_spot_variable_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.spot.variable.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_spotSpotVariableList = setup_list( LogManager.get_instance().get( "spot").get( "spot variable"), center_panel);
		if ( null == _spotSpotVariableList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_exchange_algebra_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.exchange.algebra.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_agentExchangeAlgebraList = setup_list( LogManager.get_instance().get( "agent").get( "exchange algebra"), center_panel);
		if ( null == _agentExchangeAlgebraList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_exchange_algebra_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.exchange.algebra.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_spotExchangeAlgebraList = setup_list( LogManager.get_instance().get( "spot").get( "exchange algebra"), center_panel);
		if ( null == _spotExchangeAlgebraList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * Invoked when this objet has been initialized.
	 */
	public void on_setup_completed() {
		_agentCollectionList.requestFocusInWindow();
	}

	/**
	 * Returns true for updating successfully.
	 * @return true for updating successfully
	 */
	public boolean on_ok() {
		_agentCollectionList.on_ok( LogManager.get_instance().get( "agent").get( "collection"));
		_agentListList.on_ok( LogManager.get_instance().get( "agent").get( "list"));
		_agentMapList.on_ok( LogManager.get_instance().get( "agent").get( "map"));
		_agentSpotVariableList.on_ok( LogManager.get_instance().get( "agent").get( "spot variable"));
		_spotCollectionList.on_ok( LogManager.get_instance().get( "spot").get( "collection"));
		_spotListList.on_ok( LogManager.get_instance().get( "spot").get( "list"));
		_spotMapList.on_ok( LogManager.get_instance().get( "spot").get( "map"));
		_spotSpotVariableList.on_ok( LogManager.get_instance().get( "spot").get( "spot variable"));

		if ( Environment.get_instance().is_exchange_algebra_enable()) {
			_agentExchangeAlgebraList.on_ok( LogManager.get_instance().get( "agent").get( "exchange algebra"));
			_spotExchangeAlgebraList.on_ok( LogManager.get_instance().get( "spot").get( "exchange algebra"));
		}

		return true;
	}
}
