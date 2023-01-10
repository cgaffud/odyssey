package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.monster.Weaver;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.weapon.ArrowType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import java.util.Locale;
import java.util.function.Consumer;

public class OdysseyArrow extends OdysseyAbstractArrow implements IEntityAdditionalSpawnData {
    public static final float WEAVER_FANG_ARROW_WEB_AMPLIFY = 2f;
    private ArrowType arrowType = ArrowType.AMETHYST;

    public OdysseyArrow(EntityType<? extends OdysseyArrow> p_i50158_1_, Level p_i50158_2_) {
        super(p_i50158_1_, p_i50158_2_);
    }

    public OdysseyArrow(Level p_i46768_1_, LivingEntity p_i46768_2_, ArrowType arrowType) {
        super(EntityTypeRegistry.ARROW.get(), p_i46768_2_, p_i46768_1_);
        this.arrowType = arrowType;
        this.setBaseDamage(arrowType.damage);
    }

    public OdysseyArrow(Level p_i46769_1_, double p_i46769_2_, double p_i46769_4_, double p_i46769_6_) {
        super(EntityTypeRegistry.ARROW.get(), p_i46769_2_, p_i46769_4_, p_i46769_6_, p_i46769_1_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    protected ItemStack getPickupItem() {
        return new ItemStack(this.getArrowType().getItem());
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if (compoundNBT.contains("ArrowType")) {
            this.arrowType = ArrowType.valueOf(compoundNBT.getString("ArrowType"));
        }
        this.setBaseDamage(this.getArrowType().damage);
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putString("ArrowType", this.arrowType.name());
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

    public void setEnchantmentEffectsFromEntity(LivingEntity shooter, float bowChargePower) {
        this.setBaseDamage((bowChargePower + this.random.nextGaussian() * 0.125D + (double)((float)this.level.getDifficulty().getId() * 0.055F)) * this.arrowType.damage);
        int i = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.POWER_ARROWS.get(), shooter);
        if (i > 0) {
            this.setBaseDamage(this.getBaseDamage() + (double)i * 0.5D + 0.5D);
        }
        i = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.PUNCH_ARROWS.get(), shooter);
        if (i > 0) {
            this.setKnockback(i);
        }
        i = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.FLAMING_ARROWS.get(), shooter);
        if (i > 0) {
            this.setSecondsOnFire(100*i);
        }
        i = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.PIERCING.get(), shooter);
        if (i > 0) {
            this.setPierceLevel((byte) i);
        }
        i = EnchantmentHelper.getEnchantmentLevel(Enchantments.MOB_LOOTING, shooter);
        if (i > 0) {
            this.setLootingLevel((byte) i);
        }
    }


}