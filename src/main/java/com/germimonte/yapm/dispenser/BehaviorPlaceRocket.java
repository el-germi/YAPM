package com.germimonte.yapm.dispenser;

import com.germimonte.yapm.items.ItemYapmRocket;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class BehaviorPlaceRocket implements IBehaviorDispenseItem {

	public BehaviorPlaceRocket() {
		super();
	}

	/*
	 * @Override public ItemStack dispense(IBlockSource source, ItemStack stack) {
	 * IPosition p = BlockDispenser.getDispensePosition(source); BlockPos pos = new
	 * BlockPos(p.getX(),p.getY(),p.getZ());
	 * source.getWorld().setBlockState(pos,Block.getBlockFromItem(stack.getItem()).
	 * getDefaultState()); stack.shrink(1); return stack; }
	 */

	@Override
	public ItemStack dispense(IBlockSource source, ItemStack stack) {
		BlockPos pos = source.getBlockPos();
		EnumFacing f = (EnumFacing) (source.getBlockState().getProperties().get(BlockDispenser.FACING));
		switch (f) {
		default:
			pos = pos.offset(f);
		case UP:
			pos = pos.offset(f);
			((ItemYapmRocket) stack.getItem()).use(source.getWorld(), stack, pos, null);
		}
		return stack;
	}
}