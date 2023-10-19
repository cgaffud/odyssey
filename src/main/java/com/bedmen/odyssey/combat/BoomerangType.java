package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;

import java.util.List;

public class BoomerangType extends ThrowableType {

    public static final String BOOMERANG_TYPE_PREFIX_TAG = "BoomerangType";
    public final int repairNumber;
    public final int burnTime;

    BoomerangType(String id, double damage, float velocity, int repairNumber, SoundProfile soundProfile, List<AspectInstance<?>> abilityList, List<AspectInstance<?>> innateModifierList, int burnTime){
        super(id, damage, velocity, soundProfile, abilityList, innateModifierList);
        this.repairNumber = repairNumber;
        this.burnTime = burnTime;
    }

    protected String getPrefix(){
        return BOOMERANG_TYPE_PREFIX_TAG;
    }
}