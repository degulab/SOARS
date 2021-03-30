/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.inheritance;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.ImageObserver;

import soars.application.visualshell.object.role.base.Role;

/**
 * @author kurata
 *
 */
public class ConnectOutObject extends ConnectObject {

	/**
	 * @param parent
	 */
	public ConnectOutObject(Role parent) {
		super(parent);
	}

	/**
	 * @param connectOutObject
	 * @param parent
	 */
	public ConnectOutObject(ConnectOutObject connectOutObject, Role parent) {
		super(connectOutObject, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.inheritance.ConnectObject#draw(java.awt.Point, java.awt.Dimension, java.awt.Graphics2D, java.awt.image.ImageObserver)
	 */
	public void draw(Point position, Dimension dimension, Graphics2D graphics2D, ImageObserver imageObserver) {
		graphics2D.drawLine( position.x + ( dimension.width / 2), position.y + dimension.height, position.x + ( dimension.width / 2), position.y + dimension.height + _length);
		graphics2D.drawOval( position.x + ( dimension.width / 2) - _radius, position.y + dimension.height + _length, _radius * 2, _radius * 2);
	}

	/**
	 * @param point
	 * @param position
	 * @param dimension
	 * @return
	 */
	public boolean contains(Point point, Point position, Dimension dimension) {
		return contains( point, position.x + ( dimension.width / 2), position.y + dimension.height + _length + _radius);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.inheritance.ConnectObject#get_center()
	 */
	public Point get_center() {
		return new Point( _parent._position.x + ( _parent._dimension.width / 2), _parent._position.y + _parent._dimension.height + _length + _radius);
	}
}
