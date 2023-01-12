package com.bedmen.odyssey.items.innate_modifier;

import com.bedmen.odyssey.combat.AbilityHolder;
import com.bedmen.odyssey.combat.InnateModifierArmorMaterial;
import com.bedmen.odyssey.combat.OdysseyAbilityItem;
import com.bedmen.odyssey.modifier.InnateModifierHolder;
import com.bedmen.odyssey.modifier.ModifierInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;

import java.util.List;

public class InnateModifierArmorItem extends ArmorItem implements InnateModifierItem, OdysseyAbilityItem {

    public final InnateModifierHolder innateModifierHolder;

    public InnateModifierArmorItem(Properties properties, InnateModifierArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, List<ModifierInstance> innateModifierList) {
        super(armorMaterial, equipmentSlot, properties);
        this.innateModifierHolder = new InnateModifierHolder(innateModifierList);
    }

    public InnateModifierHolder getInnateModifierHolder() {
        return this.innateModifierHolder;
    }

    public AbilityHolder getAbilityHolder() {
        return ((InnateModifierArmorMaterial)this.material).getSetBonusAbilityHolder();
    }
}
