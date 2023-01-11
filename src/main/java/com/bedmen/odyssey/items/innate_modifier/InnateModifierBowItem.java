package com.bedmen.odyssey.items.innate_modifier;

import com.bedmen.odyssey.items.odyssey_versions.OdysseyBowItem;
import com.bedmen.odyssey.items.odyssey_versions.OdysseyCrossbowItem;
import com.bedmen.odyssey.modifier.InnateModifierHolder;
import com.bedmen.odyssey.modifier.Modifier;
import com.bedmen.odyssey.modifier.ModifierInstance;
import com.bedmen.odyssey.weapon.BowAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class InnateModifierBowItem extends OdysseyBowItem implements InnateModifierItem {

    public final InnateModifierHolder innateModifierHolder;

    public InnateModifierBowItem(Properties properties, float velocityMultiplier, int baseMaxChargeTicks, List<BowAbility> bowAbilityList, List<ModifierInstance> innateModifierList) {
        super(properties, velocityMultiplier, baseMaxChargeTicks, bowAbilityList);
        this.innateModifierHolder = new InnateModifierHolder(innateModifierList);
    }

    public InnateModifierHolder getInnateModifierHolder() {
        return this.innateModifierHolder;
    }
}
