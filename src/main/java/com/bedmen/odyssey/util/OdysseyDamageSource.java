package com.bedmen.odyssey.util;

import com.bedmen.odyssey.entity.projectile.BoomerangEntity;
import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrowEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;

import javax.annotation.Nullable;

public class OdysseyDamageSource {
    public static DamageSource boomerang(BoomerangEntity boomerangEntity, @Nullable Entity entity) {
        return (new IndirectEntityDamageSource("boomerang", boomerangEntity, entity)).setProjectile();
    }
    public static DamageSource odysseyArrow(OdysseyAbstractArrowEntity odysseyAbstractArrowEntity, @Nullable Entity entity) {
        return (new IndirectEntityDamageSource("odyssey_arrow", odysseyAbstractArrowEntity, entity)).setProjectile();
    }
}
