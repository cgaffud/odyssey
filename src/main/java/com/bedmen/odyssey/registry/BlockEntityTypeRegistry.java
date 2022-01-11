package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.entity.*;
import com.bedmen.odyssey.loot.TreasureChestMaterial;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityTypeRegistry {

    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES , Odyssey.MOD_ID);

    public static void init() {
        BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<BlockEntityType<OdysseySignBlockEntity>> SIGN = BLOCK_ENTITY_TYPES.register("sign",() -> BlockEntityType.Builder.of(OdysseySignBlockEntity::new, BlockRegistry.PALM_SIGN.get(), BlockRegistry.PALM_WALL_SIGN.get()).build(null));
    public static final RegistryObject<BlockEntityType<HollowCoconutBlockEntity>> HOLLOW_COCONUT = BLOCK_ENTITY_TYPES.register("hollow_coconut", () -> BlockEntityType.Builder.of(HollowCoconutBlockEntity::new, BlockRegistry.HOLLOW_COCONUT.get()).build(null));
    public static final RegistryObject<BlockEntityType<RecyclingFurnaceBlockEntity>> RECYCLING_FURNACE = BLOCK_ENTITY_TYPES.register("recycling_furnace", () -> BlockEntityType.Builder.of(RecyclingFurnaceBlockEntity::new, BlockRegistry.RECYCLING_FURNACE.get()).build(null));
    public static final RegistryObject<BlockEntityType<AlloyFurnaceBlockEntity>> ALLOY_FURNACE = BLOCK_ENTITY_TYPES.register("alloy_furnace", () -> BlockEntityType.Builder.of(AlloyFurnaceBlockEntity::new, BlockRegistry.ALLOY_FURNACE.get()).build(null));
    public static final RegistryObject<BlockEntityType<TreasureChestBlockEntity>> STERLING_SILVER_CHEST = BLOCK_ENTITY_TYPES.register("sterling_silver_chest", () -> BlockEntityType.Builder.of((blockPos, blockState) -> new TreasureChestBlockEntity(TreasureChestMaterial.STERLING_SILVER, blockPos, blockState), BlockRegistry.STERLING_SILVER_CHEST.get()).build(null));
}