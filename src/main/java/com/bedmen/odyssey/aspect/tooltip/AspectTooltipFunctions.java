package com.bedmen.odyssey.aspect.tooltip;

import com.bedmen.odyssey.aspect.object.ActivationAspect;
import com.bedmen.odyssey.aspect.object.BonusDamageAspect;
import com.bedmen.odyssey.aspect.object.MultishotAspect;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.Component;

public class AspectTooltipFunctions {

    // Boolean
    public static final AspectTooltipFunction<Void> NAME = (input -> input.aspectInstance().aspect.getComponent());
    public static final AspectTooltipFunction<Void> NAME_AND_ACTIVATION_KEY = (input -> input.aspectInstance().aspect.getComponent().append(Component.translatable("aspect.oddc.activation_key", ((ActivationAspect)input.aspectInstance().aspect).getKeyboardKey())));

    // Integer
    public static final AspectTooltipFunction<Integer> INTEGER_ADDITION = (input -> Component.literal(StringUtil.additiveFloatFormat(input.aspectInstance().value)+" ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction<Integer> ROMAN_NUMERAL = (input -> input.aspectInstance().aspect.getComponent().append(" ").append(Component.translatable("enchantment.level." + input.aspectInstance().value)));
    public static final AspectTooltipFunction<Integer> GLIDE = (input -> Component.literal(StringUtil.timeFormat(input.aspectInstance().value)+" ").append(input.aspectInstance().aspect.getComponent()).append(Component.translatable("aspect.oddc.activation_key", ((ActivationAspect)input.aspectInstance().aspect).getKeyboardKey())));

    // Float
    public static final AspectTooltipFunction<Float> FLOAT_ADDITION = (input -> Component.literal(StringUtil.additiveFloatFormat(input.aspectInstance().value)+" ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction<Float> PERCENTAGE_DELCARATION = (input -> Component.literal(StringUtil.percentFormat(input.aspectInstance().value)+" ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction<Float> PERCENTAGE_ADDITION = (input -> Component.literal(StringUtil.additivePercentFormat(input.aspectInstance().value)+" ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction<Float> FLOAT_PER_SECOND = (input -> Component.literal(StringUtil.additiveFloatFormat(input.aspectInstance().value * 20f)+" ").append(input.aspectInstance().aspect.getComponent()).append(Component.translatable("aspect_tooltip.oddc.per_second")));
    public static final AspectTooltipFunction<Float> BONUS_DAMAGE = (input -> Component.literal(StringUtil.additiveFloatFormat( input.aspectInstance().value * (input.optionalItemStack().isPresent() ? BonusDamageAspect.getStrengthAmplifier(input.optionalItemStack().get().getItem()) : 1.0f))+" ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction<Float> HP_THRESHHOLD = (input -> Component.literal(StringUtil.additiveFloatFormat(input.aspectInstance().value)+"HP ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction<Float> MULTISHOT = (input -> Component.literal( MultishotAspect.valueToNumberOfSideProjectiles(input.aspectInstance().value)+" ").append(input.aspectInstance().aspect.getComponent()).append(Component.translatable("aspect.oddc.multishot.damage_penalty", StringUtil.percentFormat(MultishotAspect.valueToDamagePenalty(input.aspectInstance().value)))));
    public static final AspectTooltipFunction<Float> ADDITIONAL_SHIELD_DAMAGE_BLOCK = (input -> Component.literal(StringUtil.additiveFloatFormat(input.aspectInstance().value)+" ").append(input.aspectInstance().aspect.getComponent()));
}
