package com.bedmen.odyssey.entity;

public interface IOdysseyLivingEntity {
    void setFlightLevels(boolean hasSlowFalling, int glidingLevel);
    boolean hasSlowFalling();
    int getGlidingLevel();
    void incrementFlightTicks(int i);
    void decrementFlightTicks();
    int getFlightTicks();
    int getMaxFlightTicks();
    boolean getShouldCancelNextKnockback();
    void setShouldCancelNextKnockback(boolean value);
}
