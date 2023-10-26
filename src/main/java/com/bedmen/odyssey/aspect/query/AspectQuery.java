package com.bedmen.odyssey.aspect.query;

import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;

import java.util.Optional;
import java.util.function.BinaryOperator;

public abstract class AspectQuery<T> {

    private final BinaryOperator<T> addition;
    protected final Class<T> clazz;

    public AspectQuery(BinaryOperator<T> addition, Class<T> clazz){
        this.addition = addition;
        this.clazz = clazz;
    }

    public Optional<T> add(Optional<T> value1, Optional<T> value2){
        if(value1.isEmpty()){
            return value2;
        }
        if(value2.isEmpty()){
            return value1;
        }
        return Optional.of(this.addition.apply(value1.get(), value2.get()));
    }

    public abstract Optional<T> query(AspectHolder aspectHolders);
}
