package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.combat.ArrowType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class DripstoneShard extends OdysseyArrow {

    public DripstoneShard(EntityType<? extends OdysseyArrow> entityType, Level level) {
        super(entityType, level);
    }

    public DripstoneShard(Level level, LivingEntity shooter, ArrowType arrowType) {
        super(level, shooter, arrowType, false);
    }

}
