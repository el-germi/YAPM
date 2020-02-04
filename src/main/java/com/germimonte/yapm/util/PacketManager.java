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

		private int code;
		// code = 0 (BOTH) AKN (Unused)
		// code = 1 (CLIENT) display image (2 secs)

		public MyMessage() {
		}

		public MyMessage(int in) {
			code = in;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			code = buf.readInt();
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(code);
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
			}
			return null;
		}

		Timer t = new Timer();

		@SideOnly(Side.CLIENT)
		private synchronized void displayImage(MyMessage msg, MessageContext ctx) {
			message = true;
			t.schedule(new TimerTask() {

				@Override
				public void run() {
					message = false;
				}
			}, 2000l);

		}
	}
}
