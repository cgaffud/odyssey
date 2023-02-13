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
import org.checkerframework.checker.units.qual.A;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class AspectInstance {

    public static final float INFUSION_PENALTY = 0.5f;
    public static final String ID_TAG = "id";
    private static final String STRENGTH_TAG = "strength";
    private static final String DISPLAY_TAG = "display";
    private static final String OBFUSCATED_TAG = "obfuscated";

    public final Aspect aspect;
    public final float strength;
    public final AspectTooltipDisplaySetting aspectTooltipDisplaySetting;
    public final boolean obfuscated;

    public AspectInstance(FloatAspect floatAspect, float strength){
        this((Aspect)floatAspect, strength);
    }

    public AspectInstance(IntegerAspect integerAspect, int strength){
        this(integerAspect, (float)strength);
    }

    public AspectInstance(BooleanAspect booleanAspect){
        this(booleanAspect, 1.0f);
    }

    public AspectInstance(String aspectID, float strength){
        this(Aspects.ASPECT_REGISTER.get(aspectID), strength);
    }

    public AspectInstance(Aspect aspect, float strength){
        this(aspect, strength, AspectTooltipDisplaySetting.ALWAYS, false);
    }

    public AspectInstance(Aspect aspect, float strength, AspectTooltipDisplaySetting aspectTooltipDisplaySetting, boolean obfuscated){
        this.aspect = aspect;
        this.strength = strength;
        this.aspectTooltipDisplaySetting = aspectTooltipDisplaySetting;
        this.obfuscated = obfuscated;
    }

    public AspectInstance withDisplaySetting(AspectTooltipDisplaySetting aspectTooltipDisplaySetting){
        return new AspectInstance(this.aspect, this.strength, aspectTooltipDisplaySetting, this.obfuscated);
    }

    public AspectInstance withObfuscation(){
        return new AspectInstance(this.aspect, this.strength, aspectTooltipDisplaySetting, true);
    }

    public MutableComponent getMutableComponent(AspectTooltipContext aspectTooltipContext){
        return this.aspect.aspectTooltipFunction.apply(new AspectTooltipFunctionInput(this, aspectTooltipContext.optionalLevel, aspectTooltipContext.optionalItemStack));
    }

    public CompoundTag toCompoundTag(){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(ID_TAG, this.aspect.id);
        compoundTag.putFloat(STRENGTH_TAG, this.strength);
        compoundTag.putString(DISPLAY_TAG, this.aspectTooltipDisplaySetting.name());
        compoundTag.putBoolean(OBFUSCATED_TAG, this.obfuscated);
        return  compoundTag;
    }

    public static AspectInstance fromCompoundTag(CompoundTag compoundTag){
        String id = compoundTag.getString(ID_TAG);
        Aspect aspect = Aspects.ASPECT_REGISTER.get(id);
        if(aspect == null) {
            Odyssey.LOGGER.error("Unknown aspect: "+id);
            return null;
        } else {
            float strength = compoundTag.getFloat(STRENGTH_TAG);
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
            return new AspectInstance(aspect, strength, aspectTooltipDisplaySetting, obfuscated);
        }
    }

    public void toNetwork(FriendlyByteBuf friendlyByteBuf){
        friendlyByteBuf.writeNbt(this.toCompoundTag());
    }

    public static BiConsumer<FriendlyByteBuf, AspectInstance> toNetworkStatic = (friendlyByteBuf, aspectInstance) -> aspectInstance.toNetwork(friendlyByteBuf);

    public static AspectInstance fromNetwork(FriendlyByteBuf friendlyByteBuf){
        return fromCompoundTag(friendlyByteBuf.readNbt());
    }

    public float getModifiability(ItemStack itemStack){
        return this.aspect.getWeight(itemStack.getItem()) * this.strength;
    }

    public AspectInstance applyInfusionPenalty(){
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
}
