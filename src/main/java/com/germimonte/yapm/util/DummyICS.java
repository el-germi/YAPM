package com.germimonte.yapm.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class DummyICS implements ICommandSender {

	private World world = null;
	private int level = 0;
	private String name;
	private BlockPos pos = null;
	private Vec3d vector = null;

	public DummyICS(@Nullable String name, @Nonnull int level, @Nonnull World world, @Nullable BlockPos pos) {
		this.name = name != null ? name : "DummyICS";
		this.level = level;
		this.world = world != null ? world : DimensionManager.getWorld(0);
		this.pos = pos != null ? pos : BlockPos.ORIGIN;
		this.vector = new Vec3d(this.pos.getX(), this.pos.getY(), this.pos.getZ());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public BlockPos getPosition() {
		return pos;
	}

	@Override
	public Vec3d getPositionVector() {
		return vector;
	}

	@Override
	public boolean canUseCommand(int permLevel, String commandName) {
		return permLevel <= level;
	}

	@Override
	public World getEntityWorld() {
		return world;
	}

	@Override
	public MinecraftServer getServer() {
		return null;
	}

}
