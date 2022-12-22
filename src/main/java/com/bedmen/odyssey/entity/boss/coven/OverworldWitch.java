package com.bedmen.odyssey.entity.boss.coven;

import com.bedmen.odyssey.entity.ai.CovenReturnToMasterGoal;
import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OverworldWitch extends CovenWitch {
    private static final EntityDataAccessor<Byte> DATA_SPELL_CASTING_ID = SynchedEntityData.defineId(OverworldWitch.class, EntityDataSerializers.BYTE);
    private Phase phase = Phase.IDLE;
    private int spellCastingTickCount;

    public OverworldWitch(EntityType<? extends CovenWitch> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CovenReturnToMasterGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(3, new OverworldWitchRootGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SPELL_CASTING_ID, (byte)0);
    }

    public void readAdditionalSaveData(CompoundTag p_33732_) {
        super.readAdditionalSaveData(p_33732_);
        this.spellCastingTickCount = p_33732_.getInt("SpellTicks");
    }

    public void addAdditionalSaveData(CompoundTag p_33734_) {
        super.addAdditionalSaveData(p_33734_);
        p_33734_.putInt("SpellTicks", this.spellCastingTickCount);
    }

    public void aiStep() {

        Optional<CovenMaster> master = this.getMaster();
        if(!this.isNoAi() && master.isPresent()) {
            CovenMaster covenMaster = master.get();

            if (!this.level.isClientSide) {
                // Target
                switch (this.phase) {
                    case CHASING:
                        if (!this.isValidTarget(this.getTarget(), covenMaster))
                            this.phase = Phase.IDLE;
                    case IDLE:
                        if (GeneralUtil.isHashTick(this, this.level, 50)) {
                            Collection<ServerPlayer> serverPlayerEntities = covenMaster.bossEvent.getPlayers();
                            List<ServerPlayer> serverPlayerEntityList = serverPlayerEntities.stream().filter(covenMaster::validTargetPredicate).collect(Collectors.toList());

                            if (serverPlayerEntityList.isEmpty()) {
                                this.setTarget(null);
                            } else {
                                this.phase = Phase.CHASING;
                                this.setTarget(serverPlayerEntityList.get(this.random.nextInt(serverPlayerEntityList.size())));
                            }
                        }
                }
            }
        }

        super.aiStep();
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.spellCastingTickCount > 0) {
            --this.spellCastingTickCount;
        } else {
            if (this.phase == Phase.CASTING)
                this.phase = Phase.CHASING;
        }
    }

    public boolean isCastingSpell() {
        if (this.level.isClientSide) {
            return this.entityData.get(DATA_SPELL_CASTING_ID) > 0;
        } else {
            return this.phase == Phase.CASTING;
        }
    }

//    public void setIsCastingSpell(SpellcasterIllager.IllagerSpell p_33728_) {
//        this.currentSpell = p_33728_;
//        this.entityData.set(DATA_SPELL_CASTING_ID, (byte)p_33728_.id);
//    }
//

    @Override
    protected void doWhenReturnToMaster() {
        this.phase = Phase.IDLE;
    }

    enum Phase {
        IDLE,
        CHASING,
        CASTING
    }

    private class OverworldWitchRootGoal extends Goal {
        private final OverworldWitch overworldWitch;

        private int attackWarmupDelay;
        private int nextAttackTickCount;

        private final float attackRadius = 45.0F;
        private final float rangedRadius = 20.0F;
        private final int castWarmupTime = 20;
        private final int castingTime = 20;
        private final int castingInterval = 100;

        public OverworldWitchRootGoal(OverworldWitch witch) {
            this.overworldWitch = witch;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = overworldWitch.getTarget();
            if (livingentity != null && livingentity.isAlive()) {
                if (overworldWitch.isCastingSpell()) {
                    return false;
                } else {
                    return overworldWitch.tickCount >= this.nextAttackTickCount;
                }
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = overworldWitch.getTarget();
            Optional<CovenMaster> master = overworldWitch.getMaster();
            if (master.isPresent())
                return isValidTarget(livingentity, master.get()) && this.attackWarmupDelay > 0;
            return false;
        }

        public void start() {
            this.attackWarmupDelay = this.adjustedTickDelay(this.castWarmupTime);
            overworldWitch.spellCastingTickCount = this.castingTime;
            overworldWitch.phase = Phase.CASTING;

            float adjCastingInterval = this.castingInterval;
            Optional<CovenMaster> master = overworldWitch.getMaster();
            if (master.isPresent()) {
                int playerNumber = master.get().getNearbyPlayerNumber();
               adjCastingInterval *= overworldWitch.attackTimeMultiplier(playerNumber);
            }

            this.nextAttackTickCount = overworldWitch.tickCount + Mth.floor(adjCastingInterval);
            SoundEvent soundevent = this.getSpellPrepareSound();
            if (soundevent != null) {
                overworldWitch.playSound(soundevent, 1.0F, 1.0F);
            }
//            OverworldWitch.this.setIsCastingSpell(this.getSpell());
        }

        public void tick() {
            Optional<CovenMaster> master = overworldWitch.getMaster();
            if (master.isPresent()) {
                CovenMaster covenMaster = master.get();
                LivingEntity target = overworldWitch.getTarget();
                if (overworldWitch.isValidTarget(target, covenMaster)) {
                    double d0 = this.overworldWitch.distanceToSqr(target);
                    if (d0 < this.rangedRadius * this.rangedRadius) {
                        --this.attackWarmupDelay;
                        if (this.attackWarmupDelay == 0) {
                            this.performSpellCasting();
                            overworldWitch.playSound(overworldWitch.getCastingSoundEvent(), 1.0F, 1.0F);
                        }
                    } else if (d0 < this.attackRadius * this.attackRadius) {
                        overworldWitch.getNavigation().moveTo(target, 1.5D);
                    }
                }
            }

        }

        protected void performSpellCasting() {
            LivingEntity target = overworldWitch.getTarget();
            double d0 = Math.min(target.getY(), overworldWitch.getY());
            double d1 = Math.max(target.getY(), overworldWitch.getY()) + 1.0D;
            float f = (float) Mth.atan2(target.getZ() - overworldWitch.getZ(), target.getX() - overworldWitch.getX());
            if (overworldWitch.distanceToSqr(target) < 9.0D) {
                for(int i = 0; i < 5; ++i) {
                    float f1 = f + (float)i * (float)Math.PI * 0.4F;
                    this.createSpellEntity(overworldWitch.getX() + (double)Mth.cos(f1) * 1.5D, overworldWitch.getZ() + (double)Mth.sin(f1) * 1.5D, d0, d1, f1, 0);
                }

                for(int k = 0; k < 8; ++k) {
                    float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
                    this.createSpellEntity(overworldWitch.getX() + (double)Mth.cos(f2) * 2.5D, overworldWitch.getZ() + (double)Mth.sin(f2) * 2.5D, d0, d1, f2, 3);
                }
            } else {
                for(int l = 0; l < 16; ++l) {
                    double d2 = 1.25D * (double)(l + 1);
                    int j = l;
                    this.createSpellEntity(overworldWitch.getX() + (double)Mth.cos(f) * d2, overworldWitch.getZ() + (double)Mth.sin(f) * d2, d0, d1, f, j);
                }
            }
        }

        private void createSpellEntity(double p_32673_, double p_32674_, double p_32675_, double p_32676_, float p_32677_, int p_32678_) {
            BlockPos blockpos = new BlockPos(p_32673_, p_32676_, p_32674_);
            boolean flag = false;
            double d0 = 0.0D;

            do {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = overworldWitch.level.getBlockState(blockpos1);
                if (blockstate.isFaceSturdy(overworldWitch.level, blockpos1, Direction.UP)) {
                    if (!overworldWitch.level.isEmptyBlock(blockpos)) {
                        BlockState blockstate1 = overworldWitch.level.getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShape(overworldWitch.level, blockpos);
                        if (!voxelshape.isEmpty()) {
                            d0 = voxelshape.max(Direction.Axis.Y);
                        }
                    }
                    flag = true;
                    break;
                }

                blockpos = blockpos.below();
            } while(blockpos.getY() >= Mth.floor(p_32675_) - 1);

            if (flag) {
                overworldWitch.level.addFreshEntity(new CovenRootEntity(overworldWitch.level, p_32673_, (double)blockpos.getY() + d0, p_32674_, p_32677_, p_32678_, overworldWitch));
            }

        }
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }
    }
}
