package com.bedmen.odyssey.aspect.tooltip;

import com.bedmen.odyssey.aspect.object.ActivationAspect;
import com.bedmen.odyssey.aspect.object.BonusDamageAspect;
import com.bedmen.odyssey.aspect.object.MultishotAspect;
import com.bedmen.odyssey.items.aspect_items.AspectShieldItem;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.LevelAccessor;

public class AspectTooltipFunctions {
    public static final AspectTooltipFunction NAME = (input -> input.aspectInstance().aspect.getComponent());
    public static final AspectTooltipFunction PERCENTAGE_DELCARATION = (input -> new TextComponent(StringUtil.percentFormat(input.aspectInstance().strength)+" ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction NUMBER_ADDITION = (input -> new TextComponent("+"+StringUtil.floatFormat(input.aspectInstance().strength)+" ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction BONUS_DAMAGE = (input -> new TextComponent("+"+StringUtil.floatFormat(input.aspectInstance().strength * BonusDamageAspect.getStrengthAmplifier(input.itemStack().getItem()))+" ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction PERCENTAGE_ADDITION = (input -> new TextComponent("+"+StringUtil.percentFormat(input.aspectInstance().strength)+" ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction HP_THRESHHOLD = (input -> new TextComponent(StringUtil.floatFormat(input.aspectInstance().strength)+"HP ").append(input.aspectInstance().aspect.getComponent()));
    public static final AspectTooltipFunction MULTISHOT = (input -> new TextComponent( MultishotAspect.strengthToNumberOfSideProjectiles(input.aspectInstance().strength)+" ").append(input.aspectInstance().aspect.getComponent()).append(new TranslatableComponent("aspect.oddc.multishot.damage_penalty", MultishotAspect.strengthToDamagePenalty(input.aspectInstance().strength))));
    public static final AspectTooltipFunction GLIDE = (input -> new TextComponent(StringUtil.timeFormat((int)input.aspectInstance().strength)+" ").append(input.aspectInstance().aspect.getComponent()).append(new TranslatableComponent("aspect.oddc.activation_key", ((ActivationAspect)input.aspectInstance().aspect).getKey())));
    public static final AspectTooltipFunction NAME_AND_ACTIVATION_KEY = (input -> input.aspectInstance().aspect.getComponent().append(new TranslatableComponent("aspect.oddc.activation_key", ((ActivationAspect)input.aspectInstance().aspect).getKey())));
    public static final AspectTooltipFunction ADDITIONAL_SHIELD_DAMAGE_BLOCK = (input -> new TextComponent("+"+StringUtil.floatFormat(AspectShieldItem.getDifficultyAdjustedDamageBlock(input.aspectInstance().strength, input.optionalLevel().map(LevelAccessor::getDifficulty).orElse(Difficulty.NORMAL)))+" ").append(input.aspectInstance().aspect.getComponent()));
}
