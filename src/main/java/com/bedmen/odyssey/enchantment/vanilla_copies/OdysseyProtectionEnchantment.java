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
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 5;
    }

    /**
     * Calculates the damage protection of the enchantment based on level and damage source passed.
     */
    public int getDamageProtection(int enchantmentLevel, DamageSource damageSource) {
        if (damageSource.isBypassInvul()) {
            return 0;
        }
        return switch (this.type) {
            case ALL -> enchantmentLevel;
            case FIRE -> damageSource.isFire() ? enchantmentLevel * 5 : 0;
            case ICE -> damageSource == DamageSource.FREEZE ? enchantmentLevel * 5 : 0;
            case FALL -> damageSource == DamageSource.FALL ? enchantmentLevel * 5 : 0;
            case KINETIC -> damageSource == DamageSource.FLY_INTO_WALL || damageSource == DamageSource.FALL ? enchantmentLevel * 5 : 0;
            case EXPLOSION -> damageSource.isExplosion() ? enchantmentLevel * 5 : 0;
        };
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
        KINETIC,
        EXPLOSION,
    }
}