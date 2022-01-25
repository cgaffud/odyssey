package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.entity.monster.Weaver;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Blocks;

public class SpiderDaggerItem extends EquipmentMeleeItem{
    public SpiderDaggerItem(Tier tier, float attackDamageIn, float attackSpeedIn, boolean canSweep, Properties builderIn, LevEnchSup... levEnchSups) {
        super(tier, attackDamageIn, attackSpeedIn, canSweep, builderIn, levEnchSups);
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target)
    {
        if(!player.level.isClientSide && target instanceof LivingEntity livingTarget) {
            if (player.getMainHandItem().is(ItemRegistry.SPIDER_DAGGER.get())) {
                livingTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 40, 1));
            }
        }
        return false;
    }

}
