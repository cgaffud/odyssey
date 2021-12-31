package com.bedmen.odyssey.loot;

import com.bedmen.odyssey.block.entity.TreasureChestBlockEntity;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.util.OdysseyStats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public enum TreasureChestMaterial {
    STERLING_SILVER(BlockEntityTypeRegistry.STERLING_SILVER_CHEST::get, BlockRegistry.STERLING_SILVER_CHEST::get, OdysseyStats.OPEN_STERLING_SILVER_CHEST);

    private final Supplier<BlockEntityType<? extends TreasureChestBlockEntity>> blockEntityTypeSupplier;
    private final Supplier<Block> blockSupplier;
    private final ResourceLocation stat;

    TreasureChestMaterial(Supplier<BlockEntityType<? extends TreasureChestBlockEntity>> blockEntityTypeSupplier, Supplier<Block> blockSupplier, ResourceLocation stat){
        this.blockEntityTypeSupplier = blockEntityTypeSupplier;
        this.blockSupplier = blockSupplier;
        this.stat = stat;
    }

    public BlockEntityType<? extends TreasureChestBlockEntity> getBlockEntityType(){
        return this.blockEntityTypeSupplier.get();
    }

    public BlockState getBlockState(){
        return this.blockSupplier.get().defaultBlockState();
    }

    public ResourceLocation getStat(){
        return this.stat;
    }
}
