package com.bedmen.odyssey.entity.boss.coven;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.BossMaster;
import com.bedmen.odyssey.entity.boss.SubEntity;
import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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

import java.util.*;
import java.util.stream.Collectors;

public class CovenMaster extends BossMaster {
    private static final EntityDataAccessor<Map<CovenType, Integer>> DATA_WITCH_ID_MAP = SynchedEntityData.defineId(CovenMaster.class, OdysseyDataSerializers.COVENTYPE_INT_MAP);
    public static final int NUM_WITCHES = CovenType.values().length;
    public static final float ENRAGED_WITCH_HEALTH = 1.0f;
    public static final float HEALTH_PER_WITCH = 50.0f;
    public static final double MAX_HEALTH = HEALTH_PER_WITCH * NUM_WITCHES;
    public static final double DAMAGE = 8.0d;
    public static final double FOLLOW_RANGE = 75d;
    public static final double CENTER_RANGE = 40d;
    private static final String WITCH_TAG = "CovenWitch";

    // These sub entities may only be referenced directly in a server side method
    // Otherwise use the 'get' method
    public final Map<CovenType, CovenWitch> witchMap = new HashMap<>();

    public CovenMaster(EntityType<? extends CovenMaster> entityType, Level level) {
        super(entityType, level);
    }

    // Must use this method to access coven synched data because it makes a copy and protects the synched data object from outside changes
    private Map<CovenType, Integer> getTypeToIDMap(){
        return new HashMap<>(this.entityData.get(DATA_WITCH_ID_MAP));
    }

    // Adds the witch id to synched data
    private void addIdToMap(CovenType covenType, int id) {
        Map<CovenType, Integer> idMap = this.getTypeToIDMap();
        idMap.put(covenType, id);
        this.entityData.set(DATA_WITCH_ID_MAP, idMap);
    }

    private Map<CovenType, CovenWitch> getTypeToWitchMap() {
        if(this.level.isClientSide) {
            Map<CovenType, CovenWitch> witches = new HashMap<>();
            for(Map.Entry<CovenType, Integer> entry: this.getTypeToIDMap().entrySet()) {
                Entity entity = this.level.getEntity(entry.getValue());
                // instanceof also checks if it is null
                if(entity instanceof CovenWitch covenWitch) {
                    witches.put(entry.getKey(), covenWitch);
                }
            }
            return witches;
        } else {
            return this.witchMap;
        }
    }

    private Collection<CovenWitch> getWitchCollection(){
        return this.getTypeToWitchMap().values();
    }

    private Optional<CovenWitch> getWitchFromType(CovenType covenType) {
       CovenWitch covenWitch = this.getTypeToWitchMap().get(covenType);
       return covenWitch == null ? Optional.empty() : Optional.of(covenWitch);
    }

    private float getHealthFromType(CovenType covenType){
        Optional<CovenWitch> optionalCovenWitch = this.getWitchFromType(covenType);
        return optionalCovenWitch.map(this::getWitchHealth).orElse(HEALTH_PER_WITCH);
    }

    private float getWitchHealth(CovenWitch covenWitch){
        return Float.max(0.0f, covenWitch.getHealth() - ENRAGED_WITCH_HEALTH);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_WITCH_ID_MAP, new HashMap<>());
    }

    private void updateMasterHealth(){
        if(this.getHealth() > 0.0f) {
            float totalHealth = this.getWitchCollection().stream()
                    .reduce(0.0f, (health, covenWitch) -> health + covenWitch.getHealth(), Float::sum);
            this.setHealth(totalHealth);
        }
    }

    public Collection<Entity> getSubEntities() {
        return new HashSet<>(this.getTypeToWitchMap().values());
    }

    // Server-side only
    public Set<LivingEntity> getOtherTargets(CovenWitch covenWitch) {
        Set<LivingEntity> targets = this.getWitchCollection().stream().map(Mob::getTarget).collect(Collectors.toSet());
        targets.remove(covenWitch.getTarget());
        return targets;
    }

    public boolean isLastAlive(CovenWitch covenWitch) {
        for(CovenType covenType: CovenType.values()){
            if(covenWitch.getCovenType() == covenType){
                continue;
            }
            Optional<CovenWitch> optionalCovenWitch = this.getWitchFromType(covenType);
            if(optionalCovenWitch.isPresent() && optionalCovenWitch.get().isAlive() && this.getHealthFromType(covenType) > 0.0f){
                return false;
            }
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

    //Server side
    public void spawnSubEntities() {
        if(this.witchMap.isEmpty()) {
            initializeWitches();
        }
        for(Entity entity: getSubEntities()) {
            this.level.addFreshEntity(entity);
        }
    }

    // Server Side
    public void initializeWitches() {
        for(CovenType covenType: CovenType.values()){
            CovenWitch witch;
            switch (covenType) {
                case OVERWORLD:
                    witch = EntityTypeRegistry.OVERWORLD_WITCH.get().create(this.level);
                    break;
                case NETHER:
                    witch = EntityTypeRegistry.NETHER_WITCH.get().create(this.level);
                    break;
                default:
                    witch = EntityTypeRegistry.ENDER_WITCH.get().create(this.level);
            }
            if (witch != null) {
                this.witchMap.put(covenType, witch);
                this.addIdToMap(covenType, witch.getId());

                float phi = Mth.PI * 2/NUM_WITCHES * covenType.ordinal();
                BlockPos.MutableBlockPos blockpos$mutableblockpos = (this.blockPosition().offset(Mth.cos(phi), 60, Mth.sin(phi))).mutable();

                while(blockpos$mutableblockpos.getY() > this.level.getMinBuildHeight() && !this.level.getBlockState(blockpos$mutableblockpos).getMaterial().blocksMotion()) {
                    blockpos$mutableblockpos.move(Direction.DOWN);
                }
                BlockPos blockPos = blockpos$mutableblockpos.above().immutable();

                witch.moveTo(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                witch.setMasterId(this.getId());
            } else {
                Odyssey.LOGGER.error("Witch type " + covenType.name() + " failed to spawn in initializeWitches");
                break;
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

    private static String getCovenTypeTagString(CovenType covenType){
        return WITCH_TAG + covenType.name();
    }

    // Server Side
    public CompoundTag saveSubEntities() {
        CompoundTag subEntitiesTag = new CompoundTag();
        for(CovenType covenType: CovenType.values()){
            CovenWitch covenWitch = this.witchMap.get(covenType);
            CompoundTag witchCompoundTag = new CompoundTag();
            if (covenWitch != null && covenWitch.saveAsPassenger(witchCompoundTag)) {
                subEntitiesTag.put(getCovenTypeTagString(covenType), witchCompoundTag);
            }
        }
        return subEntitiesTag;
    }

    // Server Side
    public void loadSubEntities(CompoundTag subEntitiesTag) {
        for(CovenType covenType: CovenType.values()){
            String witchTagString = getCovenTypeTagString(covenType);
            if(subEntitiesTag.contains(witchTagString)) {
                CompoundTag witchCompoundTag = subEntitiesTag.getCompound(witchTagString);
                Entity entity = EntityType.loadEntityRecursive(witchCompoundTag, this.level, entity1 -> entity1);
                if(entity instanceof CovenWitch covenWitch) {
                    covenWitch.setMasterId(this.getId());
                    this.witchMap.put(covenType, covenWitch);
                    this.addIdToMap(covenType, covenWitch.getId());
                } else {
                    Odyssey.LOGGER.error("Witch type " + covenType.name() + " failed to spawn in loadSubEntities");
                    break;
                }
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, MAX_HEALTH).add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE);
    }

    // Server Side
    public void bringWitchesToMe(){
        if(!this.level.isClientSide) {
            for(CovenType covenType: CovenType.values()){
                int i = covenType.ordinal();
                CovenWitch witch = this.witchMap.get(covenType);
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
                        this.die(damageSource);
                    }
                }
                covenWitch.hurtDirectly(damageSource, originalWitchHealth - newWitchHealth);
                this.updateMasterHealth();
            } else {
                covenWitch.animateHurt();
            }
            return true;
        }
        return false;
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
    }
}
