package com.germimonte.yapm.init;

import com.germimonte.yapm.YAPM;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModSounds {
	public static SoundEvent ROCKET_LAUNCH;
	public static SoundEvent ROCKET_HIT;
	public static SoundEvent ROCKET_PLACE;
	public static SoundEvent SPARKS;
	public static SoundEvent MUSIC;

	public static void registerSounds() {
		MUSIC = regSound("music.summer_nights");
		ROCKET_LAUNCH = regSound("entity.rocket");
		ROCKET_HIT = regSound("entity.rocket_hit");
		ROCKET_PLACE = regSound("entity.rocket_place");
		SPARKS = regSound("entity.sparks");
	}

	private static SoundEvent regSound(String name) {
		ResourceLocation rl = new ResourceLocation(YAPM.MOD_ID, name);
		SoundEvent se = new SoundEvent(rl);
		se.setRegistryName(rl);
		ForgeRegistries.SOUND_EVENTS.register(se);
		return se;
	}
}
