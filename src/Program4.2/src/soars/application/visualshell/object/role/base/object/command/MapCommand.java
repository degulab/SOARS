/**
 * 
 */
package soars.application.visualshell.object.role.base.object.command;

import java.util.Vector;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;

/**
 * @author kurata
 *
 */
public class MapCommand extends Rule {

	/**
	 * Reserved words
	 */
	public static String[] _reserved_words = {
		"getMap ",
		"putMap "
	};

	/**
	 * @param value
	 * @return
	 */
	public static int get_kind(String value) {
		String reserved_word = CommonRuleManipulator.get_reserved_word( value);
		for ( int i = 0; i < _reserved_words.length; ++i) {
			if ( _reserved_words[ i].equals( reserved_word))
				return i;
		}
		return -1;
	}
	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public MapCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_names()
	 */
	protected String[] get_used_spot_names() {
		return new String[] { CommonRuleManipulator.extract_spot_name1( _value)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	protected String[] get_used_spot_variable_names(Role role) {
		return new String[] { CommonRuleManipulator.get_spot_variable_name2( _value)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_probability_names()
	 */
	protected String[] get_used_probability_names() {
		return new String[] { get_used_probability_name()};
	}

	/**
	 * @return
	 */
	private String get_used_probability_name() {
		String used_probability_name = get_used_object_name( _value, 0, 2);
		if ( null == used_probability_name)
			return null;

		if ( !CommonRuleManipulator.is_object( "probability", used_probability_name, LayerManager.get_instance()))
			return null;

		return used_probability_name;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_map_names()
	 */
	protected String[] get_used_map_names() {
		return new String[] { get_used_map_name()};
	}

	/**
	 * @return
	 */
	private String get_used_map_name() {
		String used_map_name = get_used_object_name( _value, 1, 0);
		if ( null == used_map_name)
			return null;

		if ( !CommonRuleManipulator.is_object( "map", used_map_name, LayerManager.get_instance()))
			return null;

		return used_map_name;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_keyword_names()
	 */
	protected String[] get_used_keyword_names() {
		String[] used_keyword_names = new String[] { null, null};

		used_keyword_names[ 0] = get_used_object_name( _value, 2, 1);
		if ( null == used_keyword_names[ 0])
			return null;

		used_keyword_names[ 1] = get_used_object_name( _value, 0, 2);
		if ( null == used_keyword_names[ 1])
			return null;

		if ( used_keyword_names[ 0].startsWith( "\"") && used_keyword_names[ 0].endsWith( "\""))
			used_keyword_names[ 0] = null;
		else
			used_keyword_names[ 0] = ( ( !CommonRuleManipulator.is_object( "keyword", used_keyword_names[ 0], LayerManager.get_instance())) ? null : used_keyword_names[ 0]);

		used_keyword_names[ 1] = ( ( !CommonRuleManipulator.is_object( "keyword", used_keyword_names[ 1], LayerManager.get_instance())) ? null : used_keyword_names[ 1]);

		return used_keyword_names;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_number_object_names()
	 */
	protected String[] get_used_number_object_names() {
		return new String[] { get_used_number_object_name()};
	}

	/**
	 * @return
	 */
	private String get_used_number_object_name() {
		String used_number_object_name = get_used_object_name( _value, 0, 2);
		if ( null == used_number_object_name)
			return null;

		if ( !CommonRuleManipulator.is_object( "number object", used_number_object_name, LayerManager.get_instance()))
			return null;

		return used_number_object_name;
	}

	/**
	 * @param value
	 * @param index0
	 * @param index1
	 * @return
	 */
	private String get_used_object_name(String value, int index0, int index1) {
		int kind = get_kind( value);
		if ( 0 > kind)
			return null;

		String prefix = CommonRuleManipulator.get_full_prefix( value);
		if ( null == prefix)
			return null;

		String used_object_name = get_used_object_name( kind, value, index0, index1);
		if ( null == used_object_name)
			return null;

		if ( used_object_name.startsWith( "\"") && used_object_name.endsWith( "\""))
			return used_object_name;

		return ( prefix + used_object_name);
	}

	/**
	 * @param kind
	 * @param value
	 * @param index1
	 * @param index0
	 * @return
	 */
	private String get_used_object_name(int kind, String value, int index0, int index1) {
		String[] elements = CommonRuleManipulator.get_elements( value, 3);
		if ( null == elements)
			return null;

		switch ( kind) {
			case 0:
				return elements[ index0];
			case 1:
				return elements[ index1];
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_spot_name_and_number(String new_name, String original_name, String head_name, Vector ranges, String new_head_name, Vector new_ranges) {
		String value = CommonRuleManipulator.update_spot_name2( _value, new_name, original_name, head_name, ranges, new_head_name, new_ranges);
		if ( null == value)
			return false;

		_value = value;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_spot_variable_name(String name, String new_name, String type, Role role) {
		String value = CommonRuleManipulator.update_spot_variable_name2( _value, name, new_name, type);
		if ( null == value)
			return false;

		_value = value;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_probability_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_probability_name(String name, String new_name, String type, Role role) {
		return update_object_name( name, new_name, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_map_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_map_name(String name, String new_name, String type, Role role) {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null== prefix)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value, 3);
		if ( null == elements)
			return false;


		switch ( kind) {
			case 0:
				if ( !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
					return false;

				elements[ 1] = new_name;
				break;
			case 1:
				if ( !CommonRuleManipulator.correspond( prefix, elements[ 0], name, type))
					return false;

				elements[ 0] = new_name;
				break;
			default:
				return false;
		}

		_value = ( prefix + _reserved_words[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_keyword_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_keyword_name(String name, String new_name, String type, Role role) {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null== prefix)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value, 3);
		if ( null == elements)
			return false;


		boolean result1 = false;

		switch ( kind) {
			case 0:
				result1 = CommonRuleManipulator.correspond( prefix, elements[ 0], name, type);
				if ( result1)
					elements[ 0] = new_name;

				break;
			case 1:
				result1 = CommonRuleManipulator.correspond( prefix, elements[ 1], name, type);
				if ( result1)
					elements[ 1] = new_name;

				break;
			default:
				return false;
		}

		boolean result2 = CommonRuleManipulator.correspond( prefix, elements[ 2], name, type);
		if ( result2)
			elements[ 2] = new_name;

		if ( !result1 && !result2)
			return false;

		_value = ( prefix + _reserved_words[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_number_object_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_number_object_name(String name, String new_name, String type, Role role) {
		return update_object_name( name, new_name, type);
	}

	/**
	 * @param name
	 * @param new_name
	 * @param type
	 * @param rule
	 * @return
	 */
	private boolean update_object_name(String name, String new_name, String type) {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null== prefix)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value, 3);
		if ( null == elements)
			return false;


		switch ( kind) {
			case 0:
				if ( !CommonRuleManipulator.correspond( prefix, elements[ 0], name, type))
					return false;

				elements[ 0] = new_name;
				break;
			case 1:
				if ( !CommonRuleManipulator.correspond( prefix, elements[ 2], name, type))
					return false;

				elements[ 2] = new_name;
				break;
			default:
				return false;
		}

		_value = ( prefix + _reserved_words[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	protected boolean can_paste(Role role, Layer drawObjects) {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		if ( !CommonRuleManipulator.can_paste_spot_and_spot_variable_name1( _value, drawObjects))
			return false;

		if ( !can_paste_map_name( kind, drawObjects))
			return false;

		if ( !can_paste_key( kind, drawObjects))
			return false;

		if ( !can_paste_value( kind, drawObjects))
			return false;

		return true;
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_map_name(int kind, Layer drawObjects) {
		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null == prefix)
			return false;

		String map = get_used_object_name( kind, _value, 1, 0);
		if ( null == map)
			return false;

		return CommonRuleManipulator.can_paste_object( "map", prefix + map, drawObjects);
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_key(int kind, Layer drawObjects) {
		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null == prefix)
			return false;

		String key = get_used_object_name( kind, _value, 2, 1);
		if ( null == key)
			return false;

		if ( key.startsWith( "\"") && key.endsWith( "\""))
			return true;

		return CommonRuleManipulator.can_paste_object( "keyword", prefix + key, drawObjects);
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_value(int kind, Layer drawObjects) {
		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null == prefix)
			return false;

		String val = get_used_object_name( kind, _value, 0, 2);
		if ( null == val)
			return false;

		return ( CommonRuleManipulator.can_paste_object( "probability", prefix + val, drawObjects)
			|| CommonRuleManipulator.can_paste_object( "keyword", prefix + val, drawObjects)
			|| CommonRuleManipulator.can_paste_object( "number object", prefix + val, drawObjects));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected String get_script(String value, Role role) {
		int kind = get_kind( value);
		if ( 0 > kind)
			return "";

		String spot = CommonRuleManipulator.get_full_prefix( value);
		if ( null == spot)
			return "";

		String key = get_used_object_name( kind, value, 2, 1);
		if ( null == key)
			return "";

		boolean key_is_keyword = ( key.startsWith( "\"") && key.endsWith( "\"")) ? false : true;

		String script = ( !key_is_keyword ? "" : ( spot + "equip " + key + " ; "));

		String val = get_used_object_name( kind, value, 0, 2);
		if ( null == val)
			return "";

		// TODO 今後対応が必要になりそう
//		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( val))
//				|| ( null != LayerManager.get_instance().get_spot_has_this_name( _name_textField.getText()))) {
		// TODO 修正済
		if ( CommonRuleManipulator.is_object( "keyword", spot + val, LayerManager.get_instance())
			|| CommonRuleManipulator.is_object( "file", spot + val, LayerManager.get_instance())) {
			switch ( kind) {
				case 0:
					return ( script + value + " ; " + spot + "printEquip " + val);
				case 1:
					return ( script + spot + "equip " + val + " ; " + value);
				default:
					return "";
			}
		} else if ( CommonRuleManipulator.is_object( "time variable", spot + val, LayerManager.get_instance()))
			return ( script + value + " ; " + spot + "cloneEquip $Time." + val);
		else if ( ( null != LayerManager.get_instance().get_agent_has_this_name( val))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( val))
			|| CommonRuleManipulator.is_object( "role variable", spot + val, LayerManager.get_instance())
			|| CommonRuleManipulator.is_object( "spot variable", spot + val, LayerManager.get_instance()))
			return ( script + value);
		else
			return ( script + value + " ; " + spot + "cloneEquip " + val);

//		boolean val_is_keyword = CommonRuleManipulator.is_object( "keyword", spot + val, LayerManager.get_instance());
//
//		boolean val_is_role_variable = CommonRuleManipulator.is_object( "role variable", spot + val, LayerManager.get_instance());
//		boolean val_is_time_variable = CommonRuleManipulator.is_object( "time variable", spot + val, LayerManager.get_instance());
//		boolean val_is_spot_variable = CommonRuleManipulator.is_object( "spot variable", spot + val, LayerManager.get_instance());
//
//		if ( !val_is_keyword) {
//			if ( val_is_role_variable)
//				script += ( value + " ; " + spot + "cloneEquip $Role." + val);
//			else if ( val_is_time_variable)
//				script += ( value + " ; " + spot + "cloneEquip $Time." + val);
//			else if ( val_is_spot_variable)
//				script += value;
//			else
//				script += ( value + " ; " + spot + "cloneEquip " + val);
//		} else {
//		if ( !val_is_keyword)
//			script += ( value + " ; " + spot + "cloneEquip " + val);
//		else {
//			switch ( kind) {
//				case 0:
//					script += ( value + " ; " + spot + "printEquip " + val);
//					break;
//				case 1:
//					script += ( spot + "equip " + val + " ; " + value);
//					break;
//				default:
//					return "";
//			}
//		}
//
//		return script;
	}
}
