package com.bedmen.odyssey.aspect;

import java.util.HashMap;

public class AspectStrengthMap extends HashMap<Aspect, Float> {
    public float getNonNull(Aspect aspect) {
        Float f = this.get(aspect);
        if(f == null){
            return 0.0f;
        }
        return f;
    }
}
