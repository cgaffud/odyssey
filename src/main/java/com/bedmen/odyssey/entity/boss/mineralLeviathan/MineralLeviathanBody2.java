package com.bedmen.odyssey.entity.boss.mineralLeviathan;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class MineralLeviathanBody2 extends MineralLeviathanSegment2 {
    public MineralLeviathanBody2(EntityType<? extends MineralLeviathanBody2> entityType, Level level) {
        super(entityType, level);
        if(!this.level.isClientSide){
            ShellType shellType = ShellType.getRandomShellType(this.random);
            this.setShellType(shellType);
        } else {
            this.setShellType(ShellType.COAL);
        }
    }

    public void aiStep() {
        Optional<MineralLeviathanMaster2> master = this.getMasterEntity();
        if(!this.isNoAi() && master.isPresent()){
            MineralLeviathanMaster2 mineralLeviathanMaster2 = master.get();
            MineralLeviathanSegment2 previousSegment = mineralLeviathanMaster2.getAdjacentHeadwiseSegment(this);
            if(!this.level.isClientSide && previousSegment != null){
                //Movement
                Vec3 prevSegmentPosition = previousSegment.position();
                Vec3 movement = prevSegmentPosition.subtract(this.position());
                if(movement.length() > 2.0d){
                    Vec3 newPosition = movement.normalize().scale(-1.95d).add(prevSegmentPosition);
                    this.setPos(newPosition.x, newPosition.y, newPosition.z);
                }
                this.setRotation(movement);
            }
        }
        super.aiStep();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 1.0D).add(Attributes.ATTACK_DAMAGE, MineralLeviathanMaster2.DAMAGE * 0.5d);
    }
}
