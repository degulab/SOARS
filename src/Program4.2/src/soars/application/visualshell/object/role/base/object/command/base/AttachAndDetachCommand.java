/**
 * 
 */
package soars.application.visualshell.object.role.base.object.command.base;

import java.util.Vector;

import soars.application.visualshell.layer.ILayerManipulator;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;

/**
 * @author kurata
 *
 */
public class AttachAndDetachCommand extends Rule {

	/**
	 * 
	 */
	public static String[][] _reserved_words = {
		{ "attachTo ", "detach "},
		{ "attachToAll ", "detachAll "},
		{ "attachToAll ", "detachAll "},
		{ "attachToFirst ", "detachFirst "},
		{ "attachToLast ", "detachLast "},
		{ "attachToRandomOne ", "detachRandomOne "},
		{ "attachToRandomOne ", "detachRandomOne "}
	};

	/**
	 * @param value
	 * @return
	 */
	public static int get_kind(String value) {
		return get_kind( value, LayerManager.get_instance());
	}

	/**
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	private static int get_kind(String value, ILayerManipulator layerManipulator) {
		if ( value.equals( _reserved_words[ 0][ 0].trim())
			|| value.equals( _reserved_words[ 0][ 1].trim()))
			return 0;

		String reserved_word = CommonRuleManipulator.get_reserved_word( value);
		if ( null== reserved_word)
			return -1;

		if ( reserved_word.equals( _reserved_words[ 0][ 0])
			|| reserved_word.equals( _reserved_words[ 0][ 1]))
			return 0;
		else if ( reserved_word.equals( _reserved_words[ 3][ 0])
			|| reserved_word.equals( _reserved_words[ 3][ 1]))
			return 3;
		else if ( reserved_word.equals( _reserved_words[ 4][ 0])
			|| reserved_word.equals( _reserved_words[ 4][ 1]))
			return 4;
		else {
			String spot = CommonRuleManipulator.get_full_prefix( value);
			if ( null== spot)
				return -1;

			String[] elements = CommonRuleManipulator.get_elements( value);
			if ( null == elements || 1 != elements.length || null == elements[ 0] || elements[ 0].equals( ""))
				return -1;

			int offset;
			if ( CommonRuleManipulator.is_object( "collection", spot + elements[ 0], layerManipulator))
				offset = 0;
			else if ( CommonRuleManipulator.is_object( "list", spot + elements[ 0], layerManipulator))
				offset = 1;
			else
				return -1;

			if ( reserved_word.equals( _reserved_words[ 1][ 0])
				|| reserved_word.equals( _reserved_words[ 1][ 1]))
				return ( 1 + offset);
			else if ( reserved_word.equals( _reserved_words[ 5][ 0])
				|| reserved_word.equals( _reserved_words[ 5][ 1]))
				return ( 5 + offset);
		}
		return -1;
	}

	/**
	 * @param value
	 * @return
	 */
	private int get_kind2(String value) {
		if ( value.equals( _reserved_words[ 0][ 0].trim())
			|| value.equals( _reserved_words[ 0][ 1].trim()))
			return 0;

		String reserved_word = CommonRuleManipulator.get_reserved_word( value);
		if ( null== reserved_word)
			return -1;

		if ( reserved_word.equals( _reserved_words[ 0][ 0])
			|| reserved_word.equals( _reserved_words[ 0][ 1]))
			return 0;
		else if ( reserved_word.equals( _reserved_words[ 1][ 0])
			|| reserved_word.equals( _reserved_words[ 1][ 1]))
			return 1;
//		else if ( reserved_word.equals( _reserved_words[ 2][ 0])
//			|| reserved_word.equals( _reserved_words[ 2][ 1]))
//			return 2;
		else if ( reserved_word.equals( _reserved_words[ 3][ 0])
			|| reserved_word.equals( _reserved_words[ 3][ 1]))
			return 3;
		else if ( reserved_word.equals( _reserved_words[ 4][ 0])
			|| reserved_word.equals( _reserved_words[ 4][ 1]))
			return 4;
		else if ( reserved_word.equals( _reserved_words[ 5][ 0])
			|| reserved_word.equals( _reserved_words[ 5][ 1]))
			return 5;
//		else if ( reserved_word.equals( _reserved_words[ 6][ 0])
//			|| reserved_word.equals( _reserved_words[ 6][ 1]))
//			return 6;

		return -1;
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public AttachAndDetachCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_agent_names()
	 */
	protected String[] get_used_agent_names() {
		return new String[] { get_used_agent_name()};
	}

	/**
	 * @return
	 */
	private String get_used_agent_name() {
		int kind = get_kind( _value);
		if ( 0 != kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 != elements.length || null == elements[ 0] || elements[ 0].equals( ""))
			return null;

		return elements[ 0];
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
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_collection_names()
	 */
	protected String[] get_used_collection_names() {
		return new String[] { get_used_object_name( "collection")};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_list_names()
	 */
	protected String[] get_used_list_names() {
		return new String[] { get_used_object_name( "list")};
	}

	/**
	 * @param type
	 * @return
	 */
	private String get_used_object_name(String type) {
		String used_object_name = get_used_object_name();
		if ( null == used_object_name)
			return null;

		if ( !CommonRuleManipulator.is_object( type, used_object_name, LayerManager.get_instance()))
			return null;

		return used_object_name;
	}

	/**
	 * @return
	 */
	private String get_used_object_name() {
		int kind = get_kind( _value);
		if ( 1 > kind)
			return null;

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null == prefix)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 != elements.length || null == elements[ 0] || elements[ 0].equals( ""))
			return null;

		return ( prefix + elements[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_agent_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_agent_name_and_number(String new_name, String original_name, String head_name, Vector ranges, String new_head_name, Vector new_ranges) {
		int kind = get_kind( _value);
		if ( 0 != kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 != elements.length || null == elements[ 0] || elements[ 0].equals( ""))
			return false;

		elements[ 0] = CommonRuleManipulator.get_new_agent_or_spot_name( elements[ 0], new_name, original_name, head_name, ranges, new_head_name, new_ranges);
		if ( null == elements[ 0])
			return false;

		String reserved_word = CommonRuleManipulator.get_reserved_word( _value);
		if ( null== reserved_word)
			return false;

		_value = ( reserved_word + elements[ 0]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_spot_name_and_number(String new_name, String original_name, String head_name, Vector ranges, String new_head_name, Vector new_ranges) {
		int kind = get_kind2( _value);
		if ( 1 > kind)
			return false;

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
		int kind = get_kind( _value);
		if ( 1 > kind)
			return false;

		String value = CommonRuleManipulator.update_spot_variable_name2( _value, name, new_name, type);
		if ( null == value)
			return false;

		_value = value;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_collection_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_collection_name(String name, String new_name, String type, Role role) {
		return update_object_name( name, new_name, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_list_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_list_name(String name, String new_name, String type, Role role) {
		return update_object_name( name, new_name, type);
	}

	/**
	 * @param name
	 * @param new_name
	 * @param type
	 * @return
	 */
	private boolean update_object_name(String name, String new_name, String type) {
		int kind = get_kind( _value);
		if ( 1 > kind)
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null== prefix)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 != elements.length || null == elements[ 0] || elements[ 0].equals( ""))
			return false;

		if ( !CommonRuleManipulator.correspond( prefix, elements[ 0], name, type))
			return false;

		String reserved_word = CommonRuleManipulator.get_reserved_word( _value);
		if ( null== reserved_word)
			return false;

		_value = ( prefix + reserved_word + new_name);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	protected boolean can_paste(Role role, Layer drawObjects) {
		int kind = get_kind( _value, drawObjects);
		if ( 0 > kind)
			return false;

		if ( !can_paste_agent_name( kind, drawObjects))
			return false;

		if ( !can_paste_spot_name( kind, drawObjects))
			return false;

		if ( !can_paste_collection_name( kind, drawObjects))
			return false;

		if ( !can_paste_list( kind, drawObjects))
			return false;

		return true;
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_agent_name(int kind, Layer drawObjects) {
		if ( 0 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 != elements.length || null == elements[ 0] || elements[ 0].equals( ""))
			return true;

		return ( null != drawObjects.get_agent_has_this_name( elements[ 0]));
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_spot_name(int kind, Layer drawObjects) {
		if ( _value.startsWith( _reserved_words[ 0][ 1].trim()))
			return true;

		return CommonRuleManipulator.can_paste_spot_and_spot_variable_name1( _value, drawObjects);
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_collection_name(int kind, Layer drawObjects) {
		if ( 1 != kind && 5 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 != elements.length || null == elements[ 0] || elements[ 0].equals( ""))
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null == prefix)
			return false;

		return CommonRuleManipulator.can_paste_object( "collection", prefix + elements[ 0], drawObjects);
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_list(int kind, Layer drawObjects) {
		if ( 2 != kind && 3 != kind && 4 != kind && 6 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 != elements.length || null == elements[ 0] || elements[ 0].equals( ""))
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null == prefix)
			return false;

		return CommonRuleManipulator.can_paste_object( "list", prefix + elements[ 0], drawObjects);
	}
}
