package com.germimonte.yapm.entity;

import java.util.ArrayList;
import java.util.List;

import com.germimonte.yapm.fx.Lighting;
import com.germimonte.yapm.init.ModSounds;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

public class EntitySparks extends Entity {

	@SideOnly(Side.CLIENT)
	public List<Lighting> lis = new ArrayList<Lighting>();

	private static final DataParameter<Integer> age = EntityDataManager.<Integer>createKey(EntitySparks.class,
			DataSerializers.VARINT);

	public void setAge(int age) {
		dataManager.set(this.age, age);
	}

	public int getAge() {
		return dataManager.get(age);
	}

	public void grow() {
		int a = getAge();
		if (a > 0)
			setAge(a - 1);
	}

	public EntitySparks(World world) {
		super(world);
		setSize(.1f, .1f);
		for (int i = 4; i != 0; i--)
			lis.add(new Lighting(6));
	}

	public EntitySparks(World world, double x, double y, double z) {
		this(world);
		this.setLocationAndAngles(x, y, z, 0, 0);
		this.markVelocityChanged();
	}

	@Override
	protected void entityInit() {
		dataManager.register(age, 20);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		setAge(tag.getInteger("age"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {
		tag.setInteger("age", getAge());
	}

	@SideOnly(Side.CLIENT)
	public void updateGraphics() {
		lis.replaceAll(a -> Lighting.close(a, 0.02f));
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		updateGraphics();
		if (!isSilent())
			world.playSound(null, posX, posY, posZ, ModSounds.SPARKS, SoundCategory.NEUTRAL,
					rand.nextFloat() * .1f + .45f, rand.nextFloat() * .2f + .9f);
		grow();
		if (getAge() == 0)
			setDead();
	}
}