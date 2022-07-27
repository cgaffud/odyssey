package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.items.equipment.base.EquipmentBowItem;
import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class SniperBowItem extends EquipmentBowItem {
    public SniperBowItem(Properties builder, float velocity, int chargeTime, LevEnchSup... levEnchSups) {
        super(builder, velocity, chargeTime, levEnchSups);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("item.minecraft.spyglass")
                .append(" [")
                .append(new TranslatableComponent("key.sneak"))
                .append(new TranslatableComponent("item.oddc.equipment.key"))
                .withStyle(OdysseyChatFormatting.LAVENDER)
                );
        super.appendHoverText(stack, level, tooltip, flagIn);
    }
}
