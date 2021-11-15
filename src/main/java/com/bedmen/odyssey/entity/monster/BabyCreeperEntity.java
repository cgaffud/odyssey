package com.bedmen.odyssey.entity.monster;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class BabyCreeperEntity extends OdysseyCreeperEntity {
    public BabyCreeperEntity(EntityType<? extends OdysseyCreeperEntity> p_i50213_1_, World p_i50213_2_) {
        super(p_i50213_1_, p_i50213_2_);
        this.setBaby(true);
    }
}
