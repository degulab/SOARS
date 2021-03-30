/**
 * 
 */
package soars.application.manager.sample.main.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import soars.application.manager.sample.main.Environment;
import soars.application.manager.sample.main.panel.doc.SoarsDocPanel;
import soars.application.manager.sample.main.panel.image.ImagePanel;
import soars.application.manager.sample.main.panel.tree.SampleTree;

/**
 * @author kurata
 *
 */
public class MainPanel extends JSplitPane {

	/**
	 * 
	 */
	private SampleTree _sampleTree = null;

	/**
	 * 
	 */
	private JSplitPane _splitPane = null;

	/**
	 * 
	 */
	private ImagePanel _imagePanel = null;

	/**
	 * 
	 */
	private SoarsDocPanel _soarsDocPanel = null;

	/**
	 * 
	 */
	private Frame _owner = null;

	/**
	 * 
	 */
	private Component _parent = null;

	/**
	 * @param owner
	 * @param parent
	 */
	public MainPanel(Frame owner, Component parent) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param buttonMap
	 * @return
	 */
	public boolean setup(Map<String, JButton> buttonMap) {
		_splitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT);

		_soarsDocPanel = new SoarsDocPanel();
		if ( !_soarsDocPanel.setup())
			return false;

		_sampleTree = new SampleTree( buttonMap, _owner, _parent);

		_imagePanel = new ImagePanel( buttonMap);

		if ( !_sampleTree.setup( _imagePanel, _soarsDocPanel._filenameTextField, _soarsDocPanel._soarsDocEditorPane))
			return false;

		if ( !_imagePanel.create( _sampleTree))
			return false;

		setup_sampleTree();
		setup_imagePanel();
		setup_soarsDocPanel();

		setRightComponent( _splitPane);

		setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._main_panel_divider_location1_key, "100")));

		_splitPane.setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._main_panel_divider_location2_key, "100")));

		return true;
	}

	/**
	 * 
	 */
	private void setup_sampleTree() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _sampleTree);

		setLeftComponent( scrollPane);
	}

	/**
	 * 
	 */
	private void setup_imagePanel() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBackground( new Color( 255, 255, 255));
		scrollPane.getViewport().setView( _imagePanel);

		_splitPane.setTopComponent( scrollPane);
	}

	/**
	 * 
	 */
	private void setup_soarsDocPanel() {
		_splitPane.setBottomComponent( _soarsDocPanel);
	}

	/**
	 * 
	 */
	public void optimize_divider_location() {
		setDividerLocation( 100);
		_splitPane.setDividerLocation( 100);
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_sampleTree.on_setup_completed();
	}

	/**
	 * 
	 */
	public void set_property_to_environment_file() {
		Environment.get_instance().set(
			Environment._main_panel_divider_location1_key, String.valueOf( getDividerLocation()));
		Environment.get_instance().set(
			Environment._main_panel_divider_location2_key, String.valueOf( _splitPane.getDividerLocation()));
	}

	/**
	 * 
	 */
	public void update() {
		_sampleTree.update();
	}

	/**
	 * 
	 */
	public void on_exit() {
		_sampleTree.on_exit();
	}
	/**
	 * 
	 */
	public void refresh() {
		_sampleTree.refresh();
	}

	/**
	 * @param menuItemMap 
	 */
	public void editMenuSelected(Map<String, JMenuItem> menuItemMap) {
		_sampleTree.editMenuSelected( menuItemMap);
		_imagePanel.editMenuSelected( menuItemMap);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_copy(ActionEvent actionEvent) {
		_sampleTree.on_edit_copy( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_paste(ActionEvent actionEvent) {
		_sampleTree.on_edit_paste( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_export(ActionEvent actionEvent) {
		_sampleTree.on_edit_export( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_remove(ActionEvent actionEvent) {
		_sampleTree.on_edit_remove( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_rename(ActionEvent actionEvent) {
		_sampleTree.on_edit_rename( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_new_directory(ActionEvent actionEvent) {
		_sampleTree.on_edit_new_directory( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_new_simulation_model(ActionEvent actionEvent) {
		_sampleTree.on_edit_new_simulation_model( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_model_information(ActionEvent actionEvent) {
		_sampleTree.on_edit_model_information( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_clear_image(ActionEvent actionEvent) {
		_imagePanel.on_edit_clear_image( actionEvent);
	}

	/**
	 * @param menuItemMap
	 */
	public void runMenuSelected(Map<String, JMenuItem> menuItemMap) {
		_sampleTree.runMenuSelected( menuItemMap);
	}

	/**
	 * @param actionEvent
	 */
	public void on_run_start_visual_shell(ActionEvent actionEvent) {
		_sampleTree.on_run_start_visual_shell( actionEvent);
	}

	/**
	 * 
	 */
	public void back() {
		_soarsDocPanel.back();
	}

	/**
	 * 
	 */
	public void forward() {
		_soarsDocPanel.forward();
	}
}
