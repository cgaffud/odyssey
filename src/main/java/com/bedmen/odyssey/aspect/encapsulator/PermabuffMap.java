package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.aspect.object.PermabuffAspect;
import com.bedmen.odyssey.util.NonNullMap;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;

import java.util.List;

public class PermabuffMap extends NonNullMap<PermabuffAspect, Integer> {
    public PermabuffMap(){
        super();
    }

    public PermabuffMap(List<AspectInstance> permabuffList){
        super();
        permabuffList.forEach(aspectInstance -> this.put((PermabuffAspect)aspectInstance.aspect, (int)aspectInstance.strength));
    }

    @Override
    protected Integer defaultValue() {
        return 0;
    }

    @Override
    protected String keyToString(PermabuffAspect permabuffAspect) {
        return permabuffAspect.id;
    }

    @Override
    protected PermabuffAspect stringToKey(String string) {
        return (PermabuffAspect) Aspects.ASPECT_REGISTER.get(string);
    }

    @Override
    protected Tag valueToTag(Integer integer) {
        return IntTag.valueOf(integer);
    }

    @Override
    protected Integer tagToValue(Tag tag) {
        return ((IntTag)tag).getAsInt();
    }

    @Override
    protected Integer combineValues(Integer v1, Integer v2) {
        return v1 + v2;
    }

    public PermabuffMap copy(){
        PermabuffMap permabuffMap = new PermabuffMap();
        permabuffMap.putAll(this);
        return permabuffMap;
    }

}
