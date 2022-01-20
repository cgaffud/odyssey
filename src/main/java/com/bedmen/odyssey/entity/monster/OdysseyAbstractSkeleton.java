package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.entity.ai.OdysseyRangedBowAttackGoal;
import com.bedmen.odyssey.items.OdysseyBowItem;
import com.bedmen.odyssey.items.OdysseyCrossbowItem;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.WeaponUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public abstract class OdysseyAbstractSkeleton extends AbstractSkeleton implements CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(OdysseyAbstractSkeleton.class, EntityDataSerializers.BOOLEAN);
    public final OdysseyRangedBowAttackGoal<OdysseyAbstractSkeleton> odysseyBowGoal = new OdysseyRangedBowAttackGoal<>(this, 1.0D, 20, 15.0F);
    public final RangedCrossbowAttackGoal<OdysseyAbstractSkeleton> crossBowGoal = new RangedCrossbowAttackGoal<>(this, 1.0D, 8.0F);
    private boolean crossbowMode = false;
    protected OdysseyAbstractSkeleton(EntityType<? extends AbstractSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CHARGING_CROSSBOW, false);
    }


    public void performRangedAttack(LivingEntity target, float power) {
        if(!this.crossbowMode){
            ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem)));
            AbstractArrow abstractarrow = this.getArrow(itemstack, power);
            Item item = this.getMainHandItem().getItem();
            if (item instanceof net.minecraft.world.item.BowItem)
                abstractarrow = ((net.minecraft.world.item.BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrow);
            double d0 = target.getX() - this.getX();
            double d1 = target.getY(0.3333333333333333D) - abstractarrow.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            abstractarrow.shoot(d0, d1 + d3 * (double)0.2F, d2, WeaponUtil.BASE_ARROW_VELOCITY_ENEMIES * (item instanceof OdysseyBowItem odysseyBowItem ? odysseyBowItem.getVelocity() : 1.0f), (float)(14 - this.level.getDifficulty().getId() * 4));
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(abstractarrow);
        } else {
            InteractionHand interactionhand = ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof CrossbowItem);
            ItemStack crossbow = this.getItemInHand(interactionhand);
            if (this.isHolding(is -> is.getItem() instanceof OdysseyCrossbowItem)) {
                OdysseyCrossbowItem.performShooting(this.level, this, interactionhand, crossbow, 0.0f, (float)(14 - this.level.getDifficulty().getId() * 4));
            }
            this.onCrossbowAttackPerformed();
        }
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        super.populateDefaultEquipmentSlots(difficultyInstance);
        Item item = switch(random.nextInt(19)){
            default -> ItemRegistry.BOW.get();
            case 0 -> ItemRegistry.BOWN.get();
            case 1 -> ItemRegistry.BONE_SLUG_BOW.get();
            case 2 -> ItemRegistry.BONE_LONG_BOW.get();
            case 3 -> ItemRegistry.BONE_REPEATER.get();
        };
        this.setItemSlot(EquipmentSlot.MAINHAND, item.getDefaultInstance());
    }

    public void reassessWeaponGoal() {
        if (!this.level.isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            this.goalSelector.removeGoal(this.odysseyBowGoal);
            this.goalSelector.removeGoal(this.crossBowGoal);
            this.crossbowMode = false;
            ItemStack bow = this.getItemInHand(WeaponUtil.getHandHoldingBow(this));
            Item bowItem = bow.getItem();
            ItemStack crossbow = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.CrossbowItem));
            Item crossbowItem = crossbow.getItem();
            if (bowItem instanceof OdysseyBowItem) {
                int i = ((OdysseyBowItem) bowItem).getChargeTime(bow);
                if (this.level.getDifficulty() != Difficulty.HARD) {
                    i *= 2;
                }
                this.odysseyBowGoal.setMinAttackInterval(i);
                this.goalSelector.addGoal(4, this.odysseyBowGoal);
            } else if(crossbowItem instanceof CrossbowItem) {
                this.goalSelector.addGoal(4, this.crossBowGoal);
                this.crossbowMode = true;
            } else {
                this.goalSelector.addGoal(4, this.meleeGoal);
            }
        }
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem projectileWeaponItem) {
        return projectileWeaponItem instanceof CrossbowItem;
    }

    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    public HumanoidModel.ArmPose getArmPose() {
        if (this.isChargingCrossbow()) {
            return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem)) {
            return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        } else {
            ItemStack itemstack = this.getItemInHand(InteractionHand.MAIN_HAND);
            if (itemstack.getItem() instanceof BowItem && this.isAggressive()) {
                return HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
            return HumanoidModel.ArmPose.EMPTY;
        }
    }

    @Override
    public void setChargingCrossbow(boolean b) {
        this.entityData.set(IS_CHARGING_CROSSBOW, b);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity livingEntity, ItemStack crossbow, Projectile projectile, float angle) {
        float velocity = (crossbow.getItem() instanceof OdysseyCrossbowItem odysseyCrossbowItem ? odysseyCrossbowItem.getVelocity() : 1.25f);
        this.shootCrossbowProjectile(this, livingEntity, projectile, angle, WeaponUtil.BASE_ARROW_VELOCITY_ENEMIES * velocity);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }
}
