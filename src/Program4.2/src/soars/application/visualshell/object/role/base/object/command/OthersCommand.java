/**
 * 
 */
package soars.application.visualshell.object.role.base.object.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
import soars.application.visualshell.file.exporter.java.object.RuleData;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;

/**
 * @author kurata
 *
 */
public class OthersCommand extends Rule {

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public OthersCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	@Override
	public List<String> get_java_program(RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, List<String> imports, List<String> commands, InitialValueMap initialValueMap, String packagePrefix, Role role, boolean initialStage) {
		return get_empty_codes();
	}
}
