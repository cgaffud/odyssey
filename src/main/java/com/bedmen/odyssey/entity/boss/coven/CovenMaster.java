package com.bedmen.odyssey.entity.boss.coven;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.BossMaster;
import com.bedmen.odyssey.entity.boss.SubEntity;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanBody;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanSegment;
import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.util.NonNullListCollector;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class CovenMaster extends BossMaster {
    // END, NETHER, OVR
    private static final EntityDataAccessor<IntList> WITCH_IDS_DATA = SynchedEntityData.defineId(CovenMaster.class, OdysseyDataSerializers.INT_LIST);
    private static final int NUM_WITCHES = 3;
    public static final double MAX_HEALTH = 150.0d;
    public static final double DAMAGE = 8.0d;
    public static final double FOLLOW_RANGE = 75d;
    public static final double CENTER_RANGE = 45d;
    private static final String WITCHES_TAG = "CovenWitches";

    // These sub entities may only be referenced directly in a server side method
    // Otherwise use the 'get' method
    public final List<CovenWitch> witches = new ArrayList<>();

    public CovenMaster(EntityType<? extends CovenMaster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 100;
    }


//    public int getWitchId(int witchNum) {
//        return this.entityData.get(WITCH_IDS_DATA).getInt(witchNum);
//    }
//
//    public void setWitchId(int witchNum, int id) {
//        IntList witchIDs = this.entityData.get(WITCH_IDS_DATA);
//        if (witchIDs.size() != NUM_WITCHES)
//            throw new IllegalStateException("Cannot set Witch IDs prior to instantiation");
//        witchIDs.set(witchNum, id);
//        this.entityData.set(WITCH_IDS_DATA, witchIDs);
//    }

    public IntList getBodyIds() {
    return this.entityData.get(WITCH_IDS_DATA);
}

    private void addWitchId(int id) {
        IntList witchIDs = this.entityData.get(WITCH_IDS_DATA);
        witchIDs.add(id);
        this.entityData.set(WITCH_IDS_DATA, witchIDs);
    }

    public Optional<CovenWitch> getWitch(int witchNum) {
        List<CovenWitch> witches = this.getWitches();
        if (witches.size() != NUM_WITCHES)
            return Optional.empty();
        return Optional.of(witches.get(witchNum));
    }

    public List<CovenWitch> getWitches() {
        if(this.level.isClientSide) {
            List<CovenWitch> witches = new ArrayList<>();
            for(Integer bodyId: this.entityData.get(WITCH_IDS_DATA)) {
                Entity entity = this.level.getEntity(bodyId);
                // instanceof also checks if it is null
                if(entity instanceof CovenWitch covenWitch) {
                    witches.add(covenWitch);
                }
            }
            return witches;
        } else {
            return this.witches;
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WITCH_IDS_DATA, new IntArrayList());
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
        entities.addAll(this.getWitches());
        return entities;
    }

    public int getMyWitchID(CovenWitch covenWitch) {
        return this.witches.indexOf(covenWitch);
    }

    // TODO: I'm not convinced this is like the fastest way of doing this.
    public List<LivingEntity> getOtherTargets(CovenWitch covenWitch) {
        List<LivingEntity> targets = new ArrayList<>();
        for (int i = 0 ; i < NUM_WITCHES; i++) {
            if (i == getMyWitchID(covenWitch))
                continue;
            targets.add(this.witches.get(i).getTarget());
        }
        return targets;
    }

    public void performMasterMovement() {
        Vec3 center = new Vec3(0,0,0);
        int count = 0;

        for(Entity entity : getSubEntities()) {
            center = center.add(entity.position());
            count += 1;
        }

        this.moveTo(center.scale(1.0f / count));
    }

    protected BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.RED;
    }

    public void spawnSubEntities() {
        if(this.getWitches().isEmpty()) {
            initializeWitches();
        }
        for(Entity entity: getSubEntities()) {
            this.level.addFreshEntity(entity);
        }
    }

    // Server Side
    public void initializeWitches() {
        for (int witchNum = 0; witchNum < NUM_WITCHES; witchNum++) {
            if (witchNum >= this.getWitches().size()) {
                CovenWitch witch;
                switch (witchNum) {
                    default:
                    case 0:
                        witch = EntityTypeRegistry.ENDER_WITCH.get().create(this.level);
                }
                if (witch != null) {
                    this.witches.add(witch);
                    this.addWitchId(witch.getId());
                    witch.moveTo(this.position());
                    witch.setMasterId(this.getId());
                } else {
                    Odyssey.LOGGER.error("Witch #" + witchNum + " failed to spawn in initializeWitches");
                    break;
                }
            }
        }
    }


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

    // Server Side
    public void saveSubEntities(CompoundTag compoundTag) {
        ListTag witchesTag = new ListTag();
        for(CovenWitch covenWitch : this.witches) {
            CompoundTag witchCompoundTag = new CompoundTag();
            if (covenWitch.saveAsPassenger(witchCompoundTag)) {
                witchesTag.add(witchCompoundTag);
            }
        }
        compoundTag.put(WITCHES_TAG, witchesTag);
    }

    // Server Side
    public void loadSubEntities(CompoundTag compoundTag) {
        if(compoundTag.contains(WITCHES_TAG)) {
            List<Tag> listOfTags = compoundTag.getList(WITCHES_TAG, NUM_WITCHES).stream().toList();
            Stream<Entity> entitySteam = EntityType.loadEntitiesRecursive(listOfTags, this.level);
            this.witches.addAll(entitySteam.map(entity -> {
                if(entity instanceof CovenWitch covenWitch) {
                    covenWitch.setMasterId(this.getId());
                    return covenWitch;
                }
                return null;
            }).filter(covenWitch -> {
                boolean isNull = covenWitch == null;
                if(isNull) {
                    Odyssey.LOGGER.error("Coven Witch failed to spawn body part in loadSubEntities");
                }
                return !isNull;
            }).collect(new NonNullListCollector<>()));
            for(CovenWitch covenWitch: this.witches) {
                this.addWitchId(covenWitch.getId());
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, MAX_HEALTH).add(Attributes.ATTACK_DAMAGE, DAMAGE).add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE);
    }
}
