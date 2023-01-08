package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.weapon.MeleeWeaponClass;
import com.bedmen.odyssey.items.equipment.base.EquipmentMeleeItem;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class SpiderFangDaggerItem extends EquipmentMeleeItem {

    public SpiderFangDaggerItem(Properties builderIn, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, LevEnchSup... levEnchSups) {
        super(builderIn, tier, meleeWeaponClass, damage, levEnchSups);
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target)
    {
        if(!player.level.isClientSide && target instanceof LivingEntity livingTarget) {
            livingTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 10 + 24, 1));
        }
        return false;
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(itemStack, level, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("item.oddc.spider_fang.poison_damage", StringUtil.floatFormat(2f)).withStyle(ChatFormatting.BLUE));
    }
}
