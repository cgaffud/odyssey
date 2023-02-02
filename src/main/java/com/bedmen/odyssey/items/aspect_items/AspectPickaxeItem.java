package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.AspectHolder;
import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.combat.MeleeWeaponClass;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AspectPickaxeItem extends PickaxeItem implements AspectItem, OdysseyMeleeItem {
    private final AspectHolder aspectHolder;
    protected final MeleeWeaponClass meleeWeaponClass;

    public AspectPickaxeItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance> additionalAbilityList, List<AspectInstance> innateModifierList) {
        super(tier, (int)damage, meleeWeaponClass.attackRate - 4.0f, properties);
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
}
