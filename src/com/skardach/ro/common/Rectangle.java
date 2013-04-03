package com.skardach.ro.common;

/**
 * Basic Rectangle class.
 * @author Stanislaw Kardach
 *
 */
public class Rectangle {
	Point _a, _b, _c, _d;

	public Rectangle(Point iA, Point iB, Point iC, Point iD) {
		_a = iA;
		_b = iB;
		_c = iC;
		_d = iD;
	}
	public final Point get_a() {
		return _a;
	}
	public final Point get_b() {
		return _b;
	}
	public final Point get_c() {
		return _c;
	}
	public final Point get_d() {
		return _d;
	}
	
	@Override
	public String toString() {
		return String.format("[%s,%s,%s,%s]", _a, _b, _c, _d);
	}
}
