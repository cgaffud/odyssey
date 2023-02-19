package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.aspect.object.Aspect;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.aspect.object.PermabuffAspect;
import com.bedmen.odyssey.util.NonNullMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;

import java.util.List;

public class PermabuffMap extends NonNullMap<PermabuffAspect, Float> {
    public PermabuffMap(){
        super();
    }

    public PermabuffMap(List<AspectInstance> permabuffList){
        super();
        permabuffList.forEach(aspectInstance -> this.put((PermabuffAspect)aspectInstance.aspect, aspectInstance.strength));
    }

    @Override
    protected Float defaultValue() {
        return 0.0f;
    }

    @Override
    protected String keyToString(PermabuffAspect permabuffAspect) {
        return ((Aspect)permabuffAspect).id;
    }

    @Override
    protected PermabuffAspect stringToKey(String string) {
        return (PermabuffAspect) Aspects.ASPECT_REGISTER.get(string);
    }

    @Override
    protected Tag valueToTag(Float f) {
        return FloatTag.valueOf(f);
    }

    @Override
    protected Float tagToValue(Tag tag) {
        return ((FloatTag)tag).getAsFloat();
    }

    @Override
    protected Float combineValues(Float f1, Float f2) {
        return f1 + f2;
    }

    public PermabuffMap copy(){
        PermabuffMap permabuffMap = new PermabuffMap();
        permabuffMap.putAll(this);
        return permabuffMap;
    }

    public static PermabuffMap fromCompoundTag(CompoundTag compoundTag){
        return NonNullMap.fromCompoundTag(compoundTag, PermabuffMap.class);
    }
}
