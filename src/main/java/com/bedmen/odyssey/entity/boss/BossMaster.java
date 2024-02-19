package com.bedmen.odyssey.entity.boss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import net.minecraft.world.entity.Entity.RemovalReason;

public abstract class BossMaster extends Boss {
    public static final String SUB_ENTITIES_TAG = "SubEntities";
    public static final String HAS_SPAWNED_SUB_ENTITIES_TAG = "SpawnedSubEntities";
    private boolean hasSpawnedSubentities;

    protected BossMaster(EntityType<? extends BossMaster> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.hasSpawnedSubentities = false;
        this.setNoGravity(true);
    }

    public void onAddedToWorld() {
        if(!this.level.isClientSide && !this.shouldDelaySpawn()) {
            this.spawnSubEntities();
            System.out.println(this.position());
            this.hasSpawnedSubentities = true;
        }
    }

    public void tick() {
        this.setNoGravity(true);
        this.noPhysics = true;
        super.tick();
    }

    public void serverTick() {
        super.serverTick();
        if (this.shouldDelaySpawn() && !this.hasSpawnedSubentities && (this.tickCount <= this.spawnDelayTickLength())) {
            System.out.println(this.position());
        }
        if (this.shouldDelaySpawn() && !this.hasSpawnedSubentities && (this.tickCount > this.spawnDelayTickLength())) {
            this.hasSpawnedSubentities = true;
            this.spawnSubEntities();
        }
        this.performMasterMovement();
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean(HAS_SPAWNED_SUB_ENTITIES_TAG, this.hasSpawnedSubentities);
        compoundTag.put(SUB_ENTITIES_TAG, this.saveSubEntities());
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.hasSpawnedSubentities = compoundTag.getBoolean(HAS_SPAWNED_SUB_ENTITIES_TAG);
        // subEntitiesTag could be empty (but not null) if there was no CompoundTag found for SUB_ENTITIES_TAG
        CompoundTag subEntitiesTag = compoundTag.getCompound(SUB_ENTITIES_TAG);
        this.loadSubEntities(subEntitiesTag);
    }

    public void remove(@NotNull RemovalReason removalReason) {
        this.getSubEntities().forEach(Entity::discard);
        super.remove(removalReason);
    }

    public void die(@NotNull DamageSource damageSource) {
        this.getSubEntities().forEach(entity -> {
            if(entity instanceof SubEntity subEntity) {
                subEntity.hurtDirectly(damageSource, Float.MAX_VALUE);
            }
        });
        super.die(damageSource);
    }

    public abstract void performMasterMovement();

    public abstract Collection<Entity> getSubEntities();

    public abstract void spawnSubEntities();

    public abstract void handleSubEntity(SubEntity<?> subEntity);

    public abstract CompoundTag saveSubEntities();

    public abstract void loadSubEntities(CompoundTag compoundTag);

    // Delay spawning injection:
    protected boolean shouldDelaySpawn() {
        return false;
    }

    protected int spawnDelayTickLength() {
        return 0;
    }
}
