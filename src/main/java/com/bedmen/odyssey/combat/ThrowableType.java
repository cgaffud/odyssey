package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.aspect.AspectHolder;
import com.bedmen.odyssey.aspect.AspectInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ThrowableType {

    private static final Map<String, ThrowableType> THROWABLE_TYPE_MAP = new HashMap<>();
    public final AspectHolder aspectHolder;
    public final float velocity;
    public final double thrownDamage;
    public final SoundProfile soundProfile;
    protected final String id;

    public ThrowableType(String id, double thrownDamage, float velocity, SoundProfile soundProfile, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList){
        this.id = id;
        this.thrownDamage = thrownDamage;
        this.velocity = velocity;
        this.soundProfile = soundProfile;
        this.aspectHolder = new AspectHolder(abilityList, innateModifierList);
        THROWABLE_TYPE_MAP.put(this.getName(), this);
    }

    public String getName(){
        return this.getPrefix()+":"+this.id;
    }

    protected abstract String getPrefix();

    public static ThrowableType fromName(String name){
        return THROWABLE_TYPE_MAP.get(name);
    }

    public enum SoundProfile {
        WOODEN(SoundEvents.PLAYER_ATTACK_SWEEP, SoundEvents.ARROW_HIT, SoundEvents.ARROW_HIT, SoundEvents.ARROW_HIT),
        METAL(SoundEvents.TRIDENT_THROW, SoundEvents.TRIDENT_HIT, SoundEvents.TRIDENT_HIT_GROUND, SoundEvents.TRIDENT_RETURN);

        public final SoundEvent throwSound;
        public final SoundEvent entityHitSound;
        public final SoundEvent groundHitSound;
        public final SoundEvent returnSound;

        SoundProfile(SoundEvent throwSound, SoundEvent entityHitSound, SoundEvent groundHitSound, SoundEvent returnSound){
            this.throwSound = throwSound;
            this.entityHitSound = entityHitSound;
            this.groundHitSound = groundHitSound;
            this.returnSound = returnSound;
        }
    }
}
