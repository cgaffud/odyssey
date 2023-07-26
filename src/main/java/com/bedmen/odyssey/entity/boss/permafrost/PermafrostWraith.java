package com.bedmen.odyssey.entity.boss.permafrost;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class PermafrostWraith extends AbstractMainPermafrostEntity {

    private static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(PermafrostWraith.class, EntityDataSerializers.INT);
    private static final String PHASE_TAG = "WraithPhase";

    private float pathRecalcTicker = 0;
    private float meleeAttackTicker = 0;
    private float spiralAttackTicker = 0;

    private final float MIN_CHASE_VELOCITY = 0.35f;
    private final float MAX_VELOCITY = 0.8f;

    public enum Phase {
        IDLE,
        MELEE
    }


    public PermafrostWraith(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new WraithMoveControl(this);
        this.setPhase(Phase.IDLE);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 1.0D).add(Attributes.ATTACK_DAMAGE, 10.0D);
    }


    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
    }

    public void aiStep() {
        Optional<PermafrostMaster> master = this.getMaster();
        if(!this.isNoAi() && master.isPresent() && !this.level.isClientSide()) {
            PermafrostMaster permafrostMaster = master.get();

            switch (this.getPhase()) {
                case IDLE:
                    //Choose Target
                    Collection<ServerPlayer> serverPlayers = permafrostMaster.bossEvent.getPlayers();
                    List<ServerPlayer> serverPlayerList = serverPlayers.stream().filter(permafrostMaster::validTargetPredicate).collect(Collectors.toList());
                    // Set Phase based on Target
                    if (this.level.getGameTime() % 18 == 14) {
                        if (serverPlayerList.isEmpty()) {
                            this.setTarget(null);
                        } else {
                            setTarget(serverPlayerList.get(this.random.nextInt(serverPlayerList.size())));
                        }
                    }

                    if (this.getTarget() != null) {
                        this.setPhase(Phase.MELEE);
                        this.pathRecalcTicker = 0;
                        this.meleeAttackTicker = 20;
                        this.spiralAttackTicker = 60;
                    }

                    break;
                case MELEE:
                    LivingEntity livingentity = this.getTarget();
                    if ((livingentity != null) && livingentity.isAlive()) {
                        this.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                        double d0 = this.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                        this.pathRecalcTicker = Math.max(this.pathRecalcTicker - 1, 0);
                        Vec3 rVector = new Vec3(this.getMoveControl().getWantedX() - this.getX(), this.getMoveControl().getWantedY() - this.getY(), this.getMoveControl().getWantedZ() - this.getZ());
                        double r = rVector.length();
                        if ((this.pathRecalcTicker <= 0 && (this.getRandom().nextFloat() < 0.05F)) || (r < this.getBoundingBox().getSize())) {
                            this.pathRecalcTicker = 4 + this.getRandom().nextInt(7);

                            if (d0 > 1024.0D) {
                                this.pathRecalcTicker += 10;
                            } else if (d0 > 256.0D) {
                                this.pathRecalcTicker += 5;
                            }

                            Vec3 vec3 = livingentity.position();
                            this.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, this.MIN_CHASE_VELOCITY);
                        }

                        this.meleeAttackTicker = Math.max(this.meleeAttackTicker - 1, 0);
                        this.spiralAttackTicker = Math.max(this.spiralAttackTicker - 1, 0);

                        boolean closeEnoughToMelee = (d0 < (this.getBbWidth() * 2.75f * this.getBbWidth() * .75f + livingentity.getBbWidth()));

                        if ((this.meleeAttackTicker <= 0) && closeEnoughToMelee) {
                            this.meleeAttackTicker = 20;
                            this.swing(InteractionHand.MAIN_HAND);
                            this.doHurtTarget(livingentity);
                        }

                        if ((this.spiralAttackTicker <= 0) && !closeEnoughToMelee) {
                            if (this.iciclePosition == 20)
                                this.spiralAttackTicker = 80;
                            else {
                                this.performSpiralAttack(livingentity);
                                ++this.iciclePosition;
                            }
                        } else {
                            this.iciclePosition = 0;
                        }
                    } else {
                        this.setPhase(Phase.IDLE);
                    }
                    break;
            }
        }
        super.aiStep();
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if(this.getMaster().isPresent()) {
            PermafrostMaster permafrostMaster = this.getMaster().get();
            return permafrostMaster.hurt(damageSource, amount);
        }
        return super.hurt(damageSource, amount);
    }


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PHASE, 0);
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putInt(PHASE_TAG, this.getPhase().ordinal());
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if(compoundNBT.contains(PHASE_TAG)) {
            this.setPhase(Phase.values()[compoundNBT.getInt(PHASE_TAG)]);
        }
    }

    public void setPhase(Phase phase) {
        this.entityData.set(DATA_PHASE, phase.ordinal());
    }

    public Phase getPhase() {
        return Phase.values()[(this.entityData.get(DATA_PHASE))];
    }

    // Lifted from AbstractWraith
    public class WraithMoveControl extends MoveControl {

        public WraithMoveControl(PermafrostWraith p_34062_) {
            super(p_34062_);
        }

        public void tick() {
            if (this.operation == Operation.WAIT){
                PermafrostWraith.this.setNoGravity(true);
            } else if (this.operation == Operation.STRAFE) {
                float f = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                float f1 = (float) this.speedModifier * f;

                this.mob.setSpeed(f1);
                this.mob.setZza(this.strafeForwards);
                this.mob.setXxa(this.strafeRight);
                this.operation = MoveControl.Operation.WAIT;
            } else if (this.operation == Operation.MOVE_TO) {
                Vec3 rVector = new Vec3(this.wantedX - PermafrostWraith.this.getX(), this.wantedY - PermafrostWraith.this.getY(), this.wantedZ - PermafrostWraith.this.getZ());
                double r = rVector.length();
                if (r < PermafrostWraith.this.getBoundingBox().getSize()) {
                    PermafrostWraith.this.setDeltaMovement(PermafrostWraith.this.getDeltaMovement().scale(0.7D));
                } else {
                    // 0.05(Speed Modifier) is the constant acceleration
                    Vec3 vVector;
                    if ((r >= PermafrostWraith.this.getBoundingBox().getSize() * 2) && (PermafrostWraith.this.getDeltaMovement().length() < PermafrostWraith.this.MIN_CHASE_VELOCITY)) {
                        // Divide by 80 so that Wraith can get to target in 4 seconds
                        double vmin = r / 80;
                        vmin = (vmin > PermafrostWraith.this.MIN_CHASE_VELOCITY) ? vmin : PermafrostWraith.this.MIN_CHASE_VELOCITY;
                        vVector = rVector.scale(vmin / r);
                    } else {
                        vVector = PermafrostWraith.this.getDeltaMovement().add(rVector.scale(this.speedModifier * 0.05D / r));
                    }

                    // System.out.printf("Dist: %f, Bounding Box Size * 2: %f, Velocity: %f", r, PermafrostWraith.this.getBoundingBox().getSize()*2, vVector.length());

                    PermafrostWraith.this.setDeltaMovement((vVector.length() > PermafrostWraith.this.MAX_VELOCITY) ? rVector.scale(PermafrostWraith.this.MAX_VELOCITY / r) : vVector);

                    if (PermafrostWraith.this.getTarget() == null) {
                        Vec3 vec31 = PermafrostWraith.this.getDeltaMovement();
                        PermafrostWraith.this.setYRot(-((float) Mth.atan2(vec31.x, vec31.z)) * (180F / (float) Math.PI));
                        PermafrostWraith.this.yBodyRot = PermafrostWraith.this.getYRot();
                    } else {
                        double d2 = PermafrostWraith.this.getTarget().getX() - PermafrostWraith.this.getX();
                        double d1 = PermafrostWraith.this.getTarget().getZ() - PermafrostWraith.this.getZ();
                        PermafrostWraith.this.setYRot(-((float) Mth.atan2(d2, d1)) * (180F / (float) Math.PI));
                        PermafrostWraith.this.yBodyRot = PermafrostWraith.this.getYRot();
                    }
                }
            }
        }
    }

}
