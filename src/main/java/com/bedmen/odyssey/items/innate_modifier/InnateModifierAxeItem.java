package com.bedmen.odyssey.items.innate_modifier;

import com.bedmen.odyssey.modifier.Modifier;
import com.bedmen.odyssey.modifier.ModifierInstance;
import com.bedmen.odyssey.modifier.InnateModifierHolder;
import com.bedmen.odyssey.items.odyssey_versions.OdysseyAxeItem;
import com.bedmen.odyssey.weapon.MeleeWeaponClass;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class InnateModifierAxeItem extends OdysseyAxeItem implements InnateModifierItem {
    public final InnateModifierHolder innateModifierHolder;

    public InnateModifierAxeItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<ModifierInstance> innateModifierList) {
        super(properties, tier, meleeWeaponClass, damage);
        this.innateModifierHolder = new InnateModifierHolder(innateModifierList);
    }

    public InnateModifierHolder getInnateModifierHolder() {
        return this.innateModifierHolder;
    }
}
