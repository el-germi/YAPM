package com.germimonte.yapm;

import com.germimonte.yapm.init.ModSounds;
import com.germimonte.yapm.proxy.CommonProxy;
import com.germimonte.yapm.util.Util;

import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = YAPM.MOD_ID, name = YAPM.NAME, version = YAPM.VERSION, acceptedMinecraftVersions = YAPM.ACCEPTED_VERSIONS, dependencies = "required-after:computercraft")
public class YAPM {

	@Instance
	public static YAPM instance;

	@SidedProxy(clientSide = "com.germimonte.yapm.proxy.ClientProxy", serverSide = "com.germimonte.yapm.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static final String MOD_ID = "yapm";
	public static final String NAME = "YAPM";
	public static final String VERSION = "1.0";
	public static final String ACCEPTED_VERSIONS = "[1.12.2]";

	@EventHandler
	public static void PreInit(FMLPreInitializationEvent e) {
		ModSounds.registerSounds();
		OBJLoader.INSTANCE.addDomain(MOD_ID);
		Util.LOGGER = e.getModLog();
		proxy.registerPeripherals();
		proxy.registerEntities();
		proxy.registerTEs();
		proxy.registerSRs();
	}

	@EventHandler
	public static void init(FMLInitializationEvent e) {
		proxy.registerTRs();
	}

	@EventHandler
	public static void PostInit(FMLPostInitializationEvent e) {
		proxy.registerDBs();
		proxy.registerODs();
		proxy.registerLTs();
		proxy.registerPHs();
	}

	@EventHandler
	public static void startServer(FMLServerStartingEvent e) {
		if (Config.debug) {
			e.registerServerCommand(new CommandYAPM());
		}
		Util.refreshBiomes();
	}

	@Override
	public String toString() {
		return MOD_ID;
	}
}