/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.status;

import java.awt.Component;
import java.awt.Frame;
import java.util.Vector;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import soars.application.visualshell.common.swing.TableBase;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;
import soars.common.utility.swing.table.base.StandardTableRowRenderer;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class StatusTable extends TableBase {

	/**
	 * @param owner
	 * @param parent
	 */
	public StatusTable(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !super.setup( false))
			return false;

		setAutoResizeMode( AUTO_RESIZE_OFF);
		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);

		getTableHeader().setDefaultRenderer( new StandardTableHeaderRenderer());

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 2);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.rule.dialog.status.table.header.name"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.rule.dialog.status.table.header.initial.value"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 100);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 2000);

		for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new StandardTableRowRenderer());
		}

		return true;
	}

	/**
	 * @param type
	 * @param name
	 * @param object
	 */
	public void update(String type, String name, String object) {
		clear();

		if ( null == name)
			return;

		PlayerBase playerBase = null;
		if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.type.agent")))
			playerBase = LayerManager.get_instance().get_agent( name);
		else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.type.spot")))
			playerBase = LayerManager.get_instance().get_spot( name);
		else
			return;

		if ( null == playerBase)
			return;

		Vector object_name_vector = new Vector();
		if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.probability.variable")))
			playerBase.get_object_names( "probability", object_name_vector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.collection.variable")))
			playerBase.get_object_names( "collection", object_name_vector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.list.variable")))
			playerBase.get_object_names( "list", object_name_vector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.map.variable")))
			playerBase.get_object_names( "map", object_name_vector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.keyword")))
			playerBase.get_object_names( "keyword", object_name_vector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.integer.number.variable")))
			playerBase.get_number_object_names( "integer", object_name_vector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.real.number.variable")))
			playerBase.get_number_object_names( "real number", object_name_vector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.role.variable")))
			playerBase.get_object_names( "role variable", object_name_vector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.time.variable")))
			playerBase.get_object_names( "time variable", object_name_vector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.spot.variable")))
			playerBase.get_object_names( "spot variable", object_name_vector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.class.variable")))
			playerBase.get_object_names( "class variable", object_name_vector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.file")))
			playerBase.get_object_names( "file", object_name_vector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.exchange.algebra")))
			playerBase.get_object_names( "exchange algebra", object_name_vector);
		else
			return;

		if ( object_name_vector.isEmpty())
			return;

		String[] object_names = Tool.quick_sort_string( object_name_vector, true, false);
		if ( null == object_names)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		Object[] objects = new Object[ 2];
		for ( int i = 0; i < object_names.length; ++i) {
			objects[ 0] = object_names[ i];
			objects[ 1] = get_initial_values( playerBase, object_names[ i], object);
			defaultTableModel.addRow( objects);
		}
	}

	/**
	 * @param playerBase
	 * @param name
	 * @param object
	 * @return
	 */
	private String get_initial_values(PlayerBase playerBase, String name, String object) {
		Vector initial_values = new Vector();
		if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.probability.variable")))
			playerBase.get_object_initial_values( "probability", name, initial_values);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.collection.variable")))
			playerBase.get_object_initial_values( "collection", name, initial_values);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.list.variable")))
			playerBase.get_object_initial_values( "list", name, initial_values);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.map.variable")))
			playerBase.get_object_initial_values( "map", name, initial_values);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.keyword")))
			playerBase.get_object_initial_values( "keyword", name, initial_values);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.integer.number.variable")))
			playerBase.get_object_initial_values( "integer", name, initial_values);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.real.number.variable")))
			playerBase.get_object_initial_values( "real number", name, initial_values);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.role.variable")))
			playerBase.get_object_initial_values( "role variable", name, initial_values);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.time.variable")))
			playerBase.get_object_initial_values( "time variable", name, initial_values);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.spot.variable")))
			playerBase.get_object_initial_values( "spot variable", name, initial_values);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.file")))
			playerBase.get_object_initial_values( "file", name, initial_values);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.exchange.algebra")))
			playerBase.get_object_initial_values( "exchange algebra", name, initial_values);
		else
			return "";

		if ( initial_values.isEmpty())
			return "";

		String result = "";
		for ( int i = 0; i < initial_values.size(); ++i) {
			String initial_value = ( String)initial_values.get( i);
			result += ( ( ( 0 == i) ? "" : ", ") + initial_value);
		}

		return result;
	}

	/**
	 * 
	 */
	private void clear() {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		while ( 0 < defaultTableModel.getRowCount())
			defaultTableModel.removeRow( 0);
	}
}
