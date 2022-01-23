package com.bedmen.odyssey.enchantment.odyssey;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentCategory;
import com.bedmen.odyssey.entity.player.IOdysseyPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class FlingEnchantment extends Enchantment {

    public FlingEnchantment(Rarity p_i46734_1_, EquipmentSlot... p_i46734_3_) {
        super(p_i46734_1_, OdysseyEnchantmentCategory.ALL_MELEE, p_i46734_3_);
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 10;
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && enchantment != Enchantments.KNOCKBACK;
    }

    /**
     * Called whenever a mob is damaged with an item that has this enchantment on it.
     */
    public void doPostAttack(LivingEntity user, Entity target, int enchantmentLevel) {
        if (target instanceof LivingEntity) {
            float f = user instanceof IOdysseyPlayer odysseyPlayer ? odysseyPlayer.getAttackStrengthScaleO() : 1.0f;
            target.setDeltaMovement(target.getDeltaMovement().add(0d, Math.sqrt(0.1d * (enchantmentLevel + 1) * f), 0d));
        }
    }
}