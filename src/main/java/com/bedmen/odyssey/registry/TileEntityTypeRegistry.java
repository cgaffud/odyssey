package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.tileentity.*;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypeRegistry {

    public static DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES_VANILLA = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES , "minecraft");
    public static DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES , Odyssey.MOD_ID);

    public static void init() {
        TILE_ENTITY_TYPES_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<TileEntityType<OdysseyBeaconTileEntity>> BEACON = TILE_ENTITY_TYPES_VANILLA.register("beacon", () -> TileEntityType.Builder.of(OdysseyBeaconTileEntity::new, BlockRegistry.BEACON.get()).build(null));
    public static final RegistryObject<TileEntityType<AlloyFurnaceTileEntity>> ALLOY_FURNACE = TILE_ENTITY_TYPES.register("alloy_furnace", () -> TileEntityType.Builder.of(AlloyFurnaceTileEntity::new, BlockRegistry.ALLOY_FURNACE.get()).build(null));
    public static final RegistryObject<TileEntityType<OdysseyEnchantingTableTileEntity>> ENCHANTING_TABLE = TILE_ENTITY_TYPES.register("enchanting_table", () -> TileEntityType.Builder.of(OdysseyEnchantingTableTileEntity::new, BlockRegistry.ENCHANTING_TABLE.get()).build(null));
    public static final RegistryObject<TileEntityType<BookshelfTileEntity>> BOOKSHELF = TILE_ENTITY_TYPES.register("bookshelf", () -> TileEntityType.Builder.of(BookshelfTileEntity::new, BlockRegistry.BOOKSHELF.get()).build(null));
    public static final RegistryObject<TileEntityType<RecycleFurnaceTileEntity>> RECYCLE_FURNACE = TILE_ENTITY_TYPES.register("recycle_furnace", () -> TileEntityType.Builder.of(RecycleFurnaceTileEntity::new, BlockRegistry.RECYCLE_FURNACE.get()).build(null));
    public static final RegistryObject<TileEntityType<ResearchTableTileEntity>> RESEARCH_TABLE = TILE_ENTITY_TYPES.register("research_table", () -> TileEntityType.Builder.of(ResearchTableTileEntity::new, BlockRegistry.RESEARCH_TABLE.get()).build(null));
    public static final RegistryObject<TileEntityType<SterlingSilverChestTileEntity>> STERLING_SILVER_CHEST = TILE_ENTITY_TYPES.register("sterling_silver_chest", () -> TileEntityType.Builder.of(SterlingSilverChestTileEntity::new, BlockRegistry.STERLING_SILVER_CHEST.get()).build(null));
    public static final RegistryObject<TileEntityType<HollowCoconutTileEntity>> HOLLOW_COCONUT = TILE_ENTITY_TYPES.register("hollow_coconut", () -> TileEntityType.Builder.of(HollowCoconutTileEntity::new, BlockRegistry.HOLLOW_COCONUT.get()).build(null));
    public static final RegistryObject<TileEntityType<OdysseySignTileEntity>> SIGN = TILE_ENTITY_TYPES.register("sign", () -> TileEntityType.Builder.of(OdysseySignTileEntity::new, BlockRegistry.PALM_SIGN.get(), BlockRegistry.PALM_WALL_SIGN.get()).build(null));
}
