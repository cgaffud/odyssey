package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import com.bedmen.odyssey.items.odyssey_versions.AspectBowItem;
import com.bedmen.odyssey.items.odyssey_versions.AspectCrossbowItem;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.combat.ArrowType;
import com.bedmen.odyssey.combat.WeaponUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

public class OdysseyArrow extends OdysseyAbstractArrow implements IEntityAdditionalSpawnData {
    public static final String ARROW_TYPE_TAG = "ArrowType";
    private ArrowType arrowType = ArrowType.AMETHYST;

    public OdysseyArrow(EntityType<? extends OdysseyArrow> entityType, Level level) {
        super(entityType, level);
    }

    public OdysseyArrow(Level level, LivingEntity livingEntity, ArrowType arrowType) {
        super(EntityTypeRegistry.ARROW.get(), livingEntity, level);
        this.arrowType = arrowType;
        this.setBaseDamage(arrowType.damage);
    }

    public OdysseyArrow(Level level, double x, double y, double z) {
        super(EntityTypeRegistry.ARROW.get(), x, y, z, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    protected ItemStack getPickupItem() {
        return new ItemStack(this.getArrowType().getItem());
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains(ARROW_TYPE_TAG)) {
            this.arrowType = ArrowType.valueOf(compoundTag.getString(ARROW_TYPE_TAG));
        }
        this.setBaseDamage(this.getArrowType().damage);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putString(ARROW_TYPE_TAG, this.arrowType.name());
    }

    public ArrowType getArrowType(){
        return this.arrowType;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.arrowType.ordinal());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.arrowType = ArrowType.values()[additionalData.readInt()];
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        this.arrowType.onEntityHit(entityHitResult, this.getPickupItem());
        super.onHitEntity(entityHitResult);
    }

    public void setEnchantmentEffectsFromEntity(LivingEntity shooter, float bowDamageMultiplier) {
        ItemStack bow = shooter.getItemInHand(ProjectileUtil.getWeaponHoldingHand(shooter, item -> item instanceof AspectBowItem || item instanceof AspectCrossbowItem));
        this.setBaseDamage((bowDamageMultiplier + this.random.nextGaussian() * 0.1D + (double)((float)this.level.getDifficulty().getId() * 0.055F)) * this.arrowType.damage * (WeaponUtil.BASE_ARROW_VELOCITY * WeaponUtil.BASE_ARROW_VELOCITY) / (WeaponUtil.BASE_ARROW_VELOCITY_ENEMIES * WeaponUtil.BASE_ARROW_VELOCITY_ENEMIES));
        // Knockback
        this.knockbackAspect = AspectUtil.getUnitAspectStrength(bow, Aspects.PROJECTILE_KNOCKBACK);
        // Piercing
        this.setPiercingAspect(AspectUtil.getFloatAspectStrength(bow, Aspects.PIERCING));
        // Larceny
        this.larcenyAspect = AspectUtil.getFloatAspectStrength(bow, Aspects.PROJECTILE_LARCENY_CHANCE);
    }
}