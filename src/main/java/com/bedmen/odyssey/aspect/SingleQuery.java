package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.aspect.encapsulator.AspectStrengthMap;
import com.bedmen.odyssey.aspect.object.Aspect;

public class SingleQuery implements AspectQuery {

    private final Aspect aspect;

    public SingleQuery(Aspect aspect){
        this.aspect = aspect;
    }

    public float queryStrengthMap(AspectStrengthMap aspectStrengthMap) {
        return aspectStrengthMap.get(this.aspect);
    }
}
