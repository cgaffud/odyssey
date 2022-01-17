package com.bedmen.odyssey.loot;

import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.util.OdysseyStats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Locale;
import java.util.function.Supplier;

public enum TreasureChestMaterial {
    COPPER(BlockRegistry.COPPER_CHEST::get),
    STERLING_SILVER(BlockRegistry.STERLING_SILVER_CHEST::get);

    private final Supplier<Block> blockSupplier;
    public final ResourceLocation stat;

    TreasureChestMaterial(Supplier<Block> blockSupplier){
        this.blockSupplier = blockSupplier;
        String name = this.name().toLowerCase(Locale.ROOT);
        this.stat = OdysseyStats.makeCustomStat(String.format("open_%s_chest", name), StatFormatter.DEFAULT);;
    }

    public BlockState getBlockState(){
        return this.blockSupplier.get().defaultBlockState();
    }
}
