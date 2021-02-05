package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.tileentity.*;
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

    public static final RegistryObject<TileEntityType<NewBeaconTileEntity>> BEACON = TILE_ENTITY_TYPES_VANILLA.register("beacon", () -> TileEntityType.Builder.create(NewBeaconTileEntity::new, BlockRegistry.BEACON.get()).build(null));
    public static final RegistryObject<TileEntityType<AlloyFurnaceTileEntity>> ALLOY_FURNACE = TILE_ENTITY_TYPES.register("alloy_furnace", () -> TileEntityType.Builder.create(AlloyFurnaceTileEntity::new, BlockRegistry.ALLOY_FURNACE.get()).build(null));
    public static final RegistryObject<TileEntityType<InfuserTileEntity>> INFUSER = TILE_ENTITY_TYPES.register("infuser", () -> TileEntityType.Builder.create(InfuserTileEntity::new, BlockRegistry.INFUSER.get()).build(null));
    public static final RegistryObject<TileEntityType<NewEnchantingTableTileEntity>> ENCHANTING_TABLE = TILE_ENTITY_TYPES.register("enchanting_table", () -> TileEntityType.Builder.create(NewEnchantingTableTileEntity::new, BlockRegistry.ENCHANTING_TABLE.get()).build(null));
    public static final RegistryObject<TileEntityType<BookshelfTileEntity>> BOOKSHELF = TILE_ENTITY_TYPES.register("bookshelf", () -> TileEntityType.Builder.create(BookshelfTileEntity::new, BlockRegistry.BOOKSHELF.get()).build(null));

}
