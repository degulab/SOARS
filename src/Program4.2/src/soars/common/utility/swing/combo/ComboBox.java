/*
 * Created on 2006/01/28
 */
package soars.common.utility.swing.combo;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import soars.common.utility.swing.tab.TabbedPage;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 */
public class ComboBox extends JComboBox {

	/**
	 * 
	 */
	static protected Color[] _textFieldDefaultColors = new Color[ 3];

	/**
	 * 
	 */
	private Color _color = null;

	/**
	 * 
	 */
	private JTextField _textField = null;

	/**
	 * @param items
	 * @param right
	 * @param basicComboBoxRenderer
	 * @return
	 */
	public static ComboBox create(Object[] items, boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( right, basicComboBoxRenderer);
		else
			comboBox = new ComboBox( items, right, basicComboBoxRenderer);

		return comboBox;
	}

	/**
	 * @param items
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @return
	 */
	public static ComboBox create(Object[] items, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( color, right, basicComboBoxRenderer);
		else
			comboBox = new ComboBox( items, color, right, basicComboBoxRenderer);

		return comboBox;
	}

	/**
	 * @param items
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param tabbedPage
	 * @return
	 */
	public static ComboBox create(Object[] items, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, TabbedPage tabbedPage) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( right, basicComboBoxRenderer, tabbedPage);
		else
			comboBox = new ComboBox( items, right, basicComboBoxRenderer, tabbedPage);

		return comboBox;
	}

	/**
	 * @param items
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param tabbedPage
	 * @return
	 */
	public static ComboBox create(Object[] items, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, TabbedPage tabbedPage) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( color, right, basicComboBoxRenderer, tabbedPage);
		else
			comboBox = new ComboBox( items, color, right, basicComboBoxRenderer, tabbedPage);

		return comboBox;
	}

	/**
	 * @param items
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 * @return
	 */
	public static ComboBox create(Object[] items, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( right, basicComboBoxRenderer, dialog);
		else
			comboBox = new ComboBox( items, right, basicComboBoxRenderer, dialog);

		return comboBox;
	}

	/**
	 * @param items
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 * @return
	 */
	public static ComboBox create(Object[] items, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( color, right, basicComboBoxRenderer, dialog);
		else
			comboBox = new ComboBox( items, color, right, basicComboBoxRenderer, dialog);

		return comboBox;
	}

	/**
	 * @param items
	 * @param width
	 * @param right
	 * @param basicComboBoxRenderer
	 * @return
	 */
	public static ComboBox create(Object[] items, int width, boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( right, basicComboBoxRenderer);
		else
			comboBox = new ComboBox( items, right, basicComboBoxRenderer);

		comboBox.setPreferredSize( new Dimension( width, comboBox.getPreferredSize().height));

		return comboBox;
	}

	/**
	 * @param items
	 * @param width
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @return
	 */
	public static ComboBox create(Object[] items, int width, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( color, right, basicComboBoxRenderer);
		else
			comboBox = new ComboBox( items, color, right, basicComboBoxRenderer);

		comboBox.setPreferredSize( new Dimension( width, comboBox.getPreferredSize().height));

		return comboBox;
	}

	/**
	 * @param items
	 * @param width
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param tabbedPage
	 * @return
	 */
	public static ComboBox create(Object[] items, int width, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, TabbedPage tabbedPage) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( right, basicComboBoxRenderer, tabbedPage);
		else
			comboBox = new ComboBox( items, right, basicComboBoxRenderer, tabbedPage);

		comboBox.setPreferredSize( new Dimension( width, comboBox.getPreferredSize().height));

		return comboBox;
	}

	/**
	 * @param items
	 * @param width
	 * @param right
	 * @param color
	 * @param basicComboBoxRenderer
	 * @param tabbedPage
	 * @return
	 */
	public static ComboBox create(Object[] items, int width, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, TabbedPage tabbedPage) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( color, right, basicComboBoxRenderer, tabbedPage);
		else
			comboBox = new ComboBox( items, color, right, basicComboBoxRenderer, tabbedPage);

		comboBox.setPreferredSize( new Dimension( width, comboBox.getPreferredSize().height));

		return comboBox;
	}

	/**
	 * @param items
	 * @param width
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 * @return
	 */
	public static ComboBox create(Object[] items, int width, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( right, basicComboBoxRenderer, dialog);
		else
			comboBox = new ComboBox( items, right, basicComboBoxRenderer, dialog);

		comboBox.setPreferredSize( new Dimension( width, comboBox.getPreferredSize().height));

		return comboBox;
	}

	/**
	 * @param items
	 * @param width
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 * @return
	 */
	public static ComboBox create(Object[] items, int width, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( color, right, basicComboBoxRenderer, dialog);
		else
			comboBox = new ComboBox( items, color, right, basicComboBoxRenderer, dialog);

		comboBox.setPreferredSize( new Dimension( width, comboBox.getPreferredSize().height));

		return comboBox;
	}

	/**
	 * 
	 */
	public ComboBox() {
		super();
	}

	/**
	 * @param right
	 * @param basicComboBoxRenderer
	 */
	public ComboBox(boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		super();
		setup( right, basicComboBoxRenderer);
	}

	/**
	 * @param items
	 * @param right
	 * @param basicComboBoxRenderer
	 */
	public ComboBox(Object[] items, boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		super(items);
		setup( right, basicComboBoxRenderer);
	}

	/**
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 */
	public ComboBox(Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		super();
		_color = color;
		setup( right, basicComboBoxRenderer);
	}

	/**
	 * @param items
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 */
	public ComboBox(Object[] items, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		super(items);
		_color = color;
		setup( right, basicComboBoxRenderer);
	}

	/**
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param tabbedPage
	 */
	public ComboBox(boolean right, BasicComboBoxRenderer basicComboBoxRenderer, TabbedPage tabbedPage) {
		super();
		setup( right, basicComboBoxRenderer, tabbedPage);
	}

	/**
	 * @param items
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param tabbedPage
	 */
	public ComboBox(Object[] items, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, TabbedPage tabbedPage) {
		super(items);
		setup( right, basicComboBoxRenderer, tabbedPage);
	}

	/**
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param tabbedPage
	 */
	public ComboBox(Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, TabbedPage tabbedPage) {
		super();
		_color = color;
		setup( right, basicComboBoxRenderer, tabbedPage);
	}

	/**
	 * @param items
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param tabbedPage
	 */
	public ComboBox(Object[] items, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, TabbedPage tabbedPage) {
		super(items);
		_color = color;
		setup( right, basicComboBoxRenderer, tabbedPage);
	}

	/**
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 */
	public ComboBox(boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		super();
		setup( right, basicComboBoxRenderer, dialog);
	}

	/**
	 * @param items
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 */
	public ComboBox(Object[] items, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		super(items);
		setup( right, basicComboBoxRenderer, dialog);
	}

	/**
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 */
	public ComboBox(Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		super();
		_color = color;
		setup( right, basicComboBoxRenderer, dialog);
	}

	/**
	 * @param items
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 */
	public ComboBox(Object[] items, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		super(items);
		_color = color;
		setup( right, basicComboBoxRenderer, dialog);
	}

	/**
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param tabbedPage
	 */
	private void setup(boolean right, BasicComboBoxRenderer basicComboBoxRenderer, TabbedPage tabbedPage) {
		setup(right, basicComboBoxRenderer);

		tabbedPage.link_to_ok( _textField);
		tabbedPage.link_to_cancel( _textField);

		tabbedPage.link_to_ok( this);
		tabbedPage.link_to_cancel( this);
	}

	/**
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 */
	private void setup(boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		setup(right, basicComboBoxRenderer);

		dialog.link_to_cancel( _textField);

		dialog.link_to_cancel( this);
	}

	/**
	 * @param right
	 * @param basicComboBoxRenderer
	 */
	private void setup(boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		setRenderer( basicComboBoxRenderer);

		BasicComboBoxEditor basicComboBoxEditor = new BasicComboBoxEditor();
		_textField = ( JTextField)basicComboBoxEditor.getEditorComponent();

		if ( null != _color) {
			if ( null == _textFieldDefaultColors[ 0])
				_textFieldDefaultColors[ 0] = _textField.getSelectionColor();
			if ( null == _textFieldDefaultColors[ 1])
				_textFieldDefaultColors[ 1] = _textField.getForeground();
			_textField.setEditable( false);
			if ( null == _textFieldDefaultColors[ 2])
				_textFieldDefaultColors[ 2] = _textField.getBackground();
			setEditor( basicComboBoxEditor);
			setEditable( true);
	
			_textField.setSelectionColor( _color);
			_textField.setForeground( Color.white);
			_textField.setBackground( _color);
		}

		if ( right)
			_textField.setHorizontalAlignment( JTextField.RIGHT);
	}

	/* (Non Javadoc)
	 * @see java.awt.Component#setEnabled(boolean)
	 */
	public void setEnabled(boolean arg0) {
		super.setEnabled(arg0);

		if ( null == _color)
			return;

		_textField.setSelectionColor( arg0 ? _color : _textFieldDefaultColors[ 0]);
		_textField.setForeground( arg0 ? Color.white : _textFieldDefaultColors[ 1]);
		_textField.setBackground( arg0 ? _color : _textFieldDefaultColors[ 2]);
	}
}
