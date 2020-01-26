package com.germimonte.yapm;

import java.util.Random;

import javax.annotation.Nullable;

import com.germimonte.yapm.blocks.BlockBrokenPC;
import com.germimonte.yapm.init.ModBlocks;
import com.germimonte.yapm.util.BlockPosHelp;

import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntityBedRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableManager;

public class Structure {

	private static final IBlockState[] pcs = new IBlockState[] { ModBlocks.BROKEN_PC.getDefaultState(),
			ModBlocks.BROKEN_PC_ADV.getDefaultState(), ModBlocks.BROKEN_PC_MOS.getDefaultState(),
			ModBlocks.BROKEN_PC_ADV_MOS.getDefaultState() };

	private boolean mossy = false;
	private boolean ewal = true;
	private boolean drive = false;
	private int ang = 0;
	private Random rng = null;
	private World world = null;
	private BlockPos center = null;
	private Rotation rot = null;

	public static void generate(World world, BlockPos origin, @Nullable Random rand) {
		generate(world, origin.getX(), origin.getY(), origin.getZ(), rand);
	}

	public static void generate(World worl, int x, int y, int z, @Nullable Random rand) {
		new Structure().gen(worl, x, y, z, rand);
	}

	private void gen(World worl, int x, int y, int z, @Nullable Random rand) {
		if (rand == null)
			rand = new Random();
		// YAPM.log("Generating at " + x + " " + z);
		y--;
		world = worl;
		rng = rand;
		ang = rng.nextInt(4);
		rot = Rotation.values()[ang];
		ewal = ang % 2 == 0;
		int pc = rng.nextInt(4);
		mossy = pc > 1;
		center = new BlockPos(x, y, z);
		drive = rng.nextFloat() > .5;

		BlockPos min;
		BlockPos max;
		if (ewal) {
			min = new BlockPos(x - 5, y - 1, z - 4);
			max = new BlockPos(x + 5, y - 1, z + 4);
			BlockPos.getAllInBox(min, max).forEach(b -> {
				world.setBlockState(b, Blocks.DIRT.getDefaultState());
			});
			min = new BlockPos(x - 4, y, z - 3);
			max = new BlockPos(x + 4, y, z + 3);
			BlockPos.getAllInBox(min, max).forEach(b -> {
				world.setBlockState(b, Blocks.GRASS.getDefaultState());
			});
			log(x - 4, y, z - 3);
			log(x + 4, y, z - 3);
			log(x - 4, y, z + 3);
			log(x + 4, y, z + 3);
			min = new BlockPos(x - 3, y, z - 2);
			max = new BlockPos(x + 3, y, z + 2);
			BlockPos.getAllInBox(min, max).forEach(b -> {
				world.setBlockState(b, Blocks.DOUBLE_STONE_SLAB.getDefaultState());
			});
			BlockPosHelp.square(x - 3, y + 1, z - 2, x + 3, z + 2).forEach(b -> {
				wall(b.getX(), b.getY(), b.getZ());
			});
			BlockPos.getAllInBox(x - 2, y + 1, z - 1, x + 2, y + 4, z + 1).forEach(b -> {
				world.setBlockToAir(b);
			});
			world.setBlockState(new BlockPos(x + 1, y + 2, z - 2), Blocks.IRON_BARS.getDefaultState());
			world.setBlockState(new BlockPos(x - 1, y + 2, z + 2), Blocks.IRON_BARS.getDefaultState());
		} else {
			min = new BlockPos(x - 4, y - 1, z - 5);
			max = new BlockPos(x + 4, y - 1, z + 5);
			BlockPos.getAllInBox(min, max).forEach(b -> {
				world.setBlockState(b, Blocks.DIRT.getDefaultState());
			});
			min = new BlockPos(x - 3, y, z - 4);
			max = new BlockPos(x + 3, y, z + 4);
			BlockPos.getAllInBox(min, max).forEach(b -> {
				world.setBlockState(b, Blocks.GRASS.getDefaultState());
			});
			log(x - 3, y, z - 4);
			log(x + 3, y, z - 4);
			log(x - 3, y, z + 4);
			log(x + 3, y, z + 4);
			min = new BlockPos(x - 2, y, z - 3);
			max = new BlockPos(x + 2, y, z + 3);
			BlockPos.getAllInBox(min, max).forEach(b -> {
				world.setBlockState(b, Blocks.DOUBLE_STONE_SLAB.getDefaultState());
			});
			BlockPosHelp.square(x - 2, y + 1, z - 3, x + 2, z + 3).forEach(b -> {
				wall(b.getX(), b.getY(), b.getZ());
			});
			BlockPos.getAllInBox(x - 1, y + 1, z - 2, x + 1, y + 4, z + 2).forEach(b -> {
				world.setBlockToAir(b);
			});
			world.setBlockState(new BlockPos(x + 2, y + 2, z + 1), Blocks.IRON_BARS.getDefaultState());
			world.setBlockState(new BlockPos(x - 2, y + 2, z - 1), Blocks.IRON_BARS.getDefaultState());
		}
		world.setBlockState(rotate(new BlockPos(x + 1, y + 2, z + 1)),
				pcs[pc].withProperty(BlockBrokenPC.FACING, EnumFacing.getHorizontal(ang + 2)));
		if (drive)
			world.setBlockState(rotate(new BlockPos(x + 2, y + 2, z + 1)),
					dan200.computercraft.ComputerCraft.Blocks.peripheral.getDefaultState().withProperty(
							dan200.computercraft.ComputerCraft.Blocks.peripheral.FACING,
							EnumFacing.getHorizontal(ang + 2)));
		world.setBlockState(rotate(new BlockPos(x + 1, y + 1, z + 1)),
				((BlockWoodSlab) Blocks.WOODEN_SLAB).getStateFromMeta(8));
		world.setBlockState(rotate(new BlockPos(x - 1, y + 1, z + 1)),
				Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.getHorizontal(ang + 2)));
		world.setBlockState(rotate(new BlockPos(x, y + 1, z + 1)),
				((BlockStairs) Blocks.OAK_STAIRS).getDefaultState()
						.withProperty(BlockStairs.FACING, EnumFacing.getHorizontal(ang + 1))
						.withProperty(((BlockStairs) Blocks.OAK_STAIRS).HALF, BlockStairs.EnumHalf.TOP));
		world.setBlockState(rotate(new BlockPos(x + 2, y + 1, z + 1)),
				((BlockStairs) Blocks.OAK_STAIRS).getDefaultState()
						.withProperty(BlockStairs.FACING, EnumFacing.getHorizontal(ang + 3))
						.withProperty(((BlockStairs) Blocks.OAK_STAIRS).HALF, BlockStairs.EnumHalf.TOP));
		world.setBlockState(rotate(new BlockPos(x + 1, y + 1, z - 1)),
				((BlockStairs) Blocks.OAK_STAIRS).getDefaultState()
						.withProperty(((BlockStairs) Blocks.OAK_STAIRS).FACING, EnumFacing.getHorizontal(ang + 2)));
		world.setBlockState(rotate(new BlockPos(x + 2, y + 1, z - 1)), Blocks.WALL_SIGN.getDefaultState()
				.withProperty(BlockWallSign.FACING, EnumFacing.getHorizontal(ang + 3)));
		world.setBlockState(rotate(new BlockPos(x, y + 1, z - 1)), Blocks.WALL_SIGN.getDefaultState()
				.withProperty(BlockWallSign.FACING, EnumFacing.getHorizontal(ang + 1)));
		world.setBlockState(rotate(new BlockPos(x - 2, y + 1, z + 1)),
				Blocks.BED.getDefaultState().withProperty(BlockBed.FACING, EnumFacing.getHorizontal(ang))
						.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD));
		world.setBlockState(rotate(new BlockPos(x - 2, y + 1, z)),
				Blocks.BED.getDefaultState().withProperty(BlockBed.FACING, EnumFacing.getHorizontal(ang)));
		world.setBlockState(rotate(new BlockPos(x - 1, y + 1, z - 2)),
				Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.getHorizontal(ang)));
		world.setBlockState(rotate(new BlockPos(x - 1, y + 2, z - 2)),
				Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.getHorizontal(ang))
						.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER));
		try {
			((TileEntityLockableLoot) world.getTileEntity(rotate(new BlockPos(x - 1, y + 1, z + 1))))
					.setLootTable(new ResourceLocation(YAPM.MOD_ID, "ruins"), 0);
			if (drive)
				world.getLootTableManager().getLootTableFromLocation(new ResourceLocation(YAPM.MOD_ID, "drive"))
						.fillInventory((IInventory) world.getTileEntity(rotate(new BlockPos(x + 2, y + 2, z + 1))), rng,
								new LootContext(1, (WorldServer) world, world.getLootTableManager(), null, null, null));
			TileEntityBed tile;
			NBTTagCompound t;
			int c = rand.nextInt(16);
			NBTTagCompound tag = JsonToNBT.getTagFromJson("{color:" + c + "}");
			tile = (TileEntityBed) world.getTileEntity(rotate(new BlockPos(x - 2, y + 1, z)));
			t = tile.writeToNBT(new NBTTagCompound());
			t.merge(tag);
			tile.readFromNBT(t);
			tile.markDirty();
			tile = (TileEntityBed) world.getTileEntity(rotate(new BlockPos(x - 2, y + 1, z + 1)));
			t = tile.writeToNBT(new NBTTagCompound());
			t.merge(tag);
			tile.readFromNBT(t);
			tile.markDirty();
		} catch (NBTException e) {
			e.printStackTrace();
		}
	}

	private BlockPos rotate(BlockPos block) {
		BlockPos abs = block.add(-center.getX(), -center.getY(), -center.getZ());
		abs = abs.rotate(rot);
		return abs.add(center.getX(), center.getY(), center.getZ());
	}

	private void log(int x, int y, int z) {
		BlockPos.getAllInBox(new BlockPos(x, y, z), new BlockPos(x, y + 4, z)).forEach(b -> {
			world.setBlockState(b, rng.nextBoolean() ? ((BlockNewLog) Blocks.LOG2).getStateFromMeta(1)
					: ((BlockOldLog) Blocks.LOG).getStateFromMeta(1));
		});
	}

	private void wall(int x, int y, int z) {
		world.setBlockState(new BlockPos(x, y, z), getBrick());
		float f = rng.nextFloat();
		if (f > .15) {
			world.setBlockState(new BlockPos(x, y + 1, z), getBrick());
			if (f > .25) {
				world.setBlockState(new BlockPos(x, y + 2, z), getBrick());
				if (f > .8) {
					world.setBlockState(new BlockPos(x, y + 3, z), getBrick());
				}
			}
		}
	}

	private IBlockState getBrick() {
		float f = rng.nextFloat();
		if (mossy) {
			if (f > .5) {
				return ((BlockStoneBrick) Blocks.STONEBRICK)
						.getStateFromMeta(((BlockStoneBrick) Blocks.STONEBRICK).MOSSY_META);
			} else if (f > .2) {
				return ((BlockStoneBrick) Blocks.STONEBRICK)
						.getStateFromMeta(((BlockStoneBrick) Blocks.STONEBRICK).CRACKED_META);
			} else {
				return ((BlockStoneBrick) Blocks.STONEBRICK)
						.getStateFromMeta(((BlockStoneBrick) Blocks.STONEBRICK).DEFAULT_META);
			}
		} else {
			if (f > .5) {
				return ((BlockStoneBrick) Blocks.STONEBRICK)
						.getStateFromMeta(((BlockStoneBrick) Blocks.STONEBRICK).CRACKED_META);
			} else if (f > .2) {
				return ((BlockStoneBrick) Blocks.STONEBRICK)
						.getStateFromMeta(((BlockStoneBrick) Blocks.STONEBRICK).DEFAULT_META);
			} else {
				return ((BlockStoneBrick) Blocks.STONEBRICK)
						.getStateFromMeta(((BlockStoneBrick) Blocks.STONEBRICK).MOSSY_META);
			}
		}
	}
}
