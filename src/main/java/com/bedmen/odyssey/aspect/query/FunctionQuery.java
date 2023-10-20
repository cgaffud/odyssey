package com.bedmen.odyssey.aspect.query;

import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspect;

import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class FunctionQuery<T> extends AspectQuery<T> {

    public final Function<Aspect<?>, T> valueFunction;
    public final BinaryOperator<T> multiply;

    public FunctionQuery(Function<Aspect<?>, T> valueFunction, BinaryOperator<T> add, BinaryOperator<T> multiply, T base, Class<T> clazz){
        super(add, base, clazz);
        this.valueFunction = valueFunction;
        this.multiply = multiply;
    }

    public T query(AspectHolder aspectHolder) {
        T value = this.base;
        Map<Aspect<?>, AspectInstance<?>> map = aspectHolder.map;
        for (Aspect<?> aspect : map.keySet()) {
            AspectInstance<?> aspectInstance = map.get(aspect);
            if (this.clazz.isInstance(aspectInstance.value)) {
                T aspectValue = this.clazz.cast(aspectInstance.value);
                value = this.addition.apply(value, this.multiply.apply(aspectValue, this.valueFunction.apply(aspect)));
            }
        }
        return value;
    }

    public static Function<Function<Aspect<?>, Float>, FunctionQuery<Float>> floatQuery = valueFunction ->
            new FunctionQuery<>(valueFunction, Float::sum, (f1,f2) -> f1 * f2, 0f, Float.class);
}
