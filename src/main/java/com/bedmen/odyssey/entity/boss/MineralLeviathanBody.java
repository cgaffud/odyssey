package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MineralLeviathanBody extends MineralLeviathanSegment {
    protected static final EntityDataAccessor<List<Integer>> DATA_DEPENDENCIES_ID = SynchedEntityData.defineId(MineralLeviathanBody.class, OdysseyDataSerializers.INT_LIST);
    public MineralLeviathanHead head;
    public MineralLeviathanSegment prevSegment;
    public UUID[] dependencyUUIDs = new UUID[2];

    public MineralLeviathanBody(EntityType<? extends MineralLeviathanBody> entityType, Level level) {
        this(entityType, level, null, null);
    }

    public MineralLeviathanBody(EntityType<? extends MineralLeviathanBody> entityType, Level level, MineralLeviathanHead head, MineralLeviathanSegment prevSegment) {
        super(entityType, level);
        this.head = head;
        this.prevSegment = prevSegment;
        if(head != null){
            this.dependencyUUIDs[0] = this.head.getUUID();
            this.dependencyUUIDs[1] = this.prevSegment.getUUID();
            this.setBodyIDs(this.head.getId(), this.prevSegment.getId());
            this.initBody = true;
        }
        if(!this.level.isClientSide){
            ShellType shellType = ShellType.getRandomShellType(this.random);
            this.setShellType(shellType);
        } else {
            this.setShellType(ShellType.COAL);
        }
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
            ServerLevel serverWorld = (ServerLevel) this.level;
            this.head = (MineralLeviathanHead) serverWorld.getEntity(this.dependencyUUIDs[0]);
            this.prevSegment = (MineralLeviathanSegment) serverWorld.getEntity(this.dependencyUUIDs[1]);
            this.initBody = true;
        }
        //Client side Init Body
        else if(!this.initBody){
            List<Integer> idList = this.getBodyIDs();
            if(idList.size() >= 2){
                this.head = (MineralLeviathanHead) this.level.getEntity(idList.get(0));
                this.prevSegment = (MineralLeviathanSegment) this.level.getEntity(idList.get(1));
                this.initBody = true;
            }
        }

        if(!this.isNoAi()){
            if(!this.level.isClientSide && this.prevSegment != null){
                //Movement
                Vec3 prevSegmentPosition = this.prevSegment.getPosition(1.0f);
                Vec3 movement = prevSegmentPosition.subtract(this.getPosition(1.0f));
                if(movement.length() > 2.0d){
                    Vec3 newPosition = movement.normalize().scale(-1.95d).add(prevSegmentPosition);
                    this.setPos(newPosition.x, newPosition.y, newPosition.z);
                }
                this.setRotation(movement);
            }
        }
        super.aiStep();
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        for(int i = 0; i < this.dependencyUUIDs.length; i++){
            compoundNBT.putUUID("BodyUUID"+i, this.dependencyUUIDs[i]);
        }
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
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

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 1.0D).add(Attributes.ATTACK_DAMAGE, MineralLeviathanHead.DAMAGE * 0.5d);
    }

    public ServerBossEvent getBossEvent(){
        if(this.head != null){
            return this.head.getBossEvent();
        }
        return null;
    }

    public void checkDespawn() {
        if (this.head != null && this.head.isRemoved()) {
            this.discard();
        }
    }

    public boolean canChangeBossEvent(){
        return false;
    }
}
