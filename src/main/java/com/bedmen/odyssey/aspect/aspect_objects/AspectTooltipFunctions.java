package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AspectTooltipFunction;
import com.bedmen.odyssey.items.aspect_items.AspectShieldItem;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.LevelAccessor;

public class AspectTooltipFunctions {
    protected static final AspectTooltipFunction NAME = ((aspectInstance, optionalLevel) -> aspectInstance.aspect.getName());
    protected static final AspectTooltipFunction PERCENTAGE_DELCARATION = ((aspectInstance, optionalLevel) -> new TextComponent(StringUtil.percentFormat(aspectInstance.strength)+" ").append(aspectInstance.aspect.getName()));
    protected static final AspectTooltipFunction NUMBER_ADDITION = ((aspectInstance, optionalLevel) -> new TextComponent("+"+StringUtil.floatFormat(aspectInstance.strength)+" ").append(aspectInstance.aspect.getName()));
    protected static final AspectTooltipFunction PERCENTAGE_ADDITION = ((aspectInstance, optionalLevel) -> new TextComponent("+"+StringUtil.percentFormat(aspectInstance.strength)+" ").append(aspectInstance.aspect.getName()));
    protected static final AspectTooltipFunction HP_THRESHHOLD = ((aspectInstance, optionalLevel) -> new TextComponent(StringUtil.floatFormat(aspectInstance.strength)+"HP ").append(aspectInstance.aspect.getName()));
    protected static final AspectTooltipFunction MULTISHOT = ((aspectInstance, optionalLevel) -> new TextComponent( MultishotAspect.strengthToNumberOfSideProjectiles(aspectInstance.strength)+" ").append(aspectInstance.aspect.getName()).append(new TranslatableComponent("aspect.oddc.multishot.damage_penalty", MultishotAspect.strengthToDamagePenalty(aspectInstance.strength))));
    protected static final AspectTooltipFunction GLIDE = ((aspectInstance, optionalLevel) -> new TextComponent(StringUtil.timeFormat((int)aspectInstance.strength)+" ").append(aspectInstance.aspect.getName()).append(new TranslatableComponent("aspect.oddc.activation_key", ((ActivationAspect)aspectInstance.aspect).getKey())));
    protected static final AspectTooltipFunction NAME_AND_ACTIVATION_KEY = ((aspectInstance, optionalLevel) -> aspectInstance.aspect.getName().append(new TranslatableComponent("aspect.oddc.activation_key", ((ActivationAspect)aspectInstance.aspect).getKey())));
    protected static final AspectTooltipFunction ADDITIONAL_SHIELD_DAMAGE_BLOCK = ((aspectInstance, optionalLevel) -> new TextComponent("+"+StringUtil.floatFormat(AspectShieldItem.getDifficultyAdjustedDamageBlock(aspectInstance.strength, optionalLevel.map(LevelAccessor::getDifficulty).orElse(Difficulty.NORMAL)))+" ").append(aspectInstance.aspect.getName()));
}
