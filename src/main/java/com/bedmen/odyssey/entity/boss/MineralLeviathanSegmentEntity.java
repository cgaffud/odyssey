package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;
import java.util.Random;

public abstract class MineralLeviathanSegmentEntity extends BossEntity implements IEntityAdditionalSpawnData {
    protected static final DataParameter<Float> DATA_SHELL_HEALTH_ID = EntityDataManager.defineId(MineralLeviathanSegmentEntity.class, DataSerializers.FLOAT);
    protected boolean initBody = false;
    protected float damageReduction = 1.0f;
    protected ShellType shellType;

    public MineralLeviathanSegmentEntity(EntityType<? extends MineralLeviathanSegmentEntity> entityType, World world) {
        super(entityType, world);
        this.setHealth(this.getMaxHealth());
        this.noPhysics = true;
        this.setNoGravity(true);
        this.lookControl = new MineralLeviathanSegmentLookController(this);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SHELL_HEALTH_ID, 0.0f);
    }

    public void tick() {
        this.damageReduction = this.difficultyDamageReductionMultiplier() * this.nearbyPlayerDamageReductionMultiplier();
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

    public void setShellType(ShellType shellType) {
        this.shellType = shellType;
        this.setShellHealth(shellType.getShellMaxHealth());
    }

    public ShellType getShellType() {
        return this.shellType;
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
            this.setShellType(ShellType.valueOf(compoundNBT.getString("ShellType")));
        }
        if(compoundNBT.contains("ShellHealth")){
            this.setShellHealth(compoundNBT.getFloat("ShellHealth"));
        }
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        Entity entity = damageSource.getEntity();
        if(damageSource.isExplosion()){
            return this.hurtWithShell(damageSource, amount);
        }
        else if(damageSource == DamageSource.OUT_OF_WORLD){
            return super.hurt(damageSource, amount);
        }
        else if(entity instanceof LivingEntity){
            Item item = ((LivingEntity) entity).getItemInHand(Hand.MAIN_HAND).getItem();
            if(item instanceof PickaxeItem){
                int harvestLevel = ((PickaxeItem) item).getTier().getLevel();
                if(this.shellType.getHarvestLevel() <= harvestLevel){
                    return this.hurtWithShell(damageSource, amount);
                }
            }
        }
        return false;
    }

    protected boolean hurtWithShell(DamageSource damageSource, float amount){
        float shellHealth = this.getShellHealth();
        if(shellHealth > 0.0f){
            if(!this.level.isClientSide){
                this.setShellHealth(shellHealth - amount * this.getDamageReduction());
            }
            float newShellHealth = this.getShellHealth();
            if (newShellHealth != shellHealth && !this.isSilent()) {
                if(newShellHealth > 0f){
                    this.playSound(SoundEvents.STONE_BREAK, 1.0F, 1.0F);
                } else {
                    this.playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
                    if(!this.level.isClientSide){
                        ItemEntity itementity = this.spawnAtLocation(this.shellType.getItem());
                        if (itementity != null) {
                            itementity.setExtendedLifetime();
                        }
                    }
                }
            }
            return false;
        } else {
            return this.hurtWithoutShell(damageSource, amount);
        }
    }

    protected boolean hurtWithoutShell(DamageSource damageSource, float amount){
        return super.hurt(damageSource, amount);
    }

    protected void dropCustomDeathLoot(DamageSource damageSource, int p_213333_2_, boolean p_213333_3_) {
        super.dropCustomDeathLoot(damageSource, p_213333_2_, p_213333_3_);
        if(this.getShellHealth() > 0f){
            ItemEntity itementity = this.spawnAtLocation(this.shellType.getItem());
            if (itementity != null) {
                itementity.setExtendedLifetime();
            }
        }
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

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(this.getShellType().ordinal());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.setShellType(ShellType.values()[additionalData.readInt()]);
    }

    public enum ShellType{
        RUBY(3, ItemRegistry.RUBY.get()),
        COAL(0, Items.COAL),
        COPPER(1, ItemRegistry.RAW_COPPER.get()),
        IRON(1, ItemRegistry.RAW_IRON.get()),
        LAPIS(1, Items.LAPIS_LAZULI),
        GOLD(2, ItemRegistry.RAW_GOLD.get()),
        SILVER(2, ItemRegistry.RAW_SILVER.get()),
        EMERALD(2, Items.EMERALD),
        REDSTONE(2, Items.REDSTONE);

        private final int harvestLevel;
        private final float percentageHealth;
        private final Item item;

        ShellType(int harvestLevel, Item item){
            this.harvestLevel = harvestLevel;
            this.percentageHealth = (float)harvestLevel * 0.05f + 0.1f;
            this.item = item;
        }

        public float getShellMaxHealth(){
            return this.percentageHealth * (float) MineralLeviathanHeadEntity.BASE_HEALTH;
        }

        public static ShellType getRandomShellType(Random random){
            ShellType[] values = ShellType.values();
            return values[random.nextInt(values.length-1)+1];
        }

        public Item getItem(){
            return this.item;
        }

        public int getHarvestLevel(){
            return this.harvestLevel;
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
