package com.bedmen.odyssey.items.innate_aspect_items;

import com.bedmen.odyssey.aspect.AspectInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class InnateAspectAxeItem extends AxeItem implements InnateAspectItem {
    public final InnateAspectHolder innateAspectHolder;

    public InnateAspectAxeItem(Properties properties, Tier tier, float attackSpeed, float damage, List<AspectInstance> innateAspectList) {
        super(tier, (int) damage, attackSpeed - 4.0f, properties);
        this.innateAspectHolder = new InnateAspectHolder(innateAspectList);
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        this.innateAspectHolder.addTooltip(tooltip, tooltipFlag);
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);
    }

    public List<AspectInstance> getInnateAspectInstanceList() {
        return this.innateAspectHolder.innateAspectList;
    }
}
