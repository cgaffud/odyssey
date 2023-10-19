package com.bedmen.odyssey.aspect.query;

import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspect;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class SingleQuery<T> extends AspectQuery<T> {

    private final Aspect<T> aspect;

    public SingleQuery(Aspect<T> aspect, BinaryOperator<T> add, T base, Class<T> clazz){
        super(add, base, clazz);
        this.aspect = aspect;
    }

    public void query(AspectHolder... aspectHolders) {
        this.query(Arrays.asList(aspectHolders));
    }

    public void query(List<AspectHolder> aspectHolderList) {
        for (AspectHolder aspectHolder : aspectHolderList) {
            Map<Aspect<?>, AspectInstance<?>> map = aspectHolder.map;
            if(map.containsKey(this.aspect)){
                this.value = this.add.apply(this.value, clazz.cast(map.get(aspect).value));
            }
        }
    }

    public T returnValue() {
        return this.value;
    }
}
