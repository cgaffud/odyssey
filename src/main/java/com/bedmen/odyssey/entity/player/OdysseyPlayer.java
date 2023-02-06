package com.bedmen.odyssey.entity.player;

import com.bedmen.odyssey.entity.player.permabuff.Permabuff;
import com.bedmen.odyssey.entity.player.permabuff.PermabuffMap;

public interface OdysseyPlayer {
    float getAttackStrengthScaleO();
    void updateSniperScoping();
    boolean isSniperScoping();
    int getPermabuffValue(Permabuff permabuff);
    PermabuffMap getPermabuffMap();
    void setPermabuffMap(PermabuffMap permabuffMap);
    void addPermabuffMap(PermabuffMap permabuffMap);
//    int getLifeFruits();
//    void setLifeFruits(int i);
//    void incrementLifeFruits();
//    ItemStack getTrinketSlot();
}
