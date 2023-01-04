package com.bedmen.odyssey.entity.boss;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import java.util.Optional;

/**
 * A interface for Entities that are puppets for a BossMaster. Make sure to
 * override checkDespawn() with the proper removal condition to prevent SubEntities
 * despawning before master does.
 */
public interface SubEntity<T extends BossMaster> extends IEntityAdditionalSpawnData {

    Optional<T> getMaster();

    void setMasterId(int masterId);

    boolean hurtDirectly(DamageSource damageSource, float amount);

    default Entity asEntity(){
        return (Entity)this;
    }

    default void writeSpawnData(FriendlyByteBuf buffer) {
        int masterId = this.getMaster().isPresent() ? this.getMaster().get().getId() : -1;
        buffer.writeVarInt(masterId);
    }

    default void readSpawnData(FriendlyByteBuf buffer) {
        int masterId = buffer.readVarInt();
        if(masterId > -1) {
            Entity entity = this.asEntity().level.getEntity(masterId);
            if(entity != null) {
                this.setMasterId(entity.getId());
            }
        }
    }

    // Copy and paste
    /*
    public void remove(RemovalReason removalReason) {
        if(removalReason == RemovalReason.DISCARDED || removalReason == RemovalReason.KILLED || this.getMasterEntity().isEmpty()) {
            super.remove(removalReason);
        } else {
            this.getMasterEntity().ifPresent(master -> master.handleSubEntity(this));
        }
    }

    public boolean save(CompoundTag compoundTag) {
        return false;
    }

    public boolean hurtDirectly(DamageSource damageSource, float amount) {
        return super.hurt(damageSource, amount);
    }

    public void kill() {
        this.hurtDirectly(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
    }
    */
}
