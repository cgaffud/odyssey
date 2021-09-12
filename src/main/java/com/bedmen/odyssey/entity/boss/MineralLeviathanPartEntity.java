package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.entity.IRotationallyIncompetent;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import com.bedmen.odyssey.network.packet.UpdateEntityRotationPacket;
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
import net.minecraftforge.fml.network.PacketDistributor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class MineralLeviathanPartEntity extends MineralLeviathanSegmentEntity implements IRotationallyIncompetent {
    protected static final DataParameter<List<UUID>> DATA_DEPENDENCIES_UUID_ID = EntityDataManager.defineId(MineralLeviathanPartEntity.class, OdysseyDataSerializers.UUID_LIST);
    public  MineralLeviathanEntity head;
    public MineralLeviathanSegmentEntity prevSegment;

    public MineralLeviathanPartEntity(EntityType<? extends MineralLeviathanPartEntity> entityType, World world) {
        this(entityType, world, null, null);
    }

    public MineralLeviathanPartEntity(EntityType<? extends MineralLeviathanPartEntity> entityType, World world, MineralLeviathanEntity head, MineralLeviathanSegmentEntity prevSegment) {
        super(entityType, world);
        this.head = head;
        this.prevSegment = prevSegment;
        if(head != null){
            List<UUID> uuidList = new ArrayList<>();
            uuidList.add(this.head.getUUID());
            uuidList.add(this.prevSegment.getUUID());
            this.setPartsUUIDs(uuidList);
            this.initParts = true;
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_DEPENDENCIES_UUID_ID, new ArrayList<>());
    }

    public void setPartsUUIDs(List<UUID> uuidList) {
        this.entityData.set(DATA_DEPENDENCIES_UUID_ID, uuidList);
    }

    public List<UUID> getPartsUUIDs() {
        return this.entityData.get(DATA_DEPENDENCIES_UUID_ID);
    }

    public void aiStep() {
        if (this.level.isClientSide) {
            if (!this.isSilent()) {
                //Player Sounds Here
            }
        }

        if(!this.initParts){
            List<UUID> uuidList = this.getPartsUUIDs();
            List<MineralLeviathanSegmentEntity> partList = this.level.getEntitiesOfClass(MineralLeviathanSegmentEntity.class, this.getBoundingBox().inflate(45.0d));
            for(MineralLeviathanSegmentEntity part : partList){
                if(part.getUUID().equals(uuidList.get(0))){
                    if(part instanceof MineralLeviathanEntity){
                        this.head = (MineralLeviathanEntity) part;
                    }
                }
                if (part.getUUID().equals(uuidList.get(1))){
                    this.prevSegment = part;
                }
                if(this.head != null && this.prevSegment != null){
                    break;
                }
            }
            this.initParts = true;
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
                OdysseyNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new UpdateEntityRotationPacket(this.trueYRot, this.trueXRot, this.getId()));
            }
        }
        super.aiStep();
    }

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        List<UUID> uuidList = this.getPartsUUIDs();
        for(int i = 0; i < uuidList.size(); i++){
            compoundNBT.putUUID("PartUUID"+i, uuidList.get(i));
        }
    }

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        List<UUID> uuidList = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            if (compoundNBT.hasUUID("PartUUID"+i)) {
                uuidList.add(compoundNBT.getUUID("PartUUID"+i));
            }
        }
        this.setPartsUUIDs(uuidList);
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
