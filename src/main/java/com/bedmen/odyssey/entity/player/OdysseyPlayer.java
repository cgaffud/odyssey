package com.bedmen.odyssey.entity.player;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.encapsulator.PermabuffMap;

import java.util.List;

public interface OdysseyPlayer {
    float getAttackStrengthScaleO();
    void updateSniperScoping();
    boolean isSniperScoping();
    PermabuffMap getPermabuffMap();
    void setPermabuffMap(PermabuffMap permabuffMap);
    void setPermabuff(AspectInstance aspectInstance);
    void addPermabuffs(List<AspectInstance> permabuffList);
}
