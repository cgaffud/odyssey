package com.bedmen.odyssey.entity.projectile;

import net.minecraft.world.entity.Entity;

public interface SwungProjectile {
    void launch(Entity entity, float xRot, float yRot, float zRot, float inaccuracy);
}
