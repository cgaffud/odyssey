package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.SoundEventRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MineralLeviathanHeadEntity extends MineralLeviathanSegmentEntity {
    protected static final DataParameter<List<Integer>> DATA_BODY_ID = EntityDataManager.defineId(MineralLeviathanHeadEntity.class, OdysseyDataSerializers.INT_LIST);
    protected static final DataParameter<Integer> PHASE = EntityDataManager.defineId(MineralLeviathanHeadEntity.class, DataSerializers.INT);
    private final ServerBossInfo bossEvent = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenScreen(true);
    private int passingTimer;
    private Vector3d randomTargetVelocity = new Vector3d(this.random.nextDouble()*2d-1d, this.random.nextDouble()*2d-2d, this.random.nextDouble()*2d-1d);
    public static final int NUM_SEGMENTS = 20;
    public static final double DAMAGE = 8.0d;
    public static final double DODGE_RANGE = 3.5d;
    public static final double BASE_HEALTH = 150.0d;
    public static final double TARGETING_RANGE = 75.0d;
    public MineralLeviathanBodyEntity[] bodyEntities = new MineralLeviathanBodyEntity[NUM_SEGMENTS-1];
    public UUID[] bodyEntityUUIDs = new UUID[NUM_SEGMENTS-1];
    private int mouthAngleTimer;
    private Phase phaseO = Phase.IDLE;
    private float mouthAngle;
    private float mouthAngleO;

    public MineralLeviathanHeadEntity(EntityType<? extends MineralLeviathanHeadEntity> entityType, World world) {
        super(entityType, world);
        this.setShellType(ShellType.RUBY);
    }

    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        ILivingEntityData ilivingentitydata = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
        if(this.bodyEntityUUIDs[0] == null){
            Vector3d headPosition = this.getPosition(1.0f);
            Vector3d loweringVector = new Vector3d(0.0d,-1.0d, 0.0d);
            this.xRot = 90.0f;
            for(int i = 0; i < this.bodyEntities.length; i++){
                if(i == 0){
                    this.bodyEntities[i] = new MineralLeviathanBodyEntity(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), this.level, this, this);
                } else {
                    this.bodyEntities[i] = new MineralLeviathanBodyEntity(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), this.level, this, this.bodyEntities[i-1]);
                }
                this.bodyEntities[i].moveTo(headPosition.add(loweringVector.scale(i)));
                this.bodyEntities[i].xRot = 90.0f;
                this.bodyEntityUUIDs[i] = this.bodyEntities[i].getUUID();
                this.level.addFreshEntity(this.bodyEntities[i]);
            }
            this.setBodyIDs(this.bodyEntities);
            this.initBody = true;
        }
        return ilivingentitydata;
    }

    protected void registerGoals() {
        //this.targetSelector.addGoal(1, new MineralLeviathanHeadEntity.HurtByTargetGoal(this));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BODY_ID, new ArrayList<>());
        this.entityData.define(PHASE, 0);
    }

    public void setBodyIDs(MineralLeviathanBodyEntity[] bodyEntities) {
        List<Integer> idList = new ArrayList<>();
        for(MineralLeviathanBodyEntity bodyEntity : bodyEntities){
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

    public void setCustomName(@Nullable ITextComponent p_200203_1_) {
        super.setCustomName(p_200203_1_);
        this.bossEvent.setName(this.getDisplayName());
    }

    public void aiStep() {
        //Server side Init Body
        if(!this.initBody && !this.level.isClientSide && this.bodyEntityUUIDs[0] != null){
            ServerWorld serverWorld = (ServerWorld) this.level;
            for(int i = 0; i < this.bodyEntityUUIDs.length; i++){
                this.bodyEntities[i] = (MineralLeviathanBodyEntity) serverWorld.getEntity(this.bodyEntityUUIDs[i]);
            }
            this.setBodyIDs(this.bodyEntities);
            this.initBody = true;
        }
        //Client side Init Body
        else if(!this.initBody){
            List<Integer> idList = this.getBodyIDs();
            if(idList.size() >= NUM_SEGMENTS - 1){
                for(int i = 0; i < this.bodyEntities.length; i++){
                    this.bodyEntities[i] = (MineralLeviathanBodyEntity) this.level.getEntity(idList.get(i));
                }
                this.initBody = true;
            }
        }

        if(!this.isNoAi()){
            if(!this.level.isClientSide){
                Phase phase = this.getPhase();
                //Choose Target
                if(this.level.getGameTime() % 19 == 0){
                    Collection<ServerPlayerEntity> serverPlayerEntities =  this.bossEvent.getPlayers();
                    List<ServerPlayerEntity> serverPlayerEntityList = serverPlayerEntities.stream().filter(serverPlayerEntity -> {return serverPlayerEntity.isAlive() && !serverPlayerEntity.abilities.invulnerable && this.distanceToSqr(serverPlayerEntity) < TARGETING_RANGE*TARGETING_RANGE;}).collect(Collectors.toList());
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
                            this.randomTargetVelocity = new Vector3d(this.random.nextDouble()*2d-1d, this.random.nextDouble()*2d-2d, this.random.nextDouble()*2d-1d);
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
                            Vector3d movementVector = this.getDeltaMovement();
                            Vector3d targetVector = target.getPosition(1.0f).subtract(this.getPosition(1.0f));
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
                for(MineralLeviathanBodyEntity mineralLeviathanBodyEntity : this.bodyEntities){
                    if(mineralLeviathanBodyEntity != null){
                        mineralLeviathanBodyEntity.hurtTime = this.hurtTime;
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
                        this.adjustMouthAngle((MathHelper.sin((float) (Math.PI * this.tickCount / 20f))+0.5f)*50f);
                        break;
                    case CHARGING:
                        this.adjustMouthAngle((MathHelper.sin((float) (Math.PI * this.tickCount / 10f))+0.5f)*50f);
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
//            if(Math.round(this.mouthAngle) >= 75 && this.mouthAngle != this.mouthAngleO && this.level.isClientSide){
//                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENDER_DRAGON_GROWL, this.getSoundSource(), 4.0F, 0.5F, false);
//            }
        }
        super.aiStep();
    }

    protected void moveTowards(Vector3d vector3d, double acceleration){
        this.setDeltaMovement(vector3d.normalize().scale(acceleration).add(this.getDeltaMovement()));
        this.setRotation(this.getDeltaMovement());
    }

    protected void rotateTowards(Vector3d vector3d, double acceleration, double rotationRate){
        this.setDeltaMovement(vector3d.normalize().scale(rotationRate).add(this.getDeltaMovement()).normalize().scale(acceleration));
        this.setRotation(this.getDeltaMovement());
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setPercent(this.getHealth() / this.getMaxHealth());
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

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
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

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
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
        this.randomTargetVelocity = new Vector3d(compoundNBT.getDouble("randomTargetVelocityX"),compoundNBT.getDouble("randomTargetVelocityY"),compoundNBT.getDouble("randomTargetVelocityZ"));
    }

    public void die(DamageSource damageSource) {
        for(MineralLeviathanBodyEntity mineralLeviathanBodyEntity : this.bodyEntities){
            mineralLeviathanBodyEntity.hurt(DamageSource.OUT_OF_WORLD, 1000.0f);
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

    public ServerBossInfo getBossEvent(){
        return this.bossEvent;
    }

    enum Phase {
        IDLE,
        CHARGING,
        PASSING,
        LOOPING;
    }
}
