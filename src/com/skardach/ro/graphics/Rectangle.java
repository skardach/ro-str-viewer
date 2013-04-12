package com.skardach.ro.graphics;

/**
 * Basic Rectangle class.
 * @author Stanislaw Kardach
 *
 */
public class Rectangle<TPoint> {
	TPoint _a, _b, _c, _d;

	public Rectangle(TPoint iA, TPoint iB, TPoint iC, TPoint iD) {
		_a = iA;
		_b = iB;
		_c = iC;
		_d = iD;
	}
	public final TPoint get_a() {
		return _a;
	}
	public final TPoint get_b() {
		return _b;
	}
	public final TPoint get_c() {
		return _c;
	}
	public final TPoint get_d() {
		return _d;
	}
	
	@Override
	public String toString() {
		return String.format("[%s,%s,%s,%s]", _a, _b, _c, _d);
	}
}
