package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.entity.projectile.PermafrostIcicleEntity;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class PermafrostEntity extends BossEntity {
    private float damageReduction = 1.0f;
    private float activeRotation = 0;
    private int destroyBlocksTick;
    private int IciclePosition = 0;
    private final int MaxIciclePositions = 20;
    private int movementPosition = 0;
    private final int MaxMovementPositions = 6;
    private final int[] attackCooldown = new int[2];
    private final int[] attackTimer = new int[2];
    private double TARGETING_RANGE = 40d;
    private final ServerBossInfo bossEvent = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS)).setDarkenScreen(true);

    public PermafrostEntity(EntityType<? extends PermafrostEntity> entityType, World world) {
        super(entityType, world);
        this.setHealth(this.getMaxHealth());
        this.noCulling = true;
        this.xpReward = 100;
        this.destroyBlocksTick = 0;
        this.attackTimer[0] = 0;
        this.attackTimer[1] = 0;
        this.attackCooldown[0] = 0;
        this.attackCooldown[1] = 0;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PermafrostEntity.DoNothingGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public void setCustomName(@Nullable ITextComponent p_200203_1_) {
        super.setCustomName(p_200203_1_);
        this.bossEvent.setName(this.getDisplayName());
    }

    private void performSpiralAttack(LivingEntity livingEntity) {
        double targetX = livingEntity.getX();
        double targetY = livingEntity.getY() + (double)livingEntity.getEyeHeight() * 0.5D;
        double targetZ = livingEntity.getZ();
        double angle = this.IciclePosition * Math.PI / this.MaxIciclePositions * 2.0d;
        double r = 2.0d;
        double d0 = this.getX() + Math.cos(angle) * r;
        double d1 = this.getY();
        double d2 = this.getZ() + Math.sin(angle) * r;
        double d3 = targetX - d0;
        double d4 = targetY - d1;
        double d5 = targetZ - d2;
        PermafrostIcicleEntity permafrostIcicleEntity = new PermafrostIcicleEntity(this.level, this, d3, d4, d5,6.0f);
        permafrostIcicleEntity.setOwner(this);
        permafrostIcicleEntity.setPosRaw(d0, d1, d2);
        this.level.addFreshEntity(permafrostIcicleEntity);
    }

    private void performSphereAttack(int i){
        i = 4*(i+22);
        int maxJ;
        double angleI = i * Math.PI / 180.0d;
        double angleJ0 = this.random.nextFloat() * 2 * Math.PI;
        if(i == 180 || i == 0)
            maxJ = 1;
        else{
            maxJ = (int)(Math.sin(angleI) * 30.0d);
            maxJ = Math.max(maxJ, 2);
        }
        for(int j = 0; j < maxJ; j++){
            double r = 2.0d;
            double angleJ = j * Math.PI * 2.0d / (double)maxJ + angleJ0;
            double d0 = r * Math.sin(angleI) * Math.cos(angleJ);
            double d1 = r * Math.cos(angleI);
            double d2 = r * Math.sin(angleI) * Math.sin(angleJ);
            PermafrostIcicleEntity permafrostIcicleEntity = new PermafrostIcicleEntity(this.level, this, d0, d1, d2, 12.0f);
            permafrostIcicleEntity.setOwner(this);

            permafrostIcicleEntity.setPosRaw(this.getX() + d0, this.getY() + d1, this.getZ() + d2);
            this.level.addFreshEntity(permafrostIcicleEntity);
        }
    }

    public void tick(){
        super.tick();
        this.damageReduction = this.difficultyDamageReductionMultiplier() * this.nearbyPlayerDamageReductionMultiplier();
    }

    public void aiStep() {
        super.aiStep();
        ++this.activeRotation;

        //Choose Movement Position
        if(this.level.getGameTime() % 100 == 7){
            this.movementPosition = this.random.nextInt(this.MaxMovementPositions);
        }

        //Decrement Timers
        for(int i = 0; i < this.attackTimer.length; i++) {
            --this.attackCooldown[i];
            --this.attackTimer[i];
        }

        float healthMultiplier = 0.5f + 0.5f * (this.getHealth() / this.getMaxHealth());
        if(this.attackCooldown[0] <= 0) {
            int i1 = this.random.nextInt(60)+100;
            this.attackCooldown[0] = (int)(i1* healthMultiplier);
            this.attackTimer[0] = 20;
        }
        if(this.attackCooldown[1] <= 0) {
            int i1 = this.random.nextInt(200)+100;
            this.attackCooldown[1] = (int)(i1* healthMultiplier);
            this.attackTimer[1] = 27;
        }

        //Choose Target
        Collection<ServerPlayerEntity> serverPlayerEntities =  this.bossEvent.getPlayers();
        List<ServerPlayerEntity> serverPlayerEntityList = serverPlayerEntities.stream().filter(serverPlayerEntity -> {return serverPlayerEntity.isAlive() && !serverPlayerEntity.abilities.invulnerable && this.distanceToSqr(serverPlayerEntity) < TARGETING_RANGE*TARGETING_RANGE;}).collect(Collectors.toList());
        // Set Phase based on Target
        if(this.level.getGameTime() % 18 == 14){
            if(serverPlayerEntityList.isEmpty()){
                this.setTarget(null);
            }
            else {
                setTarget(serverPlayerEntityList.get(this.random.nextInt(serverPlayerEntityList.size())));
            }
        }

        //Movement
        if(this.getTarget() != null){
            Vector3d location1 = this.getPosition(1);
            double angle = this.movementPosition * Math.PI / this.MaxMovementPositions * 2.0d;
            Vector3d location2 = Vector3d.ZERO;
            for(ServerPlayerEntity serverPlayerEntity : serverPlayerEntities){
                location2 = location2.add(serverPlayerEntity.getPosition(1));
            }
            location2 = location2.scale(1.0d / serverPlayerEntities.size());
            location2 = location2.add(Math.sin(angle) * 10.0d,10.0d,Math.cos(angle) * 10.0d);
            Vector3d direction = location2.subtract(location1);
            double speed = 0.5d;
            double sl = direction.length();
            if(sl > speed){
                direction = direction.normalize().scale(speed);
            } else {
                direction.scale(0.0d);
            }
            this.setDeltaMovement(direction);


            if(this.attackTimer[0] > 0){
                int targetsAttacked = 0;
                for(ServerPlayerEntity serverPlayerEntity : serverPlayerEntityList){
                    if(targetsAttacked < 10){
                        this.performSpiralAttack(serverPlayerEntity);
                        ++targetsAttacked;
                    }
                }
                ++this.IciclePosition;
                this.IciclePosition = this.IciclePosition % this.MaxIciclePositions;
            }
            if(this.attackTimer[1] >= 1 && this.attackTimer[1] <= 23){
                 this.performSphereAttack(this.attackTimer[1]);
            }
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setPercent(this.getHealth() / this.getMaxHealth());
        if(this.destroyBlocksTick > 0){
            this.destroyBlocksTick--;
        }

        int x = MathHelper.floor(this.getX());
        int y = MathHelper.floor(this.getY());
        int z = MathHelper.floor(this.getZ());
        if(this.destroyBlocksTick <= 10) {
            for (int x1 = x - 2; x1 <= x + 2; ++x1) {
                for (int y1 = y - 1; y1 <= y + 3; ++y1) {
                    for (int z1 = z - 2; z1 <= z + 2; ++z1) {
                        BlockPos blockpos = new BlockPos(x1, y1, z1);
                        BlockState blockstate = this.level.getBlockState(blockpos);
                        Block block = blockstate.getBlock();
                        if (!block.is(BlockTags.WITHER_IMMUNE) && block != Blocks.WATER && block != Blocks.LAVA && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                            this.level.destroyBlock(blockpos, true, this);
                            this.destroyBlocksTick = 10;
                        }
                    }
                }
            }
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.CONDUIT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.GUARDIAN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.CONDUIT_DEACTIVATE;
    }

    public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
        super.addAdditionalSaveData(p_213281_1_);
    }

    public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        if (this.isInvulnerableTo(p_70097_1_))
            return false;
        if(p_70097_1_ == DamageSource.DROWN)
            return false;
        return super.hurt(p_70097_1_, p_70097_2_);
    }

    protected void dropCustomDeathLoot(DamageSource damageSource, int p_213333_2_, boolean p_213333_3_) {
        super.dropCustomDeathLoot(damageSource, p_213333_2_, p_213333_3_);
        for(int i = 0; i < 20; i++){
            ItemEntity itementity = this.spawnAtLocation(ItemRegistry.PERMAFROST_SHARD.get());
            if (itementity != null) {
                itementity.setExtendedLifetime();
            }
        }
    }

    public void startSeenByPlayer(ServerPlayerEntity p_184178_1_) {
        super.startSeenByPlayer(p_184178_1_);
        this.bossEvent.addPlayer(p_184178_1_);
    }

    public void stopSeenByPlayer(ServerPlayerEntity p_184203_1_) {
        super.stopSeenByPlayer(p_184203_1_);
        this.bossEvent.removePlayer(p_184203_1_);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 350.0D).add(Attributes.MOVEMENT_SPEED, (double)0.6F).add(Attributes.FOLLOW_RANGE, 40.0D).add(Attributes.ARMOR, 0.0D);
    }

    public ServerBossInfo getBossEvent(){
        return this.bossEvent;
    }

    public Difficulty getDifficulty(){
        return this.level.getDifficulty();
    }

    public float getDamageReduction(){
        return this.damageReduction;
    }

    @OnlyIn(Dist.CLIENT)
    public float getActiveRotation(float p_205036_1_) {
        return (this.activeRotation + p_205036_1_) * -0.0375F;
    }

    static class DoNothingGoal extends Goal {
        private final PermafrostEntity entity;
        private LivingEntity target;

        public DoNothingGoal(PermafrostEntity entity) {
            this.entity = entity;
        }

        public boolean canUse() {
            this.target = this.entity.getTarget();
            return this.target == null;
        }

        public void tick() {
            Vector3d vector3d = this.entity.getDeltaMovement().multiply(0.6D, 0.0D, 0.6D);
            int x = MathHelper.floor(this.entity.getX());
            int y = MathHelper.floor(this.entity.getY());
            int z = MathHelper.floor(this.entity.getZ());
            boolean goUpFlag = false;
            boolean floatFlag = false;
            for(int i = 0; i < 9; i++){
                BlockPos blockpos = new BlockPos(x, y-i, z);
                BlockState blockstate = this.entity.level.getBlockState(blockpos);
                if (!blockstate.isAir()){
                    goUpFlag = true;
                    break;
                }
            }
            BlockPos blockpos = new BlockPos(x, y-9, z);
            BlockState blockstate = this.entity.level.getBlockState(blockpos);
            if (!blockstate.isAir()){
                floatFlag = true;
            }
            if(goUpFlag){
                vector3d = vector3d.add(0.0d,0.5d,0.0d);
            }
            else if(floatFlag){
                vector3d = vector3d.multiply(1.0D, 0.0D, 1.0D);
            }
            else{
                vector3d = vector3d.subtract(0.0D, 0.5D, 0.0D);
            }
            this.entity.setDeltaMovement(vector3d);
        }
    }

    static class HurtByTargetGoal extends TargetGoal {

        public HurtByTargetGoal(CreatureEntity p_i50317_1_, Class<?>... p_i50317_2_) {
            super(p_i50317_1_, true);
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        public boolean canUse() {
            return true;
        }

        public void start() {
            this.mob.setTarget(this.mob.getLastHurtByMob());
            this.unseenMemoryTicks = 300;

            super.start();
        }
    }
}
