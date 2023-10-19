package com.bedmen.odyssey.aspect.query;

import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;

import java.util.List;
import java.util.function.BinaryOperator;

public abstract class AspectQuery<T> {

    protected final BinaryOperator<T> add;
    protected final Class<T> clazz;
    protected T value;

    public AspectQuery(BinaryOperator<T> add, T base, Class<T> clazz){
        this.add = add;
        this.value = base;
        this.clazz = clazz;
    }

    public abstract void query(AspectHolder... aspectHolders);
    public abstract void query(List<AspectHolder> aspectHolders);
    public abstract T returnValue();
}
