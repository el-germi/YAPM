package com.germimonte.yapm.proxy;

import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import com.germimonte.yapm.YAPM;
import com.germimonte.yapm.dispenser.BehaviorPlaceRocket;
import com.germimonte.yapm.entity.EntityRocket;
import com.germimonte.yapm.entity.EntitySparks;
import com.germimonte.yapm.init.ModItems;
import com.germimonte.yapm.init.ModPeripherals;
import com.germimonte.yapm.tile.TileEntityConsole;
import com.germimonte.yapm.tile.TileEntityGPS;
import com.germimonte.yapm.util.FunctionRandomize;
import com.germimonte.yapm.util.PacketManager;
import com.germimonte.yapm.util.PacketManager.MessageHandeler;
import com.germimonte.yapm.util.PacketManager.MyMessage;

public class CommonProxy {

	public void registerTileEntities() {
		registerTileEntity(TileEntityGPS.class);
		registerTileEntity(TileEntityConsole.class);
	}

	private void registerTileEntity(Class<? extends TileEntity> tileEntity) {
		GameRegistry.registerTileEntity(tileEntity, new ResourceLocation(YAPM.MOD_ID, tileEntity.getSimpleName()));
	}

	public void registerItemRenderer(Item item, int i, String string) {
		// NOOP
	}

	public void textureAndModelInit() {
		// NOOP
	}

	public void registerRender() {
		// NOOP
	}

	public void regEntits() {
		EntityRegistry.registerModEntity(new ResourceLocation(YAPM.MOD_ID, "rocket"), EntityRocket.class, "rocket", 1,
				YAPM.instance, 69, 2, true);
		EntityRegistry.registerModEntity(new ResourceLocation(YAPM.MOD_ID, "spark"), EntitySparks.class, "spark", 2,
				YAPM.instance, 69, 5, false);
	}

	public void regPeripheralsInt() {
		ModPeripherals.registerInternally();
		ModPeripherals.registerWithComputerCraft();
	}

	public void registerBehaviors() {
		// BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BOWL, new
		// BehaviorFireRocket());
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.ROCKET, new BehaviorPlaceRocket());
	}

	public void registerOreDict() {
		OreDictionary.registerOre("record", ModItems.DISC);
	}

	public void registerLTs() {
		LootFunctionManager.registerFunction(new FunctionRandomize.Serializer());
		LootTableList.register(new ResourceLocation(YAPM.MOD_ID, "ruins"));
		LootTableList.register(new ResourceLocation(YAPM.MOD_ID, "drive"));
	}

	public void registerPH() {
		PacketManager.INSTANCE.registerMessage(MessageHandeler.class, MyMessage.class, 0, Side.CLIENT);
		PacketManager.INSTANCE.registerMessage(MessageHandeler.class, MyMessage.class, 1, Side.SERVER);
	}
}