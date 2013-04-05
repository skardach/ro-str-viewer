package com.skardach.ro.resource.str;

/**
 * This should be refactored to an interface which provides texture manipulation
 * method and a parametrized factory for creation of objects implementing it,
 * one for each possible value.
 * @author Stanislaw Kardach
 *
 */
public enum MultiTextureMode {
    /// Unknown
	UNKNOWN,
	/// - 0 - assume none?
	NO_CHANGE,
    /// - 1 - modulate texture <pre>
    ///   color = texture.color * color
    ///   alpha = texture.alpha * alpha</pre>
	MODULATE,
    /// - 2 - same as 1
	MODULATEV2,
    /// - 3 - add texture color, replace alpha <pre>
    ///   color = texture.color + color
    ///   alpha = texture.alpha</pre>
	ADD_COLOR_AND_ALPHA,
    /// - 4 - blend texture color according to texture alpha, keep alpha <pre>
    ///   color = texture.color * texture.alpha + color * (1 - texture.alpha)</pre>
	BLEND_COLOR_USING_ALPHA,
    /// - 5 - modulate texture colors, replace alpha <pre>
    ///   color0 = color0 = texture0.color * color
    ///   color = texture1.color * color0
    ///   alpha = texture1.alpha</pre>
	MODULATE_COLORS_AND_REPLACE_ALPHA,
    /// - 6 - modulate inverse texture colors, replace alpha <pre>
    ///   color0 = (1 - texture0.color) * color
    ///   color = (1 - texture1.color) * color0
    ///   alpha = texture1.alpha</pre>
	MODULATE_INVERSE_COLORS_AND_REPLACE_ALPHA,
    /// - 7 - replace color with modulate of texture alpha and texture color, replace alpha <pre>
    ///   color0 = texture0.color
    ///   color = texture1.alpha * color0
    ///   alpha = texture1.alpha</pre>
	REPLACE_COLOR_WITH_COLOR_AND_ALPHA_MODULATION,
    /// - 8 - add texture color with modulate of texture color, replace alpha (same as 11?) <pre>
    ///   color0 = texture0.color * color
    ///   (alpha0 = texture0.alpha)
    ///   color = texture1.color + color0
    ///   alpha = texture1.alpha</pre>
	ADD_COLOR_WITH_COLOR_AND_ALPHA_MODULATION,
    /// - 9 - blend texture color with modulate of texture color according to texture alpha, replace alpha <pre>
    ///   color0 = texture0.color * color
    ///   color = texture1.color * texture1.alpha + color0 * (1 - texture1.alpha)
    ///   alpha = texture1.alpha</pre>
	BLEND_COLOR_WITH_ALPHA_BASED_TEXTURE_COLOR_MODULATION,
    /// - 10 - blend texture color with modulate of color according to alpha, replace alpha <pre>
    ///   color0 = texture0.color * color
    ///   color = texture1.color * alpha + color0 * (1 - alpha)
    ///   alpha = texture1.alpha</pre>
	BLEND_COLOR_WITH_ALPHA_BASED_COLOR_MODULATION,
    /// - 11 - add texture color with modulate of texture color, replace alpha (same as 8?) <pre>
    ///   color0 = texture0.color * color
    ///   (alpha0 = alpha)
    ///   color = texture1.color + color0
    ///   alpha = texture1.alpha</pre>
	ADD_TEXTURE_COLOR_WITH_TEXTURE_COLOR_MODULATION,
    /// - 12 - add inverse texture color with modulate of texture color, replace alpha <pre>
    ///   color0 = texture0.color * color
    ///   color = (1 - texture1.color) + color0
    ///   alpha = texture1.alpha</pre>
	ADD_INVERSE_TEXTURE_COLOR_WITH_TEXTURE_COLOR_MODULATION,
    /// - 13 - modulate texture color with add of texture color, replace alpha <pre>
    ///   color0 = texture0.color + color
    ///   color = texture1.color * color0
    ///   alpha = texture1.alpha</pre>
	MODULATE_TEXTURE_COLOR_WITH_TEXTURE_COLOR,
    /// - 14 - bright(x2) modulate of texture colors, replace alpha <pre>
    ///   color0 = texture0.color * color
    ///   color = texture1.color * color0 * 2
    ///   alpha = texture1.alpha</pre>
	BRIGHTEN_TEXTURE_COLOR,
    /// - 15 - signed add texture color with modulate of texture color, replace alpha <pre>
    ///   color0 = texture0.color * color
    ///   color = texture1.color + color0 - 0.5
    ///   alpha = texture1.alpha</pre>
	ADD_TEXTURE_COLOR_WITH_SIGN_WITH_COLOR_MODULATION;
	
	static MultiTextureMode fromInt(int iMode)
	{
		switch(iMode) {
		case 0: return NO_CHANGE;
		case 1: return MODULATE;
		case 2: return MODULATEV2;
		case 3: return ADD_COLOR_AND_ALPHA;
		case 4: return BLEND_COLOR_USING_ALPHA;
		case 5: return MODULATE_COLORS_AND_REPLACE_ALPHA;
		case 6: return MODULATE_INVERSE_COLORS_AND_REPLACE_ALPHA;
		case 7: return REPLACE_COLOR_WITH_COLOR_AND_ALPHA_MODULATION;
		case 8: return ADD_COLOR_WITH_COLOR_AND_ALPHA_MODULATION;
		case 9: return BLEND_COLOR_WITH_ALPHA_BASED_TEXTURE_COLOR_MODULATION;
		case 10: return BLEND_COLOR_WITH_ALPHA_BASED_COLOR_MODULATION;
		case 11: return ADD_TEXTURE_COLOR_WITH_TEXTURE_COLOR_MODULATION;
		case 12: return ADD_INVERSE_TEXTURE_COLOR_WITH_TEXTURE_COLOR_MODULATION;
		case 13: return MODULATE_TEXTURE_COLOR_WITH_TEXTURE_COLOR;
		case 14: return BRIGHTEN_TEXTURE_COLOR;
		case 15: return ADD_TEXTURE_COLOR_WITH_SIGN_WITH_COLOR_MODULATION;
		default:
			System.err.println("Unknown MultiTextureMode: " + iMode);
			return UNKNOWN;
		}
	}
}