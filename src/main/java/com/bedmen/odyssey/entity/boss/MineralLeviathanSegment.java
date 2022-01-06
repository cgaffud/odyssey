package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;
import java.util.Random;

public abstract class MineralLeviathanSegment extends Boss implements IEntityAdditionalSpawnData {
    protected static final EntityDataAccessor<Float> DATA_SHELL_HEALTH_ID = SynchedEntityData.defineId(MineralLeviathanSegment.class, EntityDataSerializers.FLOAT);
    protected boolean initBody = false;
    protected float damageReduction = 1.0f;
    protected ShellType shellType;
    protected static final float SILVER_FISH_CHANCE = 0.01f;
    private int silverFishSpawned;

    public MineralLeviathanSegment(EntityType<? extends MineralLeviathanSegment> entityType, Level world) {
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
                List<LivingEntity> livingEntityList =  this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
                for(LivingEntity livingEntity : livingEntityList){
                    if(!(livingEntity instanceof MineralLeviathanHead || livingEntity instanceof Silverfish) && !(livingEntity instanceof MineralLeviathanBody) && this.isAlive()){
                        livingEntity.hurt(DamageSource.mobAttack(this).setScalesWithDifficulty(), (float)this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE));
                    }
                }
                //Spawn SilverFish
                if(!this.hasShell() && this.silverFishSpawned < this.getMaxSilverFish() && this.random.nextFloat() < SILVER_FISH_CHANCE){
                    Silverfish silverfishEntity = EntityType.SILVERFISH.spawn((ServerLevel) this.level, null, null, null, this.blockPosition(), MobSpawnType.REINFORCEMENT, false, false);
                    silverfishEntity.setPos(this.getX(), this.getY(), this.getZ());
                    this.silverFishSpawned++;
                }
            }
        }
        super.aiStep();
    }

    protected void setRotation(Vec3 vector3d) {
        float f = Mth.sqrt((float) vector3d.horizontalDistanceSqr());
        if (vector3d.lengthSqr() != 0.0D) {
            this.setYRot((float)(Mth.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(f, vector3d.y) * (double)(180F / (float)Math.PI) * -1.0f + 90.0f));
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

    public boolean hasShell(){
        return this.getShellHealth() > 0.0f;
    }

    public int getMaxSilverFish(){
        int i = 2;
        switch(this.level.getDifficulty()){
            case HARD:
                i = 6;
                break;
            case NORMAL:
                i = 4;
                break;
        }
        return i * this.getBossEvent().getPlayers().size();
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putString("ShellType", this.getShellType().name());
        compoundNBT.putFloat("ShellHealth", this.getShellHealth());
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
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
            return super.hurt(damageSource, amount*1000f);
        }
        else if(entity instanceof LivingEntity){
            Item item = ((LivingEntity) entity).getItemInHand(InteractionHand.MAIN_HAND).getItem();
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
        if(this.hasShell()){
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
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.getShellType().ordinal());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.setShellType(ShellType.values()[additionalData.readInt()]);
    }

    public enum ShellType{
        RUBY(3, ItemRegistry.RUBY.get()),
        COAL(0, Items.COAL),
        COPPER(1, Items.RAW_COPPER),
        IRON(1, Items.RAW_IRON),
        LAPIS(1, Items.LAPIS_LAZULI),
        GOLD(2, Items.RAW_GOLD),
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
            return this.percentageHealth * (float) MineralLeviathanHead.BASE_HEALTH;
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

    class MineralLeviathanSegmentLookController extends LookControl {
        MineralLeviathanSegmentLookController(Mob p_i225729_2_) {
            super(p_i225729_2_);
        }

        protected boolean resetXRotOnTick() {
            return false;
        }
    }
}
