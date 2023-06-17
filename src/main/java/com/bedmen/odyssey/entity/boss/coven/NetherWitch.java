package com.bedmen.odyssey.entity.boss.coven;

import com.bedmen.odyssey.entity.ai.CovenReturnToMasterGoal;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NetherWitch extends CovenWitch {

    public NetherWitch(EntityType<? extends CovenWitch> entityType, Level level) {
        super(entityType, level);
    }

    public CovenType getCovenType(){
        return CovenType.NETHER;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CovenReturnToMasterGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new NetherWitchAttackGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public void aiStep() {

        Optional<CovenMaster> master = this.getMaster();
        if(!this.isNoAi() && master.isPresent()) {
            CovenMaster covenMaster = master.get();

            if (!this.level.isClientSide) {
                // Target
                switch (this.getPhase()){
                    case CHASING, CASTING, SHOOTING:
                        if (!this.isValidTarget(this.getTarget(), covenMaster)) {
                            this.setPhase(Phase.IDLE);
                        }
                    case IDLE:
                        if (GeneralUtil.isHashTick(this, this.level, 50)) {
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

    protected void dropCustomDeathLoot(DamageSource damageSource, int looting, boolean b) {
        super.dropCustomDeathLoot(damageSource, looting, b);

        if ((this.isEnraged && (this.random.nextDouble() < ENRAGED_SPECIAL_DROP_CHANCE))
                || (!this.isEnraged && (this.random.nextDouble() < SPECIAL_DROP_CHANCE))) {
            this.spawnLoot(ItemRegistry.HEXFLAME_DAGGER.get(), 1);
            this.spawnLoot(ItemRegistry.HEXFLAME_DAGGER.get(), 1);
        }
    }

    static class NetherWitchAttackGoal extends Goal {
        private final NetherWitch netherWitch;
        private int attackStep;
        private int attackTime;
        private int lastSeen;

        // Static Vars
        private final float attackRadius = 45.0F;
        private final float rangedRadius = 25.0F;
        private final float evadeRadius = 10.0F;

        public NetherWitchAttackGoal(NetherWitch p_32247_) {
            this.netherWitch = p_32247_;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            Optional<CovenMaster> master = this.netherWitch.getMaster();
            if (master.isPresent()) {
                CovenMaster covenMaster = master.get();
                LivingEntity target = this.netherWitch.getTarget();
                return this.netherWitch.isValidTarget(target, covenMaster) && this.netherWitch.canAttack(target);
            }
            return false;
        }

        public void start() {
            this.attackStep = 0;
        }

        public void stop() {
            this.lastSeen = 0;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        private void handleAttackCounter(float attackTimeMultiplier, boolean fireLargeFireballs) {
            ++this.attackStep;
            // Light?
            if (this.attackStep == 1) {
                this.netherWitch.setPhase(Phase.CASTING);
                this.attackTime = 30;
            // Release small fireballs
            } else if (this.attackStep <= 6 && !fireLargeFireballs) {
                this.netherWitch.setPhase(Phase.SHOOTING);
                this.attackTime = 10;
            // Release big fireballs
            } else if (this.attackStep <= 3 && fireLargeFireballs) {
                this.netherWitch.setPhase(Phase.SHOOTING);
                this.attackTime = 10;
            // Recharge
            } else {
                this.netherWitch.setPhase(Phase.CHASING);
                this.attackTime = Mth.floor(50 * attackTimeMultiplier);
                this.attackStep = 0;
            }
        }

        public void tick() {
            --this.attackTime;
            Optional<CovenMaster> master = this.netherWitch.getMaster();
            if (master.isPresent()) {
                CovenMaster covenMaster = master.get();
                LivingEntity target = this.netherWitch.getTarget();
                if (this.netherWitch.isValidTarget(target, covenMaster)) {
                    boolean canSee = this.netherWitch.getSensing().hasLineOfSight(target);

                    if (canSee) {
                        this.lastSeen = 0;
                    } else {
                        ++this.lastSeen;
                    }

                    double d0 = this.netherWitch.distanceToSqr(target);
                    if ((d0 < 4.0D) && !(this.netherWitch.level.getBlockState(target.blockPosition()) == BlockRegistry.COVEN_ROOTS.get().defaultBlockState())) {
                        if (!canSee) {
                            return;
                        }

                        // Lava Attack
                        if (this.attackTime <= 0) {
                            this.attackTime = 20;
                            if (!target.isInLava() && !(this.netherWitch.level.getBlockState(target.blockPosition()) == Blocks.BEDROCK.defaultBlockState()))
                                this.netherWitch.level.setBlock(target.blockPosition().below(), Blocks.LAVA.defaultBlockState(), 3);
                        }
                    } else if (d0 < this.rangedRadius * this.rangedRadius && canSee) {
                        // Fire off fireballs
                        double d1 = target.getX() - this.netherWitch.getX();
                        double d2 = target.getY(0.5D) - this.netherWitch.getY(0.5D);
                        double d3 = target.getZ() - this.netherWitch.getZ();
                        if (this.attackTime <= 0) {

                            int playerNumber = covenMaster.getNearbyPlayerNumber();
                            float largeFireProbLim = (playerNumber < 3) ? (covenMaster.getNearbyPlayerNumber() * 0.25f) : 0.75f;
                            boolean fireLargeFireballs = (this.netherWitch.isEnraged && (this.netherWitch.level.random.nextFloat() < largeFireProbLim));

                            handleAttackCounter(this.netherWitch.attackTimeMultiplier(covenMaster.getNearbyPlayerNumber()), fireLargeFireballs);

                            if (this.attackStep > 1) {
                                double d4 = Math.sqrt(Math.sqrt(d0)) * 0.5D;
                                if (!this.netherWitch.isSilent()) {
                                    this.netherWitch.level.levelEvent((Player) null, 1018, this.netherWitch.blockPosition(), 0);
                                }

                                int fireballNum = ((d0 < this.evadeRadius * this.evadeRadius) && !fireLargeFireballs) ? 8 : 1;

                                for (int i = 0; i < fireballNum; ++i) {
                                    double dx = d1 + this.netherWitch.getRandom().nextGaussian() * d4;
                                    double dz = d3 + this.netherWitch.getRandom().nextGaussian() * d4;
                                    float theta = Mth.PI/4 * i;

                                     if (fireLargeFireballs) {
                                        LargeFireball largeFireball = new LargeFireball(this.netherWitch.level, this.netherWitch, Mth.cos(theta) * dx - Mth.sin(theta) * dz, d2, Mth.sin(theta) * dx + Mth.cos(theta) * dz, 1);
                                        largeFireball.setPos(largeFireball.getX(), this.netherWitch.getY(0.5D) + 0.5D, largeFireball.getZ());
                                        this.netherWitch.level.addFreshEntity(largeFireball);
                                    } else {
                                        SmallFireball smallfireball = new SmallFireball(this.netherWitch.level, this.netherWitch, Mth.cos(theta) * dx - Mth.sin(theta) * dz, d2, Mth.sin(theta) * dx + Mth.cos(theta) * dz);
                                        smallfireball.setPos(smallfireball.getX(), this.netherWitch.getY(0.5D) + 0.5D, smallfireball.getZ());
                                        this.netherWitch.level.addFreshEntity(smallfireball);
                                    }
                                }
                            }
                        }

                        this.netherWitch.getLookControl().setLookAt(target, 10.0F, 10.0F);
                    } else if (d0 < this.attackRadius * this.attackRadius) {
                        this.netherWitch.getNavigation().moveTo(target, 1.5D);
                    }

                    super.tick();
                }
            }
        }

    }

}
