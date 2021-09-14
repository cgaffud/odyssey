package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MineralLeviathanBodyEntity extends MineralLeviathanSegmentEntity {
    protected static final DataParameter<List<UUID>> DATA_DEPENDENCIES_UUID_ID = EntityDataManager.defineId(MineralLeviathanBodyEntity.class, OdysseyDataSerializers.UUID_LIST);
    public  MineralLeviathanEntity head;
    public MineralLeviathanSegmentEntity prevSegment;

    public MineralLeviathanBodyEntity(EntityType<? extends MineralLeviathanBodyEntity> entityType, World world) {
        this(entityType, world, null, null);
    }

    public MineralLeviathanBodyEntity(EntityType<? extends MineralLeviathanBodyEntity> entityType, World world, MineralLeviathanEntity head, MineralLeviathanSegmentEntity prevSegment) {
        super(entityType, world);
        this.head = head;
        this.prevSegment = prevSegment;
        if(head != null){
            List<UUID> uuidList = new ArrayList<>();
            uuidList.add(this.head.getUUID());
            uuidList.add(this.prevSegment.getUUID());
            this.setBodyUUIDs(uuidList);
            this.initBody = true;
        }
        int shellType = this.random.nextInt(2)+1;
        this.setShellType(shellType);
        this.setShellHealth(this.getShellHealthFromType(shellType));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_DEPENDENCIES_UUID_ID, new ArrayList<>());
    }

    public void setBodyUUIDs(List<UUID> uuidList) {
        this.entityData.set(DATA_DEPENDENCIES_UUID_ID, uuidList);
    }

    public List<UUID> getBodyUUIDs() {
        return this.entityData.get(DATA_DEPENDENCIES_UUID_ID);
    }

    public void aiStep() {
        if(!this.initBody && !this.level.isClientSide){
            List<UUID> uuidList = this.getBodyUUIDs();
            if(uuidList.size() >= 2){
                try {
                    Field entitiesByUuidField = ServerWorld.class.getDeclaredField("entitiesByUuid");
                    entitiesByUuidField.setAccessible(true);
                    Map<UUID, Entity> entitiesByUuid = (Map<UUID, Entity>) entitiesByUuidField.get(this.level);
                    this.head = (MineralLeviathanEntity) entitiesByUuid.get(uuidList.get(0));
                    this.prevSegment = (MineralLeviathanSegmentEntity) entitiesByUuid.get(uuidList.get(1));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                this.initBody = true;
            }
        } else if(!this.initBody){
            List<UUID> uuidList = this.getBodyUUIDs();
            if(!this.initBody && uuidList.size() >= 2){
                List<MineralLeviathanSegmentEntity> segmentEntities = this.level.getEntitiesOfClass(MineralLeviathanSegmentEntity.class, this.getBoundingBox().inflate(45.0d));
                for(MineralLeviathanSegmentEntity segmentEntity : segmentEntities){
                    if(segmentEntity.getUUID().equals(uuidList.get(0))){
                        if(segmentEntity instanceof MineralLeviathanEntity){
                            this.head = (MineralLeviathanEntity) segmentEntity;
                        }
                    }
                    if (segmentEntity.getUUID().equals(uuidList.get(1))){
                        this.prevSegment = segmentEntity;
                    }
                    if(this.head != null && this.prevSegment != null){
                        break;
                    }
                }
                this.initBody = true;
            }
        }

        if(!this.isNoAi()){
            if(!this.level.isClientSide && this.prevSegment != null){
                //Movement
                Vector3d prevSegmentPosition = this.prevSegment.getPosition(1.0f);
                Vector3d movement = prevSegmentPosition.subtract(this.getPosition(1.0f));
                if(movement.length() > 2.0d){
                    Vector3d newPosition = movement.normalize().scale(-1.95d).add(prevSegmentPosition);
                    this.setPos(newPosition.x, newPosition.y, newPosition.z);
                }
                this.setRotation(movement);
            }
        }
        super.aiStep();
    }

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        List<UUID> uuidList = this.getBodyUUIDs();
        for(int i = 0; i < uuidList.size(); i++){
            compoundNBT.putUUID("BodyUUID"+i, uuidList.get(i));
        }
    }

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        List<UUID> uuidList = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            if (compoundNBT.hasUUID("BodyUUID"+i)) {
                uuidList.add(compoundNBT.getUUID("BodyUUID"+i));
            }
        }
        this.setBodyUUIDs(uuidList);
    }

    protected boolean hurtWithoutShell(DamageSource damageSource, float amount){
        if(this.head != null && this.head.isAlive()){
            return this.head.hurtWithoutShell(damageSource, amount);
        } else {
            return super.hurtWithoutShell(damageSource, amount);
        }
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 1.0D).add(Attributes.ATTACK_DAMAGE, MineralLeviathanEntity.DAMAGE * 0.5d);
    }
}
