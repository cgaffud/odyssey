package com.bedmen.odyssey.items;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class BurnToSummonItem extends Item {

    private final List<Block> BURN_SOURCES = Arrays.asList(Blocks.FIRE, Blocks.SOUL_FIRE, Blocks.LAVA);
    private Supplier<EntityType> entityTypeSupplier;

    public BurnToSummonItem(Properties properties, Supplier<EntityType> entityTypeSupplier) {
        super(properties);
        this.entityTypeSupplier = entityTypeSupplier;
    }

    public void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {
        if (BURN_SOURCES.contains(itemEntity.getFeetBlockState().getBlock()) && !itemEntity.level.isClientSide){
            BlockPos pos = itemEntity.blockPosition();
            entityTypeSupplier.get().spawn((ServerLevel) itemEntity.level, null, null, pos.offset(1,0,0), MobSpawnType.TRIGGERED, true, true);
        }
    }
}

