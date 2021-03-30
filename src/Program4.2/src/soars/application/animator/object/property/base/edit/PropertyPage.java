/*
 * 2005/03/07
 */
package soars.application.animator.object.property.base.edit;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

import soars.application.animator.object.property.base.PropertyManager;
import soars.common.utility.swing.tab.TabbedPage;

/**
 * The component to show the PropertyTable.
 * @author kurata / SOARS project
 */
public class PropertyPage extends TabbedPage {

	/**
	 * 
	 */
	private String _name = "";

	/**
	 * 
	 */
	private PropertyManager _propertyManager = null;

	/**
	 * 
	 */
	private String _open_directory_key = "";

	/**
	 * 
	 */
	private JScrollPane _scrollPane = null;

	/**
	 * 
	 */
	private PropertyTable _propertyTable = null;

	/**
	 * 
	 */
	private Frame _owner = null;

	/**
	 * 
	 */
	private Component _parent = null;

	/**
	 * Creates this component with the specified data.
	 * @param name the name of the property
	 * @param propertyManager the Property hashtable(name(String) - value(String) - PropertyBase)
	 * @param open_directory_key the key mapped to the default directory for the file chooser dialog
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public PropertyPage(String name, PropertyManager propertyManager, String open_directory_key, Frame owner, Component parent) {
		super();
		_name = name;
		_propertyManager = propertyManager;
		_open_directory_key = open_directory_key;
		_owner = owner;
		_parent = parent;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.tab.TabbedPage#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));


		_propertyTable = new PropertyTable( _open_directory_key, _owner, _parent);

		TreeMap property_map = ( TreeMap)_propertyManager.get( _name);
		if ( null == property_map)
			return false;

		if ( !_propertyTable.setup( property_map))
			return false;

		_scrollPane = new JScrollPane();
		_scrollPane.getViewport().setView( _propertyTable);
		_scrollPane.setPreferredSize( new Dimension( 800, 200));

		add( _scrollPane);

		return true;
	}

	/**
	 * 
	 */
	protected void on_setup_completed() {
	}

	/**
	 * @param graphics2D
	 */
	protected void on_ok(Graphics2D graphics2D) {
		TreeMap property_map = ( TreeMap)_propertyManager.get( _name);
		_propertyTable.on_ok( property_map, graphics2D);
	}
}
