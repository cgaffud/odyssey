package com.bedmen.odyssey.aspect.query;

import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;

import java.util.function.BinaryOperator;

public abstract class AspectQuery<T> {

    public final BinaryOperator<T> addition;
    protected final Class<T> clazz;
    public final T base;

    public AspectQuery(BinaryOperator<T> addition, T base, Class<T> clazz){
        this.addition = addition;
        this.base = base;
        this.clazz = clazz;
    }

    public abstract T query(AspectHolder aspectHolders);
}
