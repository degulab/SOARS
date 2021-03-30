/**
 * 
 */
package soars.application.builder.animation.main.panel;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import soars.application.builder.animation.document.Document;
import soars.application.builder.animation.document.DocumentManager;
import soars.application.builder.animation.main.ResourceManager;

/**
 * @author kurata
 *
 */
public class MainPanel extends JPanel {


	/**
	 * 
	 */
	private JTextField _title_textField = null;


	/**
	 * 
	 */
	private JTextArea _comment_textArea = null;


	/**
	 * 
	 */
	private boolean _ignore = false;

	/**
	 * 
	 */
	public MainPanel() {
		super();
	}

	/**
	 * @return
	 */
	public boolean setup() {
		setLayout( new BorderLayout());


		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		//SwingTool.insert_horizontal_glue( north_panel);

		setup_title_textField( north_panel);

		//SwingTool.insert_horizontal_glue( north_panel);

		add( north_panel, "North");


		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		setup_comment_textArea( center_panel);

		add( center_panel);


		JPanel south_panel = new JPanel();
		south_panel.setLayout( new BoxLayout( south_panel, BoxLayout.Y_AXIS));

		//SwingTool.insert_horizontal_glue( south_panel);

		add( south_panel, "South");


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_title_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel title_panel = new JPanel();
		title_panel.setLayout( new BoxLayout( title_panel, BoxLayout.Y_AXIS));

		title_panel.setBorder( BorderFactory.createTitledBorder(
			ResourceManager.get( "main.panel.title.label")));

		_title_textField = new JTextField();
		_title_textField.getDocument().addDocumentListener( new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				on_update();
			}
			public void insertUpdate(DocumentEvent e) {
				on_update();
			}
			public void removeUpdate(DocumentEvent e) {
				on_update();
			}
		});
		title_panel.add( _title_textField);

		panel.add( title_panel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_comment_textArea(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel comment_panel = new JPanel();
		comment_panel.setLayout( new BoxLayout( comment_panel, BoxLayout.Y_AXIS));

		comment_panel.setBorder( BorderFactory.createTitledBorder(
			ResourceManager.get( "main.panel.comment.label")));

		_comment_textArea = new JTextArea();
		_comment_textArea.getDocument().addDocumentListener( new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				on_update();
			}
			public void insertUpdate(DocumentEvent e) {
				on_update();
			}
			public void removeUpdate(DocumentEvent e) {
				on_update();
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _comment_textArea);

		comment_panel.add( scrollPane);

		panel.add( comment_panel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void on_update() {
		if ( _ignore)
			return;

		DocumentManager.get_instance().modified();
	}

	/**
	 * @param previous
	 * @param next 
	 */
	public void update(Document previous, Document next) {
		_ignore = true;

		if ( null != previous)
			get( previous);

		set( next);

		_ignore = false;
	}

	/**
	 * @param document
	 */
	private void get(Document document) {
		document._title = _title_textField.getText();
		document._comment = _comment_textArea.getText();
	}

	/**
	 * @param document
	 */
	private void set(Document document) {
		_title_textField.setText( ( null == document) ? "" : document._title);
		_comment_textArea.setText( ( null == document) ? "" : document._comment);
	}

	/**
	 * 
	 */
	public void reset() {
		_ignore = true;
		_title_textField.setText( "");
		_comment_textArea.setText( "");
		_ignore = false;
	}

	/**
	 * @param document
	 */
	public void load(Document document) {
		if ( null == document)
			return;

		_ignore = true;
		set( document);
		_ignore = false;
	}

	/**
	 * @param document
	 */
	public void store(Document document) {
		if ( null == document)
			return;

		get( document);
	}
}
