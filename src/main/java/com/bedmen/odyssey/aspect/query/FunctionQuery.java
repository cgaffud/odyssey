package com.bedmen.odyssey.aspect.query;

import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspect;

import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class FunctionQuery<T> extends AspectQuery<T> {

    public final Function<AspectInstance<?>, T> valueFunction;

    public FunctionQuery(Function<AspectInstance<?>, T> valueFunction, BinaryOperator<T> add, T base, Class<T> clazz){
        super(add, base, clazz);
        this.valueFunction = valueFunction;
    }

    public T query(AspectHolder aspectHolder) {
        T value = this.base;
        Map<Aspect<?>, AspectInstance<?>> map = aspectHolder.map;
        for (Aspect<?> aspect : map.keySet()) {
            AspectInstance<?> aspectInstance = map.get(aspect);
            if (this.clazz.isInstance(aspectInstance.value)) {
                value = this.addition.apply(value, this.valueFunction.apply(aspectInstance));
            }
        }
        return value;
    }

    public static Function<Function<AspectInstance<?>, Float>, FunctionQuery<Float>> floatQuery = valueFunction ->
            new FunctionQuery<>(valueFunction, Float::sum, 0f, Float.class);
}
