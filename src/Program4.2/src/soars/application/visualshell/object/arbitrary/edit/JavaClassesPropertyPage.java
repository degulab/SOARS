/**
 * 
 */
package soars.application.visualshell.object.arbitrary.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.MouseInputAdapter;

import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.tab.TabbedPage;
import soars.common.utility.swing.tool.SwingTool;

/**
 * The tab component for java class list.
 * @author kurata / SOARS project
 */
public class JavaClassesPropertyPage extends TabbedPage {

	/**
	 * Title for the tab.
	 */
	public String _title = ResourceManager.get_instance().get( "edit.java.classes.dialog.title");

	/**
	 * 
	 */
	private JavaClassesTable _javaClassesTable = null;

	/**
	 * 
	 */
	private JScrollPane _javaClassesTableScrollPane = null;

	/**
	 * 
	 */
	private boolean _at_first = true;

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * Creates this object.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public JavaClassesPropertyPage(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.tab.TabbedPage#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;



		setLayout( new BorderLayout());



		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( north_panel);

		add( north_panel, "North");



		JPanel center_panel = new JPanel();
		center_panel.setLayout( new BoxLayout( center_panel, BoxLayout.Y_AXIS));

		if ( !setup_javaClassesTable( center_panel))
			return false;

		add( center_panel);



		JPanel south_panel = new JPanel();
		south_panel.setLayout( new BoxLayout( south_panel, BoxLayout.Y_AXIS));

		setup_append_java_class_button( south_panel);

		insert_horizontal_glue( south_panel);

		add( south_panel, "South");



		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( _at_first) {
					_javaClassesTable.setup_column_width( _javaClassesTableScrollPane.getWidth());
					_at_first = false;
				} else {
					_javaClassesTable.adjust_column_width( _javaClassesTableScrollPane.getWidth());
				}
			}
		});



		return true;
	}

	/**
	 * @param north_panel
	 * @return
	 */
	private boolean setup_javaClassesTable(JPanel north_panel) {
		_javaClassesTable = new JavaClassesTable( _owner, _parent);
		if ( !_javaClassesTable.setup())
			return false;

		//link_to_cancel( _javaClassesTable);

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel table_panel = new JPanel();
		table_panel.setLayout( new GridLayout( 1, 1));

		_javaClassesTableScrollPane = new JScrollPane();
		_javaClassesTableScrollPane.getViewport().setView( _javaClassesTable);
		_javaClassesTableScrollPane.addMouseListener( new MouseInputAdapter() {
			public void mouseReleased(MouseEvent arg0) {
				if ( !SwingTool.is_mouse_right_button( arg0))
					return;

				_javaClassesTable.on_mouse_right_up( new Point( arg0.getX(),
					arg0.getY() - _javaClassesTable.getTableHeader().getHeight()));
			}
		});

		//link_to_cancel( _javaClassesTableScrollPane);

		table_panel.add( _javaClassesTableScrollPane);
		panel.add( table_panel);
		panel.add( Box.createHorizontalStrut( 5));

		north_panel.add( panel);

		return true;
	}

	/**
	 * @param south_panel
	 */
	private void setup_append_java_class_button(JPanel south_panel) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel button_panel = new JPanel();
		button_panel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get_instance().get( "edit.java.classes.dialog.append.new.class.button.name"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_javaClassesTable.on_append( arg0);
			}
		});

		//link_to_cancel( button);

		button_panel.add( button);
		panel.add( button_panel);
		panel.add( Box.createHorizontalStrut( 5));

		south_panel.add( panel);
	}

	/**
	 * Invoked when this objet has been initialized.
	 */
	public void on_setup_completed() {
	}

	/**
	 * Returns true for updating successfully.
	 * @return true for updating successfully
	 */
	public boolean on_ok() {
		_javaClassesTable.on_ok();
		return true;
	}
}
