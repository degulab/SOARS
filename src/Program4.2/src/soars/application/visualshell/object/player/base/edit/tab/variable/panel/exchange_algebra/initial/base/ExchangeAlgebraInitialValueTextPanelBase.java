/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab.variable.panel.exchange_algebra.initial.base;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;
import soars.application.visualshell.object.player.base.edit.tab.variable.panel.base.panel.InitialValueTextPanelBase;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 *
 */
public class ExchangeAlgebraInitialValueTextPanelBase extends InitialValueTextPanelBase {

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
	 * @param color 
	 * @param propertyPageMap 
	 * @param playerBase 
	 */
	public ExchangeAlgebraInitialValueTextPanelBase(Color color, PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap) {
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

		_label = create_label();
		_label.setHorizontalAlignment( SwingConstants.RIGHT);
		_label.setForeground( _color);
		add( _label);

		add( Box.createHorizontalStrut( 5));

		_textField = new TextField();
		_textField.setDocument( new TextExcluder( Constant._prohibitedCharacters11));
		_textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_textField.setSelectionColor( _color);
		_textField.setForeground( _color);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _textField, this));
		add( _textField);

		add( Box.createHorizontalStrut( 5));
	}

	/**
	 * @return
	 */
	protected JLabel create_label() {
		return null;
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
}
