package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Optional;

public class AspectHolder {

    private static final MutableComponent ABILITIES_HEADER = new TranslatableComponent("item.oddc.abilities");
    private static final MutableComponent INNATE_MODIFIERS_HEADER = new TranslatableComponent("item.oddc.innate_modifiers");

    public final AspectStrengthMap abilityMap = new AspectStrengthMap();
    public final AspectStrengthMap innateModifierMap = new AspectStrengthMap();
    public final AspectStrengthMap allAspectMap = new AspectStrengthMap();
    public final List<Component> nonAdvancedTooltip;
    public final List<Component> advancedTooltip;

    public AspectHolder(List<AspectInstance> abilityList, List<AspectInstance> innateModifierList) {
        for(AspectInstance aspectInstance : abilityList){
            this.abilityMap.put(aspectInstance.aspect, aspectInstance.strength);
            this.allAspectMap.put(aspectInstance.aspect, aspectInstance.strength);
        }
        for(AspectInstance aspectInstance : innateModifierList){
            this.innateModifierMap.put(aspectInstance.aspect, aspectInstance.strength);
            if(this.allAspectMap.containsKey(aspectInstance.aspect)){
                float strength = this.allAspectMap.getNonNull(aspectInstance.aspect);
                this.allAspectMap.put(aspectInstance.aspect, strength + aspectInstance.strength);
            } else {
                this.allAspectMap.put(aspectInstance.aspect, aspectInstance.strength);
            }
        }
        this.nonAdvancedTooltip = AspectUtil.getTooltip(abilityList, false, Optional.empty(), OdysseyChatFormatting.COPPER);
        nonAdvancedTooltip.addAll(AspectUtil.getTooltip(innateModifierList, false, Optional.empty(), OdysseyChatFormatting.LAVENDER));
        this.advancedTooltip = AspectUtil.getTooltip(abilityList, true, Optional.of(ABILITIES_HEADER), OdysseyChatFormatting.COPPER);
        advancedTooltip.addAll(AspectUtil.getTooltip(innateModifierList, true, Optional.of(INNATE_MODIFIERS_HEADER), OdysseyChatFormatting.LAVENDER));
    }

    public void addTooltip(List<Component> tooltip, TooltipFlag tooltipFlag){
        if(tooltipFlag.isAdvanced()){
            tooltip.addAll(this.advancedTooltip);
        } else {
            tooltip.addAll(this.nonAdvancedTooltip);
        }
    }

    public float getAspectStrength(Aspect aspect){
        return abilityMap.getNonNull(aspect) + abilityMap.getNonNull(aspect);
    }
}
