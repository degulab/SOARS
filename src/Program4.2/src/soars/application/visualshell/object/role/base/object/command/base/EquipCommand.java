/**
 * 
 */
package soars.application.visualshell.object.role.base.object.command.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
import soars.application.visualshell.file.exporter.java.object.RuleData;
import soars.application.visualshell.file.exporter.java.object.Variable;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.command.GetEquipCommand;

/**
 * @author kurata
 *
 */
public class EquipCommand extends Rule {

	protected static String[] _kinds = new String[] {
		"probability",
		"collection",
		"list",
		"map",
		"keyword",
		"number object",
		"time variable",
		"spot variable"
	};

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public EquipCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/**
	 * @param spot
	 * @param command
	 * @param element1
	 * @param element2
	 * @param prefix
	 * @param keyword
	 * @param time_variable
	 * @param spot_variable
	 * @return
	 */
	protected static String get_script(String spot, String command, String element1, String element2, String prefix, boolean keyword, boolean time_variable, boolean spot_variable) {
		String result;

		if ( keyword)
			result = ( spot + command.substring( 0, command.length() - "Equip".length()) + " ");
		else
			result = ( spot + command + " ");

		String time_prefix = ( time_variable ? "$Time." : "");

		result += ( time_prefix + element1 + "=" + time_prefix + element2);

		if ( keyword || spot_variable)
			return result;

		return ( result + " ; " + prefix + "cloneEquip " + time_prefix + element1);
	}

	/**
	 * @param prefixAndObject1
	 * @param prefixAndObject2
	 * @param kind1
	 * @param kind2
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
	protected List<String> get_java_program(String[] prefixAndObject1, String[] prefixAndObject2, String kind1, String kind2, RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, String packagePrefix, Role role, boolean initialStage) {
		// TODO Auto-generated method stub
		if ( !kind1.equals( kind2))
			return get_unknown_codes();

		List<Variable> variables1 = new ArrayList<>();
		variables1.add( new Variable( kind1, prefixAndObject1[ 1]));

		List<String> prefixes1 = get_prefix( prefixAndObject1[ 0], variables1, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
		if ( null == prefixes1)
			return get_unknown_codes();

		List<Variable> variables2 = new ArrayList<>();
		variables2.add( new Variable( kind2, prefixAndObject2[ 1]));

		List<String> prefixes2 = get_prefix( prefixAndObject2[ 0], variables2, ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, packagePrefix, role, initialStage);
		if ( null == prefixes2)
			return get_unknown_codes();

		String spotDBname = initialStage ? "spotManager.getSpotDB()" : "spotSet";
		String code = GetEquipCommand.class.isInstance( this)
			? prefixes1.get( 0) + ".set" + prefixAndObject1[ 1] + "(" + ( variables1.get( 0)._spotSet ? spotDBname + "," : "") + prefixes2.get( 0) + ".get" + prefixAndObject2[ 1] + "(" + ( variables2.get( 0)._spotSet ? spotDBname : "") + "))"
			: prefixes2.get( 0) + ".set" + prefixAndObject2[ 1] + "(" + ( variables2.get( 0)._spotSet ? spotDBname + "," : "") + prefixes1.get( 0) + ".get" + prefixAndObject1[ 1] + "(" + ( variables1.get( 0)._spotSet ? spotDBname : "") + "))";
			//? prefixes1.get( 0) + ".set" + prefixAndObject1[ 1] + "(" + prefixes2.get( 0) + ".get" + prefixAndObject2[ 1] + "())"
			//: prefixes2.get( 0) + ".set" + prefixAndObject2[ 1] + "(" + prefixes1.get( 0) + ".get" + prefixAndObject1[ 1] + "())";
		List<String> codes = new ArrayList<>();
		codes.add( code);
		return codes;
	}
}
