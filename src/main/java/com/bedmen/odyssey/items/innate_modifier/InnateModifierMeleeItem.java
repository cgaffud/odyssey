package com.bedmen.odyssey.items.innate_modifier;

import com.bedmen.odyssey.modifier.ModifierInstance;
import com.bedmen.odyssey.modifier.InnateModifierHolder;
import com.bedmen.odyssey.combat.MeleeWeaponClass;
import com.bedmen.odyssey.items.odyssey_versions.OdysseyMeleeItem;
import net.minecraft.world.item.Tier;

import java.util.List;

public class InnateModifierMeleeItem extends OdysseyMeleeItem implements InnateModifierItem {
    public final InnateModifierHolder innateModifierHolder;

    public InnateModifierMeleeItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<ModifierInstance> innateModifierList) {
        super(properties, tier, meleeWeaponClass, damage);
        this.innateModifierHolder = new InnateModifierHolder(innateModifierList);
    }

    public InnateModifierHolder getInnateModifierHolder() {
        return this.innateModifierHolder;
    }
}