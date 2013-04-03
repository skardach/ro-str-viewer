package com.skardach.ro.common;

public class Color {
	float _r, _g, _b, _alpha;
	
	public Color(float iR, float iG, float iB, float iAlpha) {
		_r = iR;
		_g = iG;
		_b = iB;
		_alpha = iAlpha;
	}
	@Override
	public String toString() {
		return String.format("[%fR,%fG,%fB,%fA]", _r, _g, _b, _alpha);
	}
}
