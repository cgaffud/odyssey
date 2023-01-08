package com.bedmen.odyssey.items.innate_aspect_items;

import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.weapon.MeleeWeaponClass;
import com.bedmen.odyssey.items.OdysseyMeleeItem;
import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class InnateAspectMeleeItem extends OdysseyMeleeItem implements InnateAspectItem{
    public final List<AspectInstance> innateAspectList;
    public final List<Component> tooltipInnateAspectList;
    public final List<Component> advancedTooltipInnateAspectList;

    public InnateAspectMeleeItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance> innateAspectList) {
        super(properties, tier, meleeWeaponClass, damage);
        this.innateAspectList = innateAspectList;
        this.tooltipInnateAspectList = this.innateAspectList.stream().map(aspectInstance -> aspectInstance.innateComponenet).collect(Collectors.toList());
        if(this.tooltipInnateAspectList.isEmpty()){
            this.advancedTooltipInnateAspectList = List.of();
        } else {
            List<Component> advancedTooltipInnateAspectList = this.tooltipInnateAspectList.stream()
                    .map(component -> new TextComponent(" ").append(component))
                    .collect(Collectors.toList());
            advancedTooltipInnateAspectList.add(0, new TranslatableComponent("item.oddc.innate_aspect").withStyle(OdysseyChatFormatting.LAVENDER));
            this.advancedTooltipInnateAspectList = advancedTooltipInnateAspectList;
        }
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if(tooltipFlag.isAdvanced()){
            tooltip.addAll(this.advancedTooltipInnateAspectList);
        } else {
            tooltip.addAll(this.tooltipInnateAspectList);
        }
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);
    }

    public List<AspectInstance> getInnateAspectInstanceList() {
        return this.innateAspectList;
    }
}
