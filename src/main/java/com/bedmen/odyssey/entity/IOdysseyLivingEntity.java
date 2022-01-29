package com.bedmen.odyssey.entity;

public interface IOdysseyLivingEntity {
    void setGlidingLevel(int i);
    int getGlidingLevel();
    void incrementGlidingTicks();
    void decrementGlidingTicks();
    int getGlidingTicks();
    int getMaxGlidingTicks();
}
