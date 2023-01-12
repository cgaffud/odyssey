package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class SetBonusAbilityHolder extends AbilityHolder {
    private static final MutableComponent SET_BONUS_ABILITY_HEADER = new TranslatableComponent("item.oddc.set_bonus_abilities").withStyle(OdysseyChatFormatting.COPPER);

    public SetBonusAbilityHolder(List<SetBonusAbility> abilityList) {
        super(abilityList);
    }

    public void addTooltip(List<Component> tooltip, TooltipFlag tooltipFlag){
        tooltip.addAll(this.advancedTooltipAbilityList);
    }

    protected MutableComponent getHeader(){
        return SET_BONUS_ABILITY_HEADER;
    }
}
