/*
 * 2005/06/21
 */
package soars.application.visualshell.object.log.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.log.LogManager;
import soars.application.visualshell.object.log.edit.option.LogOptionList;

/**
 * The tab component to edit the log options.
 * @author kurata / SOARS project
 */
public class LogPropertyPage1 extends LogPropertyPageBase {

	/**
	 * 
	 */
	private LogOptionList _agentKeywordList = null;

	/**
	 * 
	 */
	private LogOptionList _agentNumberObjectList = null;

	/**
	 * 
	 */
	private LogOptionList _agentProbabilityList = null;

	/**
	 * 
	 */
	private LogOptionList _agentTimeVariableList = null;

	/**
	 * 
	 */
	private LogOptionList _agentRoleVariableList = null;

	/**
	 * 
	 */
	private LogOptionList _spotKeywordList = null;

	/**
	 * 
	 */
	private LogOptionList _spotNumberObjectList = null;

	/**
	 * 
	 */
	private LogOptionList _spotProbabilityList = null;

	/**
	 * 
	 */
	private LogOptionList _spotTimeVariableList = null;

	/**
	 * Creates this object.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public LogPropertyPage1(Frame owner, Component parent) {
		super(owner, parent);
		_title = ( ResourceManager.get_instance().get( "edit.log.dialog.title") + 1);
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
		panel.setLayout( new GridLayout( 1, 3));

		JPanel partial_panel = get_keyword_panel();
		if ( null == partial_panel)
			return false;

		panel.add( partial_panel);


		partial_panel = get_number_object_panel();
		if ( null == partial_panel)
			return false;

		panel.add( partial_panel);


		partial_panel = get_probability_panel();
		if ( null == partial_panel)
			return false;

		panel.add( partial_panel);


		partial_panel = get_time_variable_panel();
		if ( null == partial_panel)
			return false;

		panel.add( partial_panel);


		partial_panel = get_role_variable_panel();
		if ( null == partial_panel)
			return false;

		panel.add( partial_panel);


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
	private JPanel get_keyword_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel patial_panel = get_agent_keyword_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		patial_panel = get_spot_keyword_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_number_object_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel patial_panel = get_agent_number_object_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		patial_panel = get_spot_number_object_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_probability_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel patial_panel = get_agent_probability_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		patial_panel = get_spot_probability_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_time_variable_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel patial_panel = get_agent_time_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		patial_panel = get_spot_time_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_role_variable_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel patial_panel = get_agent_role_variable_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		patial_panel = get_spot_role_variable_panel();
		if ( null == patial_panel)
			return null;

		panel.add( patial_panel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_keyword_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.keyword.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_agentKeywordList = setup_list( LogManager.get_instance().get( "agent").get( "keyword"), center_panel);
		if ( null == _agentKeywordList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_keyword_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.keyword.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_spotKeywordList = setup_list( LogManager.get_instance().get( "spot").get( "keyword"), center_panel);
		if ( null == _spotKeywordList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_number_object_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.number.object.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_agentNumberObjectList = setup_list( LogManager.get_instance().get( "agent").get( "number object"), center_panel);
		if ( null == _agentNumberObjectList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_number_object_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.number.object.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_spotNumberObjectList = setup_list( LogManager.get_instance().get( "spot").get( "number object"), center_panel);
		if ( null == _spotNumberObjectList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_probability_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.probability.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_agentProbabilityList = setup_list( LogManager.get_instance().get( "agent").get( "probability"), center_panel);
		if ( null == _agentProbabilityList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_probability_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.probability.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_spotProbabilityList = setup_list( LogManager.get_instance().get( "spot").get( "probability"), center_panel);
		if ( null == _spotProbabilityList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_time_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.time.variable.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_agentTimeVariableList = setup_list( LogManager.get_instance().get( "agent").get( "time variable"), center_panel);
		if ( null == _agentTimeVariableList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_time_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.time.variable.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_spotTimeVariableList = setup_list( LogManager.get_instance().get( "spot").get( "time variable"), center_panel);
		if ( null == _spotTimeVariableList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_role_variable_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.role.variable.name"), north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		_agentRoleVariableList = setup_list( LogManager.get_instance().get( "agent").get( "role variable"), center_panel);
		if ( null == _agentRoleVariableList)
			return null;

		panel.add( center_panel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_role_variable_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		setup_label( "", north_panel);

		panel.add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		panel.add( center_panel);



		return panel;
	}

	/**
	 * Invoked when this objet has been initialized.
	 */
	public void on_setup_completed() {
	}

	/**
	 * Returns true for updating successfully.
	 * @return true for updating successfully
	 */
	public boolean on_ok() {
		_agentKeywordList.on_ok( LogManager.get_instance().get( "agent").get( "keyword"));
		_agentNumberObjectList.on_ok( LogManager.get_instance().get( "agent").get( "number object"));
		_agentProbabilityList.on_ok( LogManager.get_instance().get( "agent").get( "probability"));
		_agentTimeVariableList.on_ok( LogManager.get_instance().get( "agent").get( "time variable"));
		_agentRoleVariableList.on_ok( LogManager.get_instance().get( "agent").get( "role variable"));
		_spotKeywordList.on_ok( LogManager.get_instance().get( "spot").get( "keyword"));
		_spotNumberObjectList.on_ok( LogManager.get_instance().get( "spot").get( "number object"));
		_spotProbabilityList.on_ok( LogManager.get_instance().get( "spot").get( "probability"));
		_spotTimeVariableList.on_ok( LogManager.get_instance().get( "spot").get( "time variable"));
		return true;
	}
}
