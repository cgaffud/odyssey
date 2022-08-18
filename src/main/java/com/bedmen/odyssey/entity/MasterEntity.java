//package com.bedmen.odyssey.entity;
//
//import com.bedmen.odyssey.util.NonNullListCollector;
//import net.minecraft.core.NonNullList;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.nbt.Tag;
//import net.minecraft.util.Tuple;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
///**
// * Used for coalescing  groups of entities that should save/load together
// */
//public interface MasterEntity {
//    String SUB_ENTITIES_TAG = "SubEntities";
//
//    NonNullList<SubEntity> getSubEntities();
//
//    void setSubEntities(NonNullList<SubEntity> subEntities);
//
//    void markSubEntitiesAsLoaded();
//
//    Set<Tuple<SubEntity, CompoundTag>> getChunkUnloadedSubEntities();
//
//    void handleReplacementSubEntity(SubEntity subEntity);
//
//    void updateSubEntityPointer(SubEntity oldSubEntity, SubEntity newSubEntity);
//
//    default Entity asEntity(){
//        return (Entity)this;
//    }
//
//    default void markAsChunkUnloaded(SubEntity subEntity) {
//        CompoundTag compoundTag = new CompoundTag();
//        subEntity.asEntity().save(compoundTag);
//        this.getChunkUnloadedSubEntities().add(new Tuple<>(subEntity, compoundTag));
//    }
//
//    // Needs to be called from Entity's server side tick method
//    default void masterServerTick(){
//        for(Tuple<SubEntity, CompoundTag> tuple: this.getChunkUnloadedSubEntities()) {
//            SubEntity subEntity = tuple.getA();
//            CompoundTag compoundTag = tuple.getB();
//            Entity entity = EntityType.loadEntityRecursive(compoundTag, this.asEntity().level, entity1 -> entity1);
//            if(entity != null) {
//                SubEntity newSubEntity = (SubEntity)entity;
//                newSubEntity.setMasterEntity(this);
//                entity.level.addFreshEntity(entity);
//                this.handleReplacementSubEntity(newSubEntity);
//                this.updateSubEntityPointer(subEntity, newSubEntity);
//            }
//        }
//    }
//
//    default void masterClientTick(){
//        List<Entity> entityList = this.getSubEntityIds().stream().map(id -> this.asEntity().level.getEntity(id)).collect(Collectors.toList());
//        this.setSubEntities();
//    }
//
//    // Needs to be called from Entity's addAdditionalSaveData method
//    default void addSubEntitySaveData(CompoundTag compoundTag) {
//        ListTag listTag = new ListTag();
//        for(SubEntity subEntity : this.getSubEntities()) {
//            CompoundTag compoundtag = new CompoundTag();
//            if (subEntity.asEntity().saveAsPassenger(compoundtag)) {
//                listTag.add(compoundtag);
//            }
//        }
//        compoundTag.put(SUB_ENTITIES_TAG, listTag);
//    }
//
//    // Needs to be called from Entity's readAdditionalSaveData method
//    default void loadSubEntitySaveData(CompoundTag compoundTag) {
//        if(compoundTag.contains(SUB_ENTITIES_TAG)) {
//            ListTag listTag = compoundTag.getList(SUB_ENTITIES_TAG, 10);
//            List<Tag> listOfTags = listTag.stream().toList();
//            Stream<Entity> entitySteam = EntityType.loadEntitiesRecursive(listOfTags, this.asEntity().level);
//            NonNullList<SubEntity> subEntityList = entitySteam.map(entity -> {
//                this.asEntity().level.addFreshEntity(entity);
//                return (SubEntity)entity;
//            }).collect(new NonNullListCollector<>());
//            this.setSubEntities(subEntityList);
//            this.markSubEntitiesAsLoaded();
//        }
//    }
//
//    // Needs to be called from Entity's remove method
//    default void removeSubEntities() {
//        for(SubEntity subEntity : this.getSubEntities()) {
//            subEntity.asEntity().remove(Entity.RemovalReason.DISCARDED);
//        }
//    }
//
//    List<Integer> getSubEntityIds();
//
//    void setSubEntityIds();
//
//    // Copy and paste
//    /*
//        public void addAdditionalSaveData(CompoundTag compoundTag) {
//        super.addAdditionalSaveData(compoundTag);
//        this.addSubEntitySaveData(compoundTag);
//    }
//
//    public void readAdditionalSaveData(CompoundTag compoundTag) {
//        super.readAdditionalSaveData(compoundTag);
//        this.loadSubEntitySaveData(compoundTag);
//    }
//
//    public void remove(RemovalReason removalReason) {
//        this.removeSubEntities();
//        super.remove(removalReason);
//    }
//
//    protected static final EntityDataAccessor<List<Integer>> DATA_SUB_ENTITY_IDS = SynchedEntityData.defineId(CLASS_NAME.class, OdysseyDataSerializers.INT_LIST);
//    protected void defineSynchedData() {
//        super.defineSynchedData();
//        this.entityData.define(DATA_SUB_ENTITY_IDS, new ArrayList<>());
//    }
//     */
//}
