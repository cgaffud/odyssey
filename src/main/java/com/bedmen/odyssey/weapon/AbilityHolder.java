package com.bedmen.odyssey.weapon;

import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.TooltipFlag;

import java.util.*;
import java.util.stream.Collectors;

public class AbilityHolder {

    public final List<Ability> abilityList;
    public final List<Component> tooltipAbilityList;
    public final List<Component> advancedTooltipAbilityList;

    public AbilityHolder(List<Ability> abilityList) {
        this.abilityList = abilityList;
        this.tooltipAbilityList = this.createTooltipAbilityList();
        this.advancedTooltipAbilityList = this.createAdvancedTooltipAbilityList();
    }

    public void addTooltip(List<Component> tooltip, TooltipFlag tooltipFlag){
        if(tooltipFlag.isAdvanced()){
            tooltip.addAll(this.advancedTooltipAbilityList);
        } else {
            tooltip.addAll(this.tooltipAbilityList);
        }
    }

    public List<Component> createTooltipAbilityList(){
        return this.abilityList.stream()
                .filter(Ability::showOnRegularTooltip)
                .map(ability -> new TranslatableComponent("abilities.oddc."+ability.getId()).withStyle(OdysseyChatFormatting.COPPER)
        ).collect(Collectors.toList());
    }

    public List<Component> createAdvancedTooltipAbilityList(){
        if(this.abilityList.isEmpty()){
            return List.of();
        } else {
            List<Component> advancedTooltipinnateModifierList = this.abilityList.stream()
                    .map(ability -> new TextComponent(" ").append(new TranslatableComponent("abilities.oddc."+ability.getId()).withStyle(OdysseyChatFormatting.COPPER)))
                    .collect(Collectors.toList());
            advancedTooltipinnateModifierList.add(0, new TranslatableComponent("item.oddc.abilities").withStyle(OdysseyChatFormatting.COPPER));
            return advancedTooltipinnateModifierList;
        }
    }
}
