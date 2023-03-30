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
    // If you want to summon not on top of the flame
    private double dx;
    private double dy;
    private double dz;

    public BurnToSummonItem(Properties properties, Supplier<EntityType> entityTypeSupplier, double dx, double dy, double dz) {
        super(properties);
        this.entityTypeSupplier = entityTypeSupplier;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public BurnToSummonItem(Properties properties, Supplier<EntityType> entityTypeSupplier) {
        this(properties, entityTypeSupplier, 0, 0, 0);
    }

    protected void doOnBurn(ItemEntity itemEntity) {
        BlockPos pos = itemEntity.blockPosition();
        entityTypeSupplier.get().spawn((ServerLevel) itemEntity.level, null, null, pos.offset(this.dx, this.dy, this.dz), MobSpawnType.TRIGGERED, true, true);
    }

    public void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {
        if (BURN_SOURCES.contains(itemEntity.getFeetBlockState().getBlock()) && !itemEntity.level.isClientSide) {
            doOnBurn(itemEntity);
        }
    }
}

