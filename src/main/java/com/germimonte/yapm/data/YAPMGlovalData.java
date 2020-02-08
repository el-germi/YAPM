package com.germimonte.yapm.data;

import java.util.HashMap;

import com.germimonte.yapm.Config;
import com.germimonte.yapm.util.SerializationUtils;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class YAPMGlovalData extends WorldSavedData {

	private static final String DATA_NAME = "yapmDataGloval";

	private HashMap<String, Integer> builds = new HashMap<String, Integer>();
	private boolean isSatelliteOnline = false;

	private YAPMGlovalData(String name) {
		super(name);
	}

	public YAPMGlovalData() {
		this(DATA_NAME);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		isSatelliteOnline = nbt.getBoolean("satellite");
		builds = SerializationUtils.deserialize(nbt.getByteArray("builds"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setBoolean("satellite", isSatelliteOnline);
		nbt.setByteArray("builds", SerializationUtils.serialize(builds));
		return nbt;
	}

	public static YAPMGlovalData sync(World world) {
		YAPMGlovalData out = (YAPMGlovalData) world.getMapStorage().getOrLoadData(YAPMGlovalData.class, DATA_NAME);
		if (out == null) {
			out = new YAPMGlovalData();
			world.getMapStorage().setData(DATA_NAME, out);
		}
		return out;
	}

	public static boolean isSatOnline(World world) {
		return !Config.isSatelliteReqired || sync(world).isSatelliteOnline;
	}

	public static void setSatOnline(World world, boolean status) {
		YAPMGlovalData manager = sync(world);
		manager.isSatelliteOnline = status;
		manager.setDirty(true);
	}

	public static int getPlayerBuilds(World world, String name) {
		YAPMGlovalData manager = sync(world);
		Integer i = manager.builds.get(name);
		return i != null ? i : setBuildsHard(world, name, 0);
	}

	public static int setBuildsHard(World world, String name, int builds) {
		YAPMGlovalData manager = sync(world);
		manager.builds.put(name, builds);
		manager.setDirty(true);
		return builds;
	}

	/** adds builds without removing any */
	public static int setBuildsSoft(World world, String name, int builds) {
		YAPMGlovalData manager = sync(world);
		builds = builds | manager.builds.get(name);
		manager.builds.put(name, builds);
		manager.setDirty(true);
		return builds;
	}
}
