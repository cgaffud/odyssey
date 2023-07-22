package com.bedmen.odyssey.entity.boss.permafrost;

import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


public class PermafrostSpawnerIcicle extends AbstractIndexedIcicleEntity{

    public PermafrostSpawnerIcicle(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public PermafrostSpawnerIcicle(Level level, float health, int index) {
        super(EntityTypeRegistry.PERMAFROST_SPAWNER_ICICLE.get(), level, index);
        System.out.println(health);
        this.setHealth(health);

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, PermafrostMaster.MAX_HEALTH);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        return this.getMaster().map(permafrostMaster ->
                permafrostMaster.hurtSpawner(damageSource, amount, this)
        ).orElseGet(() ->
                super.hurt(damageSource, amount)
        );
    }

    public void tick() {
        super.tick();
        if ((this.getMaster().isPresent())) {
            PermafrostMaster permafrostMaster = this.getMaster().get();
            double thetaA = (double) this.getIcicleIndex() * Math.PI * 2.0D / (double) PermafrostMaster.SPAWNER_AMOUNT;
            if (!this.level.isClientSide()) {
                Vec3 ownerPos = permafrostMaster.getPosition(1.0f);
                float r2 = PermafrostMaster.ICICLE_FOLLOW_RADIUS;
                float f2x = (float) (Math.cos(thetaA) * Math.sin(Mth.HALF_PI) * r2);
                float f2y = (float) (Math.cos(Mth.HALF_PI) * r2) + 0.25f;
                float f2z = (float) (Math.sin(thetaA) * Math.sin(Mth.HALF_PI) * r2);
                this.moveTo(new Vec3(f2x, f2y, f2z).add(ownerPos));
            }
            float thetaB = (float) (thetaA + Mth.HALF_PI);
            this.setYRot(thetaB);
        }

        this.hasImpulse = true;
        this.checkInsideBlocks();
    }
}
