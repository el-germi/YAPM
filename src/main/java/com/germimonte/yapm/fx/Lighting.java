package com.germimonte.yapm.fx;

import java.util.Random;
import org.lwjgl.util.vector.Vector3f;
import net.minecraft.client.renderer.BufferBuilder;

public class Lighting {
	private static final Random rand = new Random();

	private final boolean isOrigin;
	private Vector3f pos;
	private Lighting l1, l2;

	public int depth() {
		int a = l1 != null ? l1.depth() : 0;
		int b = l2 != null ? l2.depth() : 0;
		return Math.max(a, b) + 1;
	}

	public void render(BufferBuilder bufferbuilder, float scale) {
		if (l1 != null) {
			bufferbuilder.pos(pos.x * scale, pos.y * scale, pos.z * scale).endVertex();
			bufferbuilder.pos(l1.pos.x * scale, l1.pos.y * scale, l1.pos.z * scale).endVertex();
			l1.render(bufferbuilder, scale);
			if (l2 != null) {
				bufferbuilder.pos(pos.x * scale, pos.y * scale, pos.z * scale).endVertex();
				bufferbuilder.pos(l2.pos.x * scale, l2.pos.y * scale, l2.pos.z * scale).endVertex();
				l2.render(bufferbuilder, scale);
			}
		}
	}

	public Lighting(int levels) {
		isOrigin = true;
		pos = new Vector3f(0, 0, 0);
		set(levels);
	}

	private Lighting(Lighting o, float distance, int levels) {
		isOrigin = false;
		pos = close(o.pos, distance);
		set(levels);
	}

	private void set(final int levels) {
		if (levels > 0) {
			final float c = levels * levels / 200f;
			l1 = new Lighting(this, c, levels - 1);
			l2 = rand.nextFloat() > levels / 6f ? new Lighting(this, c, levels - 1) : null;
		}
	}

	public static Lighting close(Lighting o, float distance) {
		if (o != null) {
			if (!o.isOrigin)
				o.pos = close(o.pos, distance);
			o.l1 = close(o.l1, distance);
			o.l2 = close(o.l2, distance);
		}
		return o;
	}

	public static Vector3f close(Vector3f o, float distance) {
		return new Vector3f((float) (o.x + distance * rand.nextGaussian()),
				(float) (o.y + distance * rand.nextGaussian()), (float) (o.z + distance * rand.nextGaussian()));
	}
}