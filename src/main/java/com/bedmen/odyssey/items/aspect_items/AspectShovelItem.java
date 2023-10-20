package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolderType;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.combat.MeleeWeaponClass;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class AspectShovelItem extends ShovelItem implements InnateAspectItem, MeleeWeaponClassItem {
    private final AspectHolder innateAspectHolder;
    private final AspectHolder abilityHolder;
    protected final MeleeWeaponClass meleeWeaponClass;

    public AspectShovelItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance<?>> additionalAbilityList, List<AspectInstance<?>> innateModifierList) {
        super(tier, damage, meleeWeaponClass.attackRate - 4.0f, properties);
        List<AspectInstance<?>> fullAbilityList = new ArrayList<>(meleeWeaponClass.aspectInstanceList);
        fullAbilityList.addAll(additionalAbilityList);
        this.innateAspectHolder = new AspectHolder(innateModifierList, AspectHolderType.INNATE_ASPECT);
        this.abilityHolder = new AspectHolder(fullAbilityList, AspectHolderType.ABILITY);
        this.meleeWeaponClass = meleeWeaponClass;
    }

    public AspectHolder getInnateAspectHolder() {
        return this.innateAspectHolder;
    }

    public AspectHolder getAbilityHolder() {
        return this.abilityHolder;
    }

    public MeleeWeaponClass getMeleeWeaponClass(){
        return this.meleeWeaponClass;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack itemStack)
    {
        return WeaponUtil.getAttributeModifiersWithAdjustedValues(equipmentSlot, itemStack, this.getDefaultAttributeModifiers(equipmentSlot), this.BASE_ATTACK_SPEED_UUID);
    }

    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int compartments, boolean selected) {
        ConditionalAmpUtil.setDamageTag(itemStack, entity);
        super.inventoryTick(itemStack, level, entity, compartments, selected);
    }
}
