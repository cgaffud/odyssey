package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.tileentity.AlloyFurnaceTileEntity;
import com.bedmen.odyssey.tileentity.NewBeaconTileEntity;
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

}
