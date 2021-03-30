/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab.variable.panel.exchange_algebra.initial;

import java.awt.Color;
import java.util.Map;

import javax.swing.JLabel;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.exchange_algebra.initial.base.ExchangeAlgebraInitialValueTextPanelBase;

/**
 * @author kurata
 *
 */
public class TimePanel extends ExchangeAlgebraInitialValueTextPanelBase {

	/**
	 * @param color 
	 * @param propertyPageMap 
	 * @param playerBase 
	 */
	public TimePanel(Color color, PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap) {
		super(color, playerBase, propertyPageMap);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.variable.panel.exchange_algebra.initial.base.ExchangeAlgebraInitialValueTextPanelBase#create_label()
	 */
	protected JLabel create_label() {
		return new JLabel( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.edit.time"));
	}
}
