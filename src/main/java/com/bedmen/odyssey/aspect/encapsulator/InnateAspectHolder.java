package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipContext;
import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InnateAspectHolder implements AspectHolder {

    private static final MutableComponent ABILITIES_HEADER = new TranslatableComponent("item.oddc.abilities");
    private static final MutableComponent INNATE_MODIFIERS_HEADER = new TranslatableComponent("item.oddc.innate_modifiers");
    private static final ChatFormatting ABILITY_COLOR = OdysseyChatFormatting.COPPER;
    private static final ChatFormatting INNATE_MODIFIER_COLOR = OdysseyChatFormatting.LAVENDER;

    public final List<AspectInstance> abilityList;
    public final List<AspectInstance> innateModifierList;
    public final AspectStrengthMap allAspectMap;

    public InnateAspectHolder(List<AspectInstance> abilityList, List<AspectInstance> innateModifierList) {
        this.abilityList = abilityList;
        this.innateModifierList = innateModifierList;
        List<AspectInstance> aspectInstanceList = new ArrayList<>(abilityList);
        aspectInstanceList.addAll(innateModifierList);
        this.allAspectMap = new AspectStrengthMap(aspectInstanceList);
    }

    public void addTooltip(List<Component> tooltip, TooltipFlag tooltipFlag, AspectTooltipContext aspectTooltipContext){
        if(tooltipFlag.isAdvanced()){
            tooltip.addAll(AspectUtil.getTooltip(aspectTooltipContext.withOtherContextVariables(this.abilityList, true, Optional.of(ABILITIES_HEADER), ABILITY_COLOR)));
            tooltip.addAll(AspectUtil.getTooltip(aspectTooltipContext.withOtherContextVariables(this.innateModifierList, true, Optional.of(INNATE_MODIFIERS_HEADER), INNATE_MODIFIER_COLOR)));
        } else {
            tooltip.addAll(AspectUtil.getTooltip(aspectTooltipContext.withOtherContextVariables(this.abilityList, false, Optional.empty(), ABILITY_COLOR)));
            tooltip.addAll(AspectUtil.getTooltip(aspectTooltipContext.withOtherContextVariables(this.innateModifierList, false, Optional.empty(), INNATE_MODIFIER_COLOR)));
        }
    }
}
