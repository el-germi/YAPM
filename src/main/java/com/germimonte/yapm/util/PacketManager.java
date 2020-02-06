package com.germimonte.yapm.util;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.opengl.GL11;

import com.germimonte.yapm.YAPM;

import dan200.computercraft.core.apis.http.Resource;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketManager {
	public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(YAPM.MOD_ID);

	@SideOnly(Side.CLIENT)
	public static boolean message = false;

	public static class MyMessage implements IMessage {

		public int code;
		// code = 0 (BOTH) AKN (Unused?)
		// code = 1 (S->C) display radio icon
		// code = 2 (S->C) available builds
		// code = 3 (C->S) selected option

		public int arg;

		public MyMessage() {
			this(0);
		}

		public MyMessage(int code) {
			this(code, 0);
		}

		public MyMessage(int code, int arg) {
			this.code = code;
			this.arg = arg;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			code = buf.readInt();
			arg = buf.readInt();
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(code);
			buf.writeInt(arg);
		}
	}

	public static class MessageHandeler implements IMessageHandler<MyMessage, MyMessage> {

		public MessageHandeler() {
		}

		@Override
		public MyMessage onMessage(MyMessage message, MessageContext ctx) {
			switch (message.code) {
			case 1:
				displayImage(message, ctx);
				break;
			case 2:
				// give available builds to builder
				break;
			case 3:
				setD(ctx.getServerHandler().player, message.arg);
				break;
			}
			return null;
		}

		Timer t = new Timer();

		@SuppressWarnings("unused")
		private void setD(EntityPlayerMP player, int damage) {
			ItemStack item = player.getActiveItemStack();
			if (false/* item.equals(ModItems.BUILDER) */) {
				item.setItemDamage(damage);
			}
		}

		@SideOnly(Side.CLIENT)
		private synchronized void displayImage(MyMessage msg, MessageContext ctx) {
			message = true;
			t.schedule(new TimerTask() {
				@Override
				public void run() {
					message = false;
				}
			}, 3000l);

		}
	}
}
