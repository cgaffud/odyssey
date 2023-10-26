package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.entity.projectile.WraithAmalgamProjectile;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.SoundEventRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.List;
import java.util.stream.Collectors;

public class WraithAmalgam extends AbstractWraith implements RangedAttackMob {


    private int attackAnimationTick;
    private int screamTick;

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(40, 59);

    public WraithAmalgam(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_,0.2D, 0.65D, 0.3D, 2.5D,15);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WraithAmalgamAttackGoal(this,20, 10.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.attackAnimationTick > 0) {
            --this.attackAnimationTick;
        }

        if (this.screamTick > 0) {
            --this.screamTick;
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 60.0D).add(Attributes.MOVEMENT_SPEED, 1.2D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem item) {
        return item == ItemRegistry.BOW.get();
    }

    @javax.annotation.Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @javax.annotation.Nullable SpawnGroupData spawnGroupData, @javax.annotation.Nullable CompoundTag compoundTag) {
        spawnGroupData = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
        this.populateDefaultEquipmentSlots(this.random, difficultyInstance);
        this.populateDefaultEquipmentEnchantments(this.random, difficultyInstance);
        this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * difficultyInstance.getSpecialMultiplier());
        return spawnGroupData;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        super.populateDefaultEquipmentSlots(randomSource, difficultyInstance);
        this.setItemSlot(EquipmentSlot.MAINHAND, ItemRegistry.VOID_BOW.get().getDefaultInstance());
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float bowDamageMultiplier) {
        ItemStack bow = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem));
        ItemStack ammoStack = this.getProjectile(bow);
        AbstractArrow abstractarrow = this.getOdysseyArrow(ammoStack, bowDamageMultiplier);
        Item item = bow.getItem();
        if (item instanceof BowItem bowItem)
            abstractarrow = bowItem.customArrow(abstractarrow);
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - abstractarrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        float velocity = WeaponUtil.getMaxArrowVelocity(bow, false);
        float accuracyMultiplier = 1.0f + AspectUtil.getOneHandedEntityTotalAspectValue(this, this.getUsedItemHand(), Aspects.ACCURACY).orElse(0f);
        abstractarrow.shoot(d0, d1 + d3 * (double)(0.32f / velocity), d2, velocity, (float)(14 - this.level.getDifficulty().getId() * 4) * accuracyMultiplier);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrow);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        this.attackAnimationTick = 10;
        this.level.broadcastEntityEvent(this, (byte)4);
        float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean flag = pEntity.hurt(DamageSource.mobAttack(this).setScalesWithDifficulty(), f);
        if (flag) {
            pEntity.setDeltaMovement(pEntity.getDeltaMovement().add(0.0D, (double)0.4F, 0.0D));
            this.doEnchantDamageEffects(this, pEntity);
        }

        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    public void startScreaming() {
        this.screamTick = 60;
        level.playSound(null, this.blockPosition(), SoundEventRegistry.WRAITH_SCREAM.get(), SoundSource.HOSTILE, 1.0f, 1.0f);
        this.level.broadcastEntityEvent(this, (byte)5);
    }

    public void handleEntityEvent(byte b) {
        if (b == 4) {
            this.attackAnimationTick = 10;
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        } else if (b == 5) {
            this.screamTick = 60;
        } else super.handleEntityEvent(b);
    }

    public int getAttackAnimationTick() {
        return this.attackAnimationTick;
    }

    public int getScreamTick() {
        return this.screamTick;
    }

    public boolean isScreaming() {
        return (this.screamTick > 0);
    }

    public class WraithAmalgamAttackGoal extends WraithBowAttackGoal {

        private int ticksUntilNextMeleeAttack;
        private int ticksUntilNextRangedAttack;
        private int TARGET_SWITCHUP_RANGE = 20;

        public WraithAmalgamAttackGoal(Mob Mob, int attackIntervalMin, float attackRadius) {
            super(Mob, attackIntervalMin, attackRadius);
        }

        @Override
        public void start() {
            super.start();
            this.ticksUntilNextMeleeAttack = 0;
            this.ticksUntilNextRangedAttack = 0;
        }

        @Override
        public void tick() {
            super.tick();
            LivingEntity target = this.mob.getTarget();
            if (target != null && (this.mob instanceof WraithAmalgam wraithAmalgam)) {
                if (wraithAmalgam.isScreaming()) {
                    wraithAmalgam.moveControlStop();
                    if (wraithAmalgam.getScreamTick() % 20 == 0) {
                        // This awful one-line collects all players in range of TARGET_SWITCHUP_RANGE that have line of sight with the mob
                        List<Player> nearbyPlayers = wraithAmalgam.level.getNearbyPlayers(TargetingConditions.forCombat().range(this.TARGET_SWITCHUP_RANGE), wraithAmalgam, wraithAmalgam.getBoundingBox().inflate(this.TARGET_SWITCHUP_RANGE)).stream().filter(player -> player.hasLineOfSight(wraithAmalgam)).collect(Collectors.toList());
                        LivingEntity offshootTarget = target;
                        if (!nearbyPlayers.isEmpty())
                            offshootTarget = nearbyPlayers.get(wraithAmalgam.getRandom().nextInt(nearbyPlayers.size()));

                        WraithAmalgam.this.level.addFreshEntity(new WraithAmalgamProjectile(WraithAmalgam.this.level, WraithAmalgam.this, offshootTarget));
                    }
                } else {
                    double d0 = wraithAmalgam.distanceToSqr(target.getX(), target.getY(), target.getZ());
                    double reach = this.getAttackReachSqr(target);
                    this.ticksUntilNextMeleeAttack = Math.max(this.ticksUntilNextMeleeAttack - 1, 0);
                    this.ticksUntilNextRangedAttack = Math.max(this.ticksUntilNextRangedAttack - 1, 0);
                    if (this.ticksUntilNextMeleeAttack <= 0 && d0 <= reach) {
                        this.resetMeleeAttackCooldown();
                        wraithAmalgam.doHurtTarget(target);
                    } else if (this.ticksUntilNextRangedAttack <= 0) {
                        this.resetRangedAttackCooldown();
                        wraithAmalgam.startScreaming();
                    }
                }
            }
        }

        protected void resetMeleeAttackCooldown() {
            this.ticksUntilNextMeleeAttack = this.adjustedTickDelay(40);
        }

        protected void resetRangedAttackCooldown() {
            this.ticksUntilNextRangedAttack = this.adjustedTickDelay(200);
        }

        protected double getAttackReachSqr(LivingEntity livingEntity) {
            return (this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + livingEntity.getBbWidth());
        }

    }
}
