package com.bedmen.odyssey.entity.player;

public interface OdysseyPlayer {
    float getAttackStrengthScaleO();
    void updateSniperScoping();
    boolean isSniperScoping();
    float getPartialExperiencePoint();
    void setPartialExperiencePoint(float partialExperiencePoint);
    float getBlizzardFogScale(float partialTicks);
    void updateBlizzardFogScaleO();
    void incrementBlizzardFogScale();
    void decrementBlizzardFogScale();
}
