/**
 * 
 */
package soars.application.visualshell.object.player.base.edit.tab.image;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.common.image.SelectedImagefilePanel;
import soars.application.visualshell.common.image.ThumbnailPanel;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase;

/**
 * @author kurata
 *
 */
public class ImagePropertyPage extends PropertyPageBase {

	/**
	 * 
	 */
	private SelectedImagefilePanel _selectedImagefilePanel = null;

	/**
	 * 
	 */
	private ThumbnailPanel _thumbnailPanel = null;

	/**
	 * @param title
	 * @param playerBase
	 * @param propertyPageMap
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public ImagePropertyPage(String title, PlayerBase playerBase, Map<String, PropertyPageBase> propertyPageMap, int index, Frame owner, Component parent) {
		super(title, playerBase, propertyPageMap, index, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.tab.TabbedPage#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;



		setLayout( new BorderLayout());



		_selectedImagefilePanel = new SelectedImagefilePanel( _owner, _parent);
		_thumbnailPanel = new ThumbnailPanel( _selectedImagefilePanel, _owner, _parent);



		JPanel west_panel = new JPanel();
		west_panel.setLayout( new BoxLayout( west_panel, BoxLayout.Y_AXIS));

		if ( !setup_west_panel( west_panel))
			return false;

		add( west_panel, "West");


		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.X_AXIS));

		if ( !setup_center_panel( center_panel))
			return false;

		add( center_panel);



		adjust();



		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_west_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		if ( !setup_selectedImagefilePanel( north_panel))
			return false;

		panel.add( north_panel, "North");

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_selectedImagefilePanel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_selectedImagefilePanel.setup( _playerBase._imageFilename))
			return false;

		panel.add( _selectedImagefilePanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_center_panel(JPanel parent) {
		if ( !_thumbnailPanel.setup( _playerBase._imageFilename, Environment._openImageDirectoryKey))
			return false;

		parent.add( _thumbnailPanel);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#adjust()
	 */
	protected void adjust() {
		int width = 0;
		width = Math.max( width, _selectedImagefilePanel.get_max_width());
		_selectedImagefilePanel.set_width( width);

		_thumbnailPanel.adjust();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#on_setup_completed()
	 */
	public void on_setup_completed() {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.player.base.edit.tab.base.PropertyPageBase#on_ok()
	 */
	public boolean on_ok() {
		_playerBase.update_image( _selectedImagefilePanel.get_selected_image_filename());
		return true;
	}
}
