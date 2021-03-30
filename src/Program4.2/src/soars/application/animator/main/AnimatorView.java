/*
 * 2005/01/28
 */
package soars.application.animator.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;

import soars.application.animator.object.chart.ChartObjectMap;
import soars.application.animator.object.player.ObjectManager;
import soars.application.animator.object.scenario.ScenarioManager;
import soars.application.animator.state.AnimationState;
import soars.application.animator.state.EditState;
import soars.application.animator.state.StateManager;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.window.View;

/**
 * The Animator view class.
 * @author kurata / SOARS project
 */
public class AnimatorView extends View implements IMessageCallback {

	/**
	 * Synchronized object.
	 */
	static private Object _lock = new Object();

	/**
	 * Instance of the Animator view class.
	 */
	static private AnimatorView _animatorView = null;

	/**
	 * Background color of this view.
	 */
	private final Color _backgroundColor = new Color( 255, 255, 255);

	/**
	 * Returns the instance of the Animator view class.
	 * @return the instance of the Animator view class
	 */
	public static AnimatorView get_instance() {
		return _animatorView;
	}

	/**
	 * Creates the instance of the Animator view class.
	 * @param arg0 a boolean, true for double-buffering, which uses additional memory space to achieve fast, flicker-free updates
	 * @param mainFrame the instance of the main window.
	 */
	public AnimatorView(boolean arg0, MainFrame mainFrame) {
		super(arg0);
		setBackground( _backgroundColor);
		_animatorView = this;
	}

	/**
	 * Returns true if the current state is "edit".
	 * @return true if the current state is "edit"
	 */
	public boolean is_state_edit() {
		return StateManager.get_instance().is_current_state(
			ResourceManager.get( "state.edit"));
	}

	/**
	 * Returns true if the current state is "animation".
	 * @return true if the current state is "animation"
	 */
	public boolean is_state_animation() {
		return StateManager.get_instance().is_current_state(
			ResourceManager.get( "state.animation"));
	}

	/**
	 * Cancels all selections of objects.
	 */
	public void cancel() {
		StateManager.get_instance().cancel();
	}

	/**
	 * Returns true if this component is initialized successfully.
	 * @return true if this component is initialized successfully
	 */
	private boolean setup() {
		if ( !setup_state_manager())
			return false;

		return true;
	}

	/**
	 * Returns true if the "State pattern" manager is initialized successfully.
	 * @return true if the "State pattern" manager is initialized successfully
	 */
	private boolean setup_state_manager() {
		if ( !StateManager.get_instance().setup( ""))
			return false;

		EditState editState = new EditState();
		if ( !editState.setup())
			return false;

		StateManager.get_instance().put( ResourceManager.get( "state.edit"), editState);

		AnimationState animationState = new AnimationState();
		if ( !animationState.setup())
			return false;

		StateManager.get_instance().put( ResourceManager.get( "state.animation"), animationState);

		return true;
	}

	/**
	 * Resets all.
	 */
	public void reset() {
		ObjectManager.get_instance().cleanup();
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		StateManager.get_instance().cleanup();
		reset();
	}

	/**
	 * Invoked just before this component is destroyed.
	 */
	public void on_closing() {
		cleanup();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		if ( !setup())
			return false;

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_component_resized(java.awt.event.ComponentEvent)
	 */
	protected void on_component_resized(ComponentEvent componentEvent) {
		StateManager.get_instance().on_resized( componentEvent);
		super.on_component_resized(componentEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		StateManager.get_instance().on_mouse_left_double_click(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_left_down(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_down(MouseEvent mouseEvent) {
		StateManager.get_instance().on_mouse_left_down(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_left_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_up(MouseEvent mouseEvent) {
		StateManager.get_instance().on_mouse_left_up(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_dragged(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_dragged(MouseEvent mouseEvent) {
		StateManager.get_instance().on_mouse_dragged(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_right_down(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_down(MouseEvent mouseEvent) {
		StateManager.get_instance().on_mouse_right_down(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		StateManager.get_instance().on_mouse_right_up(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_moved(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_moved(MouseEvent mouseEvent) {
		StateManager.get_instance().on_mouse_moved(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
	 */
	public String getToolTipText(MouseEvent arg0) {
		return StateManager.get_instance().get_tooltip_text( arg0);
	}

	/* (Non Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics arg0) {
		super.paintComponent( arg0);
		StateManager.get_instance().paint( arg0);
	}

	/**
	 * Invoked when the key is pressed.
	 * @param keyEvent An event which indicates that a keystroke occurred in a component
	 */
	public void on_key_pressed(KeyEvent keyEvent) {
		StateManager.get_instance().on_key_pressed( keyEvent);
	}

	/**
	 * Invoked when the key is pressed.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_key_pressed(ActionEvent actionEvent) {
		StateManager.get_instance().on_key_pressed( actionEvent);
	}

	/**
	 * Returns true if the data are loaded from the specified file successfully.
	 * @param file the specified file
	 * @return true if the data are loaded from the specified file successfully
	 */
	public boolean on_file_open(File file) {
		reset();

		Graphics2D graphics2D = ( Graphics2D)getGraphics();

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get( "application.title"), true,
			"on_file_open", ResourceManager.get( "file.open.show.message"), new Object[] { file, graphics2D}, this, MainFrame.get_instance())) {
			graphics2D.dispose();
			reset();
			setPreferredSize( new Dimension());
			updateUI();
			return false;
		}

		graphics2D.dispose();

		StateManager.get_instance().reset();
		StateManager.get_instance().change( ResourceManager.get( "state.edit"));

		MainFrame.get_instance().setTitle(
			ResourceManager.get( "application.title")
				+ ( ( Application._demo) ? Application.get_title() : ( " - " + file.getName())));
		MainFrame.get_instance().update( ScenarioManager.get_instance().get_information());

		return true;
	}

	/**
	 * Closes the current opened file.
	 */
	public void on_file_close() {
		reset();
		setPreferredSize( new Dimension());

		updateUI();
		StateManager.get_instance().reset();
		MainFrame.get_instance().setTitle( ResourceManager.get( "application.title"));
		System.gc();
	}

	/**
	 * Returns true if the current data are stored to the current selected file successfully.
	 * @return true if the current data are stored to the current selected file successfully
	 */
	public boolean on_file_save() {
		StateManager.get_instance().cancel();

		boolean result = MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get( "application.title"), true,
			"on_file_save", ResourceManager.get( "file.save.show.message"), this, MainFrame.get_instance());

		MainFrame.get_instance().setTitle(
			ResourceManager.get( "application.title")
			+ " - " + ObjectManager.get_instance().get_current_file().getName());

		return result;
	}

	/**
	 * Returns true if the current data are stored to the specified file successfully.
	 * @param file the specified file
	 * @return true if the current data are stored to the specified file successfully
	 */
	public boolean on_file_save_as(File file) {
		boolean result = MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get( "application.title"), true,
			"on_file_save_as", ResourceManager.get( "file.saveas.show.message"),
			new Object[] { file}, this, MainFrame.get_instance());

		if ( result)
			MainFrame.get_instance().setTitle(
				ResourceManager.get( "application.title")
					+ " - " + file.getName());

		return result;
	}

	/**
	 * Returns true if the data are loaded from the log files in the specified directory successfully.
	 * @param directory the specified directory
	 * @return true if the data are loaded from the log files in the specified directory successfully
	 */
	public boolean on_file_import(File directory) {
		reset();

		Graphics2D graphics2D = ( Graphics2D)getGraphics();

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get( "application.title"), true,
			"on_file_import1", ResourceManager.get( "file.import.show.message"),
			new Object[] { directory, graphics2D}, this, MainFrame.get_instance())) {
			graphics2D.dispose();
			reset();
			setPreferredSize( new Dimension());
			updateUI();
			return false;
		}

		graphics2D.dispose();

		MainFrame.get_instance().update( ScenarioManager.get_instance().get_information());

		StateManager.get_instance().reset();
		StateManager.get_instance().change( ResourceManager.get( "state.edit"));

		return true;
	}

	/**
	 * Returns true if the data are loaded from the log files in the specified directory successfully.
	 * @param parent_directory the specified directory
	 * @param root_directory the specified directory
	 * @return true if the data are loaded from the log files in the specified directory successfully
	 */
	public boolean on_file_import(File parent_directory, File root_directory) {
		reset();

		Graphics2D graphics2D = ( Graphics2D)getGraphics();

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get( "application.title"), true,
			"on_file_import2", ResourceManager.get( "file.import.show.message"),
			new Object[] { parent_directory, root_directory, graphics2D}, this, MainFrame.get_instance())) {
			graphics2D.dispose();
			reset();
			setPreferredSize( new Dimension());
			updateUI();
			return false;
		}

		graphics2D.dispose();

		MainFrame.get_instance().update( ScenarioManager.get_instance().get_information());

		StateManager.get_instance().reset();
		StateManager.get_instance().change( ResourceManager.get( "state.edit"));

		return true;
	}

	/**
	 * Returns true if the graphics data are loaded from the specified graphics data file successfully.
	 * @param file the specified file
	 * @return true if the graphics data are loaded from the specified graphics data file successfully
	 */
	public boolean on_file_import_graphic_data(File file) {
		Graphics2D graphics2D = ( Graphics2D)getGraphics();

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get( "application.title"), true,
			"on_file_import_graphic_data", ResourceManager.get( "file.import.graphic.data.show.message"),
			new Object[] { file, graphics2D}, this, MainFrame.get_instance())) {
			graphics2D.dispose();
			reset();
			setPreferredSize( new Dimension());
			updateUI();
			return false;
		}

		graphics2D.dispose();

		StateManager.get_instance().reset();
		StateManager.get_instance().change( ResourceManager.get( "state.edit"));

		return true;
	}

	/**
	 * Returns true if the graphics data are stored to the specified graphics data file successfully.
	 * @param file the specified file
	 * @return true if the graphics data are stored to the specified graphics data file successfully
	 */
	public boolean on_file_export_graphic_data(File file) {
		return MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get( "application.title"), true,
			"on_file_export_graphic_data", ResourceManager.get( "file.export.graphic.data.show.message"),
			new Object[] { file}, this, MainFrame.get_instance());
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "on_file_open"))
			return ObjectManager.get_instance().load( ( File)objects[ 0], ( Graphics2D)objects[ 1]);
		else if ( id.equals( "on_file_save"))
			return ObjectManager.get_instance().save();
		else if ( id.equals( "on_file_save_as"))
			return ObjectManager.get_instance().save_as( ( File)objects[ 0]);
		else if ( id.equals( "on_file_import1"))
			return ObjectManager.get_instance().import_data( ( File)objects[ 0], ( Graphics2D)objects[ 1]);
		else if ( id.equals( "on_file_import2"))
			return ObjectManager.get_instance().import_data( ( File)objects[ 0], ( File)objects[ 1], ( Graphics2D)objects[ 2]);
		else if ( id.endsWith( "on_file_import_graphic_data"))
			return ObjectManager.get_instance().import_graphic_data( ( File)objects[ 0], ( Graphics2D)objects[ 1]);
		else if ( id.equals( "on_file_export_graphic_data"))
			return ObjectManager.get_instance().export_graphic_data( ( File)objects[ 0]);

		return true;
	}

	/**
	 * Edits Animator common properties.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_common_property(ActionEvent actionEvent) {
		StateManager.get_instance().cancel();
		if ( !ObjectManager.get_instance().edit_common_property( this, MainFrame.get_instance()))
			return;

		repaint();
	}

	/**
	 * Edits the data of the specified agent.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_agent(ActionEvent actionEvent) {
		StateManager.get_instance().cancel();
		if ( !ObjectManager.get_instance().edit_agent( this, MainFrame.get_instance()))
			return;

		repaint();
	}

	/**
	 * Edits the data of the specified spot.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_spot(ActionEvent actionEvent) {
		StateManager.get_instance().cancel();
		if ( !ObjectManager.get_instance().edit_spot( this, MainFrame.get_instance()))
			return;

		repaint();
	}

	/**
	 * Edits the properties of the specified agent.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_agent_property(ActionEvent actionEvent) {
		StateManager.get_instance().cancel();
		if ( !ObjectManager.get_instance().edit_agent_property( this, MainFrame.get_instance()))
			return;

		repaint();
	}

	/**
	 * Edits the properties of the specified spot.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_spot_property(ActionEvent actionEvent) {
		StateManager.get_instance().cancel();
		if ( !ObjectManager.get_instance().edit_spot_property( this, MainFrame.get_instance()))
			return;

		repaint();
	}

	/**
	 * Invoked when the "About SOARS Animator" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_about(ActionEvent actionEvent) {
		StateManager.get_instance().cancel();
	}

	/**
	 * Goes backward head.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_backward_head(ActionEvent actionEvent) {
		StateManager.get_instance().change( ResourceManager.get( "state.animation"));
		StateManager.get_instance().on_backward_head();
	}

	/**
	 * Goes backward.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_backward(ActionEvent actionEvent) {
		StateManager.get_instance().change( ResourceManager.get( "state.animation"));
		StateManager.get_instance().on_backward();
	}

	/**
	 * Goes backward step.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_backward_step(ActionEvent actionEvent) {
		StateManager.get_instance().change( ResourceManager.get( "state.animation"));
		StateManager.get_instance().on_backward_step();
	}

	/**
	 * Starts or Restarts the animation.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_play(ActionEvent actionEvent) {
		StateManager.get_instance().change( ResourceManager.get( "state.animation"));
		StateManager.get_instance().on_play();
	}

	/**
	 * Pauses the animation.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_pause(ActionEvent actionEvent) {
		StateManager.get_instance().on_pause();
	}

	/**
	 * Stop the animation.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_stop(ActionEvent actionEvent) {
		StateManager.get_instance().change( ResourceManager.get( "state.edit"));
		//ScenarioManager.get_instance().go( 0);
		ScenarioManager.get_instance().dispose_retrieve_property_dialog();
		ScenarioManager.get_instance().dispose_animation_slider_dialog();
		MainFrame.get_instance().update( ScenarioManager.get_instance().get_information());
		ChartObjectMap.get_instance().clear_indication();
		repaint();
	}

	/**
	 * Goes forward step.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_forward_step(ActionEvent actionEvent) {
		StateManager.get_instance().change( ResourceManager.get( "state.animation"));
		StateManager.get_instance().on_forward_step();
	}

	/**
	 * Goes forward.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_forward(ActionEvent actionEvent) {
		StateManager.get_instance().change( ResourceManager.get( "state.animation"));
		StateManager.get_instance().on_forward();
	}

	/**
	 * Goes forward tail.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_forward_tail(ActionEvent actionEvent) {
		StateManager.get_instance().change( ResourceManager.get( "state.animation"));
		StateManager.get_instance().on_forward_tail();
	}

	/**
	 * Returns true if the specified agent property is found.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 * @return true if the specified agent property is found
	 */
	public boolean on_retrieve_agent_property(ActionEvent actionEvent) {
		if ( !ObjectManager.get_instance().retrieve_agent_property( MainFrame.get_instance()))
			return false;

		StateManager.get_instance().change( ResourceManager.get( "state.animation"));
		StateManager.get_instance().on_pause();
		return true;
	}

	/**
	 * Returns true if the specified spot property is found.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 * @return true if the specified spot property is found
	 */
	public boolean on_retrieve_spot_property(ActionEvent actionEvent) {
		if ( !ObjectManager.get_instance().retrieve_spot_property( MainFrame.get_instance()))
			return false;

		StateManager.get_instance().change( ResourceManager.get( "state.animation"));
		StateManager.get_instance().on_pause();
		return true;
	}

	/**
	 * Shows the animation slider.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_animation_slider(ActionEvent actionEvent) {
		StateManager.get_instance().change( ResourceManager.get( "state.animation"));
		ScenarioManager.get_instance().show_animation_slider( MainFrame.get_instance());
		StateManager.get_instance().on_pause();
	}
}
