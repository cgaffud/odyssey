package com.bedmen.odyssey.entity.boss.permafrost;

import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.mojang.math.Vector3f;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;


public class PermafrostWraith extends AbstractMainPermafrostEntity {

    private static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(PermafrostWraith.class, EntityDataSerializers.INT);
    private static final String PHASE_TAG = "WraithPhase";


    private static final EntityDataAccessor<Integer> DATA_RANGE_TYPE = SynchedEntityData.defineId(PermafrostWraith.class, EntityDataSerializers.INT);
    private static final String RANGE_TAG = "WraithPhase";

    private int pathRecalcTicker = 0;
    private int totalMeleeTime = 0;
    private int meleeAttackTicker = 0;
    private int spiralAttackTicker = 0;
    private int totalRangedAttackTicker = 0;
    private int tinyRangedAttackTicker = 0;


    private final float MIN_CHASE_VELOCITY = 0.5f;
    private final float MAX_VELOCITY = 0.9f;

    public enum Phase {
        IDLE,
        MELEE,
        RANGED
    }

    private enum RangeType {
        CONDUIT,
        WRAITHLING
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

            if (permafrostMaster.getTotalPhase() == 3) {
                switch (this.getPhase()) {
                    case IDLE:
                        //Choose Target
                        List<ServerPlayer> serverPlayerList = this.getValidTargets();
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
                            this.totalMeleeTime = 0;
                        }

                        break;
                    case MELEE:
                        LivingEntity livingentity = this.getTarget();
                        if ((livingentity != null) && livingentity.isAlive() && (livingentity instanceof ServerPlayer serverPlayer) && permafrostMaster.validTargetPredicate(serverPlayer)) {
                            this.totalMeleeTime++;
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
                                System.out.println("Melee Range");
                                this.meleeAttackTicker = 20;
                                this.swing(InteractionHand.MAIN_HAND);
                                this.doHurtTarget(livingentity);
                                livingentity.addEffect(new MobEffectInstance(EffectRegistry.PERMAFROST_BIG_FREEZING.get(), 80));
                            }

                            if ((this.spiralAttackTicker <= 0) && !closeEnoughToMelee) {
                                if (this.iciclePosition == 20)
                                    this.spiralAttackTicker = 60;
                                else {
                                    this.performSpiralAttack(livingentity);
                                    ++this.iciclePosition;
                                }
                            } else {
                                this.iciclePosition = 0;
                            }

                            if (this.totalMeleeTime >= 100) {
                                RangeType rangeType = RangeType.values()[this.getRandom().nextInt(2)];
                                System.out.println(rangeType);
                                this.setRangeType(rangeType);
                                this.setPhase(Phase.RANGED);
                            }

                        } else {
                            this.setPhase(Phase.IDLE);
                        }
                        break;
                    case RANGED:
                        List<ServerPlayer> serverPlayers = this.getValidTargets();
                        if (serverPlayers != null && !serverPlayers.isEmpty()) {
                            Vec3 center = serverPlayers.stream().reduce(Vec3.ZERO, (accumV, serverPlayer) -> (accumV.add(serverPlayer.position())), Vec3::add).scale(1 / ((double) permafrostMaster.getNearbyPlayerNumber()));
                            center = center.add(0, 10, 0);
                            this.pathRecalcTicker = Math.max(this.pathRecalcTicker - 1, 0);
                            Vec3 rVector = new Vec3(this.getMoveControl().getWantedX() - this.getX(), this.getMoveControl().getWantedY() - this.getY(), this.getMoveControl().getWantedZ() - this.getZ());
                            double r = rVector.length();
                            if ((this.pathRecalcTicker <= 0 && (this.getRandom().nextFloat() < 0.05F)) || (r < this.getBoundingBox().getSize())) {
                                this.pathRecalcTicker = 4 + this.getRandom().nextInt(7);
                                this.getMoveControl().setWantedPosition(center.x, center.y, center.z, this.MIN_CHASE_VELOCITY);
                            }

                            this.totalRangedAttackTicker++;
                            switch (this.getRangeType()) {
                                case CONDUIT:
                                    if (this.totalRangedAttackTicker % 80 == 60) {
                                        this.tinyRangedAttackTicker = 23;
                                    }
                                    if (this.tinyRangedAttackTicker >= 1) {
                                        this.performSphereAttack(this.tinyRangedAttackTicker);
                                        this.tinyRangedAttackTicker--;
                                    }
                                    if (this.totalRangedAttackTicker % 160 == 120)
                                        permafrostMaster.shootIcicles();
                                    if (this.totalRangedAttackTicker % 200 == 0) {
                                        this.setPhase(Phase.IDLE);
                                        permafrostMaster.getIcicles().forEach(icicle -> icicle.discardAndDoParticles());
                                    }
                                    break;
                                case WRAITHLING:
                                    if (this.totalRangedAttackTicker % 60 == 40) {
                                        int total = Mth.clamp(0, permafrostMaster.getNearbyPlayerNumber() * 2, 10);
                                        for (int i = 0; i < total; i++) {
                                            this.spawnWraithling(i, total);
                                        }
                                    }
                                    RandomSource randomSource = this.getRandom();
                                    for (int i = 0; i < 6; ++i) {
                                        ((ServerLevel) (this.getLevel())).sendParticles(new DustParticleOptions(new Vector3f(0.35f, 0.35f, 0.35f), 1.0F), this.getX() + (randomSource.nextFloat() - 0.5f), this.getEyeY() + (randomSource.nextFloat() - 1f), this.getZ() + (randomSource.nextFloat() - 0.5f), 2, 0.2D, 0.2D, 0.2D, 0.0D);
                                    }
                                    if (this.totalRangedAttackTicker % 120 == 0)
                                        this.setPhase(Phase.IDLE);
                                    break;
                            }
                            break;
                        } else {
                            this.setPhase(Phase.IDLE);
                        }
                }
            } else if (permafrostMaster.getTotalPhase() == 4) {
                this.setDeltaMovement(0, 0.15, 0);
                RandomSource randomSource = this.getRandom();
                for (int i = 0; i < 12; ++i) {
                    ((ServerLevel) (this.getLevel())).sendParticles(new DustParticleOptions(new Vector3f(0.35f, 0.35f, 0.35f), 1.0F), this.getX() + (randomSource.nextFloat() - 0.5f) * 1.5f, this.getEyeY() + (randomSource.nextFloat() - 1.25f) * 1.5f, this.getZ() + (randomSource.nextFloat() - 0.5f) * 1.5f, 2, 0.2D, 0.2D, 0.2D, 0.0D);
                }
            }
        }
        super.aiStep();
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if(this.getMaster().isPresent()) {
            PermafrostMaster permafrostMaster = this.getMaster().get();
            if (permafrostMaster.getHealth()-amount > 1.0f)
                return permafrostMaster.hurt(damageSource, amount);
            else {
                permafrostMaster.setTotalPhase(4);
                return false;
            }
        }
        return super.hurt(damageSource, amount);
    }


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PHASE, 0);
        this.entityData.define(DATA_RANGE_TYPE, 0);
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putInt(PHASE_TAG, this.getPhase().ordinal());
        compoundNBT.putInt(RANGE_TAG, this.getRangeType().ordinal());
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if(compoundNBT.contains(PHASE_TAG)) {
            this.setPhase(Phase.values()[compoundNBT.getInt(PHASE_TAG)]);
        }
        if(compoundNBT.contains(RANGE_TAG)) {
            this.setRangeType(RangeType.values()[compoundNBT.getInt(RANGE_TAG)]);
        }
    }

    public void setPhase(Phase phase) {
        this.entityData.set(DATA_PHASE, phase.ordinal());
    }

    public Phase getPhase() {
        return Phase.values()[(this.entityData.get(DATA_PHASE))];
    }

    private void setRangeType(RangeType rangeType) {
        this.totalRangedAttackTicker = 0;
        if (rangeType == RangeType.CONDUIT) {
            if (this.getMaster().isPresent()) {
                PermafrostMaster permafrostMaster = this.getMaster().get();
                for (int i = 0; i < PermafrostMaster.ICICLE_AMOUNT; i++)
                    permafrostMaster.addBigIcicle(i);
            }
        }
        this.entityData.set(DATA_RANGE_TYPE, rangeType.ordinal());
    }

    private RangeType getRangeType() {
        return RangeType.values()[(this.entityData.get(DATA_RANGE_TYPE))];
    }


    private void spawnWraithling(int i, int total) {
        Wraithling wraithling = new Wraithling(EntityTypeRegistry.WRAITHLING.get(), this.level);
        float phi = (((float) i)/((float) total)) * Mth.TWO_PI;
        wraithling.moveTo(this.position().add(new Vec3(Mth.cos(phi) * PermafrostMaster.ICICLE_FOLLOW_RADIUS, 0, Mth.sin(phi) * PermafrostMaster.ICICLE_FOLLOW_RADIUS)));
        wraithling.setOwner(this);
        this.level.addFreshEntity(wraithling);
    }

    public boolean shouldTwitch() {
        if (this.getMaster().isPresent()) {
            return this.getMaster().get().getTotalPhase() == 4;
        } return false;
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
