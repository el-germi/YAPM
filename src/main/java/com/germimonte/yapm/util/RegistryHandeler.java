package com.germimonte.yapm.util;

import com.germimonte.yapm.Config;
import com.germimonte.yapm.Structure;
import com.germimonte.yapm.init.ModBlocks;
import com.germimonte.yapm.init.ModItems;
import com.germimonte.yapm.renders.RenderRadioIcon;
import com.germimonte.yapm.renders.TileEntityConsoleRenderer;
import com.germimonte.yapm.util.ModelManager.ModelRegistrar;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryHandeler {

	public static int l = 0;
	public static int w = 0;
	public static double x = 0;
	public static double y = 0;
	public static double z = 0;
	public static double s = 0;

	@SubscribeEvent
	public static void onChat(net.minecraftforge.event.ServerChatEvent e) {
		if (Config.debug) {
			try {
				String[] cm = e.getMessage().split(" ");
				switch (cm[0]) {
				case "l":
					l = Integer.parseInt(cm[1]);
					w = TileEntityConsoleRenderer.a[l];
					x = TileEntityConsoleRenderer.ress[l][0];
					y = TileEntityConsoleRenderer.ress[l][1];
					z = TileEntityConsoleRenderer.ress[l][2];
					s = TileEntityConsoleRenderer.ress[l][3];
					break;
				case "w":
					w = Integer.parseInt(cm[1]);
					TileEntityConsoleRenderer.a[l] = w;
					break;
				case "x":
					x = Double.parseDouble(cm[1]);
					TileEntityConsoleRenderer.ress[l][0] = x;
					break;
				case "y":
					y = Double.parseDouble(cm[1]);
					TileEntityConsoleRenderer.ress[l][1] = y;
					break;
				case "z":
					z = Double.parseDouble(cm[1]);
					TileEntityConsoleRenderer.ress[l][2] = z;
					break;
				case "s":
					s = Double.parseDouble(cm[1]);
					TileEntityConsoleRenderer.ress[l][3] = s;
					break;
				default:
					throw new Throwable();
				}
				// Util.log(e.getMessage() + " done");
				e.setCanceled(true);
			} catch (Throwable t) {
				if (e.getMessage().equalsIgnoreCase("dump")) {
					Util.msgArchitect(e.getPlayer(),
							String.format("line: %d w:%d x:%f y:%f z:%f s:%f", l, w, x, y, z, s));
					e.setCanceled(true);
				}
			}
		}

	}

	private static final RenderRadioIcon rri = new RenderRadioIcon();

	@SubscribeEvent
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
		if (Minecraft.getMinecraft().player == null)
			return;
		if (PacketManager.message) {
			rri.renderIcon(event.getResolution());
		}
	}

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> e) {
		e.getRegistry().registerAll(ModItems.getItems());
	}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> e) {
		e.getRegistry().registerAll(ModBlocks.getBlocks());
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent e) {
		for (Item item : ModItems.getItems()) {
			if (item instanceof IHasModel) {
				((IHasModel) item).registerModels();
			}
		}
		for (Block block : ModBlocks.getBlocks()) {
			if (block instanceof IHasModel) {
				((IHasModel) block).registerModels();
			}
		}
	}

	@SubscribeEvent
	public static void onModelBakeEvent(ModelBakeEvent event) {
		for (ModelRegistrar modelRegistrar : ModelManager.modelRegistrars) {
			modelRegistrar.registerModels(event.getModelRegistry());
		}
	}

	@SubscribeEvent
	public static void onLootTablesLoaded(LootTableLoadEvent e) {
		if (e.getName().toString().equals("minecraft:chests/simple_dungeon")) {
			final LootPool pool = e.getTable().getPool("main");
			if (pool != null) {
				pool.addEntry(
						new LootEntryItem(ModItems.DISC, 10, 10, new LootFunction[0], new LootCondition[0], "yapm:cd"));
			}
		}
	}

	@SubscribeEvent
	public static void onChunckPopulate(PopulateChunkEvent e) {
		Util.generate(e);
	}
}