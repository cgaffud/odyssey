package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.entity.projectile.DripstoneShard;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DripstoneCreeper extends OdysseyCreeper {

    public DripstoneCreeper(EntityType<? extends OdysseyCreeper> p_i50213_1_, Level p_i50213_2_) {
        super(p_i50213_1_, p_i50213_2_);
        this.maxSwell = 20;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return OdysseyCreeper.createAttributes().add(Attributes.MAX_HEALTH, 15);
    }

    @Override
    protected void explodeCreeper() {
        super.explodeCreeper();
        if (!this.level.isClientSide) {
            int dripstoneShardCount = this.getRandom().nextInt(2)+3;
            for (int i = 0; i < dripstoneShardCount; i++) {
                DripstoneShard dripstoneShard = new DripstoneShard(this.level, this);
                float phi = this.getRandom().nextFloat() * Mth.TWO_PI;
                float theta = this.getRandom().nextFloat() * Mth.PI;
                Vec3 shootingDir = new Vec3(Mth.sin(theta)*Mth.cos(phi), Mth.sin(theta)*Mth.sin(phi), Mth.cos(theta));
                dripstoneShard.moveTo(this.getPosition(1).add(0,1,0)
                        .add(shootingDir.scale(0.5)));
                dripstoneShard.shoot(shootingDir.x(), shootingDir.y(), shootingDir.z(), 1.1F, (float)(14 - this.level.getDifficulty().getId() * 4));
                this.level.addFreshEntity(dripstoneShard);
            }
        }
    }
}
