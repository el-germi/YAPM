package com.germimonte.yapm.entity;

import java.util.stream.StreamSupport;

import javax.annotation.Nullable;

import com.germimonte.yapm.Config;
import com.germimonte.yapm.SatManager;
import com.germimonte.yapm.init.ModItems;
import com.germimonte.yapm.init.ModSounds;
import com.germimonte.yapm.util.Maths;
import com.germimonte.yapm.util.Util;
import com.ibm.icu.impl.duration.impl.DataRecord.EPluralization;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class EntityRocket extends Entity {

	private static final DataParameter<Boolean> launched = EntityDataManager.<Boolean>createKey(EntityRocket.class,
			DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> launchTick = EntityDataManager.<Integer>createKey(EntityRocket.class,
			DataSerializers.VARINT);
	private static final DataParameter<Boolean> ridable = EntityDataManager.<Boolean>createKey(EntityRocket.class,
			DataSerializers.BOOLEAN);

	public static final float WIDTH = 1.9f;
	public static final float HEIGHT = 12.95f;
	public static final int fheight = 150;

	public static final AxisAlignedBB getAABB(double x, double y, double z) {
		return new AxisAlignedBB(x - WIDTH / 2, y, z - WIDTH / 2, x + WIDTH / 2, y + HEIGHT, z + WIDTH / 2);
	}

	public static final boolean areBlocksInside(World world, double x, double y, double z) {
		AxisAlignedBB bb = getAABB(x, y, z);
		for (BlockPos bp : BlockPos.getAllInBoxMutable(new BlockPos(bb.minX, bb.minY, bb.minZ),
				new BlockPos(bb.maxX, bb.maxY, bb.maxZ))) {
			if (world.getBlockState(bp).getCollisionBoundingBox(world, bp) != null) {
				return true;
			}
		}
		return false;
	}

	public static final boolean areEntitiesInside(World world, double x, double y, double z) {
		return !world.getEntitiesWithinAABBExcludingEntity((Entity) null, getAABB(x, y, z)).isEmpty();
	}

	public static final boolean isAreaEmpty(World world, double x, double y, double z) {
		return !(areEntitiesInside(world, x, y, z) || areBlocksInside(world, x, y, z));
	}

	public EntityRocket(World worldIn) {
		super(worldIn);
		this.setSize(WIDTH, HEIGHT);
		this.preventEntitySpawning = true;
	}

	public EntityRocket(World world, double posX, double posY, double posZ) {
		super(world);
		this.setPosition(posX, posY, posZ);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;
	}

	private void setLaunched(Boolean b) {
		this.getDataManager().set(launched, b);
	}

	public boolean getLaunched() {
		return this.getDataManager().get(launched).booleanValue();
	}

	private void setRidable(Boolean b) {
		this.getDataManager().set(ridable, b);
	}

	public boolean getRidable() {
		return this.getDataManager().get(ridable).booleanValue();
	}

	private void setLaunchTick(Integer i) {
		this.getDataManager().set(launchTick, i);
	}

	public int getLaunchTick() {
		return this.getDataManager().get(launchTick).intValue();
	}

	public boolean tryLaunch(@Nullable Object launcher) {
		AxisAlignedBB a = getAABB(this.posX, this.posY, this.posZ);
		boolean out = !StreamSupport
				.stream(BlockPos
						.getAllInBoxMutable(new BlockPos(a.minX, a.minY, a.minZ), new BlockPos(a.maxX, fheight, a.maxZ))
						.spliterator(), false)
				.anyMatch(b -> world.getBlockState(b).getCollisionBoundingBox(world, b) != null);
		if (out) {
			setLaunched(true);
		} else {
			if (launcher != null && launcher instanceof EntityPlayer) {
				EntityPlayer l = (EntityPlayer) launcher;
				Util.msgTranslate(l, "msg.launch.fail");
			}
		}
		return out;
	}

	@Override
	public boolean isPushedByWater() {
		return false;
	}

	@Override
	public boolean isInWater() {
		return false;
	}

	@Override
	public boolean handleWaterMovement() {
		return false;
	}

	@Override
	public void setInWeb() {
	}

	@Override
	public double getMountedYOffset() {
		return this.height - .4;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entity) {
		return entity.getEntityBoundingBox();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getEntityBoundingBox();
	}

	@Override
	public String getName() {
		return "rocket";
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public void setFire(int i) {
		tryLaunch(null);
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (player.isSneaking()) {
			return false;
		} else {
			if (!world.isRemote) {
				if (player.getHeldItemMainhand().getItem().equals(Items.FLINT_AND_STEEL)) {
					tryLaunch(player);
				} else if (Config.ridableRockets) {
					if (player.getHeldItemMainhand().getItem().equals(Items.SADDLE) && !getRidable()) {
						setRidable(true);
						player.getHeldItemMainhand().shrink(1);
					} else if (this.getRidable()) {
						player.startRiding(this);
					}
				}
			}
			return true;
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (!getLaunched()) {
			switch (source.getDamageType()) {
			case "inFire":
			case "onFire":
			case "lava":
				tryLaunch(null);
				break;
			case "arrow":
				Entity entity = source.getImmediateSource();
				if (entity instanceof EntityArrow && entity.isBurning()) {
					tryLaunch(source.getTrueSource());
				}
				break;
			case "player":
				if (!world.isRemote) {
					if (!((EntityPlayer) (source.getTrueSource())).isCreative())
						dropItem(ModItems.ROCKET, 1);
					// world.spawnEntity(new EntityItem(world, posX, posY, posZ, new
					// ItemStack(ModItems.ROCKET)));
					playSound(ModSounds.ROCKET_HIT, 1, 1);
				}
				setDead();
				return true;
			}
		}
		return false;
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;// Gravity calculations stolen from PrimedTNT
		prevPosY = posY;
		prevPosZ = posZ;
		if (!hasNoGravity()) {
			motionY -= 0.03999999910593033D;
		}
		move(MoverType.SELF, motionX, motionY, motionZ);
		motionX *= 0.9800000190734863D;
		motionY *= 0.9800000190734863D;
		motionZ *= 0.9800000190734863D;
		if (onGround) {
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
			motionY *= -0.5D;
		}
		markVelocityChanged();// End gravity

		int i = getLaunchTick();
		if (getLaunched()) {
			if (i == 0) {
				if (!world.isRemote)
					playSound(ModSounds.ROCKET_LAUNCH, 1, 1);
				setNoGravity(true);
			}
			// AxisAlignedBB bb = getCollisionBoundingBox();
			// BlockPos.getAllInBoxMutable(new BlockPos(bb.minX, bb.maxY, bb.minZ),
			// new BlockPos(bb.maxX, bb.maxY + .5, bb.maxZ)).forEach(bp ->
			// world.destroyBlock(bp, true));
			final float siz = .3f;
			motionY += i * i * i / 1234567890F;
			int fire = Math.min((int) (50 * i / 200f), 50);
			for (int p = 50 - fire; p > 0; p--) {
				float a = rand.nextFloat(), b = rand.nextFloat() * siz;
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX + Maths.x(a, b) - 0.270433, posY,
						posZ + Maths.y(a, b) + 0.468404, 0, -rand.nextFloat(), 0);
			}
			for (int p = fire; p > 0; p--) {
				float a = rand.nextFloat(), b = rand.nextFloat() * siz;
				world.spawnParticle(EnumParticleTypes.FLAME, true, posX + Maths.x(a, b) - 0.270433, this.posY,
						posZ + Maths.y(a, b) + 0.468404, 0, -rand.nextFloat(), 0);
			}
			for (int p = 50 - fire; p > 0; p--) {
				float a = rand.nextFloat(), b = rand.nextFloat() * siz;
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX + Maths.x(a, b) - 0.270433, posY,
						posZ + Maths.y(a, b) - 0.468404, 0, -rand.nextFloat(), 0);
			}
			for (int p = fire; p > 0; p--) {
				float a = rand.nextFloat(), b = rand.nextFloat() * siz;
				world.spawnParticle(EnumParticleTypes.FLAME, true, posX + Maths.x(a, b) - 0.270433, this.posY,
						posZ + Maths.y(a, b) - 0.468404, 0, -rand.nextFloat(), 0);
			}
			for (int p = 50 - fire; p > 0; p--) {
				float a = rand.nextFloat(), b = rand.nextFloat() * siz;
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX + Maths.x(a, b) + 0.540866, posY,
						posZ + Maths.y(a, b), 0, -rand.nextFloat(), 0);
			}
			for (int p = fire; p > 0; p--) {
				float a = rand.nextFloat(), b = rand.nextFloat() * siz;
				world.spawnParticle(EnumParticleTypes.FLAME, true, posX + Maths.x(a, b) + 0.540866, this.posY,
						posZ + Maths.y(a, b), 0, -rand.nextFloat(), 0);
				// world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true,
				// posX+Maths.x(a,b), this.posY, posZ+Maths.y(a,b), 0, 0, 0);
			}
			setLaunchTick(i + 1);

			if (this.posY > fheight) {
				if (!world.isRemote) {
					playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1, 1);
				}
				for (int p = 269; p != 0; p--) {
					world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, this.posX - .5 + rand.nextFloat(),
							this.posY - .5 + rand.nextFloat(), this.posZ - .5 + rand.nextFloat(), .5 - rand.nextFloat(),
							(.5 - rand.nextFloat()) / 3, .5 - rand.nextFloat());
				}
				SatManager.setSatOnline(world, true);
				this.setDead();
			}
		}
		super.onUpdate();// kill @e[type=!player]
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.setLaunched(nbt.getBoolean("launched"));
		this.setLaunchTick(nbt.getInteger("launchTick"));
		this.setRidable(nbt.getBoolean("ridable"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setBoolean("launched", this.getLaunched());
		nbt.setInteger("launchTick", this.getLaunchTick());
		nbt.setBoolean("ridable", this.getRidable());
	}

	@Override
	protected void entityInit() {
		dataManager.register(launched, false);
		dataManager.register(launchTick, 0);
		dataManager.register(ridable, false);
	}
}
