package com.germimonte.yapm.items;

import com.germimonte.yapm.CreativeTab;
import com.germimonte.yapm.YAPM;
import com.germimonte.yapm.init.ModItems;
import com.germimonte.yapm.util.IHasModel;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel{

	public ItemBase(String s) {
		super.setUnlocalizedName(s);
		super.setRegistryName(s);
		super.setCreativeTab(CreativeTab.YAPM_TAB);

		ModItems.addItem(this);
	}

	@Override
	public void registerModels() {
		YAPM.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
