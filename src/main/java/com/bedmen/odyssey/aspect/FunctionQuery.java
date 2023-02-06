package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.aspect.encapsulator.AspectStrengthMap;
import com.bedmen.odyssey.aspect.object.Aspect;

import java.util.function.Function;

public class FunctionQuery implements AspectQuery {

    public final Function<Aspect, Float> strengthFunction;

    public FunctionQuery(Function<Aspect, Float> strengthFunction){
        this.strengthFunction = strengthFunction;
    }

    public float queryStrengthMap(AspectStrengthMap aspectStrengthMap) {
        return aspectStrengthMap.entrySet().stream()
                .map(entry -> this.strengthFunction.apply(entry.getKey()) * entry.getValue())
                .reduce(Float::sum).orElse(0.0f);
    }
}
