package com.germimonte.yapm.obj;

import com.germimonte.yapm.YAPM;
import com.germimonte.yapm.obj.OBJLoader;
import com.germimonte.yapm.obj.Obj;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderObj {

	private Obj model;
	private ResourceLocation texture;
	private ResourceLocation texture_alt;

	public RenderObj(String name) {
		this.texture = new ResourceLocation(YAPM.MOD_ID + ":obj/" + name + ".png");
		this.texture_alt = new ResourceLocation(YAPM.MOD_ID + ":obj/" + name + "_alt.png");
		this.model = OBJLoader.loadModel(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("assets/" + YAPM.MOD_ID + "/obj/" + name + ".obj"), true, false);
	}

	public void render(double x, double y, double z, float scale, float angle, boolean alt) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		if (angle != 0f) {
			GlStateManager.rotate(angle, 0, 1, 0);
		}
		if (scale != 1f) {
			GlStateManager.scale(scale, scale, scale);
		}
		Minecraft.getMinecraft().getTextureManager().bindTexture(alt ? texture_alt : texture);
		OBJLoader.render(model);
		GlStateManager.popMatrix();
	}

	public ResourceLocation getTexture() {
		return texture;
	}
}
