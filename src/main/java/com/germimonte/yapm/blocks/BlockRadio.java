package com.germimonte.yapm.blocks;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.germimonte.yapm.CreativeTab;
import com.germimonte.yapm.entity.EntitySparks;
import com.germimonte.yapm.init.ModBlocks;
import com.germimonte.yapm.init.ModItems;
import com.germimonte.yapm.init.ModSounds;
import com.germimonte.yapm.util.Util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLever;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.MusicTicker.MusicType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRadio extends BlockBase {

	private final boolean isOn;

	public BlockRadio(String s, boolean isOn) {
		super(Material.CIRCUITS, s);
		this.isOn = isOn;
		this.lightValue = isOn ? 6 : 1;
	}

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	private static final AxisAlignedBB AABB_South = new AxisAlignedBB(.2, 0, 0, .8, .85, .3);
	private static final AxisAlignedBB AABB_North = new AxisAlignedBB(.2, 0, 1, .8, .85, .7);
	private static final AxisAlignedBB AABB_East = new AxisAlignedBB(0, 0, .2, .3, .85, .8);
	private static final AxisAlignedBB AABB_West = new AxisAlignedBB(1, 0, .2, .7, .85, .8);

	private static Collection<ItemRecord> discs = Collections.EMPTY_LIST;

	static {
		try {
			Field f = ItemRecord.class.getDeclaredField("RECORDS");
			f.setAccessible(true);
			discs = ((Map<SoundEvent, ItemRecord>) f.get(null)).values();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			if (isOn) {
				world.playEvent(1010, pos, 0);
				world.setBlockState(pos, ModBlocks.RADIO.getDefaultState().withProperty(FACING, state.getValue(FACING)),
						2);
			} else {
				world.playEvent(1010, pos, Item.getIdFromItem(Util.getRandomElem(discs)));
				world.setBlockState(pos,
						ModBlocks.RADIO_LIT.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
			}
			world.notifyNeighborsOfStateChange(pos, this, false);
			world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), this, false);
		}
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		switch (blockState.getValue(FACING)) {
		case SOUTH:
			return AABB_South;
		case EAST:
			return AABB_East;
		case NORTH:
			return AABB_North;
		case WEST:
			return AABB_West;
		default:
			return NULL_AABB;
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return getCollisionBoundingBox(state, source, pos);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isBlockNormalCube(IBlockState blockState) {
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		checkForDrop(worldIn, pos, state);
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		EnumFacing enumfacing = side.getOpposite();
		BlockPos blockpos = pos.offset(enumfacing);
		IBlockState iblockstate = world.getBlockState(blockpos);
		return !isExceptBlockForAttachWithPiston(iblockstate.getBlock()) && side.getAxis().isHorizontal()
				&& iblockstate.getBlockFaceShape(world, blockpos, side) == BlockFaceShape.SOLID
				&& !iblockstate.canProvidePower();
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
			if (this.canPlaceBlockOnSide(worldIn, pos, enumfacing)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		IBlockState iblockstate = this.getDefaultState();
		if (facing.getAxis().isHorizontal()) {
			iblockstate = iblockstate.withProperty(FACING, facing);
		}
		return iblockstate;
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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);
		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}
		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(FACING)).getIndex();
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return face.equals(state.getValue(FACING).getOpposite()) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(ModBlocks.RADIO);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(ModBlocks.RADIO);
	}

	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(ModBlocks.RADIO);
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return isOn ? 15 : 0;
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return isOn && blockState.getValue(FACING) == side ? 15 : 0;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (isOn) {
			worldIn.notifyNeighborsOfStateChange(pos, this, false);
			worldIn.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), this, false);
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	private boolean checkForDrop(World world, BlockPos pos, IBlockState state) {
		if (!this.canPlaceBlockOnSide(world, pos, state.getValue(FACING))) {
			this.dropBlockAsItem(world, pos, state, 0);
			world.setBlockToAir(pos);
			return false;
		} else {
			return true;
		}
	}
}
