package com.bedmen.odyssey.entity.boss.coven;

import com.bedmen.odyssey.entity.ai.CovenReturnToMasterGoal;
import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NetherWitch extends CovenWitch {
    private Phase phase = Phase.IDLE;

    public NetherWitch(EntityType<? extends CovenWitch> entityType, Level level) {
        super(entityType, level);
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

    @Override
    protected void doWhenReturnToMaster() {
        this.phase = Phase.IDLE;
    }

    enum Phase {
        IDLE,
        CHASING
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

        private void handleAttackCounter(float attackTimeMultiplier) {
            ++this.attackStep;
            // Light?
            if (this.attackStep == 1) {
                this.attackTime = 30;
            // Release fireballs
            } else if (this.attackStep <= 6) {
                this.attackTime = 10;
            // Recharge
            } else {
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
                    if (d0 < 4.0D) {
                        if (!canSee) {
                            return;
                        }

                        // Change this to be lava attack.
                        if (this.attackTime <= 0) {
                            this.attackTime = 20;
                            this.netherWitch.doHurtTarget(target);
                        }

                        this.netherWitch.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.0D);

                    } else if (d0 < this.rangedRadius * this.rangedRadius && canSee) {
                        // Fire off fireballs
                        double d1 = target.getX() - this.netherWitch.getX();
                        double d2 = target.getY(0.5D) - this.netherWitch.getY(0.5D);
                        double d3 = target.getZ() - this.netherWitch.getZ();
                        if (this.attackTime <= 0) {

                            handleAttackCounter(this.netherWitch.attackTimeMultiplier(covenMaster.getNearbyPlayerNumber()));

                            if (this.attackStep > 1) {
                                double d4 = Math.sqrt(Math.sqrt(d0)) * 0.5D;
                                if (!this.netherWitch.isSilent()) {
                                    this.netherWitch.level.levelEvent((Player) null, 1018, this.netherWitch.blockPosition(), 0);
                                }

                                int fireballNum = (d0 < this.evadeRadius * this.evadeRadius) ? 8 : 1;

                                for (int i = 0; i < fireballNum; ++i) {
                                    double dx = d1 + this.netherWitch.getRandom().nextGaussian() * d4;
                                    double dz = d3 + this.netherWitch.getRandom().nextGaussian() * d4;
                                    float theta = Mth.PI/4 * i;
                                    SmallFireball smallfireball = new SmallFireball(this.netherWitch.level, this.netherWitch,  Mth.cos(theta)*dx - Mth.sin(theta)*dz, d2, Mth.sin(theta)*dx + Mth.cos(theta)*dz);
                                    smallfireball.setPos(smallfireball.getX(), this.netherWitch.getY(0.5D) + 0.5D, smallfireball.getZ());
                                    this.netherWitch.level.addFreshEntity(smallfireball);
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
