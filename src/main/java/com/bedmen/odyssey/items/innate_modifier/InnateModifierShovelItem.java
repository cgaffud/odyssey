package com.bedmen.odyssey.items.innate_modifier;

import com.bedmen.odyssey.modifier.ModifierInstance;
import com.bedmen.odyssey.modifier.InnateModifierHolder;
import com.bedmen.odyssey.items.odyssey_versions.OdysseyShovelItem;
import com.bedmen.odyssey.combat.MeleeWeaponClass;
import net.minecraft.world.item.Tier;

import java.util.List;

public class InnateModifierShovelItem extends OdysseyShovelItem implements InnateModifierItem {
    public final InnateModifierHolder innateModifierHolder;

    public InnateModifierShovelItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<ModifierInstance> innateModifierList) {
        super(properties, tier, meleeWeaponClass, damage);
        this.innateModifierHolder = new InnateModifierHolder(innateModifierList);
    }

    public InnateModifierHolder getInnateModifierHolder() {
        return this.innateModifierHolder;
    }
}
