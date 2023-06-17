package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.entity.ai.BoomerangAttackGoal;
import com.bedmen.odyssey.entity.ai.OdysseyRangedBowAttackGoal;
import com.bedmen.odyssey.event_listeners.EntityEvents;
import com.bedmen.odyssey.items.aspect_items.AspectArrowItem;
import com.bedmen.odyssey.items.aspect_items.AspectBowItem;
import com.bedmen.odyssey.items.aspect_items.AspectCrossbowItem;
import com.bedmen.odyssey.items.aspect_items.BoomerangItem;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public abstract class OdysseyAbstractSkeleton extends AbstractSkeleton implements CrossbowAttackMob, BoomerangAttackMob {
    private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(OdysseyAbstractSkeleton.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(OdysseyAbstractSkeleton.class, EntityDataSerializers.BOOLEAN);
    public final OdysseyRangedBowAttackGoal<OdysseyAbstractSkeleton> odysseyBowGoal = new OdysseyRangedBowAttackGoal<>(this, 1.0D, 20, 15.0F);
    public final RangedCrossbowAttackGoal<OdysseyAbstractSkeleton> crossBowGoal = new RangedCrossbowAttackGoal<>(this, 1.0D, 8.0F);
    public final BoomerangAttackGoal<OdysseyAbstractSkeleton> boomerangGoal = new BoomerangAttackGoal<>(this, 0.0D, 50, 15.0F);
    private boolean crossbowMode = false;
    private int noBoomerangTick;
    protected Item boomerangItem = null;
    protected OdysseyAbstractSkeleton(EntityType<? extends AbstractSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_BABY_ID, false);
        this.entityData.define(DATA_IS_CHARGING_CROSSBOW, false);
    }

    public void tick(){
        super.tick();
        if(this.isBaby() && !this.hasBoomerang()){
            if(this.noBoomerangTick < 300){
                this.noBoomerangTick++;
            } else {
                if(this.boomerangItem == null){
                    this.populateBabyEquipmentSlots();
                } else {
                    this.setItemInHand(InteractionHand.MAIN_HAND, this.boomerangItem.getDefaultInstance());
                }
                this.noBoomerangTick = 0;
            }
        } else {
            this.noBoomerangTick = 0;
        }
    }

    public boolean isBaby() {
        return this.getEntityData().get(DATA_BABY_ID);
    }

    public void setBaby(boolean b) {
        this.getEntityData().set(DATA_BABY_ID, b);
        if (!this.level.isClientSide) {
            AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            attributeinstance.removeModifier(SPEED_MODIFIER_BABY);
            if (b) {
                attributeinstance.addTransientModifier(SPEED_MODIFIER_BABY);
                if(this.boomerangItem == null){
                    this.populateBabyEquipmentSlots();
                }
            }
        }
    }

    protected int getExperienceReward(Player player) {
        if (this.isBaby()) {
            this.xpReward = (int)((float)this.xpReward * 2.5F);
        }
        return super.getExperienceReward(player);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_34307_) {
        if (DATA_BABY_ID.equals(p_34307_)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(p_34307_);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if(this.boomerangItem != null){
            compoundTag.put("BoomerangItem", this.boomerangItem.getDefaultInstance().save(new CompoundTag()));
        }
        compoundTag.putInt("NoBoomerangTick", this.noBoomerangTick);
        compoundTag.putBoolean("IsBaby", this.isBaby());
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        if(compoundTag.contains("BoomerangItem")){
            this.boomerangItem = ItemStack.of((CompoundTag) Objects.requireNonNull(compoundTag.get("BoomerangItem"))).getItem();
        }
        this.noBoomerangTick = compoundTag.getInt("NoBoomerangTick");
        this.setBaby(compoundTag.getBoolean("IsBaby"));
        super.readAdditionalSaveData(compoundTag);
    }

    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return this.isBaby() ? 0.93F : 1.74F;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        if(EntityEvents.isBaby(this)){
            this.setBaby(true);
        }
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    public double getMyRidingOffset() {
        return this.isBaby() ? 0.0D : -0.45D;
    }

    protected AbstractArrow getOdysseyArrow(ItemStack ammo, float bowDamageMultiplier) {
        AspectArrowItem aspectArrowItem = (AspectArrowItem)(ammo.getItem() instanceof AspectArrowItem ? ammo.getItem() : Items.ARROW);
        AbstractArrow abstractarrow = aspectArrowItem.createArrow(this.level, ammo, this);
        abstractarrow.setEnchantmentEffectsFromEntity(this, bowDamageMultiplier);
        return abstractarrow;
    }

    public void performRangedAttack(LivingEntity target, float bowDamageMultiplier) {
        if(!this.crossbowMode){
            ItemStack bow = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem));
            ItemStack ammoStack = this.getProjectile(bow);
            AbstractArrow abstractarrow = this.getOdysseyArrow(ammoStack, bowDamageMultiplier);
            Item item = bow.getItem();
            if (item instanceof BowItem  bowItem)
                abstractarrow = bowItem.customArrow(abstractarrow);
            double d0 = target.getX() - this.getX();
            double d1 = target.getY(0.3333333333333333D) - abstractarrow.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            float velocity = WeaponUtil.getMaxArrowVelocity(bow, false);
            float accuracyMultiplier = 1.0f + AspectUtil.getFloatAspectStrength(bow, Aspects.ACCURACY);
            abstractarrow.shoot(d0, d1 + d3 * (double)(0.32f / velocity), d2, velocity, (float)(14 - this.level.getDifficulty().getId() * 4) * accuracyMultiplier);
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(abstractarrow);
        } else {
            InteractionHand interactionhand = ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof CrossbowItem);
            ItemStack crossbow = this.getItemInHand(interactionhand);
            if (this.isHolding(is -> is.getItem() instanceof AspectCrossbowItem)) {
                AspectCrossbowItem.performShooting(this.level, this, interactionhand, crossbow, 0.0f, (float)(14 - this.level.getDifficulty().getId() * 4));
            }
            this.onCrossbowAttackPerformed();
        }
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        super.populateDefaultEquipmentSlots(difficultyInstance);
        Item item;
        if(this.isBaby()){
            populateBabyEquipmentSlots();
        } else {
            item = switch(random.nextInt(19)){
                default -> ItemRegistry.BOW.get();
                case 0 -> ItemRegistry.BOWN.get();
                case 1 -> ItemRegistry.BONE_SLUG_BOW.get();
                case 2 -> ItemRegistry.BONE_LONG_BOW.get();
                case 3 -> ItemRegistry.BONE_REPEATER.get();
            };
            this.setItemSlot(EquipmentSlot.MAINHAND, item.getDefaultInstance());
        }
    }

    protected void populateBabyEquipmentSlots() {
        Item item = switch(random.nextInt(5)){
            default -> ItemRegistry.WOODEN_BOOMERANG.get();
            case 1, 2 -> ItemRegistry.BONE_BOOMERANG.get();
            case 0 -> ItemRegistry.BONERANG.get();
        };
        this.boomerangItem = item;
        this.setItemSlot(EquipmentSlot.MAINHAND, item.getDefaultInstance());
        this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
    }

    public void reassessWeaponGoal() {
        if (!this.level.isClientSide) {
            this.crossbowMode = false;
            if(this.isBaby()){
                if (this.goalSelector.getAvailableGoals().stream()
                        .noneMatch(wrappedGoal -> wrappedGoal.getGoal() == this.boomerangGoal)){
                    ItemStack boomerang = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BoomerangItem));
                    if(boomerang.getItem() instanceof BoomerangItem boomerangItem){
                        int i = boomerangItem.getBaseMaxChargeTicks();
                        if (this.level.getDifficulty() == Difficulty.HARD) {
                            i *= 3;
                        } else {
                            i *= 5;
                        }
                        this.boomerangGoal.setMinAttackInterval(i);
                    }
                    this.goalSelector.addGoal(4, this.boomerangGoal);
                }
                return;
            }
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            this.goalSelector.removeGoal(this.odysseyBowGoal);
            this.goalSelector.removeGoal(this.crossBowGoal);
            this.goalSelector.removeGoal(this.boomerangGoal);
            ItemStack bow = this.getItemInHand(WeaponUtil.getHandHoldingBow(this));
            Item bowItem = bow.getItem();
            ItemStack crossbow = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof CrossbowItem));
            Item crossbowItem = crossbow.getItem();
            if (bowItem instanceof AspectBowItem) {
                int i = WeaponUtil.getRangedMaxChargeTicks(bow);
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
        return this.entityData.get(DATA_IS_CHARGING_CROSSBOW);
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
    public void setChargingCrossbow(boolean isChargingCrossbow) {
        this.entityData.set(DATA_IS_CHARGING_CROSSBOW, isChargingCrossbow);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity livingEntity, ItemStack crossbow, Projectile projectile, float angle) {
        float velocity = WeaponUtil.getMaxArrowVelocity(crossbow, false);
        this.shootCrossbowProjectile(this, livingEntity, projectile, angle, velocity);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    public boolean hasBoomerang(){
        return getBoomerangHand() != null;
    }

    public InteractionHand getBoomerangHand(){
        if(this.getMainHandItem().getItem() instanceof BoomerangItem){
            return InteractionHand.MAIN_HAND;
        }
        if(this.getOffhandItem().getItem() instanceof BoomerangItem){
            return InteractionHand.OFF_HAND;
        }
        return null;
    }

    public void performBoomerangAttack(LivingEntity target) {
        if(this.hasBoomerang()){
            InteractionHand hand = this.getBoomerangHand();
            ItemStack itemstack = new ItemStack(this.getItemInHand(hand).getItem());
            BoomerangItem boomerangItem = (BoomerangItem) itemstack.getItem();
            boomerangItem.releaseThrownEntity(itemstack, this.level, this, Optional.of(target));
            this.setItemInHand(hand, ItemStack.EMPTY);
        }
    }
}
