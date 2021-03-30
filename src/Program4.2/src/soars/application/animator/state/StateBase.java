/*
 * 2004/05/14
 */
package soars.application.animator.state;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import soars.application.animator.main.AnimatorView;

/**
 * The "State pattern" base class.
 * @author kurata / SOARS project
 */
public class StateBase {

	/**
	 * Creates the instance of the "State pattern" base class.
	 */
	public StateBase() {
		super();
	}

	/**
	 * Returns true if this object is initialized successfully.
	 * @return
	 */
	public boolean setup() {
		return true;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
	}

	/**
	 * Returns true for changing into the specified state successfully.
	 * @param newStateName the name of the specified state
	 * @return true for changing into the specified state successfully
	 */
	public boolean change(String newStateName) {
		return true;
	}

	/**
	 * Returns true if the current state has changed into this state successfully.
	 * @param newStateName the name of this state
	 * @return true if the current state has changed into this state successfully
	 */
	public boolean on_enter(String newStateName) {
		AnimatorView.get_instance().setToolTipText( "");
		return true;
	}

	/**
	 * Returns true if the current state has changed into the specified state successfully.
	 * @param newStateName the name of the specified state
	 * @return true if the current state has changed into the specified state successfully
	 */
	public boolean on_leave(String newStateName) {
		return true;
	}

	/**
	 * Cancels all selections of objects.
	 */
	public void cancel() {
	}

	/**
	 * Invoked when the position or size of the main window is changed.
	 * @param componentEvent a low-level event which indicates that a component moved, changed size, or changed visibility (also, the root class for the other component-level events)
	 */
	public void on_resized(ComponentEvent componentEvent) {
	}

	/**
	 * Invoked when the mouse button is double clicked.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 */
	public void on_mouse_left_double_click(MouseEvent mouseEvent) {
	}

	/**
	 * Invoked when the mouse left button is pressed.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 */
	public void on_mouse_left_down(MouseEvent mouseEvent) {
	}

	/**
	 * Invoked when the mouse left button is released.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 */
	public void on_mouse_left_up(MouseEvent mouseEvent) {
	}

	/**
	 * Invoked when the mouse is moved.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 */
	public void on_mouse_moved(MouseEvent mouseEvent) {
	}

	/**
	 * Invoked when the mouse right button is pressed.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 */
	public void on_mouse_right_down(MouseEvent mouseEvent) {
	}

	/**
	 * Invoked when the mouse right button is released.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 */
	public void on_mouse_right_up(MouseEvent mouseEvent) {
	}

	/**
	 * Invoked when the mouse is dragged.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 */
	public void on_mouse_dragged(MouseEvent mouseEvent) {
	}

	/**
	 * On drawing. 
	 * @param graphics the graphics object of JAVA
	 */
	public void paint(Graphics graphics) {
	}

	/**
	 * Invoked when the key is pressed.
	 * @param keyEvent an event which indicates that a keystroke occurred in a component
	 */
	public void on_key_pressed(KeyEvent keyEvent) {
	}

	/**
	 * Invoked when the key is pressed.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_key_pressed(ActionEvent actionEvent) {
	}

	/**
	 * Goes backward head.
	 */
	public void on_backward_head() {
	}

	/**
	 * Starts or Restarts the animation.
	 */
	public void on_play() {
	}

	/**
	 * Pauses the animation.
	 */
	public void on_pause() {
	}

	/**
	 * Goes backward.
	 */
	public void on_backward() {
	}

	/**
	 * Goes backward step.
	 */
	public void on_backward_step() {
	}

	/**
	 * Goes forward step.
	 */
	public void on_forward_step() {
	}

	/**
	 * Goes forward.
	 */
	public void on_forward() {
	}

	/**
	 * Goes forward tail.
	 */
	public void on_forward_tail() {
	}

	/**
	 * Sets the current position of the animation with the specified position.
	 * @param position the specified position
	 * @param slider whether the position slider is updated.
	 */
	public void set_current_position(int position, boolean slider) {
	}

	/**
	 * Returns the appropriate tool tip text.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 * @return the appropriate tool tip text
	 */
	public String get_tooltip_text(MouseEvent mouseEvent) {
		return null;
	}
}