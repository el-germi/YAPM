package com.germimonte.yapm.renders;

import java.util.List;

import com.germimonte.yapm.YAPM;
import com.germimonte.yapm.blocks.BlockConsole;
import com.germimonte.yapm.obj.RenderObj;
import com.germimonte.yapm.tile.TileEntityConsole;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityConsoleRenderer extends TileEntitySpecialRenderer<TileEntityConsole> {

	public static double[][] ress = { { 1, 1, 1, 1 }, { -4.4, -3.5, 4.63, 0.055 }, { -10.5, -10.4, 10.64, 0.023 },
			{ -25, -26, 24, 0.01333333 }, { -25, -26, 24, 0.01 }, { -30, -33, 29.6, 0.008 }, { 0.1, 0.1, 0.1, 0.1 },
			{ 0.1, 0.1, 0.1, 0.1 } };
	public static int[] a = { 0, 10, 100, 100, 61, 1000, 1000, 1000 };

	private final RenderObj base = new RenderObj("console_base");
	private final RenderObj screen = new RenderObj("console_screen");

	public void render(TileEntityConsole te, double x, double y, double z, float ticks, int stage, float alpha) {
		final int i = te.lines;
		final FontRenderer fontrenderer = this.getFontRenderer();
		final float ang = 360f
				- te.getWorld().getBlockState(te.getPos()).getValue(BlockConsole.FACING).getHorizontalAngle();
		final double nx = x + .5, nz = z + .5;
		GlStateManager.pushMatrix();
		base.render(nx, y, nz, 1f, ang, false);
		if (te.isOn)
			GlStateManager.disableLighting();
		screen.render(nx, y, nz, 1f, ang, te.isOn);
		GlStateManager.translate(nx, y + 1.3, nz);
		GlStateManager.scale(ress[i][3], -ress[i][3], ress[i][3]);
		GlStateManager.rotate(ang, 0, 1, 0);
		GlStateManager.translate(ress[i][0], ress[i][1], ress[i][2]);
		GlStateManager.rotate(13.0779f, 1, 0, 0);
		GlStateManager.depthMask(false);
		if (te.isOn) {
			for (int j = 0; j < i; ++j) {
				ITextComponent itxt = te.text.get(j);
				if (itxt != null) {
					List<ITextComponent> li = GuiUtilRenderComponents.splitText(itxt, a[i], fontrenderer, false, true);
					String s = li != null && !li.isEmpty() ? li.get(0).getFormattedText() : "";
					fontrenderer.drawString(s, 0, j * 10 - i * 5, 0);
				}
			}
			GlStateManager.enableLighting();
		}
		GlStateManager.depthMask(true);
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.popMatrix();
	}

	private static AbstractClientPlayer viewer() {
		return Minecraft.getMinecraft().getRenderViewEntity() instanceof AbstractClientPlayer
				? ((AbstractClientPlayer) Minecraft.getMinecraft().getRenderViewEntity())
				: null;
	}
}