package com.bedmen.odyssey.enchantment.odyssey;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;

public class ConditionalAmpEnchantment extends Enchantment {

    public interface AttackBoostFactorFunction {
        float getBoostFactor(BlockPos pos, Level level);
    }

    private int maxLevel;
    private AttackBoostFactorFunction factorFunction;
    private float baseMaxBoost;

    public ConditionalAmpEnchantment(Rarity rarity, int maxLevel, float baseMaxBoost, AttackBoostFactorFunction factorFunction, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.BREAKABLE, slots);
        this.maxLevel = maxLevel;
        this.factorFunction = factorFunction;
        this.baseMaxBoost = baseMaxBoost;
    }

    public float getActiveBoost(Level level, LivingEntity livingEntity) {
        return this.getActiveFactor(level,livingEntity) * this.baseMaxBoost;
    }

    public float getActiveFactor(Level level, LivingEntity livingEntity){
        BlockPos eyeLevel = new BlockPos(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ());
        return this.factorFunction.getBoostFactor(eyeLevel, level);
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return this.maxLevel;
    }

    public boolean isTreasureOnly() {
        return true;
    }

    public boolean isCurse() {
        return false;
    }
}
