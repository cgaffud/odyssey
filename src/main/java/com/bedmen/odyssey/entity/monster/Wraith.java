package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.items.odyssey_versions.OdysseyBowItem;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.SoundEventRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.weapon.WeaponUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;

public class Wraith extends Monster implements NeutralMob, RangedAttackMob {
    public final RangedBowAttackGoal<Wraith> bowGoal = new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F);
    public final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false) {
        public void stop() {
            super.stop();
            Wraith.this.setAggressive(false);
        }

        public void start() {
            super.start();
            Wraith.this.setAggressive(true);
        }
    };

    private int remainingPersistentAngerTime;
    @javax.annotation.Nullable
    private UUID persistentAngerTarget;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(30, 49);

    public Wraith(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.reassessWeaponGoal();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 60.0D).add(Attributes.MOVEMENT_SPEED, 0.4).add(Attributes.ATTACK_DAMAGE, 8.0D).add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    public void tick() {
        super.tick();
        if ((random.nextDouble() < 0.001)) {
            Player player = this.level.getNearestPlayer(this.getX(), this.getY(), this.getZ(), 20, true);
            if (player != null) {
                setPersistentAngerTarget(player.getUUID());
                startPersistentAngerTimer();
            }
        }
    }

    public void reassessWeaponGoal() {
        if (this.level != null && !this.level.isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem));
            if (itemstack.getItem() instanceof BowItem) {
                int i = 20;
                if (this.level.getDifficulty() != Difficulty.HARD)
                    i = 40;

                this.bowGoal.setMinAttackInterval(i);
                this.goalSelector.addGoal(2, this.bowGoal);
            } else {
                this.goalSelector.addGoal(2, this.meleeGoal);
            }

        }
    }

    @javax.annotation.Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_32146_, DifficultyInstance p_32147_, MobSpawnType p_32148_, @javax.annotation.Nullable SpawnGroupData p_32149_, @javax.annotation.Nullable CompoundTag p_32150_) {
        p_32149_ = super.finalizeSpawn(p_32146_, p_32147_, p_32148_, p_32149_, p_32150_);
        this.populateDefaultEquipmentSlots(p_32147_);
        this.populateDefaultEquipmentEnchantments(p_32147_);
        this.reassessWeaponGoal();
        this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * p_32147_.getSpecialMultiplier());
        return p_32149_;
    }

    public void readAdditionalSaveData(CompoundTag p_32152_) {
        super.readAdditionalSaveData(p_32152_);
        this.reassessWeaponGoal();
    }

    public void setItemSlot(EquipmentSlot p_32138_, ItemStack p_32139_) {
        super.setItemSlot(p_32138_, p_32139_);
        if (!this.level.isClientSide) {
            this.reassessWeaponGoal();
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.AMBIENT_CAVE;
    }

    protected SoundEvent getHurtSound(DamageSource DamageSource) {
        return SoundEventRegistry.WRAITH_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return SoundEventRegistry.WRAITH_DEATH.get();
    }

    @Override
    public void playAmbientSound() {
        SoundEvent soundevent = this.getAmbientSound();
        if (soundevent != null && (this.getRandom().nextBoolean() && this.getRandom().nextBoolean())) {
            this.playSound(soundevent, this.getSoundVolume(), this.getVoicePitch());
        }

    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem)));
        AbstractArrow abstractarrow = this.getArrow(itemstack, power);
        ItemStack mainHandItemStack = this.getMainHandItem();
        Item item = mainHandItemStack.getItem();
        if (item instanceof BowItem bowItem)
            abstractarrow = bowItem.customArrow(abstractarrow);
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - abstractarrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        float velocity = WeaponUtil.getMaxArrowVelocity(mainHandItemStack, false);
        float accuracyMultiplier = EnchantmentUtil.getAccuracyMultiplier(this);
        float superCharge = EnchantmentUtil.getSuperChargeMultiplier(mainHandItemStack);
        abstractarrow.shoot(d0, d1 + d3 * (double)(0.32f / velocity), d2, velocity, (float)(14 - this.level.getDifficulty().getId() * 4) * accuracyMultiplier / superCharge);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrow);
    }

    protected AbstractArrow getArrow(ItemStack p_32156_, float p_32157_) {
        return ProjectileUtil.getMobArrow(this, p_32156_, p_32157_);
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem p_32144_) {
        return p_32144_ == ItemRegistry.BOW.get();
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        super.populateDefaultEquipmentSlots(difficultyInstance);
        Item item = random.nextBoolean() ? ItemRegistry.VOID_BOW.get() : ItemRegistry.VOID_SWORD.get();
        this.setItemSlot(EquipmentSlot.MAINHAND, item.getDefaultInstance());
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
        this.remainingPersistentAngerTime = remainingPersistentAngerTime;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
    }

    public static boolean spawnPredicate(EntityType<? extends Monster> pType, ServerLevelAccessor pLevel, MobSpawnType pReason, BlockPos pPos, Random pRandom) {
        return Monster.checkMonsterSpawnRules(pType, pLevel, pReason, pPos, pRandom) && pPos.getY() <= -16;
    }

}
