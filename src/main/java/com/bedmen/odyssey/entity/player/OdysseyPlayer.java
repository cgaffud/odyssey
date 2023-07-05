package com.bedmen.odyssey.entity.player;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.encapsulator.PermabuffHolder;

import java.util.List;

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
