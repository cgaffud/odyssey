package com.bedmen.odyssey.aspect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AspectStrengthMap extends HashMap<Aspect, Float> {
    public AspectStrengthMap(){
        super();
    }

    public AspectStrengthMap(List<AspectInstance> aspectInstanceList){
        super();
        for(AspectInstance aspectInstance : aspectInstanceList){
            this.put(aspectInstance.aspect, aspectInstance.strength);
        }
    }

    public float getNonNull(Aspect aspect) {
        Float f = this.get(aspect);
        if(f == null){
            return 0.0f;
        }
        return f;
    }

    public AspectStrengthMap combine(AspectStrengthMap map){
        AspectStrengthMap aspectStrengthMap = new AspectStrengthMap();
        aspectStrengthMap.putAll(this);
        for(Map.Entry<Aspect, Float> entry: map.entrySet()){
            Aspect aspect = entry.getKey();
            float strength = aspectStrengthMap.getNonNull(aspect) + entry.getValue();
            aspectStrengthMap.put(aspect, strength);
        }
        return aspectStrengthMap;
    }
}
