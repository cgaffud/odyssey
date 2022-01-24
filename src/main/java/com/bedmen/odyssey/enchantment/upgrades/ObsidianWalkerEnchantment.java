package com.bedmen.odyssey.enchantment.upgrades;

import com.bedmen.odyssey.enchantment.TieredEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractFrostWalkerEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ObsidianWalkerEnchantment extends AbstractFrostWalkerEnchantment {
    public ObsidianWalkerEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EquipmentSlotTypes);
    }

    //TODO Implement this
    public static void onEntityMoved(LivingEntity livingEntity, Level level, BlockPos blockPos, int enchantmentLevel) {

    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public boolean canDowngrade() {
        return true;
    }

    @Override
    public TieredEnchantment getUpgrade() {
        return null;
    }

    @Override
    public TieredEnchantment getDowngrade() {
        return (TieredEnchantment) EnchantmentRegistry.FROST_WALKER.get();
    }
}
