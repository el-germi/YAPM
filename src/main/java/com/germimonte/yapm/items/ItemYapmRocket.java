package com.germimonte.yapm.items;

import com.germimonte.yapm.entity.EntityRocket;
import com.germimonte.yapm.init.ModSounds;
import com.germimonte.yapm.util.Util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemYapmRocket extends ItemBase {

	public ItemYapmRocket(String s) {
		super(s);
		setMaxStackSize(1);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 40;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			RayTraceResult ray = player.rayTrace(
					player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE).getAttributeValue(),
					Minecraft.getMinecraft().getRenderPartialTicks());
			if (ray.typeOfHit == RayTraceResult.Type.BLOCK) {
				BlockPos pos = ray.getBlockPos();
				use(world, stack, world.getBlockState(pos).getCollisionBoundingBox(world, pos) == null ? pos
						: pos.offset(ray.sideHit), player);
			}
		}
		return stack;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		player.getEntityWorld().playSound((EntityPlayer) player, player.posX, player.posY, player.posZ,
				ModSounds.ROCKET_PLACE, SoundCategory.PLAYERS, .75f + player.getEntityWorld().rand.nextFloat() / 4, 1);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
		if (!world.isRemote)
			world.getMinecraftServer().getPlayerList().getPlayerByUUID(entityLiving.getUniqueID()).connection
					.sendPacket(new SPacketCustomPayload("MC|StopSound",
							new PacketBuffer(io.netty.buffer.Unpooled.buffer())
									.writeString(SoundCategory.PLAYERS.getName())
									.writeString("yapm:entity.rocket_place")));// 288 characters of pure gibberish
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	private static final EnumActionResult fail(EntityPlayer player, boolean remote) {
		if (remote && player != null)
			Util.msgTranslate(player, "msg.place.fail", EntityRocket.WIDTH, EntityRocket.HEIGHT, EntityRocket.WIDTH);
		return EnumActionResult.FAIL;
	}

	public static EnumActionResult use(World world, ItemStack item, BlockPos pos, EntityPlayer player) {
		double x = pos.getX() + .5;
		double y = pos.getY();
		double z = pos.getZ() + .5;
		if (EntityRocket.isAreaEmpty(world, x, y, z)) {
			if (!world.isRemote) {
				Entity rocket = EntityList.createEntityByIDFromName(new ResourceLocation("yapm:rocket"), world);
				rocket.setLocationAndAngles(x, y, z, 0, 90);
				ItemMonsterPlacer.applyItemEntityDataToEntity(world, null, item, rocket);
				world.spawnEntity(rocket);
				world.playSound(null, x, y, z, ModSounds.ROCKET_HIT, SoundCategory.PLAYERS, 0.95F, 1F);
			}
			item.shrink((player != null && player.isCreative()) ? 0 : 1);
			return EnumActionResult.SUCCESS;
		} else {
			return fail(player, world.isRemote);
		}
	}
}