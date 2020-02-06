package com.germimonte.yapm.items;

import com.germimonte.yapm.CreativeTab;
import com.germimonte.yapm.YAPM;
import com.germimonte.yapm.init.ModItems;
import com.germimonte.yapm.util.IHasModel;

import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;

public class ItemDisc extends ItemRecord implements IHasModel {

	public ItemDisc(String s, SoundEvent sound) {
		super(s, sound);
		super.setCreativeTab(CreativeTab.YAPM_TAB);
		super.setUnlocalizedName(s);
		super.setRegistryName(s);
		ModItems.addItem(this);
	}

	@Override
	public void registerModels() {
		YAPM.proxy.registerIR(this, 0, "inventory");
	}
}