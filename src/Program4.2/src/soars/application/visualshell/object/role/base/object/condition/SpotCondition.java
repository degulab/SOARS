/**
 * 
 */
package soars.application.visualshell.object.role.base.object.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import soars.application.visualshell.file.exporter.java.JavaProgramExporter;
import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
import soars.application.visualshell.file.exporter.java.object.RuleData;
import soars.application.visualshell.layer.ILayerManipulator;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class SpotCondition extends Rule {
	/**
	 * @param value
	 * @return
	 */
	public static String[][] get_values(String value) {
		return get_values( value, LayerManager.get_instance());
	}

	/**
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	private static String[][] get_values(String value, ILayerManipulator layerManipulator) {
		String[] elements = Tool.split( value.startsWith( "!") ? value.substring( "!".length()) : value, ' ');
		if ( null == elements || 1 > elements.length)
			return null;

		String[][] values = new String[][] {
			CommonRuleManipulator.get_spot( "<" + elements[ 0] + ">", layerManipulator),
			null
		};

		if ( null == values[ 0])
			return null;

		if ( 1 == elements.length)
			return values;

		values[ 1] = CommonRuleManipulator.get_spot( "<" + elements[ 1] + ">", layerManipulator);
		if ( null == values[ 1])
			return null;

		return values;
	}

	/**
	 * @param values
	 * @return
	 */
	public static String get_rule_value(String[] values) {
		return values[ 0].substring( 1, values[ 0].length() - 1)
			+ ( ( null == values[ 1]) ? "" : ( " " + values[ 1].substring( 1, values[ 1].length() - 1)));
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public SpotCondition(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_names()
	 */
	protected String[] get_used_spot_names() {
		String[][] values = get_values( _value);
		if ( null == values)
			return null;

		String[] used_spot_names = new String[] { null, null};

		used_spot_names[ 0] = ( ( ( null == values[ 0][ 0]) || ( values[ 0][ 0].equals( ""))) ? null : values[ 0][ 0]);

		if ( null != values[ 1])
			used_spot_names[ 1] = ( ( ( null == values[ 1][ 0]) || ( values[ 1][ 0].equals( ""))) ? null : values[ 1][ 0]);

		return ( ( ( null == used_spot_names[ 0]) && ( null == used_spot_names[ 1])) ? null : used_spot_names);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	protected String[] get_used_spot_variable_names(Role role) {
		String[][] values = get_values( _value);
		if ( null == values)
			return null;

		return new String[] {
			CommonRuleManipulator.get_spot_variable_name3( values[ 0]),
			CommonRuleManipulator.get_spot_variable_name3( values[ 1])
		};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_spot_name_and_number(String new_name, String original_name, String head_name, Vector ranges, String new_head_name, Vector new_ranges) {
		String[] elements = Tool.split( _value, ' ');
		if ( null == elements || 1 > elements.length)
			return false;

		boolean result1 = false;
		String element = CommonRuleManipulator.update_spot_name1( elements[ 0], new_name, original_name, head_name, ranges, new_head_name, new_ranges);
		if ( null != element) {
			elements[ 0] = element;
			result1 = true;
		}

		boolean result2 = false;
		if ( 1 < elements.length) {
			element = CommonRuleManipulator.update_spot_name1( elements[ 1], new_name, original_name, head_name, ranges, new_head_name, new_ranges);
			if ( null != element) {
				elements[ 1] = element;
				result2 = true;
			}
		}

		if ( !result1 && !result2)
			return false;

		_value = ( elements[ 0] + ( ( 1 < elements.length) ? ( " " + elements[ 1]) : ""));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_spot_variable_name(String name, String new_name, String type, Role role) {
		String[] elements = Tool.split( _value, ' ');
		if ( null == elements || 1 > elements.length)
			return false;

		boolean result1 = false;
		String element = CommonRuleManipulator.update_spot_variable_name1( elements[ 0], name, new_name, type);
		if ( null != element) {
			elements[ 0] = element;
			result1 = true;
		}

		boolean result2 = false;
		if ( 1 < elements.length) {
			element = CommonRuleManipulator.update_spot_variable_name1( elements[ 1], name, new_name, type);
			if ( null != element) {
				elements[ 1] = element;
				result2 = true;
			}
		}

		if ( !result1 && !result2)
			return false;

		_value = ( elements[ 0] + ( ( 1 < elements.length) ? ( " " + elements[ 1]) : ""));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	protected boolean can_paste(Role role, Layer drawObjects) {
		String[][] values = get_values( _value, drawObjects);
		if ( null == values)
			return false;

		for ( int i = 0; i < values.length; ++i) {
			if ( null == values[ i])
				continue;

			if ( !CommonRuleManipulator.can_paste_spot_and_spot_variable_name( values[ i], drawObjects))
				return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected String get_script(String value, Role role) {
		String[] elements = Tool.split( value.startsWith( "!") ? value.substring( "!".length()) : value, ' ');
		if ( null == elements)
			return "";

		if ( 2 > elements.length)
			return ( ( value.startsWith( "!") ? "!<" : "<") + elements[ 0] + ">isSpot");
		else {
			String[][] values = get_values( value);
			if ( null == values || null == values[ 1])
				return "";

			if ( null == values[ 1][ 0] && !values[ 1][ 1].equals( ""))
				return ( ( value.startsWith( "!") ? "!<" : "<") + elements[ 0] + ">isSpot " + elements[ 1]);
			else 
				return ( "<" + elements[ 1] + ">setSpot " + Constant._spotVariableName
					+ " , " + ( value.startsWith( "!") ? "!<" : "<") + elements[ 0] + ">isSpot " + Constant._spotVariableName);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_cell_text()
	 */
	public String get_cell_text() {
		String[] elements = Tool.split( _value.startsWith( "!") ? _value.substring( "!".length()) : _value, ' ');
		if ( null == elements)
			return _value;

		if ( 2 > elements.length)
			return ( ( _value.startsWith( "!") ? "!<" : "<") + elements[ 0] + ">");
		else
			return ( "<" + elements[ 1] + "> is " + ( _value.startsWith( "!") ? "not <" : "<") + elements[ 0] + ">");
	}

	@Override
	public List<String> get_java_program(String value, RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		String denial = value.startsWith( "!") ? "!" : "";

		String[] elements = Tool.split( value.startsWith( "!") ? value.substring( "!".length()) : value, ' ');
		if ( null == elements)
			return get_unknown_codes();

		if ( 2 > elements.length) {
			String spotName = JavaProgramExporter.get_spot_name( value, spotNameMap);
			if ( null == spotName)
				return get_unknown_codes();

			JavaProgramExporter.append_import( packagePrefix + ".TSpotTypes", imports);
			String agent = initialStage ? "agent" : "getAgent()";
			List<String> codes = new ArrayList<>();
			codes.add( denial + agent + ".getCurrentSpotName().equals(" + spotName + ")");
			return codes;
		} else {
			return get_unknown_codes();	// TODO スポット変数は未実装
		}
	}
}
