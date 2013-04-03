package com.skardach.ro.common;

/**
 * Basic point class.
 * @author Stanislaw Kardach
 *
 */
public class Point {
	public float _x;
	public float _y;

	public Point(float iX, float iY) {
		_x = iX;
		_y = iY;
	}

	public final float get_x() {
		return _x;
	}

	public final float get_y() {
		return _y;
	}

	@Override
	public String toString() {
		return "(" + _x + "," + _y + ")";
	}
}