package com.bedmen.odyssey.effect;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.List;

public class AspectEffect extends OdysseyEffect {

    public final List<AspectInstance<?>> aspectInstanceList;
    public AspectEffect(MobEffectCategory typeIn, List<AspectInstance<?>> aspectInstanceList) {
        super(typeIn, 0, true);
        this.aspectInstanceList = aspectInstanceList;
    }
}
