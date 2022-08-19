package com.bedmen.odyssey.entity.boss;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import java.util.Optional;

public interface SubEntity<T extends BossMaster> extends IEntityAdditionalSpawnData {
    Optional<T> getMasterEntity();

    void setMasterEntity(T master);

    boolean hurtDirectly(DamageSource damageSource, float amount);

    default Entity asEntity(){
        return (Entity)this;
    }

    default void writeSpawnData(FriendlyByteBuf buffer) {
        int masterId = this.getMasterEntity().isPresent() ? this.getMasterEntity().get().getId() : -1;
        buffer.writeVarInt(masterId);
    }

    default void readSpawnData(FriendlyByteBuf buffer) {
        int masterId = buffer.readVarInt();
        if(masterId > 0) {
            Entity entity = this.asEntity().level.getEntity(masterId);
            this.setMasterEntity((T) entity);
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
