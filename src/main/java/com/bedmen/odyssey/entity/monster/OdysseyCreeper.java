package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.entity.ai.OdysseyCreeperSwellGoal;
import com.bedmen.odyssey.entity.projectile.DripstoneShard;
import com.bedmen.odyssey.event_listeners.EntityEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class OdysseyCreeper extends Monster implements PowerableMob {
    private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(OdysseyCreeper.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_DRIPSTONE_SPIKES = SynchedEntityData.defineId(OdysseyCreeper.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(OdysseyCreeper.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> DATA_IS_POWERED = SynchedEntityData.defineId(OdysseyCreeper.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(OdysseyCreeper.class, EntityDataSerializers.BOOLEAN);
    protected static final int MAX_SWELL = 30;
    protected static final int DRIPSTONE_MAX_SWELL = 20;
    protected int oldSwell;
    protected int swell;
    protected int maxSwell = MAX_SWELL;
    protected int explosionRadius = 3;

    public OdysseyCreeper(EntityType<? extends OdysseyCreeper> entityType, Level level) {
        super(entityType, level);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new OdysseyCreeperSwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
    }


    /**
     * The maximum height from where the entity is alowed to jump (used in pathfinder)
     */
    public int getMaxFallDistance() {
        return this.getTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F);
    }

    public boolean causeFallDamage(float p_149687_, float p_149688_, DamageSource p_149689_) {
        boolean flag = super.causeFallDamage(p_149687_, p_149688_, p_149689_);
        this.swell = (int)((float)this.swell + p_149687_ * 1.5F);
        if (this.swell > this.maxSwell - 5) {
            this.swell = this.maxSwell - 5;
        }

        return flag;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SWELL_DIR, -1);
        this.entityData.define(DATA_IS_POWERED, false);
        this.entityData.define(DATA_IS_IGNITED, false);
        this.entityData.define(DATA_BABY_ID, false);
        this.entityData.define(DATA_DRIPSTONE_SPIKES, false);
    }

    public int getMaxSwell(){
        return this.isBaby() ? Math.round((float)this.maxSwell / 3f * 2f) : this.maxSwell;
    }

    public boolean isBaby() {
        return this.getEntityData().get(DATA_BABY_ID);
    }

    public void setBaby(boolean isBaby) {
        this.getEntityData().set(DATA_BABY_ID, isBaby);
        if (!this.level.isClientSide) {
            AttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            modifiableattributeinstance.removeModifier(SPEED_MODIFIER_BABY);
            if (isBaby) {
                modifiableattributeinstance.addTransientModifier(SPEED_MODIFIER_BABY);
            }
        }
    }

    public boolean hasDripstoneSpikes() {
        return this.getEntityData().get(DATA_DRIPSTONE_SPIKES);
    }

    public void setDripstoneSpikes(boolean hasDripstoneSpikes) {
        this.getEntityData().set(DATA_DRIPSTONE_SPIKES, hasDripstoneSpikes);
        if(hasDripstoneSpikes){
            this.maxSwell = DRIPSTONE_MAX_SWELL;
        } else {
            this.maxSwell = MAX_SWELL;
        }
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_BABY_ID.equals(pKey)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(pKey);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.entityData.get(DATA_IS_POWERED)) {
            compoundTag.putBoolean("powered", true);
        }

        compoundTag.putShort("Fuse", (short)this.maxSwell);
        compoundTag.putByte("ExplosionRadius", (byte)this.explosionRadius);
        compoundTag.putBoolean("ignited", this.isIgnited());
        compoundTag.putBoolean("IsBaby", this.isBaby());
        compoundTag.putBoolean("HasDripstoneSpikes", this.hasDripstoneSpikes());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.entityData.set(DATA_IS_POWERED, compoundTag.getBoolean("powered"));
        if (compoundTag.contains("Fuse", 99)) {
            this.maxSwell = compoundTag.getShort("Fuse");
        }

        if (compoundTag.contains("ExplosionRadius", 99)) {
            this.explosionRadius = compoundTag.getByte("ExplosionRadius");
        }

        if (compoundTag.getBoolean("ignited")) {
            this.ignite();
        }
        this.setBaby(compoundTag.getBoolean("IsBaby"));
        this.setDripstoneSpikes(compoundTag.getBoolean("HasDripstoneSpikes"));
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        if(EntityEvents.isBaby(this)){
            this.setBaby(true);
        }
        if(EntityEvents.inDripstoneBiome(this)){
            this.setDripstoneSpikes(true);
        }
        System.out.println("I RAN");
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
        if (entity != this && entity instanceof Creeper) {
            Creeper creeperentity = (Creeper)entity;
            if (creeperentity.canDropMobsSkull()) {
                creeperentity.increaseDroppedSkulls();
                this.spawnAtLocation(Items.CREEPER_HEAD);
            }
        }

    }

    public int getExperienceReward() {
        if (this.isBaby()) {
            this.xpReward = (int)((float)this.xpReward * 2.5F);
        }
        return super.getExperienceReward();
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
    public float getSwelling(float pPartialTicks) {
        return Mth.lerp(pPartialTicks, (float)this.oldSwell, (float)this.swell) / (float)(this.getMaxSwell() - 2);
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

    public void thunderHit(ServerLevel pLevel, LightningBolt pLightning) {
        super.thunderHit(pLevel, pLightning);
        this.entityData.set(DATA_IS_POWERED, true);
    }

    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
            this.level.playSound(pPlayer, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.level.isClientSide) {
                this.ignite();
                itemstack.hurtAndBreak(1, pPlayer, (p_213625_1_) -> {
                    p_213625_1_.broadcastBreakEvent(pHand);
                });
            }

            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    protected void explodeCreeper() {
        if (!this.level.isClientSide) {
            Explosion.BlockInteraction explosion$blockinteraction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
            float f = this.isPowered() ? 2.0F : 1.0F;
            float f1 = this.isBaby() ? 2f/3f : 1.0F;
            this.dead = true;
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f * f1, explosion$blockinteraction);
            this.discard();
            this.spawnLingeringCloud();
            if(this.hasDripstoneSpikes()){
                int dripstoneShardCount = this.getRandom().nextInt(2)+3;
                for (int i = 0; i < dripstoneShardCount; i++) {
                    DripstoneShard dripstoneShard = new DripstoneShard(this.level, this);
                    float phi = this.getRandom().nextFloat() * Mth.TWO_PI;
                    float theta = this.getRandom().nextFloat() * Mth.PI;
                    Vec3 shootingDir = new Vec3(Mth.sin(theta)*Mth.cos(phi), Mth.sin(theta)*Mth.sin(phi), Mth.cos(theta));
                    dripstoneShard.moveTo(this.getPosition(1).add(0,1,0)
                            .add(shootingDir.scale(0.5)));
                    dripstoneShard.shoot(shootingDir.x(), shootingDir.y(), shootingDir.z(), 1.1F, (float)(14 - this.level.getDifficulty().getId() * 4));
                    this.level.addFreshEntity(dripstoneShard);
                }
            }
        }
    }

    private void spawnLingeringCloud() {
        Collection<MobEffectInstance> collection = this.getActiveEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloud areaeffectcloudentity = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
            areaeffectcloudentity.setRadius(2.5F);
            areaeffectcloudentity.setRadiusOnUse(-0.5F);
            areaeffectcloudentity.setWaitTime(10);
            areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
            areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float)areaeffectcloudentity.getDuration());

            for(MobEffectInstance effectinstance : collection) {
                areaeffectcloudentity.addEffect(new MobEffectInstance(effectinstance));
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
