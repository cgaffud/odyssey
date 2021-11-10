package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.entity.ai.OdysseyCreeperSwellGoal;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Explosion;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.UUID;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IChargeableMob.class
)
public class OdysseyCreeperEntity extends MonsterEntity implements IChargeableMob {
    private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final DataParameter<Boolean> DATA_BABY_ID = EntityDataManager.defineId(OdysseyCreeperEntity.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Integer> DATA_SWELL_DIR = EntityDataManager.defineId(OdysseyCreeperEntity.class, DataSerializers.INT);
    protected static final DataParameter<Boolean> DATA_IS_POWERED = EntityDataManager.defineId(OdysseyCreeperEntity.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> DATA_IS_IGNITED = EntityDataManager.defineId(OdysseyCreeperEntity.class, DataSerializers.BOOLEAN);
    protected int oldSwell;
    protected int swell;
    protected int maxSwell = 30;
    protected int explosionRadius = 3;

    public OdysseyCreeperEntity(EntityType<? extends OdysseyCreeperEntity> p_i50213_1_, World p_i50213_2_) {
        super(p_i50213_1_, p_i50213_2_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new OdysseyCreeperSwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, OcelotEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, CatEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    /**
     * The maximum height from where the entity is alowed to jump (used in pathfinder)
     */
    public int getMaxFallDistance() {
        return this.getTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F);
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        boolean flag = super.causeFallDamage(p_225503_1_, p_225503_2_);
        this.swell = (int)((float)this.swell + p_225503_1_ * 1.5F);
        if (this.swell > this.getMaxSwell() - 5) {
            this.swell = this.getMaxSwell() - 5;
        }

        return flag;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SWELL_DIR, -1);
        this.entityData.define(DATA_IS_POWERED, false);
        this.entityData.define(DATA_IS_IGNITED, false);
        this.entityData.define(DATA_BABY_ID, false);
    }

    public int getMaxSwell(){
        return this.isBaby() ? Math.round((float)this.maxSwell / 3f * 2f) : this.maxSwell;
    }

    public boolean isBaby() {
        return this.getEntityData().get(DATA_BABY_ID);
    }

    public void setBaby(boolean isBaby) {
        this.getEntityData().set(DATA_BABY_ID, isBaby);
        if (this.level != null && !this.level.isClientSide) {
            ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            modifiableattributeinstance.removeModifier(SPEED_MODIFIER_BABY);
            if (isBaby) {
                modifiableattributeinstance.addTransientModifier(SPEED_MODIFIER_BABY);
            }
        }
    }

    public void onSyncedDataUpdated(DataParameter<?> pKey) {
        if (DATA_BABY_ID.equals(pKey)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(pKey);
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.entityData.get(DATA_IS_POWERED)) {
            pCompound.putBoolean("powered", true);
        }

        pCompound.putShort("Fuse", (short)this.maxSwell);
        pCompound.putByte("ExplosionRadius", (byte)this.explosionRadius);
        pCompound.putBoolean("ignited", this.isIgnited());
        pCompound.putBoolean("IsBaby", this.isBaby());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(DATA_IS_POWERED, pCompound.getBoolean("powered"));
        if (pCompound.contains("Fuse", 99)) {
            this.maxSwell = pCompound.getShort("Fuse");
        }

        if (pCompound.contains("ExplosionRadius", 99)) {
            this.explosionRadius = pCompound.getByte("ExplosionRadius");
        }

        if (pCompound.getBoolean("ignited")) {
            this.ignite();
        }
        this.setBaby(pCompound.getBoolean("IsBaby"));
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);

        if(this.random.nextFloat() < net.minecraftforge.common.ForgeConfig.SERVER.zombieBabyChance.get()){
            this.setBaby(true);
        }

        return pSpawnData;
    }

    public double getMyRidingOffset() {
        return this.isBaby() ? 0.0D : -0.45D;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        if (this.isAlive()) {
            this.oldSwell = this.swell;
            if (this.isIgnited()) {
                this.setSwellDir(1);
            }

            int i = this.getSwellDir();
            if (i > 0 && this.swell == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
            }

            this.swell += i;
            if (this.swell < 0) {
                this.swell = 0;
            }

            if (this.swell >= this.getMaxSwell()) {
                this.swell = this.getMaxSwell();
                this.explodeCreeper();
            }
        }

        super.tick();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.CREEPER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.CREEPER_DEATH;
    }

    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        Entity entity = pSource.getEntity();
        if (entity != this && entity instanceof CreeperEntity) {
            CreeperEntity creeperentity = (CreeperEntity)entity;
            if (creeperentity.canDropMobsSkull()) {
                creeperentity.increaseDroppedSkulls();
                this.spawnAtLocation(Items.CREEPER_HEAD);
            }
        }

    }

    protected int getExperienceReward(PlayerEntity pPlayer) {
        if (this.isBaby()) {
            this.xpReward = (int)((float)this.xpReward * 2.5F);
        }
        return super.getExperienceReward(pPlayer);
    }

    public boolean doHurtTarget(Entity pEntity) {
        return true;
    }

    public boolean isPowered() {
        return this.entityData.get(DATA_IS_POWERED);
    }

    /**
     * Params: (Float)Render tick. Returns the intensity of the creeper's flash when it is ignited.
     */
    @OnlyIn(Dist.CLIENT)
    public float getSwelling(float pPartialTicks) {
        return MathHelper.lerp(pPartialTicks, (float)this.oldSwell, (float)this.swell) / (float)(this.getMaxSwell() - 2);
    }

    /**
     * Returns the current state of creeper, -1 is idle, 1 is 'in fuse'
     */
    public int getSwellDir() {
        return this.entityData.get(DATA_SWELL_DIR);
    }

    /**
     * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
     */
    public void setSwellDir(int pState) {
        this.entityData.set(DATA_SWELL_DIR, pState);
    }

    public void thunderHit(ServerWorld pLevel, LightningBoltEntity pLightning) {
        super.thunderHit(pLevel, pLightning);
        this.entityData.set(DATA_IS_POWERED, true);
    }

    protected ActionResultType mobInteract(PlayerEntity pPlayer, Hand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
            this.level.playSound(pPlayer, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.level.isClientSide) {
                this.ignite();
                itemstack.hurtAndBreak(1, pPlayer, (p_213625_1_) -> {
                    p_213625_1_.broadcastBreakEvent(pHand);
                });
            }

            return ActionResultType.sidedSuccess(this.level.isClientSide);
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    /**
     * Creates an explosion as determined by this creeper's power and explosion radius.
     */
    private void explodeCreeper() {
        if (!this.level.isClientSide) {
            Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
            float f = this.isPowered() ? 2.0F : 1.0F;
            float f1 = this.isBaby() ? 2f/3f : 1.0F;
            this.dead = true;
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f * f1, explosion$mode);
            this.remove();
            this.spawnLingeringCloud();
        }

    }

    private void spawnLingeringCloud() {
        Collection<EffectInstance> collection = this.getActiveEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());
            areaeffectcloudentity.setRadius(2.5F);
            areaeffectcloudentity.setRadiusOnUse(-0.5F);
            areaeffectcloudentity.setWaitTime(10);
            areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
            areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float)areaeffectcloudentity.getDuration());

            for(EffectInstance effectinstance : collection) {
                areaeffectcloudentity.addEffect(new EffectInstance(effectinstance));
            }

            this.level.addFreshEntity(areaeffectcloudentity);
        }

    }

    public boolean isIgnited() {
        return this.entityData.get(DATA_IS_IGNITED);
    }

    public void ignite() {
        this.entityData.set(DATA_IS_IGNITED, true);
    }
}
