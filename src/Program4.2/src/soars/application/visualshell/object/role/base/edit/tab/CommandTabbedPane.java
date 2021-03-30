/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab;

import java.awt.Frame;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.application.visualshell.object.role.base.edit.tab.base.SelectRuleValuePropertyPage1;
import soars.application.visualshell.object.role.base.edit.tab.command.AttachDetachCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.CreateAgentCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.CreateObjectCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.ExTransferCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.FunctionalObjectCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.KeywordCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.MapCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.MoveCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.OthersCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.RoleCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.SetRoleCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.SpotVariableCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.SubstitutionCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.TimeCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.collection.CollectionCommandPropertyPage1;
import soars.application.visualshell.object.role.base.edit.tab.command.collection.CollectionCommandPropertyPage2;
import soars.application.visualshell.object.role.base.edit.tab.command.collection.CollectionCommandPropertyPage3;
import soars.application.visualshell.object.role.base.edit.tab.command.equip.AgentEquipCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.equip.SpotEquipCommandPropertyPage;
import soars.application.visualshell.object.role.base.edit.tab.command.exchange_algebra.ExchangeAlgebraCommandPropertyPage1;
import soars.application.visualshell.object.role.base.edit.tab.command.exchange_algebra.ExchangeAlgebraCommandPropertyPage2;
import soars.application.visualshell.object.role.base.edit.tab.command.exchange_algebra.ExchangeAlgebraCommandPropertyPage3;
import soars.application.visualshell.object.role.base.edit.tab.command.exchange_algebra.ExchangeAlgebraCommandPropertyPage4;
import soars.application.visualshell.object.role.base.edit.tab.command.exchange_algebra.ExchangeAlgebraCommandPropertyPage5;
import soars.application.visualshell.object.role.base.edit.tab.command.exchange_algebra.ExchangeAlgebraCommandPropertyPage6;
import soars.application.visualshell.object.role.base.edit.tab.command.list.ListCommandPropertyPage1;
import soars.application.visualshell.object.role.base.edit.tab.command.list.ListCommandPropertyPage2;
import soars.application.visualshell.object.role.base.edit.tab.command.list.ListCommandPropertyPage3;
import soars.application.visualshell.object.role.base.edit.tab.command.list.ListCommandPropertyPage4;
import soars.application.visualshell.object.role.base.edit.tab.command.list.ListCommandPropertyPage5;
import soars.application.visualshell.object.role.base.edit.tab.command.list.ListCommandPropertyPage6;
import soars.application.visualshell.object.role.base.edit.tab.command.list.ListCommandPropertyPage7;
import soars.application.visualshell.object.role.base.edit.table.data.CommonRuleData;
import soars.application.visualshell.object.role.base.edit.tree.RuleTree;
import soars.application.visualshell.object.role.spot.SpotRole;

/**
 * @author kurata
 *
 */
public class CommandTabbedPane extends RuleTabbedPane {

	/**
	 * @param kind
	 */
	public CommandTabbedPane(String kind) {
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

		KeywordCommandPropertyPage keywordCommandPropertyPage = new KeywordCommandPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.keyword"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.keyword"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.keyword"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( keywordCommandPropertyPage))
			return false;

		_defaultRulePropertyPageBase = keywordCommandPropertyPage;


		if ( role instanceof AgentRole) {
			MoveCommandPropertyPage moveCommandPropertyPage = new MoveCommandPropertyPage(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.move"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.move"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.move"), "command"),
				role,
				_rulePropertyPageBases.size(),
				frame, editRoleFrame);
			if ( !create( moveCommandPropertyPage))
				return false;
		}


		SpotVariableCommandPropertyPage spotVariableCommandPropertyPage = new SpotVariableCommandPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.spot.variable"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.spot.variable"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.spot.variable"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( spotVariableCommandPropertyPage))
			return false;


		TimeCommandPropertyPage timeCommandPropertyPage = new TimeCommandPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.time"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.time"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.time"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( timeCommandPropertyPage))
			return false;


		if ( role instanceof AgentRole) {
			RoleCommandPropertyPage roleCommandPropertyPage = new RoleCommandPropertyPage(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.role"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.role"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.role"), "command"),
				role,
				_rulePropertyPageBases.size(),
				frame, editRoleFrame);
			if ( !create( roleCommandPropertyPage))
				return false;
		} else {
			SelectRuleValuePropertyPage1 roleCommandPropertyPage = new SelectRuleValuePropertyPage1(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.role"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.role"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.role"), "command"),
				role,
				_rulePropertyPageBases.size(),
				ResourceManager.get_instance().get( "edit.rule.dialog.command.role.label"),
				true,
				frame, editRoleFrame);
			if ( !create( roleCommandPropertyPage))
				return false;

				roleCommandPropertyPage.setup( RulePropertyPageBase.get_spot_role_names( true));
		}


		if ( role instanceof AgentRole) {
			SetRoleCommandPropertyPage setRoleCommandPropertyPage = new SetRoleCommandPropertyPage(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.set.role"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.set.role"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.set.role"), "command"),
				role,
				_rulePropertyPageBases.size(),
				frame, editRoleFrame);
			if ( !create( setRoleCommandPropertyPage))
				return false;
		}


		SubstitutionCommandPropertyPage substitutionCommandPropertyPage = new SubstitutionCommandPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.substitution"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.substitution"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.substitution"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( substitutionCommandPropertyPage))
			return false;


		CollectionCommandPropertyPage1 collectionCommandPropertyPage1 = new CollectionCommandPropertyPage1(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.add"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.collection"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.collection"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( collectionCommandPropertyPage1, ResourceManager.get_instance().get( "edit.rule.dialog.command.collection")))
			return false;


		CollectionCommandPropertyPage2 collectionCommandPropertyPage2 = new CollectionCommandPropertyPage2(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.remove"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.collection"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.collection"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( collectionCommandPropertyPage2, ResourceManager.get_instance().get( "edit.rule.dialog.command.collection")))
			return false;


		CollectionCommandPropertyPage3 collectionCommandPropertyPage3 = new CollectionCommandPropertyPage3(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.others"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.collection"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.collection"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( collectionCommandPropertyPage3, ResourceManager.get_instance().get( "edit.rule.dialog.command.collection")))
			return false;


		ListCommandPropertyPage1 listCommandPropertyPage1 = new ListCommandPropertyPage1(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.first"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.list"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.list"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( listCommandPropertyPage1, ResourceManager.get_instance().get( "edit.rule.dialog.command.list")))
			return false;


		ListCommandPropertyPage2 listCommandPropertyPage2 = new ListCommandPropertyPage2(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.last"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.list"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.list"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( listCommandPropertyPage2, ResourceManager.get_instance().get( "edit.rule.dialog.command.list")))
			return false;


		ListCommandPropertyPage3 listCommandPropertyPage3 = new ListCommandPropertyPage3(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.get"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.list"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.list"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( listCommandPropertyPage3, ResourceManager.get_instance().get( "edit.rule.dialog.command.list")))
			return false;


		ListCommandPropertyPage4 listCommandPropertyPage4 = new ListCommandPropertyPage4(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.remove"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.list"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.list"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( listCommandPropertyPage4, ResourceManager.get_instance().get( "edit.rule.dialog.command.list")))
			return false;


		ListCommandPropertyPage5 listCommandPropertyPage5 = new ListCommandPropertyPage5(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.remove.union"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.list"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.list"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( listCommandPropertyPage5, ResourceManager.get_instance().get( "edit.rule.dialog.command.list")))
			return false;


		ListCommandPropertyPage6 listCommandPropertyPage6 = new ListCommandPropertyPage6(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.order.rotate"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.list"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.list"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( listCommandPropertyPage6, ResourceManager.get_instance().get( "edit.rule.dialog.command.list")))
			return false;


		ListCommandPropertyPage7 listCommandPropertyPage7 = new ListCommandPropertyPage7(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.move.others"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.list"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.list"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( listCommandPropertyPage7, ResourceManager.get_instance().get( "edit.rule.dialog.command.list")))
			return false;


		if ( role instanceof AgentRole) {
			AgentEquipCommandPropertyPage agentEquipCommandPropertyPage = new AgentEquipCommandPropertyPage(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.agent.equip"),
				_kind,
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.get.equip"), "command"),
				role,
				_rulePropertyPageBases.size(),
				frame, editRoleFrame);
			if ( !create( agentEquipCommandPropertyPage))
				return false;
		}


		if ( role instanceof SpotRole) {
			SpotEquipCommandPropertyPage spotEquipCommandPropertyPage = new SpotEquipCommandPropertyPage(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.spot.equip"),
				_kind,
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.get.equip"), "command"),
				role,
				_rulePropertyPageBases.size(),
				frame, editRoleFrame);
			if ( !create( spotEquipCommandPropertyPage))
				return false;
		}


		CreateAgentCommandPropertyPage createAgentCommandPropertyPage = new CreateAgentCommandPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.agent"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.create.agent"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.create.agent"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( createAgentCommandPropertyPage, ResourceManager.get_instance().get( "edit.rule.dialog.command.dynamic.creation")))
			return false;


		CreateObjectCommandPropertyPage createObjectCommandPropertyPage = new CreateObjectCommandPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.create.object"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.create.object"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( createObjectCommandPropertyPage, ResourceManager.get_instance().get( "edit.rule.dialog.command.dynamic.creation")))
			return false;


		if ( role instanceof AgentRole) {
			AttachDetachCommandPropertyPage attachDetachCommandPropertyPage = new AttachDetachCommandPropertyPage(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach"),
				_kind,
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.attach"), "command"),
				role,
				_rulePropertyPageBases.size(),
				frame, editRoleFrame);
			if ( !create( attachDetachCommandPropertyPage))
				return false;
		}


		if ( Environment.get_instance().is_functional_object_enable()) {
			FunctionalObjectCommandPropertyPage functionalObjectCommandPropertyPage = new FunctionalObjectCommandPropertyPage(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.functional.object"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.functional.object"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.functional.object"), "command"),
				role,
				_rulePropertyPageBases.size(),
				frame, editRoleFrame);
			if ( !create( functionalObjectCommandPropertyPage))
				return false;
		}


		MapCommandPropertyPage mapCommandPropertyPage = new MapCommandPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.map"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.map"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.map"), "command"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( mapCommandPropertyPage))
			return false;


		if ( Environment.get_instance().is_exchange_algebra_enable()) {
			ExchangeAlgebraCommandPropertyPage1 exchangeAlgebraCommandPropertyPage1 = new ExchangeAlgebraCommandPropertyPage1(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.projection"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"), "command"),
				role,
				_rulePropertyPageBases.size(),
				frame, editRoleFrame);
			if ( !create( exchangeAlgebraCommandPropertyPage1, ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra")))
				return false;

			ExchangeAlgebraCommandPropertyPage2 exchangeAlgebraCommandPropertyPage2 = new ExchangeAlgebraCommandPropertyPage2(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.plus"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"), "command"),
				role,
				_rulePropertyPageBases.size(),
				frame, editRoleFrame);
			if ( !create( exchangeAlgebraCommandPropertyPage2, ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra")))
				return false;

			ExchangeAlgebraCommandPropertyPage3 exchangeAlgebraCommandPropertyPage3 = new ExchangeAlgebraCommandPropertyPage3(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.operatons"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"), "command"),
				role,
				_rulePropertyPageBases.size(),
				frame, editRoleFrame);
			if ( !create( exchangeAlgebraCommandPropertyPage3, ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra")))
				return false;

			ExchangeAlgebraCommandPropertyPage4 exchangeAlgebraCommandPropertyPage4 = new ExchangeAlgebraCommandPropertyPage4(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.norm"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"), "command"),
				role,
				_rulePropertyPageBases.size(),
				frame, editRoleFrame);
			if ( !create( exchangeAlgebraCommandPropertyPage4, ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra")))
				return false;

			ExchangeAlgebraCommandPropertyPage5 exchangeAlgebraCommandPropertyPage5 = new ExchangeAlgebraCommandPropertyPage5(
				ResourceManager.get_instance().get( ( role instanceof AgentRole)
					? "edit.rule.dialog.command.exchange.algebra.agent.equip"
					: "edit.rule.dialog.command.exchange.algebra.spot.equip"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"), "command"),
				role,
				_rulePropertyPageBases.size(),
				frame, editRoleFrame);
			if ( !create( exchangeAlgebraCommandPropertyPage5, ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra")))
				return false;

			ExchangeAlgebraCommandPropertyPage6 exchangeAlgebraCommandPropertyPage6 = new ExchangeAlgebraCommandPropertyPage6(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.dynamic.creation"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"), "command"),
				role,
				_rulePropertyPageBases.size(),
				frame, editRoleFrame);
			if ( !create( exchangeAlgebraCommandPropertyPage6, ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra")))
				return false;

			if ( Environment.get_instance().is_extransfer_enable()) {
				ExTransferCommandPropertyPage exTransferCommandPropertyPage = new ExTransferCommandPropertyPage(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.extransfer"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.command.extransfer"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.extransfer"), "command"),
					role,
					_rulePropertyPageBases.size(),
					frame, editRoleFrame);
				if ( !create( exTransferCommandPropertyPage, ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra")))
					return false;
			}
		}


		OthersCommandPropertyPage othersCommandPropertyPage = new OthersCommandPropertyPage(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.others"),
			_kind,
			ResourceManager.get_instance().get( "rule.type.command.others"),
			role,
			_rulePropertyPageBases.size(),
			frame, editRoleFrame);
		if ( !create( othersCommandPropertyPage,
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.others"), "command")))
			return false;


		ruleTree.expand();


		return true;
	}
}
