package com.skardach.ro.graphics;
/**
 * Basic 3d point class.
 * @author Stanislaw Kardach
 *
 */
public class Point3D {
	public float _x;
	public float _y;
	public float _z;

	public Point3D(float iX, float iY, float iZ) {
		_x = iX;
		_y = iY;
		_z = iZ;
	}

	@Override
	public String toString() {
		return "(" + _x + "," + _y + "," + _z + ")";
	}
}