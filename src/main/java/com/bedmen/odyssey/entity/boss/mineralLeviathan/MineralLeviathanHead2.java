package com.bedmen.odyssey.entity.boss.mineralLeviathan;

import com.bedmen.odyssey.registry.SoundEventRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MineralLeviathanHead2 extends MineralLeviathanSegment2 {
    protected static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(MineralLeviathanHead2.class, EntityDataSerializers.INT);
    private int passingTimer;
    private Vec3 randomTargetVelocity = new Vec3(this.random.nextDouble()*2d-1d, this.random.nextDouble()*2d-2d, this.random.nextDouble()*2d-1d);
    private int mouthAngleTimer;
    private Phase phaseO = Phase.IDLE;
    public float mouthAngle;
    public float mouthAngleO;

    public MineralLeviathanHead2(EntityType<? extends MineralLeviathanHead2> entityType, Level world) {
        super(entityType, world);
        this.setShellType(ShellType.RUBY);
    }

    protected void registerGoals() {
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PHASE, 0);
    }

    public void setPhase(Phase phase){
        this.entityData.set(PHASE, phase.ordinal());
    }

    public Phase getPhase(){
        return Phase.values()[this.entityData.get(PHASE)];
    }

    public void adjustMouthAngle(float f){
        this.mouthAngleO = mouthAngle;
        float alpha = (float)this.mouthAngleTimer / 10f;
        this.mouthAngle = this.mouthAngle * (1f-alpha) + f * alpha;
    }

    public void aiStep() {
        Optional<MineralLeviathanMaster2> master = this.getMasterEntity();
        if(!this.isNoAi() && master.isPresent()){
            MineralLeviathanMaster2 mineralLeviathanMaster2 = master.get();
            if(!this.level.isClientSide){
                Phase phase = this.getPhase();
                //Choose Target
                if(this.level.getGameTime() % 19 == 0){
                    Collection<ServerPlayer> serverPlayerEntities = mineralLeviathanMaster2.bossEvent.getPlayers();
                    List<ServerPlayer> serverPlayerEntityList = serverPlayerEntities.stream().filter(mineralLeviathanMaster2::validTargetPredicate).collect(Collectors.toList());
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
                            this.randomTargetVelocity = getRandomDownwardVector();
                        }
                        this.rotateTowards(this.randomTargetVelocity, 0.1d, 0.002d);
                        break;

                    case CHARGING:
                        if(target != null){
                            this.moveTowards(target.getPosition(1.0f).subtract(this.getPosition(1.0f)), 0.3d);
                            if(this.distanceTo(target) < MineralLeviathanMaster2.DODGE_RANGE){
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
                            } else if(this.distanceTo(target) < MineralLeviathanMaster2.DODGE_RANGE){
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
                for(MineralLeviathanBody2 mineralLeviathanBody : mineralLeviathanMaster2.bodyParts){
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

    public Vec3 getRandomDownwardVector() {
        double theta = this.random.nextDouble() * Math.PI * 2.0d;
        // phi is an angle between 0 and pi/4, with a higher chance of being near pi/4
        double phi = Math.acos(-this.level.random.nextDouble() * (1.0d - (1.0d / Math.sqrt(2.0d))) + 1.0d);
        double horizontalScaler = Math.sin(phi);
        return new Vec3(horizontalScaler * Math.cos(theta), -Math.cos(phi), horizontalScaler * Math.sin(theta));
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
        return 5.0F;
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putString("Phase", this.getPhase().name());
        compoundNBT.putInt("PassingTimer", this.passingTimer);
        compoundNBT.putDouble("randomTargetVelocityX", this.randomTargetVelocity.x);
        compoundNBT.putDouble("randomTargetVelocityY", this.randomTargetVelocity.y);
        compoundNBT.putDouble("randomTargetVelocityZ", this.randomTargetVelocity.z);
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if(compoundNBT.contains("Phase")){
            this.setPhase(Phase.valueOf(compoundNBT.getString("Phase")));
        } else {
            this.setPhase(Phase.IDLE);
        }
        this.passingTimer = compoundNBT.getInt("PassingTimer");
        this.randomTargetVelocity = new Vec3(compoundNBT.getDouble("randomTargetVelocityX"),compoundNBT.getDouble("randomTargetVelocityY"),compoundNBT.getDouble("randomTargetVelocityZ"));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 1.0D).add(Attributes.ATTACK_DAMAGE, MineralLeviathanMaster2.DAMAGE);
    }

    enum Phase {
        IDLE,
        CHARGING,
        PASSING,
        LOOPING;
    }
}
