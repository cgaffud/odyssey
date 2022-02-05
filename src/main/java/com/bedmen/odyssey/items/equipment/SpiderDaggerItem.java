package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.entity.monster.Weaver;
import com.bedmen.odyssey.items.equipment.base.EquipmentMeleeItem;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.List;

public class SpiderDaggerItem extends EquipmentMeleeItem {

    private int poisonTime;

    public SpiderDaggerItem(Tier tier, float attackDamageIn, float attackSpeedIn, int poisonTime, boolean canSweep, Properties builderIn, LevEnchSup... levEnchSups) {
        super(tier, attackDamageIn, attackSpeedIn, canSweep, builderIn, levEnchSups);
        this.poisonTime = poisonTime;
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target)
    {
        if(!player.level.isClientSide && target instanceof LivingEntity livingTarget) {
            if (player.getMainHandItem().is(ItemRegistry.SPIDER_DAGGER.get())) {
                livingTarget.addEffect(new MobEffectInstance(MobEffects.POISON, this.poisonTime, 1));
            }
        }
        return false;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("item.oddc.spider_dagger.damage_modifier", StringUtil.floatFormat(((float)this.poisonTime) / 20)).withStyle(ChatFormatting.BLUE));
    }

}
