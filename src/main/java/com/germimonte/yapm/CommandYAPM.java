package com.germimonte.yapm;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import com.germimonte.yapm.init.ModItems;
import com.germimonte.yapm.util.PacketManager;
import com.germimonte.yapm.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializationContext;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class CommandYAPM extends CommandBase {

	@Override
	public String getName() {
		return "yapm";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "command.yapm.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if ("satellite".equalsIgnoreCase(args[0])) {
			try {
				SatManager.setSatOnline(sender.getEntityWorld(), Boolean.parseBoolean(args[1]));
			} catch (Exception e) {
			}
			notifyCommandListener(sender, this, "commands.yapm.succsess", args[0],
					SatManager.isSatOnline(sender.getEntityWorld()) ? "online" : "offline");
			return;
		} else if ("itemInfo".equalsIgnoreCase(args[0])) {
			ItemStack p = ((EntityPlayer) sender.getCommandSenderEntity()).getHeldItemMainhand();
			notifyCommandListener(sender, this,
					"item:" + p.getItem().getClass().toString() + "\nnbt:" + p.getTagCompound(), new Object[0]);
			return;
		} else if ("giveRockets".equalsIgnoreCase(args[0])) {
			String out = "commands.yapm.win";
			try {
				ItemStack ribl = new ItemStack(ModItems.ROCKET);
				ItemStack auto = new ItemStack(ModItems.ROCKET);
				ItemStack fast = new ItemStack(ModItems.ROCKET);
				EntityPlayer p = (EntityPlayer) sender.getCommandSenderEntity();
				ribl.setTagCompound(JsonToNBT.getTagFromJson(json("Ridable rocket", "ridable:1b")));
				auto.setTagCompound(JsonToNBT.getTagFromJson(json("Primed rocket", "launched:1b")));
				fast.setTagCompound(JsonToNBT.getTagFromJson(json("Fast rocket", "launchTick:600")));
				if (Config.ridableRockets)
					p.inventory.addItemStackToInventory(ribl);
				p.inventory.addItemStackToInventory(auto);
				p.inventory.addItemStackToInventory(fast);
				p.inventoryContainer.detectAndSendChanges();
			} catch (Exception e) {
				out = "Something went wrong, nothing given :p\n" + e.toString();
			}
			notifyCommandListener(sender, this, out, new Object[0]);
			return;
		} else if ("blockInfo".equalsIgnoreCase(args[0])) {
			String out = "too far/nothing";
			EntityPlayerMP player = (EntityPlayerMP) sender;
			RayTraceResult ray = player.rayTrace(10, Minecraft.getMinecraft().getRenderPartialTicks());
			if (ray.typeOfHit == RayTraceResult.Type.BLOCK) {
				BlockPos pos = ray.getBlockPos();
				Block blo = player.world.getBlockState(pos).getBlock();
				out = blo.getClass().toString() + "\n" + blo.getMetaFromState(player.world.getBlockState(pos));
			}
			notifyCommandListener(sender, this, out, new Object[0]);
			return;
		} else if ("summonStructure".equalsIgnoreCase(args[0])) {
			if (args.length < 3) {
				String out = "k";
				EntityPlayerMP player = (EntityPlayerMP) sender;
				RayTraceResult ray = player.rayTrace(10, Minecraft.getMinecraft().getRenderPartialTicks());
				if (ray.typeOfHit == RayTraceResult.Type.BLOCK) {
					BlockPos pos = ray.getBlockPos();
					Block blo = player.world.getBlockState(pos).getBlock();
					Structure.generate(player.getEntityWorld(), pos, null);
				}
				notifyCommandListener(sender, this, out, new Object[0]);
				return;
			} else {
				String out = "k";
				BlockPos pos = new BlockPos(Integer.parseInt(args[1]), Integer.parseInt(args[2]),
						Integer.parseInt(args[3]));
				Structure.generate(sender.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), null);
				notifyCommandListener(sender, this, out, new Object[0]);
				return;
			}
		} else if ("dumpLists".equalsIgnoreCase(args[0])) {
			StringBuilder out = new StringBuilder();
			for (String s : Dumper.dump(sender.getEntityWorld(), false)) {
				out.append("\n{" + s + "\n}");
			}
			Util.log(out.toString());
			notifyCommandListener(sender, this, out.toString(), new Object[0]);
			return;
		} else if ("test".equalsIgnoreCase(args[0])) {
			if (sender instanceof EntityPlayerMP)
				PacketManager.INSTANCE.sendTo(new PacketManager.MyMessage(1), (EntityPlayerMP) sender);
			else
				PacketManager.INSTANCE.sendToAll(new PacketManager.MyMessage(1));
			notifyCommandListener(sender, this, "Sent", new Object[0]);
			return;
		}
		throw new WrongUsageException("commands.yapm.usage", new Object[0]);
	}

	private String json(String n, String t) {
		return "{display:{Name:\"§r§b" + n + "\",Lore:[\"§r§5EntityTag:{" + t + "}\"]},EntityTag:{" + t + "}}";
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		if (args.length == 1) {
			return getListOfStringsMatchingLastWord(args, "satellite", "itemInfo", "giveRockets", "blockInfo",
					"summonStructure", "dumpLists", "test");
		} else {
			if ("satellite".equalsIgnoreCase(args[0])) {
				return getListOfStringsMatchingLastWord(args, "true", "false");
			} else {
				return Collections.emptyList();
			}
		}
	}
}
