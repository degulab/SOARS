/**
 * 
 */
package soars.application.visualshell.object.gis;

import java.awt.Component;
import java.awt.Point;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.edit.variable.object.base.SimpleVariableObject;
import soars.application.visualshell.object.gis.edit.variable.object.keyword.KeywordObject;
import soars.application.visualshell.object.gis.edit.variable.object.number.NumberObject;
import soars.application.visualshell.object.player.spot.SpotObject;

/**
 * @author kurata
 *
 */
public class GisDataRecord extends Vector<String> {

	/**
	 * 
	 */
	public GisDataRecord() {
		super();
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set(String line) {
		String[] words = line.split( "\t");
		if ( null == words || 2 > words.length)
			return false;

		for ( int i = 1; i < words.length; ++i)
			add( words[ i]);

		return true;
	}

	/**
	 * @param index
	 * @return
	 */
	public boolean can_use_as_spot_name(int index) {
		return !get( index).matches( ".*[" + Constant._prohibitedCharacters2 + "]+.*");
		//return ( !get( index).matches( "[0123456789\\.]+") && !get( index).matches( ".*[" + Constant._prohibitedCharacters2 + "]+.*"));
	}

	/**
	 * @param index
	 * @return
	 */
	public boolean can_use_as_keyword_initial_value(int index) {
		return !get( index).matches( ".*[" + Constant._prohibitedCharacters3 + "]+.*");
	}

	/**
	 * @param indices
	 * @param spotFields
	 * @param simpleVariableObjects
	 * @param component
	 * @return
	 */
	public boolean can_append_spots(int[] indices, List<Field> spotFields, List<SimpleVariableObject> simpleVariableObjects, Component component) {
		String name = make_name( indices, spotFields);
		if ( !Constant.is_correct_agent_or_spot_name( name)) {
			JOptionPane.showMessageDialog( component,
				ResourceManager.get_instance().get( "edit.gis.data.dialog.invalid.spot.name.error.message") + " - \"" + name + "\"",
				ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}
		for ( SimpleVariableObject simpleVariableObject:simpleVariableObjects) {
			if ( simpleVariableObject instanceof KeywordObject) {
				String initialValue = make_name( simpleVariableObject._indices, simpleVariableObject._fields);
				if ( !Constant.is_correct_keyword_initial_value( initialValue)) {
					JOptionPane.showMessageDialog( component,
						ResourceManager.get_instance().get( "edit.gis.data.dialog.invalid.keyword.initial.value.error.message") + " - \"" + initialValue + "\"",
						ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
						JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param indices
	 * @param spotFields
	 * @param simpleVariableObjects
	 * @param role
	 * @param gis
	 * @param range
	 * @param ratio
	 * @param component
	 * @return
	 */
	public boolean append_spots(int[] indices, List<Field> spotFields, List<SimpleVariableObject> simpleVariableObjects, String role, String gis, double[] range, double[] ratio, JComponent component) {
		String name = make_name( indices, spotFields);
		name = get_unique_name( name);
		SpotObject spotObject = LayerManager.get_instance().get_spot_has_this_name( name);
		if ( null != spotObject)
			spotObject._position = get_spot_position( range, ratio);
		else {
			spotObject = ( SpotObject)LayerManager.get_instance().append_object2( "spot", name, get_spot_position( range, ratio), component);
			if ( null == spotObject)
				return false;
		}

		if ( null != spotObject) {
			spotObject._initialRole = role;

			for ( SimpleVariableObject simpleVariableObject:simpleVariableObjects) {
				if ( simpleVariableObject instanceof KeywordObject) {
					KeywordObject keywordObject = ( KeywordObject)simpleVariableObject;
					spotObject.append_object( "keyword", new String[] { keywordObject._name, make_name( keywordObject._indices, keywordObject._fields), keywordObject._comment});
				} else if ( simpleVariableObject instanceof NumberObject) {
					NumberObject numberObject = ( NumberObject)simpleVariableObject;
					spotObject.append_object( "number object", new String[] { numberObject._name, numberObject._type, make_name( numberObject._indices, numberObject._fields), numberObject._comment});
				}
			}

			spotObject._gis = gis;

			// TODO GISスポットに絶対座標を設定する
			spotObject._gisCoordinates[ 0] = get( size() - 2);
			spotObject._gisCoordinates[ 1] = get( size() - 1);
		}

		return true;
	}

	/**
	 * @param indices
	 * @param spotFields
	 * @return
	 */
	private String make_name(int[] indices, List<Field> spotFields) {
		String name = "";
		for ( int i = 0; i < indices.length; ++i)
			name += ( 0 > indices[ i]) ? spotFields.get( i)._value : get( indices[ i]);
		return name;
	}

	/**
	 * @param name
	 * @return
	 */
	private String get_unique_name(String name) {
		if ( ( null == LayerManager.get_instance().get_agent_has_this_name( name))
			/*&& ( null == LayerManager.get_instance().get_spot_has_this_name( name))*/)
			return name;

		int index = 2;
		while ( true) {
			String suffix = ( "(" + String.valueOf( index) + ")");
			if ( ( null == LayerManager.get_instance().get_agent_has_this_name( name + suffix))
				/*&& ( null == LayerManager.get_instance().get_spot_has_this_name( name + suffix))*/)
				return ( name + suffix);

			++index;
		}
	}

	/**
	 * @param range
	 * @param ratio
	 * @return
	 */
	private Point get_spot_position(double[] range, double[] ratio) {
		// TODO Auto-generated method stub
		return new Point(
			( int)( ( Double.parseDouble( get( size() - 2)) - range[ 0]) * ratio[ 0]),
			( int)( ( range[ 3] - Double.parseDouble( get( size() - 1))) * ratio[ 1]));
	}

	/**
	 * @param row
	 * @param table
	 * @return
	 */
	public boolean create(int row, JTable table) {
		for ( int column = 0; column < table.getColumnCount(); ++column)
			add( ( String)table.getValueAt( row, column));
		return true;
	}

	/**
	 * @return
	 */
	public String get() {
		String text = "";

		for ( int i = 0; i < size(); ++i)
			text += ( ( ( 0 == i) ? "" : "\t") + get( i));

		return text;
	}

	/**
	 * 
	 */
	public void print() {
		String record = "record";
		for ( String element:this)
			record += ( "\t" + element);
		System.out.println( record);
	}
}
