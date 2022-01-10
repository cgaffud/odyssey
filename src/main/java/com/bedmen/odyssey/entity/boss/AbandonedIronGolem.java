package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.entity.projectile.SonicBoom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class AbandonedIronGolem extends Boss {
    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
    private float damageReduction = 1.0f;
    private int attackAnimationTick;
    private int clapCooldown = 0;
    private final float CLAP_CHANCE = 0.015f;
    private final int CLAP_COOLDOWN_MAX = 100;
    private final int CLAP_DURATION = 10;
    private final double SONIC_BOOM_RANGE = 20d;
    protected static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(AbandonedIronGolem.class, EntityDataSerializers.INT);

    public AbandonedIronGolem(EntityType<? extends Monster> p_i48576_1_, Level p_i48576_2_) {
        super(p_i48576_1_, p_i48576_2_);
        this.maxUpStep = 1.0F;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new AbandonedIronGolemMeleeAttackGoal(this));
        this.goalSelector.addGoal(2, new AbandonedIronGolemMoveTowardsTargetGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, null));
    }

    public void setCustomName(@Nullable Component p_200203_1_) {
        super.setCustomName(p_200203_1_);
        this.bossEvent.setName(this.getDisplayName());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PHASE, 0);
    }

    public void setPhase(AbandonedIronGolem.Phase phase){
        this.entityData.set(PHASE, phase.ordinal());
    }

    public AbandonedIronGolem.Phase getPhase(){
        return AbandonedIronGolem.Phase.values()[this.entityData.get(PHASE)];
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 100.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.ATTACK_DAMAGE, 12.0D).add(Attributes.FOLLOW_RANGE, 25.0D);
    }

    protected void doPush(Entity pEntity) {
        if (pEntity instanceof Enemy && !(pEntity instanceof Creeper) && this.getRandom().nextInt(20) == 0) {
            this.setTarget((LivingEntity)pEntity);
        }

        super.doPush(pEntity);
    }

    public void tick(){
        super.tick();
        this.damageReduction = this.difficultyDamageReductionMultiplier() * this.nearbyPlayerDamageReductionMultiplier();
    }

    public void aiStep() {
        super.aiStep();
        if (this.attackAnimationTick > 0) {
            --this.attackAnimationTick;
        }
        if(this.clapCooldown > 0){
            --this.clapCooldown;
        }

        if(!this.isNoAi()){
            if(!this.level.isClientSide){
                LivingEntity target = this.getTarget();
                switch(this.getPhase()){
                    case NORMAL:
                        if(target != null && this.clapCooldown <= 0 && this.random.nextFloat() < CLAP_CHANCE) {
                            this.setPhase(Phase.CLAPPING);
                            this.clapCooldown = CLAP_COOLDOWN_MAX;
                            this.level.broadcastEntityEvent(this, (byte) 5);
                        }
                        break;
                    case CLAPPING:
                        if(this.clapCooldown <= CLAP_COOLDOWN_MAX-CLAP_DURATION){
                            this.setPhase(Phase.NORMAL);
                        } else if(this.clapCooldown == CLAP_COOLDOWN_MAX - CLAP_DURATION/2 && target != null && this.distanceTo(target) <= SONIC_BOOM_RANGE){
                            this.performSonicBoom();
                        }
                        break;
                }
            }
        }

        if (this.getDeltaMovement().horizontalDistanceSqr() > (double)2.5000003E-7F && this.random.nextInt(5) == 0) {
            int i = Mth.floor(this.getX());
            int j = Mth.floor(this.getY() - (double)0.2F);
            int k = Mth.floor(this.getZ());
            BlockPos pos = new BlockPos(i, j, k);
            BlockState blockstate = this.level.getBlockState(pos);
            if (!blockstate.isAir()) {
                this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(pos), this.getX() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), this.getY() + 0.1D, this.getZ() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), 4.0D * ((double)this.random.nextFloat() - 0.5D), 0.5D, ((double)this.random.nextFloat() - 0.5D) * 4.0D);
            }
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    private void performSonicBoom() {
        List<Player> playerList = this.level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(SONIC_BOOM_RANGE));
        for(Player player : playerList){
            double targetX = player.getX();
            double targetY = player.getY();
            double targetZ = player.getZ();
            double angle = (double)this.getYRot() * Math.PI / 180d;
            double r = 14d/8d;
            double d0 = this.getX() - Math.sin(angle) * r;
            double d1 = this.getY() + 31d/16d;
            double d2 = this.getZ() + Math.cos(angle) * r;
            double d3 = targetX - d0;
            double d4 = targetY - d1;
            double d5 = targetZ - d2;
            SonicBoom sonicBoom = new SonicBoom(this.level, this, d3, d4, d5,6.0f);
            sonicBoom.setOwner(this);
            sonicBoom.setPosRaw(d0, d1, d2);
            this.level.addFreshEntity(sonicBoom);
        }
    }

    public boolean doHurtTarget(Entity pEntity) {
        this.attackAnimationTick = 10;
        this.level.broadcastEntityEvent(this, (byte)4);
        float f = this.getAttackDamage();
        boolean flag = pEntity.hurt(DamageSource.mobAttack(this).setScalesWithDifficulty(), f);
        if (flag) {
            pEntity.setDeltaMovement(pEntity.getDeltaMovement().add(0.0D, (double)0.4F, 0.0D));
            this.doEnchantDamageEffects(this, pEntity);
        }

        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        if(damageSource.isExplosion()){
            return false;
        }
        if(damageSource.isProjectile()){
            amount *= 0.5f;
        }
        IronGolem.Crackiness irongolementity$cracks = this.getCrackiness();
        boolean flag = super.hurt(damageSource, amount);
        if (flag && this.getCrackiness() != irongolementity$cracks) {
            this.playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
        }

        return flag;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte b) {
        switch(b){
            case 4:
                this.attackAnimationTick = 10;
                this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
                break;
            case 5:
                this.clapCooldown = CLAP_DURATION;
                break;
            default:
                super.handleEntityEvent(b);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getAttackAnimationTick() {
        return this.attackAnimationTick;
    }

    @OnlyIn(Dist.CLIENT)
    public int getClapCooldown() {
        return this.clapCooldown;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    public void die(DamageSource pCause) {
        super.die(pCause);
    }

    protected void dropCustomDeathLoot(DamageSource damageSource, int p_213333_2_, boolean p_213333_3_) {
        super.dropCustomDeathLoot(damageSource, p_213333_2_, p_213333_3_);
        int i = 5 + this.random.nextInt(6);
        int j = 5 + this.random.nextInt(6);
        for(int i1 = 0; i1 < i; i1++){
            ItemEntity itementity = this.spawnAtLocation(Items.IRON_INGOT);
            if (itementity != null) {
                itementity.setExtendedLifetime();
            }
        }
        for(int j1 = 0; j1 < j; j1++){
            ItemEntity itementity = this.spawnAtLocation(Items.RAW_IRON);
            if (itementity != null) {
                itementity.setExtendedLifetime();
            }
        }
    }

    public IronGolem.Crackiness getCrackiness() {
        return IronGolem.Crackiness.byFraction(this.getHealth() / this.getMaxHealth());
    }

    public ServerBossEvent getBossEvent(){
        return this.bossEvent;
    }

    public Difficulty getDifficulty(){
        return this.level.getDifficulty();
    }

    public float getDamageReduction(){
        return this.damageReduction;
    }

    public void startSeenByPlayer(ServerPlayer p_184178_1_) {
        super.startSeenByPlayer(p_184178_1_);
        this.bossEvent.addPlayer(p_184178_1_);
    }

    public void stopSeenByPlayer(ServerPlayer p_184203_1_) {
        super.stopSeenByPlayer(p_184203_1_);
        this.bossEvent.removePlayer(p_184203_1_);
    }

    enum Phase {
        NORMAL,
        CLAPPING
    }

    static class AbandonedIronGolemMoveTowardsTargetGoal extends MoveTowardsTargetGoal{
        protected final AbandonedIronGolem abandonedIronGolem;

        public AbandonedIronGolemMoveTowardsTargetGoal(AbandonedIronGolem abandonedIronGolem) {
            super(abandonedIronGolem, 0.9D, 32F);
            this.abandonedIronGolem = abandonedIronGolem;
        }

        public boolean canUse() {
            return this.abandonedIronGolem.getPhase() == Phase.NORMAL && super.canUse();
        }

        public boolean canContinueToUse() {
            return this.abandonedIronGolem.getPhase() == Phase.NORMAL && super.canContinueToUse();
        }
    }

    static class AbandonedIronGolemMeleeAttackGoal extends MeleeAttackGoal{
        protected final AbandonedIronGolem abandonedIronGolem;

        public AbandonedIronGolemMeleeAttackGoal(AbandonedIronGolem abandonedIronGolem) {
            super(abandonedIronGolem, 1.0D, true);
            this.abandonedIronGolem = abandonedIronGolem;
        }

        public boolean canUse() {
            return this.abandonedIronGolem.getPhase() == Phase.NORMAL && super.canUse();
        }

        public boolean canContinueToUse() {
            return this.abandonedIronGolem.getPhase() == Phase.NORMAL && super.canContinueToUse();
        }
    }
}
