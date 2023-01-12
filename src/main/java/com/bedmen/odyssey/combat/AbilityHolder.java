package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.TooltipFlag;

import java.util.*;
import java.util.stream.Collectors;

public class AbilityHolder {
    public static final MutableComponent ABILITY_HEADER = new TranslatableComponent("item.oddc.abilities").withStyle(OdysseyChatFormatting.COPPER);
    protected final List<Ability> abilityList;
    protected final List<Component> tooltipAbilityList;
    protected final List<Component> advancedTooltipAbilityList;

    public <T extends Ability> AbilityHolder(List<T> abilityList) {
        this.abilityList = new ArrayList<>(abilityList);
        this.tooltipAbilityList = this.createTooltipAbilityList();
        this.advancedTooltipAbilityList = this.createAdvancedTooltipAbilityList();
    }

    public AbilityHolder(AbilityHolder abilityHolder, Ability ability) {
        this.abilityList = new ArrayList<>(abilityHolder.abilityList);
        this.abilityList.add(ability);
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

    protected MutableComponent getHeader(){
        return ABILITY_HEADER;
    }

    public List<Component> createAdvancedTooltipAbilityList(){
        if(this.abilityList.isEmpty()){
            return List.of();
        } else {
            List<Component> advancedTooltipinnateModifierList = this.abilityList.stream()
                    .map(ability -> new TextComponent(" ").append(new TranslatableComponent("abilities.oddc."+ability.getId()).withStyle(OdysseyChatFormatting.COPPER)))
                    .collect(Collectors.toList());
            advancedTooltipinnateModifierList.add(0, this.getHeader());
            return advancedTooltipinnateModifierList;
        }
    }

    public boolean hasAbility(Ability ability){
        return this.abilityList.contains(ability);
    }
}
