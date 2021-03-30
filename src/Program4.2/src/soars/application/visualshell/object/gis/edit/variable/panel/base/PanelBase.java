/**
 * 
 */
package soars.application.visualshell.object.gis.edit.variable.panel.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.variable.object.base.ObjectBase;
import soars.application.visualshell.object.gis.edit.variable.table.VariableTable;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class PanelBase extends JPanel {

	/**
	 * 
	 */
	protected String _kind = "";

	/**
	 * 
	 */
	protected GisDataManager _gisDataManager = null;

	/**
	 * 
	 */
	protected VariableTable _variableTable = null;

	/**
	 * 
	 */
	protected List<JComponent> _labels = new ArrayList<JComponent>();

	/**
	 * 
	 */
	protected List<JComponent> _components = new ArrayList<JComponent>();

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * 
	 */
	protected Color _color = null;

	/**
	 * 
	 */
	protected List<JButton> _buttons = new ArrayList<JButton>();

	/**
	 * 
	 */
	protected JButton _clearButton = null;

	/**
	 * 
	 */
	protected TextField _nameTextField = null;

	/**
	 * 
	 */
	protected TextField _commentTextField = null;

	/**
	 * @param kind
	 * @param gisDataManager
	 * @param variableTable
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public PanelBase(String kind, GisDataManager gisDataManager, VariableTable variableTable, Color color, Frame owner, Component parent) {
		super();
		_kind = kind;
		_gisDataManager = gisDataManager;
		_variableTable = variableTable;
		_color = color;
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @return
	 */
	public ObjectBase create() {
		return ObjectBase.create( _kind);
	}

	/**
	 * @param container
	 */
	protected void insert_vertical_strut(Container container) {
		SwingTool.insert_vertical_strut( container, 5);
	}

	/**
	 * @return
	 */
	public boolean setup() {
		setLayout( new BorderLayout());


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		setup_center_panel( centerPanel);

		add( centerPanel);


		JPanel eastPanel = new JPanel();
		eastPanel.setLayout( new BorderLayout());

		setup_buttons( eastPanel);

		add( eastPanel, "East");


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	protected boolean setup_center_panel(JPanel parent) {
		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_buttons(JPanel parent) {
		JPanel north_panel = new JPanel();
		north_panel.setLayout( new BoxLayout( north_panel, BoxLayout.Y_AXIS));

		insert_vertical_strut( north_panel);

		JPanel button_panel = new JPanel();
		button_panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/file/new.png"));
		JButton button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.file.append.button"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_append( e);
			}
		});
		_buttons.add( button);
		button_panel.add( button);

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/file/update.png"));
		button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.file.update.button"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_update( e);
			}
		});
		_buttons.add( button);
		button_panel.add( button);

		north_panel.add( button_panel);

		parent.add( north_panel, "North");


		JPanel south_panel = new JPanel();
		south_panel.setLayout( new BoxLayout( south_panel, BoxLayout.Y_AXIS));

		button_panel = new JPanel();
		button_panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/file/clear.png"));
		_clearButton = new JButton( imageIcon);
		_clearButton.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.file.clear.button"));
		_clearButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_clear( e);
			}
		});
		button_panel.add( _clearButton);

		south_panel.add( button_panel);

		//insert_vertical_strut( south_panel);

		parent.add( south_panel, "South");
	}

	/**
	 * @param actionEvent
	 */
	protected void on_append(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		ObjectBase objectBase = on_append();
		if ( null == objectBase)
			return;

		_variableTable.append( objectBase);

//		_propertyPageMap.get( "variable").update();
	}

	/**
	 * @return
	 */
	protected ObjectBase on_append() {
		// TODO Auto-generated method stub
		ObjectBase objectBase = create();
		return on_update( objectBase) ? objectBase : null;
	}

	/**
	 * @param actionEvent
	 */
	protected void on_update(ActionEvent actionEvent) {
		int[] rows = _variableTable.getSelectedRows();
		if ( null == rows || 1 != rows.length)
			return;

		ObjectBase objectBase = ( ObjectBase)_variableTable.getValueAt( rows[ 0], 0);
		update( rows[ 0], objectBase, true);
	}

	/**
	 * @param row
	 * @param objectBase
	 * @param selection
	 */
	private void update(int row, ObjectBase objectBase, boolean selection) {
		// TODO Auto-generated method stub
		WarningManager.get_instance().cleanup();

		ObjectBase originalObjectBase = ObjectBase.create( objectBase);
		if ( !on_update( objectBase))
			return;

		_variableTable.update( row, originalObjectBase, selection);

//		_propertyPageMap.get( "variable").update();

		if ( !WarningManager.get_instance().isEmpty()) {
			WarningDlg1 warningDlg1 = new WarningDlg1(
				_owner,
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message3"),
				_parent);
			warningDlg1.do_modal();
		}
	}

	/**
	 * @param objectBase
	 * @return
	 */
	protected boolean on_update(ObjectBase objectBase) {
		// TODO Auto-generated method stub
		if ( !can_get_data( objectBase))
			return false;

		get_data( objectBase);

		return true;
	}

	/**
	 * @param objectBase
	 */
	public void update(ObjectBase objectBase) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void update() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param actionEvent
	 */
	protected void on_clear(ActionEvent actionEvent) {
		clear();
	}

	/**
	 * 
	 */
	public void clear() {
		_nameTextField.setText( "");
		_commentTextField.setText( "");
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_label_width(int width) {
		for ( JComponent label:_labels)
			width = Math.max( width, label.getPreferredSize().width);

		return width;
	}

	/**
	 * @param width 
	 * @return
	 */
	public int adjust(int width) {
		for ( JComponent label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));

		return ( _buttons.get( 0).getPreferredSize().width + 5 + _buttons.get( 1).getPreferredSize().width);
	}

	/**
	 * @return
	 */
	protected boolean is_empty() {
		return ( _nameTextField.getText().equals( "")
			&& _commentTextField.getText().equals( ""));
	}

	/**
	 * @return
	 */
	protected ObjectBase create_and_get() {
		return null;
	}

	/**
	 * @param objectBase
	 */
	protected boolean can_get_data(ObjectBase objectBase) {
		return false;
	}

	/**
	 * @param objectBase
	 */
	protected void get_data(ObjectBase objectBase) {
	}

	/**
	 * @return
	 */
	public boolean confirm() {
		if ( !isVisible())
			return true;

		int[] rows = _variableTable.getSelectedRows();
		ObjectBase object = create();
		ObjectBase objectBase = confirm(
			( null == rows || 1 != rows.length) ? -1 : rows[ 0], 
			( null == rows || 1 != rows.length) ? null : ( ObjectBase)_variableTable.getValueAt( rows[ 0], 0), 
			object);
		if ( null == objectBase) {
			// 追加または更新に失敗
			return false;
		} else {
			if ( objectBase == object) {
				// 無視の場合
				if ( null == rows || 1 != rows.length)
					clear();

				return true;
			} else {
				// 追加または更新されたオブジェクトを選択させる
				_variableTable.select( objectBase);
				return false;
			}
		}
	}

	/**
	 * @param row
	 * @param targetObjectBase
	 * @param selectedObjectBase
	 * @return
	 */
	public ObjectBase confirm(int row, ObjectBase targetObjectBase, ObjectBase selectedObjectBase) {
		// TODO Auto-generated method stub
		if ( !isVisible())
			return null;

		// 編集状態でない場合
		if ( is_empty())
			return selectedObjectBase;	// 何もしない

		// 複数選択されているかまたは選択されているオブジェクトがこのオブジェクトと異なる場合
		if ( ( null == targetObjectBase)
			|| !ObjectBase.is_target( _kind, targetObjectBase)) {
			ObjectBase objectBase = ( ObjectBase)_variableTable.get( _kind, _nameTextField.getText());
			if ( null == objectBase) {
				// 同じ名前のものが無いので追加または無視を選択
				if ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.append.variable.confirm.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind.replaceAll( " ", ".")),
					JOptionPane.YES_NO_OPTION))
					return selectedObjectBase;	// 無視なら何もしない

				objectBase = create();
				if ( !can_get_data( objectBase))
					return null;		// 追加出来ないので選択を変えさせない

				// オブジェクトを追加
				get_data( objectBase);
				_variableTable.append( objectBase);
				update();
				return objectBase;
			} else {
				ObjectBase temp = create_and_get();
				if ( temp.equals( objectBase))
					return selectedObjectBase;	// 同じ名前のものがあっても中身が全く同じなら何もしない

				// 中身が異なる同じ名前のものがあるので更新または無視を選択
				if ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.update.variable.confirm.message") + " - " + objectBase._name,
					ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind.replaceAll( " ", ".")),
					JOptionPane.YES_NO_OPTION))
					return selectedObjectBase;	// 無視なら何もしない

				if ( !can_get_data( objectBase))
					return null;		// 更新出来ないので選択を変えさせない

				// 更新する行番号を取得
				row = _variableTable.get( objectBase);
				if ( 0 > row)		// これは起こり得ない筈だが念の為
					return null;	// 更新出来ないので選択を変えさせない

				// オブジェクトを更新
				get_data( objectBase);
				update( row, objectBase, false);
				update();
				return objectBase;
			}
		}

		ObjectBase objectBase = create_and_get();
		if ( objectBase.equals( targetObjectBase))
			return selectedObjectBase;	// 編集されていなければ何もしない


		if ( !targetObjectBase._name.equals( _nameTextField.getText())) {
			// 名前が異なるので追加、更新または無視を選択
			String[] options = new String[] {
				ResourceManager.get_instance().get( "edit.object.dialog.append.option"),
				ResourceManager.get_instance().get( "edit.object.dialog.update.option"),
				ResourceManager.get_instance().get( "edit.object.dialog.ignore.option")
			};
			int result = JOptionPane.showOptionDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.append.or.update.variable.confirm.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind.replaceAll( " ", ".")),
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null,
				options,
				options[ 0]);
			switch ( result) {
				case 0:		// 追加の場合
					if ( !can_get_data( objectBase))
						return null;		// 追加出来ないので選択を変えさせない

					// オブジェクトを追加
					get_data( objectBase);
					_variableTable.append( objectBase);
					update();
					return objectBase;
				case 1:		// 更新の場合
					if ( !can_get_data( targetObjectBase))
						return null;		// 更新出来ないので選択を変えさせない

					// オブジェクトを更新
					update( row, targetObjectBase, false);
					return targetObjectBase;
				default:	// 無視の場合
					return selectedObjectBase;	// 無視なら何もしない
			}
		} else {
			// 名前が同じなので更新または無視を選択
			if ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.update.variable.confirm.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind.replaceAll( " ", ".")),
				JOptionPane.YES_NO_OPTION))
				return selectedObjectBase;	// 無視なら何もしない

			if ( !can_get_data( targetObjectBase))
				return null;		// 更新出来ないので選択を変えさせない

			// オブジェクトを更新
			update( row, targetObjectBase, false);
			return targetObjectBase;
		}
	}
}
