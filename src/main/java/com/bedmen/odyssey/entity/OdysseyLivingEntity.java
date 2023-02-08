package com.bedmen.odyssey.entity;

import com.bedmen.odyssey.combat.SmackPush;

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
}
