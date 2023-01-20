package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.AspectHolder;
import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.combat.MeleeWeaponClass;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Tier;

import java.util.ArrayList;
import java.util.List;

public class AspectHoeItem extends HoeItem implements AspectItem {
    private final AspectHolder aspectHolder;

    public AspectHoeItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance> additionalAbilityList, List<AspectInstance> innateModifierList) {
        super(tier, (int)damage, meleeWeaponClass.attackRate - 4.0f, properties);
        List<AspectInstance> fullAbilityList = new ArrayList<>(meleeWeaponClass.aspectInstanceList);
        fullAbilityList.addAll(additionalAbilityList);
        this.aspectHolder = new AspectHolder(fullAbilityList, innateModifierList);
    }

    public AspectHolder getAspectHolder() {
        return this.aspectHolder;
    }
}
