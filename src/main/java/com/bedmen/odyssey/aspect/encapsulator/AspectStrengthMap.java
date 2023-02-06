package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.aspect.object.Aspect;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.util.NonNullMap;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;

import javax.annotation.Nullable;
import java.util.List;

public class AspectStrengthMap extends NonNullMap<Aspect, Float> {
    public AspectStrengthMap(){
        super();
    }

    @Override
    protected Float defaultValue() {
        return 0.0f;
    }

    @Override
    protected String keyToString(Aspect aspect) {
        return aspect.id;
    }

    @Override
    @Nullable
    protected Aspect stringToKey(String string) {
        return Aspects.ASPECT_REGISTER.get(string);
    }

    @Override
    protected Tag valueToTag(Float aFloat) {
        return FloatTag.valueOf(aFloat);
    }

    @Override
    protected Float tagToValue(Tag tag) {
        return ((FloatTag)tag).getAsFloat();
    }

    @Override
    protected Float combineValues(Float v1, Float v2) {
        return v1 + v2;
    }

    public AspectStrengthMap(List<AspectInstance> aspectInstanceList){
        super();
        for(AspectInstance aspectInstance : aspectInstanceList){
            this.put(aspectInstance.aspect, this.getNonNull(aspectInstance.aspect) + aspectInstance.strength);
        }
    }
}
