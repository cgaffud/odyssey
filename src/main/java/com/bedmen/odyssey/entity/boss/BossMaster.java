package com.bedmen.odyssey.entity.boss;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BossMaster extends BossEntity {
    private static final String PUPPETS_TAG = "Puppets";
    private boolean loadedPuppets = false;
    public BossMaster(EntityType<? extends BossEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected abstract NonNullList<PuppetEntity> getPuppetList();

    protected abstract void setPuppetList(NonNullList<PuppetEntity> puppetList);

    protected abstract void generateInitialPuppets();

    public abstract void removePuppet(PuppetEntity puppet);

    public abstract void handlePuppet(PuppetEntity puppet);

    public void serverTick() {
        super.serverTick();

        if(!this.loadedPuppets) {
            this.generateInitialPuppets();
            this.loadedPuppets = true;
        }

        NonNullList<PuppetEntity> puppetList = getPuppetList();
        for (PuppetEntity puppet : puppetList) {
            if (puppet.needsRemovalOrHandling()) {
                if (puppet.shouldBeRemoved()) {
                    this.removePuppet(puppet);
                } else {
                    this.handlePuppet(puppet);
                }
            }
        }
    }

    public void remove(Entity.RemovalReason removalReason) {
        for(PuppetEntity puppet : getPuppetList()) {
            puppet.asEntity().remove(removalReason);
        }
        super.remove(removalReason);
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if(compoundTag.contains(PUPPETS_TAG)) {
            ListTag listTag = compoundTag.getList(PUPPETS_TAG, 10);
            List<Tag> listOfTags = listTag.stream().toList();
            System.out.println(listOfTags.size());
            Stream<Entity> entitySteam = EntityType.loadEntitiesRecursive(listOfTags, this.level);
            List<PuppetEntity> puppetList = entitySteam.map(entity -> {
                this.level.addFreshEntity(entity);
                return (PuppetEntity)entity;
            }).collect(Collectors.toList());
            NonNullList<PuppetEntity> nonNullPuppetList = NonNullList.create();
            nonNullPuppetList.addAll(puppetList);
            this.setPuppetList(nonNullPuppetList);
            System.out.println("beans");
            System.out.println(this.getPuppetList());
            this.loadedPuppets = true;
        }
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        ListTag listTag = new ListTag();
        for(PuppetEntity puppet : this.getPuppetList()) {
            CompoundTag compoundtag = new CompoundTag();
            if (puppet.asEntity().saveAsPassenger(compoundtag)) {
                listTag.add(compoundtag);
            }
        }
        compoundTag.put(PUPPETS_TAG, listTag);
    }
}
