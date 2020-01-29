package com.germimonte.yapm.blocks;

import java.util.ArrayList;

import com.germimonte.yapm.init.ModBlocks;
import com.germimonte.yapm.tile.TileEntityConsole;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World w, BlockPos pos, IBlockState s, EntityPlayer p, EnumHand h, EnumFacing f,
			float x, float y, float z) {
		if (!w.isRemote && !destroy(w, pos)) {
			return w.getBlockState(pos.down()).getBlock().onBlockActivated(w, pos.down(), s, p, h, f, x, y, z);
		}
		return true;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		destroy(world, pos);
	}

	boolean destroy(World world, BlockPos pos) {
		if (!valid.contains(world.getBlockState(pos.down()).getBlock())) {
			world.setBlockToAir(pos);
			return true;
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		super.breakBlock(world, pos, state);
		world.destroyBlock(pos.down(), true);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
}
