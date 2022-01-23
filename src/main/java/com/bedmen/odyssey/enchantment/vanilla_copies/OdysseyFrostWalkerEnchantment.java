package com.bedmen.odyssey.enchantment.vanilla_copies;

import com.bedmen.odyssey.enchantment.TieredEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractFrostWalkerEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.level.Level;

public class OdysseyFrostWalkerEnchantment extends AbstractFrostWalkerEnchantment {
    public OdysseyFrostWalkerEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EquipmentSlotTypes);
    }

    public static void onEntityMoved(LivingEntity livingEntity, Level level, BlockPos blockPos, int enchantmentLevel) {
        FrostWalkerEnchantment.onEntityMoved(livingEntity, level, blockPos, enchantmentLevel);
    }

    @Override
    public boolean canUpgrade() {
        return true;
    }

    @Override
    public boolean canDowngrade() {
        return false;

    }

    @Override
    public TieredEnchantment getUpgrade() {
        return (TieredEnchantment) EnchantmentRegistry.OBSIDIAN_WALKER.get();
    }

    @Override
    public TieredEnchantment getDowngrade() {
        return null;
    }
}
