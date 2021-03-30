/*
 * 2005/06/27
 */
package soars.common.utility.swing.message;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 */
public class MessageDlg extends Dialog implements Runnable {

	/**
	 * 
	 */
	protected String _id = "";

	/**
	 * 
	 */
	protected String _cancel = null;

	/**
	 * 
	 */
	private String _message = "";

	/**
	 * 
	 */
	protected Object[] _objects = null;

	/**
	 * 
	 */
	private IMessageCallback _messageCallback = null;

	/**
	 * 
	 */
	public boolean _callbackResult = false;

	/**
	 * 
	 */
	public boolean _canceled = false;

	/**
	 * 
	 */
	private JLabel _label = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param message
	 * @param messageCallback
	 * @param component
	 * @return
	 */
	public static boolean execute(Frame arg0, String arg1, boolean arg2, String id, String message, IMessageCallback messageCallback, Component component) {
		MessageDlg messageDlg = new MessageDlg( arg0, arg1, arg2, id, message, messageCallback);
		messageDlg.do_modal( component);
		return messageDlg._callbackResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param message
	 * @param messageCallback
	 * @param component
	 * @return
	 */
	public static boolean execute(Frame arg0, String arg1, boolean arg2, String id, String cancel, String message, IMessageCallback messageCallback, Component component) {
		MessageDlg messageDlg = new MessageDlg( arg0, arg1, arg2, id, cancel, message, messageCallback);
		messageDlg.do_modal( component);
		return messageDlg._callbackResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param message
	 * @param objects
	 * @param messageCallback
	 * @param component
	 * @return
	 */
	public static boolean execute(Frame arg0, String arg1, boolean arg2, String id, String message, Object[] objects, IMessageCallback messageCallback, Component component) {
		MessageDlg messageDlg = new MessageDlg( arg0, arg1, arg2, id, message, objects, messageCallback);
		messageDlg.do_modal( component);
		return messageDlg._callbackResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param message
	 * @param objects
	 * @param messageCallback
	 * @param component
	 * @return
	 */
	public static boolean execute(Frame arg0, String arg1, boolean arg2, String id, String cancel, String message, Object[] objects, IMessageCallback messageCallback, Component component) {
		MessageDlg messageDlg = new MessageDlg( arg0, arg1, arg2, id, cancel, message, objects, messageCallback);
		messageDlg.do_modal( component);
		return messageDlg._callbackResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param message
	 * @param messageCallback
	 */
	public MessageDlg(Frame arg0, String arg1, boolean arg2, String id, String message, IMessageCallback messageCallback) {
		super(arg0, arg1, arg2);
		_id = id;
		_message = message;
		_messageCallback = messageCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param message
	 * @param messageCallback
	 */
	public MessageDlg(Frame arg0, String arg1, boolean arg2, String id, String cancel, String message, IMessageCallback messageCallback) {
		super(arg0, arg1, arg2);
		_id = id;
		_cancel = cancel;
		_message = message;
		_messageCallback = messageCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param message
	 * @param objects
	 * @param messageCallback
	 */
	public MessageDlg(Frame arg0, String arg1, boolean arg2, String id, String message, Object[] objects, IMessageCallback messageCallback) {
		super(arg0, arg1, arg2);
		_id = id;
		_message = message;
		_objects = objects;
		_messageCallback = messageCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param message
	 * @param objects
	 * @param messageCallback
	 */
	public MessageDlg(Frame arg0, String arg1, boolean arg2, String id, String cancel, String message, Object[] objects, IMessageCallback messageCallback) {
		super(arg0, arg1, arg2);
		_id = id;
		_cancel = cancel;
		_message = message;
		_objects = objects;
		_messageCallback = messageCallback;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;



		setResizable( false);



		getContentPane().setLayout( new BorderLayout());



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( centerPanel);

		if ( !setup( centerPanel))
			return false;

		insert_horizontal_glue( centerPanel);

		getContentPane().add( centerPanel);



		setup_cancel_button();



		adjust();



		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);



		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_label = new JLabel( _message);
		panel.add( _label);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * 
	 */
	private void setup_cancel_button() {
		if ( null == _cancel)
			return;

		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		setup_cancel_button( panel, _cancel, false);
		southPanel.add( panel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");
	}

	/**
	 * 
	 */
	private void adjust() {
		if ( 400 > _label.getPreferredSize().width)
			_label.setPreferredSize( new Dimension( 400, _label.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		Thread thread = new Thread( this);
		thread.start();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		if ( null == _cancel)
			return;

		_canceled = true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if ( null != _messageCallback)
			_callbackResult = _messageCallback.message_callback( _id, _objects, this);

		dispose();
	}

	/**
	 * @param message
	 */
	public void update_message(String message) {
		_label.setText( message);
	}
}
