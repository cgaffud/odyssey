package com.bedmen.odyssey.items.innate_modifier;

import com.bedmen.odyssey.combat.AbilityHolder;
import com.bedmen.odyssey.combat.ArmorAbility;
import com.bedmen.odyssey.combat.InnateModifierArmorMaterial;
import com.bedmen.odyssey.combat.SetBonusAbilityHolder;
import com.bedmen.odyssey.items.odyssey_versions.OdysseyArmorItem;
import com.bedmen.odyssey.modifier.InnateModifierHolder;
import com.bedmen.odyssey.modifier.ModifierInstance;
import com.bedmen.odyssey.modifier.Modifiers;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.common.ForgeMod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InnateModifierArmorItem extends OdysseyArmorItem implements InnateModifierItem {

    public final InnateModifierHolder innateModifierHolder;
    public static final Set<InnateModifierArmorItem> UNINITIALIZED = new HashSet<>();

    public InnateModifierArmorItem(Properties properties, InnateModifierArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, List<ArmorAbility> armorAbilityList, List<ModifierInstance> innateModifierList) {
        super(properties, armorMaterial, equipmentSlot, armorAbilityList);
        this.innateModifierHolder = new InnateModifierHolder(innateModifierList);
        UNINITIALIZED.add(this);
    }

    public static void init(){
        for(InnateModifierArmorItem innateModifierArmorItem: UNINITIALIZED){
            List<Float> depthStriderList = innateModifierArmorItem.innateModifierHolder.modifierMap.entrySet().stream()
                    .filter(entry -> entry.getKey() == Modifiers.SWIM_SPEED).map(entry -> entry.getValue()).collect(Collectors.toList());
            if(depthStriderList.size() > 0){
                Multimap<Attribute, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();
                attributeModifiers.putAll(innateModifierArmorItem.getDefaultAttributeModifiers(innateModifierArmorItem.getSlot()));
                attributeModifiers.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier("Armor Modifier", depthStriderList.get(0), AttributeModifier.Operation.ADDITION));
                innateModifierArmorItem.defaultModifiers = attributeModifiers;
            }
        }
        UNINITIALIZED.clear();
    }

    public InnateModifierHolder getInnateModifierHolder() {
        return this.innateModifierHolder;
    }

    public SetBonusAbilityHolder getSetBonusAbilityHolder() {
        return ((InnateModifierArmorMaterial)this.material).getSetBonusAbilityHolder();
    }

    public AbilityHolder getAbilityHolder() {
        return ((InnateModifierArmorMaterial)this.material).getSetBonusAbilityHolder();
    }
}
