package com.bedmen.odyssey.registry.structure;

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
    public static final RegistryObject<StructurePieceType> CAVE_RUINS = STRUCTURE_PIECE_TYPES.register("cave_ruins", () -> (StructurePieceType.StructureTemplateType)(CaveRuinsPiece::new));
    public static final RegistryObject<StructurePieceType> LONE_WAR_WATCH_TOWER = STRUCTURE_PIECE_TYPES.register("lone_war_watch_tower", () -> (StructurePieceType.StructureTemplateType)(LoneWarWatchTowerPiece::new));
    public static final RegistryObject<StructurePieceType> CAVE_BLACKSMITH = STRUCTURE_PIECE_TYPES.register("cave_blacksmith", () -> (StructurePieceType.StructureTemplateType)(CaveBlacksmithPiece::new));
    public static final RegistryObject<StructurePieceType> WEAVER_COLONY = STRUCTURE_PIECE_TYPES.register("weaver_colony", () -> (StructurePieceType.ContextlessType)(WeaverColonySpherePiece::new));
    public static final RegistryObject<StructurePieceType> MOON_TOWER = STRUCTURE_PIECE_TYPES.register("moon_tower", ()->(StructurePieceType.StructureTemplateType)(MoonTowerPiece::new));
    public static final RegistryObject<StructurePieceType> DUNGEONLESS_MOON_TOWER = STRUCTURE_PIECE_TYPES.register("dungeonless_moon_tower", ()->(StructurePieceType.StructureTemplateType)(DungeonlessMoonTowerPiece::new));
    public static final RegistryObject<StructurePieceType> COVEN_HUT = STRUCTURE_PIECE_TYPES.register("coven_hut", () -> (StructurePieceType.StructureTemplateType)(CovenHutPiece::new));

    // Barn
    public static final RegistryObject<StructurePieceType> BARN = STRUCTURE_PIECE_TYPES.register("barn", () -> (StructurePieceType.StructureTemplateType)(BarnPiece::new));
    public static final RegistryObject<StructurePieceType> BARN_SPAWNER = STRUCTURE_PIECE_TYPES.register("barn_spawner", () -> (StructurePieceType.StructureTemplateType)(BarnSpawnerPiece::new));

    // Jigsaw
    public static final RegistryObject<StructurePieceType> VANILLA_JIGSAW = STRUCTURE_PIECE_TYPES.register("vanilla_jigsaw", () -> (VanillaPoolElementStructurePiece::new));
    public static final RegistryObject<StructurePieceType> BANDIT_HIDEOUT_JIGSAW = STRUCTURE_PIECE_TYPES.register("bandit_hideout_jigsaw", () -> (BanditHideoutPiece::new));
}