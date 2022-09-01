package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.entity.ai.ReturnToPosGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class DungeonZombie extends Zombie implements DungeonMonster {
    protected BlockPos dungeonPos = null;

    public DungeonZombie(EntityType<? extends DungeonZombie> entityType, Level level, BlockPos dungeonPos) {
        super(entityType, level);
        this.dungeonPos = dungeonPos;
    }

    public DungeonZombie(EntityType<? extends DungeonZombie> entityType, Level level) {
        super(entityType, level);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.getAvailableGoals().stream()
                .map(WrappedGoal::getGoal)
                .filter(goal -> goal instanceof HurtByTargetGoal)
                .findFirst()
                .ifPresent(this.targetSelector::removeGoal);
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, DungeonMonster.class)));
        refreshReturnToDungeonGoal();
    }

    protected void refreshReturnToDungeonGoal() {
        this.targetSelector.getAvailableGoals().stream()
                .map(WrappedGoal::getGoal)
                .filter(goal -> goal instanceof ReturnToPosGoal)
                .findFirst()
                .ifPresent(this.targetSelector::removeGoal);
        if (this.dungeonPos != null) {
            this.goalSelector.addGoal(5, new ReturnToPosGoal(this, this.dungeonPos, 1.0d, 8));
        }
    }



    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.dungeonPos != null) {
            compoundTag.putInt("DungeonPosX", this.dungeonPos.getX());
            compoundTag.putInt("DungeonPosY", this.dungeonPos.getY());
            compoundTag.putInt("DungeonPosZ", this.dungeonPos.getZ());
        }
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("DungeonPosX")) {
            int dungeonPosX = compoundTag.getInt("DungeonPosX");
            int dungeonPosY = compoundTag.getInt("DungeonPosY");
            int dungeonPosZ = compoundTag.getInt("DungeonPosZ");
            this.dungeonPos = new BlockPos(dungeonPosX, dungeonPosY, dungeonPosZ);
            refreshReturnToDungeonGoal();
        }
    }
}
