package com.bedmen.odyssey.entity.boss.mineralLeviathan;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class MineralLeviathanBody extends MineralLeviathanSegment {
    public MineralLeviathanBody(EntityType<? extends MineralLeviathanBody> entityType, Level level) {
        super(entityType, level);
        if(!this.level.isClientSide){
            ShellType shellType = ShellType.getRandomShellType(this.random);
            this.setShellType(shellType);
        } else {
            this.setShellType(ShellType.COAL);
        }
    }

    public void aiStep() {
        Optional<MineralLeviathanMaster> master = this.getMasterEntity();
        if(!this.isNoAi() && master.isPresent()){
            MineralLeviathanMaster mineralLeviathanMaster = master.get();
            MineralLeviathanSegment previousSegment = mineralLeviathanMaster.getAdjacentHeadwiseSegment(this);
            if(!this.level.isClientSide && previousSegment != null){
                //Movement
                Vec3 prevSegmentPosition = previousSegment.position();
                Vec3 difference = this.position().subtract(prevSegmentPosition);
                double prevSegmentWidth = previousSegment.getBoundingBox().getXsize();
                double thisWidth = this.getBoundingBox().getXsize();
                double distanceBetweenSegments = (prevSegmentWidth + thisWidth) * 0.47d;
                if(difference.length() > distanceBetweenSegments){
                    Vec3 newPosition = difference.normalize().scale(distanceBetweenSegments).add(prevSegmentPosition);
                    this.setPos(newPosition.x, newPosition.y, newPosition.z);
                }
                this.setRotation(difference.scale(-1.0d));
            }
        }
        super.aiStep();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 1.0D).add(Attributes.ATTACK_DAMAGE, MineralLeviathanMaster.DAMAGE * 0.5d);
    }
}
