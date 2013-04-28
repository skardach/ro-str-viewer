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
	 * @param iPreloadTextures If true then renderer should pre-load all
	 * textures before first call to
	 * {@link Renderer#renderFrame(javax.media.opengl.GLAutoDrawable, long)}.
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
			float iZScale,
			int iFps,
			boolean iPreloadTextures) throws RenderException {
		assert(iEffect != null);
		return new SimpleStrRenderer(
				iEffect,
				new StepCalculator(),
				iPreloadTextures,
				iRenderPosition,
				iXRotation,
				iYRotation,
				iZRotation,
				iXScale,
				iYScale,
				iZScale);
	}
}
