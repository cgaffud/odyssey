package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.aspect_objects.Aspect;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.aspect_objects.EnvironmentConditionalAspect;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class ConditionalAmpUtil {
    public static final String DAMAGE_BOOST_TAG = Odyssey.MOD_ID + ":ConditionalAmpBoost";
    public static final String GRADIENT_COLOR_TAG = Odyssey.MOD_ID + ":GradientColor";

    public interface ConditionalAmpItem {
        Aspect getAspect();
    }

    public static float activeFactor(ItemStack itemStack, LivingEntity livingEntity) {
        if (itemStack.getItem() instanceof ConditionalAmpItem conditionalAmpItem
                && conditionalAmpItem.getAspect() instanceof EnvironmentConditionalAspect environmentConditionalAspect){
            return environmentConditionalAspect.attackBoostFactorFunction.getBoostFactor(livingEntity.eyeBlockPosition(), livingEntity.level);
        }

        return 0.0f;
    }

    public static float getDamageTag(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getFloat(DAMAGE_BOOST_TAG);
    }

    public static void setDamageTag(ItemStack itemStack, Entity entity) {
        float aspectBonus = AspectUtil.getEnvironmentalAspectStrength(itemStack, entity.eyeBlockPosition(), entity.level);
        itemStack.getOrCreateTag().putFloat(DAMAGE_BOOST_TAG, aspectBonus);
    }

    public interface NumericalItem {
        int getIntervalCount();
    }

    public static ItemPropertyFunction getNumericalItemPropertyFunction(NumericalItem numericalItem) {
        int intervalCount = numericalItem.getIntervalCount();
        return (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0f;
            }
            float factor = ConditionalAmpUtil.activeFactor(itemStack, livingEntity);
            if (factor <= 0.0f) {
                return 0.0f;
            }
            if (factor > 1.0f) {
                return intervalCount;
            }
            for (int j = 0; j < intervalCount; j++) {
                boolean lowerBoundMet = factor > ((float)j)/intervalCount;
                boolean upperBoundMet = factor <= ((float)(j+1)/intervalCount);
                if (lowerBoundMet && upperBoundMet) {
                    return j+1;
                }
            }
            return 0.0f;
        };
    }

    public interface GradientItem {
        int getColor(Level level, Entity entity);
        int getDefaultColor();
    }

    public interface ColorProvider {
        int getColor(Level level, Entity entity);
    }

    public static int getColorTag(ItemStack itemStack) {
        if (itemStack.getItem() instanceof GradientItem gradientItem){
            CompoundTag compoundTag = itemStack.getOrCreateTag();
            if (compoundTag.contains(ConditionalAmpUtil.GRADIENT_COLOR_TAG))
                return compoundTag.getInt(ConditionalAmpUtil.GRADIENT_COLOR_TAG);
            return gradientItem.getDefaultColor();
        }
        return -1;
    }

    public static void setColorTag(ItemStack itemStack, Entity entity) {
        if (itemStack.getItem() instanceof GradientItem gradientItem) {
            itemStack.getOrCreateTag().putFloat(GRADIENT_COLOR_TAG, gradientItem.getColor(entity.level, entity));
        }
    }

    public static Multimap<Attribute, AttributeModifier> getAttributeModifiersWithAdjustedAttackDamage(EquipmentSlot equipmentSlot, ItemStack itemStack, Multimap<Attribute, AttributeModifier> defaultMultimap){
        Item item = itemStack.getItem();
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            float conditionalAmpBonus = ConditionalAmpUtil.getDamageTag(itemStack);
            if(conditionalAmpBonus > 0.0f){
                Multimap<Attribute, AttributeModifier> stackAttributeModifiers = LinkedHashMultimap.create();
                for(Map.Entry<Attribute, Collection<AttributeModifier>> entry : item.getDefaultAttributeModifiers(equipmentSlot).asMap().entrySet()){
                    if (entry.getKey() == Attributes.ATTACK_DAMAGE) {
                        Collection<AttributeModifier> newDamageModifiers = entry.getValue().stream()
                                .map(attributeModifier ->
                                        attributeModifier.getId() == Item.BASE_ATTACK_DAMAGE_UUID ?
                                                new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attributeModifier.getAmount() + conditionalAmpBonus, AttributeModifier.Operation.ADDITION) :
                                                attributeModifier)
                                .collect(Collectors.toSet());
                        stackAttributeModifiers.putAll(entry.getKey(), newDamageModifiers);
                    } else {
                        stackAttributeModifiers.putAll(entry.getKey(), entry.getValue());
                    }
                }
                return stackAttributeModifiers;
            }
            return defaultMultimap;
        }
        return item.getDefaultAttributeModifiers(equipmentSlot);
    }
}
