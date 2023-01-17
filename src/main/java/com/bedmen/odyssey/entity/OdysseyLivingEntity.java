package com.bedmen.odyssey.entity;

import com.bedmen.odyssey.combat.SmackPush;

public interface OdysseyLivingEntity {
    void setFlightLevels(boolean hasSlowFalling, int glidingLevel);
    void incrementFlight();
    void decrementFlight();
    int getFlightValue();
    int getMaxFlight();
    SmackPush getSmackPush();
    void setSmackPush(SmackPush smackPush);
    float getNextKnockbackAspect();
    void setNextKnockbackAspect(float nextKnockbackAspect);
}
