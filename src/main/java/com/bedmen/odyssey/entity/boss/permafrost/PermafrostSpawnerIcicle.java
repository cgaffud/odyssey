package com.bedmen.odyssey.entity.boss.permafrost;

import com.bedmen.odyssey.entity.boss.coven.CovenMaster;
import com.bedmen.odyssey.entity.boss.coven.CovenWitch;
import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.SoundEventRegistry;
import com.bedmen.odyssey.util.GeneralUtil;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;


public class PermafrostSpawnerIcicle extends AbstractIndexedIcicleEntity{

    private static final EntityDataAccessor<Integer> DATA_WRAITHLING_NUM = SynchedEntityData.defineId(PermafrostSpawnerIcicle.class, EntityDataSerializers.INT);
    private static final String WRAITHLING_NUM_TAG = "WraithlingsTag";

    private static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(PermafrostSpawnerIcicle.class, EntityDataSerializers.INT);
    private static final String PHASE_TAG = "SpawnerPhase";

    public enum Phase {
        HOVERING,
        FLYING,
        SPAWNING,
    }

    public PermafrostSpawnerIcicle(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public PermafrostSpawnerIcicle(Level level, float health, int index) {
        super(EntityTypeRegistry.PERMAFROST_SPAWNER_ICICLE.get(), level, index);
        this.setHealth(health);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, PermafrostMaster.MAX_HEALTH);
    }

    protected SoundEvent getDeathSound() {
        return SoundEventRegistry.WRAITH_DEATH.get();
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        return this.getMaster().map(permafrostMaster ->
                permafrostMaster.hurtSpawner(damageSource, amount, this)
        ).orElseGet(() ->
                super.hurt(damageSource, amount)
        );
    }

    public void tick() {
        super.tick();
        if ((this.getMaster().isPresent())) {
            PermafrostMaster permafrostMaster = this.getMaster().get();
            switch (this.getPhase()) {
                case HOVERING:
                    double thetaA = (double) this.getIcicleIndex() * Math.PI * 2.0D / (double) PermafrostMaster.SPAWNER_AMOUNT;
                    if (!this.level.isClientSide()) {
                        Vec3 ownerPos = permafrostMaster.getPosition(1.0f);
                        float r2 = PermafrostMaster.ICICLE_FOLLOW_RADIUS;
                        float f2x = (float) (Math.cos(thetaA) * Math.sin(Mth.HALF_PI) * r2);
                        float f2y = (float) (Math.cos(Mth.HALF_PI) * r2) + 0.25f;
                        float f2z = (float) (Math.sin(thetaA) * Math.sin(Mth.HALF_PI) * r2);
                        this.moveTo(new Vec3(f2x, f2y, f2z).add(ownerPos));
                    }
                    float thetaB = (float) (thetaA + Mth.HALF_PI);
                    this.setYRot(thetaB);
                    break;
                case FLYING:
                    if (!this.level.getBlockState(this.blockPosition().below()).isAir()) {
                        this.setPhase(Phase.SPAWNING);
                    }
                    break;
                case SPAWNING:
                    if (GeneralUtil.isHashTick(this, this.level, 20) && (this.getRandom().nextFloat() < 0.2f) &&
                        !this.level.isClientSide() && (this.getWraithlingNum() < this.spawningAmount())) {
                       this.spawnWraithling();
                    }
                    if (!this.level.isClientSide()) {
                        RandomSource randomSource = this.getRandom();
                        for (int i = 0; i < 4; ++i) {
                            ((ServerLevel) (this.getLevel())).sendParticles(new DustParticleOptions(new Vector3f(0.35f, 0.35f, 0.35f), 1.0F), this.getX() + (randomSource.nextFloat() - 0.5f), this.getEyeY() + (randomSource.nextFloat()-0.5f), this.getZ() + (randomSource.nextFloat() - 0.5f) , 2, 0.2D, 0.2D, 0.2D, 0.0D);
                        }
                    }
            }
        }

        this.hasImpulse = true;
        this.checkInsideBlocks();
    }

    public void startFlying() {
        this.setPhase(Phase.FLYING);
        this.noPhysics = false;
        double thetaA = (double) this.getIcicleIndex() * Math.PI * 2.0D / (double) PermafrostMaster.SPAWNER_AMOUNT;
        float r2 = PermafrostMaster.ICICLE_FOLLOW_RADIUS;
        float f2x = (float) (Math.cos(thetaA) * Math.sin(Mth.HALF_PI) * r2);
        float f2z = (float) (Math.sin(thetaA) * Math.sin(Mth.HALF_PI) * r2);
        this.setDeltaMovement(((new Vec3(f2x, 0 ,f2z)).normalize().add(0,0.15,0)).normalize().scale(2f));
    }

    private int spawningAmount() {
        if (this.getMaster().isPresent()) {
            PermafrostMaster permafrostMaster = this.getMaster().get();
            return Mth.clamp(permafrostMaster.getNearbyPlayerNumber(), 0, 4);
        }
        return 0;
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putInt(PHASE_TAG, this.getPhase().ordinal());
        compoundNBT.putInt(WRAITHLING_NUM_TAG, this.getWraithlingNum());
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if(compoundNBT.contains(PHASE_TAG)) {
            this.setPhase(Phase.values()[compoundNBT.getInt(PHASE_TAG)]);
            if (this.getPhase() != Phase.HOVERING)
                this.noPhysics = false;
        }
        if (compoundNBT.contains(WRAITHLING_NUM_TAG)) {
            this.setWraithlingNum(compoundNBT.getInt(WRAITHLING_NUM_TAG));
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PHASE, 0);
        this.entityData.define(DATA_WRAITHLING_NUM, 0);
    }

    public void setPhase(Phase phase) {
        this.entityData.set(DATA_PHASE, phase.ordinal());
    }

    public Phase getPhase() {
        return Phase.values()[(this.entityData.get(DATA_PHASE))];
    }

    public void setWraithlingNum(int wraithlingNum) {
        this.entityData.set(DATA_WRAITHLING_NUM, wraithlingNum);
    }

    public int getWraithlingNum() {
        return this.entityData.get(DATA_WRAITHLING_NUM);
    }

    public void spawnWraithling() {
        Wraithling wraithling = new Wraithling(EntityTypeRegistry.WRAITHLING.get(), this.level);
        float phi = this.getRandom().nextFloat() * Mth.TWO_PI;
        wraithling.moveTo(this.position().add(new Vec3(Mth.cos(phi) * 1.5f, 0, Mth.sin(phi) * 1.5f)));
        wraithling.setOwner(this);
        this.setWraithlingNum(this.getWraithlingNum() + 1);
        this.level.addFreshEntity(wraithling);
    }

}
