package com.bedmen.odyssey.enchantment.odyssey;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;

public class ConditionalAmpEnchantment extends Enchantment {

    public interface AttackBoostFactorFunction {
        float getBoostFactor(BlockPos pos, Level level);
    }

    private final int maxLevel;
    private final AttackBoostFactorFunction factorFunction;
    private final float meleeMaxBoost;
    private final float rangedMaxBoost;

    public ConditionalAmpEnchantment(Rarity rarity, int maxLevel, float meleeMaxBoost, AttackBoostFactorFunction factorFunction, EquipmentSlot... slots) {
        this(rarity, maxLevel, meleeMaxBoost, meleeMaxBoost / 5.0f, factorFunction, slots);
    }

    public ConditionalAmpEnchantment(Rarity rarity, int maxLevel, float meleeMaxBoost, float rangedMaxBoost, AttackBoostFactorFunction factorFunction, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.BREAKABLE, slots);
        this.maxLevel = maxLevel;
        this.factorFunction = factorFunction;
        this.meleeMaxBoost = meleeMaxBoost;
        this.rangedMaxBoost = rangedMaxBoost;
    }

    public float getActiveBoost(Level level, Entity entity, boolean isMelee) {
        return this.getActiveFactor(level, entity) * (isMelee ? this.meleeMaxBoost : this.rangedMaxBoost);
    }

    public float getActiveFactor(Level level, Entity entity){
        BlockPos eyeLevel = new BlockPos(entity.getX(), entity.getEyeY(), entity.getZ());
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
