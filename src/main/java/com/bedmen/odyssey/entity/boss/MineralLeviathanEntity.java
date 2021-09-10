package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MineralLeviathanEntity extends MobEntity implements IMob{
    private final MineralLeviathanPartEntity head;
    private final MineralLeviathanPartEntity[] subEntities = new MineralLeviathanPartEntity[10];
    public final double[][] positions = new double[64][3];
    public int posPointer = -1;
    private static final Predicate<LivingEntity> ENTITY_SELECTOR = (entity) -> {
        return entity.attackable() && entity.getType() != EntityTypeRegistry.PERMAFROST.get();
    };
    private static final EntityPredicate TARGETING_CONDITIONS = (new EntityPredicate()).range(20.0D).selector(ENTITY_SELECTOR);
    private final ServerBossInfo bossEvent = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenScreen(true);

    public MineralLeviathanEntity(EntityType<? extends MineralLeviathanEntity> entityType, World world) {
        super(entityType, world);
        this.setHealth(this.getMaxHealth());
        this.noPhysics = true;
        this.noCulling = true;
        this.xpReward = 50;

        this.head = new MineralLeviathanPartEntity(this, "head", 2.0F, 2.0F);
        this.subEntities[0] = this.head;
        for(int i = 1; i <= 9; i++){
            this.subEntities[i] = new MineralLeviathanPartEntity(this, "body", 2.0F, 2.0F);
        }
    }

    public double[] getLatencyPos(int p_70974_1_, float p_70974_2_) {
        if (this.isDeadOrDying()) {
            p_70974_2_ = 0.0F;
        }

        p_70974_2_ = 1.0F - p_70974_2_;
        int i = this.posPointer - p_70974_1_ & 63;
        int j = this.posPointer - p_70974_1_ - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.positions[i][0];
        double d1 = MathHelper.wrapDegrees(this.positions[j][0] - d0);
        adouble[0] = d0 + d1 * (double)p_70974_2_;
        d0 = this.positions[i][1];
        d1 = this.positions[j][1] - d0;
        adouble[1] = d0 + d1 * (double)p_70974_2_;
        adouble[2] = MathHelper.lerp((double)p_70974_2_, this.positions[i][2], this.positions[j][2]);
        return adouble;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MineralLeviathanEntity.DoNothingGoal(this));
    }

    public void setCustomName(@Nullable ITextComponent p_200203_1_) {
        super.setCustomName(p_200203_1_);
        this.bossEvent.setName(this.getDisplayName());
    }

    public void aiStep() {
        super.aiStep();

        if (this.level.isClientSide) {
            this.setHealth(this.getHealth());
            if (!this.isSilent()) {
                //Player Sounds Here
            }
        }

        if(!this.isNoAi()){
            if (this.level.isClientSide) {
                //Lerp stuff
                if (this.lerpSteps > 0) {
                    double d7 = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
                    double d0 = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
                    double d1 = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
                    double d2 = MathHelper.wrapDegrees(this.lerpYRot - (double)this.yRot);
                    this.yRot = (float)((double)this.yRot + d2 / (double)this.lerpSteps);
                    this.xRot = (float)((double)this.xRot + (this.lerpXRot - (double)this.xRot) / (double)this.lerpSteps);
                    --this.lerpSteps;
                    this.setPos(d7, d0, d1);
                    this.setRot(this.yRot, this.xRot);
                }
            } else {
                //Choose Target
                List<LivingEntity> list = this.level.getNearbyEntities(LivingEntity.class, TARGETING_CONDITIONS, this, this.getBoundingBox().inflate(40.0D, 40.0D, 40.0D));
                Stream<LivingEntity> stream = list.stream().filter(livingEntity -> {return livingEntity.isAlive() && this != livingEntity;});
                list = stream.collect(Collectors.toList());
                List<LivingEntity> playerList = list.stream().filter(livingEntity -> {return livingEntity instanceof PlayerEntity && !((PlayerEntity) livingEntity).abilities.invulnerable;}).collect(Collectors.toList());
                if(!playerList.isEmpty()){
                    list = playerList;
                }
                if(this.level.getGameTime() % 10 == 9){
                    LivingEntity target = this.getTarget();
                    if(list.isEmpty()){
                        if(target != null && target.isAlive() && (!(target instanceof PlayerEntity) || !((PlayerEntity) target).abilities.invulnerable))
                            list.add(target);
                        else
                            this.setTarget(null);
                    }
                    else
                        setTarget(list.get(this.random.nextInt(list.size())));
                }
                //Movement
                LivingEntity target = this.getTarget();
                if(target != null){
                    Vector3d targetPosition = target.getPosition(1.0f);
                    Vector3d movementVector = targetPosition.subtract(this.getPosition(1.0f));
                    if(movementVector.length() > 2.0d){
                        movementVector = movementVector.normalize().scale(0.2);
                        this.setDeltaMovement(movementVector);
                    } else {
                        this.setDeltaMovement(Vector3d.ZERO);
                    }
                }
            }

            //avector3d
            this.yBodyRot = this.yRot;
            Vector3d[] avector3d = new Vector3d[this.subEntities.length];
            for(int j = 0; j < this.subEntities.length; ++j) {
                avector3d[j] = new Vector3d(this.subEntities[j].getX(), this.subEntities[j].getY(), this.subEntities[j].getZ());
            }

            //Tick Parts
            for(int i = 0; i <= 9; i++){
                if(i == 0){
                    this.head.setPos(this.getX(), this.getY(), this.getZ());
                } else {
                    Vector3d prevPartPos = this.subEntities[i-1].getPosition(1.0f);
                    if(this.subEntities[i].distanceToSqr(prevPartPos) > 4.0d){
                        Vector3d partPos = this.subEntities[i].getPosition(1.0f).subtract(prevPartPos).normalize().scale(1.95d);
                        this.subEntities[i].setPos(prevPartPos.x + partPos.x, prevPartPos.y + partPos.y, prevPartPos.z + partPos.z);
                    }
                }
            }

            //Update Old Values
            for(int l = 0; l < this.subEntities.length; ++l) {
                this.subEntities[l].xo = avector3d[l].x;
                this.subEntities[l].yo = avector3d[l].y;
                this.subEntities[l].zo = avector3d[l].z;
                this.subEntities[l].xOld = avector3d[l].x;
                this.subEntities[l].yOld = avector3d[l].y;
                this.subEntities[l].zOld = avector3d[l].z;
            }
        }
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

    public boolean hurt(MineralLeviathanPartEntity part, DamageSource damageSource, float amount) {
        return this.hurt(damageSource, amount);
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

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public net.minecraftforge.entity.PartEntity<?>[] getParts() {
        return this.subEntities;
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
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 200.0D);
    }

    static class DoNothingGoal extends Goal {
        private final MineralLeviathanEntity entity;
        private LivingEntity target;

        public DoNothingGoal(MineralLeviathanEntity entity) {
            this.entity = entity;
        }

        public boolean canUse() {
            this.target = this.entity.getTarget();
            return this.target == null;
        }

        public void tick() {
            Vector3d vector3d = this.entity.getDeltaMovement().multiply(0.6D, 0.0D, 0.6D);
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
