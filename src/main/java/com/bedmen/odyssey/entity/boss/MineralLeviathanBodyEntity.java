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
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MineralLeviathanBodyEntity extends MineralLeviathanSegmentEntity {
    protected static final DataParameter<List<Integer>> DATA_DEPENDENCIES_ID = EntityDataManager.defineId(MineralLeviathanBodyEntity.class, OdysseyDataSerializers.INT_LIST);
    public  MineralLeviathanEntity head;
    public MineralLeviathanSegmentEntity prevSegment;
    public UUID[] dependencyUUIDs = new UUID[2];

    public MineralLeviathanBodyEntity(EntityType<? extends MineralLeviathanBodyEntity> entityType, World world) {
        this(entityType, world, null, null);
    }

    public MineralLeviathanBodyEntity(EntityType<? extends MineralLeviathanBodyEntity> entityType, World world, MineralLeviathanEntity head, MineralLeviathanSegmentEntity prevSegment) {
        super(entityType, world);
        this.head = head;
        this.prevSegment = prevSegment;
        if(head != null){
            List<UUID> uuidList = new ArrayList<>();
            this.dependencyUUIDs[0] = this.head.getUUID();
            this.dependencyUUIDs[1] = this.prevSegment.getUUID();
            this.setBodyIDs(this.head.getId(), this.prevSegment.getId());
            this.initBody = true;
        }
        ShellType shellType = ShellType.getRandomShellType(this.random);
        this.setShellType(shellType);
        this.setShellHealth(shellType.getShellMaxHealth());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_DEPENDENCIES_ID, new ArrayList<>());
    }

    public void setBodyIDs(int headID, int prevSegmentID) {
        List<Integer> idList = new ArrayList<>();
        idList.add(headID);
        idList.add(prevSegmentID);
        this.entityData.set(DATA_DEPENDENCIES_ID, idList);
    }

    public List<Integer> getBodyIDs() {
        return this.entityData.get(DATA_DEPENDENCIES_ID);
    }

    public void aiStep() {
        //Server side Init Body
        if(!this.initBody && !this.level.isClientSide && this.dependencyUUIDs[0] != null){
            ServerWorld serverWorld = (ServerWorld) this.level;
            this.head = (MineralLeviathanEntity) serverWorld.getEntity(this.dependencyUUIDs[0]);
            this.prevSegment = (MineralLeviathanSegmentEntity) serverWorld.getEntity(this.dependencyUUIDs[1]);
            this.initBody = true;
        }
        //Client side Init Body
        else if(!this.initBody){
            List<Integer> idList = this.getBodyIDs();
            if(idList.size() >= 2){
                this.head = (MineralLeviathanEntity) this.level.getEntity(idList.get(0));
                this.prevSegment = (MineralLeviathanSegmentEntity) this.level.getEntity(idList.get(1));
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
        for(int i = 0; i < this.dependencyUUIDs.length; i++){
            compoundNBT.putUUID("BodyUUID"+i, this.dependencyUUIDs[i]);
        }
    }

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        for(int i = 0; i < 2; i++){
            if (compoundNBT.hasUUID("BodyUUID"+i)) {
                this.dependencyUUIDs[i] = compoundNBT.getUUID("BodyUUID"+i);
            }
        }
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

    public ServerBossInfo getBossEvent(){
        if(this.head != null){
            return this.head.getBossEvent();
        }
        return null;
    }
}
