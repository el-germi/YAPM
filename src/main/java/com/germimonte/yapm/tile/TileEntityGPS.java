package com.germimonte.yapm.tile;

import com.germimonte.yapm.data.YAPMGlovalData;
import com.germimonte.yapm.util.IPeripheralBase;
import com.germimonte.yapm.util.Util;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.turtle.ITurtleAccess;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityGPS extends TileEntity implements IPeripheralBase {

	private static final NumberFormat form = new DecimalFormat("#.##");
	private final ITurtleAccess turtle;
	private final IPocketAccess phone;
	private final boolean isTurtle;

	public TileEntityGPS(ITurtleAccess turtle) {
		this.turtle = turtle;
		this.phone = null;
		this.isTurtle = true;
		this.setWorld(turtle.getWorld());
	}

	public TileEntityGPS(IPocketAccess access) {
		this.turtle = null;
		this.phone = access;
		this.isTurtle = false;
		this.setWorld(access.getValidEntity().world);
	}

	@Override
	public String getType() {
		return "GPSystem";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "getPosition", "getAbsolutePosition", "getOrientation", "isSatelliteOnline",
				"getSeaLevel" };
	}

	@Override
	public Object[] exec(IComputerAccess pc, ILuaContext con, int meth, Object[] args)
			throws LuaException, InterruptedException {
		boolean satLink = YAPMGlovalData.isSatOnline(this.getWorld());
		switch (meth) {
		case 0:
			if (satLink) {
				if (isTurtle) {
					return Util.toArray(turtle.getPosition().getX(),
							turtle.getPosition().getY() - turtle.getWorld().getSeaLevel(), turtle.getPosition().getZ());
				} else {
					Entity p = phone.getValidEntity();
					return Util.toArray(form.format(p.posX),
							form.format(p.posY - p.world.getSeaLevel() + p.getEyeHeight()), form.format(p.posZ));
				}
			} else {
				throw new LuaException("GPS Network Offline");
			}
		case 1:
			if (satLink) {
				if (isTurtle) {
					return Util.toArray(turtle.getPosition().getX(), turtle.getPosition().getY(),
							turtle.getPosition().getZ());
				} else {
					Entity p = phone.getValidEntity();
					return Util.toArray(form.format(p.posX), form.format(p.posY), form.format(p.posZ));
				}
			} else {
				throw new LuaException("GPS Network Offline");
			}
		case 2:
			if (isTurtle) {
				return Util.toArray(turtle.getDirection().name());
			} else {
				return Util.toArray(EnumFacing.fromAngle(phone.getValidEntity().rotationYaw).name());
			}
		case 3:
			return Util.toArray(satLink);
		case 4:
			if (isTurtle) {
				return Util.toArray(turtle.getWorld().getSeaLevel());
			} else {
				return Util.toArray(phone.getValidEntity().world.getSeaLevel());
			}
		default:
			throw new LuaException("HOW DID WE GET HERE?, there is no method " + (meth + 1));
		}

	}

	@Override
	public boolean equals(IPeripheral other) {// TODO change this for an actual check(idk)
		return this == other || other.getType().equals(this.getType());
	}
}
