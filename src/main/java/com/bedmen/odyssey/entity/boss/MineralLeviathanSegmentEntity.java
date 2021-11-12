package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.items.equipment.EquipmentPickaxeItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public abstract class MineralLeviathanSegmentEntity extends BossEntity {
    protected static final DataParameter<String> DATA_SHELL_ID = EntityDataManager.defineId(MineralLeviathanSegmentEntity.class, DataSerializers.STRING);
    protected static final DataParameter<Float> DATA_SHELL_HEALTH_ID = EntityDataManager.defineId(MineralLeviathanSegmentEntity.class, DataSerializers.FLOAT);
    protected boolean initBody = false;
    protected float clientSideShellHealth;
    protected boolean clientSideShellHealthUpdated;
    protected float damageReduction = 1.0f;

    public MineralLeviathanSegmentEntity(EntityType<? extends MineralLeviathanSegmentEntity> entityType, World world) {
        super(entityType, world);
        this.setHealth(this.getMaxHealth());
        this.noPhysics = true;
        this.setNoGravity(true);
        this.lookControl = new MineralLeviathanSegmentLookController(this);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SHELL_ID, "COPPER");
        this.entityData.define(DATA_SHELL_HEALTH_ID, 0.0f);
    }

    public void tick() {
        this.damageReduction = this.difficultyDamageReductionMultiplier() * this.nearbyPlayerDamageReductionMultiplier();
        this.setNoGravity(true);
        this.noPhysics = true;
        super.tick();
    }

    public void aiStep() {
        if (this.level.isClientSide) {
            if(!this.clientSideShellHealthUpdated){
                this.clientSideShellHealth = this.getShellHealth();
                this.clientSideShellHealthUpdated = true;
            }
            if (!this.isSilent()) {
                float shellHealth = this.getShellHealth();
                if(shellHealth != this.clientSideShellHealth){
                    if(shellHealth <= 0.0f){
                        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_BREAK, SoundCategory.HOSTILE, 3.0f, 1.0f, false);
                    } else {
                        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.STONE_BREAK, SoundCategory.HOSTILE, 3.0f, 1.0f, false);
                    }
                    this.clientSideShellHealth = shellHealth;
                }
            }
        }

        if(!this.isNoAi()){
            if(!this.level.isClientSide){
                //Damage
                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(this.getX()-1.0d,this.getY(),this.getZ()-1.0d,this.getX()+1.0d,this.getY()+2.0d,this.getZ()+1.0d);
                List<LivingEntity> livingEntityList =  this.level.getEntitiesOfClass(LivingEntity.class, axisAlignedBB);
                for(LivingEntity livingEntity : livingEntityList){
                    if(!(livingEntity instanceof MineralLeviathanHeadEntity) && !(livingEntity instanceof MineralLeviathanBodyEntity) && this.isAlive()){
                        livingEntity.hurt(DamageSource.mobAttack(this).setScalesWithDifficulty(), (float)this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE));
                    }
                }
                //Spawn SilverFish
            }
        }
        super.aiStep();
    }

    protected void setRotation(Vector3d vector3d) {
        float f = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(vector3d));
        if (vector3d.lengthSqr() != 0.0D) {
            this.yRot = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
            this.xRot = (float)(MathHelper.atan2(f, vector3d.y) * (double)(180F / (float)Math.PI) * -1.0f + 90.0f);
        }
    }

    public void setShellType(String s) {
        this.entityData.set(DATA_SHELL_ID, s);
    }

    public void setShellType(ShellType shellType) {
        this.entityData.set(DATA_SHELL_ID, shellType.name());
    }

    public ShellType getShellType() {
        return ShellType.valueOf(this.entityData.get(DATA_SHELL_ID));
    }

    public void setShellHealth(float f) {
        this.entityData.set(DATA_SHELL_HEALTH_ID, f);
    }

    public float getShellHealth() {
        return this.entityData.get(DATA_SHELL_HEALTH_ID);
    }

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putString("ShellType", this.getShellType().name());
        compoundNBT.putFloat("ShellHealth", this.getShellHealth());
    }

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if(compoundNBT.contains("ShellType")){
            this.setShellType(compoundNBT.getString("ShellType"));
        }
        if(compoundNBT.contains("ShellHealth")){
            this.setShellHealth(compoundNBT.getFloat("ShellHealth"));
        }
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        Entity entity = damageSource.getEntity();
        if(entity instanceof LivingEntity){
            Item item = ((LivingEntity) entity).getItemInHand(Hand.MAIN_HAND).getItem();
            if(item instanceof PickaxeItem || item instanceof EquipmentPickaxeItem){
                return this.hurtWithShell(damageSource, amount);
            }
        }
        if(damageSource.isExplosion()){
            return this.hurtWithShell(damageSource, amount);
        }
        if(damageSource == DamageSource.OUT_OF_WORLD){
            return super.hurt(damageSource, amount);
        }
        return false;
    }

    protected boolean hurtWithShell(DamageSource damageSource, float amount){
        float shellHealth = this.getShellHealth();
        if(shellHealth > 0.0f){
            if(!this.level.isClientSide){
                this.setShellHealth(shellHealth - amount * this.getDamageReduction());
            }
            return false;
        } else {
            return this.hurtWithoutShell(damageSource, amount);
        }
    }

    protected boolean hurtWithoutShell(DamageSource damageSource, float amount){
        return super.hurt(damageSource, amount);
    }

    public Difficulty getDifficulty(){
        return this.level.getDifficulty();
    }

    public float getDamageReduction(){
        return this.damageReduction;
    }

    protected boolean isAlwaysExperienceDropper() {
        return true;
    }

    public enum ShellType{
        RUBY(0.3f),
        COPPER(0.1f),
        SILVER(0.2f);

        private final float percentageHealth;

        ShellType(float percentageHealth){
            this.percentageHealth = percentageHealth;
        }

        public float getShellMaxHealth(){
            return this.percentageHealth * (float) MineralLeviathanHeadEntity.BASE_HEALTH;
        }

        public static ShellType getRandomShellType(Random random){
            ShellType[] values = ShellType.values();
            return values[random.nextInt(values.length-1)+1];
        }
    }

    class MineralLeviathanSegmentLookController extends LookController {
        MineralLeviathanSegmentLookController(MobEntity p_i225729_2_) {
            super(p_i225729_2_);
        }

        protected boolean resetXRotOnTick() {
            return false;
        }
    }
}
