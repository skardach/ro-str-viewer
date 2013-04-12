package com.skardach.ro.graphics;
/**
 * Basic 3d point class.
 * @author Stanislaw Kardach
 *
 */
public class Point2D {
	public float _x;
	public float _y;

	public Point2D(float iX, float iY) {
		_x = iX;
		_y = iY;
	}

	public Point2D(Point2D iOther) {
		_x = iOther._x;
		_y = iOther._y;
	}

	@Override
	public String toString() {
		return "(" + _x + "," + _y + ")";
	}
}