package icbm.explosion.zhapin;

import icbm.core.BaoHu;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EZhaPin extends Entity implements IEntityAdditionalSpawnData
{
	public int explosiveID;

	public int jiaoShuMu;

	public int tickCallCounter;

	private int metadata = -1;

	private boolean endExplosion = false;

	public List<Entity> entityList = new ArrayList<Entity>();

	public List dataList = new ArrayList();

	public List dataList2 = new ArrayList();

	private boolean isMobile = false;

	public EZhaPin(World par1World)
	{
		super(par1World);
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.yOffset = this.height / 2.0F;
		this.renderDistanceWeight = 2f;
		this.ticksExisted = 0;
	}

	public EZhaPin(World par1World, Vector3 position, int explosionID, boolean isMobile)
	{
		this(par1World);
		this.jiaoShuMu = 0;
		this.explosiveID = explosionID;
		this.isMobile = isMobile;
		this.setPosition(position.x, position.y, position.z);
	}

	public EZhaPin(World par1World, Vector3 position, int explosionID, boolean isMobile, int metadata)
	{
		this(par1World, position, explosionID, isMobile);
		this.metadata = metadata;
	}

	@Override
	public String getEntityName()
	{
		return "Explosion";
	}

	@Override
	public void writeSpawnData(ByteArrayDataOutput data)
	{
		data.writeBoolean(this.isMobile);
		data.writeInt(this.explosiveID);
		data.writeInt(this.metadata);
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data)
	{
		this.isMobile = data.readBoolean();
		this.explosiveID = data.readInt();
		this.metadata = data.readInt();
	}

	@Override
	protected void entityInit()
	{
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for
	 * spiders and wolves to prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		if (!BaoHu.nengFangZhaDan(this.worldObj, new Vector3(this).toVector2()))
		{
			this.setDead();
			return;
		}

		if (this.isMobile && (this.motionX != 0 || this.motionY != 0 || this.motionZ != 0))
		{
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
		}

		if (this.ticksExisted == 1)
		{
			ZhaPin.list[this.explosiveID].baoZhaQian(this.worldObj, new Vector3(this), this);
		}

		if (this.tickCallCounter >= ZhaPin.list[this.explosiveID].proceduralInterval(this.worldObj, this.jiaoShuMu))
		{
			if (!this.endExplosion && ZhaPin.list[this.explosiveID].doBaoZha(worldObj, new Vector3(this.posX, this.posY, this.posZ), this, this.metadata, this.jiaoShuMu))
			{
				this.jiaoShuMu += ZhaPin.list[this.explosiveID].countIncrement();
				this.tickCallCounter = 0;
			}
			else
			{
				ZhaPin.list[this.explosiveID].baoZhaHou(worldObj, new Vector3(this.posX, this.posY, this.posZ), this);
				this.setDead();
			}
		}

		tickCallCounter++;

		ZhaPin.list[this.explosiveID].gengXin(worldObj, new Vector3(this.posX, this.posY, this.posZ), this.ticksExisted);

		this.ticksExisted++;
	}

	public void endExplosion()
	{
		this.endExplosion = true;
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		this.isMobile = par1NBTTagCompound.getBoolean("isMobile");
		this.explosiveID = par1NBTTagCompound.getInteger("explosionID");
		this.jiaoShuMu = par1NBTTagCompound.getInteger("callCounter");
		this.ticksExisted = par1NBTTagCompound.getInteger("ticksExisted");
		this.metadata = par1NBTTagCompound.getInteger("metadata");
		this.tickCallCounter = par1NBTTagCompound.getInteger("tickCallCounter");
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setBoolean("isMobile", this.isMobile);
		par1NBTTagCompound.setInteger("explosionID", this.explosiveID);
		par1NBTTagCompound.setInteger("callCounter", this.jiaoShuMu);
		par1NBTTagCompound.setInteger("ticksExisted", this.ticksExisted);
		par1NBTTagCompound.setInteger("metadata", this.metadata);
		par1NBTTagCompound.setInteger("tickCallCounter", this.tickCallCounter);
	}

	@Override
	public float getShadowSize()
	{
		return 0F;
	}
}