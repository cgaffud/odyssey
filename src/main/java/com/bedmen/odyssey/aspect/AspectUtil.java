package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.encapsulator.AspectStrengthMap;
import com.bedmen.odyssey.aspect.object.*;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipContext;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipDisplaySetting;
import com.bedmen.odyssey.entity.player.OdysseyPlayer;
import com.bedmen.odyssey.items.aspect_items.AspectArmorItem;
import com.bedmen.odyssey.items.aspect_items.AspectItem;
import com.google.common.collect.Multimap;
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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
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
    private static final String DISPLAY_TAG = "display";
    private static final String OBFUSCATED_TAG = "obfuscated";
    private static final String SET_BONUS_STRING = "SET_BONUS";
    private static final List<EquipmentSlot> ARMOR_EQUIPMENT_SLOT_LIST = Arrays.stream(EquipmentSlot.values()).filter(equipmentSlot -> equipmentSlot.getType() == EquipmentSlot.Type.ARMOR).collect(Collectors.toList());
    private static final MutableComponent OBFUSCATED_TOOLTIP = new TextComponent("AAAAAAAAAA").withStyle(ChatFormatting.OBFUSCATED).withStyle(ChatFormatting.DARK_RED);

    private static ListTag getAddedModifierListTag(ItemStack itemStack) {
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
                    Odyssey.LOGGER.error("Unknown aspect: "+id);
                } else {
                    map.put(aspect, aspectTag.getFloat(STRENGTH_TAG));
                }
            }
        }
        return map;
    }

    private static CompoundTag aspectInstanceToCompoundTag(AspectInstance aspectInstance){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(ID_TAG, aspectInstance.aspect.id);
        compoundTag.putFloat(STRENGTH_TAG, aspectInstance.strength);
        compoundTag.putString(DISPLAY_TAG, aspectInstance.aspectTooltipDisplaySetting.name());
        compoundTag.putBoolean(OBFUSCATED_TAG, aspectInstance.obfuscated);
        return  compoundTag;
    }

    private static AspectInstance compoundTagToAspectInstance(CompoundTag compoundTag){
        String id = compoundTag.getString(ID_TAG);
        Aspect aspect = Aspects.ASPECT_REGISTER.get(id);
        if(aspect == null) {
            Odyssey.LOGGER.error("Unknown aspect: "+id);
            return null;
        } else {
            float strength = compoundTag.getFloat(STRENGTH_TAG);
            String displaySettingName = compoundTag.getString(DISPLAY_TAG);
            AspectTooltipDisplaySetting aspectTooltipDisplaySetting;
            if(displaySettingName.isEmpty()){
                aspectTooltipDisplaySetting = AspectTooltipDisplaySetting.ALWAYS;
            } else {
                try{
                    aspectTooltipDisplaySetting = AspectTooltipDisplaySetting.valueOf(displaySettingName);
                } catch (IllegalArgumentException illegalArgumentException){
                    Odyssey.LOGGER.error("Unknown AspectTooltipDisplaySetting: "+displaySettingName);
                    aspectTooltipDisplaySetting = AspectTooltipDisplaySetting.ALWAYS;
                }
            }
            boolean obfuscated = compoundTag.getBoolean(OBFUSCATED_TAG);
            return new AspectInstance(aspect, strength, aspectTooltipDisplaySetting, obfuscated);
        }
    }


    private static List<AspectInstance> tagToAspectInstanceList(ListTag listTag){
        return listTag.stream()
                .filter(tag -> tag instanceof CompoundTag)
                .map(tag -> compoundTagToAspectInstance((CompoundTag) tag))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static AspectStrengthMap getAddedModifierMap(ItemStack itemStack){
        return tagToMap(getAddedModifierListTag(itemStack));
    }

    private static float getTotalStrengthForFunctionFromMap(Map<Aspect, Float> map, Function<Aspect, Float> strengthFunction){
        return map.entrySet().stream()
                .map(entry -> strengthFunction.apply(entry.getKey()) * entry.getValue())
                .reduce(Float::sum).orElse(0.0f);
    }

    private static float getTotalStrengthForFunction(ItemStack itemStack, Function<Aspect, Float> strengthFunction){
        if(itemStack.isEmpty()){
            return 0.0f;
        }
        return getTotalStrengthForFunctionFromMap(getAspectStrengthMap(itemStack), strengthFunction);
    }

    private static float getBonusDamageAspectStrength(ItemStack itemStack, Function<Aspect, Float> strengthFunction){
        return getTotalStrengthForFunction(itemStack, aspect -> {
            if(aspect instanceof BonusDamageAspect){
                return strengthFunction.apply(aspect) * BonusDamageAspect.getStrengthAmplifier(itemStack.getItem());
            }
            return 0.0f;
        });
    }

    private static float getTotalArmorStrengthForFunction(LivingEntity livingEntity, Function<Aspect, Float> strengthFunction){
        float total = 0.0f;
        for(ItemStack armorPiece: livingEntity.getArmorSlots()){
            total += getTotalStrengthForFunction(armorPiece, strengthFunction);
        }
        if(isFullArmorSet(livingEntity.getArmorSlots()) && livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof AspectArmorItem aspectArmorItem){
            total += getTotalStrengthForFunctionFromMap(aspectArmorItem.getSetBonusAbilityHolder().map, strengthFunction);
        }
        return total;
    }

    private static float getTotalArmorStrengthForAspect(LivingEntity livingEntity, Aspect aspect){
        return getTotalArmorStrengthForFunction(livingEntity, aspect1 -> aspect1 == aspect ? 1.0f : 0.0f);
    }

    private static void fillAttributeMultimap(ItemStack itemStack, EquipmentSlot equipmentSlot, Multimap<Attribute, AttributeModifier> multimap){
        AspectStrengthMap aspectStrengthMap = getAspectStrengthMap(itemStack);
        aspectStrengthMap.forEach((key, value) -> {
            if (key instanceof AttributeAspect attributeAspect) {
                multimap.put(attributeAspect.getAttribute(), new AttributeModifier(new UUID (equipmentSlot.getName().hashCode(), attributeAspect.getAttribute().getDescriptionId().hashCode()), attributeAspect.id, value, attributeAspect.operation));
            }
        });
    }

    private static boolean isFullArmorSet(Iterable<ItemStack> armorPieces){
        int count = 0;
        ArmorMaterial firstArmorMaterial = null;
        for(ItemStack itemStack: armorPieces){
            if(itemStack.getItem() instanceof AspectArmorItem aspectArmorItem){
                ArmorMaterial armorMaterial = aspectArmorItem.getMaterial();
                if(firstArmorMaterial == null){
                    firstArmorMaterial = armorMaterial;
                }
                if(firstArmorMaterial == armorMaterial){
                    count++;
                }
            } else {
                return false;
            }
        }
        return count == 4;
    }

    private static float getTotalAspectStrengthAllSlotsForFunction(LivingEntity livingEntity, Function<Aspect, Float> strengthFunction){
        return getTotalArmorStrengthForFunction(livingEntity, strengthFunction)
                + getTotalStrengthForFunction(livingEntity.getMainHandItem(), strengthFunction)
                + getTotalStrengthForFunction(livingEntity.getOffhandItem(), strengthFunction);
    }

    private static float getTotalAspectStrengthAllSlotsForAspect(LivingEntity livingEntity, Aspect aspect){
        return getTotalAspectStrengthAllSlotsForFunction(livingEntity, aspect1 -> aspect1 == aspect ? 1.0f : 0.0f);
    }

    // -- Public endpoints -----------------------------------------------------

    // Get AspectStrengthMap

    public static List<AspectInstance> getAddedModifiersAsAspectInstanceList(ItemStack itemStack){
        return tagToAspectInstanceList(getAddedModifierListTag(itemStack));
    }

    public static AspectStrengthMap getAspectStrengthMap(ItemStack itemStack){
        AspectStrengthMap addedModifierMap = getAddedModifierMap(itemStack);
        if(itemStack.getItem() instanceof AspectItem aspectItem){
            return addedModifierMap.combine(aspectItem.getAspectHolder().allAspectMap);
        }
        return addedModifierMap;
    }

    public static boolean hasAddedModifiers(ItemStack itemStack){
        AspectStrengthMap addedModifierMap = getAddedModifierMap(itemStack);
        return !addedModifierMap.isEmpty();
    }

    // Get aspect strength from single itemStack

    public static float getAspectStrength(ItemStack itemStack, Aspect aspect){
        return getTotalStrengthForFunction(itemStack, aspect1 -> aspect1 == aspect ? 1.0f : 0.0f);
    }

    public static float getFloatAspectStrength(ItemStack itemStack, FloatAspect floatAspect){
        return getAspectStrength(itemStack, floatAspect);
    }

    public static int getIntegerAspectStrength(ItemStack itemStack, IntegerAspect integerAspect){
        return (int) getAspectStrength(itemStack, integerAspect);
    }

    public static boolean hasBooleanAspect(ItemStack itemStack, BooleanAspect booleanAspect){
        return getAspectStrength(itemStack, booleanAspect) > 0.0f;
    }

    public static int getPermabuffAspectStrength(Player player, PermabuffAspect permabuffAspect){
        if(player instanceof OdysseyPlayer odysseyPlayer){
            return odysseyPlayer.getPermabuffMap().get(permabuffAspect);
        }
        return 0;
    }

    // Special totals over multiple aspects on single item

    public static float getTargetConditionalAspectStrength(ItemStack itemStack, LivingEntity target){
        return getBonusDamageAspectStrength(itemStack, aspect -> {
            if(aspect instanceof TargetConditionalMeleeAspect targetConditionalMeleeAspect){
                return targetConditionalMeleeAspect.livingEntityPredicate.test(target) ? 1.0f : 0.0f;
            }
            return 0.0f;
        });
    }

    public static float getEnvironmentalAspectStrength(ItemStack itemStack, BlockPos blockPos, Level level){
        return getBonusDamageAspectStrength(itemStack, aspect -> {
            if(aspect instanceof EnvironmentConditionalAspect environmentConditionalAspect){
                return environmentConditionalAspect.attackBoostFactorFunction.getBoostFactor(blockPos, level);
            }
            return 0.0f;
        });
    }

    public static float getDamageSourcePredicateAspectStrength(ItemStack itemStack, DamageSource damageSource){
        return getTotalStrengthForFunction(itemStack, aspect -> {
            if(aspect instanceof DamageSourcePredicateAspect damageSourcePredicateAspect){
                return damageSourcePredicateAspect.damageSourcePredicate.test(damageSource) ? 1.0f : 0.0f;
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
            if(aspect instanceof DamageSourcePredicateAspect damageSourcePredicateAspect){
                return damageSourcePredicateAspect.damageSourcePredicate.test(damageSource) ? 1.0f : 0.0f;
            }
            return 0.0f;
        });
    }

    // Aspect total over all EquipmentSlots

    public static int getIntegerAspectStrengthAllSlots(LivingEntity livingEntity, IntegerAspect integerAspect){
        return (int) getTotalAspectStrengthAllSlotsForAspect(livingEntity, integerAspect);
    }

    // Tooltips

    public static void addAddedModifierTooltip(ItemStack itemStack, List<Component> tooltip, TooltipFlag tooltipFlag, AspectTooltipContext aspectTooltipContext){
        List<AspectInstance> addedModifiers = getAddedModifiersAsAspectInstanceList(itemStack);
        Optional<MutableComponent> optionalHeader = tooltipFlag.isAdvanced() ? Optional.of(ADDED_MODIFIER_HEADER) : Optional.empty();
        // The isAdvanced flag for getTooltip is for filtering aspectInstances based on their display properties
        // Here we want there to be a header or not based on the isAdvanced flag
        tooltip.addAll(getTooltip(aspectTooltipContext.withOtherContextVariables(addedModifiers, tooltipFlag.isAdvanced(), optionalHeader, ChatFormatting.GRAY)));
    }

    public static List<Component> getTooltip(AspectTooltipContext context){
        List<Component> componentList = new ArrayList<>();
        componentList.addAll(context.aspectInstanceList.stream()
                .filter(aspectInstance -> context.isAdvanced ? aspectInstance.aspectTooltipDisplaySetting != AspectTooltipDisplaySetting.NEVER : aspectInstance.aspectTooltipDisplaySetting == AspectTooltipDisplaySetting.ALWAYS)
                .map(aspectInstance ->
                        new TextComponent(context.optionalHeader.isPresent() ? " " : "")
                                .append(aspectInstance.obfuscated ? OBFUSCATED_TOOLTIP : aspectInstance.getMutableComponent(context)).withStyle(context.chatFormatting))
                .collect(Collectors.toList()));
        if(componentList.isEmpty()){
            return componentList;
        }
        context.optionalHeader.ifPresent(header -> componentList.add(0, header.withStyle(context.chatFormatting)));
        return componentList;
    }

    // Attribute Multimap

    public static void fillAttributeMultimaps(LivingEntity livingEntity, ItemStack oldItemStack, ItemStack newItemStack, EquipmentSlot equipmentSlot, Multimap<Attribute, AttributeModifier> oldMultimap, Multimap<Attribute, AttributeModifier> newMultimap){
        fillAttributeMultimap(oldItemStack, equipmentSlot, oldMultimap);
        fillAttributeMultimap(newItemStack, equipmentSlot, newMultimap);
        if(equipmentSlot.getType() == EquipmentSlot.Type.ARMOR){
            List<ItemStack> oldArmorPieces = ARMOR_EQUIPMENT_SLOT_LIST.stream().map(equipmentSlot1 -> livingEntity.getLastArmorItem(equipmentSlot1)).collect(Collectors.toList());
            List<ItemStack> newArmorPieces = ARMOR_EQUIPMENT_SLOT_LIST.stream().map(equipmentSlot1 -> livingEntity.getItemBySlot(equipmentSlot1)).collect(Collectors.toList());
            if(isFullArmorSet(oldArmorPieces) && oldArmorPieces.get(0).getItem() instanceof AspectArmorItem aspectArmorItem){
                aspectArmorItem.getSetBonusAbilityHolder().map.forEach((key, value) -> {
                    if (key instanceof AttributeAspect attributeAspect) {
                        oldMultimap.put(attributeAspect.getAttribute(), new AttributeModifier(new UUID (SET_BONUS_STRING.hashCode(), attributeAspect.getAttribute().getDescriptionId().hashCode()), attributeAspect.id, value, attributeAspect.operation));
                    }
                });
            }
            livingEntity.setItemSlot(equipmentSlot, newItemStack);
            if(isFullArmorSet(newArmorPieces) && newArmorPieces.get(0).getItem() instanceof AspectArmorItem aspectArmorItem){
                aspectArmorItem.getSetBonusAbilityHolder().map.forEach((key, value) -> {
                    if (key instanceof AttributeAspect attributeAspect) {
                        newMultimap.put(attributeAspect.getAttribute(), new AttributeModifier(new UUID (SET_BONUS_STRING.hashCode(), attributeAspect.getAttribute().getDescriptionId().hashCode()), attributeAspect.id, value, attributeAspect.operation));
                    }
                });
            }
        }
    }

    // For seeing if resistant to nether flame
    public static boolean hasFireProtectionOrResistance(LivingEntity livingEntity) {
        return getFloatAspectValueFromArmor(livingEntity, Aspects.FIRE_PROTECTION) > 0 || livingEntity.hasEffect(MobEffects.FIRE_RESISTANCE);
    }

    // Add/Remove added modifiers

    public static boolean canAddModifier(ItemStack itemStack, Aspect aspect){
        return aspect.itemPredicate.test(itemStack.getItem());
    }

    // Replaces added modifier with same aspect as aspectInstance with aspectInstance,
    // or removes the modifier altogether if aspectInstance.strength is 0
    public static void replaceModifier(ItemStack itemStack, AspectInstance aspectInstance){
        removeAddedModifier(itemStack, aspectInstance.aspect);
        // Just removes old modifier if the strength is set to 0
        if(aspectInstance.strength > 0.0f){
            ListTag aspectListTag = getAddedModifierListTag(itemStack);
            aspectListTag.add(aspectInstanceToCompoundTag(aspectInstance));
            itemStack.getOrCreateTag().put(ADDED_MODIFIERS_TAG, aspectListTag);
        }
    }

    public static void removeAddedModifier(ItemStack itemStack, Aspect aspect){
        ListTag aspectListTag = getAddedModifierListTag(itemStack);
        Tag oldAspectTag = null;
        for(Tag tag: aspectListTag){
            if(tag instanceof CompoundTag compoundTag && compoundTag.getString(ID_TAG).equals(aspect.id)){
                oldAspectTag = compoundTag;
                break;
            }
        }
        if(oldAspectTag != null){
            aspectListTag.remove(oldAspectTag);
            itemStack.getOrCreateTag().put(ADDED_MODIFIERS_TAG, aspectListTag);
        }
    }
}
