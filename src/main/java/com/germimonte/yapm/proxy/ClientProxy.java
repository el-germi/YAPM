package com.germimonte.yapm.proxy;

import org.apache.logging.log4j.Level;

import com.germimonte.yapm.YAPM;
import com.germimonte.yapm.entity.EntityRocket;
import com.germimonte.yapm.entity.EntitySparks;
import com.germimonte.yapm.init.ModItems;
import com.germimonte.yapm.init.ModPeripherals;
import com.germimonte.yapm.renders.RenderRocket;
import com.germimonte.yapm.renders.RenderSparks;
import com.germimonte.yapm.renders.TileEntityConsoleRenderer;
import com.germimonte.yapm.tile.TileEntityConsole;
import com.germimonte.yapm.util.ModelManager;

import dan200.computercraft.api.turtle.ITurtleUpgrade;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {

	@SideOnly(Side.CLIENT)
	@Override
	public void registerItemRenderer(Item item, int i, String id) {
		ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(item.getRegistryName(), id));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void textureAndModelInit() {
		for (ITurtleUpgrade upgrade : ModPeripherals.TURTLE_UPGRADES) {
			if (upgrade instanceof ModelManager.ModelRegistrar)
				ModelManager.register((ModelManager.ModelRegistrar) upgrade);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerRender() {
		RenderingRegistry.registerEntityRenderingHandler(EntityRocket.class, RenderRocket.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntitySparks.class, RenderSparks.FACTORY);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityConsole.class, new TileEntityConsoleRenderer());
	}
}
