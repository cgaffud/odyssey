package com.bedmen.odyssey.aspect.query;

import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspect;

import java.util.Map;
import java.util.function.BinaryOperator;

public class SingleQuery<T> extends AspectQuery<T> {

    private final Aspect<T> aspect;

    public SingleQuery(Aspect<T> aspect){
        super(aspect.getAddition(), aspect.getBase(), aspect.getValueClass());
        this.aspect = aspect;
    }

    public T query(AspectHolder aspectHolder) {
        Map<Aspect<?>, AspectInstance<?>> map = aspectHolder.map;
        if(map.containsKey(this.aspect)){
            return clazz.cast(map.get(aspect).value);
        }
        return this.base;
    }
}
