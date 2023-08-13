package com.bedmen.odyssey.entity.boss.permafrost;

import com.bedmen.odyssey.entity.boss.BossSubEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

abstract public class AbstractMainPermafrostEntity extends BossSubEntity<PermafrostMaster> {

    protected final int MAX_ICICLE_POSITIONS = 20;
    protected int iciclePosition = 0;

    protected AbstractMainPermafrostEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    protected void performSpiralAttack(LivingEntity livingEntity) {
        double targetX = livingEntity.getX();
        double targetY = livingEntity.getY() + (double)livingEntity.getEyeHeight() * 0.5D;
        double targetZ = livingEntity.getZ();
        double angle = this.iciclePosition * Math.PI / this.MAX_ICICLE_POSITIONS * 2.0d;
        double r = 2.0d;
        double d0 = this.getX() + Math.cos(angle) * r;
        double d1 = this.getY();
        double d2 = this.getZ() + Math.sin(angle) * r;
        double d3 = targetX - d0;
        double d4 = targetY - d1;
        double d5 = targetZ - d2;
        PermafrostIcicleEntity permafrostIcicleEntity = new PermafrostIcicleEntity(this.level, this, d3, d4, d5,6.0f);
        permafrostIcicleEntity.setOwner(this);
        permafrostIcicleEntity.setPosRaw(d0, d1, d2);
        this.level.addFreshEntity(permafrostIcicleEntity);
    }

    protected void performSphereAttack(int i){
        i = 4*(i+22);
        int maxJ;
        double angleI = i * Math.PI / 180.0d;
        double angleJ0 = this.random.nextFloat() * 2 * Math.PI;
        if(i == 180 || i == 0)
            maxJ = 1;
        else{
            maxJ = (int)(Math.sin(angleI) * 30.0d);
            maxJ = Math.max(maxJ, 2);
        }
        for(int j = 0; j < maxJ; j++){
            double r = 2.0d;
            double angleJ = j * Math.PI * 2.0d / (double)maxJ + angleJ0;
            double d0 = r * Math.sin(angleI) * Math.cos(angleJ);
            double d1 = r * Math.cos(angleI);
            double d2 = r * Math.sin(angleI) * Math.sin(angleJ);
            PermafrostIcicleEntity permafrostIcicleEntity = new PermafrostIcicleEntity(this.level, this, d0, d1, d2, 12.0f);
            permafrostIcicleEntity.setOwner(this);

            permafrostIcicleEntity.setPosRaw(this.getX() + d0, this.getY() + d1, this.getZ() + d2);
            this.level.addFreshEntity(permafrostIcicleEntity);
        }
    }

    @Nullable
    protected List<ServerPlayer> getValidTargets() {
        if (this.getMaster().isPresent()) {
            PermafrostMaster permafrostMaster = this.getMaster().get();
            Collection<ServerPlayer> serverPlayers = permafrostMaster.bossEvent.getPlayers();
            return serverPlayers.stream().filter(permafrostMaster::validTargetPredicate).collect(Collectors.toList());
        }
        return null;
    }

}
