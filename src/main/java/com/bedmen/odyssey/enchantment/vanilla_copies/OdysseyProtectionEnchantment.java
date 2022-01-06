package com.bedmen.odyssey.enchantment.vanilla_copies;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

public class OdysseyProtectionEnchantment extends Enchantment {
    public final OdysseyProtectionEnchantment.Type type;

    public OdysseyProtectionEnchantment(Rarity p_i46723_1_, OdysseyProtectionEnchantment.Type type, EnchantmentCategory enchantmentType, EquipmentSlot... p_i46723_3_) {
        super(p_i46723_1_, enchantmentType, p_i46723_3_);
        this.type = type;
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinCost(int pEnchantmentLevel) {
        return 0;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 5;
    }

    /**
     * Calculates the damage protection of the enchantment based on level and damage source passed.
     */
    public int getDamageProtection(int pLevel, DamageSource pSource) {
        if (pSource.isBypassInvul()) {
            return 0;
        } else if (this.type == OdysseyProtectionEnchantment.Type.ALL) {
            return pLevel;
        } else if (this.type == OdysseyProtectionEnchantment.Type.FIRE && pSource.isFire()) {
            return pLevel * 5;
        } else if (this.type == OdysseyProtectionEnchantment.Type.ICE && pSource == DamageSource.FREEZE) {
            return pLevel * 5;
        } else if (this.type == OdysseyProtectionEnchantment.Type.FALL && pSource == DamageSource.FALL) {
            return pLevel * 5;
        } else if (this.type == OdysseyProtectionEnchantment.Type.EXPLOSION && pSource.isExplosion()) {
            return pLevel * 5;
        } else {
            return 0;
        }
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean checkCompatibility(Enchantment pEnch) {
        return !(pEnch instanceof OdysseyProtectionEnchantment);
    }

    public enum Type {
        ALL,
        FIRE,
        ICE,
        FALL,
        EXPLOSION,
    }
}