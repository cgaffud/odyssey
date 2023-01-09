package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.EnvironmentConditionalMeleeAspect;
import com.bedmen.odyssey.enchantment.odyssey.ConditionalAmpEnchantment;
import com.bedmen.odyssey.items.innate_aspect_items.InnateAspectItem;
import com.bedmen.odyssey.items.innate_aspect_items.InnateAspectMeleeItem;
import com.bedmen.odyssey.items.innate_aspect_items.InnateConditionalAmpMeleeItem;
import com.bedmen.odyssey.weapon.WeaponUtil;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class ConditionalAmpUtil {
    public static final String DAMAGE_BOOST_TAG = Odyssey.MOD_ID + ":ConditionalAmpBoost";
    public static final String GRADIENT_COLOR_TAG = Odyssey.MOD_ID + ":GradientColor";

    public interface CondAmpItem {
        Enchantment getEnchantment();
    }

    public static float activeFactor(ItemStack itemStack, LivingEntity livingEntity) {
        //todo remove enchantment
        if (itemStack.getItem() instanceof CondAmpItem condAmpItem && condAmpItem.getEnchantment() instanceof ConditionalAmpEnchantment conditionalAmpEnchantment){
            return conditionalAmpEnchantment.getActiveFactor(livingEntity.level, livingEntity);
        }

        if (itemStack.getItem() instanceof InnateConditionalAmpMeleeItem innateConditionalAmpMeleeItem
                && innateConditionalAmpMeleeItem.aspect instanceof EnvironmentConditionalMeleeAspect environmentConditionalMeleeAspect){
            return environmentConditionalMeleeAspect.attackBoostFactorFunction.getBoostFactor(livingEntity.eyeBlockPosition(), livingEntity.level);
        }

        return 0.0f;
    }

    public static float getDamageTag(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getFloat(DAMAGE_BOOST_TAG);
    }

    public static void setDamageTag(ItemStack itemStack, Entity entity, boolean isMelee) {
        // todo remove enchantment
        float enchantmentBonus = EnchantmentUtil.getConditionalAmpBonus(itemStack, entity, isMelee);
        // todo anvil aspect
        Item item = itemStack.getItem();
        float aspectBonus = item instanceof InnateAspectItem innateAspectItem ? AspectUtil.getEnvironmentalAspectStrength(innateAspectItem, entity.eyeBlockPosition(), entity.level) : 0.0f;
        itemStack.getOrCreateTag().putFloat(DAMAGE_BOOST_TAG, enchantmentBonus + aspectBonus);
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
}
