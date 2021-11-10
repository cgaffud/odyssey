package com.bedmen.odyssey.entity.monster;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;

public class BabyCreeperEntity extends AbstractCreeperEntity {
    public BabyCreeperEntity(EntityType<? extends AbstractCreeperEntity> p_i50213_1_, World p_i50213_2_) {
        super(p_i50213_1_, p_i50213_2_, 20, 2);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.375D);
    }

    public boolean isBaby() {
        return true;
    }

    public double getMyRidingOffset() {
        return this.isBaby() ? 0.0D : -0.45D;
    }

}
