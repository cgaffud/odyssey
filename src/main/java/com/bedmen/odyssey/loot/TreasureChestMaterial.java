package com.bedmen.odyssey.loot;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.entity.TreasureChestBlockEntity;
import com.bedmen.odyssey.client.renderer.blockentity.TreasureChestRenderer;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.util.OdysseyStats;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Locale;
import java.util.function.Supplier;

public enum TreasureChestMaterial {
    COPPER(BlockRegistry.COPPER_CHEST::get),
    STERLING_SILVER(BlockRegistry.STERLING_SILVER_CHEST::get);

    private final Supplier<Block> blockSupplier;
    public final ResourceLocation stat;
    private final Material renderMaterial;
    private final Material renderMaterialLocked;

    TreasureChestMaterial(Supplier<Block> blockSupplier){
        this.blockSupplier = blockSupplier;
        String name = this.name().toLowerCase(Locale.ROOT);
        this.stat = OdysseyStats.makeCustomStat(String.format("open_%s_chest", name), StatFormatter.DEFAULT);;
        this.renderMaterial = new Material(Sheets.CHEST_SHEET, new ResourceLocation(Odyssey.MOD_ID, String.format("entity/treasure_chests/%s", name)));
        this.renderMaterialLocked = new Material(Sheets.CHEST_SHEET, new ResourceLocation(Odyssey.MOD_ID, String.format("entity/treasure_chests/%s_locked", name)));;
    }

    public BlockState getBlockState(){
        return this.blockSupplier.get().defaultBlockState();
    }

    public Material getRenderMaterial(boolean locked){
        return locked ? this.renderMaterialLocked : this.renderMaterial;
    }
}
