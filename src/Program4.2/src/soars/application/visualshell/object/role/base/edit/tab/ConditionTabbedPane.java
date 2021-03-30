/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab;

import java.awt.Frame;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.EditRuleValuePropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.base.SelectRuleValuePropertyPage4;
import soars.application.visualshell.object.role.base.edit.tab.condition.CollectionAndListConditionPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.condition.FunctionalObjectConditionPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.condition.KeywordConditionPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.condition.ListConditionPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.condition.NumberObjectConditionPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.condition.ProbabilityConditionPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.condition.RoleConditionPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.condition.SpotConditionPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.condition.TimeConditionPropertyPage;
import soars.application.visualshell.object.role.base.edit.table.data.CommonRuleData;
import soars.application.visualshell.object.role.base.edit.tree.RuleTree;
import soars.application.visualshell.object.role.spot.SpotRole;

/**
 * @author kurata
 *
 */
public class ConditionTabbedPane extends RuleTabbedPane {

	/**
	 * @param kind
	 */
	public ConditionTabbedPane(String kind) {
		super(kind);
	}

	/**
	 * @param ruleTree
	 * @param role
	 * @param frame
	 * @param editRoleFrame
	 * @return
	 */
	public boolean setup(RuleTree ruleTree, Role role, Frame frame, EditRoleFrame editRoleFrame) {
		if ( !super.setup(ruleTree))
			return false;

		SpotConditionPropertyPage spotConditionPropertyPage = new SpotConditionPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.condition.spot"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.spot"), "condition"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( spotConditionPropertyPage))
			return false;

		_defaultRulePropertyPageBase = spotConditionPropertyPage;


		KeywordConditionPropertyPage keywordConditionPropertyPage = new KeywordConditionPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.keyword"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.condition.keyword"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.keyword"), "condition"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( keywordConditionPropertyPage))
			return false;


		TimeConditionPropertyPage timeConditionPropertyPage = new TimeConditionPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.time"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.condition.time"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.time"), "condition"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( timeConditionPropertyPage))
			return false;


		if ( role instanceof AgentRole) {
			RoleConditionPropertyPage roleConditionPropertyPage = new RoleConditionPropertyPage(
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.role"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.condition.role"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.role"), "condition"),
				role,
				_rulePropertyPageBases.size(),
				frame, editRoleFrame);
			if ( !create( roleConditionPropertyPage))
				return false;
		}


		ProbabilityConditionPropertyPage probabilityConditionPropertyPage = new ProbabilityConditionPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.probability"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.condition.probability"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.probability"), "condition"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( probabilityConditionPropertyPage))
			return false;


		SelectRuleValuePropertyPage4 nameConditionPropertyPage;
		if ( role instanceof AgentRole)
			nameConditionPropertyPage = new SelectRuleValuePropertyPage4(
				"agent",
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.agent.name"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.condition.agent.name"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.agent.name"), "condition"),
				role,
				_rulePropertyPageBases.size(),
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.agent.name.label"),
				false,
				frame, editRoleFrame);
		else if ( role instanceof SpotRole)
			nameConditionPropertyPage = new SelectRuleValuePropertyPage4(
				"spot",
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.name"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.condition.spot.name"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.spot.name"), "condition"),
				role,
				_rulePropertyPageBases.size(),
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.name.label"),
				false,
				frame, editRoleFrame);
		else
			return false;

		if ( !create( nameConditionPropertyPage))
			return false;


		NumberObjectConditionPropertyPage numberObjectConditionPropertyPage = new NumberObjectConditionPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.number.object"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.condition.number.object"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.number.object"), "condition"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( numberObjectConditionPropertyPage))
			return false;


		CollectionAndListConditionPropertyPage collectionConditionPropertyPage = new CollectionAndListConditionPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.condition.collection"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.collection"), "condition"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( collectionConditionPropertyPage))
			return false;


		CollectionAndListConditionPropertyPage listConditionPropertyPage1 = new CollectionAndListConditionPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.inclusion.condition"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.condition.list"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.list"), "condition"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( listConditionPropertyPage1))
			return false;


		ListConditionPropertyPage listConditionPropertyPage2 = new ListConditionPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.specified.condition"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.condition.list"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.list"), "condition"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( listConditionPropertyPage2))
			return false;


		FunctionalObjectConditionPropertyPage functionalObjectConditionPropertyPage = new FunctionalObjectConditionPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.functional.object"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.condition.functional.object"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.functional.object"), "condition"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( functionalObjectConditionPropertyPage))
			return false;


		EditRuleValuePropertyPage othersConditionPropertyPage = new EditRuleValuePropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.others"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.condition.others"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.others"), "condition"),
			role,
			_rulePropertyPageBases.size(),
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.other.label"),
			400,
			true,
			frame, editRoleFrame);
		if ( !create( othersConditionPropertyPage))
			return false;


		ruleTree.expand();


		return true;
	}
}
