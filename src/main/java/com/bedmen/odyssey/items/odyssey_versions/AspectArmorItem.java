package com.bedmen.odyssey.items.odyssey_versions;

import com.bedmen.odyssey.aspect.AspectHolder;
import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.Aspects;
import com.bedmen.odyssey.combat.*;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AspectArmorItem extends ArmorItem implements AspectItem {

    private final AspectHolder aspectHolder;
    public static final Set<AspectArmorItem> UNINITIALIZED = new HashSet<>();

    public AspectArmorItem(Properties properties, OdysseyArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList) {
        super(armorMaterial, equipmentSlot, properties);
        this.aspectHolder = new AspectHolder(abilityList, innateModifierList);
        UNINITIALIZED.add(this);
    }

    public static void init(){
        for(AspectArmorItem aspectArmorItem: UNINITIALIZED){
            List<Float> depthStriderList = aspectArmorItem.aspectHolder.allAspectMap.entrySet().stream()
                    .filter(entry -> entry.getKey() == Aspects.SWIM_SPEED).map(entry -> entry.getValue()).collect(Collectors.toList());
            if(depthStriderList.size() > 0){
                Multimap<Attribute, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();
                attributeModifiers.putAll(aspectArmorItem.getDefaultAttributeModifiers(aspectArmorItem.getSlot()));
                attributeModifiers.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier("Armor Modifier", depthStriderList.get(0), AttributeModifier.Operation.ADDITION));
                aspectArmorItem.defaultModifiers = attributeModifiers;
            }
        }
        UNINITIALIZED.clear();
    }

    public SetBonusAspectHolder getSetBonusAbilityHolder() {
        return ((OdysseyArmorMaterial)this.material).getSetBonusAbilityHolder();
    }

    public AspectHolder getAspectHolder() {
        return this.aspectHolder;
    }

    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer)
    {
        return AspectUtil.hasBooleanAspect(stack, Aspects.PIGLIN_NEUTRAL);
    }

    public boolean canElytraFly(ItemStack stack, LivingEntity livingEntity) {
        if(this.slot != EquipmentSlot.CHEST){
            return false;
        }
        if(!(AspectUtil.getIntegerAspectValueFromArmor(livingEntity, Aspects.GLIDE) > 0 || AspectUtil.getIntegerAspectValue(stack, Aspects.GLIDE) > 0)){
            return false;
        }
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            return odysseyLivingEntity.getFlightValue() > 0;
        }
        return false;
    }

    public boolean elytraFlightTick(ItemStack stack, LivingEntity livingEntity, int flightTicks) {
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity && odysseyLivingEntity.getFlightValue() > 0){
            odysseyLivingEntity.decrementFlight();
            return true;
        }
        return false;
    }
}
