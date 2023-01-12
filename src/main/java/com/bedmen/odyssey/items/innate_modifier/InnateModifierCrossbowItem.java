package com.bedmen.odyssey.items.innate_modifier;

import com.bedmen.odyssey.items.odyssey_versions.OdysseyCrossbowItem;
import com.bedmen.odyssey.modifier.InnateModifierHolder;
import com.bedmen.odyssey.modifier.ModifierInstance;
import com.bedmen.odyssey.weapon.BowAbility;

import java.util.List;

public class InnateModifierCrossbowItem extends OdysseyCrossbowItem implements InnateModifierItem {

    public final InnateModifierHolder innateModifierHolder;

    public InnateModifierCrossbowItem(Properties properties, float velocityMultiplier, int baseMaxChargeTicks, List<BowAbility> bowAbilityList, List<ModifierInstance> innateModifierList) {
        super(properties, velocityMultiplier, baseMaxChargeTicks, bowAbilityList);
        this.innateModifierHolder = new InnateModifierHolder(innateModifierList);
    }

    public InnateModifierHolder getInnateModifierHolder() {
        return this.innateModifierHolder;
    }
}
