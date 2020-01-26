package com.germimonte.yapm.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

import com.germimonte.yapm.blocks.BlockBrokenPC;
import com.germimonte.yapm.blocks.BlockConsole;
import com.germimonte.yapm.blocks.BlockRadio;
import com.germimonte.yapm.items.ItemBase;

public class ModBlocks {
	private static final List<Block> BLOCKS = new ArrayList<Block>();

	public static final BlockBrokenPC BROKEN_PC = new BlockBrokenPC("brokenpc");
	public static final BlockBrokenPC BROKEN_PC_ADV = new BlockBrokenPC("brokenpcadv");
	public static final BlockBrokenPC BROKEN_PC_MOS = new BlockBrokenPC("brokenpcmos");
	public static final BlockBrokenPC BROKEN_PC_ADV_MOS = new BlockBrokenPC("brokenpcadvmos");
	public static final BlockRadio RADIO = new BlockRadio("radio", false);
	public static final BlockRadio RADIO_LIT = new BlockRadio("radio_lit", true);
	public static final BlockConsole CONSOLE = new BlockConsole("console");

	public static void addBlock(Block i) {
		BLOCKS.add(i);
	}

	public static Block[] getBlocks() {
		return BLOCKS.toArray(new Block[0]);
	}
}
