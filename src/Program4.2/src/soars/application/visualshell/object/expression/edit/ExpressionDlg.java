/*
 * Created on 2005/10/28
 */
package soars.application.visualshell.object.expression.edit;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.window.Dialog;
import soars.common.utility.tool.expression.Expression;

/**
 * The dialog box to edit an expression.
 * @author kurata / SOARS project
 */
public class ExpressionDlg extends Dialog implements ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	private int _minimumWidth = -1;

	/**
	 * 
	 */
	private int _minimumHeight = -1;

	/**
	 * 
	 */
	private JLabel _functionLabel = null;

	/**
	 * 
	 */
	private JLabel _expressionLabel = null;

	/**
	 * 
	 */
	private JTextField _functionTextField1 = null;

	/**
	 * 
	 */
	private JTextField _functionTextField2 = null;

	/**
	 * 
	 */
	private JTextField _expressionTextField = null;

	/**
	 * 
	 */
	protected String[] _value = new String[] { "", "", ""};

	/**
	 * 
	 */
	private String[] _originalValue = new String[] { "", "", ""};

	/**
	 * 
	 */
	private ExpressionTable _expressionTable = null;

	/**
	 * 
	 */
	private List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param expressionTable the table component to edit the expressions
	 */
	public ExpressionDlg(Frame arg0, String arg1, boolean arg2, ExpressionTable expressionTable) {
		super(arg0, arg1, arg2);
		_expressionTable = expressionTable;
	}

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param expression the expression class
	 * @param expressionTable the table component to edit the expressions
	 */
	public ExpressionDlg(Frame arg0, String arg1, boolean arg2, Expression expression, ExpressionTable expressionTable) {
		super(arg0, arg1, arg2);
		_originalValue = expression._value;
		_expressionTable = expressionTable;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		link_to_cancel( getRootPane());


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup_functionTextField();

		insert_horizontal_glue();

		setup_expressiontextField();

		insert_horizontal_glue();

		setup_ok_and_cancel_button(
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			true, true);

		insert_horizontal_glue();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		adjust();


		return true;
	}

	/**
	 * 
	 */
	private void setup_functionTextField() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_functionLabel = new JLabel(
			ResourceManager.get_instance().get( "edit.expression.dialog.function"));
		_functionLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _functionLabel);

		panel.add( Box.createHorizontalStrut( 5));

		_functionTextField1 = new JTextField();
		_functionTextField1.setDocument( new TextExcluder( Constant._prohibitedExpressionCharacters1));
		_functionTextField1.setText( _originalValue[ 0]);
		_functionTextField1.setColumns( 5);
		_textUndoRedoManagers .add( new TextUndoRedoManager( _functionTextField1, this));

		link_to_cancel( _functionTextField1);

		panel.add( _functionTextField1);

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( "( ");
		panel.add( label);

		_functionTextField2 = new JTextField();
		_functionTextField2.setDocument( new TextExcluder( Constant._prohibitedExpressionCharacters2));
		_functionTextField2.setText( _originalValue[ 1]);
		_functionTextField2.setColumns( 20);
		_textUndoRedoManagers .add( new TextUndoRedoManager( _functionTextField2, this));

		link_to_cancel( _functionTextField2);

		panel.add( _functionTextField2);

		label = new JLabel( " )");
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_expressiontextField() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_expressionLabel = new JLabel(
			ResourceManager.get_instance().get( "edit.expression.dialog.expression"));
		_expressionLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _expressionLabel);

		panel.add( Box.createHorizontalStrut( 5));

		_expressionTextField = new JTextField();
		_expressionTextField.setDocument( new TextExcluder( Constant._prohibitedExpressionCharacters3));
		_expressionTextField.setText( _originalValue[ 2]);
		_expressionTextField.setColumns( 40);
		_textUndoRedoManagers .add( new TextUndoRedoManager( _expressionTextField, this));

		link_to_cancel( _expressionTextField);

		panel.add( _expressionTextField);

		panel.add( Box.createHorizontalStrut( 5));

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _functionLabel.getPreferredSize().width;
		width = Math.max( width, _expressionLabel.getPreferredSize().width);

		Dimension dimension = new Dimension( width,
			_functionLabel.getPreferredSize().height);
		_functionLabel.setPreferredSize( dimension);
		_expressionLabel.setPreferredSize( dimension);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_functionTextField1.requestFocusInWindow();

		_minimumWidth = getPreferredSize().width;
		_minimumHeight = getPreferredSize().height;

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( 0 > _minimumWidth || 0 > _minimumHeight)
					return;

				int width = getSize().width;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width, _minimumHeight);
			}
		});

		setAlwaysOnTop( true);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		String[] value = new String[ 3];

		if ( _functionTextField1.getText().equals( "")
			|| !_functionTextField1.getText().matches( "[^0-9]+.*")
			|| VisualShellExpressionManager.get_instance().is_reserved_word( _functionTextField1.getText()))
			return;

		if ( !_functionTextField1.getText().equals( _originalValue[ 0])) {
			if ( _expressionTable.contains( _functionTextField1.getText()))
				return;

			if ( !_originalValue[ 0].equals( "")) {
				WarningManager.get_instance().cleanup();
				boolean result1 = _expressionTable.get_visualShellExpressionManager().can_remove( _originalValue[ 0]);
				boolean result2 = LayerManager.get_instance().can_remove_expression( _originalValue[ 0]);
				if ( !result1 || !result2) {
					if ( !WarningManager.get_instance().isEmpty()) {
						WarningDlg1 warningDlg1 = new WarningDlg1(
							( Frame)getParent(),
							ResourceManager.get_instance().get( "warning.dialog1.title"),
							ResourceManager.get_instance().get( "warning.dialog1.message1"),
							this);
						warningDlg1.do_modal();
					}
					return;
				}
			}
		}

		value[ 0] = _functionTextField1.getText();


		Vector variables;
		if ( _functionTextField2.getText().equals( ""))
			variables = new Vector();
		else {
			if ( _functionTextField2.getText().endsWith( ","))
				return;

			variables = VisualShellExpressionManager.get_instance().get_variables( _functionTextField2.getText());
			if ( null == variables)
				return;

			if ( variables.contains( value[ 0]))
				return;
		}


		if ( !_originalValue[ 0].equals( "")) {
			Vector original_variables;
			if ( _originalValue[ 1].equals( ""))
				original_variables = new Vector();
			else {
				original_variables = VisualShellExpressionManager.get_instance().get_variables( _originalValue[ 1]);
				if ( null == original_variables)
					return;
			}

			if ( variables.size() != original_variables.size()) {
				WarningManager.get_instance().cleanup();
				if ( !_expressionTable.get_visualShellExpressionManager().can_remove( _originalValue[ 0])) {
					if ( !WarningManager.get_instance().isEmpty()) {
						WarningDlg1 warningDlg1 = new WarningDlg1(
							( Frame)getParent(),
							ResourceManager.get_instance().get( "warning.dialog1.title"),
							ResourceManager.get_instance().get( "warning.dialog1.message1"),
							this);
						warningDlg1.do_modal();
					}
					return;
				}
			}
		}

		value[ 1] = _functionTextField2.getText();


		if ( _expressionTextField.getText().equals( ""))
			return;

		if ( !_expressionTable.get_visualShellExpressionManager().is_correct( _expressionTextField.getText(), variables, value[ 0]))
			return;

		value[ 2] = _expressionTextField.getText();


		_value = value;

		super.on_ok(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.undo.UndoManager)
	 */
	public void on_changed(UndoManager undoManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.undo.UndoManager)
	 */
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent,UndoManager undoManager) {
	}
}
