package com.germimonte.yapm.blocks;

import java.lang.reflect.Field;
import java.util.Random;

import com.germimonte.yapm.YAPM;
import com.germimonte.yapm.entity.EntitySparks;
import com.germimonte.yapm.init.ModSounds;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.peripheral.diskdrive.TileDiskDrive;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

public class BlockBrokenPC extends BlockBase {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool PLAYING = PropertyBool.create("playing");

	public BlockBrokenPC(String s) {
		super(Material.IRON, s);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(PLAYING, false));
		setHarvestLevel("pickaxe", 0);
		setHardness(4);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			float x = pos.getX() + hitX + (facing.getFrontOffsetX() / 1000f);
			float y = pos.getY() + hitY + (facing.getFrontOffsetY() / 1000f);
			float z = pos.getZ() + hitZ + (facing.getFrontOffsetZ() / 1000f);
			if (world.rand.nextFloat() > .91) {
				world.spawnEntity(new EntitySparks(world, x, y, z));
				if (state.getValue(PLAYING)) {
					stop(world, pos);
					world.setBlockState(pos, state.withProperty(PLAYING, false));
				} else {
					for (EnumFacing f : EnumFacing.values()) {
						BlockPos p = pos.offset(f);
						if (play(world, p, pos)) {
							world.setBlockState(pos, state.withProperty(PLAYING, true));
							break;
						}
					}
				}
			}
		}
		return true;
	}

	private boolean play(World w, BlockPos p, BlockPos pos) {
		TileEntity t = w.getTileEntity(p);
		if (t != null && t instanceof TileDiskDrive) {
			Item i = ((IInventory) t).getStackInSlot(0).getItem();
			if (i != null && i instanceof ItemRecord) {
				w.playEvent(1010, pos, Item.getIdFromItem(i));
				return true;
			}
		}
		return false;
	}

	private void stop(World w, BlockPos p) {
		w.playEvent(1010, p, 0);
	}

	@Override
	public EnumFacing[] getValidRotations(World world, BlockPos pos) {
		return EnumFacing.HORIZONTALS;
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror) {
		return state.withRotation(mirror.toRotation((EnumFacing) state.getValue(FACING)));
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, PLAYING);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 4 * fh(state.getValue(FACING)) + (state.getValue(PLAYING) ? 1 : 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(PLAYING, (meta & 1) == 1).withProperty(FACING, fh(meta >> 2));
	}

	private EnumFacing fh(int meta) {
		switch (meta) {
		case 0:
		default:
			return EnumFacing.NORTH;
		case 1:
			return EnumFacing.SOUTH;
		case 2:
			return EnumFacing.EAST;
		case 3:
			return EnumFacing.WEST;
		}
	}

	private int fh(EnumFacing f) {
		switch (f) {
		case NORTH:
		default:
			return 0;
		case SOUTH:
			return 1;
		case EAST:
			return 2;
		case WEST:
			return 3;
		}
	}
}