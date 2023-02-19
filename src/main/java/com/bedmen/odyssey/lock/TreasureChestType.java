package com.bedmen.odyssey.lock;

import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.util.OdysseyStats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Locale;
import java.util.function.Supplier;

public enum TreasureChestType implements LockType {
    COPPER(BlockRegistry.COPPER_CHEST::get, OdysseyLootTables.COPPER_TREASURE_CHEST),
    STERLING_SILVER(BlockRegistry.STERLING_SILVER_CHEST::get, OdysseyLootTables.STERLING_SILVER_TREASURE_CHEST);

    private final Supplier<Block> blockSupplier;
    public final ResourceLocation lootTable;
    public final ResourceLocation stat;

    TreasureChestType(Supplier<Block> blockSupplier, ResourceLocation lootTable){
        this.blockSupplier = blockSupplier;
        this.lootTable = lootTable;
        String name = this.name().toLowerCase(Locale.ROOT);
        this.stat = OdysseyStats.makeCustomStat(String.format("open_%s_chest", name), StatFormatter.DEFAULT);;
    }

    public BlockState getBlockState(){
        return this.blockSupplier.get().defaultBlockState();
    }
}
