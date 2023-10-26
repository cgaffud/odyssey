package com.bedmen.odyssey.aspect.query;

import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspect;

import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;

public class SingleQuery<T> extends AspectQuery<T> {

    private final Aspect<T> aspect;

    public SingleQuery(Aspect<T> aspect){
        super(aspect.getAddition(), aspect.getValueClass());
        this.aspect = aspect;
    }

    public Optional<T> query(AspectHolder aspectHolder) {
        Map<Aspect<?>, AspectInstance<?>> map = aspectHolder.map;
        if(map.containsKey(this.aspect)){
            return Optional.of(clazz.cast(map.get(aspect).value));
        } else {
            return Optional.empty();
        }
    }
}
