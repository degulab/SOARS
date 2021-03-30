/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.command.exchange_algebra.panel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.application.visualshell.object.role.base.object.common.time.TimeRule;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextLimiter;

/**
 * @author kurata
 *
 */
public class TimePanel extends JPanel {

	/**
	 * 
	 */
	private TextField _timeTextField = null;

	/**
	 * 
	 */
	private List<ComboBox> _timeComboBoxes = new ArrayList<ComboBox>();

	/**
	 * 
	 */
	private List<JLabel> _timeLabels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private RulePropertyPageBase _rulePropertyPageBase = null;

	/**
	 * @param rulePropertyPageBase
	 * 
	 */
	public TimePanel(RulePropertyPageBase rulePropertyPageBase) {
		super();
		_rulePropertyPageBase = rulePropertyPageBase;
	}

	/**
	 * @return
	 */
	public boolean create() {
		int pad = 0;

		setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_timeTextField = new TextField();
		_timeTextField.setDocument( new TextLimiter( "0123456789"));
		_timeTextField.setHorizontalAlignment( SwingConstants.RIGHT);
		_timeTextField.setSelectionColor( _rulePropertyPageBase._color);
		_timeTextField.setForeground( _rulePropertyPageBase._color);
		_timeTextField.setPreferredSize( new Dimension( _rulePropertyPageBase._standardControlWidth, _timeTextField.getPreferredSize().height));
		add( _timeTextField);

		JLabel label = new JLabel( " / ");
		label.setForeground( _rulePropertyPageBase._color);
		_timeLabels.add( label);
		add( label);

		ComboBox comboBox = new ComboBox( CommonTool.get_hours(), _rulePropertyPageBase._color, true, new CommonComboBoxRenderer( _rulePropertyPageBase._color, true));
		comboBox.setPreferredSize( new Dimension( 60, comboBox.getPreferredSize().height));
		_timeComboBoxes.add( comboBox);
		add( comboBox);

		label = new JLabel( " : ");
		label.setForeground( _rulePropertyPageBase._color);
		_timeLabels.add( label);
		add( label);
	
		comboBox = new ComboBox( CommonTool.get_minutes00(), _rulePropertyPageBase._color, true, new CommonComboBoxRenderer( _rulePropertyPageBase._color, true));
		comboBox.setPreferredSize( new Dimension( 60, comboBox.getPreferredSize().height));
		_timeComboBoxes.add( comboBox);
		add( comboBox);

		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		_timeTextField.setEnabled( enabled);
		for ( ComboBox comboBox:_timeComboBoxes)
			comboBox.setEnabled( enabled);
		for ( JLabel label:_timeLabels)
			label.setEnabled( enabled);
		super.setEnabled(enabled);
	}

	/**
	 * @param key
	 * @return
	 */
	public boolean set(String key) {
		if ( !TimeRule.is_time( key))
			return false;

		String[] words = TimeRule.get_time_elements( key);
		if ( null == words)
			return false;

		_timeTextField.setText( words[ 0]);
		_timeComboBoxes.get( 0).setSelectedItem( words[ 1]);
		_timeComboBoxes.get( 1).setSelectedItem( words[ 2]);
		return true;
	}

	/**
	 * @return
	 */
	public String get() {
		String value = "\"";
		if ( !_timeTextField.getText().equals( "")) {
			int day;
			try {
				day = Integer.parseInt( _timeTextField.getText());
				if ( 0 != day) {
					value += ( String.valueOf( day) + "/");
					_timeTextField.setText( String.valueOf( day));
				}
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				return null;
			}
		}
		value += ( ( String)_timeComboBoxes.get( 0).getSelectedItem() + ":" + ( String)_timeComboBoxes.get( 1).getSelectedItem() + "\"");
		return value;
	}
}
