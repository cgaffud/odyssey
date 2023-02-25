package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.structure.pieces.*;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class StructurePieceTypeRegistry {

    public static DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registry.STRUCTURE_PIECE_REGISTRY, Odyssey.MOD_ID);

    public static void init() {
        STRUCTURE_PIECE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


    public static final RegistryObject<StructurePieceType> BASIC_RUINS = STRUCTURE_PIECE_TYPES.register("basic_ruins", () -> (StructurePieceType.StructureTemplateType)(BasicRuinsPiece::new));
    public static final RegistryObject<StructurePieceType> WEAVER_COLONY = STRUCTURE_PIECE_TYPES.register("weaver_colony", () -> (StructurePieceType.ContextlessType)(WeaverColonySpherePiece::new));
    public static final RegistryObject<StructurePieceType> UNDERGROUND_RUIN = STRUCTURE_PIECE_TYPES.register("underground_ruin", () -> (StructurePieceType.StructureTemplateType)(UndergroundRuinPieces.UndergroundRuinPiece::new));
    public static final RegistryObject<StructurePieceType> MOON_TOWER = STRUCTURE_PIECE_TYPES.register("moon_tower", ()->(StructurePieceType.StructureTemplateType)(MoonTowerPiece::new));
    public static final RegistryObject<StructurePieceType> COVEN_HUT = STRUCTURE_PIECE_TYPES.register("coven_hut", () -> (StructurePieceType.StructureTemplateType)(CovenHutPiece::new));

    // Barn
    public static final RegistryObject<StructurePieceType> BARN = STRUCTURE_PIECE_TYPES.register("barn", () -> (StructurePieceType.StructureTemplateType)(BarnPiece::new));
    public static final RegistryObject<StructurePieceType> BARN_SPAWNER = STRUCTURE_PIECE_TYPES.register("barn_spawner", () -> (StructurePieceType.StructureTemplateType)(BarnSpawnerPiece::new));
}