package com.bedmen.odyssey.entity.boss.mineralLeviathan;

import com.bedmen.odyssey.entity.boss.Boss;
import com.bedmen.odyssey.entity.boss.BossSubEntity;
import com.bedmen.odyssey.registry.BlockRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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

import java.util.List;
import java.util.Random;

public abstract class MineralLeviathanSegment extends BossSubEntity<MineralLeviathanMaster> {
    protected static final EntityDataAccessor<Float> DATA_SHELL_HEALTH = SynchedEntityData.defineId(MineralLeviathanSegment.class, EntityDataSerializers.FLOAT);
    protected ShellType shellType;

    public MineralLeviathanSegment(EntityType<? extends MineralLeviathanSegment> entityType, Level level) {
        super(entityType, level);
        this.setHealth(this.getMaxHealth());
        this.noPhysics = true;
        this.setNoGravity(true);
        this.noCulling = true;
        this.lookControl = new SegmentLookController(this);
        this.xpReward = 5;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SHELL_HEALTH, 0.0f);
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
                this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox())
                        .stream().filter(livingEntity -> !(livingEntity instanceof MineralLeviathanSegment || livingEntity instanceof MineralLeviathanMaster || livingEntity instanceof Silverfish))
                        .forEach(livingEntity -> livingEntity.hurt(DamageSource.mobAttack(this).setScalesWithDifficulty(), (float)this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE)));
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
        this.entityData.set(DATA_SHELL_HEALTH, f);
    }

    public float getShellHealth() {
        return this.entityData.get(DATA_SHELL_HEALTH);
    }

    public boolean hasShell(){
        return this.getShellHealth() > 0.0f;
    }

    public int getSilverFishCount(){
        int i = switch (this.level.getDifficulty()) {
            case HARD -> 6;
            case NORMAL -> 4;
            default -> 2;
        };
        return i * this.getMaster().map(Boss::getNearbyPlayerNumber).orElse(0);
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
            return this.hurtWithShellConsidered(damageSource, amount, null);
        }
        else if(damageSource == DamageSource.OUT_OF_WORLD){
            this.hurtWithoutShell(damageSource, amount);
        }
        else if(entity instanceof LivingEntity livingEntity){
            ItemStack itemStack = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);
            Item item = itemStack.getItem();
            if(item instanceof PickaxeItem && this.shellType.canHarvest(itemStack)){
                float scaledAmount = amount * 0.5f + 1f;
                if(this.getShellHealth() > 0.0f){
                    itemStack.hurtAndBreak(1, livingEntity, (livingEntity1) -> {
                        livingEntity1.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                    });
                }
                return this.hurtWithShellConsidered(damageSource, scaledAmount, itemStack);
            }
        }
        return false;
    }

    protected boolean hurtWithShellConsidered(DamageSource damageSource, float amount, ItemStack itemStack){
        float shellHealth = this.getShellHealth();
        if(shellHealth > 0.0f){
            if(this instanceof MineralLeviathanHead && damageSource.isExplosion()){
                return false;
            }
            float newShellHealth = shellHealth - amount * this.getMaster().map(Boss::getDamageReduction).orElse(1.0f);
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
                        int silverFishCount = this.getSilverFishCount();
                        for(int i = 0; i < silverFishCount; i++) {
                            Silverfish silverfishEntity = EntityType.SILVERFISH.spawn((ServerLevel) this.level, null, null, null, this.blockPosition(), MobSpawnType.REINFORCEMENT, false, false);
                            silverfishEntity.moveTo(this.getX(), this.getY(), this.getZ());
                            silverfishEntity.setDeltaMovement(new Vec3(this.random.nextDouble()*2.0d-1.0d, 0.5d, this.random.nextDouble()*2.0d-1.0d));
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
        if(this.getMaster().isPresent()) {
            MineralLeviathanMaster mineralLeviathanMaster = this.getMaster().get();
            return mineralLeviathanMaster.hurt(damageSource, amount);
        }
        return super.hurt(damageSource, amount);
    }

    protected void dropCustomDeathLoot(DamageSource damageSource, int looting, boolean killedByPlayerFlag) {
        super.dropCustomDeathLoot(damageSource, looting, killedByPlayerFlag);
        if(this.hasShell() && !this.level.isClientSide){
            this.shellType.spawnLoot((ServerLevel)this.level, damageSource, this, ItemStack.EMPTY);
        }
    }

    public void writeSpawnData(FriendlyByteBuf friendlyByteBuf) {
        super.writeSpawnData(friendlyByteBuf);
        friendlyByteBuf.writeInt(this.getShellType().ordinal());
    }

    public void readSpawnData(FriendlyByteBuf friendlyByteBuf) {
        super.readSpawnData(friendlyByteBuf);
        this.setShellType(ShellType.values()[friendlyByteBuf.readInt()]);
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
            this.maxHealth = healthPercentage * (float) MineralLeviathanMaster.MAX_HEALTH;
        }

        public float getShellMaxHealth(){
            return this.maxHealth;
        }

        public static ShellType getRandomShellType(RandomSource randomSource){
            ShellType[] values = ShellType.values();
            return values[randomSource.nextInt(values.length-1)+1];
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

    static class SegmentLookController extends LookControl {
        SegmentLookController(Mob p_i225729_2_) {
            super(p_i225729_2_);
        }

        protected boolean resetXRotOnTick() {
            return false;
        }
    }
}
