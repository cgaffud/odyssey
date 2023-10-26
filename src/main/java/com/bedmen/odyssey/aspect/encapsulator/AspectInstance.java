package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.object.*;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipContext;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipDisplaySetting;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctionInput;
import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nullable;
import java.util.Optional;

public class AspectInstance<T> {

    public static final float INFUSION_PENALTY = 0.5f;
    public static final String ID_TAG = "id";
    private static final String VALUE_TAG = "value";
    private static final String DISPLAY_TAG = "display";
    private static final String OBFUSCATED_TAG = "obfuscated";

    public final Aspect<T> aspect;
    public final T value;
    public final AspectTooltipDisplaySetting aspectTooltipDisplaySetting;
    public final boolean obfuscated;

    public AspectInstance(Aspect<T> aspect){
        this(aspect, aspect.getBase(), AspectTooltipDisplaySetting.ALWAYS, false);
    }

    public AspectInstance(Aspect<T> aspect, T value){
        this(aspect, value, AspectTooltipDisplaySetting.ALWAYS, false);
    }

    public AspectInstance(Aspect<T> aspect, JsonElement value){
        this(aspect, aspect.jsonElementToValue(value), AspectTooltipDisplaySetting.ALWAYS, false);
    }

    public static <T> AspectInstance<T> fromCommand(Aspect<T> aspect, Optional<String> optionalValue){
        return optionalValue.map(s -> new AspectInstance<>(aspect, aspect.stringToValue(s))).orElseGet(() -> new AspectInstance<>(aspect));
    }

    public AspectInstance(Aspect<T> aspect, CompoundTag compoundTag, AspectTooltipDisplaySetting aspectTooltipDisplaySetting, boolean obfuscated){
        this.aspect = aspect;
        this.value = aspect.tagToValue(compoundTag.get(VALUE_TAG));
        this.aspectTooltipDisplaySetting = aspectTooltipDisplaySetting;
        this.obfuscated = obfuscated;
    }

    public AspectInstance(Aspect<T> aspect, T value, AspectTooltipDisplaySetting aspectTooltipDisplaySetting, boolean obfuscated){
        this.aspect = aspect;
        this.value = value;
        this.aspectTooltipDisplaySetting = aspectTooltipDisplaySetting;
        this.obfuscated = obfuscated;
    }

    public AspectInstance<T> withDisplaySetting(AspectTooltipDisplaySetting aspectTooltipDisplaySetting){
        return new AspectInstance<>(this.aspect, this.value, aspectTooltipDisplaySetting, this.obfuscated);
    }

    public AspectInstance<T> withObfuscation(){
        return new AspectInstance<>(this.aspect, this.value, aspectTooltipDisplaySetting, true);
    }

    public MutableComponent getMutableComponent(AspectTooltipContext aspectTooltipContext){
        return this.aspect.aspectTooltipFunction.apply(new AspectTooltipFunctionInput(this, aspectTooltipContext.optionalItemStack));
    }

    public CompoundTag toCompoundTag(){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(ID_TAG, this.aspect.id);
        if(!(this.aspect instanceof UnitAspect)){
            compoundTag.put(VALUE_TAG, this.aspect.valueToTag(this.value));
        }
        compoundTag.putString(DISPLAY_TAG, this.aspectTooltipDisplaySetting.name());
        compoundTag.putBoolean(OBFUSCATED_TAG, this.obfuscated);
        return compoundTag;
    }

    @Nullable
    public static AspectInstance<?> fromCompoundTag(CompoundTag compoundTag){
        String id = compoundTag.getString(ID_TAG);
        Aspect<?> aspect = Aspects.ASPECT_REGISTER.get(id);
        if(aspect == null) {
            Odyssey.LOGGER.error("Unknown aspect: "+id);
            return null;
        } else {
            String displaySettingName = compoundTag.getString(DISPLAY_TAG);
            AspectTooltipDisplaySetting aspectTooltipDisplaySetting;
            if(displaySettingName.isEmpty()){
                aspectTooltipDisplaySetting = AspectTooltipDisplaySetting.ALWAYS;
            } else {
                try{
                    aspectTooltipDisplaySetting = AspectTooltipDisplaySetting.valueOf(displaySettingName);
                } catch (IllegalArgumentException illegalArgumentException){
                    Odyssey.LOGGER.error("Unknown AspectTooltipDisplaySetting: "+displaySettingName);
                    aspectTooltipDisplaySetting = AspectTooltipDisplaySetting.ALWAYS;
                }
            }
            boolean obfuscated = compoundTag.getBoolean(OBFUSCATED_TAG);
            return new AspectInstance<>(aspect, compoundTag, aspectTooltipDisplaySetting, obfuscated);
        }
    }

    public void toNetwork(FriendlyByteBuf friendlyByteBuf){
        friendlyByteBuf.writeNbt(this.toCompoundTag());
    }

    @Nullable
    public static AspectInstance<?> fromNetwork(FriendlyByteBuf friendlyByteBuf){
        return fromCompoundTag(friendlyByteBuf.readNbt());
    }

    public float getModifiability(){
        return this.aspect.weightFunction.apply(this.value);
    }

    public AspectInstance<T> applyInfusionPenalty(){
        return this.aspect.createInstanceForInfusion(this);
    }

    public AspectInstance<T> withAddedValue(Object value){
        if(this.value.getClass().isInstance(value)){
            return new AspectInstance<>(this.aspect, this.aspect.getAddition().apply(this.value, (T)value), this.aspectTooltipDisplaySetting, this.obfuscated);
        }
        return this;
    }

    public AspectInstance<T> withMultipliedStrength(int multiplier){
        return new AspectInstance<T>(this.aspect, this.aspect.getScaler().apply(this.value, multiplier), this.aspectTooltipDisplaySetting, this.obfuscated);
    }

    // Only call this if you know aspectInstance has the same aspect as this, and that Aspect is a ComparableAspect
    public int compareWith(AspectInstance<?> aspectInstance){
        ComparableAspect<T> comparableAspect = (ComparableAspect<T>)this.aspect;
        return comparableAspect.getComparator().compare(this.value, this.aspect.getValueClass().cast(aspectInstance.value));
    }
}
