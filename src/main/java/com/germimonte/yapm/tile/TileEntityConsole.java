package com.germimonte.yapm.tile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.germimonte.yapm.blocks.BlockConsole;
import com.germimonte.yapm.init.ModBlocks;
import com.germimonte.yapm.util.DummyICS;
import com.germimonte.yapm.util.IPeripheralBase;
import com.germimonte.yapm.util.SafeAL;
import com.germimonte.yapm.util.Util;
import com.google.gson.Gson;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class TileEntityConsole extends TileEntity implements IPeripheralBase {

	private final DummyICS dummy = new DummyICS(null, 0, world, pos);

	public SafeAL<ITextComponent> text = new SafeAL<ITextComponent>(7);
	public Style style = new Style();
	public boolean isOn = false;
	public int lines = 1;

	public TileEntityConsole(World world, BlockPos pos) {
		this(world);
		this.pos = pos;
	}

	public TileEntityConsole(World world) {
		this();
		this.world = world;
	}

	public TileEntityConsole() {
		super();
		this.blockType = ModBlocks.CONSOLE;
	}

	public EnumFacing getDir() {
		return world.getBlockState(pos).getValue(BlockConsole.FACING);
	}

	@Override
	protected void setWorldCreate(World world) {
		this.world = world;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.text = new SafeAL<ITextComponent>(7);
		this.isOn = nbt.getBoolean("isOn");
		this.lines = nbt.getInteger("lines");
		if (this.lines == 0) {
			this.lines = 1;
		}
		for (int i = 0; i < 7; ++i) {
			text.add(i, ITextComponent.Serializer.jsonToComponent(nbt.getString("Text" + i)));
		}
		this.style = text.safeGet(0, new TextComponentString("")).getStyle();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("isOn", isOn);
		nbt.setInteger("lines", this.lines);
		for (int i = 0; i < 7; ++i) {
			nbt.setString("Text" + i,
					ITextComponent.Serializer.componentToJson(text.safeGet(i, new TextComponentString(""))));
		}
		return nbt;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	public boolean runn(@Nonnull EntityPlayer player) {
		isOn = !isOn;
		world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, .3f, isOn ? .6f : .5f);
		fireEvent("console", isOn, player.getDisplayNameString());
		refresh();
		return true;
	}

	void refresh() {
		markDirty();
		if (world != null) {
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
			BlockPos p = pos.up();
			state = world.getBlockState(p);
			world.notifyBlockUpdate(p, state, state, 3);
			world.updateComparatorOutputLevel(p, getBlockType());
		}
	}

	@Nonnull
	private Style parse(@Nullable String c, @Nullable String f) {
		Style s = new Style();
		if (f != null) {
			try {
				s.setColor(TextFormatting.fromColorIndex(Integer.parseInt(c)));
			} catch (NumberFormatException e) {
				try {
					s.setColor(TextFormatting.fromColorIndex(Integer.parseInt(c, 16)));
				} catch (NumberFormatException g) {
					s.setColor(TextFormatting.BLACK);
				}
			}
		} else {
			s.setColor(TextFormatting.BLACK);
		}
		if (f != null) {
			f = f.toLowerCase();
			s.setStrikethrough(f.contains("m"));
			s.setObfuscated(f.contains("k"));
			s.setUnderlined(f.contains("n"));
			s.setItalic(f.contains("o"));
			s.setBold(f.contains("l"));
		}
		return s;
	}

	///////// Peripheral stuff//////////////////

	@Override
	public String getType() {
		return "Console";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "setText", "isOn", "setStyle", "setTextRaw", "power" };
	}

	@Override
	public Object[] exec(IComputerAccess pc, ILuaContext ctx, int method, Object[] args)
			throws LuaException, InterruptedException {
		switch (method) {
		case 0:
			lines = Math.min(7, args.length);
			text = new SafeAL<ITextComponent>(lines);
			for (int j = 0; j < lines; j++) {
				String s = args[j].toString();
				if (s != null && !"".equals(s.trim())) {
					text.add(new TextComponentString(s).setStyle(style));
				}
			}
			refresh();
			return EMPTY_RESPONSE;
		case 1:
			return Util.toArray(isOn);
		case 2:
			if (args.length == 2) {
				style = parse(args[0].toString(), args[1].toString());
				return EMPTY_RESPONSE;
			} else {
				throw new LuaException("Expexted 2 args, color and formating");
			}
		case 3:
			lines = Math.min(7, args.length);
			text = new SafeAL<ITextComponent>(lines);
			for (int j = 0; j < lines; j++) {
				try {
					text.add(TextComponentUtils.processComponent(dummy,
							ITextComponent.Serializer.jsonToComponent(args[j].toString()), null));
				} catch (Exception e) {
					throw new LuaException(e.getMessage() + " at line " + (j + 1));
				}
			}
			refresh();
			return EMPTY_RESPONSE;
		case 4:
			if (args.length < 1)
				throw new LuaException("Usage: power [on|off|toggle]");
			boolean old = isOn;
			switch (args[0].toString().toLowerCase()) {
			case "on":
			case "1":
				isOn = true;
				break;
			case "off":
			case "0":
				isOn = false;
				break;
			case "toggle":
				isOn = !isOn;
				break;
			default:
				throw new LuaException("Usage: power [on|off|toggle]");
			}
			if (old != isOn) {
				// fireEvent("console", isOn, ((Computer) pc).getLabel());
				refresh();
			}
			return EMPTY_RESPONSE;
		default:
			throw new LuaException("HOW DID WE GET HERE?, there is no method " + (method + 1));
		}
	}
}
