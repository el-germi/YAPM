package com.germimonte.yapm.blocks;

import java.util.ArrayList;

import com.germimonte.yapm.init.ModBlocks;
import com.germimonte.yapm.tile.TileEntityConsole;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGhost extends Block {

	public static final ArrayList<Block> valid = new ArrayList<Block>();
	public static final PropertyDirection DIR = PropertyDirection.create("facing");

	public BlockGhost(String s) {
		super(Material.ANVIL);
		this.setUnlocalizedName(s);
		this.setRegistryName(s);
		this.lightValue = 1;
		this.setHardness(2);

		ModBlocks.addBlock(this);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState bs, IBlockAccess world, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return world.getBlockState(pos.offset(state.getValue(DIR))).getComparatorInputOverride(world,
				pos.offset(state.getValue(DIR)));
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World w, BlockPos pos, IBlockState state, EntityPlayer p, EnumHand h, EnumFacing f,
			float x, float y, float z) {
		if (!w.isRemote && !destroy(w, pos, state)) {
			return w.getBlockState(pos.offset(state.getValue(DIR))).getBlock().onBlockActivated(w,
					pos.offset(state.getValue(DIR)), state, p, h, f, x, y, z);
		}
		return true;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		destroy(world, pos, state);
	}

	boolean destroy(World world, BlockPos pos, IBlockState state) {
		if (!valid.contains(world.getBlockState(pos.offset(state.getValue(DIR))).getBlock())) {
			world.setBlockToAir(pos);
			return true;
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		super.breakBlock(world, pos, state);
		world.destroyBlock(pos.offset(state.getValue(DIR)), true);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (player.capabilities.isCreativeMode) {
			world.setBlockToAir(pos.offset(state.getValue(DIR)));
		} else {
			super.onBlockHarvested(world, pos, state, player);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(DIR, EnumFacing.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(DIR).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DIR);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
}
