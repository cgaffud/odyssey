package com.bedmen.odyssey.entity.boss.coven;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.BossMaster;
import com.bedmen.odyssey.entity.boss.SubEntity;
import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.util.NonNullListCollector;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CovenMaster extends BossMaster {
    // END, NETHER, OVR
    private static final EntityDataAccessor<IntList> DATA_WITCH_ID_LIST = SynchedEntityData.defineId(CovenMaster.class, OdysseyDataSerializers.INT_LIST);
    private static final EntityDataAccessor<FloatList> DATA_WITCH_HEALTH_LIST = SynchedEntityData.defineId(CovenMaster.class, OdysseyDataSerializers.FLOAT_LIST);
    public static final int NUM_WITCHES = 3;
    public static final float HEALTH_PER_WITCH = 50.0f;
    public static final double MAX_HEALTH = HEALTH_PER_WITCH * NUM_WITCHES;
    public static final double DAMAGE = 8.0d;
    public static final double FOLLOW_RANGE = 75d;
    public static final double CENTER_RANGE = 40d;
    private static final String WITCHES_TAG = "CovenWitches";
    private static final String WITCH_HEALTH_LIST_TAG = "WitchHealthList";

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

    // Adds both the witch id and health to synched data
    private void addWitchToSychedData(int id) {
        IntList witchIDs = this.entityData.get(DATA_WITCH_ID_LIST);
        witchIDs.add(id);
        this.entityData.set(DATA_WITCH_ID_LIST, witchIDs);
        FloatList witchHealths = this.entityData.get(DATA_WITCH_HEALTH_LIST);
        witchHealths.add(HEALTH_PER_WITCH);
        this.entityData.set(DATA_WITCH_HEALTH_LIST, witchHealths);
    }

    public Optional<CovenWitch> getWitch(int witchIndex) {
        List<CovenWitch> witches = this.getWitches();
        if (witches.size() != NUM_WITCHES)
            return Optional.empty();
        return Optional.of(witches.get(witchIndex));
    }

    public List<CovenWitch> getWitches() {
        if(this.level.isClientSide) {
            List<CovenWitch> witches = new ArrayList<>();
            for(Integer bodyId: this.entityData.get(DATA_WITCH_ID_LIST)) {
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

    public FloatList getWitchHealthList() {
        return this.entityData.get(DATA_WITCH_HEALTH_LIST);
    }

    public float getWitchHealth(CovenWitch covenWitch){
        int id = this.getMyWitchID(covenWitch);
        FloatList healthList = this.getWitchHealthList();
        if(healthList.size() <= id){
            // Just in case something messes up
            return HEALTH_PER_WITCH;
        }
        return healthList.get(id);
    }

    public void setWitchHealth(CovenWitch covenWitch, float newHealth){
        int id = this.getMyWitchID(covenWitch);
        FloatList healthList = this.getWitchHealthList();
        if(healthList.size() > id){
            healthList.set(id, newHealth);
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_WITCH_ID_LIST, new IntArrayList());
        this.entityData.define(DATA_WITCH_HEALTH_LIST, new FloatArrayList());
    }

    public void clientTick() {
        for(Entity entity : this.getSubEntities()){
            if(entity instanceof LivingEntity livingEntity) {
                livingEntity.hurtTime = this.hurtTime;
            }
        }
    }

    @Override
    public void serverTick() {
        super.serverTick();
        float totalHealth = this.getWitchHealthList().stream().reduce(Float::sum).orElse(0.0f);
        this.setHealth(totalHealth);
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

    public boolean isLastAlive(CovenWitch covenWitch) {
        for (int i = 0 ; i < NUM_WITCHES; i++) {
            if (i == getMyWitchID(covenWitch))
                continue;
            Optional<CovenWitch> otherWitchOptional = this.getWitch(i);
            if (otherWitchOptional.isPresent() && !otherWitchOptional.get().isEnraged())
                return false;
        }
        return true;
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
                    case 2:
                        witch = EntityTypeRegistry.OVERWORLD_WITCH.get().create(this.level);
                        break;
                    case 1:
                        witch = EntityTypeRegistry.NETHER_WITCH.get().create(this.level);
                        break;
                    default:
                    case 0:
                        witch = EntityTypeRegistry.ENDER_WITCH.get().create(this.level);
                }
                if (witch != null) {
                    this.witches.add(witch);
                    this.addWitchToSychedData(witch.getId());

                    float phi = Mth.PI * 2/NUM_WITCHES * witchNum;
                    BlockPos.MutableBlockPos blockpos$mutableblockpos = (this.blockPosition().offset(Mth.cos(phi), 60, Mth.sin(phi))).mutable();

                    while(blockpos$mutableblockpos.getY() > this.level.getMinBuildHeight() && !this.level.getBlockState(blockpos$mutableblockpos).getMaterial().blocksMotion()) {
                        blockpos$mutableblockpos.move(Direction.DOWN);
                    }
                    BlockPos blockPos = blockpos$mutableblockpos.above().immutable();

                    witch.moveTo(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
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
            List<Tag> listOfTags = compoundTag.getList(WITCHES_TAG, Tag.TAG_COMPOUND).stream().toList();
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
                this.addWitchToSychedData(covenWitch.getId());
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, MAX_HEALTH);
    }

    public void bringWitchesToMe(){
        if(!this.level.isClientSide) {
            for (int i = 0; i < NUM_WITCHES; i++) {
                CovenWitch witch = this.witches.get(i);
                float xOffset = Mth.cos(Mth.PI*2/NUM_WITCHES * i);
                float zOffset = Mth.sin(Mth.PI*2/NUM_WITCHES * i);
                witch.teleport(this.getX() + xOffset, this.getY(), this.getZ()+zOffset);
                witch.doWhenReturnToMaster();
            }
        }
    }

    public boolean hurtWitch(DamageSource damageSource, float amount, CovenWitch covenWitch){
        // Ignore magic damage. They are witches
        if (covenWitch.isInvulnerableTo(damageSource) || damageSource.isMagic())
            return false;

        float originalWitchHealth = this.getWitchHealth(covenWitch);
        float bossReducedAmount = amount * this.getDamageReduction();

        if (originalWitchHealth > 0.0f) {
            float newWitchHealth = Float.max(originalWitchHealth - bossReducedAmount, 0.0f);
            if(!this.level.isClientSide){
                if (newWitchHealth <= 0.0f) {
                    if(!this.isLastAlive(covenWitch)) {
                        covenWitch.becomeEnraged();
                    } else {
                        //Trigger boss death?
                    }
                }
                this.setWitchHealth(covenWitch, newWitchHealth);
            }
            return true;
        }
        return false;
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        ListTag healthListTag = new ListTag();
        for(float witchHealth: this.getWitchHealthList()){
            healthListTag.add(FloatTag.valueOf(witchHealth));
        }
        compoundTag.put(WITCH_HEALTH_LIST_TAG, healthListTag);
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if(compoundTag.contains(WITCH_HEALTH_LIST_TAG, Tag.TAG_LIST))
        ListTag healthListTag = compoundTag.getList(WITCH_HEALTH_LIST_TAG, Tag.TAG_FLOAT);
        this.loadSubEntities(compoundTag);
    }
}
