package com.bedmen.odyssey.modifier;

import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.TooltipFlag;

import java.util.*;
import java.util.stream.Collectors;

public class InnateModifierHolder {

    public final Map<Modifier, Float> modifierMap = new HashMap<>();
    public final List<Component> tooltipInnateModifierList;
    public final List<Component> advancedTooltipInnateModifierList;

    public InnateModifierHolder(List<ModifierInstance> modifierInstanceList) {
        for(ModifierInstance modifierInstance : modifierInstanceList){
            this.modifierMap.put(modifierInstance.modifier, modifierInstance.strength);
        }
        this.tooltipInnateModifierList = this.createTooltipinnateModifierList(modifierInstanceList);
        this.advancedTooltipInnateModifierList = this.createAdvancedTooltipinnateModifierList();
    }

    public void addTooltip(List<Component> tooltip, TooltipFlag tooltipFlag){
        if(tooltipFlag.isAdvanced()){
            tooltip.addAll(this.advancedTooltipInnateModifierList);
        } else {
            tooltip.addAll(this.tooltipInnateModifierList);
        }
    }

    public List<Component> createTooltipinnateModifierList(List<ModifierInstance> modifierInstanceList){
        return modifierInstanceList.stream().map(modifierInstance ->
                modifierInstance.modifier.mutableComponentFunction.apply(modifierInstance.strength).withStyle(OdysseyChatFormatting.LAVENDER)
        ).collect(Collectors.toList());
    }

    public List<Component> createAdvancedTooltipinnateModifierList(){
        if(this.tooltipInnateModifierList.isEmpty()){
            return List.of();
        } else {
            List<Component> advancedTooltipinnateModifierList = this.tooltipInnateModifierList.stream()
                    .map(component -> new TextComponent(" ").append(component))
                    .collect(Collectors.toList());
            advancedTooltipinnateModifierList.add(0, new TranslatableComponent("item.oddc.innate_modifier").withStyle(OdysseyChatFormatting.LAVENDER));
            return advancedTooltipinnateModifierList;
        }
    }
}
