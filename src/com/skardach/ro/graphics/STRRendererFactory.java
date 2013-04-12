package com.skardach.ro.graphics;

import com.skardach.ro.graphics.SimpleStrRenderer;
import com.skardach.ro.graphics.Renderer;
import com.skardach.ro.resource.str.Str;

public class STRRendererFactory {

	public static Renderer createRenderer(
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
