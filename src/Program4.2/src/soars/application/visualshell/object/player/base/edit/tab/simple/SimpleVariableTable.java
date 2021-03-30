/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab.simple;

import java.awt.Component;
import java.awt.Frame;
import java.util.Map;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase;
import soars.application.visualshell.object.player.base.object.base.ObjectBase;
import soars.application.visualshell.object.player.base.object.keyword.KeywordObject;
import soars.application.visualshell.object.player.base.object.number.NumberObject;
import soars.application.visualshell.object.player.base.object.probability.ProbabilityObject;
import soars.application.visualshell.object.player.base.object.role.RoleVariableObject;
import soars.application.visualshell.object.player.base.object.spot.SpotVariableObject;
import soars.application.visualshell.object.player.base.object.time.TimeVariableObject;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;
import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 *
 */
public class SimpleVariableTable extends VariableTableBase {

	/**
	 * @param playerBase
	 * @param propertyPageMap
	 * @param propertyPageBase
	 * @param owner
	 * @param parent
	 */
	public SimpleVariableTable(PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap, PropertyPageBase propertyPageBase, Frame owner, Component parent) {
		super("simple variable", playerBase, propertyPageMap, propertyPageBase, 5, owner, parent);
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !super.setup(!_playerBase.is_multi()))
			return false;


		setAutoResizeMode( AUTO_RESIZE_OFF);


		JTableHeader tableHeader = getTableHeader();
		StandardTableHeaderRenderer standardTableHeaderRenderer = new StandardTableHeaderRenderer();
		if ( _playerBase.is_multi())
			standardTableHeaderRenderer.setEnabled( false);

		tableHeader.setDefaultRenderer( standardTableHeaderRenderer);

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( _columns);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.table.header.kind"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.table.header.name"));
		defaultTableColumnModel.getColumn( 2).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.table.header.initial.value"));
		defaultTableColumnModel.getColumn( 3).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.table.header.type"));
		defaultTableColumnModel.getColumn( 4).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.table.header.comment"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 2).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 3).setPreferredWidth( 100);
		defaultTableColumnModel.getColumn( 4).setPreferredWidth( 2000);

		for ( int i = 0; i < _columns; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new SimpleVariableTableRowRenderer());
		}

		if ( !_playerBase.is_multi()) {
			if ( !setup( _playerBase._objectMapMap.get( "probability"), defaultTableModel))
				return false;
			if ( !setup( _playerBase._objectMapMap.get( "keyword"), defaultTableModel))
				return false;
			if ( !setup( _playerBase._objectMapMap.get( "number object"), defaultTableModel))
				return false;
			if ( !setup( _playerBase._objectMapMap.get( "role variable"), defaultTableModel))
				return false;
			if ( !setup( _playerBase._objectMapMap.get( "time variable"), defaultTableModel))
				return false;
			if ( !setup( _playerBase._objectMapMap.get( "spot variable"), defaultTableModel))
				return false;

			if ( 0 < defaultTableModel.getRowCount())
				setRowSelectionInterval( 0, 0);
		}

		setup_popup_menu();

		return true;
	}

	/**
	 * @param objectBase0
	 * @param objectBase1
	 * @return
	 */
	protected int compare(ObjectBase objectBase0, ObjectBase objectBase1) {
		if ( ( objectBase0 instanceof ProbabilityObject && objectBase1 instanceof KeywordObject)
			|| ( objectBase0 instanceof ProbabilityObject && objectBase1 instanceof NumberObject)
			|| ( objectBase0 instanceof ProbabilityObject && objectBase1 instanceof RoleVariableObject)
			|| ( objectBase0 instanceof ProbabilityObject && objectBase1 instanceof TimeVariableObject)
			|| ( objectBase0 instanceof ProbabilityObject && objectBase1 instanceof SpotVariableObject)
			|| ( objectBase0 instanceof KeywordObject && objectBase1 instanceof NumberObject)
			|| ( objectBase0 instanceof KeywordObject && objectBase1 instanceof RoleVariableObject)
			|| ( objectBase0 instanceof KeywordObject && objectBase1 instanceof TimeVariableObject)
			|| ( objectBase0 instanceof KeywordObject && objectBase1 instanceof SpotVariableObject)
			|| ( objectBase0 instanceof NumberObject && objectBase1 instanceof RoleVariableObject)
			|| ( objectBase0 instanceof NumberObject && objectBase1 instanceof TimeVariableObject)
			|| ( objectBase0 instanceof NumberObject && objectBase1 instanceof SpotVariableObject)
			|| ( objectBase0 instanceof RoleVariableObject && objectBase1 instanceof TimeVariableObject)
			|| ( objectBase0 instanceof RoleVariableObject && objectBase1 instanceof SpotVariableObject)
			|| ( objectBase0 instanceof TimeVariableObject && objectBase1 instanceof SpotVariableObject))
			return -1;
		else if ( ( objectBase0 instanceof KeywordObject && objectBase1 instanceof ProbabilityObject)
			|| ( objectBase0 instanceof NumberObject && objectBase1 instanceof ProbabilityObject)
			|| ( objectBase0 instanceof NumberObject && objectBase1 instanceof KeywordObject)
			|| ( objectBase0 instanceof RoleVariableObject && objectBase1 instanceof ProbabilityObject)
			|| ( objectBase0 instanceof RoleVariableObject && objectBase1 instanceof KeywordObject)
			|| ( objectBase0 instanceof RoleVariableObject && objectBase1 instanceof NumberObject)
			|| ( objectBase0 instanceof TimeVariableObject && objectBase1 instanceof ProbabilityObject)
			|| ( objectBase0 instanceof TimeVariableObject && objectBase1 instanceof KeywordObject)
			|| ( objectBase0 instanceof TimeVariableObject && objectBase1 instanceof NumberObject)
			|| ( objectBase0 instanceof TimeVariableObject && objectBase1 instanceof RoleVariableObject)
			|| ( objectBase0 instanceof SpotVariableObject && objectBase1 instanceof ProbabilityObject)
			|| ( objectBase0 instanceof SpotVariableObject && objectBase1 instanceof KeywordObject)
			|| ( objectBase0 instanceof SpotVariableObject && objectBase1 instanceof NumberObject)
			|| ( objectBase0 instanceof SpotVariableObject && objectBase1 instanceof RoleVariableObject)
			|| ( objectBase0 instanceof SpotVariableObject && objectBase1 instanceof TimeVariableObject))
			return 1;
		else
			return StringNumberComparator.compareTo( objectBase0._name, objectBase1._name);
//		String[] kinds = new String[] {
//			"probability",
//			"keyword",
//			"number object",
//			"role variable",
//			"time variable",
//			"spot variable"
//		};
//
//		int index0 = -1, index1 = -1;
//		for ( int i = 0; i < kinds.length; ++i) {
//			if ( objectBase0._kind.equals( kinds[ i]))
//				index0 = i;
//			else if ( objectBase1._kind.equals( kinds[ i]))
//				index1 = i;
//		}
//
//		if ( index0 == index1)
//			return StringNumberComparator.compareTo( objectBase0._name, objectBase1._name);
//		else
//			return ( ( index0 < index1) ? -1 : 1);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase#can_remove_selected_objects(soars.application.visualshell.object.player.base.object.base.ObjectBase)
	 */
	protected boolean can_remove_selected_objects(ObjectBase objectBase) {
		if ( _propertyPageMap.get( "variable").contains( objectBase))
			return false;

		return super.can_remove_selected_objects( objectBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase#on_remove()
	 */
	protected void on_remove() {
		_propertyPageMap.get( "variable").update();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.VariableTableBase#on_ok()
	 */
	public void on_ok() {
		_playerBase._objectMapMap.get( "probability").clear();
		_playerBase._objectMapMap.get( "keyword").clear();
		_playerBase._objectMapMap.get( "number object").clear();
		_playerBase._objectMapMap.get( "role variable").clear();
		_playerBase._objectMapMap.get( "time variable").clear();
		_playerBase._objectMapMap.get( "spot variable").clear();

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == objectBase)
				continue;

			if ( objectBase instanceof ProbabilityObject)
				_playerBase._objectMapMap.get( "probability").put( objectBase._name, objectBase);
			else if ( objectBase instanceof KeywordObject)
				_playerBase._objectMapMap.get( "keyword").put( objectBase._name, objectBase);
			else if ( objectBase instanceof NumberObject)
				_playerBase._objectMapMap.get( "number object").put( objectBase._name, objectBase);
			else if ( objectBase instanceof RoleVariableObject)
				_playerBase._objectMapMap.get( "role variable").put( objectBase._name, objectBase);
			else if ( objectBase instanceof TimeVariableObject)
				_playerBase._objectMapMap.get( "time variable").put( objectBase._name, objectBase);
			else if ( objectBase instanceof SpotVariableObject)
				_playerBase._objectMapMap.get( "spot variable").put( objectBase._name, objectBase);
		}
	}
}
