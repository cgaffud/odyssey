package com.bedmen.odyssey.entity.boss;

import net.minecraft.world.entity.Entity;

public interface PuppetEntity {
    default boolean needsRemovalOrHandling(){
        Entity entity = this.asEntity();
        return !entity.isAlive() || entity.isRemoved() || entity.touchingUnloadedChunk();
    }

    boolean shouldBeRemoved();

    default Entity asEntity(){
        return (Entity)this;
    }

    // Copy and paste this into anything that implements PuppetEntity
//    public boolean save(CompoundTag compoundTag) {
//        return false;
//    }
}
