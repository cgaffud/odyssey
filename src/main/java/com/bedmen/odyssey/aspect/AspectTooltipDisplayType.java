package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.aspect.aspect_objects.MultishotAspect;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.Function;

public enum AspectTooltipDisplayType {
    DECLARATION(aspectInstance ->  new TranslatableComponent("aspect.oddc.tooltip.declaration", aspectInstance.aspect.id)),
    NUMBER_ADDITION(aspectInstance ->  new TranslatableComponent("aspect.oddc.tooltip.addition", aspectInstance.strength, aspectInstance.aspect.id)),
    PERCENT_ADDITION(aspectInstance ->  new TranslatableComponent("aspect.oddc.tooltip.addition", StringUtil.percentFormat(aspectInstance.strength), aspectInstance.aspect.id)),
    MULTIPLICATION(aspectInstance ->  new TranslatableComponent("aspect.oddc.tooltip.multiplication", aspectInstance.strength, aspectInstance.aspect.id)),
    HP_TRESHOLD(aspectInstance ->  new TranslatableComponent("aspect.oddc.tooltip.hp_threshold", aspectInstance.strength, aspectInstance.aspect.id)),
    ACTIVATION_KEY(aspectInstance ->  new TranslatableComponent("aspect.oddc.tooltip.has_activation_key", aspectInstance.aspect.id, ((ActivatedAspect)aspectInstance.aspect).key)),
    GLIDE(aspectInstance ->  new TranslatableComponent("aspect.oddc.tooltip.glide", StringUtil.timeFormat((int)aspectInstance.strength), aspectInstance.aspect.id, ((ActivatedAspect)aspectInstance.aspect).key)),
    ADDITION_REDUCED_DAMAGE(aspectInstance ->  new TranslatableComponent("aspect.oddc.tooltip.addition_with_reduced_damage", MultishotAspect.strengthToNumberOfSideProjectiles(aspectInstance.strength), aspectInstance.aspect.id, MultishotAspect.strengthToDamagePenalty(aspectInstance.strength)));

    public final Function<AspectInstance, MutableComponent> mutableComponentFunction;

    AspectTooltipDisplayType(Function<AspectInstance, MutableComponent> mutableComponentFunction){
        this.mutableComponentFunction = mutableComponentFunction;
    }
}
