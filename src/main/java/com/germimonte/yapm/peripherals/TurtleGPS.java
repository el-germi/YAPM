package com.germimonte.yapm.peripherals;

import javax.vecmath.Matrix4f;
import org.apache.commons.lang3.tuple.Pair;
import com.germimonte.yapm.YAPM;
import com.germimonte.yapm.init.ModItems;
import com.germimonte.yapm.tile.TileEntityGPS;
import com.germimonte.yapm.util.ModelUtil;
import com.germimonte.yapm.util.ModelManager;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;

public class TurtleGPS implements ITurtleUpgrade, ModelManager.ModelRegistrar {

	public TurtleGPS() {
		super();
		ModelManager.register(this);
	}

	@Override
	public ResourceLocation getUpgradeID() {
		return new ResourceLocation(YAPM.MOD_ID, "gpsystem");
	}

	@Override
	public int getLegacyUpgradeID() {
		return -1;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return YAPM.MOD_ID.toLowerCase() + ".turtle_upgrade.gpsystem";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(ModItems.POSITIONER);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new TileEntityGPS(turtle);
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, EnumFacing direction) {
		return null;
	}

	@Override
	public Pair<IBakedModel, Matrix4f> getModel(ITurtleAccess turtle, TurtleSide side) {
		return ModelUtil.getTurtleUpgradeModel("turtle_gps", side);
	}

	@Override
	public void registerModels(IRegistry<ModelResourceLocation, IBakedModel> registry) {
		ModelUtil.registerTurtleUpgradeModels(registry, "turtle_gps");
	}
}
