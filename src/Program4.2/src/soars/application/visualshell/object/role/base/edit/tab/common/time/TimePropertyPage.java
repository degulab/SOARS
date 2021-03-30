/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.common.time;

import java.awt.Color;
import java.awt.Frame;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.application.visualshell.object.role.base.object.common.time.TimeRule;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextField;

/**
 * @author kurata
 *
 */
public class TimePropertyPage extends RulePropertyPageBase {

	/**
	 * 
	 */
	protected CheckBox _spot_checkBox = null;

	/**
	 * 
	 */
	protected ObjectSelector _spot_selector = null;

	/**
	 * 
	 */
	protected CheckBox _spot_variable_checkBox = null;

	/**
	 * 
	 */
	protected ComboBox _spot_variable_comboBox = null;

	/**
	 * @param title
	 * @param kind
	 * @param type
	 * @param color
	 * @param role
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public TimePropertyPage(String title, String kind,
		String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/**
	 * @param value
	 * @param time_textField
	 * @param time_comboBoxes
	 * @param time_variable_comboBox
	 * @param radioButtons
	 */
	protected void set(String value, TextField time_textField, ComboBox[] time_comboBoxes, ComboBox time_variable_comboBox, RadioButton[] radioButtons) {
		if ( TimeRule.is_time( value)) {
			String[] elements = TimeRule.get_time_elements( value);
			if ( null == elements)
				return;

			time_textField.setText( elements[ 0]);
			time_comboBoxes[ 0].setSelectedItem( elements[ 1]);
			time_comboBoxes[ 1].setSelectedItem( elements[ 2]);
			radioButtons[ 0].setSelected( true);
		} else {
			if ( value.startsWith( "$")) {
				time_textField.setText( value);
				time_comboBoxes[ 0].setSelectedIndex( 0);
				time_comboBoxes[ 1].setSelectedIndex( 0);
				radioButtons[ 0].setSelected( true);
			} else {
				if ( value.equals( Constant._currentTimeName)) {
					if ( 2 < radioButtons.length)
						radioButtons[ 2].setSelected( true);
				} else {
					time_variable_comboBox.setSelectedItem( value);
					radioButtons[ 1].setSelected( true);
				}
			}
		}
	}

	/**
	 * @param time_textField
	 * @param time_comboBoxes
	 * @param time_variable_comboBox
	 * @param radioButtons
	 * @return
	 */
	protected String get(TextField time_textField, ComboBox[] time_comboBoxes, ComboBox time_variable_comboBox, RadioButton[] radioButtons) {
		if ( radioButtons[ 0].isSelected()) {
			if ( time_textField.getText().startsWith( "$")) {
				if ( time_textField.getText().equals( "$")
					|| time_textField.getText().equals( "$Name")
					|| time_textField.getText().equals( "$Role")
					|| time_textField.getText().equals( "$Spot")
					|| 0 <= time_textField.getText().indexOf( Constant._experimentName)
					|| 0 <= time_textField.getText().indexOf( Constant._currentTimeName))
					return null;

				if ( 0 < time_textField.getText().indexOf( "$", 1)
					|| 0 < time_textField.getText().indexOf( ")", 1))
					return null;

				return time_textField.getText();
			} else {
				String day = TimeRule.get_day( time_textField.getText());
				return ( ( day.equals( "")) ? "" : ( day + "/"))
					+ ( ( String)time_comboBoxes[ 0].getSelectedItem()
					+ ":"
					+ ( String)time_comboBoxes[ 1].getSelectedItem());
			}
		} else if ( radioButtons[ 1].isSelected())
			return ( String)time_variable_comboBox.getSelectedItem();
		else
			return Constant._currentTimeName;
	}
}
