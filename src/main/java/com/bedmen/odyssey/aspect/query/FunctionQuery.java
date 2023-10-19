package com.bedmen.odyssey.aspect.query;

import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspect;

import java.util.Arrays;
import java.util.List;
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
        this.value = base;
    }

    public void query(AspectHolder... aspectHolders) {
        this.query(Arrays.asList(aspectHolders));
    }

    public void query(List<AspectHolder> aspectHolderList) {
        for (AspectHolder aspectHolder : aspectHolderList) {
            Map<Aspect<?>, AspectInstance<?>> map = aspectHolder.map;
            for (Aspect<?> aspect : map.keySet()) {
                AspectInstance<?> aspectInstance = map.get(aspect);
                if (this.clazz.isInstance(aspectInstance.value)) {
                    T aspectValue = this.clazz.cast(aspectInstance.value);
                    this.value = this.add.apply(this.value, this.multiply.apply(aspectValue, this.valueFunction.apply(aspect)));
                }
            }
        }
    }

    public T returnValue() {
        return this.value;
    }

    public static Function<Function<Aspect<?>, Float>, FunctionQuery<Float>> floatQuery = valueFunction ->
            new FunctionQuery<>(valueFunction, Float::sum, (f1,f2) -> f1 * f2, 0f, Float.class);
}
