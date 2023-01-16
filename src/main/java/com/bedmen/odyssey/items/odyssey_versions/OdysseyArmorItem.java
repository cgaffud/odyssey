package com.bedmen.odyssey.items.odyssey_versions;

import com.bedmen.odyssey.combat.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class OdysseyArmorItem extends ArmorItem implements OdysseyAbilityItem {

    private final AbilityHolder abilityHolder;

    public OdysseyArmorItem(Properties properties, InnateModifierArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, List<ArmorAbility> armorAbilityList) {
        super(armorMaterial, equipmentSlot, properties);
        this.abilityHolder = new AbilityHolder(armorAbilityList);
    }

    public SetBonusAbilityHolder getSetBonusAbilityHolder() {
        return ((InnateModifierArmorMaterial)this.material).getSetBonusAbilityHolder();
    }

    public AbilityHolder getAbilityHolder() {
        return this.abilityHolder;
    }

    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer)
    {
        return this.hasAbility(ArmorAbility.PIGLIN_NEUTRAL);
    }
}
