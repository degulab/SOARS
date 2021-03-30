/**
 * 
 */
package soars.application.visualshell.object.role.base.object.command;

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
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.common.CommonRuleManipulator;

/**
 * @author kurata
 *
 */
public class MoveCommand extends Rule {

	/**
	 * @param value
	 * @return
	 */
	public static String[] get_values(String value) {
		return get_values( value, LayerManager.get_instance());
	}

	/**
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	private static String[] get_values(String value, ILayerManipulator layerManipulator) {
		return CommonRuleManipulator.get_spot( "<" + value + ">", layerManipulator);
	}

	/**
	 * @param value
	 * @return
	 */
	public static String get_rule_value(String value) {
		return value.substring( 1, value.length() - 1);
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public MoveCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_names()
	 */
	protected String[] get_used_spot_names() {
		return new String[] { get_used_spot_name()};
	}

	/**
	 * @return
	 */
	private String get_used_spot_name() {
		String[] values = get_values( _value);
		if ( null == values)
			return null;

		return ( ( ( null == values[ 0]) || ( values[ 0].equals( ""))) ? null : values[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	protected String[] get_used_spot_variable_names(Role role) {
		return new String[] { CommonRuleManipulator.get_spot_variable_name1( _value)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_spot_name_and_number(String new_name, String original_name, String head_name, Vector ranges, String new_head_name, Vector new_ranges) {
		String value = CommonRuleManipulator.update_spot_name1( _value, new_name, original_name, head_name, ranges, new_head_name, new_ranges);
		if ( null == value)
			return false;

		_value = value;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected boolean update_spot_variable_name(String name, String new_name, String type, Role role) {
		String value = CommonRuleManipulator.update_spot_variable_name1( _value , name, new_name, type);
		if ( null == value)
			return false;

		_value = value;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	protected boolean can_paste(Role role, Layer drawObjects) {
		String[] values = get_values( _value, drawObjects);
		if ( null == values)
			return false;

		return CommonRuleManipulator.can_paste_spot_and_spot_variable_name( values, drawObjects);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	protected String get_script(String value, Role role) {
		return ( "<" + value + ">moveTo");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_cell_text()
	 */
	public String get_cell_text() {
		return ( "<" + _value + ">");
	}

	@Override
	public List<String> get_java_program(String value, RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		String spotName = JavaProgramExporter.get_spot_name( value, spotNameMap);
		if ( null == spotName)
			return get_unknown_codes();

		JavaProgramExporter.append_import( packagePrefix + ".TSpotTypes", imports);
//		String spotDBname = initialStage ? "spotManager.getSpotDB()" : "spotSet";
//		String agent = initialStage ? "agent" : "getAgent()";
//		codes.add( agent + ".moveTo(" + spotName + ", " + spotDBname + ")");
		List<String> codes = new ArrayList<>();
		if ( !initialStage)
			codes.add( "getAgent().moveTo(" + spotName + ", spotSet)");
		else {
			codes.add( "if ( null != spot)\n");
			codes.add( JavaProgramExporter._indents[ 1] +	"agent.moveTo(" + spotName + ", spotManager.getSpotDB());\n");
			codes.add( "else {\n");
			codes.add( JavaProgramExporter._indents[ 1] +	"spot = spotManager.getSpotDB().get(" + spotName + ");\n");
			codes.add( JavaProgramExporter._indents[ 1] +	"agent.initializeCurrentSpot(spot.getName(), spotManager.getSpotDB());\n");
			codes.add( "}");
		}
		return codes;
	}
}
