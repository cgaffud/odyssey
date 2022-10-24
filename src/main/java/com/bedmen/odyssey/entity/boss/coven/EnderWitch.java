package com.bedmen.odyssey.entity.boss.coven;

import com.bedmen.odyssey.entity.ai.CovenReturnToMasterGoal;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanHead;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanMaster;
import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class EnderWitch extends CovenWitch implements RangedAttackMob {
    private static final UUID SPEED_MODIFIER_DRINKING_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
    private static final AttributeModifier SPEED_MODIFIER_DRINKING = new AttributeModifier(SPEED_MODIFIER_DRINKING_UUID, "Drinking speed penalty", -0.25D, AttributeModifier.Operation.ADDITION);
    private static final EntityDataAccessor<Boolean> DATA_USING_ITEM = SynchedEntityData.defineId(Witch.class, EntityDataSerializers.BOOLEAN);
    private int usingTime;
    private Phase phase = Phase.IDLE;

    public EnderWitch(EntityType<? extends CovenWitch> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CovenReturnToMasterGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EnderWitchRangedAttackGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_USING_ITEM, false);
    }

    public void setUsingItem(boolean isUsing) {
        this.getEntityData().set(DATA_USING_ITEM, isUsing);
    }

    public boolean isDrinkingPotion() {
        return this.getEntityData().get(DATA_USING_ITEM);
    }

    private boolean isValidTarget(LivingEntity target, CovenMaster covenMaster) { return ((target != null) && (covenMaster.validTargetPredicate((ServerPlayer) target))); }

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
                            List<LivingEntity> otherTargets = covenMaster.getOtherTargets(this).stream().filter(livingEntity -> this.isValidTarget(livingEntity, covenMaster)).collect(Collectors.toList());
                            if (otherTargets.isEmpty()) {
                                Collection<ServerPlayer> serverPlayerEntities = covenMaster.bossEvent.getPlayers();
                                List<ServerPlayer> serverPlayerEntityList = serverPlayerEntities.stream().filter(covenMaster::validTargetPredicate).collect(Collectors.toList());
                                // Set Phase based on Target
                                if (serverPlayerEntityList.isEmpty()) {
                                    this.setTarget(null);
                                } else {
                                    this.phase = Phase.CHASING;
                                    this.setTarget(serverPlayerEntityList.get(this.random.nextInt(serverPlayerEntityList.size())));
                                }
                            } else {
                                this.phase = Phase.CHASING;
                                this.setTarget(otherTargets.get(this.random.nextInt(otherTargets.size())));
                            }
                        }
                }
                // Handle Potions
                // If she's drinking a potion let her finish
                if (this.isDrinkingPotion()) {
                    if (this.usingTime-- <= 0) {
                        this.setUsingItem(false);
                        ItemStack itemstack = this.getMainHandItem();
                        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        if (itemstack.is(Items.POTION)) {
                            List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);
                            if (list != null) {
                                for (MobEffectInstance mobeffectinstance : list) {
                                    this.addEffect(new MobEffectInstance(mobeffectinstance));
                                }
                            }
                        }

                        this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_DRINKING);
                    }
                } else {
                    // Otherwise, fill her hand with a new potion
                    Potion potion = null;
                    if (this.isEyeInFluid(FluidTags.WATER) && !this.hasEffect(MobEffects.WATER_BREATHING)) {
                        potion = Potions.WATER_BREATHING;
                    } else if (this.random.nextFloat() < 0.65F && (this.isOnFire() || this.getLastDamageSource() != null && this.getLastDamageSource().isFire()) && !this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                        potion = Potions.FIRE_RESISTANCE;
                        // Todo: Curently disabled, will figure out how to have this communicate with master later
//                } else if (this.random.nextFloat() < 0.25F && this.getHealth() < this.getMaxHealth()) {
//                    potion = Potions.HEALING;
                    } else if (this.random.nextFloat() < 0.45F && this.getTarget() != null && !this.hasEffect(MobEffects.MOVEMENT_SPEED) && this.getTarget().distanceToSqr(this) > 121.0D) {
                        potion = Potions.SWIFTNESS;
                    }

                    if (potion != null) {
                        this.setItemSlot(EquipmentSlot.MAINHAND, PotionUtils.setPotion(new ItemStack(Items.POTION), potion));
                        this.usingTime = this.getMainHandItem().getUseDuration();
                        this.setUsingItem(true);
                        if (!this.isSilent()) {
                            this.level.playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_DRINK, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
                        }

                        AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
                        attributeinstance.removeModifier(SPEED_MODIFIER_DRINKING);
                        attributeinstance.addTransientModifier(SPEED_MODIFIER_DRINKING);
                    }
                }
                if (this.random.nextFloat() < 7.5E-4F) {
                    this.level.broadcastEntityEvent(this, (byte) 15);
                }
            }
        }

        super.aiStep();
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if (damageSource instanceof IndirectEntityDamageSource) {
            for(int i = 0; i < 64; ++i) {
                if (this.teleport()) {
                    return true;
                }
            }

            return false;
        } else {
            boolean flag = super.hurt(damageSource, amount);
            if (!this.level.isClientSide() && !(damageSource.getEntity() instanceof LivingEntity) && this.random.nextInt(10) != 0)
                this.teleport();

            return flag;
        }
    }

    public void performRangedAttack(LivingEntity target, float p_34144_) {
        if (!this.isDrinkingPotion()) {
            Vec3 vec3 = target.getDeltaMovement();
            double d0 = target.getX() + vec3.x - this.getX();
            double d1 = target.getEyeY() - (double)1.1F - this.getY();
            double d2 = target.getZ() + vec3.z - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            Potion potion = Potions.HARMING;
            if (d3 >= 8.0D && !target.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                potion = Potions.STRONG_SLOWNESS;
            } else if (target.getHealth() >= 8.0F && !target.hasEffect(MobEffects.POISON)) {
                potion = Potions.STRONG_POISON;
            } else if (d3 <= 3.0D && !target.hasEffect(MobEffects.WEAKNESS) && this.random.nextFloat() < 0.25F) {
                potion = Potions.LONG_WEAKNESS;
            }

            ThrownPotion thrownpotion = new ThrownPotion(this.level, this);
            thrownpotion.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), potion));
            thrownpotion.setXRot(thrownpotion.getXRot() - -20.0F);
            thrownpotion.shoot(d0, d1 + d3 * 0.2D, d2, 0.75F, 8.0F);
            if (!this.isSilent()) {
                this.level.playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_THROW, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
            }

            this.level.addFreshEntity(thrownpotion);
        }
    }

    // TODO: unsure if this is needed, this is the enderman's random tele
    protected boolean teleport() {
        if (!this.level.isClientSide() && this.isAlive()) {
            double randX = this.getX() + (this.random.nextDouble() - 0.5D) * 64.0D;
            double randY = this.getY() + (double)(this.random.nextInt(64) - 32);
            double randZ = this.getZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
            return this.teleport(randX, randY, randZ);
        } else {
            return false;
        }
    }

    @Override
    void doWhenReturnToMaster() {
        this.phase = Phase.IDLE;
    }

    enum Phase {
        IDLE,
        CHASING;
    }

    static class EnderWitchRangedAttackGoal extends Goal {
        private final Mob mob;
        private final EnderWitch enderWitch;
        @Nullable
        private LivingEntity target;
        private int attackTime = -1;
        private final double speedModifier;
        private int seeTime;
        private final int attackIntervalMin;
        private final int attackIntervalMax;
        private final float attackRadius;
        private final float tpRadiusSqr;
        private final float attackRadiusSqr;



        public EnderWitchRangedAttackGoal(EnderWitch enderWitch) {
            this.enderWitch = enderWitch;
            this.mob = (Mob)enderWitch;
            this.speedModifier = 1.5D;
            this.attackIntervalMin = 40;
            this.attackIntervalMax = 60;
            this.attackRadius = 25.0F;
            this.tpRadiusSqr = 15.0F * 15.0F;
            this.attackRadiusSqr = 25.0F * 25.0F;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null && livingentity.isAlive()) {
                this.target = livingentity;
                return true;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return this.canUse() || !this.mob.getNavigation().isDone();
        }

        public void stop() {
            this.target = null;
            this.seeTime = 0;
            this.attackTime = -1;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            double d0 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
            boolean flag = this.mob.getSensing().hasLineOfSight(this.target);
            if (flag) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }

            if (!(d0 > (double) this.attackRadiusSqr) && this.seeTime >= 5) {
                this.mob.getNavigation().stop();
            } else if (d0 > (double) this.tpRadiusSqr) {
                enderWitch.teleportTowards(this.target);
            } else {
                this.mob.getNavigation().moveTo(this.target, this.speedModifier);
            }

            this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            if (--this.attackTime == 0) {
                if (!flag) {
                    return;
                }

                float f = (float)Math.sqrt(d0) / this.attackRadius;
                float f1 = Mth.clamp(f, 0.1F, 1.0F);
                this.enderWitch.performRangedAttack(this.target, f1);
                this.attackTime = Mth.floor(f * (float)(this.attackIntervalMax - this.attackIntervalMin) + (float)this.attackIntervalMin);
            } else if (this.attackTime < 0) {
                this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d0) / (double)this.attackRadius, (double)this.attackIntervalMin, (double)this.attackIntervalMax));
            }

        }
    }

}
