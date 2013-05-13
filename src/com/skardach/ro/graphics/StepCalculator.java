package com.skardach.ro.graphics;
/**
 * This implementation returns next frame number (iLastRenderedFrame+1)
 * @author Stanislaw Kardach
 *
 */
public class StepCalculator implements FrameAdvanceCalculator {

	@Override
	public int calculateFrameToRender(long iDelaySinceLastInvoke,
			int iLastRenderedFrame) {
		return iLastRenderedFrame + (iDelaySinceLastInvoke > 0 ? 1 : 0);
	}

}
