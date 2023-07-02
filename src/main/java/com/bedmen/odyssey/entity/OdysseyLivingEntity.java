package com.bedmen.odyssey.entity;

import com.bedmen.odyssey.combat.SmackPush;
import com.bedmen.odyssey.effect.FireType;
import net.minecraft.util.Mth;

import java.util.Optional;

public interface OdysseyLivingEntity {
    void setFlightLevels(boolean hasSlowFalling, int glidingLevel);
    void incrementFlight();
    void decrementFlight();
    int getFlightValue();
    int getMaxFlight();
    SmackPush getSmackPush();
    void setSmackPush(SmackPush smackPush);
    float popKnockbackAspectQueue();
    void pushKnockbackAspectQueue(float nextKnockbackAspect);
    void setTrueHurtTime(Optional<Integer> trueHurtTime);
    Optional<Integer> getTrueHurtTime();
    FireType getFireType();
    void setFireType(FireType fireType);
    float getTemperature();
    void setTemperature(float temperature);
    float getShieldMeter();
    float getShieldMeterO();
    void setShieldMeter(float shieldMeter);

    void updateShieldMeterO();
    void adjustShieldMeter(float amount);

    default boolean isHot(){
        return this.getTemperature() > 0f;
    }

    default boolean isOverheating(){
        return this.getTemperature() >= 1f;
    }

    default boolean isCold(){
        return this.getTemperature() < 0f;
    }

    default float getPercentOverheated(){
        return Mth.clamp(this.getTemperature(), 0f, 1f);
    }
}
