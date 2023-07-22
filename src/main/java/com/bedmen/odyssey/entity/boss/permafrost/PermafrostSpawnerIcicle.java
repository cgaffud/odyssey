package com.bedmen.odyssey.entity.boss.permafrost;

import com.bedmen.odyssey.entity.boss.coven.CovenMaster;
import com.bedmen.odyssey.entity.boss.coven.CovenWitch;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


public class PermafrostSpawnerIcicle extends AbstractIndexedIcicleEntity{

    private static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(CovenWitch.class, EntityDataSerializers.INT);
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
        System.out.println(health);
        this.setHealth(health);

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, PermafrostMaster.MAX_HEALTH);
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
                case SPAWNING:
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
        this.setDeltaMovement(((new Vec3(f2x, 0 ,f2z)).normalize().add(0,0.2,0)).normalize().scale(1.75f));
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putInt(PHASE_TAG, this.getPhase().ordinal());
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if(compoundNBT.contains(PHASE_TAG)) {
            this.setPhase(Phase.values()[compoundNBT.getInt(PHASE_TAG)]);
            if (this.getPhase() != Phase.HOVERING)
                this.noPhysics = false;
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PHASE, 0);
    }

    public void setPhase(Phase phase) {
        this.entityData.set(DATA_PHASE, phase.ordinal());
    }
    public Phase getPhase() {
        return Phase.values()[(this.entityData.get(DATA_PHASE))];
    }

}
