package com.bedmen.odyssey.enchantment.odyssey;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;

import java.lang.reflect.Method;

public class ConditionalAmpEnchantment extends Enchantment {

    public interface AttackBoostCalculator {
        float getBoost(BlockPos pos, Level level);
    }

    private int maxLevel;
    private AttackBoostCalculator calculator;
    private float attackBoost;

    public ConditionalAmpEnchantment(Rarity rarity, int maxLevel, float attackBoost, AttackBoostCalculator calculator, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.BREAKABLE, slots);
        this.maxLevel = maxLevel;
        this.calculator = calculator;
        this.attackBoost = attackBoost;
    }

    public float getActiveBoost(Level level, LivingEntity livingEntity) {
        return this.getActiveFraction(level,livingEntity) * this.attackBoost;
    }

    public float getActiveFraction(Level level, LivingEntity livingEntity){
        BlockPos eyeLevel = new BlockPos(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ());
        return this.calculator.getBoost(eyeLevel, level);
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
