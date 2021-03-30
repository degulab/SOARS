/*
 * 2004/05/14
 */
package soars.application.animator.state;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

import soars.application.animator.main.AnimatorView;
import soars.application.animator.main.MainFrame;
import soars.application.animator.object.chart.ChartObjectMap;
import soars.application.animator.object.player.ObjectManager;
import soars.application.animator.object.player.agent.AgentObject;
import soars.application.animator.object.player.agent.AgentObjectManager;
import soars.application.animator.object.player.spot.ISpotObjectManipulator;
import soars.application.animator.object.player.spot.SpotObjectManager;
import soars.application.animator.object.scenario.ScenarioManager;
import soars.application.animator.setting.common.CommonProperty;
import soars.common.utility.tool.timer.ITimerTaskImplementCallback;
import soars.common.utility.tool.timer.Timer2;
import soars.common.utility.tool.timer.TimerTaskImplement;

/**
 * The "State pattern" class for animation.
 * @author kurata / SOARS project
 */
public class AnimationState extends StateBase implements ITimerTaskImplementCallback {

	/**
	 * 
	 */
	private Timer2[] _timer = new Timer2[ 2];

	/**
	 * 
	 */
	private TimerTaskImplement[] _timerTaskImplement = new TimerTaskImplement[ 2];

	/**
	 * 
	 */
	private int[] _id = new int[] { 0, 1};

	/**
	 * 
	 */
	private final long[] _delay = new long[] { 0l, 0l};

	/**
	 * 
	 */
	private final long[] _period = new long[] { 10l, 33l};

	/**
	 * 
	 */
	private boolean _loading = false;

	/**
	 * 
	 */
	private boolean _pausing = false;

	/**
	 * 
	 */
	private boolean _drawing = false;

	/**
	 * 
	 */
	private double _current_time = -1.0f;

	/**
	 * Creates the instance of the "State pattern" class for animation.
	 */
	public AnimationState() {
		super();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#setup()
	 */
	public boolean setup() {
		return super.setup();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#cleanup()
	 */
	public void cleanup() {
		stop_timer();
		super.cleanup();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_enter()
	 */
	public boolean on_enter(String new_state_name) {
		_loading = false;
		_drawing = false;
		_pausing = false;
		_current_time = -1.0f;
		ObjectManager.get_instance().prepare_for_animation( AnimatorView.get_instance());
		return super.on_enter(new_state_name);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_leave()
	 */
	public boolean on_leave(String new_state_name) {
		stop_timer();
		ScenarioManager.get_instance().set_current_position( 0);
		return super.on_leave(new_state_name);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_resized(java.awt.event.ComponentEvent)
	 */
	public void on_resized(ComponentEvent componentEvent) {
		super.on_resized(componentEvent);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#paint(java.awt.Graphics)
	 */
	public void paint(Graphics graphics) {
		ObjectManager.get_instance().animate( graphics, AnimatorView.get_instance().getVisibleRect(), AnimatorView.get_instance(), false);
		super.paint(graphics);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_backward_head()
	 */
	public void on_backward_head() {
		stop_timer();
		_pausing = true;
		ScenarioManager.get_instance().set_current_position( 0);
		ScenarioManager.get_instance().enable_retrieve_property_dialog( true);
		ScenarioManager.get_instance().enable_animation_slider_dialog( true);
		animate( false, true);
		super.on_backward_head();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_backward()
	 */
	public void on_backward() {
		stop_timer();
		_pausing = true;
		ScenarioManager.get_instance().backward();
		ScenarioManager.get_instance().enable_retrieve_property_dialog( true);
		ScenarioManager.get_instance().enable_animation_slider_dialog( true);
		animate( false, true);
		super.on_backward();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_backward_step()
	 */
	public void on_backward_step() {
		stop_timer();
		_pausing = true;
		ScenarioManager.get_instance().backward_step();
		ScenarioManager.get_instance().enable_retrieve_property_dialog( true);
		ScenarioManager.get_instance().enable_animation_slider_dialog( true);
		animate( false, true);
		super.on_backward_step();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_play()
	 */
	public void on_play() {
		ScenarioManager.get_instance().enable_retrieve_property_dialog( false);
		ScenarioManager.get_instance().enable_animation_slider_dialog( false);
		_drawing = false;
		if ( null == _timer[ 0] && null == _timer[ 1]) {
			if ( !_pausing)
				ScenarioManager.get_instance().set_current_position( 0);

			_pausing = false;
			start_timer();
		}
		super.on_play();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_pause()
	 */
	public void on_pause() {
		stop_timer();
		_pausing = true;
		ScenarioManager.get_instance().enable_retrieve_property_dialog( true);
		ScenarioManager.get_instance().enable_animation_slider_dialog( true);
		animate( false, true);
		super.on_pause();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_forward_step()
	 */
	public void on_forward_step() {
		stop_timer();
		_pausing = true;
		ScenarioManager.get_instance().forward_step();
		ScenarioManager.get_instance().enable_retrieve_property_dialog( true);
		ScenarioManager.get_instance().enable_animation_slider_dialog( true);
		animate( false, true);
		super.on_forward_step();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_forward()
	 */
	public void on_forward() {
		stop_timer();
		_pausing = true;
		ScenarioManager.get_instance().forward();
		ScenarioManager.get_instance().enable_retrieve_property_dialog( true);
		ScenarioManager.get_instance().enable_animation_slider_dialog( true);
		animate( false, true);
		super.on_forward();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_forward_tail()
	 */
	public void on_forward_tail() {
		stop_timer();
		_pausing = true;
		ScenarioManager.get_instance().forward_tail();
		ScenarioManager.get_instance().enable_retrieve_property_dialog( true);
		ScenarioManager.get_instance().enable_animation_slider_dialog( true);
		animate( false, true);
		super.on_forward_tail();
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.state.StateBase#set_current_position(int, boolean)
	 */
	public void set_current_position(int position, boolean slider) {
		stop_timer();
		_pausing = true;
		ScenarioManager.get_instance().set_current_position( position);
		ScenarioManager.get_instance().enable_retrieve_property_dialog( true);
		ScenarioManager.get_instance().enable_animation_slider_dialog( true);
		MainFrame.get_instance().update_user_interface();
		animate( false, slider);
	}

	/**
	 * 
	 */
	private void start_timer() {
		for ( int i = 0; i < _timer.length; ++i) {
			if ( null == _timer[ i]) {
				_timer[ i] = new Timer2();
				_timerTaskImplement[ i] = new TimerTaskImplement( _id[ i], this);
				_timer[ i].schedule( _timerTaskImplement[ i], _delay[ i], _period[ i]);
			}
		}
	}

	/**
	 * 
	 */
	private void stop_timer() {
		for ( int i = 0; i < _timer.length; ++i) {
			if ( null != _timer[ i]) {
				_timer[ i].cancel();
				_timer[ i] = null;
			}
		}
		_loading = false;
		_drawing = false;
		_pausing = false;
		_current_time = -1.0f;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.tool.timer.ITimerTaskImplementCallback#execute_timer_task(int)
	 */
	public void execute_timer_task(int id) {
		if ( _id[ 0] == id) {
			if ( _loading)
				return;

			_loading = true;

			ScenarioManager.get_instance().read();

			_loading = false;
		} else if ( _id[ 1] == id) {
			if ( _drawing)
				return;

			if ( ScenarioManager.get_instance().is_tail()) {
				if ( CommonProperty.get_instance()._repeat)
					ScenarioManager.get_instance().rewind();
				else {
					_drawing = true;
					animate( false, true);
					_drawing = false;
					return;
				}
			}

			_drawing = true;
			animate( true, true);
			_drawing = false;
		}
	}

	/**
	 * @param next
	 * @param slider
	 */
	private void animate(boolean next, boolean slider) {
		Graphics graphics = AnimatorView.get_instance().getGraphics();
		if ( null == graphics) {
			System.out.println( "execute_timer_task : getGraphics() returned null!");
			_drawing = false;
			return;
		}

		ObjectManager.get_instance().animate( graphics, AnimatorView.get_instance().getVisibleRect(), AnimatorView.get_instance(), next);

		graphics.dispose();

		if ( slider)
			ScenarioManager.get_instance().update_animation_slider_dialog();

		MainFrame.get_instance().update( ScenarioManager.get_instance().get_information());

		update_charts();
	}

	/**
	 * 
	 */
	public void update_charts() {
		double current_time = ScenarioManager.get_instance().get_current_time();
		if ( current_time != _current_time) {
			_current_time = current_time;
			ChartObjectMap.get_instance().indicate( _current_time);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.state.StateBase#get_tooltip_text(java.awt.event.MouseEvent)
	 */
	public String get_tooltip_text(MouseEvent mouseEvent) {
		if ( !_pausing)
			return null;

		AgentObject agentObject = get_agent( mouseEvent.getPoint());
		if ( null != agentObject)
			return agentObject.get_tooltip_text( "animation");
		else {
			ISpotObjectManipulator spotObjectManipulator
				= SpotObjectManager.get_instance().get_spot( mouseEvent.getPoint());
			return ( ( null == spotObjectManipulator)
				? null : spotObjectManipulator.get_tooltip_text( "animation"));
		}
	}

	/**
	 * @param mouse_position
	 * @return
	 */
	private AgentObject get_agent(Point mouse_position) {
		AgentObject agentObject = AgentObjectManager.get_instance().get_agent( mouse_position, "animation");
		if ( null == agentObject)
			return null;

		ISpotObjectManipulator spotObjectManipulator
			= ScenarioManager.get_instance().get_spot( agentObject);
		if ( null == spotObjectManipulator)
			return null;

		return agentObject;
	}
}
