/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab.variable.panel.exchange_algebra.initial;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;

/**
 * @author kurata
 *
 */
public class KeywordPanel extends JPanel {

	/**
	 * 
	 */
	private Color _color = null;

	/**
	 * 
	 */
	private PlayerBase _playerBase = null;

	/**
	 * 
	 */
	private Map<String, PropertyPageBase> _propertyPageMap = null;

	/**
	 * 
	 */
	private JLabel _label = null;

	/**
	 * 
	 */
	private ComboBox _comboBox = null;

	/**
	 * @param color 
	 * @param propertyPageMap 
	 * @param playerBase 
	 */
	public KeywordPanel(Color color, PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap) {
		super();
		_color = color;
		_playerBase = playerBase;
		_propertyPageMap = propertyPageMap;
	}

	/**
	 * 
	 */
	public void setup() {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		add( Box.createHorizontalStrut( 5));

		_label = new JLabel( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.keyword.keyword"));
		_label.setHorizontalAlignment( SwingConstants.RIGHT);
		_label.setForeground( _color);
		add( _label);

		add( Box.createHorizontalStrut( 5));

		_comboBox = new ComboBox( _propertyPageMap.get( "simple variable").get( "keyword"), _color, false, new CommonComboBoxRenderer( _color, false));
		add( _comboBox);

		add( Box.createHorizontalStrut( 5));
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_label_width(int width) {
		return Math.max( width, _label.getPreferredSize().width);
	}

	/**
	 * @param width
	 */
	public void adjust(int width) {
		_label.setPreferredSize( new Dimension( width, _label.getPreferredSize().height));
	}

	/**
	 * @param value
	 */
	public void set(String value) {
		_comboBox.setSelectedItem( value);
	}

	/**
	 * @return
	 */
	public String get() {
		return ( String)_comboBox.getSelectedItem();
	}

	/**
	 * 
	 */
	public void update() {
		CommonTool.update( _comboBox, _propertyPageMap.get( "simple variable").get( "keyword"));
	}
}
