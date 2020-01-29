package com.germimonte.yapm.renders;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector3f;

import com.germimonte.yapm.entity.EntitySparks;
import com.germimonte.yapm.fx.Lighting;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSparks extends Render<EntitySparks> {

	public static Factory FACTORY = new Factory();

	public RenderSparks(RenderManager rm) {
		super(rm);
	}

	@Override
	public void doRender(EntitySparks entity, double x, double y, double z, float entityYaw, float partialTicks) {
		Vector3f o = new Vector3f((float) x, (float) y, (float) z);
		glPushMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.color(.1f, .1f, 1f, .9f);
		bufferbuilder.setTranslation(x, y, z);
		bufferbuilder.begin(GL_LINES, DefaultVertexFormats.POSITION);
		for (Lighting l : entity.lis)
			l.render(bufferbuilder, .5f);
		tessellator.draw();
		bufferbuilder.setTranslation(0, 0, 0);
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		glPopMatrix();
		//super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySparks entity) {
		return null;
	}

	public static class Factory implements IRenderFactory<EntitySparks> {
		@Override
		public Render<? super EntitySparks> createRenderFor(RenderManager manager) {
			return new RenderSparks(manager);
		}
	}
}
