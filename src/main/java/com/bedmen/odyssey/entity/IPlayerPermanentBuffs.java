package com.bedmen.odyssey.entity;

public interface IPlayerPermanentBuffs {
    boolean getNetherImmune();
    void setNetherImmune(boolean b);
    int getLifeFruits();
    void setLifeFruits(int i);
    void incrementLifeFruits();
}
