package com.germimonte.yapm.blocks;

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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && !destroy(world, pos)) {
			return world.getBlockState(pos.down()).getBlock().onBlockActivated(world, pos.down(), state, player, hand,
					facing, hitX, hitY, hitZ);
		}
		return true;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		destroy(world, pos);
	}

	boolean destroy(World world, BlockPos pos) {
		if (!world.getBlockState(pos.down()).getBlock().equals(ModBlocks.CONSOLE)) {
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
