package com.germimonte.yapm.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

/** Unused for the foreseeable future */
public class YAPMWorldData extends WorldSavedData {

	private static final String DATA_NAME = "yapmDataWorld";

	private YAPMWorldData(String name) {
		super(name);
	}

	public YAPMWorldData() {
		this(DATA_NAME);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		return nbt;
	}

	public static YAPMWorldData sync(World world) {
		YAPMWorldData out = (YAPMWorldData) world.getPerWorldStorage().getOrLoadData(YAPMWorldData.class, DATA_NAME);
		if (out == null) {
			out = new YAPMWorldData();
			world.getMapStorage().setData(DATA_NAME, out);
		}
		return out;
	}
}
