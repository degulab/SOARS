/*
 * 2004/05/14
 */
package soars.application.animator.state;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import soars.application.animator.common.tool.CommonTool;
import soars.application.animator.main.AnimatorView;
import soars.application.animator.main.Application;
import soars.application.animator.main.Environment;
import soars.application.animator.main.MainFrame;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.common.tool.CommonObjectTool;
import soars.application.animator.object.player.ObjectManager;
import soars.application.animator.object.player.agent.AgentObject;
import soars.application.animator.object.player.agent.AgentObjectManager;
import soars.application.animator.object.player.spot.ISpotObjectManipulator;
import soars.application.animator.object.player.spot.SpotObject;
import soars.application.animator.object.player.spot.SpotObjectManager;
import soars.application.animator.observer.Observer;
import soars.application.animator.state.edit.EditSelectedImageObjectDlg;
import soars.application.animator.state.menu.ArrangeSpotsAction;
import soars.application.animator.state.menu.EditFlushBottomAction;
import soars.application.animator.state.menu.EditFlushLeftAction;
import soars.application.animator.state.menu.EditFlushRightAction;
import soars.application.animator.state.menu.EditFlushTopAction;
import soars.application.animator.state.menu.EditHorizontalEqualLayoutAction;
import soars.application.animator.state.menu.EditMoveToBackAction;
import soars.application.animator.state.menu.EditMoveToBottomAction;
import soars.application.animator.state.menu.EditMoveToFrontAction;
import soars.application.animator.state.menu.EditMoveToTopAction;
import soars.application.animator.state.menu.EditSelectAllAction;
import soars.application.animator.state.menu.EditSelectedImageObjectAction;
import soars.application.animator.state.menu.EditVerticalEqualLayoutAction;
import soars.application.animator.state.menu.NewImageObjectAction;
import soars.application.animator.state.menu.RemoveSelectedImageObjectAction;
import soars.application.animator.state.menu.RestoreSelectedImageObjectSizeAction;
import soars.common.utility.swing.gui.UserInterface;

/**
 * The "State pattern" class for editting.
 * @author kurata / SOARS project
 */
public class EditState extends StateBase {

	/**
	 * 
	 */
	private ISpotObjectManipulator _selectedSpotObjectManipulator = null;

	/**
	 * 
	 */
	private Point _mousePosition = new Point();

	/**
	 * 
	 */
	private Point _startPoint = null;

	/**
	 * 
	 */
	private Point _endPoint = new Point();

	/**
	 * TODO 値が Cursor.DEFAULT_CURSOR 以外の場合はマウスのドラッグで拡大／縮小
	 */
	private int _cursorType = Cursor.DEFAULT_CURSOR;

	/**
	 * 
	 */
	private Rectangle _currentRectangle = new Rectangle();

	/**
	 * 
	 */
	private Rectangle _previousRectangle = new Rectangle();

	/**
	 * 
	 */
	private Rectangle _clientRectangle = new Rectangle();

	/**
	 * 
	 */
	private UserInterface _userInterface = null;

	/**
	 * 
	 */
	private JPopupMenu _popupMenu = null;

	/**
	 * 
	 */
	private JMenuItem _newImageObjectMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editSelectedImageObjectMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _removeSelectedImageObjectMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _restoreSelectedImageObjectSizeMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editSelectAllMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editMoveToTopMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editMoveToBottomMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editMoveToFrontMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editMoveToBackMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editFlushTopMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editFlushBottomMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editFlushLeftMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editFlushRightMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editVerticalEqualLayoutMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editHorizontalEqualLayoutMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _arrangeSpotsMenuItem = null;

	/**
	 * Creates the instance of the "State pattern" class for editting.
	 */
	public EditState() {
		super();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#setup()
	 */
	public boolean setup() {
		return super.setup();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#cleanup()
	 */
	public void cleanup() {
		super.cleanup();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_enter()
	 */
	public boolean on_enter(String newStateName) {
		ObjectManager.get_instance().resize( AnimatorView.get_instance());
		ObjectManager.get_instance().set_preferred_size( AnimatorView.get_instance());
		AgentObjectManager.get_instance().arrange();
		setup_popup_menu();
		return super.on_enter(newStateName);
	}

	/**
	 * 
	 */
	private void setup_popup_menu() {
		_userInterface = new UserInterface();
		_popupMenu = new JPopupMenu();

		_newImageObjectMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "new.image.object.menu"),
			new NewImageObjectAction( ResourceManager.get( "new.image.object.menu"), this),
			ResourceManager.get( "new.image.object.mnemonic"),
			ResourceManager.get( "new.image.object.stroke"));
		_editSelectedImageObjectMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "edit.selected.image.object.menu"),
			new EditSelectedImageObjectAction( ResourceManager.get( "edit.selected.image.object.menu"), this),
			ResourceManager.get( "edit.selected.image.object.mnemonic"),
			ResourceManager.get( "edit.selected.image.object.stroke"));
		_removeSelectedImageObjectMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "remove.selected.image.object.menu"),
			new RemoveSelectedImageObjectAction( ResourceManager.get( "remove.selected.image.object.menu"), this),
			ResourceManager.get( "remove.selected.image.object.mnemonic"),
			ResourceManager.get( "remove.selected.image.object.stroke"));

		_popupMenu.addSeparator();

		_restoreSelectedImageObjectSizeMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "restore.selected.image.object.size.menu"),
			new RestoreSelectedImageObjectSizeAction( ResourceManager.get( "restore.selected.image.object.size.menu"), this),
			ResourceManager.get( "restore.selected.image.object.size.mnemonic"),
			ResourceManager.get( "restore.selected.image.object.size.stroke"));

		_popupMenu.addSeparator();

		_editSelectAllMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "edit.select.all.menu"),
			new EditSelectAllAction( ResourceManager.get( "edit.select.all.menu"), this),
			ResourceManager.get( "edit.select.all.mnemonic"),
			ResourceManager.get( "edit.select.all.stroke"));

		_popupMenu.addSeparator();

		_editMoveToTopMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "edit.move.to.top.menu"),
			new EditMoveToTopAction( ResourceManager.get( "edit.move.to.top.menu"), this),
			ResourceManager.get( "edit.move.to.top.mnemonic"),
			ResourceManager.get( "edit.move.to.top.stroke"));
		_editMoveToBottomMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "edit.move.to.bottom.menu"),
			new EditMoveToBottomAction( ResourceManager.get( "edit.move.to.bottom.menu"), this),
			ResourceManager.get( "edit.move.to.bottom.mnemonic"),
			ResourceManager.get( "edit.move.to.bottom.stroke"));
		_editMoveToFrontMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "edit.move.to.front.menu"),
			new EditMoveToFrontAction( ResourceManager.get( "edit.move.to.front.menu"), this),
			ResourceManager.get( "edit.move.to.front.mnemonic"),
			ResourceManager.get( "edit.move.to.front.stroke"));
		_editMoveToBackMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "edit.move.to.back.menu"),
			new EditMoveToBackAction( ResourceManager.get( "edit.move.to.back.menu"), this),
			ResourceManager.get( "edit.move.to.back.mnemonic"),
			ResourceManager.get( "edit.move.to.back.stroke"));

		_popupMenu.addSeparator();

		_editFlushTopMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "edit.flush.top.menu"),
			new EditFlushTopAction( ResourceManager.get( "edit.flush.top.menu"), this),
			ResourceManager.get( "edit.flush.top.mnemonic"),
			ResourceManager.get( "edit.flush.top.stroke"));
		_editFlushBottomMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "edit.flush.bottom.menu"),
			new EditFlushBottomAction( ResourceManager.get( "edit.flush.bottom.menu"), this),
			ResourceManager.get( "edit.flush.bottom.mnemonic"),
			ResourceManager.get( "edit.flush.bottom.stroke"));
		_editFlushLeftMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "edit.flush.left.menu"),
			new EditFlushLeftAction( ResourceManager.get( "edit.flush.left.menu"), this),
			ResourceManager.get( "edit.flush.left.mnemonic"),
			ResourceManager.get( "edit.flush.left.stroke"));
		_editFlushRightMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "edit.flush.right.menu"),
			new EditFlushRightAction( ResourceManager.get( "edit.flush.right.menu"), this),
			ResourceManager.get( "edit.flush.right.mnemonic"),
			ResourceManager.get( "edit.flush.right.stroke"));

		_popupMenu.addSeparator();

		_editVerticalEqualLayoutMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "edit.vertical.equal.layout.menu"),
			new EditVerticalEqualLayoutAction( ResourceManager.get( "edit.vertical.equal.layout.menu"), this),
			ResourceManager.get( "edit.vertical.equal.layout.mnemonic"),
			ResourceManager.get( "edit.vertical.equal.layout.stroke"));
		_editHorizontalEqualLayoutMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "edit.horizontal.equal.layout.menu"),
			new EditHorizontalEqualLayoutAction( ResourceManager.get( "edit.horizontal.equal.layout.menu"), this),
			ResourceManager.get( "edit.horizontal.equal.layout.mnemonic"),
			ResourceManager.get( "edit.horizontal.equal.layout.stroke"));

		_popupMenu.addSeparator();

		_arrangeSpotsMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get( "arrange.spots.menu"),
			new ArrangeSpotsAction( ResourceManager.get( "arrange.spots.menu"), this),
			ResourceManager.get( "arrange.spots.mnemonic"),
			ResourceManager.get( "arrange.spots.stroke"));
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_leave()
	 */
	public boolean on_leave(String newStateName) {
		ObjectManager.get_instance().select_all_objects( false);
		return super.on_leave(newStateName);
	}
	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#cancel()
	 */
	public void cancel() {
		ObjectManager.get_instance().select_all_objects( false);
		AnimatorView.get_instance().repaint();
		super.cancel();
	}


	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_resized(java.awt.event.ComponentEvent)
	 */
	public void on_resized(ComponentEvent componentEvent) {
		ObjectManager.get_instance().resize( AnimatorView.get_instance());
		super.on_resized(componentEvent);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	@SuppressWarnings("deprecation")
	public void on_mouse_left_double_click(MouseEvent mouseEvent) {
		if ( Application._demo)
			return;

		if ( 0 != ( ( InputEvent.CTRL_DOWN_MASK | InputEvent.CTRL_MASK
			| InputEvent.SHIFT_DOWN_MASK | InputEvent.SHIFT_MASK
			| InputEvent.META_DOWN_MASK | InputEvent.META_MASK)
			& mouseEvent.getModifiersEx()))
			return;

		ObjectManager.get_instance().select_all_objects( false);
		if ( null != _selectedSpotObjectManipulator) {
			_selectedSpotObjectManipulator.select( false);
			_selectedSpotObjectManipulator = null;
		}

		_selectedSpotObjectManipulator = SpotObjectManager.get_instance().get_spot( _mousePosition);
		if ( null != _selectedSpotObjectManipulator && _selectedSpotObjectManipulator.is_image_object()) {
			_selectedSpotObjectManipulator.select( true);

			AnimatorView.get_instance().repaint();

			if ( _selectedSpotObjectManipulator.edit( MainFrame.get_instance())) {
				AgentObjectManager.get_instance().arrange();
				ObjectManager.get_instance().update_preferred_size( AnimatorView.get_instance());
			}

			_selectedSpotObjectManipulator.select( false);
			_selectedSpotObjectManipulator = null;
		}

		AnimatorView.get_instance().repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_mouse_left_down(java.awt.event.MouseEvent)
	 */
	@SuppressWarnings("deprecation")
	public void on_mouse_left_down(MouseEvent mouseEvent) {
		if ( Application._demo)
			return;

		if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")
			&& 0 != ( ( InputEvent.CTRL_DOWN_MASK | InputEvent.CTRL_MASK) & mouseEvent.getModifiersEx())) {
			on_mouse_right_down( mouseEvent);
			return;
		}

		_mousePosition.setLocation( mouseEvent.getPoint());

		if ( 0 != ( ( InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK | InputEvent.META_DOWN_MASK) & mouseEvent.getModifiersEx())) {
			if ( null != _selectedSpotObjectManipulator) {
//				_selected_spotObjectManipulator.select( false);
				_selectedSpotObjectManipulator = null;
			}

			ISpotObjectManipulator selectedSpotObjectManipulator = SpotObjectManager.get_instance().get_spot( _mousePosition);
			if ( null == selectedSpotObjectManipulator)
				return;

			selectedSpotObjectManipulator.select( !selectedSpotObjectManipulator.selected());
		} else {
			if ( null != _selectedSpotObjectManipulator && _selectedSpotObjectManipulator.is_image_object()) {
				// TODO 既に１画像だけが選択されている
				int cursorType = CommonObjectTool.get_cursor_type( _selectedSpotObjectManipulator.get_image_position(), _selectedSpotObjectManipulator.get_dimension(), mouseEvent.getPoint());
				if ( Cursor.DEFAULT_CURSOR != cursorType) {
					// 拡大/縮小コントロールがクリックされたので拡大／縮小モードになる
					AnimatorView.get_instance().setCursor( new Cursor( cursorType));
					AnimatorView.get_instance().repaint();
					_cursorType = cursorType;
					return;
				} else {
					ISpotObjectManipulator selectedSpotObjectManipulator = SpotObjectManager.get_instance().get_spot( _mousePosition);
					if ( _selectedSpotObjectManipulator == selectedSpotObjectManipulator)
						// クリックされたのが同じものなのでなにもしない
						return;
				}
			}

			_selectedSpotObjectManipulator = SpotObjectManager.get_instance().get_spot( _mousePosition);
			if ( null != _selectedSpotObjectManipulator) {
				Vector<SpotObject> spots = new Vector<SpotObject>();
				SpotObjectManager.get_instance().get_selected_spot_and_image( spots);
				if ( spots.contains( _selectedSpotObjectManipulator))
					_selectedSpotObjectManipulator = null;
				else {
					ObjectManager.get_instance().select_all_objects( false);
					_selectedSpotObjectManipulator.select( true);
				}
			} else {

//				Vector<SpotObject> spots = new Vector<SpotObject>();
//				SpotObjectManager.get_instance().get_selected_spot_and_image( spots);
//				if ( 1 == spots.size()) {
//					int cursorType = CommonObjectTool.get_cursor_type( spots.get( 0).get_image_position(), spots.get( 0).get_dimension(), mouseEvent.getPoint());
//					if ( Cursor.DEFAULT_CURSOR != cursorType) {
//						AnimatorView.get_instance().setCursor( new Cursor( cursorType));
//						AnimatorView.get_instance().repaint();
//						_selectedSpotObjectManipulator = spots.get( 0);
//						_selectedSpotObjectManipulator.select( true);
//						_cursorType = cursorType;
//						return;
//					}
//				}

				ObjectManager.get_instance().select_all_objects( false);
				_startPoint = _mousePosition;
				_endPoint.setLocation( mouseEvent.getPoint());
				_previousRectangle.setBounds( mouseEvent.getPoint().x, mouseEvent.getPoint().y, 0, 0);
			}
		}

		AnimatorView.get_instance().repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_mouse_left_up(java.awt.event.MouseEvent)
	 */
	@SuppressWarnings("deprecation")
	public void on_mouse_left_up(MouseEvent mouseEvent) {
		if ( Application._demo)
			return;

		// TODO 拡大／縮小モードを解除
		_cursorType = Cursor.DEFAULT_CURSOR;

		if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")
			&& 0 != ( ( InputEvent.CTRL_DOWN_MASK | InputEvent.CTRL_MASK) & mouseEvent.getModifiersEx())) {
			on_mouse_right_up( mouseEvent);
			return;
		}

//		if ( null != _selected_spotObjectManipulator) {
//			_selected_spotObjectManipulator.select( false);
//			_selected_spotObjectManipulator = null;
//		} else {
			if ( null == _startPoint)
				return;

			_startPoint = null;
//		}

		AnimatorView.get_instance().repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_mouse_dragged(java.awt.event.MouseEvent)
	 */
	@SuppressWarnings("deprecation")
	public void on_mouse_dragged(MouseEvent mouseEvent) {
		if ( Application._demo)
			return;

		if ( 0 == ( ( InputEvent.BUTTON1_DOWN_MASK | InputEvent.BUTTON1_MASK) & mouseEvent.getModifiersEx()))
			return;

		if ( 0 != ( ( InputEvent.ALT_DOWN_MASK | InputEvent.ALT_GRAPH_DOWN_MASK
			| InputEvent.ALT_GRAPH_MASK | InputEvent.ALT_MASK
			| InputEvent.CTRL_DOWN_MASK | InputEvent.CTRL_MASK
			| InputEvent.META_DOWN_MASK | InputEvent.META_MASK
			| InputEvent.SHIFT_DOWN_MASK | InputEvent.SHIFT_MASK) & mouseEvent.getModifiersEx()))
			return;

		if ( null != _selectedSpotObjectManipulator) {
			// TODO 拡大／縮小実行
			if ( Cursor.DEFAULT_CURSOR != _cursorType) {
				_selectedSpotObjectManipulator.update_dimension( _cursorType, mouseEvent.getX() - _mousePosition.x, mouseEvent.getY() - _mousePosition.y);
//				_selectedSpotObjectManipulator.set_dimension( new Dimension(
//					_selectedSpotObjectManipulator.get_dimension().width + mouseEvent.getX() - _mousePosition.x,
//					_selectedSpotObjectManipulator.get_dimension().height + mouseEvent.getY() - _mousePosition.y));

				_mousePosition.setLocation( mouseEvent.getPoint());

				if ( !AnimatorView.get_instance().getVisibleRect().contains( mouseEvent.getPoint()))
					AnimatorView.get_instance().scrollRectToVisible(
						new Rectangle( _selectedSpotObjectManipulator.get_position(),
							_selectedSpotObjectManipulator.get_dimension()));

				ObjectManager.get_instance().update_preferred_size( AnimatorView.get_instance());

				AnimatorView.get_instance().repaint();

				return;
			}

			_selectedSpotObjectManipulator.move(
				mouseEvent.getX() - _mousePosition.x,
				mouseEvent.getY() - _mousePosition.y,
				true);
			_mousePosition.setLocation( mouseEvent.getPoint());

			if ( !AnimatorView.get_instance().getVisibleRect().contains( mouseEvent.getPoint()))
				AnimatorView.get_instance().scrollRectToVisible(
					new Rectangle( _selectedSpotObjectManipulator.get_position(),
						_selectedSpotObjectManipulator.get_dimension()));

			ObjectManager.get_instance().update_preferred_size( AnimatorView.get_instance());
		} else {
			if ( null == _startPoint) {
				Vector<SpotObject> spots = new Vector<SpotObject>();
				SpotObjectManager.get_instance().get_selected_spot_and_image( spots);
				if ( spots.isEmpty())
					return;

				move( spots, mouseEvent.getX() - _mousePosition.x, mouseEvent.getY() - _mousePosition.y);
				_mousePosition.setLocation( mouseEvent.getPoint());
			} else {
				_endPoint.setLocation( mouseEvent.getPoint());

				if ( 0 > _endPoint.x)
					_endPoint.x = 0;
				if ( 0 > _endPoint.y)
					_endPoint.y = 0;

				_currentRectangle.setBounds(
					Math.min( _startPoint.x, _endPoint.x),
					Math.min( _startPoint.y, _endPoint.y),
					Math.abs( _startPoint.x - _endPoint.x + 1),
					Math.abs( _startPoint.y - _endPoint.y + 1));

				if ( AnimatorView.get_instance().getVisibleRect().contains( _currentRectangle))
					AnimatorView.get_instance().scrollRectToVisible( _currentRectangle);
				else {
					_clientRectangle.setBounds( AnimatorView.get_instance().getVisibleRect());

					if (  _currentRectangle.x < AnimatorView.get_instance().getVisibleRect().x
						|| _currentRectangle.x + _currentRectangle.width > AnimatorView.get_instance().getVisibleRect().x + AnimatorView.get_instance().getVisibleRect().width) {
						if ( _currentRectangle.x == _previousRectangle.x) {
							if ( 0 != _currentRectangle.x)
								_clientRectangle.x = _currentRectangle.x + _currentRectangle.width - AnimatorView.get_instance().getVisibleRect().width;
						} else
							_clientRectangle.x = _currentRectangle.x;
					}

					if ( _currentRectangle.y < AnimatorView.get_instance().getVisibleRect().y
						|| _currentRectangle.y + _currentRectangle.height > AnimatorView.get_instance().getVisibleRect().y + AnimatorView.get_instance().getVisibleRect().height) {
						if ( _currentRectangle.y == _previousRectangle.y) {
							if ( 0 != _currentRectangle.y)
								_clientRectangle.y = _currentRectangle.y + _currentRectangle.height - AnimatorView.get_instance().getVisibleRect().height;
						} else
							_clientRectangle.y = _currentRectangle.y;
					}

					AnimatorView.get_instance().scrollRectToVisible( _clientRectangle);
				}

				_previousRectangle.setBounds( _currentRectangle);

				ObjectManager.get_instance().select_object( _currentRectangle);
			}
		}

		AnimatorView.get_instance().repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_mouse_moved(java.awt.event.MouseEvent)
	 */
	public void on_mouse_moved(MouseEvent mouseEvent) {
		if ( null == _selectedSpotObjectManipulator || !_selectedSpotObjectManipulator.is_image_object()) {
			AnimatorView.get_instance().setCursor( Cursor.getDefaultCursor());
			AnimatorView.get_instance().repaint();
			return;
		}

		// TODO １画像だけが選択されていて、マウスカーソルが拡大/縮小コントロール上にある場合
		AnimatorView.get_instance().setCursor( CommonObjectTool.get_cursor( _selectedSpotObjectManipulator.get_image_position(), _selectedSpotObjectManipulator.get_dimension(), mouseEvent.getPoint()));
		AnimatorView.get_instance().repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_mouse_right_down(java.awt.event.MouseEvent)
	 */
	public void on_mouse_right_down(MouseEvent mouseEvent) {
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	public void on_mouse_right_up(MouseEvent mouseEvent) {
		if ( Application._demo)
			return;

//		if ( null != _selected_spotObjectManipulator)
//			return;

		_mousePosition.setLocation( mouseEvent.getPoint());

		ISpotObjectManipulator spotObjectManipulator = SpotObjectManager.get_instance().get_spot( _mousePosition);
		if ( !SpotObjectManager.get_instance().exist_selected_spot()) {
			if ( null != spotObjectManipulator) {
				spotObjectManipulator.select( true);

				AnimatorView.get_instance().repaint();
			}
		}

		_newImageObjectMenuItem.setEnabled( true);
		_editSelectedImageObjectMenuItem.setEnabled( false);
		_removeSelectedImageObjectMenuItem.setEnabled( false);
		_restoreSelectedImageObjectSizeMenuItem.setEnabled( false);

		_editSelectAllMenuItem.setEnabled( true);

		_editMoveToTopMenuItem.setEnabled( false);
		_editMoveToBottomMenuItem.setEnabled( false);
		_editMoveToFrontMenuItem.setEnabled( false);
		_editMoveToBackMenuItem.setEnabled( false);

		_editFlushTopMenuItem.setEnabled( false);
		_editFlushBottomMenuItem.setEnabled( false);
		_editFlushLeftMenuItem.setEnabled( false);
		_editFlushRightMenuItem.setEnabled( false);

		_editVerticalEqualLayoutMenuItem.setEnabled( false);
		_editHorizontalEqualLayoutMenuItem.setEnabled( false);

		_arrangeSpotsMenuItem.setEnabled( false);

		Vector<SpotObject> spots = new Vector<SpotObject>();
		SpotObjectManager.get_instance().get_selected_spot( spots);

		Vector<SpotObject> images = new Vector<SpotObject>();
		SpotObjectManager.get_instance().get_selected_image( images);

//		if ( !spots.isEmpty() || !images.isEmpty()) {
		if ( !spots.isEmpty() || !images.isEmpty() || null != _selectedSpotObjectManipulator) {
			if ( null != spotObjectManipulator
				&& ( spots.contains( spotObjectManipulator) || images.contains( spotObjectManipulator))) {
				if ( spots.contains( spotObjectManipulator)) {
					_arrangeSpotsMenuItem.setEnabled( 1 < spots.size());
				} else if ( images.contains( spotObjectManipulator)) {
					_editSelectedImageObjectMenuItem.setEnabled( true);
					_removeSelectedImageObjectMenuItem.setEnabled( true);
					SpotObject imageObject = ( SpotObject)spotObjectManipulator;
					if ( !imageObject._imageFilename.equals( ""))
						_restoreSelectedImageObjectSizeMenuItem.setEnabled( true);
				}

				if ( null != _selectedSpotObjectManipulator && _selectedSpotObjectManipulator == spotObjectManipulator)
					_arrangeSpotsMenuItem.setEnabled( true);

				_editMoveToTopMenuItem.setEnabled( 0 < ( spots.size() + images.size()));
				_editMoveToBottomMenuItem.setEnabled( 0 < ( spots.size() + images.size()));
				_editMoveToFrontMenuItem.setEnabled( 0 < ( spots.size() + images.size()));
				_editMoveToBackMenuItem.setEnabled( 0 < ( spots.size() + images.size()));

				_editFlushTopMenuItem.setEnabled( 1 < ( spots.size() + images.size()));
				_editFlushBottomMenuItem.setEnabled( 1 < ( spots.size() + images.size()));
				_editFlushLeftMenuItem.setEnabled( 1 < ( spots.size() + images.size()));
				_editFlushRightMenuItem.setEnabled( 1 < ( spots.size() + images.size()));

				_editVerticalEqualLayoutMenuItem.setEnabled( 2 < ( spots.size() + images.size()));
				_editHorizontalEqualLayoutMenuItem.setEnabled( 2 < ( spots.size() + images.size()));
			}
		}

		_popupMenu.show( AnimatorView.get_instance(), mouseEvent.getX(), mouseEvent.getY());
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#paint(java.awt.Graphics)
	 */
	public void paint(Graphics graphics) {
		ObjectManager.get_instance().draw( graphics, AnimatorView.get_instance(), AnimatorView.get_instance().getVisibleRect(), _startPoint, _endPoint);
		super.paint(graphics);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_key_pressed(java.awt.event.KeyEvent)
	 */
	@SuppressWarnings("deprecation")
	public void on_key_pressed(KeyEvent keyEvent) {
		if ( Application._demo)
			return;

		switch ( keyEvent.getKeyCode()) {
			case KeyEvent.VK_A:
				if ( 0 != ( Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() & keyEvent.getModifiers())) {
					ObjectManager.get_instance().select_all_objects( true);
					AnimatorView.get_instance().repaint();
				}
				break;
			case KeyEvent.VK_ESCAPE:
				ObjectManager.get_instance().select_all_objects( false);
				AnimatorView.get_instance().repaint();
				break;
			case KeyEvent.VK_LEFT:
				move( -1, 0);
				AnimatorView.get_instance().repaint();
				break;
			case KeyEvent.VK_UP:
				move( 0, -1);
				AnimatorView.get_instance().repaint();
				break;
			case KeyEvent.VK_RIGHT:
				move( 1, 0);
				AnimatorView.get_instance().repaint();
				break;
			case KeyEvent.VK_DOWN:
				move( 0, 1);
				AnimatorView.get_instance().repaint();
				break;
		}

		super.on_key_pressed(keyEvent);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_key_pressed(java.awt.event.ActionEvent)
	 */
	public void on_key_pressed(ActionEvent actionEvent) {
		if ( Application._demo)
			return;

		if ( actionEvent.getActionCommand().equals( "escape")) {
			ObjectManager.get_instance().select_all_objects( false);
			AnimatorView.get_instance().repaint();
		} else if ( actionEvent.getActionCommand().equals( "left")) {
			move( -1, 0);
			AnimatorView.get_instance().repaint();
		} else if ( actionEvent.getActionCommand().equals( "up")) {
			move( 0, -1);
			AnimatorView.get_instance().repaint();
		} else if ( actionEvent.getActionCommand().equals( "right")) {
			move( 1, 0);
			AnimatorView.get_instance().repaint();
		} else if ( actionEvent.getActionCommand().equals( "down")) {
			move( 0, 1);
			AnimatorView.get_instance().repaint();
		}

		super.on_key_pressed(actionEvent);
	}

	/**
	 * @param deltaX
	 * @param deltaY
	 */
	private void move(int deltaX, int deltaY) {
		Vector<SpotObject> spots = new Vector<SpotObject>();
		SpotObjectManager.get_instance().get_selected_spot_and_image( spots);
		move( spots, deltaX, deltaY);
	}

	/**
	 * @param spots
	 * @param deltaX
	 * @param deltaY
	 */
	private void move(Vector<SpotObject> spots, int deltaX, int deltaY) {
		for ( int i = 0; i < spots.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)spots.get( i);
			if ( !spotObjectManipulator.test( deltaX, deltaY))
				return;
		}

		for ( int i = 0; i < spots.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)spots.get( i);
			spotObjectManipulator.move( deltaX, deltaY, true);
		}

		ObjectManager.get_instance().update_preferred_size( AnimatorView.get_instance());
	}

	/**
	 * @param mousePosition
	 * @return
	 */
	private AgentObject get_agent(Point mousePosition) {
		AgentObject agentObject = AgentObjectManager.get_instance().get_agent( mousePosition, "edit");
		if ( null == agentObject)
			return null;

		if ( null == agentObject._spotObjectManipulator || !agentObject._spotObjectManipulator.is_visible())
			return null;

		return agentObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.state.StateBase#get_tooltip_text(java.awt.event.MouseEvent)
	 */
	public String get_tooltip_text(MouseEvent mouseEvent) {
		AgentObject agentObject = get_agent( mouseEvent.getPoint());
		if ( null != agentObject)
			return agentObject.get_tooltip_text( "edit");
		else {
			ISpotObjectManipulator spotObjectManipulator = SpotObjectManager.get_instance().get_spot( mouseEvent.getPoint());
			return ( ( null == spotObjectManipulator) ? null : spotObjectManipulator.get_tooltip_text( "edit"));
		}
	}

	/**
	 * Invoked when the "Edit selected agents" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_selected_agent(ActionEvent actionEvent) {
	}

	/**
	 * Invoked when the "Edit selected spots" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_selected_spot(ActionEvent actionEvent) {
	}

	/**
	 * Invoked when the "New image" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_new_image_object(ActionEvent actionEvent) {
		ObjectManager.get_instance().select_all_objects( false);
		AnimatorView.get_instance().repaint();

		SpotObject spotObject = SpotObjectManager.get_instance().append(
			SpotObjectManager.get_instance().get_unique_name(),
			_mousePosition, AnimatorView.get_instance());
		ObjectManager.get_instance().update_preferred_size( AnimatorView.get_instance());

		spotObject.select( true);

		// TODO 拡大／縮小モードの為に必要
		_selectedSpotObjectManipulator = spotObject;

		AnimatorView.get_instance().repaint();
	}

	/**
	 * Invoked when the "Edit selected image" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_selected_image_object(ActionEvent actionEvent) {
		Vector<SpotObject> images = new Vector<SpotObject>();
		SpotObjectManager.get_instance().get_selected_image( images);
		if ( images.isEmpty())
			return;

		AgentObjectManager.get_instance().select_all_objects( false);
		SpotObjectManager.get_instance().select_all_spot_objects( false);
		AnimatorView.get_instance().repaint();

		EditSelectedImageObjectDlg editSelectedImageObjectDlg = new EditSelectedImageObjectDlg( MainFrame.get_instance(),
			ResourceManager.get( "edit.selected.image.object.dialog.title"),
			true, Environment._openImageObjectImageDirectoryKey, images);

		if ( editSelectedImageObjectDlg.do_modal()) {
			ObjectManager.get_instance().update_preferred_size( AnimatorView.get_instance());
		}

		AnimatorView.get_instance().repaint();
	}

	/**
	 * Invoked when the "Remove selected image" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_remove_selected_image_object(ActionEvent actionEvent) {
		Vector<SpotObject> images = new Vector<SpotObject>();
		SpotObjectManager.get_instance().get_selected_image( images);
		if ( images.isEmpty())
			return;

		AgentObjectManager.get_instance().select_all_objects( false);
		SpotObjectManager.get_instance().select_all_spot_objects( false);
		AnimatorView.get_instance().repaint();

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			MainFrame.get_instance(),
			ResourceManager.get( "edit.image.object.confirm.remove.message"),
			ResourceManager.get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {
			SpotObjectManager.get_instance().remove( images);
			ObjectManager.get_instance().update_preferred_size( AnimatorView.get_instance());
			ObjectManager.get_instance().select_all_objects( false);
		}

		AnimatorView.get_instance().repaint();
	}

	/**
	 * @param actionEvent
	 */
	public void on_restore_selected_image_object_size(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		Vector<SpotObject> images = new Vector<SpotObject>();
		SpotObjectManager.get_instance().get_selected_image( images);
		if ( images.isEmpty())
			return;

		for ( int i = 0; i < images.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)images.get( i);
			spotObjectManipulator.restore_image_size();
		}

		AnimatorView.get_instance().repaint();
	}

	/**
	 * Invoked when the "Select all" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_select_all(ActionEvent actionEvent) {
		ObjectManager.get_instance().select_all_objects( true);
		AnimatorView.get_instance().repaint();
	}

	/**
	 * Move the selected spot to top.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_move_to_top(ActionEvent actionEvent) {
		SpotObjectManager.get_instance().move_to_top();
		AnimatorView.get_instance().repaint();
	}

	/**
	 * Move the selected spot to bottom.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_move_to_bottom(ActionEvent actionEvent) {
		SpotObjectManager.get_instance().move_to_bottom();
		AnimatorView.get_instance().repaint();
	}

	/**
	 * Move the selected spot to front.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_move_to_front(ActionEvent actionEvent) {
		SpotObjectManager.get_instance().move_to_front();
		AnimatorView.get_instance().repaint();
	}

	/**
	 * Move the selected spot to back.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_move_to_back(ActionEvent actionEvent) {
		SpotObjectManager.get_instance().move_to_back();
		AnimatorView.get_instance().repaint();
	}

	/**
	 * Flushes top the selected objects.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_flush_top(ActionEvent actionEvent) {
		ObjectManager.get_instance().flush_top( AnimatorView.get_instance());
	}

	/**
	 * Flushes bottom the selected objects.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_flush_bottom(ActionEvent actionEvent) {
		ObjectManager.get_instance().flush_bottom( AnimatorView.get_instance());
	}

	/**
	 * Flushes left the selected objects.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_flush_left(ActionEvent actionEvent) {
		ObjectManager.get_instance().flush_left( AnimatorView.get_instance());
	}

	/**
	 * Flushes the selected objects right.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_flush_right(ActionEvent actionEvent) {
		ObjectManager.get_instance().flush_right( AnimatorView.get_instance());
	}

	/**
	 * Makes vertical gaps between the selected objects equal.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_vertical_equal_layout(ActionEvent actionEvent) {
		ObjectManager.get_instance().vertical_equal_layout( AnimatorView.get_instance());
	}

	/**
	 * Makes horizontal gaps between the selected objects equal.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_horizontal_equal_layout(ActionEvent actionEvent) {
		ObjectManager.get_instance().horizontal_equal_layout( AnimatorView.get_instance());
	}

	/**
	 * Arranges the selected spots.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_arrange_spots(ActionEvent actionEvent) {
		Vector<SpotObject> spots = new Vector<SpotObject>();
		SpotObjectManager.get_instance().get_selected_spot( spots);
		if ( spots.isEmpty())
			return;

		AgentObjectManager.get_instance().select_all_objects( false);
		SpotObjectManager.get_instance().select_all_image_objects( false);
		AnimatorView.get_instance().repaint();

		CommonTool.arrange_spots( spots);

		Observer.get_instance().modified();
	}
}
