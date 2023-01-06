package com.bedmen.odyssey.entity.boss.mineralLeviathan;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.SubEntity;
import com.bedmen.odyssey.entity.boss.BossMaster;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.stream.Stream;

public class MineralLeviathanMaster extends BossMaster {
    private static final EntityDataAccessor<Integer> DATA_HEAD_ID = SynchedEntityData.defineId(MineralLeviathanMaster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<IntList> DATA_BODY_ID_LIST = SynchedEntityData.defineId(MineralLeviathanMaster.class, OdysseyDataSerializers.INT_LIST);
    public static final int NUM_SEGMENTS = 20;
    public static final double MAX_HEALTH = 150.0d;
    public static final double DAMAGE = 8.0d;
    public static final double FOLLOW_RANGE = 75d;
    public static final double DODGE_RANGE = 3.5d;
    private static final String HEAD_TAG = "HeadSubEntity";
    private static final String BODY_TAG = "BodySubEntities";

    // These sub entities may only be referenced directly in a server side method
    // Otherwise use the 'get' method
    public MineralLeviathanHead head;
    public final List<MineralLeviathanBody> bodyParts = new ArrayList<>();

    public MineralLeviathanMaster(EntityType<? extends MineralLeviathanMaster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 100;
    }

    public int getHeadId() {
        return this.entityData.get(DATA_HEAD_ID);
    }

    public void setHeadId(int headId) {
        this.entityData.set(DATA_HEAD_ID, headId);
    }

    public Optional<MineralLeviathanHead> getHead() {
        if(this.level.isClientSide) {
            int headId = this.entityData.get(DATA_HEAD_ID);
            Entity entity = this.level.getEntity(headId);
            // instanceof also checks if it is null
            if(entity instanceof MineralLeviathanHead mineralLeviathanHead) {
                return Optional.of(mineralLeviathanHead);
            }
            return Optional.empty();
        } else if (this.head != null) {
            return Optional.of(this.head);
        }
        return Optional.empty();
    }

    public IntList getBodyIds() {
        return this.entityData.get(DATA_BODY_ID_LIST);
    }

    public List<MineralLeviathanBody> getBodyParts() {
        if(this.level.isClientSide) {
            List<MineralLeviathanBody> bodyParts = new ArrayList<>();
            for(Integer bodyId: this.entityData.get(DATA_BODY_ID_LIST)) {
                Entity entity = this.level.getEntity(bodyId);
                // instanceof also checks if it is null
                if(entity instanceof MineralLeviathanBody mineralLeviathanBody) {
                    bodyParts.add(mineralLeviathanBody);
                }
            }
            return bodyParts;
        } else {
            return this.bodyParts;
        }
    }

    private void addBodyId(int id) {
        IntList bodyIds = this.entityData.get(DATA_BODY_ID_LIST);
        bodyIds.add(id);
        this.entityData.set(DATA_BODY_ID_LIST, bodyIds);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_HEAD_ID, -1);
        this.entityData.define(DATA_BODY_ID_LIST, new IntArrayList());
    }

    public void clientTick() {
        for(Entity entity : this.getSubEntities()){
            if(entity instanceof LivingEntity livingEntity) {
                livingEntity.hurtTime = this.hurtTime;
            }
        }
    }

    public Collection<Entity> getSubEntities() {
        List<Entity> entities = new ArrayList<>();
        this.getHead().ifPresent(entities::add);
        entities.addAll(this.getBodyParts());
        return entities;
    }

    public void performMasterMovement() {
        this.getHead().ifPresent(head -> this.moveTo(head.position()));
    }

    protected BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.RED;
    }

    public void spawnSubEntities() {
        if(this.getHead().isEmpty()) {
            intializeHead();
        }
        if(this.getBodyParts().isEmpty()) {
            initializeBodyParts();
        }
        for(Entity entity: getSubEntities()) {
            this.level.addFreshEntity(entity);
        }
    }

    // Server Side
    public void intializeHead() {
        MineralLeviathanHead mineralLeviathanHead = EntityTypeRegistry.MINERAL_LEVIATHAN_HEAD.get().create(this.level);
        if(mineralLeviathanHead != null) {
            this.head = mineralLeviathanHead;
            this.setHeadId(mineralLeviathanHead.getId());
            mineralLeviathanHead.moveTo(this.position());
            this.finishInitializingSegment(mineralLeviathanHead);
        } else {
            Odyssey.LOGGER.error("Mineral Leviathan failed to spawn head in spawnInitialHead");
        }
    }

    // Server Side
    public void initializeBodyParts() {
        while(this.getBodyIds().size() < NUM_SEGMENTS - 1) {
            MineralLeviathanBody mineralLeviathanBody = EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get().create(this.level);
            if(mineralLeviathanBody != null) {
                this.bodyParts.add(mineralLeviathanBody);
                this.addBodyId(mineralLeviathanBody.getId());
                mineralLeviathanBody.moveTo(this.position());
                finishInitializingSegment(mineralLeviathanBody);
            } else {
                Odyssey.LOGGER.error("Mineral Leviathan failed to spawn body part in spawnInitialBodyParts");
                break;
            }
        }
    }

    public void finishInitializingSegment(MineralLeviathanSegment segment2) {
        segment2.setXRot(90.0f);
        segment2.setMasterId(this.getId());
    }

    public void handleSubEntity(SubEntity<?> subEntity) {
        Entity entity = subEntity.asEntity();
        if(entity.getId() == this.getHeadId()) {
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
        } else {
            MineralLeviathanSegment segment = (MineralLeviathanSegment) entity;
            do {
                segment = this.getAdjacentHeadwiseSegment((MineralLeviathanBody) segment);
            } while (segment.touchingUnloadedChunk() && segment.getId() != this.getHeadId());
            entity.moveTo(segment.position());
        }
    }

    // Server Side
    public void saveSubEntities(CompoundTag compoundTag) {
        CompoundTag headCompoundTag = new CompoundTag();
        this.head.saveAsPassenger(headCompoundTag);
        compoundTag.put(HEAD_TAG, headCompoundTag);
        ListTag bodyPartsTag = new ListTag();
        for(MineralLeviathanBody mineralLeviathanBody : this.bodyParts) {
            CompoundTag bodyCompoundTag = new CompoundTag();
            if (mineralLeviathanBody.saveAsPassenger(bodyCompoundTag)) {
                bodyPartsTag.add(bodyCompoundTag);
            }
        }
        compoundTag.put(BODY_TAG, bodyPartsTag);
    }

    // Server Side
    public void loadSubEntities(CompoundTag compoundTag) {
        if(compoundTag.contains(HEAD_TAG)) {
            CompoundTag headCompoundTag = compoundTag.getCompound(HEAD_TAG);
            Entity entity = EntityType.loadEntityRecursive(headCompoundTag, this.level, entity1 -> entity1);
            if(entity instanceof MineralLeviathanHead mineralLeviathanHead) {
                mineralLeviathanHead.setMasterId(this.getId());
                this.head = mineralLeviathanHead;
                this.setHeadId(mineralLeviathanHead.getId());
            } else {
                Odyssey.LOGGER.error("Mineral Leviathan failed to spawn head in loadSubEntities");
            }
        }
        if(compoundTag.contains(BODY_TAG)) {
            List<Tag> listOfTags = compoundTag.getList(BODY_TAG, Tag.TAG_COMPOUND).stream().toList();
            Stream<Entity> entitySteam = EntityType.loadEntitiesRecursive(listOfTags, this.level);
            this.bodyParts.addAll(entitySteam.map(entity -> {
                if(entity instanceof MineralLeviathanBody mineralLeviathanBody) {
                    mineralLeviathanBody.setMasterId(this.getId());
                    return mineralLeviathanBody;
                }
                return null;
            }).filter(mineralLeviathanBody -> {
                boolean isNull = mineralLeviathanBody == null;
                if(isNull) {
                    Odyssey.LOGGER.error("Mineral Leviathan failed to spawn body part in loadSubEntities");
                }
                return !isNull;
            }).collect(new NonNullListCollector<>()));
            for(MineralLeviathanBody mineralLeviathanBody: this.bodyParts) {
                this.addBodyId(mineralLeviathanBody.getId());
            }
        }
    }

    // Server Side
    public MineralLeviathanSegment getAdjacentHeadwiseSegment(MineralLeviathanBody mineralLeviathanBody) {
        int index = this.bodyParts.indexOf(mineralLeviathanBody);
        if(index >= 1) {
            return this.bodyParts.get(index - 1);
        }
        return this.head;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, MAX_HEALTH);
    }
}
