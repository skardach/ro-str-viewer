package com.skardach.ro.resource.str;

/**
 * Describes key frame type. Can be either basic frame or animation (morph)
 * frame.
 * @author Stanislaw Kardach
 *
 */
public enum KeyFrameType {
	BASIC,
	MORPH;
	
	static KeyFrameType fromInt(int iType)
	{
		if(iType == 1)
			return MORPH;
		else
			return BASIC;
	}
}
