package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.entity.IRotationallyIncompetent;
import com.bedmen.odyssey.util.BossUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class MineralLeviathanSegmentEntity extends BossEntity implements IRotationallyIncompetent {
    protected float trueYRot;
    protected float trueXRot;
    protected boolean initParts = false;

    public MineralLeviathanSegmentEntity(EntityType<? extends MineralLeviathanSegmentEntity> entityType, World world) {
        super(entityType, world);
        this.setHealth(this.getMaxHealth());
        this.noPhysics = true;
        this.setNoGravity(true);
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
                    if(!(livingEntity instanceof MineralLeviathanEntity) && !(livingEntity instanceof MineralLeviathanPartEntity) && this.isAlive()){
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
            this.trueYRot = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
            this.trueXRot = (float)(MathHelper.atan2(f, vector3d.y) * (double)(180F / (float)Math.PI) * -1.0f + 90.0f);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getYRot() {
        return this.trueYRot;
    }

    @OnlyIn(Dist.CLIENT)
    public float getXRot() {
        return this.trueXRot;
    }

    public void setYRot(float f) {
        this.trueYRot = f;
    }

    public void setXRot(float f) {
        this.trueXRot = f;
    }
}
