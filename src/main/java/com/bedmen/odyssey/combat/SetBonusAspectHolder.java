package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.aspect.Aspect;
import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.aspect.AspectStrengthMap;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Optional;

public class SetBonusAspectHolder {
    private static final MutableComponent SET_BONUS_ABILITY_HEADER = new TranslatableComponent("item.oddc.set_bonus_abilities").withStyle(OdysseyChatFormatting.COPPER);

    public final AspectStrengthMap map = new AspectStrengthMap();
    public final List<Component> nonAdvancedTooltip;
    public final List<Component> advancedTooltip;

    public SetBonusAspectHolder(List<AspectInstance> aspectInstanceList) {
        for(AspectInstance aspectInstance : aspectInstanceList){
            this.map.put(aspectInstance.aspect, aspectInstance.strength);
        }
        this.nonAdvancedTooltip = AspectUtil.getTooltip(aspectInstanceList, false, Optional.of(SET_BONUS_ABILITY_HEADER), OdysseyChatFormatting.COPPER);
        this.advancedTooltip = AspectUtil.getTooltip(aspectInstanceList, true, Optional.of(SET_BONUS_ABILITY_HEADER), OdysseyChatFormatting.COPPER);
    }

    public void addTooltip(List<Component> tooltip, TooltipFlag tooltipFlag){
        if(tooltipFlag.isAdvanced()){
            tooltip.addAll(this.advancedTooltip);
        } else {
            tooltip.addAll(this.nonAdvancedTooltip);
        }
    }

    public float getAspectStrength(Aspect aspect){
        return map.getNonNull(aspect);
    }
}
