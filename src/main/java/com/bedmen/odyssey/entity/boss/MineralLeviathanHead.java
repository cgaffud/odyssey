package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.SoundEventRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MineralLeviathanHead extends MineralLeviathanSegment {
    protected static final EntityDataAccessor<List<Integer>> DATA_BODY_ID = SynchedEntityData.defineId(MineralLeviathanHead.class, OdysseyDataSerializers.INT_LIST);
    protected static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(MineralLeviathanHead.class, EntityDataSerializers.INT);
    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
    private int passingTimer;
    private Vec3 randomTargetVelocity = new Vec3(this.random.nextDouble()*2d-1d, this.random.nextDouble()*2d-2d, this.random.nextDouble()*2d-1d);
    public static final int NUM_SEGMENTS = 20;
    public static final double DAMAGE = 8.0d;
    public static final double FOLLOW_RANGE = 75d;
    public static final double DODGE_RANGE = 3.5d;
    public static final double BASE_HEALTH = 150.0d;
    public MineralLeviathanBody[] bodyEntities = new MineralLeviathanBody[NUM_SEGMENTS-1];
    public UUID[] bodyEntityUUIDs = new UUID[NUM_SEGMENTS-1];
    private int mouthAngleTimer;
    private Phase phaseO = Phase.IDLE;
    private float mouthAngle;
    private float mouthAngleO;

    public MineralLeviathanHead(EntityType<? extends MineralLeviathanHead> entityType, Level world) {
        super(entityType, world);
        this.setShellType(ShellType.RUBY);
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_213386_1_, DifficultyInstance p_213386_2_, MobSpawnType p_213386_3_, @Nullable SpawnGroupData p_213386_4_, @Nullable CompoundTag p_213386_5_) {
        SpawnGroupData ilivingentitydata = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
        if(this.bodyEntityUUIDs[0] == null){
            Vec3 headPosition = this.getPosition(1.0f);
            Vec3 loweringVector = new Vec3(0.0d,-1.0d, 0.0d);
            this.setXRot(90.0f);
            for(int i = 0; i < this.bodyEntities.length; i++){
                if(i == 0){
                    this.bodyEntities[i] = new MineralLeviathanBody(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), this.level, this, this);
                } else {
                    this.bodyEntities[i] = new MineralLeviathanBody(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), this.level, this, this.bodyEntities[i-1]);
                }
                this.bodyEntities[i].moveTo(headPosition.add(loweringVector.scale(i)));

                this.bodyEntities[i].setXRot(90.0f);
                this.bodyEntityUUIDs[i] = this.bodyEntities[i].getUUID();
                this.level.addFreshEntity(this.bodyEntities[i]);
            }
            this.setBodyIDs(this.bodyEntities);
            this.initBody = true;
        }
        return ilivingentitydata;
    }

    protected void registerGoals() {
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BODY_ID, new ArrayList<>());
        this.entityData.define(PHASE, 0);
    }

    public void setBodyIDs(MineralLeviathanBody[] bodyEntities) {
        List<Integer> idList = new ArrayList<>();
        for(MineralLeviathanBody bodyEntity : bodyEntities){
            if(bodyEntity != null){
                idList.add(bodyEntity.getId());
            }
        }
        this.entityData.set(DATA_BODY_ID, idList);
    }

    public List<Integer> getBodyIDs() {
        return this.entityData.get(DATA_BODY_ID);
    }

    public void setPhase(Phase phase){
        this.entityData.set(PHASE, phase.ordinal());
    }

    public Phase getPhase(){
        return Phase.values()[this.entityData.get(PHASE)];
    }

    public float getMouthAngle(){
        return this.mouthAngle;
    }

    public float getMouthAngleO(){
        return this.mouthAngleO;
    }

    public void adjustMouthAngle(float f){
        this.mouthAngleO = mouthAngle;
        float alpha = (float)this.mouthAngleTimer / 10f;
        this.mouthAngle = this.mouthAngle * (1f-alpha) + f * alpha;
    }




    public void aiStep() {
        //Server side Init Body
        if(!this.initBody && !this.level.isClientSide && this.bodyEntityUUIDs[0] != null){
            ServerLevel serverWorld = (ServerLevel) this.level;
            for(int i = 0; i < this.bodyEntityUUIDs.length; i++){
                this.bodyEntities[i] = (MineralLeviathanBody) serverWorld.getEntity(this.bodyEntityUUIDs[i]);
            }
            this.setBodyIDs(this.bodyEntities);
            this.initBody = true;
        }
        //Client side Init Body
        else if(!this.initBody){
            List<Integer> idList = this.getBodyIDs();
            if(idList.size() >= NUM_SEGMENTS - 1){
                for(int i = 0; i < this.bodyEntities.length; i++){
                    this.bodyEntities[i] = (MineralLeviathanBody) this.level.getEntity(idList.get(i));
                }
                this.initBody = true;
            }
        }

        if(!this.isNoAi()){
            if(!this.level.isClientSide){
                Phase phase = this.getPhase();
                //Choose Target
                if(this.level.getGameTime() % 19 == 0){
                    Collection<ServerPlayer> serverPlayerEntities =  this.bossEvent.getPlayers();
                    List<ServerPlayer> serverPlayerEntityList = serverPlayerEntities.stream().filter(this::validTargetPredicate).collect(Collectors.toList());
                    // Set Phase based on Target
                    if(serverPlayerEntityList.isEmpty()){
                        this.setTarget(null);
                        this.setPhase(Phase.IDLE);
                    } else if(phase == Phase.IDLE || phase == Phase.PASSING) {
                        this.setTarget(serverPlayerEntityList.get(this.random.nextInt(serverPlayerEntityList.size())));
                        if(phase == Phase.IDLE){
                            this.setPhase(Phase.LOOPING);
                        }
                    }
                }

                //Movement
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
                LivingEntity target = this.getTarget();
                switch(phase){

                    case IDLE:
                        if(this.random.nextInt(80) == 0){
                            this.randomTargetVelocity = new Vec3(this.random.nextDouble()*2d-1d, this.random.nextDouble()*2d-2d, this.random.nextDouble()*2d-1d);
                        }
                        this.rotateTowards(this.randomTargetVelocity, 0.1d, 0.002d);
                        break;

                    case CHARGING:
                        if(target != null){
                            this.moveTowards(target.getPosition(1.0f).subtract(this.getPosition(1.0f)), 0.3d);
                            if(this.distanceTo(target) < DODGE_RANGE){
                                this.setPhase(Phase.PASSING);
                                this.passingTimer = 20;
                            }
                        } else {
                            this.setPhase(Phase.IDLE);
                        }
                        break;

                    case PASSING:
                        this.passingTimer--;
                        this.moveTowards(this.getDeltaMovement(), 0.3d);
                        if(this.passingTimer <= 0){
                            this.setPhase(Phase.LOOPING);
                        }
                        break;

                    case LOOPING:
                        if(target != null){
                            Vec3 movementVector = this.getDeltaMovement();
                            Vec3 targetVector = target.getPosition(1.0f).subtract(this.getPosition(1.0f));
                            double angle = Math.acos(Math.min(1.0d, movementVector.dot(targetVector) / movementVector.length() / targetVector.length()));
                            if(angle < Math.PI / 8.0d){
                                this.setPhase(Phase.CHARGING);
                            } else if(this.distanceToSqr(target) < DODGE_RANGE*DODGE_RANGE){
                                this.setPhase(Phase.PASSING);
                                this.passingTimer = 20;
                            }
                            this.rotateTowards(target.getPosition(1.0f).subtract(this.getPosition(1.0f)), 0.3d, 0.02d);
                        } else {
                            this.setPhase(Phase.IDLE);
                        }
                        break;
                }
            //Client Side
            } else {
                for(MineralLeviathanBody mineralLeviathanBody : this.bodyEntities){
                    if(mineralLeviathanBody != null){
                        mineralLeviathanBody.hurtTime = this.hurtTime;
                    }
                }

                Phase phase = this.getPhase();
                if(phase != this.phaseO){
                    this.mouthAngleTimer = 0;
                } else {
                    this.mouthAngleTimer = Integer.min(this.mouthAngleTimer + 1, 10);
                }
                this.phaseO = phase;

                switch(phase){
                    case IDLE:
                        this.adjustMouthAngle((Mth.sin((float) (Math.PI * this.tickCount / 20f))+0.5f)*50f);
                        break;
                    case CHARGING:
                        this.adjustMouthAngle((Mth.sin((float) (Math.PI * this.tickCount / 10f))+0.5f)*50f);
                        break;
                    default:
                        this.adjustMouthAngle(-25f);
                        break;
                }
            }
            BlockState blockState = this.level.getBlockState(this.blockPosition());
            if(!blockState.isAir()){
                this.playSound(blockState.getSoundType().getBreakSound(), 4.0F, this.random.nextFloat()*0.2f+0.5f);
            }
        }
        super.aiStep();
    }

    protected void moveTowards(Vec3 vector3d, double acceleration){
        this.setDeltaMovement(vector3d.normalize().scale(acceleration).add(this.getDeltaMovement()));
        this.setRotation(this.getDeltaMovement());
    }

    protected void rotateTowards(Vec3 vector3d, double acceleration, double rotationRate){
        this.setDeltaMovement(vector3d.normalize().scale(rotationRate).add(this.getDeltaMovement()).normalize().scale(acceleration));
        this.setRotation(this.getDeltaMovement());
    }

    protected SoundEvent getAmbientSound() {
        return SoundEventRegistry.MINERAL_LEVIATHAN_ROAR.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEventRegistry.MINERAL_LEVIATHAN_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return SoundEventRegistry.MINERAL_LEVIATHAN_DEATH.get();
    }

    protected float getSoundVolume() {
        return 3.0F;
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        for(int i = 0; i < this.bodyEntityUUIDs.length; i++){
            compoundNBT.putUUID("BodyUUID"+i, this.bodyEntityUUIDs[i]);
        }
        compoundNBT.putString("Phase", this.getPhase().name());
        compoundNBT.putInt("PassingTimer", this.passingTimer);
        compoundNBT.putDouble("randomTargetVelocityX", this.randomTargetVelocity.x);
        compoundNBT.putDouble("randomTargetVelocityY", this.randomTargetVelocity.y);
        compoundNBT.putDouble("randomTargetVelocityZ", this.randomTargetVelocity.z);
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
        for(int i = 0; i < this.bodyEntityUUIDs.length; i++){
            if (compoundNBT.hasUUID("BodyUUID"+i)) {
                this.bodyEntityUUIDs[i] = compoundNBT.getUUID("BodyUUID"+i);
            }
        }
        if(compoundNBT.contains("Phase")){
            this.setPhase(Phase.valueOf(compoundNBT.getString("Phase")));
        } else {
            this.setPhase(Phase.IDLE);
        }
        this.passingTimer = compoundNBT.getInt("PassingTimer");
        this.randomTargetVelocity = new Vec3(compoundNBT.getDouble("randomTargetVelocityX"),compoundNBT.getDouble("randomTargetVelocityY"),compoundNBT.getDouble("randomTargetVelocityZ"));
    }

    public void die(DamageSource damageSource) {
        for(MineralLeviathanBody mineralLeviathanBody : this.bodyEntities){
            mineralLeviathanBody.hurt(DamageSource.OUT_OF_WORLD, 1000.0f);
        }
        super.die(damageSource);
    }

    public void startSeenByPlayer(ServerPlayer p_184178_1_) {
        super.startSeenByPlayer(p_184178_1_);
        this.bossEvent.addPlayer(p_184178_1_);
    }

    public void stopSeenByPlayer(ServerPlayer p_184203_1_) {
        super.stopSeenByPlayer(p_184203_1_);
        this.bossEvent.removePlayer(p_184203_1_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, BASE_HEALTH).add(Attributes.ATTACK_DAMAGE, DAMAGE).add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE);
    }

    public ServerBossEvent getBossEvent(){
        return this.bossEvent;
    }

    enum Phase {
        IDLE,
        CHARGING,
        PASSING,
        LOOPING;
    }
}
