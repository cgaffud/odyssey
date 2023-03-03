package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.world.BiomeUtil;
import com.bedmen.odyssey.world.gen.biome.weather.OdysseyPrecipitation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;

import java.util.Random;

public class OdysseyStray extends OdysseyAbstractSkeleton {
    public OdysseyStray(EntityType<? extends OdysseyStray> entityType, Level level) {
        super(entityType, level);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.STRAY_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33850_) {
        return SoundEvents.STRAY_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.STRAY_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.STRAY_STEP;
    }

    public ItemStack getProjectile(ItemStack bow) {
        return new ItemStack(ItemRegistry.FROST_ARROW.get());
    }

    protected void populateBabyEquipmentSlots() {
        this.boomerangItem = ItemRegistry.GLACIERANG.get();
        this.setItemSlot(EquipmentSlot.MAINHAND, this.boomerangItem.getDefaultInstance());
        this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
    }

    public ResourceLocation getDefaultLootTable() {
        if(this.isBaby()){
            return OdysseyLootTables.BABY_SKELETON;
        }
        return super.getDefaultLootTable();
    }

    public static boolean spawnPredicate(EntityType<OdysseyStray> entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, Random random) {
        BlockPos onTopOfPowderSnowBlockPos = blockPos;
        do {
            onTopOfPowderSnowBlockPos = onTopOfPowderSnowBlockPos.above();
        } while(serverLevelAccessor.getBlockState(onTopOfPowderSnowBlockPos).is(Blocks.POWDER_SNOW));
        return checkMonsterSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos, random) && (mobSpawnType == MobSpawnType.SPAWNER || serverLevelAccessor.canSeeSky(onTopOfPowderSnowBlockPos.below()));
    }
}