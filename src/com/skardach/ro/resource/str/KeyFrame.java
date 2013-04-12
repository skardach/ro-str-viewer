package com.skardach.ro.resource.str;
import com.skardach.ro.graphics.Color;
import com.skardach.ro.graphics.Point2D;
import com.skardach.ro.graphics.Rectangle;

/**
 * Describes Key frame of an effect animation
 * @author Stanislaw Kardach
 */
public class KeyFrame {
	int _framenum;
	KeyFrameType _frameType; // subclass maybe? Take a look at roengine processing 
	Point2D _position;
	/**
	 * Texture mapping.
	 * Vertices go like this:
	 * a--b
	 * |  |
	 * c--d
	 */
	Rectangle<Point2D> _textureUVMapping;
	/**
	 * Second texture mapping. Not sure yet what's it for...
	 * Vertices go like this:
	 * a--b
	 * |  |
	 * c--d
	 */
	Rectangle<Point2D> _textureUVMapping2;
	/**
	 * Drawing rectangle for OpenGL.
	 * Vertices go like this:
	 * d--c
	 * |  |
	 * a--b
	 */
	Rectangle<Point2D> _drawingRectangle;
	float _textureId;
    AnimationType _animationType;
    float _animationDelta; ///< Texture animation delta.
    float _rotation; //< Rotation [0,1024[ is equivalent to [0,360[ degrees
    Color _color;
    int _sourceBlend; //< Source blend mode
    int _destBlend; //< Destination blend mode
    MultiTextureMode _multiTexturePreset;
    
	public final int get_framenum() {
		return _framenum;
	}
	public final KeyFrameType get_frameType() {
		return _frameType;
	}
	public final Point2D get_position() {
		return _position;
	}
	public final Rectangle<Point2D> get_textureUVMapping() {
		return _textureUVMapping;
	}
	public final Rectangle<Point2D> get_textureUVMapping2() {
		return _textureUVMapping2;
	}
	public final Rectangle<Point2D> get_drawingRectangle() {
		return _drawingRectangle;
	}
	public final float get_textureId() {
		return _textureId;
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
	public final int get_sourceBlend() {
		return _sourceBlend;
	}
	public final int get_destBlend() {
		return _destBlend;
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
	public final void set_position(Point2D _position) {
		this._position = _position;
	}
	public final void set_textureUVMapping(Rectangle<Point2D> iMapping) {
		assert(iMapping != null);
		this._textureUVMapping = iMapping;
	}
	public final void set_textureUVMapping2(Rectangle<Point2D> iMapping) {
		assert(iMapping != null);
		this._textureUVMapping2 = iMapping;
	}
	public final void set_drawingRectangle(Rectangle<Point2D> _rectangle) {
		this._drawingRectangle = _rectangle;
	}
	public final void set_textureId(float _textureId) {
		this._textureId = _textureId;
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
		this._sourceBlend = _sourceAlpha;
	}
	public final void set_destAlpha(int _destAlpha) {
		this._destBlend = _destAlpha;
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
			iPrefix + "  _textureUVMapping=" + _textureUVMapping + ",\n"+
			iPrefix + "  _textureUVMapping2=" + _textureUVMapping2 + ",\n"+
			iPrefix + "  _drawingRectangle=" + _drawingRectangle + ",\n"+
			iPrefix + "  _textureId=" + _textureId + ",\n"+
			iPrefix + "  _animationType=" + _animationType + ",\n"+
			iPrefix + "  _animationDelta=" + _animationDelta + ",\n"+
			iPrefix + "  _rotation=" + _rotation + ",\n"+
			iPrefix + "  _color=" + _color + ",\n"+
			iPrefix + "  _sourceBlend=" + _sourceBlend + ",\n"+
			iPrefix + "  _destBlend=" + _destBlend + ",\n"+
			iPrefix + "  _multiTexturePreset=" + _multiTexturePreset + "\n"+
			iPrefix + "]";
	}
    
}
