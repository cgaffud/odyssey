package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.items.odyssey_versions.AspectArmorItem;
import com.bedmen.odyssey.items.odyssey_versions.AspectItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AspectUtil {

    private static final MutableComponent ADDED_MODIFIER_HEADER = new TranslatableComponent("item.oddc.added_modifiers");
    private static final String ADDED_MODIFIERS_TAG = Odyssey.MOD_ID + ":AddedModifiers";
    private static final String ID_TAG = "id";
    private static final String STRENGTH_TAG = "strength";

    private static ListTag getAspectListTag(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        return compoundTag != null ? compoundTag.getList(ADDED_MODIFIERS_TAG, Tag.TAG_COMPOUND) : new ListTag();
    }

    private static AspectStrengthMap tagToMap(ListTag listTag){
        AspectStrengthMap map = new AspectStrengthMap();
        for(Tag tag: listTag){
            if(tag instanceof CompoundTag aspectTag){
                String id = aspectTag.getString(ID_TAG);
                Aspect aspect = Aspects.ASPECT_REGISTER.get(id);
                if(aspect == null) {
                    Odyssey.LOGGER.error("Unkown aspect: "+id);
                } else {
                    map.put(aspect, aspectTag.getFloat(STRENGTH_TAG));
                }
            }
        }
        return map;
    }

    private static List<AspectInstance> tagToAspectInstanceList(ListTag listTag){
        return listTag.stream()
                .filter(tag -> tag instanceof CompoundTag)
                .map(tag -> {
                    CompoundTag aspectTag = (CompoundTag)tag;
                    String id = aspectTag.getString(ID_TAG);
                    Aspect aspect = Aspects.ASPECT_REGISTER.get(id);
                    if(aspect == null) {
                        Odyssey.LOGGER.error("Unkown aspect: "+id);
                        return null;
                    } else {
                        return new AspectInstance(aspect, aspectTag.getFloat(STRENGTH_TAG));
                    }
                }).collect(Collectors.toList());
    }

    private static Map<Aspect, Float> getAspectMap(ItemStack itemStack){
        return tagToMap(getAspectListTag(itemStack));
    }

    private static List<AspectInstance> getAspectInstanceList(ItemStack itemStack){
        return tagToAspectInstanceList(getAspectListTag(itemStack));
    }

    private static float getItemStackAspectStrengthFromTag(ItemStack itemStack, Aspect aspect){
        ListTag listTag = getAspectListTag(itemStack);
        for(Tag tag: listTag){
            if(tag instanceof CompoundTag aspectTag){
                String id = aspectTag.getString(ID_TAG);
                if(id.equals(aspect.id)){
                    return aspectTag.getFloat(STRENGTH_TAG);
                }
            }
        }
        return 0.0f;
    }

    private static float getItemStackAspectStrengthFromItem(ItemStack itemStack, Aspect aspect){
        Item item = itemStack.getItem();
        if(item instanceof AspectItem aspectItem){
            Float f = aspectItem.getAspectHolder().getAspectStrength(aspect);
            return f == null ? 0.0f : f;
        }
        return 0.0f;
    }

    private static float getTotalStrengthForFunctionFromMap(Map<Aspect, Float> map, Function<Aspect, Float> strengthFunction){
        return map.entrySet().stream()
                .map(entry -> strengthFunction.apply(entry.getKey()) * entry.getValue())
                .reduce(Float::sum).orElse(0.0f);
    }

    private static float getTotalStrengthForFunctionFromTag(ItemStack itemStack, Function<Aspect, Float> strengthFunction){
        return getTotalStrengthForFunctionFromMap(getAspectMap(itemStack), strengthFunction);
    }

    private static float getTotalStrengthForFunctionFromItem(ItemStack itemStack, Function<Aspect, Float> strengthFunction){
        Item item = itemStack.getItem();
        if(item instanceof AspectItem aspectItem){
            return getTotalStrengthForFunctionFromMap(aspectItem.getAspectHolder().allAspectMap, strengthFunction);
        }
        return 0.0f;
    }

    private static float getItemStackAspectStrength(ItemStack itemStack, Aspect aspect){
        if(itemStack.isEmpty()){
            return 0.0f;
        }
        return getItemStackAspectStrengthFromTag(itemStack, aspect) + getItemStackAspectStrengthFromItem(itemStack, aspect);
    }

    private static float getTotalStrengthForFunction(ItemStack itemStack, Function<Aspect, Float> strengthFunction){
        if(itemStack.isEmpty()){
            return 0.0f;
        }
        return getTotalStrengthForFunctionFromTag(itemStack, strengthFunction) + getTotalStrengthForFunctionFromItem(itemStack, strengthFunction);
    }

    private static float getTotalArmorStrengthForFunction(LivingEntity livingEntity, Function<Aspect, Float> strengthFunction){
        float total = 0.0f;
        float setBonusMin = Float.MAX_VALUE;
        float setBonusCount = 0;
        for(ItemStack armorPiece: livingEntity.getArmorSlots()){
            total += getTotalStrengthForFunction(armorPiece, strengthFunction);
            if(armorPiece.getItem() instanceof AspectArmorItem aspectArmorItem){
                float strength = aspectArmorItem.getSetBonusAbilityHolder().map.entrySet().stream()
                        .map(entry -> strengthFunction.apply(entry.getKey()) * entry.getValue())
                        .reduce(Float::sum).orElse(0.0f);
                if(strength < setBonusMin){
                    setBonusMin = strength;
                }
                setBonusCount++;
            }
        }
        if(setBonusCount == 4){
            total += setBonusMin;
        }
        return total;
    }

    private static float getTotalArmorStrengthForAspect(LivingEntity livingEntity, Aspect aspect){
        return getTotalArmorStrengthForFunction(livingEntity, aspect1 -> aspect1 == aspect ? 1.0f : 0.0f);
    }

    private static List<Component> createAspectTooltipList(Map<Aspect, Float> map){
        return map.entrySet().stream().map(entry ->
                entry.getKey().mutableComponentFunction.apply(entry.getValue()).withStyle(ChatFormatting.GRAY)
        ).collect(Collectors.toList());
    }

    private static List<Component> createAspectAdvancedTooltipList(Map<Aspect, Float> map){
        List<Component> componentList = createAspectTooltipList(map);
        if(componentList.isEmpty()){
            return List.of();
        } else {
            List<Component> advancedTooltipinnateAspectList = componentList.stream()
                    .map(component -> new TextComponent(" ").append(component))
                    .collect(Collectors.toList());
            advancedTooltipinnateAspectList.add(0, ADDED_MODIFIER_HEADER);
            return advancedTooltipinnateAspectList;
        }
    }

    // Public endpoints

    // Get direct value from single itemstack
    public static float getFloatAspectValue(ItemStack itemStack, FloatAspect floatAspect){
        return getItemStackAspectStrength(itemStack, floatAspect);
    }

    public static float getUnitAspectValue(ItemStack itemStack, UnitAspect unitAspect){
        return 1.0f + getItemStackAspectStrength(itemStack, unitAspect);
    }

    public static int getIntegerAspectValue(ItemStack itemStack, IntegerAspect integerAspect){
        return (int) getItemStackAspectStrength(itemStack, integerAspect);
    }

    public static boolean hasBooleanAspect(ItemStack itemStack, BooleanAspect booleanAspect){
        return getItemStackAspectStrength(itemStack, booleanAspect) > 0.0f;
    }

    // Special totals over multiple aspects on single item

    public static float getTargetConditionalAspectStrength(ItemStack itemStack, LivingEntity target){
        return getTotalStrengthForFunction(itemStack, aspect -> {
            if(aspect instanceof TargetConditionalAspect targetConditionalAspect){
                return targetConditionalAspect.livingEntityPredicate.test(target) ? 1.0f : 0.0f;
            }
            return 0.0f;
        });
    }

    public static float getEnvironmentalAspectStrength(ItemStack itemStack, BlockPos blockPos, Level level){
        return getTotalStrengthForFunction(itemStack, aspect -> {
            if(aspect instanceof EnvironmentConditionalAspect environmentConditionalAspect){
                return environmentConditionalAspect.attackBoostFactorFunction.getBoostFactor(blockPos, level);
            }
            return 0.0f;
        });
    }

    // Get total value from armor

    public static float getFloatAspectValueFromArmor(LivingEntity livingEntity, FloatAspect floatAspect){
        return getTotalArmorStrengthForAspect(livingEntity, floatAspect);
    }

    public static int getIntegerAspectValueFromArmor(LivingEntity livingEntity, IntegerAspect integerAspect){
        return (int) getTotalArmorStrengthForAspect(livingEntity, integerAspect);
    }

    public static boolean hasBooleanAspectOnArmor(LivingEntity livingEntity, BooleanAspect booleanAspect){
        return getTotalArmorStrengthForAspect(livingEntity, booleanAspect) > 0.0f;
    }

    // Special totals over multiple aspects on armor

    public static float getProtectionAspectStrength(LivingEntity livingEntity, DamageSource damageSource){
        return getTotalArmorStrengthForFunction(livingEntity, aspect -> {
            if(aspect instanceof ProtectionAspect protectionAspect){
                return protectionAspect.damageSourcePredicate.test(damageSource) ? 1.0f : 0.0f;
            }
            return 0.0f;
        });
    }

    // Tooltips

    public static void addAddedModifierTooltip(ItemStack itemStack, List<Component> tooltip, TooltipFlag tooltipFlag){
        List<AspectInstance> aspectInstanceList = getAspectInstanceList(itemStack);
        Optional<MutableComponent> optionalHeader = tooltipFlag.isAdvanced() ? Optional.of(ADDED_MODIFIER_HEADER) : Optional.empty();
        // The isAdvanced flag for getTooltip is for filtering aspectInstances based on their display properties
        // Here we want there to be a header or not based on the isAdvanced flag
        tooltip.addAll(getTooltip(aspectInstanceList, true, optionalHeader, ChatFormatting.GRAY));
    }

    public static List<Component> getTooltip(List<AspectInstance> aspectInstanceList, boolean isAdvanced, Optional<MutableComponent> optionalHeader, ChatFormatting chatFormatting){
        if(aspectInstanceList.isEmpty()){
            return new ArrayList<>();
        }
        List<Component> componentList = new ArrayList<>();
        optionalHeader.ifPresent(header -> componentList.add(header.withStyle(chatFormatting)));
        componentList.addAll(aspectInstanceList.stream()
                .filter(aspectInstance -> isAdvanced ? aspectInstance.aspectTooltipDisplaySetting != AspectTooltipDisplaySetting.NEVER : aspectInstance.aspectTooltipDisplaySetting == AspectTooltipDisplaySetting.ALWAYS)
                .map(aspectInstance -> (optionalHeader.isPresent() ? new TextComponent(" ").append(aspectInstance.getMutableComponent()) : aspectInstance.getMutableComponent()).withStyle(chatFormatting))
                .collect(Collectors.toList()));
        return componentList;
    }
}
