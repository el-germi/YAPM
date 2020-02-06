package com.germimonte.yapm.renders;

import org.lwjgl.opengl.GL11;

import com.germimonte.yapm.YAPM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderRadioIcon extends Gui {

	public RenderRadioIcon() {

	}

	private static final ResourceLocation texture = new ResourceLocation(YAPM.MOD_ID, "textures/gui/icon.png");

	public void renderIcon(ScaledResolution res) {
		final double s = res.getScaledWidth() * 0.15;
		final double x = res.getScaledWidth() - s - 5;
		final double y = 5;
		final double t = 300.0 / 512.0;
		Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		Tessellator.getInstance().getBuffer().pos(x + 0, y + s, 0).tex(0, t).endVertex();
		Tessellator.getInstance().getBuffer().pos(x + s, y + s, 0).tex(t, t).endVertex();
		Tessellator.getInstance().getBuffer().pos(x + s, y + 0, 0).tex(t, 0).endVertex();
		Tessellator.getInstance().getBuffer().pos(x + 0, y + 0, 0).tex(0, 0).endVertex();
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		Tessellator.getInstance().draw();
		GlStateManager.disableBlend();
	}
}
