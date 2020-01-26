package com.germimonte.yapm.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DummyICS implements ICommandSender {

	private World world = null;
	private MinecraftServer server = null;
	private int level = 0;
	private String name;
	private BlockPos pos = null;
	private Vec3d vector = null;

	public DummyICS(@Nullable String name, @Nonnull int level, @Nonnull World world, @Nullable MinecraftServer server,
			@Nullable BlockPos pos) {
		this.name = name != null ? name : "dummyICS";
		this.level = level;
		this.world = world;
		this.server = server;
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
		return server;
	}

}
