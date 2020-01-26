package com.germimonte.yapm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;

public class Dumper {

	public static String[] dump(World world, boolean dumpEntityes) {
		try {
			Field f = LootTable.class.getDeclaredField("pools");
			f.setAccessible(true);
			List<String> out = new ArrayList<String>();
			for (ResourceLocation a : LootTableList.getAll()) {
				if (!a.toString().startsWith("minecraft:entities") || dumpEntityes) {
					LootTable lt = world.getLootTableManager().getLootTableFromLocation(a);
					List<LootPool> lp = (List<LootPool>) f.get(lt);
					String loot = a.toString() + "\n";
					for (LootPool l : lp) {
						loot += str(l) + "\n";
					}
					out.add(loot);
				}
			}
			return out.toArray(new String[0]);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	private static String str(LootPool lp)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = LootPool.class.getDeclaredField("lootEntries");
		f.setAccessible(true);
		List<LootEntry> les = (List<LootEntry>) f.get(lp);
		StringBuilder out = new StringBuilder();
		for (LootEntry l : les) {
			out.append(str(l) + "\n");
		}
		return out.toString().trim();
	}

	private static String str(LootEntry le)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (le instanceof LootEntryItem) {
			LootEntryItem lei = (LootEntryItem) le;
			Field item = LootEntryItem.class.getDeclaredField("item");
			item.setAccessible(true);
			Field func = LootEntryItem.class.getDeclaredField("functions");
			func.setAccessible(true);
			LootFunction[] lfs = (LootFunction[]) func.get(lei);
			return ((Item) item.get(lei)).getRegistryName().toString() + " " + lfs.length;
		} else {
			return le.getEntryName();
		}
	}

	private static String str(LootFunction lf) {
		return lf.toString();
	}
}
