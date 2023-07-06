package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipContext;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TempBuffHolder implements AspectHolder {
    private static final MutableComponent TEMPBUFF_HEADER = Component.translatable("aspect_tooltip.oddc.tempbuffs");
    private static final ChatFormatting TEMPBUFF_COLOR = ChatFormatting.WHITE;
    public final List<AspectInstance> aspectInstanceList;
    public final AspectStrengthMap aspectStrengthMap;

    public TempBuffHolder(List<AspectInstance> aspectInstanceList) {
        this.aspectInstanceList = aspectInstanceList;
        this.aspectStrengthMap = new AspectStrengthMap(aspectInstanceList);
    }

    public void addTooltip(List<Component> tooltip, TooltipFlag tooltipFlag, AspectTooltipContext aspectTooltipContext){
        if(tooltipFlag.isAdvanced()){
            tooltip.addAll(AspectUtil.getTooltip(aspectTooltipContext.withOtherContextVariables(this.aspectInstanceList, true, Optional.of(TEMPBUFF_HEADER), TEMPBUFF_COLOR)));
        } else {
            tooltip.addAll(AspectUtil.getTooltip(aspectTooltipContext.withOtherContextVariables(this.aspectInstanceList, false, Optional.of(TEMPBUFF_HEADER), TEMPBUFF_COLOR)));
        }
    }
}
