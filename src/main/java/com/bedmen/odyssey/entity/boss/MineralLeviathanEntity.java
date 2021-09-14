package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MineralLeviathanEntity extends MineralLeviathanSegmentEntity {
    private static final EntityPredicate TARGETING_CONDITIONS = (new EntityPredicate()).range(60.0D).selector(BossEntity.ENTITY_SELECTOR);
    protected static final DataParameter<List<UUID>> DATA_BODY_UUID_ID = EntityDataManager.defineId(MineralLeviathanEntity.class, OdysseyDataSerializers.UUID_LIST);
    private final ServerBossInfo bossEvent = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenScreen(true);
    private Phase phase = Phase.IDLE;
    private int passingTimer;
    private float dYRot;
    private float dXRot;
    public static final int NUM_SEGMENTS = 20;
    public static final double DAMAGE = 8.0d;
    public static final double DODGE_RANGE = 4.0d;
    public static final double BASE_HEALTH = 150.0d;
    public MineralLeviathanBodyEntity[] bodyEntities = new MineralLeviathanBodyEntity[NUM_SEGMENTS-1];

    public MineralLeviathanEntity(EntityType<? extends MineralLeviathanEntity> entityType, World world) {
        super(entityType, world);
        if(!this.level.isClientSide){
            this.setShellType(0);
            this.setShellHealth(this.getShellHealthFromType(0));
        }
    }

    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        ILivingEntityData ilivingentitydata = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
        if(this.getBodyUUIDs().isEmpty()){
            Vector3d headPosition = this.getPosition(1.0f);
            Vector3d loweringVector = new Vector3d(0.0d,-1.0d, 0.0d);
            this.setXRot(90.0f);
            for(int i = 0; i < this.bodyEntities.length; i++){
                if(i == 0){
                    this.bodyEntities[i] = new MineralLeviathanBodyEntity(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), this.level, this, this);
                } else {
                    this.bodyEntities[i] = new MineralLeviathanBodyEntity(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), this.level, this, this.bodyEntities[i-1]);
                }
                this.bodyEntities[i].moveTo(headPosition.add(loweringVector.scale(i)));
                this.bodyEntities[i].setXRot(90.0f);
                this.level.addFreshEntity(this.bodyEntities[i]);
            }
            this.setBodyUUIDs(this.bodyEntities);
            this.initBody = true;
        }
        return ilivingentitydata;
    }

    protected void registerGoals() {
        this.targetSelector.addGoal(1, new MineralLeviathanEntity.HurtByTargetGoal(this));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BODY_UUID_ID, new ArrayList<>());
    }

    public void setBodyUUIDs(MineralLeviathanBodyEntity[] bodyEntities) {
        List<UUID> uuidList = new ArrayList<>();
        for(MineralLeviathanBodyEntity bodyEntity : bodyEntities){
            uuidList.add(bodyEntity.getUUID());
        }
        this.entityData.set(DATA_BODY_UUID_ID, uuidList);
    }

    public void setBodyUUIDs(List<UUID> uuidList) {
        this.entityData.set(DATA_BODY_UUID_ID, uuidList);
    }

    public List<UUID> getBodyUUIDs() {
        return this.entityData.get(DATA_BODY_UUID_ID);
    }

    public void setCustomName(@Nullable ITextComponent p_200203_1_) {
        super.setCustomName(p_200203_1_);
        this.bossEvent.setName(this.getDisplayName());
    }

    public void aiStep() {
        //Server side Init Body
        if(!this.initBody && !this.level.isClientSide){
            List<UUID> uuidList = this.getBodyUUIDs();
            if(uuidList.size() >= NUM_SEGMENTS - 1){
                try {
                    Field entitiesByUuidField = ServerWorld.class.getDeclaredField("entitiesByUuid");
                    entitiesByUuidField.setAccessible(true);
                    Map<UUID, Entity> entitiesByUuid = (Map<UUID, Entity>) entitiesByUuidField.get(this.level);
                    for(int i = 0; i < uuidList.size(); i++){
                        this.bodyEntities[i] = (MineralLeviathanBodyEntity) entitiesByUuid.get(uuidList.get(i));
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                this.initBody = true;
            }
        }
        else if(!this.initBody){
            List<UUID> uuidList = this.getBodyUUIDs();
            if(uuidList.size() >= NUM_SEGMENTS - 1){
                List<MineralLeviathanBodyEntity> bodyEntities = this.level.getEntitiesOfClass(MineralLeviathanBodyEntity.class, this.getBoundingBox().inflate(45.0d));
                for(int i = 0; i < this.bodyEntities.length; i++){
                    UUID uuid = uuidList.get(i);
                    for(MineralLeviathanBodyEntity bodyEntity : bodyEntities){
                        if(bodyEntity.getUUID().equals(uuid)){
                            this.bodyEntities[i] = bodyEntity;
                            break;
                        }
                    }
                }
                this.initBody = true;
            }
        }

        if(!this.isNoAi()){
            if(!this.level.isClientSide){
                //Choose Target
                if(this.level.getGameTime() % 19 == 0){
                    LivingEntity target = this.getTarget();
                    List<LivingEntity> list = this.level.getNearbyEntities(LivingEntity.class, TARGETING_CONDITIONS, this, this.getBoundingBox().inflate(60.0D, 60.0D, 60.0D));
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
                            if(this.distanceTo(target) < DODGE_RANGE){
                                this.phase = Phase.PASSING;
                                this.passingTimer = 20;
                            }
                        } else {
                            this.phase = Phase.IDLE;
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
                        } else {
                            this.phase = Phase.IDLE;
                        }
                        break;
                }
            } else {
                for(MineralLeviathanBodyEntity mineralLeviathanBodyEntity : this.bodyEntities){
                    if(mineralLeviathanBodyEntity != null){
                        mineralLeviathanBodyEntity.hurtTime = this.hurtTime;
                    }
                }
            }
        }
        super.aiStep();
    }

    protected void moveTowards(Vector3d vector3d, double acceleration){
        this.setDeltaMovement(vector3d.normalize().scale(acceleration).add(this.getDeltaMovement()));
        this.setRotation(this.getDeltaMovement());
    }

    protected void rotateTowards(float dYRot, float dXRot, double speed){
        this.setYRot(this.getYRot() + dYRot);
        this.setXRot(this.getXRot() + dXRot);
        float yRotRadians = this.getYRot() * (float)Math.PI / 180f;
        float xRotRadians = this.getXRot() * (float)Math.PI / 180f;
        double x = MathHelper.sin(yRotRadians) * MathHelper.cos(xRotRadians) * speed;
        double y = MathHelper.sin(xRotRadians) * speed;
        double z = MathHelper.cos(yRotRadians) * MathHelper.cos(xRotRadians) * speed;
        this.setDeltaMovement(new Vector3d(x,y,z));
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

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        List<UUID> uuidList = this.getBodyUUIDs();
        for(int i = 0; i < uuidList.size(); i++){
            compoundNBT.putUUID("BodyUUID"+i, uuidList.get(i));
        }
        compoundNBT.putString("Phase", this.phase.toString());
        compoundNBT.putInt("PassingTimer", this.passingTimer);
        compoundNBT.putFloat("DYRot", this.dYRot);
        compoundNBT.putFloat("DXRot", this.dXRot);
    }

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
        List<UUID> uuidList = new ArrayList<>();
        for(int i = 0; i < NUM_SEGMENTS-1; i++){
            if (compoundNBT.hasUUID("BodyUUID"+i)) {
                uuidList.add(compoundNBT.getUUID("BodyUUID"+i));
            }
        }
        this.setBodyUUIDs(uuidList);
        if(compoundNBT.contains("Phase")){
            this.phase = Phase.valueOf(compoundNBT.getString("Phase"));
        } else {
            this.phase = Phase.IDLE;
        }
        this.passingTimer = compoundNBT.getInt("PassingTimer");
        this.dYRot = compoundNBT.getFloat("DYRot");
        this.dXRot = compoundNBT.getFloat("DXRot");
    }

    protected void dropCustomDeathLoot(DamageSource damageSource, int p_213333_2_, boolean p_213333_3_) {
        super.dropCustomDeathLoot(damageSource, p_213333_2_, p_213333_3_);
        ItemEntity itementity = this.spawnAtLocation(ItemRegistry.RUBY.get());
        if (itementity != null) {
            itementity.setExtendedLifetime();
        }
    }

    public void die(DamageSource damageSource) {
        if(!this.level.isClientSide){
            for(MineralLeviathanBodyEntity mineralLeviathanBodyEntity : this.bodyEntities){
                mineralLeviathanBodyEntity.hurt(DamageSource.OUT_OF_WORLD, 1.1f);
            }
        }
        super.die(damageSource);
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
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, BASE_HEALTH).add(Attributes.ATTACK_DAMAGE, DAMAGE);
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
            LivingEntity livingEntity = this.mob.getLastHurtByMob();
            if(livingEntity != null && livingEntity.isAlive() && livingEntity.isAttackable() && (!(livingEntity instanceof PlayerEntity) || !(((PlayerEntity) livingEntity).abilities.instabuild || ((PlayerEntity) livingEntity).abilities.invulnerable))){
                this.mob.setTarget(livingEntity);
            }
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
