package com.bedmen.odyssey.entity;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public abstract class MonsterWaterEntity extends MonsterEntity {
    protected MonsterWaterEntity(EntityType<? extends MonsterWaterEntity> type, World p_i48565_2_) {
        super(type, p_i48565_2_);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.WATER;
    }

    public boolean checkSpawnObstruction(IWorldReader worldIn) {
        return worldIn.isUnobstructed(this);
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getAmbientSoundInterval() {
        return 120;
    }

    protected void updateAir(int p_209207_1_) {
        if (this.isAlive() && !this.isInWaterOrBubble()) {
            this.setAirSupply(p_209207_1_ - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(DamageSource.DROWN, 2.0F);
            }
        } else {
            this.setAirSupply(300);
        }

    }

    /**
     * Gets called every tick from main Entity class
     */
    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();
        this.updateAir(i);
    }

    public boolean isPushedByFluid() {
        return false;
    }

    public boolean canBeLeashed(PlayerEntity player) {
        return false;
    }
}