package com.skardach.ro.resource.str;
import com.skardach.ro.graphics.Color;
import com.skardach.ro.graphics.Point;
import com.skardach.ro.graphics.Rectangle;

/**
 * Describes Key frame of an effect animation
 * @author Stanislaw Kardach
 */
public class KeyFrame {
	int _framenum;
	KeyFrameType _frameType; // subclass maybe? Take a look at roengine processing 
	Point _position;
	float _u; // Vector 1?
	float _v;
	float _us;
	float _vs; // Vector 1 end?
	float _u2; // Vector 2?
	float _v2;
	float _us2;
	float _vs2; // Vector 2 end?
	Rectangle _rectangle;
	float _animationFrame;
    AnimationType _animationType;
    float _animationDelta; ///< Texture animation delta.
    float _rotation; //< Rotation [0,1024[ is equivalent to [0,360[ degrees
    Color _color;
    int _sourceAlpha; //< Source blend mode (D3DBLEND_*)
    int _destAlpha; //< Destination blend mode (D3DBLEND_*)
    MultiTextureMode _multiTexturePreset;
    
	public final int get_framenum() {
		return _framenum;
	}
	public final KeyFrameType get_frameType() {
		return _frameType;
	}
	public final Point get_position() {
		return _position;
	}
	public final float get_u() {
		return _u;
	}
	public final float get_v() {
		return _v;
	}
	public final float get_us() {
		return _us;
	}
	public final float get_vs() {
		return _vs;
	}
	public final float get_u2() {
		return _u2;
	}
	public final float get_v2() {
		return _v2;
	}
	public final float get_us2() {
		return _us2;
	}
	public final float get_vs2() {
		return _vs2;
	}
	public final Rectangle get_rectangle() {
		return _rectangle;
	}
	public final float get_animationFrame() {
		return _animationFrame;
	}
	public final AnimationType get_animationType() {
		return _animationType;
	}
	public final float get_animationDelta() {
		return _animationDelta;
	}
	public final float get_rotation() {
		return _rotation;
	}
	public final Color get_color() {
		return _color;
	}
	public final int get_sourceAlpha() {
		return _sourceAlpha;
	}
	public final int get_destAlpha() {
		return _destAlpha;
	}
	public final MultiTextureMode get_multiTexturePreset() {
		return _multiTexturePreset;
	}
	public final void set_framenum(int _framenum) {
		this._framenum = _framenum;
	}
	public final void set_frameType(KeyFrameType _frameType) {
		this._frameType = _frameType;
	}
	public final void set_position(Point _position) {
		this._position = _position;
	}
	public final void set_u(float _u) {
		this._u = _u;
	}
	public final void set_v(float _v) {
		this._v = _v;
	}
	public final void set_us(float _us) {
		this._us = _us;
	}
	public final void set_vs(float _vs) {
		this._vs = _vs;
	}
	public final void set_u2(float _u2) {
		this._u2 = _u2;
	}
	public final void set_v2(float _v2) {
		this._v2 = _v2;
	}
	public final void set_us2(float _us2) {
		this._us2 = _us2;
	}
	public final void set_vs2(float _vs2) {
		this._vs2 = _vs2;
	}
	public final void set_rectangle(Rectangle _rectangle) {
		this._rectangle = _rectangle;
	}
	public final void set_animationFrame(float _animationFrame) {
		this._animationFrame = _animationFrame;
	}
	public final void set_animationType(AnimationType _animationType) {
		this._animationType = _animationType;
	}
	public final void set_animationDelta(float _animationDelta) {
		this._animationDelta = _animationDelta;
	}
	public final void set_rotation(float _rz) {
		this._rotation = _rz;
	}
	public final void set_color(Color _color) {
		this._color = _color;
	}
	public final void set_sourceAlpha(int _sourceAlpha) {
		this._sourceAlpha = _sourceAlpha;
	}
	public final void set_destAlpha(int _destAlpha) {
		this._destAlpha = _destAlpha;
	}
	public final void set_multiTexturePreset(MultiTextureMode _multiTexturePreset) {
		this._multiTexturePreset = _multiTexturePreset;
	}
	
	@Override
	public String toString() {
		return toString("");
	}
	public String toString(String iPrefix) {
		return
			iPrefix + "KeyFrame [\n"+
			iPrefix + "  _framenum=" + _framenum + ",\n"+
			iPrefix + "  _frameType=" + _frameType + ",\n"+
			iPrefix + "  _position=" + _position + ",\n"+
			iPrefix + "  _u=" + _u + ",\n"+
			iPrefix + "  _v=" + _v + ",\n"+
			iPrefix + "  _us=" + _us + ",\n"+
			iPrefix + "  _vs=" + _vs + ",\n"+
			iPrefix + "  _u2=" + _u2 + ",\n"+
			iPrefix + "  _v2=" + _v2 + ",\n"+
			iPrefix + "  _us2=" + _us2 + ",\n"+
			iPrefix + "  _vs2=" + _vs2 + ",\n"+
			iPrefix + "  _rectangle=" + _rectangle + ",\n"+
			iPrefix + "  _animationFrame=" + _animationFrame + ",\n"+
			iPrefix + "  _animationType=" + _animationType + ",\n"+
			iPrefix + "  _animationDelta=" + _animationDelta + ",\n"+
			iPrefix + "  _rotation=" + _rotation + ",\n"+
			iPrefix + "  _color=" + _color + ",\n"+
			iPrefix + "  _sourceAlpha=" + _sourceAlpha + ",\n"+
			iPrefix + "  _destAlpha=" + _destAlpha + ",\n"+
			iPrefix + "  _multiTexturePreset=" + _multiTexturePreset + "\n"+
			iPrefix + "]";
	}
    
}
