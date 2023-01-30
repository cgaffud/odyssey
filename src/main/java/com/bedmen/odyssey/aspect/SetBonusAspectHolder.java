package com.bedmen.odyssey.aspect;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class SetBonusAspectHolder {
    private static final MutableComponent SET_BONUS_ABILITY_HEADER = new TranslatableComponent("item.oddc.set_bonus_abilities");
    private static final ChatFormatting SET_BONUS_COLOR = ChatFormatting.AQUA;

    public final List<AspectInstance> aspectInstanceList;
    public final AspectStrengthMap map;

    public SetBonusAspectHolder(List<AspectInstance> aspectInstanceList) {
        this.aspectInstanceList = aspectInstanceList;
        this.map = new AspectStrengthMap(aspectInstanceList);
    }

    public void addTooltip(List<Component> tooltip, TooltipFlag tooltipFlag, Optional<Level> optionalLevel){
        if(tooltipFlag.isAdvanced()){
            tooltip.addAll(AspectUtil.getTooltip(this.aspectInstanceList, true, Optional.of(SET_BONUS_ABILITY_HEADER), SET_BONUS_COLOR, optionalLevel));
        } else {
            tooltip.addAll(AspectUtil.getTooltip(this.aspectInstanceList, false, Optional.of(SET_BONUS_ABILITY_HEADER), SET_BONUS_COLOR, optionalLevel));
        }
    }
}
