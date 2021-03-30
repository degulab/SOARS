/**
 * 
 */
package soars.application.visualshell.object.arbitrary.select;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.w3c.dom.Node;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.common.arbitrary.ClassTree;
import soars.application.visualshell.object.common.arbitrary.IClassTreeCallback;
import soars.common.utility.swing.window.Dialog;
import soars.common.utility.xml.dom.XmlTool;

/**
 * @author kurata
 *
 */
public class SelectJarAndClassDlg extends Dialog implements IClassTreeCallback {

	/**
	 * 
	 */
	private int _minimum_width = -1;

	/**
	 * 
	 */
	private int _minimum_height = -1;

	/**
	 * 
	 */
	private JTextField _current_jar_filename_textField = null;

	/**
	 * 
	 */
	private JTextField _current_classname_textField = null;

	/**
	 * 
	 */
	private JTextField _jar_filename_textField = null;

	/**
	 * 
	 */
	private JTextField _classname_textField = null;

	/**
	 * 
	 */
	private JLabel[] _labels = new JLabel[] {
		null, null, null, null
	};

	/**
	 * 
	 */
	private ClassTree _classTree = null;

	/**
	 * 
	 */
	private JScrollPane _scrollPane = null;

	/**
	 * 
	 */
	private JButton _ok_button = null;

	/**
	 * 
	 */
	private JButton _ignore_button = null;

	/**
	 * 
	 */
	private JButton _abort_button = null;

	/**
	 * 
	 */
	private String _jarFilename = "";

	/**
	 * 
	 */
	private String _classname = "";

	/**
	 * 
	 */
	private JarAndClassMap _jarAndClassMap = null;

	/**
	 * 
	 */
	public JarAndClass _jarAndClass = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param jarFilename
	 * @param classname
	 * @param jarAndClassMap
	 * @throws HeadlessException
	 */
	public SelectJarAndClassDlg(Frame arg0, String arg1, boolean arg2, String jarFilename, String classname, JarAndClassMap jarAndClassMap) throws HeadlessException {
		super(arg0, arg1, arg2);
		_jarFilename = jarFilename;
		_classname = classname;
		_jarAndClassMap = jarAndClassMap;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.common.arbitrary.IClassTreeCallback#selected(org.w3c.dom.Node)
	 */
	public void selected(Node node) {
		if ( null == node || !node.getNodeName().equals( "class")) {
			_jar_filename_textField.setText( "");
			_classname_textField.setText( "");
			return;
		}

		String value = XmlTool.get_attribute( node, "name");
		if ( null != value)
			_classname_textField.setText( value);

		node = node.getParentNode();
		if ( null != node) {
			value = XmlTool.get_attribute( node, "name");
			if ( null != value)
				_jar_filename_textField.setText( value);
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		link_to_cancel( getRootPane());


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup_current_jar_filename_textField();

		insert_horizontal_glue();

		setup_current_classname_textField();

		insert_horizontal_glue();

		setup_jar_filename_textField();

		insert_horizontal_glue();

		setup_classname_textField();

		insert_horizontal_glue();

		if ( !setup_classTree())
			return false;

		insert_horizontal_glue();

		setup_buttons();

		insert_horizontal_glue();


		adjust();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * 
	 */
	private void setup_current_jar_filename_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 0] = new JLabel( ResourceManager.get_instance().get( "select.jar.and.class.dialog.current.jar.filename"));
		_labels[ 0].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 0]);

		panel.add( Box.createHorizontalStrut( 5));

		_current_jar_filename_textField = new JTextField( _jarFilename);
		_current_jar_filename_textField.setEditable( false);
		_current_jar_filename_textField.setPreferredSize( new Dimension( 400, _current_jar_filename_textField.getPreferredSize().height));

		link_to_cancel( _current_jar_filename_textField);

		panel.add( _current_jar_filename_textField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_current_classname_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 1] = new JLabel( ResourceManager.get_instance().get( "select.jar.and.class.dialog.current.classname"));
		_labels[ 1].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 1]);

		panel.add( Box.createHorizontalStrut( 5));

		_current_classname_textField = new JTextField( _classname);
		_current_classname_textField.setEditable( false);
		_current_classname_textField.setPreferredSize( new Dimension( 400, _current_classname_textField.getPreferredSize().height));

		link_to_cancel( _current_classname_textField);

		panel.add( _current_classname_textField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_jar_filename_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 2] = new JLabel( ResourceManager.get_instance().get( "select.jar.and.class.dialog.jar.filename"));
		_labels[ 2].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 2]);

		panel.add( Box.createHorizontalStrut( 5));

		_jar_filename_textField = new JTextField();
		_jar_filename_textField.setEditable( false);
		_jar_filename_textField.setPreferredSize( new Dimension( 400, _jar_filename_textField.getPreferredSize().height));

		link_to_cancel( _jar_filename_textField);

		panel.add( _jar_filename_textField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_classname_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 3] = new JLabel( ResourceManager.get_instance().get( "select.jar.and.class.dialog.classname"));
		_labels[ 3].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 3]);

		panel.add( Box.createHorizontalStrut( 5));

		_classname_textField = new JTextField();
		_classname_textField.setEditable( false);
		_classname_textField.setPreferredSize( new Dimension( 400, _classname_textField.getPreferredSize().height));

		link_to_cancel( _classname_textField);

		panel.add( _classname_textField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * @return
	 */
	private boolean setup_classTree() {
		_classTree = new ClassTree( ( Frame)getOwner(), this, this);
		if ( !_classTree.setup( false))
			return false;

		link_to_cancel( _classTree);

		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_scrollPane = new JScrollPane();
		_scrollPane.getViewport().setView( _classTree);

		link_to_cancel( _scrollPane);

		panel.add( _scrollPane);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);

		return true;
	}

	/**
	 * 
	 */
	private void setup_buttons() {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		_ok_button = new JButton( ResourceManager.get_instance().get( "dialog.ok"));

		getRootPane().setDefaultButton( _ok_button);

		_ok_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_ok( arg0);
			}
		});

		link_to_cancel( _ok_button);

		panel.add( _ok_button);


		_ignore_button = new JButton( ResourceManager.get_instance().get( "dialog.ignore"));
		_ignore_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_ignore( arg0);
			}
		});

		link_to_cancel( _ignore_button);

		panel.add( _ignore_button);


		_abort_button = new JButton( ResourceManager.get_instance().get( "dialog.abort"));
		_abort_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_abort( arg0);
			}
		});

		link_to_cancel( _abort_button);

		panel.add( _abort_button);


		int width = _ok_button.getPreferredSize().width;
		width = Math.max( width, _ignore_button.getPreferredSize().width);
		width = Math.max( width, _abort_button.getPreferredSize().width);

		_ok_button.setPreferredSize( new Dimension( width, _ok_button.getPreferredSize().height));
		_ignore_button.setPreferredSize( new Dimension( width, _ignore_button.getPreferredSize().height));
		_abort_button.setPreferredSize( new Dimension( width, _abort_button.getPreferredSize().height));

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;

		for ( int i = 0; i < _labels.length; ++i)
			width = Math.max( width, _labels[ i].getPreferredSize().width);

		for ( int i = 0; i < _labels.length; ++i)
			_labels[ i].setPreferredSize( new Dimension(
				width, _labels[ i].getPreferredSize().height));

		_scrollPane.setPreferredSize( new Dimension( width + 5 + _current_jar_filename_textField.getPreferredSize().width, 350));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_classTree.requestFocusInWindow();

		if ( null == _classTree)
			return;

		_minimum_width = getPreferredSize().width;
		_minimum_height = getPreferredSize().height;

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( 0 > _minimum_width || 0 > _minimum_height)
					return;

				int width = getSize().width;
				setSize( ( _minimum_width > width) ? _minimum_width : width, _minimum_height);
			}
		});
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		// 新たなjarFilenameとclassnameが選択された場合は、新たなJarAndClassを作って_jarAndClassへセットしマップへ追加する
		String jarFilename = _jar_filename_textField.getText();
		if ( null == jarFilename || jarFilename.equals( ""))
			return;

		String classname = _classname_textField.getText();
		if ( null == classname || classname.equals( ""))
			return;

		_jarAndClass = new JarAndClass( jarFilename, classname);
		_jarAndClassMap.put( new JarAndClass( _jarFilename, _classname), _jarAndClass);

		super.on_ok(actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_ignore(ActionEvent actionEvent) {
		// 無視する場合はそのままの_jarFilenameと_classnameでJarAndClassを作って_jarAndClassへセットする
		_jarAndClass = new JarAndClass( _jarFilename, _classname);
		super.on_ok(actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_abort(ActionEvent actionEvent) {
		// 読み込みを中止する場合は_jarAndClassへnullをセットする
		_jarAndClass = null;
		super.on_cancel(actionEvent);
	}
}
