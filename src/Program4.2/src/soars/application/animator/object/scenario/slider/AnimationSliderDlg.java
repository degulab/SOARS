/*
 * 2005/03/31
 */
package soars.application.animator.object.scenario.slider;

import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import soars.application.animator.object.scenario.ScenarioManager;
import soars.application.animator.state.StateManager;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box to set the position of the scenario.
 * @author kurata / SOARS project
 */
public class AnimationSliderDlg extends Dialog {

	/**
	 * 
	 */
	private JSlider _animation_slider = null;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 */
	public AnimationSliderDlg(Frame arg0, String arg1, boolean arg2) {
		super(arg0, arg1, arg2);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;

		setResizable( false);

		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		setup_animation_slider();

		return true;
	}

	/**
	 * 
	 */
	private void setup_animation_slider() {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 5));

		_animation_slider = new JSlider();
		_animation_slider.setMinimum( 1);
		_animation_slider.setMaximum( ScenarioManager.get_instance().get_size());
		_animation_slider.setValue( ScenarioManager.get_instance().get_current_position());
		_animation_slider.setPaintTicks( true);
		_animation_slider.setPaintLabels( true);
		_animation_slider.setMajorTickSpacing( _animation_slider.getMaximum() / 5);
		_animation_slider.setMinorTickSpacing( _animation_slider.getMajorTickSpacing() / 4);
//		_animation_slider.setMajorTickSpacing( 20);
//		_animation_slider.setMinorTickSpacing( 5);
		_animation_slider.addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				on_state_changed( arg0);
			}
		});

		panel.add( _animation_slider);
		getContentPane().add( panel);
	}

	/**
	 * @param changeEvent
	 */
	protected void on_state_changed(ChangeEvent changeEvent) {
		if ( _animation_slider.isEnabled() && !_animation_slider.getValueIsAdjusting())
			StateManager.get_instance().set_current_position( _animation_slider.getValue() - 1, false);
	}

	/**
	 * Sets whether or not the slider is enabled.
	 * @param enable true if the slider should be enabled, false otherwise
	 */
	public void enable_user_interface(boolean enable) {
		_animation_slider.setEnabled( enable);
	}

	/**
	 * Sets the position of the slider with the specified new position.
	 * @param index the specified new position
	 */
	public void update(int index) {
		_animation_slider.setValue( index + 1);
	}
}
