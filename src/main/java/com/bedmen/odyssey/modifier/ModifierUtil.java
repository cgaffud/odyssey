package com.bedmen.odyssey.modifier;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.items.innate_modifier.InnateModifierItem;
import com.bedmen.odyssey.util.OdysseyChatFormatting;
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
import org.antlr.v4.misc.MutableInt;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModifierUtil {

    private static final MutableComponent ADDED_MODIFIER_HEADER = new TranslatableComponent("item.oddc.added_modifiers").withStyle(ChatFormatting.GRAY);
    private static final String MODIFIER_TAG = Odyssey.MOD_ID + ":Modifiers";
    private static final String ID_TAG = "id";
    private static final String STRENGTH_TAG = "strength";

    private static ListTag getModifierListTag(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        return compoundTag != null ? compoundTag.getList(MODIFIER_TAG, Tag.TAG_COMPOUND) : new ListTag();
    }

    private static Map<Modifier, Float> tagToMap(ListTag listTag){
        Map<Modifier, Float> map = new HashMap<>();
        for(Tag tag: listTag){
            if(tag instanceof CompoundTag modifierTag){
                String id = modifierTag.getString(ID_TAG);
                Modifier modifier = Modifiers.modifierRegister.get(id);
                if(modifier == null) {
                    Odyssey.LOGGER.error("Unkown modifier: "+id);
                } else {
                    map.put(modifier, modifierTag.getFloat(STRENGTH_TAG));
                }
            }
        }
        return map;
    }

    private static Map<Modifier, Float> getModifierMap(ItemStack itemStack){
        return tagToMap(getModifierListTag(itemStack));
    }

    private static float getItemStackModifierStrengthFromTag(ItemStack itemStack, Modifier modifier){
        ListTag listTag = getModifierListTag(itemStack);
        for(Tag tag: listTag){
            if(tag instanceof CompoundTag modifierTag){
                String id = modifierTag.getString(ID_TAG);
                if(id.equals(modifier.id)){
                    return modifierTag.getFloat(STRENGTH_TAG);
                }
            }
        }
        return 0.0f;
    }

    private static float getItemStackModifierStrengthFromInnate(ItemStack itemStack, Modifier modifier){
        Item item = itemStack.getItem();
        if(item instanceof InnateModifierItem innateModifierItem){
            Float f = innateModifierItem.getInnateModifierHolder().modifierMap.get(modifier);
            return f == null ? 0.0f : f;
        }
        return 0.0f;
    }

    private static float getTotalStrengthForFunctionFromMap(Map<Modifier, Float> map, Function<Modifier, Float> strengthFunction){
        return map.entrySet().stream()
                .map(entry -> strengthFunction.apply(entry.getKey()) * entry.getValue())
                .reduce(Float::sum).orElse(0.0f);
    }

    private static float getTotalStrengthForFunctionFromTag(ItemStack itemStack, Function<Modifier, Float> strengthFunction){
        return getTotalStrengthForFunctionFromMap(getModifierMap(itemStack), strengthFunction);
    }

    private static float getTotalStrengthForFunctionFromInnate(ItemStack itemStack, Function<Modifier, Float> strengthFunction){
        Item item = itemStack.getItem();
        if(item instanceof InnateModifierItem innateModifierItem){
            return getTotalStrengthForFunctionFromMap(innateModifierItem.getInnateModifierHolder().modifierMap, strengthFunction);
        }
        return 0.0f;
    }

    private static float getItemStackModifierStrength(ItemStack itemStack, Modifier modifier){
        if(itemStack.isEmpty()){
            return 0.0f;
        }
        return getItemStackModifierStrengthFromTag(itemStack, modifier) + getItemStackModifierStrengthFromInnate(itemStack, modifier);
    }

    private static float getTotalStrengthForFunction(ItemStack itemStack, Function<Modifier, Float> strengthFunction){
        if(itemStack.isEmpty()){
            return 0.0f;
        }
        return getTotalStrengthForFunctionFromTag(itemStack, strengthFunction) + getTotalStrengthForFunctionFromInnate(itemStack, strengthFunction);
    }

    private static List<Component> createModifierTooltipList(Map<Modifier, Float> map){
        return map.entrySet().stream().map(entry ->
                entry.getKey().mutableComponentFunction.apply(entry.getValue()).withStyle(ChatFormatting.GRAY)
        ).collect(Collectors.toList());
    }

    private static List<Component> createModifierAdvancedTooltipList(Map<Modifier, Float> map){
        List<Component> componentList = createModifierTooltipList(map);
        if(componentList.isEmpty()){
            return List.of();
        } else {
            List<Component> advancedTooltipinnateModifierList = componentList.stream()
                    .map(component -> new TextComponent(" ").append(component))
                    .collect(Collectors.toList());
            advancedTooltipinnateModifierList.add(0, ADDED_MODIFIER_HEADER);
            return advancedTooltipinnateModifierList;
        }
    }

    // Public endpoints

    public static float getFloatModifierValue(ItemStack itemStack, FloatModifier floatModifier){
        return getItemStackModifierStrength(itemStack, floatModifier);
    }

    public static float getFloatModifierValueFromArmor(LivingEntity livingEntity, FloatModifier floatModifier){
        float total = 0.0f;
        for(ItemStack armorPiece: livingEntity.getArmorSlots()){
            total += getFloatModifierValue(armorPiece, floatModifier);
        }
        return total;
    }

    public static float getUnitModifierValue(ItemStack itemStack, UnitModifier unitModifier){
        return 1.0f + getItemStackModifierStrength(itemStack, unitModifier);
    }

    public static int getIntegerModifierValue(ItemStack itemStack, IntegerModifier integerModifier){
        return (int) getItemStackModifierStrength(itemStack, integerModifier);
    }

    public static boolean hasBooleanModifier(ItemStack itemStack, BooleanModifier booleanModifier){
        return getItemStackModifierStrength(itemStack, booleanModifier) > 0.0f;
    }

    public static float getTargetConditionalModifierStrength(ItemStack itemStack, LivingEntity target){
        return getTotalStrengthForFunction(itemStack, modifier -> {
            if(modifier instanceof TargetConditionalMeleeModifier targetConditionalMeleeModifier){
                return targetConditionalMeleeModifier.livingEntityPredicate.test(target) ? 1.0f : 0.0f;
            }
            return 0.0f;
        });
    }

    public static float getEnvironmentalModifierStrength(ItemStack itemStack, BlockPos blockPos, Level level){
        return getTotalStrengthForFunction(itemStack, modifier -> {
            if(modifier instanceof EnvironmentConditionalMeleeModifier environmentConditionalMeleeModifier){
                return environmentConditionalMeleeModifier.attackBoostFactorFunction.getBoostFactor(blockPos, level);
            }
            return 0.0f;
        });
    }

    public static float getProtectionModifierStrength(LivingEntity livingEntity, DamageSource damageSource){
        float total = 0.0f;
        for(ItemStack armorPiece: livingEntity.getArmorSlots()){
            total += getTotalStrengthForFunction(armorPiece, modifier -> {
                if(modifier instanceof ProtectionModifier protectionModifier){
                    return protectionModifier.damageSourcePredicate.test(damageSource) ? 1.0f : 0.0f;
                }
                return 0.0f;
            });
        }
        return total;
    }

    public static void addModifierTooltip(ItemStack itemStack, List<Component> tooltip, TooltipFlag tooltipFlag){
        Item item = itemStack.getItem();
        if(item instanceof InnateModifierItem innateModifierItem){
            innateModifierItem.getInnateModifierHolder().addTooltip(tooltip, tooltipFlag);
        }
        Map<Modifier, Float> map = getModifierMap(itemStack);
        if(tooltipFlag.isAdvanced()){
            tooltip.addAll(createModifierAdvancedTooltipList(map));
        } else {
            tooltip.addAll(createModifierTooltipList(map));
        }
    }
}
