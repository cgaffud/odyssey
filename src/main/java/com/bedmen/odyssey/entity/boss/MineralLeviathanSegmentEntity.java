package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.util.BossUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;

import java.util.List;

public class MineralLeviathanSegmentEntity extends BossEntity {
    protected static final DataParameter<Float> DATA_YROT_ID = EntityDataManager.defineId(MineralLeviathanSegmentEntity.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> DATA_XROT_ID = EntityDataManager.defineId(MineralLeviathanSegmentEntity.class, DataSerializers.FLOAT);
    protected boolean initBody = false;

    public MineralLeviathanSegmentEntity(EntityType<? extends MineralLeviathanSegmentEntity> entityType, World world) {
        super(entityType, world);
        this.setHealth(this.getMaxHealth());
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_YROT_ID, 0.0f);
        this.entityData.define(DATA_XROT_ID, 0.0f);
    }

    public void tick() {
        this.setNoGravity(true);
        this.noPhysics = true;
        super.tick();
    }

    public void aiStep() {
        if(!this.isNoAi()){
            if(!this.level.isClientSide){
                //Damage
                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(this.getX()-1.0d,this.getY(),this.getZ()-1.0d,this.getX()+1.0d,this.getY()+2.0d,this.getZ()+1.0d);
                List<LivingEntity> livingEntityList =  this.level.getEntitiesOfClass(LivingEntity.class, axisAlignedBB);
                for(LivingEntity livingEntity : livingEntityList){
                    if(!(livingEntity instanceof MineralLeviathanEntity) && !(livingEntity instanceof MineralLeviathanBodyEntity) && this.isAlive()){
                        livingEntity.hurt(DamageSource.mobAttack(this), (float)this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE) * BossUtil.difficultyMultiplier(this.level.getDifficulty()));
                    }
                }
            }
        }
        super.aiStep();
    }

    protected void setRotation(Vector3d vector3d) {
        float f = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(vector3d));
        if (vector3d.lengthSqr() != 0.0D) {
            this.setYRot((float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(MathHelper.atan2(f, vector3d.y) * (double)(180F / (float)Math.PI) * -1.0f + 90.0f));
        }
    }

    public void setYRot(float f) {
        this.entityData.set(DATA_YROT_ID, f);
    }

    public float getYRot() {
        return this.entityData.get(DATA_YROT_ID);
    }

    public void setXRot(float f) {
        this.entityData.set(DATA_XROT_ID, f);
    }

    public float getXRot() {
        return this.entityData.get(DATA_XROT_ID);
    }

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putFloat("TrueYRot", this.getYRot());
        compoundNBT.putFloat("TrueXRot", this.getXRot());
    }

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if(compoundNBT.contains("TrueYRot")){
            this.setYRot(compoundNBT.getFloat("TrueYRot"));
        }
        if(compoundNBT.contains("TrueXRot")){
            this.setXRot(compoundNBT.getFloat("TrueXRot"));
        }
    }
}
