package com.bedmen.odyssey.entity.boss.mineralLeviathan;

import com.bedmen.odyssey.entity.boss.Boss;
import com.bedmen.odyssey.registry.BlockRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;
import java.util.Random;

public abstract class MineralLeviathanSegment extends Boss implements IEntityAdditionalSpawnData {
    protected static final EntityDataAccessor<Float> DATA_SHELL_HEALTH_ID = SynchedEntityData.defineId(MineralLeviathanSegment.class, EntityDataSerializers.FLOAT);
    protected boolean initBody = false;
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
        return i * this.getNearbyPlayerNumber();
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
            return this.hurtWithShell(damageSource, amount, null);
        }
        else if(damageSource == DamageSource.OUT_OF_WORLD){
            return super.hurt(damageSource, amount*1000f);
        }
        else if(entity instanceof LivingEntity livingEntity){
            ItemStack itemStack = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);
            Item item = itemStack.getItem();
            if(item instanceof PickaxeItem pickaxeItem && this.shellType.canHarvest(itemStack)){
                amount = amount * 0.5f + 1f;
                if(this.getShellHealth() > 0.0f){
                    itemStack.hurtAndBreak(1, livingEntity, (livingEntity1) -> {
                        livingEntity1.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                    });
                }
                return this.hurtWithShell(damageSource, amount, itemStack);
            }
        }
        return false;
    }

    protected boolean hurtWithShell(DamageSource damageSource, float amount, ItemStack itemStack){
        float shellHealth = this.getShellHealth();
        if(shellHealth > 0.0f){
            if(this instanceof MineralLeviathanHead && damageSource.isExplosion()){
                return false;
            }
            float newShellHealth = shellHealth - amount * this.damageReduction;
            if(!this.level.isClientSide){
                this.setShellHealth(newShellHealth);
            }
            if (newShellHealth != shellHealth && !this.isSilent()) {
                if(newShellHealth > 0f){
                    this.playSound(SoundEvents.STONE_BREAK, 1.0F, 1.0F);
                } else {
                    this.playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
                    if(!this.level.isClientSide){
                        this.shellType.spawnLoot((ServerLevel) this.level, damageSource, this, itemStack);
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

    protected void dropCustomDeathLoot(DamageSource damageSource, int looting, boolean b) {
        super.dropCustomDeathLoot(damageSource, looting, b);
        if(this.hasShell() && !this.level.isClientSide){
            this.shellType.spawnLoot((ServerLevel)this.level, damageSource, this, ItemStack.EMPTY);
        }
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
        RUBY(BlockRegistry.DEEPSLATE_RUBY_ORE::get, 0.25f),
        COAL(() -> Blocks.DEEPSLATE_COAL_ORE, 0.1f),
        COPPER(() -> Blocks.DEEPSLATE_COPPER_ORE, 0.15f),
        IRON(() -> Blocks.DEEPSLATE_IRON_ORE, 0.15f),
        LAPIS(() -> Blocks.DEEPSLATE_LAPIS_ORE, 0.15f),
        GOLD(() -> Blocks.DEEPSLATE_GOLD_ORE, 0.2f),
        SILVER(BlockRegistry.DEEPSLATE_SILVER_ORE::get, 0.2f),
        EMERALD(() -> Blocks.DEEPSLATE_EMERALD_ORE, 0.2f),
        REDSTONE(() -> Blocks.DEEPSLATE_REDSTONE_ORE, 0.2f);

        private final Lazy<Block> lazyBlock;
        private final float maxHealth;

        ShellType(Lazy<Block> lazyBlock, float healthPercentage){
            this.lazyBlock = lazyBlock;
            this.maxHealth = healthPercentage * (float) MineralLeviathanHead.BASE_HEALTH;
        }

        public float getShellMaxHealth(){
            return this.maxHealth;
        }

        public static ShellType getRandomShellType(Random random){
            ShellType[] values = ShellType.values();
            return values[random.nextInt(values.length-1)+1];
        }

        public void spawnLoot(ServerLevel serverLevel, DamageSource damageSource, Entity entity, ItemStack itemStack){
            BlockState blockState = this.lazyBlock.get().defaultBlockState();
            List<ItemStack> loot = blockState.getDrops(new LootContext.Builder(serverLevel)
                    .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                    .withParameter(LootContextParams.THIS_ENTITY, entity)
                    .withParameter(LootContextParams.BLOCK_STATE, blockState)
                    .withParameter(LootContextParams.ORIGIN, entity.position())
                    .withParameter(LootContextParams.TOOL, itemStack == null ? ItemStack.EMPTY : itemStack)
                    .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, damageSource.getDirectEntity())
                    .withOptionalParameter(LootContextParams.KILLER_ENTITY, damageSource.getEntity()));
            for(ItemStack itemStack1 : loot){
                entity.spawnAtLocation(itemStack1);
            }
        }

        public boolean canHarvest(ItemStack itemStack){
            return itemStack.isCorrectToolForDrops(this.lazyBlock.get().defaultBlockState());
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
