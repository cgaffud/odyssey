package com.bedmen.odyssey.modifier;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.items.innate_modifier.InnateModifierItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ModifierUtil {

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
                map.put(modifier, modifierTag.getFloat(STRENGTH_TAG));
            }
        }
        return map;
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
            Float f = innateModifierItem.getInnateModifierMap().get(modifier);
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
        ListTag listTag = getModifierListTag(itemStack);
        Map<Modifier, Float> map = tagToMap(listTag);
        return getTotalStrengthForFunctionFromMap(map, strengthFunction);
    }

    private static float getTotalStrengthForFunctionFromInnate(ItemStack itemStack, Function<Modifier, Float> strengthFunction){
        Item item = itemStack.getItem();
        if(item instanceof InnateModifierItem innateModifierItem){
            return getTotalStrengthForFunctionFromMap(innateModifierItem.getInnateModifierMap(), strengthFunction);
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

    // Public endpoints

    public static float getFloatModifierValue(ItemStack itemStack, FloatModifier floatModifier){
        return getItemStackModifierStrength(itemStack, floatModifier);
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
}
