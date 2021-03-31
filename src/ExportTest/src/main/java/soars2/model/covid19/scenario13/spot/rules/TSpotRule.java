package soars2.model.covid19.scenario13.spot.rules;

import soars2.core.TRole;
import soars2.core.TRule;
import soars2.core.TSpot;
import soars2.core.TTime;

abstract public class TSpotRule extends TRule {

	/**
	 * コンストラクタ
	 * @param ruleName ルール名
	 * @param ownerRole このルールを持つロール
	 */
	public TSpotRule(String ruleName, TRole ownerRole) {
		super(ruleName, ownerRole);
		// TODO TAgentRuleの発火するスポットのようなメンバ変数が必要か検討
	}
	@Override
	public TSpotRule setTimeAndStage(boolean isRegular, TTime time, String stage) {
		return (TSpotRule)super.setTimeAndStage(isRegular, time, stage);
	}

	@Override
	public TSpotRule setRelativeTimeAndStage(TRule precedentRule, TTime relativeTime, String stage) {
		return (TSpotRule)super.setRelativeTimeAndStage(precedentRule, relativeTime, stage);
	}

	/**
	 * このルールを持つスポットを返す
	 * @return スポット
	 */
	public TSpot getSpot() {
		return (TSpot)getOwnerRole().getOwner();
	}
}
