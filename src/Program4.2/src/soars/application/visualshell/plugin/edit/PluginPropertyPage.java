/*
 * Created on 2006/06/23
 */
package soars.application.visualshell.plugin.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.tab.TabbedPage;

/**
 * @author kurata
 */
public class PluginPropertyPage extends TabbedPage {

	/**
	 * 
	 */
	public String _title = ResourceManager.get_instance().get( "edit.plugin.dialog.title");

	/**
	 * 
	 */
	private PluginTable _pluginTable = null;

	/**
	 * 
	 */
	private JScrollPane _pluginTableScrollPane = null;

	/**
	 * 
	 */
	private boolean _at_first = true;

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * @param owner
	 * @param parent
	 */
	public PluginPropertyPage(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.tab.TabbedPage#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;



		setLayout( new BorderLayout());



		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		create_plugin_table();

		if ( !setup_plugin_table( center_panel))
			return false;

		add( center_panel);



		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( _at_first) {
					_pluginTable.setup_column_width( _pluginTableScrollPane.getWidth());
					_at_first = false;
				} else {
					_pluginTable.adjust_column_width( _pluginTableScrollPane.getWidth());
				}
			}
		});



		return true;
	}

	/**
	 * 
	 */
	private void create_plugin_table() {
		_pluginTable = new PluginTable( _owner, _parent);
	}

	/**
	 * @param center_panel
	 * @return
	 */
	private boolean setup_plugin_table(JPanel center_panel) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel table_panel = new JPanel();
		table_panel.setLayout( new GridLayout( 1, 1));

		if ( !_pluginTable.setup())
			return false;

		_pluginTableScrollPane = new JScrollPane();
		_pluginTableScrollPane.getViewport().setView( _pluginTable);

		//_pluginTable.adjust_column_width( _pluginTableScrollPane.getPreferredSize().width);

		table_panel.add( _pluginTableScrollPane);
		panel.add( table_panel);
		panel.add( Box.createHorizontalStrut( 5));

		center_panel.add( panel);

		return true;
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_pluginTable.requestFocusInWindow();
	}

	/**
	 * @return
	 */
	public boolean on_ok() {
		_pluginTable.on_ok();
		return true;
	}
}
