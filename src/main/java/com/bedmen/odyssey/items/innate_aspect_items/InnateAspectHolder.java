package com.bedmen.odyssey.items.innate_aspect_items;

import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.util.OdysseyChatFormatting;
import com.bedmen.odyssey.weapon.MeleeWeaponClass;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.stream.Collectors;

public class InnateAspectHolder {

    public final List<AspectInstance> innateAspectList;
    public final List<Component> tooltipInnateAspectList;
    public final List<Component> advancedTooltipInnateAspectList;

    public InnateAspectHolder(List<AspectInstance> innateAspectList) {
        this.innateAspectList = innateAspectList;
        this.tooltipInnateAspectList = this.createTooltipInnateAspectList();
        this.advancedTooltipInnateAspectList = this.createAdvancedTooltipInnateAspectList();
    }

    protected void addTooltip(List<Component> tooltip, TooltipFlag tooltipFlag){
        if(tooltipFlag.isAdvanced()){
            tooltip.addAll(this.advancedTooltipInnateAspectList);
        } else {
            tooltip.addAll(this.tooltipInnateAspectList);
        }
    }

    protected List<Component> createTooltipInnateAspectList(){
        return this.innateAspectList.stream().map(aspectInstance -> aspectInstance.innateComponenet).collect(Collectors.toList());
    }

    protected List<Component> createAdvancedTooltipInnateAspectList(){
        if(this.tooltipInnateAspectList.isEmpty()){
            return List.of();
        } else {
            List<Component> advancedTooltipInnateAspectList = this.tooltipInnateAspectList.stream()
                    .map(component -> new TextComponent(" ").append(component))
                    .collect(Collectors.toList());
            advancedTooltipInnateAspectList.add(0, new TranslatableComponent("item.oddc.innate_aspect").withStyle(OdysseyChatFormatting.LAVENDER));
            return advancedTooltipInnateAspectList;
        }
    }
}
