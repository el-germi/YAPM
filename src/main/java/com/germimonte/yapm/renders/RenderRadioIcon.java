package com.germimonte.yapm.renders;

import com.germimonte.yapm.YAPM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderRadioIcon extends Gui {

	private static final ResourceLocation texture = new ResourceLocation(YAPM.MOD_ID, "textures/gui/icon.png");

	public void renderIcon(ScaledResolution res) {
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		double s = res.getScaledWidth() * 0.15;
		double x = res.getScaledWidth() - s - 5;
		double y = 5;
		double t = 300.0 / 512.0;
		bufferbuilder.pos(x + 0, y + s, 0).tex(0, t).endVertex();
		bufferbuilder.pos(x + s, y + s, 0).tex(t, t).endVertex();
		bufferbuilder.pos(x + s, y + 0, 0).tex(t, 0).endVertex();
		bufferbuilder.pos(x + 0, y + 0, 0).tex(0, 0).endVertex();
		tessellator.draw();
		GlStateManager.disableBlend();
	}
}
