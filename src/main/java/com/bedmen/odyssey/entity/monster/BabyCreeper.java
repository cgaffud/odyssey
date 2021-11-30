package com.bedmen.odyssey.entity.monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BabyCreeper extends OdysseyCreeper {
    public BabyCreeper(EntityType<? extends OdysseyCreeper> p_i50213_1_, Level p_i50213_2_) {
        super(p_i50213_1_, p_i50213_2_);
        this.setBaby(true);
    }
}
