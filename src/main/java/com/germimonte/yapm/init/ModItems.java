package com.germimonte.yapm.init;

import net.minecraft.item.Item;

import com.germimonte.yapm.items.ItemBase;
import com.germimonte.yapm.items.ItemDisc;
import com.germimonte.yapm.items.ItemYapmRocket;

public class ModItems {
	private static final java.util.List<Item> ITEMS = new java.util.ArrayList<Item>();

	public static final ItemBase POSITIONER = new ItemBase("positioner");
	public static final ItemBase SATELITE = new ItemBase("satelite");
	public static final ItemYapmRocket ROCKET = new ItemYapmRocket("rocket");
	public static final ItemBase ENGINE = new ItemBase("engine");
	public static final ItemDisc DISC = new ItemDisc("music", ModSounds.MUSIC);
	public static final ItemBase BUILDER = null;// to be implemented

	public static void addItem(Item i) {
		ITEMS.add(i);
	}

	public static Item[] getItems() {
		return ITEMS.toArray(new Item[0]);
	}
}
