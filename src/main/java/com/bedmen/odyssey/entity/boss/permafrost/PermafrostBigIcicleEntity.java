package com.bedmen.odyssey.entity.boss.permafrost;

import com.bedmen.odyssey.entity.boss.BossSubEntity;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanMaster;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanSegment;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PermafrostBigIcicleEntity extends BossSubEntity<PermafrostMaster> {

    private static final EntityDataAccessor<Integer> DATA_ICICLE_ID = SynchedEntityData.defineId(MineralLeviathanMaster.class, EntityDataSerializers.INT);
    public static final int TOTAL_NUM = 6;

    public PermafrostBigIcicleEntity(EntityType<? extends BossSubEntity<PermafrostMaster>> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
        this.noPhysics = true;
    }

    public PermafrostBigIcicleEntity(Level level, int id) {
        this(EntityTypeRegistry.PERMAFROST_BIG_ICICLE_ENTITY.get(), level);
        this.setIcicleId(id);
    }

    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL ) {
            this.discard();
        }
    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide && (this.getMaster().isPresent())) {
            Vec3 ownerPos = this.getMaster().get().getPosition(1.0f);
            double theta = Mth.TWO_PI / 20.0D * (double) (this.tickCount % 20.0f);
            double thetaA = theta + (double) this.getIcicleId() * Math.PI * 2.0D / (double) this.TOTAL_NUM;
            float r2 = 4;
            float f2x = (float) (Math.cos(thetaA) * Math.sin(Mth.HALF_PI) * r2);
            float f2y = (float) (Math.cos(Mth.HALF_PI) * r2) + 1.0f;
            float f2z = (float) (Math.sin(thetaA) * Math.sin(Mth.HALF_PI) * r2);
            this.moveTo(new Vec3(f2x, f2y, f2z).add(ownerPos));
        }

        this.hasImpulse = true;
        this.checkInsideBlocks();
    }


    public boolean isOnFire() {
        return false;
    }

    public boolean shouldRenderAtSqrDistance(double p_37336_) {
        return p_37336_ < 16384.0D;
    }

    public boolean isPickable() {
        return true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 1d).add(Attributes.MOVEMENT_SPEED, 2D).add(Attributes.ATTACK_DAMAGE, 10.0D).add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ICICLE_ID, -1);
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putInt("IcicleId", this.getIcicleId());
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if(compoundNBT.contains("IcicleId")){
            this.setIcicleId(compoundNBT.getInt("IcicleId"));
        }
    }

    public void setIcicleId(int icicleId) {
        this.entityData.set(DATA_ICICLE_ID, icicleId);
    }

    public int getIcicleId() {
        return this.entityData.get(DATA_ICICLE_ID);
    }
}
