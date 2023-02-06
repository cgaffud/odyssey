package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Optional;

public class PermabuffHolder implements AspectHolder {
    private static final MutableComponent PERMABUFF_HEADER = new TranslatableComponent("aspect_tooltip.oddc.permabuffs");
    private static final ChatFormatting PERMABUFF_COLOR = ChatFormatting.YELLOW;

    public final List<AspectInstance> aspectInstanceList;
    public final PermabuffMap permabuffMap;

    public PermabuffHolder(List<AspectInstance> aspectInstanceList) {
        this.aspectInstanceList = aspectInstanceList;
        this.permabuffMap = new PermabuffMap(aspectInstanceList);
    }

    public void addTooltip(List<Component> tooltip, TooltipFlag tooltipFlag, AspectTooltipContext aspectTooltipContext){
        if(tooltipFlag.isAdvanced()){
            tooltip.addAll(AspectUtil.getTooltip(aspectTooltipContext.withOtherContextVariables(this.aspectInstanceList, true, Optional.of(PERMABUFF_HEADER), PERMABUFF_COLOR)));
        } else {
            tooltip.addAll(AspectUtil.getTooltip(aspectTooltipContext.withOtherContextVariables(this.aspectInstanceList, false, Optional.of(PERMABUFF_HEADER), PERMABUFF_COLOR)));
        }
    }
}
