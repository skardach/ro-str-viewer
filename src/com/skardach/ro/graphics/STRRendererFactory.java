package com.skardach.ro.graphics;

import com.skardach.ro.graphics.SimpleStrRenderer;
import com.skardach.ro.graphics.Renderer;
import com.skardach.ro.resource.str.Str;

public class STRRendererFactory {

	public static Renderer createRenderer(Str iEffect) {
		return new SimpleStrRenderer(iEffect);
	}
}
