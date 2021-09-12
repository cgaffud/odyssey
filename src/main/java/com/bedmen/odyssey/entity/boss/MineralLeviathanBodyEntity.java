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
import net.minecraftforge.fml.common.Mod;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
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
        if (this.level.isClientSide) {
            if (!this.isSilent()) {
                //Player Sounds Here
            }
        }

        if(!this.initBody){
            List<UUID> uuidList = this.getBodyUUIDs();
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

        if(!this.isNoAi()){
            if(!this.level.isClientSide){
                //Movement
                Vector3d prevSegmentPosition = this.prevSegment.getPosition(1.0f);
                Vector3d movement = prevSegmentPosition.subtract(this.getPosition(1.0f));
                if(movement.length() > 1.8d){
                    Vector3d newPosition = movement.normalize().scale(-1.75d).add(prevSegmentPosition);
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

    public boolean hurt(DamageSource damageSource, float amount) {
        if(this.head != null && this.head.isAlive()){
            return this.head.hurt(damageSource, amount);
        } else {
            return super.hurt(damageSource, amount);
        }
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 1.0D).add(Attributes.ATTACK_DAMAGE, MineralLeviathanEntity.DAMAGE * 0.5d);
    }
}
