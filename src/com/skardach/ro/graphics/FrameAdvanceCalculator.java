package com.skardach.ro.graphics;

/**
 * Interface for calculating next frame to render based on the given delay
 * since last invoke and/or last rendered frame
 * @author Stanislaw Kardach
 *
 */
public interface FrameAdvanceCalculator {
	/**
	 * Constant indicating undefined frame value.
	 */
	public static final int NO_FRAME = -1;
	/**
	 * Given the delay since last invoke and last rendered frame, calculate
	 * which frame should be rendered next
	 * @param iDelaySinceLastInvoke Time in ms from the last call
	 * @param iLastRenderedFrame Last rendered frame number.
	 * @return Number of the frame which should be rendered next.
	 */
	int calculateFrameToRender(long iDelaySinceLastInvoke,
			int iLastRenderedFrame);

}
