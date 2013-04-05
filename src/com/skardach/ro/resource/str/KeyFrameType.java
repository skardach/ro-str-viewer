package com.skardach.ro.resource.str;

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
