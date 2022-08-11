package com.bedmen.odyssey.enchantment.odyssey;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentCategory;
import com.bedmen.odyssey.entity.player.IOdysseyPlayer;
import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;

public class ShatteringEnchantment extends Enchantment{
    public ShatteringEnchantment(Rarity rarityIn, EquipmentSlot... slots) {
        super(rarityIn, OdysseyEnchantmentCategory.AXE, slots);
    }

    public int getMaxLevel() {
        return 3;
    }

    public void doPostAttack(LivingEntity user, Entity target, int enchantmentLevel) {
        if(!user.level.isClientSide && (!(user instanceof IOdysseyPlayer odysseyPlayer) || odysseyPlayer.getAttackStrengthScaleO() > 0.9f) && target instanceof LivingEntity livingTarget) {
            for(int i = 0; i <= enchantmentLevel; i++) {
                MobEffectInstance effectInstance = new MobEffectInstance(EffectRegistry.SHATTERED.get(), (enchantmentLevel - i + 1) * 40, i, false, true, true);
                livingTarget.addEffect(effectInstance);
            }
        }
    }
}
