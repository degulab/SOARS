/**
 * 
 */
package soars.application.animator.common.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.animator.main.ResourceManager;
import soars.common.utility.swing.color.ColorDlg;
import soars.common.utility.swing.window.Dialog;

/**
 * The common GUI panel for color selection.
 * @author kurata / SOARS project
 */
public class ColorPanel extends JPanel {

	/**
	 * Name of object.
	 */
	private String _name = "";

	/**
	 * Label for image color.
	 */
	public JLabel _image_color_label = null;

	/**
	 * Label for text color.
	 */
	public JLabel _text_color_label = null;

	/**
	 * Button for image color selection.
	 */
	public JButton _image_color_button = null;

	/**
	 * Button for text color selection.
	 */
	public JButton _text_color_button = null;

	/**
	 * Frame of the parent container.
	 */
	private Frame _owner = null;

	/**
	 * Parent container of this component.
	 */
	private Component _parent = null;

	/**
	 * Creates the common GUI panel for color selection.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public ColorPanel(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/**
	 * Creates the common GUI panel for color selection.
	 * @param name the name of object
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public ColorPanel(String name, Frame owner, Component parent) {
		super();
		_name = name;
		_owner = owner;
		_parent = parent;
	}

	/**
	 * Creates the components to select image and text color.
	 * @param image_color the specified image color
	 * @param text_color the specified text color
	 * @param dialog the parent container of this component
	 */
	public void setup(Color image_color, Color text_color, Dialog dialog) {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		add( Box.createHorizontalStrut( 5));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		panel.setBorder( BorderFactory.createTitledBorder(
			ResourceManager.get( "color.panel.border.title")));

		setup_image_color_label( image_color, panel);

		dialog.insert_horizontal_glue( panel);

		setup_text_color_label( text_color, panel);

		add( panel);

		add( Box.createHorizontalStrut( 5));
	}

	/**
	 * Creates the components to select image color.
	 * @param image_color the specified image color
	 * @param dialog the parent container of this component
	 */
	public void setup(Color image_color, Dialog dialog) {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		add( Box.createHorizontalStrut( 5));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		panel.setBorder( BorderFactory.createTitledBorder(
			ResourceManager.get( "color.panel.border.title")));

		setup_image_color_label( image_color, panel);

		add( panel);

		add( Box.createHorizontalStrut( 5));
	}

	/**
	 * Creates the components to select image color.
	 * @param image_color the specified image color
	 * @param parent the parent container of these components
	 */
	private void setup_image_color_label(Color image_color, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_image_color_label = new JLabel( " ");
		_image_color_label.setOpaque( true);
		_image_color_label.setBackground( image_color);
		_image_color_label.setBorder( BorderFactory.createLineBorder( _image_color_label.getForeground(), 1));
		panel.add( _image_color_label);

		panel.add( Box.createHorizontalStrut( 5));

		JPanel button_panel = new JPanel();
		button_panel.setLayout( new GridLayout( 1, 1));

		_image_color_button = new JButton(
			ResourceManager.get( "color.panel.image.color.button"));
		_image_color_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_get_color( _image_color_label,
					ResourceManager.get( "color.dialog.image.color.title"));
			}
		});
		button_panel.add( _image_color_button);

		panel.add( button_panel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * Creates the components to select text color.
	 * @param text_color the specified text color
	 * @param parent the parent container of these components
	 */
	private void setup_text_color_label(Color text_color, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_text_color_label = new JLabel( " ");
		_text_color_label.setOpaque( true);
		_text_color_label.setBackground( text_color);
		_text_color_label.setBorder( BorderFactory.createLineBorder( _text_color_label.getForeground(), 1));
		panel.add( _text_color_label);

		panel.add( Box.createHorizontalStrut( 5));

		JPanel button_panel = new JPanel();
		button_panel.setLayout( new GridLayout( 1, 1));

		_text_color_button = new JButton(
			ResourceManager.get( "color.panel.text.color.button"));
		_text_color_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_get_color( _text_color_label,
					ResourceManager.get( "color.dialog.text.color.title"));
			}
		});
		button_panel.add( _text_color_button);

		panel.add( button_panel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * Invoked when the button is clicked.
	 * @param label the label for the specified color
	 * @param title the title string of the color chooser dialog box
	 */
	protected void on_get_color(JLabel label, String title) {
		Color color = ColorDlg.showDialog( _owner,
			title + ( _name.equals( "") ? "" : ( " - " + _name)),
			label.getBackground(),
			_parent,
			ResourceManager.get( "dialog.ok"),
			ResourceManager.get( "dialog.cancel"),
			ResourceManager.get( "make.color"));
		if ( null == color)
			return;
	
		label.setBackground( color);
	}

	/**
	 * Returns the maximum width of the labels on this component.
	 * @return the maximum width of the labels on this component
	 */
	public int get_max_width() {
		int width = 0;
		if ( null != _image_color_label)
			width = Math.max( width, _image_color_label.getPreferredSize().width);
		if ( null != _text_color_label)
			width = Math.max( width, _text_color_label.getPreferredSize().width);
		return width;
	}

	/**
	 * Sets the specified width of the labels on this component.
	 * @param width the specified width
	 */
	public void set_width(int width) {
		if ( null != _image_color_label)
			_image_color_label.setPreferredSize( new Dimension( width, _image_color_button.getPreferredSize().height));
		if ( null != _text_color_label)
			_text_color_label.setPreferredSize( new Dimension( width, _text_color_button.getPreferredSize().height));
	}
}
