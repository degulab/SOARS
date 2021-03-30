/**
 * 
 */
package soars.application.animator.object.player.base;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soars.application.animator.object.player.agent.AgentObjectManager;
import soars.application.animator.object.player.spot.ISpotObjectManipulator;
import soars.application.animator.object.player.spot.SpotObjectManager;

/**
 * @author kurata
 *
 */
public class ObjectProperty {

	/**
	 * Name of this object.
	 */
	public String _name = "";

	/**
	 * True if this object is visible.
	 */
	public boolean _visible = true;

	/**
	 * True if the name string is visible.
	 */
	public boolean _visible_name = true;

	/**
	 * Name of image file.
	 */
	public String _image_filename = "";

	/**
	 * Color for this object.
	 */
	public Color _image_color = null;

	/**
	 * Color for the name of this object.
	 */
	public Color _text_color = null; 

	/**
	 * Font object for the name string of this object.
	 */
	public Font _font = null;

	/**
	 * 
	 */
	public int _number = 0;

	/**
	 * @param objectBase
	 */
	public ObjectProperty(ObjectBase objectBase) {
		super();
		_name = objectBase._name;
		_visible = objectBase._visible;
		_visible_name = objectBase._visibleName;
		_image_filename = objectBase._imageFilename;
		_image_color = new Color( objectBase._imageColor.getRed(), objectBase._imageColor.getGreen(), objectBase._imageColor.getBlue());
		_text_color = new Color( objectBase._textColor.getRed(), objectBase._textColor.getGreen(), objectBase._textColor.getBlue());
		_font = new Font( objectBase._font.getFamily(), objectBase._font.getStyle(), objectBase._font.getSize());
	}

	/**
	 * @param name
	 * @param number
	 * @param objectBase
	 */
	public ObjectProperty(String name, int number, ObjectBase objectBase) {
		super();
		_name = name;
		_number = number;
		_visible = objectBase._visible;
		_visible_name = objectBase._visibleName;
		_image_filename = objectBase._imageFilename;
		_image_color = new Color( objectBase._imageColor.getRed(), objectBase._imageColor.getGreen(), objectBase._imageColor.getBlue());
		_text_color = new Color( objectBase._textColor.getRed(), objectBase._textColor.getGreen(), objectBase._textColor.getBlue());
		_font = new Font( objectBase._font.getFamily(), objectBase._font.getStyle(), objectBase._font.getSize());
	}

	/**
	 * Returns true if this object is updated successfully.
	 * @param spot true if objects are spots.
	 * @param graphics2D the graphics object of JAVA
	 * @return true if objects are updated successfully
	 */
	public boolean update(boolean spot, Graphics2D graphics2D) {
		Map map = ( spot ? SpotObjectManager.get_instance() : AgentObjectManager.get_instance());
		if ( 0 == _number) {
			ObjectBase objectBase = ( ObjectBase)map.get( _name);
			if ( null == objectBase)
				return false;

			return objectBase.update( _visible, _visible_name, _image_color.getRed(), _image_color.getGreen(), _image_color.getBlue(),
				_text_color.getRed(), _text_color.getGreen(), _text_color.getBlue(), _font.getFamily(), _font.getStyle(), _font.getSize(), _image_filename, graphics2D);
		} else {
			for ( int i = 1; i <= _number; ++i) {
				ObjectBase objectBase = ( ObjectBase)map.get( _name + String.valueOf( i));
				if ( null == objectBase)
					return false;

				if ( !objectBase.update( _visible, _visible_name, _image_color.getRed(), _image_color.getGreen(), _image_color.getBlue(),
					_text_color.getRed(), _text_color.getGreen(), _text_color.getBlue(), _font.getFamily(), _font.getStyle(), _font.getSize(), _image_filename, graphics2D))
					return false;
			}
		}
		return true;
	}

	/**
	 * @param list
	 * @return
	 */
	public boolean get_objectBases(List list) {
		if ( 0 == _number) {
			ObjectBase objectBase = ( ObjectBase)SpotObjectManager.get_instance().get( _name);
			if ( null == objectBase)
				return false;

			list.add( objectBase);
		} else {
			for ( int i = 1; i <= _number; ++i) {
				ObjectBase objectBase = ( ObjectBase)SpotObjectManager.get_instance().get( _name + String.valueOf( i));
				if ( null == objectBase)
					return false;

				list.add( objectBase);
			}
		}
		return true;
	}

	/**
	 * @param spot_position_map
	 */
	public void restore(HashMap spot_position_map) {
		if ( 0 == _number) {
			ObjectBase objectBase = ( ObjectBase)SpotObjectManager.get_instance().get( _name);
			if ( null == objectBase)
				return;

			restore( objectBase, spot_position_map);
		} else {
			for ( int i = 1; i <= _number; ++i) {
				ObjectBase objectBase = ( ObjectBase)SpotObjectManager.get_instance().get( _name + String.valueOf( i));
				if ( null == objectBase)
					return;

				restore( objectBase, spot_position_map);
			}
		}
	}

	/**
	 * @param objectBase
	 * @param spot_position_map
	 */
	private void restore(ObjectBase objectBase, HashMap spot_position_map) {
		Point position = ( Point)spot_position_map.get( objectBase._name);
		if ( null == position)
			return;

		ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)objectBase;
		spotObjectManipulator.move_to( position.x, position.y, false);
	}

	/**
	 * 
	 */
	public void debug() {
		System.out.println( _name + " : " + String.valueOf( _number));
	}
}
