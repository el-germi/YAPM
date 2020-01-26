package com.germimonte.yapm.util;

import java.util.Random;

import com.germimonte.yapm.YAPM;
import com.germimonte.yapm.items.ItemDisc;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.oredict.OreDictionary;

public class FunctionRandomize extends LootFunction {

	private final String ores;

	protected FunctionRandomize(LootCondition[] conditionsIn, String ores) {
		super(conditionsIn);
		this.ores = ores;
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		if (stack.getItem() instanceof ItemDisc) {
			NonNullList<ItemStack> o = OreDictionary.getOres(ores);
			ItemStack item = o.get(rand.nextInt(o.size()));
			return item;
		} else {
			return stack;
		}
	}

	public static class Serializer extends LootFunction.Serializer<FunctionRandomize> {

		public Serializer() {
			super(new ResourceLocation(YAPM.MOD_ID, "randomize"), FunctionRandomize.class);
		}

		public void serialize(JsonObject object, FunctionRandomize func, JsonSerializationContext context) {
			object.addProperty("ores", func.ores);
		}

		public FunctionRandomize deserialize(JsonObject obj, JsonDeserializationContext c, LootCondition[] conds) {
			return new FunctionRandomize(conds, JsonUtils.getString(obj, "ores"));
		}
	}
}
