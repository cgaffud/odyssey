package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.object.*;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipContext;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipDisplaySetting;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctionInput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

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

    public AspectInstance(Aspect<T> aspect, T value){
        this(aspect, value, AspectTooltipDisplaySetting.ALWAYS, false);
    }

    public AspectInstance(Aspect<T> aspect, CompoundTag compoundTag, AspectTooltipDisplaySetting aspectTooltipDisplaySetting, boolean obfuscated){
        this.aspect = aspect;
        this.value = aspect.tagToValue(compoundTag.getCompound(VALUE_TAG));
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
        compoundTag.put(VALUE_TAG, this.aspect.valueToTag(this.value));
        compoundTag.putString(DISPLAY_TAG, this.aspectTooltipDisplaySetting.name());
        compoundTag.putBoolean(OBFUSCATED_TAG, this.obfuscated);
        return  compoundTag;
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

    public static FriendlyByteBuf.Writer<AspectInstance<?>> toNetworkStatic = (friendlyByteBuf, aspectInstance) -> aspectInstance.toNetwork(friendlyByteBuf);

    @Nullable
    public static AspectInstance fromNetwork(FriendlyByteBuf friendlyByteBuf){
        return fromCompoundTag(friendlyByteBuf.readNbt());
    }

    public float getModifiability(){
        return this.aspect.weight * this.strength;
    }

    public AspectInstance<T> applyInfusionPenalty(){
        return this.aspect.createWeakerInstanceForInfusion(this);

        if(this.aspect instanceof BooleanAspect){
            return this;
        }
        if(this.aspect instanceof IntegerAspect integerAspect){
            if(integerAspect.hasInfusionPenalty){
                return new AspectInstance(this.aspect, Mth.floor(this.strength * INFUSION_PENALTY), this.aspectTooltipDisplaySetting, this.obfuscated);
            } else {
                return this;
            }
        }
        return new AspectInstance(this.aspect, this.strength * INFUSION_PENALTY, this.aspectTooltipDisplaySetting, this.obfuscated);
    }

    public AspectInstance withAddedStrength(float bonusStrength){
        return new AspectInstance(this.aspect, this.strength + bonusStrength, this.aspectTooltipDisplaySetting, this.obfuscated);
    }

    public AspectInstance withMultipliedStrength(float multiplier){
        return new AspectInstance(this.aspect, this.strength * multiplier, this.aspectTooltipDisplaySetting, this.obfuscated);
    }
}
