package com.germimonte.yapm.util;

import java.util.HashSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implement this on any TileEntity instead of {@code IPeripheral} to have a
 * plethora of utility methods
 */
public interface IPeripheralBase extends IPeripheral {

	final static Object[] EMPTY_RESPONSE = new Object[0];

	final HashSet<IComputerAccess> COMPUTERS = new HashSet<IComputerAccess>();

	@Override
	public default void attach(IComputerAccess computer) {
		COMPUTERS.add(computer);
	}

	@Override
	public default void detach(IComputerAccess computer) {
		COMPUTERS.remove(computer);
	}

	@Override
	public default boolean equals(IPeripheral other) {// TODO change this for an actual check(idk)
		return other != null && (this == other || other.getType().equals(this.getType()));
	}

	public default void fireEvent(String event, Object... args) {
		for (IComputerAccess pc : COMPUTERS) {
			try {
				pc.queueEvent(event, args);
			} catch (RuntimeException e) {
				detach(pc);
			}
		}
	}

	class Provider implements IPeripheralProvider {

		@Override
		public IPeripheral getPeripheral(World world, BlockPos pos, EnumFacing side) {
			TileEntity tile = world.getTileEntity(pos);
			return tile instanceof IPeripheralBase ? (IPeripheral) tile : null;
		}
	}
}