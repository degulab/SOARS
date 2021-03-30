/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.condition.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPageBase;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 *
 */
public class CollectionAndListConditionPropertyPageBase extends RulePropertyPageBase {

	/**
	 * 
	 */
	protected RadioButton[] _radioButtons1 = null;

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
	 * 
	 */
	protected CheckBox _checkBox = null;

	/**
	 * 
	 */
	protected ComboBox _comboBox = null;

	/**
	 * 
	 */
	protected JLabel[] _label = null;

	/**
	 * 
	 */
	protected JLabel _dummy1 = null;

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
	public CollectionAndListConditionPropertyPageBase(String title, String kind,
		String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/**
	 * @param parent
	 */
	protected void setup_spot_checkBox_and_spot_selector(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy1 = new JLabel();
		panel.add( _dummy1);

		_spot_checkBox = create_checkBox(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.spot.check.box.name")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.spot.check.box.name"),
			false, true);
		_spot_checkBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_spot_selector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				on_update( ItemEvent.SELECTED == arg0.getStateChange(),
					_spot_selector,
					_spot_variable_checkBox,
					_spot_variable_comboBox);
			}
		});
		panel.add( _spot_checkBox);

		_spot_selector = create_spot_selector( true, true, panel);


		_spot_variable_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.spot.variable.check.box.name"),
			true, true);
		_spot_variable_checkBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				on_update( _spot_checkBox.isSelected(),
					_spot_selector,
					_spot_variable_checkBox,
					_spot_variable_comboBox);
			}
		});
		panel.add( _spot_variable_checkBox);

		_spot_variable_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _spot_variable_comboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	protected void setup_header(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.check.box.denial"),
			true, true);
		panel.add( _checkBox);

		_label[ 0] = create_label(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.header")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.header"),
			true);
		panel.add( _label[ 0]);

		_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _comboBox);

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void adjust() {
		int width = _checkBox.getPreferredSize().width;
		for ( int i = 0; i < _radioButtons1.length; ++i)
			width = Math.max( width, _radioButtons1[ i].getPreferredSize().width);

		_dummy1.setPreferredSize( new Dimension( width, _dummy1.getPreferredSize().height));
		_checkBox.setPreferredSize( new Dimension( width, _checkBox.getPreferredSize().height));

		Dimension dimension = new Dimension( width,
			_radioButtons1[ 0].getPreferredSize().height);
		for ( int i = 0; i < _radioButtons1.length; ++i)
			_radioButtons1[ i].setPreferredSize( dimension);


		width = _spot_checkBox.getPreferredSize().width;
		for ( int i = 0; i < _label.length; ++i)
			width = Math.max( width, _label[ i].getPreferredSize().width);

		_spot_checkBox.setPreferredSize( new Dimension( width, _spot_checkBox.getPreferredSize().height));

		dimension = new Dimension( width,
			_label[ 0].getPreferredSize().height);
		for ( int i = 0; i < _label.length; ++i)
			_label[ i].setPreferredSize( dimension);
	}

	/**
	 * 
	 */
	protected void set_handler() {
		_spot_selector.set_handler( this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPageBase#on_setup_completed()
	 */
	public void on_setup_completed() {
		reset( _spot_selector, _spot_variable_checkBox, _spot_variable_comboBox);
		super.on_setup_completed();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.tab.TabbedPage#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		_parent.on_apply( this, actionEvent);
	}
}
