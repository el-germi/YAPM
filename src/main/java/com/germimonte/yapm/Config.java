package com.germimonte.yapm;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;

@net.minecraftforge.common.config.Config(modid = YAPM.MOD_ID)
public class Config {

	@Name("Is Satellite Required")
	@Comment({ "whether you need the satellite in orbit before accesing the position features", "default = true" })
	public static boolean isSatelliteReqired = true;

	@Name("Are Riddable rockets enabled")
	@Comment({ "whether you can saddle and ride rockets (dangerous)", "default = false" })
	public static boolean ridableRockets = false;

	@Name("Are Structures enabled")
	@Comment({ "whether structures will generate on world gen", "default = true" })
	public static boolean generateStructures = true;

	@Name("Biomes for structure")
	@Comment({ "witch biomes can structures generate on", "names separeted by commas", "needs server restart",
			"default = Plains, Ice_flats, Swampland" })
	@RequiresWorldRestart
	public static String biomes = "Plains, Ice_flats, Swampland";
}
