/*
 * 2005/03/15
 */
package soars.application.animator.object.player.base.edit.objects;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import soars.application.animator.common.font.FontSizeListCellRenderer;
import soars.application.animator.common.font.SelectFontFamilyDlg;
import soars.application.animator.common.font.SelectFontSizeDlg;
import soars.application.animator.common.font.SelectFontStyleDlg;
import soars.application.animator.common.menu.basic1.ArrangeSpotsAction;
import soars.application.animator.common.menu.basic1.ChangeFontFamilyAction;
import soars.application.animator.common.menu.basic1.ChangeFontSizeAction;
import soars.application.animator.common.menu.basic1.ChangeFontStyleAction;
import soars.application.animator.common.menu.basic1.ChangeImageColorAction;
import soars.application.animator.common.menu.basic1.ChangeImagefileAction;
import soars.application.animator.common.menu.basic1.ChangeTextColorAction;
import soars.application.animator.common.menu.basic1.IBasicMenuHandler1;
import soars.application.animator.common.menu.basic1.InvisibleAction;
import soars.application.animator.common.menu.basic1.InvisibleNameAction;
import soars.application.animator.common.menu.basic1.RemoveImagefileAction;
import soars.application.animator.common.menu.basic1.VisibleAction;
import soars.application.animator.common.menu.basic1.VisibleNameAction;
import soars.application.animator.common.tool.CommonTool;
import soars.application.animator.main.AnimatorView;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.common.edit.EditSelectedImageDlg;
import soars.application.animator.object.player.ObjectManager;
import soars.application.animator.object.player.agent.AgentObjectManager;
import soars.application.animator.object.player.base.ObjectBase;
import soars.application.animator.object.player.base.ObjectProperty;
import soars.application.animator.object.player.spot.ISpotObjectManipulator;
import soars.common.utility.swing.color.ColorDlg;
import soars.common.utility.swing.table.base.StandardTable;
import soars.common.utility.tool.sort.QuickSort;

/**
 * The table to edit all objects.
 * @author kurata / SOARS project
 */
public class ObjectTable extends StandardTable implements IBasicMenuHandler1 {

	/**
	 * 
	 */
	private JMenuItem _arrange_spots_menuItem = null;

	/**
	 * 
	 */
	private String _open_directory_key = "";

	/**
	 * 
	 */
	private boolean _spot = false;

	/**
	 * 
	 */
	private HashMap _spot_position_map = null;

	/**
	 * @param open_directory_key the key mapped to the default directory for the file chooser dialog
	 * @param spot true if objects are spots.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public ObjectTable(String open_directory_key, boolean spot, Frame owner, Component parent) {
		super(owner, parent);
		_open_directory_key = open_directory_key;
		_spot = spot;
	}

	/**
	 * Returns true if this component is initialized successfully.
	 * @param order the array of all objects
	 * @return true if this component is initialized successfully
	 */
	public boolean setup(Vector order) {
		if ( !super.setup( true))
			return false;


		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);


		JTableHeader tableHeader = getTableHeader();
		tableHeader.setDefaultRenderer( new ObjectTableHeaderRenderer());


		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 9);


		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get( "object.table.header.name"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get( "object.table.header.visible"));
		defaultTableColumnModel.getColumn( 2).setHeaderValue(
			ResourceManager.get( "object.table.header.visible.name"));
		defaultTableColumnModel.getColumn( 3).setHeaderValue(
			ResourceManager.get( "object.table.header.image.color"));
		defaultTableColumnModel.getColumn( 4).setHeaderValue(
			ResourceManager.get( "object.table.header.text.color"));
		defaultTableColumnModel.getColumn( 5).setHeaderValue(
			ResourceManager.get( "object.table.header.font.family"));
		defaultTableColumnModel.getColumn( 6).setHeaderValue(
			ResourceManager.get( "object.table.header.font.style"));
		defaultTableColumnModel.getColumn( 7).setHeaderValue(
			ResourceManager.get( "object.table.header.font.size"));
		defaultTableColumnModel.getColumn( 8).setHeaderValue(
			ResourceManager.get( "object.table.header.imagefile.name"));


		for ( int i = 0; i < 9; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			if ( 1 == i || 2 == i) {
				ObjectCheckBoxTableCellRenderer objectCheckBoxTableCellRenderer
					= new ObjectCheckBoxTableCellRenderer();
				tableColumn.setCellRenderer( objectCheckBoxTableCellRenderer);
			} else {
				ObjectTableCellRenderer objectTableCellRenderer
					= new ObjectTableCellRenderer();
				tableColumn.setCellRenderer( objectTableCellRenderer);
			}
		}


		JComboBox comboBox = CommonTool.get_font_family_combo_box();
		TableColumn tableColumn = defaultTableColumnModel.getColumn( 5);
		DefaultCellEditor defaultCellEditor = new DefaultCellEditor( comboBox);
		tableColumn.setCellEditor( defaultCellEditor);


		comboBox = CommonTool.get_font_style_combo_box();
		tableColumn = defaultTableColumnModel.getColumn( 6);
		defaultCellEditor = new DefaultCellEditor( comboBox);
		tableColumn.setCellEditor( defaultCellEditor);


		comboBox = CommonTool.get_font_size_combo_box();
		comboBox.setRenderer( new FontSizeListCellRenderer());
		tableColumn = defaultTableColumnModel.getColumn( 7);
		defaultCellEditor = new DefaultCellEditor( comboBox);
		tableColumn.setCellEditor( defaultCellEditor);


		if ( _spot)
			_spot_position_map = new HashMap();


		ObjectBase[] objectBases = ( ObjectBase[])order.toArray( new ObjectBase[ 0]);
		QuickSort.sort( objectBases, new ObjectComparator( true, false));

		ObjectProperty[] objectProperties = get_ObjectProperties( objectBases);
		Object[] items = new Object[ 9];
		for ( int i = 0; i < objectProperties.length; ++i) {
			items[ 0] = objectProperties[ i];
			items[ 1] = new JCheckBox( "", objectProperties[ i]._visible);
			items[ 2] = new JCheckBox( "", objectProperties[ i]._visible_name);
			items[ 3] = objectProperties[ i]._image_color;
			items[ 4] = objectProperties[ i]._text_color;
			items[ 5] = objectProperties[ i]._font.getFamily();
			items[ 6] = CommonTool.get_font_style( objectProperties[ i]._font.getStyle());
			items[ 7] = String.valueOf( objectProperties[ i]._font.getSize());
			items[ 8] = objectProperties[ i]._image_filename;
			defaultTableModel.addRow( items);
		}

		if ( _spot) {
			for ( int i = 0; i < objectBases.length; ++i) {
				ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)objectBases[ i];
				_spot_position_map.put( objectBases[ i]._name, new Point( spotObjectManipulator.get_position()));
			}
		}

		setRowSelectionInterval( 0, 0);

		return true;
	}

	/**
	 * @param objectBases
	 * @return
	 */
	private ObjectProperty[] get_ObjectProperties(ObjectBase[] objectBases) {
		ObjectProperty objectProperty = null;
		List objectProperties = new ArrayList();
		for ( int i = 0; i < objectBases.length; ++i) {
			if ( _spot) {
				ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)objectBases[ i];
				if ( spotObjectManipulator.is_image_object())
					continue;
			}

			if ( null != objectProperty) {
				if ( objectBases[ i]._name.equals( objectProperty._name + String.valueOf( objectProperty._number + 1))) {
					++objectProperty._number;
					continue;
				}

				objectProperty = null;
			}

			if ( !objectBases[ i]._name.endsWith( "1"))
				objectProperties.add( new ObjectProperty( objectBases[ i]));
			else {
				if ( objectBases.length - 1 == i)
					objectProperties.add( new ObjectProperty( objectBases[ i]));
				else {
					String name = objectBases[ i]._name.substring( 0, objectBases[ i]._name.length() - 1);
					if ( !objectBases[ i + 1]._name.equals( name + "2"))
						objectProperties.add( new ObjectProperty( objectBases[ i]));
					else {
						objectProperty = new ObjectProperty( name, 1, objectBases[ i]);
						objectProperties.add( objectProperty);
					}
				}
			}
		}
		return ( ObjectProperty[])objectProperties.toArray( new ObjectProperty[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		JMenuItem item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "object.table.visible.menu"),
			new VisibleAction( ResourceManager.get( "object.table.visible.menu"), this),
			ResourceManager.get( "object.table.visible.mnemonic"),
			ResourceManager.get( "object.table.visible.stroke"));
		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "object.table.invisible.menu"),
			new InvisibleAction( ResourceManager.get( "object.table.invisible.menu"), this),
			ResourceManager.get( "object.table.invisible.mnemonic"),
			ResourceManager.get( "object.table.invisible.stroke"));

		_popupMenu.addSeparator();

		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "object.table.visible.name.menu"),
			new VisibleNameAction( ResourceManager.get( "object.table.visible.name.menu"), this),
			ResourceManager.get( "object.table.visible.name.mnemonic"),
			ResourceManager.get( "object.table.visible.name.stroke"));
		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "object.table.invisible.name.menu"),
			new InvisibleNameAction( ResourceManager.get( "object.table.invisible.name.menu"), this),
			ResourceManager.get( "object.table.invisible.name.mnemonic"),
			ResourceManager.get( "object.table.invisible.name.stroke"));

		_popupMenu.addSeparator();

		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "object.table.change.image.color.menu"),
			new ChangeImageColorAction( ResourceManager.get( "object.table.change.image.color.menu"), this),
			ResourceManager.get( "object.table.change.image.color.mnemonic"),
			ResourceManager.get( "object.table.change.image.color.stroke"));
		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "object.table.change.text.color.menu"),
			new ChangeTextColorAction( ResourceManager.get( "object.table.change.text.color.menu"), this),
			ResourceManager.get( "object.table.change.text.color.mnemonic"),
			ResourceManager.get( "object.table.change.text.color.stroke"));

		_popupMenu.addSeparator();

		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "object.table.change.font.family.menu"),
			new ChangeFontFamilyAction( ResourceManager.get( "object.table.change.font.family.menu"), this),
			ResourceManager.get( "object.table.change.font.family.mnemonic"),
			ResourceManager.get( "object.table.change.font.family.stroke"));
		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "object.table.change.font.style.menu"),
			new ChangeFontStyleAction( ResourceManager.get( "object.table.change.font.style.menu"), this),
			ResourceManager.get( "object.table.change.font.style.mnemonic"),
			ResourceManager.get( "object.table.change.font.style.stroke"));
		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "object.table.change.font.size.menu"),
			new ChangeFontSizeAction( ResourceManager.get( "object.table.change.font.size.menu"), this),
			ResourceManager.get( "object.table.change.font.size.mnemonic"),
			ResourceManager.get( "object.table.change.font.size.stroke"));

		_popupMenu.addSeparator();

		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "object.table.change.imagefile.menu"),
			new ChangeImagefileAction( ResourceManager.get( "object.table.change.imagefile.menu"), this),
			ResourceManager.get( "object.table.change.imagefile.mnemonic"),
			ResourceManager.get( "object.table.change.imagefile.stroke"));
		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "object.table.remove.imagefile.menu"),
			new RemoveImagefileAction( ResourceManager.get( "object.table.remove.imagefile.menu"), this),
			ResourceManager.get( "object.table.remove.imagefile.mnemonic"),
			ResourceManager.get( "object.table.remove.imagefile.stroke"));

		if ( _spot) {
			_popupMenu.addSeparator();

			_arrange_spots_menuItem = _userInterface.append_popup_menuitem(
				_popupMenu,
				ResourceManager.get( "object.table.arrange.spots.menu"),
				new ArrangeSpotsAction( ResourceManager.get( "object.table.arrange.spots.menu"), this),
				ResourceManager.get( "object.table.arrange.spots.mnemonic"),
				ResourceManager.get( "object.table.arrange.spots.stroke"));
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_pressed(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
		int row = rowAtPoint( mouseEvent.getPoint());
		if ( 0 > row || getRowCount() <= row)
			return;

		int column = columnAtPoint( mouseEvent.getPoint());
		if ( 0 > column || getColumnCount() <= column)
			return;

		column = convertColumnIndexToModel( column);

		switch ( column) {
			case 1:
			case 2:
				JCheckBox checkBox = ( JCheckBox)getModel().getValueAt( row, column);
				checkBox.setSelected( !checkBox.isSelected());
				break;
		}

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		int row = rowAtPoint( mouseEvent.getPoint());
		if ( 0 > row || getRowCount() <= row)
			return;

		int column = columnAtPoint( mouseEvent.getPoint());
		if ( 0 > column || getColumnCount() <= column)
			return;

		column = convertColumnIndexToModel( column);

		switch ( column) {
			case 3:
				on_change_color( row, column, "object.table.image.color.dialog.title");
				break;
			case 4:
				on_change_color( row, column, "object.table.text.color.dialog.title");
				break;
			case 8:
				on_change_imagefile( row, column);
				break;
		}
	}

	/**
	 * @param row
	 * @param column
	 * @param title_key
	 */
	private void on_change_color(int row, int column, String title_key) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		ObjectProperty objectProperty = ( ObjectProperty)defaultTableModel.getValueAt( row, 0);
		Color color = ( Color)defaultTableModel.getValueAt( row, column);
		color = ColorDlg.showDialog( _owner,
			ResourceManager.get( title_key)
				+ " - " + objectProperty._name,
			color,
			_parent,
			ResourceManager.get( "dialog.ok"),
			ResourceManager.get( "dialog.cancel"),
			ResourceManager.get( "make.color"));
		if ( null == color)
			return;
	
		defaultTableModel.setValueAt( color, row, column);
		repaint();
	}

	/**
	 * @param row
	 * @param column
	 */
	private void on_change_imagefile(int row, int column) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		ObjectProperty objectProperty = ( ObjectProperty)defaultTableModel.getValueAt( row, 0);
		EditSelectedImageDlg editSelectedImageDlg = new EditSelectedImageDlg( _owner,
			ResourceManager.get( "select.imagefile.dialog.title") + " - " + objectProperty._name,
			true, _open_directory_key, ( String)defaultTableModel.getValueAt( row, column));
		if ( !editSelectedImageDlg.do_modal())
			return;

		defaultTableModel.setValueAt( editSelectedImageDlg._new_image_filename, row, column);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		int[] indices = getSelectedRows();
		if ( 0 == indices.length)
			return;

		int row = rowAtPoint( mouseEvent.getPoint());
		if ( !isRowSelected( row))
			return;

		if ( _spot) {
			boolean condition = false;
			if ( 1 == indices.length) {
				DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
				ObjectProperty objectProperty = ( ObjectProperty)defaultTableModel.getValueAt( indices[ 0], 0);
				condition = ( 1 < objectProperty._number);
			}
			_arrange_spots_menuItem.setEnabled( 1 < indices.length || condition);
		}

		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_visible(java.awt.event.ActionEvent)
	 */
	public void on_visible(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < indices.length; ++i) {
			JCheckBox checkbox = ( JCheckBox)defaultTableModel.getValueAt( indices[ i], 1);
			checkbox.setSelected( true);
		}

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_invisible(java.awt.event.ActionEvent)
	 */
	public void on_invisible(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < indices.length; ++i) {
			JCheckBox checkbox = ( JCheckBox)defaultTableModel.getValueAt( indices[ i], 1);
			checkbox.setSelected( false);
		}

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_visible_name(java.awt.event.ActionEvent)
	 */
	public void on_visible_name(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < indices.length; ++i) {
			JCheckBox checkbox = ( JCheckBox)defaultTableModel.getValueAt( indices[ i], 2);
			checkbox.setSelected( true);
		}

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_invisible_name(java.awt.event.ActionEvent)
	 */
	public void on_invisible_name(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < indices.length; ++i) {
			JCheckBox checkbox = ( JCheckBox)defaultTableModel.getValueAt( indices[ i], 2);
			checkbox.setSelected( false);
		}

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_change_image_color(java.awt.event.ActionEvent)
	 */
	public void on_change_image_color(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		if ( 0 == indices.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		Color color = ColorDlg.showDialog( _owner,
			ResourceManager.get( "object.table.image.color.dialog.title"),
			( Color)defaultTableModel.getValueAt( indices[ 0], 3),
			_parent,
			ResourceManager.get( "dialog.ok"),
			ResourceManager.get( "dialog.cancel"),
			ResourceManager.get( "make.color"));
		if ( null == color)
			return;
	
		for ( int i = 0; i < indices.length; ++i)
			defaultTableModel.setValueAt( color, indices[ i], 3);

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_change_text_color(java.awt.event.ActionEvent)
	 */
	public void on_change_text_color(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		if ( 0 == indices.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		Color color = ColorDlg.showDialog( _owner,
			ResourceManager.get( "object.table.text.color.dialog.title"),
			( Color)defaultTableModel.getValueAt( indices[ 0], 4),
			_parent,
			ResourceManager.get( "dialog.ok"),
			ResourceManager.get( "dialog.cancel"),
			ResourceManager.get( "make.color"));
		if ( null == color)
			return;
	
		for ( int i = 0; i < indices.length; ++i)
			defaultTableModel.setValueAt( color, indices[ i], 4);

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_change_font_family(java.awt.event.ActionEvent)
	 */
	public void on_change_font_family(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		if ( 0 == indices.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		SelectFontFamilyDlg selectFontFamilyDlg
			= new SelectFontFamilyDlg( _owner, true,
				( String)defaultTableModel.getValueAt( indices[ 0], 5));
		if ( !selectFontFamilyDlg.do_modal( _parent))
			return;

		for ( int i = 0; i < indices.length; ++i)
			defaultTableModel.setValueAt( selectFontFamilyDlg._family_name, indices[ i], 5);

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_change_font_style(java.awt.event.ActionEvent)
	 */
	public void on_change_font_style(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		if ( 0 == indices.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		SelectFontStyleDlg selectFontStyleDlg
			= new SelectFontStyleDlg( _owner, true,
				( String)defaultTableModel.getValueAt( indices[ 0], 6));
		if ( !selectFontStyleDlg.do_modal( _parent))
			return;

		for ( int i = 0; i < indices.length; ++i)
			defaultTableModel.setValueAt( selectFontStyleDlg._style, indices[ i], 6);

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_change_font_size(java.awt.event.ActionEvent)
	 */
	public void on_change_font_size(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		if ( 0 == indices.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		SelectFontSizeDlg selectFontSizeDlg
			= new SelectFontSizeDlg( _owner, true,
				( String)defaultTableModel.getValueAt( indices[ 0], 7));
		if ( !selectFontSizeDlg.do_modal( _parent))
			return;

		for ( int i = 0; i < indices.length; ++i)
			defaultTableModel.setValueAt( selectFontSizeDlg._size, indices[ i], 7);

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_change_imagefile(java.awt.event.ActionEvent)
	 */
	public void on_change_imagefile(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		if ( 0 == indices.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		EditSelectedImageDlg editSelectedImageDlg = new EditSelectedImageDlg( _owner,
			ResourceManager.get( "select.imagefile.dialog.title"),
			true, _open_directory_key, ( String)defaultTableModel.getValueAt( indices[ 0], 8));
		if ( !editSelectedImageDlg.do_modal())
			return;

		for ( int i = 0; i < indices.length; ++i)
			defaultTableModel.setValueAt( editSelectedImageDlg._new_image_filename, indices[ i], 8);

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_remove_imagefile(java.awt.event.ActionEvent)
	 */
	public void on_remove_imagefile(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < indices.length; ++i)
			defaultTableModel.setValueAt( "", indices[ i], 8);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_arrange_spots(java.awt.event.ActionEvent)
	 */
	public void on_arrange_spots(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		if ( 0 == indices.length)
			return;

		List list = new ArrayList();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < indices.length; ++i) {
			ObjectProperty objectProperty = ( ObjectProperty)defaultTableModel.getValueAt( indices[ i], 0);
			if ( !objectProperty.get_objectBases( list))
				return;
		}

		CommonTool.arrange_spots( list);

		repaint();
	}

	/* (Non Javadoc)
	 * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
	 */
	public String getToolTipText(MouseEvent mouseEvent) {
		int row = rowAtPoint( mouseEvent.getPoint());
		if ( 0 > row || getRowCount() <= row)
			return null;

		int column = columnAtPoint( mouseEvent.getPoint());
		if ( 0 > column || getColumnCount() <= column)
			return null;

		if ( 8 != convertColumnIndexToModel( column))
			return null;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		String text = ( String)defaultTableModel.getValueAt( row, 8);
		if ( null == text || text.equals( ""))
			return null;

		return text;
	}

	/**
	 * 
	 */
	protected void on_ok() {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectProperty objectProperty = ( ObjectProperty)defaultTableModel.getValueAt( i, 0);
			JCheckBox checkbox = ( JCheckBox)defaultTableModel.getValueAt( i, 1);
			objectProperty._visible = checkbox.isSelected();
			checkbox = ( JCheckBox)defaultTableModel.getValueAt( i, 2);
			objectProperty._visible_name = checkbox.isSelected();
			objectProperty._image_color = ( Color)defaultTableModel.getValueAt( i, 3);
			objectProperty._text_color = ( Color)defaultTableModel.getValueAt( i, 4);
			objectProperty._font = new Font( ( String)defaultTableModel.getValueAt( i, 5),
				CommonTool.get_font_style( ( String)defaultTableModel.getValueAt( i, 6)),
				Integer.parseInt( ( String)defaultTableModel.getValueAt( i, 7)));
			objectProperty._image_filename = ( String)defaultTableModel.getValueAt( i, 8);
			objectProperty.update( _spot, ( Graphics2D)getGraphics());
		}
	}

	/**
	 * 
	 */
	protected void on_cancel() {
		if ( !_spot || null == _spot_position_map)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectProperty objectProperty = ( ObjectProperty)defaultTableModel.getValueAt( i, 0);
			objectProperty.restore( _spot_position_map);
		}

		AgentObjectManager.get_instance().arrange();
		ObjectManager.get_instance().update_preferred_size( AnimatorView.get_instance());
		AnimatorView.get_instance().repaint();
	}
}
