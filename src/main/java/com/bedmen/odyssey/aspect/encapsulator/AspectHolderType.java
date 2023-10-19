package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum AspectHolderType {
    INNATE_ASPECT(Component.translatable("aspect_tooltip.oddc.innate_modifiers"), OdysseyChatFormatting.LAVENDER),
    ABILITY(Component.translatable("aspect_tooltip.oddc.abilities"), OdysseyChatFormatting.COPPER),
    SET_BONUS(Component.translatable("aspect_tooltip.oddc.set_bonus_abilities"), ChatFormatting.AQUA),
    PERMABUFF(Component.translatable("aspect_tooltip.oddc.permabuffs"), ChatFormatting.YELLOW),
    TEMPBUFF(Component.translatable("aspect_tooltip.oddc.tempbuffs"), ChatFormatting.WHITE),
    ADDED_MODIFIER(Component.translatable("aspect_tooltip.oddc.added_modifiers"), ChatFormatting.GRAY);

    public final MutableComponent header;
    public final ChatFormatting color;

    AspectHolderType(MutableComponent header, ChatFormatting color){
        this.header = header;
        this.color = color;
    }
}
