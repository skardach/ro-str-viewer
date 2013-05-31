package com.skardach.ro.graphics;

import com.skardach.ro.graphics.SimpleStrRenderer;
import com.skardach.ro.graphics.Renderer;
import com.skardach.ro.graphics.debug.DebugSimpleStrRenderer;
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
	 * @param iEffect Effect to render
	 * @param iRenderPosition Position to render the effect on
	 * @param iXRotation Rotation on X axis to apply to the effect
	 * @param iYRotation Rotation on Y axis to apply to the effect
	 * @param iZRotation Rotation on Z axis to apply to the effect
	 * @param iXScale Scale on X axis to apply to the effect
	 * @param iYScale Scale on Y axis to apply to the effect
	 * @param iZScale Scale on Z axis to apply to the effect
	 * @param iFps Frames per second to use when rendering
	 * @param iPreloadTextures If true then renderer should pre-load all
	 * textures before first call to
	 * {@link Renderer#renderFrame(javax.media.opengl.GLAutoDrawable, long)}.
	 * @param iDebug If true, a debugging version of a renderer will be
	 * created
	 * @return Renderer implementation
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
			boolean iPreloadTextures,
			boolean iDebug) throws RenderException {
		assert(iEffect != null);
		FrameAdvanceCalculator calc =
				new StepCalculator();
				//new DelayBasedFPSFrameAdvanceCalculator(iFps);
		if(iDebug)
			return new DebugSimpleStrRenderer(
				iEffect,
				calc,
				iPreloadTextures,
				iRenderPosition,
				iXRotation,
				iYRotation,
				iZRotation,
				iXScale,
				iYScale,
				iZScale);
		else
			return new SimpleStrRenderer(
				iEffect,
				calc,
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
