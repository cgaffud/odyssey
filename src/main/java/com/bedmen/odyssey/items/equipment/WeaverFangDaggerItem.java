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

public class WeaverFangDaggerItem extends EquipmentMeleeItem {
    public WeaverFangDaggerItem(Tier tier, float attackDamageIn, float attackSpeedIn, boolean canSweep, Properties builderIn, LevEnchSup... levEnchSups) {
        super(tier, attackDamageIn, attackSpeedIn, canSweep, builderIn, levEnchSups);
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target)
    {
        if(!player.level.isClientSide && player.getAttackStrengthScale(0.5F) > 0.9f && target instanceof LivingEntity livingTarget) {
            if (player.getMainHandItem().is(ItemRegistry.WEAVER_FANG_DAGGER.get()) && player.getRandom().nextFloat() < Weaver.WEB_ATTACK_CHANCE) {
                BlockPos blockPos = new BlockPos(livingTarget.getPosition(1f));
                if (livingTarget.level.getBlockState(blockPos).getBlock() == Blocks.AIR) {
                    livingTarget.level.setBlock(blockPos, Blocks.COBWEB.defaultBlockState(), 3);
                }
            }
        }
        return false;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("item.oddc.weaver_fang.web_chance").append(StringUtil.percentFormat(Weaver.WEB_ATTACK_CHANCE)).withStyle(ChatFormatting.BLUE));
    }
}
