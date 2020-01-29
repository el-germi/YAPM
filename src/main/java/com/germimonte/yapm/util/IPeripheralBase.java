package com.germimonte.yapm.util;

import java.util.HashSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implement this on instead of {@code IPeripheral} to have a plethora of
 * utility methods
 */
public interface IPeripheralBase extends IPeripheral {

	@Nonnull
	final static Object[] EMPTY_RESPONSE = new Object[0];

	@Nonnull
	final HashSet<IComputerAccess> COMPUTERS = new HashSet<IComputerAccess>();

	@Override
	public default void attach(@Nonnull IComputerAccess computer) {
		COMPUTERS.add(computer);
	}

	@Override
	public default void detach(@Nonnull IComputerAccess computer) {
		COMPUTERS.remove(computer);
	}

	/**
	 * TODO change this for an actual check(idk)
	 */
	@Override
	public default boolean equals(@Nullable IPeripheral other) {
		return other != null && (this == other || other.getType().equals(this.getType()));
	}

	/**
	 * Sends an event to all attached computers
	 * 
	 * @see #IComputerAccess.queueEvent
	 */
	public default void fireEvent(@Nonnull String event, @Nullable Object... args) {
		for (IComputerAccess pc : COMPUTERS) {
			try {
				pc.queueEvent(event, args);
			} catch (RuntimeException e) {
				detach(pc);
			}
		}
	}

	/**
	 * Don't Override this method, use exec() instead
	 * 
	 * @see #exec
	 */
	@Deprecated
	@Nullable
	@Override
	default Object[] callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int method,
			@Nonnull Object[] arguments) throws LuaException, InterruptedException {
		attach(computer);// in case it's not attached, don't know if possible, but better safe than sorry
		return exec(computer, context, method, arguments);
	}

	/**
	 * Override this instead of callMethod for stability and feature reasons
	 */
	@Nullable
	public abstract Object[] exec(@Nonnull IComputerAccess pc, @Nonnull ILuaContext ictx, int method,
			@Nonnull Object[] args) throws LuaException, InterruptedException;

	class Provider implements IPeripheralProvider {

		@Override
		@Nullable
		public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
			TileEntity tile = world.getTileEntity(pos);
			return tile instanceof IPeripheralBase ? (IPeripheral) tile : null;
		}
	}
}