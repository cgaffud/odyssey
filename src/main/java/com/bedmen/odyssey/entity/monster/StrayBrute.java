package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class StrayBrute extends OdysseyStray {
    public StrayBrute(EntityType<? extends OdysseyStray> entityType, Level level) {
        super(entityType, level);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        super.populateDefaultEquipmentSlots(difficultyInstance);
        if(this.random.nextBoolean()){
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.FROST_MACE.get()));
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.FROST_SPEAR.get()));
        }
    }

    public boolean isBaby() {
        return false;
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        spawnGroupData = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
        if(this.isBaby()){
            this.setBaby(false);
        }
        return spawnGroupData;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.MAX_HEALTH, 60d);
    }
}
