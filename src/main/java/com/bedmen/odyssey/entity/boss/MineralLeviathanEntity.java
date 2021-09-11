package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.entity.IRotationallyIncompetent;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.UpdateEntityRotationPacket;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
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
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MineralLeviathanEntity extends MonsterEntity implements IRotationallyIncompetent {
    private static final Predicate<LivingEntity> ENTITY_SELECTOR = (entity) -> {
        return entity.attackable() && !(entity instanceof MonsterEntity);
    };
    private static final EntityPredicate TARGETING_CONDITIONS = (new EntityPredicate()).range(40.0D).selector(ENTITY_SELECTOR);
    private final ServerBossInfo bossEvent = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenScreen(true);
    private float trueYRot;
    private float trueXRot;
    private Phase phase = Phase.IDLE;
    private int passingTimer;
    private float dYRot;
    private float dXRot;

    public MineralLeviathanEntity(EntityType<? extends MineralLeviathanEntity> entityType, World world) {
        super(entityType, world);
        this.setHealth(this.getMaxHealth());
        this.noPhysics = true;
        this.setNoGravity(true);
        this.xpReward = 50;
    }

    protected void registerGoals() {
//        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, true));
//        this.goalSelector.addGoal(1, new MineralLeviathanEntity.IdleGoal(this));
//        this.goalSelector.addGoal(1, new MineralLeviathanEntity.ChargingGoal(this));
//        this.goalSelector.addGoal(1, new MineralLeviathanEntity.PassingGoal(this));
//        this.goalSelector.addGoal(1, new MineralLeviathanEntity.LoopingGoal(this));
    }

    public void setCustomName(@Nullable ITextComponent p_200203_1_) {
        super.setCustomName(p_200203_1_);
        this.bossEvent.setName(this.getDisplayName());
    }

    public void tick() {
        this.setNoGravity(true);
        this.noPhysics = true;
        super.tick();
    }

    public void aiStep() {
        if (this.level.isClientSide) {
            if (!this.isSilent()) {
                //Player Sounds Here
            }
        }

        if(!this.isNoAi()){
            if(!this.level.isClientSide){
                //Choose Target
                if(this.level.getGameTime() % 19 == 0){
                    LivingEntity target = this.getTarget();
                    List<LivingEntity> list = this.level.getNearbyEntities(LivingEntity.class, TARGETING_CONDITIONS, this, this.getBoundingBox().inflate(40.0D, 40.0D, 40.0D));
                    if(target != null && target.isAttackable()){
                        list.add(target);
                    }
                    Stream<LivingEntity> stream = list.stream().filter(livingEntity -> {return livingEntity.isAlive() && this != livingEntity;});
                    list = stream.collect(Collectors.toList());
                    List<LivingEntity> playerList = list.stream().filter(livingEntity -> {return livingEntity instanceof PlayerEntity && !((PlayerEntity) livingEntity).abilities.invulnerable;}).collect(Collectors.toList());
                    if(!playerList.isEmpty()){
                        list = playerList;
                    }
                    // Set Phase based on Target
                    if(list.isEmpty()){
                        this.setTarget(null);
                        this.phase = Phase.IDLE;
                    } else if(this.phase == Phase.IDLE || this.phase == Phase.PASSING) {
                        setTarget(list.get(this.random.nextInt(list.size())));
                        if(this.phase == Phase.IDLE){
                            this.phase = Phase.LOOPING;
                        }
                    }
                }
                //Movement
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
                LivingEntity target = this.getTarget();
                switch(this.phase){

                    case IDLE:
                        if(this.random.nextInt(80) == 0){
                            this.dYRot = (this.random.nextFloat() - 0.5f) * 4.0f;
                            this.dXRot = (this.random.nextFloat() - 0.5f) * 4.0f;
                        }
                        this.rotateTowards(this.dYRot, this.dXRot, 0.1d);
                        break;

                    case CHARGING:
                        if(target != null){
                            this.moveTowards(target.getPosition(1.0f).subtract(this.getPosition(1.0f)), 0.3d);
                            if(this.distanceToSqr(target) < 9.0d){
                                this.phase = Phase.PASSING;
                                this.passingTimer = 20;
                            }
                        }
                        break;

                    case PASSING:
                        this.passingTimer--;
                        this.moveTowards(this.getDeltaMovement(), 0.3d);
                        if(this.passingTimer <= 0){
                            this.phase = Phase.LOOPING;
                        }
                        break;

                    case LOOPING:
                        if(target != null){
                            Vector3d movementVector = this.getDeltaMovement();
                            Vector3d targetVector = target.getPosition(1.0f).subtract(this.getPosition(1.0f));
                            double angle = Math.acos(Math.min(1.0d, movementVector.dot(targetVector) / movementVector.length() / targetVector.length()));
                            if(angle < Math.PI / 8.0d){
                                this.phase = Phase.CHARGING;
                            } else if(this.distanceToSqr(target) < 9.0d){
                                this.phase = Phase.PASSING;
                                this.passingTimer = 20;
                            }
                            this.rotateTowards(getdYRot(movementVector, targetVector), getdXRot(movementVector, targetVector), 0.3d);
                        }
                        break;
                }
                //Damage
                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(this.getX()-1.0d,this.getY(),this.getZ()-1.0d,this.getX()+1.0d,this.getY()+2.0d,this.getZ()+1.0d);
                List<LivingEntity> livingEntityList =  this.level.getEntitiesOfClass(LivingEntity.class, axisAlignedBB);
                for(LivingEntity livingEntity : livingEntityList){
                    if(livingEntity != this){
                        livingEntity.hurt(DamageSource.mobAttack(this), 5.0f);
                    }
                }
            }
        }
        super.aiStep();
    }

    protected boolean isInBlock(){
        BlockPos blockPos = new BlockPos(this.getX(), this.getY(), this.getZ());
        return !this.level.getBlockState(blockPos).isAir();
    }

    protected void moveTowards(Vector3d vector3d, double acceleration){
        this.setDeltaMovement(vector3d.normalize().scale(acceleration).add(this.getDeltaMovement()));
        this.setRotation(this.getDeltaMovement());
        OdysseyNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new UpdateEntityRotationPacket(this.trueYRot, this.trueXRot, this.getId()));
    }

    protected void rotateTowards(float dYRot, float dXRot, double speed){
        this.trueYRot += dYRot;
        this.trueXRot += dXRot;
        float yRotRadians = this.trueYRot * (float)Math.PI / 180f;
        float xRotRadians = this.trueXRot * (float)Math.PI / 180f;
        double x = MathHelper.sin(yRotRadians) * MathHelper.cos(xRotRadians) * speed;
        double y = MathHelper.sin(xRotRadians) * speed;
        double z = MathHelper.cos(yRotRadians) * MathHelper.cos(xRotRadians) * speed;
        this.setDeltaMovement(new Vector3d(x,y,z));
        OdysseyNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new UpdateEntityRotationPacket(this.trueYRot, this.trueXRot, this.getId()));
    }

    protected void setRotation(Vector3d vector3d) {
        float f = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(vector3d));
        if (vector3d.lengthSqr() != 0.0D) {
            this.trueYRot = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
            this.trueXRot = (float)(MathHelper.atan2(f, vector3d.y) * (double)(180F / (float)Math.PI) * -1.0f + 90.0f);
        }
    }

    protected static float getdYRot(Vector3d movementVector, Vector3d targetVector){
        float theta = (float)(MathHelper.atan2(movementVector.x, movementVector.z) * (double)(180F / (float)Math.PI));
        float phi = (float)(MathHelper.atan2(targetVector.x, targetVector.z) * (double)(180F / (float)Math.PI));
        float angleDifference = MathHelper.abs(theta - phi);
        boolean flag1 = angleDifference < 180f;
        angleDifference = Math.min(5.0f, angleDifference);
        boolean flag2 = theta > phi;
        return flag1 ^ flag2 ? angleDifference : -angleDifference;
    }

    protected static float getdXRot(Vector3d movementVector, Vector3d targetVector){
        float theta = (float)(MathHelper.atan2(MathHelper.sqrt(Entity.getHorizontalDistanceSqr(movementVector)), movementVector.y) * (double)(180F / (float)Math.PI) * -1.0f + 90.0f);
        float phi = (float)(MathHelper.atan2(MathHelper.sqrt(Entity.getHorizontalDistanceSqr(targetVector)), targetVector.y) * (double)(180F / (float)Math.PI) * -1.0f + 90.0f);
        float angleDifference = MathHelper.abs(theta - phi);
        boolean flag1 = angleDifference < 180f;
        angleDifference = Math.min(5.0f, angleDifference);
        boolean flag2 = theta > phi;
        return flag1 ^ flag2 ? angleDifference : -angleDifference;
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

    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setPercent(this.getHealth() / this.getMaxHealth());
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SILVERFISH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.SILVERFISH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
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

    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.remove();
        } else {
            this.noActionTime = 0;
        }
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        if (this.isInvulnerableTo(damageSource) || amount < 0.01f)
            return false;
        if(damageSource == DamageSource.DROWN)
            return false;
        return super.hurt(damageSource, amount);
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    protected void dropCustomDeathLoot(DamageSource damageSource, int p_213333_2_, boolean p_213333_3_) {
        super.dropCustomDeathLoot(damageSource, p_213333_2_, p_213333_3_);
        ItemEntity itementity = this.spawnAtLocation(ItemRegistry.RUBY.get());
        if (itementity != null) {
            itementity.setExtendedLifetime();
        }
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEFINED;
    }

    public boolean canChangeDimensions() {
        return false;
    }

    protected boolean canRide(Entity p_184228_1_) {
        return false;
    }

    public boolean addEffect(EffectInstance p_195064_1_) {
        return false;
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
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 200.0D).add(Attributes.ATTACK_DAMAGE, 5.0d);
    }

    static class IdleGoal extends Goal {
        private final MineralLeviathanEntity entity;
        private float dXRot;
        private float dYRot;

        public IdleGoal(MineralLeviathanEntity entity) {
            this.entity = entity;
        }

        public boolean canUse() {
            return this.entity.phase == Phase.IDLE;
        }

        public void tick() {
            if(this.entity.random.nextInt(80) == 0){
                this.dYRot = (this.entity.random.nextFloat() - 0.5f) * 4.0f;
                this.dXRot = (this.entity.random.nextFloat() - 0.5f) * 4.0f;
            }
            this.entity.rotateTowards(this.dYRot, this.dXRot, 0.1d);
        }
    }

    static class ChargingGoal extends Goal {
        private final MineralLeviathanEntity entity;
        private LivingEntity target;

        public ChargingGoal(MineralLeviathanEntity entity) {
            this.entity = entity;
            this.target = entity.getTarget();
        }

        public boolean canUse() {
            return this.entity.phase == Phase.CHARGING && this.target != null;
        }

        public void tick() {
            this.entity.moveTowards(this.target.getPosition(1.0f).subtract(this.entity.getPosition(1.0f)), 0.3d);
            if(this.entity.distanceToSqr(this.target) < 9.0d){
                this.entity.phase = Phase.PASSING;
                this.entity.passingTimer = 20;
            }
        }
    }

    static class PassingGoal extends Goal {
        private final MineralLeviathanEntity entity;

        public PassingGoal(MineralLeviathanEntity entity) {
            this.entity = entity;
        }

        public boolean canUse() {
            return this.entity.phase == Phase.PASSING;
        }

        public void tick() {
            this.entity.passingTimer--;
            this.entity.moveTowards(this.entity.getDeltaMovement(), 0.25d);
            if(this.entity.passingTimer <= 0){
                this.entity.phase = Phase.LOOPING;
            }
        }
    }

    static class LoopingGoal extends Goal {
        private final MineralLeviathanEntity entity;
        private LivingEntity target;

        public LoopingGoal(MineralLeviathanEntity entity) {
            this.entity = entity;
            this.target = entity.getTarget();
        }

        public boolean canUse() {
            return this.entity.phase == Phase.CHARGING && this.target != null;
        }

        public void tick() {
            Vector3d movement = this.entity.getDeltaMovement();
            Vector3d target = this.target.getPosition(1.0f).subtract(this.entity.getPosition(1.0f));
            double angle = Math.acos(Math.max(1.0d, movement.dot(target) / movement.length() / target.length()));
            if(angle < Math.PI / 4.0d){
                this.entity.phase = Phase.CHARGING;
            }
            float theta = (float)(MathHelper.atan2(movement.x, movement.z) * (double)(180F / (float)Math.PI));
            float phi = (float)(MathHelper.atan2(target.x, target.z) * (double)(180F / (float)Math.PI));
            boolean flag1 = MathHelper.abs(theta - phi) < 180f;
            boolean flag2 = theta > phi;
            float dYRot = flag1 ^ flag2 ? 3.0f : -3.0f;
            theta = (float)(MathHelper.atan2(MathHelper.sqrt(Entity.getHorizontalDistanceSqr(movement)), movement.y) * (double)(180F / (float)Math.PI) * -1.0f + 90.0f);
            phi = (float)(MathHelper.atan2(MathHelper.sqrt(Entity.getHorizontalDistanceSqr(target)), target.y) * (double)(180F / (float)Math.PI) * -1.0f + 90.0f);
            flag1 = MathHelper.abs(theta - phi) < 180f;
            flag2 = theta > phi;
            float dXRot = flag1 ^ flag2 ? 3.0f : -3.0f;
            this.entity.rotateTowards(dYRot, dXRot, 0.2d);
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

    enum Phase {
        IDLE,
        CHARGING,
        PASSING,
        LOOPING;
    }
}
