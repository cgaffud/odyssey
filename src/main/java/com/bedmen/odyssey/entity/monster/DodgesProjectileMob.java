package com.bedmen.odyssey.entity.monster;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;

public interface DodgesProjectileMob {
    boolean tryDoDodge(Projectile projectile, EntityHitResult hitResult);
}
