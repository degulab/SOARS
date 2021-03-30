/*
 * 2005/08/05
 */
package soars.application.visualshell.object.role.base.object.condition.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import soars.application.visualshell.file.exporter.java.JavaProgramExporter;
import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
import soars.application.visualshell.file.exporter.java.object.RuleData;
import soars.application.visualshell.file.exporter.java.object.Variable;
import soars.application.visualshell.layer.ILayerManipulator;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.condition.CollectionCondition;

/**
 * @author kurata
 */
public class CollectionAndListCondition {

	/**
	 * 
	 */
	static public String[] _reservedWords = {
		"isEmpty ",
		"containsAgent ",
		"containsSpot ",
		"containsEquip ",
		"containsEquip ",
		"containsString ",
		"containsAll ",
		"containsAll ",
		"askAll "
	};

	/**
	 * @param rule
	 * @return
	 */
	public static int get_kind(Rule rule) {
		return get_kind( rule, LayerManager.get_instance());
	}

	/**
	 * @param rule
	 * @param layerManipulator
	 * @return
	 */
	public static int get_kind(Rule rule, ILayerManipulator layerManipulator) {
		if ( rule._value.startsWith( _reservedWords[ 0])
			|| rule._value.startsWith( "!" + _reservedWords[ 0]))
			return 0;
		else if ( rule._value.startsWith( _reservedWords[ 1])
			|| rule._value.startsWith( "!" + _reservedWords[ 1]))
			return 1;
		else if ( rule._value.startsWith( _reservedWords[ 2])
			|| rule._value.startsWith( "!" + _reservedWords[ 2]))
			return 2;
		else if ( rule._value.startsWith( _reservedWords[ 5])
			|| rule._value.startsWith( "!" + _reservedWords[ 5]))
			return 5;
		else if ( rule._value.startsWith( _reservedWords[ 8])
			|| rule._value.startsWith( "!" + _reservedWords[ 8]))
			return 8;
		else {
			String[] elements = CommonRuleManipulator.get_elements( rule._value);
			if ( null == elements || 2 != elements.length)
				return -2;

			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return -2;

			if ( rule._value.startsWith( _reservedWords[ 3])
				|| rule._value.startsWith( "!" + _reservedWords[ 3])) {
				if ( CommonRuleManipulator.is_object( "number object", prefix + elements[ 1], layerManipulator))
					return 3;
				else if ( CommonRuleManipulator.is_object( "probability", prefix + elements[ 1], layerManipulator))
					return 4;
				else
					return -2;
			} else if ( rule._value.startsWith( _reservedWords[ 6])
				|| rule._value.startsWith( "!" + _reservedWords[ 6])) {
				if ( CommonRuleManipulator.is_object( "collection", prefix + elements[ 1], layerManipulator))
					return ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection"))) ? 6 : 7);
				else if ( CommonRuleManipulator.is_object( "list", prefix + elements[ 1], layerManipulator))
					return ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection"))) ? 7 : 6);
				else
					return -2;
			}
		}

		return -1;
	}

	/**
	 * @param value
	 * @return
	 */
	private static int get_kind2(String value) {
		if ( value.startsWith( _reservedWords[ 0])
			|| value.startsWith( "!" + _reservedWords[ 0]))
			return 0;
		else if ( value.startsWith( _reservedWords[ 1])
			|| value.startsWith( "!" + _reservedWords[ 1]))
			return 1;
		else if ( value.startsWith( _reservedWords[ 2])
			|| value.startsWith( "!" + _reservedWords[ 2]))
			return 2;
		else if ( value.startsWith( _reservedWords[ 3])
			|| value.startsWith( "!" + _reservedWords[ 3]))
			return 3;
//		else if ( value.startsWith( _reserved_words[ 4])
//			|| value.startsWith( "!" + _reserved_words[ 4]))
//			return 4;
		else if ( value.startsWith( _reservedWords[ 5])
			|| value.startsWith( "!" + _reservedWords[ 5]))
			return 5;
		else if ( value.startsWith( _reservedWords[ 6])
			|| value.startsWith( "!" + _reservedWords[ 6]))
			return 6;
//		else if ( value.startsWith( _reserved_words[ 7])
//			|| value.startsWith( "!" + _reserved_words[ 7]))
//			return 7;
		else if ( value.startsWith( _reservedWords[ 8])
			|| value.startsWith( "!" + _reservedWords[ 8]))
			return 8;

		return -1;
	}

	/**
	 * @param rule
	 * @return
	 */
	public static String get_used_agent_name(Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 2 != elements.length)
			return null;

		switch ( kind) {
			case 1:
				if ( elements[ 1].equals( ""))
					break;

				return elements[ 1];
		}

		return null;
	}

	/**
	 * @param rule
	 * @return
	 */
	public static String[] get_used_spot_names(Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return null;

		String[] spot_names = new String[] {
			CommonRuleManipulator.extract_spot_name2( elements[ 0]),
			null};

		if ( 2 == elements.length) {
			switch ( kind) {
				case 2:
					if ( elements[ 1].equals( ""))
						break;

					spot_names[ 1] = elements[ 1];
					break;
			}
		}

		return spot_names;
	}

	/**
	 * @param rule
	 * @return
	 */
	public static String get_used_spot_variable_name(Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return null;

		return CommonRuleManipulator.get_spot_variable_name2( elements[ 0]);
	}

	/**
	 * @param rule
	 * @return
	 */
	public static String get_used_probability_name(Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 2 != elements.length)
			return null;

		switch ( kind) {
			case 4:
				if ( elements[ 1].equals( ""))
					break;

				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix)
					break;

				return ( prefix + elements[ 1]);
		}

		return null;
	}

	/**
	 * @param rule
	 * @return
	 */
	public static String[] get_used_collection_names(Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return null;

		String[] collection_names = new String[] {
			rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")) ? elements[ 0] : null,
			null};

		if ( 2 == elements.length && !elements[ 1].equals( "")
			&& ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")) && 6 == kind)
				|| ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")) && 7 == kind))) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null != prefix)
				collection_names[ 1] = ( prefix + elements[ 1]);
		}

		return collection_names;
	}

	/**
	 * @param rule
	 * @return
	 */
	public static String[] get_used_list_names(Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return null;

		String[] list_names = new String[] {
			rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")) ? elements[ 0] : null,
			null};

		if ( 2 == elements.length && !elements[ 1].equals( "")
			&& ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")) && 6 == kind)
				|| ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")) && 7 == kind))) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null != prefix)
				list_names[ 1] = ( prefix + elements[ 1]);
		}

		return list_names;
	}

	/**
	 * @param rule
	 * @return
	 */
	public static String get_used_number_object_name(Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 2 != elements.length)
			return null;

		switch ( kind) {
			case 3:
				if ( elements[ 1].equals( ""))
					break;

				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix)
					break;

				return ( prefix + elements[ 1]);
		}

		return null;
	}

	/**
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param rule
	 * @return
	 */
	public static boolean update_agent_name_and_number(String newName, String originalName, String headName, Vector ranges, String newHeadName, Vector newRanges, Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 2 > elements.length)
			return false;

		switch ( kind) {
			case 1:
				String agent_name = CommonRuleManipulator.get_new_agent_or_spot_name( elements[ 1], newName, originalName, headName, ranges, newHeadName, newRanges);
				if ( null == agent_name)
					return false;

				elements[ 1] = agent_name;
				break;
			default:
				return false;
		}

		rule._value = ( ( rule._value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			rule._value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param rule
	 * @return
	 */
	public static boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector ranges, String newHeadName, Vector newRanges, Rule rule) {
		int kind = get_kind2( rule._value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return false;

		boolean result1 = false;
		String element = CommonRuleManipulator.update_spot_name2( elements[ 0], newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null != element) {
			elements[ 0] = element;
			result1 = true;
		}

		boolean result2 = false;
		if ( 2 == elements.length) {
			switch ( kind) {
				case 2:
					element = CommonRuleManipulator.get_new_agent_or_spot_name( elements[ 1], newName, originalName, headName, ranges, newHeadName, newRanges);
					if ( null != element) {
						elements[ 1] = element;
						result2 = true;
					}
					break;
			}
		}

		if ( !result1 && !result2)
			return false;

		rule._value = ( ( rule._value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			rule._value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @param rule
	 * @return
	 */
	public static boolean update_spot_variable_name(String name, String newName, String type, Rule rule) {
		int kind = get_kind2( rule._value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return false;

		elements[ 0] = CommonRuleManipulator.update_spot_variable_name2( elements[ 0], name, newName, type);
		if ( null == elements[ 0])
			return false;

		rule._value = ( ( rule._value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			rule._value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @param rule
	 * @return
	 */
	public static boolean update_probability_name(String name, String newName, String type, Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 2 != elements.length || elements[ 1].equals( ""))
			return false;

		switch ( kind) {
			case 4:
				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
					return false;

				elements[ 1] = newName;
				break;
			default:
				return false;
		}

		rule._value = ( ( rule._value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			rule._value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @param rule
	 * @return
	 */
	public static boolean update_collection_name(String name, String newName, String type, Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return false;

		boolean result1 = false;
		if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection"))) {
			String element0 = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
			if ( null != element0) {
				elements[ 0] = element0;
				result1 = true;
			}
		}

		boolean result2 = false;
		if ( 2 == elements.length && !elements[ 1].equals( "")
			&& ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")) && 6 == kind)
				|| ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")) && 7 == kind))) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null != prefix && CommonRuleManipulator.correspond( prefix, elements[ 1], name, type)) {
				elements[ 1] = newName;
				result2 = true;
			}
		}

		if ( !result1 && !result2)
			return false;

		rule._value = ( ( rule._value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			rule._value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @param rule
	 * @return
	 */
	public static boolean update_list_name(String name, String newName, String type, Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return false;

		boolean result1 = false;
		if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list"))) {
			String element0 = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
			if ( null != element0) {
				elements[ 0] = element0;
				result1 = true;
			}
		}

		boolean result2 = false;
		if ( 2 == elements.length && !elements[ 1].equals( "")
			&& ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")) && 6 == kind)
				|| ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")) && 7 == kind))) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null != prefix && CommonRuleManipulator.correspond( prefix, elements[ 1], name, type)) {
				elements[ 1] = newName;
				result2 = true;
			}
		}

		if ( !result1 && !result2)
			return false;

		rule._value = ( ( rule._value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			rule._value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @param rule
	 * @return
	 */
	public static boolean update_number_object_name(String name, String newName, String type, Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 2 != elements.length || elements[ 1].equals( ""))
			return false;

		switch ( kind) {
			case 3:
				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
					return false;

				elements[ 1] = newName;
				break;
			default:
				return false;
		}

		rule._value = ( ( rule._value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			rule._value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param rule
	 * @param role
	 * @param drawObjects
	 * @return
	 */
	public static boolean can_paste(Rule rule, Role role, Layer drawObjects) {
		int kind = get_kind( rule, drawObjects);
		if ( 0 > kind)
			return false;

		return can_paste( kind, rule, role, drawObjects);
	}

	/**
	 * @param kind
	 * @param rule
	 * @param role
	 * @param drawObjects
	 * @return
	 */
	public static boolean can_paste(int kind, Rule rule, Role role, Layer drawObjects) {
		if ( !can_paste_agent_name( kind, rule, drawObjects))
			return false;

		if ( !can_paste_spot_name( kind, rule, drawObjects))
			return false;

		if ( !can_paste_probability_name( kind, rule, drawObjects))
			return false;

		if ( !can_paste_collection_name( kind, rule, drawObjects))
			return false;

		if ( !can_paste_list_name( kind, rule, drawObjects))
			return false;

		if ( !can_paste_number_object_name( kind, rule, drawObjects))
			return false;

		return true;
	}

	/**
	 * @param kind
	 * @param value
	 * @param type
	 * @param drawObjects
	 * @return
	 */
	private static boolean can_paste_agent_name(int kind, Rule rule, Layer drawObjects) {
		if ( 1 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return false;

		if ( 2 > elements.length)
			return true;

		if ( elements[ 1].equals( ""))
			return true;

		return ( null != drawObjects.get_agent_has_this_name( elements[ 1]));
	}

	/**
	 * @param kind
	 * @param value
	 * @param type
	 * @param drawObjects
	 * @return
	 */
	private static boolean can_paste_spot_name(int kind, Rule rule, Layer drawObjects) {
		if ( 2 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return false;

		if ( 2 > elements.length)
			return true;

		if ( elements[ 1].equals( ""))
			return true;

		return ( null != drawObjects.get_spot_has_this_name( elements[ 1]));
	}

	/**
	 * @param kind
	 * @param value
	 * @param type
	 * @param drawObjects
	 * @return
	 */
	private static boolean can_paste_probability_name(int kind, Rule rule, Layer drawObjects) {
		if ( 4 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 2 != elements.length)
			return false;

		if ( elements[ 1].equals( ""))
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return false;

		return CommonRuleManipulator.can_paste_object( "probability", prefix + elements[ 1], drawObjects);
	}

	/**
	 * @param kind
	 * @param value
	 * @param type
	 * @param drawObjects
	 * @return
	 */
	private static boolean can_paste_collection_name(int kind, Rule rule, Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return false;

		if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection"))
			&& !CommonRuleManipulator.can_paste_object( "collection", elements[ 0], drawObjects))
			return false;

		if ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")) && 6 == kind)
			|| ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")) && 7 == kind)) {
			if ( 2 > elements.length || null == elements[ 1] || elements[ 1].equals( ""))
				return false;

			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return false;

			return CommonRuleManipulator.can_paste_object( "collection", prefix + elements[ 1], drawObjects);
		}

		return true;
	}

	/**
	 * @param kind
	 * @param value
	 * @param type
	 * @param drawObjects
	 * @return
	 */
	private static boolean can_paste_list_name(int kind, Rule rule, Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return false;

		if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list"))
			&& !CommonRuleManipulator.can_paste_object( "list", elements[ 0], drawObjects))
			return false;

		if ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")) && 6 == kind)
			|| ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")) && 7 == kind)) {
			if ( 2 > elements.length || null == elements[ 1] || elements[ 1].equals( ""))
				return false;

			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return false;

			return CommonRuleManipulator.can_paste_object( "list", prefix + elements[ 1], drawObjects);
		}

		return true;
	}

	/**
	 * @param kind
	 * @param value
	 * @param type
	 * @param drawObjects
	 * @return
	 */
	private static boolean can_paste_number_object_name(int kind, Rule rule, Layer drawObjects) {
		if ( 3 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 2 != elements.length)
			return false;

		if ( elements[ 1].equals( ""))
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return false;

		return CommonRuleManipulator.can_paste_object( "number object", prefix + elements[ 1], drawObjects);
	}

	/**
	 * @param value
	 * @return
	 */
	public static String get_script(String value) {
		// TODO 修正済
		String denial = ( value.startsWith( "!") ? "!" : "");

		String method = CommonRuleManipulator.get_reserved_word( value);
		if ( null == method)
			return "";

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements)
			return "";

		String[] prefix_and_object = get_prefix_and_object( elements[ 0]);
		//String[] prefix_and_object = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String script = ( denial + prefix_and_object[ 0] + method + prefix_and_object[ 1]);

		for ( int i = 1; i < elements.length; ++i) {
			prefix_and_object = get_prefix_and_object( elements[ i]);	// 旧いデータへの配慮 - 旧いデータでは頭に<>または<spot>がついているので、それを取り除く必要がある
			//prefix_and_object = CommonRuleManipulator.get_prefix_and_object( elements[ i]);	// 旧いデータへの配慮
			script += ( "=" + prefix_and_object[ 1]);
		}

		return script;
	}

	/**
	 * @param element
	 * @return
	 */
	public static String[] get_prefix_and_object(String element) {
		// TODO 追加
		String[] spot_and_object = CommonRuleManipulator.separate_into_spot_and_object( element);
		if ( null == spot_and_object) {
			if ( CommonRuleManipulator.is_object( "role variable", element))
				return new String[] { "", "$Role." + element};
			else if ( CommonRuleManipulator.is_object( "time variable", element))
				return new String[] { "", "$Time." + element};
			else
				return new String[] { "", element};
		} else {
			if ( CommonRuleManipulator.is_object( "role variable", element))
				spot_and_object[ 1] = "$Role." + spot_and_object[ 1];
			else if ( CommonRuleManipulator.is_object( "time variable", element))
				spot_and_object[ 1] = "$Time." + spot_and_object[ 1];
			if ( spot_and_object[ 0].equals( ""))
				return new String[] { "<>", spot_and_object[ 1]};
			else
				return new String[] { "<" + spot_and_object[ 0] + ">", spot_and_object[ 1]};
		}
	}

	/**
	 * @param value
	 * @return
	 */
	public static String get_cell_text(String value) {
		// TODO 修正済
		//return get_script( value);
		String denial = ( value.startsWith( "!") ? "!" : "");

		String method = CommonRuleManipulator.get_reserved_word( value);
		if ( null == method)
			return "";

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements)
			return "";

		String[] prefix_and_object = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String script = ( denial + prefix_and_object[ 0] + method + prefix_and_object[ 1]);

		for ( int i = 1; i < elements.length; ++i) {
			prefix_and_object = CommonRuleManipulator.get_prefix_and_object( elements[ i]);	// 旧いデータへの配慮
			script += ( "=" + prefix_and_object[ 1]);
		}

		return script;
	}

	/**
	 * @param type
	 * @param value
	 * @param variables
	 * @param role 
	 */
	public static void get_variable(String type, String value, List<Variable> variables, Role role) {
		// TODO Auto-generated method stub
		System.out.println( "[" + ( role instanceof AgentRole ? "AgentRole" : "SpotRole") + " : " + role._name + "]");

		System.out.println( "[" + type + "]");

		String denial = ( value.startsWith( "!") ? "!" : "");

		String method = CommonRuleManipulator.get_reserved_word( value);
		if ( null == method)
			return;

		System.out.println( "method=" + denial + method);

		String[] elements = CommonRuleManipulator.get_elements( value, 1);
		if ( null == elements)
			return;

		String[] prefixAndObject = get_prefix_and_object( elements[ 0]);

		String parameter = "";
		for ( int i = 0; i < prefixAndObject.length; ++i)
			parameter += ( ( 0 == i) ? "" : ":") + prefixAndObject[ i];

		System.out.println( parameter);

		for ( int i = 1; i < elements.length; ++i) {
			prefixAndObject = get_prefix_and_object( elements[ i]);	// 旧いデータへの配慮
			parameter = "";
			for ( int j = 0; j < prefixAndObject.length; ++j)
				parameter += ( ( 0 == j) ? "" : ":") + prefixAndObject[ j];
			System.out.println( parameter);
		}

		System.out.println( get_script( value));
		System.out.println( "");
	}

	/**
	 * @param rule
	 * @param value 
	 * @param ruleData
	 * @param roleDataSet
	 * @param agentNameMap
	 * @param spotNameMap
	 * @param imports
	 * @param commands
	 * @param packagePrefix
	 * @param role
	 * @param initialStage
	 * @return
	 */
	public static List<String> get_java_program(Rule rule, String value, RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		String denial = ( value.startsWith( "!") ? "!" : "");

		String method = CommonRuleManipulator.get_reserved_word( value);
		if ( null == method)
			return Rule.get_empty_codes();

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements)
			return Rule.get_empty_codes();

		String[] prefixAndObject = get_prefix_and_object( elements[ 0]);
		//String[] prefix_and_object = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		List<String> variableNames = new ArrayList<>();
		for ( int i = 1; i < elements.length; ++i)
			variableNames.add( elements[ i]);

		//String script = ( denial + prefixAndObject[ 0] + method + prefixAndObject[ 1]);
		List<Variable> variables = get_variables( get_kind( rule, LayerManager.get_instance()), rule._type, prefixAndObject[ 1], variableNames);

//		for ( int i = 1; i < elements.length; ++i) {
//			prefixAndObject = get_prefix_and_object( elements[ i]);	// 旧いデータへの配慮 - 旧いデータでは頭に<>または<spot>がついているので、それを取り除く必要がある
//			//prefix_and_object = CommonRuleManipulator.get_prefix_and_object( elements[ i]);	// 旧いデータへの配慮
//			script += ( "=" + prefixAndObject[ 1]);
//		}

		List<String> prefixes = rule.get_prefix( prefixAndObject[ 0], variables, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
		if ( null == prefixes)
			return Rule.get_unknown_codes();

		String code = "condition : " + String.valueOf( get_kind( rule, LayerManager.get_instance())) + " : " + _reservedWords[ get_kind( rule, LayerManager.get_instance())];

		String spotDBname = initialStage ? "spotManager.getSpotDB()" : "spotSet";
		String agent = initialStage ? "agent" : "getAgent()";
		String spot = initialStage ? "spot" : "getSpot()";

		switch ( get_kind( rule, LayerManager.get_instance())) {
			case 0:
				break;
			case 1:
				code = denial + prefixes.get( 0) + ".get" + prefixAndObject[ 1] + "(" + ( variables.get( 0)._spotSet ? spotDBname : "") + ").contains(";
				if ( 0 == variableNames.size())
					code += agent + ")";
				else {
					JavaProgramExporter.append_import( packagePrefix + ".TAgentTypes", imports);
					code += JavaProgramExporter.get_agent_name( elements[ 1], agentNameMap) + ")";
				}
				break;
			case 2:
				code = denial + prefixes.get( 0) + ".get" + prefixAndObject[ 1] + "(" + ( variables.get( 0)._spotSet ? spotDBname : "") + ").contains(";
				if ( 0 == variableNames.size())
					code += AgentRole.class.isInstance( role) ? ( spotDBname + ".get(" + agent + ".getCurrentSpotName()))") : ( spot + ")");
				else {
					JavaProgramExporter.append_import( packagePrefix + ".TSpotTypes", imports);
					code += JavaProgramExporter.get_spot_name( elements[ 1], spotNameMap) + ")";
				}
				break;
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
				break;
			default:
				return Rule.get_unknown_codes();
		}
		List<String> codes = new ArrayList<>();
		codes.add( code);
		return codes;
	}

	/**
	 * @param kind
	 * @param type
	 * @param variableName
	 * @param variableNames
	 */
	private static List<Variable> get_variables(int kind, String type, String variableName, List<String> variableNames) {
		// TODO Auto-generated method stub
		List<Variable> variables = new ArrayList<>();
		variables.add( new Variable( type, variableName));
		for ( String name:variableNames) {
			switch ( kind) {
				case 0:
				case 1:	// nameはエージェント名なので処理は不要
				case 2:	// nameはスポット名なので処理は不要
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				default:
			}
		}
		return variables;
	}
}
