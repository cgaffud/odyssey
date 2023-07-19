package com.bedmen.odyssey.entity.boss.permafrost;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.BossMaster;
import com.bedmen.odyssey.entity.boss.SubEntity;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanBody;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanMaster;
import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.util.NonNullListCollector;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class PermafrostMaster extends BossMaster {
    public static final double MAX_HEALTH = 350.0d;
    public static final double FOLLOW_RANGE = 75d;
    private static final EntityDataAccessor<Integer> DATA_MAIN_ID = SynchedEntityData.defineId(MineralLeviathanMaster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<IntList> DATA_ICICLE_ID_LIST = SynchedEntityData.defineId(MineralLeviathanMaster.class, OdysseyDataSerializers.INT_LIST);
    private static final int ICICLE_AMOUNT = 6;

    private static final String CONDUIT_TAG = "ConduitSubEntity";
    private static final String ICICLES_TAG = "IcicleEntities";
    private static final String ICICLE_ID_TAG = "IcicleId";
    public int totalPhase;

    // Reminder: serverside only
    public PermafrostConduit conduit;
    public List<PermafrostBigIcicleEntity> icicles  = new ArrayList<>();

    public PermafrostMaster(EntityType<? extends BossMaster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected BossEvent.BossBarColor getBossBarColor() {return BossEvent.BossBarColor.BLUE; }

    public int getMainId() {return this.entityData.get(DATA_MAIN_ID);}

    public void setMainId(int mainId) {this.entityData.set(DATA_MAIN_ID, mainId);}

    public Optional<PermafrostConduit> getConduit() {
        if(this.level.isClientSide) {
            int mainId = this.getMainId();
            Entity entity = this.level.getEntity(mainId);
            // instanceof also checks if it is null
            if(entity instanceof PermafrostConduit permafrostConduit) {
                return Optional.of(permafrostConduit);
            }
            return Optional.empty();
        } else if (this.conduit != null) {
            return Optional.of(this.conduit);
        }
        return Optional.empty();
    }

    public IntList getIcicleIds() {
        return new IntArrayList(this.entityData.get(DATA_ICICLE_ID_LIST));
    }

    public List<PermafrostBigIcicleEntity> getIcicles() {
        if(this.level.isClientSide) {
            List<PermafrostBigIcicleEntity> icicles = new ArrayList<>();
            for(Integer icicleId: this.getIcicleIds()) {
                Entity entity = this.level.getEntity(icicleId);
                // instanceof also checks if it is null
                if(entity instanceof PermafrostBigIcicleEntity mineralLeviathanBody) {
                    icicles.add(mineralLeviathanBody);
                }
            }
            return icicles;
        } else {
            return this.icicles;
        }
    }

    private void addIcicleId(int id) {
        IntList icicleIds = this.getIcicleIds();
        icicleIds.add(id);
        this.entityData.set(DATA_ICICLE_ID_LIST, icicleIds);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_MAIN_ID, -1);
        this.entityData.define(DATA_ICICLE_ID_LIST, new IntArrayList());
    }

    @Override
    public void performMasterMovement() {this.getConduit().ifPresent(conduit -> this.moveTo(conduit.position())); }

    @Override
    public Collection<Entity> getSubEntities() {
        List<Entity> entities = new ArrayList<>();
        this.getConduit().ifPresent(entities::add);
        entities.addAll(this.getIcicles());
        return entities;
    }

    @Override
    public void spawnSubEntities() {
        if (this.getConduit().isEmpty()) {
            PermafrostConduit permafrostConduit = EntityTypeRegistry.PERMAFROST_CONDUIT.get().create(this.level);
            if (permafrostConduit != null) {
                this.conduit = permafrostConduit;
                this.setMainId(permafrostConduit.getId());
                permafrostConduit.moveTo(this.position());
                permafrostConduit.setMasterId(this.getId());
            } else {
                Odyssey.LOGGER.error("PermafrostConduit failed to spawn in spawnSubEntities");
            }
        }
        if (this.getIcicles().isEmpty()) {
            for (int icicleId = 0; icicleId < ICICLE_AMOUNT; icicleId++) {
                PermafrostBigIcicleEntity permafrostBigIcicleEntity = new PermafrostBigIcicleEntity(this.level, icicleId);
                if (permafrostBigIcicleEntity != null) {
                    this.icicles.add(permafrostBigIcicleEntity);
                    this.addIcicleId(permafrostBigIcicleEntity.getId());
                    permafrostBigIcicleEntity.moveTo(this.position());
                    permafrostBigIcicleEntity.setMasterId(this.getId());
                } else {
                    Odyssey.LOGGER.error("Mineral Leviathan failed to spawn in spawnInitialBodyParts");
                    break;
                }
            }
        }
        for(Entity entity: getSubEntities()) {
            this.level.addFreshEntity(entity);
        }
    }

    @Override
    public void handleSubEntity(SubEntity<?> subEntity) {
        Entity entity = subEntity.asEntity();
        AABB aabb = entity.getBoundingBox();
        double x = aabb.getXsize() + 0.5d;
        double z = aabb.getZsize() + 0.5d;
        for(double x1 = -x; x1 <= x; x1 += x) {
            for(double z1 = -z; z1 <= z; z1 += z) {
                Vec3 position = this.position().add(x1, 0d, z1);
                entity.moveTo(position);
                if(!entity.touchingUnloadedChunk()) {
                    return;
                }
            }
        }
    }

    @Override
    public CompoundTag saveSubEntities() {
        CompoundTag subEntitiesTag = new CompoundTag();
        CompoundTag conduitCompoundTag = new CompoundTag();
        this.conduit.saveAsPassenger(conduitCompoundTag);
        subEntitiesTag.put(CONDUIT_TAG, conduitCompoundTag);
        ListTag iciclesTag = new ListTag();
        for(PermafrostBigIcicleEntity bigIcicleEntity : this.icicles) {
            CompoundTag icicleTag = new CompoundTag();
            if (bigIcicleEntity.saveAsPassenger(icicleTag)) {
                iciclesTag.add(icicleTag);
            }
        }
        subEntitiesTag.put(ICICLES_TAG, iciclesTag);
        return subEntitiesTag;
    }

    @Override
    public void loadSubEntities(CompoundTag subEntitiesTag) {
        if(subEntitiesTag.contains(CONDUIT_TAG)) {
            CompoundTag conduitCompoundTag = subEntitiesTag.getCompound(CONDUIT_TAG);
            Entity entity = EntityType.loadEntityRecursive(conduitCompoundTag, this.level, entity1 -> entity1);
            if(entity instanceof PermafrostConduit permafrostConduit) {
                permafrostConduit.setMasterId(this.getId());
                this.conduit = permafrostConduit;
                this.setMainId(permafrostConduit.getId());
            } else {
                Odyssey.LOGGER.error("PermafrostConduit failed to spawn head in loadSubEntities");
            }
        }
        if(subEntitiesTag.contains(ICICLES_TAG)) {
            List<Tag> listOfTags = subEntitiesTag.getList(ICICLES_TAG, Tag.TAG_COMPOUND).stream().toList();
            Stream<Entity> entitySteam = EntityType.loadEntitiesRecursive(listOfTags, this.level);
            this.icicles.addAll(entitySteam.map(entity -> {
                if(entity instanceof PermafrostBigIcicleEntity permafrostBigIcicleEntity) {
                    permafrostBigIcicleEntity.setMasterId(this.getId());
                    return permafrostBigIcicleEntity;
                }
                return null;
            }).filter(permafrostBigIcicleEntity -> {
                boolean isNull = permafrostBigIcicleEntity == null;
                if(isNull) {
                    Odyssey.LOGGER.error("PermafrostIcicle failed to spawn in loadSubEntities");
                }
                return !isNull;
            }).collect(new NonNullListCollector<>()));

            for(PermafrostBigIcicleEntity permafrostBigIcicleEntity: this.icicles) {
                this.addIcicleId(permafrostBigIcicleEntity.getId());
            }
        }

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, MAX_HEALTH).add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE);
    }
}
