package com.bedmen.odyssey.items.innate_aspect_items;

import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.items.MeleeWeaponClass;
import com.bedmen.odyssey.items.OdysseyMeleeItem;
import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class InnateAspectMeleeItem extends OdysseyMeleeItem {
    protected final List<AspectInstance> innateAspectList;

    public InnateAspectMeleeItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance> innateAspectList) {
        super(properties, tier, meleeWeaponClass, damage);
        this.innateAspectList = innateAspectList;
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag advancedTooltipsOn) {
        tooltip.add(new TranslatableComponent("item.oddc.innate_aspect").withStyle(OdysseyChatFormatting.LAVENDER));
        tooltip.addAll(this.innateAspectList.stream().map(AspectInstance::getInnateComponent).collect(Collectors.toList()));
        super.appendHoverText(itemStack, level, tooltip, advancedTooltipsOn);
    }
}
