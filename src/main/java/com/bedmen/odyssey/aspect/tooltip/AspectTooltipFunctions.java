package com.bedmen.odyssey.aspect.tooltip;

import com.bedmen.odyssey.aspect.object.ActivationAspect;
import com.bedmen.odyssey.aspect.object.BonusDamageAspect;
import com.bedmen.odyssey.aspect.object.MultishotAspect;
import com.bedmen.odyssey.items.aspect_items.AspectShieldItem;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.LevelAccessor;

public class AspectTooltipFunctions {
    public static final AspectTooltipFunction NAME = (input -> input.aspectInstance().aspect.getComponent());
    public static final AspectTooltipFunction PERCENTAGE_DELCARATION = (input -> Component.literal(StringUtil.percentFormat(input.aspectInstance().strength)+" ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction NUMBER_ADDITION = (input -> Component.literal("+"+StringUtil.floatFormat(input.aspectInstance().strength)+" ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction TIME = (input -> Component.literal(StringUtil.timeFormat((int)input.aspectInstance().strength)+" ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction BONUS_DAMAGE = (input -> Component.literal("+"+StringUtil.floatFormat( input.aspectInstance().strength * (input.optionalItemStack().isPresent() ? BonusDamageAspect.getStrengthAmplifier(input.optionalItemStack().get().getItem()) : 1.0f))+" ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction PERCENTAGE_ADDITION = (input -> Component.literal("+"+StringUtil.percentFormat(input.aspectInstance().strength)+" ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction HP_THRESHHOLD = (input -> Component.literal("+"+StringUtil.floatFormat(input.aspectInstance().strength)+"HP ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction MULTISHOT = (input -> Component.literal( MultishotAspect.strengthToNumberOfSideProjectiles(input.aspectInstance().strength)+" ").append(input.aspectInstance().aspect.getComponent()).append(Component.translatable("aspect.oddc.multishot.damage_penalty", StringUtil.percentFormat(MultishotAspect.strengthToDamagePenalty(input.aspectInstance().strength)))));
    public static final AspectTooltipFunction GLIDE = (input -> Component.literal(StringUtil.timeFormat((int)input.aspectInstance().strength)+" ").append(input.aspectInstance().aspect.getComponent()).append(Component.translatable("aspect.oddc.activation_key", ((ActivationAspect)input.aspectInstance().aspect).getKeyboardKey())));
    public static final AspectTooltipFunction NAME_AND_ACTIVATION_KEY = (input -> input.aspectInstance().aspect.getComponent().append(Component.translatable("aspect.oddc.activation_key", ((ActivationAspect)input.aspectInstance().aspect).getKeyboardKey())));
    public static final AspectTooltipFunction ADDITIONAL_SHIELD_DAMAGE_BLOCK = (input -> Component.literal("+"+StringUtil.floatFormat(input.aspectInstance().strength)+" ").append(input.aspectInstance().aspect.getComponent()));
}
