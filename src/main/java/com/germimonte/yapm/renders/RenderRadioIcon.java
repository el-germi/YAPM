package com.germimonte.yapm.renders;

import org.lwjgl.opengl.GL11;

import com.germimonte.yapm.YAPM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class RenderRadioIcon extends Gui {

	private static final ResourceLocation texture = new ResourceLocation(YAPM.MOD_ID, "textures/gui/icon.png");

	public void renderIcon(int x, int y) {
		// System.out.println("DRAWING");

		x = x / 2 + 40;
		y = y / 2 - 40;
		//GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		//GL11.glPushMatrix();

		GlStateManager.enableBlend();
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawScaledCustomSizeModalRect(x, y, 0, 0, 300, 300, 100, 100, 300, 300);
		GlStateManager.disableBlend();

		//GL11.glPopMatrix();
		//GL11.glPopAttrib();
	}
}
