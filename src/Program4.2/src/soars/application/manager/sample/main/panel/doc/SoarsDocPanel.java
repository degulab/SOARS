/**
 * 
 */
package soars.application.manager.sample.main.panel.doc;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import soars.application.manager.sample.main.Constant;
import soars.application.manager.sample.main.ResourceManager;

/**
 * @author kurata
 *
 */
public class SoarsDocPanel extends JPanel {

	/**
	 * 
	 */
	public JButton _backButton = null;

	/**
	 * 
	 */
	public JButton _forwardButton = null;

	/**
	 * 
	 */
	public JTextField _filenameTextField = null;

	/**
	 * 
	 */
	public SoarsDocEditorPane _soarsDocEditorPane = null;

	/**
	 * 
	 */
	public SoarsDocPanel() {
		super();
	}

	/**
	 * @return
	 */
	public boolean setup() {
		setLayout( new BorderLayout());


		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.X_AXIS));

		if ( !setup_north_panel( north_panel))
			return false;

		add( north_panel, "North");


		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		if ( !setup_center_panel( center_panel))
			return false;

		add( center_panel);


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_north_panel(JPanel parent) {
		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/button/back.png"));
		_backButton = new JButton( imageIcon);
		_backButton.setToolTipText( ResourceManager.get( "history.back.menu"));
		//_backButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _backButton.getPreferredSize().height));
		_backButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_soarsDocEditorPane.back();
			}
		});
		parent.add( _backButton);


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/button/forward.png"));
		_forwardButton = new JButton( imageIcon);
		_forwardButton.setToolTipText( ResourceManager.get( "history.forward.menu"));
		//_forwardButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _forwardButton.getPreferredSize().height));
		_forwardButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_soarsDocEditorPane.forward();
			}
		});
		parent.add( _forwardButton);


		JLabel label = new JLabel( " " + ResourceManager.get( "soars.doc.panel.sample.filename.label") + " : ");
		parent.add( label);


		_filenameTextField = new JTextField();
		_filenameTextField.setEditable( false);
		parent.add( _filenameTextField);


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_center_panel(JPanel parent) {
		_soarsDocEditorPane = new SoarsDocEditorPane( _backButton, _forwardButton);
		_soarsDocEditorPane.setEditable( false);
		if ( !_soarsDocEditorPane.setup())
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _soarsDocEditorPane);

		parent.add( scrollPane);

		return true;
	}

	/**
	 * 
	 */
	public void back() {
		_soarsDocEditorPane.back();
	}

	/**
	 * 
	 */
	public void forward() {
		_soarsDocEditorPane.forward();
	}
}
