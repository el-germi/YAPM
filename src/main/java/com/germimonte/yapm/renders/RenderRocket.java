package com.germimonte.yapm.renders;

import com.germimonte.yapm.entity.EntityRocket;
import com.germimonte.yapm.obj.RenderObj;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderRocket extends Render<EntityRocket> {

	public static Factory FACTORY = new Factory();
	RenderObj obj = new RenderObj("rocket_low");

	public RenderRocket(RenderManager rm) {
		super(rm);
	}

	@Override
	public void doRender(EntityRocket entity, double x, double y, double z, float entityYaw, float partialTicks) {
		obj.render(x, y, z, 1, 0, entity.getRidable());
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityRocket entity) {
		return obj.getTexture();
	}

	public static class Factory implements IRenderFactory<EntityRocket> {
		@Override
		public Render<? super EntityRocket> createRenderFor(RenderManager manager) {
			return new RenderRocket(manager);
		}
	}
}
