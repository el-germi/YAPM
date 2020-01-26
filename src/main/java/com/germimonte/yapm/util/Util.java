package com.germimonte.yapm.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.germimonte.yapm.Config;
import com.germimonte.yapm.Structure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

public class Util {

	private Util() {
		throw new RuntimeException("Class is static, not instanciable");
	}

	@Nonnull
	public static List<Biome> biomes = new ArrayList<Biome>();
	public static boolean allBiomes = false;

	@Nonnull
	public static final Object[] toArray(Object... o) {
		return o;
	}

	@Nullable
	public static <E> Optional<E> getRandomOpt(@Nonnull Collection<E> e) {
		return e.stream().skip(new Random().nextInt(e.size())).findFirst();
	}

	@Nullable
	public static <E> E getRandomElem(@Nonnull Collection<E> e) {
		return e.stream().skip(new Random().nextInt(e.size())).findFirst().orElse(null);
	}

	@Nullable
	public static <E> Optional<E> getRandomOpt(@Nonnull Collection<E> e, @Nonnull Random r) {
		return e.stream().skip(r.nextInt(e.size())).findFirst();
	}

	@Nullable
	public static <E> E getRandomElem(@Nonnull Collection<E> e, @Nonnull Random r) {
		return e.stream().skip(r.nextInt(e.size())).findFirst().orElse(null);
	}

	@Nullable
	public static Logger LOGGER = null;

	public static void log(@Nonnull String s, Object... o) {
		if (LOGGER != null) {
			LOGGER.log(Level.INFO, "\n\n" + s + Arrays.stream(o).reduce("\n\n", (a, b) -> a + "\n" + b));
		} else {
			System.out.println("Logger not ready\n\n" + s + Arrays.stream(o).reduce("\n\n", (a, b) -> a + "\n" + b));
		}
	}

	public static void msgTranslate(@Nonnull Entity dest, @Nonnull String msg, Object... o) {
		dest.sendMessage(new TextComponentTranslation(msg, o));
	}

	private static final ITextComponent msgFromGod = ITextComponent.Serializer.jsonToComponent(
			"[\"\",{\"text\":\"<\"},{\"text\":\"Architect\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Architect\n\"},{\"text\":\"you shou\",\"obfuscated\":true},{\"text\":\"-\"},{\"text\":\"ldn'\",\"obfuscated\":true},{\"text\":\"-\"},{\"text\":\"t se\",\"obfuscated\":true},{\"text\":\"-\"},{\"text\":\"e th\",\"obfuscated\":true},{\"text\":\"-\"},{\"text\":\"is, bug?????\",\"obfuscated\":true}]}},{\"text\":\"> \"}]");

	public static void msgArchitect(@Nonnull Entity dest, @Nonnull String msg) {
		dest.sendMessage(msgFromGod.createCopy().appendText(msg));
	}

	public static void msgFake(@Nonnull Entity dest, @Nonnull String name, @Nonnull String msg) {
		dest.sendMessage(ITextComponent.Serializer.jsonToComponent("[\"\",{\"text\":\"<\"},{\"text\":\"" + name
				+ "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"" + name
				+ "\n\"},{\"text\":\"you shou\",\"obfuscated\":true},{\"text\":\"-\"},{\"text\":\"ldn'\",\"obfuscated\":true},{\"text\":\"-\"},{\"text\":\"t se\",\"obfuscated\":true},{\"text\":\"-\"},{\"text\":\"e th\",\"obfuscated\":true},{\"text\":\"-\"},{\"text\":\"is, bug?????\",\"obfuscated\":true}]}},{\"text\":\"> "
				+ msg + "\"}]"));
	}

	private static final Field[] biomeFields = Biomes.class.getFields();

	public static void refreshBiomes() {
		allBiomes = "all".equalsIgnoreCase(Config.biomes.trim()) || "*".equalsIgnoreCase(Config.biomes.trim());
		if (!allBiomes) {
			biomes = new ArrayList<Biome>();
			String[] bss = Config.biomes.split(",");
			for (String bs : bss) {
				bs = bs.trim().toLowerCase().replace(' ', '_');
				Biome b = Biome.REGISTRY.getObject(new ResourceLocation(bs));
				if (b != null) {
					biomes.add(b);
				} else {
					boolean fail = true;
					for (Field fi : biomeFields) {
						if (fi.getName().equalsIgnoreCase(bs)) {
							try {
								biomes.add((Biome) fi.get(null));
								fail = false;
								break;
							} catch (Throwable e) {
								// NOOP
							}
						}
					}
					if (fail) {
						log("Can't find \"" + bs + "\"\nare you sure you didn't forget the mod prefix?");
					}
				}
			}
		}
	}

	public static void generate(@Nonnull PopulateChunkEvent e) {
		if (Config.generateStructures) {
			World world = e.getWorld();
			BlockPos source = world.getHeight(new BlockPos(e.getChunkX() * 16 + e.getRand().nextInt(12) + 2, 64,
					e.getChunkZ() * 16 + e.getRand().nextInt(12) + 2));
			if (!(world.getBlockState(source).getBlock().equals(Blocks.WATER)
					|| world.getBlockState(source).getBlock().equals(Blocks.FLOWING_WATER)
					|| world.getBlockState(source).getBlock().equals(Blocks.BEDROCK)
					|| world.getBlockState(source).getBlock().equals(Blocks.LAVA)
					|| world.getBlockState(source).getBlock().equals(Blocks.FLOWING_LAVA))) {
				if ((allBiomes || biomes.contains(world.getBiome(source))) && e.getRand().nextFloat() > .985f) {
					Structure.generate(world, source, e.getRand());
				}
			}
		}
	}

	private final List<Block> waters = Arrays.asList(Blocks.FLOWING_WATER, Blocks.WATER, Blocks.FLOWING_LAVA,
			Blocks.LAVA, Blocks.AIR);

	public int height(int x, int z, @Nonnull World world, boolean water) {
		if (water) {
			for (int i = world.getHeight(); i > 0; i--) {
				BlockPos pos = new BlockPos(x, i, z);
				if (!waters.contains(world.getBlockState(pos).getBlock())) {
					return pos.getY();
				}
			}
		}
		return world.getHeight(x, z);
	}
}