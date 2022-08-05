package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.items.OdysseyCrossbowItem;
import com.bedmen.odyssey.items.equipment.base.EquipmentMeleeItem;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.WeaponUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class Bandit extends AbstractIllager implements CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Bandit.class, EntityDataSerializers.BOOLEAN);
    public final RangedCrossbowAttackGoal<Bandit> crossBowGoal = new RangedCrossbowAttackGoal<>(this, 1.0D, 8.0F);
    public final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.0D, false);
    public Bandit(EntityType<? extends Bandit> entityType, Level level) {
        super(entityType, level);
        this.reassessWeaponGoal();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CHARGING_CROSSBOW, false);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        spawnGroupData = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
        this.populateDefaultEquipmentSlots(difficultyInstance);
        return spawnGroupData;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        super.populateDefaultEquipmentSlots(difficultyInstance);
        Item item = switch(random.nextInt(10)){
            default -> ItemRegistry.CROSSBOW.get();
            case 0 -> ItemRegistry.BANDIT_DAGGER.get();
        };
        this.setItemSlot(EquipmentSlot.MAINHAND, item.getDefaultInstance());
        if (item instanceof EquipmentMeleeItem equipmentMeleeItem && equipmentMeleeItem.meleeWeaponClass.isDualWield) {
            this.setItemSlot(EquipmentSlot.OFFHAND, item.getDefaultInstance());
        }
        this.reassessWeaponGoal();
    }

    public void reassessWeaponGoal() {
        if (!this.level.isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.crossBowGoal);
            ItemStack crossbow = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof CrossbowItem));
            Item crossbowItem = crossbow.getItem();
            if(crossbowItem instanceof CrossbowItem) {
                this.goalSelector.addGoal(4, this.crossBowGoal);
            } else {
                this.goalSelector.addGoal(4, this.meleeGoal);
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.35F).add(Attributes.FOLLOW_RANGE, 12.0D).add(Attributes.MAX_HEALTH, 24.0D).add(Attributes.ARMOR, 4.0d).add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    public void applyRaidBuffs(int p_37844_, boolean p_37845_) {

    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }
    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    @Override
    public void setChargingCrossbow(boolean isChargingCrossbow) {
        this.entityData.set(IS_CHARGING_CROSSBOW, isChargingCrossbow);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity livingEntity, ItemStack crossbow, Projectile projectile, float angle) {
        float velocity = (crossbow.getItem() instanceof OdysseyCrossbowItem odysseyCrossbowItem ? odysseyCrossbowItem.getVelocityMultiplier() : 1.25f);
        this.shootCrossbowProjectile(this, livingEntity, projectile, angle, WeaponUtil.BASE_ARROW_VELOCITY_ENEMIES * velocity);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    public void performRangedAttack(LivingEntity target, float power) {
        InteractionHand interactionhand = ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof CrossbowItem);
        ItemStack crossbow = this.getItemInHand(interactionhand);
        if (this.isHolding(is -> is.getItem() instanceof OdysseyCrossbowItem)) {
            OdysseyCrossbowItem.performShooting(this.level, this, interactionhand, crossbow, 0.0f, (float)(14 - this.level.getDifficulty().getId() * 4));
        }
        this.onCrossbowAttackPerformed();
    }

    public AbstractIllager.IllagerArmPose getArmPose() {
        if (this.isChargingCrossbow()) {
            return AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem)) {
            return AbstractIllager.IllagerArmPose.CROSSBOW_HOLD;
        } else {
            return this.isAggressive() ? AbstractIllager.IllagerArmPose.ATTACKING : AbstractIllager.IllagerArmPose.NEUTRAL;
        }
    }
}
