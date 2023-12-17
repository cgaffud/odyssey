package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.items.aspect_items.AspectCrossbowItem;
import com.bedmen.odyssey.items.odyssey_versions.OdysseyMapItem;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.tags.OdysseyStructureTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class Bandit extends AbstractIllager implements CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Bandit.class, EntityDataSerializers.BOOLEAN);
    public final RangedCrossbowAttackGoal<Bandit> crossBowGoal = new RangedCrossbowAttackGoal<>(this, 1.0D, 8.0F);
    public final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.0D, false);

    // Logic for spawning maps to Bandit Hideout
    private boolean isHideoutSpawn = false;
    private final String HIDEOUT_SPAWN_TAG = "IsHideoutSpawnTag";
    private final float MAP_SPAWN_CHANCE = 1f;

    public Bandit(EntityType<? extends Bandit> entityType, Level level) {
        super(entityType, level);
        this.reassessWeaponGoal();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_CHARGING_CROSSBOW, false);
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag, boolean isHideoutSpawn) {
        this.isHideoutSpawn = isHideoutSpawn;
        spawnGroupData = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
        this.populateDefaultEquipmentSlots(this.random, difficultyInstance);
        return spawnGroupData;
    }

    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        super.populateDefaultEquipmentSlots(randomSource, difficultyInstance);
        Item item = switch(random.nextInt(7)){
            default -> ItemRegistry.CROSSBOW.get();
            case 0 -> ItemRegistry.BANDIT_DAGGER.get();
            case 1 -> ItemRegistry.BANDIT_CROSSBOW.get();
            case 2 -> ItemRegistry.GOLDEN_MACE.get();
            case 3,4 -> Items.GOLDEN_SWORD;
        };
        this.setItemSlot(EquipmentSlot.MAINHAND, item.getDefaultInstance());
        if(AspectUtil.getItemStackAspectStrength(item.getDefaultInstance(), Aspects.DUAL_WIELD)){
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

    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        super.setItemSlot(equipmentSlot, itemStack);
        if (!this.level.isClientSide) {
            this.reassessWeaponGoal();
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
        return this.entityData.get(DATA_IS_CHARGING_CROSSBOW);
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

    public void performRangedAttack(LivingEntity target, float power) {
        InteractionHand interactionhand = ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof CrossbowItem);
        ItemStack crossbow = this.getItemInHand(interactionhand);
        if (this.isHolding(is -> is.getItem() instanceof AspectCrossbowItem)) {
            AspectCrossbowItem.performShooting(this.level, this, interactionhand, crossbow, 0.0f, (float)(14 - this.level.getDifficulty().getId() * 4));
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

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean(this.HIDEOUT_SPAWN_TAG, this.isHideoutSpawn);
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.reassessWeaponGoal();
        this.isHideoutSpawn = compoundTag.getBoolean(this.HIDEOUT_SPAWN_TAG);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        Entity entity = pSource.getEntity();
        if (!this.isHideoutSpawn && (this.getRandom().nextFloat() < this.MAP_SPAWN_CHANCE) && (entity instanceof Player) && (!this.level.isClientSide())) {
            ServerLevel serverLevel = (ServerLevel) this.level;
            BlockPos blockpos = serverLevel.findNearestMapStructure(OdysseyStructureTags.ON_BANDIT_HIDEOUT_MAPS, entity.blockPosition(), 100, true);
            if (blockpos != null) {
                ItemStack itemstack = OdysseyMapItem.create(serverLevel, blockpos.getX(), blockpos.getZ(), (byte)1, true, true);
                MapItem.renderBiomePreviewMap(serverLevel, itemstack);
                itemstack.setHoverName(Component.translatable("filled_map.bandit_hideout"));
                this.spawnAtLocation(itemstack);
             } else {
                return;
            }
        }
    }
}
