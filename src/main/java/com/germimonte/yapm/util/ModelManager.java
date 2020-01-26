package com.germimonte.yapm.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.registry.IRegistry;

/**
 * A class to manage additional model registrations, just implement
 * {@link ModelRegistrar}
 */
public class ModelManager {

	public static List<ModelRegistrar> modelRegistrars = new ArrayList<>();

	/**
	 * Registers an object which requires textures/sprites to be registered
	 * 
	 * @param modelRegistrar The object
	 */
	public static void register(ModelRegistrar modelRegistrar) {
		modelRegistrars.add(modelRegistrar);
	}

	/**
	 * Implement this to be able to register additional models
	 */
	public interface ModelRegistrar {

		/**
		 * Called to register models
		 * 
		 * @param modelRegistry The model registry to register models into
		 */
		void registerModels(IRegistry<ModelResourceLocation, IBakedModel> modelRegistry);
	}
}