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
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OverworldWitch extends CovenWitch {
    private static final EntityDataAccessor<Byte> DATA_SPELL_CASTING_ID = SynchedEntityData.defineId(OverworldWitch.class, EntityDataSerializers.BYTE);
    private int spellCastingTickCount;

    private final OverworldWitchSpikeGoal spikeGoal;
    private final OverworldWitchRootGoal rootGoal;

    public OverworldWitch(EntityType<? extends CovenWitch> entityType, Level level) {
        super(entityType, level);
        this.spikeGoal = new OverworldWitchSpikeGoal(this);
        this.rootGoal = new OverworldWitchRootGoal(this);
    }

    public CovenType getCovenType(){
        return CovenType.OVERWORLD;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CovenReturnToMasterGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
//        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 0.6D, 1.0D));
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
                switch (this.getPhase()) {
                    case CHASING, CASTING:
                        if (!this.isValidTarget(this.getTarget(), covenMaster))
                            this.setPhase(Phase.IDLE);
                    case IDLE:
                        if (GeneralUtil.isHashTick(this, this.level, 50)) {
                            if (this.goalSelector.getAvailableGoals().stream()
                                    .noneMatch(wrappedGoal -> (wrappedGoal.getGoal() == this.rootGoal) || (wrappedGoal.getGoal() == this.spikeGoal))) {
                                this.goalSelector.addGoal(3, this.rootGoal);
                            }

                            Collection<ServerPlayer> serverPlayerEntities = covenMaster.bossEvent.getPlayers();
                            List<ServerPlayer> serverPlayerEntityList = serverPlayerEntities.stream().filter(covenMaster::validTargetPredicate).collect(Collectors.toList());

                            if (serverPlayerEntityList.isEmpty()) {
                                this.setTarget(null);
                            } else {
                                this.setPhase(Phase.CHASING);
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
            if (this.getPhase() == Phase.CASTING)
                this.setPhase(Phase.CHASING);
        }
    }

    public boolean isCastingSpell() {
        if (this.level.isClientSide) {
            return this.entityData.get(DATA_SPELL_CASTING_ID) > 0;
        } else {
            return this.getPhase() == Phase.CASTING;
        }
    }

    public void setSpikeGoal() {
        this.goalSelector.removeGoal(this.rootGoal);
        this.goalSelector.addGoal(3, this.spikeGoal);
    }

    public void setRootGoal() {
        this.goalSelector.removeGoal(this.spikeGoal);
        this.goalSelector.addGoal(3, this.rootGoal);
    }

//    public void setIsCastingSpell(SpellcasterIllager.IllagerSpell p_33728_) {
//        this.currentSpell = p_33728_;
//        this.entityData.set(DATA_SPELL_CASTING_ID, (byte)p_33728_.id);
//    }
//

    protected void doWhenReturnToMaster() {
        super.doWhenReturnToMaster();
        setRootGoal();
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
            return (master.isPresent()) && isValidTarget(livingentity, master.get()) && this.attackWarmupDelay > 0;
        }

        public void start() {
            this.attackWarmupDelay = this.adjustedTickDelay(this.castWarmupTime);
            overworldWitch.spellCastingTickCount = this.castingTime;
            overworldWitch.setPhase(Phase.CHASING);

            float adjCastingInterval = this.castingInterval;
            if (overworldWitch.isEnraged())
                adjCastingInterval *= 0.5;

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

        public void stop() {
            overworldWitch.setPhase(Phase.CHASING);
        }

        public void tick() {
            Optional<CovenMaster> master = overworldWitch.getMaster();
            if (master.isPresent()) {
                CovenMaster covenMaster = master.get();
                LivingEntity target = overworldWitch.getTarget();
                if (overworldWitch.isValidTarget(target, covenMaster)) {
                    double d0 = this.overworldWitch.distanceToSqr(target);
                    if (d0 < this.rangedRadius * this.rangedRadius) {
                        overworldWitch.setPhase(Phase.CASTING);
                        --this.attackWarmupDelay;
                        if (this.attackWarmupDelay == 0) {
                            this.performSpellCasting();
                            overworldWitch.playSound(overworldWitch.getCastingSoundEvent(), 1.0F, 1.0F);
                        }
                    } else if (d0 < this.attackRadius * this.attackRadius) {
                        overworldWitch.setPhase(Phase.CHASING);
                        overworldWitch.getNavigation().moveTo(target, 1.5D);
                    }
                    overworldWitch.getLookControl().setLookAt(target, 30.0F, 30.0F);
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

    private class OverworldWitchSpikeGoal extends Goal {
        private final OverworldWitch overworldWitch;

        private int attackWarmupDelay;
        private int nextAttackTickCount;

        private final int castWarmupTime = 20;
        private final int castingTime = 20;
        private final int castingInterval = 150;

        public OverworldWitchSpikeGoal(OverworldWitch witch) {
            this.overworldWitch = witch;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            Optional<CovenMaster> master = overworldWitch.getMaster();
            if (master.isPresent()) {
                int playerNumber = master.get().getNearbyPlayerNumber();
                if (playerNumber > 0) {
                    if (overworldWitch.isCastingSpell()) {
                        return false;
                    } else {
                        return overworldWitch.tickCount >= this.nextAttackTickCount;
                    }
                }
            }
            return false;
        }

        public boolean canContinueToUse() {
            Optional<CovenMaster> master = overworldWitch.getMaster();
            return (master.isPresent()) && (master.get().getNearbyPlayerNumber() > 0) && this.attackWarmupDelay > 0;
        }

        public void start() {
            this.attackWarmupDelay = this.adjustedTickDelay(this.castWarmupTime);
            overworldWitch.spellCastingTickCount = this.castingTime;
            overworldWitch.setPhase(Phase.CHASING);

            float adjCastingInterval = this.castingInterval;
            if (overworldWitch.isEnraged())
                adjCastingInterval *= 0.25;
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
        }

        public void stop() {
            overworldWitch.setPhase(Phase.CHASING);
        }

        public void tick() {
            Optional<CovenMaster> master = overworldWitch.getMaster();
            if (master.isPresent()) {
                CovenMaster covenMaster = master.get();
                --this.attackWarmupDelay;
                overworldWitch.setPhase(Phase.CASTING);
                if (this.attackWarmupDelay == 0) {
                    Collection<ServerPlayer> serverPlayerEntities = covenMaster.bossEvent.getPlayers();
                    List<ServerPlayer> serverPlayerEntityList = serverPlayerEntities.stream().filter(covenMaster::validTargetPredicate).collect(Collectors.toList());

                    this.performSpellCasting(serverPlayerEntityList);
                    overworldWitch.playSound(overworldWitch.getCastingSoundEvent(), 1.0F, 1.0F);
                    overworldWitch.setRootGoal();
                }
            }
        }

        protected void performSpellCasting(List<ServerPlayer> serverPlayerEntityList) {
            for (ServerPlayer player : serverPlayerEntityList) {
                createSpellEntity(player.position());
            }
        }

        private void createSpellEntity(Vec3 playerPosition) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(playerPosition.x, playerPosition.y, playerPosition.z);
            double initialY = playerPosition.y;
            BlockState blockstate;

            do {
                mutableBlockPos.move(Direction.UP);
                blockstate = overworldWitch.level.getBlockState(mutableBlockPos);
            } while (blockstate.isAir() && (mutableBlockPos.getY() < initialY+10));

            if (mutableBlockPos.getY() != initialY) {
                FallingBlockEntity dripstone = new FallingBlockEntity(overworldWitch.level, playerPosition.x, mutableBlockPos.getY(), playerPosition.z, Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(PointedDripstoneBlock.TIP_DIRECTION, Direction.DOWN));
                System.out.println(dripstone.position());
                dripstone.setHurtsEntities(6, 40);
                overworldWitch.level.addFreshEntity(dripstone);
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
        }

    }
}
