/**
 * 
 */
package soars.application.animator.common.image;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.player.ObjectManager;
import soars.common.utility.swing.tool.SwingTool;

/**
 * The common GUI panel for thumbnail list.
 * @author kurata / SOARS project
 */
public class ThumbnailPanel extends JPanel {

	/**
	 * Thumbnail list class object.
	 */
	private ThumbnailList _thumbnailList = null;

	/**
	 * Label for the number of current page.
	 */
	private JLabel _pageLabel = null;

	/**
	 * Button to go to previous page.
	 */
	private JButton _previousPageButton = null;

	/**
	 * Button to go to next page.
	 */
	private JButton _nextPageButton = null;

	/**
	 * Button to add new image files.
	 */
	private JButton _newImageFilesButton = null;

	/**
	 * Thumbnail selection handler.
	 */
	private IThumbnailListCallback _thumbnailListCallback = null;

	/**
	 * Frame of the parent container.
	 */
	private Frame _owner = null;

	/**
	 * Parent container of this component.
	 */
	private Component _parent = null;

	/**
	 * Creates the common GUI panel for thumbnail list.
	 * @param thumbnailListCallback the thumbnail selection handler
	 */
	public ThumbnailPanel(IThumbnailListCallback thumbnailListCallback, Frame owner, Component parent) {
		super();
		_thumbnailListCallback = thumbnailListCallback;
		_owner = owner;
		_parent = parent;
	}

	/**
	 * Returns true if this component is initialized successfully.
	 * @param image_filename the image filename
	 * @param open_directory_key the key mapped to the default directory for JFileChooser
	 * @return true if this component is initialized successfully
	 */
	public boolean setup(String image_filename, String open_directory_key) {
		setLayout( new BorderLayout());

		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		SwingTool.insert_horizontal_glue( center_panel);

		if ( !setup_thumbnailList( image_filename, open_directory_key, center_panel))
			return false;

		setup_pageLabel( center_panel);

		setup_pageButtons( center_panel);

		setup_newImageFilesButton( center_panel);

		add( center_panel);

		return true;
	}

	/**
	 * Returns true if the thumbnail list is initialized successfully.
	 * @param image_filename the image filename
	 * @param open_directory_key the key mapped to the default directory for JFileChooser
	 * @param parent the parent container of the component on which the thumbnail list is displayed
	 * @return true if the thumbnail list is initialized successfully
	 */
	private boolean setup_thumbnailList(String image_filename, String open_directory_key, JPanel parent) {
		JPanel basic_panel = new JPanel();
		basic_panel.setLayout( new BoxLayout( basic_panel, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		_thumbnailList = new ThumbnailList( ObjectManager.get_instance().get_image_directory(),
			ObjectManager.get_instance().get_thumbnail_image_directory(), open_directory_key,
			_thumbnailListCallback, this, _owner, _parent);
		if ( !_thumbnailList.setup( image_filename, true, false))
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView( _thumbnailList);
		scrollPane.setBackground( new Color( 255, 255, 255));
		panel.add( scrollPane);

		basic_panel.add( panel);

		basic_panel.add( Box.createHorizontalStrut( 5));

		parent.add( basic_panel);

		return true;
	}

	/**
	 * Creates the label for the number of current page.
	 * @param parent the parent container of the component on which the label for the number of current page is displayed
	 */
	private void setup_pageLabel(JPanel parent) {
		JPanel basic_panel = new JPanel();
		basic_panel.setLayout( new BoxLayout( basic_panel, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 1));

		_pageLabel = new JLabel( "1 / 1");
		_pageLabel.setOpaque( true);
		_pageLabel.setBackground( _thumbnailList.getBackground());
		_pageLabel.setBorder( BorderFactory.createLineBorder( _pageLabel.getForeground(), 1));
		_pageLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _pageLabel);

		basic_panel.add( panel);

		basic_panel.add( Box.createHorizontalStrut( 5));

		parent.add( basic_panel);
	}

	/**
	 * Creates the buttons to go to previous and next page.
	 * @param parent the parent container of the component on which the buttons to go to previous and next page are displayed
	 */
	private void setup_pageButtons(JPanel parent) {
		JPanel basic_panel = new JPanel();
		basic_panel.setLayout( new BoxLayout( basic_panel, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 2));

		_previousPageButton = new JButton(
			ResourceManager.get( "thumbnail.panel.previous.page.button"));
		_previousPageButton.setEnabled( _thumbnailList.can_move_to_previous_page());
		_previousPageButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_thumbnailList.move_to_previous_page();
				update();
			}
		});
		panel.add( _previousPageButton);

		_nextPageButton = new JButton(
			ResourceManager.get( "thumbnail.panel.next.page.button"));
		_nextPageButton.setEnabled( _thumbnailList.can_move_to_next_page());
		_nextPageButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_thumbnailList.move_to_next_page();
				update();
			}
		});
		panel.add( _nextPageButton);

		basic_panel.add( panel);

		basic_panel.add( Box.createHorizontalStrut( 5));

		parent.add( basic_panel);
	}

	/**
	 * Creates the button to add new image files.
	 * @param parent the parent container of the component on which the button to add new image files are displayed
	 */
	private void setup_newImageFilesButton(JPanel parent) {
		JPanel basic_panel = new JPanel();
		basic_panel.setLayout( new BoxLayout( basic_panel, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 1));

		_newImageFilesButton = new JButton(
			ResourceManager.get( "thumbnail.panel.new.imagefiles.button"));
		_newImageFilesButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_thumbnailList.on_append( null);
				update();
			}
		});
		panel.add( _newImageFilesButton);

		basic_panel.add( panel);

		basic_panel.add( Box.createHorizontalStrut( 5));

		parent.add( basic_panel);
	}

	/**
	 * Arranges all components.
	 */
	public void adjust() {
		_thumbnailList.adjust();
		update();
	}

	/**
	 * Updates the number of current page and the status of buttons.
	 */
	public void update() {
		_pageLabel.setText( String.valueOf( _thumbnailList._page + 1) + " / " + String.valueOf( _thumbnailList.get_max_page()));
		_previousPageButton.setEnabled( _thumbnailList.can_move_to_previous_page());
		_nextPageButton.setEnabled( _thumbnailList.can_move_to_next_page());
	}
}
