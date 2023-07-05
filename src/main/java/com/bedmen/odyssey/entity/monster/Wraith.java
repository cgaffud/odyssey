package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.mojang.math.Vector3f;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
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

public class Wraith extends AbstractWraith implements RangedAttackMob {
    public final WraithBowAttackGoal bowGoal = new WraithBowAttackGoal(this, 20, 10.0F);
    public final WraithMeleeAttackGoal meleeGoal = new WraithMeleeAttackGoal(this);
    private final float METAMORPHOSIS_CHANCE = 0.1f;
    private int metamorphosisTick;


    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(30, 49);

    public Wraith(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_, 0.2D, 0.65D, 0.3D, 2.5D, 20);
        this.metamorphosisTick = 0;
        this.reassessWeaponGoal();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 60.0D).add(Attributes.MOVEMENT_SPEED, 1.2D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    public void setAggressive(boolean aggressive) {
        if (aggressive && (this.getRandom().nextFloat() < this.METAMORPHOSIS_CHANCE)) {
            this.forgetCurrentTargetAndRefreshUniversalAnger();
            this.metamorphosisTick = 60;
            this.level.broadcastEntityEvent(this, (byte)4);
            this.goalSelector.removeAllGoals();
        }
        super.setAggressive(aggressive);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.metamorphosisTick > 0) {
            --this.metamorphosisTick;
            if (!this.level.isClientSide()){
                RandomSource randomSource = this.getRandom();
                ServerLevel serverLevel = (ServerLevel) this.level;
                for(int i = 0; i < 8; ++i) {
                    (serverLevel).sendParticles(new DustParticleOptions(new Vector3f(0.35f, 0.35f, 0.35f), 1.0F), this.getX() + (randomSource.nextFloat()-0.5f)/2, this.getEyeY() + (randomSource.nextFloat()), this.getZ() + (randomSource.nextFloat()-0.5f)/2, 2, 0.2D, 0.2D, 0.2D, 0.0D);
                }
                if (this.metamorphosisTick == 0) {
                    AbstractWraith wraith;
                    if (randomSource.nextFloat() < 0.4) {
                        wraith = EntityTypeRegistry.WRAITH_STALKER.get().create(this.level);
                    } else {
                        wraith = EntityTypeRegistry.WRAITH_AMALGAM.get().create(this.level);
                    }
                    wraith.setPos(this.getX(), this.getY(), this.getZ());
                    wraith.finalizeSpawn(serverLevel, this.level.getCurrentDifficultyAt(wraith.blockPosition()), MobSpawnType.TRIGGERED, (SpawnGroupData) null, (CompoundTag) null);
                    serverLevel.addFreshEntity(wraith);
                    this.discard();
                }
            }
        }
    }

    public int getMetamorphosisTick() {
        return this.metamorphosisTick;
    }

    public void handleEntityEvent(byte b) {
        if (b == 4) {
            this.metamorphosisTick = 60;
        } else super.handleEntityEvent(b);
    }

    public void reassessWeaponGoal() {
        if (!this.level.isClientSide) {
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @javax.annotation.Nullable SpawnGroupData spawnGroupData, @javax.annotation.Nullable CompoundTag compoundTag) {
        spawnGroupData = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
        this.populateDefaultEquipmentSlots(this.random, difficultyInstance);
        this.populateDefaultEquipmentEnchantments(this.random, difficultyInstance);
        this.reassessWeaponGoal();
        this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * difficultyInstance.getSpecialMultiplier());
        return spawnGroupData;
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.reassessWeaponGoal();
    }

    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        super.setItemSlot(equipmentSlot, itemStack);
        if (!this.level.isClientSide) {
            this.reassessWeaponGoal();
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float bowDamageMultiplier) {
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
        float accuracyMultiplier = 1.0f + AspectUtil.getOneHandedTotalAspectStrength(this, this.getUsedItemHand(), Aspects.ACCURACY);
        abstractarrow.shoot(d0, d1 + d3 * (double)(0.32f / velocity), d2, velocity, (float)(14 - this.level.getDifficulty().getId() * 4) * accuracyMultiplier);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrow);
    }

    protected AbstractArrow getArrow(ItemStack p_32156_, float p_32157_) {
        return ProjectileUtil.getMobArrow(this, p_32156_, p_32157_);
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem p_32144_) {
        return p_32144_ == ItemRegistry.BOW.get();
    }

    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        super.populateDefaultEquipmentSlots(randomSource, difficultyInstance);
        Item item = random.nextBoolean() ? ItemRegistry.VOID_BOW.get() : ItemRegistry.VOID_SWORD.get();
        this.setItemSlot(EquipmentSlot.MAINHAND, item.getDefaultInstance());
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
    }

}
