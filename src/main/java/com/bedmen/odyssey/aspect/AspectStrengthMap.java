package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.aspect.aspect_objects.Aspect;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.K;

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
            return super.get(aspect);
        }
        return 0.0f;
    }

    public <V extends Float, K extends Aspect> V get(K key) {
        throw new IllegalArgumentException("Do not use get, use getNonNull");
    }

    public AspectStrengthMap combine(AspectStrengthMap map){
        AspectStrengthMap aspectStrengthMap = new AspectStrengthMap();
        aspectStrengthMap.putAll(this);
        map.forEach((aspect, strength) -> aspectStrengthMap.put(aspect, aspectStrengthMap.getNonNull(aspect) + strength));
        return aspectStrengthMap;
    }

    public CompoundTag toCompoundTag(){
        CompoundTag compoundTag = new CompoundTag();
        this.forEach(((aspect, strength) -> compoundTag.put(aspect.id, FloatTag.valueOf(strength))));
        return compoundTag;
    }

    public static AspectStrengthMap fromCompoundTag(CompoundTag compoundTag){
        AspectStrengthMap aspectStrengthMap = new AspectStrengthMap();
        for(String key: compoundTag.getAllKeys()){
            if(Aspects.ASPECT_REGISTER.containsKey(key)){
                aspectStrengthMap.put(Aspects.ASPECT_REGISTER.get(key), compoundTag.getFloat(key));
            }
        }
        return aspectStrengthMap;
    }
}
