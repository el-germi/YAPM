package com.germimonte.yapm;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SatManager extends WorldSavedData {

	private static final String DATA_NAME = "yapm:Satellite";
	private boolean isSatelliteOnline = false;

	public SatManager(String name) {
		super(name);
		isSatelliteOnline = false;
	}

	public SatManager() {
		super(DATA_NAME);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		isSatelliteOnline = nbt.getBoolean(DATA_NAME);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setBoolean(DATA_NAME, isSatelliteOnline);
		return nbt;
	}

	public static boolean isSatOnline(World world) {
		if (!Config.isSatelliteReqired)
			return true;
		MapStorage storage = world.getMapStorage();
		SatManager manager = (SatManager) storage.getOrLoadData(SatManager.class, DATA_NAME);
		if (manager == null) {
			manager = new SatManager();
			storage.setData(DATA_NAME, manager);
		}
		return manager.isSatelliteOnline;
	}

	public static void setSatOnline(World world, boolean status) {
		MapStorage storage = world.getMapStorage();
		SatManager manager = (SatManager) storage.getOrLoadData(SatManager.class, DATA_NAME);
		if (manager == null) {
			manager = new SatManager();
			storage.setData(DATA_NAME, manager);
		}
		manager.isSatelliteOnline = status;
		manager.setDirty(true);
	}
}
