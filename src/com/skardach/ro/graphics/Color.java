package com.skardach.ro.graphics;
/**
 * Little helper class for encapsulating RGBA color definition
 * @author Stanislaw Kardach
 *
 */
public class Color {
	public float _r, _g, _b, _alpha;

	public Color(float iR, float iG, float iB, float iAlpha) {
		_r = iR;
		_g = iG;
		_b = iB;
		_alpha = iAlpha;
	}

	public Color(Color iOther) {
		_r = iOther._r;
		_g = iOther._g;
		_b = iOther._b;
		_alpha = iOther._alpha;
	}

	@Override
	public String toString() {
		return String.format("[%fR,%fG,%fB,%fA]", _r, _g, _b, _alpha);
	}
}
