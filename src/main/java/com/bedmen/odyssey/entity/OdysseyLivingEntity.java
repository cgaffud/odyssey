package com.bedmen.odyssey.entity;

import com.bedmen.odyssey.weapon.SmackPush;

public interface OdysseyLivingEntity {
    void setFlightLevels(boolean hasSlowFalling, int glidingLevel);
    boolean hasSlowFalling();
    int getGlidingLevel();
    void incrementFlightTicks(int i);
    void decrementFlightTicks();
    int getFlightTicks();
    int getMaxFlightTicks();
    SmackPush getSmackPush();
    void setSmackPush(SmackPush smackPush);
    float getNextKnockbackModifier();
    void setNextKnockbackModifier(float nextKnockbackModifier);
}
