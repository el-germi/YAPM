package com.germimonte.yapm.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.util.math.BlockPos;

public class BlockPosHelp {

	public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);

	public static Iterable<BlockPos> square(int x, int y, int z, int x2, int z2) {
		List<BlockPos> l = new ArrayList<BlockPos>();
		for (int i = x; i <= x2; i++) {
			l.add(new BlockPos(i, y, z));
			l.add(new BlockPos(i, y, z2));
		}
		for (int i = z + 1; i <= z2 - 1; i++) {
			l.add(new BlockPos(x, y, i));
			l.add(new BlockPos(x2, y, i));
		}

		return new Iterable<BlockPos>() {
			@Override
			public Iterator<BlockPos> iterator() {
				return l.iterator();
			}
		};
	}

	public static Iterable<BlockPos> tube(int x, int y, int z, int x2, int y2, int z2) {
		List<BlockPos> l = new ArrayList<BlockPos>();
		for (int j = y; j <= y2; j++) {
			for (int i = x; i <= x2; i++) {
				l.add(new BlockPos(i, j, z));
				l.add(new BlockPos(i, j, z2));
			}
			for (int i = z + 1; i <= z2 - 1; i++) {
				l.add(new BlockPos(x, j, i));
				l.add(new BlockPos(x2, j, i));
			}
		}

		return new Iterable<BlockPos>() {
			@Override
			public Iterator<BlockPos> iterator() {
				return l.iterator();
			}
		};
	}

	public static Iterable<BlockPos> box(int x, int y, int z, int x2, int y2, int z2) {
		List<BlockPos> l = new ArrayList<BlockPos>();
		for (int j = y; j <= y2; j++) {
			for (int i = x; i <= x2; i++) {
				l.add(new BlockPos(i, j, z));
				l.add(new BlockPos(i, j, z2));
			}
			for (int i = z + 1; i <= z2 - 1; i++) {
				l.add(new BlockPos(x, j, i));
				l.add(new BlockPos(x2, j, i));
			}
		}
		for (int j = z + 1; j <= z2 - 1; j++)
			for (int i = x + 1; i <= x2 - 1; i++) {
				l.add(new BlockPos(i, y, j));
				l.add(new BlockPos(i, y2, j));
			}

		return new Iterable<BlockPos>() {
			@Override
			public Iterator<BlockPos> iterator() {
				return l.iterator();
			}
		};
	}
}
