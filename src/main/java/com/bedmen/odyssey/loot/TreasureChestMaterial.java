package com.bedmen.odyssey.loot;

import com.bedmen.odyssey.block.entity.TreasureChestBlockEntity;
import com.bedmen.odyssey.client.renderer.blockentity.TreasureChestRenderer;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.util.OdysseyStats;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public enum TreasureChestMaterial {
    STERLING_SILVER(
            BlockEntityTypeRegistry.STERLING_SILVER_CHEST::get,
            BlockRegistry.STERLING_SILVER_CHEST::get,
            OdysseyStats.OPEN_STERLING_SILVER_CHEST,
            TreasureChestRenderer.STERLING_SILVER_MATERIAL,
            TreasureChestRenderer.STERLING_SILVER_LOCKED_MATERIAL);

    private final Supplier<BlockEntityType<? extends TreasureChestBlockEntity>> blockEntityTypeSupplier;
    private final Supplier<Block> blockSupplier;
    private final ResourceLocation stat;
    private final Material renderMaterial;
    private final Material renderMaterialLocked;

    TreasureChestMaterial(Supplier<BlockEntityType<? extends TreasureChestBlockEntity>> blockEntityTypeSupplier, Supplier<Block> blockSupplier, ResourceLocation stat, Material renderMaterial, Material renderMaterialLocked){
        this.blockEntityTypeSupplier = blockEntityTypeSupplier;
        this.blockSupplier = blockSupplier;
        this.stat = stat;
        this.renderMaterial = renderMaterial;
        this.renderMaterialLocked = renderMaterialLocked;
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

    public Material getRenderMaterial(boolean locked){
        return locked ? this.renderMaterialLocked : this.renderMaterial;
    }
}
