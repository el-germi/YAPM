package com.germimonte.yapm;

import com.germimonte.yapm.init.ModItems;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CreativeTab {

	public static final CreativeTabs YAPM_TAB = new CreativeTabs(YAPM.MOD_ID.toLowerCase()) {

		@SideOnly(Side.CLIENT)
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.POSITIONER);
		}

		@SideOnly(Side.CLIENT)
		@Override
		public void displayAllRelevantItems(NonNullList<ItemStack> list) {
			super.displayAllRelevantItems(list);
		}
	};
}
