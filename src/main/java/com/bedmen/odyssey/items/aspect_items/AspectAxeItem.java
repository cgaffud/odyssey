package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.AspectHolder;
import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.combat.MeleeWeaponClass;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class AspectAxeItem extends AxeItem implements AspectItem, OdysseyMeleeItem {
    protected final AspectHolder aspectHolder;
    protected final MeleeWeaponClass meleeWeaponClass;

    public AspectAxeItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance> additionalAbilityList, List<AspectInstance> innateModifierList) {
        super(tier, damage, meleeWeaponClass.attackRate - 4.0f, properties);
        List<AspectInstance> fullAbilityList = new ArrayList<>(meleeWeaponClass.aspectInstanceList);
        fullAbilityList.addAll(additionalAbilityList);
        this.aspectHolder = new AspectHolder(fullAbilityList, innateModifierList);
        this.meleeWeaponClass = meleeWeaponClass;
    }

    public AspectHolder getAspectHolder() {
        return this.aspectHolder;
    }

    public MeleeWeaponClass getMeleeWeaponClass(){
        return this.meleeWeaponClass;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack itemStack)
    {
        return ConditionalAmpUtil.getAttributeModifiersWithAdjustedAttackDamage(equipmentSlot, itemStack, this.getDefaultAttributeModifiers(equipmentSlot));
    }

    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int compartments, boolean selected) {
        ConditionalAmpUtil.setDamageTag(itemStack, entity);
        super.inventoryTick(itemStack, level, entity, compartments, selected);
    }
}
