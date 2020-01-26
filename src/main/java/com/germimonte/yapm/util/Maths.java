package com.germimonte.yapm.util;

import java.awt.Point;

public class Maths {
	public static float x(float ang, float dist) {
		return (float) (dist * Math.cos(angle(ang)));
	}

	public static float y(float ang, float dist) {
		return (float) (dist * Math.sin(angle(ang)));
	}

	public static float angle(float ang) {
		return (float) (2 * Math.PI * ang);
	}
}
