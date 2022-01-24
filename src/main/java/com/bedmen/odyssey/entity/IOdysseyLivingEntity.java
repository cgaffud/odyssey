package com.bedmen.odyssey.entity;

public interface IOdysseyLivingEntity {
    void incrementGlidingTicks();
    void resetGlidingTicks();
    int getGlidingTicks();
}
