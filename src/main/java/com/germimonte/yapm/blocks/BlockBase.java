package com.germimonte.yapm.blocks;

import com.germimonte.yapm.CreativeTab;
import com.germimonte.yapm.YAPM;
import com.germimonte.yapm.init.ModBlocks;
import com.germimonte.yapm.init.ModItems;
import com.germimonte.yapm.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel {

	public BlockBase(Material m, String s) {
		super(m);
		this.setUnlocalizedName(s);
		this.setRegistryName(s);
		this.setCreativeTab(CreativeTab.YAPM_TAB);

		ModBlocks.addBlock(this);
		ModItems.addItem(new ItemBlock(this).setRegistryName(this.getRegistryName().toString()));
	}

	@Override
	public void registerModels() {
		YAPM.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
