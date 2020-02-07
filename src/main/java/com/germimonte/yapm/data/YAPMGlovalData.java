package com.germimonte.yapm.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang3.SerializationUtils;

import com.germimonte.yapm.Config;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class YAPMGlovalData extends WorldSavedData {

	private static final String DATA_NAME = "yapmDataGloval";

	private HashMap<String, Integer> capable = new HashMap<String, Integer>();
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
		capable = SerializationUtils.deserialize(nbt.getByteArray("builds"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setBoolean("satellite", isSatelliteOnline);
		nbt.setByteArray("builds", SerializationUtils.serialize(capable));
		return nbt;
	}

	public static YAPMGlovalData syncGlobal(World world) {
		YAPMGlovalData out = (YAPMGlovalData) world.getMapStorage().getOrLoadData(YAPMGlovalData.class, DATA_NAME);
		if (out == null) {
			out = new YAPMGlovalData();
			world.getMapStorage().setData(DATA_NAME, out);
		}
		return out;
	}

	public static boolean isSatOnline(World world) {
		return !Config.isSatelliteReqired || syncGlobal(world).isSatelliteOnline;
	}

	public static void setSatOnline(World world, boolean status) {
		YAPMGlovalData manager = syncGlobal(world);
		manager.isSatelliteOnline = status;
		manager.setDirty(true);
	}

	public static int getPlayerBuilds(World world, String name) {
		return syncGlobal(world).capable.get(name);
	}

	public static void setBuildsHard(World world, String name, int builds) {
		YAPMGlovalData manager = syncGlobal(world);
		manager.capable.put(name, builds);
		manager.setDirty(true);
	}

	/** adds builds without removing any */
	public static void setBuildsSoft(World world, String name, int builds) {
		YAPMGlovalData manager = syncGlobal(world);
		manager.capable.put(name, builds | manager.capable.get(name));
		manager.setDirty(true);
	}
}
