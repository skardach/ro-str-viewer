package com.skardach.ro.graphics;

import com.skardach.ro.graphics.SimpleStrRenderer;
import com.skardach.ro.graphics.Renderer;
import com.skardach.ro.resource.str.Str;
/**
 * Factory hiding the renderer implementation
 * @author Stanislaw Kardach
 *
 */
public class STRRendererFactory {

	/**
	 * Create the STR file renderer which will render the effect at given
	 * position, with given rotation and scale.
	 * @throws RenderException In case creating the renderer fails.
	 */
	public static Renderer createEffectRenderer(
			Str iEffect,
			Point3D iRenderPosition,
			float iXRotation,
			float iYRotation,
			float iZRotation,
			float iXScale,
			float iYScale,
			float iZScale) throws RenderException {
		return new SimpleStrRenderer(
				iEffect,
				iRenderPosition,
				iXRotation,
				iYRotation,
				iZRotation,
				iXScale,
				iYScale,
				iZScale);
	}
}
