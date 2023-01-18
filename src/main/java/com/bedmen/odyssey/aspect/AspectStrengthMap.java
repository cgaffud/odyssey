package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.aspect.aspect_objects.Aspect;

import java.util.HashMap;
import java.util.List;

public class AspectStrengthMap extends HashMap<Aspect, Float> {
    public AspectStrengthMap(){
        super();
    }

    public AspectStrengthMap(List<AspectInstance> aspectInstanceList){
        super();
        for(AspectInstance aspectInstance : aspectInstanceList){
            this.put(aspectInstance.aspect, this.getNonNull(aspectInstance.aspect) + aspectInstance.strength);
        }
    }

    public float getNonNull(Aspect aspect) {
        if(this.containsKey(aspect)){
            return this.get(aspect);
        }
        return 0.0f;
    }

    public AspectStrengthMap combine(AspectStrengthMap map){
        AspectStrengthMap aspectStrengthMap = new AspectStrengthMap();
        aspectStrengthMap.putAll(this);
        map.forEach((aspect, strength) -> aspectStrengthMap.put(aspect, aspectStrengthMap.getNonNull(aspect) + strength));
        return aspectStrengthMap;
    }
}
